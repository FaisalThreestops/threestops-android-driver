package com.delivx.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delivx.app.main.history.HistoryOrderDetailsNew;
import com.driver.delivx.R;
import com.delivx.pojo.TripsPojo.Appointments;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by embed on 23/5/17.
 */

public class HistoryTripsRVA extends RecyclerView.Adapter<HistoryTripsRVA.ViewHolder> {

    private Context context;
    private View view;
    private ArrayList<Appointments> appointments;
    private FontUtils fontUtils;


    /**********************************************************************************************/
    public HistoryTripsRVA(Context context, ArrayList<Appointments> appointments, FontUtils fontUtils) {
        setHasStableIds(true);
        this.context = context;
        this.appointments = appointments;
        this.fontUtils=fontUtils;
    }

    /**********************************************************************************************/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_currentupcoming_job, parent, false);

        return new HistoryTripsRVA.ViewHolder(view);
    }

    /**********************************************************************************************/
    @Override
    public void onBindViewHolder(HistoryTripsRVA.ViewHolder holder, final int position) {
        final int index = holder.getAdapterPosition();
        holder.setIsRecyclable(false);

        Utility.printLog("HistoryTripsRVA AdapterPosition "+index+"  Bid: "+appointments.get(index));
        holder.cv_singlerow_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HistoryOrderDetailsNew.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("data",appointments.get(index));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.tvOrderStatus.setText("Completed");
        holder.tv_job_id.setText(context.getResources().getString(R.string.id) + " " + appointments.get(position).getOrderId());
        holder.tv_pickup_loc.setText(appointments.get(position).getPickAddress());
        holder.tv_drop_loc.setText(appointments.get(position).getDropAddress());
        holder.tv_deliveryfee.setText(appointments.get(position).getCurrencySymbol() + "" + appointments.get(position).getTotalAmount());

        holder.tv_pickup_loc_header.setText(appointments.get(position).getStoreName());
        holder.tv_drop_loc_header.setText(appointments.get(position).getCustomerName());

        String date = Utility.DateFormatChange(appointments.get(position).getBookingDate());
        holder.tv_date_time.setText(date);


        Utility.printLog("SetTripsTime :" + date);
    }

    /**********************************************************************************************/
    @Override
    public int getItemCount() {
        return appointments.size();
    }

    /**********************************************************************************************/
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_timeleft) LinearLayout ll_timeleft ;
        @BindView(R.id.tv_job_id)TextView tv_job_id ;
        @BindView(R.id.tv_deliveryfee)TextView tv_deliveryfee ;
        @BindView(R.id.tv_time_left_txt)TextView tv_time_left_txt ;
        @BindView(R.id.tv_deliverytime)TextView tv_deliverytime ;
        @BindView(R.id.tv_pickup_loc)TextView tv_pickup_loc ;
        @BindView(R.id.tv_drop_loc)TextView tv_drop_loc ;
        @BindView(R.id.tv_date_time)TextView tv_date_time ;
        @BindView(R.id.tvOrderStatus)TextView tvOrderStatus ;
        @BindView(R.id.cv_singlerow_job)CardView cv_singlerow_job ;
        @BindView(R.id.tv_pickup_loc_header)TextView tv_pickup_loc_header ;
        @BindView(R.id.tv_drop_loc_header)TextView tv_drop_loc_header ;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            ll_timeleft.setVisibility(View.GONE);
            tv_job_id.setTypeface(fontUtils.titaliumRegular());
            tv_deliveryfee.setTypeface(fontUtils.titaliumSemiBold());
            tv_time_left_txt.setTypeface(fontUtils.titaliumRegular());
            tv_deliverytime.setTypeface(fontUtils.titaliumRegular());
            tv_pickup_loc.setTypeface(fontUtils.titaliumRegular());
            tv_drop_loc.setTypeface(fontUtils.titaliumRegular());
            tv_date_time.setTypeface(fontUtils.titaliumRegular());
            tvOrderStatus.setTypeface(fontUtils.titaliumRegular());
            tv_pickup_loc_header.setTypeface(fontUtils.titaliumRegular());
            tv_drop_loc_header.setTypeface(fontUtils.titaliumRegular());

        }
    }
}

