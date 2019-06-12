package com.delivx.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ads on 16/05/17.
 */

public class PubnubResponse implements Serializable {

    private String storeName;
    private String customerName;
    private String customerEmail;
    private String customerMobile;
    private String deliveryFee;
    private String estimatedTime;
    private String amount;
    private String currency;
    private String currencySymbol;
    private String mileageMetric;

    private String pickZone;
    private String dropZone;

    @SerializedName("orderDatetime")
    private String dt;
    @SerializedName("deliveryDatetime")
    private String dropDt;
    @SerializedName("orderId")
    private String bid;
    @SerializedName("pickUpAddress")
    private String adr1;
    @SerializedName("deliveryAddress")
    private String drop1;
    private String paymentType;
    private String helpers;
    private int a;
    private long serverTime;
    private String chn;
    @SerializedName("distance")
    private String dis;
    @SerializedName("message")
    private String msg;
    private int ExpiryTimer;
    private int PingId;
    private String storeTypeMsg;

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

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
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

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getHelpers() {
        return helpers;
    }

    public void setHelpers(String helpers) {
        this.helpers = helpers;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getPickZone() {
        return pickZone;
    }

    public void setPickZone(String pickZone) {
        this.pickZone = pickZone;
    }

    public String getDropZone() {
        return dropZone;
    }

    public void setDropZone(String dropZone) {
        this.dropZone = dropZone;
    }

    public String getDropDt() {
        return dropDt;
    }

    public void setDropDt(String dropDt) {
        this.dropDt = dropDt;
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

    public void setAdr1(String adr1) {
        this.adr1 = adr1;
    }

    public String getDrop1() {
        return drop1;
    }

    public void setDrop1(String drop1) {
        this.drop1 = drop1;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getChn() {
        return chn;
    }

    public void setChn(String chn) {
        this.chn = chn;
    }

    public int getExpiryTimer() {
        return ExpiryTimer;
    }

    public void setExpiryTimer(int expiryTimer) {
        ExpiryTimer = expiryTimer;
    }

    public int getPingId() {
        return PingId;
    }

    public void setPingId(int pingId) {
        PingId = pingId;
    }

    public String getStoreTypeMsg() {
        return storeTypeMsg;
    }

    public void setStoreTypeMsg(String storeTypeMsg) {
        this.storeTypeMsg = storeTypeMsg;
    }
}
