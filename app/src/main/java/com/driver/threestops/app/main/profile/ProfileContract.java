package com.driver.threestops.app.main.profile;

import android.content.Intent;
import android.graphics.Bitmap;

import com.driver.threestops.BaseView;
import com.driver.threestops.login.language.LanguagesList;
import com.driver.threestops.pojo.ProfileData;

import java.util.ArrayList;


public interface ProfileContract {

    interface ViewOperations extends BaseView
    {
        /**
         * <h2>setProfileDetails</h2>
         * <p>set the views by values</p>
         * @param profileDetails : profile details
         */
        void setProfileDetails(ProfileData profileDetails);

        /**
         * <h2>onError</h2>
         * <p>showing the error message</p>
         * @param error :error message
         */
        void onError(String error);

        /**
         * <h2>startEditActivity</h2>
         * <p>moving to EditProfileActivity to edit the details</p>
         * @param data : details
         */
        void startEditActivity(String data);

        /**
         * <h2>startCropImage</h2>
         * <p>crop the images to fit the profile</p>
         */
        void startCropImage();

        /**
         * <h2>setProfileImage</h2>
         * <p>set the image to profile</p>
         * @param circle_bMap : image resource
         */
        void setProfileImage(Bitmap circle_bMap);

        /**
         * <h1>setLanguageDialog</h1>
         * <p>shoe the language change dialog</p>
         * @param data language data
         * @param indexOf language index
         */
        void setLanguageDialog(ArrayList<LanguagesList> data, int indexOf);

        /**
         * <h2>setLanguage</h2>
         * <p>set the langauge code</p>
         * @param langName : language name
         * @param b : false
         */
        void setLanguage(String langName, boolean b);
    }
    interface PresenterOpetaions{

        /**
         * <h2>attachView</h2>
         * <p>attaching the MyProfileFrag view to MyProfilePresenter</p>
         * @param viewOperations : MyProfileFrag view
         */
        void attachView(ProfileContract.ViewOperations viewOperations);

        /**
         * <h2>getProfileDetails</h2>
         * <p>get the saved profile details</p>
         */
        void getProfileDetails();

        /**
         * <h2>editName</h2>
         * <p>edit the profile name</p>
         */
        void editName();

        /**
         * <h2>editPhone</h2>
         * <p>edit the phone number in profile</p>
         */
        void editPhone();

        /**
         * <h2>editPassword</h2>
         * <p>edit the password in profile</p>
         */
        void editPassword();

        /**
         * <h2>profileEdit</h2>
         * <p>changing the profile images</p>
         */
        void profileEdit();

        /**
         * <h2>logout</h2>
         * <p>logout from the account</p>
         */
        void logout();

        /**
         * <h2>onActivityResult</h2>
         * <p>checking from where to take pic to upload the image to profile</p>
         * @param requestCode : requestcode
         * @param resultCode : resultcode (gallery or camera)
         * @param data : profile details
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);

        /**
         * <h2>getLanguages</h2>
         * <p>get the language Code</p>
         */
        void getLanguages();

    }
}
