package com.driver.threestops.pojo;

import java.io.Serializable;
import java.util.ArrayList;


public class SignupZonesPojo implements Serializable {


    private String message;

    private ArrayList<ZoneData> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ZoneData> getData() {
        return data;
    }

    public void setData(ArrayList<ZoneData> data) {
        this.data = data;
    }
}
