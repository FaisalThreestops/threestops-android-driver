package com.delivx.ForgotPassword.RetriveFromMobile;

import android.app.Activity;

import com.delivx.ForgotPassword.ForgotPasswordMobNum;
import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RetriveFromMobModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(ForgotPasswordMobNum activity);

    @Binds
    @ActivityScoped
    abstract ForgotPassMobNumView getView(ForgotPasswordMobNum view);


    @Binds
    @ActivityScoped
    abstract ForgotPassPresenterContract getPresenter(PresenterForgotPass presenter);

}
