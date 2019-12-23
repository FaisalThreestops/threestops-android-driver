package com.delivx.payment_card_detail;

import com.delivx.BaseView;

public interface CardDetailView extends BaseView {

    void setErrorMsg(String errorMsg);

    void navToPayment();
    void setCardData(String brand, String cardNo, String exp);

}
