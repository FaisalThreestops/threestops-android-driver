package com.delivx.app.storeDetails;

import android.app.Activity;
import android.os.Bundle;

import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.networking.DispatcherService;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.AppConstants;
import com.delivx.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class StoreDetailsPresenter implements StoreDetailsContract.Presenter
{
    @Inject   Activity context;
    @Inject   StoreDetailsContract.View view;
    @Inject   PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject   DispatcherService dispatcherService;

    private AssignedAppointments appointments;

    @Inject
    StoreDetailsPresenter() {
    }

    @Override
    public void setActionBar()
    {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        if(appointments!=null)
            view.setTitle(context.getResources().getString(R.string.bid)+appointments.getBid());
    }

    @Override
    public void getBundleData(Bundle bundle) {
        if(bundle!=null && bundle.containsKey("data")){
            appointments= (AssignedAppointments) bundle.getSerializable("data");
            view.setViews(appointments);
        }
        if(bundle!=null && bundle.containsKey("from")){
            view.hideViews();
        }
    }

    @Override
    public void callCustomer(boolean isCustomer) {
        if(isCustomer)
            Utility.MakePhoneCall(appointments.getCustomerPhone(), context);
        else if(!"".equals(appointments.getStorePhone()) && appointments.getStorePhone()!=null)
            Utility.MakePhoneCall(appointments.getStorePhone(), context);
    }

    @Override
    public void updateBookingStatus() {
        if(view!=null){
            view.showProgress();
        }
        //onTheWay->10
        Utility.printLog("Appointment Status: "+AppConstants.BookingStatus.OnTheWay);
        Observable<Response<ResponseBody>> bookingStatusRide=dispatcherService.bookingStatusRide(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken(),
                appointments.getBid(),
                AppConstants.BookingStatus.OnTheWay,
                preferenceHelperDataSource.getDriverCurrentLat(),
                preferenceHelperDataSource.getDriverCurrentLongi(),
                null,null,null,null,null,null);

        bookingStatusRide.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {

                        if(view!=null){
                            view.hideProgress();
                        }
                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());
                                setAppointmentStatus(AppConstants.BookingStatus.OnTheWay);
                                view.onSuccess(appointments);


                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());
                                view.onError(jsonObject.getString("message"));
                            }

                            Utility.printLog("bookingStatusRide : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("bookingStatusRide : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null){
                            view.hideProgress();
                        }

                    }

                    @Override
                    public void onComplete() {
                        if(view!=null){
                            view.hideProgress();
                        }
                    }
                });
    }

    @Override
    public void openChat() {
        view.openChatAct(appointments);
    }

    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }


    /**
     * <h1>setAppointmentStatus</h1>
     * <p>Update booking status in shared preference</p>
     * @param status booking status
     */
    private void setAppointmentStatus(String status){
        try {
            JSONArray jsonArray=new JSONArray(preferenceHelperDataSource.getBookings());
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.get("bid").equals(appointments.getBid())) {
                    jsonObject.put("status",status);
                }

            }

            preferenceHelperDataSource.setBookings(jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
