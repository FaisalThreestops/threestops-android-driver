package com.delivx.app.main.support;

import android.app.Activity;

import com.google.gson.Gson;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.SupportData;
import com.delivx.pojo.SupportPojo;
import com.delivx.utility.Utility;

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

    private SupportFragPresenterImplement presenterImplement;

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
                            JSONObject jsonObject;
                            if(value.code()==200)
                            {
                                jsonObject=new JSONObject(value.body().string());
                                SupportPojo supportPojo = new Gson().fromJson(jsonObject.toString(), SupportPojo.class);
                                view.setSupportDetails(supportPojo.getData());

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onError(jsonObject.getString("message"));
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


    public void onFailure(String failureMsg) {
        presenterImplement.stopProgressBar();
        presenterImplement.onFailure(failureMsg);
    }

    public void onFailure() {
        presenterImplement.stopProgressBar();
        presenterImplement.onFailure();
    }

    public void onSuccess(ArrayList<SupportData> supportDatas) {
        presenterImplement.stopProgressBar();
        presenterImplement.getSupportDetails(supportDatas);
    }

    @Override
    public void getSupportLinks() {
        supportApi();
    }




    interface SupportFragPresenterImplement {

        void stopProgressBar();

        void onFailure(String msg);

        void onFailure();

        void getSupportDetails(ArrayList<SupportData> supportDatas);
    }
}

