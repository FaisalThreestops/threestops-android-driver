package com.delivx.networking;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;



public interface MessageService {

    @POST("message")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> message(@Header("language")String language,
                                               @Header("authorization")String authorization,
                                               @Field("type")String type,
                                               @Field("timestamp")String timestamp,
                                               @Field("content")String content,
                                               @Field("fromID")String fromID,
                                               @Field("bid")String bid,
                                               @Field("targetId")String targetId
                                                      );


    @GET("chatHistory/{bid}/{page}")
    Observable<Response<ResponseBody>> chatHistory (@Header("language") String language,
                                                @Header("authorization") String authorization,
                                                @Path("bid") String bid,
                                                @Path("page") String page);
}
