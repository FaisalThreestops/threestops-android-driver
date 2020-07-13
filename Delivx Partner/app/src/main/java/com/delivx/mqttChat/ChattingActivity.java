package com.delivx.mqttChat;


import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.driver.Threestops.R;
import com.delivx.utility.FontUtils;

import java.util.ArrayList;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class ChattingActivity extends DaggerAppCompatActivity implements ChattingContract.ViewOperations,View.OnClickListener{

    private ArrayList<ChatData> chatDataArry=new ArrayList<>();
    private ChattingAdapter cAdapter;
    static public boolean isOpen=false;

    @Inject FontUtils appTypeface;
    @BindView(R.id.ivAddFiles) ImageView ivAddFiles ;
    @BindView(R.id.ivBackBtn) ImageView ivBackBtnivBackBtn ;
    @BindView(R.id.toolbarChatting) Toolbar toolbarChatting;
    @BindView(R.id.chatProgress) ProgressBar chatProgress;
    @BindView(R.id.tvchatproname) TextView tvchatproname ;
    @BindView(R.id.tvEventId) TextView tvEventId ;
    @BindView(R.id.tvSend) TextView tvSend ;
    @BindView(R.id.etMsg) EditText etMsg;
    @BindView(R.id.rcvChatMsg) RecyclerView rcvChatMsg;

    @Inject ChattingContract.PresenterOperations presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ButterKnife.bind(this);

        presenter.getData(getIntent());
        presenter.setActionBar();
        presenter.initViews();
    }

    @OnClick({R.id.tvSend,R.id.ivBackBtn})
    @Override
    public void onClick(View view)
    {
        switch (view.getId()){
            //event for send
            case R.id.tvSend:
               presenter.message(etMsg.getText().toString());
               etMsg.setText("");
            break;

            case R.id.ivBackBtn:
               onBackPressed();
            break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        isOpen=true;
        presenter.getChattingData();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOpen=false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOpen=false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * <h2>scrollToBottom</h2>
     * <p>ScrollToBottom when new message were updated</p>
     */
    private void scrollToBottom() {
        rcvChatMsg.scrollToPosition(cAdapter.getItemCount() - 1);
    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        chatProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        chatProgress.setVisibility(View.GONE);
    }

    @Override
    public void setActionBar(String custName) {
        setSupportActionBar(toolbarChatting);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        tvchatproname.setText(custName);
        tvchatproname.setTypeface(appTypeface.titaliumRegular());

    }

    @Override
    public void setViews(String bid) {

        tvchatproname.setTypeface(appTypeface.titaliumSemiBold());
        tvEventId.setTypeface(appTypeface.titaliumRegular());
        etMsg.setTypeface(appTypeface.titaliumRegular());
        tvSend.setTypeface(appTypeface.titaliumSemiBold());
        tvEventId.setText(getResources().getString(R.string.bid)+" "+bid);
        presenter.getChattingData();
    }

    @Override
    public void setRecyclerView()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        cAdapter = new ChattingAdapter(this,chatDataArry);
        rcvChatMsg.setLayoutManager(mLayoutManager);
        int resId = R.anim.layoutanimation_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        rcvChatMsg.setLayoutAnimation(animation);
        rcvChatMsg.setAdapter(cAdapter);
    }

    @Override
    public void updateData(ArrayList<ChatData> chatDataArryList) {
        this.chatDataArry.clear();
        this.chatDataArry.addAll(chatDataArryList);
        cAdapter.notifyDataSetChanged();
        scrollToBottom();
    }
}
