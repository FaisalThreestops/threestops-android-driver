package com.delivx.ForgotPassword.RetriveFromMobile;

import android.view.KeyEvent;
import android.view.View;

/**
 * Created by DELL on 29-12-2017.
 */

public interface ForgotPassPresenterContract {

    void setActionBar();

    void setActionBarTitle();

    void getCountryCode();

    void showDialogForCountryPicker();

    void onOutSideTouch();

    boolean onNextKey(View v, int keyCode, KeyEvent event, String mob);

    void validatePhone(String mob,String countryCode);

    void etAfterTextChanged();

    void rbEmailChecked();

    void rbMobileChecked();

    String getlanguageCode();
}
