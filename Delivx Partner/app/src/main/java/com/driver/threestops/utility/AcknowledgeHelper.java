package com.driver.threestops.utility;

import android.app.Activity;
import android.content.Context;

import com.driver.threestops.app.MyApplication;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.networking.DispatcherService;


import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AcknowledgeHelper {
    private String TAG = "AcknowledgeHelper";

    PreferenceHelperDataSource preferenceHelperDataSource;
    DispatcherService dispatcherService;

    Context activity;

    @Inject
    public AcknowledgeHelper(PreferenceHelperDataSource dataSource,DispatcherService dispatcherService) {
        preferenceHelperDataSource=dataSource;
        this.dispatcherService=dispatcherService;
    }


    public void  bookingAckApi(String bid, Context context, final AcknowledgementCallback callback){
        activity = context;
        final Observable<Response<ResponseBody>> bookingAck=dispatcherService.bookingAck(preferenceHelperDataSource.getLanguage(), MyApplication.getInstance().getAuthToken(preferenceHelperDataSource.getDriverID()),bid);
        bookingAck.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                callback.callback(jsonObject.getString("message"));

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                            }

                            Utility.printLog("bookingAckApi : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("bookingAckApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }


}