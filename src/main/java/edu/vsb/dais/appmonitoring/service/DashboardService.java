package edu.vsb.dais.appmonitoring.service;

import edu.vsb.dais.appmonitoring.database.repositories.SnapshotRepository;
import edu.vsb.dais.appmonitoring.database.repositories.UserRepository;
import edu.vsb.dais.appmonitoring.service.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasekric on 8. 5. 2015.
 */
@Service
public class DashboardService {

    @Autowired private SnapshotRepository snapshotRepository;
    @Autowired private UserRepository userRepository;


    public List<EntityStatusPanel> getDashboardData(int userId) {
        final List<Pair<Entity, Observer>> entityObserverPairs = userRepository.findAllEntitiesAndObservers(userId);
        final List<EntityStatusPanel> entityStatusPanels = new ArrayList<>();

        entityObserverPairs.parallelStream().forEach((pair) -> {
            Entity entity = pair.getLeft();
            Observer observer = pair.getRight();
            final int actualStatus = snapshotRepository.getActualStatus(entity.getId(), observer.getId());
            final ThresholdStats thresholdStats = snapshotRepository.thresholdStatistics(Date.valueOf(LocalDate.now().minusDays(1)),
                    Date.valueOf(LocalDate.now()), entity.getId(), observer.getId());
            final List<Snapshot> all = snapshotRepository.findAll(entity.getId(), observer.getId(), 1, 0);

            final EntityStatusPanel entityStatusPanel = new EntityStatusPanel();
            entityStatusPanel.setEntityName(entity.getName());
            entityStatusPanel.setActualStatus(actualStatus);
            entityStatusPanel.setThresholdWarning(thresholdStats.getThresholdWarning());
            entityStatusPanel.setThresholdError(thresholdStats.getThresholdError());
            if (!all.isEmpty()) {
                final Snapshot snapshot = all.get(0);
                entityStatusPanel.setLastResponseTime(snapshot.getResponseTime());
                entityStatusPanel.setLastResponse(snapshot.getOrigReturnValue() + " " + snapshot.getOrigReturnDesc());
                entityStatusPanel.setLastStatus(snapshot.getOrigStatuId());
                entityStatusPanel.setTimestamp(snapshot.getTimestamp());
            }
            List<ThresholdStats.Stat> stats = thresholdStats.getStatistics();
            if (!stats.isEmpty()) {
                ThresholdStats.Stat stat = stats.get(0);
                int total = stat.getTotal();
                System.out.println("Total "+total);
                int warns = stat.getWarns();
                int errors = stat.getErrors();
                int warnPercent;
                int errPercent;
                if (total == 0) {
                    errPercent = 0;
                    warnPercent = 0;
                } else {
                    warnPercent = (warns * 100) / total;
                    errPercent = (errors * 100) / total;
                }
                int upPercent = 100 - warnPercent - errPercent;
                entityStatusPanel.setUpPercent(upPercent);
                entityStatusPanel.setWarnPercent(warnPercent);
                entityStatusPanel.setErrPercent(errPercent);

                List<Integer> responseTimes = snapshotRepository.lastResponseTimes(total, entity.getId(), observer.getId());
                if(responseTimes.isEmpty()) {
                    long timeDiff = ((new java.util.Date().getTime() - entityStatusPanel.getTimestamp().getTime()) / 1000);
                    if(timeDiff < 24*60*60) {
                        int numberOfTry = 5;
                        for(int i = 0; i < numberOfTry; i++) {
                            responseTimes = snapshotRepository.lastResponseTimes(total, entity.getId(), observer.getId());
                            if(!responseTimes.isEmpty()) {
                                break;
                            }
                        }
                    }
                }
                entityStatusPanel.setResponseTimes(responseTimes);
            }
            entityStatusPanels.add(entityStatusPanel);
        });
        return entityStatusPanels;
    };
}
