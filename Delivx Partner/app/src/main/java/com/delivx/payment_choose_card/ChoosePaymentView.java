package com.delivx.payment_choose_card;

import com.delivx.BaseView;
import com.delivx.payment.CardData;
import com.delivx.payment.Cards;
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
