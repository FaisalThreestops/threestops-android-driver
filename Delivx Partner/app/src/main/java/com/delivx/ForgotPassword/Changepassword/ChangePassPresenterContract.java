package com.delivx.ForgotPassword.Changepassword;

import android.os.Bundle;


public interface ChangePassPresenterContract {

    /**
     * <h2>setActionBar</h2>
     * <p>setting the action bar</p>
     */
    void setActionBar();

    /**
     * <h2>setActionBarTitle</h2>
     * <p>set the title for action bar</p>
     */
    void setActionBarTitle();

    /**
     * <h2>getBundleData</h2>
     * <p>get the data from privious(ForgotPasswordVerify) activity</p>
     * @param bundle : data(OTP,Country code,Phone number)
     */
    void getBundleData(Bundle bundle);

    /**
     * <h2>onOutSideTouch</h2>
     * <p>hiding the KeyBoard</p>
     */
    void onOutSideTouch();

    /**
     * <h2>validatePassword</h2>
     * <p>verifying the new password</p>
     * @param pass : newPassword
     * @param confPass :ConfirmPassword
     */
    void validatePassword(String pass, String confPass);

    /**
     * <h2>getlanguageCode</h2>
     * <p>get the country code</p>
     * @return : country code
     */
    String getlanguageCode();
}
