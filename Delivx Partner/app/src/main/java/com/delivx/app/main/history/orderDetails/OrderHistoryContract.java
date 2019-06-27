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

        /**
         * <h2>setViews</h2>
         * <p>set the views by values</p>
         * @param appointment : appointment pojo details
         */
        void setViews(Appointments appointment);
//        void setTitle(String format);
//        void setPickUpTime(String format);
//        void setDropTime(String format);
//        void showItems();
//        void hideItems();
    }

    interface PresenterOperation
    {
        /**
         * <h2>initActionBar</h2>
         * <p>initializing the action bar</p>
         */
//        void initActionBar();

        /**
         * <h2>getBundleData</h2>
         * <p>get data from previous(HistoryTripsRVA)</p>
         * @param extras : Appointments details
         */
        void getBundleData(Bundle extras);
        void showOrderDetails();
        void hideOrderDetails();

        String getlanguageCode();
    }

}


