package com.delivx.app.storePickUp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.delivx.app.bookingRide.BookingRide;
import com.delivx.app.invoice.InvoiceActivity;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.mqttChat.ChattingActivity;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.ShipmentDetails;
import com.delivx.utility.AppConstants;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Slider;
import com.delivx.utility.Utility;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class StorePickUp extends DaggerAppCompatActivity implements PickUpContract.ViewOperations {


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_cust_name_title) TextView tv_cust_name_title;
    @BindView(R.id.tv_cust_name) TextView tv_cust_name;
    @BindView(R.id.tvTitleStore) TextView tvTitleStore;
    @BindView(R.id.tvProductHint) TextView tvProductHint;
    @BindView(R.id.tvStore) TextView tvStore;
    @BindView(R.id.iv_call_customer) ImageView iv_call_customer;
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
    @BindView(R.id.tvAddItem) TextView tvAddItem;
    @BindView(R.id.ll_item_container) LinearLayout ll_item_container;
    @BindView(R.id.myseek) Slider seekbar;
    @BindView(R.id.progressBar) ProgressBar progressBar;



    @BindView(R.id.tv_paymentbreskdown) TextView tv_paymentbreskdown;
    @BindView(R.id.tv_subTotal) TextView tv_subTotal;
    @BindView(R.id.tv_subTotal_val) TextView tv_subTotal_val;
    @BindView(R.id.tv_delCharge) TextView tv_delCharge;
    @BindView(R.id.tv_delCharge_val) TextView tv_delCharge_val;
    @BindView(R.id.tv_discount) TextView tv_discount;
    @BindView(R.id.tv_discount_val) TextView tv_discount_val;
    @BindView(R.id.ll_discount) LinearLayout ll_discount;

    @BindView(R.id.ll_tax) LinearLayout ll_tax;
    @BindView(R.id.ll_tax_item_container) LinearLayout ll_tax_item_container;
    @BindView(R.id.tv_tax) TextView tv_tax;

    private boolean tax_added = false;

    private Typeface font,fontBold;

    @Inject PickUpContract.PresenterOperations presenter;
    @Inject FontUtils fontUtils;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.activity_store_pick_up);
        ButterKnife.bind(this);
        initViews();
        presenter.getBundleData(getIntent().getExtras());
        presenter.setActionBar();
        presenter.setActionBarTitle();
    }

    private void initViews() {
        font= fontUtils.titaliumRegular();
        fontBold=fontUtils.titaliumSemiBold();
        tv_status_text.setTypeface(fontBold);
        tvAddItem.setTypeface(fontBold);
        tv_title.setTypeface(fontBold);
        tv_cust_name_title.setTypeface(font);
        tv_cust_name.setTypeface(font);
        tvProductHint.setTypeface(font);
        tvStore.setTypeface(font);
        tvTitleStore.setTypeface(font);
        tv_drop_up_title.setTypeface(font);
        tv_dropup.setTypeface(font);
        tv_payment_title.setTypeface(font);
        tv_payment_type.setTypeface(font);
        tvProductsTitle.setTypeface(font);
        tvPriceTitle.setTypeface(font);
        tvItems.setTypeface(font);
        tvGrandTotal.setTypeface(fontBold);
        tvGrandTotalTitle.setTypeface(fontBold);


        tv_subTotal.setTypeface(font);
        tv_subTotal_val.setTypeface(font);
        tv_delCharge.setTypeface(font);
        tv_delCharge_val.setTypeface(font);
        tv_discount.setTypeface(font);
        tv_discount_val.setTypeface(font);
        tv_paymentbreskdown.setTypeface(font);

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
        iv_search.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    @Override
    public void setViews(AssignedAppointments appointments) {

        tv_cust_name.setText(appointments.getCustomerName());
        if(appointments.getOrderStatus().equals(AppConstants.BookingStatus.ReachedAtLocation)){
            tv_dropup.setVisibility(View.GONE);
            tvAddItem.setVisibility(View.GONE);
            tv_drop_up_title.setVisibility(View.GONE);
            tvStore.setText(appointments.getDropAddress());
            tv_status_text.setText(getResources().getString(R.string.delivered));
        }
        else {
            tv_dropup.setText(appointments.getDropAddress());
            String text = "<b>" + appointments.getStoreName() + "</b> "+" : "+appointments.getPickUpAddress() ;
            tvStore.setText(Html.fromHtml(text));
            tv_status_text.setText(getResources().getString(R.string.picked_and_start));
        }


        if(appointments.getPaymentType().equals("2"))
            tv_payment_type.setText(getResources().getString(R.string.cash));
        else
            tv_payment_type.setText(getResources().getString(R.string.card));


        double sub_total_amount = Double.parseDouble(appointments.getSubTotalAmount());
        tv_subTotal_val.setText(appointments.getCurrencySymbol()+" "+String.format("%.2f", sub_total_amount));

        double deliveryCharge = Double.parseDouble(appointments.getDeliveryCharge());
        tv_delCharge_val.setText(appointments.getCurrencySymbol()+" "+String.format("%.2f", deliveryCharge));

        double appliedDiscount = Double.parseDouble(appointments.getShipmentDetails().get(0).getAppliedDiscount());
        tv_discount_val.setText(appointments.getCurrencySymbol()+" "+String.format("%.2f", appliedDiscount));


        addItems(appointments);
    }

    @SuppressLint("SetTextI18n")
    public void addItems(final AssignedAppointments appointments){
        ll_item_container.removeAllViews();
        int size=appointments.getShipmentDetails().size();
        float total=0.0f;
        if(size>0){
            for(int i=0;i<size;i++){
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.history_item_show_single_raw, null);

                TextView itemName= view.findViewById(R.id.tvItemName);
                itemName.setTypeface(font);
                TextView itemPrice= view.findViewById(R.id.tvItemPrice);
                itemPrice.setTypeface(font);
                TextView itemUnit= view.findViewById(R.id.tvQuantity);
                itemPrice.setTypeface(font);


                String item=appointments.getShipmentDetails().get(i).getItemName();
                SpannableString spannableString=new SpannableString(item);
                spannableString.setSpan(new UnderlineSpan(),0,spannableString.length(),0);
                itemName.setText(spannableString);

                int quantity= Integer.parseInt(appointments.getShipmentDetails().get(i).getQuantity());
                itemUnit.setText(appointments.getShipmentDetails().get(i).getQuantity());


                float unitPrice= Float.parseFloat(appointments.getShipmentDetails().get(i).getFinalPrice());



                float subTotal=quantity*unitPrice;
                total+=subTotal;

                itemPrice.setText(appointments.getCurrencySymbol()+" "+
                        String.format(Locale.US,"%.2f",subTotal));
                String addOns="";/*+appointments.getShipmentDetails().get(i).getAddOns().toString();*/
                addOns=appointments.getShipmentDetails().get(i).getAddOns().size()>0?
                        "Addons: "+appointments.getShipmentDetails().get(i).getAddOns().toString():"";


               /* itemUnit.setText(getResources().getString(R.string.qty)
                        +" "+quantity+"\n"+
                        getResources().getString(R.string.unit)+": "+appointments.getShipmentDetails().get(i).getUnitName()
                +"\n"+addOns);
                itemName.setTextColor(getResources().getColor(R.color.sky_blue));*/


                if(appointments.getExcTax()!=null ){
                    float exc_tax = Float.parseFloat(appointments.getExcTax());
                    if(exc_tax>0 /*&& !tax_added*/){
                        addTaxItems(appointments);
                    }else {
                        ll_tax.setVisibility(View.GONE);
                    }
                }else
                    ll_tax.setVisibility(View.GONE);

                final int finalI = i;
                itemName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.editOrder(appointments.getShipmentDetails().get(finalI));
                    }
                });


                ll_item_container.addView(view);
            }
        }
        tvGrandTotal.setText(appointments.getCurrencySymbol()+" "+String.format(Locale.US,"%.2f",Float.parseFloat(appointments.getTotalAmount())));
    }


    public void addTaxItems(AssignedAppointments appointments){
        tax_added = true;

        ll_tax_item_container.removeAllViews();
        int size=appointments.getExclusiveTaxes().size();
        if(size>0){
            for(int i=0;i<size;i++){
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.history_item_show_single_raw, null);

                TextView itemName= view.findViewById(R.id.tvItemName);
                itemName.setTypeface(font,Typeface.ITALIC);
                itemName.setTextColor(getResources().getColor(R.color.divider2));
                TextView itemPrice= view.findViewById(R.id.tvItemPrice);
                itemPrice.setTypeface(font);
                itemPrice.setTextColor(getResources().getColor(R.color.divider2));
                TextView itemUnit= view.findViewById(R.id.tvQuantity);
                itemPrice.setTypeface(font, Typeface.ITALIC);
                itemUnit.setVisibility(View.INVISIBLE);

                String tax_name = "("+appointments.getExclusiveTaxes().get(i).getTaxCode()+"%"+")";
                itemName.setText(tax_name);

                float tax_price=Float.parseFloat(appointments.getExclusiveTaxes().get(i).getPrice());
                itemPrice.setText(appointments.getCurrencySymbol()+" "+
                        String.format(Locale.US,"%.2f",tax_price));

                ll_tax_item_container.addView(view);


            }
        }
    }


    @Override
    public void onSuccess(AssignedAppointments appointments) {

        Bundle bundle=new Bundle();
        bundle.putSerializable("data",appointments);
        if(appointments.getOrderStatus().equals(AppConstants.BookingStatus.Completed)){
            Intent intent=new Intent(StorePickUp.this, InvoiceActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }else {
            Intent intent=new Intent(StorePickUp.this, BookingRide.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onError(String message) {
        seekbar.setProgress(0);
        Utility.mShowMessage(getResources().getString(R.string.message),message,this);
    }

    @Override
    public void openOrderEditDialog(final ShipmentDetails shipmentDetails, AssignedAppointments appointments) {
        dialog=new Dialog(StorePickUp.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.order_edit_dailog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        TextView tvItem_Name= (TextView) dialog.findViewById(R.id.tvItem_Name);
        tvItem_Name.setTypeface(fontUtils.titaliumRegular());
        TextView tvItemFrom= (TextView) dialog.findViewById(R.id.tvItemFrom);
        tvItemFrom.setTypeface(fontUtils.titaliumRegular());
        TextView tvQty= (TextView) dialog.findViewById(R.id.tvQty);
        tvQty.setTypeface(fontUtils.titaliumSemiBold());
        TextView tvItem_Price= (TextView) dialog.findViewById(R.id.tvItem_Price);
        tvItem_Price.setTypeface(fontUtils.titaliumSemiBold());
        TextView tvdelete= (TextView) dialog.findViewById(R.id.tvdelete);
        tvdelete.setTypeface(fontUtils.titaliumSemiBold());
        TextView tvUpdate= (TextView) dialog.findViewById(R.id.tvUpdate);
        tvUpdate.setTypeface(fontUtils.titaliumSemiBold());
        final EditText etQuantity= (EditText) dialog.findViewById(R.id.etQuantity);
        etQuantity.setTypeface(fontUtils.titaliumSemiBold());
        ImageView ivCancel = (ImageView) dialog.findViewById(R.id.ivCancel);

        tvItem_Name.setText(shipmentDetails.getItemName());
        etQuantity.setText(shipmentDetails.getQuantity());
        etQuantity.setSelection(etQuantity.getText().length());
        tvItemFrom.setText(getResources().getString(R.string.from)+" "+appointments.getStoreName());
//        tvItem_Price.setText(" X "+shipmentDetails.getCurrencySymbol()+Utility.formatPrice(shipmentDetails.getUnitPrice()));
        tvItem_Price.setText(" X "+appointments.getCurrencySymbol()+Utility.formatPrice(shipmentDetails.getFinalPrice()));

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.updateOrder(shipmentDetails,etQuantity.getText().toString());
                dialog.dismiss();
            }
        });
        tvdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteItem(shipmentDetails);
                dialog.dismiss();
            }
        });


        dialog.show();
    }
    @OnClick(R.id.iv_call_customer)
    public void onClick(View view){
        presenter.callCustomer();
    }

    @OnClick(R.id.iv_search)
    public void openChatOnclick(View view){
        presenter.openChat();
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
