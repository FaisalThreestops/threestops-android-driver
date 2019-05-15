package com.delivx.signup.vehicle;

import android.content.Intent;
import android.os.Bundle;

import java.io.File;

/**
 * Created by DELL on 15-01-2018.
 */

public interface SignupPresenterContract
{
    void setActionBar();

    void setActionBarTitle();

    void getBundleData(Bundle bundle);

    void onOutSideTouch();

    void getTypeList();

    void attachView(SignupVehicleView view);

    void getVehicleMakeList();

    void uploadImage(String imageType);

    void uploadImageToAmazon(File file);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void getModelList();

    void validateFields(String plateNo,String type,String make,String model);
}
