package com.driver.threestops.ForgotPassword;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.driver.threestops.ForgotPassword.Changepassword.ChangePassView;
import com.driver.threestops.ForgotPassword.Changepassword.PresenterChangePass;
import com.driver.threestops.login.LoginActivity;
import com.driver.Threestops.R;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Utility;


import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;


public class ForgotPasswordChangePass extends DaggerAppCompatActivity implements View.OnClickListener,ChangePassView {

    private Typeface ClanaproNarrMedium,ClanaproNarrNews;


    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_newpass_msg) TextView tv_newpass_msg;
    @BindView(R.id.tv_new_pass) TextView tv_new_pass;
    @BindView(R.id.tv_reenter_pass) TextView tv_reenter_pass;
    @BindView(R.id.tv_continue) TextView tv_continue;
    @BindView(R.id.et_new_pass) EditText et_new_pass;
    @BindView(R.id.et_reenter_pass) EditText et_reenter_pass;
    @BindView(R.id.sv_signup) LinearLayout ll_first;


    @BindString(R.string.EntNewPass)String title;

    @Inject PresenterChangePass presenter;
    @Inject FontUtils fontUtils;

    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.activity_forgot_password_change_pass);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        ButterKnife.bind(this);

        presenter.getBundleData(getIntent().getExtras());

        initializeViews();

        presenter.setActionBar();
    }


    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.history_back_icon_off);
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
    public void startLoginAct() {
        Intent intent = new Intent(ForgotPasswordChangePass.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void showError(String string) {
        Utility.mShowMessage(title, string, ForgotPasswordChangePass.this);
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

    /**
     * <h2>initializeViews</h2>
     * <p>Initializing the view(style,font)</p>
     */
    private void initializeViews() {

        ClanaproNarrMedium = fontUtils.titaliumSemiBold();

        ClanaproNarrNews = fontUtils.titaliumRegular();

        tv_newpass_msg.setTypeface(ClanaproNarrNews);

        tv_new_pass.setTypeface(ClanaproNarrNews);

        tv_reenter_pass.setTypeface(ClanaproNarrNews);

        tv_continue.setTypeface(ClanaproNarrMedium);

        et_new_pass.setTypeface(ClanaproNarrNews);

        et_reenter_pass.setTypeface(ClanaproNarrNews);


        ll_first.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                presenter.onOutSideTouch();
                return false;
            }
        });

    }

    @OnClick(R.id.tv_continue)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //event for save
            case R.id.tv_continue:
                presenter.validatePassword(et_new_pass.getText().toString(),et_reenter_pass.getText().toString());
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
