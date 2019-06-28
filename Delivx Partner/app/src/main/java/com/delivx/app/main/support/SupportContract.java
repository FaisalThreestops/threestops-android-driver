package com.delivx.app.main.support;

import com.delivx.BaseView;
import com.delivx.pojo.SupportData;

import java.util.ArrayList;


public interface SupportContract {

    interface ViewOperation extends BaseView{

        /**
         * <h2>onError</h2>
         * <p>error message while calling API</p>
         * @param message : server api error message
         */
        void onError(String message);

        /**
         * <h2>setSupportDetails</h2>
         * <p>set the retrieval data to recycler view</p>
         * @param supportDatas :supportDatas pojo details
         */
        void setSupportDetails(ArrayList<SupportData> supportDatas);
    }

    interface PresenterOperation{

        /**
         * <h2>getSupportLinks</h2>
         * <p>calling the API for get the support links</p>
         */
        void getSupportLinks();

        /**
         * <h2>attachView</h2>
         * <p>attach the supportFrag view to supportFragPresenter</p>
         * @param viewOperation : supportFrag view
         */
        void attachView(SupportContract.ViewOperation viewOperation);
    }
}
