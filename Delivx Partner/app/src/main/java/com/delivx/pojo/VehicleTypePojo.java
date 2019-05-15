package com.delivx.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 15/5/17.
 */

public class VehicleTypePojo implements Serializable {

    private String message;

    private ArrayList<VehTypeData> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<VehTypeData> getData() {
        return data;
    }

    public void setData(ArrayList<VehTypeData> data) {
        this.data = data;
    }
}
