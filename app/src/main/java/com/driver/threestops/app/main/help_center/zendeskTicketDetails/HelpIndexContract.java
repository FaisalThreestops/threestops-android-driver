package com.driver.threestops.app.main.help_center.zendeskTicketDetails;

import android.content.Context;
import android.widget.ImageView;

import com.driver.threestops.app.main.help_center.zendeskpojo.ZendeskDataEvent;

import java.util.ArrayList;


/**
 * <h>HelpIndexContract</h>
 * Created by Ali on 2/26/2018.
 */

public interface HelpIndexContract
{
    interface presenter
    {

        void onPriorityImage(Context helpIndexTicketDetails, String priority, ImageView ivHelpCenterPriorityPre);

        void callApiToCommentOnTicket(String trim, int zenId);

        void callApiToCreateTicket(String trim, String subject, String priority);

        void callApiToGetTicketInfo(int zenId);

        String getLanguage();

        void start();
        void stop();
        boolean isNetworkAvailable();
    }
    interface HelpView
    {

        void onTicketInfoSuccess(ArrayList<ZendeskDataEvent> events, String timeToSet, String subject, String priority, String type);

        void onZendeskTicketAdded(String response);
        void onError(String errMsg);
        void showLoading();
        void hideLoading();
        void stopAct();
    }
}
