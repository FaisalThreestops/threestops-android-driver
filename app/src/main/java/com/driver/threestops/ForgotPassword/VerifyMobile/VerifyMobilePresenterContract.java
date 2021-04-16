package com.driver.threestops.ForgotPassword.VerifyMobile;

import android.os.Bundle;

public interface VerifyMobilePresenterContract {

    /**
     * <h2>setActionBar</h2>
     * <p>set the actionbar</p>
     */
    void setActionBar();

    /**
     * <h2>setActionBarTitle</h2>
     * <p>set the title to action bar</p>
     */
    void setActionBarTitle();

    /**
     * <h2>getBundleData</h2>
     * <p>get the data from ForgotPasswordMobNum</p>
     * @param bundle Intent bundle
     */
    void getBundleData(Bundle bundle);

    /**
     * <h2>getVerifyMessage</h2>
     * <p>to get the message, which number sent the OTP</p>
     * @param msg1 :country code
     * @param msg2 :phone number
     */
    void getVerifyMessage(String msg1, String msg2);

    /**
     * <h2>startTimer</h2>
     * <p>start the timer for resend option enable</p>
     * @param i :time 60sec
     */
    void startTimer(int i);

    /**
     * <h2>onOutSideTouch</h2>
     * <p>hiding the keyboard</p>
     */
    void onOutSideTouch();

    /**
     * <h2>sendOtpApi</h2>
     * <p>resend the OTP</p>
     */
    void sendOtpApi();

    /**
     * <h2>validateOtp</h2>
     * <p>verifying the OTP</p>
     * @param digit1 : text1
     * @param digit2 :text2
     * @param digit3 :text3
     * @param digit4 :text4
     */
    void validateOtp(String digit1,String digit2,String digit3,String digit4);

    /**
     * <h2>onSmsReceived</h2>
     * <p>receiving the otp and set in to the textview</p>
     * @param msg
     */
    void onSmsReceived(String msg);

    /**
     * <h2>getlanguageCode</h2>
     * <p>get the country code</p>
     * @return :country code
     */
    String getlanguageCode();
}
