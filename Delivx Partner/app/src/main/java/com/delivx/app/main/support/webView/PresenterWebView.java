package com.delivx.app.main.support.webView;

import android.content.Intent;

import com.delivx.data.source.PreferenceHelperDataSource;

import javax.inject.Inject;

public class PresenterWebView implements WebViewContract.PresenterOpearation {
    private String url;
    @Inject WebViewContract.ViewOperation view;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    PresenterWebView() {
    }

    @Override
    public void getBundleData(Intent intent) {
        url =intent.getStringExtra("URL");
        view.setViews(url);
    }

    @Override
    public void initActionBar() {
        view.setActionBar();
    }

    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }
}
