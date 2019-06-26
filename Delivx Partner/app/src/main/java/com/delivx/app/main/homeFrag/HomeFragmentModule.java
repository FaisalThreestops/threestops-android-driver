package com.delivx.app.main.homeFrag;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

import com.delivx.dagger.ActivityScoped;
import com.delivx.dagger.FragmentScoped;


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
