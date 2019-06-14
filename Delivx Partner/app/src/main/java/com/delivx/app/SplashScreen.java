package com.delivx.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.delivx.app.main.MainActivity;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.login.LoginActivity;
import com.driver.delivx.R;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.fabric.sdk.android.Fabric;

/**************************************************************************************************/
public class SplashScreen extends DaggerAppCompatActivity {

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    private Runnable runnable;
    private Handler handler;


    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);

        preferenceHelperDataSource.setFCMRegistrationId(FirebaseInstanceId.getInstance().getToken());
        runnable=new Runnable() {

            /*
             * Showing splash screen with a timer. For showing app logo
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start app
                checkConfiguration();

            }
        };
        handler=new Handler();
        handler.postDelayed(runnable, 2000);






        //initializeViews();

    }
    /**
     * <h2>checkConfiguration</h2>
     * <p>check user is logged in or not and
     * based on status it corresponding activity will open</p>
     */
    private void checkConfiguration() {
        //
        if (preferenceHelperDataSource.isLoggedIn()/*&&!preferenceHelperDataSource.getVehicleId().isEmpty()*/) {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        /*if(runnable!=null && handler!=null){
            handler.removeCallbacks(runnable);
        }*/
    }


}
