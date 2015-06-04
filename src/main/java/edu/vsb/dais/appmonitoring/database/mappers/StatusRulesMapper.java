package edu.vsb.dais.appmonitoring.database.mappers;

import edu.vsb.dais.appmonitoring.service.models.StatusRules;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vasekric on 28. 4. 2015.
 */
@Component
public class StatusRulesMapper implements RowMapper<StatusRules> {
    @Override
    public StatusRules mapRow(ResultSet rs, int rowNum) throws SQLException {
        final int status_rules_id = rs.getInt("status_rules_id");
        final int threshold_warning = rs.getInt("threshold_warning");
        final int threshold_error = rs.getInt("threshold_error");
        final int quantity = rs.getInt("quantity");

        return new StatusRules(status_rules_id, threshold_warning, threshold_error, quantity);
    }
}
