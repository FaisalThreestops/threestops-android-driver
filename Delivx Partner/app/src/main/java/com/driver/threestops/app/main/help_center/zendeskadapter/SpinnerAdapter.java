package com.driver.threestops.app.main.help_center.zendeskadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.driver.threestops.app.main.help_center.zendeskpojo.SpinnerRowItem;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Utility;
import com.driver.Threestops.R;

import java.util.ArrayList;

/**
 * <h>SpinnerAdapter</h>
 * Created by Ali on 12/29/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<SpinnerRowItem> {

    private Context mContext;
    FontUtils fontUtils;
    private ArrayList<SpinnerRowItem> spinnerRowItems;

    public SpinnerAdapter(Context mContext, int resouceId, int textviewId, ArrayList<SpinnerRowItem> spinnerRowItems, FontUtils fontUtils) {
        super(mContext, resouceId, textviewId, spinnerRowItems);
        this.mContext = mContext;
        this.spinnerRowItems = spinnerRowItems;
        this.fontUtils = fontUtils;
    }


    @Override
    public int getCount() {
        return spinnerRowItems.size();
    }

    @Override
    public long getItemId(int i) {
        return spinnerRowItems.get(i).getColorId();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);

    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_adapter, parent, false);
            holder = new ViewHolder();
            holder.ivSpinnerPriority = convertView.findViewById(R.id.ivSpinnerPriority);
            holder.tvSpinnerPriority = convertView.findViewById(R.id.tvSpinnerPriority);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        holder.tvSpinnerPriority.setText(spinnerRowItems.get(position).getPriority());
        holder.ivSpinnerPriority.setBackgroundColor(Utility.getColor(mContext, spinnerRowItems.get(position).getColorId()));
        holder.tvSpinnerPriority.setTypeface(fontUtils.getFont_muli_regular());
        return convertView;
    }

    private class ViewHolder {
        private TextView ivSpinnerPriority;
        private TextView tvSpinnerPriority;
    }
}
