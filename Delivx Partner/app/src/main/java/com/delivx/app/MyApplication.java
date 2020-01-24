package com.delivx.app;

import android.content.Context;
import androidx.multidex.MultiDex;

import com.delivx.login.language.LanguagesList;
import com.delivx.networking.ConnectivityReceiver;
import com.delivx.utility.Utility;
import com.facebook.FacebookSdk;
import com.delivx.dagger.AppComponent;
import com.delivx.dagger.DaggerAppComponent;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.managers.mqtt.MQTTManager;
import com.delivx.service.CouchDbHandler;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;



public class MyApplication extends DaggerApplication {

    CouchDbHandler couchDBHandler;
    @Inject    MQTTManager mqttManager;
    @Inject    PreferenceHelperDataSource preferenceHelperDataSource;
    private static MyApplication myApplication;

    @Override
    public void onLowMemory() {
        System.gc();
        super.onLowMemory();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
        myApplication=this;

        Utility.printLog(" preferenceHelperDataSource.getLanguageSettings() "+
                preferenceHelperDataSource.getLanguageSettings());
        if (preferenceHelperDataSource.getLanguageSettings() == null)
            preferenceHelperDataSource.setLanguageSettings(new LanguagesList("en","English"));
        else
            Utility.changeLanguageConfig(preferenceHelperDataSource.getLanguageSettings().getLanguageCode(),this);


        couchDBHandler = new CouchDbHandler(MyApplication.this);

    }


    public CouchDbHandler getDBHandler() {
        return couchDBHandler;
    }

    public void connectMQTT(){
        if(preferenceHelperDataSource.isLoggedIn()){
            mqttManager.createMQttConnection(preferenceHelperDataSource.getDriverID()+preferenceHelperDataSource.getDeviceId());
        }
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void disconnectMqtt(){
        mqttManager.disconnect();

    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    public boolean isMQTTConnected()
    {
        return mqttManager.isMQTTConnected();
    }

    public void publishMqtt(JSONObject reqObject)
    {
        mqttManager.publish(reqObject);
    }

    public void unSubscribeMqtt(String topic)
    {
        mqttManager.unSubscribeToTopic(topic);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}

