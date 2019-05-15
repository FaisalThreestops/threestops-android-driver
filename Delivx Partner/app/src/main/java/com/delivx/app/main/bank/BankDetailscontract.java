package com.delivx.app.main.bank;

import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.bank.BankList;
import com.delivx.pojo.bank.LegalEntity;

import java.util.ArrayList;

/**
 * Created by DELL on 20-03-2018.
 */

public interface BankDetailscontract {

    interface ViewOperations extends BaseView{

        void onFailure(String msg);

        void onFailure();

        void onSuccess(LegalEntity legalEntity, ArrayList<BankList> bankLists);

        void showAddStipe();

        void moveToAddAccountActivity();

        void moveToNewStripeActivity(Bundle bundleBankDetails);
    }

    interface PresenterOperations{

        void attachView(BankDetailscontract.ViewOperations bankListFrag);

        void addNewAccount();

        void addNewStripeAccount();
    }
}
