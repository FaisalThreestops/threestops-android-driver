package com.delivx.ForgotPassword.VerifyMobile;

import android.os.Bundle;

/**
 * Created by DELL on 02-01-2018.
 */

public interface VerifyMobilePresenterContract {

    void setActionBar();

    void setActionBarTitle();

    void getBundleData(Bundle bundle);

    void getVerifyMessage(String msg1, String msg2);

    void startTimer(int i);

    void onOutSideTouch();

    void sendOtpApi();

    void validateOtp(String digit1,String digit2,String digit3,String digit4);

    void onSmsReceived(String msg);

    String getlanguageCode();
}
