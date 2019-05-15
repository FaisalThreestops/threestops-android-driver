package com.delivx.app.main.bank.stripe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.delivx.BaseView;

import java.io.File;

/**
 * Created by DELL on 22-03-2018.
 */

public interface StripeAccountContract {

    interface ViewOperations extends BaseView{
        void onSuccess(String msg);

        void setValues(Bundle bundle);

        void getFname();

        void setFirstNameError();

        void getLastName();

        void setLastNameError();

        void getDob();

        void setDobError();

        void getPersonalId();

        void setPersonalIDError();

        void getAddress();

        void setAddressError();

        void getCity();

        void setCityError();

        void setStateError();

        void getState();

        void getPostalCode();

        void setPostalCodeError();

        void showError(String string);

        void disableError();

        void setImage(Bitmap bitmap);

        void selectImage(boolean isPictureTaken);

        void takePicture(File mFileTemp);

        void openGallery();

        void setDate(String date);
    }

    interface PresenterOperations {
        void init(Bundle bundle);

        void onActivityResult(int requestCode, int requestCode1, Intent data);

        void openCalender();

        void onSave();

        void setFname(String name);

        void setLastName(String name);

        void setDob(String dob);

        void setPersonalId(String id);

        void setAddress(String address);

        void setCityName(String cityName);

        void setState(String state);

        void setPostalCode(String postalCode);

        void selectImage();

        void removeImage();

        void takePicture();

        void openGallery();
    }
}
