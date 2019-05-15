package com.delivx.signup.perosonal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.DatePicker;

import com.delivx.pojo.Zone;
import com.delivx.pojo.ZoneModel;
import com.delivx.utility.TextUtil;
import com.google.gson.Gson;
import com.delivx.utility.country_picker.CountryPicker;
import com.delivx.utility.country_picker.CountryPickerListener;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.Cities;
import com.delivx.pojo.SignupZonesPojo;
import com.delivx.pojo.ZoneData;
import com.delivx.utility.ImageEditUpload;
import com.delivx.utility.MyTextUtils;
import com.delivx.utility.Upload_file_AmazonS3;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

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
 * Created by DELL on 08-01-2018.
 */

public class PersonalPresenter implements PersonalPresenterContract,DatePickerDialog.OnDateSetListener
{
    private int minPhoneLength=0,maxPhoneLength=15;
    private String dialCode="",takenNewImage,state,profileUrl="",licenseUrl="",licenseBackUrl="",fileName,base64dependentPic="";
    private boolean isEmailValid=false,isPhoneValid=false;
    private ArrayList<Cities> cities=new ArrayList<>();
    private ArrayList<Zone> zones=new ArrayList<>();
    private JSONArray selectedCity =new JSONArray();
    private JSONArray selectedZone =new JSONArray();
    private ArrayList<Cities> citiesArrayList ;
    private ArrayList<Zone> zoneArrayList ;
    private static final int SELECT_A_CITY = 409;
    private static final int SELECT_A_ZONE = 410;
    private static final int CAMERA_PIC = 11, GALLERY_PIC = 12,CROP_IMAGE = 13;
    private String  profile_pic = "profile_pic", licence_pic = "add_licence",licence_pic_back="license_back";;
    private JSONObject jsonObjectSignUp=new JSONObject();
    private String cityID="",cityName="";


    @Inject
    Activity context;
    @Inject
    PersonalView view;
    @Inject
    NetworkService networkService;
    @Inject
    Upload_file_AmazonS3 amazonS3;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    private String currentVersion;
    private int typeOfDate;


    @Inject
    public PersonalPresenter() {
    }

    public static int getResId(String drawableName) {
        try {
            Class<R.drawable> res = R.drawable.class;
            Field field = res.getField(drawableName);
            int drawableId = field.getInt(null);
            return drawableId;
        } catch (Exception e) {
            Log.e("CountryCodePicker", "Failure to get drawable id.", e);
        }
        return -1;
    }

    @Override
    public void setActionBar() {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        view.setTitle();
    }

