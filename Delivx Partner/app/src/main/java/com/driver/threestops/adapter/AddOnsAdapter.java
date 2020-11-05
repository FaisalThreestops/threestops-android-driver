package com.driver.threestops.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.driver.Threestops.R;
import com.driver.threestops.pojo.AddOnGroup;
import com.driver.threestops.utility.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */
public class AddOnsAdapter extends RecyclerView.Adapter<AddOnsAdapter.ViewHolder> {

    private ArrayList<AddOnGroup> addOnGroupArrayList;
    private String currencySymbol;
    private int quantity;

    public AddOnsAdapter(ArrayList<AddOnGroup> addOnGroupArrayList, String currencySymbol, int quantity) {
        this.addOnGroupArrayList = addOnGroupArrayList;
        this.currencySymbol = currencySymbol;
        this.quantity = quantity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.addons_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Log.d("exe", "position" + i);
        AddOnGroup addOnGroup = addOnGroupArrayList.get(i);

        viewHolder.tvItemName.setText(addOnGroup.getName());
        viewHolder.etQuantity.setText(String.valueOf(quantity).concat(" (").concat(currencySymbol.concat(" ").concat(Utility.roundOfDoubleValue(addOnGroup.getPrice()))).concat(")"));
//        viewHolder.etUnitPrice.setText(addOnGroup.getPrice());
//
//        viewHolder.tv_currencySymbol.setText(currencySymbol.concat(" "));
        float unitPrice = 0.00f;
        if (!TextUtils.isEmpty(addOnGroup.getPrice()))
            unitPrice = Float.parseFloat(addOnGroup.getPrice());

        viewHolder.tvPrice.setText(currencySymbol.concat(" ").concat(Utility.roundOfDoubleValue(String.valueOf(quantity * unitPrice))));
    }

    @Override
    public int getItemCount() {
        return addOnGroupArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvItemName)
        TextView tvItemName;
        @BindView(R.id.etQuantity)
        EditText etQuantity;
        @BindView(R.id.tvPrice)
        TextView tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }
}
