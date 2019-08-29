package com.delivx.app.storeDetails;

import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.Cancel.CancelData;
import com.delivx.pojo.Cancel.CancelDataPojo;

import java.util.ArrayList;

public interface StoreDetailsContract
{
    interface View extends BaseView{

        /**
         * <h2>initActionBar</h2>
         * <p>initailaizing the action bar</p>
         */
        void initActionBar();

        /**
         * <h2>setTitle</h2>
         * <p>set the text to title in action bar</p>
         * @param title : activity title
         */
        void setTitle(String title);

        /**
         * <h2>setViews</h2>
         * <p>set the view by value</p>
         * @param appointments : appointment details pojo
         */
        void setViews(AssignedAppointments appointments);

        /**
         * <h2>onSuccess</h2>
         * <p>moving to the next activity(BookingRide)</p>
         * @param appointments : appointments details Pojo
         */
        void onSuccess(AssignedAppointments appointments);

        /**
         * <h2>openChatAct</h2>
         * <p>Moving ChattingActivity to chat</p>
         * @param appointments : appointments details Pojo
         */
        void openChatAct(AssignedAppointments appointments);

        /**
         * <h2>onError</h2>
         * <p>show the error message if any issues in while API calling</p>
         * @param message : error information
         */
        void onError(String message);

        /**
         * <h2>hideViews</h2>
         * <p>if appointments are not available hiding the views</p>
         */
        void hideViews();

        void hideKeyboard();

        void hideSeekBar();

        void moveHomeActivity();

        void cancelDialog(CancelData cancelData);


    }
    interface Presenter{

        /**
         * <h2>setActionBar</h2>
         * <p>setting the action bar</p>
         */
        void setActionBar();

        /**
         * <h2>setActionBarTitle</h2>
         * <p>set the title to action bar</p>
         */
        void setActionBarTitle();

        /**
         * <h2>getBundleData</h2>
         * <p>get the data from previous (CurrentUpcomingJobRVA)</p>
         * @param bundle :data
         */
        void getBundleData(Bundle bundle);

        /**
         * <h2>callCustomer</h2>
         * <p>action for dial phone number</p>
         * @param isCustomer : phone number
         */
        void callCustomer(boolean isCustomer);

        void cancelReason();
        /**
         * <h2>updateBookingStatus</h2>
         * <p>Calling the API to update the seekbar status</p>
         */
        void updateBookingStatus();

        /**
         * ,<h2>openChat</h2>
         * <p>open chat activity</p>
         */
        void openChat();

        void hideSoftKeyboard();

        void cancelOrder();
        /**
         * <h2>getlanguageCode</h2>
         * <p>get the language code</p>
         * @return : language code
         */
        String getlanguageCode();
    }
}
