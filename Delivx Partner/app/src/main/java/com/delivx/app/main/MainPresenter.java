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

    String getlanguageCode();

    void getLanguages();

    void languageChanged(String langCode, String langName, int dir);
}
