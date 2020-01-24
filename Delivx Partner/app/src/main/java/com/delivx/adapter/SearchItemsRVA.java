package com.delivx.adapter;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delivx.app.replaceItem.ReplaceItemsActivity;
import com.delivx.pojo.SearchPojo.Data;
import com.driver.delivx.R;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchItemsRVA extends RecyclerView.Adapter<SearchItemsRVA.ViewHolder> {
    Context context;
    ArrayList<Data> dataArrayList;
    private Dialog dialog;

    public interface Messagepassing
    {
        public void data(Data message);
    }
    Messagepassing messagepassing;





    public SearchItemsRVA(ReplaceItemsActivity replaceItemsActivity, ArrayList<Data> dataArrayList) {
        this.dataArrayList=dataArrayList;
        context=replaceItemsActivity;
        messagepassing= (Messagepassing) context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(context);
        View view= (View) layoutInflater.inflate(R.layout.search_items_list,viewGroup,false);
        return new SearchItemsRVA.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {



        viewHolder.product_name.setText(dataArrayList.get(i).getProductName());

        viewHolder.available_quantity.setText(dataArrayList.get(i).getUnits().get(0).getAvailableQuantity());
        viewHolder.add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagepassing.data(dataArrayList.get(i));
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_name)
        TextView product_name;

        @BindView(R.id.available_quantity)
        TextView available_quantity;

        @BindView(R.id.add_bt)
        Button add_bt;

        @BindView(R.id.plus_iv)
        ImageView plus_iv;

        @BindView(R.id.minus_iv)
        ImageView minus_iv;

        @BindView(R.id.items_quantity_tv)
        TextView items_quantity_tv;

        @BindView(R.id.increment_item_rl)
        RelativeLayout increment_item_rl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

