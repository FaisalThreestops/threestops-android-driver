package com.driver.threestops.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewBookingMQTTResponse implements Serializable {

    private String storeName;
    private String customerName;
    private String deliveryFee;
    private String driverTip;
    private String amount;
    private String currency;
    private String currencySymbol;
    private String mileageMetric;

    @SerializedName("orderId")
    private String bid;
    @SerializedName("pickUpAddress")
    private String adr1;
    @SerializedName("deliveryAddress")
    private String drop1;
    private String paymentType;
    private int a;
    @SerializedName("distance")
    private String dis;
    @SerializedName("message")
    private String msg;
    private String ExpiryTimer;
    private String storeType;

    private String storeTypeMsg;
    private String dueDatetimeTimeStamp;
    private String orderDateTimeStamp;

    public NewBookingMQTTResponse() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getMileageMetric() {
        return mileageMetric;
    }

    public void setMileageMetric(String mileageMetric) {
        this.mileageMetric = mileageMetric;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public String getDriverTip() {
        return driverTip;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDis() {
        return dis;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getAdr1() {
        return adr1;
    }

    public String getDrop1() {
        return drop1;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public String getExpiryTimer() {
        return ExpiryTimer;
    }

    public String getStoreTypeMsg() {
        return storeTypeMsg;
    }

    public String getDeliveryDatetimeTimeStamp() {
        return dueDatetimeTimeStamp;
    }

    public String getStoreType() {
        return storeType;
    }

    public String getOrderDateTimeStamp() {
        return orderDateTimeStamp;
    }

}
