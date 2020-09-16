package com.driver.threestops.payment_choose_card;




public interface ChoosePaymentPresenter {
    void getCards();
    void showCashBtn();
    void hideCashBtn();
    void addNewCard();
    String getLanguage();
    void showWalletPayment();
    void payWallet();
    void addMoneyWallet();
    boolean canSelectWallet();

}



