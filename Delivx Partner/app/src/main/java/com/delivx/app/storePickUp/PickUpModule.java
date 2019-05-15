package com.delivx.app.storePickUp;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 06-02-2018.
 */
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
