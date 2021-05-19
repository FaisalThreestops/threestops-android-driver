package com.driver.threestops.utility;


/**
 * <h1>AppConstants</h1>
 * <p>save the status for check booking and online status</p>
 */
public enum AppConstants {

    getAptStatus_OnTheWay("6"),
    getAptStatus_Arrived("7"),
    getAptStatus_LoadedAndDelivery("8"),
    getAptStatus_reached_drop_loc("9"),
    getAptStatus_Unloaded("16"),
    getAptStatus_Completed("10");

    public String value;

    AppConstants(String value) {
        this.value = value;
    }

    public interface ACTION
    {
        String MAIN_ACTION = "com.driver.foregroundservice.action.main";
        String STARTFOREGROUND_ACTION = "com.driver.service.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.driver.service.action.stopforeground";
        String PUSH_ACTION = "com.driver.firebase.action";
    }

    public interface NOTIFICATION_ID
    {
        int FOREGROUND_SERVICE = 108;
    }
    public interface BookingStatus
    {
        String Accept="8";
        String Reject="9";
        String OnTheWay="10";
        String Arrived="11";
        String JourneyStarted="12";
        String ReachedAtLocation="13";
        String Completed="14";
        String Done="15";
    }

    public static final String WALLET_ACT = "act_wallet";
    public static  boolean WALLET_AVAILABLE=false;
    public static  String WALLET_AMOUNT="0.00";
    public static final String CHOOSEPAYMENT_ACT = "act_choose_payment";
    public static final int REQUEST_CODE_PERMISSION_MULTIPLE = 123;
    public static final String ADDCARD_ACT = "act_addcard";
    public static final String PAYMENT_ACT = "act_payment";
    public static final String CARD_DETAIL_ACT = "act_carddetail";
    public static  boolean Offline_status = false;

}
