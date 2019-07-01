package com.delivx.pojo.bank;

import java.io.Serializable;



public class BankList implements Serializable {
    private String id;
    private String object;
    private String account;
    private String account_holder_name;
    private String account_holder_type;
    private String bank_name;
    private String country;
    private String currency;
    private String default_for_currency;
    private String fingerprint;
    private String last4;
    private String routing_number;
    private String status;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount_holder_name() {
        return account_holder_name;
    }

    public void setAccount_holder_name(String account_holder_name) {
        this.account_holder_name = account_holder_name;
    }

    public String getAccount_holder_type() {
        return account_holder_type;
    }

    public void setAccount_holder_type(String account_holder_type) {
        this.account_holder_type = account_holder_type;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDefault_for_currency() {
        return default_for_currency;
    }

    public void setDefault_for_currency(String default_for_currency) {
        this.default_for_currency = default_for_currency;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getRouting_number() {
        return routing_number;
    }

    public void setRouting_number(String routing_number) {
        this.routing_number = routing_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
