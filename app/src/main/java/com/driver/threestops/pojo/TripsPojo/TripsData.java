package com.driver.threestops.pojo.TripsPojo;

import java.io.Serializable;
import java.util.ArrayList;


public class TripsData implements Serializable {

    private ArrayList<Appointments> orders;

    private ArrayList<TotalEarning> totalEarning;

    public ArrayList<Appointments> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Appointments> orders) {
        this.orders = orders;
    }

    public ArrayList<TotalEarning> getTotalEarnings() {
        return totalEarning;
    }

    public void setTotalEarnings(ArrayList<TotalEarning> totalEarnings) {
        this.totalEarning = totalEarnings;
    }
}
