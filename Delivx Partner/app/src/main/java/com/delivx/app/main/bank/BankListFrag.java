package com.delivx.app.main.bank;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delivx.adapter.BankDetailsRVA;
import com.delivx.app.main.bank.addBankAccount.BankNewAccountActivity;
import com.delivx.app.main.bank.stripe.BankNewStripeActivity;
import com.driver.delivx.R;
import com.delivx.pojo.bank.BankList;
import com.delivx.pojo.bank.LegalEntity;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;



public class BankListFrag extends DaggerFragment implements BankDetailscontract.ViewOperations,BankDetailsRVA.RefreshBankDetails, View.OnClickListener {

    private BankDetailsRVA bankDetailsRVA;
    private ArrayList<BankList> bankLists;

    @BindView(R.id.ivStatus) ImageView ivStatus;
    @BindView(R.id.tvStatus) TextView tvStatus;
    @BindView(R.id.tvStep2) TextView tvStep2;
    @BindView(R.id.tvAddStripeAccount) TextView tvAddStripeAccount;
    @BindView(R.id.tvStipeAccountNo) TextView tvStipeAccountNo;
    @BindView(R.id.tvAddBankAccount) TextView tvAddBankAccount;
    @BindView(R.id.tvStep1) TextView tvStep1;
    @BindView(R.id.cvStipeDetails) CardView cvStipeDetails;
    @BindView(R.id.cvLinkBankAcc) CardView cvLinkBankAcc;
    @BindView(R.id.rvBank) RecyclerView rvBank ;
    @BindView(R.id.progressBar) ProgressBar progressBar ;

    @Inject
    FontUtils fontUtils;

    @Inject
    BankListFragPresenter bankListFragPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bank_list_fragment2, container, false);
        ButterKnife.bind(this,rootView);

        init();
        bankListFragPresenter.attachView(this);
        return rootView;
    }

    /**
     * <h2>init</h2>
     * <p>intializing the views(Fonts and style)</p>
     */
    private void init() {

        tvStep2.setTypeface(fontUtils.titaliumRegular());
        tvStep1.setTypeface(fontUtils.titaliumRegular());
        tvStatus.setTypeface(fontUtils.titaliumRegular());
        tvStipeAccountNo.setTypeface(fontUtils.titaliumRegular());
        tvAddBankAccount.setTypeface(fontUtils.titaliumRegular());
        tvAddStripeAccount.setTypeface(fontUtils.titaliumRegular());

        rvBank.setLayoutManager(new LinearLayoutManager(getActivity()));
        bankLists = new ArrayList<>();
        bankDetailsRVA = new BankDetailsRVA(getActivity(), bankLists, getFragmentManager(), this);
        rvBank.setAdapter(bankDetailsRVA);


        tvAddBankAccount.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.printLog("BankListFrag onResume...");
        bankListFragPresenter.getBankDetails();
    }


    @Override
    public void onFailure(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure() {
        Toast.makeText(getActivity(), getString(R.string.serverError), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(LegalEntity legalEntity, ArrayList<BankList> bankLists) {
        VariableConstant.IS_STRIPE_ADDED = true;
        tvAddStripeAccount.setVisibility(View.GONE);

        tvStatus.setText(legalEntity.getVerification().getStatus());

        if (legalEntity.getVerification().getStatus().equals("verified")) {
            ivStatus.setImageResource(R.drawable.verified);
            tvStatus.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
            tvAddBankAccount.setFocusable(true);
            tvAddBankAccount.setOnClickListener(this);
            cvStipeDetails.setOnClickListener(null);
            if(bankLists.size()>0){
                cvLinkBankAcc.setVisibility(View.GONE);
            }

        } else {
            ivStatus.setImageResource(R.drawable.unverified);
            tvStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
            tvAddBankAccount.setFocusable(false);
            cvStipeDetails.setOnClickListener(this);
            tvAddBankAccount.setOnClickListener(null);
            tvAddBankAccount.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_corner_bank_acc_gunsmoke));
            tvAddBankAccount.setTextColor(getResources().getColor(R.color.gunsmoke));
        }

        this.bankLists.clear();
        this.bankLists.addAll(bankLists);
        bankDetailsRVA.notifyDataSetChanged();


    }

    @Override
    public void showAddStipe() {
        tvAddStripeAccount.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.GONE);
        ivStatus.setVisibility(View.GONE);
        tvStipeAccountNo.setVisibility(View.GONE);
        cvStipeDetails.setOnClickListener(this);
        tvAddBankAccount.setOnClickListener(null);
        tvAddBankAccount.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_corner_bank_acc_gunsmoke));
        tvAddBankAccount.setTextColor(getResources().getColor(R.color.gunsmoke));
    }

    @Override
    public void moveToAddAccountActivity() {
            Intent intent = new Intent(getActivity(), BankNewAccountActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.bottom_to_top, R.anim.stay);
    }

    @Override
    public void moveToNewStripeActivity(Bundle bundleBankDetails) {
        Intent intent1 = new Intent(getActivity(), BankNewStripeActivity.class);
        intent1.putExtras(bundleBankDetails);
        startActivity(intent1);
        getActivity().overridePendingTransition(R.anim.bottom_to_top, R.anim.stay);
    }

    @Override
    public void onRefresh() {
        bankListFragPresenter.getBankDetails();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //event for addBankaccount
            case R.id.tvAddBankAccount:
                bankListFragPresenter.addNewAccount();
                break;

                //event for stripeAccount
            case R.id.cvStipeDetails:
            case R.id.tvAddStripeAccount:
                bankListFragPresenter.addNewStripeAccount();
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
