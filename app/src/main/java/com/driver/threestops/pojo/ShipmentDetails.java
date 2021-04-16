package com.driver.threestops.pojo;

import java.io.Serializable;
import java.util.ArrayList;


public class ShipmentDetails implements Serializable {

    private String childProductId;

    private String itemImageURL;

    private String parentProductId;

    private String itemName;

    private String appliedDiscount;

    private String status;

    private String unitName;

    private String unitPrice;

    private String quantity;

    private String finalPrice;

    private String offerId;

    private String unitId;

    private ArrayList<AddOns> addOns;


    /**/
    private String productId;

    private String addedToCartOn;

    private String catName;

    private String mileageMetric;

    private String currencySymbol;

    private String currency;

    private String cityId;



    public ArrayList<AddOns> getAddOns() {
        return addOns;
    }

    public void setAddOns(ArrayList<AddOns> addOns) {
        this.addOns = addOns;
    }

    public String getChildProductId() {
        return childProductId;
    }

    public void setChildProductId(String childProductId) {
        this.childProductId = childProductId;
    }

    public String getItemImageURL() {
        return itemImageURL;
    }

    public void setItemImageURL(String itemImageURL) {
        this.itemImageURL = itemImageURL;
    }

    public String getParentProductId() {
        return parentProductId;
    }

    public void setParentProductId(String parentProductId) {
        this.parentProductId = parentProductId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAppliedDiscount() {
        return appliedDiscount;
    }

    public void setAppliedDiscount(String appliedDiscount) {
        this.appliedDiscount = appliedDiscount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAddedToCartOn() {
        return addedToCartOn;
    }

    public void setAddedToCartOn(String addedToCartOn) {
        this.addedToCartOn = addedToCartOn;
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
}
