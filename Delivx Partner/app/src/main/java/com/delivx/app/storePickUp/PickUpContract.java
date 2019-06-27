package com.delivx.app.storePickUp;

import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.ShipmentDetails;

public interface PickUpContract {

    interface ViewOperations extends BaseView
    {
        /**
         * <h2>initActionBar</h2>
         * <p>initializing the action bar</p>
         */
        void initActionBar();

        /**
         * <h2>setTitle</h2>
         * <p>set title to the action bar</p>
         * @param title Activity title
         */
        void setTitle(String title);

        /**
         * <h2>setViews</h2>
         * <p>set the views by text</p>
         * @param appointments : appointmentsPojo data
         */
        void setViews(AssignedAppointments appointments);

        /**
         * <h2>onSuccess</h2>
         * <p>moving to next(BookingRide)</p>
         * @param appointments : appointmentsPojo data
         */
        void onSuccess(AssignedAppointments appointments);

        /**
         * <h2>openChatAct</h2>
         * <p>moving to ChattingActivity to chat</p>
         * @param appointments : appointmentPojo data
         */
        void openChatAct(AssignedAppointments appointments);

        /**
         * <h2>onError</h2>
         * <p>show Error messages</p>
         * @param message : error
         */
        void onError(String message);

        /**
         * <h2>openOrderEditDialog</h2>
         * <p>open the alert dialog to edit the order item</p>
         * @param shipmentDetails booking details pojo
         * @param assignedAppointments : Appointment details pojo
         */
        void openOrderEditDialog(ShipmentDetails shipmentDetails, AssignedAppointments assignedAppointments);
    }

    interface PresenterOperations
    {
        /**
         * <h2>setActionBar</h2>
         * <p>set the action bar</p>
         */
        void setActionBar();

        /**
         * <h2>setActionBarTitle</h2>
         * <p>set title to the action bar</p>
         */
        void setActionBarTitle();

        /**
         *<h2>getBundleData</h2>
         * <p>get the data from(BokkingRide)</p>
         * @param bundle Appointment data from previous activity
         */
        void getBundleData(Bundle bundle);

        /**
         * <h2>callCustomer</h2>
         * <p>giving the action Dial to call customer</p>
         */
        void callCustomer();

        /**
         * <h2>updateBookingStatus</h2>
         * <p>calling the API to  update status,, if seekbar >65 progresss</p>
         */
        void updateBookingStatus();

        /**
         * <h2>editOrder</h2>
         * <p>edit the order items</p>
         * @param shipmentDetails booking details pojo
         */
        void editOrder(ShipmentDetails shipmentDetails);

        /**
         * <h2>updateOrder</h2>
         * <p>updating the edited items</p>
         * @param shipmentDetails booking details pojo
         * @param qty  edited quantity
         */
        void updateOrder(ShipmentDetails shipmentDetails, String qty);

        /**
         * <h2>deleteItem</h2>
         * <p>deleting the items from the list</p>
         * @param shipmentDetails booking details pojo
         */
        void deleteItem(ShipmentDetails shipmentDetails);

        /**
         * <h2>openChat</h2>
         * <p>opens the chat option to chat with customer</p>
         */
        void openChat();

        /**
         * <h2>getlanguageCode</h2>
         * <p>get the country code</p>
         * @return : languageCode
         */
        String getlanguageCode();
    }
}
