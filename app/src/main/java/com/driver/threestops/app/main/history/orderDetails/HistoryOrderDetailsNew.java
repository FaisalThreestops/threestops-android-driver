package com.driver.threestops.app.main.history.orderDetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.driver.threestops.utility.Utility;
import com.driver.Threestops.R;
import com.driver.threestops.pojo.TripsPojo.Appointments;
import com.driver.threestops.utility.FontUtils;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class HistoryOrderDetailsNew extends DaggerAppCompatActivity implements OrderHistoryContract.ViewOperation {

    @BindView(R.id.tv_total) TextView tv_total;
    @BindView(R.id.tv_totalVal) TextView tv_totalVal;
    @BindView(R.id.tv_distance) TextView tv_distance;
    @BindView(R.id.tv_distance_value) TextView tv_distance_value;
    @BindView(R.id.tv_distanceTime_value) TextView tv_distanceTime_value;
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
    @BindView(R.id.ll_discount) LinearLayout ll_discount;
    @BindView(R.id.tv_subToatal) TextView tv_subToatal;
    @BindView(R.id.tv_subToatal_val) TextView tv_subToatal_val;
    @BindView(R.id.tv_eaning_title) TextView tv_eaning_title;
    @BindView(R.id.tv_earned) TextView tv_earned;
    @BindView(R.id.tv_earned_val) TextView tv_earned_val;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_bid) TextView tv_bid;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvPickUp) TextView tvPickUp;
    @BindView(R.id.tvDrop) TextView tvDrop;
    @BindView(R.id.llItemContainer) LinearLayout llItemContainer;
    @BindView(R.id.tvWalletValue) TextView tvWalletValue;
    @BindView(R.id.tvCashValue) TextView tvCashValue;
    @BindView(R.id.tvCash) TextView tvCash;
    @BindView(R.id.tvCardValue) TextView tvCardValue;
    @BindView(R.id.tv_type_of_delivery) TextView tv_type_of_delivery;
    @BindView(R.id.tv_type_of_delivery_value) TextView tv_type_of_delivery_value;
    @BindView(R.id.tv_pickup_drop) TextView tv_pickup_drop;
    @BindView(R.id.tv_item_detail) TextView tv_item_detail;
    @BindView(R.id.tv_items) TextView tv_items;
    @BindView(R.id.tv_qty) TextView tv_qty;
    @BindView(R.id.tv_price) TextView tv_price;
    @BindView(R.id.tv_payment_method) TextView tv_payment_method;
    @BindView(R.id.tvCard) TextView tvCard;
    @BindView(R.id.tvWallet) TextView tvWallet;
    @BindView(R.id.ll_tax) LinearLayout ll_tax;
    @BindView(R.id.ll_cash) LinearLayout ll_cash;
    @BindView(R.id.ll_card) LinearLayout ll_card;
    @BindView(R.id.ll_wallet) LinearLayout ll_wallet;
    @BindView(R.id.tvPickUp_name) TextView tvPickUp_name;
    @BindView(R.id.tv_drop_name) TextView tv_drop_name;
    @BindView(R.id.tv_orderStatus) TextView tv_orderStatus;
    @BindView(R.id.tv_customer_details)TextView tv_customer_details;
    @BindView(R.id.rl_customer_details)
    RelativeLayout rl_customer_details;
    @BindView(R.id.tv_cust_name)TextView tv_cust_name;
    @BindView(R.id.ll_subTotal) LinearLayout ll_subTotal;


    @Inject
    FontUtils fontUtils;
    @Inject
    OrderHistoryContract.PresenterOperation presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.history_order_details);
        ButterKnife.bind(this);
        presenter.getBundleData(getIntent().getExtras());
        setActionBar();
    }

    /**
     * <h2>setActionBar</h2>
     * <p>setting the action bar</p>
     */
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


        tv_total.setTypeface(fontUtils.clanaproNarrNews());
        tv_totalVal.setTypeface(fontUtils.clanaproNarrMedium());
        tv_distance.setTypeface(fontUtils.clanaproNarrNews());
        tv_distance_value.setTypeface(fontUtils.clanaproNarrNews());
        tv_distanceTime_value.setTypeface(fontUtils.clanaproNarrNews());
        tv_distanceTime.setTypeface(fontUtils.clanaproNarrNews());
        tv_type_of_delivery.setTypeface(fontUtils.clanaproNarrNews());
        tv_type_of_delivery_value.setTypeface(fontUtils.clanaproNarrMedium());
        tv_pickup_drop.setTypeface(fontUtils.clanaproNarrMedium());
        tv_item_detail.setTypeface(fontUtils.clanaproNarrMedium());
        tv_payment_title.setTypeface(fontUtils.clanaproNarrMedium());
        tv_items.setTypeface(fontUtils.clanaproNarrNews());
        tv_qty.setTypeface(fontUtils.clanaproNarrNews());
        tv_price.setTypeface(fontUtils.clanaproNarrNews());
        tv_subTotal.setTypeface(fontUtils.clanaproNarrNews());
        tv_subTotal_val.setTypeface(fontUtils.clanaproNarrNews());
        tv_Tax.setTypeface(fontUtils.clanaproNarrNews());
        tv_Tax_val.setTypeface(fontUtils.clanaproNarrNews());
        tv_delCharge.setTypeface(fontUtils.clanaproNarrNews());
        tv_delCharge_val.setTypeface(fontUtils.clanaproNarrNews());
        tv_discount.setTypeface(fontUtils.clanaproNarrNews());
        tv_discount_val.setTypeface(fontUtils.clanaproNarrNews());
        tv_subToatal.setTypeface(fontUtils.clanaproNarrMedium());
        tv_subToatal_val.setTypeface(fontUtils.clanaproNarrMedium());
        tv_payment_method.setTypeface(fontUtils.clanaproNarrNews());
        tvCash.setTypeface(fontUtils.clanaproNarrNews());
        tvCashValue.setTypeface(fontUtils.clanaproNarrNews());
        tvCard.setTypeface(fontUtils.clanaproNarrNews());
        tvWallet.setTypeface(fontUtils.clanaproNarrNews());
        tvWalletValue.setTypeface(fontUtils.clanaproNarrNews());
        tvCardValue.setTypeface(fontUtils.clanaproNarrNews());
        tv_eaning_title.setTypeface(fontUtils.clanaproNarrNews());
        tvPickUp_name.setTypeface(fontUtils.clanaproNarrNews());
        tv_drop_name.setTypeface(fontUtils.clanaproNarrNews());
        tv_orderStatus.setTypeface(fontUtils.clanaproNarrMedium());


        if(appointment.getStoreType().equals("7")){
            tv_customer_details.setVisibility(View.VISIBLE);
            rl_customer_details.setVisibility(View.VISIBLE);
            tvPickUp_name.setText(this.getResources().getString(R.string.pickup));
            tv_drop_name.setText(this.getResources().getString(R.string.delivery));
            tv_qty.setVisibility(View.INVISIBLE);
            tv_price.setText(this.getResources().getString(R.string.quant));
            ll_subTotal.setVisibility(View.GONE);
        }
        else{
            tvPickUp_name.setText(appointment.getStoreName());
            tv_drop_name.setText(appointment.getCustomerName());
        }


        tv_cust_name.setText(appointment.getCustomerName());
        tv_title.setText(Utility.parseDateToddMMyyyy(appointment.getBookingDate()));
        tv_bid.setText(getResources().getString(R.string.order_id_dot)+appointment.getOrderId());
        tvDrop.setText(appointment.getDropAddress());
        tvPickUp.setText(appointment.getPickAddress());
        double total_amount = Double.parseDouble(appointment.getTotalAmount());
        tv_totalVal.setText(appointment.getCurrencySymbol()+" "+String.format("%.2f", total_amount));
        double sub_total_amount = Double.parseDouble(appointment.getSubTotalAmount());
        tv_subTotal_val.setText(appointment.getCurrencySymbol()+" "+String.format("%.2f", sub_total_amount));
        double tax = Double.parseDouble(appointment.getTax());
        tv_Tax_val.setText(appointment.getCurrencySymbol()+" "+String.format("%.2f", tax));
        double driverEarning = Double.parseDouble(appointment.getDriverEarning());
        tv_earned_val.setText(appointment.getCurrencySymbol()+" "+String.format("%.2f", driverEarning));
        double deliveryCharge = Double.parseDouble(appointment.getDeliveryCharge());
        tv_delCharge_val.setText(appointment.getCurrencySymbol()+" "+String.format("%.2f", deliveryCharge));
        double totalAmount = Double.parseDouble(appointment.getTotalAmount());
        tv_subToatal_val.setText(appointment.getCurrencySymbol()+" "+String.format("%.2f", totalAmount));
        if(appointment.getItems().get(0).getAppliedDiscount()!=null) {
            double appliedDiscount = Double.parseDouble(appointment.getItems().get(0).getAppliedDiscount());
            tv_discount_val.setText(appointment.getCurrencySymbol() + " " + String.format("%.2f", appliedDiscount));
        }else{
            tv_discount_val.setText(appointment.getCurrencySymbol() + " 0.00");
        }

        if(appointment.getTax().matches("0"))
            ll_tax.setVisibility(View.GONE);

        double paidByCash = Double.parseDouble(appointment.getPaidByCash());
        tvCashValue.setText(appointment.getCurrencySymbol()+" "+String.format("%.2f", paidByCash));
        double paidBywallet = Double.parseDouble(appointment.getPaidByWallet());
        tvWalletValue.setText(appointment.getCurrencySymbol()+" "+String.format("%.2f", paidBywallet));
        double paidByCard = Double.parseDouble(appointment.getPaidByCard());
        tvCardValue.setText(appointment.getCurrencySymbol()+" "+String.format("%.2f", paidByCard));

        tv_distance_value.setText(appointment.getDistanceDriver()+" "+appointment.getMileageMetric() );
        tv_distanceTime_value.setText(Utility.getDurationString_(appointment.getJourneyStartToEndTime()));

        if(appointment.getPaidByCash().matches("0"))
            ll_cash.setVisibility(View.GONE);

        if(appointment.getPaidByWallet().matches("0"))
            ll_wallet.setVisibility(View.GONE);

        if(appointment.getPaidByCard().matches("0"))
            ll_card.setVisibility(View.GONE);



        tv_type_of_delivery_value.setText(appointment.getStoreTypeMsg());

        addItems(appointment);
    }

    /**
     * <h2>addItems</h2>
     * <p>adding the delivery items list </p>
     * @param appointments : appointments pojo details
     */
    public void addItems( Appointments appointments){
        llItemContainer.removeAllViews();
        int size=appointments.getItems().size();

        if(size>0){
            for(int i=0;i<size;i++){


                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.history_item_show_single_raw, null);

                TextView tvItemName= view.findViewById(R.id.tvItemName);
                tvItemName.setTypeface(fontUtils.clanaproNarrNews());
                TextView tvQuantity= view.findViewById(R.id.tvQuantity);
                tvQuantity.setTypeface(fontUtils.clanaproNarrNews());
                TextView tvItemPrice= view.findViewById(R.id.tvItemPrice);
                tvItemPrice.setTypeface(fontUtils.clanaproNarrNews());

                if(appointments.getStoreType().equals("7")){
                    tvItemPrice.setVisibility(View.GONE);
                }
                float unitPrice;
                int quantity= Integer.parseInt(appointments.getItems().get(i).getQuantity());
                if(appointments.getItems().get(i).getUnitPrice()!=null) {
                    unitPrice = Float.parseFloat(appointments.getItems().get(i).getUnitPrice());
                }else{
                    unitPrice=0.00f;
                }

                String item=appointments.getItems().get(i).getItemName();

                float subTotal=quantity*unitPrice;

                tvItemPrice.setText(appointments.getCurrencySymbol()+" "+
                        String.format(Locale.US,"%.2f",subTotal));

                tvItemName.setText(item);
                tvQuantity.setText(quantity+"");

                llItemContainer.addView(view);
            }
        }
    }

}
