package com.delivx.app.main.support.webView;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class WebViewModule {

    @Binds
    @ActivityScoped
    abstract Activity getContext(WebViewActivity webViewActivity);

    @Binds
    @ActivityScoped
    abstract WebViewContract.ViewOperation getView(WebViewActivity webViewActivity);

    @Binds
    @ActivityScoped
    abstract WebViewContract.PresenterOpearation getPresenter(PresenterWebView presenter);
}
