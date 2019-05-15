package com.delivx.app.main.history;

import com.github.mikephil.charting.data.BarEntry;
import com.delivx.BaseView;
import com.delivx.pojo.TripsPojo.Appointments;

import java.util.ArrayList;

/**
 * Created by DELL on 26-02-2018.
 */

public interface HistoryContract
{
    interface ViewOperations extends BaseView
    {
        void onDayInitialized(int differenceDays, ArrayList<String> currentCycleDays, ArrayList<String> postCycleDays);
        void setValues(ArrayList<Appointments> appointments, ArrayList<BarEntry> barEntries, String total, int highestPosition);
        void onFailure(String msg);
        void onFailure(int failure);
        void onFailure();

        void setEmptyHistory();
    }
    interface PresenterOperations
    {
        void attachView(HistoryContract.ViewOperations view);
        void getOrders(String startDate,String endDate, int selectedTabPosition, int tabcount);
    }
}
