package com.driver.threestops.app.invoice;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class InvoiceModule {
    @ActivityScoped
    @Binds
    abstract Activity getActivity(InvoiceActivity activity);

    @Binds
    @ActivityScoped
    abstract InvoiceContract.ViewOpetaions getView(InvoiceActivity view);

    @Binds
    @ActivityScoped
    abstract InvoiceContract.PresenterOpetaions getPresenter(InvoicePresenter presenter);
}
