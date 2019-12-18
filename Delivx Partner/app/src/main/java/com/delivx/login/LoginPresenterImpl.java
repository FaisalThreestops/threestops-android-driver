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
import com.delivx.pojo.ServiceZoneList;
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
import java.util.ArrayList;
import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginPresenterImpl implements LoginPresenter
{
    @Inject Activity context;
    @Inject NetworkService networkService;
    @Inject LoginView loginView;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject MQTTManager mqttManager;
    @Inject LanguageApiService languageApiService;

    private boolean isEmailOptionSelected=true;
    private String countryCode="+31";
    private ArrayList<LanguagesList> languagesLists;

    @Inject
    LoginPresenterImpl() {
        languagesLists = new ArrayList<>();
    }

    @Override
    public void validateCredentials(String phone, String username, String password) {

        if (MyTextUtils.isEmpty(username) && isEmailOptionSelected) {
            onUsernameError(context.getString(R.string.err_email));
        }
        else if (!MyTextUtils.isEmail(username) && isEmailOptionSelected  )
        {
            onUsernameError(context.getString(R.string.invalidEmail));
        }
        else if(!isEmailOptionSelected && MyTextUtils.isEmpty(phone))
        {
            loginView.showError(context.getString(R.string.phone_mis));
        }
        else if( !Utility.phoneNumberLengthValidation(phone,countryCode) && !isEmailOptionSelected )
        {
            loginView.showError(context.getString(R.string.invalidPhone));
        }
        else if (MyTextUtils.isEmpty(password)) {
            onPasswordError(context.getString(R.string.password_miss));
        }
        else {
            if(isEmailOptionSelected)
                signIn(username,password);
            else
                signIn(phone,password);
        }
    }

    @Override
    public void getCountryCode() {
        String code= Utility.getCounrtyCode(context);

        if(!code.isEmpty()){
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
                      break;
                        }
                    }
                    loginView.setCountryCode(dialCode);
                    countryCode=dialCode;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <h1>readEncodedJsonString</h1>
     * <p>decode the country code</p>
     * @param context activity
     * @return decoded country code
     * @throws java.io.IOException
     */
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
    public void forgotPassOnclick() {
        loginView.startForgotPassAct();
    }

    @Override
    public void signUpOnclick() {
        loginView.startSignUpAct();
    }

    @Override
    public void getBundleData(Intent intent) {
        if(preferenceHelperDataSource.getLanguageSettings()!=null && preferenceHelperDataSource.getLanguageSettings().getLanguageName()!=null)
        loginView.setLanguage(preferenceHelperDataSource.getLanguageSettings().getLanguageName(),
                false);

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

    @Override
    public void showDialogForCountryPicker() {
        final CountryPicker picker = CountryPicker.newInstance(context.getResources().getString(R.string.select_country));
        picker.show(((LoginActivity)context).getSupportFragmentManager(), "COUNTRY_CODE_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,int minLength,int maxLength) {
                countryCode=dialCode;
                loginView.setCountryCode(dialCode);
                picker.dismiss();
            }
        });
    }

    /**
     * <h1>onUsernameError</h1>
     * <p>for the email error message show</p>
     * @param message error message for email
     */
    private void onUsernameError(String message) {
        if (loginView != null) {
            loginView.setUsernameError(message);
            loginView.hideProgress();
        }
    }


    /**
     * <h1>onPasswordError</h1>
     * <p>set the error for password </p>
     * @param message error message for password
     */
    private void onPasswordError(String message) {
        if (loginView != null) {
            loginView.setPasswordError(message);
            loginView.hideProgress();
        }
    }


    /**
     * <h1>onSuccess</h1>
     * <p>for hide the progress and open the MainActivity after login success</p>
     */
    public void onSuccess() {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.navigateToHome();
        }
    }

    /**
     * <h2>onError<h2/>
     * <p>This method shows the error received from the API call
     * in the view and hide the progress bar</p>
     * @param error This is the error received from the API
     */
    public void onError(String error) {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.showError(error);
        }
    }


    /**
     * <h1>signIn</h1>
     * <p>Sign In API call</p>
     * @param username email id or phone number
     * @param password password
     */
    private void signIn(String username, final String password){
        if (loginView != null) {
            loginView.showProgress();
        }
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
                    if(value.code()==200){
                        String res = value.body().string();
                        Utility.printLog("the login response : "+res);
                        jsonObject=new JSONObject(res);
                        Gson gson=new Gson();
                        SinginResponsePojo signInResponse=gson.fromJson(jsonObject.toString(),SinginResponsePojo.class);
                        preferenceHelperDataSource.setDriverType(signInResponse.getData().getDriverType());
                        preferenceHelperDataSource.setPushTopic(jsonObject.getJSONObject("data").getString("fcmTopics"));
                        setSignInData(signInResponse.getData(),password);
                        Utility.printLog("LoginResponse : "+jsonObject.toString());
                    }else {
                        jsonObject=new JSONObject(value.errorBody().string());
                        loginView.showError(jsonObject.getString("message"));
                        Utility.printLog("LoginResponse : "+value.errorBody().string());
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

    /**
     * <h1>setSignInData</h1>
     * <p>Handle the login response ,
     * store the config data in shared preference, and subscribe the MQTT Topic.</p>
     * @param data sign in response
     * @param password password for login
     */
    private void setSignInData(SigninData data, String password){
        preferenceHelperDataSource.setDriverChannel(data.getChn());
        //driver schedulerType(0,1)
        preferenceHelperDataSource.setDriverScheduleType(data.getDriverScheduleType());
        String driver_channel = "message/"+data.getMid();
        Utility.printLog("login driver_channel "+driver_channel);
        preferenceHelperDataSource.setDriverChannel_msg(driver_channel);
        VariableConstant.MQTT_CHANEL = data.getChn();
        preferenceHelperDataSource.setDriverID(data.getMid());
        preferenceHelperDataSource.setToken(data.getToken());
        preferenceHelperDataSource.setPresenceChannel(data.getPresence_chn());
        preferenceHelperDataSource.setMyName(data.getName());
        preferenceHelperDataSource.setProfilePic(data.getProfilePic());
        preferenceHelperDataSource.setIsLogin(true);
        preferenceHelperDataSource.setPassword(password);
        preferenceHelperDataSource.setMyEmail(data.getEmail());


        preferenceHelperDataSource.setCityId(data.getCityId());
        ServiceZoneList serviceZoneList = new ServiceZoneList();
        serviceZoneList.setServiceZones(data.getServiceZones());
        preferenceHelperDataSource.setServiceZoneList(serviceZoneList);

        preferenceHelperDataSource.setDefaultBankAccount(data.getDefaultBankAccount());
        preferenceHelperDataSource.setCountry(data.getCountry());
        preferenceHelperDataSource.setCurrency(data.getCurrency());
        preferenceHelperDataSource.setEnableBankAccount(data.getEnableBankAccount());

        Utility.printLog("pushTopics shared pref "+preferenceHelperDataSource.getPushTopic());
        try {
            Utility.subscribeOrUnsubscribeTopics(new JSONArray(preferenceHelperDataSource.getPushTopic()),true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(MyApplication.getInstance().isMQTTConnected()){
            Log.i("check", "setSignInData: if");
            mqttManager.subscribeToTopic(preferenceHelperDataSource.getDriverID());

        }else {
            Log.i("check", "setSignInData: else");
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
