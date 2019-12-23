package com.delivx.wallet.voucher;


import com.delivx.dagger.FragmentScoped;
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
