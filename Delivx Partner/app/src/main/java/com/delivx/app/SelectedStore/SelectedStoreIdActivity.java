package com.delivx.app.SelectedStore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delivx.adapter.Set_the_order_list_rva;
import com.delivx.adapter.SortedStoreIDRVA;
import com.delivx.app.bookingRide.BookingRide;
import com.delivx.app.invoice.InvoiceActivity;
import com.delivx.app.storePickUp.StorePickUp;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.mqttChat.ChattingActivity;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.ShipmentDetails;
import com.delivx.utility.AppConstants;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Slider;
import com.delivx.utility.Utility;
import com.driver.delivx.R;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class SelectedStoreIdActivity extends DaggerAppCompatActivity implements SelectedStoreIdContract.ViewOperations, Set_the_order_list_rva.ErrorMessage, SortedStoreIDRVA.PickupLoc {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.myseek)
    Slider seekbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rvSelectedStoreID_BID)
    RecyclerView recyclerView;

    @BindView(R.id.Bottomlayout)
    RelativeLayout hideSeekBar;


    private Typeface font, fontBold;
    private ArrayList<AssignedAppointments> appointments = new ArrayList<>();
    private ArrayList<AssignedAppointments> sortStoreId = new ArrayList<>();
    private SortedStoreIDRVA sortedStoreIDRVA;
    private int upIndexToItems;

    @Inject
    SelectedStoreIdContract.PresenterOperations presenter;
    @Inject
    FontUtils fontUtils;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    private Dialog dialog;
    private int updIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this, presenter.getlanguageCode());
        setContentView(R.layout.selected_storeid);
        ButterKnife.bind(this);
        presenter.getBundleData(getIntent().getExtras());
        presenter.setActionBar();
        presenter.setActionBarTitle();
        initViews();
    }

    private void initViews() {
        font = fontUtils.titaliumRegular();
        fontBold = fontUtils.titaliumSemiBold();
        sortedStoreIDRVA = new SortedStoreIDRVA(this, appointments, sortStoreId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sortedStoreIDRVA);
        sortedStoreIDRVA.notifyDataSetChanged();

        seekbar.setSliderProgressCallback(new Slider.SliderProgressCallback() {
            @Override
            public void onSliderProgressChanged(int progress) {

                if (progress > 65) {
                    seekbar.setProgress(100);
                    presenter.arrived();

                }
            }
        });
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
    public void initActionBar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.history_back_icon_off);
        }

        iv_search.setImageDrawable(getResources().getDrawable(R.drawable.ic_message_black_24dp));
        iv_search.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    @Override
    public void setViews(ArrayList<AssignedAppointments> appointments, ArrayList<AssignedAppointments> sortStoreId,int upIndexToItems) {
        this.upIndexToItems=upIndexToItems;
        this.appointments.clear();
        this.appointments.addAll(appointments);
        this.sortStoreId.clear();
        this.sortStoreId.addAll(sortStoreId);
        if (Integer.parseInt(appointments.get(0).getOrderStatus()) >= (Integer.parseInt(AppConstants.BookingStatus.Arrived))) {
            hideSeekBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onSuccess(ArrayList<AssignedAppointments> updateAppointments) {
        hideSeekBar.setVisibility(View.GONE);
        appointments.clear();
        appointments.addAll(updateAppointments);
        sortedStoreIDRVA.notifyDataSetChanged();
    }

    @Override
    public void onError(String message) {
        seekbar.setProgress(0);
        Utility.mShowMessage(getResources().getString(R.string.message), message, this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("exe", "data" + data);

        if (data != null) {
            Bundle bundle = data.getExtras();

            if (resultCode == Activity.RESULT_OK) {

                AssignedAppointments updAppointments = (AssignedAppointments) bundle.getSerializable("data");
                updIndex = bundle.getInt("updIndex");
                //if (updAppointments!=null) {
                appointments.remove(updIndex);
                appointments.add(updIndex, updAppointments);
                sortedStoreIDRVA.notifyDataSetChanged();
            }
            else if(resultCode == Activity.RESULT_CANCELED){
                AssignedAppointments updAppointments = (AssignedAppointments) bundle.getSerializable("updAppointments");
                updIndex = bundle.getInt("updIndex");
                //if (updAppointments!=null) {
                appointments.remove(updIndex);
                appointments.add(updIndex, updAppointments);
                sortedStoreIDRVA.notifyDataSetChanged();
            }
        }
    }


    public void startActivity(AssignedAppointments assignedAppointments, int updIndex) {
        Log.d("exe", "updIndex" + updIndex);
        Intent intent4 = new Intent(SelectedStoreIdActivity.this, StorePickUp.class);
        Bundle bundle4 = new Bundle();
        bundle4.putSerializable("data", assignedAppointments);
        bundle4.putInt("index", updIndex);
        intent4.putExtras(bundle4);
        startActivityForResult(intent4, 123);
    }

    public void MoveToBookingRide(AssignedAppointments assignedAppointments, int updIndex) {
        Log.d("exe", "updIndex" + updIndex);
        Intent intent = new Intent(this, BookingRide.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", assignedAppointments);
        bundle.putInt("index", updIndex);
        intent.putExtras(bundle);
        startActivityForResult(intent, 123);
    }


    @Override
    public void errorToast(String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
//         super.onBackPressed();
        if (appointments != null) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("updAppointments", appointments);
            bundle.putInt("updIndex", upIndexToItems);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void openGoogleMap(String uri) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    public void pickup() {
        presenter.getDirection();
    }
}
