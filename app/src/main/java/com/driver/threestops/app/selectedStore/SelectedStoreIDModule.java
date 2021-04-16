package com.driver.threestops.app.selectedStore;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class SelectedStoreIDModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(SelectedStoreIdActivity activity);

    @Binds
    @ActivityScoped
    abstract SelectedStoreIdContract.ViewOperations getView(SelectedStoreIdActivity view);

    @Binds
    @ActivityScoped
    abstract SelectedStoreIdContract.PresenterOperations getPresenter(SelectedStoreIDPresenter presenter);
}
