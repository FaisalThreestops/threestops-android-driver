package com.driver.threestops.managers.booking;

import com.driver.threestops.pojo.BookingAssigned;

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
