package com.driver.threestops.login;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LoginDaggerModule {


    @ActivityScoped
    @Binds
    abstract Activity getActivity(LoginActivity activity);

    @Binds
    @ActivityScoped
    abstract   LoginView getView(LoginActivity view);


    @Binds
    @ActivityScoped
    abstract  LoginPresenter getPresenter(LoginPresenterImpl presenter);


}
