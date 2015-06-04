package edu.vsb.dais.appmonitoring.service.models;

/**
 * Created by vasekric on 25. 4. 2015.
 */
public class Location {
    private Integer id;
    private String country;
    private String city;

    public Location(Integer id, String country, String city) {
        this.id = id;
        this.country = country;
        this.city = city;
    }

    public Location() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
