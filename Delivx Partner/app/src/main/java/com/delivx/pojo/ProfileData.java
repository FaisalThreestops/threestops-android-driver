package com.delivx.pojo;

import java.io.Serializable;

/**
 * Created by embed on 19/5/17.
 */

public class ProfileData implements Serializable {

    private String Name;

    private String planName;

    private String phone;

    private String vehiclePlatNo;

    private String email;

    private String vehicleTypeName;

    private String profilePic;

    private String storeName;

    private String accountType;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehiclePlatNo() {
        return vehiclePlatNo;
    }

    public void setVehiclePlatNo(String vehiclePlatNo) {
        this.vehiclePlatNo = vehiclePlatNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
