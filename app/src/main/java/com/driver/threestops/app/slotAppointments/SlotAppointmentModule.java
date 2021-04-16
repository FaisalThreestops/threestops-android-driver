package com.driver.threestops.app.slotAppointments;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

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
