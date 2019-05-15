package com.delivx.app.main.profile;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.gson.Gson;
import com.delivx.app.main.profile.editProfile.ChangePasswordDialog;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.logout.LogoutPopup;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.ProfilePojo;
import com.delivx.utility.ImageEditUpload;
import com.delivx.utility.Upload_file_AmazonS3;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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


public class MyProfilePresenter implements ProfileContract.PresenterOpetaions {


    ProfileContract.ViewOperations view;

    @Inject
    NetworkService networkService;

    @Inject
    Activity context;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    Upload_file_AmazonS3 amazonS3;

    private ChangePasswordDialog changePasswordDialog;
    private String  TAG=MyProfilePresenter.class.getSimpleName();
    private String fileName,takenNewImage,state;
    private static final int CAMERA_PIC = 11, GALLERY_PIC = 12, CROP_IMAGE = 13;


    @Inject
    public MyProfilePresenter() {
    }

    @Override
    public void attachView(ProfileContract.ViewOperations viewOperations) {
        view=viewOperations;
    }

    @Override
    public void getProfileDetails()
    {
        view.showProgress();
        final Observable<Response<ResponseBody>> profile=networkService.profile(preferenceHelperDataSource.getLanguage(),preferenceHelperDataSource.getToken());
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
                                Gson gson=new Gson();
                                ProfilePojo profilePojo = gson.fromJson(jsonObject.toString(), ProfilePojo.class);
                                preferenceHelperDataSource.setMyName(profilePojo.getData().getName());
                                preferenceHelperDataSource.setProfilePic(profilePojo.getData().getProfilePic());
                                view.setProfileDetails(profilePojo.getData());

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onError(jsonObject.getString("message"));
                            }

                            Utility.printLog("profileApi : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("profileApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                        view.onError( context.getResources().getString(R.string.serverError));
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }

    @Override
    public void editName() {
        view.startEditActivity("name");
    }

    @Override
    public void editPhone() {
        view.startEditActivity("phone");
    }

    @Override
    public void editPassword() {
        changePasswordDialog = new ChangePasswordDialog(context, "Password", new ChangePasswordDialog.RefreshProfile() {
            @Override
            public void onRefresh() {
                changePasswordDialog.dismiss();
                changePasswordDialog.cancel();
                view.startEditActivity("password");
            }
        },preferenceHelperDataSource);

        changePasswordDialog.show();
    }

    @Override
    public void profileEdit() {
        VariableConstant.isVehPictureTaken = false;
        ImageEditUpload imageEditUpload = new ImageEditUpload(context, "profile_pic");
    }

    @Override
    public void logout() {
        LogoutPopup logoutPopup = new LogoutPopup(context,preferenceHelperDataSource,networkService);
        logoutPopup.setCancelable(false);
        logoutPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logoutPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutPopup.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Utility.printLog(TAG+" onActivtyResult "+requestCode);
        if (resultCode != RESULT_OK) {
            return;
        } else if (requestCode != -1) {
            switch (requestCode) {
                case CAMERA_PIC:
                    view.startCropImage();
                    break;

                case GALLERY_PIC:
                    try {
                        Utility.printLog("RegistrationAct in GALLERY_PIC:");
                        takenNewImage = "";
                        state = Environment.getExternalStorageState();
                        takenNewImage = VariableConstant.PARENT_FOLDER + String.valueOf(System.nanoTime()) + ".png";

                        if (Environment.MEDIA_MOUNTED.equals(state)) {
                            VariableConstant.newFile = new File(Environment.getExternalStorageDirectory() + "/" + VariableConstant.PARENT_FOLDER /*+ "/Media/Images/CropImages/"*/, takenNewImage);
                        } else {
                            VariableConstant.newFile = new File(context.getFilesDir() + "/" + VariableConstant.PARENT_FOLDER /*+ "/Media/Images/CropImages/"*/, takenNewImage);
                        }

                        InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(VariableConstant.newFile);

                        Utility.copyStream(inputStream, fileOutputStream);

                        fileOutputStream.close();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);
                        Utility.printLog("RegistrationAct in GALLERY_PIC fileOutputStream: " + fileOutputStream);
                        view.startCropImage();
                    } catch (Exception e) {
                        Utility.printLog("RegistrationAct in GALLERY_PIC Error while creating newfile:" + e);
                    }
                    break;

                case CROP_IMAGE:
                    fileName = "";
                    String path = data.getStringExtra(CropImage.IMAGE_PATH);
                    if (path == null) {
                        Utility.printLog("RegistrationAct CROP_IMAGE file path is null: " + VariableConstant.newFile.getPath());

                        return;
                    } else {

                        VariableConstant.newProfileImageUri = Uri.fromFile(VariableConstant.newFile);

                        fileName = VariableConstant.newFile.getName();

                        try {
                            String[] type = fileName.split("\\.");

                            byte[] bytes = new byte[(int) VariableConstant.newFile.length()];
                            InputStream inputStream = context.getContentResolver().openInputStream(VariableConstant.newProfileImageUri);
                            if (inputStream != null) {
                                inputStream.read(bytes);
                            }

                            Bitmap bMap = BitmapFactory.decodeFile(path);
                            Bitmap circle_bMap = Utility.getCircleCroppedBitmap(bMap);
                            view.setProfileImage(circle_bMap);
                            amzonUpload(new File(path));

                        } catch (Exception e) {
                            Utility.printLog("RegistrationAct in CROP_IMAGE exception while copying file = " + e.toString());
                        }
                    }
                    break;

                default:

                    Toast.makeText(context, context.getResources().getString(R.string.oops)
                            + " " + context.getResources().getString(R.string.smthWentWrong), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    public void amzonUpload(File file) {
        String BUCKETSUBFOLDER = VariableConstant.PROFILE_PIC;
        Log.d(TAG, "amzonUpload: " + file);

        amazonS3.Upload_data(VariableConstant.BUCKET_NAME, BUCKETSUBFOLDER + file.getName(), file, new Upload_file_AmazonS3.Upload_CallBack() {
            @Override
            public void sucess(String url) {

                Log.d(TAG, "sucess: u" + url);
                updateProfilePic(url);
            }

            @Override
            public void sucess(String url, String type) {
                Log.d(TAG, "sucess: u" + url);
                updateProfilePic(url);
            }

            @Override
            public void error(String errormsg) {
                Log.d(TAG, "error: u" + errormsg);
            }
        });
    }

    void updateProfilePic(final String url)
    {
        view.showProgress();
        final Observable<Response<ResponseBody>> profile=networkService.updateProfile(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken(),
                null,
                null,
                null,
                null,
                null,
                url
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
                                preferenceHelperDataSource.setProfilePic(url);
//                                getProfileDetails();

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

}
