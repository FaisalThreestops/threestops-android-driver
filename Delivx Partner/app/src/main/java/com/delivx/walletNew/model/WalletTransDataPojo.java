package com.delivx.walletNew.model;

import java.io.Serializable;
import java.util.ArrayList;

public class WalletTransDataPojo implements Serializable
{
    private String walletBalance;
    private String currencySymbol;

    private ArrayList<CreditDebitTransctions> debitTransctions;

    private ArrayList<CreditDebitTransctions> creditTransctions;

    private ArrayList<CreditDebitTransctions> creditDebitTransctions;

    public ArrayList<CreditDebitTransctions> getDebitTransctions() {
        return debitTransctions;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public void setDebitTransctions(ArrayList<CreditDebitTransctions> debitTransctions) {
        this.debitTransctions = debitTransctions;
    }

    public ArrayList<CreditDebitTransctions> getCreditTransctions() {
        return creditTransctions;
    }

    public void setCreditTransctions(ArrayList<CreditDebitTransctions> creditTransctions) {
        this.creditTransctions = creditTransctions;
    }

    public ArrayList<CreditDebitTransctions> getCreditDebitTransctions() {
        return creditDebitTransctions;
    }

    public void setCreditDebitTransctions(ArrayList<CreditDebitTransctions> creditDebitTransctions) {
        this.creditDebitTransctions = creditDebitTransctions;
    }
}
