package com.delivx.pojo.TripsPojo;

import java.io.Serializable;



public class TotalEarning implements Serializable {
    private String amt;

    private String date;

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
