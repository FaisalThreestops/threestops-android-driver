package com.delivx.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;


import com.facebook.FacebookSdk;
import com.delivx.dagger.AppComponent;
import com.delivx.dagger.DaggerAppComponent;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.managers.account.AccountGeneral;
import com.delivx.managers.mqtt.MQTTManager;
import com.delivx.networking.NetworkCheckerService;
import com.delivx.service.CouchDbHandler;
import com.delivx.utility.Utility;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;


/**
 * Created by Admin on 7/5/2017.
 */

public class MyApplication extends DaggerApplication {

    @Inject    CouchDbHandler couchDBHandler;
    @Inject    MQTTManager mqttManager;
    @Inject    PreferenceHelperDataSource preferenceHelperDataSource;

    private AccountManager mAccountManager;
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

        mAccountManager = AccountManager.get(getApplicationContext());

        initNetworkService();
    }
    public void connectMQTT(){
        if(preferenceHelperDataSource.isLoggedIn()){
            mqttManager.createMQttConnection(preferenceHelperDataSource.getDriverID()+preferenceHelperDataSource.getDeviceId());
        }
    }

    /*
     *Starting the network service for the internet checking. */
    private void initNetworkService()
    {
        startService(new Intent( this, NetworkCheckerService.class));
    }
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    public CouchDbHandler getDBHandler() {
        return couchDBHandler;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void disconnectMqtt(){
        mqttManager.disconnect();

    }


    /**
     * <h2>addAccount</h2>
     * This method is used to set the auth token by creating the account manager with the account
     *
     * @param emailID email ID to be added
     */
    public void setAuthToken(String emailID, String password, String authToken) {
        Account account = new Account(emailID, AccountGeneral.ACCOUNT_TYPE);
        mAccountManager.addAccountExplicitly(account, password, null);
        mAccountManager.setAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, authToken);
    }

    /**
     * <h2>getAuthToken</h2>
     * This method is used to get the auth token from the created account
     *
     * @return auth token stored
     */
    public String getAuthToken(String emailID) {
        Account[] account = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        List<Account> accounts = Arrays.asList(account);
        Utility.printLog( "auth token from size " + accounts.size() + " " + emailID);
        if (accounts.size() > 0) {
            for (int i = 0; i < accounts.size(); i++) {
                Utility.printLog(  "auth token from size " + accounts.get(i).name);
                if (accounts.get(i).name.equals(emailID))
                    return mAccountManager.peekAuthToken(accounts.get(i), AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
                else
                    removeAccount(accounts.get(i).name);
            }
        }
        return null;
    }

    /**
     * <h2>removeAccount</h2>
     * This method is used to remove the account stored
     *
     * @param emailID email ID of the account
     */
    public void removeAccount(String emailID) {
        Account[] account = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        List<Account> accounts = Arrays.asList(account);
        Utility.printLog( "auth token from size " + accounts.size() + " " + emailID);
        if (accounts.size() > 0) {
            for (int i = 0; i < accounts.size(); i++) {
                Utility.printLog( "auth token from size " + accounts.get(i).name);
                if (accounts.get(i).name.equals(emailID)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
                        Utility.printLog("account removed " + mAccountManager.removeAccountExplicitly(accounts.get(i)));
                }
            }
        }
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    public void publishMqtt(JSONObject reqObject)
    {
        mqttManager.publish(reqObject);
    }

    public boolean isMQTTConnected()
    {
        return mqttManager.isMQTTConnected();
    }

    public void unSubscribeMqtt(String topic)
    {
        mqttManager.unSubscribeToTopic(topic);
    }
}

