package com.driver.threestops.utility;

import android.util.Log;

import com.driver.threestops.networking.NetworkService;

import org.json.JSONObject;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ImageUploadAPI {

//    @Inject
//    NetworkService networkService;

    public void uploadImage(File file,String type,String res,NetworkService networkService){
        Log.d("uplad", "uploadImage: "+file);

        final Observable<Response<ResponseBody>> uploadImage=networkService.uploadImage(file,type,res);
        uploadImage.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Response<ResponseBody> value) {
                       /* if(view!=null)
                            view.hideProgress();*/

                        try {
                            JSONObject jsonObject;
                            if(value.code()==200)
                            {
                                String res=value.body().toString();
                                jsonObject=new JSONObject(value.body().string());
                               /* preferenceHelperDataSource.setProfilePic(url);*/
                            }else
                            {
                                jsonObject=new JSONObject(value.errorBody().string());
                               /* view.onError(jsonObject.getString("message"));*/
                            }

                            Utility.printLog("updateProfile : "+jsonObject.toString());

                        }catch (Exception e)
                        {
                            Utility.printLog("updateProfile : Catch :"+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", "onError: "+e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        Log.d("error", "completed ");
                    }
                });

    }

}
