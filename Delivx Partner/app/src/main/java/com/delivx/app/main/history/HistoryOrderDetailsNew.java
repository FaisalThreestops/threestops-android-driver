package com.delivx.app.main.history;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delivx.app.main.history.orderDetails.OrderHistoryContract;
import com.driver.delivx.R;
import com.delivx.pojo.TripsPojo.Appointments;
import com.delivx.utility.FontUtils;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class HistoryOrderDetailsNew extends DaggerAppCompatActivity implements OrderHistoryContract.ViewOperation {

    @BindView(R.id.tv_total) TextView tv_total;
    @BindView(R.id.tv_totalVal) TextView tv_totalVal;
    @BindView(R.id.tv_distance) TextView tv_distance;
    @BindView(R.id.tv_distanceTime) TextView tv_distanceTime;
    @BindView(R.id.tv_addressTitle) TextView tv_addressTitle;
    @BindView(R.id.tv_payment_title) TextView tv_payment_title;
    @BindView(R.id.tv_subTotal) TextView tv_subTotal;
    @BindView(R.id.tv_subTotal_val) TextView tv_subTotal_val;
    @BindView(R.id.tv_Tax) TextView tv_Tax;
    @BindView(R.id.tv_Tax_val) TextView tv_Tax_val;
    @BindView(R.id.tv_delCharge) TextView tv_delCharge;
    @BindView(R.id.tv_delCharge_val) TextView tv_delCharge_val;
    @BindView(R.id.tv_discount) TextView tv_discount;
    @BindView(R.id.tv_discount_val) TextView tv_discount_val;
    @BindView(R.id.tv_subToatal) TextView tv_subToatal;
    @BindView(R.id.tv_subToatal_val) TextView tv_subToatal_val;
    @BindView(R.id.tv_eaning_title) TextView tv_eaning_title;
    @BindView(R.id.tv_earned) TextView tv_earned;
    @BindView(R.id.tv_earned_val) TextView tv_earned_val;
    @BindView(R.id.tv_storeEarn) TextView tv_storeEarn;
    @BindView(R.id.tv_storeEarn_val) TextView tv_storeEarn_val;
    @BindView(R.id.tv_appCommn) TextView tv_appCommn;
    @BindView(R.id.tv_appCommn_val) TextView tv_appCommn_val;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_bid) TextView tv_bid;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvPickUp) TextView tvPickUp;
    @BindView(R.id.tvDrop) TextView tvDrop;
    @BindView(R.id.llItemContainer) LinearLayout llItemContainer;
    @BindView(R.id.tvShowOrderDetals) TextView tvShowOrderDetals;
    @BindView(R.id.tvHideOrderDetals) TextView tvHideOrderDetals;
    @BindView(R.id.ivStore) ImageView ivStore;
    @BindView(R.id.tvStore) TextView tvStore;
    @BindView(R.id.tvStoreAddress) TextView tvStoreAddress;
    @BindView(R.id.tvWalletValue) TextView tvWalletValue;
    @BindView(R.id.tvCashValue) TextView tvCashValue;
    @BindView(R.id.tvCardValue) TextView tvCardValue;
    @BindView(R.id.llOrderDetails) LinearLayout llOrderDetails;



    @Inject
    FontUtils fontUtils;
    @Inject
    OrderHistoryContract.PresenterOperation presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_order_details);
        ButterKnife.bind(this);
        presenter.getBundleData(getIntent().getExtras());
        setActionBar();

        tvShowOrderDetals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShowOrderDetals.setVisibility(View.GONE);
                llOrderDetails.setVisibility(View.VISIBLE);
            }
        });

        tvHideOrderDetals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llOrderDetails.setVisibility(View.GONE);
                tvShowOrderDetals.setVisibility(View.VISIBLE);
            }
        });
    }
    public void setActionBar() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.history_back_icon_off);
        }
        tv_title.setTypeface(fontUtils.titaliumSemiBold());
        tv_bid.setTypeface(fontUtils.titaliumRegular());

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void setViews(Appointments appointment) {

        tv_title.setText(appointment.getBookingDate());
        tv_bid.setText(appointment.getOrderId());
        tvDrop.setText(appointment.getDropAddress());
        tvPickUp.setText(appointment.getPickAddress());
        tv_appCommn_val.setText(appointment.getCurrencySymbol()+" "+appointment.getAppCommission());
        tv_totalVal.setText(appointment.getCurrencySymbol()+" "+appointment.getTotalAmount());
        tv_subTotal_val.setText(appointment.getCurrencySymbol()+" "+appointment.getSubTotalAmount());
        tv_Tax_val.setText(appointment.getCurrencySymbol()+" "+appointment.getTax());
        tv_earned_val.setText(appointment.getCurrencySymbol()+" "+appointment.getDriverEarning());
        tv_storeEarn_val.setText(appointment.getCurrencySymbol()+" "+appointment.getStoreEarning());
        tv_delCharge_val.setText(appointment.getCurrencySymbol()+" "+appointment.getDeliveryCharge());
        tv_subToatal_val.setText(appointment.getCurrencySymbol()+" "+appointment.getTotalAmount());
        tv_discount_val.setText(appointment.getCurrencySymbol()+" "+appointment.getItems().get(0).getAppliedDiscount());

        if(appointment.getStoreLogo()!=null && !appointment.getStoreLogo().isEmpty()){
            Picasso.with(this).load(appointment.getStoreLogo()).resize(60,60).into(ivStore);
        }
        tvStore.setText(appointment.getStoreName());
        tvStoreAddress.setText(appointment.getStoreAddress());
        tvCashValue.setText(appointment.getCurrencySymbol()+" "+appointment.getPaidByCash());
        tvWalletValue.setText(appointment.getCurrencySymbol()+" "+appointment.getPaidByWallet());
        tvCardValue.setText(appointment.getCurrencySymbol()+" "+appointment.getPaidByCard());

        addItems(appointment);
    }

    public void addItems( Appointments appointments){
        llItemContainer.removeAllViews();
        int size=appointments.getItems().size();

        if(size>0){
            for(int i=0;i<size;i++){


                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.store_details_item_single_row, null);

                TextView itemName= (TextView) view.findViewById(R.id.tvItemName);
                itemName.setTypeface(fontUtils.titaliumRegular());
                TextView itemPrice= (TextView) view.findViewById(R.id.tvItemPrice);
                itemPrice.setTypeface(fontUtils.titaliumRegular());
                TextView itemUnit= (TextView) view.findViewById(R.id.tvUnit);
                itemPrice.setTypeface(fontUtils.titaliumRegular());
                TextView tvQuantity= (TextView) view.findViewById(R.id.tvQuantity);
                tvQuantity.setTypeface(fontUtils.titaliumRegular());


                int quantity= Integer.parseInt(appointments.getItems().get(i).getQuantity());
                float unitPrice= Float.parseFloat(appointments.getItems().get(i).getUnitPrice());

                String item=appointments.getItems().get(i).getItemName();

                float subTotal=quantity*unitPrice;

                itemPrice.setText(appointments.getCurrencySymbol()+" "+
                        String.format(Locale.US,"%.2f",subTotal));

                itemName.setText(item);
                itemUnit.setText(appointments.getItems().get(i).getUnitName());
                tvQuantity.setText(quantity+"");
//                itemUnit.setText("( "+quantity+" "+appointments.getItems().get(i).getUnitName()+" )");
                itemName.setTextColor(getResources().getColor(R.color.color_sup_txt));


                llItemContainer.addView(view);
            }
        }
    }

}
