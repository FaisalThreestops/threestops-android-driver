package com.driver.threestops.walletNew;


import com.driver.threestops.walletNew.model.CreditDebitTransctions;

import java.util.ArrayList;

public interface WalletTransactionContract
{

    interface WalletTrasactionView
    {
        /**
         *<h2>walletTransactionsApiSuccessViewNotifier</h2>
         * <p> method to update fields data on the success response of api </p>
         */
        void walletTransactionsApiSuccessViewNotifier();

        /**
         * <h2>showToastNotifier</h2>
         * <p> method to trigger activity/fragment show progress dialog interface </p>
         * @param msg: message to be shown along with the progress dialog
         */
        void showProgressDialog(String msg);

        /**
         * <h2>showToastNotifier</h2>
         * <p> method to trigger activity/fragment showToast interface to show test </p>
         * @param msg: message to be shown in toast
         * @param duration: toast duration
         */
        void showToast(String msg, int duration);

        /**
         * <h2>showAlertNotifier</h2>
         * <p> method to trigger activity/fragment showAlertNotifier interface to show alert </p>
         * @param title: alert title to be setList
         * @param msg: alert message to be displayed
         */
        void showAlert(String title, String msg);

        /**
         * <H>Hide Progress bar</H>
         * <p>This method is using to hide the progress bar</p>
         */
        void hideProgressDialog();

        /**
         * <h>Set All transaction data to display</h>
         * <p>this method is using to set the all transaction data</p>
         * @param allTransactionsAL all transaction data
         */
        void setAllTransactionsAL(ArrayList<CreditDebitTransctions> allTransactionsAL);

        /**
         * <h>Set debit Transactions data to display</h>
         * <p>this method is using to set the debit  transaction data</p>
         * @param debitTransactionsAL debit transaction data
         */
        void setDebitTransactionsAL(ArrayList<CreditDebitTransctions> debitTransactionsAL);

        /**
         * <h>Set credit Transactions data to display</h>
         * <p>this method is using to set the credit  transaction data</p>
         * @param creditTransactionsAL credit transaction data
         */
        void setCreditTransactionsAL(ArrayList<CreditDebitTransctions> creditTransactionsAL);

        /**
         * <h2>setWallet</h2>
         * <p>set the walletBalance in the view</p>
         * @param walletBalance : total Balance
         * @param inr : CurrencySymbol
         */
        void setWallet(String walletBalance, String inr);
    }

    interface WalletTransactionPresenter
    {
        /**
         * <h>Show notification</h>
         * <p>this method is using to  Show notification to user</p>
         * @param msg notification message
         * @param duration duration
         */
        void showToastNotifier(String msg, int duration);

        /**
         *<h>Initialize transaction Api call</h>
         * <P>this method is using to initialize the Api call</P>
         * @param isToLoadMore is load more
         * @param isFromOnRefresh it from refresh
         */
        void initLoadTransactions(boolean isToLoadMore, boolean isFromOnRefresh);

        /**
         * <h2>getlanguageCode</h2>
         * <p>get the languageCode</p>
         * @return : languageCode
         */
        String getlanguageCode();

    }
}
