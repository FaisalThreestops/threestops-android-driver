package com.delivx.app.main.profile.editProfile;

import android.content.Intent;
import android.os.Bundle;

import com.delivx.BaseView;

/**
 * Created by DELL on 22-02-2018.
 */

public interface EditProfileContract {

    interface ViewOperations extends BaseView{

        /**
         * <h2>onError</h2>
         * <p>showing the error messages</p>
         * @param message : Error
         */
        void onError(String message);

        /**
         * <h2>getPhoneNumber</h2>
         * <p>get the phone number to verify</p>
         */
        void getPhoneNumber();

        /**
         * <h2>getName</h2>
         * <p>get the name to verify</p>
         */
        void getName();

        /**
         * <h2>moveToVerifyAct</h2>
         * <p>moving to ForgotPasswordVerify to verify password</p>
         * @param bundle : profile details
         */
        void moveToVerifyAct(Bundle bundle);

        /**
         * <h2>getPassword</h2>
         * <p>get the password to verify </p>
         */
        void getPassword();

        /**
         * <h2>finishActivity</h2>
         * <p>finish the EditProfileActivity</p>
         */
        void finishActivity();

        /**
         * <h2>setCounryFlag</h2>
         * <p>set the country code in the view</p>
         * @param id : Id
         * @param dialCode : country code
         * @param minPhoneLength : minimum phone number length
         * @param maxPhoneLength :maximum phone number length
         */
        void setCounryFlag(int id, String dialCode, int minPhoneLength, int maxPhoneLength);

        /**
         * <h2>setMaxLength</h2>
         * <p>set the maximum phone number length</p>
         * @param maxPhoneLength : maximum phone number length
         */
        void setMaxLength(int maxPhoneLength);

        /**
         * <h2>initActionBar</h2>
         * <p>initializing the action bar</p>
         */
        void initActionBar();

        /**
         * <h2>setTitle</h2>
         * <p>set the title to the action bar</p>
         * @param string : Activity title
         */
        void setTitle(String string);

        /**
         * <h2>enablePhoneEdit</h2>
         * <p>enabling the phone number view to edit</p>
         */
        void enablePhoneEdit();

        /**
         * <h2>enableNameEdit</h2>
         * <p>enabling the name view to  edit</p>
         */
        void enableNameEdit();

        /**
         * <h2>enablePasswordEdit</h2>
         * <p>enabling the password view to edit</p>
         */
        void enablePasswordEdit();

    }
    interface PresenterOperations
    {
        /**
         * <h2><getBundleData</h2>
         * <p>get the data from MyProfileFrag</p>
         * @param intent : profile details
         */
        void getBundleData(Intent intent);

        /**
         * <h2>updateProfileDetails</h2>
         * <p>API call for updating the profile details</p>
         */
        void updateProfileDetails();

        /**
         * <h2>setPhoneNumber</h2>
         * <p>set the phone number and code preesenter</p>
         * @param phone : phone number
         * @param countryCode : code
         */
        void setPhoneNumber(String phone, String countryCode);

        /**
         * <h2>setName</h2>
         * <p>set the profile name to presenter</p>
         * @param name : profile name
         */
        void setName(String name);

        /**
         * <h2>onActivityResult</h2>
         * <p>update the phone profile details</p>
         */
        void onActivityResult();

        /**
         * <h2>setPassword</h2>
         * <p>set the password to presenter</p>
         * @param pass : new password
         * @param re_pass : confirm password
         */
        void setPassword(String pass, String re_pass);

        /**
         * <h2>getCountryCode</h2>
         * <p>get the country code</p>
         */
        void getCountryCode();

        /**
         * <h2>showDialogForCountryPicker</h2>
         * <p>showing the alert dialog to pick the country code</p>
         */
        void showDialogForCountryPicker();

        /**
         * <h2>setActionBar</h2>
         * <p>set the action bar</p>
         */
        void setActionBar();

        /**
         * <h2>setActionBarTitle</h2>
         * <p>set the title to the action bar</p>
         */
        void setActionBarTitle();

        /**
         * <h2>onOutSideTouch</h2>
         * <p>hides the keyboard</p>
         */
        void onOutSideTouch();

        /**
         * <h2>getlanguageCode</h2>
         * <p>set and get the language code</p>
         * @return :language code
         */
        String getlanguageCode();
    }
}
