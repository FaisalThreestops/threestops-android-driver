package com.delivx.app.main;

import com.delivx.BaseView;

/**
 * Created by DELL on 29-01-2018.
 */

public interface MainView extends BaseView
{
    void setVersiontext(String version);

    void setError(int code,String msg);

    void setProfileDetails();

    void dismissDialog();

    void logout();
}
