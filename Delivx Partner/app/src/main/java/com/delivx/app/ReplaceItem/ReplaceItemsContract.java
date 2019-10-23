package com.delivx.app.ReplaceItem;

import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.pojo.SearchPojo.Data;
import com.delivx.pojo.SearchPojo.Units;
import com.delivx.pojo.ShipmentDetails;

import org.json.JSONException;

import java.util.ArrayList;

public interface ReplaceItemsContract {

    interface ViewOperations extends BaseView
    {

        void initActionBar();

        void setTitle(String title);

        void setViews(String storeId, ShipmentDetails shipmentDetails);

        void moveToStorePickup(AssignedAppointments appointments);

        void onSuccess(AssignedAppointments appointments);

        void setItemsList(ArrayList<Data> dataArrayList);

        void hideKeyboard();
    }

    interface PresenterOperations
    {
        void setActionBar();

        void setActionBarTitle();

        void getBundleData(Bundle bundle);

        void getItems(String searchItem);

        void setNewQty(String quantity,Data data, Units unit);

        void updateQty() throws JSONException;

        void hideSoftKeyboard();

        String getlanguageCode();
    }
}
