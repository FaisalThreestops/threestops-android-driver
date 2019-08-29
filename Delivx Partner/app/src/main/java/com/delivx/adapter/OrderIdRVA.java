package com.delivx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.delivx.pojo.AssignedAppointments;
import com.driver.delivx.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderIdRVA extends RecyclerView.Adapter<OrderIdRVA.ViewHolder> {
    private ArrayList<AssignedAppointments> slotResponseAppointments;
    Context context;

    public OrderIdRVA(ArrayList<AssignedAppointments> slotResponseAppointments, Context context) {
        this.slotResponseAppointments=slotResponseAppointments;
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(context);
        View view= (View) layoutInflater.inflate(R.layout.booking_id,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.tvBookingId.setText("OID: "+slotResponseAppointments.get(position).getBid());
    }

    @Override
    public int getItemCount() {
        return slotResponseAppointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvBookingId)
        TextView tvBookingId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}