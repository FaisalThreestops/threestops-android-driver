package com.driver.threestops.app.main.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.driver.Threestops.R;
import com.driver.threestops.adapter.BankDetailsRVA;
import com.driver.threestops.app.main.bank.addBankAccount.BankNewAccountActivity;
import com.driver.threestops.app.main.bank.bankAccountDetails.BankBottomSheetFragment;
import com.driver.threestops.app.main.bank.stripe.BankNewStripeActivity;
import com.driver.threestops.app.main.bank.stripe.GetBankData;
import com.driver.threestops.pojo.bank.BankList;
import com.driver.threestops.pojo.bank.LegalEntity;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Utility;
import com.driver.threestops.utility.VariableConstant;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;


public class BankListFrag extends DaggerFragment implements BankDetailscontract.ViewOperations, BankDetailsRVA.RefreshBankDetails, View.OnClickListener {

    private BankDetailsRVA bankDetailsRVA;
    private ArrayList<BankList> bankLists;

    @BindView(R.id.ivStatus)
    ImageView ivStatus;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvStep2)
    TextView tvStep2;
    @BindView(R.id.tvAddStripeAccount)
    TextView tvAddStripeAccount;
    @BindView(R.id.tvStipeAccountNo)
    TextView tvStipeAccountNo;
    @BindView(R.id.tvAddBankAccount)
    TextView tvAddBankAccount;
    @BindView(R.id.tvStep1)
    TextView tvStep1;
    @BindView(R.id.cvStipeDetails)
    CardView cvStipeDetails;
    @BindView(R.id.cvLinkBankAcc)
    CardView cvLinkBankAcc;
    @BindView(R.id.rvBank)
    RecyclerView rvBank;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ivCheck)
    ImageView ivCheck;
    @BindView(R.id.tvAccountNoLabel)
    TextView tvAccountNoLabel;
    @BindView(R.id.tvAccountNo)
    TextView tvAccountNo;
    @BindView(R.id.tvAccountHolderLabel)
    TextView tvAccountHolderLabel;
    @BindView(R.id.tvAccountHolder)
    TextView tvAccountHolder;
    @BindView(R.id.llBankDetails)
    LinearLayout llBankDetails;
    @BindView(R.id.cvBankDetails)
    CardView cvBankDetails;
    @BindView(R.id.ll_bankDetails)
    LinearLayout ll_bankDetails;
    @BindView(R.id.tvDeleteBankAccount)
    TextView tvDeleteBankAccount;
    GetBankData getBankData;
    @Inject
    FontUtils fontUtils;

    String BeneId = "";

    @Inject
    BankListFragPresenter bankListFragPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bank_list_fragment2, container, false);
        ButterKnife.bind(this, rootView);

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
        cvBankDetails.setOnClickListener(this);
        tvDeleteBankAccount.setOnClickListener(this);

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
            if (bankLists.size() > 0) {
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
        tvAddBankAccount.setFocusable(true);
        tvAddBankAccount.setOnClickListener(this);
        cvBankDetails.setFocusable(true);
        cvBankDetails.setOnClickListener(this);
        /*tvAddStripeAccount.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.GONE);
        ivStatus.setVisibility(View.GONE);
        tvStipeAccountNo.setVisibility(View.GONE);
        cvStipeDetails.setOnClickListener(this);
        tvAddBankAccount.setOnClickListener(null);
        tvAddBankAccount.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_corner_bank_acc_gunsmoke));
        tvAddBankAccount.setTextColor(getResources().getColor(R.color.gunsmoke));*/
    }

    @Override
    public void onSuccessData(GetBankData getBankData) {
        if (getBankData.getBankAccount() != null) {
            this.getBankData = getBankData;
            BeneId = getBankData.getBeneId();
            cvBankDetails.setVisibility(View.VISIBLE);
            cvLinkBankAcc.setVisibility(View.GONE);
            tvAccountHolder.setText(getBankData.getName());
            String subStr = getBankData.getBankAccount();
            tvAccountNo.setText("xxxxxxxx" + subStr.substring(10, subStr.length()));
        } else {
            cvBankDetails.setVisibility(View.GONE);
            cvLinkBankAcc.setVisibility(View.VISIBLE);
        }
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
    public void showAddBankAccount() {
        cvBankDetails.setVisibility(View.GONE);
        cvLinkBankAcc.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        bankListFragPresenter.getBankDetails();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //event for addBankaccount
            case R.id.tvAddBankAccount:
                bankListFragPresenter.addNewAccount();
                break;

            //event for stripeAccount
            case R.id.cvStipeDetails:
            case R.id.tvAddStripeAccount:
                bankListFragPresenter.addNewStripeAccount();
                break;

            case R.id.tvDeleteBankAccount:
//          Toast.makeText(getContext(), "Delete Bank Account", Toast.LENGTH_SHORT).show();
                bankListFragPresenter.deleteBankAccount(BeneId);
                break;

            case R.id.cvBankDetails:
                BottomSheetDialogFragment bottomSheetDialogFragment = BankBottomSheetFragment.newInstance(getBankData, this);
                bottomSheetDialogFragment.show(getFragmentManager(), bottomSheetDialogFragment.getTag());
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
