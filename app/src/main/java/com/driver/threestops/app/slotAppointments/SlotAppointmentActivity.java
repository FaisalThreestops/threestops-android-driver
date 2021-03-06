package com.driver.threestops.app.slotAppointments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.driver.threestops.adapter.SlotAppointmentRVA;
import com.driver.threestops.app.selectedStore.SelectedStoreIdActivity;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.pojo.AssignedAppointments;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Utility;
import com.driver.Threestops.R;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class SlotAppointmentActivity extends DaggerAppCompatActivity implements com.driver.threestops.app.slotAppointments.SlotContract.ViewOperations, SlotAppointmentRVA.PickupLoc {


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.rvBookingId)
    RecyclerView rvBookingId;

//    private ItemsListRVA itemsListRVA;

    private boolean tax_added = false;

    private Typeface font,fontBold;

    @Inject com.driver.threestops.app.slotAppointments.SlotContract.PresenterOperations presenter;
    @Inject FontUtils fontUtils;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    private Dialog dialog;
    private ArrayList<AssignedAppointments> appointments=new ArrayList<>();
    private ArrayList<AssignedAppointments> slotResponseAppointments=new ArrayList<>();
    private SlotAppointmentRVA slotAppointmentRVA;
    private int updIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.store_and_its_booking);
        ButterKnife.bind(this);
        presenter.getBundleData(getIntent().getExtras());
        initViews();
        presenter.setActionBar();
        presenter.setActionBarTitle();
        setBookingId();
    }

    private void initViews() {
        font= fontUtils.titaliumRegular();
        fontBold=fontUtils.titaliumSemiBold();
        slotAppointmentRVA=new SlotAppointmentRVA(this,appointments,slotResponseAppointments);
        rvBookingId.setLayoutManager(new LinearLayoutManager(this));
        rvBookingId.setAdapter(slotAppointmentRVA);
        slotAppointmentRVA.notifyDataSetChanged();
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
    public void initActionBar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.history_back_icon_off);
        }

        iv_search.setImageDrawable(getResources().getDrawable(R.drawable.ic_message_black_24dp));
        iv_search.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    @Override
    public void setViews(ArrayList<AssignedAppointments> appointments) {
        this.appointments.clear();
        this.appointments.addAll(appointments);
        slotResponseAppointments.clear();
        slotResponseAppointments.addAll(appointments);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void setBookingId(){
        if (appointments != null && appointments.size() > 0) {

            ArrayList<AssignedAppointments> assignedTripsTemp = new ArrayList<>();

            String previousStoreID = "", storeID = "";


            for (int i = 0; i < appointments.size(); i++) {

                AssignedAppointments assignedAppointments = appointments.get(i);
                if(assignedAppointments!=null) {
                    storeID = assignedAppointments.getStoreId();
                    if (!storeID.equals(previousStoreID)) {
                        assignedTripsTemp.add(assignedAppointments);
                    }
                    previousStoreID = storeID;
                }
            }
            appointments.clear();
            appointments.addAll(assignedTripsTemp);
            slotAppointmentRVA.notifyDataSetChanged();
        }
    }

    public void startActivity(ArrayList<AssignedAppointments> slotResponseAppointments,ArrayList<AssignedAppointments> assignedAppointments, int updIndex) {
        Intent intent1 = new Intent(this, SelectedStoreIdActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("data", slotResponseAppointments);
        bundle1.putSerializable("sortStoreId", assignedAppointments);
        bundle1.putInt("index",updIndex);
//                bundle.putString("title", viewHolder.tv_timer.getText().toString());
        intent1.putExtras(bundle1);
        startActivityForResult(intent1,111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getExtras();

                ArrayList<AssignedAppointments> updAppointments = (ArrayList<AssignedAppointments>) bundle.getSerializable("updAppointments");
                updIndex = bundle.getInt("updIndex");
                appointments.clear();
                appointments.addAll(updAppointments);
                slotResponseAppointments.clear();
                slotResponseAppointments.addAll(updAppointments);
                setBookingId();
            }
        }
    }

    @Override
    public void openGoogleMap(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    public void pickup(ArrayList<AssignedAppointments> slotResponseAppointments) {
        presenter.getDirection(slotResponseAppointments);
    }
}
