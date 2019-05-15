package com.delivx.login;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 27-12-2017.
 */

@Module
public abstract class LoginDaggerModule {

       /**
     * <P>This method provides activity reference</P>
     * @return activity.
     */
    @ActivityScoped
    @Binds
    abstract Activity getActivity(LoginActivity activity);
    /**
     * <P>This method provides LoginView reference</P>
     * @return LoginView.
     */
    @Binds
    @ActivityScoped
    abstract   LoginView getView(LoginActivity view);

    /**
     * <P>This method provides Login BookingRidePresenter  reference</P>
     * @return LoginPresenterImpl.
     */
    @Binds
    @ActivityScoped
    abstract  LoginPresenter getPresenter(LoginPresenterImpl presenter);


}
