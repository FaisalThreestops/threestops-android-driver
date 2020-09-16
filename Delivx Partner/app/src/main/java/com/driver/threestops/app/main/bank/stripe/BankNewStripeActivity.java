package com.driver.threestops.app.main.bank.stripe;

import android.Manifest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

 import com.driver.Threestops.BuildConfig;
import com.driver.Threestops.R;
import com.driver.threestops.utility.FontUtils;

import com.driver.threestops.utility.Utility;
import com.driver.threestops.utility.VariableConstant;

import java.io.File;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import pub.devrel.easypermissions.EasyPermissions;

public class BankNewStripeActivity extends DaggerAppCompatActivity implements View.OnClickListener,StripeAccountContract.ViewOperations{


    @BindView(R.id.ivAddFile) ImageView ivAddFile;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvBankDetails) TextView tv_title;
    @BindView(R.id.tvSave) TextView tvSave ;
    @BindView(R.id.etName) EditText etName;
    @BindView(R.id.etLName) EditText etLName;
    @BindView(R.id.etDob) EditText etDob;
    @BindView(R.id.etPersonalId) EditText etPersonalId;
    @BindView(R.id.etState) EditText etState;
    @BindView(R.id.etPostalCode) EditText etPostalCode;
    @BindView(R.id.etCity) EditText etCity;
    @BindView(R.id.etCountry) EditText etCountry;
    @BindView(R.id.etAddress) EditText etAddress;
    @BindView(R.id.tilName) TextInputLayout tilName;
    @BindView(R.id.tilLastName) TextInputLayout tilLastName;
    @BindView(R.id.tilDob) TextInputLayout tilDob;
    @BindView(R.id.tilPersonalId) TextInputLayout tilPersonalId;
    @BindView(R.id.tilState) TextInputLayout tilState;
    @BindView(R.id.tilPostalCode) TextInputLayout tilPostalCode;
    @BindView(R.id.tilCity) TextInputLayout tilCity;
    @BindView(R.id.tilCountry) TextInputLayout tilCountry;
    @BindView(R.id.tilAddress) TextInputLayout tilAddress;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    
    @Inject
    FontUtils fontUtils;

    @Inject
    BankNewStripePresenter presenter;

    private final int REQUEST_CODE_GALLERY = 0x1;
    private final int REQUEST_CODE_TAKE_PICTURE = 0x2;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.activity_bank_new_stripe_details);
        ButterKnife.bind(this);
        initViews();
        presenter.init(getIntent().getExtras());
    }

    @Override
    public void onSuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }


    /**
     * <h1>initViews</h1>
     * <p>initialize widgets </p>
     */
    private void initViews() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.vector_cancel);
        }


        tv_title.setTypeface(fontUtils.titaliumSemiBold());
        tvSave.setTypeface(fontUtils.titaliumRegular());
        etName.setTypeface(fontUtils.titaliumRegular());
        etLName.setTypeface(fontUtils.titaliumRegular());
        etDob.setTypeface(fontUtils.titaliumRegular());
        etPersonalId.setTypeface(fontUtils.titaliumRegular());
        etState.setTypeface(fontUtils.titaliumRegular());
        etPostalCode.setTypeface(fontUtils.titaliumRegular());
        etCity.setTypeface(fontUtils.titaliumRegular());
        etAddress.setTypeface(fontUtils.titaliumRegular());


    }

    @Override
    public void setValues(Bundle bundleBankDetails) {
        try {
            etName.setText(bundleBankDetails.getString("fname"));
            etLName.setText(bundleBankDetails.getString("lname"));
            etCountry.setText(bundleBankDetails.getString("country"));
            etState.setText(bundleBankDetails.getString("state"));
            etCity.setText(bundleBankDetails.getString("city"));
            etAddress.setText(bundleBankDetails.getString("address"));
            if (bundleBankDetails.getString("month") != null && bundleBankDetails.getString("day") != null && bundleBankDetails.getString("year") != null) {
                etDob.setText(bundleBankDetails.getString("month") + "/" + bundleBankDetails.getString("day") + "/" + bundleBankDetails.getString("year"));
            }else {
                etDob.setText("");
            }
            etPostalCode.setText(bundleBankDetails.getString("postalcode"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getFname() {
        presenter.setFname(etName.getText().toString());
    }

    @Override
    public void setFirstNameError() {
        tilName.setErrorEnabled(true);
        tilName.setError(getString(R.string.enterAccountHoldername));
    }

    @Override
    public void getLastName() {
        presenter.setLastName(etLName.getText().toString());
    }

    @Override
    public void setLastNameError() {
        tilName.setErrorEnabled(false);
        tilLastName.setErrorEnabled(true);
        tilLastName.setError(getString(R.string.enterLastName));
    }

    @Override
    public void getDob() {
        presenter.setDob(etDob.getText().toString());
    }

    @Override
    public void setDobError() {
        tilName.setErrorEnabled(false);
        tilLastName.setErrorEnabled(false);
        tilDob.setErrorEnabled(true);
        tilDob.setError(getString(R.string.enterDob));
    }

    @Override
    public void getPersonalId() {
        presenter.setPersonalId(etPersonalId.getText().toString());
    }

    @Override
    public void setPersonalIDError() {
        tilLastName.setErrorEnabled(false);
        tilName.setErrorEnabled(false);
        tilDob.setErrorEnabled(false);
        tilPersonalId.setErrorEnabled(true);
        tilPersonalId.setError(getString(R.string.enterPersonalid));
    }

    @Override
    public void getAddress() {
        presenter.setAddress(etAddress.getText().toString());
    }

    @Override
    public void setAddressError() {
        tilLastName.setErrorEnabled(false);
        tilName.setErrorEnabled(false);
        tilDob.setErrorEnabled(false);
        tilPersonalId.setErrorEnabled(false);
        tilAddress.setErrorEnabled(true);
        tilAddress.setError(getString(R.string.enterAddress));
    }

    @Override
    public void getCity() {
        presenter.setCityName(etCity.getText().toString());
    }

    @Override
    public void setCityError() {
        tilLastName.setErrorEnabled(false);
        tilName.setErrorEnabled(false);
        tilDob.setErrorEnabled(false);
        tilPersonalId.setErrorEnabled(false);
        tilAddress.setErrorEnabled(false);
        tilCity.setErrorEnabled(true);
        tilCity.setError(getString(R.string.enterCity));
    }


    @Override
    public void getState() {
        presenter.setState(etState.getText().toString());
    }

    @Override
    public void setStateError() {
        tilLastName.setErrorEnabled(false);
        tilName.setErrorEnabled(false);
        tilDob.setErrorEnabled(false);
        tilPersonalId.setErrorEnabled(false);
        tilCity.setErrorEnabled(false);
        tilAddress.setErrorEnabled(false);

        tilState.setErrorEnabled(true);
        tilState.setError(getString(R.string.enterState));
    }


    @Override
    public void getPostalCode() {
        presenter.setPostalCode(etPostalCode.getText().toString());
    }

    @Override
    public void setPostalCodeError() {
        tilName.setErrorEnabled(false);
        tilLastName.setErrorEnabled(false);
        tilDob.setErrorEnabled(false);
        tilPersonalId.setErrorEnabled(false);
        tilState.setErrorEnabled(false);
        tilCity.setErrorEnabled(false);
        tilAddress.setErrorEnabled(false);
        tilPostalCode.setErrorEnabled(true);
        tilPostalCode.setError(getString(R.string.enterPostalCode));
    }

    @Override
    public void showError(String string) {
        Utility.showAlert(string,this);
    }

    @Override
    public void disableError() {
        tilLastName.setErrorEnabled(false);
        tilName.setErrorEnabled(false);
        tilDob.setErrorEnabled(false);
        tilPersonalId.setErrorEnabled(false);
        tilState.setErrorEnabled(false);
        tilPostalCode.setErrorEnabled(false);
        tilCity.setErrorEnabled(false);
        tilAddress.setErrorEnabled(false);
    }

    @Override
    public void setImage(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap, ivAddFile.getWidth(), ivAddFile.getHeight(), true);
        ivAddFile.setImageBitmap(bitmap);

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
        finish();
        overridePendingTransition(R.anim.stay, R.anim.top_to_bottom);
    }

    @OnClick({R.id.ivAddFile,R.id.tvSave,R.id.etDob})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //event for add file
            case R.id.ivAddFile:
                String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                if (EasyPermissions.hasPermissions(this, perms)) {
                    presenter.selectImage();
                } else {
                    // Do not have permissions, requesting permission
                    EasyPermissions.requestPermissions(this, getString(R.string.read_storage_and_camera_state_permission_message),
                            VariableConstant.RC_LOCATION_STATE, perms);
                }
                break;

                //event for save
            case R.id.tvSave:
                presenter.onSave();
                break;

                //event for select the DOB
            case R.id.etDob:
                presenter.openCalender();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void selectImage(boolean isPicturetaken) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(this).inflate(R.layout.profile_pic_options, null);
        alertDialogBuilder.setView(view);

        final AlertDialog mDialog = alertDialogBuilder.create();
        mDialog.setCancelable(false);
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button btnCamera = view.findViewById(R.id.camera);
        Button btnCancel = view.findViewById(R.id.cancel);
        Button btnGallery = view.findViewById(R.id.gallery);
        Button btnRemove = view.findViewById(R.id.removephoto);
        TextView tvHeader = view.findViewById(R.id.tvHeader);

        btnCamera.setTypeface(fontUtils.titaliumRegular());
        btnCancel.setTypeface(fontUtils.titaliumRegular());
        btnGallery.setTypeface(fontUtils.titaliumRegular());
        btnRemove.setTypeface(fontUtils.titaliumRegular());
        tvHeader.setTypeface(fontUtils.titaliumRegular());

        if (isPicturetaken) {
            btnRemove.setVisibility(View.VISIBLE);
        } else {
            btnRemove.setVisibility(View.GONE);
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.takePicture();
                mDialog.dismiss();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openGallery();
                mDialog.dismiss();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivAddFile.setImageResource(R.drawable.vector_add_file);
                presenter.removeImage();
                mDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    @Override
    public void takePicture(File mFileTemp) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            mImageCaptureUri = Uri.fromFile(mFileTemp);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mImageCaptureUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", mFileTemp);
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (Exception e) {
            Log.d("error", "cannot take picture", e);
        }
    }

    @Override
    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    @Override
    public void setDate(String date) {
        etDob.setText(date);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode,resultCode,data);

    }

    @Override
    public void networkError(String message) {
        
    }

    @Override
    public void showProgress() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
