package com.delivx.app.OrderEdit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.delivx.app.MyApplication;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.login.LoginActivity;
import com.delivx.login.language.LanguagesList;
import com.delivx.networking.DispatcherService;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.AssignedTripData;
import com.delivx.pojo.AssignedTripsPojo;
import com.delivx.pojo.ShipmentDetails;
import com.delivx.service.LocationUpdateService;
import com.delivx.utility.AppConstants;
import com.delivx.utility.Utility;
import com.driver.delivx.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class OrderEditPresenter implements OrderEditContract.PresenterOperations {

    @Inject OrderEditContract.ViewOperations view;
    @Inject Activity context;
    @Inject DispatcherService dispatcherService;
    @Inject NetworkService networkService;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    private AssignedAppointments appointments;
    private ShipmentDetails shipmentDetails;
    private ArrayList<ShipmentDetails> updateItemList = new ArrayList<>();
    private boolean isUpdate;

    @Inject
    public OrderEditPresenter() {
    }

    @Override
    public void setActionBar() {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        if(appointments!=null)
            view.setTitle(context.getResources().getString(R.string.edited_item));
    }

    @Override
    public void getBundleData(Bundle bundle) {
        if(bundle!=null && bundle.containsKey("data")){
            appointments= (AssignedAppointments) bundle.getSerializable("data");
            shipmentDetails=(ShipmentDetails) bundle.getSerializable("shipment");
            view.setViews(shipmentDetails,appointments);
        }
    }

    @Override
    public void openReplaceActivity() {
        view.moveToReplaceActivity();
    }

    @Override
    public void callCustomer() {
        Utility.MakePhoneCall(appointments.getCustomerPhone(), context);
    }

    @Override
    public void openChat() {
        view.openChatAct(appointments);
    }

    @Override
    public void cancelAlert() {
        view.setCancelAlert();
    }

    @Override
    public void cancelItem(String statusType,String updateQty) {
        if(appointments.getOrderStatus().equals("11")){
            if(appointments.getShipmentDetails().size()==1) {
                view.toastMessage("unable to cancel");
            }
            else {
                JSONObject oldJsonArray = getOldItems(statusType,updateQty);
                cancelApi(oldJsonArray,statusType);
            }
        }
        else{
            JSONObject oldJsonArray = getOldItems(statusType,updateQty);
            cancelApi(oldJsonArray,statusType);
        }
    }

    private void cancelApi(JSONObject oldJsonArray, final String statusType) {
        view.showProgress();

        Observable<Response<ResponseBody>> updateOrder = networkService.updateQty(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken(),
                oldJsonArray,
                null,
                statusType,
                "fjkfn",
                "cndsnc",
                Double.parseDouble(appointments.getBid()),
                Double.parseDouble("0"),
                Double.parseDouble(preferenceHelperDataSource.getDriverCurrentLat() + ""),
                Double.parseDouble(preferenceHelperDataSource.getDriverCurrentLongi() + ""));

        updateOrder.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if (view != null)
                            view.hideProgress();

                        try {

                            //    JSONObject jsonObject = new JSONObject();
                            Utility.printLog("updated item : " + value.code());

                            if (value.code() == 200) {
                                String res = value.body().string();
                                Utility.printLog("updated item : " + res);
//                                jsonObject=new JSONObject(res);

                                Gson gson = new Gson();

                                AssignedTripsPojo tripsPojo = gson.fromJson(res, AssignedTripsPojo.class);

                                updateItemList = tripsPojo.getData().getItems();
                                updateCancelItem(updateItemList, tripsPojo.getData());

                            } else {

                                //  jsonObject = new JSONObject(value.errorBody().string());

                            }


                            //    Utility.printLog("updateOrderApi : "+jsonObject.toString());

                        } catch (Exception e) {

                            Utility.printLog("updateOrderApi : Catch :" + e.getMessage());

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if (view != null)
                            view.hideProgress();
                    }
                });
    }

    private JSONObject getOldItems(String statusType,String updateQty) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < appointments.getShipmentDetails().size(); i++) {

                jsonObject.put("addedToCartOn", Integer.parseInt(appointments.getShipmentDetails().get(i).getAddedToCartOn()));
                jsonObject.put("childProductId", appointments.getShipmentDetails().get(i).getChildProductId());
                jsonObject.put("unitId", appointments.getShipmentDetails().get(i).getUnitId());
                jsonObject.put("unitPrice", appointments.getShipmentDetails().get(i).getUnitPrice());
                jsonObject.put("appliedDiscount", 0);
                jsonObject.put("finalPrice", Float.parseFloat(appointments.getShipmentDetails().get(i).getFinalPrice()));
                jsonObject.put("oldQuantity", Integer.parseInt(appointments.getShipmentDetails().get(i).getQuantity()));
                jsonObject.put("quantity", Integer.parseInt(appointments.getShipmentDetails().get(i).getQuantity()));

                if (statusType.equals("3") && jsonObject.getString("unitId").equals(shipmentDetails.getUnitId())) {
                    Utility.printLog("UpdateOrder items: " + jsonArray.toString());
                    return jsonObject;
                }

                if(statusType.equals("1")&& jsonObject.getString("unitId").equals(shipmentDetails.getUnitId())){
                    jsonObject.put("quantity",updateQty);
                    return jsonObject;
                }
                jsonArray.put(jsonObject);
            }

            Utility.printLog("UpdateOrder items: " + jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void updateCancelItem(ArrayList<ShipmentDetails> updateList, AssignedTripData assignedTripData) {

        appointments.setShipmentDetails(updateList);
        appointments.setStoreId(assignedTripData.getStoreId());
        appointments.setSubTotalAmount(assignedTripData.getSubTotalAmount());
        appointments.setDeliveryCharge(assignedTripData.getDeliveryCharge());
        appointments.setDiscount(assignedTripData.getDiscount());
        appointments.setTotalAmount(assignedTripData.getTotalAmount());
        for(int i=0;i<appointments.getShipmentDetails().size();i++) {
            appointments.getShipmentDetails().get(i).setMileageMetric(assignedTripData.getMileageMetric());
            appointments.getShipmentDetails().get(i).setCurrencySymbol(assignedTripData.getCurrencySymbol());
            appointments.getShipmentDetails().get(i).setCurrency(assignedTripData.getCurrency());
            appointments.getShipmentDetails().get(i).setCityId(assignedTripData.getCityId());
        }
        view.itemCanceled(appointments);
    }

    @Override
    public void cancelAllItem(ShipmentDetails shipmentDetails, String qty) {
        cancelAllItemAPI();
    }

    private void cancelAllItemAPI() {
        if(view!=null)
            view.showProgress();

        Observable<Response<ResponseBody>> updateOrder=networkService.cancelOrder(
                preferenceHelperDataSource.getToken(),
                preferenceHelperDataSource.getLanguage(),
                "",
                "emfke",
                appointments.getBid(),
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
                            if(value.code()==200|| value.code()==201){
                                String res = value.body().string();
                                Utility.printLog("updated item : "+res);
                                jsonObject=new JSONObject(res);
                                jsonObject = jsonObject.getJSONObject("data");
                                String totalAmount=jsonObject.getString("totalAmount");
                                String sub_total=jsonObject.getString("subTotalAmount");
                                appointments.setTotalAmount(totalAmount);
                                view.moveHomeActivity();

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

    @Override
    public void openPartiallyDialog() {
        view.setReturnQty();
    }

    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();
    }

    @Override
    public void returnCompletelyAlert() {
        view.setReturnCompleteAlert();
    }

}
