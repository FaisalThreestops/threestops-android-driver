package com.delivx.app.main.history;

import android.util.Log;

import com.delivx.pojo.TripsPojo.Appointments;
import com.google.gson.Gson;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.github.mikephil.charting.data.BarEntry;
import com.delivx.pojo.TripsPojo.TripsPojo;
import com.delivx.utility.Utility;

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


    @Inject
    NetworkService networkService;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    HistoryContract.ViewOperations view;

    @Inject
    public HistoryPresenter() {


    }

    public void attachView(HistoryContract.ViewOperations view){
        this.view=view;
        init();
    }

    void init(){
        XAxisFormat = new SimpleDateFormat("EEE", Locale.US);
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
                preferenceHelperDataSource.getToken(),
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
                            TripsPojo tripsPojo;
                            if(value.code()==200){
                                String response=value.body().string();
                                Utility.printLog("orderApi : "+response);
                                Gson gson=new Gson();
                                tripsPojo=gson.fromJson(response,TripsPojo.class);
                                if(tripsPojo.getData()!=null)
                                    handleDate(tripsPojo, selectedTabPosition, tabcount);
                            }
                            else {
                                view.setEmptyHistory();
                                barEntries = new ArrayList<>();
                                ArrayList<Appointments> appointments = new ArrayList<>();
                                view.setValues(appointments,barEntries,"0",0);
                                String err = value.errorBody().string();
                                Utility.printLog("  HistoryRes err : "+err);
                                JSONObject jsonObject = new JSONObject(err);
                                view.onFailure(jsonObject.getString("message"));




                            }
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


    private void initDays() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String currentDay = XAxisFormat.format(c.getTime()).toUpperCase();
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
            Log.d(TAG, "currentCycleDays: " + currenCycleDays.get(i));
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
