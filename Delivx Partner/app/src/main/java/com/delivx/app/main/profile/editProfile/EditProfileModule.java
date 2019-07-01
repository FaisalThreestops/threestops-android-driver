package com.delivx.app.main.profile.editProfile;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class EditProfileModule {
    @ActivityScoped
    @Binds
    abstract Activity getActivity(EditProfileActivity activity);

    @Binds
    @ActivityScoped
    abstract EditProfileContract.ViewOperations getView(EditProfileActivity view);

    @Binds
    @ActivityScoped
    abstract EditProfileContract.PresenterOperations getPresenter(EditProfilePresenter presenter);
}
