package com.delivx.ForgotPassword.VerifyMobile;

import com.delivx.BaseView;

/**
 * Created by DELL on 02-01-2018.
 */

public interface ForgotPasswordVerifyView extends BaseView {

    void initActionBar();

    void setTitle();

    void setVerifyMessage(String msg);

    void enableResendButton();

    void disableResendButton();

    void setTimerText(String text);

    void showError(String msg);

    void startNextActivity(String otp, String mob, String cCode);

    void mSetResult();

    void startLoginAct();

    void setOtp(String otp);


}
