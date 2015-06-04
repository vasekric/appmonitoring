package edu.vsb.dais.appmonitoring.database.repositories;

import edu.vsb.dais.appmonitoring.database.mappers.ObserverMapper;
import edu.vsb.dais.appmonitoring.service.models.Entity;
import edu.vsb.dais.appmonitoring.service.models.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.JDBCType;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by vasekric on 25. 4. 2015.
 */
@Repository
public class ObserverRepository {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private ObserverMapper mapper;
    private SimpleJdbcInsert jdbcInsert;

    @PostConstruct
    public void init() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("observer")
                .usingGeneratedKeyColumns("observer_id");
    }

    /**
     * Function 3.1 Vlozeni
     * @param observer
     * @return
     */
    public Observer save(Observer observer) {

        final String name = observer.getName();
        final java.sql.Date changed = new java.sql.Date(observer.getChanged().getTime());
        final Integer entityCapacityLeft = observer.getEntityCapacityLeft();
        final Integer mtId = observer.getMonitoringType().getId();
        final Integer lId = observer.getLocation().getId();

        final SqlParameterSource sql = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("changed", changed)
                .addValue("entity_capacity_left", entityCapacityLeft)
                .addValue("monitoring_type_id", mtId)
                .addValue("location_id", lId);
        final Number newId = jdbcInsert.executeAndReturnKey(sql);

        observer.setId(newId.intValue());
        return observer;
    }

    /**
     * Fucntion 3.2 Aktualizace
     * @param observer
     * @return
     */
    public void update(Observer observer) {

        final String sql = "UPDATE observer SET name=?, changed=?, entity_capacity_left=?, monitoring_type_id=?, location_id=? WHERE observer_id=?";
        final String name = observer.getName();
        final java.sql.Date changed = new java.sql.Date(observer.getChanged().getTime());
        final Integer entityCapacityLeft = observer.getEntityCapacityLeft();
        final Integer mtId = observer.getMonitoringType().getId();
        final Integer lId = observer.getLocation().getId();
        final Integer id = observer.getId();

        jdbcTemplate.update(sql, new Object[]{name, changed, entityCapacityLeft, mtId, lId, id});
    }

    /**
     * Function 3.3 Zruseni
     * @param id
     */
    public void delete(int id) {

        final String sql = "DELETE FROM observer WHERE observer_id=?";

        jdbcTemplate.update(sql, new Object[]{id});
    }

    /**
     * Function 3.4 Vypis uplneho seznamu
     * @return
     */
    public List<Observer> findAll() {

        final String sql = "SELECT * FROM observer";

        final List<Observer> observers = jdbcTemplate.query(sql, mapper);

        return observers;
    }

    /**
     * Function 3.5 Výpis observerů s volným místem pro měření pro daný monitorovací typ
     * @param monitoringTypeId
     * @return
     */
    public List<Observer> findAllFree(int monitoringTypeId) {

        final String sql = "SELECT * FROM observer WHERE monitoring_type_id=? and entity_capacity_left > 0";

        final List<Observer> observers = jdbcTemplate.query(sql, new Object[]{monitoringTypeId}, mapper);

        return observers;
    }

    /**
     * Function 5.5 Seznam observerů s entitou
     * @param entityId
     * @return
     */
    public List<Observer> findAllByEntity(int entityId) {

        final String sql = "SELECT o.* FROM observer o JOIN sniff_config sc ON sc.observer_id=o.observer_id WHERE sc.entity_id=?";

        final List<Observer> observers = jdbcTemplate.query(sql, new Object[]{entityId}, mapper);

        return observers;
    }
}
