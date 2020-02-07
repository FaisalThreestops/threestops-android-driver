package com.delivx.app.main.support;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.delivx.adapter.SupportRVA;
import com.driver.delivx.R;
import com.delivx.pojo.SupportData;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;


public class SupportFrag extends DaggerFragment implements SupportContract.ViewOperation {

    private ArrayList<SupportData> supportDatas=new ArrayList<>();
    private SupportRVA supportRVA;

    @BindView(R.id.rvSupport)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    SupportContract.PresenterOperation presenter;
    @Inject
    public SupportFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.support_fragment, container, false);

        ButterKnife.bind(this,rootView);
        initLayout();

        presenter.attachView(this);
        presenter.getSupportLinks();
        return rootView;
    }

    /**
     * <h2>initLayout</h2>
     * <p>initializing the recyclerView for supportView</p>
     */
    public void initLayout() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        supportRVA = new SupportRVA(getActivity(), supportDatas, true);
        recyclerView.setAdapter(supportRVA);
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setSupportDetails(ArrayList<SupportData> supportDatas) {
        this.supportDatas.addAll(supportDatas);
        supportRVA.notifyDataSetChanged();
    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
