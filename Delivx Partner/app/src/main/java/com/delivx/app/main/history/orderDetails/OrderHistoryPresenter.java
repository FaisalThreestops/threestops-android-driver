package com.delivx.app.main.history.orderDetails;

import android.os.Bundle;

import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.pojo.TripsPojo.Appointments;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class OrderHistoryPresenter implements OrderHistoryContract.PresenterOperation{

    @Inject OrderHistoryContract.ViewOperation view;
    @Inject
    PreferenceHelperDataSource  preferenceHelperDataSource;

    @Inject
    OrderHistoryPresenter() {
    }


    @Override
    public void getBundleData(Bundle extras) {
        if(extras!=null && extras.containsKey("data")){
            Appointments appointment = (Appointments) extras.getSerializable("data");
            view.setViews(appointment);
        }
    }

    @Override
    public void showOrderDetails() {
    }

    @Override
    public void hideOrderDetails() {
    }

    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }

}
