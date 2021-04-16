package com.driver.threestops.app.main.homeFrag;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

import com.driver.threestops.dagger.ActivityScoped;
import com.driver.threestops.dagger.FragmentScoped;


@Module
public abstract class HomeFragmentModule
{
    @FragmentScoped
    @ContributesAndroidInjector
    abstract HomeFragment homeFragment();

    @ActivityScoped
    @Binds
    abstract HomeFragmentContract.View view(HomeFragment homeFragment);

    @ActivityScoped
    @Binds
    abstract HomeFragmentContract.Presenter presenter(Presenter presenter);


}
