package com.driver.threestops.wallet.voucher;


public interface VoucherPresenter {

    void attachView(VoucherBottomSheet view);
    void clearData();
    void addAmount(String amt);

}
