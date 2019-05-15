package com.delivx.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 8/3/2017.
 */

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
