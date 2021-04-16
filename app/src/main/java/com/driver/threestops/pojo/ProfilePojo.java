package com.driver.threestops.pojo;

import java.io.Serializable;


public class ProfilePojo implements Serializable {


    private String message;

    private ProfileData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProfileData getData() {
        return data;
    }

    public void setData(ProfileData data) {
        this.data = data;
    }
}
