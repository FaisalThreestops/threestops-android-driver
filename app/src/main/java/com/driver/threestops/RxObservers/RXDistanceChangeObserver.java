package com.driver.threestops.RxObservers;

import org.json.JSONArray;

import io.reactivex.Observable;
import io.reactivex.Observer;


public class RXDistanceChangeObserver extends Observable<JSONArray> {

    static RXDistanceChangeObserver changeObserver=null;
    Observer<? super JSONArray> observer;
    private RXDistanceChangeObserver()
    {
    }

    public static RXDistanceChangeObserver getInstance(){
        if(changeObserver==null){
            changeObserver=new RXDistanceChangeObserver();
        }
        return changeObserver;
    }


    @Override
    protected void subscribeActual(Observer<? super JSONArray> observer) {
        this.observer=observer;
    }

    public void emit(JSONArray jsonArray){
        if(observer!=null){
            observer.onNext(jsonArray);
        }
    }

}
