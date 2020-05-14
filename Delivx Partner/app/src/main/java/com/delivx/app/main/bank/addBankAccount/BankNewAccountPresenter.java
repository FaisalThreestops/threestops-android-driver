package com.delivx.app.main.bank.addBankAccount;

import android.app.Activity;

import com.delivx.app.MyApplication;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.delivx.utility.MyTextUtils;
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


public class BankNewAccountPresenter implements AddBankAccountContract.PresenterOperations {

    @Inject
    public BankNewAccountPresenter() {
    }

    @Inject
    NetworkService networkService;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    AddBankAccountContract.ViewOperation view;

    @Inject
    Activity activity;

    /**
     * <h1>createBankAccount</h1>
     * <p>API call for  create bank account</p>
     * @param name name of the bank holder
     * @param acc account number
     * @param routing routing number
     */
    private void createBankAccount(String name, String acc, String routing){

        view.showProgress();
        final Observable<Response<ResponseBody>> externalAccount=networkService.externalAccount(preferenceHelperDataSource.getLanguage(),
                ((MyApplication) activity.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()),
                preferenceHelperDataSource.getMyEmail(),
                acc,
                routing,
                name,
                preferenceHelperDataSource.getCountry()

        );
        externalAccount.observeOn(AndroidSchedulers.mainThread())
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
                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                            }

                            Utility.printLog("externalAccount : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("externalAccount : Catch :"+e.getMessage());
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

    @Override
    public void validateFields(String name, String acc, String routing) {

        if(MyTextUtils.isEmpty(name)){
            view.setNameError();
            return;
        }
        else if(MyTextUtils.isEmpty(acc)){
            view.setAccError();
            return;
        }
        else if(MyTextUtils.isEmpty(routing)){
            view.setRoutingError();
            return;
        }
        else {
            view.disableError();
            createBankAccount(name,acc,routing);
        }
    }

    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }

    @Override
    public String getAccountNoHint() {
        return preferenceHelperDataSource.getDefaultBankAccount().concat("*");
    }
}
