package com.delivx.payment_card_detail;



/**
 * @author Pramod
 * @since 31-01-2018.
 */

public interface CardDetailPresenter{

    void deleteCard(String cardId);

    void makeDefault(String cardId);

    void getIntentData(String id, String brand, String lastDigit, int mn, int yr);
}
