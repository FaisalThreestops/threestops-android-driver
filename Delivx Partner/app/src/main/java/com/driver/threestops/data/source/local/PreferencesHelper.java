package com.driver.threestops.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.driver.threestops.login.language.LanguagesList;
import com.driver.threestops.pojo.ServiceZoneList;
import com.google.gson.Gson;
import com.driver.threestops.data.source.PreferenceHelperDataSource;

import javax.inject.Inject;



public class PreferencesHelper implements PreferenceHelperDataSource {

    private String PREF_NAME = "getFlexyDriver";
    private SharedPreferences sharedPreferences=null;
    private Gson gson;
    private SharedPreferences.Editor editor=null;




    @Inject
    public PreferencesHelper(Context context) {
        int PRIVATE_MODE = 0;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        editor.commit();
        gson = new Gson();
    }

    @Override
    public void setMyEmail(String email) {
        editor.putString(PreferenceKeys.KeysEntry.EMAIL,email);
        editor.commit();
    }

    @Override
    public String getMyEmail() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.EMAIL,"3embedsoft@gmail.com");
    }

    @Override
    public void setLanguage(String language) {
        editor.putString(PreferenceKeys.KeysEntry.LANGUAGE,language);
        editor.commit();
    }

    @Override
    public String getLanguage() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.LANGUAGE,"en");
    }

    @Override
    public void setPassword(String password) {
        editor.putString(PreferenceKeys.KeysEntry.PASSWORD,password);
        editor.commit();
    }

    @Override
    public String getPassword() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.PASSWORD,"");
    }

    @Override
    public void setDocumentID(String id) {
        editor.putString(PreferenceKeys.KeysEntry.DOCUMENT_ID,id);
        editor.commit();
    }

    @Override
    public String getDocumentID() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.DOCUMENT_ID,"");
    }

    @Override
    public void setTimerStarted(boolean started) {
        editor.putBoolean(PreferenceKeys.KeysEntry.TIMER_STARTED,started);
        editor.commit();
    }

    @Override
    public boolean isTimerStarted() {
        return sharedPreferences.getBoolean(PreferenceKeys.KeysEntry.TIMER_STARTED,false);
    }

    @Override
    public void setTimeWhilePaused(long milliSec) {
        editor.putLong(PreferenceKeys.KeysEntry.TIMER_WHILE_PAUSED,milliSec);
        editor.commit();
    }

    @Override
    public long getTimeWhilePaused() {
        return sharedPreferences.getLong(PreferenceKeys.KeysEntry.TIMER_WHILE_PAUSED,0);

    }

    @Override
    public void setTripStartedInterval(String interval) {
        editor.putString(PreferenceKeys.KeysEntry.TRIP_STARTED_INTERVAL,interval);
        editor.commit();
    }

    @Override
    public int getTripStartedInterval() {
        return Integer.parseInt(sharedPreferences.getString(PreferenceKeys.KeysEntry.TRIP_STARTED_INTERVAL,"10"));
    }

    @Override
    public void setBookings(String bookings) {
        editor.putString(PreferenceKeys.KeysEntry.ASSIGNED_BOOKINGS,bookings);
        editor.commit();
    }

    @Override
    public String getBookings() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.ASSIGNED_BOOKINGS,"");
    }

    @Override
    public void setLastBooking(String bid) {
        editor.putString(PreferenceKeys.KeysEntry.LAST_BOOKING,bid);
        editor.commit();
    }

    @Override
    public String getLastBooking() {
      return sharedPreferences.getString(PreferenceKeys.KeysEntry.LAST_BOOKING,"");
    }

    @Override
    public void setDriverType(String status) {
        editor.putString(PreferenceKeys.KeysEntry.DRIVER_TYPE, status);
        editor.commit();
    }

    @Override
    public String getDriverType() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.DRIVER_TYPE, "@");
    }

    @Override
    public void setMasterStatus(int status) {
        editor.putInt(PreferenceKeys.KeysEntry.DRIVER_STATUS, status);
        editor.commit();
    }

    @Override
    public int getMasterStatus() {
        return sharedPreferences.getInt(PreferenceKeys.KeysEntry.DRIVER_STATUS, 4);
    }

    @Override
    public void setDriverCurrentLongi(String longi) {
        editor.putString(PreferenceKeys.KeysEntry.CUR_LONGITUDE, longi);
        editor.commit();
    }

    @Override
    public double getDriverCurrentLongi() {
        String driverCurrentLongiStr = sharedPreferences.getString(PreferenceKeys.KeysEntry.CUR_LONGITUDE, "0.0");
        double driverCurrentLongi = 0.0;
        try {
            driverCurrentLongi = Double.parseDouble(driverCurrentLongiStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return driverCurrentLongi;
    }

    @Override
    public void setDriverCurrentLat(String lat) {
        editor.putString(PreferenceKeys.KeysEntry.CUR_LATITUDE, lat);
        editor.commit();
    }

    @Override
    public double getDriverCurrentLat() {
        String driverCurrentLatStr = sharedPreferences.getString(PreferenceKeys.KeysEntry.CUR_LATITUDE, "0.0");
        double driverCurrentLat = 0.0;
        try {
            driverCurrentLat = Double.parseDouble(driverCurrentLatStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return driverCurrentLat;
    }

    @Override
    public void setCurrencySymbol(String symbol) {
        editor.putString(PreferenceKeys.KeysEntry.CURRENCY_SYMBOL,symbol);
        editor.commit();
    }

    @Override
    public String getCurrencySymbol() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.CURRENCY_SYMBOL,"$");
    }

    @Override
    public void setMileageMetric(String metric) {
        editor.putString(PreferenceKeys.KeysEntry.DISTANCE_METRIC,metric);
        editor.commit();
    }

    @Override
    public String getMileageMetric() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.DISTANCE_METRIC,"Miles");
    }

    @Override
    public void setDistanceConvUnit(String unit) {
        editor.putString(PreferenceKeys.KeysEntry.DISTANCE_UNIT,unit);
        editor.commit();
    }

    @Override
    public String getDistanceConvUnit() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.DISTANCE_UNIT,"0.00062137");
    }

    @Override
    public void setPresenceInterval(String distance) {
        editor.putString(PreferenceKeys.KeysEntry.PRESENCE_INTERVAL,distance);
        editor.commit();
    }

    @Override
    public String getPresenceInterval() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.PRESENCE_INTERVAL,"5");
    }

    @Override
    public void setMinDistForRouteArray(String distance) {
        editor.putString(PreferenceKeys.KeysEntry.MIN_DIST_FOR_ROUTE,distance);
        editor.commit();
    }

    @Override
    public int getMinDistForRouteArray() {
        return Integer.parseInt(sharedPreferences.getString(PreferenceKeys.KeysEntry.MIN_DIST_FOR_ROUTE,"5"));
    }

    /**
     * <h2>setMaxDistForRouteArray</h2>
     * This method is used to set the distance
     *
     * @param distance distance
     */
    @Override
    public void setMaxDistForRouteArray(String distance) {
        editor.putString(PreferenceKeys.KeysEntry.MAX_DIST_FOR_ROUTE,distance);
        editor.commit();
    }

    /**
     * <h2>getMaxDistForRouteArray</h2>
     * This method is used to get the MaxDistForRouteArray
     */
    @Override
    public int getMaxDistForRouteArray() {
        return Integer.parseInt(sharedPreferences.getString(PreferenceKeys.KeysEntry.MAX_DIST_FOR_ROUTE,"500"));

    }

    @Override
    public void setPushTopic(String pushTopic) {
        editor.putString(PreferenceKeys.KeysEntry.PUSH_TOPIC,pushTopic);
        editor.commit();
    }

    @Override
    public String getPushTopic() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.PUSH_TOPIC,null);

    }

    @Override
    public void setProfilePic(String url) {
        editor.putString(PreferenceKeys.KeysEntry.PROFILE_PIC,url);
        editor.commit();
    }

    @Override
    public String getProfilePic() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.PROFILE_PIC,null);
    }

    @Override
    public void setMyName(String name) {
        editor.putString(PreferenceKeys.KeysEntry.MY_NAME,name);
        editor.commit();
    }

    @Override
    public String getMyName() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.MY_NAME,null);
    }

    @Override
    public void setVehicles(String vehicles) {
        editor.putString(PreferenceKeys.KeysEntry.VEHICLES,vehicles);
        editor.commit();
    }

    @Override
    public String getVehicles() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.VEHICLES,null);

    }

    @Override
    public void setPresenceChannel(String channel) {
        editor.putString(PreferenceKeys.KeysEntry.PRESENCE_CHN,channel);
        editor.commit();
    }

    @Override
    public String getPresenceChannel() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.PRESENCE_CHN,null);
    }

    @Override
    public void setToken(String token) {
        editor.putString(PreferenceKeys.KeysEntry.TOKEN,token);
        editor.commit();
    }

    @Override
    public String getToken() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.TOKEN,null);
    }

    @Override
    public void setDriverID(String id) {
        editor.putString(PreferenceKeys.KeysEntry.MID,id);
        editor.commit();
    }

    @Override
    public String getDriverID() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.MID,null);
    }

    @Override
    public void setDriverChannel(String chn) {
        editor.putString(PreferenceKeys.KeysEntry.CHN,chn);
        editor.commit();
    }

    @Override
    public String getDriverChannel() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.CHN,null);
    }

    @Override
    public void setDriverChannel_msg(String chn) {
        editor.putString(PreferenceKeys.KeysEntry.CHN_MSG,chn);
        editor.commit();
    }

    @Override
    public String getDriverChannel_msg() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.CHN_MSG,null);
    }

    @Override
    public void setVehicleId(String id) {
        editor.putString(PreferenceKeys.KeysEntry.VEH_ID,id);
        editor.commit();
    }

    @Override
    public String getVehicleId() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.VEH_ID,"");
    }

    @Override
    public void setDeviceId(String deviceId) {
        editor.putString(PreferenceKeys.KeysEntry.DEVICE_ID,deviceId);
        editor.commit();
    }

    @Override
    public String getDeviceId() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.DEVICE_ID,null);
    }

    @Override
    public void setFCMRegistrationId(String regId) {
        editor.putString(PreferenceKeys.KeysEntry.REG_ID,regId);
        editor.commit();
    }

    @Override
    public String getFCMRegistrationId() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.REG_ID,"aksdoihrqr");
    }

    @Override
    public void setIsLogin(boolean isLogin) {
        editor.putBoolean(PreferenceKeys.KeysEntry.LOGGED_IN,isLogin);
        editor.commit();
    }

    @Override
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(PreferenceKeys.KeysEntry.LOGGED_IN,false);
    }

    @Override
    public void clearSharedPredf() {
        editor.clear();
        editor.apply();
    }


    private static final String LANGUAGE_SETTINGS = "LANGUAGE_SETTINGS";
    @Override
    public LanguagesList getLanguageSettings() {
        String jsonString = sharedPreferences.getString(LANGUAGE_SETTINGS, "");
        return new Gson().fromJson(jsonString, LanguagesList.class);
    }

    @Override
    public void setLanguageSettings(LanguagesList languageSettings) {
        if (languageSettings != null)
        {
            String jsonString = new Gson().toJson(languageSettings);
            editor.putString(LANGUAGE_SETTINGS, jsonString);
            editor.commit();
        }
        else
        {
            editor.putString(LANGUAGE_SETTINGS, "");
            editor.commit();
        }

    }

    @Override
    public void setDefaultBankAccount(String bankaccount) {
        editor.putString(PreferenceKeys.KeysEntry.BANK_ACCOUT,bankaccount);
        editor.commit();
    }

    @Override
    public String getDefaultBankAccount() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.BANK_ACCOUT,"Account Number");
    }

    @Override
    public void setCountry(String country) {
        editor.putString(PreferenceKeys.KeysEntry.COUNTRY,country);
        editor.commit();
    }

    @Override
    public String getCountry() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.COUNTRY_CODE,"IN");
    }

    @Override
    public void setCountryCode(String countryCode) {
        editor.putString(PreferenceKeys.KeysEntry.COUNTRY_CODE,countryCode);
        editor.commit();
    }

    @Override
    public String getCountryCode() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.COUNTRY,"+91");
    }

    @Override
    public void setMobileNo(String mobileNo) {
        editor.putString(PreferenceKeys.KeysEntry.MOBILE_NO, mobileNo);
        editor.commit();
    }

    @Override
    public String getMobileNo() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.MOBILE_NO,"");
    }

    @Override
    public void setCurrency(String currency) {
        editor.putString(PreferenceKeys.KeysEntry.CURRENCY,currency);
        editor.commit();
    }

    @Override
    public String getCurrency() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.CURRENCY,"INR");
    }

    @Override
    public void setEnableBankAccount(String enableBank) {
        editor.putString(PreferenceKeys.KeysEntry.ENABLE_BANK_ACCCOUNT,enableBank);
        editor.commit();
    }

    @Override
    public String getEnableBankAccount() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.ENABLE_BANK_ACCCOUNT,"1");
    }


    private static final String serviceZoneList_ = "ServiceZoneList";
    @Override
    public ServiceZoneList getServiceZoneList() {
        String jsonString = sharedPreferences.getString(serviceZoneList_, "");
        return new Gson().fromJson(jsonString, ServiceZoneList.class);
    }

    @Override
    public void setServiceZoneList(ServiceZoneList serviceZoneList) {
        if (serviceZoneList != null)
        {
            String jsonString = new Gson().toJson(serviceZoneList);
            editor.putString(serviceZoneList_, jsonString);
            editor.commit();
        }
        else
        {
            editor.putString(serviceZoneList_, "");
            editor.commit();
        }

    }





    @Override
    public void setCityId(String cityId) {
        editor.putString(PreferenceKeys.KeysEntry.CITY_ID,cityId);
        editor.commit();
    }

    @Override
    public String getCityId() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.CITY_ID,"");

    }

    @Override
    public void setDriverScheduleType(int driverStatus) {
        editor.putInt(PreferenceKeys.KeysEntry.DRS,driverStatus);
        editor.commit();
    }

    @Override
    public int getDriverScheduleType() {
        return sharedPreferences.getInt(PreferenceKeys.KeysEntry.DRS,0);
    }

    @Override
    public void setStripeKey(String stripeKey) {
        editor.putString(PreferenceKeys.KeysEntry.STRIPE_KEY, stripeKey);
        editor.commit();
    }

    @Override
    public String getStripe() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.STRIPE_KEY, "");
    }

    @Override
    public void setRequesterId(String rId) {
        editor.putString(PreferenceKeys.KeysEntry.REQUESTER_ID, rId);
        editor.commit();
    }

    @Override
    public String getRequesterId() {
        return sharedPreferences.getString(PreferenceKeys.KeysEntry.REQUESTER_ID, null);
    }
}
