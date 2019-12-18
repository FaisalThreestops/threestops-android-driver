package com.delivx.app.selectedStore;

import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;

import java.util.ArrayList;

public interface SelectedStoreIdContract {

    interface ViewOperations extends BaseView
    {
        void initActionBar();

        void setTitle(String title);

        void setViews(ArrayList<AssignedAppointments> appointments, ArrayList<AssignedAppointments> sortStoreID, int upIndexToItems);

        void onSuccess(ArrayList<AssignedAppointments> appointments);

        void onError(String message);

        void openGoogleMap(String uri);
    }

    interface PresenterOperations
    {
        void setActionBar();

        void setActionBarTitle();

        void getBundleData(Bundle bundle);

        void getDirection();

        void arrived();

        String getlanguageCode();
    }
}
