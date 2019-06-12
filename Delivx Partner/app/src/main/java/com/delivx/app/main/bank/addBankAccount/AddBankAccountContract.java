package com.delivx.app.main.bank.addBankAccount;

import com.delivx.BaseView;

/**
 * Created by DELL on 23-03-2018.
 */

public interface AddBankAccountContract {

    interface ViewOperation extends BaseView
    {
        void onSuccess(String msg);

        void onFailure();

        void setNameError();

        void setAccError();

        void setRoutingError();

        void disableError();
    }

    interface PresenterOperations{

        void validateFields(String name, String acc, String routing);

        String getlanguageCode();

    }
}
