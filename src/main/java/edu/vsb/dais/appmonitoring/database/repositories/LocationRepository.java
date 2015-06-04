package edu.vsb.dais.appmonitoring.database.repositories;

import edu.vsb.dais.appmonitoring.database.mappers.LocationMapper;
import edu.vsb.dais.appmonitoring.service.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.nativejdbc.SimpleNativeJdbcExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created by vasekric on 25. 4. 2015.
 */

@Repository
public class LocationRepository {

    @Autowired private JdbcTemplate template;
    private SimpleJdbcInsert jdbcInsert;
    @Autowired private LocationMapper mapper;

    @PostConstruct
    public void init() {
        jdbcInsert = new SimpleJdbcInsert(template).withTableName("location").usingGeneratedKeyColumns("location_id");
    }

    /**
     * Function 4.1
     * @param location
     * @return
     */
    public Location save(Location location) {

        final String city = location.getCity();
        final String country = location.getCountry();
        final SqlParameterSource sql = new MapSqlParameterSource()
                .addValue("country", country)
                .addValue("city", city);
        final Number newId = jdbcInsert.executeAndReturnKey(sql);

        location.setId(newId.intValue());
        return location;
    }

    /**
     * Function 4.2
     * @param location
     */
    public void update(Location location) {

        final String sql = "UPDATE location SET country= :country, city= :city WHERE location_id= :id";
        final String city = location.getCity();
        final Integer id = location.getId();
        final String country = location.getCountry();

        template.update(sql, new Object[]{country, city, id});
    }

    /**
     * Function 4.3
     * @param id
     */
    public void delete(int id) {

        final String sql = "DELETE FROM location WHERE location_id=?";

        template.update(sql, new Object[]{id});

    }

    /**
     * Function 4.4
     * @return
     */
    public List<Location> findAll() {

        final String sql = "SELECT * FROM location";

        final List<Location> locations = template.query(sql, mapper);

        return locations;
    }

}
