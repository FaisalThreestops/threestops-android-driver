package com.driver.threestops.app.main.bank;

import com.driver.threestops.dagger.ActivityScoped;
import com.driver.threestops.dagger.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

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
