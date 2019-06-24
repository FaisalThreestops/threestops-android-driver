package com.delivx.app.bookingRequest;

import android.content.Intent;

import com.delivx.pojo.NewBookingMQTTResponse;


/**
 * <h1>BookingPopUpMainMVP</h1>
 * <p>presenter for the  BookingPopup Activity</p>
 */
public interface BookingPopUpMainMVP {

    interface ViewOperations{

        /**
         * <h1>onSuccess</h1>
         * <p>show toast message</p>
         * @param msg message for show
         */
        void onSuccess(String msg);

        /**
         * <h1>onError</h1>
         * <p>show toast  message for error</p>
         * @param error error message
         */
        void onError(String error);

        /**
         * <h1>showProgressbar</h1>
         * <p>show progressbar</p>
         */
        void showProgressbar();

        /**
         * <h1>dismissProgressbar</h1>
         * <p>dismiss the progressbar</p>
         */
        void dismissProgressbar();

        /**
         * <h1>onTimerChanged</h1>
         * @param progress : update the progress for timer
         * @param time time for show
         */
        void onTimerChanged(int progress,String time);

        /**
         * <h1>onFinish</h1>
         * <p>close media player and open Main Activity</p>
         */
        void onFinish();

        /**
         * <h1>setTexts</h1>
         * <p>set booking details</p>
         * @param newBookingMQTTResponse booking details pojo class
         */
        void setTexts(NewBookingMQTTResponse  newBookingMQTTResponse );

        /**
         * <h1>startMusicPlayer</h1>
         * <p>start media player for Booking popup</p>
         */
        void startMusicPlayer();
    }



    interface PresenterOperations
    {

        /**
         * <h1>updateApptRequest</h1>
         * <p>online and offline change using API.</p>
         * @param response status 3 online, 4 offline
         */
        void updateApptRequest(String response);

        /**
         * <h1>startTimer</h1>
         * <p>start count down timer</p>
         */
        void startTimer();

        /**
         * <h1>getBundleData</h1>
         * <p>get the Bundle data MQTT </p>
         * @param intent data
         */
        void getBundleData(Intent intent);

    }

}

