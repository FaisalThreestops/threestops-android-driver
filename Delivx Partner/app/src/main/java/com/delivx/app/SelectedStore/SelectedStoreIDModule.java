package com.delivx.app.SelectedStore;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

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
