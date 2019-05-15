package com.delivx.signup.perosonal;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.Cities;
import com.delivx.pojo.Zone;

import java.util.ArrayList;

/**
 * Created by DELL on 08-01-2018.
 */

public interface PersonalView extends BaseView {

    void sendZones(ArrayList<Zone> zones);
    void initActionBar();

    void setTitle();

    void setCounryFlag(int drawableID,String dialCOde,int minLength,int maxLength);

    void setMaxLength(int length);

    void onEmailError(String msg);

    void onPhoneError(String msg);

    void onFirstNameError(String msg);

    void onPasswordError(String password);

    void moveToVehicleDetails(Bundle bundle);

    void moveToZoneList(ArrayList<Cities> cities);

    void setCityTextView(String zone);
    void setZoneTextView(String zone);

    void startCropImage();

    void setProfileImage(Bitmap bitmap);

    void setLicenseImage(Bitmap bitmap);

    void setLicenseBackImage(Bitmap bMap);

    void onError(String error);

    void uploadProfileError();

    void uploadLicenseError();

    String getCountryCode();

    void enableLogin();

    void setExpiryDate(String format);

    void setBirthDate(String format);

    void uploadLicenseBackError();

}
