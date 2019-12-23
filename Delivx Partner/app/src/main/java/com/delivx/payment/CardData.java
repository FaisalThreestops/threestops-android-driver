package com.delivx.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dell on 06-Mar-18.
 */

public class CardData implements Serializable {

    private ArrayList<Cards> cards;

    public ArrayList<Cards> getCards ()
    {
        return cards;
    }

    public void setCards (ArrayList<Cards> cards)
    {
        this.cards = cards;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [cards = "+cards+"]";
    }
}
