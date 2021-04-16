package com.driver.threestops.app.main.history.orderDetails;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class OrderHistoryModule {
    @Binds
    @ActivityScoped
    abstract Activity getContext(HistoryOrderDetailsNew historyOrderDetails);

    @Binds
    @ActivityScoped
    abstract OrderHistoryContract.ViewOperation getView(HistoryOrderDetailsNew  historyOrderDetails);

    @Binds
    @ActivityScoped
    abstract OrderHistoryContract.PresenterOperation getPresenter(OrderHistoryPresenter presenter);
}
