package com.delivx.walletNew;



import com.delivx.dagger.ActivityScoped;
import com.delivx.dagger.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class WalletTransactionActivityDaggerModule
{
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