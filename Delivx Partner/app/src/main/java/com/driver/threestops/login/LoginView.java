package com.driver.threestops.login;

import com.driver.threestops.BaseView;
import com.driver.threestops.login.language.LanguagesList;

import java.util.ArrayList;



public interface LoginView extends BaseView {

    /**
     * <h2>setUsernameError</h2>
     * <p>give error message to the calling view after validating username</p>
     * @param message Error message get from validation of email and phone number
     */
    void setUsernameError(String message);

    /**
     * <h2>setPasswordError</h2>
     * <p>give error message to the calling view after validating password</p>
     * @param message Error message get from validation of password
     */
    void setPasswordError(String message);

    /**
     * <h2>showError</h2>
     * <p>if user get a error from api</p>
     * @param error what error user get at run time
     */
    void showError(String error);

    /**
     * <h2>navigateToHome</h2>
     * <p>if successfully validates all the field user enters
     * it will navigate to the home page of application</p>
     */
    void navigateToHome();

    /**
     * <h1>setLoginCreds</h1>
     * <p>setting the username and password from shared preference if available</p>
     * @param username email
     * @param pass password
     */
    void setLoginCreds(String username,String  pass);

    /**
     * <h1>startForgotPassAct</h1>
     * <p>start activity event for  forgot password</p>
     */
    void startForgotPassAct();

    /**
     * <h1>startSignUpAct</h1>
     * <p>start activity event  for signup</p>
     */
    void startSignUpAct();

    /**
     * <h1>onPhoneLoginSelected</h1>
     * <p>set phone number login layout</p>
     */
    void onPhoneLoginSelected();

    /**
     * <h1>onEmailLoginSelected</h1>
     * <p>set email login layout</p>
     */
    void onEmailLoginSelected();

    /**
     * <h1>setCountryCode</h1>
     * <p>set country code</p>
     * @param dialCode country code
     */
    void setCountryCode(String dialCode);

    /**
     * <h1>setLanguageDialog</h1>
     * @param languagesListModel : array list for the language
     * @param indexOf : selected language index
     */
    void setLanguageDialog(ArrayList<LanguagesList> languagesListModel, int indexOf);

    /**
     * <h1>setLanguage</h1>
     * <p>set the language name and check need restart or not ,
     * if  the method call from API response restart the activity</p>
     * @param languageName : language name
     * @param b : if true restart the activity
     */
    void setLanguage(String languageName, boolean b);
}
