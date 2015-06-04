CREATE OR REPLACE FUNCTION AddConfiguration(
  p_threshold_error IN INTEGER,
  p_threshold_warning IN INTEGER,
  p_quantity IN INTEGER,
  p_frequency IN INTEGER,
  p_enabled IN CHAR,
  p_subscribe_to IN VARCHAR2,
  p_auto_subscription IN CHAR,
  p_entity_id IN INTEGER,
  p_observer_id IN INTEGER
) RETURN INTEGER
AS
  v_capacity INTEGER;
  v_sr_id INTEGER;
  v_inserted_id INTEGER;
  v_sub TIMESTAMP;
BEGIN
    SELECT entity_capacity_left INTO v_capacity FROM observer WHERE observer_id=p_observer_id;
    IF v_capacity > 0 THEN
      v_capacity := v_capacity-1;
      UPDATE observer SET entity_capacity_left=v_capacity WHERE observer_id=p_observer_id;
      INSERT INTO status_rules VALUES (null, p_threshold_warning, p_threshold_error, p_quantity) RETURNING status_rules_id INTO v_sr_id;
      v_sub := TO_TIMESTAMP(p_subscribe_to, 'YYYY-MM-DD HH:MI:SS');
      INSERT INTO sniff_config VALUES (null, p_frequency, p_enabled, v_sub, p_auto_subscription, p_entity_id, p_observer_id, v_sr_id) RETURNING sniff_config_id INTO v_inserted_id;
      COMMIT;
      RETURN v_inserted_id;
    END IF;

    EXCEPTION
      WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(SQLERRM);
        ROLLBACK;
        RETURN -1;
END;

CREATE OR REPLACE PROCEDURE observer_change(
  p_observer_id IN INTEGER,
  p_sniff_config_id IN INTEGER
)
AS
  v_capacity INTEGER;
  v_old_observer_id INTEGER;
  v_entity_id INTEGER;
BEGIN
    SELECT entity_capacity_left INTO v_capacity FROM observer WHERE observer_id=p_observer_id;
    IF v_capacity > 0 THEN
      v_capacity := v_capacity-1;
      UPDATE observer SET entity_capacity_left=v_capacity WHERE observer_id=p_observer_id;
      SELECT observer_id, entity_id INTO v_old_observer_id, v_entity_id FROM sniff_config WHERE sniff_config_id=p_sniff_config_id;

      SELECT entity_capacity_left INTO v_capacity FROM observer WHERE observer_id=v_old_observer_id;
      v_capacity := v_capacity+1;
      UPDATE observer SET entity_capacity_left=v_capacity WHERE observer_id=v_old_observer_id;
      UPDATE sniff_config SET observer_id=p_observer_id WHERE sniff_config_id=p_sniff_config_id;
      UPDATE snapshot SET observer_id=p_observer_id WHERE entity_id=v_entity_id AND observer_id=v_old_observer_id;
      COMMIT;
    END IF;

    EXCEPTION
    WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE(SQLERRM);
    ROLLBACK;
END;

CREATE OR REPLACE FUNCTION actualStatus(
  p_entity_id IN INTEGER,
  p_observer_id IN INTEGER
)RETURN INTEGER
AS
  v_status_rules_id INTEGER;
  v_threshold_warning INTEGER;
  v_threshold_error INTEGER;
  v_quantity INTEGER;
  v_response_time INTEGER;
  v_orig_status INTEGER;
  v_status INTEGER;
  v_counter INTEGER;
  CURSOR c_snapshots IS SELECT response_time, orig_status_id FROM snapshot WHERE entity_id=p_entity_id AND observer_id=p_observer_id ORDER BY timestamp DESC;
  BEGIN
    SELECT status_rules_id INTO v_status_rules_id FROM sniff_config WHERE entity_id=p_entity_id AND observer_id=p_observer_id;
    SELECT threshold_warning, threshold_error, quantity INTO v_threshold_warning, v_threshold_error, v_quantity FROM status_rules WHERE status_rules_id=v_status_rules_id;

    v_counter := 0;
    v_status :=1;
    FOR snapshot IN c_snapshots LOOP
      v_counter := v_counter+1;
      IF v_counter > v_quantity THEN
        RETURN v_status;
      END IF;

      v_response_time := snapshot.response_time;
      v_orig_status := snapshot.orig_status_id;
      IF v_orig_status >= 3 THEN
        v_status := 3;
      ELSIF v_orig_status = 2 THEN
        v_status := 2;
      ELSIF v_status > 1 AND v_orig_status = 1 THEN
        v_status := v_status-1;
      END IF;
      IF v_response_time > v_threshold_error THEN
        IF v_status < 3 THEN
          v_status := v_status+1;
        ELSIF v_response_time > v_threshold_warning THEN
          IF v_status = 1 THEN
            v_status := 2;
          END IF;
        END IF;
      END IF;
    END LOOP;

    RETURN v_status;
  END;

CREATE OR REPLACE PROCEDURE renewal(
  p_date DATE,
  p_sniff_config_id INTEGER
)
AS
  v_sub_to DATE;
  v_day_price INTEGER;
  v_day_diff INTEGER;
  v_price INTEGER;
  v_bundle INTEGER;
  v_user_id INTEGER;
  BEGIN
    SELECT subscription_to, day_price INTO v_sub_to, v_day_price FROM sniff_config sc JOIN observer o ON o.observer_id=sc.observer_id JOIN monitoring_type mt ON mt.monitoring_type_id=o.monitoring_type_id WHERE sniff_config_id=p_sniff_config_id;
    v_day_diff := p_date - v_sub_to;
    v_price := v_day_diff * v_day_price;
    SELECT bundle, u.users_id INTO v_bundle, v_user_id FROM users u JOIN entity e ON e.users_id=u.users_id JOIN sniff_config sc ON sc.entity_id=e.entity_id WHERE sniff_config_id=p_sniff_config_id;
    IF v_bundle >= v_price THEN
      UPDATE users SET bundle=bundle-v_price WHERE users_id=v_user_id;
      UPDATE sniff_config SET subscription_to=p_date;
    END IF;
    COMMIT;

    EXCEPTION
    WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE(SQLERRM);
    ROLLBACK;
  END;
