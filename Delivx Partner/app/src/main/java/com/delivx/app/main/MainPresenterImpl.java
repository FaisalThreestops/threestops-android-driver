package com.delivx.app.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.delivx.RxObservers.RXMqttMessageObserver;
import com.delivx.RxObservers.RxNetworkObserver;
import com.delivx.app.bookingRequest.BookingPopUp;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.delivx.networking.NetworkStateHolder;
import com.delivx.pojo.GetConfig;
import com.delivx.utility.AcknowledgeHelper;
import com.delivx.utility.AcknowledgementCallback;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by DELL on 29-01-2018.
 */

public class MainPresenterImpl implements MainPresenter {

    @Inject
    AcknowledgeHelper acknowledgeHelper;

    @Inject
    Activity context;

    @Inject
    MainView view;

    @Inject
    PreferenceHelperDataSource helperDataSource;

    @Inject
    NetworkService networkService;

    @Inject
    RxNetworkObserver rxNetworkObserver;

    private String currentVersion;

    @Inject
    public MainPresenterImpl() {
    }

    @Override
    public void getVersion() {
        try {
            currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            view.setVersiontext(currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        RXMqttMessageObserver.getInstance().subscribe(observer);
        rxNetworkObserver.subscribe(networkObserver);
        rxNetworkObserver.subscribeOn(Schedulers.newThread());
        rxNetworkObserver.observeOn(AndroidSchedulers.mainThread());


    }

    @Override
    public void getAppConfig() {
        view.showProgress();

        Observable<Response<ResponseBody>> config = networkService.config(
                helperDataSource.getLanguage(), helperDataSource.getToken());
        config.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if (view != null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if (value.code() == 200) {
                                jsonObject = new JSONObject(value.body().string());
                                Gson gson = new Gson();
                                GetConfig getConfig = gson.fromJson(jsonObject.toString(), GetConfig.class);

                                helperDataSource.setMinDistForRouteArray(getConfig.getData().getDistanceForLogingLatLongs());
                                helperDataSource.setPresenceInterval(getConfig.getData().getPresenceTime());
                                helperDataSource.setTripStartedInterval(getConfig.getData().getTripStartedInterval());

                            } else {
                                jsonObject = new JSONObject(value.errorBody().string());
                                view.setError(value.code(), jsonObject.getString("message"));
                            }

                            Utility.printLog("config auth : " + helperDataSource.getToken());
                            Utility.printLog("config : " + jsonObject.toString());

                        } catch (Exception e) {
                            Utility.printLog("config : Catch :" + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null)
                            view.hideProgress();
                        Utility.printLog("config : onError :" + e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        if (view != null)
                            view.hideProgress();
                    }
                });
    }

    @Override
    public void onDrawerOpen() {
        view.setProfileDetails();
    }


    Observer<JSONObject> observer = new Observer<JSONObject>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(JSONObject value) {
            try {
                if(value.has("code")){
                    getAppConfig();
                }else {
                    switch (value.getInt("action")){
                        case 12:
                            getAppConfig();
//                            view.logout();
                            break;

                        case 11:
                            getBookingDetails(value);
                            break;

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    private void getBookingDetails(final JSONObject jsonObject) {
        try{
            if(helperDataSource.getDriverChannel().equals(jsonObject.getString("chn"))){
                acknowledgeHelper.bookingAckApi(jsonObject.getString("orderId"), new AcknowledgementCallback() {
                    @Override
                    public void callback(String bid) {
                        if(!VariableConstant.IS_POP_UP_OPEN){
                            Intent intent = new Intent(context, BookingPopUp.class);
                            intent.putExtra("booking_Data", jsonObject.toString());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }

                    }


                });
            }

        }catch (Exception e){
            Utility.printLog("MainPresenter"+" "+e.getMessage());
        }
    }

    Observer<NetworkStateHolder> networkObserver=new Observer<NetworkStateHolder>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(NetworkStateHolder data) {

                Utility.printLog("main presenter: "+data.isConnected());
                Utility.printLog("main presenter: "+data.getMessage());

              if(!data.isConnected()){
                 view.networkError("");
              }else {
                 view.dismissDialog();
              }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };



}
