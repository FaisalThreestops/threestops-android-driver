package com.delivx.signup.perosonal;

import android.content.Intent;

import com.delivx.pojo.Cities;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by DELL on 08-01-2018.
 */

public interface PersonalPresenterContract {

    void getZonesOnCity(String cityID);
    String getCityId();
    void setActionBar();

    void setActionBarTitle();

    void getCountryCode();

    void showDialogForCountryPicker();

    void validateEmail(String email);

    void validatePhone(String countryCode, String phone);

    void validateReferralCode(String code);

    void onOutSideTouch();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void validateFields( String fName, String mob, String email, String password, String lastName, String countryCode, String referral,String city, String expiryDate,String license,String dob);

    void getCity();

    void onCitySelected(ArrayList<Cities> cities);

    void uploadProfileImg(File file);

    void uploadLicenseImg(File file);

    void uploadLicenseBackImg(File file);

    void profileOnclick();

    void licenseOnclik();

    void openCalender(int type);
}
