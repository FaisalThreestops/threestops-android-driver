package com.driver.threestops.app.main.history.orderDetails;

import android.os.Bundle;

import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.pojo.TripsPojo.Appointments;

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
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }

}
