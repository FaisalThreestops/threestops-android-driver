package com.delivx.app.main.support.subCategory;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class SubCatActivityModule {

    @Binds
    @ActivityScoped
    abstract Activity getContext(SupportSubCategoryActivity activity);

    @Binds
    @ActivityScoped
    abstract SubCatContract.ViewOperation getView(SupportSubCategoryActivity activity);

    @Binds
    @ActivityScoped
    abstract SubCatContract.PresenterOperation getPresenter(PresenterSubCat presenterSubCat);

}
