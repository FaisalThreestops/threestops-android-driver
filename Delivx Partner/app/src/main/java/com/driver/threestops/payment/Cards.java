package com.driver.threestops.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Cards implements Serializable {
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("last4")
  @Expose
  private String last4;
  @SerializedName("expYear")
  @Expose
  private Integer expYear;
  @SerializedName("expMonth")
  @Expose
  private Integer expMonth;
  @SerializedName("id")
  @Expose
  private String id;
  @SerializedName("brand")
  @Expose
  private String brand;
  @SerializedName("funding")
  @Expose
  private String funding;
  @SerializedName("isDefault")
  @Expose
  private Boolean isDefault;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLast4() {
    return last4;
  }

  public void setLast4(String last4) {
    this.last4 = last4;
  }

  public Integer getExpYear() {
    return expYear;
  }

  public void setExpYear(Integer expYear) {
    this.expYear = expYear;
  }

  public Integer getExpMonth() {
    return expMonth;
  }

  public void setExpMonth(Integer expMonth) {
    this.expMonth = expMonth;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getFunding() {
    return funding;
  }

  public void setFunding(String funding) {
    this.funding = funding;
  }

  public Boolean getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(Boolean isDefault) {
    this.isDefault = isDefault;
  }
}
