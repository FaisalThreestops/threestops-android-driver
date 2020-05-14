package com.delivx.payment_card_detail;


import static com.delivx.utility.AppConstants.CARD_DETAIL_ACT;

import android.app.Activity;
import android.util.Log;

import com.delivx.app.MyApplication;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.delivx.networking.NetworkStateHolder;
import com.driver.delivx.R;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Named;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Response;


public class CardDetailPresenterImpl implements CardDetailPresenter {

    @Inject
    CardDetailView mView;

    @Inject
    NetworkService service;

    @Inject
    @Named(CARD_DETAIL_ACT)
    CompositeDisposable compositeDisposable;

    @Inject
    Activity mActivity;

    @Inject
    PreferenceHelperDataSource manager;


    @Inject
    NetworkStateHolder networkStateHolder;

    @Inject
    CardDetailPresenterImpl(CardDetailView cardDetailView) {
        this.mView = cardDetailView;

    }

    @Override
    public void deleteCard(String cardId) {

        if (isNetworkAvailable()) {
            mDeleteCard(cardId);
        } else {
            mView.setErrorMsg(mActivity.getString(R.string.networkslow));
        }

    }

    private void mDeleteCard(String cardId) {
        if (mView != null) {
            mView.showProgress();
        }
        Observable<Response<ResponseBody>> bad = service.deleteCard( ((MyApplication) mActivity.getApplication()).getAuthToken(manager.getDriverID()),manager.getLanguage(),cardId);
        bad.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if (200 == value.code()) {
                            mView.hideProgress();
                            mView.navToPayment();
                        } else {
                            try {
                                if (value.errorBody() != null) {
                                    JSONObject errJson = new JSONObject(value.errorBody().string());
                                    mView.setErrorMsg(errJson.getString("message"));
                                    mView.hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error", "error" + e.getMessage());
                        e.printStackTrace();
                        mView.setErrorMsg(e.getMessage());
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }

    @Override
    public void makeDefault(String cardId) {

        if (isNetworkAvailable()) {
            mMakeDefault(cardId);
        } else {
            mView.setErrorMsg(mActivity.getString(R.string.networkslow));
        }
    }

    private void mMakeDefault(String cardId) {
        if (mView != null) {
            mView.showProgress();
        }
        Observable<Response<ResponseBody>> bad = service.makeDefaultCard( ((MyApplication) mActivity.getApplication()).getAuthToken(manager.getDriverID()),manager.getLanguage(),cardId);
        bad.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        mView.hideProgress();
                        if (200 == value.code()) {
                          mView.navToPayment();
                        } else {
                            try {
                                if (value.errorBody() != null) {
                                    JSONObject errJson = new JSONObject(value.errorBody().string());
                                    mView.setErrorMsg(errJson.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error", "error" + e.getMessage());
                        e.printStackTrace();
                        mView.setErrorMsg(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @Override
    public void getIntentData(String id, String brand, String lastDigit, int mn, int yr) {

        String exp = mn + "/" + yr;
        String number = mActivity.getString(R.string.xxx) + " " + lastDigit;
        mView.setCardData(brand, number, exp);

    }


    public boolean isNetworkAvailable() {
        return networkStateHolder.isConnected();
    }
}
