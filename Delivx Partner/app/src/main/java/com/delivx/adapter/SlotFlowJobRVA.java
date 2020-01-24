package com.delivx.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.delivx.app.slotAppointments.SlotAppointmentActivity;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.Utility;
import com.driver.delivx.R;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.delivx.utility.VariableConstant.DATA;

public class SlotFlowJobRVA extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    private ArrayList<AssignedAppointments> appointmentsResponse;
    private ArrayList<AssignedAppointments> orderDetails;
    private ArrayList<AssignedAppointments> assignedTrips;
    private ArrayList<AssignedAppointments> passingAssignmentTrips = new ArrayList<>();
    private static final int DATE=0,ORDER=1;
    private int currentMonth;

    public SlotFlowJobRVA(Context context,  ArrayList<AssignedAppointments> orderDetails,ArrayList<AssignedAppointments> assignedTrips ) {
        this.context=context;
        this.appointmentsResponse=assignedTrips;
        this.orderDetails=orderDetails;
        this.assignedTrips=assignedTrips;
        Calendar c = Calendar.getInstance();

        currentMonth = c.get(Calendar.DAY_OF_MONTH);
    }

    @NonNull
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if(viewType==ORDER){
        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(context);
        view = (View) layoutInflater.inflate(R.layout.slot_flow_job, viewGroup, false);
        return new ViewHolder(view);
    } else {
            LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(context);
            view = (View) layoutInflater.inflate(R.layout.group_slots_by_date, viewGroup, false);
            return new DateViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == ORDER) {
            final ViewHolder holder = (ViewHolder) viewHolder;
            holder.tvSlotTimings.setText(Utility.getDate(Long.parseLong(orderDetails.get(position).getSlotStartTime())) + " - " + Utility.getDate(Long.parseLong(orderDetails.get(position).getSlotEndTime())));
            holder.tvSlotTimings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0; i < appointmentsResponse.size(); i++) {
                        if (appointmentsResponse.get(i).getSlotId().equals(orderDetails.get(position).getSlotId())) {
                            passingAssignmentTrips.add(appointmentsResponse.get(i));
                        }
                    }

                    if (passingAssignmentTrips.size() > 0) {
                        Intent intent = new Intent(context, SlotAppointmentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(DATA, passingAssignmentTrips);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "No Orders Available For This Slot.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            passingAssignmentTrips.clear();
        }
        else{
            DateViewHolder holder = (DateViewHolder) viewHolder;
            String month = (orderDetails.get(position).getDueDatetime());
            String monthIndex = Utility.getMonth(month);
            String date = Utility.getMonth(month);
            int daysAgo=currentMonth-Integer.parseInt(date);

            if (currentMonth == Integer.parseInt(monthIndex)) {
                holder.tvDate.setText(context.getResources().getString(R.string.today));
            }
            else if(currentMonth>Integer.parseInt(monthIndex)) {
                holder.tvDate.setText(daysAgo+" "+context.getResources().getString(R.string.days_ago));
            }
            else if(currentMonth+1==Integer.parseInt(monthIndex)) {
                holder.tvDate.setText(context.getResources().getString(R.string.tomorrow));
            }
            else{
                holder.tvDate.setText(Utility.getMonthDay(month));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (orderDetails.get(position).getItemType() == ORDER) {
            return ORDER;
        } else if ((orderDetails.get(position).getItemType() == DATE)) {
            return DATE;
        } else
            return ORDER;
    }

    @Override
    public int getItemCount() {
        int size=orderDetails.size();
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvSlotTimings)
        TextView tvSlotTimings ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    public  class DateViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvDate)
        TextView tvDate;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
