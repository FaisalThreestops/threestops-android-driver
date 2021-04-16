package com.driver.threestops.ForgotPassword.VerifyMobile;

import com.driver.threestops.BaseView;


public interface ForgotPasswordVerifyView extends BaseView {

    /**
     * <h2>initActionBar</h2>
     * <p>initializing the action bar</p>
     */
    void initActionBar();

    /**
     * <h2>setTitle</h2>
     * <p>set the title to action bar</p>
     */
    void setTitle();

    /**
     * <h2>setVerifyMessage</h2>
     * <p>set the otp sent phone number in the text</p>
     * @param msg verify message including phone number
     */
    void setVerifyMessage(String msg);

    /**
     * <h2>enableResendButton</h2>
     * <p>enabling the resend button after 60sec</p>
     */
    void enableResendButton();

    /**
     * <h2>disableResendButton</h2>
     * <p>disabling the resend button for 60sec</p>
     */
    void disableResendButton();

    /**
     * <h2>setTimerText</h2>
     * <p>setting the timer count </p>
     * @param text :time in sec
     */
    void setTimerText(String text);

    /**
     * <h2>showError</h2>
     * <p>show error message</p>
     * @param msg : error
     */
    void showError(String msg);

    /**
     * <h2>startNextActivity</h2>
     * <p>Move to the ForgotPasswordChangePass activity to the change password</p>
     * @param otp :otp
     * @param mob :mobile number
     * @param cCode :country code
     */
    void startNextActivity(String otp, String mob, String cCode);

    /**
     * <h2>mSetResult</h2>
     * <p>finish this activity and moving to EditProfileActivity</p>
     */
    void mSetResult();

    /**
     * <h2>startLoginAct</h2>
     * <p>move to login activity</p>
     */
    void startLoginAct();

    /**
     * <h2>setOtp</h2>
     * <p>set the otp to text</p>
     * @param otp : OTP
     */
    void setOtp(String otp);


}
