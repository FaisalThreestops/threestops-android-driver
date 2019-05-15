package com.delivx.app.storeDetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delivx.adapter.LaunderListRVA;
import com.delivx.app.bookingRide.BookingRide;
import com.driver.delivx.R;
import com.delivx.mqttChat.ChattingActivity;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Slider;
import com.delivx.utility.Utility;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class StorePickUpDetails extends DaggerAppCompatActivity implements StoreDetailsContract.View{


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_cust_name_title) TextView tv_cust_name_title;
    @BindView(R.id.tv_cust_name) TextView tv_cust_name;
    @BindView(R.id.iv_call_customer) ImageView iv_call_customer;
    @BindView(R.id.tv_pick_up_title) TextView tv_pick_up_title;
    @BindView(R.id.ll_item_head) LinearLayout ll_item_head;
    @BindView(R.id.ll_total) LinearLayout ll_total;
    @BindView(R.id.tv_pickup) TextView tv_pickup;
    @BindView(R.id.iv_call_pickUp) ImageView iv_call_pickUp;
    @BindView(R.id.iv_search) ImageView iv_search;
    @BindView(R.id.tv_drop_up_title) TextView tv_drop_up_title;
    @BindView(R.id.tv_dropup) TextView tv_dropup;
    @BindView(R.id.tv_payment_title) TextView tv_payment_title;
    @BindView(R.id.tv_payment_type) TextView tv_payment_type;
    @BindView(R.id.tvProductsTitle) TextView tvProductsTitle;
    @BindView(R.id.tvPriceTitle) TextView tvPriceTitle;
    @BindView(R.id.tvItems) TextView tvItems;
    @BindView(R.id.tvGrandTotal) TextView tvGrandTotal;
    @BindView(R.id.tvGrandTotalTitle) TextView tvGrandTotalTitle;
    @BindView(R.id.tv_status_text) TextView tv_status_text;
    @BindView(R.id.ll_item_container) LinearLayout ll_item_container;
    @BindView(R.id.myseek) Slider seekbar;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.Bottomlayout) RelativeLayout bottomLayout;
    @BindView(R.id.view_last) View view_last;
    @BindView(R.id.rv_item_show)
    RecyclerView rv_item_show;
    private Typeface font,fontBold;

    @Inject
    StoreDetailsContract.Presenter presenter;
    @Inject
    FontUtils fontUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_pick_up_details);
        ButterKnife.bind(this);

        initViews();
        presenter.getBundleData(getIntent().getExtras());
        presenter.setActionBar();
        presenter.setActionBarTitle();

    }

    public void initViews()
    {
        font= fontUtils.titaliumRegular();
        fontBold=fontUtils.titaliumSemiBold();

        tv_title.setTypeface(fontBold);
        tv_status_text.setTypeface(fontBold);
        tv_cust_name_title.setTypeface(font);
        tv_cust_name.setTypeface(font);
        tv_pick_up_title.setTypeface(font);
        tv_pickup.setTypeface(font);
        tv_drop_up_title.setTypeface(font);
        tv_dropup.setTypeface(font);
        tv_payment_title.setTypeface(font);
        tv_payment_type.setTypeface(font);
        tvProductsTitle.setTypeface(font);
        tvPriceTitle.setTypeface(fontBold);
        tvItems.setTypeface(fontBold);
        tvGrandTotal.setTypeface(fontBold);
        tvGrandTotalTitle.setTypeface(fontBold);

        seekbar.setSliderProgressCallback(new Slider.SliderProgressCallback() {
            @Override
            public void onSliderProgressChanged(int progress) {

                if (progress > 65) {
                    seekbar.setProgress(100);
                    presenter.updateBookingStatus();
                }
            }
        });



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
        iv_search.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(String title) {
        tv_title.setText(title);
        tv_title.setTypeface(fontBold);
    }

    @Override
    public void setViews(AssignedAppointments appointments) {

        tv_cust_name.setText(appointments.getCustomerName());
        if(appointments.getPaymentType().equals("2"))
            tv_payment_type.setText(getResources().getString(R.string.cash));
        else
            tv_payment_type.setText(getResources().getString(R.string.card));


        if(appointments.getStoreType().matches("5") &&
                !appointments.isCominigFromStore()){

            iv_call_customer.setVisibility(View.GONE);
            iv_call_pickUp.setVisibility(View.GONE);
            tv_pick_up_title.setText(getResources().getString(R.string.pickup_location));
            tv_pickup.setText(appointments.getPickUpAddress());
            tv_drop_up_title.setText(getResources().getString(R.string.pickup_slot));
            tv_dropup.setText("12 AM -12 PM");
            ll_item_head.setVisibility(View.GONE);
            tvProductsTitle.setText(getResources().getString(R.string.laundry_details));
            ll_total.setVisibility(View.GONE);
            view_last.setVisibility(View.GONE);
            rv_item_show.setVisibility(View.VISIBLE);

            LaunderListRVA  launderListRVA = new LaunderListRVA(this,
                    appointments.getShipmentDetails(),font,this.getSupportFragmentManager());
            rv_item_show.setLayoutManager(new LinearLayoutManager(this));
            rv_item_show.setAdapter(launderListRVA);


        }else {

            String text = "<b>" + appointments.getStoreName() + "</b> "+" : "+appointments.getPickUpAddress() ;
            tv_pickup.setText(Html.fromHtml(text));
            tv_dropup.setText(appointments.getDropAddress());

        }


        addItems(appointments);
    }

    @Override
    public void onSuccess(AssignedAppointments appointments) {

        Intent intent=new Intent(StorePickUpDetails.this, BookingRide.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",appointments);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    @Override
    public void onError(String message) {
        seekbar.setProgress(0);
        Utility.mShowMessage(getResources().getString(R.string.message),message,this);
    }

    @Override
    public void hideViews() {
        bottomLayout.setVisibility(View.GONE);
        iv_call_pickUp.setVisibility(View.GONE);
        iv_call_customer.setVisibility(View.GONE);
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


    @OnClick({R.id.iv_call_customer,R.id.iv_call_pickUp})
    public void onClick(View view){
        switch (view.getId())
        {
            case R.id.iv_call_customer:
                presenter.callCustomer(true);
                break;
            case R.id.iv_call_pickUp:
                presenter.callCustomer(false);
                break;
        }

    }

    @OnClick(R.id.iv_search)
    public void chattingOnclick(View view){
        presenter.openChat();
    }

    @SuppressLint("SetTextI18n")
    public void addItems(AssignedAppointments appointments){
        int size=appointments.getShipmentDetails().size();
        float total=0.0f;
        if(size>0){
            for(int i=0;i<size;i++){
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.store_details_item_single_row, null);

                TextView itemName= view.findViewById(R.id.tvItemName);
                itemName.setTypeface(font);
                TextView itemPrice= view.findViewById(R.id.tvItemPrice);
                itemPrice.setTypeface(font);
                TextView itemUnit= view.findViewById(R.id.tvUnit);
                itemPrice.setTypeface(font);

                int quantity= Integer.parseInt(appointments.getShipmentDetails().get(i).getQuantity());
                String unitPriceStr=appointments.getShipmentDetails().get(i).getUnitPrice();
                float unitPrice=0;
                if(!"".equals(unitPriceStr) && unitPriceStr!=null)
                    unitPrice= Float.parseFloat(unitPriceStr);
                String item=appointments.getShipmentDetails().get(i).getItemName();

                float subTotal=quantity*unitPrice;
                total+=subTotal;

                itemPrice.setText(appointments.getCurrencySymbol()+" "+
                                String.format(Locale.US,"%.2f",subTotal));
                itemName.setText(item);

                if(appointments.getShipmentDetails().get(i).getAddOns()!=null) {
                    String addOns = "";
                    addOns = appointments.getShipmentDetails().get(i).getAddOns().size() > 0 ? "Addons: " + appointments.getShipmentDetails().get(i).getAddOns().toString() : "";

                    itemUnit.setText(getResources().getString(R.string.qty) + " " + quantity + "\n" + getResources().getString(R.string.unit) + ": " + appointments.getShipmentDetails().get(i).getUnitName() + "\n" + addOns);
                    ll_item_container.addView(view);
                }else
                    ll_item_container.setVisibility(View.GONE);

            }
        }
        tvGrandTotal.setText(appointments.getCurrencySymbol()+" "+String.format(Locale.US,"%.2f",Float.parseFloat(appointments.getTotalAmount())));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

     @Override
    public void openChatAct(AssignedAppointments appointments) {

        Intent intent=new Intent(this,ChattingActivity.class);
        intent.putExtra("BID",appointments.getBid());
        intent.putExtra("CUST_ID",appointments.getCustomerId());
        intent.putExtra("CUST_NAME",appointments.getCustomerName());
        startActivity(intent);


    }
}
