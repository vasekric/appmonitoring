package edu.vsb.dais.appmonitoring.database.repositories;

import edu.vsb.dais.appmonitoring.database.mappers.SnapshotMapper;
import edu.vsb.dais.appmonitoring.service.models.Snapshot;
import edu.vsb.dais.appmonitoring.service.models.StatusRules;
import edu.vsb.dais.appmonitoring.service.models.ThresholdStats;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.sql.Types;
import java.util.*;

/**
 * Created by vasekric on 27. 4. 2015.
 */
@Repository
public class SnapshotRepository {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private SnapshotMapper snapshotMapper;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcCall actualStatusCall;

    @PostConstruct
    public void init() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("snapshot")
                .usingGeneratedKeyColumns("snapshot_id");

        actualStatusCall = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName("actualStatus")
                .declareParameters(
                        new SqlOutParameter("RESULTS", OracleTypes.INTEGER, new ColumnMapRowMapper()),
                        new SqlParameter("p_entity_id", Types.INTEGER),
                        new SqlParameter("p_observer_id", Types.INTEGER)
                )
                .withoutProcedureColumnMetaDataAccess();
    }

    /**
     * Function 7.1
     * @param snapshot
     * @return
     */
    public Snapshot save(Snapshot snapshot) {

        final SqlParameterSource sql = new MapSqlParameterSource()
                .addValue("observer_id", snapshot.getObserver().getId())
                .addValue("entity_id", snapshot.getEntity().getId())
                .addValue("orig_request", snapshot.getOrigRequest())
                .addValue("orig_return_desc", snapshot.getOrigReturnDesc())
                .addValue("orig_status_id", snapshot.getOrigStatuId())
                .addValue("response_time", snapshot.getResponseTime())
                .addValue("timestamp", snapshot.getTimestamp())
                .addValue("orig_response", snapshot.getOrigResponse())
                .addValue("orig_return_value", snapshot.getOrigReturnValue());

        final Number newId = jdbcInsert.executeAndReturnKey(sql);

        snapshot.setId(newId.intValue());
        return snapshot;
    }

    /**
     * Function 7.2
     * @param id
     */
    public void delete(int id) {

        final String sql = "DELETE FROM snapshot WHERE snapshot_id=?";

        jdbcTemplate.update(sql, id);
    }

    /**
     * Function 7.3
     * @param entityId
     * @param observerId
     * @param pageSize
     * @param pageNumber
     * @return
     */
    public List<Snapshot> findAll(int entityId, int observerId, int pageSize, int pageNumber) {

        final int from = pageSize*pageNumber;
        final int to = from+pageSize;
        final String sql = "SELECT * FROM (SELECT rownum rnum, s.* FROM snapshot s WHERE rownum <=? AND entity_id=? AND observer_id=?) WHERE rnum >=? ORDER BY timestamp DESC";

        final List<Snapshot> snapshots = jdbcTemplate.query(sql, new Object[]{to, entityId, observerId, from}, snapshotMapper);

        return snapshots;
    }

    /**
     * Function 7.4
     * @param entityId
     * @param observerId
     * @return
     */
    public int getActualStatus(int entityId, int observerId) {

        Map<String, Object> map = new HashMap<>();
        map.put("p_entity_id", entityId);
        map.put("p_observer_id", observerId);
        SqlParameterSource params = new MapSqlParameterSource(map);

        Map<String, Object> results = actualStatusCall.execute(params);

        return (Integer) results.get("RESULTS");
    }

    /**
     * Function 7.5
     * @param since
     * @param to
     * @param entityId
     * @param observerId
     * @return
     */
    public ThresholdStats thresholdStatistics(Date since, Date to, int entityId, int observerId) {

        final String thresholdsSql = "SELECT threshold_warning, threshold_error FROM status_rules sr JOIN sniff_config sc ON sc.status_rules_id=sr.status_rules_id WHERE sc.observer_id=? AND entity_id=?";
        final String totalSql = "SELECT COUNT(*) FROM snapshot WHERE observer_id=? AND entity_id=? AND TRUNC(timestamp)=?";
        final String responseTimesSql = "SELECT COUNT(*) FROM snapshot WHERE observer_id=? AND entity_id=? AND TRUNC(timestamp)=? AND response_time >=?";

        final StatusRules thresholds = jdbcTemplate.queryForObject(thresholdsSql, new Object[]{observerId, entityId}, (rs, rowNum) -> {
            final int thresholdWarning = rs.getInt("threshold_warning");
            final int thresholdError = rs.getInt("threshold_error");
            final StatusRules statusRules = new StatusRules();
            statusRules.setThresholdWarning(thresholdWarning);
            statusRules.setThresholdError(thresholdError);
            return statusRules;
        });
        
        final int thresholdWarning = thresholds.getThresholdWarning();
        final int thresholdError = thresholds.getThresholdError();
        final ThresholdStats thresholdStats = new ThresholdStats(thresholdWarning, thresholdError);
        final List<Date> dateList = SnapshotRepository.dateInterval(since, to);

        dateList.parallelStream().forEach((date) -> {
            final Integer total = jdbcTemplate.queryForObject(totalSql, Integer.class, observerId, entityId, date);
            final Integer warnsNErrors = jdbcTemplate.queryForObject(responseTimesSql, Integer.class, observerId, entityId, date, thresholdWarning);
            final Integer errors = jdbcTemplate.queryForObject(responseTimesSql, Integer.class, observerId, entityId, date, thresholdError);
            final Integer warns = warnsNErrors - errors;
            thresholdStats.addStatistic(new ThresholdStats.Stat(errors, warns, total, date.toLocalDate()));
        });

        return thresholdStats;
    }

    /**
     * Function 7.6
     * @param quantity
     * @return
     */
    public List<Integer> lastResponseTimes(int quantity, int entityId, int observerId) {

        final String sql = "SELECT response_time FROM snapshot s WHERE rownum <=? AND entity_id=? AND observer_id=? ORDER BY timestamp DESC";

        final List<Integer> responseTimes = jdbcTemplate.queryForList(sql, new Object[]{quantity, entityId, observerId}, (Class<Integer>) Integer.class);

        return responseTimes;
    }

    public static List<Date> dateInterval(Date initial, Date end) {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(initial);

        while (calendar.getTime().before(end)) {
            Date result = new Date(calendar.getTimeInMillis());
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        dates.add(end);

        return dates;
    }
}
