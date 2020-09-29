package com.driver.threestops.app.main.bank.addBankAccount;


import com.driver.threestops.BaseView;

public interface BankNewAccountContract {

    /**
     * <h1>BankNewAccountView</h1>
     * <p>interface for view</p>
     */
    interface BankNewAccountView extends BaseView {

        void initActionBar();

        void setTitle();

        void editTextErr(String errorEditText);

        /**
         * <h1>externalAccountAPISuccess</h1>
         * <p>for inform the Bank Account Add is success</p>
         *
         * @param msg : Success message
         */
        void externalAccountAPISuccess(String msg);
    }

    /**
     * <h1>BankNewAccountPresenter</h1>
     * <p>interface for the BookingPopUpPresenter or Implementation</p>
     */
    interface BankNewAccountPresenter {

        /**
         * setting the toolBar
         */
        void setActionBar();

        /**
         * <p>setting teh ActionBar titles</p>
         */
        void setActionBarTitle();


        /**
         * <h1>validateData</h1>
         * <p>Check the data input correct or not</p>
         *
         * @param name
         * @param phone     :Name
         * @param AccountNo : Account Number
         * @param RoutingNo : Routing Number
         * @param address
         * @param city
         * @param state
         * @param pinCode
         */
        void validateData(String name, String phone, String AccountNo, String RoutingNo,
                          String address, String city, String state, String pinCode);

        /**
         * <h1>externalAccountAPI</h1>
         * <p>API call for adding the BankAccount </p>
         */
        void externalAccountAPI(String name, String Phone, String AccountNo,
                                String RoutingNo, String address, String city, String state, String pinCode);

        /**
         * <h1>getlanguageCode</h1>
         * <p>It's return app current language code</p>
         * @return string app current langauge code
         */
        String getlanguageCode();
    }
}
