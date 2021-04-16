package com.driver.threestops.walletNew;



import android.app.Activity;

import com.driver.threestops.dagger.ActivityScoped;
import com.driver.threestops.dagger.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class WalletTransactionActivityDaggerModule
{
    @Binds
    @ActivityScoped
    abstract Activity getActivity(WalletTransActivity activity);

    @Binds
    @ActivityScoped
    abstract WalletTransactionContract.WalletTrasactionView provideWalletTransactionView(WalletTransActivity transActivity);

    @Binds
    @ActivityScoped
    abstract WalletTransactionContract.WalletTransactionPresenter provideWalletTransPresnter(WalletTransactionActivityPresenter transactionActivityPresenter);

    @ContributesAndroidInjector
    @FragmentScoped
    abstract WalletTransactionsFragment provideWalletListFragment();

}