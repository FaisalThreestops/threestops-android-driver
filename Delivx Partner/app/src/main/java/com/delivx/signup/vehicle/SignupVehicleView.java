package com.delivx.signup.vehicle;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.VehMakeData;
import com.delivx.pojo.VehTypeData;
import com.delivx.pojo.VehicleMakeModel;

import java.util.ArrayList;

/**
 * Created by DELL on 15-01-2018.
 */

public interface SignupVehicleView extends BaseView
{
    void initActionBar();

    void setTitle();

    void moveToVehicleList(ArrayList<VehTypeData> data);

    void moveToVehicleMakeList(ArrayList<VehMakeData>data);

    void moveToVehicleModelList(ArrayList<VehicleMakeModel>data);

    void uploadVehicleError();

    void uploadCarriageError();

    void uploadRegistrationError();

    void uploadInsuranceError();

    void setTypeText(String type);

    void setMakeText(String make);

    void setModelText(String model);

    void setVehicleImage(Bitmap bitmap);

    void setRegistrationImage(Bitmap bitmap);

    void setcarriagePermitImage(Bitmap bitmap);

    void setInsuranceImage(Bitmap bitmap);

    void startCropImage();

    void moveToVerifyAct(Bundle bundle);

}
