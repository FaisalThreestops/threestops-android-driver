package com.delivx.RxObservers;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class RXMqttMessageObserver extends Observable<JSONObject> {

    static RXMqttMessageObserver changeObserver=null;
    Observer<? super JSONObject> observer;
    ArrayList<Observer<? super JSONObject>> observers=new ArrayList<>();
    private RXMqttMessageObserver() {
    }

    public static RXMqttMessageObserver getInstance(){
        if(changeObserver==null){
            changeObserver=new RXMqttMessageObserver();
        }
        return changeObserver;
    }
    @Override
    protected void subscribeActual(Observer<? super JSONObject> observer) {
        this.observer=observer;
        if(!observers.contains(observer))
            observers.add(observer);
    }

    public void emit(JSONObject jsonObject){
        if(observer!=null){
//            observer.onNext(jsonObject);
            if(observers.size()>0){
                for(int i=0;i<observers.size();i++){
                    observers.get(i).onNext(jsonObject);
                }
            }
        }
    }
}
