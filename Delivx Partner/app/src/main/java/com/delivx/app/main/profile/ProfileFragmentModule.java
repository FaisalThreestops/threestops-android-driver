package com.delivx.app.main.profile;

import com.delivx.dagger.ActivityScoped;
import com.delivx.dagger.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by DELL on 21-02-2018.
 */
@Module
public abstract class ProfileFragmentModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract MyProfileFrag myProfileFrag();

    @ActivityScoped
    @Binds
    abstract ProfileContract.ViewOperations view(MyProfileFrag myProfileFrag);

    @ActivityScoped
    @Binds
    abstract ProfileContract.PresenterOpetaions presenter(MyProfilePresenter presenter);
}
