package com.delivx.app.invoice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.delivx.app.MyApplication;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.login.LoginActivity;
import com.delivx.login.language.LanguagesList;
import com.delivx.service.LocationUpdateService;
import com.driver.delivx.R;
import com.delivx.networking.DispatcherService;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.AppConstants;
import com.delivx.utility.MyImageHandler;
import com.delivx.utility.Upload_file_AmazonS3;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class InvoicePresenter implements InvoiceContract.PresenterOpetaions
{

    @Inject InvoiceContract.ViewOpetaions view;
    @Inject Activity context;
    @Inject Upload_file_AmazonS3 amazonS3;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject DispatcherService dispatcherService;

    private AssignedAppointments appointments;
    private Bitmap signBitmap;
    private File takenNewSignature;
    private String newSignatureName;
    private String signatureUrl="";

    @Inject
    InvoicePresenter()
    {
    }


    @Override
    public void setActionBar() {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        view.setTitle(context.getResources().getString(R.string.delivery_completed),appointments.getBid());
    }

    @Override
    public void getBundleData(Bundle bundle) {
        if(bundle!=null && bundle.containsKey("data")){
            appointments= (AssignedAppointments) bundle.getSerializable("data");
            newSignatureName = appointments.getBid() + ".jpg";
            float bill= Float.parseFloat(appointments.getTotalAmount());
            view.setViews(appointments.getCurrencySymbol()+" "+String.format(Locale.US,"%.2f",bill));
        }
    }

    @Override
    public void refresh() {
        view.clearSignature();
    }

    @Override
    public void onBackPressSign() {
        view.hideSignature(signBitmap);
    }

    @Override
    public void onSigned(Bitmap signBitmap) {
        this.signBitmap=signBitmap;
        signatureUrl="";
    }

    @Override
    public void onSignatureApprove() {
        if(signBitmap!=null){
            createFile();
            uploadToAmazon();
        }

    }

    @Override
    public void openSignaturePad() {
        view.showSignature();
    }

    @Override
    public void completeBooking(float rating) {

        Utility.printLog("rating is : "+rating);
        if(!signatureUrl.isEmpty())
            updateBookingStatus(rating);
        else
            view.onError(context.getResources().getString(R.string.please_signature));
    }

    @Override
    public void requestPermission() {
        view.methodRequiresOnePermission();
    }

    @Override
    public String getlanguageCode() {

        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }

    /**
     * <h2>createFile</h2>
     * <p>create the file to save the signature</p>
     */
    private void createFile() {
        MyImageHandler myImageHandler = MyImageHandler.getInstance();
        File dir = myImageHandler.getAlbumStorageDir(context, VariableConstant.SIGNATURE_PIC_DIR, true);
        takenNewSignature = new File(dir, newSignatureName);
        try {
            saveBitmapToJPG(signBitmap, takenNewSignature);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h2>saveBitmapToJPG</h2>
     * <p>signature image saving in JPG format</p>
     * @param bitmap : signature image resource
     * @param photo : image
     * @throws IOException : handling exception
     */
    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    /**
     * <h2>uploadToAmazon</h2>
     * <p>uploading the image to amazon site </p>
     */
    public void uploadToAmazon(){
        view.showProgress();
        String BUCKETSUBFOLDER = VariableConstant.SIGNATURE_UPLOAD;

        amazonS3.Upload_data(VariableConstant.BUCKET_NAME, BUCKETSUBFOLDER  + takenNewSignature.getName(), takenNewSignature, new Upload_file_AmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String url) {
                view.hideProgress();
                Log.d("UploadUrl", url);
                signatureUrl=url;
                view.onSignatureApprove(signBitmap);

            }

            @Override
            public void sucess(String url, String type) {
                view.hideProgress();
                signatureUrl=url;
                view.onSignatureApprove(signBitmap);
            }

            @Override
            public void error(String errormsg) {
                view.hideProgress();
                Log.d("URL", "error");
            }
        });
    }

    /**
     * <h2>updateBookingStatus</h2>
     * <p>API call for updating the Booking status</p>
     * @param rating : rating (number of stars)
     */
    private void updateBookingStatus(float rating) {
        if(view!=null){
            view.showProgress();
        }
        Observable<Response<ResponseBody>> bookingStatusRide=dispatcherService.bookingStatusRide(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken(),
                appointments.getBid(),
                AppConstants.BookingStatus.Done,
                preferenceHelperDataSource.getDriverCurrentLat(),
                preferenceHelperDataSource.getDriverCurrentLongi(),
                "1000",signatureUrl, String.valueOf(rating),null,null,null);

        bookingStatusRide.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        if(view!=null){
                            view.hideProgress();
                        }
                        try {
                            JSONObject jsonObject = null;
                            switch (value.code()) {
                                //success
                                case 200:
                                    jsonObject=new JSONObject(value.body().string());
                                    view.onSuccess(appointments);
                                    break;
                                case 440:
                                case 498:
                                    Utility.printLog("pushTopics shared pref "+preferenceHelperDataSource.getPushTopic());
                                    Utility.subscribeOrUnsubscribeTopics(new JSONArray(preferenceHelperDataSource.getPushTopic()),false);
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    ((MyApplication)context.getApplicationContext()).disconnectMqtt();
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                    if(Utility.isMyServiceRunning(LocationUpdateService.class, context))
                                    {
                                        Intent stopIntent = new Intent(context, LocationUpdateService.class);
                                        stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                                        context.startService(stopIntent);
                                    }

                                    break;
                                default:
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    break;
                            }
                            Utility.printLog("bookingStatusRide : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("bookingStatusRide : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null){
                            view.hideProgress();
                        }

                    }

                    @Override
                    public void onComplete() {
                        if(view!=null){
                            view.hideProgress();
                        }
                    }
                });
    }
}
