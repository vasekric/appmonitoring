package edu.vsb.dais.appmonitoring.database.repositories;

import edu.vsb.dais.appmonitoring.database.mappers.ConfigMapper;
import edu.vsb.dais.appmonitoring.database.mappers.StatusRulesMapper;
import edu.vsb.dais.appmonitoring.service.models.SniffConfig;
import edu.vsb.dais.appmonitoring.service.models.StatusRules;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vasekric on 27. 4. 2015.
 */
@Repository
public class ConfigurationRepository {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private ConfigMapper configMapper;
    @Autowired private StatusRulesMapper statusRulesMapper;
    private SimpleJdbcCall addConfigCall;
    private SimpleJdbcCall observerChangeCall;
    private SimpleJdbcCall renewalCall;

    @PostConstruct
    private void init() {
        addConfigCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("AddConfiguration")
                .declareParameters(
                        new SqlOutParameter("RESULTS", OracleTypes.INTEGER, new ColumnMapRowMapper()),
                        new SqlParameter("p_threshold_error", Types.INTEGER),
                        new SqlParameter("p_threshold_warning", Types.INTEGER),
                        new SqlParameter("p_quantity", Types.INTEGER),
                        new SqlParameter("p_frequency", Types.INTEGER),
                        new SqlParameter("p_enabled", Types.CHAR),
                        new SqlParameter("p_subscribe_to", OracleTypes.VARCHAR),
                        new SqlParameter("p_auto_subscription", Types.CHAR),
                        new SqlParameter("p_entity_id", Types.INTEGER),
                        new SqlParameter("p_observer_id", Types.INTEGER)
                )
                .withoutProcedureColumnMetaDataAccess();

        observerChangeCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("observer_change");
        observerChangeCall.addDeclaredParameter(new SqlParameter("p_observer_id", Types.INTEGER));
        observerChangeCall.addDeclaredParameter(new SqlParameter("p_sniff_config_id", Types.INTEGER));

        renewalCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("renewal");
        renewalCall.addDeclaredParameter(new SqlParameter("p_date", Types.DATE));
        renewalCall.addDeclaredParameter(new SqlParameter("p_sniff_config_id", Types.INTEGER));
    }

