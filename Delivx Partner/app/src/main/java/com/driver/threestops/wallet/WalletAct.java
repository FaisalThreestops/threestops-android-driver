package com.driver.threestops.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

import com.gocashfree.cashfreesdk.CFPaymentService;
import com.google.android.material.textfield.TextInputLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.payment.Cards;
import com.driver.threestops.payment_add_card.AddCardAct;
import com.driver.threestops.payment_choose_card.ChoosePaymentAdapter;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Utility;
import com.driver.threestops.walletNew.WalletTransActivity;
import com.driver.Threestops.R;
import dagger.android.support.DaggerAppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;

public class WalletAct extends DaggerAppCompatActivity implements WalletView, View.OnClickListener {
    private Typeface fontMedium, fontRegular;


    @BindView(R.id.addMoneyBtn)
    Button addMoneyBtn;
    @BindView(R.id.priceTv)
    TextView priceTv;
    @BindView(R.id.balanceTxtTv)
    TextView balanceTxtTv;
    @BindView(R.id.addMoneytxtTv)
    TextView addMoneytxtTv;
    @BindView(R.id.amountTv)
    TextView amountTv;
    @BindView(R.id.addAmountEt)
    EditText addAmountEt;

    @BindView(R.id.historyTv)
    TextView historyTv;
    @BindView(R.id.softLimitTxtTv)
    TextView softLimitTxtTv;
    @BindView(R.id.softLimitTv)
    TextView softLimitTv;
    @BindView(R.id.hardLimitTxtTv)
    TextView hardLimitTxtTv;
    @BindView(R.id.hardLimitTv)
    TextView hardLimitTv;
    @BindView(R.id.cardsListRv)
    RecyclerView cardsListRv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.newCardRl)
    RelativeLayout newCardRl;
    @BindView(R.id.payRl)
    RelativeLayout payRl;

    @BindView(R.id.tvSoftLimit)
    TextView tvSoftLimit;
    @BindView(R.id.tvHardLimit)
    TextView tvHardLimit;
    @BindView(R.id.voucherTv) TextView voucherTv;
    @BindView(R.id.amountTl)
    TextInputLayout amountTl;
    @BindDrawable(R.drawable.back_white_btn)
    Drawable back_white_btn;

    Toolbar toolBar;
    TextView tvAbarTitle;
    private CFPaymentService cfPaymentService;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    private ChoosePaymentAdapter paymentAdapter;

    @Inject
    WalletPresenter mPresenter;
    /*@Inject
    VoucherBottomSheet voucherBottomSheet;*/
    @Inject
    FontUtils appTypeface;


    private ArrayList<Cards> cardList = new ArrayList<>();
    private String cardId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,preferenceHelperDataSource.getLanguageSettings().getLanguageCode());
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        mInitialization();
        initToolBar();

        setTypeFaces();

    }

    private void setTypeFaces() {

        priceTv.setTypeface(fontMedium);
        addMoneyBtn.setTypeface(fontMedium);
        addAmountEt.setTypeface(fontMedium);
        balanceTxtTv.setTypeface(fontMedium);
        addMoneytxtTv.setTypeface(fontMedium);
        amountTv.setTypeface(fontMedium);

        softLimitTxtTv.setTypeface(fontRegular);
        softLimitTv.setTypeface(fontRegular);
        hardLimitTv.setTypeface(fontRegular);
        hardLimitTxtTv.setTypeface(fontRegular);
        historyTv.setTypeface(fontMedium);

        tvSoftLimit.setTypeface(fontRegular);
        tvHardLimit.setTypeface(fontRegular);
        String softLimit = "<font color='#222224'>" + getString(R.string.softLimit) + "</font> " + getString(R.string.softLimitDesc);
        String hardLimit = "<font color='#222224'>" + getString(R.string.hardLimit) + "</font> " + getString(R.string.hardLimitDesc);
        tvSoftLimit.setText(Html.fromHtml(softLimit));
        tvHardLimit.setText(Html.fromHtml(hardLimit));
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void initToolBar()
    {
        toolBar=findViewById(R.id.toolbar);
        tvAbarTitle=findViewById(R.id.tvAbarTitle);
        final RelativeLayout rlABarBackBtn = findViewById(R.id.rlABarBackBtn);
        setSupportActionBar(toolBar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(back_white_btn);
        }

        tvAbarTitle.setTypeface(appTypeface.clanaproNarrMedium());
        tvAbarTitle.setText(getResources().getString(R.string.flexyCoin));
        rlABarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void mInitialization() {


        fontMedium = appTypeface.getFont_muli_semi_bold();
        fontRegular = appTypeface.getFont_muli_regular();

        mPresenter.callApi();

        RecyclerView.LayoutManager mlayoutManager;
        paymentAdapter = new ChoosePaymentAdapter(this, cardList, fontRegular);
        cardsListRv.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        cardsListRv.setLayoutManager(mlayoutManager);
        cardsListRv.setItemAnimator(new DefaultItemAnimator());
        cardsListRv.setAdapter(paymentAdapter);

        cardsListRv.setNestedScrollingEnabled(false);


        addAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (cardId != null && cardList.size() > 0) {
                    payRl.setSelected(true);
                }
                if(addAmountEt.getText().toString().equals(""))
                {
                    payRl.setSelected(false);

                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getcards();
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
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @OnClick({ R.id.addMoneyBtn, R.id.historyTv, R.id.newCardRl, R.id.voucherTv})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.addMoneyBtn:
                Utility.hideSoftKeyboard(addAmountEt);
                if (payRl.isSelected()) {
                    confirmAlert();
                }

                break;

            case R.id.historyTv:
                mPresenter.startHistory();
                break;
            case R.id.newCardRl:
                mPresenter.addCard();
                break;

           case R.id.voucherTv:
             /*  voucherBottomSheet.setCancelable(true);

               voucherBottomSheet.show(getSupportFragmentManager(), voucherBottomSheet.getTag());*/
                break;

            default:
                break;
        }


    }

    public void getAmount()
    {
        mPresenter.callApi();
    }

    void confirmAlert() {
        String amt = mPresenter.getCurrency() + " " + addAmountEt.getText().toString();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);

        alertDialog.setTitle(getString(R.string.alert));
        alertDialog.setMessage(getString(R.string.addMoneyConfirm) + " " + amt + " ?");
        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.chooseCard(addAmountEt.getText().toString());
                    }
                });
        alertDialog.show();
    }

    void mSuccessAlert() {
        String amt = mPresenter.getCurrency() + " " + addAmountEt.getText().toString();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.success));
        alertDialog.setCancelable(false);

        alertDialog.setMessage(getString(R.string.addMoneySuccess) + " " + amt);


        alertDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addAmountEt.setText("");

                    }
                });
        alertDialog.show();
    }

    @Override
    public void startAddCardAct() {
        Intent intent = new Intent(this, AddCardAct.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
    }

    @Override
    public void showSuccessAlert() {

        payRl.setSelected(false);
        mSuccessAlert();
    }

    public void setCardId(String id) {
        cardId = id;
        if(addAmountEt.getText()!=null && !addAmountEt.getText().toString().equals(""))
        {
            payRl.setSelected(true);

        }
    }



    @Override
    public void setFixedAmount(String amountCurrency, String softLimit, String hardLimit) {

        softLimitTv.setText(softLimit);
        hardLimitTv.setText(hardLimit);
      //  addAmountEt.setHint(amountCurrency);
        amountTl.setHint(amountCurrency);

    }

    @Override
    public void setLimitAmount(String softLimit, String hardLimit) {
        softLimitTv.setText(softLimit);
        hardLimitTv.setText(hardLimit);
    }

    @Override
    public void setTotalBalance(String balance) {
        priceTv.setText(balance);
    }

    @Override
    public void addAmt(String amt) {
        addAmountEt.setText(amt);
        addAmountEt.setSelection(addAmountEt.getText().length());


    }

    @Override
    public void setPaymentCardsList(ArrayList<Cards> cardsList) {

        if (cardsList.size() > 0) {
            cardList.clear();
            cardList.addAll(cardsList);
            paymentAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void startWalletHistory() {
        Intent intent = new Intent(this, WalletTransActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1111:
                    mPresenter.callApi();
                    break;
                default:
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null)
                            this.cardId = bundle.getString("orderId");
                        if (this.cardId != null && !this.cardId.equals("")) {
                            mPresenter.chooseCard(addAmountEt.getText().toString(), cardId);
                        }
                    }
            }
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

    @Override
    public void callCashFree(String orderId, String orderNote, String stage, String appId, String token, String finalTotal, String name, String phone, String email) {
        cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, finalTotal);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, name);
        params.put(PARAM_CUSTOMER_PHONE, phone);
        params.put(PARAM_CUSTOMER_EMAIL, email);

        cfPaymentService.doPayment(this, params, token, stage, "#000850", "#FFFFFF", false);
    }

}
