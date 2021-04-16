package com.driver.threestops.pojo.TripsPojo;

import java.io.Serializable;
import java.util.ArrayList;



public class Appointments implements Serializable {

    private String dropLat;

    private String timeStamp;

    private String pickupLat;

    private String appCommission;

    private String customerPhone;

    private String currency;

    private String statusCode;

    private String totalAmount;

    private String signatureUrl;

    private String pickAddress;

    private String pickupLong;

    private String driverEarning;

    private String orderId;

    private String cancellationFee;

    private String customerName;

    private String mileageMetric;

    private String storeTypeMsg;



    private String subTotalAmount;

    private String bookingDate;

    private String storeName;

    private String deliveredTime;

    private String paymentType;

    private String storeAddress;

    private String earnedAmount;

    private String dropLong;

    private String currencySymbol;

    private ArrayList<ShipmentDetails> items;

    private String rating;

    private String distanceDriver;

    private String dropAddress;

    private String pickedupTime;

    private String tax;

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    private String storeType;

    private String storeEarning;

    private String storeLogo;

    private String paidByCard;

    private String paidByCash;

    private String paidByWallet;

    private int journeyStartToEndTime;

    public String getPaidByCard() {
        return paidByCard;
    }

    public void setPaidByCard(String paidByCard) {
        this.paidByCard = paidByCard;
    }

    public String getPaidByCash() {
        return paidByCash;
    }

    public void setPaidByCash(String paidByCash) {
        this.paidByCash = paidByCash;
    }

    public String getPaidByWallet() {
        return paidByWallet;
    }

    public void setPaidByWallet(String paidByWallet) {
        this.paidByWallet = paidByWallet;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    private String deliveryCharge;

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getStoreEarning() {
        return storeEarning;
    }

    public void setStoreEarning(String storeEarning) {
        this.storeEarning = storeEarning;
    }

    public String getExcTax() {
        return excTax;
    }

    public void setExcTax(String excTax) {
        this.excTax = excTax;
    }

    private String excTax;

    public String getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(String subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }

    public String getDropLat() {
        return dropLat;
    }

    public void setDropLat(String dropLat) {
        this.dropLat = dropLat;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public String getAppCommission() {
        return appCommission;
    }

    public void setAppCommission(String appCommission) {
        this.appCommission = appCommission;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    public String getPickAddress() {
        return pickAddress;
    }

    public void setPickAddress(String pickAddress) {
        this.pickAddress = pickAddress;
    }

    public String getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(String pickupLong) {
        this.pickupLong = pickupLong;
    }

    public String getDriverEarning() {
        return driverEarning;
    }

    public void setDriverEarning(String driverEarning) {
        this.driverEarning = driverEarning;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(String cancellationFee) {
        this.cancellationFee = cancellationFee;
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

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDeliveredTime() {
        return deliveredTime;
    }

    public void setDeliveredTime(String deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getEarnedAmount() {
        return earnedAmount;
    }

    public void setEarnedAmount(String earnedAmount) {
        this.earnedAmount = earnedAmount;
    }

    public String getDropLong() {
        return dropLong;
    }

    public void setDropLong(String dropLong) {
        this.dropLong = dropLong;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public ArrayList<ShipmentDetails> getItems() {
        return items;
    }

    public void setItems(ArrayList<ShipmentDetails> items) {
        this.items = items;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDistanceDriver() {
        return distanceDriver;
    }

    public void setDistanceDriver(String distanceDriver) {
        this.distanceDriver = distanceDriver;
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

    public String getStoreTypeMsg() {
        return storeTypeMsg;
    }

    public void setStoreTypeMsg(String storeTypeMsg) {
        this.storeTypeMsg = storeTypeMsg;
    }

    public int getJourneyStartToEndTime() {
        return journeyStartToEndTime;
    }

    public void setJourneyStartToEndTime(int journeyStartToEndTime) {
        this.journeyStartToEndTime = journeyStartToEndTime;
    }
}
