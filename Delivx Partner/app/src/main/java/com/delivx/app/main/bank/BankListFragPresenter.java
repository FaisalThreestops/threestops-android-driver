package com.delivx.app.main.bank;


import android.os.Bundle;

import com.google.gson.Gson;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;

import com.delivx.pojo.bank.StripeDetailsPojo;
import com.delivx.pojo.bank.StripeResponse;
import com.delivx.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;



public class BankListFragPresenter implements BankDetailscontract.PresenterOperations  {

    @Inject
    public BankListFragPresenter() {
    }

    @Inject
    NetworkService networkService;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    private StripeDetailsPojo stripeDetailsPojo;
    private StripeResponse stripeResponse;

    BankDetailscontract.ViewOperations view;

    void getBankDetails() {
        getAccount();
    }

    @Override
    public void attachView(BankDetailscontract.ViewOperations bankListFrag) {
        this.view=bankListFrag;
    }

    @Override
    public void addNewAccount() {
        view.moveToAddAccountActivity();
    }

    @Override
    public void addNewStripeAccount() {
        Bundle bundleBankDetails=new Bundle();
        if(stripeDetailsPojo!=null){
            bundleBankDetails.putString("fname", stripeDetailsPojo.getLegal_entity().getFirstName());
            bundleBankDetails.putString("lname", stripeDetailsPojo.getLegal_entity().getLastName());
            bundleBankDetails.putString("country", stripeDetailsPojo.getLegal_entity().getAddress().getCountry());
            bundleBankDetails.putString("state", stripeDetailsPojo.getLegal_entity().getAddress().getState());
            bundleBankDetails.putString("city", stripeDetailsPojo.getLegal_entity().getAddress().getCity());
            bundleBankDetails.putString("address", stripeDetailsPojo.getLegal_entity().getAddress().getLine1());
            bundleBankDetails.putString("postalcode", stripeDetailsPojo.getLegal_entity().getAddress().getPostalCode());
            bundleBankDetails.putString("month", stripeDetailsPojo.getLegal_entity().getDob().getMonth());
            bundleBankDetails.putString("day", stripeDetailsPojo.getLegal_entity().getDob().getDay());
            bundleBankDetails.putString("year", stripeDetailsPojo.getLegal_entity().getDob().getYear());
        }

        view.moveToNewStripeActivity(bundleBankDetails);
    }


    private void getAccount() {
        view.showProgress();
        final Observable<Response<ResponseBody>> connectAccount=networkService.connectAccount(preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken());
        connectAccount.observeOn(AndroidSchedulers.mainThread())
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
                                stripeResponse = new Gson().fromJson(jsonObject.toString(), StripeResponse.class);
                                stripeDetailsPojo=stripeResponse.getData();
                                Utility.printLog("connectAccount : "+jsonObject.toString());

                                view.onSuccess(stripeDetailsPojo.getLegal_entity(), stripeDetailsPojo.getExternal_accounts().getData());

                            }else if(value.code()==400)
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.showAddStipe();
                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onFailure(jsonObject.getString("message"));
                            }

                            Utility.printLog("connectAccount : "+jsonObject.toString());

                        }catch (JSONException e)
                        {
                            Utility.printLog("connectAccount : Catch :"+e.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                        view.onFailure();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }

}
