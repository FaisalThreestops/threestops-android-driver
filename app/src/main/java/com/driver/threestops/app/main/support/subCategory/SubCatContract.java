package com.driver.threestops.app.main.support.subCategory;

import android.content.Intent;

import com.driver.threestops.BaseView;
import com.driver.threestops.pojo.SupportData;

import java.util.ArrayList;

public interface SubCatContract {

    interface ViewOperation extends BaseView{

        /**
         * <h2>setActionBar</h2>
         * <p>initializing the back option in the action bar</p>
         */
        void setActionBar();

        /**
         * <h2>setActionBarTitle</h2>
         * <p>set the title to the action bar</p>
         * @param title :activity title
         */
        void setActionBarTitle(String title);

        /**
         * <h2>setView</h2>
         * <p>initializing the recyclerview and set the datas</p>
         * @param supportDatas : supportDatas pojo details
         */
        void setView(ArrayList<SupportData> supportDatas);
    }
    interface PresenterOperation{

        /**
         * <h2>getBundleData</h2>
         * <p>getting the previous data from the SupportRVA activity class</p>
         * @param intent :SupportRVA intent referenece
         */
        void getBundleData(Intent intent);

        /**
         * <h2>initActionBar</h2>
         * <p>initialize the action bar</p>
         */
        void initActionBar();

        /**
         * <h2>getlanguageCode</h2>
         * <p>get the language code</p>
         * @return : language code
         */
        String getlanguageCode();
    }
}
