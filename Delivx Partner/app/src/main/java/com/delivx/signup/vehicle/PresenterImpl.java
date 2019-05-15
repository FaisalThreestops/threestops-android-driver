package com.delivx.signup.vehicle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.VehMakeData;
import com.delivx.pojo.VehTypeData;
import com.delivx.pojo.VehicleMakeModel;
import com.delivx.pojo.VehicleMakePojo;
import com.delivx.pojo.VehicleTypePojo;
import com.delivx.utility.ImageEditUpload;
import com.delivx.utility.MyTextUtils;
import com.delivx.utility.Upload_file_AmazonS3;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.inject.Inject;

import eu.janmuller.android.simplecropimage.CropImage;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import static android.app.Activity.RESULT_OK;

/**
 * Created by DELL on 15-01-2018.
 */

public class PresenterImpl implements SignupPresenterContract {

    private String currentVersion,lastName,firstName,mobile,password,licensePic,profile_pic,referral,countryCode,zone,email,vehicle_pic_url = "",carriageImage = "", registrationImage = "", insuranceImage = "";
    private String TAG=PresenterImpl.class.getSimpleName();
    private static final int SELECT_AN_TYPE = 405;
    private static final int SELECT_AN_Make = 407;
    private static final int SELECT_AN_MODEL = 408;
    private String str_type="",make_id="",model_id="";
    private static final int CAMERA_PIC = 11, GALLERY_PIC = 12, CROP_IMAGE = 13;
    String base64dependentPic = "",takenNewImage,state,fileName;
    private String  vehicle_pic = "vehicle_pic", licence_pic = "add_licence", carriage_permit = "carriage_permit", reg_certificate = "reg_certificate";

    @Inject
    Activity context;
//    @Inject
    SignupVehicleView view;
    @Inject
    NetworkService networkService;
    @Inject
    Upload_file_AmazonS3 amazonS3;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    private ArrayList<VehTypeData> vehicleTypeData;
    private ArrayList<VehMakeData> vehMakeData;
    private ArrayList<VehicleMakeModel> vehicleMakeModels;


    @Inject
    public PresenterImpl() {
    }

    @Override
    public void attachView(SignupVehicleView view){
        this.view=view;
    }

    @Override
    public void getVehicleMakeList() {
        if (vehMakeData != null && vehMakeData.size() > 0)
            view.moveToVehicleMakeList(vehMakeData);
        else
            makeApi();
    }

    @Override
    public void uploadImage(String imageType) {
        ImageEditUpload imageEditUpload = new ImageEditUpload(context, imageType);
    }

    @Override
    public void setActionBar()
    {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        view.setTitle();
    }

    @Override
    public void getBundleData(Bundle bundle) {
        if(bundle!=null){
            lastName=bundle.getString("lastName");
            countryCode=bundle.getString("countryCode");
            referral=bundle.getString("referral");
            firstName=bundle.getString("firstName");
            email=bundle.getString("email");
            password=bundle.getString("password");
            mobile=bundle.getString("mobile");
            zone=bundle.getString("zone");
            profile_pic=bundle.getString("profilePic");
            licensePic=bundle.getString("licensePic");
        }
    }

    @Override
    public void onOutSideTouch() {
        Utility.hideSoftKeyboard(context);
    }

    @Override
    public void getTypeList() {
        if (vehicleTypeData != null && vehicleTypeData.size() > 0)
            view.moveToVehicleList(vehicleTypeData);
        else
            typeApi();
    }

    public void typeApi(){
        view.showProgress();

        Observable<Response<ResponseBody>> vehicleType=networkService.vehicleType(
                preferenceHelperDataSource.getLanguage());
        vehicleType.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200)
                            {
                                jsonObject=new JSONObject(value.body().string());
                                Gson gson=new Gson();
                                VehicleTypePojo vehicleTypePojo=gson.fromJson(jsonObject.toString(),VehicleTypePojo.class);
                                vehicleTypeData=vehicleTypePojo.getData();

                                view.moveToVehicleList(vehicleTypeData);

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                            }

