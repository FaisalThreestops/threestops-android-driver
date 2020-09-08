package com.delivx.app.storeDetails.bottomItemView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.delivx.pojo.ShipmentDetails;
import com.delivx.utility.Utility;
import com.driver.Threestops.R;
import com.squareup.picasso.Picasso;

public class LaundryBottomSheetFragment extends BottomSheetDialogFragment  {

    private static final String PARAM1 = "param1";
    ShipmentDetails shipmentDetails;
    private Typeface fontRegular, fontLight;
    private ProgressDialog pDialog;
    private static final String TAG = "LaundryBottomSheetFragment";
    LayoutInflater inflater;

    public LaundryBottomSheetFragment()
    {

    }

    public static LaundryBottomSheetFragment newInstance(ShipmentDetails shipmentDetails) {
        LaundryBottomSheetFragment fragment = new LaundryBottomSheetFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM1, shipmentDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shipmentDetails = (ShipmentDetails) getArguments().getSerializable(PARAM1);
        }

        fontRegular = Utility.getFontRegular(getActivity());
        fontLight = Utility.getFontRegular(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.alert_dialog_laundry_detail,null);
        dialog.setContentView(view);

        TextView tv_item_name= view.findViewById(R.id.tv_item_name);
        TextView tv_quantity= view.findViewById(R.id.tv_quantity);
        ImageView ivCancel= view.findViewById(R.id.ivCancel);
        ImageView iv_item_img= view.findViewById(R.id.iv_item_img);


        tv_item_name.setTypeface(fontLight);
        tv_quantity.setTypeface(fontLight);

        if(shipmentDetails!=null)
        {
            tv_item_name.setText(shipmentDetails.getItemName());
            tv_quantity.setText(getResources().getString(R.string.quantity_cap)+shipmentDetails.getQuantity());

            Picasso.get().load(shipmentDetails.getItemImageURL())
                    .into(iv_item_img);
        }
        else
        {
            return;
        }

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

    }

}