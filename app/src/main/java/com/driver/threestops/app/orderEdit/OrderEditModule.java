package com.driver.threestops.app.orderEdit;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class OrderEditModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(OrderEditActivity activity);

    @Binds
    @ActivityScoped
    abstract OrderEditContract.ViewOperations getView(OrderEditActivity view);

    @Binds
    @ActivityScoped
    abstract OrderEditContract.PresenterOperations getPresenter(OrderEditPresenter presenter);
}
