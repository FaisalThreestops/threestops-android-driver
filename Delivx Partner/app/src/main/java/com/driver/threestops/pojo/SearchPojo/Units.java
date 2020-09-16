package com.driver.threestops.pojo.SearchPojo;

import java.util.ArrayList;

public class Units {
    private String availableQuantity;

    private ArrayList<String> addOns;

    private String name;

    private String unitId;

    private String floatValue;

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public ArrayList<String> getAddOns() {
        return addOns;
    }

    public void setAddOns(ArrayList<String> addOns) {
        this.addOns = addOns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(String floatValue) {
        this.floatValue = floatValue;
    }

    @Override
    public String toString() {
        return "ClassPojo [availableQuantity = " + availableQuantity + ", addOns = " + addOns + ", name = " + name + ", unitId = " + unitId + ", floatValue = " + floatValue + "]";
    }
}
