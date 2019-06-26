package com.delivx.app.invoice;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.driver.delivx.R;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class InvoiceActivity extends DaggerAppCompatActivity implements
        EasyPermissions.PermissionCallbacks, InvoiceContract.ViewOpetaions,SignaturePad.OnSignedListener {


    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.tv_title) TextView toolBarTitle;
    @BindView(R.id.tv_bid) TextView tv_bid;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.llTop) LinearLayout llTop;
    @BindView(R.id.llBottom) LinearLayout llBottom;
    @BindView(R.id.tvBillTitle) TextView tvBillTitle;
    @BindView(R.id.tvBill) TextView tvBill;
    @BindView(R.id.tvRatingTitle) TextView tvRatingTitle;
    @BindView(R.id.ratingbar) ImageView ratingbar;
    @BindView(R.id.ratingbar1) ImageView ratingbar1;
    @BindView(R.id.ratingbar2) ImageView ratingbar2;
    @BindView(R.id.ratingbar3) ImageView ratingbar3;
    @BindView(R.id.ratingbar4) ImageView ratingbar4;
    @BindView(R.id.tv_submit) TextView tv_submit;
    @BindView(R.id.tvSignTitle) TextView tvSignTitle;
    @BindView(R.id.ivSignature) ImageView ivSignature;
    @BindView(R.id.ll_sign_here1) LinearLayout ll_sign_here1;
    @BindView(R.id.tvSignHere1) TextView tvSignHere1;
    @BindView(R.id.rlSignaturePad) RelativeLayout rlSignaturePad;
    @BindView(R.id.ivBackBtn) ImageView ivBackBtn;
    @BindView(R.id.ivRefresh) ImageView ivRefresh;
    @BindView(R.id.tv_approve) TextView tv_approve;
    @BindView(R.id.signature_pad) SignaturePad signature_pad;
    @BindView(R.id.ll_sign_here) LinearLayout ll_sign_here;
    @BindView(R.id.tvSignHere) TextView tvSignHere;
    @BindView(R.id.include_actionbar) View actionbar;
    private Typeface fontSemiBold,fontBold;

    @Inject InvoiceContract.PresenterOpetaions presenter;
    @Inject FontUtils fontUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.activity_invoice);
        ButterKnife.bind(this);

        initViews();
        presenter.getBundleData(getIntent().getExtras());
        presenter.setActionBar();
        presenter.setActionBarTitle();
    }

    /**
     * <h2>initViews</h2>
     * <p>initializing the views(fonts and style)</p>
     */
    public void initViews(){

        fontSemiBold= fontUtils.titaliumSemiBold();
        fontBold= fontUtils.titaliumBold();

        toolBarTitle.setTypeface(fontSemiBold);
        tv_bid.setTypeface(fontSemiBold);
        tvBillTitle.setTypeface(fontSemiBold);
        tvBill.setTypeface(fontBold);
        tvRatingTitle.setTypeface(fontSemiBold);
        tv_submit.setTypeface(fontBold);
        tv_approve.setTypeface(fontBold);
        tvSignTitle.setTypeface(fontSemiBold);
        tvSignHere1.setTypeface(fontSemiBold);
        tvSignHere.setTypeface(fontSemiBold);

        signature_pad.setOnSignedListener(this);
        ratingbar.setSelected(true);
        ratingbar1.setSelected(true);
        ratingbar2.setSelected(true);

    }

    @Override
    public void initActionBar() {

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    public void onSuccess(AssignedAppointments appointments) {
        finish();
    }

    @Override
    public void onError(String message) {
        Utility.mShowMessage(getResources().getString(R.string.message),message,this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.requestPermission();
    }

    @OnClick({R.id.signature,R.id.ivRefresh,R.id.ivBackBtn,R.id.tv_approve,R.id.tv_submit,
            R.id.ratingbar,R.id.ratingbar1,R.id.ratingbar2,R.id.ratingbar3,R.id.ratingbar4})
    public void onClick(View view){
        switch (view.getId())
        {
            case R.id.ratingbar:
                ratingbar.setSelected(true);
                ratingbar1.setSelected(false);
                ratingbar2.setSelected(false);
                ratingbar3.setSelected(false);
                ratingbar4.setSelected(false);
                break;

            case R.id.ratingbar1:
                ratingbar.setSelected(true);
                ratingbar1.setSelected(true);
                ratingbar2.setSelected(false);
                ratingbar3.setSelected(false);
                ratingbar4.setSelected(false);
                break;

            case R.id.ratingbar2:
                ratingbar.setSelected(true);
                ratingbar1.setSelected(true);
                ratingbar2.setSelected(true);
                ratingbar3.setSelected(false);
                ratingbar4.setSelected(false);
                break;

            case R.id.ratingbar3:
                ratingbar.setSelected(true);
                ratingbar1.setSelected(true);
                ratingbar2.setSelected(true);
                ratingbar3.setSelected(true);
                ratingbar4.setSelected(false);
                break;

            case R.id.ratingbar4:
                ratingbar.setSelected(true);
                ratingbar1.setSelected(true);
                ratingbar2.setSelected(true);
                ratingbar3.setSelected(true);
                ratingbar4.setSelected(true);
                break;

                //event for relative layout
            case R.id.signature:
                presenter.openSignaturePad();
                break;

                //clearing the signature
            case R.id.ivRefresh:
                presenter.refresh();
                break;

                //back option in action bar
            case R.id.ivBackBtn:
                presenter.onBackPressSign();
                break;

                //signature approved
            case R.id.tv_approve:
                presenter.onSignatureApprove();
                break;

                //submit
            case R.id.tv_submit:
                float rating =3;
                if(ratingbar4.isSelected())
                {
                    rating = 5;
                }else if(ratingbar3.isSelected())
                {
                    rating = 4;
                }else if(ratingbar2.isSelected())
                {
                    rating = 3;
                }else if(ratingbar1.isSelected())
                {
                    rating = 2;
                }else if(ratingbar.isSelected())
                {
                    rating = 1;
                }

                presenter.completeBooking(rating);
                break;
        }

    }



    public void setTitle(String title,String bid) {
        toolBarTitle.setText(title);
        tv_bid.setText(bid);
    }

    public void setViews(String amount){
        tvBill.setText(amount);
    }

    @Override
    public void onStartSigning() {
        ll_sign_here.setVisibility(View.GONE);
    }

    @Override
    public void onSigned() {
        Bitmap signBitmap = signature_pad.getSignatureBitmap();
        presenter.onSigned(signBitmap);
    }

    @Override
    public void onClear() {
        presenter.onSigned(null);
        ll_sign_here.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearSignature() {
        signature_pad.clear();
    }

    @Override
    public void showSignature(){
        actionbar.setVisibility(View.GONE);
        rlSignaturePad.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideSignature(Bitmap signBitmap){
        rlSignaturePad.setVisibility(View.GONE);
        actionbar.setVisibility(View.VISIBLE);
        ivSignature.setImageBitmap(signBitmap);
        if(signBitmap==null){
            ll_sign_here1.setVisibility(View.VISIBLE);
        }else {
            ll_sign_here1.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSignatureApprove(Bitmap bitmap){
        rlSignaturePad.setVisibility(View.GONE);
        actionbar.setVisibility(View.VISIBLE);
        ll_sign_here1.setVisibility(View.GONE);
        ivSignature.setImageBitmap(bitmap);

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forwarding results to for permission check
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else if (requestCode == VariableConstant.RC_READ_WRITE_CAMERA_STATE) {
            methodRequiresOnePermission();
        }
    }

    @AfterPermissionGranted(VariableConstant.RC_READ_WRITE_CAMERA_STATE)
    public void methodRequiresOnePermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.read_storage_and_camera_state_permission_message),
                    VariableConstant.RC_READ_WRITE_CAMERA_STATE, perms);
        }
    }
}
