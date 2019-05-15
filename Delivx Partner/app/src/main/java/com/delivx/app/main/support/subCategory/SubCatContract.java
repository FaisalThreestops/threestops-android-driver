package com.delivx.app.main.support.subCategory;

import android.content.Intent;

import com.delivx.BaseView;
import com.delivx.pojo.SupportData;

import java.util.ArrayList;

/**
 * Created by DELL on 07-03-2018.
 */

public interface SubCatContract {

    interface ViewOperation extends BaseView{
        void setActionBar();
        void setActionBarTitle(String title);
        void setView(ArrayList<SupportData> supportDatas);
    }
    interface PresenterOperation{
        void getBundleData(Intent intent);
        void initActionBar();
    }
}
