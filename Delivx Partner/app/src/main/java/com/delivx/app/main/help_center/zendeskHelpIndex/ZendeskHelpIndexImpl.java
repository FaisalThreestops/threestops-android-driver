package com.delivx.app.main.help_center.zendeskHelpIndex;

import android.app.Activity;
import android.util.Log;


import com.delivx.app.main.help_center.zendeskpojo.AllTicket;
import com.delivx.app.main.help_center.zendeskpojo.OpenClose;
import com.delivx.app.main.help_center.zendeskpojo.TicketClose;
import com.delivx.app.main.help_center.zendeskpojo.TicketOpen;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.delivx.utility.Utility;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h>ZendeskHelpIndexImpl</h>
 * Created by Ali on 2/26/2018.
 */

public class ZendeskHelpIndexImpl implements ZendeskHelpIndexContract.Presenter
{

    /*http://45.77.190.140:9999/zendesk/user/akbar%40gmail.com*/

    @Inject ZendeskHelpIndexContract.ZendeskView zendeskView;
    @Inject
    NetworkService lspServices;
    private Gson gson;
    @Inject
    PreferenceHelperDataSource manager;
    @Inject
    Activity mActivity;


    @Inject
    public ZendeskHelpIndexImpl()
    {
        gson = new Gson();
    }
    @Override
    public void onToGetZendeskTicket()
    {
        String token = manager.getToken();

        Observable<Response<ResponseBody>> observable = lspServices.onToGetZendeskTicket(token,
                manager.getLanguage(),manager.getMyEmail());

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        int code = responseBodyResponse.code();
                        Utility.printLog("get zendex "+code);

                        String response;
                        String message="";
                        try
                        {
                            JSONObject object=new JSONObject(responseBodyResponse.errorBody().string());
                            if(object.has("message"))
                            {
                                message=object.getString("message");
                            }
                            Utility.printLog("get zendex msg "+message);

                        }catch (JSONException |NullPointerException|IOException e)
                        {}
                        //ErrorHandel errorHandel;
                        try{


                            switch (code)
                            {
                                case 200:
                                    response = Objects.requireNonNull(responseBodyResponse.body()).string();
                                    Log.d("TAG", "onNextTICKETSuccess: "+response);
                                    AllTicket allTicket = gson.fromJson(response,AllTicket.class);

                                    try {
                                        if (allTicket != null && allTicket.getData().getClose() != null && allTicket.getData().getOpen() != null) {
                                            if(allTicket.getData().getClose().size() > 0 || allTicket.getData().getOpen().size() > 0)
                                            {
                                                if(allTicket.getData().getOpen().size()>0)
                                                {
                                                    for(int i = 0;i<allTicket.getData().getOpen().size();i++)
                                                    {
                                                        TicketOpen ticketOpen = allTicket.getData().getOpen().get(i);
                                                        OpenClose openClose = new OpenClose(ticketOpen.getId(),ticketOpen.getTimeStamp()
                                                                ,ticketOpen.getStatus(),ticketOpen.getSubject(),ticketOpen.getType(),
                                                                ticketOpen.getPriority(),ticketOpen.getDescription());

                                                        if(i==0)
                                                            openClose.setFirst(true);
                                                        // openCloses.add(openClose);
                                                        zendeskView.onTicketStatus(openClose,allTicket.getData().getOpen().size(),true);
                                                    }
                                                }

                                                if(allTicket.getData().getClose().size()>0)
                                                {
                                                    for(int i = 0;i<allTicket.getData().getClose().size();i++)
                                                    {
                                                        TicketClose ticketClose = allTicket.getData().getClose().get(i);
                                                        OpenClose openClose = new OpenClose(ticketClose.getId(),ticketClose.getTimeStamp()
                                                                ,ticketClose.getStatus(),ticketClose.getSubject(),ticketClose.getType(),ticketClose.getPriority()
                                                                ,ticketClose.getDescription());
                                                        if(i==0)
                                                        {
                                                            openClose.setFirst(true);
                                                        }
                                                        // openCloses.add(openClose);
                                                        // zendeskView.onTicketStatus(openClose, allTicket.getData().getClose().size());
                                                        zendeskView.onTicketStatus(openClose,allTicket.getData().getClose().size(),false);
                                                    }
                                                }

                                                zendeskView.onNotifyData();
                                                zendeskView.hideLoading();
                                            }else
                                            {
                                                zendeskView.onEmptyTicket();
                                                zendeskView.hideLoading();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 498:
//                                    SessionTokenExpObservable.getInstance().emit(true,message);
                                    break;
                                case 440:
//                                    SessionTokenExpObservable.getInstance().emit(false,message);
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public String getLanguage() {
//        return manager.getLocalLang();
        return "en";
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isNetworkAvailable() {
        return false;
    }
}
