package com.driver.threestops.app.main.help_center.zendeskHelpIndex;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.driver.threestops.app.main.help_center.zendeskTicketDetails.HelpIndexTicketDetailsAct;
import com.driver.threestops.app.main.help_center.zendeskadapter.HelpIndexAdapter;
import com.driver.threestops.app.main.help_center.zendeskpojo.OpenClose;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Utility;
import com.driver.Threestops.R;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class ZendeskHelpIndexAct extends DaggerAppCompatActivity implements ZendeskHelpIndexContract.ZendeskView {

    @Inject
    FontUtils fontUtils;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    HelpIndexAdapter helpIndexAdapter;
    @Inject
    ZendeskHelpIndexContract.Presenter presenter;

    @BindView(R.id.tool_helpIndex)
    Toolbar toolHelpIndex;
    @BindView(R.id.rlHelpIndex)
    RelativeLayout rlHelpIndex;
    @BindView(R.id.recyclerHelpIndex)
    RecyclerView recyclerHelpIndex;
    @BindView(R.id.progressbarHelpIndex)
    ProgressBar progressbarHelpIndex;

    private ArrayList<OpenClose> openCloses = new ArrayList<>();
    private int closeSize, openSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_index);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        ButterKnife.bind(this);
        Utility.RtlConversion(ZendeskHelpIndexAct.this, preferenceHelperDataSource.getLanguage());

        initializeToolBar();
        initializeView();
    }

    private void initializeView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerHelpIndex.setLayoutManager(layoutManager);
        helpIndexAdapter.onCreateIndex(this, openCloses, fontUtils);
        recyclerHelpIndex.setAdapter(helpIndexAdapter);
        showLoading();
        presenter.onToGetZendeskTicket();
    }

    /*
    initialize toolBar
     */
    private void initializeToolBar() {

        setSupportActionBar(toolHelpIndex);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tvHelpCenter = findViewById(R.id.tv_center);
        ImageView tvAddNewTicket = findViewById(R.id.ivFilter);

        getSupportActionBar().setTitle("");
        tvHelpCenter.setText(R.string.helpcenter);
        tvAddNewTicket.setVisibility(View.VISIBLE);
        tvAddNewTicket.setImageDrawable(getResources().getDrawable(R.drawable.add));
        tvHelpCenter.setTypeface(fontUtils.getFont_muli_semi_bold());
        toolHelpIndex.setNavigationOnClickListener(v -> onBackPressed());

        tvAddNewTicket.setOnClickListener(view -> {
            Intent intent = new Intent(ZendeskHelpIndexAct.this, HelpIndexTicketDetailsAct.class);
            intent.putExtra("ISTOAddTICKET", true);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.stay);
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toolHelpIndex.setVisibility(View.INVISIBLE);
        finish();
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_right);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onError(String error) {


    }


    @Override
    public void onGetTicketSuccess() {

    }

    @Override
    public void onEmptyTicket() {
        rlHelpIndex.setVisibility(View.VISIBLE);
        recyclerHelpIndex.setVisibility(View.GONE);
    }

    @Override
    public void onTicketStatus(OpenClose openClose, int openCloseSize, boolean isOpenClose) {
        if (isOpenClose)
            openSize = openCloseSize;
        else
            closeSize = openCloseSize;

        helpIndexAdapter.openCloseSize(openSize, closeSize);
        openCloses.add(openClose);
    }

    @Override
    public void onNotifyData() {
        rlHelpIndex.setVisibility(View.GONE);
        recyclerHelpIndex.setVisibility(View.VISIBLE);
        helpIndexAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        progressbarHelpIndex.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressbarHelpIndex.setVisibility(View.GONE);
    }

    @Override
    public void stopAct() {

    }
}
