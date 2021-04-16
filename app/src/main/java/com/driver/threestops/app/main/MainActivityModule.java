package com.driver.threestops.app.main;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MainActivityModule {
    @ActivityScoped
    @Binds
    abstract Activity getActivity(MainActivity activity);

    @Binds
    @ActivityScoped
    abstract MainView getView(MainActivity view);

    @Binds
    @ActivityScoped
    abstract MainPresenter getPresenter(MainPresenterImpl presenter);
}
