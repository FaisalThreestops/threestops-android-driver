package com.delivx.utility;

import android.net.Uri;
import android.os.Build;

import com.driver.delivx.BuildConfig;

import java.io.File;

/**
 * Created by embed on 17/6/16.
 */
public class VariableConstant {

    public static final String PARENT_FOLDER = "Delivx";
    public static final String PREF_NAME = "DelivxPartner";
    public static final int RC_READ_WRITE_CAMERA_STATE = 102;
    public static final String APP_VERSION = BuildConfig.VERSION_NAME;
    public static final String DEVICE_MODEL = android.os.Build.MODEL;
    public static final String DEVICE_MAKER = android.os.Build.MANUFACTURER;
    public static final String DEVICE_VERSION = Build.VERSION.RELEASE;
    public static final int DEVICE_TYPE = 2; //1 for ios 2 for android
    public static final int USER_TYPE = 2; //1 for customer 2 for driver
    //permission constants
    public static final int RC_READ_PHONE_STATE = 101;
    public static final int RC_LOCATION_STATE = 102;

    public static final String MQTT_HOST = "159.203.182.173";
    public static final String MQTT_PORT = "2052";
    public static final String MQTT_USERNAME =  "DelivXVernemq";
    public static final String MQTT_PASSWORD =  "dZuxnpHxXD86Sz8g";

    public static final String COGNITO_POOL_ID = "ap-south-1:23fd5020-068c-4452-997a-d084a640a136";
    public static final String BUCKET_NAME = "grocerufly";
    public static final String PROFILE_PIC = "driver/ProfilePics/";
    public static final String LICENCE = "driver/DriverLincence/";
    public static final String VEHICLE = "Vehicles/VehicleImage/";
    public static final String VEHICLE_DOCUMENTS = "Vehicles/VehicleDocuments/";
    public static final String SIGNATURE_UPLOAD = "driver/signature/";
    public static final String BANK_PROOF = "driver/BankProof/";
    public static final String SIGNATURE_PIC_DIR = PARENT_FOLDER + "/sign";
    public static final String AMAZON_BASE_URL = "https://s3.amazonaws.com/";
    public static final String CURRENCY = "";
    public static boolean IS_PROFILE_EDITED = false;
    public static boolean EDIT_PHONE_NUMBER = false;
    public static boolean EDIT_PASSWORD_DIALOG = false;
    public static Uri newProfileImageUri;
    public static File newFile;
    public static boolean isPictureTaken;
    public static boolean isVehPictureTaken;
    public static boolean VECHICLESELECTED = false;
    public static String VEHICLEID = "";
    public static String VEHICLE_TYPE_ID = "";

    public static boolean IS_STRIPE_ADDED = false;
    public static boolean IS_POP_UP_OPEN = false;

    public static boolean isWalletFragActive = false, isWalletUpdateCalled = false;
    public static boolean isPaymentFragActive = false, isToUpdateAlertVisible = false;


    public static boolean isChattingOpen=false;
    public static boolean isChattingToSave=false;
    public static boolean isChattingCalled=false;

    public static boolean APPISBACKGROUND = false;
    public static boolean FORGROUND_LOCK = false;
    public static String MQTT_CHANEL = "";
}
