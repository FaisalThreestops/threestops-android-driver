package com.delivx.payment_add_card;

import com.stripe.android.model.Card;

/**
 * @author Pramod
 * @since 31-01-2018.
 */

public interface AddCardPresenter {

    void addCard(Card card);
}
