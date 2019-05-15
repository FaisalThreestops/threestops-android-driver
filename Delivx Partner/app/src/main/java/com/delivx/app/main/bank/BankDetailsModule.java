package com.delivx.app.main.bank;

import com.delivx.dagger.ActivityScoped;
import com.delivx.dagger.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by DELL on 20-03-2018.
 */
@Module
public abstract class BankDetailsModule
{
    @FragmentScoped
    @ContributesAndroidInjector
    abstract BankListFrag bankListFrag();

    @ActivityScoped
    @Binds
    abstract BankDetailscontract.ViewOperations view(BankListFrag bankListFrag);

    @ActivityScoped
    @Binds
    abstract BankDetailscontract.PresenterOperations presenter(BankListFragPresenter presenter);
}
