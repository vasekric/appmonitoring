package edu.vsb.dais.appmonitoring.service.models;

/**
 * Created by vasekric on 25. 4. 2015.
 */
public class User {

    private Integer id;
    private String name;
    private int bundle;

    public User(Integer id, String name, int bundle) {
        this.id = id;
        this.name = name;
        this.bundle = bundle;
    }

    public User() {
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

    public int getBundle() {
        return bundle;
    }

    public void setBundle(int bundle) {
        this.bundle = bundle;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bundle=" + bundle +
                '}';
    }
}
