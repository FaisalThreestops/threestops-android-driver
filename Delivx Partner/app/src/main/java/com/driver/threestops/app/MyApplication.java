package com.driver.threestops.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.driver.threestops.account.AccountGeneral;
import com.driver.threestops.login.language.LanguagesList;
import com.driver.threestops.networking.ConnectivityReceiver;
import com.driver.threestops.utility.Utility;
import com.facebook.FacebookSdk;
import com.driver.threestops.dagger.AppComponent;
import com.driver.threestops.dagger.DaggerAppComponent;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.managers.mqtt.MQTTManager;
import com.driver.threestops.service.CouchDbHandler;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;



public class MyApplication extends DaggerApplication {

    CouchDbHandler couchDBHandler;
    @Inject    MQTTManager mqttManager;
    @Inject    PreferenceHelperDataSource preferenceHelperDataSource;
    private static MyApplication myApplication;
    private AccountManager mAccountManager;

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
        mAccountManager = AccountManager.get(getApplicationContext());

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

    /*
    <h2>getAuthToken</h2>
    This method is used to get the auth token from the created account
    @return  auth token stored
    */

    public String getAuthToken(String emailID) {
        Account[] account = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        List<Account> accounts = Arrays.asList(account);
        if (accounts.size() > 0) {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).name.equals(emailID)) {
                    return mAccountManager.peekAuthToken(accounts.get(i), AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
                }
            }
        }
        return null;
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
}

