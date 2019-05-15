package com.delivx.pojo;

import java.io.Serializable;

/**
 * Created by DELL on 10-01-2018.
 */

public class Cities implements Serializable
{
    private String cityId;

    private String cityName;

    public boolean isSelected;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
