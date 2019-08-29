package com.delivx.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class AssignedTripData implements Serializable {
    private ArrayList<AssignedAppointments> appointments;
    @SerializedName("driverStatus")
    private int MasterStatus;
    private ArrayList<ShipmentDetails> Items;

    private String subTotalAmount;
    private String totalAmount;
    private String deliveryCharge;
    private String discount;
    //
    private String catName;
    private String mileageMetric;
    private String currencySymbol;
    private String currency;
    private String cityId;

    public ArrayList<AssignedAppointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<AssignedAppointments> appointments) {
        this.appointments = appointments;
    }

    public int getMasterStatus() {
        return MasterStatus;
    }

    public void setMasterStatus(int masterStatus) {
        MasterStatus = masterStatus;
    }

    public ArrayList<ShipmentDetails> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ShipmentDetails> items) {
        Items = items;
    }

    public String getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(String subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }


    private String storeId;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getMileageMetric() {
        return mileageMetric;
    }

    public void setMileageMetric(String mileageMetric) {
        this.mileageMetric = mileageMetric;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "AssignedTripData{" +
                "appointments=" + appointments +
                ", MasterStatus=" + MasterStatus +
                '}';
    }
}
