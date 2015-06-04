package edu.vsb.dais.appmonitoring.database.repositories;

import edu.vsb.dais.appmonitoring.database.mappers.MonitoringTypeMapper;
import edu.vsb.dais.appmonitoring.database.mappers.UserMapper;
import edu.vsb.dais.appmonitoring.service.models.MonitoringType;
import edu.vsb.dais.appmonitoring.service.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by vasekric on 25. 4. 2015.
 */
@Repository
public class MonitoringTypeRepository {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private MonitoringTypeMapper mapper;
    private SimpleJdbcInsert jdbcInsert;

    @PostConstruct
    public void init() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("monitoring_type")
                .usingGeneratedKeyColumns("monitoring_type_id");
    }

    /**
     * Function 2.1 Vložení
     * @param monitoringType
     * @return
     */
    public MonitoringType save(MonitoringType monitoringType) {

        final String name = monitoringType.getName();
        final int dayPrice = monitoringType.getDayPrice();
        final SqlParameterSource sql = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("day_price", dayPrice);

        final Number newId = jdbcInsert.executeAndReturnKey(sql);

        monitoringType.setId(newId.intValue());
        return monitoringType;
    }

    /**
     * Funkce 2.2 Aktualizace
     * @param monitoringType
     * @return
     */
    public void update(MonitoringType monitoringType) {

        final String sql = "UPDATE monitoring_type SET name=?, day_price=? WHERE monitoring_type_id=?";
        final Integer id = monitoringType.getId();
        final String name = monitoringType.getName();
        final int dayPrice = monitoringType.getDayPrice();

        jdbcTemplate.update(sql, new Object[]{name, dayPrice, id});
    }

    /**
     * Function 2.3 Zruseni
     * @param id
     */
    public void delete(int id) {
        final String sql = "DELETE FROM monitoring_type WHERE monitoring_type_id=?";

        jdbcTemplate.update(sql, new Object[]{id});
    }

    /**
     * Function 2.4 Vypis uplneho seznamu
     * @return
     */
    public List<MonitoringType> findAll() {

        final String sql = "SELECT * FROM monitoring_type";

        final List<MonitoringType> monitoringTypes = jdbcTemplate.query(sql, mapper);

        return monitoringTypes;
    }
}
