package edu.vsb.dais.appmonitoring.service.models;

/**
 * Created by vasekric on 25. 4. 2015.
 */
public class StatusRules {

    private Integer id;
    private int thresholdWarning;
    private int thresholdError;
    private int quantity;

    public StatusRules(Integer id, int thresholdWarning, int thresholdError, int quantity) {
        this.id = id;
        this.thresholdWarning = thresholdWarning;
        this.thresholdError = thresholdError;
        this.quantity = quantity;
    }

    public StatusRules() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getThresholdWarning() {
        return thresholdWarning;
    }

    public void setThresholdWarning(int thresholdWarning) {
        this.thresholdWarning = thresholdWarning;
    }

    public int getThresholdError() {
        return thresholdError;
    }

    public void setThresholdError(int thresholdError) {
        this.thresholdError = thresholdError;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "StatusRules{" +
                "id=" + id +
                ", thresholdWarning=" + thresholdWarning +
                ", thresholdError=" + thresholdError +
                ", quantity=" + quantity +
                '}';
    }
}
