package com.driver.threestops.pojo;

import java.io.Serializable;

public class ExclusiveTaxes implements Serializable {

    private String taxId;

    private String taxtName;

    private String taxFlagMsg;

    private String taxValue;

    private String taxCode;

    private String price;


    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getTaxtName() {
        return taxtName;
    }

    public void setTaxtName(String taxtName) {
        this.taxtName = taxtName;
    }

    public String getTaxFlagMsg() {
        return taxFlagMsg;
    }

    public void setTaxFlagMsg(String taxFlagMsg) {
        this.taxFlagMsg = taxFlagMsg;
    }

    public String getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(String taxValue) {
        this.taxValue = taxValue;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
