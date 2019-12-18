package com.delivx.app.replaceItem;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.DispatcherService;
import com.delivx.networking.NetworkService;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.AssignedTripData;
import com.delivx.pojo.AssignedTripsPojo;
import com.delivx.pojo.SearchPojo.Data;
import com.delivx.pojo.SearchPojo.SearchPojoItems;
import com.delivx.pojo.SearchPojo.Units;
import com.delivx.pojo.ShipmentDetails;
import com.delivx.utility.Utility;
import com.driver.delivx.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.couchbase.lite.Database.TAG;

public class ReplaceItemsPresenter implements ReplaceItemsContract.PresenterOperations {


    @Inject
    ReplaceItemsContract.ViewOperations view;
    @Inject
    Activity context;
    @Inject
    DispatcherService dispatcherService;
    @Inject
    NetworkService networkService;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    private String storeId;
    Gson gson = new Gson();
    private String newQty;
    private ArrayList<Data> dataArrayList;
    private Units units;
    private ShipmentDetails shipmentDetails;
    private String bID;
    private Data data;
    private String deliveryCharge;
    private AssignedAppointments appointments;
    private ArrayList<ShipmentDetails> updateItemList = new ArrayList<>();
    ShipmentDetails newShipment = new ShipmentDetails();
    private AssignedTripData assignedTripData;

    @Inject
    public ReplaceItemsPresenter() {
    }


    @Override
    public void setActionBar() {
        view.initActionBar();
    }

    @Override
    public void setActionBarTitle() {
        if (storeId != null)
            view.setTitle(context.getResources().getString(R.string.replace_item));
    }

    @Override
    public void getBundleData(Bundle bundle) {
        if (bundle != null) {
            storeId = bundle.getString("StoreID");
            bID = bundle.getString("BID");
            deliveryCharge = bundle.getString("deliveryCharge");
            shipmentDetails = (ShipmentDetails) bundle.getSerializable("shipmentDetails");
            appointments = (AssignedAppointments) bundle.getSerializable("appointment");
            newShipment = shipmentDetails;
            view.setViews(storeId, shipmentDetails);
        }
    }

    @Override
    public void getItems(String searchItem) {

        if (view != null) {
            view.showProgress();
        }
        final Observable<Response<ResponseBody>> request = networkService.getSearchItems(
                preferenceHelperDataSource.getToken(),
                preferenceHelperDataSource.getLanguage(), storeId, "0", "20", searchItem);

        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if (view != null) {
                            view.hideProgress();
                        }
                        switch (value.code()) {
                            case 200:
                                String response = null;
                                try {
                                    response = value.body().string();
                                    if (response != null) {
                                        JSONObject jsonObject = new JSONObject(response);
                                        SearchPojoItems searchPojoItems = gson.fromJson(jsonObject.toString(), SearchPojoItems.class);
                                        handleResponse(searchPojoItems.getData());
                                        Utility.printLog(TAG + " getWalletTrans onNext: " + jsonObject.toString());
                                        view.setItemsList(searchPojoItems.getData());
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                break;
                            default:
                                break;
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        if (view != null) {
                            view.hideProgress();
                        }
                        Utility.printLog(TAG + "getItems error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) {
                            view.hideProgress();
                        }
                    }
                });

    }

    private void handleResponse(ArrayList<Data> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public void setNewQty(String quantity, Data data, Units units) {
        this.newQty = quantity;
        this.units = units;
        this.data = data;

    }

    @Override
    public void updateQty() throws JSONException {

        JSONObject newJsonArray = getItems(units, newQty);
        JSONObject oldJsonArray = getOldItems();
        Log.d("exe", "newQty" + newQty + "units" + units);
        orderApi(newJsonArray, oldJsonArray, newQty);

    }

    private void orderApi(final JSONObject newJsonArray, JSONObject oldJsonArray, final String newQty) throws JSONException {

        view.showProgress();

        Log.d("exe", "newJsonArray" + newJsonArray);
        Log.d("exe", "oldJsonArray" + oldJsonArray);
        Observable<Response<ResponseBody>> updateOrder = networkService.updateQty(
                preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken(),
                oldJsonArray,
                newJsonArray,
                "2",
                "fjkfn",
                "cndsnc",
                Double.parseDouble(bID),
                Double.parseDouble(deliveryCharge),
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
                                assignedTripData=tripsPojo.getData();
                                updateItemList = tripsPojo.getData().getItems();


                                upadteItemDetails(updateItemList, tripsPojo.getData().getStoreId(),tripsPojo.getData());

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

    private void upadteItemDetails(ArrayList<ShipmentDetails> updateList, String storeId, AssignedTripData totals) {


        for(int i=0;i<updateList.size();i++){

            Log.d("exe", "updated item" + updateList.get(i).getItemName()+ "addCardID" +  updateList.get(i).getAddedToCartOn());

        }

        appointments.setShipmentDetails(updateList);
        appointments.setStoreId(totals.getStoreId());
        appointments.setSubTotalAmount(totals.getSubTotalAmount());
        appointments.setDeliveryCharge(totals.getDeliveryCharge());
        appointments.setDiscount(totals.getDiscount());
        appointments.setTotalAmount(totals.getTotalAmount());
        for(int i=0;i<appointments.getShipmentDetails().size();i++) {
            appointments.getShipmentDetails().get(i).setMileageMetric(assignedTripData.getMileageMetric());
            appointments.getShipmentDetails().get(i).setCurrencySymbol(assignedTripData.getCurrencySymbol());
            appointments.getShipmentDetails().get(i).setCurrency(assignedTripData.getCurrency());
            appointments.getShipmentDetails().get(i).setCityId(assignedTripData.getCityId());
        }
        view.moveToStorePickup(appointments);



    }

    @Override
    public void hideSoftKeyboard() {

        view.hideKeyboard();

    }


    @Override
    public String getlanguageCode() {

        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode();

    }

    private JSONObject getOldItems() {
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

                if (jsonObject.getString("unitId").equals(shipmentDetails.getUnitId())) {
                    return jsonObject;
                }

            }
            Utility.printLog("UpdateOrder items: " + jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject getItems(Units units, String qty) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < data.getUnits().size(); i++) {
                jsonObject.put("addedToCartOn", 0);
                jsonObject.put("childProductId", data.getParentProductId());
                jsonObject.put("unitId", data.getUnits().get(i).getUnitId());
                jsonObject.put("unitPrice", Float.parseFloat(data.getUnits().get(i).getFloatValue()));
                jsonObject.put("appliedDiscount", 0);
                jsonObject.put("finalPrice", Float.parseFloat(data.getUnits().get(i).getFloatValue()));
                jsonObject.put("oldQuantity", 0);
                if (jsonObject.getString("unitId").equals(units.getUnitId())) {
                    jsonObject.put("quantity", qty);
                    return jsonObject;
                }
            }

            Utility.printLog("UpdateOrder items: " + jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
