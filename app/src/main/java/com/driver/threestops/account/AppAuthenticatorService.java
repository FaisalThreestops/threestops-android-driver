package com.driver.threestops.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created with IntelliJ IDEA.
 * User: Udini
 * Date: 19/03/13
 * Time: 19:10
 */
public class AppAuthenticatorService extends Service
{
    @Override
    public IBinder onBind(Intent intent) {

        AppAuthenticator authenticator = new AppAuthenticator(this);
        return authenticator.getIBinder();
    }
}
