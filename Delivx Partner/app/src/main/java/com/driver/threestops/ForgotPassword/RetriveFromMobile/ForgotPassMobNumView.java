package com.driver.threestops.ForgotPassword.RetriveFromMobile;

import com.driver.threestops.BaseView;

public interface ForgotPassMobNumView extends BaseView {

    /**
     * <h2>initActionBar</h2>
     * <p>initializing the action bar</p>
     */
    void initActionBar();

    /**
     * <h2>setTitle</h2>
     * <p>set the Action bar title name</p>
     */
    void setTitle();

    /**
     * <h2>setCountryFlag</h2>
     * <p>set the country code</p>
     * @param drawableID :flag resource
     * @param dialCOde : country code
     * @param minLength : minimum length phone number
     * @param maxLength : maximum length phone number
     */
    void setCountryFlag(int drawableID,String dialCOde,int minLength,int maxLength);

    /**
     * <h2>editextAfterChanged</h2>
     * <p>text will changed, after edited the text</p>
     */
    void editextAfterChanged();

    /**
     * <h2>onEmailSelection</h2>
     * <p>select the email option </p>
     */
    void onEmailSelection();

    /**
     * <h2>onMobileSelection</h2>
     * <p>select the mobile number option</p>
     */
    void onMobileSelection();

    /**
     * <h2>startNextActivity</h2>
     * <p>moving to the ForgotPasswordVerify to verify the Phone number</p>
     * @param countryCode : country code
     * @param mob_mail :mobile or email
     */
    void startNextActivity( String countryCode, String mob_mail);

    /**
     * <h2>moveToLogin</h2>
     * <p>moving to mainLoginActivity</p>
     * @param message : message after API success
     */
    void moveToLogin(String message);

    /**
     * <h2>onError</h2>
     * <p>showing error alert</p>
     * @param error : error message
     */
    void onError(String error);
}
