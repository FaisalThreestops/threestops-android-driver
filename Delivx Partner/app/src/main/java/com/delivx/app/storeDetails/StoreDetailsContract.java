package com.delivx.app.storeDetails;

import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;

/**
 * Created by DELL on 02-02-2018.
 */

public interface StoreDetailsContract
{
    interface View extends BaseView{

        void initActionBar();

        void setTitle(String title);

        void setViews(AssignedAppointments appointments);

        void onSuccess(AssignedAppointments appointments);

        void openChatAct(AssignedAppointments appointments);

        void onError(String message);

        void hideViews();
    }
    interface Presenter{

        void setActionBar();

        void setActionBarTitle();

        void getBundleData(Bundle bundle);

        void callCustomer(boolean isCustomer);

        void updateBookingStatus();

        void openChat();
    }
}
