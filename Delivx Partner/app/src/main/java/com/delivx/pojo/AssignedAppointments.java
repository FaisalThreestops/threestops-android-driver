package com.delivx.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ads on 15/05/17.
 */

public class AssignedAppointments implements Serializable {
    private String customerEmail;

    private String customerPhone;

    private String dueDatetime;

    private String currency;

    private String driverId;

    private String totalAmount;

    private String dropLatLng;

    private String driverChn;

    private String bid;

    private String driverName;

    private String customerName;

    private String mileageMetric;

    private String storeName;

    private String pickUpAddress;

    private String paymentType;

    private String driverPhone;

    private String driverPhoto;

    private ArrayList<ShipmentDetails> shipmentDetails;

    private String orderStatus;

    private String deliveryCharge;

    private String pickUpLatLng;

    private String statusMessage;

    private String driverEmail;

    private String currencySymbol;

    private String onWayTime;

    private String orderDatetime;

    private String dropAddress;

    private String pickedupTime;

    private String customerChn;

    private String customerId;

    private String storeTypeMsg;

    private String storeType;

    private String subTotalAmount;

    private boolean isCominigFromStore;

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    private String storePhone;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getDueDatetime() {
        return dueDatetime;
    }

    public void setDueDatetime(String dueDatetime) {
        this.dueDatetime = dueDatetime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDropLatLng() {
        return dropLatLng;
    }

    public void setDropLatLng(String dropLatLng) {
        this.dropLatLng = dropLatLng;
    }

    public String getDriverChn() {
        return driverChn;
    }

    public void setDriverChn(String driverChn) {
        this.driverChn = driverChn;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getPickUpAddress() {
        return pickUpAddress;
    }

    public void setPickUpAddress(String pickUpAddress) {
        this.pickUpAddress = pickUpAddress;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverPhoto() {
        return driverPhoto;
    }

    public void setDriverPhoto(String driverPhoto) {
        this.driverPhoto = driverPhoto;
    }

    public ArrayList<ShipmentDetails> getShipmentDetails() {
        return shipmentDetails;
    }

    public void setShipmentDetails(ArrayList<ShipmentDetails> shipmentDetails) {
        this.shipmentDetails = shipmentDetails;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getPickUpLatLng() {
        return pickUpLatLng;
    }

    public void setPickUpLatLng(String pickUpLatLng) {
        this.pickUpLatLng = pickUpLatLng;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getOnWayTime() {
        return onWayTime;
    }

    public void setOnWayTime(String onWayTime) {
        this.onWayTime = onWayTime;
    }

    public String getOrderDatetime() {
        return orderDatetime;
    }

    public void setOrderDatetime(String orderDatetime) {
        this.orderDatetime = orderDatetime;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    public String getPickedupTime() {
        return pickedupTime;
    }

    public void setPickedupTime(String pickedupTime) {
        this.pickedupTime = pickedupTime;
    }

    public String getCustomerChn() {
        return customerChn;
    }

    public void setCustomerChn(String customerChn) {
        this.customerChn = customerChn;
    }

    public String getStoreTypeMsg() {
        return storeTypeMsg;
    }

    public void setStoreTypeMsg(String storeTypeMsg) {
        this.storeTypeMsg = storeTypeMsg;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public boolean isCominigFromStore() {
        return isCominigFromStore;
    }

    public void setCominigFromStore(boolean cominigFromStore) {
        isCominigFromStore = cominigFromStore;
    }

    public String getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(String subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }
}
