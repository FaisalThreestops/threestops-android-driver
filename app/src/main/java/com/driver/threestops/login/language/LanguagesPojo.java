package com.driver.threestops.login.language;

import java.io.Serializable;
import java.util.ArrayList;

public class LanguagesPojo implements Serializable{

    private String message;

    private ArrayList<LanguagesList> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<LanguagesList> getData() {
        return data;
    }

    public void setData(ArrayList<LanguagesList> data) {
        this.data = data;
    }
}
