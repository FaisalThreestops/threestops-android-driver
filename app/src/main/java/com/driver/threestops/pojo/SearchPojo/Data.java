package com.driver.threestops.pojo.SearchPojo;

import java.util.ArrayList;

public class Data {
    private String detailedDescription;

    private String parentProductId;

    private String productId;

    private ArrayList<String> taxes;

    private String shortDescription;

    private ArrayList<Units> units;

    private String storeId;

    private String productName;

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getParentProductId() {
        return parentProductId;
    }

    public void setParentProductId(String parentProductId) {
        this.parentProductId = parentProductId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ArrayList<String> getTaxes() {
        return taxes;
    }

    public void setTaxes(ArrayList<String> taxes) {
        this.taxes = taxes;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public ArrayList<Units> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Units> units) {
        this.units = units;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "ClassPojo [detailedDescription = " + detailedDescription + ", parentProductId = " + parentProductId + ", productId = " + productId + ", taxes = " + taxes + ", shortDescription = " + shortDescription + ", units = " + units + ", storeId = " + storeId + ", productName = " + productName + "]";
    }
}
