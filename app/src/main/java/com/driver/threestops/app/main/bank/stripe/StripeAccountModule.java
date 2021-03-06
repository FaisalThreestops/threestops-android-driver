package com.driver.threestops.app.main.bank.stripe;

import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class StripeAccountModule {
    @Binds
    @ActivityScoped
    abstract Activity getContext(BankNewStripeActivity  bankNewStripeActivity);

    @Binds
    @ActivityScoped
    abstract StripeAccountContract.ViewOperations getView(BankNewStripeActivity  bankNewStripeActivity);

    @Binds
    @ActivityScoped
    abstract StripeAccountContract.PresenterOperations getPresenter(BankNewStripePresenter presenter);
}


