package edu.vsb.dais.appmonitoring.database.mappers;

import edu.vsb.dais.appmonitoring.service.models.Entity;
import edu.vsb.dais.appmonitoring.service.models.Observer;
import edu.vsb.dais.appmonitoring.service.models.Snapshot;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by vasekric on 1. 5. 2015.
 */
@Component
public class SnapshotMapper implements RowMapper<Snapshot> {


    @Override
    public Snapshot mapRow(ResultSet rs, int rowNum) throws SQLException {
        final int snapshotId = rs.getInt("snapshot_id");
        final Timestamp timestamp = rs.getTimestamp("timestamp");
        final int responseTime = rs.getInt("response_time");
        final String origReturnValue = rs.getString("orig_return_value");
        final String origReturnDesc = rs.getString("orig_return_desc");
        final int origStatusId = rs.getInt("orig_status_id");
        final String origRequest = rs.getString("orig_request");
        final String origResponse = rs.getString("orig_response");
        final int observerId = rs.getInt("observer_id");
        final int entityId = rs.getInt("entity_id");

        final Snapshot snapshot = new Snapshot();
        snapshot.setId(snapshotId);
        snapshot.setOrigRequest(origRequest);
        snapshot.setOrigResponse(origResponse);
        snapshot.setOrigReturnDesc(origReturnDesc);
        snapshot.setOrigReturnValue(origReturnValue);
        snapshot.setOrigStatuId(origStatusId);
        snapshot.setResponseTime(responseTime);
        snapshot.setTimestamp(new Date(timestamp.getTime()));
        final Entity entity = new Entity();
        entity.setId(entityId);
        snapshot.setEntity(entity);
        final Observer observer = new Observer();
        observer.setId(observerId);
        snapshot.setObserver(observer);

        return snapshot;
    }
}
