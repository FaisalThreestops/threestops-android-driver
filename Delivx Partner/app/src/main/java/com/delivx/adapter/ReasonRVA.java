package com.delivx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.delivx.app.storeDetails.StorePickUpDetails;
import com.delivx.app.storePickUp.StorePickUp;
import com.delivx.pojo.Cancel.CancelData;
import com.delivx.pojo.SearchPojo.Data;
import com.delivx.utility.SessionManager;
import com.delivx.utility.VariableConstant;
import com.delivx.vehiclelist.VechicleListRVA;
import com.driver.delivx.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReasonRVA extends RecyclerView.Adapter<ReasonRVA.ViewHolder> {
    Context context;
    CancelData data;
    int activity;

    public ReasonRVA(Context context, CancelData data,int activity) {
        this.context = context;
        this.data = data;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.cancel_details_raw,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.reasonTv.setText(data.getReasons().get(i).getReasons());

        if(data.getReasons().get(i).isSelected()){
            viewHolder.radioBtn.setChecked(true);
            viewHolder.radioBtn.setBackgroundResource(R.drawable.ic_check_icon_on);
        }
        else {
            viewHolder.radioBtn.setChecked(false);
            viewHolder.radioBtn.setBackgroundResource(R.drawable.ic_check_off);
        }

        viewHolder.radioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVehicleSelect(i);
                if(activity==1) {
                    StorePickUpDetails storePickUpDetails = (StorePickUpDetails) context;
                    storePickUpDetails.showDialog();
                }
                else{
                    StorePickUp storePickUp = (StorePickUp) context;
                    storePickUp.showDialog();
                }
            }
        });
    }

    public void onVehicleSelect(int index){
        resetAll();
        data.getReasons().get(index).setSelected(true);
        ReasonRVA.this.notifyDataSetChanged();
    }

    private void resetAll() {
        for (int i = 0; i < data.getReasons().size(); i++) {
            data.getReasons().get(i).setSelected(false);

        }
    }

    @Override
    public int getItemCount() {
        return data.getReasons().size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reasonTv) TextView reasonTv;
        @BindView(R.id.radioBtn)
        RadioButton radioBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
