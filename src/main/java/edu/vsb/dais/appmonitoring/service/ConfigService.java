package edu.vsb.dais.appmonitoring.service;

import edu.vsb.dais.appmonitoring.database.repositories.ConfigurationRepository;
import edu.vsb.dais.appmonitoring.database.repositories.ObserverRepository;
import edu.vsb.dais.appmonitoring.database.repositories.UserRepository;
import edu.vsb.dais.appmonitoring.service.models.Entity;
import edu.vsb.dais.appmonitoring.service.models.Observer;
import edu.vsb.dais.appmonitoring.service.models.Pair;
import edu.vsb.dais.appmonitoring.service.models.SniffConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vasekric on 9. 5. 2015.
 */
@Service
public class ConfigService {

    @Autowired private UserRepository userRepository;
    @Autowired private ConfigurationRepository configRepository;
    @Autowired private ObserverRepository observerRepository;

    public List<Pair<Entity,Observer>> getPairs(int userId) {
        return userRepository.findAllEntitiesAndObservers(userId);
    }

    public SniffConfig getConfig(int entityId, int observerId) {
        return configRepository.findOneByEntityIdAndObserverId(entityId, observerId);
    }

    public List<Observer> getObservers(int monitoringTypeId) {
        return observerRepository.findAllFree(monitoringTypeId);
    }

    public void observerChange(int observerId, int configId) {
        configRepository.observerChange(observerId, configId);
    }

    public void updateConfig(SniffConfig config) {
        configRepository.update(config);
    }
}

