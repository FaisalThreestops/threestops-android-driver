package com.delivx.app.bookingRequest;

import android.content.Intent;

import com.delivx.pojo.PubnubResponse;

/**
 * Created by DELL on 21-09-2017.
 */

public interface BookingPopUpMainMVP {

    interface ViewOperations{
        void onSuccess(String msg);
        void onError(String error);
        void showProgressbar();
        void dismissProgressbar();
        void onTimerChanged(int progress,String time);
        void onFinish();
        void setTexts(PubnubResponse pubnubResponse);
        void startMusicPlayer();
    }
    interface PresenterOperations
    {
        void updateApptRequest(String response);
        void startTimer();
        void getBundleData(Intent intent);

    }

}

