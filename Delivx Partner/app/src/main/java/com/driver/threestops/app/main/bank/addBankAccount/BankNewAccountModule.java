package com.driver.threestops.app.main.bank.addBankAccount;

import android.app.Activity;


import com.driver.threestops.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class BankNewAccountModule {
    @ActivityScoped
    @Binds
    abstract Activity activity(BankNewAccountActivity bankNewAccountActivity);

    @ActivityScoped
    @Binds
    abstract BankNewAccountContract.BankNewAccountPresenter getPresenter(BankNewAccountPresenter bankNewAccountPresenter);

    @ActivityScoped
    @Binds
    abstract BankNewAccountContract.BankNewAccountView getView(BankNewAccountActivity bankNewAccountActivity);
}
