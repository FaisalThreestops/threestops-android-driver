package com.driver.threestops.pojo;

import android.os.Parcel;

import java.io.Serializable;


public class VehTypeSepecialities implements Serializable {

    private String id;

    private String Name;

    private boolean selected;

    protected VehTypeSepecialities(Parcel in) {
        id = in.readString();
        Name = in.readString();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


}
