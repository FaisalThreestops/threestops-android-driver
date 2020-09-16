package com.driver.threestops.app.main.history;

import com.driver.threestops.dagger.ActivityScoped;
import com.driver.threestops.dagger.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HistoryModule
{
    @FragmentScoped
    @ContributesAndroidInjector
    abstract HistoryFragment historyFragment();

    @ActivityScoped
    @Binds
    abstract HistoryContract.ViewOperations view(HistoryFragment historyFragment);

    @ActivityScoped
    @Binds
    abstract HistoryContract.PresenterOperations presenter(HistoryPresenter presenter);
}
