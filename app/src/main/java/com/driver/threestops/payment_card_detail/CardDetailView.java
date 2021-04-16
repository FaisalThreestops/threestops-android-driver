package com.driver.threestops.payment_card_detail;

import com.driver.threestops.BaseView;

public interface CardDetailView extends BaseView {

    void setErrorMsg(String errorMsg);

    void navToPayment();
    void setCardData(String brand, String cardNo, String exp);

}
