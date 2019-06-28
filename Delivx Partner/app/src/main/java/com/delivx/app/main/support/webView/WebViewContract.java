package com.delivx.app.main.support.webView;

import android.content.Intent;

public interface WebViewContract {

    interface ViewOperation {

        void setActionBar();

        void setViews(String url);

    }

    interface PresenterOpearation{

        void getBundleData(Intent intent);

        void initActionBar();

        String getlanguageCode();
    }

}
