package com.delivx.app.bookingRequest;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.google.gson.Gson;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.DispatcherService;
import com.delivx.pojo.PubnubResponse;
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

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by DELL on 26-09-2017.
 */

public class Presenter implements BookingPopUpMainMVP.PresenterOperations {

    @Inject
    BookingPopUpMainMVP.ViewOperations view;

    @Inject
    Activity context;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    DispatcherService dispatcherService;

    private PubnubResponse pubnubResponse;
    private CountDownTimer countDownTimer;

    @Inject
    public Presenter() {
    }

    @Override
    public void updateApptRequest(String status) {
        respondToreqApi(status);
    }

    @Override
    public void startTimer()
    {
        view.startMusicPlayer();

        final long finalTime = pubnubResponse.getExpiryTimer();
        countDownTimer = new CountDownTimer(finalTime * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int res = (int) (((float) seconds / finalTime) * 100);

                Utility.printLog("the timer time is :  "+seconds);
                if(seconds<=99){
                    view.onTimerChanged(res,String.format("%02d", seconds));
                }else {
                    view.onTimerChanged(res,String.format("%03d", seconds));
                }
            }
            public void onFinish() {
                view.onTimerChanged(0,"00");
                cancelCoutdownTimer();
            }
        }.start();

    }

    @Override
    public void getBundleData(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey("booking_Data")) {
            NotificationManager notificationManager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

            String response = bundle.getString("booking_Data");
            pubnubResponse = new Gson().fromJson(response, PubnubResponse.class);
            view.setTexts(pubnubResponse);

//            preferenceHelperDataSource.setLastBooking(pubnubResponse.getBid());
        }
    }




    public void respondToreqApi(String status){
        if(view!=null){
            view.showProgressbar();
        }
        Observable<Response<ResponseBody>> bookingAck=dispatcherService.respondToRequest(preferenceHelperDataSource.getLanguage(),preferenceHelperDataSource.getToken(),pubnubResponse.getBid(), String.valueOf(status));
        bookingAck.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        if(view!=null){
                            view.dismissProgressbar();
                        }
                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                view.onSuccess(jsonObject.getString("message"));

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onSuccess(jsonObject.getString("message"));
                            }
                            cancelCoutdownTimer();

                            Utility.printLog("respondToRequest : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("respondToRequest : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null){
                            view.dismissProgressbar();
                        }
                        cancelCoutdownTimer();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null){
                            view.dismissProgressbar();
                        }
                    }
                });
    }

    private void cancelCoutdownTimer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        view.onFinish();
    }
}
