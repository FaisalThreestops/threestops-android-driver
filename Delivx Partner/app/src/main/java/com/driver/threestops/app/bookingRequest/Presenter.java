package com.driver.threestops.app.bookingRequest;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.driver.threestops.app.MyApplication;
import com.driver.threestops.utility.SessionManager;
import com.driver.threestops.utility.TextUtil;
import com.google.gson.Gson;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.networking.DispatcherService;
import com.driver.threestops.pojo.NewBookingMQTTResponse;
import com.driver.threestops.utility.Utility;

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


public class Presenter implements BookingPopUpMainMVP.PresenterOperations {

    @Inject   BookingPopUpMainMVP.ViewOperations view;
    @Inject   Activity context;
    @Inject   PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject   SessionManager sessionManager;
    @Inject   DispatcherService dispatcherService;

    private NewBookingMQTTResponse newBookingMQTTResponse;
    private CountDownTimer countDownTimer;

    @Inject
    public Presenter() {
    }

    @Override
    public void updateApptRequest(String status) {
        respondToReqApi(status);
    }

    @Override
    public void startTimer()
    {
        view.startMusicPlayer();
        final long finalTime = newBookingMQTTResponse.getExpiryTimer();
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
                cancelCoutDownTimer();
            }
        }.start();

    }

    @Override
    public void getBundleData(Intent intent) {
        String data = sessionManager.getBookingPopupDetails();
        if (!TextUtil.isEmpty(data)) {
            sessionManager.setBookingPopupDetails("");
            NotificationManager notificationManager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

            Utility.printLog("the booking req details : "+ data);
            newBookingMQTTResponse = new Gson().fromJson(data, NewBookingMQTTResponse.class);
            view.setTexts(newBookingMQTTResponse);
        }
    }


    /**
     * <h1>respondToReqApi</h1>
     * <p>Update online and offline in API</p>
     * @param status 3 online, 4 offline
     */
    private void respondToReqApi(String status){
        if(view!=null){
            view.showProgressbar();
        }
        Observable<Response<ResponseBody>> bookingAck=dispatcherService.respondToRequest(
                preferenceHelperDataSource.getLanguage(),((MyApplication) context.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()),
                newBookingMQTTResponse.getBid(), String.valueOf(status));
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
                            Utility.printLog("value.code() "+value.code());
                            if(value.code()==200){
                                Utility.printLog("value.code() "+value.code());
//                                Utility.printLog("value.code() "+value.body().string());
                                String response=value.body().string();
                                Utility.printLog("value.code() "+response);
                                jsonObject=new JSONObject(response);
                                view.onSuccess(jsonObject.getString("message"));

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onSuccess(jsonObject.getString("message"));
                            }
                            cancelCoutDownTimer();

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
                        cancelCoutDownTimer();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null){
                            view.dismissProgressbar();
                        }
                    }
                });
    }

    /**
     * <h1>cancelCoutDownTimer</h1>
     * <p>cancel or finish the timer count</p>
     */
    private void cancelCoutDownTimer(){
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        view.onFinish();
    }
}
