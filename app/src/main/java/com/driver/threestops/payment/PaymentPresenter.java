package com.driver.threestops.payment;


/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public interface PaymentPresenter {
    void callApi();
    void addNewCard();
    void getCardDetail(String brand, String id, String lastDigit, int mn, int yr, boolean isDefault);
    void startShopping();
}
