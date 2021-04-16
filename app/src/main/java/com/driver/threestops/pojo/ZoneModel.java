package com.driver.threestops.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class ZoneModel implements Serializable{

    private String message;

    private ArrayList<Zone> data;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public ArrayList<Zone> getData ()
    {
        return data;
    }

    public void setData (ArrayList<Zone> data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", data = "+data+"]";
    }
}
