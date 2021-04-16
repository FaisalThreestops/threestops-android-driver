package com.driver.threestops.payment_choose_card;

import com.driver.threestops.BaseView;
import com.driver.threestops.payment.Cards;
import java.util.ArrayList;


public interface ChoosePaymentView extends BaseView
{

    void onError(String msg);
    void setPaymentCardsList(ArrayList<Cards> cardsList);
    void showCashBtn();
    void hideCashBtn();
    void startAddCardAct();
    void showWalletPayment();
    void  payWallet(String payWallet);
    void hideWalletPay();
    void setWalletAmount(String amount);
    void addWallet();
    void disableWalletPay();
    void enableWalletPay();
}