    @Override
    public void getCountryCode() {
        String code= Utility.getCounrtyCode(context);

        if(!code.isEmpty()){
            String drawableName = "flag_"
                    + code.toLowerCase(Locale.ENGLISH);
            int id=getResId(drawableName);
            String allCountriesCode = null;
            try {
                allCountriesCode = readEncodedJsonString(context);
                JSONArray countrArray = new JSONArray(allCountriesCode);
                JSONObject jsonObject;

                for(int index=0;index<countrArray.length();index++){
                    jsonObject=countrArray.getJSONObject(index);
                    if(jsonObject.getString("code").equals(code)){
                        dialCode=jsonObject.getString("dial_code");
                       /* maxPhoneLength=jsonObject.getInt("max_digits");
                        minPhoneLength=(!jsonObject.getString("min_digits").isEmpty())?jsonObject.getInt("min_digits"):5;*/
                        break;
                    }
                }

                view.setCounryFlag(id,dialCode,minPhoneLength,maxPhoneLength);
                view.setMaxLength(maxPhoneLength);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showDialogForCountryPicker() {
        final CountryPicker picker = CountryPicker.newInstance(context.getResources().getString(R.string.select_country));
        picker.show(((SignupPersonal)context).getSupportFragmentManager(), "COUNTRY_CODE_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dCode,int minLength,int maxLength) {
                String drawableName = "flag_"
                        + code.toLowerCase(Locale.ENGLISH);
                int resID=getResId(drawableName);

                minPhoneLength=minLength;
                maxPhoneLength=maxLength;

                dialCode=dCode;
                view.setMaxLength(maxPhoneLength);
                view.setCounryFlag(resID,dialCode,minPhoneLength,maxPhoneLength);

                picker.dismiss();
            }
        });
    }

    @Override
    public void validateEmail(String email)
    {
        if (!MyTextUtils.isEmpty(email)) {
            if (MyTextUtils.isEmail(email)) {
                 validaEmailPhone(2,email);
            } else {
                view.onEmailError(context.getResources().getString(R.string.invalidEmail));
            }
        }
        else {
            view.onEmailError(context.getResources().getString(R.string.err_email));
        }
    }

    @Override
    public void validatePhone(String countryCode, String phone) {

      /*  if (!MyTextUtils.isEmpty(phone)) {

            if(dialCode.isEmpty()){
                dialCode=view.getCountryCode();
            }
            if (Utility.phoneNumberLengthValidation(phone,dialCode)) {

//                Utility.printLog(dialCode+phone+" is valid: "+Utility.phoneNumberLengthValidation(phone,dialCode));
                validaEmailPhone(1,phone);
            } else {
                view.onPhoneError(context.getResources().getString(R.string.invalidPhone));
            }
        }
        else {
            view.onPhoneError(context.getResources().getString(R.string.err_phone_no));
        }
*/





        //        signUpPersonalCheck.setPhone(false);


        if(!TextUtil.isEmpty(phone))
        {
            String phoneNumberE164Format = countryCode.concat(phone);
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(phoneNumberE164Format, null);
                boolean isValid = phoneUtil.isValidNumber(phoneNumberProto); // returns true if valid

                if (isValid ) {
                    validaEmailPhone(1,phone);

                } else {
                    view.onPhoneError(context.getResources().getString(R.string.invalidPhone));
                }

            }catch (NumberParseException e){

            }
        }
        else {
            view.onPhoneError(context.getResources().getString(R.string.err_phone_no));
        }
    }

    @Override
    public void validateReferralCode(String code) {

    }

