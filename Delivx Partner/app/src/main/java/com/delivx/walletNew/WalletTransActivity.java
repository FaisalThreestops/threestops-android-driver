package com.delivx.walletNew;


import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.delivx.R;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;
import com.delivx.walletNew.adapter.WalletViewPagerAdapter;
import com.delivx.walletNew.model.CreditDebitTransctions;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>WalletFragment</h1>
 * <p> Class to load WalletTransActivity and show all transactions list </p>
 */
public class WalletTransActivity extends DaggerAppCompatActivity
        implements WalletTransactionContract.WalletTrasactionView
{
    private ArrayList<CreditDebitTransctions> allTransactionsAL=new ArrayList<>();
    private ArrayList<CreditDebitTransctions> debitTransactionsAL;
    private ArrayList<CreditDebitTransctions> creditTransactionsAL;
    private WalletTransactionsFragment allTransactionsFrag, debitsFrag, creditsFrag;
//    private Alerts alerts;
    private ProgressDialog pDialog;

    Toolbar toolBar;
    TextView tvAbarTitle;
    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tvWalletBalance) TextView tvWalletBalance;

    @Inject FontUtils appTypeface;
    @Inject WalletTransactionContract.WalletTransactionPresenter walletTransPresenter;

    @BindString(R.string.recentTransactions) String recentTransactions;
    @BindString(R.string.all) String all;
    @BindString(R.string.debit) String debit;
    @BindString(R.string.credit) String credit;
    @BindString(R.string.wait) String wait;

    @BindDrawable(R.drawable.back_white_btn) Drawable back_white_btn;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,walletTransPresenter.getlanguageCode());
        setContentView(R.layout.activity_wallet_transactions);
        ButterKnife.bind(this);
//        alerts = new Alerts();
        initToolBar();
        initViews();
    }

    /* <h2>initToolBar</h2>
     * <p> method to initialize customer toolbar </p>
     */
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
        tvAbarTitle.setText(recentTransactions);
        rlABarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setAllTransactionsAL(ArrayList<CreditDebitTransctions> allTransactionsAL) {
        this.allTransactionsAL = allTransactionsAL;
    }

    public void setDebitTransactionsAL(ArrayList<CreditDebitTransctions> debitTransactionsAL) {
        this.debitTransactionsAL = debitTransactionsAL;
    }

    public void setCreditTransactionsAL(ArrayList<CreditDebitTransctions> creditTransactionsAL) {
        this.creditTransactionsAL = creditTransactionsAL;
    }

    @Override
    public void setWallet(String walletBalance, String inr) {
        tvWalletBalance.setText(inr+" "+Utility.formatPrice(walletBalance));
    }


    /**
     *<h2>initViews</h2>
     * <P> custom method to initializes all the views of the screen </P>
     */
    private void initViews()
    {
        viewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        String tabTitles[]  = new String[]{all, debit, credit};
        WalletViewPagerAdapter viewPagerAdapter = new WalletViewPagerAdapter(getSupportFragmentManager());


        this.allTransactionsFrag = WalletTransactionsFragment.getNewInstance();
        viewPagerAdapter.addFragment(allTransactionsFrag, tabTitles[0]);

        this.debitsFrag = WalletTransactionsFragment.getNewInstance();
        viewPagerAdapter.addFragment(debitsFrag, tabTitles[1]);

        this.creditsFrag = WalletTransactionsFragment.getNewInstance();
        viewPagerAdapter.addFragment(creditsFrag, tabTitles[2]);
        viewPagerAdapter.notifyDataSetChanged();

        viewPager.setAdapter(viewPagerAdapter);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        loadTransactions(false);
    }


    /**
     * <h2>loadTransactions</h2>
     * <p> method to init getTransactionsList api </p>
     */
    public void loadTransactions(boolean isFromOnRefresh)
    {
        walletTransPresenter.initLoadTransactions(false, isFromOnRefresh);
    }



    @Override
    public void hideProgressDialog()
    {
        if(pDialog.isShowing())
        {
            pDialog.dismiss();
        }
    }



    @Override
    public void showProgressDialog(String msg)
    {
        if(pDialog == null)
        {
            pDialog = new ProgressDialog(this);
        }

        if(!pDialog.isShowing())
        {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(wait);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }


    @Override
    public void showToast(String msg, int duration)
    {
        hideProgressDialog();
        Toast.makeText(this, msg, duration).show();
    }


    @Override
    public void showAlert(String title, String msg)
    {
        hideProgressDialog();
    }


    /**
     *<h2>showAlert</h2>
     * <p> method to show alert that these is no internet connectivity there </p>
     */
    @Override
    public void noInternetAlert()
    {
//        alerts.showNetworkAlert(this);
    }



    /**
     *<h2>walletTransactionsApiSuccessViewNotifier</h2>
     * <p> method to update fields data on the success response of api </p>
     */
    @Override
    public void walletTransactionsApiSuccessViewNotifier()
    {
        hideProgressDialog();
        String TAG = "WalletTransactionAct";
        Log.d(TAG, "walletTransactionsApiSuccessViewNotifier onSuccess: ");

        if(this.allTransactionsFrag != null)
        {
            this.allTransactionsFrag.hideRefreshingLayout();
            this.allTransactionsFrag.notifyDataSetChangedCustom(allTransactionsAL);
        }

        if(this.debitsFrag != null)
        {
            this.debitsFrag.hideRefreshingLayout();
            this.debitsFrag.notifyDataSetChangedCustom(debitTransactionsAL);
        }

        if(this.creditsFrag != null)
        {
            this.creditsFrag.hideRefreshingLayout();
            this.creditsFrag.notifyDataSetChangedCustom(creditTransactionsAL);
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

}

