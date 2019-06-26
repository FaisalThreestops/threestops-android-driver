package com.delivx.app.storeDetails;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class StoreDetailsModule
{
    @ActivityScoped
    @Binds
    abstract Activity getActivity(StorePickUpDetails activity);

    @Binds
    @ActivityScoped
    abstract StoreDetailsContract.View getView(StorePickUpDetails view);

    @Binds
    @ActivityScoped
    abstract StoreDetailsContract.Presenter getPresenter(StoreDetailsPresenter presenter);
}
