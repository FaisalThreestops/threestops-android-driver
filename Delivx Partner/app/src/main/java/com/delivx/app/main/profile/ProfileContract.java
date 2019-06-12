package com.delivx.app.main.profile;

import android.content.Intent;
import android.graphics.Bitmap;

import com.delivx.BaseView;
import com.delivx.login.language.LanguagesList;
import com.delivx.pojo.ProfileData;

import java.util.ArrayList;

/**
 * Created by DELL on 21-02-2018.
 */

public interface ProfileContract {

    interface ViewOperations extends BaseView
    {
        void setProfileDetails(ProfileData profileDetails);

        void onError(String error);

        void startEditActivity(String data);

        void startCropImage();

        void setProfileImage(Bitmap circle_bMap);

        void setLanguageDialog(ArrayList<LanguagesList> data, int indexOf);

        void setLanguage(String langName, boolean b);
    }
    interface PresenterOpetaions{

        void attachView(ProfileContract.ViewOperations viewOperations);

        void getProfileDetails();

        void editName();

        void editPhone();

        void editPassword();

        void profileEdit();

        void logout();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void getLanguages();

        void languageChanged(String langCode, String langName, int dir);
    }
}
