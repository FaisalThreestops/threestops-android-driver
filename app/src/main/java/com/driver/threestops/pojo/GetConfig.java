package com.driver.threestops.pojo;

import java.io.Serializable;


public class GetConfig implements Serializable {

    private String message;

    private ConfigData data;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConfigData getData() {
        return data;
    }

    public void setData(ConfigData data) {
        this.data = data;
    }
}
