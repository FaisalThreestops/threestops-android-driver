package com.delivx.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class AssignedTripData implements Serializable {
    private ArrayList<AssignedAppointments> appointments;
    @SerializedName("driverStatus")
    private int MasterStatus;

    public ArrayList<AssignedAppointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<AssignedAppointments> appointments) {
        this.appointments = appointments;
    }

    public int getMasterStatus() {
        return MasterStatus;
    }

    public void setMasterStatus(int masterStatus) {
        MasterStatus = masterStatus;
    }

    @Override
    public String toString() {
        return "AssignedTripData{" +
                "appointments=" + appointments +
                ", MasterStatus=" + MasterStatus +
                '}';
    }
}
