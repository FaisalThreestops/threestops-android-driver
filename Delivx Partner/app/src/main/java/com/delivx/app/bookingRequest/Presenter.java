package com.delivx.app.bookingRequest;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.delivx.app.MyApplication;
import com.delivx.login.LoginActivity;
import com.delivx.login.language.LanguagesList;
import com.delivx.service.LocationUpdateService;
import com.delivx.utility.AppConstants;
import com.google.gson.Gson;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.DispatcherService;
import com.delivx.pojo.NewBookingMQTTResponse;
import com.delivx.utility.Utility;

import org.json.JSONArray;
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
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey("booking_Data")) {
            NotificationManager notificationManager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

            String response = bundle.getString("booking_Data");
            Utility.printLog("the booking req details : "+ response);
            newBookingMQTTResponse = new Gson().fromJson(response, NewBookingMQTTResponse.class);
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
                preferenceHelperDataSource.getLanguage(),preferenceHelperDataSource.getToken(),
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
                            JSONObject jsonObject = null;
                            switch (value.code()){
                                case 200:
                                    jsonObject=new JSONObject(value.body().string());
                                    view.onSuccess(jsonObject.getString("message"));
                                    break;
                                case 440:
                                case 498:
                                    Utility.printLog("pushTopics shared pref "+preferenceHelperDataSource.getPushTopic());
                                    Utility.subscribeOrUnsubscribeTopics(new JSONArray(preferenceHelperDataSource.getPushTopic()),false);
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    ((MyApplication)context.getApplicationContext()).disconnectMqtt();
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                    if(Utility.isMyServiceRunning(LocationUpdateService.class, context))
                                    {
                                        Intent stopIntent = new Intent(context, LocationUpdateService.class);
                                        stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                                        context.startService(stopIntent);
                                    }

                                    break;
                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    break;
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
