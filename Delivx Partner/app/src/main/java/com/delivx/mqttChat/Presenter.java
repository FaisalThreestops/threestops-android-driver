package com.delivx.mqttChat;

import android.content.Intent;

import com.google.gson.Gson;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.MessageService;
import com.delivx.utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by DELL on 29-03-2018.
 */

public class Presenter implements ChattingContract.PresenterOperations {

    @Inject
    public Presenter() {
    }

    @Inject MessageService messageService;

    @Inject ChattingContract.ViewOperations view;

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    private String bid,custName,custId;

    private Observer<JSONObject> observer;

    private String pageNo="0";

    private ArrayList<ChatData> chatDataArry=new ArrayList<>();

    @Override
    public void getData(Intent intent) {
        bid = intent.getStringExtra("BID");
        custName = intent.getStringExtra("CUST_NAME");
        custId= intent.getStringExtra("CUST_ID");
    }

    @Override
    public void setActionBar() {
        view.setActionBar(custName);
    }

    @Override
    public void initViews() {
        view.setViews(bid);
        view.setRecyclerView();
        initializeRxJava();
    }

    @Override
    public void getChattingData() {

        view.showProgress();
        final Observable<Response<ResponseBody>> chatHistory=messageService.chatHistory(preferenceHelperDataSource.getLanguage(),
                preferenceHelperDataSource.getToken(),
                bid,
                pageNo);
        chatHistory.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200)
                            {
                                jsonObject=new JSONObject(value.body().string());

                                ChatHistoryResponse  chatHistoryResponse = new Gson().fromJson(jsonObject.toString(), ChatHistoryResponse.class);
                                if(chatHistoryResponse.getData()!=null && chatHistoryResponse.getData().size()>0){
                                    chatDataArry.clear();
                                    for(int i=0;i<chatHistoryResponse.getData().size();i++){
                                        if(chatHistoryResponse.getData().get(i).getFromID().equals(preferenceHelperDataSource.getDriverID())){
                                            chatHistoryResponse.getData().get(i).setCustProType(1);
                                        }else {
                                            chatHistoryResponse.getData().get(i).setCustProType(2);
                                        }
                                        chatDataArry.add(chatHistoryResponse.getData().get(i));
                                    }
//                                    chatDataArry.addAll(chatHistoryResponse.getData());
                                    Collections.reverse(chatDataArry);
                                    view.updateData(chatDataArry);
                                }

                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
//                                view.onError(jsonObject.getString("message"));
                            }

                            Utility.printLog("chatHistory : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("chatHistory : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
//                        view.onError( context.getResources().getString(R.string.serverError));
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }

    @Override
    public void message(String message) {
        if(!message.trim().isEmpty())
        {
            sendMessage(message);
        }
    }


    private void initializeRxJava()
    {

        observer = new Observer<JSONObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(JSONObject jsonObject) {

                try{
                    Gson gson=new Gson();
                    ChatData chatData=gson.fromJson(jsonObject.getString("data"),ChatData.class);
                    chatData.setCustProType(2);

                    chatDataArry.add(chatData);
                    view.updateData(chatDataArry);

                }catch (Exception e){
                    e.printStackTrace();
                    Utility.printLog("Caught : "+e.getMessage());
                }



            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        ChatDataObervable.getInstance().subscribe(observer);
    }

    private void sendMessage(final String content){

        final long timeStamp=System.currentTimeMillis();

        if(view!=null)
            view.showProgress();

        Observable<Response<ResponseBody>> message=messageService.message(
                "1",
                preferenceHelperDataSource.getToken(),
                "1",
                timeStamp+"",
                content,
                preferenceHelperDataSource.getDriverID(),
                bid,
                custId
                );
        message.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        if(view!=null)
                            view.hideProgress();

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200){
                                jsonObject=new JSONObject(value.body().string());

                                ChatData chatData=new ChatData();
                                chatData.setTimestamp(timeStamp);
                                chatData.setType(1);
                                chatData.setTargetId(custId);
                                chatData.setFromID(preferenceHelperDataSource.getDriverID());
                                chatData.setContent(content);
                                chatData.setCustProType(1);

                                chatDataArry.add(chatData);
                                view.updateData(chatDataArry);

                            }else {
                                jsonObject=new JSONObject(value.errorBody().string());

                            }

                            Utility.printLog("messageApi : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("messageApi : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)
                            view.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)
                            view.hideProgress();
                    }
                });
    }
}
