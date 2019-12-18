package com.delivx.app.slotAppointments;

import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;

import java.util.ArrayList;

public interface SlotContract {

    interface ViewOperations extends BaseView
    {
        void initActionBar();

        void setTitle(String title);

        void setViews(ArrayList<AssignedAppointments> appointments);

        void openGoogleMap(String uri);
    }

    interface PresenterOperations
    {
        void setActionBar();

        void setActionBarTitle();

        void getBundleData(Bundle bundle);

        void getDirection(ArrayList<AssignedAppointments> slotResponseAppointments);

        String getlanguageCode();
    }
}
