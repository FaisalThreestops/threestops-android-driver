package com.delivx.managers.booking;

import com.delivx.pojo.BookingAssigned;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h1>BookingManager</h1>
 * <p></p>
 * @author : 3embed
 * @since : 5/4/18
 */

public class BookingManager {

    private Gson gson;
    private RxDriverCancelledObserver rxDriverCancelledObserver;
    private RxBookingAssignObserver rxBookingAssignObserver;


    public BookingManager(Gson gson, RxDriverCancelledObserver rxDriverCancelledObserver,RxBookingAssignObserver rxBookingAssignObserver) {

        this.gson = gson;
        this.rxDriverCancelledObserver = rxDriverCancelledObserver;
        this.rxBookingAssignObserver = rxBookingAssignObserver;
    }



    /**
     * <h2>handleBookingsStatus</h2>
     * This method is used to handle the user bookings details
     * @param bookingDetails booking details to be handled
     */
    public void handleBookingsStatus(String bookingDetails)
    {
        try
        {
            JSONObject object = new JSONObject(bookingDetails);
            if(object.has("status"))
            {
                switch (object.getInt("status"))
                {
                   /* case 2:
                        //update drop location from passenger
                        RideBookingDropUpdate rideBookingDropUpdate = gson.fromJson(bookingDetails,
                                RideBookingDropUpdate.class);
                        rxDropLocationUpdateObserver.publishDropUpdate(rideBookingDropUpdate);
                        break;
                    case 4:
                        //passenger cancel the booking
                        RideBookingCancel driverDetailsModel = gson.fromJson(bookingDetails,
                                RideBookingCancel.class);
                        rxDriverCancelledObserver.publishDriverCancelDetails(driverDetailsModel);
                        break;

                    case 16:
//                        JSONObject data = object.getJSONObject("data");
                        ChatData rideChat = gson.fromJson(object.toString(),ChatData.class);
                        ChatDataObervable.getInstance().emitData(rideChat);
                        break;*/

                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void assignedBooking(BookingAssigned bookingAssigned){

        try {

            rxBookingAssignObserver.assignedNewBooking(bookingAssigned);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
