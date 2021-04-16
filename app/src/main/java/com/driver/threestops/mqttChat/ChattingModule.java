package com.driver.threestops.mqttChat;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class ChattingModule {

    @ActivityScoped
    @Binds
    abstract Activity getActivity(ChattingActivity activity);

    @Binds
    @ActivityScoped
    abstract   ChattingContract.ViewOperations getView(ChattingActivity view);

    @Binds
    @ActivityScoped
    abstract  ChattingContract.PresenterOperations getPresenter(Presenter presenter);
}
