package com.delivx.pojo;

import java.io.Serializable;
import java.util.ArrayList;


public class SupportData implements Serializable {
    private String name;

    private String desc;

    private ArrayList<SupportData> subcat;

    private String link = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

   /* public ArrayList<SupportSubCat> getSubcat() {
        return subcat;
    }

    public void setSubcat(ArrayList<SupportSubCat> subcat) {
        this.subcat = subcat;
    }*/

    public ArrayList<SupportData> getSubcat() {
        return subcat;
    }

    public void setSubcat(ArrayList<SupportData> subcat) {
        this.subcat = subcat;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
