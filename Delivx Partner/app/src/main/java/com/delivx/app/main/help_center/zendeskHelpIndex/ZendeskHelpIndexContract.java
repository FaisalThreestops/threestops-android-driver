package com.delivx.app.main.help_center.zendeskHelpIndex;


import com.delivx.app.main.help_center.zendeskpojo.OpenClose;

/**
 * <h>ZendeskHelpIndexContract</h>
 * Created by Ali on 2/26/2018.
 */

public interface ZendeskHelpIndexContract
{
    interface Presenter
    {
        void onToGetZendeskTicket();

        String getLanguage();

        void start();
        void stop();
        boolean isNetworkAvailable();
    }
    interface  ZendeskView
    {
        void onGetTicketSuccess();

        void onEmptyTicket();

        void onTicketStatus(OpenClose openClose, int openCloseSize, boolean isOpenClose);

        void onNotifyData();
        void onError(String error);
        void showLoading();
        void hideLoading();
        void stopAct();
    }
}
