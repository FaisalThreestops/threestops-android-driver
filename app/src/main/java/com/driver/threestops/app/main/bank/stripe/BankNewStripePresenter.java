package com.driver.threestops.app.main.bank.stripe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.DatePicker;

import com.driver.threestops.app.MyApplication;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
 import com.driver.Threestops.BuildConfig;
import com.driver.Threestops.R;
import com.driver.threestops.networking.NetworkService;
import com.driver.threestops.utility.MyTextUtils;
import com.driver.threestops.utility.Upload_file_AmazonS3;
import com.driver.threestops.utility.Utility;
import com.driver.threestops.utility.VariableConstant;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import javax.inject.Inject;

import eu.janmuller.android.simplecropimage.CropImage;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class BankNewStripePresenter implements StripeAccountContract.PresenterOperations {

    @Inject   NetworkService networkService;
    @Inject   Activity context;
    @Inject   StripeAccountContract.ViewOperations view;
    @Inject   Upload_file_AmazonS3 amazonS3;
    @Inject   PreferenceHelperDataSource preferenceHelperDataSource;


    private final int REQUEST_CODE_GALLERY = 0x1;
    private final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    private final int REQUEST_CODE_CROP_IMAGE = 0x3;
    private String imageUrl = "";
    private Bitmap bitmap;
    private boolean isPicturetaken;
    private File mFileTemp;
    private static String sentingCaimDate;
    private DatePickerFragment datePickerFragment;
    private String fname="",lName="",dob="",personalId="",address="",city="",state="",postalCode="";
    private String ip="10.10.10.10";


    @Inject
    BankNewStripePresenter() {
    }

    @Override
    public void init(Bundle bundle) {
        //bundle is empty when 1st time adding the stripe
        if (bundle != null) {
            view.setValues(bundle);
        }

        String state = Environment.getExternalStorageState();
        //state= mounted
        String filename = System.currentTimeMillis() + ".png";
        //filename=1561095711190.png
        datePickerFragment = new DatePickerFragment();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), filename);
            //mFileTemp=/storage/emulated/0/1561095711190.png
        } else {
            mFileTemp = new File(context.getFilesDir(), filename);
        }
        getIpAddress();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK)
            {
                switch (requestCode) {
                    //selecting the image from gallery
                    case REQUEST_CODE_GALLERY:

                        try {
                            //reading the data
                            InputStream inputStream = context.getContentResolver().openInputStream(
                                    data.getData());
                            // writing data to a file.
                            FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                            copyStream(inputStream, fileOutputStream);
                            fileOutputStream.close();
                            inputStream.close();
                            bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                            view.setImage(bitmap);
                            isPicturetaken = true;

                        } catch (Exception e) {
                            Log.d("", "Error while creating temp file", e);
                        }

                        break;
                    //selecting the image from camera
                    case REQUEST_CODE_TAKE_PICTURE:
                        isPicturetaken = true;
                        //bitmap handles the image resources
                        bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                        view.setImage(bitmap);
                        break;

                    case REQUEST_CODE_CROP_IMAGE:
                        String path = data.getStringExtra(CropImage.IMAGE_PATH);
                        if (path == null) {

                            return;
                        }

                        bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                        view.setImage(bitmap);


                        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

                        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                        Paint paint = new Paint();
                        paint.setShader(shader);
                        paint.setAntiAlias(true);
                        Canvas c = new Canvas(circleBitmap);
                        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);


                        isPicturetaken = true;
                        break;
                }

            }
        } catch (Exception e) {
            Log.d("BankNewStripePresenter", "onActivityResult: " + e);
        }
    }

    @Override
    public void openCalender() {
        if (!datePickerFragment.isResumed()) {
            datePickerFragment.show(context.getFragmentManager(), "dataPicker");
        }
    }

    @Override
    public void onSave() {
        if(validateFields()){
            amzonUpload();
        }
    }

    /**
     * <h1>addBankDetails</h1>
     * <p>API call for adding the stripe account</p>
     */
    private void addBankDetails() {
        String[] dateOfBirth = dob.split("/");

        Utility.printLog("bank sripe : "+ ((MyApplication) context.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID())+"\n"+
                preferenceHelperDataSource.getMyEmail()+"\n"+
                city+"\n"+
                "US"+"\n"+
                address+"\n"+
                postalCode+"\n"+
                state+"\n"+
                dateOfBirth[1]+"\n"+
                dateOfBirth[0]+"\n"+
                dateOfBirth[2]+"\n"+
                fname+"\n"+
                lName+"\n"+
                imageUrl+"\n"+
                personalId+"\n"+
                Utility.currentDate()+"\n"+
                ip);

        final Observable<Response<ResponseBody>> profile=networkService.createStripeAccount(preferenceHelperDataSource.getLanguage(),
                ((MyApplication) context.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()),
                preferenceHelperDataSource.getMyEmail(),
                city,
                preferenceHelperDataSource.getCountry(),
                address,
                postalCode,
                state,
                dateOfBirth[1],
                dateOfBirth[0],
                dateOfBirth[2],
                fname,
                lName,
                imageUrl,
                personalId,
                Utility.currentDate(),
                ip
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
                            switch (value.code()){

                                case 200:
                                    jsonObject=new JSONObject(value.body().string());
                                    Utility.printLog("bank stripe getConnectAccount : "+value.body().string());
                                    view.onSuccess(jsonObject.getString("message"));
                                    break;

                                default:
                                    String err =value.errorBody().string();
                                    Utility.BlueToast(context,err);
                                    Utility.printLog("bank stripe error getConnectAccount : "+value.errorBody().string());
                                    break;
                            }


                        }catch (Exception e)
                        {
                            Utility.printLog("bank connectAccount : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                        Utility.printLog("bank connectAccount : Catch :"+e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }

    @Override
    public void setFname(String name) {
        this.fname=name;
    }

    @Override
    public void setLastName(String name) {
        this.lName=name;
    }

    @Override
    public void setDob(String dob) {
        this.dob=dob;
    }

    @Override
    public void setPersonalId(String id) {
        this.personalId=id;
    }

    @Override
    public void setAddress(String address) {
        this.address=address;
    }

    @Override
    public void setCityName(String cityName) {
        this.city=cityName;
    }

    @Override
    public void setState(String state) {
        this.state=state;
    }

    @Override
    public void setPostalCode(String postalCode) {
        this.postalCode=postalCode;
    }

    @Override
    public void selectImage() {
        view.selectImage(isPicturetaken);
    }

    @Override
    public void removeImage() {
        isPicturetaken=false;
    }

    @Override
    public void takePicture() {
        view.takePicture(mFileTemp);
    }

    @Override
    public void openGallery() {
        view.openGallery();
    }

    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }


    /**
     * <h2>getIpAddress</h2>
     * <p>API call to get the ip address</p>
     */
    private void getIpAddress() {

        final Observable<Response<ResponseBody>> profile=networkService.ipAddess();
        profile.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200)
                            {
                                jsonObject=new JSONObject(value.body().string());
                                ip=jsonObject.getString("ip");

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                            }

                            Utility.printLog("IpAddress : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("IpAddress : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * <h2>copyStream</h2>
     * <p>copy the read inputStream to output</p>
     * @param input : readed data
     * @param output : copying the readed data
     * @throws IOException : execption
     */
    private static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    /**<h2>amzonUpload</h2>
     * <p>uploading the image to amazon site</p>
     */
    private void amzonUpload() {

        view.showProgress();
        String BUCKETSUBFOLDER = VariableConstant.BANK_PROOF;
        imageUrl = BuildConfig.AMAZON_BASE_URL + BuildConfig.BUCKET_NAME + "/" + BUCKETSUBFOLDER + "/" + mFileTemp.getName();


        amazonS3.Upload_data(BuildConfig.BUCKET_NAME, BUCKETSUBFOLDER + "/" + mFileTemp.getName(), mFileTemp, new Upload_file_AmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String url) {
                imageUrl=url;
                Utility.printLog("Bank document : "+imageUrl);
                /*view.hideProgress();*/
                addBankDetails();
            }

            @Override
            public void sucess(String url, String type) {
                imageUrl=url;
                Utility.printLog("Bank document :: "+imageUrl);
                view.hideProgress();
                addBankDetails();
            }

            @Override
            public void error(String errormsg) {
                view.hideProgress();
            }
        });
    }


    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker v, int year, int monthOfYear, int dayOfMonth) {
            sentingCaimDate = Utility.sentingDateFormat(year, monthOfYear, dayOfMonth);
            view.setDate(Utility.displayDateFormat(year, monthOfYear, dayOfMonth));
        }

    }

    /**
     * <h2>validateFields</h2>
     * <p>validating the fields</p>
     * @return true: validate success, false : error
     */
    private boolean validateFields(){

        if(!isPicturetaken){
            view.showError(context.getString(R.string.plsUploadIdProf));
            return false;
        }

        view.getFname();

        if(MyTextUtils.isEmpty(fname)){
            view.setFirstNameError();
            return false;
        }

        view.getLastName();

        if(MyTextUtils.isEmpty(lName)){
            view.setLastNameError();
            return false;
        }

        view.getDob();
        if(MyTextUtils.isEmpty(dob)){
            view.setDobError();
            return false;
        }

        view.getPersonalId();
        if(MyTextUtils.isEmpty(personalId)){
            view.setPersonalIDError();
            return false;
        }

        view.getAddress();
        if(MyTextUtils.isEmpty(address)){
            view.setAddressError();
            return false;
        }

        view.getCity();
        if(MyTextUtils.isEmpty(city)){
            view.setCityError();
            return false;
        }

        view.getState();
        if(MyTextUtils.isEmpty(state)){
            view.setStateError();
            return false;
        }

        view.getPostalCode();
        if(MyTextUtils.isEmpty(postalCode)){
            view.setPostalCodeError();
            return false;
        }

        view.disableError();
        return true;
    }
}
