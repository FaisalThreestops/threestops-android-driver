package com.delivx.login;

import android.content.Intent;


public interface LoginPresenter {

    /**
     * <h2>validateCredentials</h2>
     * <p>method will validate the username and password entered by user</p>
     * @param phone phone number
     * @param username Email id
     * @param password any varchar string will be place here
     */
    void validateCredentials(String phone, String username, String password);

    /**
     * <h2>onDestroy</h2>
     * <p>use to freed the existing reference loaded into memory<p/>
     */
    void onDestroy();

    /**
     * <h1>getLoginCreds</h1>
     * <p>fetching the login username and password from the shared preference</p>
     */
    void getLoginCreds();

    /**
     * <h1>forgotPassOnclick</h1>
     * <p>for perform Forgot password event</p>
     */
    void forgotPassOnclick();

    /**
     * <h1>signUpOnclick</h1>
     * <p>for perform signup event</p>
     */
    void signUpOnclick();

    /**
     * <h1>getBundleData</h1>
     * <p>Used to get the user's data via intent to check if the user was previously logged in or not</p>
     * @param intent  message from other activity
     */
    void getBundleData(Intent intent);

    /**
     * <h1>onOutSideTouch</h1>
     * <p>for perform touch event anywhere in the screen, perform is hiding keyboard</p>
     */
    void onOutSideTouch();

    /**
     * <h1>choosePhoneLogin</h1>
     * <p>for select the phone number login</p>
     */
    void choosePhoneLogin();

    /**
     * <h1>chooseEmailLogin</h1>
     * <p>for select email login option</p>
     */
    void chooseEmailLogin();

    /**
     * <h1>showDialogForCountryPicker</h1>
     * <p>dialog for select country code</p>
     */
    void showDialogForCountryPicker();

    /**
     * <h1>getCountryCode</h1>
     * <p>Fetch country code from shared preference</p>
     */
    void getCountryCode();

    /**
     * <h1>unSubScribeMQTT</h1>
     * <p>Unsubscribe the subscribed MQTT Topic</p>
     */
    void unSubScribeMQTT();

    /**
     * <h1>getLanguages</h1>
     * <p>API call to fetch the active languages on the app</p>
     */
    void getLanguages();

    /**
     * <h1>languageChanged</h1>
     * <p>Store the language details after language selection event callback </p>
     * @param langCode language code
     * @param langName language name
     */
    void languageChanged(String langCode, String langName);
}