    /**
     * Function 6.1
     * @param sniffConfig
     * @return
     */
    public SniffConfig save(SniffConfig sniffConfig) {

        final Integer eId = sniffConfig.getEntity().getId();
        final Integer oId = sniffConfig.getObserver().getId();
        final int frequency = sniffConfig.getFrequency();
        final Date subscibtionTo = sniffConfig.getSubscibtionTo();
        final boolean autoSubscribe = sniffConfig.isAutoSubscribe();
        final boolean enabled = sniffConfig.isEnabled();
        final StatusRules statusRules = sniffConfig.getStatusRules();
        final int quantity = statusRules.getQuantity();
        final int thresholdWarning = statusRules.getThresholdWarning();
        final int thresholdError = statusRules.getThresholdError();
        String enabledChar = "0";
        if(enabled) {
            enabledChar = "1";
        }
        String autoChar = "0";
        if(autoSubscribe) {
            autoChar = "1";
        }

        final LocalDateTime dateTime = LocalDateTime.ofInstant(subscibtionTo.toInstant(), ZoneId.systemDefault());
        final String format = dateTime.format(DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("p_threshold_error", thresholdError);
        map.put("p_threshold_warning", thresholdWarning);
        map.put("p_quantity", quantity);
        map.put("p_frequency", frequency);
        map.put("p_enabled", enabledChar);
        map.put("p_subscribe_to", format);
        map.put("p_auto_subscription", autoChar);
        map.put("p_entity_id", eId);
        map.put("p_observer_id", oId);
        SqlParameterSource params = new MapSqlParameterSource(map);

        Map<String, Object> results = addConfigCall.execute(params);

        sniffConfig.setId((Integer) results.get("RESULTS"));
        return sniffConfig;
    }

    /**
     * Function 6.2
     * @param date
     * @param sniffConfigId
     */
    public void subscriptionRenewal(java.sql.Date date, int sniffConfigId) {
        renewalCall.execute(date, sniffConfigId);
    }

    public void delete(int id) {
        final String sql = "DELETE FROM sniff_config WHERE sniff_config_id=?";

        jdbcTemplate.update(sql, id);
    }

    /**
     * Function 6.4
     * @param newObserverId
     * @param sniffConfigId
     */
    public void observerChange(int newObserverId, int sniffConfigId) {
        observerChangeCall.execute(newObserverId, sniffConfigId);
    }

    /**
     * Function 6.5
     * @param sniffConfigId
     * @return
     */
    public SniffConfig findOne(int sniffConfigId) {

        final String sql = "SELECT * FROM sniff_config sc JOIN status_rules sr on sr.status_rules_id = sc.status_rules_id WHERE sniff_config_id=?";

        final SniffConfig sniffConfig = jdbcTemplate.queryForObject(sql, new Object[]{sniffConfigId}, configMapper);

        return sniffConfig;
    }

    /**
     * Function 6.6
     * @param entityId
     * @return
     */
    public List<SniffConfig> findAllByEntityId(int entityId) {

        final String sql = "SELECT * FROM sniff_config sc JOIN status_rules sr on sr.status_rules_id=sc.status_rules_id WHERE entity_id=?";

        final List<SniffConfig> sniffConfigs = jdbcTemplate.query(sql, new Object[]{entityId}, configMapper);

        return sniffConfigs;
    }

    /**
     * Function 6.7
     * @param entityId
     * @param observerId
     * @return
     */
    public SniffConfig findOneByEntityIdAndObserverId(int entityId, int observerId) {

        final String sql = "SELECT * FROM sniff_config sc JOIN status_rules sr on sr.status_rules_id=sc.status_rules_id WHERE entity_id=? AND observer_id=?";

        final SniffConfig sniffConfig = jdbcTemplate.queryForObject(sql, new Object[]{entityId, observerId}, configMapper);

        return sniffConfig;
    }

    /**
     * Function 6.8
     * @param sniffConfig
     */
    public void update(SniffConfig sniffConfig) {
        final Integer scId = sniffConfig.getId();
        final int frequency = sniffConfig.getFrequency();
        final boolean enabled = sniffConfig.isEnabled();
        final boolean autoSubscribe = sniffConfig.isAutoSubscribe();
        final StatusRules statusRules = sniffConfig.getStatusRules();
        final Integer srId = statusRules.getId();
        final int quantity = statusRules.getQuantity();
        final int thresholdWarning = statusRules.getThresholdWarning();
        final int thresholdError = statusRules.getThresholdError();
        String enabledChar = "0";
        if(enabled) {
            enabledChar = "1";
        }
        String autoChar = "0";
        if(autoSubscribe) {
            autoChar = "1";
        }

        final String sniffConfigSql = "UPDATE sniff_config SET FREQUENCY=?, ENABLED=?, AUTO_SUBSCRIBE=? WHERE sniff_config_id=?";
        final String statusRulesSql = "UPDATE status_rules SET THRESHOLD_WARNING=?, THRESHOLD_ERROR=?, QUANTITY=? WHERE status_rules_id=?";

        jdbcTemplate.update(sniffConfigSql, new Object[]{frequency, enabledChar, autoChar, scId});
        jdbcTemplate.update(statusRulesSql, new Object[]{thresholdWarning, thresholdError, quantity, srId});
    }

    /**
     * Function 6.9
     * @param userId
     * @return
     */
    public List<SniffConfig> findAllByUserId(int userId) {

        final String sql = "SELECT sc.*, sr.* FROM sniff_config sc JOIN status_rules sr ON sr.status_rules_id=sc.status_rules_id JOIN entity e ON e.entity_id=sc.entity_id WHERE users_id=?";

        final List<SniffConfig> sniffConfigs = jdbcTemplate.query(sql, new Object[]{userId}, configMapper);

        return sniffConfigs;
    }

    public List<SniffConfig> findAll() {

        final String sql = "SELECT * FROM sniff_config sc JOIN status_rules sr ON sr.status_rules_id=sc.status_rules_id";

        final List<SniffConfig> sniffConfigs = jdbcTemplate.query(sql, configMapper);

        return sniffConfigs;
    }




}
