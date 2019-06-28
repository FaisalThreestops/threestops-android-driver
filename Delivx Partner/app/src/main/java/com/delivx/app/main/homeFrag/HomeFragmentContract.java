package com.delivx.app.main.homeFrag;

import android.location.Location;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;

import java.util.ArrayList;


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
         * @param appointments appointment details pojo
         */
        void minimizeMap(ArrayList<AssignedAppointments> appointments);


        /**
         * <h1>addMarkers</h1>
         * <p>add marker for all address, include pickup and delivery</p>
         * @param appointments appoinment details pojo
         */
        void addMarkers(ArrayList<AssignedAppointments> appointments);

        /**
         * <h1>removePickUpMarkers</h1>
         * <p>remove pickup marker from map</p>
         */
        void removePickUpMarkers();

        /**
         * <h1>removeDeliveryMarkers</h1>
         * <p>remove delivery marker from map</p>
         */
        void removeDeliveryMarkers();

        /**
         * <h2>showError</h2>
         * <p>show the error alert dailog</p>
         * @param statusMsg error message
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
         * <p>updating the online and offline status of the driver</p>
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
         * @param location latitude and longitude
         */
        void updateLocation(Location location);

        /**
         * <h2>expandMap</h2>
         * <p>minimizing and maximizing the google map</p>
         */
        void expandMap();


        /**
         * <h1>markerAllOnclik</h1>
         * <p>click event for all text, show all location in map</p>
         */
        void markerAllOnclik();

        /**
         * <h1>markerPickUpOnclik</h1>
         * <p>click event for pickup text, show pickup locations only in map</p>
         */
        void markerPickUpOnclik();

        /**
         *<h1>markerDeliveryOnclik</h1>
         * <p>remove pickup marker</p>
         */
        void markerDeliveryOnclik();

        /**
         * <h1>checkBookingPopUp</h1>
         * <p>check whether the booking popup media player ring</p>
         */
        void checkBookingPopUp();
    }
}
