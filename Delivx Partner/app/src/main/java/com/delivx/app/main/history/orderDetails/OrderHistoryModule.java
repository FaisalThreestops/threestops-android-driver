package com.delivx.app.main.history.orderDetails;

import android.app.Activity;

import com.delivx.app.main.history.HistoryOrderDetailsNew;
import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 16-03-2018.
 */
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
