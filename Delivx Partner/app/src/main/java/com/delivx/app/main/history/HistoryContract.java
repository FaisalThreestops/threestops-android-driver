package com.delivx.app.main.history;

import com.github.mikephil.charting.data.BarEntry;
import com.delivx.BaseView;
import com.delivx.pojo.TripsPojo.Appointments;

import java.util.ArrayList;


public interface HistoryContract
{
    interface ViewOperations extends BaseView
    {
        /**
         * <h2>onDayInitialized</h2>
         * <p>initialize the dates in toolBar </p>
         * @param differenceDays : difference days( current date difference)
         * @param currentCycleDays : list from sunday to current day
         * @param postCycleDays : all days in a week
         */
        void onDayInitialized(int differenceDays, ArrayList<String> currentCycleDays, ArrayList<String> postCycleDays);

        /**
         * <h2>setValues</h2>
         * <P>set the values to view</P>
         * @param appointments arrylist for appointment
         * @param barEntries arraylist for x and y axis
         * @param total total amount
         * @param highestPosition set highest x and y axis in chart
         */
        void setValues(ArrayList<Appointments> appointments, ArrayList<BarEntry> barEntries, String total, int highestPosition);

        /**
         *<h2>onFailure</h2>
         * <p>error message while calling the API</p>
         * @param msg : error message
         */
        void onFailure(String msg);


        /**
         * <h2>setEmptyHistory</h2>
         * <p>set the empty value to the view if any error in server</p>
         */
        void setEmptyHistory();
    }
    interface PresenterOperations
    {
        /**
         * <h2>attachView</h2>
         * <p>attaching the HistoryFragment view to HistoryPresenter</p>
         * @param view : HistoryFragment view
         */
        void attachView(HistoryContract.ViewOperations view);

        /**
         * <h2>getOrders</h2>
         * <p>API call for set the values</p>
         * @param startDate : start day of the week
         * @param endDate : end day of the week
         * @param selectedTabPosition : tab position
         * @param tabcount : tab count
         */
        void getOrders(String startDate,String endDate, int selectedTabPosition, int tabcount);
    }
}
