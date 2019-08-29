package com.delivx.app.SlotAppointments;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class SlotAppointmentModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(SlotAppointmentActivity activity);

    @Binds
    @ActivityScoped
    abstract SlotContract.ViewOperations getView(SlotAppointmentActivity view);

    @Binds
    @ActivityScoped
    abstract SlotContract.PresenterOperations getPresenter(SlotAppointmentPresenter presenter);
}
