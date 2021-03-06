package com.driver.threestops.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class SupportPojo implements Serializable {

    private String message;

    private ArrayList<SupportData> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<SupportData> getData() {
        return data;
    }

    public void setData(ArrayList<SupportData> data) {
        this.data = data;
    }
}
