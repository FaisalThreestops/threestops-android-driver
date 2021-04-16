package com.driver.threestops.app.replaceItem;

import android.os.Bundle;

import com.driver.threestops.BaseView;
import com.driver.threestops.pojo.AssignedAppointments;
import com.driver.threestops.pojo.SearchPojo.Data;
import com.driver.threestops.pojo.SearchPojo.Units;
import com.driver.threestops.pojo.ShipmentDetails;

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
