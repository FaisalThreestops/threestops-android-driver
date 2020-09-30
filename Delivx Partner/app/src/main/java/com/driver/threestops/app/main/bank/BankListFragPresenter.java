package com.driver.threestops.app.main.bank;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.driver.threestops.app.MyApplication;
import com.driver.threestops.app.main.bank.stripe.GetBankData;
import com.driver.threestops.app.main.bank.stripe.GetBankDetailsPojo;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.login.LoginActivity;
import com.driver.threestops.login.language.LanguagesList;
import com.driver.threestops.networking.NetworkService;
import com.driver.threestops.service.LocationUpdateService;
import com.driver.threestops.utility.AppConstants;
import com.driver.threestops.utility.Utility;
import com.google.gson.Gson;

import org.json.JSONArray;
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

public class BankListFragPresenter implements BankDetailscontract.PresenterOperations {

    @Inject
    BankListFragPresenter() {
    }

    @Inject
    NetworkService networkService;

    @Inject
    Activity context;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    private GetBankData stripeDetailsPojo;
    private GetBankDetailsPojo stripeResponse;

    BankDetailscontract.ViewOperations view;

    @Override
    public void getBankDetails() {
        getAccount();
    }

    @Override
    public void attachView(BankDetailscontract.ViewOperations bankListFrag) {
        this.view = bankListFrag;
    }

    @Override
    public void addNewAccount() {
        view.moveToAddAccountActivity();
    }

    @Override
    public void addNewStripeAccount() {
        /*Bundle bundleBankDetails=new Bundle();
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

        view.moveToNewStripeActivity(bundleBankDetails);*/
    }

    /**
     * <h2>getAccount</h2>
     * <p>API call for get the bank details</p>
     */
    private void getAccount() {
        view.showProgress();
        final Observable<Response<ResponseBody>> connectAccount = networkService.getBankDetails(
                ((MyApplication) context.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()), preferenceHelperDataSource.getLanguage());
        connectAccount.observeOn(AndroidSchedulers.mainThread())
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
                            JSONObject jsonObject = null;
                            String res = "";
                            switch (value.code()) {
                                //success
                                case 200:
                                    res = value.body().string();
//                                    jsonObject=new JSONObject(value.body().string());
                                    stripeResponse = new Gson().fromJson(res, GetBankDetailsPojo.class);
                                    stripeDetailsPojo = stripeResponse.getData();
                                    view.onSuccessData(stripeDetailsPojo);
                                    //  view.onSuccess(stripeDetailsPojo.getLegal_entity(), stripeDetailsPojo.getExternal_accounts().getData());
                                    break;

                                case 400:
                                    jsonObject = new JSONObject(value.errorBody().string());
                                    view.showAddStipe();
                                    break;

                                case 440:
                                case 498:
                                    Utility.printLog("pushTopics shared pref " + preferenceHelperDataSource.getPushTopic());
                                    Utility.subscribeOrUnsubscribeTopics(new JSONArray(preferenceHelperDataSource.getPushTopic()), false);
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    ((MyApplication) context.getApplicationContext()).disconnectMqtt();
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                    if (Utility.isMyServiceRunning(LocationUpdateService.class, context)) {
                                        Intent stopIntent = new Intent(context, LocationUpdateService.class);
                                        stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                                        context.startService(stopIntent);
                                    }

                                    break;
                                default:
                                    jsonObject = new JSONObject(value.errorBody().string());
                                    break;
                            }

//                            Utility.printLog("connectAccount : "+jsonObject.toString());

                        } catch (JSONException e) {
                            Utility.printLog("connectAccount : Catch :" + e.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null)
                            view.hideProgress();
                        view.onFailure();
                    }

                    @Override
                    public void onComplete() {
                        if (view != null)
                            view.hideProgress();
                    }
                });
    }

    @Override
    public void deleteBankAccount(String beneId) {
//        Utility.printLog(""+preferenceHelperDataSource.getSessionToken());
        view.showProgress();
        io.reactivex.Observable<Response<ResponseBody>> getConnectAccount = networkService.deleteBankAccount(
                ((MyApplication) context.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()), preferenceHelperDataSource.getLanguage(), beneId);

        getConnectAccount.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {
                            JSONObject jsonObject;

                            switch (value.code()) {
                                case 200:
                                    jsonObject = new JSONObject(value.body().string());
                                    Utility.printLog("deleteBankAccount : " + jsonObject.toString());
//                                jsonObject = new JSONObject(jsonObject.getString("data"));
                                    view.showAddBankAccount();
                                    break;

                           /* case RESPONSE_CODE_NO_STRIPE_ACCOUNT_FOUND:
                                jsonObject=new JSONObject(value.errorBody().string());
                                Utility.printLog("deleteBankAccount No Stripe Added: "+value.toString());
//                                    bankDetailsFragView.showAddStipe(jsonObject.getString("message"));
//                                bankDetailsFragView.showAddBankAccount(jsonObject.getString("message"));
                                break;*/

                                default:
//                                    bankDetailsFragView.onFailure(DataParser.fetchErrorMessage(value));
                                    break;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Utility.printLog("deleteBankAccount : Catch :" + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utility.printLog("the api error is : " + e.getMessage());
                        view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgress();
                    }
                });
    }

}
