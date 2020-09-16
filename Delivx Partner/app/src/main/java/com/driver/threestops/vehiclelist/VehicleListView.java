package com.driver.threestops.vehiclelist;

import com.driver.threestops.BaseView;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.networking.NetworkService;
import com.driver.threestops.pojo.SigninDriverVehicle;

import java.util.ArrayList;

/**
 * Created by DELL on 04-01-2018.
 */

public interface VehicleListView extends BaseView
{
    void initActionBar();

    void setTitle();

    void setListData(ArrayList<SigninDriverVehicle> listData);

    void notifyAdapter();

    void onError(String error);

    void onSuccess(String result);

    void logout(PreferenceHelperDataSource dataSource, NetworkService networkService);
}
