package com.delivx.payment_choose_card;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.payment.CardData;
import com.delivx.payment.Cards;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;
import com.driver.delivx.R;
import dagger.android.support.DaggerAppCompatActivity;
import java.util.ArrayList;
import javax.inject.Inject;

public class ChoosePaymentAct extends DaggerAppCompatActivity implements ChoosePaymentView {

    private Typeface fontMedium;
    private Typeface fontRegular;


    @BindView(R.id.walletTxtTv) TextView walletTxtTv;
    @BindView(R.id.walletTv) TextView walletTv;
    @BindView(R.id.walletAmountTv) TextView walletAmountTv;
    @BindView(R.id.creditAndDebitTv) TextView creditAndDebitTv;
    @BindView(R.id.addNewCardTv) TextView addNewCardTv;
    @BindView(R.id.payYourOrderTv) TextView payYourOrderTv;
    @BindView(R.id.payOnDelTv) TextView payOnDelTv;
    @BindView(R.id.payOnDelTxtTv) TextView payOnDelTxtTv;
    @BindView(R.id.payDelTv) TextView payDelTv;
    @BindView(R.id.cardsListRv)
    RecyclerView cardsListRv;
    @BindView(R.id.payOnDelRl) RelativeLayout payOnDelRl;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.payOnDelTickIv) ImageView payOnDelTickIv;
    @BindView(R.id.newCardRl) RelativeLayout newCardRl;
    @BindView(R.id.walletAmountRl) RelativeLayout walletAmountRl;
    @BindView(R.id.walletPayTv) TextView walletPayTv;
    @BindView(R.id.addMoneyTv) TextView addMoneyTv;
    @BindView(R.id.walletPayLl) LinearLayout walletPayLl;
    @BindView(R.id.walletTickIv) ImageView walletTickIv;

    @BindView(R.id.idealLabelTxtTv) TextView idealLabelTxtTv;
    @BindView(R.id.idealRl) RelativeLayout idealRl;
    @BindView(R.id.idealTv) TextView idealTv;
    @BindView(R.id.idealTickIv) ImageView idealTickIv;
    @BindView(R.id.payOnIdealTv) TextView payOnIdealTv;

    Toolbar toolBar;
    TextView tvAbarTitle;


    @Inject
    FontUtils mFontUtils;
    @Inject
    ChoosePaymentPresenter mPresenter;
    @Inject
    PreferenceHelperDataSource mPreferenceHelperDataSource;
    @BindDrawable(R.drawable.back_white_btn)
    Drawable back_white_btn;

    private ChoosePaymentAdapter paymentAdapter;
    private ArrayList<Cards> cardList = new ArrayList<>();
    private boolean hideWallet=false;

    private int paymentType = 0;
    private int isPayByWallet = 0;
    private boolean isSendAnything = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(ChoosePaymentAct.this,mPreferenceHelperDataSource.getLanguage());
        setContentView(R.layout.activity_choose_payment);
        ButterKnife.bind(this);
        mInitialization();
        initToolBar();
        setTypeFaces();

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

        tvAbarTitle.setTypeface(mFontUtils.clanaproNarrMedium());
        tvAbarTitle.setText(getResources().getString(R.string.paymentoptions));
        rlABarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void mInitialization()
    {

        //obtaining the typeface objects
        fontMedium = mFontUtils.getFont_muli_semi_bold();
        fontRegular = mFontUtils.getFont_muli_regular();

        if(getIntent()!=null && getIntent().hasExtra("hideWallet"))
        {
            hideWallet=getIntent().getBooleanExtra("hideWallet",false);
        }
        if (getIntent()!=null && getIntent().getBooleanExtra("isSendAnything", false)) {
            this.isSendAnything = getIntent().getBooleanExtra("isSendAnything", false);
        }
        if (hideWallet)
        {
            walletAmountRl.setVisibility(View.GONE);
            walletTxtTv.setVisibility(View.GONE);
        }

        RecyclerView.LayoutManager mlayoutManager;
        paymentAdapter = new ChoosePaymentAdapter(this, cardList, fontRegular);
        cardsListRv.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        cardsListRv.setLayoutManager(mlayoutManager);
        cardsListRv.setItemAnimator(new DefaultItemAnimator());
        cardsListRv.setAdapter(paymentAdapter);


        if (getIntent() != null && getIntent().hasExtra("paymentType")) {
            paymentType = getIntent().getIntExtra("paymentType", 0);
            isPayByWallet = getIntent().getIntExtra("isPayByWallet", 0);
        }

        if ((paymentType == 0 && isPayByWallet == 1)) { /*wallet*/
            showWalletPayment();
        } else if (paymentType == 1 && isPayByWallet == 1) { /*card + wallet*/
            showWalletPayment();
            paymentAdapter.setPaymentType(paymentType);
            paymentAdapter.notifyDataSetChanged();
        } else if (paymentType == 2 && isPayByWallet == 1) { /*cash + wallet*/
            showCashBtn();
            showWalletPayment();
        } else if (paymentType == 1 && isPayByWallet == 0) {/*card*/
            paymentAdapter.setPaymentType(paymentType);
            paymentAdapter.notifyDataSetChanged();
        } else if (paymentType == 2 && isPayByWallet == 0) {/*cash*/
            showCashBtn();
        }

        if (isSendAnything) {
            payOnDelTxtTv.setVisibility(View.GONE);
            payOnDelRl.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getCards();
    }

    private void setTypeFaces()
    {
        walletTxtTv.setTypeface(fontMedium);
        walletTv.setTypeface(fontMedium);
        walletAmountTv.setTypeface(fontMedium);
        creditAndDebitTv.setTypeface(fontMedium);
        payDelTv.setTypeface(fontMedium);
        payOnIdealTv.setTypeface(fontMedium);
        addNewCardTv.setTypeface(fontMedium);
        payYourOrderTv.setTypeface(fontMedium);
        payOnDelTv.setTypeface(fontRegular);
        walletPayTv.setTypeface(fontRegular);
        addMoneyTv.setTypeface(fontRegular);
        payOnDelTxtTv.setTypeface(fontMedium);

        idealLabelTxtTv.setTypeface(fontMedium);
        idealTv.setTypeface(fontMedium);
    }



    @Override
    public void onError(String msg) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPaymentCardsList(ArrayList<Cards> cardsList) {

        if (cardsList.size() > 0) {
            cardList.clear();
            cardList.addAll(cardsList);
            paymentAdapter.notifyDataSetChanged();
        }

    }

    public void setSelectedCardId(String id, String cardNumber, String brand)
    {

        Intent intent = new Intent();
        intent.putExtra("paymentType", "card");
        intent.putExtra("isPayByWallet", isPayByWallet);
        intent.putExtra("cardId", id);
        intent.putExtra("cardNo", cardNumber);
        intent.putExtra("brand", brand);
        setResult(RESULT_OK, intent);
        finish();

        }

    @Override
    public void showCashBtn()
    {
        paymentAdapter.unSelectItem();
//        hideWalletPay();
        hideIdeal();
        payOnDelTickIv.setImageResource(R.drawable.ic_check_icon_on);
        payOnDelTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCashBtn() {
        payOnDelTickIv.setImageResource(R.drawable.ic_check_off);
        payOnDelTv.setVisibility(View.GONE);

    }

    @Override
    public void startAddCardAct() {
      /*  Intent intent = new Intent(this, AddCardAct.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);*/
    }

    @Override
    public void showWalletPayment() {
//        paymentAdapter.unSelectItem();
//        hideCashBtn();
        hideIdeal();
        walletPayLl.setVisibility(View.VISIBLE);
        walletTickIv.setImageResource(R.drawable.ic_check_icon_on);

    }

    @Override
    public void payWallet(String amt) {
        Intent intent = new Intent();
        intent.putExtra("paymentType", "wallet");
        intent.putExtra("isPayByWallet", isPayByWallet);
        intent.putExtra("amount", amt);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void hideWalletPay() {
        walletPayLl.setVisibility(View.GONE);
        walletTickIv.setImageResource(R.drawable.ic_check_off);
    }

    @Override
    public void setWalletAmount(String amount) {
        walletAmountTv.setText(amount);
    }

    @Override
    public void addWallet() {
       /* Intent intent=new Intent(this,WalletAct.class);
        startActivity(intent);*/
    }

    @Override
    public void disableWalletPay() {
        walletPayTv.setEnabled(false);
        walletPayTv.setBackgroundResource(R.drawable.diable_button_bg);
    }

    @Override
    public void enableWalletPay() {
        walletPayTv.setEnabled(true);
        walletPayTv.setBackgroundResource(R.drawable.button_bag);
    }


    private void showIdeal() {
        idealTickIv.setImageResource(R.drawable.ic_check_icon_on);
        payOnIdealTv.setVisibility(View.VISIBLE);
        hideCashBtn();
        hideWalletPay();
    }

    private void hideIdeal() {
        idealTickIv.setImageResource(R.drawable.ic_check_off);
        payOnIdealTv.setVisibility(View.GONE);
    }

    private void idealSelected()
    {
        Intent intent = new Intent();
        intent.putExtra("paymentType", "ideal");
        setResult(RESULT_OK, intent);
        finish();
    }


    @OnClick({ R.id.payOnDelRl, R.id.payOnDelTv, R.id.newCardRl,
            R.id.walletAmountRl, R.id.walletPayTv, R.id.addMoneyTv, R.id.payOnIdealTv, R.id.idealRl})
    public void onClick(View view) {
        if (progressBar.getVisibility() == View.VISIBLE) {
            return;
        }
        switch (view.getId())
        {
            case R.id.walletPayTv:
              mPresenter.payWallet();
                break;

            case R.id.addMoneyTv:
                addWallet();
                break;

            case R.id.walletAmountRl:
                if (walletAmountRl.isSelected()) {
                    walletAmountRl.setSelected(false);
                    hideWalletPay();
                    isPayByWallet = 0;
                } else {
                    walletAmountRl.setSelected(true);
                    showWalletPayment();
                    if(mPresenter.canSelectWallet())
                        isPayByWallet = 1;
                    else
                        isPayByWallet = 0;
                }
                break;

            case R.id.idealRl:
                showIdeal();
                break;

            case R.id.payOnIdealTv:
                idealSelected();
                break;

            case R.id.newCardRl:
                mPresenter.addNewCard();
                break;

            case R.id.payOnDelRl:
                if (payOnDelTv.getVisibility() == View.VISIBLE)
                {
                    mPresenter.hideCashBtn();
                } else {
                    mPresenter.showCashBtn();
                }
                break;

            case R.id.payOnDelTv:
                Intent intent1 = new Intent();
                intent1.putExtra("paymentType", "cash");
                intent1.putExtra("isPayByWallet", isPayByWallet);
                setResult(RESULT_OK, intent1);
                finish();
                break;

            default:
                break;

        }
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
