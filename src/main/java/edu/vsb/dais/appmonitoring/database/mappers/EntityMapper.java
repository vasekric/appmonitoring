package edu.vsb.dais.appmonitoring.database.mappers;

import edu.vsb.dais.appmonitoring.service.models.Entity;
import edu.vsb.dais.appmonitoring.service.models.MonitoringType;
import edu.vsb.dais.appmonitoring.service.models.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vasekric on 27. 4. 2015.
 */
@Component
public class EntityMapper implements RowMapper<Entity> {

    @Override
    public Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
        final int entityId = rs.getInt("entity_id");
        final String name = rs.getString("name");
        final String uri = rs.getString("uri");
        final String params = rs.getString("params");
        final int userId = rs.getInt("users_id");
        final int monitoringTypeId = rs.getInt("monitoring_type_id");

        final User user = new User();
        user.setId(userId);
        final MonitoringType monitoringType = new MonitoringType();
        monitoringType.setId(monitoringTypeId);

        return new Entity(entityId, name, uri, params, user, monitoringType);
    }
}

