package com.delivx.data.source;

import com.delivx.login.language.LanguagesList;
import com.delivx.pojo.ServiceZoneList;

import java.util.ArrayList;


public interface PreferenceHelperDataSource
{

    /**
     * <h2>setMyEmail</h2>
     * This method is used to set Email
     * @param email
     */
    void setMyEmail(String email);
    /**
     * <h2>getMyEmail</h2>
     * This method is used to get the email
     *
     */
    String getMyEmail();

    /**
     * <h2>setLanguage</h2>
     * This method is used to set the language
     * @param language
     */
    void setLanguage(String language);
    /**
     * <h2>getLanguage</h2>
     * This method is used to get the language
     *
     */
    String getLanguage();
    /**
     * <h2>setPassword</h2>
     * This method is used to set the password
     * @param password
     */
    void setPassword(String password);
    /**
     * <h2>getPassword</h2>
     * This method is used to get the password
     *
     */
    String getPassword();
    /**
     * <h2>setDocumentId</h2>
     * This method is used to set Document Id for couch
     * @param id
     */
    void setDocumentID(String id);
    /**
     * <h2>getDocumentId</h2>
     * This method is used to get Document Id for couch
     *
     */
    String getDocumentID();
    /**
     * <h2>setTimerStarted</h2>
     * This method is used to set if the timer started
     * @param started true if started  else false
     */
    void setTimerStarted(boolean started);
    /**
     * <h2>isTimerStarted</h2>
     * This method is used to get if the timer started
     * @return  true if started  else false
     */
    boolean isTimerStarted();
    /**
     * <h2>setTimeWhilePaused</h2>
     * This method is used to set Time While Paused
     * @param milliSec milliSec
     */
    void setTimeWhilePaused(long milliSec);
    /**
     * <h2>getTimeWhilePaused</h2>
     * This method is used to get Time While Paused
     */
    long getTimeWhilePaused();
    /**
     * <h2>setTripStartedInterval</h2>
     * This method is used to set TripStartedInterval
     * @param interval bookings
     */
    void setTripStartedInterval(String interval);
    /**
     * <h2>getTripStartedInterval</h2>
     * This method is used to get TripStartedInterval
     */
    int getTripStartedInterval();
    /**
     * <h2>setBookings</h2>
     * This method is used to set Bookings
     * @param bookings bookings
     */
    void setBookings(String bookings);
    /**
     * <h2>getBookings</h2>
     * This method is used to get Bookings
     */
    String getBookings();
    /**
     * <h2>setLastBooking</h2>
     * This method is used to set Last Booking id
     * @param bid bid
     */
    void setLastBooking(String bid);
    /**
     * <h2>getLastBooking</h2>
     * This method is used to get Last Booking id
     */
    String getLastBooking();


    void setDriverType(String driverType);

    String getDriverType();

    /**
     * <h2>setMasterStatus</h2>
     * This method is used to set the Master Status
     * @param status status
     */
    void setMasterStatus(int status);
    /**
     * <h2>getMasterStatus</h2>
     * This method is used to get the Master Status
     */
    int getMasterStatus();
    /**
     * <h2>setDriverCurrentLongi</h2>
     * This method is used to set the Driver Current Longitude
     * @param longi longitude
     */
    void setDriverCurrentLongi(String longi);
    /**
     * <h2>getDriverCurrentLongi</h2>
     * This method is used to get the Driver Current Longitude
     */
    double getDriverCurrentLongi();

    /**
     * <h2>setDriverCurrentLat</h2>
     * This method is used to set the Driver Current Latitude
     * @param lat latitude
     */
    void setDriverCurrentLat(String lat);
    /**
     * <h2>getDriverCurrentLat</h2>
     * This method is used to get the Driver Current Latitude
     */
    double getDriverCurrentLat();
    /**
     * <h2>setCurrencySymbol</h2>
     * This method is used to set the Currency Symbol
     * @param symbol distance
     */
    void setCurrencySymbol(String symbol);
    /**
     * <h2>getCurrencySymbol</h2>
     * This method is used to get the Currency Symbol
     */
    String getCurrencySymbol();
    /**
     * <h2>setMileageMetric</h2>
     * This method is used to set the metric
     * @param metric distance
     */
    void setMileageMetric(String metric);
    /**
     * <h2>getMileageMetric</h2>
     * This method is used to get the Mileage  Metric
     */
    String getMileageMetric();
    /**
     * <h2>setDistanceConvUnit</h2>
     * This method is used to set the unit
     * @param unit distance
     */
    void setDistanceConvUnit(String unit);
    /**
     * <h2>getDistanceConvUnit</h2>
     * This method is used to get the Distance Conversion Unit
     */
    String getDistanceConvUnit();
    /**
     * <h2>setPresenceInterval</h2>
     * This method is used to set the interval
     * @param interval distance
     */
    void setPresenceInterval(String interval);
    /**
     * <h2>getPresenceInterval</h2>
     * This method is used to get the getPresenceInterval
     */
    String getPresenceInterval();
    /**
     * <h2>setMinDistForRouteArray</h2>
     * This method is used to set the distance
     * @param distance distance
     */
    void setMinDistForRouteArray(String distance);
    /**
     * <h2>getMinDistForRouteArray</h2>
     * This method is used to get the MinDistForRouteArray
     */
    int getMinDistForRouteArray();

