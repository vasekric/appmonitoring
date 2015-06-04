package edu.vsb.dais.appmonitoring;

import edu.vsb.dais.appmonitoring.database.repositories.*;
import edu.vsb.dais.appmonitoring.service.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;

/**
 * Created by vasekric on 2. 5. 2015.
 */
@Service
public class DatabaseLayerTest {

    @Autowired private EntityRepository entityRepository;
    @Autowired private MonitoringTypeRepository monitoringTypeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SnapshotRepository snapshotRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private ObserverRepository observerRepository;
    @Autowired private ConfigurationRepository configRepository;

    @Autowired private PlatformTransactionManager transactionManager;

    //@PostConstruct
    public void testMethods() {
        DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
        final TransactionStatus transaction = transactionManager.getTransaction(paramTransactionDefinition);
        try {
            final User user = userRepository.save(new User(null, "vasek", 20000));
            final MonitoringType monitoringType = monitoringTypeRepository.save(new MonitoringType(null, "name", 20));
            final Entity entity = entityRepository.save(new Entity(null, "entity", "uri", "params", user, monitoringType));
            final Location location = locationRepository.save(new Location(null, "country", "city"));
            final Observer observer = observerRepository.save(new Observer(null, "name", new Date(), 100, monitoringType, location));
            final SniffConfig sniffConfig = new SniffConfig();
            sniffConfig.setAutoSubscribe(true);
            sniffConfig.setObserver(observer);
            sniffConfig.setEntity(entity);
            sniffConfig.setStatusRules(new StatusRules(null, 500, 5000, 1));
            sniffConfig.setSubscibtionTo(new Date());
            sniffConfig.setEnabled(true);
            sniffConfig.setFrequency(50);
            final SniffConfig savedSniffConfig = configRepository.save(sniffConfig);

            System.out.println("------------------------------------------------------------------------------------");

            System.out.println("savedSniffConfig " + savedSniffConfig);
            System.out.println("user "+user);
            System.out.println("monitoringType "+monitoringType);
            System.out.println("entity "+entity);
            System.out.println("location "+location);
            System.out.println("observer "+observer);

            System.out.println("configRepository.findAll() " + configRepository.findAll());
            System.out.println("configRepository.findAllByEntityId " + configRepository.findAllByEntityId(1));
            System.out.println("configRepository.findAllByUserId "+configRepository.findAllByUserId(1));
            System.out.println("configRepository.findOne "+configRepository.findOne(savedSniffConfig.getId()));
            System.out.println("configRepository.findOneByEntityIdAndObserverId " + configRepository.findOneByEntityIdAndObserverId(1, 1));

            System.out.println("observerRepository.findAll "+observerRepository.findAll());;
            System.out.println("observerRepository.findAllFree "+observerRepository.findAllFree(monitoringType.getId()));
            observerRepository.update(observer);
            System.out.println("observerRepository.findAllByEntity " + observerRepository.findAllByEntity(1));

            System.out.println("locationRepository.findAll "+locationRepository.findAll());
            locationRepository.update(location);

            System.out.println("entityRepository.findOne " + entityRepository.findOne(entity.getId()));
            System.out.println("entityRepository.findAllByUserId "+entityRepository.findAllByUserId(user.getId()));
            entityRepository.update(entity);

            System.out.println("userRepository.findOne " + userRepository.findOne(user.getId()));
            System.out.println("userRepository.findAllEntitiesAndObservers "+userRepository.findAllEntitiesAndObservers(1));
            userRepository.update(user);

            monitoringTypeRepository.update(monitoringType);
            System.out.println("monitoringTypeRepository.findAll " + monitoringTypeRepository.findAll());

            System.out.println("------------------------------------------------------------------------------------");

            configRepository.delete(savedSniffConfig.getId());
            observerRepository.delete(observer.getId());
            locationRepository.delete(location.getId());
            entityRepository.delete(entity.getId());
            userRepository.delete(user.getId());
            monitoringTypeRepository.delete(monitoringType.getId());

            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            e.printStackTrace();

        }

    }

    //@PostConstruct
    public void testSnapshots() {
        System.out.println("====================================================================================");
        System.out.println("snapshotRepository.lastResponseTimes "+snapshotRepository.lastResponseTimes(5, 1, 1));
        final ThresholdStats thresholdStats = snapshotRepository.thresholdStatistics(java.sql.Date.valueOf("2015-05-10"), java.sql.Date.valueOf("2015-05-11"), 1, 1);
        System.out.println("thresholdStats "+thresholdStats);
        System.out.println("snapshotRepository.getActualStatus "+snapshotRepository.getActualStatus(1, 1));
        System.out.println("snapshotRepository.findAll "+snapshotRepository.findAll(1, 1, 1, 1));
        System.out.println("====================================================================================");
    }
}
