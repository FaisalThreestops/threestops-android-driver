package com.delivx.vehiclelist;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 04-01-2018.
 */

@Module
public abstract class VehicleListModule {
    @ActivityScoped
    @Binds
    abstract Activity getActivity(VehicleList activity);

    @Binds
    @ActivityScoped
    abstract VehicleListView getView(VehicleList view);


    @Binds
    @ActivityScoped
    abstract VehicleListPresenterContract getPresenter(VehicleListPresenter presenter);

}
