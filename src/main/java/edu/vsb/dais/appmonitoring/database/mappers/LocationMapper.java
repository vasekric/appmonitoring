package edu.vsb.dais.appmonitoring.database.mappers;

import edu.vsb.dais.appmonitoring.service.models.Location;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vasekric on 27. 4. 2015.
 */
@Component
public class LocationMapper implements RowMapper<Location> {
    @Override
    public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
        final int locationId = rs.getInt("location_id");
        final String country = rs.getString("country");
        final String city = rs.getString("city");

        return new Location(locationId, country, city);
    }
}
