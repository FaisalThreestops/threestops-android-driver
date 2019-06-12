package com.delivx.app.bookingRide;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;

/**
 * Created by DELL on 05-02-2018.
 */

public interface BookingRideContract
{
    interface ViewOperations extends BaseView
    {
        void initActionBar();

        void setTitle(String orderStatus);

        void setViews(AssignedAppointments appointments);

        void setLatLong(double lat,double longi);

        void setCarMarker(LatLng latLng);

        void setCarMarker(Location latLng);

        void setmarkers(LatLng current,LatLng customer );

        void openGoogleMap(String uri);

        void onSuccess(AssignedAppointments appointments);

        void onError(String message);

        void openJobDetails(AssignedAppointments appointments);

        void setDistanceText(String distance);

        void setTimerText(String s);

        void openChatAct(AssignedAppointments appointments);
    }

    interface PresenterOperations {
        void setActionBar();

        void setActionBarTitle();

        void getBundleData(Bundle bundle);

        void getJobdetails();

        void onActDestroy();

        void callCustomer();

        void getDirection();

        void getLatLong();

        void getMarkers();

        void updateBookingStatus();

        void openChat();

        String getlanguageCode();


    }

}
