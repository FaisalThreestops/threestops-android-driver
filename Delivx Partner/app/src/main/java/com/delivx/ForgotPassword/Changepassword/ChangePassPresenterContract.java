package com.delivx.ForgotPassword.Changepassword;

import android.os.Bundle;

/**
 * Created by DELL on 03-01-2018.
 */

public interface ChangePassPresenterContract {

    void setActionBar();

    void setActionBarTitle();

    void getBundleData(Bundle bundle);

    void onOutSideTouch();

    void validatePassword(String pass, String confPass);
}
