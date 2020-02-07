package com.delivx.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.delivx.app.replaceItem.ReplaceItemsActivity;
import com.delivx.pojo.SearchPojo.Data;
import com.delivx.pojo.SearchPojo.Units;
import com.driver.delivx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnitItemsRVA extends RecyclerView.Adapter<UnitItemsRVA.ViewHolder> {
    Context context;
    Data data;

    public interface CallQuantity
    {
        public void setQty(String message, Data data, Units unit);
        public void error(String errorMsg);
    }
    CallQuantity callQuantity;

    public UnitItemsRVA(ReplaceItemsActivity replaceItemsActivity, Data data) {
        context=replaceItemsActivity;
        this.data=data;
        callQuantity= (CallQuantity) replaceItemsActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.increment_or_decrement_items,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tvItem_Name.setText(data.getUnits().get(i).getName());
        viewHolder.unit.setText("$"+data.getUnits().get(i).getFloatValue());
        viewHolder.items_quantity_tv.setText("0");
        viewHolder.plus_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuantity();
            }

            private void incrementQuantity() {
                int value=Integer.valueOf( viewHolder.items_quantity_tv.getText().toString());
                int avaiableQty=Integer.valueOf(data.getUnits().get(i).getAvailableQuantity());
                if(value>=0 && value<avaiableQty){
                    value++;
                    viewHolder.items_quantity_tv.setText(value+"");
                    callQuantity.setQty(viewHolder.items_quantity_tv.getText().toString(),data,data.getUnits().get(i));
                }
                else{
                    callQuantity.error("out of stock");
                }
            }
        });

        viewHolder.minus_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity();
            }

            private void decrementQuantity() {
                int value=Integer.valueOf( viewHolder.items_quantity_tv.getText().toString());
                int avaiableQty=Integer.valueOf(data.getUnits().get(i).getAvailableQuantity());
                if(value>1){
                    --value;
                    viewHolder.items_quantity_tv.setText(value+"");
                    callQuantity.setQty(viewHolder.items_quantity_tv.getText().toString(),data,data.getUnits().get(i));
                }
                else{
                    callQuantity.error("invalid quantity");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.getUnits().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvItem_Name)
        TextView tvItem_Name;

        @BindView(R.id.unit)
        TextView unit;

        @BindView(R.id.minus_iv)
        ImageView minus_iv;

        @BindView(R.id.plus_iv)
        ImageView plus_iv;

        @BindView(R.id.items_quantity_tv)
        TextView items_quantity_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

