package edu.vsb.dais.appmonitoring.service.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vasekric on 8. 5. 2015.
 */
public class EntityStatusPanel {
    private String entityName;
    private int lastResponseTime;
    private String lastResponse;
    private int lastStatus;
    private int actualStatus;
    private int upPercent;
    private int warnPercent;
    private int errPercent;
    private Date timestamp;

    public int getThresholdWarning() {
        return thresholdWarning;
    }

    public void setThresholdWarning(int thresholdWarning) {
        this.thresholdWarning = thresholdWarning;
    }

    private int thresholdWarning;

    public int getThresholdError() {
        return thresholdError;
    }

    public void setThresholdError(int thresholdError) {
        this.thresholdError = thresholdError;
    }

    private int thresholdError;
    private List<Integer> responseTimes = new ArrayList<>();

    public EntityStatusPanel() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Integer> getResponseTimes() {
        return responseTimes;
    }

    public void setResponseTimes(List<Integer> responseTimes) {
        this.responseTimes = responseTimes;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getLastResponseTime() {
        return lastResponseTime;
    }

    public void setLastResponseTime(int lastResponseTime) {
        this.lastResponseTime = lastResponseTime;
    }

    public String getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(String lastResponse) {
        this.lastResponse = lastResponse;
    }

    public int getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(int lastStatus) {
        this.lastStatus = lastStatus;
    }

    public int getActualStatus() {
        return actualStatus;
    }

    public void setActualStatus(int actualStatus) {
        this.actualStatus = actualStatus;
    }

    public int getUpPercent() {
        return upPercent;
    }

    public void setUpPercent(int upPercent) {
        this.upPercent = upPercent;
    }

    public int getWarnPercent() {
        return warnPercent;
    }

    public void setWarnPercent(int warnPercent) {
        this.warnPercent = warnPercent;
    }

    public int getErrPercent() {
        return errPercent;
    }

    public void setErrPercent(int errPercent) {
        this.errPercent = errPercent;
    }
}
