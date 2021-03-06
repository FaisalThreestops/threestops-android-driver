package com.driver.threestops.adapter;

import android.content.Context;
import android.graphics.Typeface;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.driver.threestops.app.storeDetails.StorePickUpDetails;
import com.driver.threestops.app.storeDetails.bottomItemView.LaundryBottomSheetFragment;
import com.driver.threestops.pojo.ShipmentDetails;
import com.driver.Threestops.R;

import java.util.ArrayList;


public class LaunderListRVA extends RecyclerView.Adapter<LaunderListRVA.ViewHolder> {

    private ArrayList<ShipmentDetails> shipmentDetails;
    private Context context;
    private Typeface font;
    private FragmentManager fragmentManager;


    public LaunderListRVA(StorePickUpDetails context, ArrayList<ShipmentDetails> shipmentDetails, Typeface font, FragmentManager fragmentManager) {
        this.context = context;
        this.shipmentDetails = shipmentDetails;
        this.font = font;
        this.fragmentManager = fragmentManager;
    }

    /**********************************************************************************************/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_launder_layout, parent, false);
        return new LaunderListRVA.ViewHolder(view);

    }

    /**********************************************************************************************/
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_laundry_item.setText(shipmentDetails.get(position).getItemName());
        holder.tv_laundry_item_quan.setText("X "+shipmentDetails.get(position).getQuantity());
        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheetDialogFragment = LaundryBottomSheetFragment.newInstance(
                        shipmentDetails.get(position));
                bottomSheetDialogFragment.show(fragmentManager, bottomSheetDialogFragment.getTag());
            }
        });

    }

    /**********************************************************************************************/
    @Override
    public int getItemCount() {
        return shipmentDetails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_laundry_item;
        private TextView tv_laundry_item_quan;
        private RelativeLayout rl_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_layout = itemView.findViewById(R.id.rl_layout);
            tv_laundry_item = itemView.findViewById(R.id.tv_laundry_item);
            tv_laundry_item.setTypeface(font);
            tv_laundry_item_quan = itemView.findViewById(R.id.tv_laundry_item_quan);
            tv_laundry_item_quan.setTypeface(font);

        }
    }


}
