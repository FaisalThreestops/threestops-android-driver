package com.delivx.app.main.bank.addBankAccount;

import com.delivx.BaseView;


public interface AddBankAccountContract {

    interface ViewOperation extends BaseView
    {
        /**
         * <h2>setNameError</h2>
         * <p>displaying the name error message </p>
         */
        void setNameError();

        /**
         * <h2>setAccError</h2>
         * <p>displaying the account number error message</p>
         */
        void setAccError();

        /**
         * <h2>setRoutingError</h2>
         * <p>displaying the routing number error message</p>
         */
        void setRoutingError();

        /**
         * <h2>disableError</h2>
         * <p>disabling the error message</p>
         */
        void disableError();
    }

    interface PresenterOperations{

        /**
         * <h2>validateFields</h2>
         * <p>validating the all fields</p>
         * @param name : account holder name
         * @param acc : account number
         * @param routing : routing number
         */
        void validateFields(String name, String acc, String routing);

        /**
         * <h2>getlanguageCode</h2>
         * <p>get the language code</p>
         * @return : language code
         */
        String getlanguageCode();

    }
}
