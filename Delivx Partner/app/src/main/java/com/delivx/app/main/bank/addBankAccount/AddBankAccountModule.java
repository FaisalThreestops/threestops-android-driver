package com.delivx.app.main.bank.addBankAccount;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * Created by DELL on 23-03-2018.
 */

@Module
public abstract class AddBankAccountModule {
    @Binds
    @ActivityScoped
    abstract Activity getContext(BankNewAccountActivity  bankNewAccountActivity);

    @Binds
    @ActivityScoped
    abstract AddBankAccountContract.ViewOperation getView(BankNewAccountActivity  bankNewAccountActivity);

    @Binds
    @ActivityScoped
    abstract AddBankAccountContract.PresenterOperations getPresenter(BankNewAccountPresenter presenter);
}
