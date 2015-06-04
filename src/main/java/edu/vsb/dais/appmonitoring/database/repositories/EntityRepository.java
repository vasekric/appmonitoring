package edu.vsb.dais.appmonitoring.database.repositories;

import edu.vsb.dais.appmonitoring.database.mappers.EntityMapper;
import edu.vsb.dais.appmonitoring.service.models.Entity;
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
public class EntityRepository {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityMapper mapper;
    private SimpleJdbcInsert jdbcInsert;

    @PostConstruct
    public void init() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("entity")
                .usingGeneratedKeyColumns("entity_id");
    }

    /**
     * Function 5.1
     * @param entity
     * @return
     */
    public Entity save(Entity entity) {

        final String name = entity.getName();
        final String params = entity.getParams();
        final String uri = entity.getUri();
        final Integer mtId = entity.getMonitoringType().getId();
        final Integer uId = entity.getUser().getId();
        final SqlParameterSource sql = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("params", params)
                .addValue("uri", uri)
                .addValue("monitoring_type_id", mtId)
                .addValue("users_id", uId);

        final Number newId = jdbcInsert.executeAndReturnKey(sql);

        entity.setId(newId.intValue());
        return entity;
    }

    /**
     * Function 5.2
     * @param entity
     * @return
     */
    public void update(Entity entity) {

        final String sql = "UPDATE entity SET name=?, uri=?, params=?, users_id=?, monitoring_type_id=? WHERE entity_id=?";
        final String name = entity.getName();
        final String params = entity.getParams();
        final String uri = entity.getUri();
        final Integer mtId = entity.getMonitoringType().getId();
        final Integer uId = entity.getUser().getId();
        final Integer id = entity.getId();

        jdbcTemplate.update(sql, new Object[]{name, uri, params, uId, mtId, id});
    }

    /**
     * Function 5.3
     * @param id
     */
    public void delete(int id) {

        final String sql = "DELETE FROM entity WHERE entity_id=?";

        jdbcTemplate.update(sql, new Object[]{id});
    }

    /**
     * Function 5.4
     * @param id
     * @return
     */
    public Entity findOne(int id) {

        final String sql = "SELECT * FROM entity WHERE entity_id=?";

        final Entity entity = jdbcTemplate.queryForObject(sql, new Object[]{id}, mapper);

        return entity;
    }

    /**
     * Function 1.4
     * @param userId
     * @return
     */
    public List<Entity> findAllByUserId(int userId) {

        final String sql = "SELECT * FROM entity WHERE users_id=?";

        final List<Entity> entities = jdbcTemplate.query(sql, new Object[]{userId}, mapper);

        return entities;
    }


}
