package com.driver.threestops.app.storeDetails;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.driver.Threestops.R;
import com.driver.threestops.adapter.AddOnsAdapter;
import com.driver.threestops.adapter.LaunderListRVA;
import com.driver.threestops.adapter.ReasonRVA;
import com.driver.threestops.app.bookingRide.BookingRide;
import com.driver.threestops.app.main.MainActivity;
import com.driver.threestops.mqttChat.ChattingActivity;
import com.driver.threestops.pojo.AddOnGroup;
import com.driver.threestops.pojo.AddOns;
import com.driver.threestops.pojo.AssignedAppointments;
import com.driver.threestops.pojo.Cancel.CancelData;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Slider;
import com.driver.threestops.utility.TextUtil;
import com.driver.threestops.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class StorePickUpDetails extends DaggerAppCompatActivity implements
        StoreDetailsContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_cust_name_title)
    TextView tv_cust_name_title;
    @BindView(R.id.tv_cust_name)
    TextView tv_cust_name;
    @BindView(R.id.iv_call_customer)
    ImageView iv_call_customer;
    @BindView(R.id.tv_pick_up_title)
    TextView tv_pick_up_title;
    @BindView(R.id.tv_pickup)
    TextView tv_pickup;
    @BindView(R.id.iv_call_pickUp)
    ImageView iv_call_pickUp;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.tv_drop_up_title)
    TextView tv_drop_up_title;
    @BindView(R.id.tv_dropup)
    TextView tv_dropup;
    @BindView(R.id.tv_payment_title)
    TextView tv_payment_title;
    @BindView(R.id.tv_estimate_title)
    TextView tv_estimate_title;
    @BindView(R.id.tv_customer_notes)
    TextView tv_customer_notes;
    @BindView(R.id.tv_payment_type)
    TextView tv_payment_type;
    @BindView(R.id.ll_estimate_value)
    LinearLayout ll_estimate_value;
    @BindView(R.id.tv_customer_value)
    TextView tv_customer_value;
    @BindView(R.id.ll_customer_note)
    LinearLayout ll_customer_note;
    @BindView(R.id.tv_estimate_value)
    TextView tv_estimate_value;
    @BindView(R.id.tvProductsTitle)
    TextView tvProductsTitle;
    @BindView(R.id.tvPriceTitle)
    TextView tvPriceTitle;
    @BindView(R.id.tvItems)
    TextView tvItems;
    @BindView(R.id.tv_qty)
    TextView tv_qty;
    @BindView(R.id.tv_status_text)
    TextView tv_status_text;
    @BindView(R.id.ll_item_container)
    LinearLayout ll_item_container;
    @BindView(R.id.ll_tax)
    LinearLayout ll_tax;
    @BindView(R.id.ll_tax_item_container)
    LinearLayout ll_tax_item_container;
    @BindView(R.id.ll_subTotal)
    LinearLayout ll_subTotal;
    @BindView(R.id.tv_tax)
    TextView tv_tax;
    @BindView(R.id.myseek)
    Slider seekbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.Bottomlayout)
    RelativeLayout bottomLayout;
    @BindView(R.id.rv_item_show)
    RecyclerView rv_item_show;
    @BindView(R.id.view_estimate)
    View view_estimate;
    @BindView(R.id.view_customer)
    View view_customer;
    private Typeface font, fontBold;

    @BindView(R.id.tv_paymentbreskdown)
    TextView tv_paymentbreskdown;
    @BindView(R.id.tv_subTotal)
    TextView tv_subTotal;
    @BindView(R.id.tv_subTotal_val)
    TextView tv_subTotal_val;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.tv_delCharge)
    TextView tv_delCharge;
    @BindView(R.id.tv_delCharge_val)
    TextView tv_delCharge_val;
    @BindView(R.id.tv_discount)
    TextView tv_discount;
    @BindView(R.id.tv_discount_val)
    TextView tv_discount_val;
    @BindView(R.id.ll_discount)
    LinearLayout ll_discount;
    @BindView(R.id.tv_subToatal)
    TextView tv_subToatal;
    @BindView(R.id.tv_subToatal_val)
    TextView tv_subToatal_val;
    @BindView(R.id.ll_tip)
    LinearLayout ll_tip;
    @BindView(R.id.tv_tip)
    TextView tv_tip;
    @BindView(R.id.tv_tip_val)
    TextView tv_tip_val;

    @BindView(R.id.ll_wallet)
    LinearLayout ll_wallet;
    @BindView(R.id.ll_cash)
    LinearLayout ll_cash;
    @BindView(R.id.tv_wallet_amount)
    TextView tv_wallet_amount;
    @BindView(R.id.tv_cashAmt)
    TextView tv_cashAmt;

    private boolean tax_added = false;
    private Dialog dialog;
    private AlertDialog.Builder alertDialog;

    @Inject
    StoreDetailsContract.Presenter presenter;
    @Inject
    FontUtils fontUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this, presenter.getlanguageCode());
        setContentView(R.layout.activity_store_pick_up_details);
        ButterKnife.bind(this);
        initViews();
        presenter.getBundleData(getIntent().getExtras());
        presenter.setActionBar();
        presenter.setActionBarTitle();

    }

    /**
     * <h2>initViews</h2>
     * <h2>initializing the views</h2>
     */
    public void initViews() {
        font = fontUtils.titaliumRegular();
        fontBold = fontUtils.titaliumSemiBold();

        tv_title.setTypeface(fontBold);
        tv_status_text.setTypeface(fontBold);
        tv_cust_name_title.setTypeface(font);
        tv_cust_name.setTypeface(font);
        tv_pick_up_title.setTypeface(font);
        tv_pickup.setTypeface(font);
        tv_drop_up_title.setTypeface(font);
        tv_dropup.setTypeface(font);
        tv_cancel.setTypeface(font);
        tv_payment_title.setTypeface(font);
        tv_estimate_title.setTypeface(font);
        tv_customer_notes.setTypeface(font);
        tv_customer_value.setTypeface(font);
        tv_payment_type.setTypeface(font);
        tv_estimate_value.setTypeface(font);
        tvProductsTitle.setTypeface(font);
        tvPriceTitle.setTypeface(fontBold);
        tvItems.setTypeface(fontBold);

        tv_subTotal.setTypeface(font);
        tv_subTotal_val.setTypeface(font);
        tv_delCharge.setTypeface(font);
        tv_delCharge_val.setTypeface(font);
        tv_discount.setTypeface(font);
        tv_discount_val.setTypeface(font);
        tv_tip.setTypeface(font);
        tv_tip_val.setTypeface(font);
        tv_tax.setTypeface(font);
        tv_subToatal.setTypeface(fontBold);
        tv_subToatal_val.setTypeface(fontBold);
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
        if (appointments.getPaymentType().equals("2"))
            tv_payment_type.setText(getResources().getString(R.string.cash));
        else if (appointments.getPaymentType().equals("1"))
            tv_payment_type.setText(getResources().getString(R.string.card));
        else
            tv_payment_type.setText(getResources().getString(R.string.lbl_wallet));

        if (appointments.getStoreType().equals("7")) {
            ll_estimate_value.setVisibility(View.VISIBLE);
            view_estimate.setVisibility(View.VISIBLE);
            ll_customer_note.setVisibility(View.VISIBLE);
            view_customer.setVisibility(View.VISIBLE);
            tvPriceTitle.setVisibility(View.VISIBLE);
            tv_qty.setVisibility(View.INVISIBLE);
            tvPriceTitle.setText(this.getResources().getString(R.string.quant));
            tvPriceTitle.setGravity(Gravity.RIGHT);
            ll_subTotal.setVisibility(View.GONE);
            tv_cancel.setVisibility(View.VISIBLE);
            tv_cancel.setPaintFlags(tv_cancel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            ll_estimate_value.setVisibility(View.GONE);
            view_estimate.setVisibility(View.GONE);
            ll_customer_note.setVisibility(View.GONE);
            view_customer.setVisibility(View.GONE);
            tv_qty.setVisibility(View.VISIBLE);
            ll_subTotal.setVisibility(View.VISIBLE);
            tv_cancel.setVisibility(View.GONE);
        }

        tv_estimate_value.setText(appointments.getCurrencySymbol() + "" + appointments.getEstimatedPackageValue());
        tv_customer_value.setText(appointments.getExtraNote());

        if (appointments.getStoreType().equals("7") && appointments.getExtraNote().equals("")) {
            ll_customer_note.setVisibility(View.GONE);
            view_customer.setVisibility(View.GONE);
        }

        if (appointments.getStoreType().matches("5") &&
                !appointments.isCominigFromStore()) {

            iv_call_customer.setVisibility(View.GONE);
            iv_call_pickUp.setVisibility(View.GONE);
            tv_pick_up_title.setText(getResources().getString(R.string.pickup_location));
            tv_pickup.setText(appointments.getPickUpAddress());
            tv_drop_up_title.setText(getResources().getString(R.string.pickup_slot));
            tv_dropup.setText("12 AM -12 PM");
            tvProductsTitle.setText(getResources().getString(R.string.laundry_details));
            rv_item_show.setVisibility(View.VISIBLE);

            LaunderListRVA launderListRVA = new LaunderListRVA(this,
                    appointments.getShipmentDetails(), font, this.getSupportFragmentManager());
            rv_item_show.setLayoutManager(new LinearLayoutManager(this));
            rv_item_show.setAdapter(launderListRVA);


        } else {

            String text = "<b>" + appointments.getStoreName() + "</b> " + " : " + appointments.getPickUpAddress();
            tv_pickup.setText(Html.fromHtml(text));
            tv_dropup.setText(appointments.getDropAddress());

        }


        double sub_total_amount = Double.parseDouble(appointments.getSubTotalAmount());
        tv_subTotal_val.setText(String.format(Locale.getDefault(), "%s %s", appointments.getCurrencySymbol(), String.format(Locale.getDefault(), "%.2f", sub_total_amount)));

        double deliveryCharge = Double.parseDouble(appointments.getDeliveryCharge());
        tv_delCharge_val.setText(String.format("%s %s", appointments.getCurrencySymbol(), String.format(Locale.getDefault(), "%.2f", deliveryCharge)));

        double totalAmount = Double.parseDouble(appointments.getTotalAmount());
        tv_subToatal_val.setText(String.format("%s %s", appointments.getCurrencySymbol(), String.format(Locale.getDefault(), "%.2f", totalAmount)));

        double appliedDiscount = Double.parseDouble(appointments.getDiscount());
        tv_discount_val.setText(String.format("%s %s", appointments.getCurrencySymbol(), String.format(Locale.getDefault(), "%.2f", appliedDiscount)));

        if (!TextUtil.isEmpty(appointments.getDriverTip())) {
            double driverTip = Double.parseDouble(appointments.getDriverTip());
            tv_tip_val.setText(new StringBuilder().append(appointments.getCurrencySymbol()).append(" ").append(String.format(Locale.getDefault(), "%.2f", driverTip)));
        } else {
            ll_tip.setVisibility(View.GONE);
        }

        double cash = Double.parseDouble(appointments.getCashCollect());
        double wallet = 0;
        ll_wallet.setVisibility(View.VISIBLE);
        ll_cash.setVisibility(View.VISIBLE);
        wallet = totalAmount - cash;
        tv_wallet_amount.setText(appointments.getCurrencySymbol() + " " + Utility.currencyFormat(wallet + ""));
        tv_cashAmt.setText(appointments.getCurrencySymbol() + " " + Utility.currencyFormat(cash + ""));

        addItems(appointments);
    }

    @Override
    public void onSuccess(AssignedAppointments appointments) {

        Intent intent = new Intent(StorePickUpDetails.this, BookingRide.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", appointments);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    @Override
    public void onError(String message) {
        seekbar.setProgress(0);
        Utility.mShowMessage(getResources().getString(R.string.message), message, this);
    }

    @Override
    public void hideViews() {
        bottomLayout.setVisibility(View.GONE);
        iv_call_pickUp.setVisibility(View.GONE);
        iv_call_customer.setVisibility(View.GONE);
        tv_cancel.setVisibility(View.GONE);
    }

    @Override
    public void hideSeekBar() {
        bottomLayout.setVisibility(View.GONE);
    }

    @Override
    public void cancelDialog(CancelData cancelData) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cancel_reason_row);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        presenter.hideSoftKeyboard();
        ReasonRVA reasonRVA = new ReasonRVA(this, cancelData, 1);
        RecyclerView rv_reasons = dialog.findViewById(R.id.bottomSheetReasonRv);
//        Button confirm=dialog.findViewById(R.id.createNewListTv);
        ImageView cancelDialog = dialog.findViewById(R.id.iv_jobdetails);
        rv_reasons.setLayoutManager(new LinearLayoutManager(this));
        rv_reasons.setAdapter(reasonRVA);
        reasonRVA.notifyDataSetChanged();
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                presenter.cancelOrder();
//            }
//        });
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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


    @OnClick({R.id.iv_call_customer, R.id.iv_call_pickUp, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            //call to customer
            case R.id.iv_call_customer:
                presenter.callCustomer(true);
                break;
            //call to store
            case R.id.iv_call_pickUp:
                presenter.callCustomer(false);
                break;

            case R.id.tv_cancel:
                presenter.cancelReason();
                break;

        }

    }

    @OnClick(R.id.iv_search)
    public void chattingOnclick(View view) {
        presenter.openChat();
    }

    @SuppressLint("SetTextI18n")
    public void addItems(AssignedAppointments appointments) {
        int size = appointments.getShipmentDetails().size();
        float total = 0.0f;
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.history_item_show_single2, null);

                TextView itemName = view.findViewById(R.id.tvItemName);
                itemName.setTypeface(font);
                TextView itemPrice = view.findViewById(R.id.tvItemPrice);
                itemPrice.setTypeface(font);
                TextView itemUnit = view.findViewById(R.id.tvQuantity);
                itemPrice.setTypeface(font);

                ImageView iv_item_img = view.findViewById(R.id.iv_item_img);
                if (!appointments.getShipmentDetails().get(i).getItemImageURL().equals("") && !appointments.getShipmentDetails().get(i).getItemImageURL().isEmpty()) {
                    Picasso.get().load(appointments.getShipmentDetails().get(i).getItemImageURL()).placeholder(R.drawable.login_logo).error(R.drawable.login_logo).into(iv_item_img);
                } else {
                    iv_item_img.setVisibility(View.GONE);
                }

                ArrayList<AddOnGroup> addOnGroupArrayList = new ArrayList<>();

                RecyclerView rvAddOnsList = view.findViewById(R.id.rvAddOnsList);
                rvAddOnsList.setNestedScrollingEnabled(false);
                rvAddOnsList.setLayoutManager(new LinearLayoutManager(this));
                rvAddOnsList.setHasFixedSize(true);
                int quantity = (!TextUtil.isEmpty(appointments.getShipmentDetails().get(i).getQuantity()) && TextUtils.isDigitsOnly(appointments.getShipmentDetails().get(i).getQuantity()) ? Integer.parseInt(appointments.getShipmentDetails().get(i).getQuantity()) : 0);
                AddOnsAdapter addOnsAdapter = new AddOnsAdapter(addOnGroupArrayList, appointments.getCurrencySymbol(), quantity);
                rvAddOnsList.setAdapter(addOnsAdapter);
                addOnGroupArrayList.clear();

                if (appointments.getShipmentDetails().get(i).getAddOns() != null && appointments.getShipmentDetails().get(i).getAddOns().size() > 0) {
                    for (AddOns addOns : appointments.getShipmentDetails().get(i).getAddOns()) {
                        if (addOns.getAddOnGroup() != null && addOns.getAddOnGroup().size() > 0)
                            addOnGroupArrayList.addAll(addOns.getAddOnGroup());
                    }
                }

                if (addOnGroupArrayList.size() > 0) {
                    rvAddOnsList.setVisibility(View.VISIBLE);
                    addOnsAdapter.notifyDataSetChanged();
                }

                if (appointments.getStoreType().equals("7")) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2f);
                    itemName.setLayoutParams(params);
                    itemName.setGravity(Gravity.START);

                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                    itemUnit.setLayoutParams(params1);
                    itemUnit.setGravity(Gravity.END);

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f);
                    itemPrice.setLayoutParams(params2);
                    itemPrice.setGravity(Gravity.END);
                    itemPrice.setVisibility(View.GONE);
                } else {
                    itemPrice.setVisibility(View.VISIBLE);
                }

                itemName.setText(appointments.getShipmentDetails().get(i).getItemName());
                String unitPriceStr = appointments.getShipmentDetails().get(i).getFinalPrice();
                float unitPrice = 0;
                if (!"".equals(unitPriceStr) && unitPriceStr != null)
                    unitPrice = Float.parseFloat(unitPriceStr);
                float subTotal = quantity * unitPrice;
                itemUnit.setText(appointments.getShipmentDetails().get(i).getQuantity() + " (" + appointments.getCurrencySymbol().concat(" ").concat(String.format(Locale.US, "%.2f", unitPrice)) + ")");
                itemPrice.setText(appointments.getCurrencySymbol().concat(" ").concat(String.format(Locale.US, "%.2f", subTotal)));


                total += subTotal;

                ll_item_container.addView(view);


                if (appointments.getExcTax() != null) {
                    float exc_tax = Float.parseFloat(appointments.getExcTax());
                    if (exc_tax > 0 && !tax_added) {
                        addTaxItems(appointments);
                    } else {
                        ll_tax.setVisibility(View.GONE);
                    }
                } else
                    ll_tax.setVisibility(View.GONE);

                /*if(appointments.getShipmentDetails().get(i).getAddOns()!=null) {
                    String addOns = "";
                    addOns = appointments.getShipmentDetails().get(i).getAddOns().size() > 0 ? "Addons: " + appointments.getShipmentDetails().get(i).getAddOns().toString() : "";

                    itemUnit.setText(getResources().getString(R.string.qty) + " " + quantity + "\n" + getResources().getString(R.string.unit) + ": " + appointments.getShipmentDetails().get(i).getUnitName() + "\n" + addOns);
                    ll_item_container.addView(view);
                }else
                    ll_item_container.setVisibility(View.GONE);*/


            }
        }
    }

    /**
     * <h2>addTaxItems</h2>
     * <p>calculate and adding the tax view layout</p>
     *
     * @param appointments
     */
    public void addTaxItems(AssignedAppointments appointments) {
        tax_added = true;
        int size = appointments.getExclusiveTaxes().size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.history_item_show_single_raw, null);

                TextView itemName = view.findViewById(R.id.tvItemName);
                itemName.setTypeface(font, Typeface.ITALIC);
                itemName.setTextColor(getResources().getColor(R.color.divider2));
                TextView itemPrice = view.findViewById(R.id.tvItemPrice);
                itemPrice.setTypeface(font);
                itemPrice.setTextColor(getResources().getColor(R.color.divider2));
                TextView itemUnit = view.findViewById(R.id.tvQuantity);
                itemPrice.setTypeface(font, Typeface.ITALIC);
                itemUnit.setVisibility(View.INVISIBLE);


                String tax_name = "(" + appointments.getExclusiveTaxes().get(i).getTaxCode() + "%" + ")";
                itemName.setText(tax_name);

                float tax_price = Float.parseFloat(appointments.getExclusiveTaxes().get(i).getPrice());
                itemPrice.setText(appointments.getCurrencySymbol() + " " +
                        String.format(Locale.US, "%.2f", tax_price));

                ll_tax_item_container.addView(view);

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void moveHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void openChatAct(AssignedAppointments appointments) {

        Intent intent = new Intent(this, ChattingActivity.class);
        intent.putExtra("BID", appointments.getBid());
        intent.putExtra("CUST_ID", appointments.getCustomerId());
        intent.putExtra("CUST_NAME", appointments.getCustomerName());
        startActivity(intent);
    }

    public void showDialog() {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getApplicationContext().getResources().getString(R.string.message));
        alertDialog.setMessage(getApplicationContext().getResources().getString(R.string.want_to_cancel_item));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(this.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.cancelOrder();
                    }
                });
        alertDialog.setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
