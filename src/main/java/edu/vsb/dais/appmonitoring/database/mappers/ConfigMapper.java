package edu.vsb.dais.appmonitoring.database.mappers;

import edu.vsb.dais.appmonitoring.service.models.Entity;
import edu.vsb.dais.appmonitoring.service.models.Observer;
import edu.vsb.dais.appmonitoring.service.models.SniffConfig;
import edu.vsb.dais.appmonitoring.service.models.StatusRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vasekric on 28. 4. 2015.
 */
@Component
public class ConfigMapper implements RowMapper<SniffConfig> {

    @Autowired private StatusRulesMapper statusRulesMapper;

    @Override
    public SniffConfig mapRow(ResultSet rs, int rowNum) throws SQLException {

        final int sniff_config_id = rs.getInt("sniff_config_id");
        final int frequency = rs.getInt("frequency");
        final boolean enabled = rs.getString("enabled").equals("1");
        final boolean auto_subscribe = rs.getString("auto_subscribe").equals("1");
        final Date subsctiption_to = new Date(rs.getTimestamp("SUBSCRIPTION_TO").getTime());
        final int observer_id = rs.getInt("observer_id");
        final int entity_id = rs.getInt("entity_id");
        final StatusRules statusRules = statusRulesMapper.mapRow(rs, rowNum);

        final SniffConfig sniffConfig = new SniffConfig();
        sniffConfig.setId(sniff_config_id);
        sniffConfig.setAutoSubscribe(auto_subscribe);
        sniffConfig.setStatusRules(statusRules);
        sniffConfig.setFrequency(frequency);
        sniffConfig.setEnabled(enabled);
        sniffConfig.setSubscibtionTo(subsctiption_to);
        final Observer observer = new Observer();
        observer.setId(observer_id);
        final Entity entity = new Entity();
        entity.setId(entity_id);
        sniffConfig.setEntity(entity);
        sniffConfig.setObserver(observer);

        return sniffConfig;
    }
}
