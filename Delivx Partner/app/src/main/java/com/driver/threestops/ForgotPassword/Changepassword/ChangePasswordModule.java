package com.driver.threestops.ForgotPassword.Changepassword;

import android.app.Activity;

import com.driver.threestops.ForgotPassword.ForgotPasswordChangePass;
import com.driver.threestops.dagger.ActivityScoped;

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
