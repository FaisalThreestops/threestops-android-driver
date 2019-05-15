package com.delivx.app.main.history.orderDetails;

import android.os.Bundle;

import com.delivx.pojo.TripsPojo.Appointments;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by DELL on 16-03-2018.
 */

public class OrderHistoryPresenter implements OrderHistoryContract.PresenterOperation{

    private Appointments appointment;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    @Inject OrderHistoryContract.ViewOperation view;

    @Inject
    public OrderHistoryPresenter() {
    }


    @Override
    public void initActionBar() {
//        view.setActionBar();
    }

    @Override
    public void getBundleData(Bundle extras) {
        if(extras!=null && extras.containsKey("data")){
            appointment = (Appointments) extras.getSerializable("data");
            view.setViews(appointment);
//            setTitle();
//            setPickUpTime();
//            setDropTime();
        }


    }

    @Override
    public void showOrderDetails() {
//        view.showItems();
    }

    @Override
    public void hideOrderDetails() {
//        view.hideItems();
    }

    private void setDropTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a", Locale.US);

        try {

            Date apnt = dateFormat.parse(appointment.getPickedupTime());
//            view.setPickUpTime(simpleDateFormat.format(apnt));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPickUpTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a", Locale.US);

        try {
            Date apnt = dateFormat.parse(appointment.getDeliveredTime());
//            view.setDropTime(simpleDateFormat.format(apnt));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTitle() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMM dd", Locale.US);

        try {

            Date apnt = dateFormat.parse(appointment.getBookingDate());
//            view.setTitle(appointment.getBookingDate());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
