package com.delivx.app.main;

import com.delivx.BaseView;
import com.delivx.login.language.LanguagesList;

import java.util.ArrayList;

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

    void networkAvailable();

    void networkNotAvailable();

    void setLanguageDialog(ArrayList<LanguagesList> data, int indexOf);

    /**
     * <h1>setLanguageSuccess</h1>
     * <p>restart the main activity</p>
     */
    void setLanguageSuccess();
}
