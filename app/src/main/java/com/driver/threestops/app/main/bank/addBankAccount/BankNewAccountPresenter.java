package com.driver.threestops.app.main.bank.addBankAccount;

import android.app.Activity;
import android.content.Context;

import com.driver.threestops.app.MyApplication;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.networking.NetworkService;
import com.driver.threestops.utility.TextUtil;
import com.driver.threestops.utility.Utility;
import com.google.gson.Gson;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.driver.threestops.utility.VariableConstant.RESPONSE_CODE_SUCCESS;


public class BankNewAccountPresenter implements BankNewAccountContract.BankNewAccountPresenter {

    @Inject
    BankNewAccountContract.BankNewAccountView view;
    @Inject
    Context context;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    NetworkService networkService;
    @Inject
    Activity activity;

    @Inject
    public BankNewAccountPresenter() {
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
    public void validateData(String name, String phone, String AccountNo,
                             String RoutingNo, String address, String city, String state, String pinCode) {
        if (TextUtil.isEmpty(name)) {
            view.editTextErr("Name");
        } else if (TextUtil.isEmpty(phone)) {
            view.editTextErr("Phone");
        } else if (TextUtil.isEmpty(AccountNo)) {
            view.editTextErr("AccountNo");
        } else if (TextUtil.isEmpty(RoutingNo)) {
            view.editTextErr("RoutingNo");
        } else if (TextUtil.isEmpty(address)) {
            view.editTextErr("Address");
        } else if (TextUtil.isEmpty(city)) {
            view.editTextErr("City");
        } else if (TextUtil.isEmpty(state)) {
            view.editTextErr("State");
        } else if (TextUtil.isEmpty(pinCode)) {
            view.editTextErr("PinCode");
        } else {
            view.editTextErr("default");
        }
    }

    @Override
    public void externalAccountAPI(String name, String Phone, String AccountNo,
                                   String RoutingNo, String address, String city, String state, String pinCode) {
        view.showProgress();
        io.reactivex.Observable<Response<ResponseBody>> postExternalAccount = networkService.bankDetailValidation(
                ((MyApplication) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getDriverID()),
                "en",
                name, Phone, AccountNo, RoutingNo);

        postExternalAccount.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {
                            JSONObject jsonObject;

                            switch (value.code()) {
                                case RESPONSE_CODE_SUCCESS:
                                    jsonObject = new JSONObject(value.body().string());
                                    Utility.printLog("postExternalAccount : " + new Gson().toJson(jsonObject));


                                    addBankAccount(name, Phone, AccountNo, RoutingNo, address, city, state, pinCode);
//                                    bankNewAccountView.externalAccountAPISuccess(jsonObject.getString("message"));
                                    break;

                                default:
                                    Utility.printLog("postExternalAccount : " + value.errorBody().string());
                                    Utility.BlueToast(context, value.errorBody().string());
                                    break;
                            }

                        } catch (Exception e) {
                            Utility.printLog("postExternalAccount : Catch :" + e.getMessage());
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

    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }

    private void addBankAccount(String name, String phone, String accountNo, String routingNo,
                                String address, String city, String state,
                                String pinCode) {
        view.showProgress();
        io.reactivex.Observable<Response<ResponseBody>> postExternalAccount = networkService.addBankDetails(
                ((MyApplication) context.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getDriverID()),
                preferenceHelperDataSource.getLanguage(),
                accountNo, routingNo, address, city, state, pinCode);

        postExternalAccount.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        try {
                            JSONObject jsonObject;

                            switch (value.code()) {
                                case RESPONSE_CODE_SUCCESS:
                                    jsonObject = new JSONObject(value.body().string());
                                    Utility.printLog("postExternalAccount RESPONSE_CODE_SUCCESS addBankDetails: " + new Gson().toJson(jsonObject));

                                    view.externalAccountAPISuccess(jsonObject.getString("message"));
                                    break;
                                case 500:
                                    jsonObject = new JSONObject(value.errorBody().string());
                                    Utility.printLog("postExternalAccount RESPONSE_CODE_INTERNAL_SERVER_ERROR addBankDetails: " + new Gson().toJson(jsonObject));

                                    view.externalAccountAPISuccess(jsonObject.getString("message"));
                                    break;

                                default:
                                    Utility.printLog("postExternalAccount default addBankDetails: " + value.errorBody().string());
                                    Utility.BlueToast(context, value.errorBody().string());
                                    break;
                            }

                        } catch (Exception e) {
                            Utility.printLog("postExternalAccount addBankDetails : Catch :" + e.getMessage());
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
