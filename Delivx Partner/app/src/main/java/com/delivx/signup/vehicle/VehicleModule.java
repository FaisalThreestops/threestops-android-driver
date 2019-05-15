package com.delivx.signup.vehicle;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 20-01-2018.
 */
@Module
public abstract class VehicleModule {

    @Binds
    @ActivityScoped
    abstract Activity getActivity(SignupVehicle vehicle);

    @Binds
    @ActivityScoped
    abstract SignupPresenterContract getPresenter(PresenterImpl presenter);
   /* @Binds
    @ActivityScoped
    abstract SignupVehicleView getSignUpVehicleView(SignupVehicle vehicle);*/


}
