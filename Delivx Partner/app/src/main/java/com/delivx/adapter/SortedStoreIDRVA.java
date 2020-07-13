package com.delivx.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.AppConstants;
import com.driver.Threestops.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SortedStoreIDRVA extends RecyclerView.Adapter<SortedStoreIDRVA.ViewHolder>{
    Activity context;
    ArrayList<AssignedAppointments> appointments;
    ArrayList<AssignedAppointments> sortStoreId;
    private ArrayList<AssignedAppointments> sameStoreBookingID= new ArrayList<>();
    private Set_the_order_list_rva set_the_order_list_rva;
    String bookingStatus;

    public SortedStoreIDRVA(Activity context, ArrayList<AssignedAppointments> appointments, ArrayList<AssignedAppointments> sortStoreId) {
        this.appointments=appointments;
        this.context=context;
        this.sortStoreId=sortStoreId;
        this.bookingStatus=bookingStatus;
        pickupLoc = (PickupLoc) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(context);
        View view= (View) layoutInflater.inflate(R.layout.store_id_booking_details,viewGroup,false);
        return new SortedStoreIDRVA.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        try {
            if (Integer.parseInt(appointments.get(0).getOrderStatus())>=(Integer.parseInt(AppConstants.BookingStatus.Arrived))) {
                viewHolder.afetr_arrive_ll.setVisibility(View.VISIBLE);
                viewHolder.before_arrive_ll.setVisibility(View.GONE);
                String text = "<b>" + appointments.get(position).getStoreName() + "</b> "+" : "+appointments.get(0).getPickUpAddress() ;
                viewHolder.item_and_address_tv.setText(Html.fromHtml(text));
            }
            else{
                viewHolder.shore_name_tv.setText(sortStoreId.get(position).getStoreName());
                viewHolder.store_address_tv.setText(sortStoreId.get(position).getPickUpAddress());
            }
        }catch (Exception e){
            e.getMessage();
        }
        sameStoreBookingID.clear();
        for(int j=0;j<appointments.size();j++){

            Log.d("exe","sotID"+sortStoreId.get(position).getStoreId());

            Log.d("exe","appoin"+appointments.get(j).getStoreId());

            if(sortStoreId.get(position).getStoreId().equals(appointments.get(j).getStoreId()))
            {
                sameStoreBookingID.add(appointments.get(j));
            }

        }
        set_the_order_list_rva=new Set_the_order_list_rva(sameStoreBookingID,context);
        viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.recyclerView.setAdapter(set_the_order_list_rva);
        set_the_order_list_rva.notifyDataSetChanged();

        viewHolder.ivPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickupLoc.pickup();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sortStoreId.size();
    }

    PickupLoc pickupLoc;
    public interface PickupLoc{
        public void pickup();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.shore_name_tv)
        TextView shore_name_tv;

        @BindView(R.id.store_address_tv)
        TextView store_address_tv;

        @BindView(R.id.store_BID_rv)
        RecyclerView recyclerView;

        @BindView(R.id.afetr_arrive_ll)
        LinearLayout afetr_arrive_ll;

        @BindView(R.id.item_and_address_tv)
        TextView item_and_address_tv;

        @BindView(R.id.before_arrive_ll)
        LinearLayout before_arrive_ll;

        @BindView(R.id.ivPickUp)
        ImageView ivPickUp;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
