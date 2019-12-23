package com.delivx.wallet.voucher;


public interface VoucherPresenter {

    void attachView(VoucherBottomSheet view);
    void clearData();
    void addAmount(String amt);

}
