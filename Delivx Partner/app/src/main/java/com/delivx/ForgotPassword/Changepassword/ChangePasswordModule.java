package com.delivx.ForgotPassword.Changepassword;

import android.app.Activity;

import com.delivx.ForgotPassword.ForgotPasswordChangePass;
import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ChangePasswordModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(ForgotPasswordChangePass activity);

    @Binds
    @ActivityScoped
    abstract ChangePassView getView(ForgotPasswordChangePass view);


    @Binds
    @ActivityScoped
    abstract ChangePassPresenterContract getPresenter(PresenterChangePass presenter);
}
