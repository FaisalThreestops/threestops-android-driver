package com.delivx.app.main.bank.stripe;

/**
 * Created by murashid on 25-Aug-17.
 */

public class BankNewStripeModel {

    private static final String TAG = "BankNewStripeModel";

/*
    void addBankAccount(String token, JSONObject jsonObject) {
        OkHttp3Connection.doOkHttp3Connection(token, ServiceUrl.FETCH_ADD_STRIPE_ACCOUNT, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    if (result != null) {
                        Log.d(TAG, "onSuccess: " + result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("errFlag").equals("0")) {
                            bankNewStripModelImplement.onSuccess(jsonObject.getString("errMsg"));
                        } else {
                            bankNewStripModelImplement.onFailure();
                        }
                    } else {
                        bankNewStripModelImplement.onFailure();
                    }
                } catch (Exception e) {
                    bankNewStripModelImplement.onFailure();
                }
            }

            @Override
            public void onError(String error) {
                bankNewStripModelImplement.onFailure();
            }
        }, token);
    }
*/



}
