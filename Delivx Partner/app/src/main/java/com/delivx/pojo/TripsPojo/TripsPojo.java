package com.delivx.pojo.TripsPojo;

import java.io.Serializable;


public class TripsPojo implements Serializable {

    private String message;

    private TripsData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TripsData getData() {
        return data;
    }

    public void setData(TripsData data) {
        this.data = data;
    }
}
