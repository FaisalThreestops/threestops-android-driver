package com.driver.threestops.app.main.help_center.zendeskadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.driver.threestops.app.main.help_center.zendeskpojo.ZendeskDataEvent;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Utility;
import com.driver.Threestops.R;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h>HelpIndexRecyclerAdapter</h>
 * Created by Ali on 12/29/2017.
 */

public class HelpIndexRecyclerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<ZendeskDataEvent> zendexDataEvents;

    FontUtils fontUtils;

    @Inject
    public HelpIndexRecyclerAdapter() {
    }

    public void onHelpIndexRecyclerAdapter(Context mContext, ArrayList<ZendeskDataEvent> zendexDataEvents, FontUtils fontUtils) {
        this.mContext = mContext;
        this.zendexDataEvents = zendexDataEvents;
        this.fontUtils = fontUtils;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.help_index_recycler_content, parent, false);
        return new ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolders holdr = (ViewHolders) holder;

        char c = zendexDataEvents.get(position).getName().charAt(0);
        holdr.tvHelpIndexImageTextPre.setText(c + "");
        holdr.tvHelpIndexCustNamePre.setText(zendexDataEvents.get(position).getName());
        holdr.tvHelpIndexText.setText(zendexDataEvents.get(position).getBody());
        Date date = new Date(zendexDataEvents.get(0).getTimeStamp() * 1000L);
        String dateTime[] = Utility.getFormattedDate(date).split(",");
        holdr.tvHelpIndexDateNTimePre.setText(dateTime[0]);
        holdr.tvHelpIndexTime.setText(dateTime[1]);

    }

    @Override
    public int getItemCount() {
        return zendexDataEvents.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {

        @BindView(R.id.tvHelpIndexImageTextPre)
        TextView tvHelpIndexImageTextPre;
        @BindView(R.id.tvHelpIndexCustNamePre)
        TextView tvHelpIndexCustNamePre;
        @BindView(R.id.tvHelpIndexDateNTimePre)
        TextView tvHelpIndexDateNTimePre;
        @BindView(R.id.tvHelpIndexText)
        TextView tvHelpIndexText;
        @BindView(R.id.tvHelpIndexTime)
        TextView tvHelpIndexTime;

        ViewHolders(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            tvHelpIndexImageTextPre = itemView.findViewById(R.id.tvHelpIndexImageTextPre);
            tvHelpIndexCustNamePre = itemView.findViewById(R.id.tvHelpIndexCustNamePre);
            tvHelpIndexDateNTimePre = itemView.findViewById(R.id.tvHelpIndexDateNTimePre);
            tvHelpIndexText = itemView.findViewById(R.id.tvHelpIndexText);
            tvHelpIndexTime = itemView.findViewById(R.id.tvHelpIndexTime);
            tvHelpIndexImageTextPre.setTypeface(fontUtils.getFont_muli_semi_bold());
        }
    }
}
