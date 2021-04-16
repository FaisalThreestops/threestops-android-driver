package com.driver.threestops.app.main;

import android.content.Intent;

/**
 * <h1>MainPresenter</h1>
 * <p>presenter for the MainActivity presenter class</p>
 */
public interface MainPresenter {

    /**
     * <h1>getVersion</h1>
     * <p>get current version of the Package</p>
     */
    void getVersion();

    /**
     * <h1>getAppConfig</h1>
     * <p>API call for the Config of the app </p>
     */
    void getAppConfig();

    /**
     * <h1>onDrawerOpen</h1>
     * <p>set profile details in navigation drawable</p>
     */
    void onDrawerOpen();

    /**
     * <h1>subscribeNetworkObserver</h1>
     * <p>check network </p>
     */
    void subscribeNetworkObserver();

    /**
     * <h1>checkForNetwork</h1>
     * <p>check network availability</p>
     * @param isConnected return is available
     */
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
