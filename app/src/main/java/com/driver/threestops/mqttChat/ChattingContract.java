package com.driver.threestops.mqttChat;

import android.content.Intent;

import com.driver.threestops.BaseView;

import java.util.ArrayList;

public interface ChattingContract {

        interface ViewOperations extends BaseView{

                /**
                 * <h2>setActionBar</h2>
                 * <p>set the Title to Action bar</p>
                 * @param custName : Activity Title
                 */
                void setActionBar(String custName);

                /**
                 * <h2>setViews</h2>
                 * <p>set the Booking Id to the Tool bar</p>
                 * @param bid : booking ID
                 */
                void setViews(String bid);

                /**
                 * <h2>setRecyclerView</h2>
                 * <p>updating the chat message on recyclerView</p>
                 */
                void setRecyclerView();

                /**
                 * <h2>updateData</h2>
                 * <p>updating the messages on the view</p>
                 * @param chatDataArry
                 */
                void updateData(ArrayList<ChatData> chatDataArry);
        }

        interface PresenterOperations {

                /**
                 * <h2>getData</h2>
                 * <p>get the data from previous(SlotAppointmentActivity,StorePickUpDetails and BookingRide)</p>
                 * @param intent :(SlotAppointmentActivity,StorePickUpDetails and BookingRide)
                 */
                void getData(Intent intent);

                /**
                 * <h2>setActionBar</h2>
                 * <p>set the action bar</p>
                 */
                void setActionBar();

                /**
                 * <h2>initViews</h2>
                 * <p>initializing the action bar</p>
                 */
                void initViews();

                /**
                 * <h2>getChattingData</h2>
                 * <P>get the previously chatting message</P>
                 */
                void getChattingData();

                /**
                 * <h2>message</h2>
                 * <p>sending the text messages</p>
                 * @param message : text message
                 */
                void message(String message);
        }
}


