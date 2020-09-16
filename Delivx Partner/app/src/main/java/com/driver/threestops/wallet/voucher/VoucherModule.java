package com.driver.threestops.wallet.voucher;


import com.driver.threestops.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class VoucherModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract VoucherBottomSheet getVoucherSheet();

    @Binds
    abstract VoucherPresenter getVoucherPresenter(VoucherPresenterImpl presenter);
}
