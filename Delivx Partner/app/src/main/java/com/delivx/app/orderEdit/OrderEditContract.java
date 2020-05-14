package com.delivx.app.orderEdit;

import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.ShipmentDetails;

public interface OrderEditContract {

    interface ViewOperations extends BaseView
    {
        void initActionBar();

        void setTitle(String title);

        void setViews(ShipmentDetails shipmentDetails,AssignedAppointments appointments);

        void moveToReplaceActivity();

        void setCancelAlert();

        void toastMessage(String message);

        void itemCanceled(AssignedAppointments assignedAppointments);

        void openChatAct(AssignedAppointments appointments);

        void setReturnQty();

        void moveHomeActivity();

        void setReturnCompleteAlert();
    }

    interface PresenterOperations
    {
        void setActionBar();

        void setActionBarTitle();

        void getBundleData(Bundle bundle);

        void callCustomer();

        void openChat();

        void openReplaceActivity();

        void returnCompletelyAlert();

        void cancelAlert();

        void cancelItem(String statusType,String updateQty);

        void cancelAllItem(ShipmentDetails shipmentDetails, String qty);

        void openPartiallyDialog();

        String getlanguageCode();
    }
}
