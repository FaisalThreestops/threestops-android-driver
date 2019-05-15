package com.delivx.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ads on 11/05/17.
 */

public class SigninData {
    private String chn;
    @SerializedName("driverId")
    private String mid;
    private String token;
    private String pub_key;
    private String sub_key;
    private String server_chn;
    private String presence_chn;
    private String publishChn;
    private String code;
    private String name;
    private String profilePic;
    private String pushTopic;
    private String email;
    private ArrayList<SigninDriverVehicle> vehicles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPushTopic() {
        return pushTopic;
    }

    public void setPushTopic(String pushTopic) {
        this.pushTopic = pushTopic;
    }

    public String getPublishChn() {
        return publishChn;
    }

    public void setPublishChn(String publishChn) {
        this.publishChn = publishChn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChn() {
        return chn;
    }

    public void setChn(String chn) {
        this.chn = chn;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPub_key() {
        return pub_key;
    }

    public void setPub_key(String pub_key) {
        this.pub_key = pub_key;
    }

    public String getSub_key() {
        return sub_key;
    }

    public void setSub_key(String sub_key) {
        this.sub_key = sub_key;
    }

    public String getServer_chn() {
        return server_chn;
    }

    public void setServer_chn(String server_chn) {
        this.server_chn = server_chn;
    }

    public String getPresence_chn() {
        return presence_chn;
    }

    public void setPresence_chn(String presence_chn) {
        this.presence_chn = presence_chn;
    }

    public ArrayList<SigninDriverVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<SigninDriverVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public String toString() {
        return "{" +
                "vehicles\":" + vehicles +
                "}";
    }
}
