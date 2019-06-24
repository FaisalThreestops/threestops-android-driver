package com.delivx.signup.perosonal;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delivx.ForgotPassword.ForgotPasswordVerify;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.pojo.Cities;
import com.delivx.pojo.Zone;
import com.delivx.signup.GenericListActivity;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTouch;
import dagger.android.support.DaggerAppCompatActivity;
import eu.janmuller.android.simplecropimage.CropImage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SignupPersonal extends DaggerAppCompatActivity implements View.OnClickListener,PersonalView,View.OnFocusChangeListener ,View.OnTouchListener{

    private final String TAG = SignupPersonal.class.getSimpleName();
    private static final int SELECT_A_CITY = 409,SELECT_A_ZONE = 410, CROP_IMAGE = 13;

    public static String ImageType = "";
    private String  profile_pic = "profile_pic", licence_pic = "add_licence",licence_pic_back="license_back";

    private Typeface ClanaproNarrMedium;
    private Typeface ClanaproNarrNews;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rg_signup_work_group) RadioGroup rg_WorkGroup;
    @BindView(R.id.tv_personal) TextView tv_personal;
    @BindView(R.id.tv_city) TextView tv_zones;
    @BindView(R.id.tv_camera_up) TextView tv_camera_up;
    @BindView(R.id.tv_camera_up2) TextView tv_camera_up2;
    @BindView(R.id.tv_signup_city)TextView tv_signup_city;
    @BindView(R.id.tv_signup_zone)TextView tv_signup_zone;
    @BindView(R.id.tv_licence)TextView tv_licence;
    @BindView(R.id.tvDobHeader)TextView tvDobHeader;
    @BindView(R.id.tvDob)TextView tvDob;
    @BindView(R.id.et_fname)EditText et_fname;
    @BindView(R.id.et_lname)EditText et_lname;
    @BindView(R.id.et_license_no)EditText et_license_no;
    @BindView(R.id.et_signup_mob)EditText et_signup_mob;
    @BindView(R.id.et_email)EditText et_email;
    @BindView(R.id.et_referral)EditText et_referral;
    @BindView(R.id.et_password)EditText et_password;
    @BindView(R.id.tv_next)TextView tv_next;
    @BindView(R.id.iv_camera_uploader3)ImageView iv_camera_uploader3;
    @BindView(R.id.iv_camera_uploader2)ImageView iv_camera_uploader2;
    @BindView(R.id.iv_signup_pp)ImageView iv_signup_pp;
    @BindView(R.id.ll_add_licence)RelativeLayout ll_add_licence;
    @BindView(R.id.ll_add_licenc2)RelativeLayout ll_add_licence2;
    @BindView(R.id.countryPicker) RelativeLayout countryPicker;
    @BindView(R.id.code) TextView countryCode;
    @BindView(R.id.tvExpiryDate) TextView tvExpiryDate;
    @BindView(R.id.tvExpiryDateHeader) TextView tvExpiryDateHeader;
    @BindView(R.id.flag) ImageView flag;
    @BindView(R.id.ll_layout) LinearLayout ll_layout ;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @BindString(R.string.message)String title;

    @Inject FontUtils fontUtils;
    @Inject PersonalPresenterContract presenter;
    @Inject PreferenceHelperDataSource preferenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,preferenceHelper.getLanguageSettings().getLanguageCode());
        setContentView(R.layout.activity_signup_personal);
        //Animation for opening the Sign in page
        overridePendingTransition(R.anim.bottem_slide_down, R.anim.stay_activity);
        ButterKnife.bind(this);
        initializeViews();
        presenter.setActionBar();
        presenter.setActionBarTitle();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

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
    public void setCountryFlag(int drawableID, String dialCOde, int minLength, int maxLength) {
        flag.setBackgroundResource(drawableID);
        countryCode.setText(dialCOde);
    }

    @Override
    public void setMaxLength(int length) {

    }

    @Override
    public void onEmailError(String msg) {
        tv_next.setEnabled(true);
        Utility.mShowMessage(title, msg, SignupPersonal.this);

    }

    @Override
    public void onPhoneError(String msg) {
        tv_next.setEnabled(true);
        Utility.mShowMessage(title, msg, SignupPersonal.this);
    }

    @Override
    public void onFirstNameError(String msg) {
        tv_next.setEnabled(true);
        Utility.mShowMessage(title, msg, SignupPersonal.this);
        et_fname.requestFocus();
    }

    @Override
    public void onPasswordError(String msg) {
        tv_next.setEnabled(true);
        Utility.mShowMessage(title, msg, SignupPersonal.this);
        et_password.requestFocus();
    }

    @Override
    public void moveToVehicleDetails(Bundle bundle)
    {
        Intent intent=new Intent(this, ForgotPasswordVerify.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void moveToZoneList(ArrayList<Cities> cities) {

        Intent intent = new Intent(SignupPersonal.this, GenericListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("TITLE", getResources().getString(R.string.select_city));
        bundle.putSerializable("DATA", cities);
        bundle.putSerializable("TYPE", "ZONE");
        intent.putExtras(bundle);
        startActivityForResult(intent, SELECT_A_CITY);
    }

    @Override
    public void sendZones(ArrayList<Zone> zone)
    {
        Intent intent = new Intent(SignupPersonal.this, GenericListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("TITLE", getResources().getString(R.string.select_zone));
        bundle.putSerializable("DATA", zone);
        bundle.putSerializable("TYPE", "ZONE1");
        intent.putExtras(bundle);
        startActivityForResult(intent, SELECT_A_ZONE);
    }

    @Override
    public void setCityTextView(String zone) {
        tv_signup_city.setText(zone);
    }

    @Override
    public void setZoneTextView(String zone) {
        tv_signup_zone.setText(zone);
    }

    /**
     * <h1>initializeViews</h1>
     * <p>this is the method, for initialize the views</p>
     * <pre>setting the text style </pre>
     */
    private void initializeViews()
    {
        ClanaproNarrMedium = fontUtils.titaliumSemiBold();
        ClanaproNarrNews = fontUtils.titaliumRegular();

        tv_personal.setTypeface(ClanaproNarrNews);
        tv_zones.setTypeface(ClanaproNarrNews);
        tv_camera_up.setTypeface(ClanaproNarrNews);
        tv_camera_up2.setTypeface(ClanaproNarrNews);
        tv_signup_city.setTypeface(ClanaproNarrNews);
        tv_licence.setTypeface(ClanaproNarrNews);
        et_fname.setTypeface(ClanaproNarrNews);
        et_lname.setTypeface(ClanaproNarrNews);
        et_signup_mob.setTypeface(ClanaproNarrNews);
        et_email.setTypeface(ClanaproNarrNews);
        et_referral.setTypeface(ClanaproNarrNews);
        et_password.setTypeface(ClanaproNarrNews);
        tvExpiryDateHeader.setTypeface(ClanaproNarrNews);
        tvExpiryDate.setTypeface(ClanaproNarrNews);
        tvDob.setTypeface(ClanaproNarrNews);
        tvDobHeader.setTypeface(ClanaproNarrNews);
        et_license_no.setTypeface(ClanaproNarrNews);
        tv_next.setTypeface(ClanaproNarrMedium);

        presenter.getCountryCode();

    }


    @OnFocusChange({R.id.et_password,R.id.et_referral,R.id.et_signup_mob,R.id.et_email})
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId())
        {
            //referral focus check
            case R.id.et_referral:
                if(!hasFocus && !et_referral.getText().toString().isEmpty())
                    presenter.validateReferralCode(et_referral.getText().toString());
                break;

            //phone number check
            case R.id.et_signup_mob:
                //if we enter the number then it will entered to this condition for validation
                if(!hasFocus && !et_signup_mob.getText().toString().isEmpty())
                    presenter.validatePhone(countryCode.getText().toString(),et_signup_mob.getText().toString());
                break;

            //email validate
            case R.id.et_email:
                if(!hasFocus && !et_email.getText().toString().isEmpty())
                    presenter.validateEmail(et_email.getText().toString());
                break;

                //password validate
            case R.id.et_password:
                if (!hasFocus && !et_password.getText().toString().isEmpty() && tv_signup_city.getText().toString().isEmpty())
                    tv_signup_city.callOnClick();
                break;
        }
    }

    @OnClick({R.id.tvExpiryDate,R.id.ll_add_licence,
            R.id.ll_add_licenc2,R.id.tv_next,R.id.countryPicker,
            R.id.iv_signup_pp,R.id.tv_signup_city,R.id.tvDob,R.id.tv_signup_zone})
    public void onClick(View v)
    {
        switch (v.getId()) {
            //event for confirm sign up
            case R.id.tv_next:
                tv_next.setEnabled(false);
                methodRequiresOnePermission();
                //validating the all fields in signUp
                if (preferenceHelper.getDeviceId() != null)
                    presenter.validateFields(et_fname.getText().toString(),
                        et_signup_mob.getText().toString(),
                        et_email.getText().toString(),
                        et_password.getText().toString(),
                        et_lname.getText().toString(),
                        countryCode.getText().toString(),
                        et_referral.getText().toString(),
                        tv_signup_city.getText().toString(),
                        tvExpiryDate.getText().toString(),
                        et_license_no.getText().toString(),
                        tvDob.getText().toString()
                            );
                break;

            //event for country
            case R.id.countryPicker:
                presenter.showDialogForCountryPicker();
                break;

            //event for profile image
            case R.id.iv_signup_pp:
                presenter.profileOnclick();
                ImageType = profile_pic;
                break;

            //event for city
            case R.id.tv_signup_city:
                presenter.getCity();
                break;
            //event for Zone
            case R.id.tv_signup_zone:
                if(!"".equals(presenter.getCityId()) &&presenter.getCityId()!=null)
                    presenter.getZonesOnCity(presenter.getCityId());
                else
                    Toast.makeText(this, " Please select City to Continue", Toast.LENGTH_SHORT).show();
                break;
            //event for licence expiry date
            case R.id.tvExpiryDate:
                presenter.openCalender(1);
                break;

            //event for DOB
            case R.id.tvDob:
                presenter.openCalender(2);
                break;

            //event for licence front image
            case R.id.ll_add_licence:
                presenter.licenseOnclik();
                ImageType = licence_pic;
                break;

            //event for licence back image
            case R.id.ll_add_licenc2:
                presenter.licenseOnclik();
                ImageType = licence_pic_back;
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utility.printLog(TAG + " onActivityResult    requestCode " + requestCode + " resultCode " + resultCode);
        presenter.onActivityResult( requestCode,  resultCode,  data);

    }

    @Override
    public void startCropImage() {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, VariableConstant.newFile.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);
        intent.putExtra("locale", Locale.getDefault().getDisplayLanguage());
        startActivityForResult(intent, SignupPersonal.CROP_IMAGE);
    }

    @Override
    public void setProfileImage(Bitmap bitmap) {
        iv_signup_pp.setImageBitmap(bitmap);
    }

    @Override
    public void setLicenseImage(Bitmap bMap) {
        iv_camera_uploader3.setVisibility(View.VISIBLE);
        bMap = Bitmap.createScaledBitmap(bMap, iv_camera_uploader3.getWidth(), iv_camera_uploader3.getHeight(), true);
        iv_camera_uploader3.setImageBitmap(bMap);
    }

    @Override
    public void setLicenseBackImage(Bitmap bMap) {
        iv_camera_uploader2.setVisibility(View.VISIBLE);
        bMap = Bitmap.createScaledBitmap(bMap, iv_camera_uploader2.getWidth(), iv_camera_uploader2.getHeight(), true);
        iv_camera_uploader2.setImageBitmap(bMap);
    }

    @Override
    public void onError(String error) {
        tv_next.setEnabled(true);
        Utility.mShowMessage(title, error, SignupPersonal.this);
    }

    @Override
    public void uploadProfileError() {
        tv_next.setEnabled(true);
        iv_signup_pp.setImageResource(R.drawable.signup_profile_default_image);
    }

    @Override
    public void uploadLicenseError() {
        tv_next.setEnabled(true);
        iv_camera_uploader3.setVisibility(View.GONE);
    }

    @Override
    public String getCountryCode() {
        return countryCode.getText().toString();
    }

    @Override
    public void enableLogin() {
        tv_next.setEnabled(true);
    }

    @Override
    public void setExpiryDate(String format) {
        tvExpiryDate.setText(format);
    }

    @Override
    public void setBirthDate(String format) {
        tvDob.setText(format);
    }

    @Override
    public void uploadLicenseBackError() {
        tv_next.setEnabled(true);
        iv_camera_uploader2.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay_activity, R.anim.bottem_slide_up);
    }


    @Override
    protected void onResume() {
        super.onResume();
        tv_next.setEnabled(true);
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

    @OnTouch(R.id.ll_layout)
    public boolean onTouch(View v, MotionEvent event) {
        presenter.onOutSideTouch();
        return false;
    }

    //after giving the permission
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
}
