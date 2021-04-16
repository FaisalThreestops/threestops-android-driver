package com.driver.threestops.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.driver.threestops.app.slotAppointments.SlotAppointmentActivity;
import com.driver.threestops.pojo.AssignedAppointments;
import com.driver.Threestops.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlotAppointmentRVA extends RecyclerView.Adapter<SlotAppointmentRVA.ViewHolder> {
    private Activity context;
    private ArrayList<AssignedAppointments> appointments;
    private ArrayList<AssignedAppointments> slotResponseAppointments;
    private ArrayList<AssignedAppointments> sameStoreBookingID= new ArrayList<>();
    private OrderIdRVA orderIdRVA;

    public SlotAppointmentRVA(SlotAppointmentActivity slotAppointmentActivity, ArrayList<AssignedAppointments> appointments, ArrayList<AssignedAppointments> slotResponseAppointments) {
        this.context=slotAppointmentActivity;
        this.appointments=appointments;
        this.slotResponseAppointments=slotResponseAppointments;
        pickupLoc= (PickupLoc) context;
    }

    @NonNull
    @Override
    public SlotAppointmentRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(context);
        View view= (View) layoutInflater.inflate(R.layout.slot_appointment_rva,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tvStoreName.setText(appointments.get(i).getStoreName());
        viewHolder.tvStoreAddress.setText(appointments.get(i).getPickUpAddress());
        if (i == appointments.size() - 1) {
            viewHolder.brekerView.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.brekerView.setVisibility(View.VISIBLE);
        }
        sameStoreBookingID.clear();
        for(int j=0;j<slotResponseAppointments.size();j++){
            if(slotResponseAppointments.get(j)!=null && appointments.get(i).getStoreId().equals(slotResponseAppointments.get(j).getStoreId()))
            {
                sameStoreBookingID.add(slotResponseAppointments.get(j));
            }
        }
        orderIdRVA=new OrderIdRVA(sameStoreBookingID,context);
        viewHolder.rvBookingId.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.rvBookingId.setAdapter(orderIdRVA);
        orderIdRVA.notifyDataSetChanged();
        viewHolder.cvStoreId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlotAppointmentActivity itemsListActivity=(SlotAppointmentActivity)context;
                itemsListActivity.startActivity(slotResponseAppointments,appointments,i);
            }
        });

        viewHolder.ivPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickupLoc.pickup(slotResponseAppointments);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public interface PickupLoc{
        public void pickup(ArrayList<AssignedAppointments> slotResponseAppointments);
    }

    PickupLoc pickupLoc;

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvStoreName)
        TextView tvStoreName ;

        @BindView(R.id.tvStoreAddress)
        TextView tvStoreAddress ;

        @BindView(R.id.rvBookingId)
        RecyclerView rvBookingId ;

        @BindView((R.id.cvStoreId))
        CardView cvStoreId;

        @BindView(R.id.brekerView)
        View brekerView;

        @BindView(R.id.ivPickUp)
        ImageView ivPickUp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
