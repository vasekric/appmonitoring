package edu.vsb.dais.appmonitoring.view;

import edu.vsb.dais.appmonitoring.service.ConfigService;
import edu.vsb.dais.appmonitoring.service.models.Entity;
import edu.vsb.dais.appmonitoring.service.models.Observer;
import edu.vsb.dais.appmonitoring.service.models.Pair;
import edu.vsb.dais.appmonitoring.service.models.SniffConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasekric on 9. 5. 2015.
 */
@Component
@Scope("session")
public class ModifyConfigView implements Serializable {

    @Autowired private ConfigService configService;

    private List<Pair<Entity, Observer>> pairs;
    private Pair<Entity, Observer> selectedPair;
    private Observer originalObserver;
    private SniffConfig config = new SniffConfig();
    //private List<Observer> observers;
    private List<SelectItem> observers;
    private Integer selectedObserver;
    private List<Observer> loadedObservers;

    @PostConstruct
    private void init() {
        int userId = 1;
        pairs = configService.getPairs(userId);
    }

    public void rowSelect() {
        if(selectedPair == null) {
            System.err.println("selectedPair == null " +selectedPair == null);
            return;
        }
        config = configService.getConfig(selectedPair.getLeft().getId(), selectedPair.getRight().getId());
        originalObserver = config.getObserver();
        loadedObservers = configService.getObservers(selectedPair.getLeft().getMonitoringType().getId());
        observers = new ArrayList<>();
        loadedObservers.forEach((observer) -> {
            observers.add(new SelectItem(observer.getId(), observer.getName()));
        });
        observers.forEach((observer) -> {
            if (observer.getValue().equals(config.getObserver().getId())) {
                selectedObserver = (int) observer.getValue();
                return;
            }
        });
    }

    public void deleteConfig() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Configuration deleted."));
    }

    public void updateConfig() {
        System.out.println("updateConfig()");
        if(loadedObservers == null) {
            System.err.println("loadedObservers == null");
        }
        loadedObservers.forEach((observer) -> {
            if (observer.getId().equals(selectedObserver)) {
                config.setObserver(observer);
            }
        });
        if(!originalObserver.equals(config.getObserver())) {
            configService.observerChange(config.getObserver().getId(), config.getId());
            selectedPair.setRight(config.getObserver());
        }
        configService.updateConfig(config);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Configuration updated."));
    }

    public ConfigService getConfigService() {
        return configService;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    public Observer getOriginalObserver() {
        return originalObserver;
    }

    public void setOriginalObserver(Observer originalObserver) {
        this.originalObserver = originalObserver;
    }

    public Integer getSelectedObserver() {
        return selectedObserver;
    }

    public void setSelectedObserver(Integer selectedObserver) {
        this.selectedObserver = selectedObserver;
    }

    public List<Observer> getLoadedObservers() {
        return loadedObservers;
    }

    public void setLoadedObservers(List<Observer> loadedObservers) {
        this.loadedObservers = loadedObservers;
    }

    public List<Pair<Entity, Observer>> getPairs() {
        return pairs;
    }

    public void setPairs(List<Pair<Entity, Observer>> pairs) {
        this.pairs = pairs;
    }

    public Pair<Entity, Observer> getSelectedPair() {
        return selectedPair;
    }

    public void setSelectedPair(Pair<Entity, Observer> selectedPair) {
        this.selectedPair = selectedPair;
    }

    public SniffConfig getConfig() {
        return config;
    }

    public void setConfig(SniffConfig config) {
        this.config = config;
    }

    public List<SelectItem> getObservers() {
        return observers;
    }

    public void setObservers(List<SelectItem> observers) {
        this.observers = observers;
    }
}
