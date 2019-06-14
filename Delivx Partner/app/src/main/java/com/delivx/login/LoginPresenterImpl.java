package com.delivx.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.delivx.login.language.LanguagesList;
import com.delivx.login.language.LanguagesPojo;
import com.delivx.managers.mqtt.MQTTManager;
import com.delivx.networking.LanguageApiService;
import com.google.gson.Gson;
import com.delivx.app.MyApplication;
import com.delivx.utility.country_picker.CountryPicker;
import com.delivx.utility.country_picker.CountryPickerListener;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.SigninData;
import com.delivx.pojo.SinginResponsePojo;
import com.delivx.utility.MyTextUtils;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.delivx.utility.VariableConstant.LOGIN;

public class LoginPresenterImpl implements LoginPresenter
{

    @Inject Activity context;
    @Inject NetworkService networkService;
    @Inject LoginView loginView;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject MQTTManager mqttManager;
    @Inject LanguageApiService languageApiService;

    private int minPhoneLength=0,maxPhoneLength=15;
    private boolean isEmailOptionSelected=true;
    private String countryCode="+31";
    private ArrayList<LanguagesList> languagesLists;

    @Inject
    public LoginPresenterImpl() {
        languagesLists = new ArrayList<>();
    }

    @Override
    public void validateCredentials(String phone, String username, String password) {

        //if username is empty and choose the email Adrress to login
        if (MyTextUtils.isEmpty(username) && isEmailOptionSelected) {
            onUsernameError(context.getString(R.string.err_email));
            return;
        }
        //choose the email adrress to login and if email is not in format
        else if (!MyTextUtils.isEmail(username) && isEmailOptionSelected  )
        {
            onUsernameError(context.getString(R.string.invalidEmail));
            return;
        }
        //choose the phone number to login and phone number is empty
        else if(!isEmailOptionSelected && MyTextUtils.isEmpty(phone))
        {
            loginView.showError(context.getString(R.string.phone_mis));
            return;
        }
        //choose the phone number to login and if checking the phone number length
        else if( !Utility.phoneNumberLengthValidation(phone,countryCode) && !isEmailOptionSelected )
        {
            loginView.showError(context.getString(R.string.invalidPhone));
            return;
        }
        //if password is not entered
        else if (MyTextUtils.isEmpty(password)) {
            onPasswordError(context.getString(R.string.password_miss));
            return;
        }
        //checking the authorize username and password
        else {
            //checking by email and password
            if(isEmailOptionSelected)
                signIn(username,password);
            //checking by phone number and password
            else
                signIn(phone,password);
        }

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
                if(allCountriesCode!=null){
                    JSONArray countrArray = new JSONArray(allCountriesCode);
                    JSONObject jsonObject;

                    String dialCode="+91";
                    for(int index = 0; index<countrArray.length(); index++){
                        jsonObject=countrArray.getJSONObject(index);
                        if(jsonObject.getString("code").equals(code)){
                            dialCode=jsonObject.getString("dial_code");
//                            maxPhoneLength=jsonObject.getInt("max_digits");
//                            minPhoneLength=(!jsonObject.getString("min_digits").isEmpty())?jsonObject.getInt("min_digits"):5;
                            break;
                        }
                    }

                    loginView.setMaxLength(maxPhoneLength);
                    loginView.setCounryCode(id,dialCode,minPhoneLength,maxPhoneLength);
                    countryCode=dialCode;
                }else {
                    loginView.setMaxLength(10);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static String readEncodedJsonString(Context context) throws java.io.IOException {

        try{
            String base64 = context.getResources().getString(R.string.countries_code);
            byte[] data = Base64.decode(base64, Base64.DEFAULT);
            return new String(data, "UTF-8");
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();

        }
        return null;
    }
    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void getLoginCreds() {
        String name = Utility.getPreference(context, "USER_EMAIL");
        String pass = Utility.getPreference(context, "USER_PASSWORD");
        loginView.setLoginCreds(name,pass);
    }

    @Override
    public void forgotpassOnclick() {
        loginView.startForgotPassAct();
    }

    @Override
    public void signUpOnclick() {
        loginView.startSignUpAct();
    }

    @Override
    public void getBundleData(Intent intent) {
        if(preferenceHelperDataSource.getLanguageSettings()!=null && preferenceHelperDataSource.getLanguageSettings().getLanguageName()!=null)
        loginView.setLanguage(preferenceHelperDataSource.getLanguageSettings().getLanguageName(),false);
        String msg=intent.getStringExtra("success_msg");

        if(msg!=null){
            Utility.mShowMessage(context.getResources().getString(R.string.message),msg,context);
        }
    }

    @Override
    public void onOutSideTouch() {
        Utility.hideSoftKeyboard(context);
    }

    @Override
    public void choosePhoneLogin() {
        isEmailOptionSelected=false;
        loginView.onPhoneLoginSelected();

    }

    @Override
    public void chooseEmailLogin() {
        isEmailOptionSelected=true;
        loginView.onEmailLoginSelected();
    }

    //selecting the country code in the Dialog
    @Override
    public void showDialogForCountryPicker() {
        final CountryPicker picker = CountryPicker.newInstance(context.getResources().getString(R.string.select_country));
        picker.show(((LoginActivity)context).getSupportFragmentManager(), "COUNTRY_CODE_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,int minLength,int maxLength) {
                String drawableName = "flag_"
                        + code.toLowerCase(Locale.ENGLISH);
                int resID=getResId(drawableName);

                minPhoneLength=minLength;
                maxPhoneLength=maxLength;
                countryCode=dialCode;

                loginView.setMaxLength(maxPhoneLength);
                loginView.setCounryCode(resID,dialCode,minPhoneLength,maxPhoneLength);

                picker.dismiss();
            }
        });
    }
    //Getting the RegisterID
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



