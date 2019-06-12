package com.delivx.app.storePickUp;

import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.ShipmentDetails;

/**
 * Created by DELL on 06-02-2018.
 */

public interface PickUpContract {

    interface ViewOperations extends BaseView
    {
        void initActionBar();

        void setTitle(String title);

        void setViews(AssignedAppointments appointments);

        void onSuccess(AssignedAppointments appointments);

        void openChatAct(AssignedAppointments appointments);

        void onError(String message);

        void openOrderEditDialog(ShipmentDetails shipmentDetails, AssignedAppointments currency);
    }

    interface PresenterOperations
    {
        void setActionBar();

        void setActionBarTitle();

        void getBundleData(Bundle bundle);

        void callCustomer();

        void updateBookingStatus();

        void editOrder(ShipmentDetails shipmentDetails);

        void updateOrder(ShipmentDetails shipmentDetails, String qty);

        void deleteItem(ShipmentDetails shipmentDetails);

        void openChat();

        String getlanguageCode();
    }
}
