package com.driver.threestops.ForgotPassword.RetriveFromMobile;

import android.app.Activity;

import com.driver.threestops.ForgotPassword.ForgotPasswordMobNum;
import com.driver.threestops.dagger.ActivityScoped;

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
