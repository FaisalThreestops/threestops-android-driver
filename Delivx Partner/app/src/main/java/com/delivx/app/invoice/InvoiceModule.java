package com.delivx.app.invoice;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

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
