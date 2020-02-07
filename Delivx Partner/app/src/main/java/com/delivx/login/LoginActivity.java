package com.delivx.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delivx.ForgotPassword.ForgotPasswordMobNum;
import com.delivx.app.main.MainActivity;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.login.language.LanguagesList;
import com.delivx.utility.DialogHelper;
import com.driver.delivx.R;
import com.delivx.signup.perosonal.SignupPersonal;
import com.delivx.utility.DisableError;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class LoginActivity extends DaggerAppCompatActivity implements View.OnClickListener,
        LoginView, DisableError, EasyPermissions.PermissionCallbacks {


    Typeface ClanaproNarrMedium;
    Typeface ClanaproNarrNews;

    @BindView(R.id.tv_log_login) TextView tv_log_login;
    @BindView(R.id.tv_splash_msg) TextView tv_splash_msg;
    @BindView(R.id.tv_log_forgortpass) TextView tv_log_forgortpass;
    @BindView(R.id.tv_log_signup) TextView tv_log_signup ;
    @BindView(R.id.et_log_mail_mob) EditText et_log_mob;
    @BindView(R.id.et_log_password) EditText et_log_pass;
    @BindView(R.id.progressBar) ProgressBar  progressBar;
    @BindView(R.id.activityRoot) RelativeLayout activityRoot;

    @BindView(R.id.til_log_mail_mob) TextInputLayout til_log_mob;
    @BindView(R.id.til_log_password) TextInputLayout til_log_pass;
    @BindView(R.id.tvOr) TextView tvOr;
    @BindView(R.id.tvUserNameHint) TextView tvUserNameHint;
    @BindView(R.id.tv_option_email) TextView tv_option_email;
    @BindView(R.id.tv_option_phone) TextView tv_option_phone;
    @BindView(R.id.tvCountryCode) TextView tvCountryCode;
    @BindView(R.id.ll_phone_number) LinearLayout ll_phone_number;
    @BindView(R.id.llLogo) LinearLayout llLogo;
    @BindView(R.id.et_phone_num) EditText et_phone_num;
    @BindView(R.id.view_background) View view_background;
    @BindView(R.id.view_phone_option) View view_phone_option;
    @BindView(R.id.view_email_option) View view_email_option;
    @BindView(R.id.cvBottom) CardView cvBottom;
    @BindView(R.id.tv_selected_language) TextView tv_selected_language;
    ArrayList<LanguagesList> languagesLists = new ArrayList<>();
    @BindDrawable(R.drawable.drop_down)  Drawable drop_down;
    @Inject DialogHelper dialogHelper;
    @BindString(R.string.location_permission_message) String location_permission_message;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject LoginPresenter loginPresenter;
    @Inject FontUtils fontUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Check if the language code needs RTL changes to the UI
        Utility.RtlConversion(this,preferenceHelperDataSource.getLanguageSettings().getLanguageCode());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initializeViews();
        preferenceHelperDataSource.setFCMRegistrationId(FirebaseInstanceId.getInstance().getToken());
        loginPresenter.getBundleData(getIntent());
        loginPresenter.getCountryCode();

    }


    @Override
    protected void onResume() {
        super.onResume();
        loginPresenter.unSubScribeMQTT();

        //asking the location run time permission above 23 version devices
        if (Build.VERSION.SDK_INT >= 23) {
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            //if permission is delayed,, telling the importances of the permission and ask again
            if (!EasyPermissions.hasPermissions(this, perms)) {
                EasyPermissions.requestPermissions(this, location_permission_message,
                        VariableConstant.RC_LOCATION_STATE, perms);
            }
        }
    }


    /**
     * <h1>initializeViews</h1>
     * <p>this is the method, for initialize the views</p>
     */
    private void initializeViews() {
        ClanaproNarrMedium = fontUtils.titaliumSemiBold();
        ClanaproNarrNews = fontUtils.titaliumRegular();
        tv_splash_msg.setTypeface(ClanaproNarrMedium);
        tv_log_login.setTypeface(ClanaproNarrMedium);
        tv_log_forgortpass.setTypeface(ClanaproNarrNews);
        et_log_mob.setTypeface(ClanaproNarrNews);
        tv_log_signup.setText(Html.fromHtml(getString(R.string.login_signup_txt)));
        tv_log_signup.setTypeface(ClanaproNarrNews);
        til_log_mob.setTypeface(ClanaproNarrNews);
        et_log_pass.setTypeface(ClanaproNarrNews);
        tvOr.setTypeface(ClanaproNarrNews);
        tv_option_email.setTypeface(ClanaproNarrNews);
        tv_option_phone.setTypeface(ClanaproNarrNews);
        tv_selected_language.setTypeface(ClanaproNarrNews);
        til_log_pass.setTypeface(ClanaproNarrNews);

        loginPresenter.getLoginCreds();

        //DelivX image will hide when we enter the  values of email or password
        activityRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRoot.getRootView().getHeight() - activityRoot.getHeight();
                Utility.printLog("dptopx "+dpToPx(LoginActivity.this, 200));
                if (heightDiff > dpToPx(LoginActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    llLogo.setVisibility(View.GONE);
                    cvBottom.setVisibility(View.GONE);

                }else {
                    llLogo.setVisibility(View.VISIBLE);
                    cvBottom.setVisibility(View.VISIBLE);
                }
            }
        });

        dialogHelper.getDialogCallbackHelper(new DialogHelper.DialogCallbackHelper() {

            @Override
            public void walletFragOpen() {

            }

            @Override
            public void changeLanguage(String langCode, String langName, int dir) {
                loginPresenter.languageChanged(langCode,langName);
            }

        });


    }


    /***
     * <h1>dpToPx</h1>
     * <p>Converting DP to Pixel</p>
     * @param context activity
     * @param valueInDp value in dp
     * @return value in pixel
     */
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }



    @OnClick({R.id.tvCountryCode,R.id.tv_option_phone,R.id.tv_option_email,
            R.id.activityRoot,R.id.tv_log_login,
            R.id.tv_log_forgortpass,R.id.tv_log_signup,R.id.tv_selected_language})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Hide keyboard, Disable the sign in button, validate user inputs and invoking the login presenter
            case R.id.tv_log_login:
                hideSoftKeyboard();
                enableDisableSignIn(false);
                methodRequiresOnePermission();
                if (preferenceHelperDataSource.getDeviceId() != null)
                    loginPresenter.validateCredentials(et_phone_num.getText().toString(),et_log_mob.getText().toString(), et_log_pass.getText().toString());
                break;

            //Invoke the forgot password activity
            case R.id.tv_log_forgortpass:
                loginPresenter.forgotPassOnclick();
                break;

            //Invoke the user signup activity
            case R.id.tv_log_signup:
                loginPresenter.signUpOnclick();
                break;

            //Hide keyboard
            case R.id.activityRoot:
                loginPresenter.onOutSideTouch();
                break;

            //Select Login by Phone Number
            case R.id.tv_option_phone:
                loginPresenter.choosePhoneLogin();
                break;

            //Select Login by Email
            case R.id.tv_option_email:
                loginPresenter.chooseEmailLogin();
                break;

            //Invoke the activity to choose the country code
            case R.id.tvCountryCode:
                loginPresenter.showDialogForCountryPicker();
                break;

            //Invoke the activity to change the language
            case R.id.tv_selected_language:
                hideSoftKeyboard();
                loginPresenter.getLanguages();
                break;
        }

    }

    @AfterPermissionGranted(VariableConstant.RC_READ_PHONE_STATE)
    private void methodRequiresOnePermission() {
        String[] perms = {Manifest.permission.READ_PHONE_STATE};
        //Set the DeviceId if permission is allowed
        if (EasyPermissions.hasPermissions(this, perms)) {
            preferenceHelperDataSource.setDeviceId(Utility.getDeviceId(LoginActivity.this));
        }
        //Show the alert if the permission is delayed
        else {
            EasyPermissions.requestPermissions(this, getString(R.string.read_phone_state_permission_message),
                    VariableConstant.RC_READ_PHONE_STATE, perms);
        }
        enableDisableSignIn(true);
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
    public void setUsernameError(String message) {
        til_log_mob.setErrorEnabled(true);
        til_log_mob.setError(message);
        enableDisableSignIn(true);
    }


    @Override
    public void setPasswordError(String message) {
        til_log_pass.setErrorEnabled(true);
        til_log_pass.setError(message);
        enableDisableSignIn(true);
    }


    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void setLoginCreds(String username, String pass) {
        et_log_mob.setText(username);
        et_log_pass.setText(pass);
    }

    @Override
    public void startForgotPassAct() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordMobNum.class));
    }

    @Override
    public void startSignUpAct() {
        startActivity(new Intent(LoginActivity.this, SignupPersonal.class));
    }

    @Override
    public void onPhoneLoginSelected() {
        et_phone_num.requestFocus();
        tv_option_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_option_email.setTextColor(getResources().getColor(R.color.color_sup_txt));
        view_phone_option.setVisibility(View.VISIBLE);
        view_email_option.setVisibility(View.GONE);
        tvUserNameHint.setVisibility(View.VISIBLE);
        ll_phone_number.setVisibility(View.VISIBLE);
        til_log_mob.setVisibility(View.GONE);
        view_background.setVisibility(View.VISIBLE);
        et_log_pass.setText("");
    }

    @Override
    public void onEmailLoginSelected() {
        et_log_mob.requestFocus();
        tv_option_phone.setTextColor(getResources().getColor(R.color.color_sup_txt));
        tv_option_email.setTextColor(getResources().getColor(R.color.colorPrimary));
        view_phone_option.setVisibility(View.GONE);
        tvUserNameHint.setVisibility(View.GONE);
        ll_phone_number.setVisibility(View.GONE);
        til_log_mob.setVisibility(View.VISIBLE);
        view_email_option.setVisibility(View.VISIBLE);
        view_background.setVisibility(View.GONE);
        et_log_pass.setText("");
    }
    

    @Override
    public void setCountryCode(String dialCode) {
        tvCountryCode.setText(dialCode);
    }

    @Override
    public void showError(String message) {
        enableDisableSignIn(true);
        Utility.mShowMessage(getResources().getString(R.string.message),message,LoginActivity.this);
    }

    @Override
    protected void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setDisableError(View view) {
        switch (view.getId()) {
            case R.id.et_log_mail_mob:
                til_log_mob.setError(null);
                til_log_mob.setErrorEnabled(false);
                break;
            case R.id.et_log_password:
                til_log_pass.setError(null);
                til_log_pass.setErrorEnabled(false);
                break;
        }
    }

    @Override
    public void enableSighUp() {
        tv_log_login.setBackground(ContextCompat.getDrawable(this, R.drawable.sigin_back_green_click));
    }

    @Override
    public void disableSighUp() {
        tv_log_login.setBackground(ContextCompat.getDrawable(this, R.drawable.sigin_back_grey));
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Utility.printLog("LoginActivity Device ID " + Utility.getDeviceId(LoginActivity.this));
        preferenceHelperDataSource.setDeviceId(Utility.getDeviceId(LoginActivity.this));

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms)
    {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else if (requestCode == VariableConstant.RC_READ_PHONE_STATE) {
            EasyPermissions.requestPermissions(LoginActivity.this, getString(R.string.read_phone_state_permission_message)
                    , VariableConstant.RC_READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forwarding results to for permission check
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * <h2>enableDisableSignIn</h2>
     * <p>enabling or disabling the sign in based on the boolean option</p>
     * @param enable : true(enable), false(disable)
     */
    public void enableDisableSignIn(boolean enable){
        tv_log_login.setEnabled(enable);
        if(enable)
            tv_log_login.setOnClickListener(this);
        else
            tv_log_login.setOnClickListener(null);
    }


    /**
     * <h1>hideSoftKeyboard</h1>
     * <p>hiding the keyboard</p>
     */
    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void setLanguageDialog(ArrayList<LanguagesList> languagesListModel, int indexSelected) {
        languagesLists = languagesListModel;
        DialogHelper.languageSelectDialog(this, languagesLists, indexSelected);

    }

    @Override
    public void setLanguage(String language,boolean restart)    {
        tv_selected_language.setText(language);
        tv_selected_language.setCompoundDrawablesWithIntrinsicBounds(null,null ,drop_down,null);
        if(restart)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Runtime.getRuntime().exit(0);
        }
    }

}
