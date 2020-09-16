package com.driver.threestops.vehiclelist;

import android.app.Activity;

import com.driver.threestops.app.MyApplication;
import com.google.gson.Gson;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.Threestops.R;
import com.driver.threestops.networking.NetworkService;
import com.driver.threestops.pojo.SigninDriverVehicle;
import com.driver.threestops.utility.Utility;
import com.driver.threestops.utility.VariableConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by DELL on 04-01-2018.
 */

public class VehicleListPresenter implements VehicleListPresenterContract {

    @Inject
    VehicleListView view;
    @Inject
    PreferenceHelperDataSource preferenceHelper;
    @Inject
    Activity context;
    @Inject
    NetworkService networkService;

    private ArrayList<SigninDriverVehicle>list=new ArrayList<>();

    @Inject
    public VehicleListPresenter() {
        VariableConstant.VECHICLESELECTED = false;
    }

    @Override
    public void setActionBar() {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        view.setTitle();
    }

    @Override
    public void getList() {

        list.clear();
        JSONArray jsonArray;
        Gson gson = new Gson();
        try {
            jsonArray = new JSONArray(preferenceHelper.getVehicles());

            for (int i = 0; i < jsonArray.length(); i++) {
                SigninDriverVehicle signinDriverVehicle = gson.fromJson(jsonArray.getString(i), SigninDriverVehicle.class);
                list.add(signinDriverVehicle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        view.setListData(list);
        view.notifyAdapter();
    }

    @Override
    public void confirmOnclick() {
        if (VariableConstant.VECHICLESELECTED) {
            setDefaultVehicle();
        } else {
            Utility.BlueToast(context,context.getResources().getString(R.string.selectVechicle));
        }
    }

    @Override
    public void logoutOnclick() {
        view.logout(preferenceHelper,networkService);
    }

    @Override
    public String getlanguageCode() {
        return preferenceHelper.getLanguageSettings().getLanguageCode();
    }


    public void setDefaultVehicle(){
        if(view!=null)
            view.showProgress();

        Observable<Response<ResponseBody>> defaultVehicle=networkService.defaultVehicle(preferenceHelper.getLanguage(),((MyApplication) context.getApplication()).getAuthToken(preferenceHelper.getDriverID()),VariableConstant.VEHICLEID,VariableConstant.VEHICLE_TYPE_ID,"");
        defaultVehicle.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                Utility.printLog("defaultVehicle respone: :"+jsonObject.toString());
                                preferenceHelper.setVehicleId(VariableConstant.VEHICLE_TYPE_ID);
                                view.onSuccess(jsonObject.getString("message"));

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onError(jsonObject.getString("message"));
                            }

                            Utility.printLog("defaultVehicle : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("defaultVehicle : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });

    }

}
