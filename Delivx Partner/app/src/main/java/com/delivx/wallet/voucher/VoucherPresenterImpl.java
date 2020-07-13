package com.delivx.wallet.voucher;

import android.app.Activity;

import com.delivx.app.MyApplication;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.delivx.utility.Utility;
import com.driver.Threestops.R;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

public class VoucherPresenterImpl implements VoucherPresenter {

    private VoucherBottomSheet mView;
    @Inject
    NetworkService service;
    @Inject
    PreferenceHelperDataSource manager;
    @Inject
    Activity mActivity;
    @Inject
    public VoucherPresenterImpl() {
    }



    @Override
    public void attachView(VoucherBottomSheet view) {
        mView=view;
    }

    @Override
    public void clearData() {

    }


    @Override
    public void addAmount(String amt) {
        if(amt!=null && !amt.equals(""))
        {
            addAmountApi(amt);
        }
        else
        {
            mView.showError("Enter voucher code");
        }

    }


     private void addAmountApi(String amt) {

        mView.showProgress();
        Observable<Response<ResponseBody>> observable = service.voucher( ((MyApplication) mActivity.getApplication()).getAuthToken(manager.getDriverID()), manager.getLanguage(),
                amt);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        Utility.printLog("codeOfItem"+responseBodyResponse.code());

                        String msg="";
                        try {
                            JSONObject object=new JSONObject(responseBodyResponse.errorBody().string());
                            if(object.has("message"))
                            {
                                msg=object.getString("message");
                            }

                        }catch (Exception e)
                        {

                        }
                        mView.hideProgress();
                        switch (responseBodyResponse.code()) {
                            case 200:
                                String result;
                                try {
                                    /*

*/
                                    result = responseBodyResponse.body().string();
                                    Utility.printLog("voucher re "+result);
                                    JSONObject object=new JSONObject(result);
                                    if(object.has("message"))
                                    {
                                        msg=object.getString("message");
                                    }
                                    mView.showError(msg);
                                    mView.closePopup();


                                } catch (IOException|JSONException  e) {
                                    e.printStackTrace();
                                }

                                break;

                            case 498:

                                break;

                            case 440:

                                break;
                            case 502:
                                mView.showError(mActivity.getString(R.string.serverError));
                                break;
                            case 404:
                                mView.showError(msg);
                                break;
                            default:

                                mView.showError(mActivity.getString(R.string.somethingWentWrong));
                                break;
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(mActivity.getString(R.string.somethingWentWrong));
                        mView.hideProgress();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