    @Override
    public void onOutSideTouch() {
        Utility.hideSoftKeyboard(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_A_CITY && data != null) {

            citiesArrayList = (ArrayList<Cities>) data.getSerializableExtra("DATA");
            onCitySelected(citiesArrayList);

        }
        else if(requestCode == SELECT_A_ZONE && data != null)
        {
            zoneArrayList = (ArrayList<Zone>) data.getSerializableExtra("DATA");
            onZoneSelected(zoneArrayList);
        }
        else if (resultCode != RESULT_OK) {
            return;
        }
        else if (requestCode != -1)
        {
            switch (requestCode) {
                case CAMERA_PIC:
//                    fileType = "image";
                    view.startCropImage();
                    break;

                case GALLERY_PIC:
                    try {
                        takenNewImage = "";
                        state = Environment.getExternalStorageState();
                        takenNewImage = context.getResources().getString(R.string.app_name).replace(" ","") + String.valueOf(System.nanoTime()) + ".png";

                        if (Environment.MEDIA_MOUNTED.equals(state)) {

                            VariableConstant.newFile = new File(Environment.getExternalStorageDirectory() /*+ "/" + VariableConstant.PARENT_FOLDER + "/Media/Images/CropImages/"*/, takenNewImage);
                        } else {
                            VariableConstant.newFile = new File(context.getFilesDir() /*+ "/" + VariableConstant.PARENT_FOLDER + "/Media/Images/CropImages/"*/, takenNewImage);
                        }

                        InputStream inputStream = context.getContentResolver().openInputStream(
                                data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(VariableConstant.newFile.getAbsolutePath()));

                        Utility.copyStream(inputStream, fileOutputStream);

                        fileOutputStream.close();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);
                        view.startCropImage();
                    } catch (Exception e) {
                        Utility.printLog("RegistrationAct in GALLERY_PIC Error while creating newfile:" + e);
                    }
                    break;

                case CROP_IMAGE:
                    fileName = "";
                    String path = data.getStringExtra(CropImage.IMAGE_PATH);
                    if (path == null)
                    {
                        return;
                    }
                    else
                    {
                        if (SignupPersonal.ImageType.equals(profile_pic)) {
                            VariableConstant.isPictureTaken = true;
                        }
                        VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);

                        fileName = VariableConstant.newFile.getName();
                        Utility.printLog("RegistrationAct CROP_IMAGE fileName: " + fileName);

                        try {
                            String[] type = fileName.split("\\.");

                            byte[] bytes = new byte[(int) VariableConstant.newFile.length()];
                            InputStream inputStream = context.getContentResolver().openInputStream(VariableConstant.newProfileImageUri);
                            if (inputStream != null) {
                                inputStream.read(bytes);
                            }
                            byte[] encoded = Base64.encode(bytes, Base64.NO_WRAP);
                            base64dependentPic = new String(encoded);
                            base64dependentPic = "data:image/png;base64," + base64dependentPic;
                            Bitmap bMap = BitmapFactory.decodeFile(path);

                            if (SignupPersonal.ImageType.equals(profile_pic)) {

                                if (Utility.isNetworkAvailable(context)) {
                                    Bitmap circle_bMap = Utility.getCircleCroppedBitmap(bMap);
                                    view.setProfileImage(circle_bMap);
                                    VariableConstant.isPictureTaken = true;
                                    uploadProfileImg(new File(path));
                                } else {
                                    Utility.BlueToast(context, context.getResources().getString(R.string.no_network));
                                }

                            } else if (SignupPersonal.ImageType.equals(licence_pic)) {

                                if (Utility.isNetworkAvailable(context)) {
                                    view.setLicenseImage(bMap);
                                    uploadLicenseImg(new File(path));
                                } else {
                                    Utility.BlueToast(context, context.getResources().getString(R.string.no_network));
                                }

                            }
                            else if (SignupPersonal.ImageType.equals(licence_pic_back)) {

                                if (Utility.isNetworkAvailable(context)) {
                                    view.setLicenseBackImage(bMap);
                                    uploadLicenseBackImg(new File(path));
                                } else {
                                    Utility.BlueToast(context, context.getResources().getString(R.string.no_network));
                                }

                            }
                        } catch (IOException e) {
                            Utility.printLog("RegistrationAct in CROP_IMAGE exception while copying file = " + e.toString());
                        }
                    }
                    break;

                default:

                    Utility.BlueToast(context, context.getResources().getString(R.string.smthWentWrong));

                    break;
            }
        }
    }

    @Override
    public void validateFields(String fName, String mob, String email, String password,String lastName,String countryCode,String referral,String city,String expiryDate,String license,String dob) {

        if(profileUrl.isEmpty())
        {
            view.onError(context.getResources().getString(R.string.choose_prof_pic));
        }
        else if(MyTextUtils.isEmpty(fName)){
            view.onFirstNameError(context.getResources().getString(R.string.err_fname));
        }
        else if(!isPhoneValid)
        {
            validatePhone(countryCode,mob);
        }
        else if(!isEmailValid)
        {
            validateEmail(email);
        }
        else if(MyTextUtils.isEmpty(password)){
            view.onPasswordError(context. getResources().getString(R.string.password_miss));
        }
        /*else if(!password.matches("^*[a-zA-Z](?=.*\\d).{5,14}$")){
            view.onPasswordError(context.getResources().getString(R.string.pass_validation));
        }*/
        else if(MyTextUtils.isEmpty(city))
        {
            view.onError(context.getResources().getString(R.string.choose_city));
        }
        else if(MyTextUtils.isEmpty(license))
        {
            view.onError(context.getResources().getString(R.string.license_empty));
        }
        else if(MyTextUtils.isEmpty(dob))
        {
            view.onError(context.getResources().getString(R.string.dob_empty));
        }
        else if(MyTextUtils.isEmpty(expiryDate))
        {
            view.onError(context.getResources().getString(R.string.license_expiry_error));
        }
        else if(licenseUrl.isEmpty())
        {
            view.onError(context.getResources().getString(R.string.choose_licence_img));
        }
        else if(licenseBackUrl.isEmpty())
        {
            view.onError(context.getResources().getString(R.string.choose_licence_img));
        }
        else if(profileUrl.equals("UPLOADING")||licenseUrl.equals("UPLOADING")||licenseBackUrl.equals("UPLOADING")){
            view.onError("Uploading documents please wait...");
        }
        else {

            double[] latlong = Utility.getLocation(context);
            try {
                currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            try {
                jsonObjectSignUp.put("mobile",mob);
                jsonObjectSignUp.put("countryCode",countryCode);
                jsonObjectSignUp.put("firstName",fName);
                jsonObjectSignUp.put("lastName",lastName);
                jsonObjectSignUp.put("email",email);
                jsonObjectSignUp.put("password",password);
                jsonObjectSignUp.put("zipCode","1234");
                jsonObjectSignUp.put("latitude",latlong[0]);
                jsonObjectSignUp.put("longitude",latlong[1]);
                jsonObjectSignUp.put("profilePic",profileUrl);
                jsonObjectSignUp.put("referral",referral);
//                jsonObjectSignUp.put("cities", selectedCity.toString());
                jsonObjectSignUp.put("zones", selectedZone.toString());
                jsonObjectSignUp.put("driverLicense",licenseUrl+","+licenseBackUrl);
                jsonObjectSignUp.put("accountType",1);
                jsonObjectSignUp.put("deviceType",2);
                jsonObjectSignUp.put("deviceId",preferenceHelperDataSource.getDeviceId());
                jsonObjectSignUp.put("appVersion",currentVersion);
                jsonObjectSignUp.put("deviceMake", Build.MANUFACTURER);
                jsonObjectSignUp.put("deviceModel",Build.MODEL);
                jsonObjectSignUp.put("pushToken",preferenceHelperDataSource.getFCMRegistrationId());
                jsonObjectSignUp.put("driverLicenseNumber",license);
                jsonObjectSignUp.put("cityId",cityID);
                jsonObjectSignUp.put("cityName",cityName);
                jsonObjectSignUp.put("driverLicenseExpiry",expiryDate);
                jsonObjectSignUp.put("dateOfBirth",dob);
            }catch (JSONException e){
                e.printStackTrace();
            }

            signUpApi(mob,countryCode);


//            view.moveToVehicleDetails(bundle);
        }

    }

    @Override
    public void getCity() {
        if(cities.size()>0){
            view.moveToZoneList(cities);
        }
        else
            getCitiesApi();
    }

    @Override
    public void onCitySelected(ArrayList<Cities> cities) {
        this.cities.clear();
        this.cities.addAll(cities);

        selectedCity = new JSONArray();

        String zoneNames = "";
            for (Cities s : cities) {
                if (s.isSelected()) {
                    if (zoneNames.equals("")) {
                        zoneNames = s.getCityName();
                    } else {
                        zoneNames = zoneNames + "," + s.getCityName();
                    }
                    cityID=s.getCityId();
                    cityName=s.getCityName();
                    selectedCity.put(s.getCityId());
                }
            }
        view.setCityTextView(zoneNames);
    }

    public void onZoneSelected(ArrayList<Zone> zone) {
        this.zones.clear();
        this.zones.addAll(zone);

//        selectedZone = new JSONArray();

        String zoneNames = "";
        for (Zone s : zones) {
            if (s.isSelected()) {
                if (zoneNames.equals("")) {
                    zoneNames = s.getTitle();
                } else {
                    zoneNames = zoneNames + "," + s.getCity();
                }
                cityID=s.getCity_ID();
                cityName=s.getCity();
                selectedZone.put(s.get_id());
            }
        }

        Utility.printLog("SelectedZone"+selectedZone.toString());
        view.setZoneTextView(zoneNames);
    }

    @Override
    public void uploadProfileImg(File file) {
        uploadImageToAmazon(file,"PROFILE");
    }

    @Override
    public void uploadLicenseImg(File file) {
        uploadImageToAmazon(file,"LICENSE");
    }
    @Override
    public void uploadLicenseBackImg(File file) {
        uploadImageToAmazon(file,"LICENSE_BACK");
    }

    @Override
    public void profileOnclick() {
        new ImageEditUpload(context, profile_pic);
    }

    @Override
    public void licenseOnclik() {
         new ImageEditUpload(context, licence_pic);
    }

    @Override
    public void openCalender(int type) {
            Calendar calendar=Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int date = calendar.get(Calendar.DAY_OF_MONTH);


            typeOfDate=type;
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context, (DatePickerDialog.OnDateSetListener) this, year, month, date);
            if(type==1){
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-10000);
            }
            else{
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
//                Utility.printLog("PK minAgeDate "+minAdultAge.getTime()+" , "+minAdultAge.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(minAdultAge.getTimeInMillis());
            }
            datePickerDialog.show();
    }

    private static String readEncodedJsonString(Context context) throws java.io.IOException {
            String base64 = context.getResources().getString(R.string.countries_code);
            byte[] data = Base64.decode(base64, Base64.DEFAULT);
            return new String(data, "UTF-8");
    }


    private void validaEmailPhone(final int type, String emailOrPhone)
    {
        view.showProgress();
        Utility.printLog("emailPhoneValidateApi req : "+emailOrPhone);

        Observable<Response<ResponseBody>> emailPhoneValidateApi=networkService.emailPhoneValidate(preferenceHelperDataSource.getLanguage(),dialCode,emailOrPhone,type,emailOrPhone);
        emailPhoneValidateApi.observeOn(AndroidSchedulers.mainThread())
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
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                if(type==1)
                                    isPhoneValid=true;
                                else
                                    isEmailValid=true;


                                view.enableLogin();

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                if(type==1){
                                    view.onPhoneError(jsonObject.getString("message"));
                                    isPhoneValid=false;
                                }
                                else{
                                    view.onEmailError(jsonObject.getString("message"));
                                    isEmailValid=false;
                                }

                            }

                            Utility.printLog("emailPhoneValidateApi : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("emailPhoneValidateApi : Catch :"+e.getMessage());
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

    private void getCitiesApi(){
        view.showProgress();

        Observable<Response<ResponseBody>> cities=networkService.cities(preferenceHelperDataSource.getLanguage());
        cities.observeOn(AndroidSchedulers.mainThread())
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
                                SignupZonesPojo signupZonesPojo=gson.fromJson(jsonObject.toString(),SignupZonesPojo.class);
                                setCities(signupZonesPojo.getData());

                                view.moveToZoneList(PersonalPresenter.this.cities);

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                            }

                            Utility.printLog("zonesApi : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("zonesApi : Catch :"+e.getMessage());
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

    private void setCities(ArrayList<ZoneData> zoneData){
        cities.clear();
        for(ZoneData data:zoneData){
            cities.addAll(data.getCities());
        }
    }

    public String getCityId()
    {
        return cityID;
    }
    private void uploadImageToAmazon(File file, final String type){
        String BUCKETSUBFOLDER = "";

        if (type.equals("PROFILE"))
        {
            BUCKETSUBFOLDER = VariableConstant.PROFILE_PIC;
            profileUrl="UPLOADING";


        } else if (type.equals("LICENSE"))
        {
            BUCKETSUBFOLDER = VariableConstant.LICENCE;
            licenseUrl="UPLOADING";
        }
        else if (type.equals("LICENSE_BACK"))
        {
            BUCKETSUBFOLDER = VariableConstant.LICENCE;
            licenseBackUrl="UPLOADING";
        }
//        view.showProgress();

        final String imageUrl = VariableConstant.AMAZON_BASE_URL + VariableConstant.BUCKET_NAME + BUCKETSUBFOLDER + file.getName().trim();
        Log.d("", "amzonUpload: " + imageUrl);
        Log.d("", "amzonUpload: " + BUCKETSUBFOLDER + file.getName());

        amazonS3.Upload_data(type,VariableConstant.BUCKET_NAME, BUCKETSUBFOLDER + file.getName().trim(), file, new Upload_file_AmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String url) {
//                view.hideProgress();
                if (type.equals("PROFILE")) {
                    profileUrl = url;

                    Log.d("Profile_pic_url", profileUrl);

                } else if (type.equals("LICENSE")) {
                    licenseUrl = url;
                    Log.d("license_image", licenseUrl);

                } else if (type.equals("LICENSE_BACK")) {
                    licenseBackUrl = url;
                    Log.d("license_back_image", licenseUrl);

                }

            }

            @Override
            public void sucess(String url, String type) {
//                view.hideProgress();

                if (type.equals("PROFILE")) {
                    profileUrl = url;

                    Log.d("Profile_pic_url", profileUrl);

                } else if (type.equals("LICENSE")) {
                    licenseUrl = url;
                    Log.d("license_image", licenseUrl);

                }
                else if (type.equals("LICENSE_BACK")) {
                    licenseBackUrl = url;
                    Log.d("license_back_image", licenseBackUrl);

                }
            }

            @Override
            public void error(String errormsg) {
//                view.hideProgress();
                if (type.equals("PROFILE")) {
                    profileUrl = "";
                    view.uploadProfileError();

                } else if (type.equals("LICENSE")) {
                    licenseUrl = "";
                    view.uploadLicenseError();

                }else if (type.equals("LICENSE_BACK")) {
                    licenseBackUrl = "";
                    view.uploadLicenseBackError();

                }
            }
        });
    }

    private void signUpApi(final String mob, final String countryCode)
    {
        view.showProgress();

        Observable<Response<ResponseBody>> sendOtp=networkService.sendOtp(preferenceHelperDataSource.getLanguage(), mob, countryCode);
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
                            Utility.printLog("REsponseCode"+value.code());
                            if(value.code()==200)
                            {
                                Utility.printLog("REsponseCode"+value.code());
                                jsonObject=new JSONObject(value.body().string());
                                Bundle bundle=new Bundle();
                                bundle.putString("from", "SignUpVahicle");
                                bundle.putString("signupdata", jsonObjectSignUp.toString());
                                bundle.putString("mobile", mob);
                                bundle.putString("otp","");
                                bundle.putString("countryCode",countryCode);

//                                view.moveToVerifyAct(bundle);
                                view.moveToVehicleDetails(bundle);

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onError(jsonObject.getString("message"));
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


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

            if(typeOfDate==1)
                view.setExpiryDate(String.format("%d/%d/%d",year ,month + 1,dayOfMonth ));
            else
                view.setBirthDate(String.format("%d/%d/%d",year ,month + 1,dayOfMonth ));
    }



    public void getZonesOnCity(String cityID)
    {
        view.showProgress();

        Observable<Response<ResponseBody>> sendOtp=networkService.getZones(preferenceHelperDataSource.getLanguage(), cityID);
        sendOtp.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        Utility.printLog("ZoneResponse"+value.code());
                        JSONObject jsonObject;
                        if(value.code()==200) {
                            try {
                                jsonObject=new JSONObject(value.body().string());
                                Gson gson=new Gson();
                                ZoneModel zoneModel=gson.fromJson(jsonObject.toString(),ZoneModel.class);
//                                setCities(signupZonesPojo.getData());
                                view.sendZones(zoneModel.getData());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(view!=null)
                            view.hideProgress();



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
