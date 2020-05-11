package com.delivx.app.main.help_center.zendeskpojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ali on 12/29/2017.
 */

public class AllTicket implements Serializable
{
    /*{
        "data": {
        "open": [],
        "close": []
    }
    }*/

    private AllTicketData data;

    public AllTicketData getData() {
        return data;
    }

    public class AllTicketData
    {
        /* "open": [],
        "close": []*/

        public ArrayList<TicketOpen>open;
        public ArrayList<TicketClose>close;

        public ArrayList<TicketOpen> getOpen() {
            return open;
        }

        public ArrayList<TicketClose> getClose() {
            return close;
        }
    }
}
