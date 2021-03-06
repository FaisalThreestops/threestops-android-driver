package com.driver.threestops.app.bookingRide;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.driver.threestops.BaseView;
import com.driver.threestops.pojo.AssignedAppointments;

public interface BookingRideContract
{
    interface ViewOperations extends BaseView
    {
        /**
         * <h2>initActionBar</h2>
         * <p>initializing the action bar</p>
         */
        void initActionBar();

        /**
         * <h2>setTitle</h2>
         * <p>set text to title in action bar</p>
         * @param orderStatus : updating the status( where driver is) it depends on the status
         */
        void setTitle(String orderStatus);

        /**
         * <h2>setViews</h2>
         * <p>set the views by values</p>
         * @param appointments : appointments details pojo
         */
        void setViews(AssignedAppointments appointments);

        /**
         * <h2>setLatLong</h2>
         * <p>setting the current location </p>
         * @param lat : latitude
         * @param longi : longitude
         */
        void setLatLong(double lat,double longi);

        /**
         * <h2>setCarMarker</h2>
         * <p>set the driver location</p>
         * @param latLng :latitude and longitude
         */
        void setCarMarker(LatLng latLng);

        /**
         * <h2>setCarMarker</h2>
         * <p>set the updated location driver location</p>
         * @param latLng :location
         */
        void setCarMarker(Location latLng);

        /**
         * <h2>setmarkers</h2>
         * <p>setting the route between driver to customer</p>
         * @param current : current location
         * @param customer : destination location
         */
        void setmarkers(LatLng current,LatLng customer );

        /**
         * <h2>openGoogleMap</h2>
         * <p>set the destination url to map</p>
         * @param uri : url
         */
        void openGoogleMap(String uri);

        /**
         * <h2>onSuccess</h2>
         * <p>moving to next(SlotAppointmentActivity) by passing data</p>
         * @param appointments :appintmentsPoji details
         */
        void onSuccess(AssignedAppointments appointments);

        /**
         * <h2>onError</h2>
         * <p>error message while API call issues</p>
         * @param message :error data
         */
        void onError(String message);

        /**
         * <h2>openJobDetails</h2>
         * <p>moving to StorePickUpDetails to get details </p>
         * @param appointments : appointmentPojo details
         */
        void openJobDetails(AssignedAppointments appointments);

        /**
         * <h2>setDistanceText</h2>
         * <p>set the booking ride distance </p>
         * @param distance :distances
         */
        void setDistanceText(String distance);

        /**
         * <h2>setTimerText</h2>
         * <p>set the timer </p>
         * @param s :time
         */
        void setTimerText(String s);

        /**
         * <h2>openChatAct</h2>
         * <p>moving to ChattingActivity to chat</p>
         * @param appointments : appointmentPojo data
         */
        void openChatAct(AssignedAppointments appointments);

        /**
         * <h2>updateLocation</h2>
         * <p>this method is provide updated location.</p>
         * @param location object of {@link Location}
         */
        void updateLocation(Location location);
    }

    interface PresenterOperations {

        /**
         * <h2>setActionBar</h2>
         * <p>setting the action bar </p>
         */
        void setActionBar();

        /**
         * <h2>setActionBarTitle</h2>
         * <p>setting title to the action bar</p>
         */
        void setActionBarTitle();

        /**
         * <h2>getBundleData</h2>
         * <p>get the data from previous(storePickUpDetails)</p>
         * @param bundle : appointment details
         */
        void getBundleData(Bundle bundle);

        /**
         * <h2>getJobdetails</h2>
         * <p>get the job details</p>
         */
        void getJobdetails();

        /**
         * <p>onActDestroy</p>
         * <p>set time while paused</p>
         */
        void onActDestroy();

        /**
         * <h2>callCustomer</h2>
         * <p>giving Dial action to call </p>
         */
        void callCustomer();

        /**
         * <h2>startLocationUpdate</h2>
         * <p>This method is start location updating</p>
         */
        void startLocationUpdate();

        /**
         * <h2>stopLocationUpdate</h2>
         * <p>This method is stop location updating</p>
         */
        void stopLocationUpdate();

        /**
         *<h2>getDirection</h2>
         * <p>get the destination direction to move </p>
         */
        void getDirection();

        /**
         * <h2>getLatLong</h2>
         * <P>get the current location </P>
         */
        void getLatLong();

        /**
         * <h2>getMarkers</h2>
         * <p>get the markers(moving icon)</p>
         */
        void getMarkers();

        /**
         * <h2>updateBookingStatus</h2>
         * <p>calling API, if seekbar >65 to update the booking status</p>
         */
        void updateBookingStatus();

        /**
         * <h2>openChat</h2>
         * <p>opens the chat to communicate with customer</p>
         */
        void openChat();

        /**
         * <h2>getlanguageCode</h2>
         * <p>get the language code</p>
         * @return : language code
         */
        String getlanguageCode();


    }

}
