package com.delivx.data.source.local;

/**
 * Created by DELL on 16-02-2018.
 */

public class PreferenceKeys
{
    private PreferenceKeys() {
    }

    static abstract class KeysEntry{
        public static final String REG_ID = "push_token";
        public static final String PUSH_TOPIC = "push_topic";
        public static final String VEH_ID = "vehicle_id";
        public static final String VEHICLES = "vehicles";
        public static final String CHN = "driver_channel";
        public static final String CHN_MSG = "CHN_MSG";
        public static final String PRESENCE_CHN = "presence_channel";
        public static final String LOGGED_IN = "login";
        public static final String DEVICE_ID = "device_id";
        public static final String MID = "driver_id";
        public static final String TOKEN = "token";
        public static final String MY_NAME = "my_name";
        public static final String PROFILE_PIC = "profile_pic";
        public static final String PRESENCE_INTERVAL = "presence_interval";
        public static final String MIN_DIST_FOR_ROUTE = "mini_dist_for_latlongs";
        public static final String MAX_DIST_FOR_ROUTE = "max_dist_for_latlongs";
        public static final String DISTANCE_UNIT = "distance_unit";
        public static final String DISTANCE_METRIC = "distance_metric";
        public static final String CURRENCY_SYMBOL = "currency";
        public static final String CUR_LONGITUDE = "current_latitude";
        public static final String CUR_LATITUDE = "current_longitude";
        public static final String DRIVER_STATUS = "driver_status";
        public static final String LAST_BOOKING = "last_booking";
        public static final String ASSIGNED_BOOKINGS = "assigned_bookings";
        public static final String TRIP_STARTED_INTERVAL = "trip_started_interval";
        public static final String TIMER_WHILE_PAUSED = "time_while_timer_paused";
        public static final String TIMER_STARTED = "is_timer_started";
        public static final String DOCUMENT_ID = "document_id";
        public static final String PASSWORD = "password";
        public static final String LANGUAGE="language";
        public static final String EMAIL="email";
        public static final String BANK_ACCOUT = "BANK_ACCOUT";
        public static final String COUNTRY = "COUNTRY";
        public static final String CURRENCY = "CURRENCY";
        public static final String ENABLE_BANK_ACCCOUNT = "enableBankAccount";
        public static final String CITY_ID ="CITY_ID" ;
    }
}

