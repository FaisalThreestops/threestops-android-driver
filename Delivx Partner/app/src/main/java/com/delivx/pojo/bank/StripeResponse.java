package com.delivx.pojo.bank;

import java.io.Serializable;

/**
 * Created by DELL on 23-03-2018.
 */

public class StripeResponse implements Serializable {

    private String message;

    private StripeDetailsPojo data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StripeDetailsPojo getData() {
        return data;
    }

    public void setData(StripeDetailsPojo data) {
        this.data = data;
    }
}
