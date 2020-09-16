package com.driver.threestops.app.replaceItem;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class ReplaceModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(ReplaceItemsActivity activity);

    @Binds
    @ActivityScoped
    abstract ReplaceItemsContract.ViewOperations getView(ReplaceItemsActivity view);

    @Binds
    @ActivityScoped
    abstract ReplaceItemsContract.PresenterOperations getPresenter(ReplaceItemsPresenter presenter);
}
