package com.delivx.app.main.bank.stripe;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 22-03-2018.
 */


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


