package com.driver.threestops.app.main.history;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.threestops.adapter.HistoryTripsRVA;
import com.driver.Threestops.R;
import com.driver.threestops.pojo.TripsPojo.Appointments;
import com.driver.threestops.utility.FontUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;


public class HistoryFragment extends DaggerFragment implements TabLayout.OnTabSelectedListener ,HistoryContract.ViewOperations {

    private static final String TAG = "HistoryFragment";

    int tabcount = 12;
    int tabIncrement = 5;
    HistoryTripsRVA historyTripsRVA;
    private ProgressDialog pDialog;

    private SimpleDateFormat tabDateFormat,apiDateFormat;
    private String selectedWeeks;
    private XAxis xAxis;

    private  int differenceDays = 0;
    private int selectedTabPosition = 0 ;
    private ArrayList<Date> apiStartWeek=new ArrayList<>();
    private ArrayList<Appointments> appointments;
    private ArrayList<String> currentCycleDays=new ArrayList<>(),pastCycleDays=new ArrayList<>();

    @BindView(R.id.rv_job_home) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.tv_amount_earned) TextView tv_amount_earned;
    @BindView(R.id.mChart) BarChart mChart;
    @BindView(R.id.tv_earned_value)TextView tv_earned_value;
    @BindView(R.id.tabLayout) TabLayout tabLayout;

    @Inject   FontUtils fontUtils;
    @Inject   HistoryContract.PresenterOperations presenter;
    @Inject   public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this,rootView);
        initViews();
        presenter.attachView(this);

        return rootView;
    }

    /**
     * <h2>initViews</h2>
     * <p>initializing the Views,setting the date format and set the RecyclerView</p>
     */
    private void initViews()
    {

        pDialog= new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);

        tabDateFormat = new SimpleDateFormat("MMM dd", Locale.US);
        apiDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);


        appointments = new ArrayList<>();
        historyTripsRVA = new HistoryTripsRVA(getActivity(),appointments,fontUtils);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(historyTripsRVA);

        tv_amount_earned.setTypeface(fontUtils.titaliumSemiBold());
        tv_earned_value.setTypeface(fontUtils.titaliumRegular());

    }


    @Override
    public void onDayInitialized(int differenceDays, ArrayList<String> currentCycleDays, ArrayList<String> postCycleDays) {
        this.differenceDays = differenceDays;
        this.currentCycleDays.addAll(currentCycleDays);
        this.pastCycleDays.addAll(postCycleDays);
        initTabLayout(tabcount);
        initBarChart();
    }

    /**
     * <h2>initTabLayout</h2>
     * <P>initializing the tab layout</P>
     * @param selectableTab : tab count
     */
    private void initTabLayout(final int selectableTab)
    {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        tabLayout.removeOnTabSelectedListener(this);
        tabLayout.removeAllTabs();
        apiStartWeek.clear();

        for(int i = 0; i <= tabcount ;i++)
        {
            Log.i("check", "initTabLayout: for i "+i);
            tabLayout.addTab(tabLayout.newTab());
        }

        for(int i=tabcount ; i >= 0 ; i--)
        {
            String startDate = "";
            String endDate = "";
            if(i==tabcount)
            {
                endDate = tabDateFormat.format(c.getTime());
                c.add(Calendar.DATE , -differenceDays);
                startDate = tabDateFormat.format(c.getTime());
                apiStartWeek.add(c.getTime());
                c.add(Calendar.DATE , -1);

                if(differenceDays != 0)
                {
                    tabLayout.getTabAt(i).setText(startDate + "-" + endDate);
                }
                else
                {
                    tabLayout.getTabAt(i).setText(startDate);
                }
            }
            else
            {
                endDate = tabDateFormat.format(c.getTime());
                Log.i("check", "initTabLayout: endate "+endDate);
                c.add(Calendar.DATE , -6);
                startDate = tabDateFormat.format(c.getTime());
                apiStartWeek.add(c.getTime());
                Log.i("check", "initTabLayout: apiStartWeek "+apiStartWeek);
                c.add(Calendar.DATE , -1);
                tabLayout.getTabAt(i).setText(startDate + "-" + endDate);
                Log.i(TAG, "initTabLayout: startDate - endDate" +startDate + "-" + endDate);
            }
        }

        new Handler().postDelayed(
                new Runnable(){
                    @Override
                    public void run() {
                        tabLayout.getTabAt(selectableTab).select();
                    }
                }, 100);

        tabLayout.addOnTabSelectedListener(this);

        selectedWeeks = tabLayout.getTabAt(tabcount).getText().toString();

    }

    /**
     * <h2>initBarChart</h2>
     * <p>initializing the bar chart</p>
     */
    private void initBarChart()
    {
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(true);// scaling can now only be done on x- and y-axis separately
        mChart.setDrawGridBackground(false);// scaling can now only be done on x- and y-axis separately
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.setScaleEnabled(false);
        mChart.setDoubleTapToZoomEnabled(false);


        xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        selectedTabPosition = tab.getPosition();

        if(selectedTabPosition == 0)
        {
            tabcount += tabIncrement;
            Log.d(TAG, "onTabSelected: "+tabcount);
            initTabLayout(tabIncrement);
            return;
        }

        if(selectedTabPosition == tabcount)
        {
            int count = differenceDays +1;
            Log.i(TAG, "onTabSelected: if "+count);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(currentCycleDays));
            xAxis.setLabelCount(count);
            mChart.notifyDataSetChanged();
        }
        else
        {
            Log.i(TAG, "onTabSelected: else ");
            xAxis.setValueFormatter(new IndexAxisValueFormatter(pastCycleDays));
            xAxis.setLabelCount(7);
            mChart.notifyDataSetChanged();
        }

        selectedWeeks = tabLayout.getTabAt(selectedTabPosition).getText().toString();

        int position = tabcount - selectedTabPosition ;
        String apiSelectedDate = apiDateFormat.format(apiStartWeek.get(position));
        Date endDate = apiStartWeek.get(position);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE,5);
        String apiSelectedEndDate = apiDateFormat.format(calendar.getTime());

        Log.d(TAG, "onTabSelected: after minus tabPosition "+apiSelectedDate);

        presenter.getOrders(apiSelectedDate,apiSelectedEndDate,selectedTabPosition,tabcount);
    }



    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEmptyHistory() {
        tv_earned_value.setText("0");

    }


    @Override
    public void setValues(ArrayList<Appointments> appointments, ArrayList<BarEntry> barEntries, String total, int highestPosition) {
        this.appointments.clear();
        this.appointments.addAll(appointments);
        historyTripsRVA.notifyDataSetChanged();

        if(appointments.size()>0)
            tv_earned_value.setText(total);


        BarDataSet set1;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0)
        {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(barEntries);
            set1.setLabel("The Week "+ selectedWeeks);
            set1.setHighLightColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
            Highlight highlight = new Highlight(highestPosition,0, 0);
            mChart.highlightValue(highlight, false);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        }
        else
        {
            set1 = new BarDataSet(barEntries, "The Week "+ selectedWeeks);
            set1.setDrawIcons(false);
            set1.setColors(ContextCompat.getColor(getActivity(),R.color.colorPrimaryLight));
            set1.setHighLightColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.8f);
            data.setHighlightEnabled(true);

            mChart.setData(data);

            Highlight highlight = new Highlight(highestPosition,0, 0);
            mChart.highlightValue(highlight, false);
        }

        mChart.invalidate();

        mChart.animateXY(1000,500);

    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
