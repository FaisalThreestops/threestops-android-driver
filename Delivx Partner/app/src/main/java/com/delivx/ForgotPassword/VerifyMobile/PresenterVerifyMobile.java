package com.delivx.ForgotPassword.VerifyMobile;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.Threestops.R;
import com.delivx.networking.NetworkService;
import com.delivx.utility.Utility;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class PresenterVerifyMobile implements VerifyMobilePresenterContract {

    @Inject ForgotPasswordVerifyView view;
    @Inject Activity context;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    private String from,phone,otp,cCode;
    private JSONObject jsonObjectSignUp;
    private String SignUpVahicle = "SignUpVahicle",ForgotPass = "ForgotPass", EditPhone = "EditPhone";

    @Inject
    PresenterVerifyMobile() {
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
    public void getBundleData(Bundle bundle) {
        from = bundle.getString("from");

        if (from.equals(SignUpVahicle)) {
            String siginUpData = bundle.getString("signupdata");
            Utility.printLog("Json String for Signup :" + siginUpData);
            try {
                jsonObjectSignUp = new JSONObject(siginUpData);
            } catch (Exception e) {

            }
        }
        phone = bundle.getString("mobile");
        otp = bundle.getString("otp");
        cCode = bundle.getString("countryCode");
    }

    @Override
    public void getVerifyMessage(String msg1, String msg2) {
        view.setVerifyMessage(msg1+" "+cCode+"-"+phone+" "+msg2);
    }

    @Override
    public void startTimer(int i) {
        view.disableResendButton();
        final long finalTime = i;
        CountDownTimer countDownTimer = new CountDownTimer(finalTime * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;

                int barVal = (int) seconds;
                view.setTimerText("00:" + String.format("%02d", seconds % 60));

            }

            public void onFinish() {
                view.setTimerText("");
                view.enableResendButton();
            }
        }.start();
    }

    @Override
    public void onOutSideTouch() {
        Utility.hideSoftKeyboard(context);
    }

    @Override
    public void sendOtpApi() {

        view.disableResendButton();
        view.showProgress();

        Observable<Response<ResponseBody>> sendOtp=networkService.sendOtp(preferenceHelperDataSource.getLanguage(),phone,cCode);
        sendOtp.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ResponseBody> value) {
                view.hideProgress();

                try {
                    JSONObject jsonObject;
                    if(value.code()==200){
                        jsonObject=new JSONObject(value.body().string());
                        view.showError(jsonObject.getString("message"));
                        startTimer(60);
                    }else {
                        jsonObject=new JSONObject(value.errorBody().string());
                        view.enableResendButton();
                        view.showError(jsonObject.getString("message"));
                    }
                    Utility.printLog("sendOtp : "+jsonObject.toString());

                }catch (Exception e)
                {
                    Utility.printLog("sendOtp : Catch :"+e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                view.enableResendButton();
                view.hideProgress();
            }

            @Override
            public void onComplete() {
                view.hideProgress();
            }
        });
    }

    @Override
    public void validateOtp(String digit1,String digit2,String digit3,String digit4) {

        Utility.hideSoftKeyboard(context);
       if(digit1.matches("")||digit2.matches("")||digit3.matches("")||digit4.matches("")){
            view.showError(context.getResources().getString(R.string.enter_otp));
        }else {
            verifyOtpApi(digit1+digit2+digit3+digit4);
        }
    }

    @Override
    public void onSmsReceived(String msg) {
        Pattern pattern = Pattern.compile("(\\d{4}\\s)");
        Matcher matcher = pattern.matcher(msg);
        String OTP = "";
        if (matcher.find()) {
            OTP = matcher.group(1);  // 4 digit number
        }
        if(OTP.matches("") || OTP.matches(null))
        {
            OTP = msg.substring(Math.max(msg.length() - 4, 0));
        }

        view.setOtp(OTP);
    }

    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }

    /**
     * <h2>verifyOtpApi</h2>
     * <p>Invoke API call to verify the OTP</p>
     * @param otp : OTP numbers
     */
    public void verifyOtpApi(final String otp){
        view.showProgress();

        Observable<Response<ResponseBody>>verifyOtp=networkService.verifyOtp(preferenceHelperDataSource.getLanguage(),phone,cCode,otp);
        verifyOtp.observeOn(AndroidSchedulers.mainThread())
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

                                Utility.printLog("verifyOtp : from "+from);
                                if(from.equalsIgnoreCase(ForgotPass))
                                {
                                    view.startNextActivity(otp,phone,cCode);
                                }
                                else if(from.equalsIgnoreCase(SignUpVahicle)){
                                    signUpApi();
                                }
                                else
                                {
                                    view.mSetResult();
                                }
                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.showError(jsonObject.getString("message"));
                            }
                            Utility.printLog("verifyOtp : "+jsonObject.getString("message"));

                        }catch (Exception e)
                        {
                            Utility.printLog("verifyOtp : Catch :"+e.getMessage());
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


    /**
     * <h1>signUpApi</h1>
     * <p>API call for signup, once the phone number verify success</p>
     */
    private void signUpApi(){
        view.showProgress();

        try{
            Observable<Response<ResponseBody>>signUpApi=networkService.signUp(
                    preferenceHelperDataSource.getLanguage()
                    ,jsonObjectSignUp.getString("mobile")
                    ,jsonObjectSignUp.getString("countryCode")
                    ,jsonObjectSignUp.getString("firstName")
                    ,jsonObjectSignUp.getString("lastName")
                    ,jsonObjectSignUp.getString("email")
                    ,jsonObjectSignUp.getString("password")
                    ,jsonObjectSignUp.getString("zipCode")
                    ,jsonObjectSignUp.getString("latitude")
                    ,jsonObjectSignUp.getString("longitude")
                    ,jsonObjectSignUp.getString("profilePic")
                    ,null
                    ,null
                    ,null
                    ,null
                    ,null
                    ,null
                    ,jsonObjectSignUp.getString("referral")
                    /*,jsonObjectSignUp.getString("cities")*/
                    ,jsonObjectSignUp.getString("zones")
                    ,null
                    ,null
                    ,null
                    ,null
                    ,jsonObjectSignUp.getString("driverLicense")
                    ,jsonObjectSignUp.getString("accountType")
                    ,jsonObjectSignUp.getString("deviceType")
                    ,jsonObjectSignUp.getString("deviceId")
                    ,jsonObjectSignUp.getString("appVersion")
                    ,jsonObjectSignUp.getString("deviceMake")
                    ,jsonObjectSignUp.getString("deviceModel")
                    ,jsonObjectSignUp.getString("pushToken")
                    ,jsonObjectSignUp.getString("driverLicenseNumber")
                    ,jsonObjectSignUp.getString("cityId")
                    ,jsonObjectSignUp.getString("cityName")
                    ,jsonObjectSignUp.getString("driverLicenseExpiry")
                    ,jsonObjectSignUp.getString("dateOfBirth")
            );


            Utility.printLog("presenterVerify Signup Req: "+jsonObjectSignUp.toString());
            signUpApi.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            view.hideProgress();

                            try {
                                JSONObject jsonObject;
                                if(value.code()==200){
                                    jsonObject=new JSONObject(value.body().string());
                                    view.startLoginAct();

                                }else {
                                    jsonObject=new JSONObject(value.errorBody().string());
                                }
                                Utility.printLog("signUpApi : "+jsonObject.toString());

                            }catch (Exception e)
                            {
                                Utility.printLog("signUpApi : Catch :"+e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.hideProgress();
                        }

                        @Override
                        public void onComplete() {
                            view.hideProgress();
                        }
                    });
        }catch (Exception e){

            Utility.printLog("presenterVerify caught: "+e.getMessage());
        }
    }
}
