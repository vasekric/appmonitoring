package edu.vsb.dais.appmonitoring.service.models;

public class Entity {

    private Integer id;
    private String name;
    private String uri;
    private String params;
    private User user;
    private MonitoringType monitoringType;

    public Entity(Integer id, String name, String uri, String params, User user, MonitoringType monitoringType) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.params = params;
        this.user = user;
        this.monitoringType = monitoringType;
    }

    public Entity() {
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MonitoringType getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(MonitoringType monitoringType) {
        this.monitoringType = monitoringType;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", params='" + params + '\'' +
                ", user=" + user +
                ", monitoringType=" + monitoringType +
                '}';
    }
}
