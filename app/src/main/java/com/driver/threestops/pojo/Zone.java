package com.driver.threestops.pojo;

import java.io.Serializable;

public class Zone implements Serializable{

    private String mileageMetric;

    private String title;

    private String _id;

    private String currencySymbol;

    private String weightMetric;

    private String city_ID;

    private String currency;

    private String city;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getMileageMetric ()
    {
        return mileageMetric;
    }

    public void setMileageMetric (String mileageMetric)
    {
        this.mileageMetric = mileageMetric;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getCurrencySymbol ()
    {
        return currencySymbol;
    }

    public void setCurrencySymbol (String currencySymbol)
    {
        this.currencySymbol = currencySymbol;
    }

    public String getWeightMetric ()
    {
        return weightMetric;
    }

    public void setWeightMetric (String weightMetric)
    {
        this.weightMetric = weightMetric;
    }

    public String getCity_ID ()
    {
        return city_ID;
    }

    public void setCity_ID (String city_ID)
    {
        this.city_ID = city_ID;
    }

    public String getCurrency ()
    {
        return currency;
    }

    public void setCurrency (String currency)
    {
        this.currency = currency;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [mileageMetric = "+mileageMetric+", title = "+title+", _id = "+_id+", currencySymbol = "+currencySymbol+", weightMetric = "+weightMetric+", city_ID = "+city_ID+", currency = "+currency+", city = "+city+"]";
    }
}
