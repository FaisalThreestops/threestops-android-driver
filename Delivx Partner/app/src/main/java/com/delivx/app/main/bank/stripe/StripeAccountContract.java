package com.delivx.app.main.bank.stripe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.delivx.BaseView;

import java.io.File;



public interface StripeAccountContract {

    interface ViewOperations extends BaseView{
        void onSuccess(String msg);

        /**
         * <h2>setValues</h2>
         * <p>set the view by values</p>
         * @param bundle : previously stored data
         */
        void setValues(Bundle bundle);

        /**
         * <h2>getFname</h2>
         * <p>get the first name</p>
         */
        void getFname();

        /**
         * <h2>setFirstNameError</h2>
         * <p>display the fisrt name error message on the view</p>
         */
        void setFirstNameError();

        /**
         *<h2>getLastName</h2>
         * <p>get the last name</p>
         */
        void getLastName();

        /**
         * <h2>setLastNameError</h2>
         * <p>display the last name error message on the screen</p>
         */
        void setLastNameError();

        /**
         * <h2>getDob</h2>
         * <p>to get the date of birth</p>
         */
        void getDob();

        /**
         * <h2>setDobError</h2>
         * <p>display the Date of birth error message on the screen</p>
         */
        void setDobError();

        /**
         * <h2>getPersonalId</h2>
         * <p>get the ssn(personal Id)</p>
         */
        void getPersonalId();

        /**
         * <h2>setPersonalIDError</h2>
         * <p>set the error message</p>
         */
        void setPersonalIDError();

        /**
         * <h2>getAddress</h2>
         * <p>get the address and set it into the presenter</p>
         */
        void getAddress();

        /**
         * <h2>setAddressError</h2>
         * <p>set the address error message on the view</p>
         */
        void setAddressError();

        /**
         * <h2>getCity</h2>
         * <p>get the city from the textview</p>
         */
        void getCity();

        /**
         * <h2>setCityError</h2>
         * <p>set the city name error on the view</p>
         */
        void setCityError();

        /**
         * <h2>setStateError</h2>
         * <p>set the state name error on the view</p>
         */
        void setStateError();

        /**
         * <h2>getState</h2>
         * <p>get the state name and set that in to presenter</p>
         */
        void getState();

        /**
         * <h2>getPostalCode</h2>
         * <p>get the ssn and set that in to presenter</p>
         */
        void getPostalCode();

        /**
         * <h2>setPostalCodeError</h2>
         * <p>set the ssn to presenter</p>
         */
        void setPostalCodeError();

        /**
         * <h2>showError</h2>
         * <p>displaying the error messages</p>
         * @param string : error message
         */
        void showError(String string);

        /**
         * <h2>disableError</h2>
         * <p>disables the error message after all the fields valid</p>
         */
        void disableError();

        /**
         * <h2>setImage</h2>
         * <p>setting the image to the imagview field</p>
         * @param bitmap :image resource
         */
        void setImage(Bitmap bitmap);

        /**
         * <h2>selectImage</h2>
         * <p>selecting the image(camera,gallery)</p>
         * @param isPictureTaken :false
         */
        void selectImage(boolean isPictureTaken);

        /**
         * <h2>takePicture</h2>
         * <p>select the capture image</p>
         * @param mFileTemp :path of the image
         */
        void takePicture(File mFileTemp);

        /**
         * <h2>openGallery</h2>
         * <p>take the image from gallery</p>
         */
        void openGallery();

        /**
         * <h2>setDate</h2>
         * <p>set the date </p>
         * @param date :date of birth
         */
        void setDate(String date);
    }

    interface PresenterOperations {

        /**
         * <h2>init</h2>
         * <p>set the values if bundle is not empty</p>
         * @param bundle : previous data
         */
        void init(Bundle bundle);

        /**
         * <h2>onActivityResult</h2>
         * <p>for picking the images(camera or gallery)</p>
         * @param requestCode : request code
         * @param requestCode1 : requestCode
         * @param data : details
         */
        void onActivityResult(int requestCode, int requestCode1, Intent data);

        /**
         * <h2>openCalender</h2>
         * <p>open the calender to select the date</p>
         */
        void openCalender();

        /**
         * <h2>onSave</h2>
         * <p>validate the fields and save the data</p>
         */
        void onSave();

        /**
         * <h2>setFname</h2>
         * <p>get the firstname and stored in to the presenter</p>
         * @param name : Firstname
         */
        void setFname(String name);

        /**
         * <h2>setLastName</h2>
         * <p>set the last name to the presenter</p>
         * @param name: last name
         */
        void setLastName(String name);

        /**
         * <h2>setDob</h2>
         * <P>set the DOB </P>
         * @param dob : date
         */
        void setDob(String dob);

        /**
         * <h2>setPersonalId</h2>
         * <p>set the ssn to the presenter</p>
         * @param id : ssn(id)
         */
        void setPersonalId(String id);

        /**
         * <h2>setAddress</h2>
         * <p>set the address </p>
         * @param address :address
         */
        void setAddress(String address);

        /**
         * <h2>setCityName</h2>
         * <p>set the city name to the presenter</p>
         * @param cityName :city name
         */
        void setCityName(String cityName);

        /**
         * ,h2>setState
         * <p>set the state name</p>
         * @param state : state
         */
        void setState(String state);

        /**
         * <h2>setPostalCode</h2>
         * <p>set the ssn view</p>
         * @param postalCode :ssn
         */
        void setPostalCode(String postalCode);

        /**
         * <h2>selectImage</h2>
         * <p>selecting the image</p>
         */
        void selectImage();

        /**
         * <h2>removeImage</h2>
         * <p>removing the uploaded image </p>
         */
        void removeImage();

        /**
         * <h2>takePicture</h2>
         * <p>taking the path of the capture image</p>
         */
        void takePicture();

        /**
         * <h2>openGallery</h2>
         * <p>opens the gallery to select the image</p>
         */
        void openGallery();

        /**
         * <h2>getlanguageCode</h2>
         * <p>get the languagecode</p>
         * @return :language code
         */
        String getlanguageCode();
    }
}
