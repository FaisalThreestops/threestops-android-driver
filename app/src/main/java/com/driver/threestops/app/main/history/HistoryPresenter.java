package com.driver.threestops.app.main.history;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.driver.threestops.app.MyApplication;
import com.driver.threestops.login.LoginActivity;
import com.driver.threestops.login.language.LanguagesList;
import com.driver.threestops.pojo.TripsPojo.Appointments;
import com.driver.threestops.service.LocationUpdateService;
import com.driver.threestops.utility.AppConstants;
import com.google.gson.Gson;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.networking.NetworkService;
import com.github.mikephil.charting.data.BarEntry;
import com.driver.threestops.pojo.TripsPojo.TripsPojo;
import com.driver.threestops.utility.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;



public class HistoryPresenter implements HistoryContract.PresenterOperations {

    private String TAG=HistoryPresenter.class.getSimpleName();
    private SimpleDateFormat XAxisFormat;
    private int differenceDays = 0;
    private ArrayList<Float> totalsForBar;
    private ArrayList<BarEntry> barEntries;
    @Inject   NetworkService networkService;
    @Inject   PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    Activity context;

    HistoryContract.ViewOperations view;

    @Inject
    HistoryPresenter() {
    }
    @Override
    public void attachView(HistoryContract.ViewOperations view){
        this.view=view;
        init();
    }

    /**
     * <h2>init</h2>
     * <p>set the date format</p>
     */
    private void init(){
        XAxisFormat = new SimpleDateFormat("EEE", Locale.US);
        Log.i("check", "init: "+XAxisFormat.toString());
        totalsForBar = new ArrayList<>();
        barEntries = new ArrayList<>();
        initDays();
    }



    @Override
    public void getOrders(String startDate,String endDate, final int selectedTabPosition, final int tabcount)
    {

        if(view!=null)
            view.showProgress();

        Utility.printLog("  HistoryRes req : startDate : "+startDate+"\nenddate : "+endDate+"\n");

        Observable<Response<ResponseBody>> order=networkService.order(
                preferenceHelperDataSource.getLanguage(),
                ((MyApplication) context.getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()),
                0,startDate,endDate);
        order.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            Utility.printLog("HistoryRes"+value.code());
                            String response=null;
                            TripsPojo tripsPojo;
                            switch (value.code()){
                                case 200:
                                     response=value.body().string();
                                    Utility.printLog("orderApi : "+response);
                                    Gson gson=new Gson();
                                    tripsPojo=gson.fromJson(response,TripsPojo.class);
                                    if(tripsPojo.getData()!=null)
                                        handleDate(tripsPojo, selectedTabPosition, tabcount);
                                    break;

                                case 440:
                                case 498:
                                    Utility.printLog("pushTopics shared pref "+preferenceHelperDataSource.getPushTopic());
                                    Utility.subscribeOrUnsubscribeTopics(new JSONArray(preferenceHelperDataSource.getPushTopic()),false);
                                    LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
                                    preferenceHelperDataSource.clearSharedPredf();
                                    preferenceHelperDataSource.setLanguageSettings(languagesList);
                                    ((MyApplication)context.getApplicationContext()).disconnectMqtt();
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                    if(Utility.isMyServiceRunning(LocationUpdateService.class, context))
                                    {
                                        Intent stopIntent = new Intent(context, LocationUpdateService.class);
                                        stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                                        context.startService(stopIntent);
                                    }
                                    break;
                                    default:
                                        view.setEmptyHistory();
                                        barEntries = new ArrayList<>();
                                        ArrayList<Appointments> appointments = new ArrayList<>();
                                        view.setValues(appointments,barEntries,"0",0);
                                        String err = value.errorBody().string();
                                        Utility.printLog("  HistoryRes err : "+err);
                                        JSONObject jsonObject = new JSONObject(err);
                                        view.onFailure(jsonObject.getString("message"));
                                        break;
                            }
                            Utility.printLog("bookingStatusRide : "+response);
                        }catch (Exception e)
                        {
                            Utility.printLog("orderApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }


    /**
     * <h2>initDays</h2>
     * <p>calculating the difference days between current day to previous stored date</p>
     */
    private void initDays() {
        Log.i("check", "initDays: ");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String currentDay = XAxisFormat.format(c.getTime()).toUpperCase();
        Log.i("check", "initDays: currentDay "+currentDay);
        differenceDays = 0;
        switch (currentDay) {
            case "SUN":
                differenceDays = 0;
                break;
            case "MUN":
                differenceDays = 1;
                break;
            case "TUE":
                differenceDays = 2;
                break;
            case "WED":
                differenceDays = 3;
                break;
            case "THU":
                Log.i("check", "initDays: tuesday");
                differenceDays = 4;
                break;
            case "FRI":
                differenceDays = 5;
                break;
            case "SAT":
                differenceDays = 6;
                break;
        }

        ArrayList<String> currenCycleDays = new ArrayList<>();
        c.add(Calendar.DATE, -differenceDays);
        for (int i = 0; i <= differenceDays; i++) {
            currenCycleDays.add(XAxisFormat.format(c.getTime()).toUpperCase());
            c.add(Calendar.DATE, +1);
            Log.d("check", "currentCycleDays: " + currenCycleDays.get(i));
        }

        ArrayList<String> pastCycleDays = new ArrayList<>();
        pastCycleDays.add("SUN");
        pastCycleDays.add("MON");
        pastCycleDays.add("TUE");
        pastCycleDays.add("WED");
        pastCycleDays.add("THR");
        pastCycleDays.add("FRI");
        pastCycleDays.add("SAT");

        view.onDayInitialized(differenceDays,currenCycleDays,pastCycleDays);
    }

    /**
     * <h2>handleDate</h2>
     * <p>handling the data to set the values on the position</p>
     * @param tripsPojo : tripsPojo is handling the data
     * @param selectedTabPosition : tab position
     * @param tabcount :tab count
     */
    private void handleDate(TripsPojo tripsPojo, int selectedTabPosition, int tabcount) {
        double amountEarned = 0;
        for (int i = 0; i < tripsPojo.getData().getTotalEarnings().size(); i++) {
            amountEarned += Double.parseDouble(tripsPojo.getData().getTotalEarnings().get(i).getAmt());
        }

        totalsForBar.clear();
        for (int i = 0; i < tripsPojo.getData().getTotalEarnings().size(); i++) {
            String amt = tripsPojo.getData().getTotalEarnings().get(i).getAmt();
            if (amt != null && !amt.isEmpty()) {
                if (!amt.equals("NaN")) {
                    totalsForBar.add(Float.parseFloat(amt));
                } else {
                    totalsForBar.add(0.0f);
                }
            } else {
                totalsForBar.add(0.0f);
            }
        }

        barEntries.clear();
        float highestvalue = 0;
        int highestPosition = 0;

        if (selectedTabPosition == tabcount) {
            for (int i = 0; i <= differenceDays; i++) {
                barEntries.add(new BarEntry(i, totalsForBar.get(i)));

                if (totalsForBar.get(i) > highestvalue) {
                    highestPosition = i;
                    highestvalue = totalsForBar.get(i);
                }
            }
        } else {
            for (int i = 0; i < 7; i++) {
                barEntries.add(new BarEntry(i, totalsForBar.get(i)));

                if (totalsForBar.get(i) > highestvalue) {
                    highestPosition = i;
                    highestvalue = totalsForBar.get(i);
                }
            }
        }

        view.setValues(tripsPojo.getData().getOrders(),barEntries,String.valueOf((int)amountEarned),highestPosition);
    }

}
