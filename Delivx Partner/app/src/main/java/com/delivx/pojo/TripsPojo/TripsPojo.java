package com.delivx.pojo.TripsPojo;

import java.io.Serializable;

/**
 * Created by embed on 23/5/17.
 */

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
