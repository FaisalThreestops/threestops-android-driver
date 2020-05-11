package com.delivx.app.main.help_center.zendeskTicketDetails;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delivx.app.main.help_center.zendeskadapter.HelpIndexRecyclerAdapter;
import com.delivx.app.main.help_center.zendeskadapter.SpinnerAdapter;
import com.delivx.app.main.help_center.zendeskpojo.SpinnerRowItem;
import com.delivx.app.main.help_center.zendeskpojo.ZendeskDataEvent;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;
import com.driver.delivx.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h>HelpIndexTicketDetailsAct</h>
 * Created by Ali on 2/26/2018.
 */

public class HelpIndexTicketDetailsAct extends DaggerAppCompatActivity implements HelpIndexContract.HelpView {
    public static final Integer[] priorityColor = {R.color.green_continue,
            R.color.livemblue3498, R.color.red_login_dark,
            R.color.saffron,};
    //private String[] priorityTitles;
    @Inject
    FontUtils appTypeface;
    @Inject
    PreferenceHelperDataSource sharedPrefs;
    @Inject
    HelpIndexContract.presenter presenter;
    @Inject
    HelpIndexRecyclerAdapter helpIndexRecyclerAdapter;

    @BindView(R.id.tool_helpindex_ticket)
    Toolbar toolBarLayout;
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindArray(R.array.ticketPriority)
    String[] priorityTitles;
    @BindView(R.id.etHelpIndexSubjectPre)
    EditText etHelpIndexSubjectPre;
    @BindView(R.id.etWriteMsg)
    EditText etWriteMsg;
    @BindView(R.id.etHelpIndexSubject)
    EditText etHelpIndexSubject;
    @BindView(R.id.tvHelpIndexDateNTimePre)
    TextView tvHelpIndexDateNTimePre;
    @BindView(R.id.spinnerHelpIndexPre)
    TextView spinnerHelpIndexPre;
    @BindView(R.id.tvHelpIndexImageText)
    TextView tvHelpIndexImageText;
    @BindView(R.id.tvHelpIndexCustName)
    TextView tvHelpIndexCustName;
    @BindView(R.id.tvHelpIndexDateNTime)
    TextView tvHelpIndexDateNTime;
    @BindView(R.id.ivHelpIndexImage)
    ImageView ivHelpIndexImage;
    @BindView(R.id.cardHelpIndexTicket)
    CardView cardHelpIndexTicket;
    @BindView(R.id.ivHelpCenterPriority)
    ImageView ivHelpCenterPriority;
    @BindView(R.id.spinnerHelpIndex)
    Spinner spinnerHelpIndex;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.cardHelpIndexTicketPre)
    CardView cardHelpIndexTicketPre;
    @BindView(R.id.tvHelpIndexImageTextPre)
    TextView tvHelpIndexImageTextPre;
    @BindView(R.id.ivHelpIndexImagePre)
    ImageView ivHelpIndexImagePre;
    @BindView(R.id.ivHelpCenterPriorityPre)
    ImageView ivHelpCenterPriorityPre;
    @BindView(R.id.tvHelpIndexCustNamePre)
    TextView tvHelpIndexCustNamePre;
    @BindView(R.id.tvHelpIndexSend)
    TextView tvHelpIndexSend;
    @BindView(R.id.rlTextInput)
    RelativeLayout rlTextInput;


    private String subject, priority;
    private int zenId;
    private ArrayList<SpinnerRowItem> rowItems;
    private boolean isToAddTicket;
    private ArrayList<ZendeskDataEvent> zendeskDataEvents = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_help_index_ticket);
        ButterKnife.bind(this);
        Utility.RtlConversion(HelpIndexTicketDetailsAct.this, sharedPrefs.getLanguage());

        isToAddTicket = getIntent().getBooleanExtra("ISTOAddTICKET", false);
        if (getIntent().getExtras() != null) {
            zenId = getIntent().getIntExtra("ZendeskId", 0);
        }
        if (getIntent().getExtras() != null) {
            String str = getIntent().getStringExtra("COMINGFROM");
            Log.i("StringValue", "onCreate: " + str);
            if (str != null && str.equals("FragmentHelp")) {
                etHelpIndexSubject.setText(getIntent().getStringExtra("SUBJECT"));
            }
            //
        }
        initializeArrayList();
        initializeToolBar();
        initializeView();

    }

    private void initializeArrayList() {
        rowItems = new ArrayList<>();
        rowItems.add(new SpinnerRowItem(priorityColor[0], priorityTitles[0]));
        rowItems.add(new SpinnerRowItem(priorityColor[1], priorityTitles[1]));
        rowItems.add(new SpinnerRowItem(priorityColor[2], priorityTitles[2]));
        rowItems.add(new SpinnerRowItem(priorityColor[3], priorityTitles[3]));
    }

    @SuppressLint("SetTextI18n")
    private void initializeView() {
        if (isToAddTicket) {
            etHelpIndexSubject.setTypeface(appTypeface.getFont_muli_regular());
            tvHelpIndexDateNTime.setTypeface(appTypeface.getFont_muli_regular());
            tvHelpIndexCustName.setTypeface(appTypeface.getFont_muli_regular());
            tvHelpIndexImageText.setTypeface(appTypeface.getFont_muli_regular());
            priority = rowItems.get(0).getPriority();
            SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_adapter, R.id.tvSpinnerPriority, rowItems, appTypeface);
            spinnerHelpIndex.setAdapter(adapter);

            spinnerHelpIndex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("TAG", "onItemSelected: " + i);
                    priority = rowItems.get(i).getPriority();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            cardHelpIndexTicket.setVisibility(View.VISIBLE);
            String name = sharedPrefs.getMyName();//+" "+sharedPrefs.getLastName();
//            String name = "Harsha";
            tvHelpIndexCustName.setText(name);
            char c = 0;
            if(name!=null && name.length()>0)
                c = name.charAt(0);
            tvHelpIndexImageText.setText(c + "");
            Date date = new Date(System.currentTimeMillis());
            String dateTime[] = Utility.getFormattedDate(date).split(",");
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            String timeToSet = dateTime[0] + " | " + dateFormat.format(cal.getTime());
            tvHelpIndexDateNTime.setText(timeToSet);

        } else {
            etHelpIndexSubjectPre.setEnabled(false);
            spinnerHelpIndexPre.setTypeface(appTypeface.getFont_muli_regular());
            tvHelpIndexCustNamePre.setTypeface(appTypeface.getFont_muli_regular());
            tvHelpIndexImageTextPre.setTypeface(appTypeface.getFont_muli_regular());
            etHelpIndexSubjectPre.setTypeface(appTypeface.getFont_muli_regular());
            tvHelpIndexDateNTimePre.setTypeface(appTypeface.getFont_muli_regular());
            cardHelpIndexTicketPre.setVisibility(View.VISIBLE);
            String name = sharedPrefs.getMyName();//+" "+sharedPrefs.getLastName();
//            String name = "Harsha";
            tvHelpIndexCustNamePre.setText(name);
            char c = name.charAt(0);
            tvHelpIndexImageTextPre.setText(c + "");
            showLoading();
            presenter.callApiToGetTicketInfo(zenId);
        }

        helpIndexRecyclerAdapter.onHelpIndexRecyclerAdapter(this, zendeskDataEvents, appTypeface);
        RecyclerView recyclerViewHelpIndex = findViewById(R.id.recyclerViewHelpIndex);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewHelpIndex.setLayoutManager(linearLayoutManager);
        recyclerViewHelpIndex.setAdapter(helpIndexRecyclerAdapter);
        etWriteMsg = findViewById(R.id.etWriteMsg);

        etWriteMsg.setTypeface(appTypeface.getFont_muli_regular());
        tvHelpIndexSend.setTypeface(appTypeface.getFont_muli_regular());
        recyclerViewHelpIndex.setNestedScrollingEnabled(false);

    }

    @OnClick(R.id.tvHelpIndexSend)
    public void msgSendText() {
        String trim = etWriteMsg.getText().toString().trim();
        if (!trim.isEmpty()) {
            if (isToAddTicket) {
                subject = etHelpIndexSubject.getText().toString().trim();
                if (!subject.isEmpty()) {
                    presenter.callApiToCreateTicket(trim, subject, priority);
                    setAndNotifyAdapter(sharedPrefs.getMyName(), trim);
//                    setAndNotifyAdapter("Harsha", trim);
                    etHelpIndexSubject.setEnabled(false);
                    isToAddTicket = false;
                    Utility.hideSoftKeyboard(HelpIndexTicketDetailsAct.this);
                } else
                    Toast.makeText(HelpIndexTicketDetailsAct.this, "Please add subject", Toast.LENGTH_SHORT).show();
                etWriteMsg.setText("");

            } else {
                presenter.callApiToCommentOnTicket(trim, zenId);
                etWriteMsg.setText("");
                setAndNotifyAdapter(sharedPrefs.getMyName(), trim);
//                setAndNotifyAdapter("Harsha", trim);
                Utility.hideSoftKeyboard(HelpIndexTicketDetailsAct.this);
            }

        }
    }


    private void setAndNotifyAdapter(String name, String trim) {
        long timeStsmp = System.currentTimeMillis() / 1000;
        ZendeskDataEvent dataEvent = new ZendeskDataEvent();
        dataEvent.setBody(trim);
        dataEvent.setName(name);
        dataEvent.setTimeStamp(timeStsmp);
        zendeskDataEvents.add(dataEvent);
        helpIndexRecyclerAdapter.notifyDataSetChanged();
    }


    /*
    initialize toolBar
     */
    private void initializeToolBar() {
        setSupportActionBar(toolBarLayout);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tv_center.setTypeface(appTypeface.getFont_muli_semi_bold());
        toolBarLayout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (isToAddTicket)
            tv_center.setText(R.string.newTicket);
        else
            tv_center.setText(R.string.ticket);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //      overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
        overridePendingTransition(R.anim.stay, R.anim.slide_down);

    }


    @Override
    public void onZendeskTicketAdded(String msg) {
        onBackPressed();
    }


    @Override
    public void onError(String errMsg) {
        hideLoading();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(this.getString(R.string.alert));
        alertDialog.setMessage(errMsg);
        alertDialog.setNegativeButton(R.string.ok,
                (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    @Override
    public void onTicketInfoSuccess(ArrayList<ZendeskDataEvent> events, String timeToSet, String subject, String priority, String type) {


        zendeskDataEvents.addAll(events);
        helpIndexRecyclerAdapter.notifyDataSetChanged();
        etHelpIndexSubjectPre.setText(subject);
        tvHelpIndexDateNTimePre.setText(timeToSet);
        this.subject = subject;
        this.priority = priority;
        spinnerHelpIndexPre.setText(priority);
        presenter.onPriorityImage(this, priority, ivHelpCenterPriorityPre);
        if (!"open".equalsIgnoreCase(type)) {
            rlTextInput.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void stopAct() {

    }
}
