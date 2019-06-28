package com.delivx.ForgotPassword.RetriveFromMobile;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;

import com.delivx.ForgotPassword.ForgotPasswordMobNum;
import com.delivx.utility.country_picker.CountryPicker;
import com.delivx.utility.country_picker.CountryPickerListener;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.networking.NetworkService;
import com.delivx.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;



public class PresenterForgotPass implements ForgotPassPresenterContract {

    @Inject ForgotPassMobNumView view;
    @Inject Activity context;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    private int minPhoneLength=0,maxPhoneLength=15;
    private boolean isEmail;


    @Inject
    PresenterForgotPass() {
    }



    private static int getResId(String drawableName) {
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
        String code=Utility.getCounrtyCode(context);

        if(!code.isEmpty()){
            String drawableName = "flag_"
                    + code.toLowerCase(Locale.ENGLISH);
            int id=getResId(drawableName);
            String allCountriesCode = null;
            try {
                allCountriesCode = readEncodedJsonString(context);
                JSONArray countrArray = new JSONArray(allCountriesCode);
                JSONObject jsonObject;
                String dialCode="";
                for(int index=0;index<countrArray.length();index++){
                    jsonObject=countrArray.getJSONObject(index);
                    if(jsonObject.getString("code").equals(code)){
                        dialCode=jsonObject.getString("dial_code");
                        maxPhoneLength=jsonObject.getInt("max_digits");
                        minPhoneLength=(!jsonObject.getString("min_digits").isEmpty())?jsonObject.getInt("min_digits"):5;
                        break;
                    }
                }

                view.setCountryFlag(id,dialCode,minPhoneLength,maxPhoneLength);
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
        picker.show(((ForgotPasswordMobNum)context).getSupportFragmentManager(), "COUNTRY_CODE_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,int minLength,int maxLength) {
                String drawableName = "flag_"
                        + code.toLowerCase(Locale.ENGLISH);
                int resID=getResId(drawableName);
                minPhoneLength=minLength;
                maxPhoneLength=maxLength;
                view.setCountryFlag(resID,dialCode,minPhoneLength,maxPhoneLength);
                picker.dismiss();
            }
        });
    }

    @Override
    public void onOutSideTouch() {
        Utility.hideSoftKeyboard(context);
    }


    private static String readEncodedJsonString(Context context)
            throws java.io.IOException {
        String base64 = context.getResources().getString(R.string.countries_code);
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }

    @Override
    public boolean onNextKey(View v, int keyCode, KeyEvent event, String mob) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            validatePhone(mob,"");
            return true;
        }
        return false;
    }

    @Override
    public void validatePhone(String mob_mail,String countryCode) {
        //if mobile number is empty
        if (!mob_mail.matches("")) {
            //if email matches the format
            if (Patterns.EMAIL_ADDRESS.matcher(mob_mail).matches()) {
                forgotPasswordApi(2,"",mob_mail);
            }
            //if phone match
            else if (Utility.phoneNumberLengthValidation(mob_mail,countryCode)) {
                forgotPasswordApi(1,countryCode,mob_mail);
            } else {
                if(isEmail){
                    Utility.mShowMessage(context.getResources().getString(R.string.message),
                            context.getResources().getString(R.string.invalidEmail),context);
                }else {
                    Utility.mShowMessage(context.getResources().getString(R.string.message),
                            context.getResources().getString(R.string.invalidPhone),context);
                }
            }
        }
    }

    @Override
    public void etAfterTextChanged() {
        view.editextAfterChanged();
    }

    @Override
    public void rbEmailChecked() {
        isEmail=true;
        view.onEmailSelection();
    }

    @Override
    public void rbMobileChecked() {
        isEmail=false;
        view.onMobileSelection();
    }

    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }

    /**
     * <h1>forgotPasswordApi</h1>
     * <p>API call for verify phone number or mail id</p>
     * @param verifyType email or phone
     * @param countryCode country code
     * @param mob_mail mobile number
     */
    private void forgotPasswordApi(final int verifyType, final String countryCode,
                                   final String mob_mail){
        if(view!=null)
            view.showProgress();

        Observable<Response<ResponseBody>> forgotPass=networkService.forgotPassword(preferenceHelperDataSource.getLanguage(),countryCode,mob_mail,verifyType);
        forgotPass.observeOn(AndroidSchedulers.mainThread())
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
                                //mobile number verification
                                if(verifyType==1){
                                    view.startNextActivity(countryCode,mob_mail);
                                }else {
                                    view.moveToLogin(jsonObject.getString("message"));
                                }

                            }else {
                                //email address verification
                                if(value.code()==202 && verifyType==2){
                                    jsonObject=new JSONObject(value.body().string());
                                    view.moveToLogin(jsonObject.getString("message"));
                                }else{
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    view.onError(jsonObject.getString("message"));
                                }
                            }
                            Utility.printLog("forgotPasswordApi : "+jsonObject.toString());

                        }catch (JSONException e)
                        {
                            Utility.printLog("forgotPasswordApi : Catch :"+e.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
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
