package com.delivx.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delivx.app.bookingRide.BookingRide;
import com.delivx.app.invoice.InvoiceActivity;
import com.delivx.app.storeDetails.StorePickUpDetails;
import com.delivx.app.storePickUp.StorePickUp;
import com.delivx.utility.AppConstants;
import com.driver.delivx.R;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**************************************************************************************************/

/**
 * Created by vibin on 31/3/17.
 */

public class CurrentUpcomingJobRVA extends RecyclerView.Adapter<CurrentUpcomingJobRVA.ViewHolder> {

    SimpleDateFormat simpleDateFormat;
    private Context context;
    private View view;
    private ArrayList<AssignedAppointments> mData;
    FontUtils fontUtils;
    private  String driverType;

    /**********************************************************************************************/
    public CurrentUpcomingJobRVA(Context context, ArrayList<AssignedAppointments> mData,FontUtils fontUtils,String driverType) {
        this.context = context;
        this.mData = mData;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        this.fontUtils=fontUtils;
        this.driverType=driverType;
    }

    /**********************************************************************************************/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_currentupcoming_job, parent, false);

        return new ViewHolder(view);
    }

    /**********************************************************************************************/
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final int index = holder.getAdapterPosition();
        try {

            holder.tv_job_id.setText(context.getString(R.string.id) + mData.get(position).getBid());
            holder.tv_pickup_loc.setText(mData.get(position).getPickUpAddress());
            if(mData.get(position).getStoreType().equals("7")){
                holder.tv_pickup_loc_header.setText(context.getResources().getText(R.string.pickup));
            }
            else {
                holder.tv_pickup_loc_header.setText(mData.get(position).getStoreName());
            }
            holder.tv_store_type.setText(mData.get(position).getStoreTypeMsg());
            holder.tv_drop_loc.setText(mData.get(position).getDropAddress());
            holder.tv_drop_loc_header.setText(mData.get(position).getCustomerName());
            holder.tv_deliveryfee.setText(mData.get(position).getCurrencySymbol() + " " + Utility.formatPrice(mData.get(position).getDeliveryCharge()));
            holder.tv_date_time.setText(Utility.getDateFormat(Long.parseLong(mData.get(position).getOrderDateTimeStamp())));
            holder.tv_deliverytime.setText(Utility.getDate(Long.parseLong(mData.get(position).getDueDatetimeTimeStamp())));
            holder.tvOrderStatus.setText(mData.get(position).getStatusMessage());
            holder.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.red_light));


            if(mData.get(position).getStoreType().matches("5") &&
                    !mData.get(position).isCominigFromStore()){

                switch (mData.get(position).getOrderStatus()){

                    case AppConstants.BookingStatus.Accept:
                    case AppConstants.BookingStatus.OnTheWay:
                        holder.tv_pickup_loc_header.setVisibility(View.GONE);
                        holder.tv_pickup_loc.setVisibility(View.GONE);
                        break;


                    default:
                        holder.tv_pickup_loc_header.setVisibility(View.GONE);
                        holder.tv_pickup_loc.setVisibility(View.GONE);
                        break;
                }





            }
                /*switch (mData.get(position).getOrderStatus()){
                    case "8":
                        *//*holder.tvOrderStatus.setText(context.getResources().getString(R.string.not_picked));*//*
                        holder.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.app_color_off));
                        *//*holder.llItem.setBackgroundColor(context.getResources().getColor(R.color.red_light));
                        changeTextColor(1,holder);*//*
                        break;
                    case "10":
                        *//*holder.tvOrderStatus.setText(context.getResources().getString(R.string.on_the_way));*//*
                        holder.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.green));
                       *//* holder.llItem.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                        changeTextColor(2,holder);*//*

                        break;
                    case "11":
                        *//*holder.tvOrderStatus.setText(context.getResources().getString(R.string.arrived));*//*
                        holder.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.green));
                        *//*holder.llItem.setBackgroundColor(context.getResources().getColor(R.color.blue));
                        changeTextColor(1,holder);*//*
                        break;
                    case "12":
                        *//*holder.tvOrderStatus.setText(context.getResources().getString(R.string.journey_started));*//*
                        holder.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.green));
                        *//*holder.llItem.setBackgroundColor(context.getResources().getColor(R.color.green));
                        changeTextColor(1,holder);*//*
                        break;
                    case "13":
                        *//*holder.tvOrderStatus.setText(context.getResources().getString(R.string.reached));*//*
                        holder.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.green));
                       *//* holder.llItem.setBackgroundColor(context.getResources().getColor(R.color.green));
                        changeTextColor(1,holder);*//*
                        break;
                    case "14":
                        *//*holder.tvOrderStatus.setText(context.getResources().getString(R.string.delivered));*//*
                        holder.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.green));
                        *//*holder.llItem.setBackgroundColor(context.getResources().getColor(R.color.green));
                        changeTextColor(1,holder);*//*
                        break;

                }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        //recyclerView appoinments
        holder.cv_singlerow_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = null;
                Bundle bundle=new Bundle();
                bundle.putSerializable("data",mData.get(position));

                if(driverType.equals("2")){
                    switch (mData.get(position).getOrderStatus()) {
                        case "8":
                        case "13":
                            intent = new Intent(context, StorePickUp.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            break;
                        case "12":
                            intent = new Intent(context, BookingRide.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            break;
                        case "14":
                            intent = new Intent(context, InvoiceActivity.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            break;
                    }
                }
                else {
                    switch (mData.get(position).getOrderStatus()) {
                        //booking accepted
                        case "8":
                            intent = new Intent(context, StorePickUpDetails.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            break;
                        //booking ride
                        case "10":
                        case "12":
                            intent = new Intent(context, BookingRide.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            break;
                        //store pick up
                        case "11":
                        case "13":
                            intent = new Intent(context, StorePickUp.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            break;
                        //invoice
                        case "14":
                            intent = new Intent(context, InvoiceActivity.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                            break;


                    }
                    Utility.printLog("printing : " + mData.get(index).toString());
                }
            }
        });



    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_job_id) TextView tv_job_id ;
        @BindView(R.id.tv_deliveryfee) TextView tv_deliveryfee ;
        @BindView(R.id.tv_time_left_txt) TextView tv_time_left_txt ;
        @BindView(R.id.tv_deliverytime) TextView tv_deliverytime ;
        @BindView(R.id.tv_pickup_loc) TextView tv_pickup_loc ;
        @BindView(R.id.tv_drop_loc) TextView tv_drop_loc ;
        @BindView(R.id.tv_date_time) TextView tv_date_time ;
        @BindView(R.id.tvOrderStatus) TextView tvOrderStatus ;
        @BindView(R.id.cv_singlerow_job) CardView cv_singlerow_job ;
        @BindView(R.id.tv_pickup_loc_header) TextView tv_pickup_loc_header ;
        @BindView(R.id.tv_drop_loc_header) TextView tv_drop_loc_header ;
        @BindView(R.id.tv_store_type) TextView tv_store_type ;
        @BindView(R.id.llItem) LinearLayout llItem ;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            Typeface ClanaproNarrMedium = fontUtils.titaliumSemiBold();
            Typeface ClanaproNarrNews =fontUtils.titaliumRegular();
            tv_job_id.setTypeface(ClanaproNarrNews);
            tv_deliveryfee.setTypeface(ClanaproNarrMedium);
            tv_time_left_txt.setTypeface(ClanaproNarrNews);
            tv_deliverytime.setTypeface(ClanaproNarrMedium);
            tv_pickup_loc.setTypeface(ClanaproNarrNews);
            tv_drop_loc.setTypeface(ClanaproNarrNews);
            tv_date_time.setTypeface(ClanaproNarrNews);
            tv_pickup_loc_header.setTypeface(ClanaproNarrNews);
            tv_drop_loc_header.setTypeface(ClanaproNarrNews);
            tvOrderStatus.setTypeface(ClanaproNarrMedium);
            tv_store_type.setTypeface(ClanaproNarrMedium);
        }
    }
}
