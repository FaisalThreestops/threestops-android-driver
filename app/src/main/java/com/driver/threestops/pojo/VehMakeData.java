package com.driver.threestops.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class VehMakeData implements Serializable {

    private ArrayList<VehicleMakeModel> models;

    private String Name;

    private String _id;

    private String Makeid;

    private boolean selected;

    public String getMakeid() {
        return Makeid;
    }

    public void setMakeid(String makeid) {
        Makeid = makeid;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ArrayList<VehicleMakeModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<VehicleMakeModel> models) {
        this.models = models;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = _id;
    }
}
