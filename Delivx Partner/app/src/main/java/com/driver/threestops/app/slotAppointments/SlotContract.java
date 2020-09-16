package com.driver.threestops.app.slotAppointments;

import android.os.Bundle;

import com.driver.threestops.BaseView;
import com.driver.threestops.pojo.AssignedAppointments;

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
