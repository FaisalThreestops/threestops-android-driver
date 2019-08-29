package com.delivx.app.SelectedStore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.delivx.app.MyApplication;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.login.LoginActivity;
import com.delivx.login.language.LanguagesList;
import com.delivx.networking.DispatcherService;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.ShipmentDetails;
import com.delivx.service.LocationUpdateService;
import com.delivx.utility.AppConstants;
import com.delivx.utility.Utility;
import com.driver.delivx.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class SelectedStoreIDPresenter implements SelectedStoreIdContract.PresenterOperations {

    @Inject
    SelectedStoreIdContract.ViewOperations view;
    @Inject
    Activity context;
    @Inject
    DispatcherService dispatcherService;
    @Inject
    NetworkService networkService;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    private ArrayList<AssignedAppointments> appointments;
    private ArrayList<AssignedAppointments> sortStoreID;
    int count=0;
    private int upIndexToItems;

    @Inject
    public SelectedStoreIDPresenter() {
    }


    @Override
    public void setActionBar() {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
//        if (appointments != null)
        view.setTitle(Utility.getDate(Long.parseLong(appointments.get(0).getSlotStartTime())) + " - " + Utility.getDate(Long.parseLong(appointments.get(0).getSlotEndTime())));
    }

    @Override
    public void getBundleData(Bundle bundle) {
        if (bundle != null && bundle.containsKey("data")) {
            appointments = (ArrayList<AssignedAppointments>) bundle.getSerializable("data");
            sortStoreID=(ArrayList<AssignedAppointments>) bundle.getSerializable("sortStoreId") ;
            upIndexToItems=bundle.getInt("index");
            view.setViews(appointments,sortStoreID,upIndexToItems);
        }
    }

    @Override
    public void getDirection() {
        if(!sortStoreID.get(0).getPickUpLatLng().isEmpty()){
            String[] latLong = sortStoreID.get(0).getPickUpLatLng().split(",");
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

    @Override
    public void arrived() {
        if(view!=null){
            view.showProgress();
        }

        final String status;
        status=AppConstants.BookingStatus.Arrived;

        Utility.printLog("Appointment Status: "+AppConstants.BookingStatus.Arrived);
        Observable<Response<ResponseBody>> bookingStatusRide=dispatcherService.bookingStatusRide(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken(),
                appointments.get(0).getBid(),
                AppConstants.BookingStatus.Arrived,
                preferenceHelperDataSource.getDriverCurrentLat(),
                preferenceHelperDataSource.getDriverCurrentLongi(),
                null,
                null,
                null,null,null,appointments.get(0).getSlotId(),appointments.get(0).getStoreId(),null);

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
                                for(int i=0;i<appointments.size();i++) {
                                    appointments.get(i).setOrderStatus(status);
                                }
                                setAppointmentStatus(AppConstants.BookingStatus.Arrived);
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

    public void setAppointmentStatus(String status){
        try {
            JSONArray jsonArray=new JSONArray(preferenceHelperDataSource.getBookings());
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.get("bid").equals(appointments.get(count).getBid())) {
                    count++;
                    jsonObject.put("status",status);
                }
                if(count==appointments.size()){
                    break;
                }

            }

            preferenceHelperDataSource.setBookings(jsonArray.toString());
            Utility.printLog("AssignedTrips preferenceHelperDataSourced stored" + preferenceHelperDataSource.getBookings());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }
}
