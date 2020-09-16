package com.driver.threestops.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceZoneList implements Serializable {

    private ArrayList<String> serviceZones;

    public ArrayList<String> getServiceZones() {
        return serviceZones;
    }

    public void setServiceZones(ArrayList<String> serviceZones) {
        this.serviceZones = serviceZones;
    }
}
