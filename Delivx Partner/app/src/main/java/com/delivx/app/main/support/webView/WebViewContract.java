package com.delivx.app.main.support.webView;

import android.content.Intent;

/**
 * Created by DELL on 07-03-2018.
 */

public interface WebViewContract {

    interface ViewOperation {

        void setActionBar();

        void setViews(String url);

    }

    interface PresenterOpearation{

        void getBundleData(Intent intent);

        void initActionBar();
    }

}
