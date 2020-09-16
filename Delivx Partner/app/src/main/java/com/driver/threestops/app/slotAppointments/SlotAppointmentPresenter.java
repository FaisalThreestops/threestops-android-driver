package com.driver.threestops.app.slotAppointments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.networking.DispatcherService;
import com.driver.threestops.networking.NetworkService;
import com.driver.threestops.pojo.AssignedAppointments;
import com.driver.threestops.utility.Utility;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

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
