package com.delivx.mqttChat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DELL on 30-03-2018.
 */

public class ChatHistoryResponse implements Serializable {

    private String message;

    private ArrayList<ChatData> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ChatData> getData() {
        return data;
    }

    public void setData(ArrayList<ChatData> data) {
        this.data = data;
    }
}
