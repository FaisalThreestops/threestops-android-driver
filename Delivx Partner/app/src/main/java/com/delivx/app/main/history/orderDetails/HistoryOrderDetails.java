package com.delivx.app.main.history.orderDetails;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.driver.delivx.R;
import com.delivx.pojo.TripsPojo.Appointments;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class HistoryOrderDetails extends DaggerAppCompatActivity /*implements OrderHistoryContract.ViewOperation*/ {

    @BindView(R.id.cardViewEarning) CardView cardViewEarning;
    @BindView(R.id.llItemContainer) LinearLayout llItemContainer;
    @BindView(R.id.llOrderedItems) LinearLayout llOrderedItems;
    @BindView(R.id.ivSignature) ImageView ivSignature;
    @BindView(R.id.ivOrderBottomStyle) ImageView ivOrderBottomStyle;
    @BindView(R.id.viewOrderBottomStyle) View viewOrderBottomStyle;
    @BindView(R.id.tvHeaderTotalEarning) TextView tvHeaderTotalEarning;
    @BindView(R.id.tvTotalEarning) TextView tvTotalEarning;
    @BindView(R.id.tvHeaderYourEarning) TextView tvHeaderYourEarning;
    @BindView(R.id.tvYourEarning) TextView tvYourEarning;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvPhone) TextView tvPhone;
    @BindView(R.id.tvRating) TextView tvRating;
    @BindView(R.id.tvRatingTitle) TextView tvRatingTitle;
    @BindView(R.id.tvPickUpTime) TextView tvPickUpTime;
    @BindView(R.id.tvPickUpTimeTitle) TextView tvPickUpTimeTitle;
    @BindView(R.id.tvDeliverTime) TextView tvDeliverTime;
    @BindView(R.id.tvDeliverTimeTitle) TextView tvDeliverTimeTitle;
    @BindView(R.id.tvPickUp) TextView tvPickUp;
    @BindView(R.id.tvDrop) TextView tvDrop;
    @BindView(R.id.tvShowOrderDetals) TextView tvShowOrderDetals;
    @BindView(R.id.tvHeaderOrders) TextView tvHeaderOrders;
    @BindView(R.id.tvGrandTotalTitle) TextView tvGrandTotalTitle;
    @BindView(R.id.tvGrandTotal) TextView tvGrandTotal;
    @BindView(R.id.tvhideOrderDetals) TextView tvhideOrderDetals;
    @BindView(R.id.tvPaymentTitle) TextView tvPaymentTitle;
    @BindView(R.id.tvPayment) TextView tvPayment;
    @BindView(R.id.tvReceivedByTitle) TextView tvReceivedByTitle;
    @BindView(R.id.tvRecvr) TextView tvRecvr;
    @BindView(R.id.tvRecvrPhone) TextView tvRecvrPhone;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_bid) TextView tv_bid;
    @BindView(R.id.toolbar) Toolbar toolbar;


    @Inject
    FontUtils fontUtils;

    @Inject
    OrderHistoryContract.PresenterOperation presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.activity_history_order_details);
        ButterKnife.bind(this);

        initViews();
        presenter.initActionBar();
        presenter.getBundleData(getIntent().getExtras());

    }

    private void initViews()
    {
        tvHeaderTotalEarning.setTypeface(fontUtils.titaliumRegular());
        tvHeaderYourEarning.setTypeface(fontUtils.titaliumRegular());
        tvTotalEarning.setTypeface(fontUtils.titaliumSemiBold());
        tvYourEarning.setTypeface(fontUtils.titaliumSemiBold());
        tvName.setTypeface(fontUtils.titaliumRegular());
        tvPhone.setTypeface(fontUtils.titaliumRegular());
        tvRating.setTypeface(fontUtils.titaliumRegular());
        tvRatingTitle.setTypeface(fontUtils.titaliumRegular());
        tvPickUp.setTypeface(fontUtils.titaliumRegular());
        tvDrop.setTypeface(fontUtils.titaliumRegular());
        tvPickUpTime.setTypeface(fontUtils.titaliumRegular());
        tvPickUpTimeTitle.setTypeface(fontUtils.titaliumRegular());
        tvDeliverTimeTitle.setTypeface(fontUtils.titaliumRegular());
        tvDeliverTime.setTypeface(fontUtils.titaliumRegular());
        tvShowOrderDetals.setTypeface(fontUtils.titaliumSemiBold());
        tvHeaderOrders.setTypeface(fontUtils.titaliumSemiBold());
        tvhideOrderDetals.setTypeface(fontUtils.titaliumSemiBold());
        tvGrandTotalTitle.setTypeface(fontUtils.titaliumSemiBold());
        tvGrandTotal.setTypeface(fontUtils.titaliumSemiBold());
        tvPaymentTitle.setTypeface(fontUtils.titaliumRegular());
        tvPayment.setTypeface(fontUtils.titaliumRegular());
        tvReceivedByTitle.setTypeface(fontUtils.titaliumRegular());
        tvRecvr.setTypeface(fontUtils.titaliumRegular());
        tvRecvrPhone.setTypeface(fontUtils.titaliumRegular());
    }


