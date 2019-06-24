package com.delivx.app.main;

import com.delivx.BaseView;
import com.delivx.login.language.LanguagesList;

import java.util.ArrayList;


/**
 * <h1>MainView</h1>
 * <p>presenter for the mainActivity view class</p>
 */
public interface MainView extends BaseView
{
    /**
     * <h1>setVersiontext</h1>
     * <p>set version text</p>
     * @param version Application version
     */
    void setVersiontext(String version);

    /**
     * <h1>setError</h1>
     * <p>error message for session expired and error</p>
     * @param code error code
     * @param msg error message
     */
    void setError(int code,String msg);

    /**
     * <h1>setProfileDetails</h1>
     * <p>set profile name and image in navigation drawable</p>
     */
    void setProfileDetails();

    /**
     * <h1>dismissDialog</h1>
     * <p>dismiss dialog</p>
     */
    void dismissDialog();

    /**
     * <h1>logout</h1>
     * <p>logout event after api response</p>
     */
    void logout();

    /**
     * <h1>networkAvailable</h1>
     * <p>dismiss the no internet dialog</p>
     */
    void networkAvailable();

    /**
     * <h1>networkNotAvailable</h1>
     * <p>show no internet dialog</p>
     */
    void networkNotAvailable();

    /**
     * <h1>setLanguageDialog</h1>
     * <p>show change language dialog</p>
     * @param data language list
     * @param indexOf selected index of language
     */
    void setLanguageDialog(ArrayList<LanguagesList> data, int indexOf);

    /**
     * <h1>setLanguageSuccess</h1>
     * <p>restart the main activity</p>
     */
    void setLanguageSuccess();
}
