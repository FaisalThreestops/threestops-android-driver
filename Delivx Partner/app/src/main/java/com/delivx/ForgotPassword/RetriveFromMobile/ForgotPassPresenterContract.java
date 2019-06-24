package com.delivx.ForgotPassword.RetriveFromMobile;

import android.view.KeyEvent;
import android.view.View;


public interface ForgotPassPresenterContract {

    /**
     * <h2>setActionBar</h2>
     * <p>set the actionBar</p>
     */
    void setActionBar();

    /**
     * <h2>setActionBarTitle</h2>
     * <p>set the action bar title</p>
     */
    void setActionBarTitle();

    /**
     * <h1>getCountryCode</h1>
     * <p>get country from the dialog</p>
     */
    void getCountryCode();

    /**
     * <h2>showDialogForCountryPicker</h2>
     * <p>show the country codes</p>
     */
    void showDialogForCountryPicker();

    /**
     * <h2>onOutSideTouch</h2>
     * <p>hide the keyboard</p>
     */
    void onOutSideTouch();

    /**
     * <h2>onNextKey</h2>
     * <p>validating the phone number</p>
     * @param v :view
     * @param keyCode :keycode
     * @param event : event
     * @param mob :mobile number
     */
    boolean onNextKey(View v, int keyCode, KeyEvent event, String mob);

    /**
     * <h2>validatePhone</h2>
     * <p>validating the phone number</p>
     * @param mob :mobile number
     * @param countryCode :country code
     */
    void validatePhone(String mob,String countryCode);

    /**
     * <h2>etAfterTextChanged</h2>
     * <p>changing the text</p>
     */
    void etAfterTextChanged();

    /**
     * <h2>rbEmailChecked</h2>
     * <p>select the email option to verify</p>
     */
    void rbEmailChecked();

    /**
     * <h2>rbMobileChecked</h2>
     * <p>select the phonenumber option to verify</p>
     */
    void rbMobileChecked();

    /**
     * <h2>getlanguageCode</h2>
     * <p>get the language code</p>
     * @return : Language  ccode
     */
    String getlanguageCode();
}
