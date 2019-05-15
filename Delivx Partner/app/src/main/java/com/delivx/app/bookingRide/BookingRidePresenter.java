package com.delivx.app.bookingRide;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;
import com.delivx.RxObservers.RXDistanceChangeObserver;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.DispatcherService;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.AppConstants;
import com.delivx.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by DELL on 05-02-2018.
 */

public class BookingRidePresenter implements BookingRideContract.PresenterOperations
{
    @Inject BookingRideContract.ViewOperations view;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject Activity context;
    @Inject DispatcherService dispatcherService;
    private AssignedAppointments appointments;
    private boolean runTimer = false;
    private int timeConsumedSecond = 0;
    private Handler handler;
    private Runnable myRunnable;
    private String distance=null;



    @Inject
    public BookingRidePresenter()
    {
    }

    @Override
    public void setActionBar() {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        view.setTitle(appointments.getOrderStatus() );
    }

    @Override
    public void getBundleData(Bundle bundle) {
        if(bundle!=null && bundle.containsKey("data")){
            appointments= (AssignedAppointments) bundle.getSerializable("data");
            view.setViews(appointments);
            RXDistanceChangeObserver.getInstance().subscribe(observer);

            try {
                JSONArray jsonArray=new JSONArray(preferenceHelperDataSource.getBookings());
                setDistanceText(jsonArray);
                setTimerText(jsonArray);
            } catch (JSONException e) {
            }
        }
    }

