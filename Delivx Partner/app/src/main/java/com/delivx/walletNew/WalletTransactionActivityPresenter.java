package com.delivx.walletNew;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.delivx.app.MyApplication;
import com.delivx.utility.VariableConstant;
import com.driver.Threestops.R;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.delivx.networking.NetworkStateHolder;
import com.delivx.utility.Utility;
import com.delivx.walletNew.model.WalletTransDataPojo;
import com.google.gson.Gson;

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

public class WalletTransactionActivityPresenter implements WalletTransactionContract.WalletTransactionPresenter
{
    private final String TAG = "WalletTransProvider";

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject Context mContext;
    @Inject WalletTransactionContract.WalletTrasactionView trasactionView;
    @Inject NetworkService networkService;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject Activity activity;

    Gson gson;

    @Inject
    WalletTransactionActivityPresenter()
    {
    }

    /**
     * <h2>initLoadTransactions</h2>
     * <p> method to init the getTransactionsHistory() api call if network connectivity is there </p>
     * @param isToLoadMore: true if is from load more option
     * @param isFromOnRefresh: true if it is to refresh
     */
    public void initLoadTransactions(boolean isToLoadMore, boolean isFromOnRefresh)
    {
        if(!isFromOnRefresh)
        {
            getTransactionHistory();
        }
    }

    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }


    @Override
    public void showToastNotifier(String msg, int duration)
    {
        trasactionView.showToast(msg, duration);
    }


    /**
     * <h>get Wallet History</h>
     * <p>this method is using to get the Wallet history data</p>
     */
    private void getTransactionHistory()
    {

        trasactionView.showProgressDialog(mContext.getString(R.string.pleaseWait));
        Observable<Response<ResponseBody>> request = networkService.getWalletTransaction(
                ((MyApplication) activity.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()),
                preferenceHelperDataSource.getLanguage(),"0");

        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>()
                {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value)
                    {
                        switch (value.code())
                        {
                            case 200:
                                String response= null;
                                try {
                                    response = value.body().string();
                                    JSONObject jsonObject=new JSONObject(response);
                                    handleResponse(jsonObject.getString("data"));
                                    Utility.printLog(TAG+ " getWalletTrans onNext: "+jsonObject.toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Utility.printLog(TAG+ "getWalletTrans error: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        trasactionView.hideProgressDialog();

                    }
                });
    }


    /**
     * <h>Response Handler</h>
     * <p>this method is using to  handle the Server Response</p>
     * @param response server response
     */
    private void handleResponse(String response)
    {
        try {
            gson=new Gson();
            WalletTransDataPojo walletTransDataPojo = gson.fromJson(response, WalletTransDataPojo.class);
            trasactionView.setAllTransactionsAL(walletTransDataPojo.getCreditDebitTransctions());
            trasactionView.setWallet(walletTransDataPojo.getWalletBalance(),walletTransDataPojo.getCurrencySymbol());
            trasactionView.setCreditTransactionsAL(walletTransDataPojo.getCreditTransctions());
            trasactionView.setDebitTransactionsAL(walletTransDataPojo.getDebitTransctions());
            trasactionView.walletTransactionsApiSuccessViewNotifier();
        }catch (ArithmeticException e){

        }
    }
}