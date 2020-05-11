package com.delivx.app.main.help_center.zendeskTicketDetails;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;


import com.delivx.app.main.help_center.zendeskpojo.ZendeskHistory;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.networking.NetworkService;
import com.delivx.utility.Utility;
import com.driver.delivx.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h>HelpIndexContractImpl</h>
 * Created by Ali on 2/26/2018.
 */

public class HelpIndexContractImpl implements HelpIndexContract.presenter {
    @Inject
    NetworkService lspServices;
    @Inject
    HelpIndexContract.HelpView helpView;
    Gson gson;
    @Inject
    PreferenceHelperDataSource manager;
    @Inject
    Activity mActivity;

    @Inject
    public HelpIndexContractImpl() {
        gson = new Gson();
    }


    @Override
    public void onPriorityImage(Context mContext, String priority, ImageView ivHelpCenterPriorityPre) {

        if (priority.equalsIgnoreCase(mContext.getString(R.string.priorityUrgent)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext, R.color.green_continue));
        else if (priority.equalsIgnoreCase(mContext.getString(R.string.priorityHigh)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext, R.color.livemblue3498));
        else if (priority.equalsIgnoreCase(mContext.getString(R.string.priorityNormal)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext, R.color.red_login_dark));
        else if (priority.equalsIgnoreCase(mContext.getString(R.string.priorityLow)))
            ivHelpCenterPriorityPre.setBackgroundColor(Utility.getColor(mContext, R.color.saffron));

    }

    @Override
    public void callApiToCommentOnTicket(String trim, int zenId) {
        String token = manager.getToken();
        Observable<Response<ResponseBody>> observable = lspServices.commentOnTicket(token,
                manager.getLanguage(), String.valueOf(zenId), trim, manager.getRequesterId());

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        int code = responseBodyResponse.code();

                        switch (code) {
                            case 200:

                                break;
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
    public void callApiToCreateTicket(final String trim, final String subject, final String priority) {
        Log.d("check", "manager.getToken(): "+manager.getToken()+"\n"+
                manager.getLanguage()+"\n"+
                subject+"\n"+
                trim+"\n"+
                priority+"\n"+
                manager.getRequesterId()
        );

        Observable<Response<ResponseBody>> observable = lspServices.createTicket(manager.getToken(),
                manager.getLanguage(), subject, trim, "open", priority, "problem", manager.getRequesterId());

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        int code = responseBodyResponse.code();
                        String message = "";
                        Utility.printLog("create zendex "+code);
                        try {
                            JSONObject object = new JSONObject(responseBodyResponse.errorBody().string());
                            if (object.has("message")) {
                                message = object.getString("message");
                            }
                            Utility.printLog("create zendex msg "+message);

                        } catch (JSONException | NullPointerException | IOException e) {
                        }

                        String response;
                        try {


                            switch (code) {
                                case 200:
                                    response = responseBodyResponse.body().string();
                                    Log.d("TAG", "RESULTonSuccess: " + response);
                                    helpView.onZendeskTicketAdded(response);
                                    break;
                                    default:
                                        helpView.onError(message);
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
    public void callApiToGetTicketInfo(final int zenId) {
        Observable<Response<ResponseBody>> observable = lspServices.onToGetZendeskHistory(manager.getToken(),
                manager.getLanguage(), String.valueOf(zenId));

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        int code = responseBodyResponse.code();
                        String message = "";
                        try {
                            JSONObject object = new JSONObject(responseBodyResponse.errorBody().string());
                            if (object.has("message")) {
                                message = object.getString("message");
                            }

                        } catch (JSONException | NullPointerException | IOException e) {
                        }

                        String response;
                        JSONObject jsonObject;
                        try {


                            switch (code) {
                                case 200:
                                    response = responseBodyResponse.body().string();
                                    Log.d("HELPINDEX", "onNextResponseINFOTICKET: " + response);
                                    ZendeskHistory zendeskHistory = gson.fromJson(response, ZendeskHistory.class);

                                    Date date = new Date(zendeskHistory.getData().getTimeStamp() * 1000L);
                                    String dateTime[] = Utility.getFormattedDate(date).split(",");
                                    String timeToSet = dateTime[0] + " | " + dateTime[1];
                                    helpView.onTicketInfoSuccess(zendeskHistory.getData().getEvents(), timeToSet,
                                            zendeskHistory.getData().getSubject(), zendeskHistory.getData().getPriority(), zendeskHistory.getData().getType());
                                    helpView.hideLoading();
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
        return "";
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
