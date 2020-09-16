package com.driver.threestops.app.main.support;

import com.driver.threestops.dagger.ActivityScoped;
import com.driver.threestops.dagger.FragmentScoped;

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
