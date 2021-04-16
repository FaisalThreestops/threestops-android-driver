package com.driver.threestops.app.main.bank.stripe;

import java.io.Serializable;

public class GetBankDetailsPojo implements Serializable {
    private GetBankData data;

    private String message;

    public GetBankData getData ()
    {
        return data;
    }

    public void setData (GetBankData data)
    {
        this.data = data;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", message = "+message+"]";
    }
}
