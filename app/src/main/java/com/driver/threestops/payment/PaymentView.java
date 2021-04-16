package com.driver.threestops.payment;

import com.driver.threestops.BaseView;
import java.util.ArrayList;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public interface PaymentView extends BaseView {
    void setPaymentCardsList(ArrayList<Cards> paymentCardsList);
    void startAddCardAct();
    void startCardDetail(String brand, String id, String lastDigit, int mn, int yr,
        boolean isDefault);
    void onError(String msg);
    void startShopping();

}
