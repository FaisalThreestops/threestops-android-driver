package com.delivx.ForgotPassword;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.delivx.ForgotPassword.VerifyMobile.ForgotPasswordVerifyView;
import com.delivx.ForgotPassword.VerifyMobile.VerifyMobilePresenterContract;
import com.delivx.app.main.profile.editProfile.EditProfileActivity;
import com.driver.delivx.R;
import com.delivx.login.LoginActivity;
import com.delivx.utility.AutoReadSms;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;


public class ForgotPasswordVerify extends DaggerAppCompatActivity implements View.OnClickListener,ForgotPasswordVerifyView {

    private Typeface ClanaproNarrMedium,ClanaproNarrNews;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_verify_txt) TextView tv_verify_txt;
    @BindView(R.id.tv_verify_msg) TextView tv_verify_msg;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_resendcode) TextView tv_resendcode;
    @BindView(R.id.tvTimer) TextView tvTimer;
    @BindView(R.id.et_otp1) EditText et_otp1;
    @BindView(R.id.et_otp2) EditText  et_otp2;
    @BindView(R.id.et_otp3) EditText  et_otp3;
    @BindView(R.id.et_otp4) EditText  et_otp4;
    @BindView(R.id.ll_first) LinearLayout ll_first;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    @BindString(R.string.verify_msg1) String msg1;
    @BindString(R.string.verify_msg2) String msg2;

    @Inject VerifyMobilePresenterContract presenter;
    @Inject FontUtils fontUtils;
    private AutoReadSms autoReadSms;


    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.activity_forgot_password_verify);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        ButterKnife.bind(this);

        presenter.getBundleData(getIntent().getExtras());

        initializeViews();

        presenter.setActionBar();

        presenter.startTimer(60);

        autoReadSms = new AutoReadSms(){
            @Override
            protected void onSmsReceived(String str) {

               presenter.onSmsReceived(str);

            }
        };
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(1000);
        this.registerReceiver(autoReadSms, intentFilter);
    }


   @Override
    public void initActionBar() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white_btn);
        }

        tv_title.setTypeface(ClanaproNarrMedium);

        iv_search.setVisibility(View.GONE);
        presenter.setActionBarTitle();

    }

    @Override
    public void setTitle() {
        tv_title.setText(getResources().getString(R.string.verify_mob));
    }

    @Override
    public void setVerifyMessage(String msg) {
        tv_verify_msg.setText(msg);
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * <h2>initializeViews</h2>
     * <p>initializing the views</p>
     */
    private void initializeViews() {
        ClanaproNarrMedium =fontUtils.titaliumSemiBold() ;
        ClanaproNarrNews = fontUtils.titaliumRegular();
        tv_verify_txt.setTypeface(ClanaproNarrMedium);
        tv_verify_msg.setTypeface(ClanaproNarrNews);
        tvTimer.setTypeface(ClanaproNarrNews);
        presenter.getVerifyMessage(msg1,msg2);
        tv_resendcode.setTypeface(ClanaproNarrNews);

        et_otp1.setTypeface(ClanaproNarrMedium);
        et_otp1.requestFocus();
        et_otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                et_otp1.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    et_otp2.requestFocus();
                } else {
                    et_otp1.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_otp2.setTypeface(ClanaproNarrMedium);
        et_otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    et_otp3.requestFocus();
                } else {
                    et_otp1.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_otp3.setTypeface(ClanaproNarrMedium);
        et_otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    et_otp4.requestFocus();
                } else {
                    et_otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_otp4.setTypeface(ClanaproNarrMedium);
        et_otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    et_otp4.requestFocus();

                    presenter.validateOtp(et_otp1.getText().toString(),et_otp2.getText().toString(),et_otp3.getText().toString(),et_otp4.getText().toString());
                } else {
                    et_otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                Utility.hideSoftKeyboard(ForgotPasswordVerify.this);
//                OTPValidation();
            }
        });




        ll_first.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                presenter.onOutSideTouch();
                return false;
            }
        });

    }

    /**********************************************************************************************/
    @OnClick({R.id.tv_verify_txt,R.id.tv_resendcode})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_verify_txt:
                presenter.validateOtp(et_otp1.getText().toString(),et_otp2.getText().toString(),
                        et_otp3.getText().toString(),et_otp4.getText().toString());
                break;

            case R.id.tv_resendcode:
                presenter.sendOtpApi();
                break;

        }
    }

    @Override
    public void enableResendButton() {
        tv_resendcode.setTextColor(getResources().getColor(R.color.black1));
        tv_resendcode.setEnabled(true);
    }

    @Override
    public void disableResendButton() {
        tv_resendcode.setEnabled(false);
        tv_resendcode.setTextColor(getResources().getColor(R.color.gray_heading));
    }

    @Override
    public void setTimerText(String text) {
        tvTimer.setText(text);
    }

    @Override
    public void showError(String msg) {
        Utility.mShowMessage(getResources().getString(R.string.message),msg, ForgotPasswordVerify.this);
        et_otp1.setText("");
        et_otp2.setText("");
        et_otp3.setText("");
        et_otp4.setText("");
        et_otp1.requestFocus();
    }

    @Override
    public void startNextActivity(String otp, String mob, String cCode) {
        Intent intent = new Intent(ForgotPasswordVerify.this, ForgotPasswordChangePass.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("otp", otp);
        mBundle.putString("phone", mob);
        mBundle.putString("countryCode", cCode);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public void mSetResult() {
        Intent intent = new Intent(ForgotPasswordVerify.this, EditProfileActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void startLoginAct() {
        Intent intent = new Intent(ForgotPasswordVerify.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle mBundle = new Bundle();
        mBundle.putString("success_msg", getResources().getString(R.string.signup_success_msg));
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public void setOtp(String otp) {
        et_otp1.setText(String.valueOf(otp.charAt(0)));
        et_otp2.setText(String.valueOf(otp.charAt(1)));
        et_otp3.setText(String.valueOf(otp.charAt(2)));
        et_otp4.setText(String.valueOf(otp.charAt(3)));
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
    protected void onDestroy() {
        super.onDestroy();
        try{
            unregisterReceiver(autoReadSms);
        }catch (Exception e){
        }
    }
}
