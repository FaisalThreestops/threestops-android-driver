package com.delivx.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delivx.app.selectedStore.SelectedStoreIdActivity;
import com.delivx.app.invoice.InvoiceActivity;
import com.delivx.app.storeDetails.StorePickUpDetails;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.AppConstants;
import com.delivx.utility.Utility;
import com.driver.Threestops.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Set_the_order_list_rva extends RecyclerView.Adapter<Set_the_order_list_rva.ViewHolder> {
    Activity context;
    ArrayList<AssignedAppointments> sameStoreBookingID;
    int count=0;

    public Set_the_order_list_rva(ArrayList<AssignedAppointments> sameStoreBookingID, Activity context) {
        this.context = context;
        this.sameStoreBookingID = sameStoreBookingID;
        errorMessage= (ErrorMessage) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(context);
        View view = (View) layoutInflater.inflate(R.layout.set_order_list_rva, viewGroup, false);
        return new Set_the_order_list_rva.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.order_id_tv.setText(sameStoreBookingID.get(i).getBid());
        viewHolder.tv_currency.setText(sameStoreBookingID.get(i).getCurrencySymbol() + " " + sameStoreBookingID.get(i).getTotalAmount());
        viewHolder.customer_name.setText(sameStoreBookingID.get(i).getCustomerName());
        viewHolder.customer_address_tv.setText(sameStoreBookingID.get(i).getDropAddress());
        viewHolder.customer_delivery_time_tv.setText(Utility.getDateFormat(Long.parseLong(sameStoreBookingID.get(i).getDueDatetimeTimeStamp())));
        viewHolder.delivery_time_tv.setText(Utility.getDate(Long.parseLong(sameStoreBookingID.get(i).getOrderDateTimeStamp())));
        if(Integer.parseInt(sameStoreBookingID.get(i).getOrderStatus())>=Integer.parseInt(AppConstants.BookingStatus.JourneyStarted))
        {
            viewHolder.tvPicked.setVisibility(View.VISIBLE);
            count++;
        }
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.printLog("updated item : " + "sameStoreBookingID.get(i).getOrderStatus()" + sameStoreBookingID.get(i).getOrderStatus() + "po" + i);
                switch (sameStoreBookingID.get(i).getOrderStatus()) {

                    case "8":
                        Intent intent = new Intent(context, StorePickUpDetails.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order_view", sameStoreBookingID.get(i));
                        bundle.putString("view", "hide_seek_bar");
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        break;

                    case "12":
                        if(sameStoreBookingID.size()==count) {
//                            Intent intent2 = new Intent(context, BookingRide.class);
//                            Bundle bundle2 = new Bundle();
//                            bundle2.putSerializable("data", sameStoreBookingID.get(i));
//                            bundle2.putInt("index",i);
//                            intent2.putExtras(bundle2);
//                            context.startActivity(intent2);
                            SelectedStoreIdActivity selectedStoreIdActivity = (SelectedStoreIdActivity) context;
                            selectedStoreIdActivity.MoveToBookingRide(sameStoreBookingID.get(i), i);
                        }
                        else{
                            errorMessage.errorToast("some items are not picked");
                        }
                        break;

                    case "14":
                        Intent intent3 = new Intent(context, InvoiceActivity.class);
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("data", sameStoreBookingID.get(i));
                        intent3.putExtras(bundle3);
                        context.startActivity(intent3);
                        break;

                    case "11":
                    case "13":

                        Log.d("exe", "index" + i);
                        SelectedStoreIdActivity selectedStoreIdActivity = (SelectedStoreIdActivity) context;
                        selectedStoreIdActivity.startActivity(sameStoreBookingID.get(i), i);
                        break;

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return sameStoreBookingID.size();
    }

    public interface ErrorMessage{
        public void errorToast(String error);
    }

    ErrorMessage errorMessage;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_id_tv)
        TextView order_id_tv;

        @BindView(R.id.tv_currency)
        TextView tv_currency;

        @BindView(R.id.customer_name)
        TextView customer_name;

        @BindView(R.id.customer_address_tv)
        TextView customer_address_tv;

        @BindView(R.id.parent_ll)
        LinearLayout linearLayout;

        @BindView(R.id.customer_delivery_time_tv)
        TextView customer_delivery_time_tv;

        @BindView(R.id.delivery_time_tv)
        TextView delivery_time_tv;

        @BindView(R.id.tvPicked)
        TextView tvPicked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
