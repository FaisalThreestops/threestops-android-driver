package com.delivx.signup.vehicle;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delivx.ForgotPassword.ForgotPasswordVerify;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.pojo.VehMakeData;
import com.delivx.pojo.VehTypeData;
import com.delivx.pojo.VehicleMakeModel;
import com.delivx.signup.GenericListActivity;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import eu.janmuller.android.simplecropimage.CropImage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**************************************************************************************************/
public class SignupVehicle extends DaggerAppCompatActivity implements SignupVehicleView,View.OnClickListener {

    private static final int SELECT_AN_TYPE = 405;
    private static final int SELECT_AN_Make = 407;
    private static final int SELECT_AN_MODEL = 408;
    private static final int CROP_IMAGE = 13;

    private String  vehicle_pic = "vehicle_pic", licence_pic = "add_licence", carriage_permit = "carriage_permit", reg_certificate = "reg_certificate";

    public static String ImageType;

    @BindView(R.id.ll_add_insurance) RelativeLayout ll_add_insurance;
    @BindView(R.id.ll_carriage_permit) RelativeLayout ll_carriage_permit;
    @BindView(R.id.ll_reg_certificate) RelativeLayout ll_reg_certificate;
    @BindView(R.id.tv_camera_up1) TextView tv_camera_up;
    @BindView(R.id.tv_vechicle) TextView tv_vechicle;
    @BindView(R.id.tv_header_certificate) TextView tv_header_certificate ;
    @BindView(R.id.tv_header_insurance) TextView tv_header_insurance;
    @BindView(R.id.tv_header_registration) TextView tv_header_registration;
    @BindView(R.id.et_plate_no)EditText et_plate_no;
    @BindView(R.id.et_color)EditText et_color;
    @BindView(R.id.tv_type_head) TextView tv_type_head;
    @BindView(R.id.tv_type) TextView tv_type;
    @BindView(R.id.tv_sepecialities_head) TextView tv_sepecialities_head;
    @BindView(R.id.tv_sepecialities) TextView tv_sepecialities;
    @BindView(R.id.tv_make_head) TextView tv_make_head ;
    @BindView(R.id.tv_make) TextView tv_make ;
    @BindView(R.id.tv_model_head) TextView tv_model_head ;
    @BindView(R.id.tv_model) TextView tv_model ;
    @BindView(R.id.tv_finish) TextView tv_finish ;
    @BindView(R.id.iv_camera_uploader1) ImageView iv_insurance ;
    @BindView(R.id.iv_signup_vp) ImageView iv_signup_vp ;
    @BindView(R.id.iv_camera_uploader2) ImageView iv_carriage_permit ;
    @BindView(R.id.iv_camera_uploader3) ImageView iv_reg_cert ;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.llOutSide) LinearLayout llOutSide;


    @Inject
    SignupPresenterContract presenter;

    @Inject
    PreferenceHelperDataSource preferenceHelper;

    @Inject
    FontUtils fontUtils;

    private Typeface ClanaproNarrMedium,ClanaproNarrNews;


    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_vehicle);
        overridePendingTransition(R.anim.bottem_slide_down, R.anim.stay_activity);
        ButterKnife.bind(this);


        presenter.attachView(this);
        presenter.getBundleData(getIntent().getExtras());

        initializeViews();

        presenter.setActionBar();
        presenter.setActionBarTitle();

    }


    @Override
    public void initActionBar() {

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_signup_close);
        }
        iv_search.setVisibility(View.GONE);

    }

    @Override
    public void setTitle() {
        tv_title.setText(getResources().getString(R.string.signup_));
        tv_title.setTypeface(ClanaproNarrMedium);
    }

    @Override
    public void moveToVehicleList(ArrayList<VehTypeData> data) {
        Intent intent = new Intent(SignupVehicle.this, GenericListActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("DATA",data);
        mBundle.putSerializable("TYPE", "VEHICLE_TYPE");
        mBundle.putString("TITLE", getResources().getString(R.string.err_type));
        intent.putExtras(mBundle);
        startActivityForResult(intent, SELECT_AN_TYPE);
    }

    @Override
    public void moveToVehicleMakeList(ArrayList<VehMakeData> data) {
            Bundle mBundle=new Bundle();
            Intent intent=new Intent(this,GenericListActivity.class);
            mBundle.putSerializable("DATA", data);
            mBundle.putSerializable("TYPE", "VEHICLE_MAKE");
            mBundle.putString("TITLE", getResources().getString(R.string.err_make));
            intent.putExtras(mBundle);
            startActivityForResult(intent, SELECT_AN_Make);
    }

    @Override
    public void moveToVehicleModelList(ArrayList<VehicleMakeModel> data) {
        Bundle mBundle=new Bundle();
        Intent intent=new Intent(this,GenericListActivity.class);
        mBundle.putString("TITLE", getResources().getString(R.string.err_model));
        mBundle.putString("TYPE", "VEHICLE_MODEL");
        mBundle.putSerializable("DATA", data);
        intent.putExtras(mBundle);
        startActivityForResult(intent, SELECT_AN_MODEL);

    }

    @Override
    public void uploadVehicleError() {
        iv_signup_vp.setImageResource(R.drawable.signup_vechicle_default_image);
    }

    @Override
    public void uploadCarriageError() {
        iv_carriage_permit.setVisibility(View.GONE);
    }

    @Override
    public void uploadRegistrationError() {
        iv_reg_cert.setVisibility(View.GONE);
    }

    @Override
    public void uploadInsuranceError() {
        iv_insurance.setVisibility(View.GONE);
    }

    @Override
    public void setTypeText(String type) {
        tv_type.setText(type);
    }

    @Override
    public void setMakeText(String make) {
        tv_make.setText(make);
    }

    @Override
    public void setModelText(String model) {
        tv_model.setText(model);
    }

    @Override
    public void setVehicleImage(Bitmap bitmap) {
        iv_signup_vp.setImageBitmap(bitmap);
    }

    @Override
    public void setRegistrationImage(Bitmap bitmap) {
        iv_reg_cert.setVisibility(View.VISIBLE);
        bitmap = Bitmap.createScaledBitmap(bitmap, iv_reg_cert.getWidth(), iv_reg_cert.getHeight(), true);
        iv_reg_cert.setImageBitmap(bitmap);
    }

    @Override
    public void setcarriagePermitImage(Bitmap bitmap) {
        iv_carriage_permit.setVisibility(View.VISIBLE);
        bitmap = Bitmap.createScaledBitmap(bitmap, iv_carriage_permit.getWidth(), iv_carriage_permit.getHeight(), true);
        iv_carriage_permit.setImageBitmap(bitmap);
    }

    @Override
    public void setInsuranceImage(Bitmap bitmap) {
        iv_insurance.setVisibility(View.VISIBLE);
        bitmap = Bitmap.createScaledBitmap(bitmap, iv_insurance.getWidth(), iv_insurance.getHeight(), true);
        iv_insurance.setImageBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.onOutSideTouch();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay_activity, R.anim.bottem_slide_up);
    }


    private void initializeViews() {
        ClanaproNarrMedium = fontUtils.titaliumSemiBold();
        ClanaproNarrNews = fontUtils.titaliumRegular();
        tv_header_registration.setTypeface(ClanaproNarrNews);
        tv_header_insurance.setTypeface(ClanaproNarrNews);
        tv_header_certificate.setTypeface(ClanaproNarrNews);
        tv_vechicle.setTypeface(ClanaproNarrNews);
        tv_camera_up.setTypeface(ClanaproNarrNews);
        et_plate_no.setTypeface(ClanaproNarrNews);
        et_color.setTypeface(ClanaproNarrNews);
        tv_type_head.setTypeface(ClanaproNarrNews);
        tv_type.setTypeface(ClanaproNarrNews);
        tv_sepecialities_head.setTypeface(ClanaproNarrNews);
        tv_sepecialities.setTypeface(ClanaproNarrNews);
        tv_make_head.setTypeface(ClanaproNarrNews);
        tv_make.setTypeface(ClanaproNarrNews);
        tv_model_head.setTypeface(ClanaproNarrNews);
        tv_model.setTypeface(ClanaproNarrNews);
        tv_finish.setTypeface(ClanaproNarrMedium);

    }

    @OnClick({R.id.llOutSide,R.id.iv_signup_vp,R.id.ll_add_insurance,R.id.ll_carriage_permit,R.id.ll_reg_certificate,R.id.tv_finish,R.id.tv_type,R.id.tv_sepecialities,R.id.tv_make,R.id.tv_model})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_signup_vp:
                presenter.uploadImage(ImageType = vehicle_pic);
                break;

            case R.id.ll_add_insurance:;
                presenter.uploadImage(ImageType = licence_pic);
                break;

            case R.id.ll_carriage_permit:
                presenter.uploadImage( ImageType = carriage_permit);
                break;

            case R.id.ll_reg_certificate:;
                presenter.uploadImage(ImageType = reg_certificate);
                break;

            case R.id.tv_finish:
                methodRequiresOnePermission();
                if (preferenceHelper.getDeviceId() != null)
                    presenter.validateFields(et_plate_no.getText().toString(),
                            tv_type.getText().toString(),tv_make.getText().toString(),
                            tv_model.getText().toString());
                break;

            case R.id.llOutSide:
                presenter.onOutSideTouch();
                break;

            case R.id.tv_type:
                presenter.getTypeList();
                break;

            case R.id.tv_sepecialities:
                break;

            case R.id.tv_make:
                presenter.getVehicleMakeList();
                break;

            case R.id.tv_model:
                presenter.getModelList();
                break;

        }

    }


    @AfterPermissionGranted(VariableConstant.RC_READ_PHONE_STATE)
    private void methodRequiresOnePermission() {
        String[] perms = {Manifest.permission.READ_PHONE_STATE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission
            preferenceHelper.setDeviceId(Utility.getDeviceId(this));
        } else {
            // Do not have permissions, requesting permission
            EasyPermissions.requestPermissions(this, getString(R.string.read_phone_state_permission_message),
                    VariableConstant.RC_READ_PHONE_STATE, perms);
        }
    }

    /**********************************************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        presenter.onActivityResult(requestCode,resultCode,data);
    }


    @Override
    public void startCropImage() {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, VariableConstant.newFile.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);
        startActivityForResult(intent, SignupVehicle.CROP_IMAGE);
    }

    @Override
    public void moveToVerifyAct(Bundle bundle) {
        Intent mIntent = new Intent(SignupVehicle.this, ForgotPasswordVerify.class);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
    }


    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

}
