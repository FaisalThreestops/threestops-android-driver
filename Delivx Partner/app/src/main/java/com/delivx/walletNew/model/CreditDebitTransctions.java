package com.delivx.walletNew.model;



public class CreditDebitTransctions
{
    private String txnType;

    private String trigger;

    private String openingBal;

    private String tripId;

    private String paymentType;

    private String intiatedBy;

    private String txnId;

    private String currencySymbol;

    private String amount;

    private String txnDate;

    private String paymentTxnId;

    private String comment;

    private String closingBal;

    private String currencyAbbr;

    public String getTxnType ()
    {
        return txnType;
    }

    public void setTxnType (String txnType)
    {
        this.txnType = txnType;
    }

    public String getTrigger ()
    {
        return trigger;
    }

    public void setTrigger (String trigger)
    {
        this.trigger = trigger;
    }

    public String getOpeningBal ()
    {
        return openingBal;
    }

    public void setOpeningBal (String openingBal)
    {
        this.openingBal = openingBal;
    }

    public String getTripId ()
    {
        return tripId;
    }

    public void setTripId (String tripId)
    {
        this.tripId = tripId;
    }

    public String getPaymentType ()
    {
        return paymentType;
    }

    public void setPaymentType (String paymentType)
    {
        this.paymentType = paymentType;
    }

    public String getIntiatedBy ()
    {
        return intiatedBy;
    }

    public void setIntiatedBy (String intiatedBy)
    {
        this.intiatedBy = intiatedBy;
    }

    public String getTxnId ()
    {
        return txnId;
    }

    public void setTxnId (String txnId)
    {
        this.txnId = txnId;
    }

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getTimestamp ()
    {
        return txnDate;
    }

    public void setTimestamp (String timestamp)
    {
        this.txnDate = timestamp;
    }

    public String getPaymentTxnId ()
    {
        return paymentTxnId;
    }

    public void setPaymentTxnId (String paymentTxnId)
    {
        this.paymentTxnId = paymentTxnId;
    }

    public String getComment ()
    {
        return comment;
    }

    public void setComment (String comment)
    {
        this.comment = comment;
    }

    public String getClosingBal ()
    {
        return closingBal;
    }

    public void setClosingBal (String closingBal)
    {
        this.closingBal = closingBal;
    }

    public String getCurrencyAbbr() {
        return currencyAbbr;
    }

    public void setCurrencyAbbr(String currencyAbbr) {
        this.currencyAbbr = currencyAbbr;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }
}
