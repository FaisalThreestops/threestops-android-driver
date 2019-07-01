package com.delivx.pojo;

import java.io.Serializable;
import java.util.ArrayList;


public class ZoneData implements Serializable {
    private String id;

    private ArrayList<Cities> cities;

    private String country;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Cities> getCities() {
        return cities;
    }

    public void setCities(ArrayList<Cities> cities) {
        this.cities = cities;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
