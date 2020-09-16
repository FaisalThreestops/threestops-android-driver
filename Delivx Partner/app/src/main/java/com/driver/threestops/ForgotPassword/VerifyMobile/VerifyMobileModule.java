package com.driver.threestops.ForgotPassword.VerifyMobile;

import android.app.Activity;


import com.driver.threestops.ForgotPassword.ForgotPasswordVerify;
import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class VerifyMobileModule
{
    @ActivityScoped
    @Binds
    abstract Activity getActivity(ForgotPasswordVerify activity);

    @Binds
    @ActivityScoped
    abstract ForgotPasswordVerifyView getView(ForgotPasswordVerify view);


    @Binds
    @ActivityScoped
    abstract VerifyMobilePresenterContract getPresenter(PresenterVerifyMobile presenter);

}
