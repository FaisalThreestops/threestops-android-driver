package com.delivx.app.main.bank.addBankAccount;

import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.delivx.R;
import com.delivx.utility.FontUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class BankNewAccountActivity extends DaggerAppCompatActivity implements AddBankAccountContract.ViewOperation{
    @Inject
    BankNewAccountPresenter presenter;

    @Inject
    FontUtils fontUtils;

    @BindView(R.id.etName) EditText etName;
    @BindView(R.id.etAccountNo) EditText etAccountNo;
    @BindView(R.id.etRoutingNo) EditText etRoutingNo;
    @BindView(R.id.tilName) TextInputLayout tilName;
    @BindView(R.id.tilAccountNo) TextInputLayout tilAccountNo;
    @BindView(R.id.tilRoutingNo) TextInputLayout tilRoutingNo;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvSave) TextView tvSave ;
    @BindView(R.id.progressBar) ProgressBar progressBar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_new_account);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews()
    {
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.vector_cancel);
        }

        etName.setTypeface(fontUtils.titaliumRegular());
        etAccountNo.setTypeface(fontUtils.titaliumRegular());
        etRoutingNo.setTypeface(fontUtils.titaliumRegular());
        tvSave.setTypeface(fontUtils.titaliumSemiBold());

    }


    @OnClick(R.id.tvSave)
    public void onClick(View v){
        presenter.validateFields(etName.getText().toString(),etAccountNo.getText().toString(),etRoutingNo.getText().toString());
    }
    @Override
    public void disableError() {

            tilName.setErrorEnabled(false);
            tilAccountNo.setErrorEnabled(false);
            tilRoutingNo.setErrorEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
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
    public void onSuccess(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onFailure() {
        Toast.makeText(this,getString(R.string.accountAdditionError),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNameError() {
        tilName.setErrorEnabled(true);
        tilName.setError(getString(R.string.enterAccountHoldername));
    }

    @Override
    public void setAccError() {
        tilName.setErrorEnabled(false);

        tilAccountNo.setErrorEnabled(true);
        tilAccountNo.setError(getString(R.string.enterAccountNo));
    }

    @Override
    public void setRoutingError() {
        tilName.setErrorEnabled(false);
        tilAccountNo.setErrorEnabled(false);

        tilRoutingNo.setErrorEnabled(true);
        tilRoutingNo.setError(getString(R.string.enterRoutinNo));
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
