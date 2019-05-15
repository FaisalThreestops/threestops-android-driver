package com.delivx.app.bookingRide;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 05-02-2018.
 */

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
