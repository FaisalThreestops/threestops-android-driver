package com.delivx.app.SlotAppointments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

public class SlotAppointmentPresenter implements SlotContract.PresenterOperations {

    @Inject SlotContract.ViewOperations view;
    @Inject Activity context;
    @Inject DispatcherService dispatcherService;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    private ArrayList<AssignedAppointments> appointments;
    private String title="";

    @Inject
    public SlotAppointmentPresenter() {
    }


    @Override
    public void setActionBar() {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        if(appointments!=null)
            view.setTitle(Utility.getDate(Long.parseLong(appointments.get(0).getSlotStartTime())) + " - " + Utility.getDate(Long.parseLong(appointments.get(0).getSlotEndTime())));
    }

    @Override
    public void getBundleData(Bundle bundle) {
        if(bundle!=null && bundle.containsKey("data")){
            appointments= (ArrayList<AssignedAppointments>) bundle.getSerializable("data");
            Log.i("data", "getBundleData: "+(ArrayList<AssignedAppointments>) bundle.getSerializable("data"));
            view.setViews(appointments);
        }
    }

    @Override
    public void getDirection(ArrayList<AssignedAppointments> slotResponseAppointments) {
        if(!slotResponseAppointments.get(0).getPickUpLatLng().isEmpty()){
            String[] latLong = appointments.get(0).getPickUpLatLng().split(",");
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
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }

}