    /**
     * <h2>setMaxDistForRouteArray</h2>
     * This method is used to set the distance
     * @param distance distance
     */
    void setMaxDistForRouteArray(String distance);
    /**
     * <h2>getMaxDistForRouteArray</h2>
     * This method is used to get the MaxDistForRouteArray
     */
    int getMaxDistForRouteArray();
    /**
     * <h2>setPushTopic</h2>
     * This method is used to set the url
     * @param pushTopic pushTopic
     */
    void setPushTopic(String pushTopic);
    /**
     * <h2>getPushTopic</h2>
     * This method is used to get the pushTopic
     */
    String getPushTopic();
    /**
     * <h2>setProfilePic</h2>
     * This method is used to set the url
     * @param url url
     */
    void setProfilePic(String url);
    /**
     * <h2>getProfilePic</h2>
     * This method is used to get the url
     */
    String getProfilePic();
    /**
     * <h2>setMyName</h2>
     * This method is used to set the Driver Name
     * @param name name
     */
    void setMyName(String name);
    /**
     * <h2>getMyName</h2>
     * This method is used to get the Driver Name
     */
    String getMyName();
    /**
     * <h2>setVehicles</h2>
     * This method is used to set the vehicles
     * @param vehicles vehicles
     */
    void setVehicles(String vehicles);
    /**
     * <h2>getVehicles</h2>
     * This method is used to get the vehicles
     */
    String getVehicles();
    /**
     * <h2>setPresenceChannel</h2>
     * This method is used to set the presence channel
     * @param channel channel
     */
    void setPresenceChannel(String channel);
    /**
     * <h2>getPresenceChannel</h2>
     * This method is used to get the presence channel
     */
    String getPresenceChannel();
    /**
     * <h2>.setToken</h2>
     * This method is used to set the token
     * @param token ID
     */
    void setToken(String token);
    /**
     * <h2>getToken</h2>
     * This method is used to get the token
     */
    String getToken();
    /**
     * <h2>.setDriverChannel</h2>
     * This method is used to set the Driver ID
     * @param id ID
     */
    void setDriverID(String id);
    /**
     * <h2>getDriverID</h2>
     * This method is used to get the Driver ID
     */
    String getDriverID();
    /**
     * <h2>.setDriverChannel</h2>
     * This method is used to set the channel name
     * @param chn channel
     */
    void setDriverChannel(String chn);
    /**
     * <h2>getDriverChannel</h2>
     * This method is used to get the channel
     */
    String getDriverChannel();

    void setDriverChannel_msg(String chn);
    String getDriverChannel_msg();

    /**
     * <h2>.setVehicleId</h2>
     * This method is used to set the vehicle Id
     * @param id vehicle Id
     */
    void setVehicleId(String id);
    /**
     * <h2>getVehicleId</h2>
     * This method is used to get the vehicle Id
     */
    String getVehicleId();

    /**
     * <h2>setDeviceId</h2>
     * This method is used to set the Device ID
     * @param deviceId Device ID
     */
    void setDeviceId(String deviceId);
    /**
     * <h2>getDeviceId</h2>
     * This method is used to get the Device ID
     */
    String getDeviceId();
    /**
     * <h2>.setFCMRegistrationId</h2>
     * This method is used to set the FCM push token
     * @param regId FCM push token
     */
    void setFCMRegistrationId(String regId);
    /**
     * <h2>getFCMRegistrationId</h2>
     * This method is used to get the FCM push token
     */
    String getFCMRegistrationId();

    /**
     * <h2>setIsLogin</h2>
     * This method is used to set if the user is logged in
     * @param isLogin true if logged in else false
     */
    void setIsLogin(boolean isLogin);
    /**
     * <h2>isLoggedIn</h2>
     * This method is used to get if the user is logged in
     * @return  true if logged in else false
     */
    boolean isLoggedIn();

    /**
     * <h2>clearSharedPredf</h2>
     * This method is used to clear the shared prefernce
     */
    void clearSharedPredf();


    /**
     * getLanguageSettings      method is used to get language details
     * @return  language data
     */
    LanguagesList getLanguageSettings();

    /**
     *  setLanguageSettings      method is used to set language details
     *  @param languageSettings   language data
     **/
    void setLanguageSettings(LanguagesList languageSettings);



    void setDefaultBankAccount(String bankaccount);
    String getDefaultBankAccount();

    void setCountry(String country);
    String getCountry();

    void setCurrency(String currency);
    String getCurrency();


    void setEnableBankAccount(String enableBank);
    String getEnableBankAccount();

    ServiceZoneList getServiceZoneList();
    void setServiceZoneList(ServiceZoneList serviceZoneList);

    void setCityId(String cityId);
    String getCityId();

    void setDriverScheduleType(int driverStatus);

    int getDriverScheduleType();

    void setStripeKey(String stripeKey);

    String getStripe();
}
