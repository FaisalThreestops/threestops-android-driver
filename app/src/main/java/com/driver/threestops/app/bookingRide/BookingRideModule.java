package com.driver.threestops.app.bookingRide;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class BookingRideModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(BookingRide activity);

    @Binds
    @ActivityScoped
    abstract   BookingRideContract.ViewOperations getView(BookingRide view);

    @Binds
    @ActivityScoped
    abstract  BookingRideContract.PresenterOperations getPresenter(BookingRidePresenter bookingRidePresenter);
}
