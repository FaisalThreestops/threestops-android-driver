package com.delivx.app.storePickUp;

import android.app.Activity;
import android.os.Bundle;

import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.networking.DispatcherService;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.ShipmentDetails;
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

public class PickUpPresenter implements PickUpContract.PresenterOperations {

    @Inject PickUpContract.ViewOperations view;
    @Inject Activity context;
    @Inject DispatcherService dispatcherService;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    private AssignedAppointments appointments;

    @Inject
    PickUpPresenter() {
    }


    @Override
    public void setActionBar() {
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
    }

    @Override
    public void callCustomer() {
        Utility.MakePhoneCall(appointments.getCustomerPhone(), context);
    }

    @Override
    public void updateBookingStatus() {
        if(view!=null){
            view.showProgress();
        }
        final String status;
        //if reaches the destination
        if(appointments.getOrderStatus().equals(AppConstants.BookingStatus.ReachedAtLocation))
            status=AppConstants.BookingStatus.Completed;
        else
            //after pick up the item from store
            status=AppConstants.BookingStatus.JourneyStarted;

        Utility.printLog("Appointment Status: "+status);
        Observable<Response<ResponseBody>> bookingStatusRide=dispatcherService.bookingStatusRide(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken(),
                appointments.getBid(),
                status,
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
                                appointments.setOrderStatus(status);
                                setAppointmentStatus(status);
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
    public void editOrder(ShipmentDetails shipmentDetails) {
        view.openOrderEditDialog(shipmentDetails,appointments);
    }

    @Override
    public void updateOrder( ShipmentDetails shipmentDetails,  String qty) {

        JSONArray jsonArray=getItems(shipmentDetails,qty);
        orderApi(jsonArray,shipmentDetails,qty,false);
    }

    @Override
    public void deleteItem(ShipmentDetails shipmentDetails) {

        appointments.getShipmentDetails().remove(shipmentDetails);

        JSONArray jsonArray=getItems(shipmentDetails,shipmentDetails.getQuantity());
        orderApi(jsonArray,shipmentDetails,shipmentDetails.getQuantity(),true);
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
     * <h1>orderApi</h1>
     * <p>API call for edit the shipment item</p>
     * @param jsonArray json format data included booking details
     * @param shipmentDetails booking details pojo
     * @param qty edited quantity
     * @param isDelete   boolean value for check edit or delete
     */
    private void orderApi(final JSONArray jsonArray, final ShipmentDetails shipmentDetails,
                          final String qty, final boolean isDelete){
        if(view!=null)
            view.showProgress();

        Observable<Response<ResponseBody>> updateOrder=networkService.updateOrder(
                preferenceHelperDataSource.getToken(),
                preferenceHelperDataSource.getLanguage(),
                jsonArray.toString(),
                null,
                appointments.getBid(),
                appointments.getDeliveryCharge(),
                preferenceHelperDataSource.getDriverCurrentLat()+"",
                preferenceHelperDataSource.getDriverCurrentLongi()+""
        );
        updateOrder.observeOn(AndroidSchedulers.mainThread())
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
                            JSONObject jsonObject;
                            if(value.code()==200){
                                String res = value.body().string();
                                Utility.printLog("updated item : "+res);
                                jsonObject=new JSONObject(res);
                                jsonObject = jsonObject.getJSONObject("data");
                                String totalAmount=jsonObject.getString("totalAmount");
                                String sub_total=jsonObject.getString("subTotalAmount");

                                appointments.setTotalAmount(totalAmount);
                                appointments.setSubTotalAmount(sub_total);
                                if(isDelete)
                                    view.setViews(appointments);
                                else
                                    upadteItemDetails(shipmentDetails,qty);

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());

                            }

                            Utility.printLog("updateOrderApi : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("updateOrderApi : Catch :"+e.getMessage());
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
     * <h1>upadteItemDetails</h1>
     * <p>add the quantity and set view for edited</p>
     * @param shipmentDetails booking details pojo
     * @param qty edited quantity
     */
    private void upadteItemDetails(ShipmentDetails shipmentDetails, String qty) {

        for(int i=0;i<appointments.getShipmentDetails().size();i++){
            if(shipmentDetails.getUnitId().equals(appointments.getShipmentDetails().get(i).getUnitId())){
                appointments.getShipmentDetails().get(i).setQuantity(qty);
            }
        }
        view.setViews(appointments);
    }

    /**
     * <h1>getItems</h1>
     * @param shipmentDetails booking details pojo
     * @param qty edited quantity
     * @return jsonarray for edit
     */
    private JSONArray getItems(ShipmentDetails shipmentDetails, String qty){
        JSONArray jsonArray=new JSONArray();
        try{
            for(int i=0;i<appointments.getShipmentDetails().size();i++)
            {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("unitId",appointments.getShipmentDetails().get(i).getUnitId());
                jsonObject.put("itemName",appointments.getShipmentDetails().get(i).getItemName());
                jsonObject.put("quantity",Integer.parseInt(appointments.getShipmentDetails().get(i).getQuantity()));
                jsonObject.put("childProductId",appointments.getShipmentDetails().get(i).getChildProductId());
                jsonObject.put("itemImageURL",appointments.getShipmentDetails().get(i).getItemImageURL());
                jsonObject.put("unitPrice",Float.parseFloat(appointments.getShipmentDetails().get(i).getUnitPrice()));
                jsonObject.put("unitName",appointments.getShipmentDetails().get(i).getUnitName());
                jsonObject.put("parentProductId",appointments.getShipmentDetails().get(i).getParentProductId());
                jsonObject.put("addedToCartOn",Integer.parseInt(appointments.getShipmentDetails().get(i).getAddedToCartOn()));
                jsonObject.put("finalPrice",Integer.parseInt(appointments.getShipmentDetails().get(i).getFinalPrice()));

                if(jsonObject.getString("unitId").equals(shipmentDetails.getUnitId())){
                    jsonObject.put("quantity",qty);
                }
                jsonArray.put(jsonObject);
            }

            Utility.printLog("UpdateOrder items: "+jsonArray.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray;
    }


    /**
     * <h1>setAppointmentStatus</h1>
     * <p>change booking status after  update </p>
     * @param status updated booking status
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
