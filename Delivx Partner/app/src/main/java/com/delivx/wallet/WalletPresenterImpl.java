package com.delivx.wallet;

import static com.delivx.utility.AppConstants.WALLET_ACT;

import android.app.Activity;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.delivx.networking.NetworkStateHolder;
import com.delivx.payment.PaymentResponse;
import com.delivx.utility.AppConstants;
import com.delivx.utility.Utility;
import com.driver.delivx.R;
import com.google.gson.Gson;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class WalletPresenterImpl implements WalletPresenter {


    private Activity mActivity;
    @Inject
    WalletView mView;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    @Named(WALLET_ACT)
    CompositeDisposable compositeDisposable;

    @Inject
    NetworkStateHolder networkStateHolder;

    @Inject
    NetworkService service;

    private String currency;

    private double walletAmt=0.0;

    @Inject
    public WalletPresenterImpl(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void callApi() {
        mGetWalletApi();
    }

    @Override
    public void getcards() {
        mGetCard();
    }

    @Override
    public void setAmount(String amt) {
        mView.addAmt(amt);
    }

    @Override
    public String getLanguage() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }

    @Override
    public void chooseCard(String amt,String cardId) {

        if(amt!=null && !amt.equals(""))
        {
             walletAmt=Double.parseDouble(amt);
             mAddMoneyApi(cardId);
        }
    }

    @Override
    public void startHistory() {
        mView.startWalletHistory();
    }

    @Override
    public void addCard()
    {
        mView.startAddCardAct();
    }

    @Override
    public String getCurrency() {
        return preferenceHelperDataSource.getCurrencySymbol();
    }

    @Override
    public void start()
    {
        currency=preferenceHelperDataSource.getCurrencySymbol();

        String amt=mActivity.getString(R.string.Amount)+" ("+currency+")";
        mView.setFixedAmount(amt,"","");

    }

   /* @Override
    public void stop() {
        mView.stopAct();
    }*/

    public boolean isNetworkAvailable() {
        return networkStateHolder.isConnected();
    }


    private void mGetCard()
    {
        mView.showProgress();
        String token=preferenceHelperDataSource.getToken();

        Observable<Response<ResponseBody>> observable= service.getCard(token,preferenceHelperDataSource.getLanguage());
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        mView.hideProgress();
                        Utility.printLog("get card "+responseBodyResponse.code());
                        String message="";
                        try
                        {
                            JSONObject object=new JSONObject(responseBodyResponse.errorBody().string());
                            if(object.has("message"))
                            {
                                message=object.getString("message");
                            }

                        }catch (JSONException|NullPointerException|IOException e)
                        {}
                        switch (responseBodyResponse.code())
                        {
                            case 200:
                                try
                                {
                                    String result=responseBodyResponse.body().string();
                                    JSONObject object=new JSONObject(result);

                                    if(object.has("data") && object.get("data")!=null)
                                    {
                                        Gson gson = new Gson();
                                        PaymentResponse paymentResponse = gson.fromJson(result, PaymentResponse.class);
                                        mView.setPaymentCardsList(paymentResponse.getData().getCards());
                                    }

                                }catch (JSONException |NullPointerException|IOException e)
                                {
                                    e.printStackTrace();
                                }


                                break;

                            case 498:

                                break;

                            case 440:

                                break;
                            case 502:
                                mView.onError(mActivity.getString(R.string.serverError));
                                break;
                            default:
                                break;

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgress();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void mGetWalletApi()
    {
        mView.showProgress();

            Observable<Response<ResponseBody>> bad=service.getWalletApi(preferenceHelperDataSource.getToken(),preferenceHelperDataSource.getLanguage());

            bad.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            mView.hideProgress();

                            String message="";
                            try
                            {
                                JSONObject object=new JSONObject(value.errorBody().string());
                                if(object.has("message"))
                                {
                                    message=object.getString("message");
                                }

                            }catch (JSONException |NullPointerException|IOException e)
                            {}
                            switch (value.code())
                            {
                                case 200:
                                    JSONObject object = null;
                                    try
                                    {
                                        String result=value.body().string();
                                        object=new JSONObject(result);
                                        if(object.has("data")&& object.getJSONObject("data")!=null)
                                        {
                                            AppConstants.WALLET_AVAILABLE=true;

                                            currency=object.getJSONObject("data").getString("currencySymbol");
                                            double walletBalance= Double.parseDouble( object.getJSONObject("data").opt("walletBalance").toString());
                                            AppConstants.WALLET_AMOUNT=Utility.currencyFormat(walletBalance+"");
                                            mView.setTotalBalance(currency+" "+AppConstants.WALLET_AMOUNT);

                                            double softLimit= Double.parseDouble( object.getJSONObject("data").opt("walletSoftLimit").toString());
                                            double hardLimit= Double.parseDouble( object.getJSONObject("data").opt("walletHardLimit").toString());

                                            mView.setLimitAmount(currency+" "+Utility.currencyFormat(softLimit+""),currency+" "+Utility.currencyFormat(hardLimit+""));

                                        }

                                    } catch (ClassCastException e)
                                    {
                                        try {
                                            if(object!= null && object.has("data")&& object.getJSONObject("data")!=null)
                                            {
                                                AppConstants.WALLET_AVAILABLE=true;

                                                double walletBalance=  Double.parseDouble(object.getJSONObject("data").opt("walletBalance").toString());

                                                AppConstants.WALLET_AMOUNT=Utility.currencyFormat(walletBalance+"");
                                                mView.setTotalBalance(currency+" "+AppConstants.WALLET_AMOUNT);

                                                try {
                                                    double softLimit= Double.parseDouble( object.getJSONObject("data").opt("walletSoftLimit").toString());
                                                    double hardLimit= Double.parseDouble( object.getJSONObject("data").opt("walletHardLimit").toString());
                                                    mView.setLimitAmount(currency+" "+Utility.currencyFormat(softLimit+""),currency+" "+Utility.currencyFormat(hardLimit+""));
                                                }catch (ClassCastException e1){
                                                    int softLimit= Integer.parseInt( object.getJSONObject("data").opt("walletSoftLimit").toString());
                                                    int hardLimit= Integer.parseInt( object.getJSONObject("data").opt("walletHardLimit").toString());
                                                    mView.setLimitAmount(currency+" "+Utility.currencyFormat(softLimit+""),currency+" "+Utility.currencyFormat(hardLimit+""));
                                                }

                                            }
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    catch (Exception  e)
                                    {
                                        AppConstants.WALLET_AMOUNT="0.00";

                                        mView.setTotalBalance( currency+" "+AppConstants.WALLET_AMOUNT);

                                        e.printStackTrace();
                                    }

                                    break;

                                case 404:
                                    AppConstants.WALLET_AVAILABLE=true;
                                    AppConstants.WALLET_AMOUNT="0.00";

                                    mView.setTotalBalance(currency+" "+AppConstants.WALLET_AMOUNT);
                                    break;



                                case 498:

                                    break;

                                case 440:

                                    break;


                                default:
                                    break;
                            }

                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            mView.hideProgress();

                            e.printStackTrace();
                        }
                        @Override
                        public void onComplete() {

                        }
                    });
        }



    private void mAddMoneyApi(String cardId)
    {
        if(isNetworkAvailable())
        {
            mView.showProgress();


            Observable<Response<ResponseBody>> bad=service.addWalletApi(preferenceHelperDataSource.getToken(),
                    preferenceHelperDataSource.getLanguage(),cardId,walletAmt);

            bad.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }
                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            mView.hideProgress();

                            String message="";
                            try
                            {
                                JSONObject object=new JSONObject(value.errorBody().string());
                                if(object.has("message"))
                                {

                                    message=object.getString("message");
                                    Utility.printLog("amt message err "+message.toString());

                                }

                            }catch (JSONException |NullPointerException|IOException e)
                            {}
                            switch (value.code())
                            {
                                case 200:
                                    try
                                    {
                                        String result=value.body().string();
                                        JSONObject object=new JSONObject(result);
                                        Utility.printLog("amt added "+object.toString());
                                        callApi();
                                        mView.showSuccessAlert();


                                    } catch (Exception  e)
                                    {
                                        e.printStackTrace();
                                    }

                                    break;

                                case 498:

                                    break;

                                case 440:

                                    break;


                                default:
                                    mView.onError(message);
                                    break;
                            }

                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            mView.hideProgress();

                            e.printStackTrace();
                        }
                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }

}
