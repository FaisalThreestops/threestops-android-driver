package com.delivx.app.main;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

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
