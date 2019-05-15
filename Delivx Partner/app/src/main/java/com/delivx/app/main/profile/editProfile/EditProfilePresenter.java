package com.delivx.app.main.profile.editProfile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;


import com.delivx.utility.country_picker.CountryPicker;
import com.delivx.utility.country_picker.CountryPickerListener;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.networking.NetworkService;
import com.delivx.utility.MyTextUtils;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;

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

public class EditProfilePresenter implements EditProfileContract.PresenterOperations {

    @Inject
    EditProfileContract.ViewOperations view;
    @Inject
    NetworkService networkService;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    Activity context;
    private String countryCode="",phone="",name="",pass="",re_pass="";
    private String data;
    private String dialCode;
    private int maxPhoneLength=15,minPhoneLength=0;


    @Inject
    public EditProfilePresenter() {
    }

    @Override
    public void getBundleData(Intent intent) {
        data =intent.getStringExtra("data");

    }

    @Override
    public void updateProfileDetails() {
        switch (data) {
            case "phone":
                view.getPhoneNumber();
                if (!MyTextUtils.isEmpty(phone) && !MyTextUtils.isEmpty(countryCode))
                {
                    if(Utility.phoneNumberLengthValidation(phone,countryCode)){
                        validaEmailPhone();
                    }else {
                        view.onError(context.getResources().getString(R.string.invalidPhone));
                    }

                } else
                {
                    view.onError(context.getResources().getString(R.string.phone_mis));
                }
                break;

            case "name":
                    view.getName();
                    if (!MyTextUtils.isEmpty(name))
                    {
                        updateProfile(name,data);
                    } else
                    {
                        view.onError(context.getResources().getString(R.string.nameMiss));
                    }

                break;

            case "password":
                view.getPassword();

                if (validPassword()) {
                    updateProfile(pass,data);
                }

                break;

            default:

                break;
        }
    }

    @Override
    public void setPhoneNumber(String phone, String countryCode) {
        this.phone=phone;
        this.countryCode=countryCode;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public void onActivityResult() {
        updateProfile(countryCode+phone,data);
    }

    @Override
    public void setPassword(String pass, String re_pass) {
        this.pass=pass;
        this.re_pass=re_pass;
    }


    private boolean validPassword()
    {
        Utility.printLog("validPassword pass: "+pass+", re pass: "+re_pass);
        if (MyTextUtils.isEmpty(pass))
        {
            view.onError(context.getString(R.string.entNewPass));
            return false;
        }
        /*else if(!pass.matches("^*[a-zA-Z](?=.*\\d).{5,14}$")){
            view.onError(context.getResources().getString(R.string.pass_validation));
            return false;
        }*/
        else if (MyTextUtils.isEmpty(re_pass))
        {
            view.onError(context.getResources().getString(R.string.reentrPass));
            return false;
        }
        else if (!pass.equals(re_pass))
        {
            view.onError(context.getResources().getString(R.string.passNotMactch));
            return false;
        }
        return true;
    }

    private void updateProfile(String value,String type)
    {
        String password=null,mobile=null,cCode=null,name=null;
        view.showProgress();

        switch (type) {
            case "phone":
                mobile=phone;
                cCode=countryCode;
                break;
            case "name":
                name=value;
                break;
            case "password":
                password=value;
                break;

        }

        final Observable<Response<ResponseBody>> profile=networkService.updateProfile(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken(),
                password,
                mobile,
                cCode,
                null,
                name,
                null
        );
        profile.observeOn(AndroidSchedulers.mainThread())
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
                                VariableConstant.IS_PROFILE_EDITED = true;
                                if(data.equals("password"))
                                    preferenceHelperDataSource.setPassword(pass);
                                view.finishActivity();

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onError(jsonObject.getString("message"));
                            }

                            Utility.printLog("updateProfile : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("updateProfile : Catch :"+e.getMessage());
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

    private void validaEmailPhone()
    {
        view.showProgress();
        Utility.printLog("emailPhoneValidateApi req : "+countryCode+phone);

        Observable<Response<ResponseBody>> emailPhoneValidateApi=networkService.emailPhoneValidate(preferenceHelperDataSource.getLanguage(),countryCode,phone,1,null);
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
                                sendOtpApi();

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onError(jsonObject.getString("message"));
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

    private void sendOtpApi() {

        view.showProgress();

        Observable<Response<ResponseBody>> sendOtp=networkService.sendOtp(preferenceHelperDataSource.getLanguage(),phone,countryCode);
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
                                Bundle bundle=new Bundle();
                                bundle.putString("from", "EditPhone");
                                bundle.putString("mobile", phone);
                                bundle.putString("otp","");
                                bundle.putString("countryCode",countryCode);

                                view.moveToVerifyAct(bundle);

                            }else {
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
                        view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgress();
                    }
                });
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
//                        maxPhoneLength=jsonObject.getInt("max_digits");
//                        minPhoneLength=(!jsonObject.getString("min_digits").isEmpty())?jsonObject.getInt("min_digits"):5;
                        break;
                    }
                }
                Utility.printLog("EditProfile Country code:"+dialCode);
                view.setCounryFlag(id,dialCode,minPhoneLength,maxPhoneLength);
                view.setMaxLength(maxPhoneLength);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    private static String readEncodedJsonString(Context context) throws java.io.IOException {
        String base64 = context.getResources().getString(R.string.countries_code);
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }

    @Override
    public void showDialogForCountryPicker() {
        final CountryPicker picker = CountryPicker.newInstance(context.getResources().getString(R.string.select_country));
        picker.show(((EditProfileActivity)context).getSupportFragmentManager(), "COUNTRY_CODE_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,int minLength,int maxLength) {
                String drawableName = "flag_"
                        + code.toLowerCase(Locale.ENGLISH);
                int resID=getResId(drawableName);

                minPhoneLength=minLength;
                maxPhoneLength=maxLength;

                view.setMaxLength(maxPhoneLength);
                view.setCounryFlag(resID,dialCode,minPhoneLength,maxPhoneLength);

                picker.dismiss();
            }
        });
    }

    @Override
    public void setActionBar() {
        view.initActionBar();
    }

    @Override
    public void onOutSideTouch() {
        Utility.hideSoftKeyboard(context);
    }

    @Override
    public void setActionBarTitle() {
        switch (data) {
            case "phone":
                view.setTitle(context.getResources().getString(R.string.edit_phone));
                view.enablePhoneEdit();
                break;
            case "name":
                view.setTitle(context.getResources().getString(R.string.change_name));
                view.enableNameEdit();
                break;
            case "password":
                view.setTitle(context.getResources().getString(R.string.change_pass));
                view.enablePasswordEdit();
                break;

        }
    }

}
