package edu.vsb.dais.appmonitoring.database.mappers;

import edu.vsb.dais.appmonitoring.service.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vasekric on 27. 4. 2015.
 */
@Component
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        final int userId = rs.getInt("users_id");
        final String name = rs.getString("name");
        final int bundle = rs.getInt("bundle");

        return new User(userId, name, bundle);
    }
}
