package edu.vsb.dais.appmonitoring.service.models;

import java.util.Date;

/**
 * Created by vasekric on 25. 4. 2015.
 */
public class Observer {

    private Integer id;
    private String name;
    private Date changed;
    private Integer entityCapacityLeft;
    private MonitoringType monitoringType;
    private Location location;

    public Observer() {
    }

    public Observer(Integer id, String name, Date changed, Integer entityCapacityLeft, MonitoringType monitoringType, Location location) {
        this.id = id;
        this.name = name;
        this.changed = changed;
        this.entityCapacityLeft = entityCapacityLeft;
        this.monitoringType = monitoringType;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getChanged() {
        return changed;
    }

    public void setChanged(Date changed) {
        this.changed = changed;
    }

    public Integer getEntityCapacityLeft() {
        return entityCapacityLeft;
    }

    public void setEntityCapacityLeft(Integer entityCapacityLeft) {
        this.entityCapacityLeft = entityCapacityLeft;
    }

    public MonitoringType getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(MonitoringType monitoringType) {
        this.monitoringType = monitoringType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Observer observer = (Observer) o;

        return !(id != null ? !id.equals(observer.id) : observer.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
