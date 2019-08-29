package com.delivx.app.OrderEdit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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

import com.delivx.app.ReplaceItem.ReplaceItemsActivity;
import com.delivx.app.bookingRide.BookingRide;
import com.delivx.app.invoice.InvoiceActivity;
import com.delivx.app.main.MainActivity;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.mqttChat.ChattingActivity;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.ShipmentDetails;
import com.delivx.utility.AppConstants;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Slider;
import com.delivx.utility.Utility;
import com.driver.delivx.R;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static android.view.View.VISIBLE;

public class OrderEditActivity extends DaggerAppCompatActivity implements OrderEditContract.ViewOperations {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.product_name)
    TextView product_name;
    @BindView(R.id.tv_qty)
    TextView tv_qty;
    @BindView(R.id.total_price_tv)
    TextView total_price_tv;
    @BindView(R.id.customer_name_tv)
    TextView customer_name_tv;
    @BindView(R.id.return_partially_cv)
    CardView return_partially_cv;
    @BindView(R.id.return_Completely_cv)
    CardView cv_return_completly;
    @BindView(R.id.unit_cost_tv)
    TextView unit_cost_tv;
    @BindView(R.id.partially_rl)
    RelativeLayout partially_rl;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.iv_message_icon)
    ImageView iv_message_icon;

    @BindView(R.id.return_rl)
    RelativeLayout return_rl;

    private AssignedAppointments appointments;
    private ShipmentDetails shipmentDetails;


    @Inject
    OrderEditContract.PresenterOperations presenter;
    @Inject
    FontUtils fontUtils;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this, presenter.getlanguageCode());
        setContentView(R.layout.activity_order_edit);
        ButterKnife.bind(this);

        presenter.getBundleData(getIntent().getExtras());
        presenter.setActionBar();
        presenter.setActionBarTitle();
        initViews();
    }

    private void initViews() {
    }

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.history_back_icon_off);
        }
    }

    @Override
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    @OnClick({R.id.iv_call_customer,R.id.iv_message_icon,R.id.replace_rl, R.id.cancel_item_rl,R.id.return_partially_cv,R.id.return_Completely_cv})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_call_customer:
                presenter.callCustomer();
                break;

            case R.id.iv_message_icon:
                presenter.openChat();
                break;

            case R.id.return_Completely_cv:
                presenter.returnCompletelyAlert();
                break;

            case R.id.replace_rl:
                presenter.openReplaceActivity();
                break;

            case R.id.cancel_item_rl:
                presenter.cancelAlert();
                break;

            case R.id.return_partially_cv:
                presenter.openPartiallyDialog();
                break;


        }
    }


    @Override
    public void setViews(ShipmentDetails shipmentDetails, AssignedAppointments appointments) {
        this.appointments = appointments;
        this.shipmentDetails = shipmentDetails;
        product_name.setText(shipmentDetails.getItemName());
        tv_qty.setText(shipmentDetails.getQuantity());
        unit_cost_tv.setText(Utility.formatPrice(shipmentDetails.getUnitPrice()));
        total_price_tv.setText(" X " + appointments.getCurrencySymbol() + Utility.formatPrice(shipmentDetails.getFinalPrice()));
        customer_name_tv.setText(appointments.getCustomerName());
        if (appointments.getOrderStatus().equals(AppConstants.BookingStatus.ReachedAtLocation)) {
            return_rl.setVisibility(View.GONE);
            partially_rl.setVisibility(VISIBLE);
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
    public void moveToReplaceActivity() {

        Intent intent = new Intent(this, ReplaceItemsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("StoreID", appointments.getStoreId());
        bundle.putString("BID", appointments.getBid());
        bundle.putString("deliveryCharge", appointments.getDeliveryCharge());
        bundle.putSerializable("appointment", appointments);
        bundle.putSerializable("shipmentDetails", shipmentDetails);
        intent.putExtras(bundle);
        startActivityForResult(intent, 123);
    }


    @Override
    public void setCancelAlert() {
        if(appointments.getShipmentDetails().size()>1) {
            dialog = new Dialog(OrderEditActivity.this);
            dialog.setContentView(R.layout.new_order_edit_dailog);
            TextView message = dialog.findViewById(R.id.message_tv);
            TextView tvNo = dialog.findViewById(R.id.tvNo);
            TextView tvYes = dialog.findViewById(R.id.tvYes);
            message.setText(R.string.want_to_cancel_item);
            dialog.setCancelable(false);
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.cancelItem("3","");
                }
            });
            dialog.show();
        }
        else
            Toast.makeText(this,"Single item unable to cancel",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setReturnCompleteAlert() {
        dialog = new Dialog(OrderEditActivity.this);
        dialog.setContentView(R.layout.new_order_edit_dailog);
        TextView message = dialog.findViewById(R.id.message_tv);
        TextView tvNo = dialog.findViewById(R.id.tvNo);
        TextView tvYes = dialog.findViewById(R.id.tvYes);
        if(appointments.getShipmentDetails().size()>1)
            message.setText(R.string.return_item_completely);
        else
            message.setText(R.string.last_item_delet_text);
        dialog.setCancelable(true);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appointments.getShipmentDetails().size()>1){
                    presenter.cancelItem("3","");
                }else {
                    presenter.cancelAllItem(shipmentDetails, "");
                }
            }
        });
        dialog.show();
    }

    @Override
    public void moveHomeActivity() {
        Intent intent=new Intent(this, MainActivity.class);
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

    @Override
    public void itemCanceled(AssignedAppointments assignedAppointments) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", assignedAppointments);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void setReturnQty() {
        dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.return_partially);
        TextView tvItem_Name=dialog.findViewById(R.id.tvItem_Name);
        TextView tvItem_Price=dialog.findViewById(R.id.tvItem_Price);
        final EditText etQuantity=dialog.findViewById(R.id.etQuantity);
        etQuantity.setText(shipmentDetails.getQuantity());
        TextView tvUpdate=dialog.findViewById(R.id.tvUpdate);
        TextView tvdelete=dialog.findViewById(R.id.tvdelete);
        tvItem_Name.setText(shipmentDetails.getItemName());
        tvItem_Price.setText(" X $"+Utility.formatPrice(shipmentDetails.getUnitPrice()));
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity=Integer.parseInt(shipmentDetails.getQuantity())-Integer.parseInt(etQuantity.getText().toString());
                if(Integer.parseInt(shipmentDetails.getQuantity())<Integer.parseInt(etQuantity.getText().toString())) {
                    Toast.makeText(OrderEditActivity.this,"Can't return",Toast.LENGTH_SHORT).show();
                }
                else{
//                    presenter.updateOrder(shipmentDetails,String.valueOf(quantity),true);
                    presenter.cancelItem("1",String.valueOf(quantity));
                }
            }
        });
        tvdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        dialog.dismiss();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            switch (requestCode) {

                case 123:

                    if (resultCode == Activity.RESULT_OK) {
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    }

                    break;
                default:
                    break;
            }
        }
    }
}
