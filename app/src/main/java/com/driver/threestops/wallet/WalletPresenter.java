package com.driver.threestops.wallet;

/**
 * Created by ${3embed} on ${27-10-2017}.
 */

public interface WalletPresenter {
    void callApi();
    void getcards();
    void setAmount(String amt);
    String getLanguage();
    void chooseCard(String amt, String cardId);
    void startHistory();
    void addCard();
    void start();
    String getCurrency();
    void chooseCard(String amount);

}
