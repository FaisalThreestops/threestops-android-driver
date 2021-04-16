package com.driver.threestops.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class AddOns implements Serializable{

    private String id;

    private String packId;

    private ArrayList<AddOnGroup> addOnGroup;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPackId ()
    {
        return packId;
    }

    public void setPackId (String packId)
    {
        this.packId = packId;
    }

    public ArrayList<AddOnGroup> getAddOnGroup ()
    {
        return addOnGroup;
    }

    public void setAddOnGroup (ArrayList<AddOnGroup> addOnGroup)
    {
        this.addOnGroup = addOnGroup;
    }

    @Override
    public String toString()
    {
        String addOns=addOnGroup.toString();
        return addOns.length()>0?addOns.replace("[","").replace("]",""):"";
    }
}
