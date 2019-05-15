package com.delivx.networking;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

/**
 * Created by DELL on 31-01-2018.
 */

public interface DispatcherService
{
    @POST("driver/bookingAck")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> bookingAck (@Header("language") String language,
                                                   @Header("authorization") String authorization,
                                                   @Field("orderId")  String orderId);

    @POST("driver/respondToRequest")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> respondToRequest (@Header("language") String language,
                                                         @Header("authorization") String authorization,
                                                         @Field("orderId")  String orderId,
                                                         @Field("status")  String status);

    @POST("driver/bookingStatusRide")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> bookingStatusRide (@Header("language") String language,
                                                          @Header("authorization") String authorization,
                                                          @Field("orderId")  String orderId,
                                                          @Field("status")  String status,
                                                          @Field("lat") double lat,
                                                          @Field("long") double longi,
                                                          @Field("distance") String distance,
                                                          @Field("signatureUrl") String signature,
                                                          @Field("rating") String rating,
                                                          @Field("review") String review,
                                                          @Field("receiverName") String receiverName,
                                                          @Field("receiverPhone") String receiverPhone);


    @PATCH("driver/location")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> location (@Header("language") String language,
                                                 @Header("authorization") String authorization,
                                                 @Field("latitude")String latitude,
                                                 @Field("longitude")String longitude,
                                                 @Field("vehicleType")String vehicleType,
                                                 @Field("pubnubStr")String pubnubStr,
                                                 @Field("appVersion")String appVersion,
                                                 @Field("batteryPer")String batteryPer,
                                                 @Field("locationCheck")String locationCheck,
                                                 @Field("deviceType")String deviceType,
                                                 @Field("locationHeading")String locationHeading,
                                                 @Field("transit")String transit,
                                                 @Field("presenceTime")String presenceTime,
                                                 @Field("status")String status);

    @PATCH("driver/status")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> status (@Header("language") String language,
                                               @Header("authorization") String authorization,
                                               @Field("status")int status);

}
