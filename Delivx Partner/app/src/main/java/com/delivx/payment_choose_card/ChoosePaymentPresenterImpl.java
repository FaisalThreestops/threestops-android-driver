package com.delivx.payment_choose_card;



import static com.delivx.utility.AppConstants.CHOOSEPAYMENT_ACT;

import android.app.Activity;

import com.delivx.app.MyApplication;
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

public class ChoosePaymentPresenterImpl implements ChoosePaymentPresenter
{

    @Inject ChoosePaymentView mView;
    @Inject Activity mActivity;
    @Inject
    PreferenceHelperDataSource mPreferenceHelperDataSource;
    @Inject
    NetworkService service;
    @Inject
    NetworkStateHolder networkStateHolder;
    @Inject
    @Named(CHOOSEPAYMENT_ACT)
    CompositeDisposable compositeDisposable;

    @Inject
    public ChoosePaymentPresenterImpl() {
    }

    @Override
    public void getCards()
    {
        if(isNetworkAvailable())
        {
            mView.setWalletAmount(mPreferenceHelperDataSource.getCurrencySymbol()+" "+ AppConstants.WALLET_AMOUNT);
            mGetCard();
            mGetWalletApi();

        }else
        {
            mView.onError(mActivity.getString(R.string.networkslow));
        }
    }

    @Override
    public void showCashBtn()
    {
     mView.showCashBtn();
    }

    @Override
    public void hideCashBtn()
    {
        mView.hideCashBtn();

    }

    @Override
    public void addNewCard() {

        mView.startAddCardAct();

    }

    @Override
    public String getLanguage() {
        return mPreferenceHelperDataSource.getLanguage();
    }

    @Override
    public void showWalletPayment() {
        mView.showWalletPayment();
    }

    @Override
    public void payWallet()
    {

        if(AppConstants.WALLET_AMOUNT!=null && !AppConstants.WALLET_AMOUNT.equals("0.00") )
      {
          mView.payWallet(mPreferenceHelperDataSource.getCurrencySymbol()+" "+AppConstants.WALLET_AMOUNT);

      }else
      {
          mView.onError(mActivity.getString(R.string.insufficientBal));
      }
    }

    @Override
    public void addMoneyWallet() {
        mView.addWallet();
    }



    public boolean isNetworkAvailable() {
        return networkStateHolder.isConnected();
    }

    private void mGetCard()
    {
        mView.showProgress();
        String token= ((MyApplication) mActivity.getApplication()).getAuthToken(mPreferenceHelperDataSource.getDriverID());

        Observable<Response<ResponseBody>> observable= service.getCard(token,mPreferenceHelperDataSource.getLanguage());
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

                         /*   case 498:
                                SessionTokenExpObservable.getInstance().emit(true,message);
                                break;

                            case 440:
                                SessionTokenExpObservable.getInstance().emit(false,message);
                                break;*/
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
        if(isNetworkAvailable())
        {

            Observable<Response<ResponseBody>> bad=service.getWalletApi( ((MyApplication) mActivity.getApplication()).getAuthToken(mPreferenceHelperDataSource.getDriverID()),mPreferenceHelperDataSource.getLanguage());

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
                                    JSONObject object=null;
                                    try
                                    {
                                        String result=value.body().string();
                                         object=new JSONObject(result);
                                        if(object.has("data")&& object.getJSONObject("data")!=null)
                                        {
                                            AppConstants.WALLET_AVAILABLE=true;

                                            int walletBalance= (int) object.getJSONObject("data").opt("walletBalance");
                                            if (walletBalance == 0) {
                                                mView.disableWalletPay();
                                            } else {
                                                mView.enableWalletPay();
                                            }
                                            AppConstants.WALLET_AMOUNT=Utility.currencyFormat(walletBalance+"");
                                            mView.setWalletAmount(mPreferenceHelperDataSource.getCurrencySymbol()+" "+AppConstants.WALLET_AMOUNT);
                                        }

                                    } catch (ClassCastException e)
                                    {
                                        try {
                                            if(object!= null && object.has("data")&& object.getJSONObject("data")!=null)
                                            {
                                                AppConstants.WALLET_AVAILABLE=true;

                                                double walletBalance= (double) object.getJSONObject("data").opt("walletBalance");
                                                if (walletBalance == 0.0) {
                                                    mView.disableWalletPay();
                                                } else {
                                                    mView.enableWalletPay();
                                                }
                                                AppConstants.WALLET_AMOUNT=Utility.currencyFormat(walletBalance+"");
                                                mView.setWalletAmount(mPreferenceHelperDataSource.getCurrencySymbol()+" "+AppConstants.WALLET_AMOUNT);

                                            }
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    catch (Exception  e)
                                    {
                                        AppConstants.WALLET_AMOUNT="0.00";
                                        mView.setWalletAmount(mPreferenceHelperDataSource.getCurrencySymbol()+" "+AppConstants.WALLET_AMOUNT);
                                        e.printStackTrace();
                                    }

                                    break;

                                case 404:
                                    AppConstants.WALLET_AVAILABLE=true;
                                    AppConstants.WALLET_AMOUNT="0.00";
                                    mView.setWalletAmount(mPreferenceHelperDataSource.getCurrencySymbol()+" "+AppConstants.WALLET_AMOUNT);

                                    break;



                               /* case 498:
                                    SessionTokenExpObservable.getInstance().emit(true,message);
                                    break;

                                case 440:
                                    SessionTokenExpObservable.getInstance().emit(false,message);
                                    break;*/


                                default:
                                    break;
                            }

                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            e.printStackTrace();
                        }
                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }

    public boolean canSelectWallet()
    {
        if(AppConstants.WALLET_AMOUNT!=null && !"".equals(AppConstants.WALLET_AMOUNT))
        {
            double amount=Double.parseDouble(AppConstants.WALLET_AMOUNT);
            if(amount<=0)
                return false;
            else
                return true;

        }
        else
            return false;
    }
}
