package com.delivx.app.storeDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.delivx.app.MyApplication;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.login.LoginActivity;
import com.delivx.login.language.LanguagesList;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.Cancel.CancelDataPojo;
import com.delivx.pojo.CancelPojo;
import com.delivx.service.LocationUpdateService;
import com.driver.delivx.R;
import com.delivx.networking.DispatcherService;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.AppConstants;
import com.delivx.utility.Utility;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class StoreDetailsPresenter implements StoreDetailsContract.Presenter
{
    @Inject   Activity context;
    @Inject   StoreDetailsContract.View view;
    @Inject   PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject   DispatcherService dispatcherService;
    @Inject
    NetworkService networkService;

    private AssignedAppointments appointments;
    private String activityView;
    Gson gson = new Gson();


    @Inject
    StoreDetailsPresenter() {
    }

    @Override
    public void setActionBar()
    {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        if(appointments!=null)
            view.setTitle(context.getResources().getString(R.string.bid)+appointments.getBid());
    }

    @Override
    public void getBundleData(Bundle bundle) {
        if(bundle!=null && bundle.containsKey("order_view")){
            appointments= (AssignedAppointments) bundle.getSerializable("order_view");
            activityView=bundle.getString("view");
            if (activityView.equals("hide_seek_bar")){
                view.hideSeekBar();
            }
            view.setViews(appointments);
        }
        if(bundle!=null && bundle.containsKey("data")){
            appointments= (AssignedAppointments) bundle.getSerializable("data");
            view.setViews(appointments);
        }
        if(bundle!=null && bundle.containsKey("from")){
            view.hideViews();
        }
    }

    @Override
    public void callCustomer(boolean isCustomer) {
        if(isCustomer)
            Utility.MakePhoneCall(appointments.getCustomerPhone(), context);
        else if(!"".equals(appointments.getStorePhone()) && appointments.getStorePhone()!=null)
            Utility.MakePhoneCall(appointments.getStorePhone(), context);
    }

    @Override
    public void updateBookingStatus() {
        if(view!=null){
            view.showProgress();
        }
        //onTheWay->10
        Utility.printLog("Appointment Status: "+AppConstants.BookingStatus.OnTheWay);
        Observable<Response<ResponseBody>> bookingStatusRide=dispatcherService.bookingStatusRide(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken(),
                appointments.getBid(),
                AppConstants.BookingStatus.OnTheWay,
                preferenceHelperDataSource.getDriverCurrentLat(),
                preferenceHelperDataSource.getDriverCurrentLongi(),
                null,
                null,
                null,null,null,null,null,null);

        bookingStatusRide.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        if(view!=null){
                            view.hideProgress();
                        }
                        try {
                            JSONObject jsonObject = null;
                            switch (value.code()) {
                                //success
                                case 200:
                                    jsonObject=new JSONObject(value.body().string());
                                    setAppointmentStatus(AppConstants.BookingStatus.OnTheWay);
                                    view.onSuccess(appointments);
                                    break;

                                case 440:
                                case 498:
                                    Utility.printLog("pushTopics shared pref "+preferenceHelperDataSource.getPushTopic());
                                    Utility.subscribeOrUnsubscribeTopics(new JSONArray(preferenceHelperDataSource.getPushTopic()),false);
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    ((MyApplication)context.getApplicationContext()).disconnectMqtt();
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                    if(Utility.isMyServiceRunning(LocationUpdateService.class, context))
                                    {
                                        Intent stopIntent = new Intent(context, LocationUpdateService.class);
                                        stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                                        context.startService(stopIntent);
                                    }

                                    break;
                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    break;
                            }
                            Utility.printLog("bookingStatusRide : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("bookingStatusRide : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null){
                            view.hideProgress();
                        }

                    }

                    @Override
                    public void onComplete() {
                        if(view!=null){
                            view.hideProgress();
                        }
                    }
                });
    }

    @Override
    public void openChat() {
        view.openChatAct(appointments);
    }

    @Override
    public void hideSoftKeyboard() {

        view.hideKeyboard();

    }



    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }


    /**
     * <h1>setAppointmentStatus</h1>
     * <p>Update booking status in shared preference</p>
     * @param status booking status
     */
    private void setAppointmentStatus(String status){
        try {
            JSONArray jsonArray=new JSONArray(preferenceHelperDataSource.getBookings());
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.get("bid").equals(appointments.getBid())) {
                    jsonObject.put("status",status);
                }

            }

            preferenceHelperDataSource.setBookings(jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void cancelReason(){
        if(view!=null){
            view.showProgress();
        }
        Observable<Response<ResponseBody>> assignedTrips=networkService.cancelReason(
                preferenceHelperDataSource.getLanguage(),preferenceHelperDataSource.getToken());
        assignedTrips.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        if(view!=null){
                            view.hideProgress();
                        }
                        try {
                            JSONObject jsonObject = null;
                            switch (value.code()) {
                                //success
                                case 200:
                                    String res=value.body().string();
                                    jsonObject=new JSONObject(res);
                                    CancelDataPojo cancelPojo=gson.fromJson(jsonObject.toString(),CancelDataPojo.class);
                                    view.cancelDialog(cancelPojo.getData());
                                    Log.i("cancel", "cancelResponse: "+res);
                                    Utility.printLog("cancelResponse "+jsonObject.toString());
                                    break;

                                case 440:
                                case 498:
                                    Utility.printLog("pushTopics shared pref "+preferenceHelperDataSource.getPushTopic());
                                    Utility.subscribeOrUnsubscribeTopics(new JSONArray(preferenceHelperDataSource.getPushTopic()),false);
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    ((MyApplication)context.getApplicationContext()).disconnectMqtt();
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                    if(Utility.isMyServiceRunning(LocationUpdateService.class, context))
                                    {
                                        Intent stopIntent = new Intent(context, LocationUpdateService.class);
                                        stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                                        context.startService(stopIntent);
                                    }

                                    break;
                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    break;
                            }
                            Utility.printLog("bookingStatusRide : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("bookingStatusRide : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null){
                            view.hideProgress();
                        }

                    }

                    @Override
                    public void onComplete() {
                        if(view!=null){
                            view.hideProgress();
                        }
                    }
                });
    }

    @Override
    public void cancelOrder() {
        if(view!=null)
            view.showProgress();

        Observable<Response<ResponseBody>> updateOrder=networkService.cancelOrder(
                preferenceHelperDataSource.getToken(),
                preferenceHelperDataSource.getLanguage(),
                "",
                "emfke",
                appointments.getBid(),
                preferenceHelperDataSource.getDriverCurrentLat()+"",
                preferenceHelperDataSource.getDriverCurrentLongi()+""
        );
        updateOrder.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200|| value.code()==201){
                                String res = value.body().string();
                                Utility.printLog("updated item : "+res);
                                jsonObject=new JSONObject(res);
                                view.moveHomeActivity();

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                            }

                            Utility.printLog("updateOrderApi : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("updateOrderApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }
}
