package com.driver.threestops.app.main.bank.stripe;

import java.io.Serializable;

public class GetBankData implements Serializable {

    private String bankAccount;

    private String phone;

    private String name;

    private String beneId;

    private String email;

    private String status;

    public String getBankAccount ()
    {
        return bankAccount;
    }

    public void setBankAccount (String bankAccount)
    {
        this.bankAccount = bankAccount;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getBeneId ()
    {
        return beneId;
    }

    public void setBeneId (String beneId)
    {
        this.beneId = beneId;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [bankAccount = "+bankAccount+", phone = "+phone+", name = "+name+", beneId = "+beneId+", email = "+email+", status = "+status+"]";
    }

}
