package com.delivx.signup.perosonal;

import android.content.Intent;

import com.delivx.pojo.Cities;

import java.io.File;
import java.util.ArrayList;


public interface PersonalPresenterContract {

    /**
     * <h2>getZonesOnCity</h2>
     * <p>get the zone lists</p>
     * @param cityID  city ID
     */
    void getZonesOnCity(String cityID);

    /**
     * <h2>getCityId</h2>
     * <p>get the CityID</p>
     * @return
     */
    String getCityId();

    /**
     * <h2>setActionBar</h2>
     * <p>Setting the cancel option in actionbar</p>
     */
    void setActionBar();

    /**
     * <h2>setActionBarTitle</h2>
     * <p>Setting the name for titleBar</p>
     */
    void setActionBarTitle();

    /**
     * <h2>getCountryCode</h2>
     * <p>getting the country code,maximum phone number minimum pone number</p>
     */
    void getCountryCode();

    /**
     * <h2>showDialogForCountryPicker</h2>
     * <p>picking the country by country code</p>
     */
    void showDialogForCountryPicker();

    /**
     * <h2>validateEmail</h2>
     * <p>validating the email address</p>
     * @param email : email address
     */
    void validateEmail(String email);

    /**
     * <h2>validatePhone</h2>
     * <p>checking the phone number</p>
     * @param countryCode : country code
     * @param phone : with phone number
     */
    void validatePhone(String countryCode, String phone);

    void validateReferralCode(String code);

    /**
     * <h2>onOutSideTouch</h2>
     * <p>hides the keyboard </p>
     */
    void onOutSideTouch();

    /**
     * <h2>onActivityResult</h2>
     * <p>passing onactivity result from activity to model</p>
     * @param requestCode :requestcode
     * @param resultCode :result code
     * @param data : activity result
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * <h2>validateFields</h2>
     * <p>Validating the fields in the signUP</p>
     * @param fName :first name
     * @param mob :mobile number
     * @param email : email address
     * @param password :password
     * @param lastName :lastname
     * @param countryCode :country code
     * @param referral
     * @param city :city name
     * @param expiryDate :expirtdate
     * @param license :license
     * @param dob :date of birth
     */
    void validateFields( String fName, String mob, String email, String password, String lastName, String countryCode, String referral,String city, String expiryDate,String license,String dob);

    /**
     * <h2>getCity</h2>
     * <p>get the city to verify </p>
     */
    void getCity();

    /**
     * <h2>onCitySelected</h2>
     * <p>verifying the city to add the profile</p>
     * @param cities
     */
    void onCitySelected(ArrayList<Cities> cities);

    /**
     * <h2>uploadProfileImg</h2>
     * <p>uploading the profile image to amazone site</p>
     * @param file : uploaded profile image path
     */
    void uploadProfileImg(File file);

    /**
     * <h2>uploadLicenseImg</h2>
     * <p>uploading the license image to amazone site</p>
     * @param file :uploaded license image path
     */
    void uploadLicenseImg(File file);

    /**
     * <h2>uploadLicenseBackImg</h2>
     * <p>uploading the license back image to amazone site</p>
     * @param file :uploaded license image path
     */
    void uploadLicenseBackImg(File file);

    /**
     * <h2>profileOnclick</h2>
     * <p>adding the profile picture the profile</p>
     */
    void profileOnclick();

    /**
     * <h2>licenseOnclik</h2>
     * <p>adding the license images</p>
     */
    void licenseOnclik();

    /**
     * <h2>openCalender</h2>
     * <p>opens the calender to select the dtaes</p>
     * @param type: 1:expiry date,, 2: pass date
     */
    void openCalender(int type);
}
