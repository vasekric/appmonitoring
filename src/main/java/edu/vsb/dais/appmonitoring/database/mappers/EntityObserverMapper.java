package edu.vsb.dais.appmonitoring.database.mappers;

import edu.vsb.dais.appmonitoring.service.models.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by vasekric on 3. 5. 2015.
 */
@Component
public class EntityObserverMapper implements RowMapper<Pair<Entity, Observer>> {
    @Override
    public Pair<Entity, Observer> mapRow(ResultSet rs, int rowNum) throws SQLException {
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

        final Entity entity = new Entity(entityId, name, uri, params, user, monitoringType);

        final int observerId = rs.getInt("observer_id");
        final String oName = rs.getString("observer_name");
        final Timestamp changed = rs.getTimestamp("changed");
        final int entityCapacityLeft = rs.getInt("entity_capacity_left");
        final int locationId = rs.getInt("location_id");

        final Location location = new Location();
        location.setId(locationId);

        final Observer observer = new Observer(observerId, oName, new Date(changed.getTime()), entityCapacityLeft, monitoringType, location);

        return new Pair<>(entity, observer);
    }
}
