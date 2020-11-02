package com.driver.threestops.payment_add_card;

import com.stripe.android.model.CardParams;

/**
 * @author Pramod
 * @since 31-01-2018.
 */

public interface AddCardPresenter {

    void addCard(CardParams card);
}
