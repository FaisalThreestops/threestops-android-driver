package com.delivx.app.bookingRequest;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class BookingPopupModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(BookingPopUp activity);

    @Binds
    @ActivityScoped
    abstract   BookingPopUpMainMVP.ViewOperations getView(BookingPopUp view);

    @Binds
    @ActivityScoped
    abstract  BookingPopUpMainMVP.PresenterOperations getPresenter(Presenter presenter);
}
