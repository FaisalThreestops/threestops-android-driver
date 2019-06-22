package com.delivx.utility;


public class ServiceUrl {

    public static final String TWITTER_URL = "http:www.twitter.com/";

    public static final String BASE_URL = "https://api.uberfortruck.com/";


    private static final String APP_URL = BASE_URL + "app/";


    public static final String CANCELREASONS = APP_URL + "cancelReasons";


    private static final String MASTER_URL = BASE_URL + "master/";

    public static final String CANCELBOOKING = MASTER_URL + "cancelBooking";


    public static final String ADD_DELETE_BANK_DETAILS = MASTER_URL + "stripe/bank/me";

    public static final String SET_DEFAULT_BANK = MASTER_URL + "stripe/bankdefault/me";


}
