package com.delivx.vehiclelist;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.driver.Threestops.R;
import com.delivx.pojo.SigninDriverVehicle;
import com.delivx.utility.SessionManager;
import com.delivx.utility.VariableConstant;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**************************************************************************************************/

/**
 * Created by vibin baby on 10/4/17.
 */

public class VechicleListRVA extends RecyclerView.Adapter {


    ArrayList<View> views;
    ArrayList<RadioButton> buttons;
    ArrayList<SigninDriverVehicle> vDataList;
    private Context context;
    private View view;


    /**********************************************************************************************/
    @Inject
    public VechicleListRVA(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<SigninDriverVehicle> data){
        vDataList=data;
    }

    /**********************************************************************************************/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_select_vechicle, parent, false);
        return new VehicleViewHolder(view);
    }

    /**********************************************************************************************/
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VehicleViewHolder) {

            VehicleViewHolder vehicle = (VehicleViewHolder) holder;
            vehicle.tv_plateno_value.setText(vDataList.get(position).getPlatNo());
            vehicle.tv_type_value.setText(vDataList.get(position).getVehicleType());
            vehicle.tv_model_value.setText(vDataList.get(position).getVehicleModel());
            /*buttons.add(holder.rb_selectveh);
            views.add(holder.view_green);*/
            final int index = holder.getAdapterPosition();

            if (vDataList.get(position).isSelected()) {
                vehicle.rb_selectveh.setChecked(true);
                vehicle.view_green.setBackgroundResource(R.drawable.single_sel_veh_leftgreen);

            } else {
                vehicle.rb_selectveh.setChecked(false);
                vehicle.view_green.setBackgroundResource(R.drawable.single_sel_veh_leftgray);
            }


            vehicle.ll_vechicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onVehicleSelect(position);
                }
            });
            vehicle.rb_selectveh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onVehicleSelect(position);
                }
            });
        }

    }
    public void onVehicleSelect(int index){
        resetAll();
        vDataList.get(index).setSelected(true);
        SessionManager.getSessionManager(context).setTypeId(vDataList.get(index).getTypeId());
        VechicleListRVA.this.notifyDataSetChanged();
        VariableConstant.VECHICLESELECTED = true;
        VariableConstant.VEHICLEID = vDataList.get(index).getId();
        VariableConstant.VEHICLE_TYPE_ID = vDataList.get(index).getTypeId();
    }

    private void resetAll() {
        for (int i = 0; i < vDataList.size(); i++) {
            vDataList.get(i).setSelected(false);

        }
    }

    /* *********************************************************************************************/

    /**********************************************************************************************/
    @Override
    public int getItemCount() {
        return vDataList.size();
    }

    /**********************************************************************************************/
    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_plateno_vl) TextView tv_plateno_vl;
        @BindView(R.id.tv_type_vl) TextView tv_type_vl;
        @BindView(R.id.tv_model_vl) TextView tv_model_vl;
        @BindView(R.id.tv_plateno_value) TextView tv_plateno_value;
        @BindView(R.id.tv_type_value) TextView tv_type_value;
        @BindView(R.id.tv_model_value) TextView tv_model_value;
        @BindView(R.id.rb_selectveh) RadioButton rb_selectveh;
        @BindView(R.id.view_green) View view_green;
        @BindView(R.id.ll_vechicle) LinearLayout ll_vechicle;

        public VehicleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            Typeface ClanaproNarrMedium = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrMedium.otf");
            Typeface ClanaproNarrNews = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrNews.otf");

            tv_plateno_vl.setTypeface(ClanaproNarrNews);
            tv_type_vl.setTypeface(ClanaproNarrNews);
            tv_model_vl.setTypeface(ClanaproNarrNews);
            tv_plateno_value.setTypeface(ClanaproNarrMedium);
            tv_type_value.setTypeface(ClanaproNarrMedium);
            tv_model_value.setTypeface(ClanaproNarrMedium);

        }

    }
}
