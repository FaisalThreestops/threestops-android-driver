package com.delivx.ForgotPassword.Changepassword;

import android.app.Activity;
import android.os.Bundle;

import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.networking.NetworkService;
import com.delivx.utility.Utility;

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
 * Created by DELL on 03-01-2018.
 */

public class PresenterChangePass implements ChangePassPresenterContract
{
    @Inject ChangePassView view;

    @Inject Activity context;

    @Inject NetworkService networkService;

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    private String OTP,MobNum,Ccode;

    @Inject
    public PresenterChangePass() {
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
    public void getBundleData(Bundle bundle) {
        OTP = bundle.getString("otp");
        MobNum = bundle.getString("phone");
        Ccode = bundle.getString("countryCode");
    }

    @Override
    public void onOutSideTouch() {
        Utility.hideSoftKeyboard(context);
    }

    @Override
    public void validatePassword(String pass, String confPass) {
        if (pass.matches("") && confPass.matches(""))
        {
            view.showError(context.getResources().getString(R.string.entNewPass));
        }
        else if (pass.matches(""))
        {
            view.showError(context.getResources().getString(R.string.entNewPass));
        }
       /* else if(!pass.matches("^*[a-zA-Z](?=.*\\d).{5,14}$")){
            view.showError(context.getResources().getString(R.string.pass_validation));
        }*/
        else if (confPass.matches(""))
        {
            view.showError(context.getResources().getString(R.string.reentrPass));
        }
        else
        {
            if (!pass.matches(confPass)) {

                view.showError(context.getResources().getString(R.string.passNotMactch));
            } else {
                updatePasswordApi(pass);
            }
        }

    }

    public void updatePasswordApi(String password){

        Observable<Response<ResponseBody>> updatePassword=networkService.password(preferenceHelperDataSource.getLanguage(),MobNum,Ccode,OTP,password);

        updatePassword.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                view.showError( jsonObject.getString("message"));
                                view.startLoginAct();

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.showError( jsonObject.getString("message"));

                            }
                            Utility.printLog("passwordApi : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("passwordApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgress();
                    }
                });
    }
}
