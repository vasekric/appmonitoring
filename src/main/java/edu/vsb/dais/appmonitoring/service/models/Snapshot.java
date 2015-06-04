package edu.vsb.dais.appmonitoring.service.models;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by vasekric on 25. 4. 2015.
 */
public class Snapshot {

    private Integer id;
    private Date timestamp;
    private Integer responseTime;
    private String origReturnValue;
    private String origReturnDesc;
    private int origStatuId;
    private String origRequest;
    private String origResponse;
    private Entity entity;
    private Observer observer;

    public Snapshot() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public String getOrigReturnValue() {
        return origReturnValue;
    }

    public void setOrigReturnValue(String origReturnValue) {
        this.origReturnValue = origReturnValue;
    }

    public String getOrigReturnDesc() {
        return origReturnDesc;
    }

    public void setOrigReturnDesc(String origReturnDesc) {
        this.origReturnDesc = origReturnDesc;
    }

    public int getOrigStatuId() {
        return origStatuId;
    }

    public void setOrigStatuId(int origStatuId) {
        this.origStatuId = origStatuId;
    }

    public String getOrigRequest() {
        return origRequest;
    }

    public void setOrigRequest(String origRequest) {
        this.origRequest = origRequest;
    }

    public String getOrigResponse() {
        return origResponse;
    }

    public void setOrigResponse(String origResponse) {
        this.origResponse = origResponse;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    @Override
    public String toString() {
        return "Snapshot{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", responseTime=" + responseTime +
                ", origReturnValue='" + origReturnValue + '\'' +
                ", origReturnDesc='" + origReturnDesc + '\'' +
                ", origStatuId=" + origStatuId +
                ", origRequest='" + origRequest + '\'' +
                ", origResponse='" + origResponse + '\'' +
                ", entity=" + entity +
                ", observer=" + observer +
                '}';
    }
}
