package com.delivx.vehiclelist;

/**
 * Created by DELL on 04-01-2018.
 */

public interface VehicleListPresenterContract {

    void setActionBar();

    void setActionBarTitle();

    void getList();

    void confirmOnclick();

    void logoutOnclick();

    String getlanguageCode();
}
