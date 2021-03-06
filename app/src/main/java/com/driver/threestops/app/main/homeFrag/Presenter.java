package com.driver.threestops.app.main.homeFrag;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;


import android.util.Log;
import com.driver.threestops.app.MyApplication;
import com.driver.threestops.app.bookingRequest.BookingPopUp;
import com.driver.threestops.login.LoginActivity;
import com.driver.threestops.login.language.LanguagesList;
import com.driver.threestops.managers.booking.RxBookingAssignObserver;
import com.driver.threestops.managers.location.LocationCallBack;
import com.driver.threestops.managers.location.LocationProvider;
import com.driver.threestops.managers.location.RxLocationObserver;
import com.driver.threestops.pojo.BookingAssigned;
import com.driver.threestops.service.LocationUpdateService;
import com.driver.threestops.utility.AppConstants;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.driver.threestops.RxObservers.RXMqttMessageObserver;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.networking.DispatcherService;
import com.driver.threestops.networking.NetworkService;
import com.driver.threestops.pojo.AssignedAppointments;
import com.driver.threestops.pojo.AssignedTripsPojo;
import com.driver.threestops.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class Presenter implements HomeFragmentContract.Presenter, LocationCallBack {

    private RxBookingAssignObserver rxBookingAssignObserver;
    private boolean isMapInFullView=false;
    private HomeFragmentContract.View view;
    private boolean assignManually=false;


    @Inject   NetworkService networkService;
    @Inject   Activity context;
    @Inject   DispatcherService dispatcherService;
    @Inject   PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    LocationProvider locationProvider;
    @Inject
    RxLocationObserver locationObserver;
    private Disposable locationDisposable;
    private ArrayList<AssignedAppointments> appointments;

    @Inject
    public Presenter(RxBookingAssignObserver  rxBookingAssignObserver) {
        this.rxBookingAssignObserver  = rxBookingAssignObserver;

        subscribeBookingAssign();

    }

    /**
     * <h2>subscribeBookingAssign</h2>
     * <p>subscribe assigned booking from dispatcher </p>
     */
    private void subscribeBookingAssign()
    {
        Observer<BookingAssigned> observer = new Observer<BookingAssigned>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {

            }

            @Override
            public void onNext(BookingAssigned data)
            {
                getAssignedTRips();

            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(" queue onError  "+e);
            }

            @Override
            public void onComplete()
            {}
        };
        rxBookingAssignObserver.subscribeOn(Schedulers.io());
        rxBookingAssignObserver.observeOn(AndroidSchedulers.mainThread());
        rxBookingAssignObserver.subscribe(observer);
    }

    @Override
    public void attachView(HomeFragmentContract.View homeView) {
        view=homeView;
        RXMqttMessageObserver.getInstance().subscribe(observer);
    }

    @Override
    public void updateMasterStatus() {
        if(preferenceHelperDataSource.getMasterStatus()==3)
            updateMasterStatusApi(4);
        else
            updateMasterStatusApi(3);
    }

    @Override
    public void startLocationUpdate() {
        locationProvider.startLocation(this);
        locationObserver.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Location>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        locationDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Location location) {
                        view.updateLocation(location);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void stopLocationUpdate() {
        if (locationDisposable != null && !locationDisposable.isDisposed())
            locationDisposable.dispose();

        if (locationProvider.isLocationUpdating())
            locationProvider.stopLocationUpdates();
    }

    @Override
    public void getAssignedTRips() {

        if(assignManually==false)
        view.showProgress();

        Observable<Response<ResponseBody>> assignedTrips=networkService.assignedTrips(
                preferenceHelperDataSource.getLanguage(),((MyApplication) context.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()));
        assignedTrips.observeOn(AndroidSchedulers.mainThread())
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
                            JSONObject jsonObject = null;

                            switch (value.code()){
                                //success
                                case 200:
                                    String response=value.body().string();
                                    Utility.printLog("AssignedTRipResponse"+response);
                                    jsonObject=new JSONObject(response);
                                    Gson gson=new Gson();
                                    AssignedTripsPojo tripsPojo=gson.fromJson(jsonObject.toString(),AssignedTripsPojo.class);
                                    appointments=tripsPojo.getData().getAppointments();
                                    setAppointments();
                                    view.setAssignedTrips(appointments);
                                    view.onMasterStatusUpdate(tripsPojo.getData().getMasterStatus());
                                    view.addMarkers(appointments);
                                    preferenceHelperDataSource.setMasterStatus(tripsPojo.getData().getMasterStatus());
                                    break;
                                    //session expired
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
                            Utility.printLog("assignedTrips : Catch :"+e.getMessage());
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


    /**
     * <h1>setAppointments</h1>
     * <p>set  booking details to shared preference</p>
     */
    private void setAppointments() {
        try {
            JSONArray oldArray = new JSONArray();
            if (!preferenceHelperDataSource.getBookings().isEmpty()) {
                oldArray = new JSONArray(preferenceHelperDataSource.getBookings());
            }

            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < appointments.size(); i++) {

                boolean found = false;
                AssignedAppointments appointment = appointments.get(i);
                for (int j = 0; j < oldArray.length(); j++) {
                    JSONObject jsonObject = oldArray.getJSONObject(j);
                    if (jsonObject.get("bid").equals(appointment.getBid())) {
                        jsonObject.put("status",appointment.getOrderStatus());
                        jsonArray.put(jsonObject);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("bid", appointment.getBid());
                        jsonObject.put("distance", 0.0);
                        jsonObject.put("custChn", appointment.getCustomerChn());
                        jsonObject.put("status", appointment.getOrderStatus());
                        jsonObject.put("time_paused", 0);
                        jsonObject.put("time_elapsed", 0);

                        if (appointment.getMileageMetric().equals("Km")) {
                            jsonObject.put("dist_conv_unit", 0.001);
                        }
                        else {
                            jsonObject.put("dist_conv_unit", 0.00062137);
                        }

                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            preferenceHelperDataSource.setBookings(jsonArray.toString());
            Utility.printLog("AssignedTrips preferenceHelperDataSource" + preferenceHelperDataSource.getBookings());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLocation(Location location) {
        preferenceHelperDataSource.setDriverCurrentLat(String.valueOf(location.getLatitude()));
        preferenceHelperDataSource.setDriverCurrentLongi(String.valueOf(location.getLongitude()));
    }

    @Override
    public void expandMap() {
        //if map is full viewed
        if(isMapInFullView){
            view.minimizeMap(appointments);
            isMapInFullView=false;
        }
        //if map is not fully viewed
        else
        {
            view.openMapInFullView();
            isMapInFullView=true;
        }

    }

    @Override
    public void markerAllOnclik() {
        view.addMarkers(appointments);
    }

    @Override
    public void markerPickUpOnclik() {
        view.removeDeliveryMarkers();
    }

    @Override
    public void markerDeliveryOnclik() {
        view.removePickUpMarkers();
    }

    @Override
    public void checkBookingPopUp() {

    }

    /**
     * <h2>updateMasterStatusApi</h2>
     * <p>Invoke API for updating the status of the driver</p>
     * @param masterStatus : 3.online,, 4.offline
     */
    private void updateMasterStatusApi(final int masterStatus) {

        view.showProgress();
        Observable<Response<ResponseBody>> status=dispatcherService.status(
                /*preferenceHelperDataSource.getLanguage()*/"0",((MyApplication) context.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()),masterStatus);
        status.observeOn(AndroidSchedulers.mainThread())
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
                            if(value.code()==200)
                            {
                                jsonObject=new JSONObject(value.body().string());
                                preferenceHelperDataSource.setMasterStatus(masterStatus);
                                view.onMasterStatusUpdate(masterStatus);
                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                            }

                            Utility.printLog("status : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("status : Catch :"+e.getMessage());
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

    private Observer<JSONObject> observer=new Observer<JSONObject>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(JSONObject value) {
            Log.d("cancel", "onNext: ");
            try {
                if(value.getInt("action")==16 || value.getInt("action")==29){
                    assignManually=true;
                    getAssignedTRips();
                }
                else if(value.getInt("action")==10 || value.getInt("action")==14){
                    Log.d("cancel", "else if: ");
                    switch (value.getInt("action")){
                        case 10:
                        case 14:
                            view.showError(value.getString("statusMsg"));
                            break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    public void getDriverScheduleType() {
        view.driverStatusType(preferenceHelperDataSource.getDriverScheduleType());
    }

    @Override
    public void onLocationServiceDisabled(Status status) {

    }
}
