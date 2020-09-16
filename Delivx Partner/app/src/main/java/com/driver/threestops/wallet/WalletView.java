package com.driver.threestops.wallet;


import com.driver.threestops.BaseView;
import com.driver.threestops.payment.Cards;
import java.util.ArrayList;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public interface WalletView  extends BaseView
{

    void setFixedAmount(String amountCurrency, String softLimit, String hardLimit);
    void setLimitAmount(String softLimit, String hardLimit);
    void setTotalBalance(String balance);
    void addAmt(String amt);
    void startWalletHistory();

    void setPaymentCardsList(ArrayList<Cards> cardsList);
    void onError(String msg);
    void startAddCardAct();
    void showSuccessAlert();
}
