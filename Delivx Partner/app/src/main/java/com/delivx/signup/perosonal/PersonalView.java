package com.delivx.signup.perosonal;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.Cities;
import com.delivx.pojo.Zone;

import java.util.ArrayList;


public interface PersonalView extends BaseView {

    /**
     * <h2>sendZones</h2>
     * <p>send the zone list with the specified city</p>
     * @param zones : Zones List
     */
    void sendZones(ArrayList<Zone> zones);

    /**
     * <h2>initActionBar</h2>
     * <p2>implementing the cancel option in Action Bar</p2>
     */
    void initActionBar();

    /**
     * <h2>setTitle</h2>
     * <p>Set the SignUp name for Action Bar</p>
     */
    void setTitle();

    /**
     * <h2>setCountryFlag</h2>
     * <p>passing the country code,flag,minlength phone number and maxlength phone number</p>
     * @param drawableID :Flag Resource
     * @param dialCOde :country code
     * @param minLength :minimum length phone number
     * @param maxLength :maximum length phone number
     */
    void setCountryFlag(int drawableID,String dialCOde,int minLength,int maxLength);


    /**
     * <h1>setMaxLength</h1>
     * <p>Set  maximum number of phone number for validation</p>
     * @param length mobile number length
     */
    void setMaxLength(int length);

    /**
     * <h2>onEmailError</h2>
     * <p>passing the email error message </p>
     * @param msg : error message
     */
    void onEmailError(String msg);

    /**
     * <h2>onPhoneError</h2>
     * <p>passing the phone number error message </p>
     * @param msg :phone number error message
     */
    void onPhoneError(String msg);

    /**
     * <h2>onFirstNameError</h2>
     * <p>error message, first name is empty</p>
     * @param msg :error message for first name
     */
    void onFirstNameError(String msg);

    /**
     * <h2>onPasswordError</h2>
     * <p>showing the error message for password is not valid</p>
     * @param password :error message for password
     */
    void onPasswordError(String password);

    /**
     * <h2>moveToVehicleDetails</h2>
     * <p>open ForgotPasswordVerify for verifying phone number</p>
     * @param bundle : contains all Sign up data
     */
    void moveToVehicleDetails(Bundle bundle);

    /**
     * <h2>moveToZoneList</h2>
     * <p>show zoneList after selecting the city</p>
     * @param cities :zone list
     */
    void moveToZoneList(ArrayList<Cities> cities);

    /**
     * <h2>setCityTextView</h2>
     * <p>setting the city name</p>
     * @param zone :city name
     */
    void setCityTextView(String zone);

    /**
     * <h2>setZoneTextView</h2>
     * <p>setting the zone name</p>
     * @param zone :zone name
     */
    void setZoneTextView(String zone);

    /**
     * <h2>startCropImage</h2>
     * <p>Cropping the upload images</p>
     */
    void startCropImage();

    /**
     * <h2>setProfileImage</h2>
     * <p>setting the image to profile by passing bitmap</p>
     * @param bitmap : passing profile picture resource
     */
    void setProfileImage(Bitmap bitmap);

    /**
     * <h2>setLicenseImage</h2>
     * <p>setting the image to license front imageView by passing bitmap</p>
     * @param bitmap : passing license picture resource
     */
    void setLicenseImage(Bitmap bitmap);

    /**
     * <h2>setLicenseBackImage</h2>
     * <p>setting the image to license back imageView by passing bitmap</p>
     * @param bMap :  passing license picture resource
     */
    void setLicenseBackImage(Bitmap bMap);

    /**
     * <h2>onError</h2>
     * <p>show message </p>
     * @param error : messages
     */
    void onError(String error);

    /**
     * <h2>uploadProfileError</h2>
     * <p>set default profile image</p>
     */
    void uploadProfileError();

    /**
     * <h2>uploadLicenseError</h2>
     * <p>set default license image </p>
     */
    void uploadLicenseError();

    /**
     * <h2>getCountryCode</h2>
     * <p>get country code from text</p>
     * @return :country code
     */
    String getCountryCode();

    /**
     * <h2>enableLogin</h2>
     * <p>enabling the login option </p>
     */
    void enableLogin();

    /**
     * <h2>setExpiryDate</h2>
     * <p>adding the expiry date to the textview</p>
     * @param format : expiry date
     */
    void setExpiryDate(String format);

    /**
     * <h2>setBirthDate</h2>
     * <p>adding DOB to the textview</p>
     * @param format : Date of birth
     */
    void setBirthDate(String format);

    /**
     * <h2>uploadLicenseBackError</h2>
     * <p>error message for while uploading the license back side image</p>
     */
    void uploadLicenseBackError();

}
