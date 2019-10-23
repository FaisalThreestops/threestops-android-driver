package com.delivx.app.main.support;

import android.app.Activity;
import android.content.Intent;

import com.delivx.app.MyApplication;
import com.delivx.login.LoginActivity;
import com.delivx.login.language.LanguagesList;
import com.delivx.service.LocationUpdateService;
import com.delivx.utility.AppConstants;
import com.google.gson.Gson;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.SupportData;
import com.delivx.pojo.SupportPojo;
import com.delivx.utility.Utility;

import org.json.JSONArray;
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

public class SupportFragPresenter implements SupportContract.PresenterOperation {


    @Inject  NetworkService networkService;
    @Inject  PreferenceHelperDataSource preferenceHelperDataSource;

    private  SupportContract.ViewOperation view;
    @Inject   Activity context;
    @Inject
    SupportFragPresenter() {
    }


    @Override
    public void attachView(SupportContract.ViewOperation view){
        this.view=view;
    }

    /**
     * <h2>supportApi</h2>
     * <p>Invoke API call to get the support details</p>
     */
    private void supportApi() {
        view.showProgress();
        final Observable<Response<ResponseBody>> profile=networkService.support(preferenceHelperDataSource.getLanguage());
        profile.observeOn(AndroidSchedulers.mainThread())
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
                            JSONObject jsonObject = null;

                            switch (value.code()) {
                                //success
                                case 200:
                                    jsonObject=new JSONObject(value.body().string());
                                    SupportPojo supportPojo = new Gson().fromJson(jsonObject.toString(), SupportPojo.class);
                                    view.setSupportDetails(supportPojo.getData());
                                    break;
                                    
                                case 400:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    view.onError(jsonObject.getString("message"));
                                    break;
                                    

                                case 440:
                                case 498:
                                    Utility.printLog("pushTopics shared pref "+preferenceHelperDataSource.getPushTopic());
                                    Utility.subscribeOrUnsubscribeTopics(new JSONArray(preferenceHelperDataSource.getPushTopic()),false);
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    ((MyApplication)context.getApplicationContext()).disconnectMqtt();
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                    if(Utility.isMyServiceRunning(LocationUpdateService.class, context))
                                    {
                                        Intent stopIntent = new Intent(context, LocationUpdateService.class);
                                        stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                                        context.startService(stopIntent);
                                    }

                                    break;
                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    break;
                            }

                            Utility.printLog("supportApi : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("supportApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                        view.onError( context.getResources().getString(R.string.serverError));
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }

    @Override
    public void getSupportLinks() {
        supportApi();
    }

}

