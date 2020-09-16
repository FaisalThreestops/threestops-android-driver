package com.driver.threestops.managers.booking;


import com.driver.threestops.pojo.BookingAssigned;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * <h1>RxBookingAssignObserver</h1>
 */

public class RxBookingAssignObserver extends Observable<BookingAssigned>
{
    private Observer<? super BookingAssigned> observer;

    @Override
    protected void subscribeActual(Observer<? super BookingAssigned> observer) {
        this.observer = observer;
    }

    /**
     * <h2>assignedNewBooking</h2>
     */
    void assignedNewBooking(BookingAssigned data)
    {
        if(observer!=null)
        {
            observer.onNext(data);
            observer.onComplete();
        }
    }
}
