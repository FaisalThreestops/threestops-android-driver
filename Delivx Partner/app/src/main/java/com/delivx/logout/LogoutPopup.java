package com.delivx.logout;

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

import com.delivx.app.MyApplication;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.login.LoginActivity;
import com.delivx.networking.NetworkService;
import com.delivx.service.LocationUpdateService;
import com.delivx.utility.AppConstants;
import com.delivx.utility.Utility;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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




    public LogoutPopup(Context context,PreferenceHelperDataSource dataSource,NetworkService networkService) {
        super(context);
        this.context = context;
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

      pDialog.show();
      Observable<Response<ResponseBody>> logoutApi=networkService.logout(preferenceHelperDataSource.getLanguage(),preferenceHelperDataSource.getToken());

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
                          if(value.code()==200){
                              jsonObject=new JSONObject(value.body().string());
                              Utility.printLog("pushTopics shared pref "+preferenceHelperDataSource.getPushTopic());
                              Utility.subscribeOrUnsubscribeTopics(new JSONArray(preferenceHelperDataSource.getPushTopic()),false);
                              preferenceHelperDataSource.clearSharedPredf();
                              ((MyApplication)context.getApplicationContext()).disconnectMqtt();
                              context.startActivity(new Intent(context, LoginActivity.class));
                              if(Utility.isMyServiceRunning(LocationUpdateService.class,(Activity) context))
                              {
                                  Intent stopIntent = new Intent(context, LocationUpdateService.class);
                                  stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                                  context.startService(stopIntent);
                              }
                              dismiss();
                              ((Activity) context).finish();

                          }else {
                              jsonObject=new JSONObject(value.errorBody().string());
                              Utility.BlueToast(context, jsonObject.getString("message"));
                          }
                          Utility.printLog("logoutApi : "+jsonObject.toString());

                      }catch (JSONException e)
                      {
                          Utility.printLog("logoutApi : Catch :"+e.getMessage());
                      } catch (IOException e) {
                          e.printStackTrace();
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

  }


}
