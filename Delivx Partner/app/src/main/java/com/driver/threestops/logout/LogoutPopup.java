package com.driver.threestops.logout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.driver.threestops.app.MyApplication;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.login.language.LanguagesList;
import com.driver.Threestops.R;
import com.driver.threestops.login.LoginActivity;
import com.driver.threestops.networking.NetworkService;
import com.driver.threestops.service.LocationUpdateService;
import com.driver.threestops.utility.AppConstants;
import com.driver.threestops.utility.Utility;


import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by embed on 1/12/15.
 */
public class LogoutPopup extends Dialog {
    private Context context;

    private ProgressDialog pDialog;
    private AlertDialog.Builder alertDialog;
    private String TAG = LogoutPopup.class.getSimpleName();

    PreferenceHelperDataSource preferenceHelperDataSource;


    NetworkService networkService;
   Activity activity;




    public LogoutPopup(Context context,PreferenceHelperDataSource dataSource,NetworkService networkService) {
        super(context);
        this.context = context;
        this.activity= (Activity) context;
        preferenceHelperDataSource=dataSource;
        this.networkService=networkService;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        sessionManager = SessionManager.getSessionManager(context);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getResources().getString(R.string.logging_out));

        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(context.getString(R.string.logout));
        alertDialog.setMessage(context.getString(R.string.logoutmessage));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(context.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logoutApi();
                    }
                });
        alertDialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        alertDialog.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


  public void logoutApi(){

        try {
//      pDialog.show();
            Observable<Response<ResponseBody>> logoutApi = networkService.logout(preferenceHelperDataSource.getLanguage(), ((MyApplication)activity.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()));

            logoutApi.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {

                            try {
                                JSONObject jsonObject;
                                if (value.code() == 200) {
                                    jsonObject = new JSONObject(value.body().string());
                                    Utility.printLog("pushTopics shared pref " + preferenceHelperDataSource.getPushTopic());
                                    Utility.subscribeOrUnsubscribeTopics(new JSONArray(preferenceHelperDataSource.getPushTopic()), false);
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    ((MyApplication) context.getApplicationContext()).disconnectMqtt();
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                    if (Utility.isMyServiceRunning(LocationUpdateService.class, (Activity) context)) {
                                        Intent stopIntent = new Intent(context, LocationUpdateService.class);
                                        stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                                        context.startService(stopIntent);
                                    }
                                    dismiss();
                                    ((Activity) context).finish();

                                } else {
                                    jsonObject = new JSONObject(value.errorBody().string());
                                    Utility.BlueToast(context, jsonObject.getString("message"));
                                }
                                Utility.printLog("logoutApi : " + jsonObject.toString());

                            } catch (Exception e) {
                                Utility.printLog("logoutApi : Catch :" + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            pDialog.dismiss();
                        }

                        @Override
                        public void onComplete() {
                            pDialog.dismiss();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

  }


}
