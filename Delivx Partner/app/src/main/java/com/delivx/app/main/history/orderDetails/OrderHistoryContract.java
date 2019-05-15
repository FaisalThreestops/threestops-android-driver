package com.delivx.app.main.history.orderDetails;

import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.TripsPojo.Appointments;

/**
 * Created by DELL on 16-03-2018.
 */

public interface OrderHistoryContract {

    interface ViewOperation extends BaseView
    {
//        void setActionBar();
        void setViews(Appointments appointment);
//        void setTitle(String format);
//        void setPickUpTime(String format);
//        void setDropTime(String format);
//        void showItems();
//        void hideItems();
    }

    interface PresenterOperation
    {
        void initActionBar();
        void getBundleData(Bundle extras);
        void showOrderDetails();
        void hideOrderDetails();
    }

}


