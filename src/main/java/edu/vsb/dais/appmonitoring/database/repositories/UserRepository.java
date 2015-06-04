package edu.vsb.dais.appmonitoring.database.repositories;

import edu.vsb.dais.appmonitoring.database.mappers.EntityObserverMapper;
import edu.vsb.dais.appmonitoring.database.mappers.UserMapper;
import edu.vsb.dais.appmonitoring.service.models.Entity;
import edu.vsb.dais.appmonitoring.service.models.Observer;
import edu.vsb.dais.appmonitoring.service.models.Pair;
import edu.vsb.dais.appmonitoring.service.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by vasekric on 25. 4. 2015.
 */
@Repository
public class UserRepository {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private UserMapper mapper;
    @Autowired private EntityObserverMapper entityObserverMapper;
    private SimpleJdbcInsert jdbcInsert;

    @PostConstruct
    public void init() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("users_id");;
    }

    /**
     * Function 1.1 Vložení uživatele
     *
     * @param user
     * @return
     */
    public User save(User user) {

        final String name = user.getName();
        final int bundle = user.getBundle();
        final SqlParameterSource sql = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("bundle", bundle);

        final Number newId = jdbcInsert.executeAndReturnKey(sql);

        user.setId(newId.intValue());
        return user;
    }

    /**
     * Function 1.2 Aktualizace uzivatele
     * @param user
     * @return
     */
    public void update(User user) {

        final String sql = "UPDATE users SET name=?, bundle=? WHERE users_id=?";
        final Integer id = user.getId();
        final String name = user.getName();
        final int bundle = user.getBundle();

        jdbcTemplate.update(sql, new Object[]{name, bundle, id});
    }

    /**
     * Function 1.3 Zruseni uzivatele
     * @param userId
     */
    public void delete(int userId) {
        final String sql = "DELETE FROM users WHERE users_id=?";

        jdbcTemplate.update(sql, new Object[]{userId});
    }

    /**
     * Function 1.5
     * @param userId
     * @return
     */
    public List<Pair<Entity, Observer>> findAllEntitiesAndObservers(int userId) {
        final String sql = "SELECT e.*, o.observer_id, o.name AS observer_name, o.changed, o.entity_capacity_left, o.location_id FROM entity e JOIN sniff_config sc ON sc.entity_id=e.entity_id JOIN observer o ON o.observer_id=sc.observer_id WHERE users_id=?";

        final List<Pair<Entity, Observer>> pairs = jdbcTemplate.query(sql, new Object[]{userId}, entityObserverMapper);

        return pairs;
    }


    public User findOne(int userId) {
        final String sql = "SELECT * FROM users WHERE users_id=?";

        final User user = jdbcTemplate.queryForObject(sql, new Object[]{userId}, mapper);

        return user;
    }

}
