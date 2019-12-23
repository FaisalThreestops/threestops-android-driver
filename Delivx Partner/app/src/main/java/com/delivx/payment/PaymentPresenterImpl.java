package com.delivx.payment;

import static com.delivx.utility.AppConstants.PAYMENT_ACT;

import android.app.Activity;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.delivx.networking.NetworkStateHolder;
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

public class PaymentPresenterImpl implements PaymentPresenter {
    @Inject
    PaymentView mView;

    private Activity mActivity;

    @Inject
    PreferenceHelperDataSource manager;
    @Inject
    NetworkService service;

    @Inject
    @Named(PAYMENT_ACT)
    CompositeDisposable compositeDisposable;

    @Inject
    NetworkStateHolder networkStateHolder;

    @Inject
    public PaymentPresenterImpl(Activity activity) {

        this.mActivity = activity;

    }

    @Override
    public void callApi() {
        if (isNetworkAvailable()) {
            mGetCard();
        } else {
            mView.hideProgress();
            mView.onError(mActivity.getString(R.string.networkslow));
        }
    }

    @Override
    public void addNewCard() {
        mView.startAddCardAct();
    }

    @Override
    public void getCardDetail(String brand, String id, String lastDigit, int mn, int yr, boolean isDefault) {
        mView.startCardDetail(brand, id, lastDigit, mn, yr, isDefault);

    }

    @Override
    public void startShopping() {
        mView.startShopping();
    }


    private void mGetCard() {
        mView.showProgress();
        String token = (manager.getToken());
        Observable<Response<ResponseBody>> observable = service.getCard(token, manager.getLanguage());
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
                        Utility.printLog("get card " + responseBodyResponse.code());
                        String message = "";
                        try {
                            JSONObject object = new JSONObject(responseBodyResponse.errorBody().string());
                            if (object.has("message")) {
                                message = object.getString("message");
                            }
                        } catch (JSONException | NullPointerException | IOException e) {
                            e.printStackTrace();
                        }
                        switch (responseBodyResponse.code()) {
                            case 200:
                                try {
                                    String result = responseBodyResponse.body().string();
                                    JSONObject object = new JSONObject(result);

                                    if (object.has("data") && object.get("data") != null) {
                                        Gson gson = new Gson();
                                        PaymentResponse paymentResponse = gson.fromJson(result, PaymentResponse.class);
                                        mView.setPaymentCardsList(paymentResponse.getData().getCards());
                                    }

                                } catch (JSONException | NullPointerException | IOException e) {
                                    e.printStackTrace();
                                }


                                break;

                            /*case 498:
                                SessionTokenExpObservable.getInstance().emit(true, message);
                                break;

                            case 440:
                                SessionTokenExpObservable.getInstance().emit(false, message);
                                break;
                            case 502:
                                mView.onError(mActivity.getString(R.string.serverError));
                                break;*/
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
                        mView.hideProgress();
                    }
                });
    }


    public boolean isNetworkAvailable() {
        return networkStateHolder.isConnected();
    }
}
