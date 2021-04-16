package com.driver.threestops.app.replaceItem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.threestops.adapter.SearchItemsRVA;
import com.driver.threestops.adapter.UnitItemsRVA;
import com.driver.threestops.app.bookingRide.BookingRide;
import com.driver.threestops.app.invoice.InvoiceActivity;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.pojo.AssignedAppointments;
import com.driver.threestops.pojo.SearchPojo.Data;
import com.driver.threestops.pojo.SearchPojo.Units;
import com.driver.threestops.pojo.ShipmentDetails;
import com.driver.threestops.utility.AppConstants;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Utility;
import com.driver.Threestops.R;

import org.json.JSONException;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class ReplaceItemsActivity extends DaggerAppCompatActivity implements ReplaceItemsContract.ViewOperations, SearchItemsRVA.Messagepassing, UnitItemsRVA.CallQuantity  {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.search_item_et)
    EditText search_item_et;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.image_rl)
    RelativeLayout image_rl;

    @BindView(R.id.items_rc)
    RecyclerView items_rc;

    TextView confirm_tv;

    private String storeId;
    ArrayList<Data> dataArrayList = new ArrayList<>();
    private SearchItemsRVA searchItemsRVA;

    @Inject
    ReplaceItemsContract.PresenterOperations presenter;
    @Inject
    FontUtils fontUtils;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    private Dialog dialog;
    private ShipmentDetails shipmentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this, presenter.getlanguageCode());
        setContentView(R.layout.replace_item);
        ButterKnife.bind(this);
        initViews();
        presenter.getBundleData(getIntent().getExtras());
        presenter.setActionBar();
        presenter.setActionBarTitle();
    }

    private void initViews() {

        searchItemsRVA = new SearchItemsRVA(this, dataArrayList);
        items_rc.setLayoutManager(new LinearLayoutManager(this));
        items_rc.setAdapter(searchItemsRVA);

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
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_signup_close);
        }

        iv_search.setImageDrawable(getResources().getDrawable(R.drawable.ic_message_black_24dp));
        iv_search.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    @Override
    public void setViews(String storeId, ShipmentDetails shipmentDetails) {
        this.storeId = storeId;
        this.shipmentDetails = shipmentDetails;
    }

    @Override
    public void moveToStorePickup(AssignedAppointments appointments) {
        Log.d("exe", "appointments" + appointments);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", appointments);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        for (int i = 0; i < appointments.getShipmentDetails().size(); i++) {
            Log.d("exe", "addToCartOnReplace" + appointments.getShipmentDetails().get(i).getAddedToCartOn());
        }


      /*  Intent intent = new Intent(ReplaceItemsActivity.this, StorePickUp.class);
        intent.putExtras(bundle);
        startActivity(intent);*/
        setResult(Activity.RESULT_OK, intent);
        finish();

    }


    @Override
    public void onSuccess(AssignedAppointments appointments) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", appointments);
        if (appointments.getOrderStatus().equals(AppConstants.BookingStatus.Completed)) {
            Intent intent = new Intent(ReplaceItemsActivity.this, InvoiceActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(ReplaceItemsActivity.this, BookingRide.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void setItemsList(ArrayList<Data> dataArrayList) {
        this.dataArrayList.clear();
        if (dataArrayList.size() > 0) {
            items_rc.setVisibility(View.VISIBLE);
            this.dataArrayList.addAll(dataArrayList);
            searchItemsRVA.notifyDataSetChanged();
            image_rl.setVisibility(View.GONE);
        } else {
            items_rc.setVisibility(View.GONE);
            image_rl.setVisibility(View.VISIBLE);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        search_item_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is too short
                if (s.length() >= 3) {
                    presenter.getItems(search_item_et.getText().toString());
                } else {
                    items_rc.setVisibility(View.GONE);
                    image_rl.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void data(Data message) {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.order_edit_new_dialog);
        presenter.hideSoftKeyboard();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        UnitItemsRVA unitItemsRVA = new UnitItemsRVA(this, message);
        confirm_tv = dialog.findViewById(R.id.confirm_tv);
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        RecyclerView items_rv = dialog.findViewById(R.id.items_rv);
        items_rv.setLayoutManager(new LinearLayoutManager(this));
        items_rv.setAdapter(unitItemsRVA);
        unitItemsRVA.notifyDataSetChanged();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    presenter.updateQty();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void setQty(String message, Data data, Units unit) {
        presenter.setNewQty(message, data, unit);
        if(message.equals("0"))
            confirm_tv.setBackgroundColor(getResources().getColor(R.color.gray));
        else
            confirm_tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void error(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog!=null)
            dialog.dismiss();
    }
}
