package com.delivx.app.main.homeFrag;

import android.location.Location;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;

import java.util.ArrayList;

/**
 * Created by DELL on 01-02-2018.
 */

public interface HomeFragmentContract {

    interface View extends BaseView{

        /**
         * <h2>onMasterStatusUpdate</h2>
         * <p>updating the master location</p>
         * @param status : 3.online,, 4.offline
         */
        void onMasterStatusUpdate(int status);

        /**
         * <h2>setAssignedTrips</h2>
         * <p>set the assigned(appointments) trips </p>
         * @param assignedTrips : trips list
         */
        void setAssignedTrips(ArrayList<AssignedAppointments> assignedTrips);

        /**
         * <h2>openMapInFullView</h2>
         * <p>maximize the google map </p>
         */
        void openMapInFullView();

        /**
         * <h2>minimizeMap</h2>
         * <p>minimize the google map</p>
         * @param appointments
         */
        void minimizeMap(ArrayList<AssignedAppointments> appointments);

        void addMarkers(ArrayList<AssignedAppointments> appointments);

        void removePickUpMarkers();

        void removeDeliveryMarkers();

        /**
         * <h2>showError</h2>
         * <p>show the error alert dailog</p>
         * @param statusMsg
         */
        void showError(String statusMsg);
    }


    interface Presenter {

        /**
         * <h2>attachView</h2>
         * <p>Attach the HomeFragment view to presenter</p>
         * @param view : HomeFragment view
         */
        void attachView(HomeFragmentContract.View view);

        /**
         * <h2>updateMasterStatus</h2>
         * <p>updating the status of the driver</p>
         */
        void updateMasterStatus();

        /**
         * <h2>getAssignedTRips</h2>
         * <p>get the assigned trips details</p>
         */
        void getAssignedTRips();

        /**
         * <h2>updateLocation</h2>
         * <p>updating the current location to sharedPreference</p>
         * @param location
         */
        void updateLocation(Location location);

        /**
         * <h2>expandMap</h2>
         * <p>minimizing and maximizing the google map</p>
         */
        void expandMap();


        void markerAllOnclik();

        void markerPickUpOnclik();

        void markerDeliveryOnclik();

        /**
         * <h1>checkBookingPopUp</h1>
         * <p>check whether the booking popup media player ring</p>
         */
        void checkBookingPopUp();
    }
}
