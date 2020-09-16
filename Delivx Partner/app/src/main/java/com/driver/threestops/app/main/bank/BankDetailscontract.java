package com.driver.threestops.app.main.bank;

import android.os.Bundle;

import com.driver.threestops.BaseView;
import com.driver.threestops.pojo.bank.BankList;
import com.driver.threestops.pojo.bank.LegalEntity;

import java.util.ArrayList;


public interface BankDetailscontract {

    interface ViewOperations extends BaseView{

        /**
         * <h2>onFailure</h2>
         * <p>if failure in API call</p>
         * @param msg : error message
         */
        void onFailure(String msg);

        /**
         * <h2>onFailure</h2>
         * <p>Toast the error message</p>
         */
        void onFailure();

        /**
         * <h2>onSuccess</h2>
         * <p>getting the bank details</p>
         * @param legalEntity : verified or reject
         * @param bankLists : bank details
         */
        void onSuccess(LegalEntity legalEntity, ArrayList<BankList> bankLists);

        /**
         * <h2>showAddStipe</h2>
         * <p>show the AddStripe view visibility</p>
         */
        void showAddStipe();

        /**
         * <h2>moveToAddAccountActivity</h2>
         * <p>moving to next (BankNewAccountActivity) adding new bank account</p>
         */
        void moveToAddAccountActivity();

        /**
         * <h2>moveToNewStripeActivity</h2>
         * <p>moving to next (BankNewStripeActivity) for adding new stripe account</p>
         * @param bundleBankDetails : stripeDetailsPojo details
         */
        void moveToNewStripeActivity(Bundle bundleBankDetails);
    }

    interface PresenterOperations{

        /**
         * <h2>attachView</h2>
         * <p>attach the BankListFrag view to BankListFragPresenter</p>
         * @param bankListFrag : BankListFrag view
         */
        void attachView(BankDetailscontract.ViewOperations bankListFrag);

        /**
         * <h2>addNewAccount</h2>
         * <p>adding the new bank account</p>
         */
        void addNewAccount();

        /**
         * <h2>addNewStripeAccount</h2>
         * <p>adding the new stripe account</p>
         */
        void addNewStripeAccount();

        /**
         * <h2>getBankDetails</h2>
         * <p>getting the bank details</p>
         */
        void getBankDetails();
    }
}
