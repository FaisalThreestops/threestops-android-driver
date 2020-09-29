package com.driver.threestops.app.main.bank.addBankAccount;

import com.google.android.material.textfield.TextInputLayout;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.driver.threestops.utility.Utility;
import com.driver.Threestops.R;
import com.driver.threestops.utility.FontUtils;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class BankNewAccountActivity extends DaggerAppCompatActivity implements BankNewAccountContract.BankNewAccountView {

    @Inject
    BankNewAccountContract.BankNewAccountPresenter bankNewAccountPresenter;

    @Inject
    FontUtils fontUtils;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindString(R.string.addBank)
    String title;
    @BindString(R.string.save)
    String save;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.etState)
    EditText etState;
    @BindView(R.id.etPinCode)
    EditText etPinCode;
    @BindView(R.id.etAccountNo)
    EditText etAccountNo;
    @BindView(R.id.etRoutingNo)
    EditText etRoutingNo;

    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.tilPhone)
    TextInputLayout tilPhone;
    @BindView(R.id.tilAddress)
    TextInputLayout tilAddress;
    @BindView(R.id.tilCity)
    TextInputLayout tilCity;
    @BindView(R.id.tilState)
    TextInputLayout tilState;
    @BindView(R.id.tilPinCode)
    TextInputLayout tilPinCode;
    @BindView(R.id.tilAccountNo)
    TextInputLayout tilAccountNo;
    @BindView(R.id.tilRoutingNo)
    TextInputLayout tilRoutingNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this, bankNewAccountPresenter.getlanguageCode());
        setContentView(R.layout.activity_bank_new_account);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        ButterKnife.bind(this);
        bankNewAccountPresenter.setActionBar();
        initViews();
    }

    /**
     * <h2>initViews</h2>
     * <p>initializing the views(fonts and styles)</p>
     */
    private void initViews() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.vector_cancel);
        }

        etName.setTypeface(fontUtils.titaliumRegular());
        etPhone.setTypeface(fontUtils.titaliumRegular());
        etAddress.setTypeface(fontUtils.titaliumRegular());
        etCity.setTypeface(fontUtils.titaliumRegular());
        etState.setTypeface(fontUtils.titaliumRegular());
        etPinCode.setTypeface(fontUtils.titaliumRegular());
        etAccountNo.setTypeface(fontUtils.titaliumRegular());
        etRoutingNo.setTypeface(fontUtils.titaliumRegular());
        tvSave.setTypeface(fontUtils.titaliumSemiBold());

    }

    @OnClick({R.id.tvSave})
    public void onClick(View view) {
        if (view.getId() == R.id.tvSave) {
            bankNewAccountPresenter.validateData(etName.getText().toString(), etPhone.getText().toString(),
                    etAccountNo.getText().toString(),
                    etRoutingNo.getText().toString(), etAddress.getText().toString(), etCity.getText().toString(), etState.getText().toString(), etPinCode.getText().toString());
        }
    }

    @Override
    public void initActionBar() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_selector);
        }
        tvSave.setVisibility(View.VISIBLE);
        bankNewAccountPresenter.setActionBarTitle();

    }

    @Override
    public void setTitle() {
        tvSave.setText(save);
    }

    @Override
    public void editTextErr(String errorEditText) {

        switch (errorEditText) {

            case "Name":
                tilName.setErrorEnabled(true);
                tilName.setError(getString(R.string.enterAccountHoldername));
                etName.requestFocus();
                break;
            case "Phone":
                tilName.setErrorEnabled(false);
                tilPhone.setErrorEnabled(true);
                tilPhone.setError(getString(R.string.enterAccountHolderPhoneNumber));
                etPhone.requestFocus();
                break;
            case "AccountNo":
                tilName.setErrorEnabled(false);
                tilPhone.setErrorEnabled(false);
                tilAccountNo.setErrorEnabled(true);
                tilAccountNo.setError(getString(R.string.enterAccountNo));
                etAccountNo.requestFocus();

                break;
            case "RoutingNo":
                tilName.setErrorEnabled(false);
                tilPhone.setErrorEnabled(false);
                tilAccountNo.setErrorEnabled(false);
                tilRoutingNo.setErrorEnabled(true);
                tilRoutingNo.setError(getString(R.string.enterRoutinNo));
                etRoutingNo.requestFocus();
                break;
            case "Address":
                tilName.setErrorEnabled(false);
                tilPhone.setErrorEnabled(false);
                tilAccountNo.setErrorEnabled(false);
                tilRoutingNo.setErrorEnabled(false);
                tilAddress.setErrorEnabled(true);
                tilAddress.setError(getString(R.string.enterAddressBank));
                etAddress.requestFocus();
                break;
            case "City":
                tilName.setErrorEnabled(false);
                tilPhone.setErrorEnabled(false);
                tilAccountNo.setErrorEnabled(false);
                tilRoutingNo.setErrorEnabled(false);
                tilAddress.setErrorEnabled(false);
                tilCity.setErrorEnabled(true);
                tilCity.setError(getString(R.string.enterCityBank));
                etCity.requestFocus();
                break;
            case "State":
                tilName.setErrorEnabled(false);
                tilPhone.setErrorEnabled(false);
                tilAccountNo.setErrorEnabled(false);
                tilRoutingNo.setErrorEnabled(false);
                tilAddress.setErrorEnabled(false);
                tilCity.setErrorEnabled(false);
                tilState.setErrorEnabled(true);
                tilState.setError(getString(R.string.enterStateBank));
                etState.requestFocus();
                break;
            case "PinCode":
                tilName.setErrorEnabled(false);
                tilPhone.setErrorEnabled(false);
                tilAccountNo.setErrorEnabled(false);
                tilRoutingNo.setErrorEnabled(false);
                tilAddress.setErrorEnabled(false);
                tilCity.setErrorEnabled(false);
                tilState.setErrorEnabled(false);
                tilPinCode.setErrorEnabled(true);
                tilPinCode.setError(getString(R.string.enterPincodeBank));
                etPinCode.requestFocus();
                break;
            default:
                tilName.setErrorEnabled(false);
                tilPhone.setErrorEnabled(false);
                tilAccountNo.setErrorEnabled(false);
                tilRoutingNo.setErrorEnabled(false);
                tilAddress.setErrorEnabled(false);
                tilCity.setErrorEnabled(false);
                tilState.setErrorEnabled(false);
                tilPinCode.setErrorEnabled(false);
                bankNewAccountPresenter.externalAccountAPI(etName.getText().toString(), etPhone.getText().toString(),
                        etAccountNo.getText().toString(),
                        etRoutingNo.getText().toString(), etAddress.getText().toString(), etCity.getText().toString(), etState.getText().toString(), etPinCode.getText().toString());
                break;
        }

    }

    @Override
    public void externalAccountAPISuccess(String msg) {
        Utility.BlueToast(this, msg);
        onBackPressed();
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
