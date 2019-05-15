package com.delivx.ForgotPassword.RetriveFromMobile;

import com.delivx.BaseView;

/**
 * Created by DELL on 29-12-2017.
 */

public interface ForgotPassMobNumView extends BaseView {

    void initActionBar();

    void setTitle();

    void setCounryFlag(int drawableID,String dialCOde,int minLength,int maxLength);

    void setMaxLength(int length);

    void editextAfterChanged();

    void onEmailSelection();

    void onMobileSelection();

    void startNextActivity( String countryCode, String mob_mail);

    void moveToLogin(String message);

    void onError(String error);
}
