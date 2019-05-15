package com.delivx.app.main.support;

import com.delivx.BaseView;
import com.delivx.pojo.SupportData;

import java.util.ArrayList;

/**
 * Created by DELL on 05-03-2018.
 */

public interface SupportContract {

    interface ViewOperation extends BaseView{

        void onError(String message);

        void setSupportDetails(ArrayList<SupportData> supportDatas);
    }

    interface PresenterOperation{

        void getSupportLinks();

        void attachView(SupportContract.ViewOperation viewOperation);
    }
}
