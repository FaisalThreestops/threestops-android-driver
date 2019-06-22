package com.delivx.app.main;

/**
 * Created by DELL on 29-01-2018.
 */

public interface MainPresenter {

    void getVersion();

    void getAppConfig();

    void onDrawerOpen();

    void subscribeNetworkObserver();

    void checkForNetwork(boolean isConnected);

    /**
     * <h1>getlanguageCode</h1>
     * <p>Checking the language code, for RTL</p>
     * @return : language code
     */
    String getlanguageCode();


    /**
     * <h1>getLanguages</h1>
     * <p>API call for the getting language</p>
     */
    void getLanguages();

    /**
     * <h1>languageChanged</h1>
     * <p>for storing the language in shared preference</p>
     * @param langCode : language code
     * @param langName : language name
     */
    void languageChanged(String langCode, String langName);
}
