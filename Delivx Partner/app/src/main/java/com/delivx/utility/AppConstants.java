package com.delivx.utility;


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

}
