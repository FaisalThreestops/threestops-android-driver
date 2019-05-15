package com.delivx.pojo;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by embed on 16/5/17.
 */

public class VehicleMakeModel implements Serializable {

    private String _id;
    private String Name;
    private boolean selected;

    protected VehicleMakeModel(Parcel in) {
        _id = in.readString();
        Name = in.readString();
        selected = in.readByte() != 0;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
