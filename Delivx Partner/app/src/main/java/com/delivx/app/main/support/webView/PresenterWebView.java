package com.delivx.app.main.support.webView;

import android.content.Intent;

import javax.inject.Inject;

/**
 * Created by DELL on 07-03-2018.
 */

public class PresenterWebView implements WebViewContract.PresenterOpearation {
    private String url;
    @Inject WebViewContract.ViewOperation view;

    @Inject
    public PresenterWebView() {
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
}
