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
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.activity_bank_new_account);
        ButterKnife.bind(this);
        initViews();
    }

    /**
     * <h2>initViews</h2>
     * <p>initializing the views(fonts and styles)</p>
     */
    private void initViews()
    {
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.vector_cancel);
        }

        etAccountNo.setHint(presenter.getAccountNoHint());
        etName.setTypeface(fontUtils.titaliumRegular());
        etAccountNo.setTypeface(fontUtils.titaliumRegular());
        etRoutingNo.setTypeface(fontUtils.titaliumRegular());
        tvSave.setTypeface(fontUtils.titaliumSemiBold());

    }

    //event for save
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
