package com.delivx.app.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.util.Log;

import com.delivx.app.MyApplication;
import com.delivx.login.language.LanguagesList;
import com.delivx.login.language.LanguagesPojo;
import com.delivx.networking.LanguageApiService;
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

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;



public class MainPresenterImpl implements MainPresenter {

    @Inject  AcknowledgeHelper acknowledgeHelper;
    @Inject  Activity context;
    @Inject  MainView view;
    @Inject  PreferenceHelperDataSource helperDataSource;
    @Inject  NetworkService networkService;
    @Inject  RxNetworkObserver rxNetworkObserver;
    @Inject  NetworkStateHolder networkStateHolder;
    private ArrayList<LanguagesList> languagesLists = new ArrayList<>();
    @Inject LanguageApiService languageApiService;


    @Inject
    MainPresenterImpl() {
    }

    @Override
    public void getVersion() {
        try {
            String currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            view.setVersiontext(currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        RXMqttMessageObserver.getInstance().subscribe(observer);
    }

    @Override
    public void getAppConfig() {
        view.showProgress();

        String token=((MyApplication) context.getApplication()).getAuthToken(helperDataSource.getDriverID());
        Utility.printLog("qkey " + ((MyApplication) context.getApplication()).getAuthToken(helperDataSource.getDriverID()));

        Observable<Response<ResponseBody>> config = networkService.config(
                helperDataSource.getLanguage(), ((MyApplication) context.getApplication()).getAuthToken(helperDataSource.getDriverID()));
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
                                String resp = value.body().string();
                                Utility.printLog("config resp  :"+resp);
                                Gson gson = new Gson();
                                GetConfig getConfig = gson.fromJson(resp, GetConfig.class);
                                helperDataSource.setMinDistForRouteArray(getConfig.getData().getDistanceForLogingLatLongs());
                                helperDataSource.setMaxDistForRouteArray(getConfig.getData().getDistanceForLogingLatLongsMax());
                                helperDataSource.setPresenceInterval(getConfig.getData().getPresenceTime());
                                helperDataSource.setTripStartedInterval(getConfig.getData().getTripStartedInterval());

                            } else {
                                jsonObject = new JSONObject(value.errorBody().string());
                                view.setError(value.code(), jsonObject.getString("message"));
                            }

                            Utility.printLog("config auth : " + ((MyApplication) context.getApplication()).getAuthToken(helperDataSource.getDriverID()));


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



    @Override
    public void checkForNetwork(boolean isConnected) {
        Utility.printLog("the internet check mainActivity : "+isConnected);
        networkStateHolder.setConnected(isConnected);
        rxNetworkObserver.publishData(networkStateHolder);

        if(isConnected)
            view.networkAvailable();
        else
            view.networkNotAvailable();
    }

    @Override
    public String getlanguageCode() {
        return helperDataSource.getLanguageSettings().getLanguageCode() ;
    }


    @Override
    public void subscribeNetworkObserver() {
        checkForNetwork(networkStateHolder.isConnected());
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

    /**
     * <h2>getBookingDetails</h2>
     * <p>get the booking details</p>
     * @param jsonObject: get the status
     */
    private void getBookingDetails(final JSONObject jsonObject) {
        try{
            if(helperDataSource.getDriverChannel().equals(jsonObject.getString("chn"))){
                acknowledgeHelper.bookingAckApi(jsonObject.getString("orderId"),context, new AcknowledgementCallback() {
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



    @Override
    public void getLanguages() {

        if(Utility.isNetworkAvailable(context)) {
            view.showProgress();

            Observable<Response<ResponseBody>> getLanguages = languageApiService.getLanguage();

            getLanguages.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Response<ResponseBody>>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            view.hideProgress();

                            try {
                                switch (value.code()) {
                                    case VariableConstant.RESPONSE_CODE_SUCCESS:
                                        String res = value.body().string();
                                        Utility.printLog("language res : "+res);

                                        Gson gson = new Gson();
                                        LanguagesPojo languagesListModel = gson.fromJson(res,LanguagesPojo.class);
                                        Utility.printLog("language res : "+languagesListModel.getData().size());
                                        languagesLists.clear();
                                        languagesLists.addAll(languagesListModel.getData());
                                        boolean isLanguage = false;
                                        for(LanguagesList languagesList : languagesLists)
                                        {
                                            if(helperDataSource.getLanguageSettings().getLanguageCode().equals(languagesList.getLanguageCode()))
                                            {
                                                isLanguage = true;
                                                view.setLanguageDialog( languagesListModel.getData(),languagesLists.indexOf(languagesList));
                                                break;
                                            }
                                        }
                                        if(!isLanguage)
                                            view.setLanguageDialog(null,-1);

                                        break;

                                    default:
                                        break;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Utility.printLog("getLanguages : Catch :" + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.hideProgress();
                            Utility.printLog("getLanguages : onError :" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {

        }

    }

    @Override
    public void languageChanged(String langCode, String langName) {
        helperDataSource.setLanguage(langCode);
        helperDataSource.setLanguageSettings(new LanguagesList(langCode,langName));
        view.setLanguageSuccess();
    }

    @Override
    public void getBundleData(Intent intent) {
        try {
            if (intent != null) {
                String response = intent.getExtras().getString("booking_Data");
                Log.d("response", "getBundleData: MainPresenter " + response);
                if (response != null)
                    view.bookingPopUp(response);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
