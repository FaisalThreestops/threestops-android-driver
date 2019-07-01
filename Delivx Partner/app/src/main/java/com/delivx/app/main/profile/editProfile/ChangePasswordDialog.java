package com.delivx.app.main.profile.editProfile;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;

public class ChangePasswordDialog extends Dialog {
    RefreshProfile refreshProfile;
    private Context context;
    private ProgressDialog pDialog;
    private Dialog passwordDialog;
    private String TAG = ChangePasswordDialog.class.getSimpleName(), from;
    private EditText et_oldpass;
    private TextView tv_confirm;
    private String from_pass = "Password", from_name = "Name", from_signup = "from_signup",from_rcvr="receiver";
    private View view_;
    PreferenceHelperDataSource preferenceHelperDataSource;

    public ChangePasswordDialog(Context context, String from, RefreshProfile refreshProfile,PreferenceHelperDataSource preferenceHelperDataSource) {
        super(context);
        this.context = context;
        this.from = from;
        this.refreshProfile = refreshProfile;
        this.setCanceledOnTouchOutside(true);
        this.preferenceHelperDataSource=preferenceHelperDataSource;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCanceledOnTouchOutside(true);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage(context.getString(R.string.loading));
        pDialog.setCancelable(false);


        passwordDialog = new Dialog(context);
        passwordDialog.setCancelable(true);
        passwordDialog.setCanceledOnTouchOutside(true);
        passwordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        passwordDialog.setContentView(R.layout.single_password_conform_dialog);
        passwordDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Typeface ClanaproNarrNews = Typeface.createFromAsset(getContext().getAssets(), "fonts/TITILLIUMWEB-REGULAR.TTF");
        Typeface ClanaproNarrMedium = Typeface.createFromAsset(getContext().getAssets(), "fonts/TITILLIUMWEB-SEMIBOLD.TTF");
        TextView tv_verify_msg, tv_oldpass;

        tv_verify_msg = passwordDialog.findViewById(R.id.tv_verify_msg);
        tv_verify_msg.setTypeface(ClanaproNarrNews);

        tv_oldpass = passwordDialog.findViewById(R.id.tv_oldpass);
        tv_oldpass.setTypeface(ClanaproNarrNews);

        et_oldpass = passwordDialog.findViewById(R.id.et_oldpass);
        tv_oldpass.setTypeface(ClanaproNarrNews);

        tv_confirm = passwordDialog.findViewById(R.id.tv_confirm);
        tv_confirm.setTypeface(ClanaproNarrMedium);
        view_ = passwordDialog.findViewById(R.id.view_);

        if (from.equals(from_pass)) {
            tv_verify_msg.setText(context.getResources().getString(R.string.enter_old_pass));
            tv_oldpass.setText(context.getResources().getString(R.string.old_pas));
            tv_confirm.setText(context.getResources().getString(R.string.ok));
            et_oldpass.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else if (from.equals(from_signup)) {
            et_oldpass.setVisibility(View.GONE);
            tv_verify_msg.setText(context.getResources().getString(R.string.message));
            tv_oldpass.setText(context.getResources().getString(R.string.signup_success_msg));
            tv_confirm.setText(context.getResources().getString(R.string.ok));
            view_.setVisibility(View.GONE);

        }
        else if (from.equals(from_rcvr)) {
            et_oldpass.setVisibility(View.GONE);
            tv_verify_msg.setText(context.getResources().getString(R.string.message));
            tv_oldpass.setText(context.getResources().getString(R.string.collect_cash_from_rcvr));
            tv_oldpass.setTextSize(20);
            tv_confirm.setText(context.getResources().getString(R.string.ok));
            view_.setVisibility(View.GONE);

        }

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals(from_pass)) {
                    OldPasswordVerify();
                } else  {
                    refreshProfile.onRefresh();
                }
            }
        });

    }
    @Override
    public void show() {
        super.show();
        passwordDialog.show();
    }
    @Override
    public void dismiss() {
        super.dismiss();
        passwordDialog.dismiss();
    }


    /**
     * <h1>OldPasswordVerify</h1>
     * <p>validating password</p>
     */
    private void OldPasswordVerify() {
        String oldpass = et_oldpass.getText().toString();

        if (!oldpass.matches("")) {
            if (oldpass.equals(preferenceHelperDataSource.getPassword())) {
                refreshProfile.onRefresh();
                VariableConstant.EDIT_PASSWORD_DIALOG = true;
                ChangePasswordDialog.this.dismiss();
            } else {
                Utility.mShowMessage(context.getResources().getString(R.string.message), context.getResources().getString(R.string.invalid_pass),(Activity) context);
            }

        } else {
            Utility.BlueToast(context, context.getResources().getString(R.string.err_pass));
        }

    }

    public interface RefreshProfile {
        /**
         * <h2>onRefresh</h2>
         * <p>removing the dialog</p>
         */
        void onRefresh();
    }

}
