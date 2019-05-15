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

        void onMasterStatusUpdate(int status);

        void setAssignedTrips(ArrayList<AssignedAppointments> assignedTrips);

        void openMapInFullView();

        void minimizeMap(ArrayList<AssignedAppointments> appointments);

        void addMarkers(ArrayList<AssignedAppointments> appointments);

        void removePickUpMarkers();

        void removeDeliveryMarkers();

        void showError(String statusMsg);
    }


    interface Presenter {

        void attachView(HomeFragmentContract.View view);

        void updateMasterStatus();

        void getAssignedTRips();

        void updateLocation(Location location);

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
