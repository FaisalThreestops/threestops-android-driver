package com.delivx.login;

import android.content.Intent;

/**
 * <h2>LoginPresenter</h2>
 * <p>This interface is i</p>
 */
public interface LoginPresenter {

    /**
     * <h2>validateCredentials</h2>
     * <p>method will validate the username and password entered by user</p>
     * @param phone
     * @param username can be mobile number and email
     * @param password any varchar string will be place here
     */
    void validateCredentials(String phone, String username, String password);

    /**
     * <h2>onDestroy</h2>
     * <p>use to freed the exsisting reference loaded into memory<p/>
     */
    void onDestroy();

    /**
     * <h2>getLoginCreds</h2>
     * <p>specifies the key values to the username and password</p>
     */
    void getLoginCreds();

    void forgotpassOnclick();

    void signUpOnclick();

    void getBundleData(Intent intent);

    void onOutSideTouch();

    void choosePhoneLogin();

    void chooseEmailLogin();

    void showDialogForCountryPicker();

    /** setting the country code
     * but default is +91
     */
    void getCountryCode();

    /**
     * <h1>unSubScribeMQTT</h1>
     * <p>unsubscribing the mqtt which are already subscribed</p>
     */
    void unSubScribeMQTT();

    /**
     * <h1>getLanguages</h1>
     * <p>api call for get the languages</p>
     */
    void getLanguages();

    /**
     * <h2>languageChanged</h2>
     * <p>Changing the language by  passsing language code and language name</p>
     * @param langCode
     * @param langName
     */
    void languageChanged(String langCode, String langName);
}
