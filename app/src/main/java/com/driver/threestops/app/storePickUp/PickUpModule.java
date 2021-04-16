package com.driver.threestops.app.storePickUp;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class PickUpModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(StorePickUp activity);

    @Binds
    @ActivityScoped
    abstract PickUpContract.ViewOperations getView(StorePickUp view);

    @Binds
    @ActivityScoped
    abstract PickUpContract.PresenterOperations getPresenter(PickUpPresenter presenter);
}