//    @Override
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

//    @Override
    public void setViews(Appointments appointment) {
        tv_bid.setText(appointment.getOrderId());
        tvTotalEarning.setText(Utility.formatPrice(appointment.getTotalAmount()));
        tvYourEarning.setText(Utility.formatPrice(appointment.getEarnedAmount()));
        tvName.setText(appointment.getCustomerName());
        tvPhone.setText(appointment.getCustomerPhone());
        tvRating.setText(appointment.getRating());
        tvPickUp.setText(appointment.getPickAddress());
        tvDrop.setText(appointment.getDropAddress());
        tvRecvr.setText(appointment.getCustomerName());
        tvRecvrPhone.setText(appointment.getCustomerPhone());

        if(appointment.getPaymentType().equals("2"))
            tvPayment.setText(getResources().getString(R.string.cash));
        else
            tvPayment.setText(getResources().getString(R.string.card));

        addItems(appointment);
    }

    public void addItems( Appointments appointments){
        llItemContainer.removeAllViews();
        int size=appointments.getItems().size();
        float total=0.0f;
        if(size>0){
            for(int i=0;i<size;i++){

                Utility.printLog("OrderHistory "+appointments.getCurrencySymbol());

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.store_details_item_single_row, null);

                TextView itemName= (TextView) view.findViewById(R.id.tvItemName);
                itemName.setTypeface(fontUtils.titaliumRegular());
                TextView itemPrice= (TextView) view.findViewById(R.id.tvItemPrice);
                itemPrice.setTypeface(fontUtils.titaliumRegular());
                TextView itemUnit= (TextView) view.findViewById(R.id.tvUnit);
                itemPrice.setTypeface(fontUtils.titaliumRegular());


                int quantity= Integer.parseInt(appointments.getItems().get(i).getQuantity());
                float unitPrice= Float.parseFloat(appointments.getItems().get(i).getUnitPrice());

                String item=appointments.getItems().get(i).getItemName();

                float subTotal=quantity*unitPrice;
                total+=subTotal;

                itemPrice.setText(appointments.getItems().get(i).getCurrencySymbol()+" "+
                        String.format(Locale.US,"%.2f",subTotal));

                itemName.setText(item);
                itemUnit.setText(getResources().getString(R.string.qty)+" "+quantity+"\n"+getResources().getString(R.string.unit)+": "+appointments.getItems().get(i).getUnitName());

//                itemUnit.setText("( "+quantity+" "+appointments.getItems().get(i).getUnitName()+" )");
                itemName.setTextColor(getResources().getColor(R.color.color_sup_txt));


                llItemContainer.addView(view);
            }
        }
        tvGrandTotal.setText(appointments.getItems().get(0).getCurrencySymbol()+" "+String.format(Locale.US,"%.2f",total));
    }

    @OnClick({R.id.tvShowOrderDetals,R.id.tvhideOrderDetals})
    public void onclick(View view){
      switch (view.getId()){
          case R.id.tvShowOrderDetals:
              presenter.showOrderDetails();
              break;

          case R.id.tvhideOrderDetals:
              presenter.hideOrderDetails();
              break;
      }
    }

//    @Override
    public void setTitle(String format) {
        tv_title.setText(format);
    }

//    @Override
    public void setPickUpTime(String format) {
        tvPickUpTime.setText(format);
    }

//    @Override
    public void setDropTime(String format) {
        tvDeliverTime.setText(format);
    }

    //@Override
    public void showItems() {
        llOrderedItems.setVisibility(View.VISIBLE);
        viewOrderBottomStyle.setVisibility(View.VISIBLE);
        ivOrderBottomStyle.setVisibility(View.VISIBLE);
        tvShowOrderDetals.setVisibility(View.GONE);
    }

    //@Override
    public void hideItems() {
        llOrderedItems.setVisibility(View.GONE);
        viewOrderBottomStyle.setVisibility(View.GONE);
        ivOrderBottomStyle.setVisibility(View.GONE);
        tvShowOrderDetals.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    //@Override
    public void networkError(String message) {

    }

    //@Override
    public void showProgress() {

    }

    //@Override
    public void hideProgress() {

    }
}
