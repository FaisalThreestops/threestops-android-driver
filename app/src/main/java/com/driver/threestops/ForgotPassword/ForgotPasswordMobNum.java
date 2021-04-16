package com.driver.threestops.ForgotPassword;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.driver.threestops.ForgotPassword.RetriveFromMobile.ForgotPassMobNumView;
import com.driver.threestops.ForgotPassword.RetriveFromMobile.ForgotPassPresenterContract;

import com.driver.Threestops.R;
import com.driver.threestops.login.LoginActivity;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Utility;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class ForgotPasswordMobNum extends DaggerAppCompatActivity implements View.OnClickListener ,ForgotPassMobNumView{

    @BindView(R.id.tv_forgot_next) TextView tv_forgot_next;
    @BindView(R.id.et_forgot_mob) EditText et_forgot_mob;
    @BindView(R.id.code) TextView countryCode;
    @BindView(R.id.flag) ImageView flag;
    @BindView(R.id.ll_first) LinearLayout sv_signup;
    @BindView(R.id.tv_forgot_msg) TextView tv_forgot_msg;
    @BindView(R.id.tv_forgot_phoneno) TextView tv_forgot_phoneno;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindString(R.string.forgotPassword) String title;
    @BindView(R.id.rgForgotPass) RadioGroup rg_WorkGroup;

    @Inject ForgotPassPresenterContract presenter;
    @Inject FontUtils fontUtils;

    private int minPhoneLength=0;
    private Typeface ClanaproNarrMedium,ClanaproNarrNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.activity_forgot_password_mob_num);
        overridePendingTransition(R.anim.bottem_slide_down, R.anim.stay_activity);

        ButterKnife.bind(this);
        initializeViews();
        presenter.setActionBar();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_signup_close);
        }
        tv_title.setTypeface(ClanaproNarrMedium);
        iv_search.setVisibility(View.GONE);
        presenter.setActionBarTitle();
    }

    @Override
    public void setTitle() {
        tv_title.setText(title);
    }

    @Override
    public void setCountryFlag(int drawableID, String dialCOde, int minLength, int maxLength) {
        flag.setBackgroundResource(drawableID);
        countryCode.setText(dialCOde);
    }

    @Override
    public void editextAfterChanged() {
        //if phone number is empty
        if(et_forgot_mob.getText().toString().isEmpty()){
            tv_forgot_next.setFocusable(false);
            tv_forgot_next.setBackgroundColor(getResources().getColor(R.color.gray));
        }
        //if phone is not empty
        else {
            //if email address matches
            if (Patterns.EMAIL_ADDRESS.matcher(et_forgot_mob.getText().toString()).matches()) {
                tv_forgot_next.setFocusable(true);
                tv_forgot_next.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            }
            //if phone number matches and >=minimum length
            else if (Patterns.PHONE.matcher(et_forgot_mob.getText().toString()).matches() && et_forgot_mob.getText().toString().length()>=minPhoneLength ) {
                tv_forgot_next.setFocusable(true);
                tv_forgot_next.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            //if above condition doesn't match
            else {
                tv_forgot_next.setFocusable(false);
                tv_forgot_next.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        }
    }

    @Override
    public void onEmailSelection() {
        et_forgot_mob.setHint(getResources().getString(R.string.Email));
        et_forgot_mob.setText("");
        tv_forgot_msg.setText(getResources().getString(R.string.forgotMsgEmail));
        et_forgot_mob.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        countryCode.setVisibility(View.GONE);
    }

    @Override
    public void onMobileSelection() {
        et_forgot_mob.setText("");
        et_forgot_mob.setHint(getResources().getString(R.string.phone_number));
        tv_forgot_msg.setText(getResources().getString(R.string.forgotMsgMob));
        et_forgot_mob.setInputType(InputType.TYPE_CLASS_NUMBER);
        countryCode.setVisibility(View.VISIBLE);
    }

    @Override
    public void startNextActivity( String countryCode, String mob_mail) {
        Intent intent = new Intent(ForgotPasswordMobNum.this, ForgotPasswordVerify.class);
        Bundle mbundle = new Bundle();
        mbundle.putString("mobile", mob_mail);
        mbundle.putString("countryCode", countryCode);
        mbundle.putString("from", "ForgotPass");
        intent.putExtras(mbundle);
        startActivity(intent);
    }

    @Override
    public void moveToLogin(String msg) {
        Utility.BlueToast(this,msg);
        Intent intent = new Intent(ForgotPasswordMobNum.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onError(String error) {
        Utility.showAlert(error,this);
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
        overridePendingTransition(R.anim.stay_activity, R.anim.bottem_slide_up);
    }

    /**
     * <h1>initializeViews</h1>
     * <p>this is the method, for initialize the views</p>
     */
    private void initializeViews() {

        ClanaproNarrMedium = fontUtils.titaliumSemiBold();
        ClanaproNarrNews = fontUtils.titaliumRegular();
        tv_forgot_msg.setTypeface(ClanaproNarrNews);
        tv_forgot_phoneno.setTypeface(ClanaproNarrNews);
        et_forgot_mob.setTypeface(ClanaproNarrNews);
        et_forgot_mob.setHint(getResources().getString(R.string.phone_number));
        tv_forgot_next.setTypeface(ClanaproNarrMedium);
        countryCode.setTypeface(ClanaproNarrNews);

        //listener for ll_First linear layout
        sv_signup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                presenter.onOutSideTouch();
                return false;
            }
        });

        //listener for ok option keyboard
        et_forgot_mob.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return presenter.onNextKey(v,keyCode,event,et_forgot_mob.getText().toString());
            }
        });
        et_forgot_mob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                presenter.etAfterTextChanged();
            }
        });


        rg_WorkGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    //email event
                    case R.id.rbEmail:
                        presenter.rbEmailChecked();
                        break;
                    //phone event
                    case R.id.rbPhone:
                        presenter.rbMobileChecked();
                        break;

                }
            }
        });
    }

    @OnClick({R.id.tv_forgot_next,R.id.code})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //event for next button
            case R.id.tv_forgot_next:
                presenter.validatePhone(et_forgot_mob.getText().toString(),countryCode.getText().toString());
                break;
            //event for picking up the country code
            case R.id.code:
                presenter.showDialogForCountryPicker();
                break;
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
}
