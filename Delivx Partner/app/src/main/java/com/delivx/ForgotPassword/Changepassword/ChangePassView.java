package com.delivx.ForgotPassword.Changepassword;

import com.delivx.BaseView;

/**
 * Created by DELL on 03-01-2018.
 */

public interface ChangePassView extends BaseView {

    void initActionBar();

    void setTitle();

    void startLoginAct();

    void showError(String string);
}
