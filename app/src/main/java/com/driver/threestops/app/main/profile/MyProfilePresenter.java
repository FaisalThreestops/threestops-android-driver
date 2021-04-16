package com.driver.threestops.app.main.profile;


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

import com.driver.threestops.app.MyApplication;
import com.driver.threestops.login.LoginActivity;
import com.driver.threestops.login.language.LanguagesList;
import com.driver.threestops.login.language.LanguagesPojo;
import com.driver.threestops.logout.LogoutPopup;
import com.driver.threestops.networking.LanguageApiService;
import com.driver.threestops.service.LocationUpdateService;
import com.driver.threestops.utility.AppConstants;
import com.driver.threestops.utility.ImageUploadAPI;
 import com.driver.Threestops.BuildConfig;
import com.google.gson.Gson;
import com.driver.threestops.app.main.profile.editProfile.ChangePasswordDialog;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.Threestops.R;
import com.driver.threestops.networking.NetworkService;
import com.driver.threestops.pojo.ProfilePojo;
import com.driver.threestops.utility.ImageEditUpload;
import com.driver.threestops.utility.Upload_file_AmazonS3;
import com.driver.threestops.utility.Utility;
import com.driver.threestops.utility.VariableConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

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

    @Inject NetworkService networkService;
    @Inject LanguageApiService languageApiService;
    @Inject Activity context;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject Upload_file_AmazonS3 amazonS3;
    @Inject
    ImageUploadAPI imageUploadAPI;

    private ChangePasswordDialog changePasswordDialog;
    private String  TAG=MyProfilePresenter.class.getSimpleName();
    private String fileName,takenNewImage,state;
    private static final int CAMERA_PIC = 11, GALLERY_PIC = 12, CROP_IMAGE = 13;

    private ArrayList<LanguagesList> languagesLists;


    @Inject
    MyProfilePresenter() {
        languagesLists = new ArrayList<>();
    }

    @Override
    public void attachView(ProfileContract.ViewOperations viewOperations) {
        view=viewOperations;
    }

    @Override
    public void getProfileDetails()
    {

        if(preferenceHelperDataSource.getLanguageSettings()!=null && preferenceHelperDataSource.getLanguageSettings().getLanguageName()!=null)
            view.setLanguage(preferenceHelperDataSource.getLanguageSettings().getLanguageName(),false);

        view.showProgress();
        final Observable<Response<ResponseBody>> profile=networkService.profile(preferenceHelperDataSource.getLanguage(),((MyApplication) context.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()));
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
                            JSONObject jsonObject = null;
                            switch (value.code()){
                                //success
                                case 200:
                                    String res=value.body().string();
                                    jsonObject=new JSONObject(res);
                                    Gson gson=new Gson();
                                    ProfilePojo profilePojo = gson.fromJson(jsonObject.toString(), ProfilePojo.class);
                                    preferenceHelperDataSource.setMyName(profilePojo.getData().getName());
                                    preferenceHelperDataSource.setProfilePic(profilePojo.getData().getProfilePic());
                                    view.setProfileDetails(profilePojo.getData());
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

                        File imgFolder;
                        if (Environment.MEDIA_MOUNTED.equals(state)) {
                            imgFolder = new File(context.getExternalFilesDir(null) + "/" + VariableConstant.PARENT_FOLDER /*+ "/Media/Images/CropImages/"*/);
                        } else {
                            imgFolder = new File(context.getFilesDir() + "/" + VariableConstant.PARENT_FOLDER /*+ "/Media/Images/CropImages/"*/);
                        }

                        if (!imgFolder.exists())
                            imgFolder.mkdirs();

                        VariableConstant.newFile = new File(imgFolder, takenNewImage);

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

    /**
     * <h2>amzonUpload</h2>
     * <p>updating the image to amazon</p>
     * @param file image file
     */
    private void amzonUpload(File file) {
        String BUCKETSUBFOLDER = VariableConstant.PROFILE_PIC;
        Log.d(TAG, "amzonUpload: " + file);

        amazonS3.Upload_data(BuildConfig.BUCKET_NAME, BUCKETSUBFOLDER + file.getName(), file, new Upload_file_AmazonS3.Upload_CallBack() {
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

    /**
     * <h2>updateProfilePic</h2>
     * <p>API call for updating the profile picture</p>
     * @param url : url
     */
    private void updateProfilePic(final String url)
    {
        view.showProgress();
        final Observable<Response<ResponseBody>> profile=networkService.updateProfile(
                preferenceHelperDataSource.getLanguage(),
                ((MyApplication) context.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()),
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


    @Override
    public void getLanguages() {
        if(Utility.isNetworkAvailable(context)) {
            view.showProgress();

            Observable<Response<ResponseBody>> getLanguages = languageApiService.getLanguage();

            getLanguages.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Response<ResponseBody>>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            view.hideProgress();

                            try {
                                switch (value.code()) {
                                    case VariableConstant.RESPONSE_CODE_SUCCESS:
                                        String res = value.body().string();
                                        Utility.printLog("language res : "+res);

                                        Gson gson = new Gson();
                                        LanguagesPojo languagesListModel = gson.fromJson(res,LanguagesPojo.class);
                                        Utility.printLog("language res : "+languagesListModel.getData().size());
                                        languagesLists.clear();
                                        languagesLists.addAll(languagesListModel.getData());
                                        boolean isLanguage = false;
                                        for(LanguagesList languagesList : languagesLists)
                                        {
                                            if(preferenceHelperDataSource.getLanguageSettings().getLanguageCode().equals(languagesList.getLanguageCode()))
                                            {
                                                isLanguage = true;
                                                view.setLanguageDialog( languagesListModel.getData(),languagesLists.indexOf(languagesList));
                                                break;
                                            }
                                        }
                                        if(!isLanguage)
                                            view.setLanguageDialog(null,-1);

                                        break;

                                    default:
                                        /*loginView.showError();*/
                                        break;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Utility.printLog("getLanguages : Catch :" + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.hideProgress();
                            Utility.printLog("getLanguages : onError :" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {
            view.onError(context.getResources().getString(R.string.no_network));
        }

    }

}
