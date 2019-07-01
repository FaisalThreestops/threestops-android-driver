package com.delivx.app.main.support.webView;

import android.content.Intent;

public interface WebViewContract {

    interface ViewOperation {

        /**
         * <h2>setActionBar</h2>
         * <p>set the action bar</p>
         */
        void setActionBar();

        /**
         * <h2>setViews</h2>
         * <p>set the url to start the view</p>
         * @param url : web url
         */
        void setViews(String url);

    }

    interface PresenterOpearation{

        /**
         * <h2>getBundleData</h2>
         * <p>get the previous(SupportRVA) data </p>
         * @param intent : SupportRVA intent)data)
         */
        void getBundleData(Intent intent);

        /**
         * <h2>initActionBar</h2>
         * <p>initializing the action bar</p>
         */
        void initActionBar();

        /**
         * <h2>getlanguageCode</h2>
         * <p>get the country code</p>
         * @return "languageCode
         */
        String getlanguageCode();
    }

}
