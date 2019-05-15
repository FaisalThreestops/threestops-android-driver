package com.delivx.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driver.delivx.R;
import com.delivx.pojo.Cities;
import com.delivx.pojo.VehMakeData;
import com.delivx.pojo.VehTypeData;
import com.delivx.pojo.VehTypeSepecialities;
import com.delivx.pojo.VehicleMakeModel;
import com.delivx.pojo.Zone;
import com.delivx.signup.GenericListActivity;

import java.util.ArrayList;

/**
 * Created by Admin on 8/1/2017.
 */

public class GenericListAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private final Typeface ClanaproNarrMedium,ClanaproNarrNews;
    private ArrayList data;
    private Activity context;
    private String type;

    private ArrayList<Cities> signupZonedata;
    private ArrayList<VehTypeData> vehTypeDatas;
    private ArrayList<VehTypeSepecialities> vehTypeSepecialities;
    private ArrayList<VehMakeData> vehMakeDatas;
    private ArrayList<VehicleMakeModel> vehicleMakeModels;
    private ArrayList<Zone> zoneList;
    private int lastCheckedPosition = 0;


    public GenericListAdapter(Activity context, ArrayList data, String type) {
        this.context = context;
        this.data = data;
        this.type = type;

        ClanaproNarrMedium = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrMedium.otf");
        ClanaproNarrNews = Typeface.createFromAsset(context.getAssets(), "fonts/TITILLIUMWEB-REGULAR.TTF");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewsd = LayoutInflater.from(context).inflate(R.layout.single_select_operator, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(viewsd);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final MyViewHolder viewHolder = (MyViewHolder) holder;

            switch (type) {
                case "ZONE":
                    signupZonedata = data;
                    viewHolder.operator.setText(signupZonedata.get(position).getCityName());
                    if (signupZonedata.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;
                case "ZONE1":
                    zoneList = data;
                    viewHolder.operator.setText(zoneList.get(position).getTitle());
                    if (zoneList.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case "VEHICLE_TYPE":

                    vehTypeDatas = data;
                    viewHolder.operator.setText(vehTypeDatas.get(position).getType_name());
                    if (vehTypeDatas.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case "VEHICLE_SPECIALITIES":

                    vehTypeSepecialities = data;
                    viewHolder.operator.setText(vehTypeSepecialities.get(position).getName());
                    if (vehTypeSepecialities.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case "VEHICLE_MAKE":

                    vehMakeDatas = data;
                    viewHolder.operator.setText(vehMakeDatas.get(position).getName());
                    if (vehMakeDatas.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case "VEHICLE_MODEL":

                    vehicleMakeModels = data;
                    viewHolder.operator.setText(vehicleMakeModels.get(position).getName());
                    if (vehicleMakeModels.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;
            }


            /*if(position == lastCheckedPosition  ) {
                viewHolder.operator.setChecked(true);
                setView(position, true);
            }else*/ {

                viewHolder.operator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setView(viewHolder.getAdapterPosition(),false);
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        switch (type) {
            case "ZONE":
                ((GenericListActivity) context).sendResult(signupZonedata, type);

                break;

            case "ZONE1":
                ((GenericListActivity) context).sendResult(zoneList, type);
                break;

            case "VEHICLE_TYPE":
                ((GenericListActivity) context).sendResult(vehTypeDatas, type);

                break;

            case "VEHICLE_SPECIALITIES":
                ((GenericListActivity) context).sendResult(vehTypeSepecialities, type);

                break;

            case "VEHICLE_MAKE":
                ((GenericListActivity) context).sendResult(vehMakeDatas, type);

                break;

            case "VEHICLE_MODEL":
                ((GenericListActivity) context).sendResult(vehicleMakeModels, type);
                break;
        }

    }

    public void setView(int position, boolean isSelected) {
        switch (type) {
            case "ZONE":
                if (signupZonedata.get(position).isSelected()/*!isSelected*/) {
                    signupZonedata.get(position).setSelected(false);

                } else {
                    signupZonedata.get(position).setSelected(true);
                    removeOtherSelectedViews(signupZonedata, position);
                }
                break;

            case "ZONE1":
                if (zoneList.get(position).isSelected()/*!isSelected*/) {
                    zoneList.get(position).setSelected(false);

                } else {
                    zoneList.get(position).setSelected(true);
                    removeOtherSelectedViews(zoneList, position);
                }
                break;
            case "VEHICLE_TYPE":
                Log.d("mura", "setView: " + vehTypeDatas.get(position).isSelected());
                if (vehTypeDatas.get(position).isSelected()) {
                    vehTypeDatas.get(position).setSelected(false);
                } else {

                    vehTypeDatas.get(position).setSelected(true);
                    removeOtherSelectedViews(vehTypeDatas, position);
                }

                break;

            case "VEHICLE_SPECIALITIES":
                if (vehTypeSepecialities.get(position).isSelected()) {
                    vehTypeSepecialities.get(position).setSelected(false);
                } else {
                    vehTypeSepecialities.get(position).setSelected(true);
                }
                break;

            case "VEHICLE_MAKE":
                if (vehMakeDatas.get(position).isSelected()) {
                    vehMakeDatas.get(position).setSelected(false);
                } else {
                    vehMakeDatas.get(position).setSelected(true);
                    removeOtherSelectedViews(vehMakeDatas, position);
                }

                break;

            case "VEHICLE_MODEL":
                if (vehicleMakeModels.get(position).isSelected()) {
                    vehicleMakeModels.get(position).setSelected(false);
                } else {
                    vehicleMakeModels.get(position).setSelected(true);
                    removeOtherSelectedViews(vehicleMakeModels, position);
                }
                break;
        }
    }

    private void removeOtherSelectedViews(ArrayList list, int selected) {
        for (int i = 0; i < list.size(); i++) {
            if (i != selected) {
                Object o = list.get(i);

                if (o instanceof VehTypeData) {
                    ((VehTypeData) o).setSelected(false);
                }
                if (o instanceof VehMakeData) {
                    ((VehMakeData) o).setSelected(false);
                }
                if (o instanceof VehicleMakeModel) {
                    ((VehicleMakeModel) o).setSelected(false);
                }
                if (o instanceof Cities) {
                    ((Cities) o).setSelected(false);
                }
            }
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatRadioButton operator;

        public MyViewHolder(View itemView) {
            super(itemView);
            operator = itemView.findViewById(R.id.rb_single_operator);
            operator.setTypeface(ClanaproNarrNews);


           /* operator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastCheckedPosition = getAdapterPosition();

                    notifyDataSetChanged();

                }
            });*/

        }
    }
}
