package com.driver.threestops.mqttChat;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class ChatDataObervable extends Observable<JSONObject>
{
    private static ChatDataObervable chatDataObervable;

    Observer<?super JSONObject> observer;

    public static ChatDataObervable getInstance()
    {
        if(chatDataObervable==null)
        {
            chatDataObervable  = new ChatDataObervable();
            return chatDataObervable;
        }
        else
        {
            return chatDataObervable;
        }
    }
    @Override
    protected void subscribeActual(Observer<? super JSONObject> observer)
    {
        this.observer = observer;
    }
    public void emitData(JSONObject cData)
    {
        observer.onNext(cData);
        observer.onComplete();
    }

}
