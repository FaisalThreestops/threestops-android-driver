package com.driver.threestops.app.main.support.subCategory;

import android.content.Intent;

import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.pojo.SupportData;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

public class PresenterSubCat implements SubCatContract.PresenterOperation {

    @Inject SubCatContract.ViewOperation view;
    private ArrayList<SupportData> supportDatas = new ArrayList<>();
    String title="";
    @Inject    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject    PresenterSubCat() {
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

    @Override
    public String getlanguageCode() {
        return preferenceHelperDataSource.getLanguageSettings().getLanguageCode() ;
    }
}
