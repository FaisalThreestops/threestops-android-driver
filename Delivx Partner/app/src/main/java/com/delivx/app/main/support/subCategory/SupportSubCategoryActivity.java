package com.delivx.app.main.support.subCategory;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.delivx.adapter.SupportRVA;
import com.delivx.utility.Utility;
import com.driver.delivx.R;
import com.delivx.pojo.SupportData;
import com.delivx.utility.FontUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class SupportSubCategoryActivity extends DaggerAppCompatActivity implements SubCatContract.ViewOperation {


    @BindView(R.id.toolbar) Toolbar toolbar ;
    @BindView(R.id.tvTitle) TextView tvTitle ;
    @BindView(R.id.rvSupportSubCateogry) RecyclerView rvSupportSubCateogry ;

    @Inject SubCatContract.PresenterOperation presenter;
    @Inject
    FontUtils fontUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.activity_support_sub_category);
        ButterKnife.bind(this);
        presenter.getBundleData(getIntent());
        presenter.initActionBar();
    }

    @Override
    public void setView(ArrayList<SupportData> supportDatas) {

        rvSupportSubCateogry.setLayoutManager(new LinearLayoutManager(this));
        SupportRVA supportRVA = new SupportRVA(this, supportDatas, false);
        rvSupportSubCateogry.setAdapter(supportRVA);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_back_btn);
        }
    }

    @Override
    public void setActionBarTitle(String title) {
        tvTitle.setText(getResources().getString(R.string.support));
        tvTitle.setTypeface(fontUtils.titaliumSemiBold());
    }
}
