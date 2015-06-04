package edu.vsb.dais.appmonitoring;

import edu.vsb.dais.appmonitoring.database.repositories.ConfigurationRepository;
import edu.vsb.dais.appmonitoring.database.repositories.SnapshotRepository;
import edu.vsb.dais.appmonitoring.service.models.Entity;
import edu.vsb.dais.appmonitoring.service.models.Observer;
import edu.vsb.dais.appmonitoring.service.models.Snapshot;
import edu.vsb.dais.appmonitoring.service.models.SniffConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by vasekric on 2. 5. 2015.
 */
@Component
@EnableScheduling
public class SnapshotGenerator {

    @Autowired private SnapshotRepository snapshotRepository;
    @Autowired private ConfigurationRepository configRepository;
    private Random random = new Random();

    @Scheduled(fixedDelay = 20000, initialDelay = 5000)
    public void generateRandomSnapshots() {

        final List<SniffConfig> configs = configRepository.findAll();

        configs.forEach((config) -> {
            final Entity entity = config.getEntity();
            final Observer observer = config.getObserver();
            final Snapshot snapshot = new Snapshot();
            snapshot.setId(null);
            snapshot.setObserver(observer);
            snapshot.setEntity(entity);
            snapshot.setResponseTime(random.nextInt(8000));
            snapshot.setOrigStatuId(1);
            snapshot.setOrigRequest("PING site");
            snapshot.setOrigResponse("PONG");
            snapshot.setOrigReturnDesc("OK");
            snapshot.setOrigReturnValue("200");
            snapshot.setTimestamp(new Date());

            snapshotRepository.save(snapshot);
        });
    }


}
