package edu.vsb.dais.appmonitoring.service.models;

/**
 * Created by vasekric on 25. 4. 2015.
 */
public class MonitoringType {

    private Integer id;
    private String name;
    private int dayPrice;

    public MonitoringType(Integer id, String name, int dayPrice) {
        this.id = id;
        this.name = name;
        this.dayPrice = dayPrice;
    }

    public MonitoringType() {
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

    public int getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(int dayPrice) {
        this.dayPrice = dayPrice;
    }

    @Override
    public String toString() {
        return "MonitoringType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dayPrice=" + dayPrice +
                '}';
    }
}
