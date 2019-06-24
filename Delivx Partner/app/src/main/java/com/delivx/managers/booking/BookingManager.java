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


    private RxDriverCancelledObserver rxDriverCancelledObserver;
    private RxBookingAssignObserver rxBookingAssignObserver;


    public BookingManager(RxBookingAssignObserver rxBookingAssignObserver) {
        this.rxBookingAssignObserver = rxBookingAssignObserver;
    }


    public void assignedBooking(BookingAssigned bookingAssigned){

        try {
            rxBookingAssignObserver.assignedNewBooking(bookingAssigned);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
