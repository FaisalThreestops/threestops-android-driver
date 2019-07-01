package com.delivx.pojo;

import java.io.Serializable;


public class VehTypeData implements Serializable {

    private String type_name;

    private String _id;

    private boolean selected;
//    private ArrayList<VehTypeSepecialities> sepecialities;


    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
