package com.delivx.app.main.profile.editProfile;

import android.content.Intent;
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
import com.driver.delivx.R;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class EditProfileActivity extends DaggerAppCompatActivity implements View.OnClickListener,EditProfileContract.ViewOperations {

    private static final String TAG = "EditPhoneNumber";
    private final int PHONE_VALIDATION_REQUEST_CODE = 700;

    @BindView(R.id.flag) ImageView flag;
    @BindView(R.id.toolbar) Toolbar toolbar ;
    @BindView(R.id.llPhone) LinearLayout llPhone;
    @BindView(R.id.llName) LinearLayout llName ;
    @BindView(R.id.ll_password) LinearLayout ll_password;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.code) TextView countryCode;
    @BindView(R.id.tv_editphone_msg) TextView tv_editphone_msg;
    @BindView(R.id.tv_phone_num) TextView tv_phone_num;
    @BindView(R.id.tv_save) TextView tv_save;
    @BindView(R.id.et_phone_num) EditText et_phone_num;
    @BindView(R.id.etName) EditText etName;
    @BindView(R.id.et_newpass) EditText et_newpass;
    @BindView(R.id.et_re_newpass) EditText et_re_newpass;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.countryPicker) RelativeLayout countryPicker;
    @BindView(R.id.rlOutSide) RelativeLayout rlOutSide;
    @Inject
    FontUtils fontUtils;
    @Inject
    EditProfileContract.PresenterOperations presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.activity_edit_phone_number);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);

        presenter.getBundleData(getIntent());
        initializeViews();
    }


    private void initializeViews() {

        Typeface ClanaproNarrMedium =fontUtils.titaliumSemiBold();
        Typeface ClanaproNarrNews = fontUtils.titaliumRegular();

        presenter.setActionBar();
        presenter.setActionBarTitle();

        tv_title.setTypeface(ClanaproNarrMedium);
        tv_editphone_msg.setTypeface(ClanaproNarrNews);
        tv_phone_num.setTypeface(ClanaproNarrNews);
        tv_save.setTypeface(ClanaproNarrMedium);
        et_phone_num.setTypeface(ClanaproNarrNews);
        etName.setTypeface(ClanaproNarrNews);
        et_newpass.setTypeface(ClanaproNarrNews);
        et_re_newpass.setTypeface(ClanaproNarrNews);
        countryCode.setTypeface(ClanaproNarrNews);

        /*presenter.getCountryCode();*/
    }

    @Override
    public void setCounryFlag(int drawableID, String dialCOde, int minLength, int maxLength) {
        flag.setBackgroundResource(drawableID);
        countryCode.setText(dialCOde);
    }

    @Override
    public void setMaxLength(int length) {
    }

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white_btn);
        }
    }

    @Override
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    @Override
    public void enablePhoneEdit() {
        llPhone.setVisibility(View.VISIBLE);
        tv_save.setText(getResources().getString(R.string.next));
    }

    @Override
    public void enableNameEdit() {
        llName.setVisibility(View.VISIBLE);
    }

    @Override
    public void enablePasswordEdit() {
        ll_password.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @OnClick({R.id.tv_save,R.id.countryPicker,R.id.rlOutSide})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_save:
                presenter.updateProfileDetails();
                break;

            case R.id.countryPicker:
                presenter.showDialogForCountryPicker();
                break;

            case R.id.rlOutSide:
                presenter.onOutSideTouch();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHONE_VALIDATION_REQUEST_CODE:
                    presenter.onActivityResult();
                    break;
            }
        }

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

    @Override
    public void onError(String message) {
        Utility.showAlert(message,this);
    }

    @Override
    public void getPhoneNumber() {
     presenter.setPhoneNumber(et_phone_num.getText().toString(),countryCode.getText().toString());
    }

    @Override
    public void getName() {
        presenter.setName(etName.getText().toString());
    }

    @Override
    public void moveToVerifyAct(Bundle bundle) {
        Intent mIntent = new Intent(EditProfileActivity.this, ForgotPasswordVerify.class);
        mIntent.putExtras(bundle);
        startActivityForResult(mIntent, PHONE_VALIDATION_REQUEST_CODE);
    }

    @Override
    public void getPassword() {
        presenter.setPassword(et_newpass.getText().toString(),et_re_newpass.getText().toString());
    }

    @Override
    public void finishActivity() {
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
