package com.delivx.app.main.profile.editProfile;

import android.content.Intent;
import android.os.Bundle;

import com.delivx.BaseView;

/**
 * Created by DELL on 22-02-2018.
 */

public interface EditProfileContract {

    interface ViewOperations extends BaseView{

        void onError(String message);

        void getPhoneNumber();

        void getName();

        void moveToVerifyAct(Bundle bundle);

        void getPassword();

        void finishActivity();

        void setCounryFlag(int id, String dialCode, int minPhoneLength, int maxPhoneLength);

        void setMaxLength(int maxPhoneLength);

        void initActionBar();

        void setTitle(String string);

        void enablePhoneEdit();

        void enableNameEdit();

        void enablePasswordEdit();

    }
    interface PresenterOperations
    {
        void getBundleData(Intent intent);

        void updateProfileDetails();

        void setPhoneNumber(String phone, String countryCode);

        void setName(String name);

        void onActivityResult();

        void setPassword(String pass, String re_pass);

        void getCountryCode();

        void showDialogForCountryPicker();

        void setActionBar();

        void setActionBarTitle();

        void onOutSideTouch();
    }
}
