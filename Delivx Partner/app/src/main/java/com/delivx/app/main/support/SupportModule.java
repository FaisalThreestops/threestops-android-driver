package com.delivx.app.main.support;

import com.delivx.dagger.ActivityScoped;
import com.delivx.dagger.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SupportModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract SupportFrag  supportFrag();

    @ActivityScoped
    @Binds
    abstract SupportContract.ViewOperation view(SupportFrag supportFrag);

    @ActivityScoped
    @Binds
    abstract SupportContract.PresenterOperation presenter(SupportFragPresenter presenter);
}