                            Utility.printLog("vehicleType : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("vehicleType : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }


    public void makeApi(){
        view.showProgress();

        Observable<Response<ResponseBody>> vehicleMake=networkService.vehicleMake(
                preferenceHelperDataSource.getLanguage());
        vehicleMake.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200)
                            {
                                jsonObject=new JSONObject(value.body().string());
                                Gson gson=new Gson();
                                VehicleMakePojo  vehicleMakePojo=gson.fromJson(jsonObject.toString(),VehicleMakePojo.class);
                                vehMakeData=vehicleMakePojo.getData();

                                view.moveToVehicleMakeList(vehMakeData);

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                            }

                            Utility.printLog("vehicleMake : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("vehicleMake : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }

    @Override
    public void uploadImageToAmazon(File file) {
        String BUCKETSUBFOLDER = "";

        /*if (ImageType.equals(carriage_permit)) {
            pDialog.show();
        }*/

        switch (SignupVehicle.ImageType){
            case "vehicle_pic" :
                vehicle_pic_url="UPLOADING";
                BUCKETSUBFOLDER = VariableConstant.VEHICLE;
                break;

            case "add_licence" :
                insuranceImage = "UPLOADING";
                BUCKETSUBFOLDER = VariableConstant.VEHICLE_DOCUMENTS;
                break;

            case "reg_certificate" :
                registrationImage = "UPLOADING";
                BUCKETSUBFOLDER = VariableConstant.VEHICLE_DOCUMENTS;
                break;

            case "carriage_permit" :
                carriageImage = "UPLOADING";
                BUCKETSUBFOLDER = VariableConstant.VEHICLE_DOCUMENTS;
                break;
        }

        amazonS3.Upload_data(SignupVehicle.ImageType,VariableConstant.BUCKET_NAME, BUCKETSUBFOLDER + "/" + file.getName(), file, new Upload_file_AmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String sucess) {

            }

            @Override
            public void sucess(String url, String type) {
//                pDialog.dismiss();

                Log.d(TAG, "URL : " + url);
                switch (type){
                    case "vehicle_pic" :
                        vehicle_pic_url = url;
                        Utility.printLog(TAG+" type "+type);
                        break;

                    case "add_licence" :
                        insuranceImage = url;
                        Utility.printLog(TAG+" type "+type);
                        break;

                    case "reg_certificate" :
                        registrationImage = url;
                        Utility.printLog(TAG+" type "+type);
                        break;

                    case "carriage_permit" :
                        carriageImage = url;
                        Utility.printLog(TAG+" type "+type);
                        break;
                }

            }

            @Override
            public void error(String type) {
//                pDialog.dismiss();
                switch (type){
                    case "vehicle_pic" :
                        view.uploadVehicleError();
                        vehicle_pic_url="";
                        break;

                    case "add_licence" :
                        view.uploadInsuranceError();
                        insuranceImage="";
                        break;

                    case "reg_certificate" :
                        view.uploadRegistrationError();
                        registrationImage="";
                        break;

                    case "carriage_permit" :
                        view.uploadCarriageError();
                        carriageImage="";
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_AN_TYPE && data != null) {
            view.setTypeText("");
            str_type="";
            if (vehicleTypeData != null) {
                vehicleTypeData.clear();
            }
            vehicleTypeData = (ArrayList<VehTypeData>) data.getSerializableExtra("DATA");
            if(vehicleTypeData.size()>0){
                for (VehTypeData v : vehicleTypeData) {
                    if (v.isSelected()) {
                        view.setTypeText(v.getType_name());
                        str_type = v.get_id();
                    }

                }
            }else {
                str_type = "";
            }

        }
        if (requestCode == SELECT_AN_Make && data != null) {
            if (vehMakeData != null) {
                vehMakeData.clear();
            }
            view.setMakeText("");
            vehMakeData = (ArrayList<VehMakeData>) data.getSerializableExtra("DATA");
            if(vehMakeData.size()>0){
                for (VehMakeData v : vehMakeData) {
                    if (v.isSelected()) {
                        view.setMakeText(v.getName());
                        make_id = v.getId();
                        if (vehicleMakeModels != null) {
                            vehicleMakeModels.clear();
                            vehicleMakeModels.addAll(v.getModels());
                        } else {
                            vehicleMakeModels = v.getModels();
                        }
                    }
                }
            }else {
                make_id = "";
                model_id = "";
            }

            view.setModelText("");
        }
        if (requestCode == SELECT_AN_MODEL && data != null) {
            view.setModelText("");
            vehicleMakeModels = (ArrayList<VehicleMakeModel>) data.getSerializableExtra("DATA");

            if(vehicleMakeModels.size()>0){
                for (VehicleMakeModel v : vehicleMakeModels) {
                    if (v.isSelected()) {
                        view.setModelText(v.getName());
                        model_id = v.getId();
                    }
                }
            }else {
                model_id = "";
            }


        }
        if (resultCode != RESULT_OK) {
            return;
        } else if (requestCode != -1) {
            switch (requestCode) {
                case CAMERA_PIC:
//                    fileType = "image";
                    view.startCropImage();
                    break;

                case GALLERY_PIC:
                    try {
                        Utility.printLog("RegistrationAct in GALLERY_PIC:");
                        takenNewImage = "";
                        state = Environment.getExternalStorageState();
                        takenNewImage = VariableConstant.PARENT_FOLDER + String.valueOf(System.nanoTime()) + ".png";

                        if (Environment.MEDIA_MOUNTED.equals(state)) {
                            VariableConstant.newFile = new File(Environment.getExternalStorageDirectory() /*+ "/" + VariableConstant.PARENT_FOLDER*/ /*+ "/Media/Images/CropImages/"*/, takenNewImage);
                        } else {
                            VariableConstant.newFile = new File(context.getFilesDir()/* + "/" + VariableConstant.PARENT_FOLDER*/ /*+ "/Media/Images/CropImages/"*/, takenNewImage);
                        }

                        InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(VariableConstant.newFile);

                        Utility.copyStream(inputStream, fileOutputStream);

                        fileOutputStream.close();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);
                        Utility.printLog("RegistrationAct in GALLERY_PIC fileOutputStream: " + fileOutputStream);
                        view.startCropImage();
                    } catch (Exception e) {
                        Utility.printLog("RegistrationAct in GALLERY_PIC Error while creating newfile:" + e);
                    }
                    break;

                case CROP_IMAGE:
                    fileName = "";
                    String path = data.getStringExtra(CropImage.IMAGE_PATH);
                    if (path == null) {
                        Utility.printLog("RegistrationAct CROP_IMAGE file path is null: " + VariableConstant.newFile.getPath());

                        return;
                    } else {
                        if (SignupVehicle.ImageType.equals(vehicle_pic)) {
                            VariableConstant.isVehPictureTaken = true;
                        }
                        Utility.printLog("RegistrationAct CROP_IMAGE FilePAth : " + VariableConstant.newFile.getPath());
                        VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);
                        Utility.printLog("RegistrationAct CROP_IMAGE file URi: " + VariableConstant.newProfileImageUri);

                        fileName = VariableConstant.newFile.getName();
                        Utility.printLog("RegistrationAct CROP_IMAGE fileName: " + fileName);

                        try {
                            String[] type = fileName.split("\\.");
                            Log.d("file ", "File Type: " + type[1]);

                            byte[] bytes = new byte[(int) VariableConstant.newFile.length()];
                            InputStream inputStream = context.getContentResolver().openInputStream(VariableConstant.newProfileImageUri);
                            if (inputStream != null) {
                                inputStream.read(bytes);
                            }
                            byte[] encoded = Base64.encode(bytes, Base64.NO_WRAP);
                            base64dependentPic = new String(encoded);
                            base64dependentPic = "data:image/png;base64," + base64dependentPic;


                            if (SignupVehicle.ImageType.equals(vehicle_pic)) {
                                Bitmap bMap = BitmapFactory.decodeFile(path);
                                Bitmap circle_bMap = Utility.getCircleCroppedBitmap(bMap);
                                VariableConstant.isVehPictureTaken = true;
                                view.setVehicleImage(circle_bMap);
                                uploadImageToAmazon(new File(path));
                            }
                            else if ( SignupVehicle.ImageType.equals(reg_certificate) ) {
                                Bitmap bMap = BitmapFactory.decodeFile(path);
                                view.setRegistrationImage(bMap);
                                uploadImageToAmazon(new File(path));
                            }
                            else if (SignupVehicle.ImageType.equals(licence_pic)) {
                                Bitmap bMap = BitmapFactory.decodeFile(path);
                                view.setInsuranceImage(bMap);
                                uploadImageToAmazon(new File(path));
                            }
                            else if (SignupVehicle.ImageType.equals(carriage_permit)) {
                                Bitmap bMap = BitmapFactory.decodeFile(path);
                                view.setcarriagePermitImage(bMap);
                                uploadImageToAmazon(new File(path));
                            }
                        } catch (Exception e) {
                            Utility.printLog("RegistrationAct in CROP_IMAGE exception while copying file = " + e.toString());
                        }
                    }
                    break;

                default:

                    //Toast.makeText(this, getResources().getString(R.string.oops)+" "+getResources().getString(R.string.smthWentWrong), Toast.LENGTH_LONG).show();
                    break;
            }
        }

    }

    @Override
    public void getModelList() {
        if(!make_id.isEmpty())
            view.moveToVehicleModelList(vehicleMakeModels);
        else
            Utility.mShowMessage(context.getResources().getString(R.string.message), context.getResources().getString(R.string.err_make), context);
    }

    @Override
    public void validateFields(String plateNo,String type,String make,String model)
    {
        if(MyTextUtils.isEmpty(plateNo)){
            Utility.mShowMessage(context.getResources().getString(R.string.message), context.getResources().getString(R.string.err_plate), context);
        }
        else if (str_type.matches("")|| MyTextUtils.isEmpty(type)) {
            Utility.mShowMessage(context.getResources().getString(R.string.message), context.getResources().getString(R.string.err_type), context);
        }
        else if (make_id.matches("")||MyTextUtils.isEmpty(make)) {
            Utility.mShowMessage(context.getResources().getString(R.string.message), context.getResources().getString(R.string.err_make), context);
        }
        else if (model_id.matches("")||MyTextUtils.isEmpty(model)) {
            Utility.mShowMessage(context.getResources().getString(R.string.message), context.getResources().getString(R.string.err_model), context);
        }
        else if (vehicle_pic_url.matches("")) {
            Utility.mShowMessage(context.getResources().getString(R.string.message), context.getResources().getString(R.string.err_veh_img), context,
                    new Utility.AlertDialogCallBack() {
                        @Override
                        public void onOkPressed() {
                            uploadImage(SignupVehicle.ImageType=vehicle_pic);
                    }

                        @Override
                        public void onCancelPressed() {

                        }
                    });
        }
        else if (registrationImage.equals("")) {
            Utility.mShowMessage(context.getResources().getString(R.string.message), context.getResources().getString(R.string.reg_certficate), context,
                    new Utility.AlertDialogCallBack() {
                        @Override
                        public void onOkPressed() {
                            uploadImage(SignupVehicle.ImageType=reg_certificate);
                        }

                        @Override
                        public void onCancelPressed() {

                        }
                    });
        }
        else if (insuranceImage.equals("")) {
            Utility.mShowMessage(context.getResources().getString(R.string.message), context.getResources().getString(R.string.Insurance_miss), context,
                    new Utility.AlertDialogCallBack() {
                        @Override
                        public void onOkPressed() {
                            uploadImage(SignupVehicle.ImageType=licence_pic);
                        }

                        @Override
                        public void onCancelPressed() {

                        }
                    });
        }
        else if (carriageImage.equals(""))
        {
            Utility.mShowMessage(context.getResources().getString(R.string.message), context.getResources().getString(R.string.certificate_miss), context,
                    new Utility.AlertDialogCallBack() {
                        @Override
                        public void onOkPressed() {
                            uploadImage(SignupVehicle.ImageType=carriage_permit);
                        }

                        @Override
                        public void onCancelPressed() {

                        }
                    });

        }
        else if(carriageImage.equals("UPLOADING")||insuranceImage.equals("UPLOADING")||registrationImage.equals("UPLOADING")||vehicle_pic_url.equals("UPLOADING")){
            Utility.BlueToast(context,"Uploading documents please wait...");
        }
        else {
            signUpApi( plateNo);
        }
    }

    public void signUpApi(String plateNo)
    {
        view.showProgress();
        double[] latlong = Utility.getLocation(context);
        try {
            currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        final JSONObject jsonObjectSignUp=new JSONObject();
        try {
            jsonObjectSignUp.put("mobile",mobile);
            jsonObjectSignUp.put("countryCode",countryCode);
            jsonObjectSignUp.put("firstName",firstName);
            jsonObjectSignUp.put("lastName",lastName);
            jsonObjectSignUp.put("email",email);
            jsonObjectSignUp.put("password",password);
            jsonObjectSignUp.put("zipCode","1234");
            jsonObjectSignUp.put("latitude",latlong[0]);
            jsonObjectSignUp.put("longitude",latlong[1]);
            jsonObjectSignUp.put("profilePic",profile_pic);
            jsonObjectSignUp.put("plateNo",plateNo);
            jsonObjectSignUp.put("vehicleImage",vehicle_pic_url);
            jsonObjectSignUp.put("type",str_type);
            jsonObjectSignUp.put("specialities","");
            jsonObjectSignUp.put("vehicleMake",make_id);
            jsonObjectSignUp.put("vehicleModel",model_id);
            jsonObjectSignUp.put("referral",referral);
            jsonObjectSignUp.put("cities",zone);
            jsonObjectSignUp.put("insurancePhoto",insuranceImage);
            jsonObjectSignUp.put("carriagePermit",carriageImage);
            jsonObjectSignUp.put("regCert",registrationImage);
            jsonObjectSignUp.put("operator","");
            jsonObjectSignUp.put("driverLicense",licensePic);
            jsonObjectSignUp.put("accountType",1);
            jsonObjectSignUp.put("deviceType",2);
            jsonObjectSignUp.put("deviceId",preferenceHelperDataSource.getDeviceId());
            jsonObjectSignUp.put("appVersion",currentVersion);
            jsonObjectSignUp.put("deviceMake",Build.MANUFACTURER);
            jsonObjectSignUp.put("deviceModel",Build.MODEL);
            jsonObjectSignUp.put("pushToken",preferenceHelperDataSource.getFCMRegistrationId());
        }catch (JSONException e){
            e.printStackTrace();
        }
        Observable<Response<ResponseBody>> sendOtp=networkService.sendOtp(preferenceHelperDataSource.getLanguage(), mobile, countryCode);
        sendOtp.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200)
                            {
                                jsonObject=new JSONObject(value.body().string());
                                Bundle bundle=new Bundle();
                                bundle.putString("from", "SignUpVahicle");
                                bundle.putString("signupdata", jsonObjectSignUp.toString());
                                bundle.putString("mobile", mobile);
                                bundle.putString("otp","");
                                bundle.putString("countryCode",countryCode);

                                view.moveToVerifyAct(bundle);

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                            }

                            Utility.printLog("sendOtp : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("sendOtp : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }
}
