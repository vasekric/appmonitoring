package edu.vsb.dais.appmonitoring.service.models;

import java.util.Date;

/**
 * Created by vasekric on 25. 4. 2015.
 */
public class SniffConfig {

    private Integer id;
    private int frequency;
    private boolean enabled;
    private Date subscibtionTo;
    private boolean autoSubscribe;
    private Observer observer;
    private Entity entity;
    private StatusRules statusRules;

    public SniffConfig() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getSubscibtionTo() {
        return subscibtionTo;
    }

    public void setSubscibtionTo(Date subscibtionTo) {
        this.subscibtionTo = subscibtionTo;
    }

    public boolean isAutoSubscribe() {
        return autoSubscribe;
    }

    public void setAutoSubscribe(boolean autoSubscribe) {
        this.autoSubscribe = autoSubscribe;
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public StatusRules getStatusRules() {
        return statusRules;
    }

    public void setStatusRules(StatusRules statusRules) {
        this.statusRules = statusRules;
    }

    @Override
    public String toString() {
        return "SniffConfig{" +
                "id=" + id +
                ", frequency=" + frequency +
                ", enabled=" + enabled +
                ", subscibtionTo=" + subscibtionTo +
                ", autoSubscribe=" + autoSubscribe +
                ", observer=" + observer +
                ", entity=" + entity +
                ", statusRules=" + statusRules +
                '}';
    }
}