    private void setTimerText(JSONArray jsonArray) {
        try{
            JSONObject jsonObject=getJobDetails(jsonArray);
            if (jsonObject != null && (jsonObject.getLong("time_elapsed") == 0)) {
                runTimer = true;
                mJobTimer();

            } else if (jsonObject != null) {
                mSubStractTimePaused(jsonObject);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void getJobdetails() {
        view.openJobDetails(appointments);
    }

    @Override
    public void callCustomer() {
        Utility.MakePhoneCall(appointments.getCustomerPhone(), context);
    }

    @Override
    public void getDirection() {
        if(appointments.getOrderStatus().equals(AppConstants.BookingStatus.JourneyStarted)){
            if(!appointments.getDropLatLng().isEmpty()){
                String[] latLong = appointments.getDropLatLng().split(",");
                Double lat = null, lng = null;
                if (latLong.length > 0) {
                    lat = Double.parseDouble(latLong[0]);
                    lng = Double.parseDouble(latLong[1]);
                }
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                        preferenceHelperDataSource.getDriverCurrentLat(), preferenceHelperDataSource.getDriverCurrentLongi(), lat, lng);
                view.openGoogleMap(uri);
            }
        }else
        {
            if(!appointments.getPickUpLatLng().isEmpty()){
                String[] latLong = appointments.getPickUpLatLng().split(",");
                Double lat = null, lng = null;
                if (latLong.length > 0) {
                    lat = Double.parseDouble(latLong[0]);
                    lng = Double.parseDouble(latLong[1]);
                }
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                        preferenceHelperDataSource.getDriverCurrentLat(), preferenceHelperDataSource.getDriverCurrentLongi(), lat, lng);
                view.openGoogleMap(uri);
            }
        }


    }

    @Override
    public void getLatLong() {
        view.setLatLong(preferenceHelperDataSource.getDriverCurrentLat(),preferenceHelperDataSource.getDriverCurrentLongi());
    }

    @Override
    public void getMarkers()
    {
        if(appointments.getOrderStatus().equals(AppConstants.BookingStatus.JourneyStarted)){
            if(!appointments.getDropLatLng().isEmpty()){
                String[] latLong = appointments.getDropLatLng().split(",");
                LatLng customer = new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1]));

                LatLng current=new LatLng(preferenceHelperDataSource.getDriverCurrentLat(),preferenceHelperDataSource.getDriverCurrentLongi());

                view.setmarkers(current,customer);
            }
        }else
        {
            if(!appointments.getPickUpLatLng().isEmpty()){
                String[] latLong = appointments.getPickUpLatLng().split(",");
                LatLng customer = new LatLng(Double.valueOf(latLong[0]), Double.valueOf(latLong[1]));

                LatLng current=new LatLng(preferenceHelperDataSource.getDriverCurrentLat(),preferenceHelperDataSource.getDriverCurrentLongi());

                view.setmarkers(current,customer);
            }
        }

    }

    @Override
    public void updateBookingStatus() {
        if(view!=null){
            view.showProgress();
        }
        String status="";
        if(appointments.getOrderStatus().equals(AppConstants.BookingStatus.JourneyStarted))
            status=AppConstants.BookingStatus.ReachedAtLocation;
        else
            status=AppConstants.BookingStatus.Arrived;

        Utility.printLog("Appointment distance: "+distance);
        Observable<Response<ResponseBody>> bookingStatusRide=dispatcherService.bookingStatusRide(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken(),
                appointments.getBid(),
                status,
                preferenceHelperDataSource.getDriverCurrentLat(),
                preferenceHelperDataSource.getDriverCurrentLongi(),
                distance,
                null,
                null,
                null,
                null,
                null);

        final String finalStatus = status;
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
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                appointments.setOrderStatus(finalStatus);
                                setAppointmentStatus(finalStatus);
                                updateDistanceAndTimerOnresult();

                                view.onSuccess(appointments);

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onError(jsonObject.getString("message"));
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

    Observer<JSONArray> observer=new Observer<JSONArray>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(JSONArray value) {
            setDistanceText(value);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };


    public void setDistanceText(JSONArray value){
        if(value!=null){
            JSONObject jsonObject=getJobDetails(value);
            if(jsonObject!=null){
                try {
                    double distance_conv_unit=jsonObject.getDouble("dist_conv_unit");
                    distance= String.valueOf((jsonObject.getDouble("distance") / distance_conv_unit));
                    view.setDistanceText(jsonObject.get("distance")+" "+appointments.getMileageMetric());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public JSONObject getJobDetails(JSONArray jsonArray){
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.get("bid").equals(appointments.getBid())) {
                    return jsonObject;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void updateDistanceAndTimerOnresult(){

        try {
            JSONArray jsonArray=new JSONArray(preferenceHelperDataSource.getBookings());
            for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Utility.printLog("onDistance changed ...." + jsonObject.get("bid"));
                    if (jsonObject.get("bid").equals(appointments.getBid())) {
                        jsonObject.put("distance",0.0);
                        jsonObject.put("time_paused",0);
                        jsonObject.put("time_elapsed",0);
                    }
            }
            preferenceHelperDataSource.setBookings(jsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (myRunnable != null) {
            handler.removeCallbacks(myRunnable);
        }
        preferenceHelperDataSource.setTimerStarted(false);
        preferenceHelperDataSource.setTimeWhilePaused(0);
    }

    private void mJobTimer() {
        preferenceHelperDataSource.setTimerStarted(true);

        handler = new Handler();
        myRunnable = new Runnable() {

            @Override
            public void run() {
                if (runTimer) {
                    timeConsumedSecond = timeConsumedSecond + 1;
                    view.setTimerText("" + Utility.getDurationString(timeConsumedSecond));
                    handler.postDelayed(this, 1000);
                } else {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(preferenceHelperDataSource.getBookings());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            if (jsonObject.get("bid").equals(appointments.getBid())) {
                                jsonObject.put("time_paused", System.currentTimeMillis());
                                jsonObject.put("time_elapsed", timeConsumedSecond);
                            }

                        }
                        preferenceHelperDataSource.setBookings(jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        handler.postDelayed(myRunnable, 1000);

    }

    public void onActDestroy(){
        runTimer = false;
        preferenceHelperDataSource.setTimeWhilePaused(System.currentTimeMillis());
    }

    private void mSubStractTimePaused(JSONObject jsonObject) {
        try {

            if (jsonObject.getLong("time_elapsed") > 0) {
                long mTimeWhilePaused = jsonObject.getLong("time_paused");
                if (!"0".equals(mTimeWhilePaused)) {
                    long currentTime = System.currentTimeMillis() - mTimeWhilePaused;
                    timeConsumedSecond = jsonObject.getInt("time_elapsed");
                    timeConsumedSecond = timeConsumedSecond + Math.round(((float) (currentTime)) / 1000f);
                    timeConsumedSecond += 1;//for error apprx
                } else {
                    timeConsumedSecond = (0);
                }
            } else {
                timeConsumedSecond = (0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        runTimer = true;

        mJobTimer();

    }

    public void setAppointmentStatus(String status){


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
}
