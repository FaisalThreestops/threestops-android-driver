package com.driver.threestops.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.driver.Threestops.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.driver.threestops.app.main.MainActivity;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.login.LoginActivity;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class SplashScreen extends DaggerAppCompatActivity {

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    private Runnable runnable;
    private Handler handler;
    String response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        preferenceHelperDataSource.setFCMRegistrationId(FirebaseInstanceId.getInstance().getToken());
        /*FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        preferenceHelperDataSource.setFCMRegistrationId(token);

                    }
                });
*/
     //  getBundleData(getIntent().getExtras());
        /*
         * Showing splash screen with a timer. For showing app logo
         */
        // This method will be executed once the timer is over
        // Start app
        runnable = this::checkConfiguration;
        handler = new Handler();
        handler.postDelayed(runnable, 2000);

        //initializeViews();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    /**
     * <h2>checkConfiguration</h2>
     * <p>check user is logged in or not and
     * based on status it corresponding activity will open</p>
     */
    private void checkConfiguration() {
        if (preferenceHelperDataSource.isLoggedIn()/*&&!preferenceHelperDataSource.getVehicleId().isEmpty()*/) {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.putExtra("booking_Data", response);
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
