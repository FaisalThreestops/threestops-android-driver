package com.delivx.vehiclelist;

import com.delivx.BaseView;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.SigninDriverVehicle;

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
