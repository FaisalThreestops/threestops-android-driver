package com.driver.threestops.app.main.help_center.zendeskadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.driver.threestops.app.main.help_center.zendeskTicketDetails.HelpIndexTicketDetailsAct;
import com.driver.threestops.app.main.help_center.zendeskpojo.OpenClose;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Utility;
import com.driver.Threestops.R;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h>HelpIndexAdapter</h>
 * Created by Ali on 2/26/2018.
 */

public class HelpIndexAdapter extends RecyclerView.Adapter
{

    private Context mContext;
    private ArrayList<OpenClose> openCloses;
    FontUtils fontUtils;

    private int openSize,closeSize;

    @Inject
    public HelpIndexAdapter()
    {

    }

    public void onCreateIndex(Activity zendeskHelpIndex, ArrayList<OpenClose> openCloses,FontUtils fontUtils)
    {
        mContext = zendeskHelpIndex;
        this.openCloses = openCloses;
        this.fontUtils =fontUtils;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.help_index_adapter,parent,false);
        return new ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ViewHolders hldr  = (ViewHolders) holder;

        if(openCloses.get(position).isFirst())
        {
            hldr.tvOpenCloseStatus.setVisibility(View.VISIBLE);
            String status = mContext.getString(R.string.status)+" : "+openCloses.get(position).getStatus();
            hldr.tvOpenCloseStatus.setText(status);
        }
        else {
            hldr.tvOpenCloseStatus.setVisibility(View.GONE);
        }
        hldr.tvHelpSubject.setText(openCloses.get(position).getSubject());
        Date date = new Date(openCloses.get(position).getTimeStamp() * 1000L);

        String formattedDate[] = Utility.getFormattedDate(date).split(",");
        hldr.tvHelpDate.setText(formattedDate[0]);
        hldr.tvHelpTime.setText(formattedDate[1]);
        char c = openCloses.get(position).getSubject().charAt(0);
        hldr.tvHelpText.setText(c+"");

        if(openSize>0)
        {
            if(position == openSize-1)
                hldr.idView.setVisibility(View.GONE);
        }
        if(position==(getItemCount()-1)){
            hldr.idView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return openCloses.size();
    }

    public void openCloseSize(int openSize, int closeSize)
    {
        this.openSize = openSize;
        this.closeSize = closeSize;
    }

    public class ViewHolders extends RecyclerView.ViewHolder
    {

        @BindView(R.id.idView)View idView;
        @BindView(R.id.tvOpenCloseStatus)TextView tvOpenCloseStatus;
        @BindView(R.id.tvHelpSubject)TextView tvHelpSubject;
        @BindView(R.id.tvHelpTime)TextView tvHelpTime;
        @BindView(R.id.tvHelpDate)TextView tvHelpDate;
        @BindView(R.id.tvHelpText)TextView tvHelpText;
        ViewHolders(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tvOpenCloseStatus.setTypeface(fontUtils.getFont_muli_bold());
            tvHelpSubject.setTypeface(fontUtils.getFont_muli_regular());
            tvHelpTime.setTypeface(fontUtils.getFont_muli_regular());
            tvHelpDate.setTypeface(fontUtils.getFont_muli_regular());
            itemView.setOnClickListener(view -> {

                Intent intent = new Intent(mContext, HelpIndexTicketDetailsAct.class);
                intent.putExtra("ISTOAddTICKET",false);
                intent.putExtra("ZendeskId",openCloses.get(getAdapterPosition()).getId());
                mContext.startActivity(intent);
                Activity activity= (Activity) mContext;
                activity.overridePendingTransition(R.anim.slide_up,R.anim.stay);
            });

        }
    }
}
