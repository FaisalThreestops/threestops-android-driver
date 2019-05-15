package com.delivx.app.main.support.subCategory;

import android.content.Intent;

import com.delivx.pojo.SupportData;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by DELL on 07-03-2018.
 */

public class PresenterSubCat implements SubCatContract.PresenterOperation {

    @Inject SubCatContract.ViewOperation view;
    ArrayList<SupportData> supportDatas = new ArrayList<>();
    String title="";

    @Inject
    public PresenterSubCat() {
    }

    @Override
    public void getBundleData(Intent intent) {

        supportDatas.addAll((Collection<? extends SupportData>) intent.getSerializableExtra("data"));
        title=intent.getStringExtra("title");
        view.setView(supportDatas);
    }

    @Override
    public void initActionBar() {
        view.setActionBar();
        view.setActionBarTitle(title);
    }
}
