package edu.vsb.dais.appmonitoring.database.mappers;

import edu.vsb.dais.appmonitoring.service.models.MonitoringType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vasekric on 27. 4. 2015.
 */
@Component
public class MonitoringTypeMapper implements RowMapper<MonitoringType> {

    @Override
    public MonitoringType mapRow(ResultSet rs, int rowNum) throws SQLException {
        final int monitoringTypeId = rs.getInt("monitoring_type_id");
        final String name = rs.getString("name");
        final int dayPrice = rs.getInt("day_price");

        return new MonitoringType(monitoringTypeId, name, dayPrice);
    }
}
