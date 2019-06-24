package com.delivx.ForgotPassword.VerifyMobile;

import android.app.Activity;


import com.delivx.ForgotPassword.ForgotPasswordVerify;
import com.delivx.dagger.ActivityScoped;

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