    public void onUsernameError(String message) {
        if (loginView != null) {
            loginView.setUsernameError(message);
            loginView.hideProgress();
        }
    }


    public void onPasswordError(String message) {
        if (loginView != null) {
            loginView.setPasswordError(message);
            loginView.hideProgress();
        }
    }


    public void onSuccess() {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.navigateToHome();
        }
    }

    /**
     * <h2>onError<h2/>
     * <p>this method have a api error message and this message will
     * show to their and will stop progress dialog</p>
     * @param error will get api error message and show to the user
     */

    public void onError(String error) {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.showError(error);
        }
    }

    //checking for authorized user
    void signIn(String username, final String password){

        //loginView holds the loginActivity class so its not null
        if (loginView != null) {
            loginView.showProgress();
        }

        //api call
        Observable<Response<ResponseBody>> responseObservable=networkService.signIn(preferenceHelperDataSource.getLanguage(),countryCode,username,password,preferenceHelperDataSource.getDeviceId(),preferenceHelperDataSource.getFCMRegistrationId(),
                VariableConstant.APP_VERSION,VariableConstant.DEVICE_MAKER,
                VariableConstant.DEVICE_MODEL,VariableConstant.DEVICE_TYPE,Utility.date(), Build.VERSION.SDK_INT);

        responseObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
        .subscribe(new Observer<Response<ResponseBody>>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ResponseBody> value) {

                if (loginView != null) {
                    loginView.hideProgress();
                }

                try {
                    JSONObject jsonObject;
                    //if the email and password exists successfully
                    if(value.code()==200){
                        jsonObject=new JSONObject(value.body().string());
                        Log.i("value", "onNext: "+value.body());
                        Gson gson=new Gson();
                        SinginResponsePojo signInResponse=gson.fromJson(jsonObject.toString(),SinginResponsePojo.class);
                        preferenceHelperDataSource.setPushTopic(jsonObject.getJSONObject("data").getString("fcmTopics"));
                        setSignInData(signInResponse.getData(),password);
                        Utility.printLog("LoginResponse : "+jsonObject.toString());
                    }else {
                        // if the email and password not exists,, prints the error message
                        jsonObject=new JSONObject(value.errorBody().string());
                        loginView.showError(jsonObject.getString("message"));
                        Utility.printLog("LoginResponse : "+value.errorBody().string());
                        Log.i("value", "onNext: "+value.errorBody());
                    }


                }catch (JSONException e)
                {
                    Utility.printLog("LoginResponse : Catch :"+e.getMessage());
                }
                catch (IOException e)
                {
                    Utility.printLog("LoginResponse : Catch :"+e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                if (loginView != null) {
                    loginView.hideProgress();
                }
                Utility.printLog("LoginResponse : onError :"+e.getMessage());
            }

            @Override
            public void onComplete() {
                if (loginView != null) {
                    loginView.hideProgress();
                }
            }
        });
    }

    public void setSignInData(SigninData data, String password){
        preferenceHelperDataSource.setDriverChannel(data.getChn());
        VariableConstant.MQTT_CHANEL = data.getChn();
        preferenceHelperDataSource.setDriverID(data.getMid());
        preferenceHelperDataSource.setToken(data.getToken());
        preferenceHelperDataSource.setPresenceChannel(data.getPresence_chn());
        preferenceHelperDataSource.setMyName(data.getName());
        preferenceHelperDataSource.setProfilePic(data.getProfilePic());

        preferenceHelperDataSource.setIsLogin(true);
        preferenceHelperDataSource.setPassword(password);
        preferenceHelperDataSource.setPassword(password);
        preferenceHelperDataSource.setMyEmail(data.getEmail());


        Utility.printLog("pushTopics shared pref "+preferenceHelperDataSource.getPushTopic());
        try {
            Utility.subscribeOrUnsubscribeTopics(new JSONArray(preferenceHelperDataSource.getPushTopic()),true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(MyApplication.getInstance().isMQTTConnected()){
            mqttManager.subscribeToTopic(preferenceHelperDataSource.getDriverID());

        }else {
            MyApplication.getInstance().connectMQTT();
        }

        onSuccess();
    }

    @Override
    public void unSubScribeMQTT() {

        try {
            if(VariableConstant.MQTT_CHANEL!=null && mqttManager!=null && mqttManager.isMQTTConnected()){
                mqttManager.unSubscribeToTopic(VariableConstant.MQTT_CHANEL);
                Utility.printLog("testing unsubScribed Mqtt Topic :   "+VariableConstant.MQTT_CHANEL);
                VariableConstant.MQTT_CHANEL = null;

        }}catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void getLanguages() {
        if(Utility.isNetworkAvailable(context)) {
            loginView.showProgress();

            Observable<Response<ResponseBody>> getLanguages = languageApiService.getLanguage();

            getLanguages.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Response<ResponseBody>>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            loginView.hideProgress();

                            try {
                                switch (value.code()) {
                                    case VariableConstant.RESPONSE_CODE_SUCCESS:
                                        String res = value.body().string();
                                        Utility.printLog("language res : "+res);

                                        Gson gson = new Gson();
                                        LanguagesPojo languagesListModel = gson.fromJson(res,LanguagesPojo.class);
                                        Utility.printLog("language res : "+languagesListModel.getData().size());
                                        languagesLists.clear();
                                        languagesLists.addAll(languagesListModel.getData());
                                        boolean isLanguage = false;
                                        for(LanguagesList languagesList : languagesLists)
                                        {
                                            if(preferenceHelperDataSource.getLanguageSettings().getLanguageCode().equals(languagesList.getLanguageCode()))
                                            {
                                                isLanguage = true;
                                                loginView.setLanguageDialog( languagesListModel.getData(),languagesLists.indexOf(languagesList));
                                                break;
                                            }
                                        }
                                        if(!isLanguage)
                                            loginView.setLanguageDialog(null,-1);

                                        break;

                                    default:
                                        /*loginView.showError();*/
                                        break;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Utility.printLog("getLanguages : Catch :" + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            loginView.hideProgress();
                            Utility.printLog("getLanguages : onError :" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {
            loginView.showError(context.getResources().getString(R.string.no_network));
        }

    }

    @Override
    public void languageChanged(String langCode, String langName) {
        preferenceHelperDataSource.setLanguage(langCode);
        preferenceHelperDataSource.setLanguageSettings(new LanguagesList(langCode,langName));
        loginView.setLanguage(langName,true);
    }



}
