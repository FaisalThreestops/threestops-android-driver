package com.delivx.vehiclelist;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.delivx.app.main.MainActivity;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.logout.LogoutPopup;
import com.driver.delivx.R;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.SigninDriverVehicle;
import com.delivx.utility.Utility;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

/**************************************************************************************************/
public class VehicleList extends DaggerAppCompatActivity implements View.OnClickListener,VehicleListView {

    public ArrayList<SigninDriverVehicle> vDataList=new ArrayList<>();
    @Inject VechicleListRVA currentJobRVA;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_title)  TextView tv_title;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_logout) TextView tv_logout;
    @BindView(R.id.tv_confirm) TextView tv_confirm ;
    @BindView(R.id.rv_vehiclelist) RecyclerView rv_vehiclelist;
    @BindString(R.string.selectVeh_vl) String title;
    @BindString(R.string.message) String msg;

    @Inject VehicleListPresenter presenter;


    private Typeface ClanaproNarrMedium;


    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vehicle_list);

        ButterKnife.bind(this);

        currentJobRVA.setData(vDataList);

        presenter.setActionBar();

        initializeViews();

    }

    public void initActionBar() {

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_signup_close);
        }
        ClanaproNarrMedium = Typeface.createFromAsset(getAssets(), "fonts/ClanPro-NarrMedium.otf");

        tv_title.setTypeface(ClanaproNarrMedium);

        tv_logout.setVisibility(View.VISIBLE);

        tv_logout.setTypeface(ClanaproNarrMedium);

        iv_search.setVisibility(View.GONE);

        presenter.setActionBarTitle();

    }

    @Override
    public void setTitle() {

        tv_title.setText(title);
    }

    @Override
    public void setListData(ArrayList<SigninDriverVehicle> listData) {
        vDataList.clear();
        vDataList.addAll(listData);
    }

    @Override
    public void notifyAdapter() {
        currentJobRVA.notifyDataSetChanged();
    }

    @Override
    public void onError(String error) {
        Utility.mShowMessage(msg,error,this);
    }

    @Override
    public void onSuccess(String result) {
//        Utility.mShowMessage(msg,result,this);

        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void logout(PreferenceHelperDataSource dataSource, NetworkService networkService) {
        LogoutPopup logoutPopup = new LogoutPopup(VehicleList.this,dataSource,networkService);
        logoutPopup.setCancelable(false);
        logoutPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logoutPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutPopup.show();
    }


    private void initializeViews() {

        tv_confirm.setTypeface(ClanaproNarrMedium);

        rv_vehiclelist.setLayoutManager(new LinearLayoutManager(this));
        rv_vehiclelist.setNestedScrollingEnabled(true);
        rv_vehiclelist.setAdapter(currentJobRVA);

        presenter.getList();

    }

    @OnClick({R.id.tv_logout,R.id.tv_confirm})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_logout:
               presenter.logoutOnclick();
                break;

            case R.id.tv_confirm:
                presenter.confirmOnclick();
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
