package com.driver.threestops.app.main.history.orderDetails;

import android.os.Bundle;

import com.driver.threestops.BaseView;
import com.driver.threestops.pojo.TripsPojo.Appointments;


public interface OrderHistoryContract {

    interface ViewOperation extends BaseView
    {

        /**
         * <h2>setViews</h2>
         * <p>set the views by values</p>
         * @param appointment : appointment pojo details
         */
        void setViews(Appointments appointment);
    }

    interface PresenterOperation
    {

        /**
         * <h2>getBundleData</h2>
         * <p>get data from previous(HistoryTripsRVA)</p>
         * @param extras : Appointments details
         */
        void getBundleData(Bundle extras);

        /**
         * <h2>getlanguageCode</h2>
         * <p>get the languageCode</p>
         * @return : languageCode
         */
        String getlanguageCode();
    }

}


