package edu.vsb.dais.appmonitoring.database.mappers;

import edu.vsb.dais.appmonitoring.service.models.Location;
import edu.vsb.dais.appmonitoring.service.models.MonitoringType;
import edu.vsb.dais.appmonitoring.service.models.Observer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by vasekric on 27. 4. 2015.
 */
@Component
public class ObserverMapper implements RowMapper<Observer>{
    @Override
    public Observer mapRow(ResultSet rs, int rowNum) throws SQLException {
        final int observerId = rs.getInt("observer_id");
        final String name = rs.getString("name");
        final Timestamp changed = rs.getTimestamp("changed");
        final int entityCapacityLeft = rs.getInt("entity_capacity_left");
        final int monitoringTypeId = rs.getInt("monitoring_type_id");
        final int locationId = rs.getInt("location_id");

        final MonitoringType monitoringType = new MonitoringType();
        monitoringType.setId(monitoringTypeId);
        final Location location = new Location();
        location.setId(locationId);

        return new Observer(observerId, name, new Date(changed.getTime()), entityCapacityLeft, monitoringType, location);
    }
}
