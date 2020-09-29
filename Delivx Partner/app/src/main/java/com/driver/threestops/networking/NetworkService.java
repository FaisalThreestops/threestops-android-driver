package com.driver.threestops.networking;

import com.driver.threestops.service.LatLngBody;

import org.json.JSONObject;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface NetworkService {

    @POST("driver/signIn")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> signIn(@Header("language") String language ,
                                              @Field("countryCode")String countryCode,
                                              @Field("mobile") String mobile,
                                              @Field("password") String password,
                                              @Field("deviceId") String deviceId,
                                              @Field("pushToken") String pushToken,
                                              @Field("appVersion") String version,
                                              @Field("deviceMake") String deviceMake,
                                              @Field("deviceModel")String deviceModel,
                                              @Field("deviceType")int deviceType,
                                              @Field("deviceTime")String deviceTime,
                                              @Field("deviceOsVersion")int deviceOsVersion);

    @POST("driver/forgotPassword")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> forgotPassword(@Header("language")String language,
                                                      @Field("countryCode")String countryCode,
                                                      @Field("emailOrMobile")String emailOrMobile,
                                                      @Field("verifyType")int verifyType);


    @POST("driver/sendOtp")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> sendOtp (@Header("language")String language,
                                                @Field("mobile") String mobile,
                                                @Field("countryCode")String countryCode);

    @POST("driver/verifyOtp")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> verifyOtp (@Header("language")String language,
                                                  @Field("mobile") String mobile,
                                                  @Field("countryCode")String countryCode,
                                                  @Field("code") String otp);

    @PATCH("/update/order")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updateQty(@Header("language") String language,
                                                 @Header("authorization") String authorization,
                                                 @Field("items") JSONObject oldItems,
                                                 @Field("newItems") JSONObject newItems,
                                                 @Field("updateType") String updateType,
                                                 @Field("extraNote") String extraNote,
                                                 @Field("ipAddress") String ipAddress,
                                                 @Field("orderId") Double orderId,
                                                 @Field("deliveryCharge") Double deliveryCharge,
                                                 @Field("latitude") Double latitude,
                                                 @Field("longitude") Double longitude);

    @PATCH("driver/password")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> password (@Header("language")String language,
                                                 @Field("mobile") String mobile,
                                                 @Field("countryCode")String countryCode,
                                                 @Field("code") String otp,
                                                 @Field("password") String password);

    @POST("driver/signUp")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> signUp (@Header("language")String language,
                                               @Field("mobile") String mobile,
                                               @Field("countryCode")String countryCode,
                                               @Field("firstName") String firstName,
                                               @Field("lastName") String lastName,
                                               @Field("email") String email,
                                               @Field("password") String password,
                                               @Field("zipCode") String zipCode,
                                               @Field("latitude") String latitude,
                                               @Field("longitude") String longitude,
                                               @Field("profilePic") String profilePic,
                                               @Field("plateNo") String plateNo,
                                               @Field("vehicleImage") String vehicleImage,
                                               @Field("type") String type,
                                               @Field("specialities") String specialities,
                                               @Field("vehicleMake") String vehicleMake,
                                               @Field("vehicleModel") String vehicleModel,
                                               @Field("referral") String referral,
            /*@Field("cities") String city,*/
                                               @Field("zones") String zone,
                                               @Field("insurancePhoto") String insurancePhoto,
                                               @Field("carriagePermit") String carriagePermit,
                                               @Field("regCert") String regCert,
                                               @Field("operator") String operator,
                                               @Field("driverLicense") String driverLicense,
                                               @Field("accountType") String accountType,
                                               @Field("deviceType") String deviceType,
                                               @Field("deviceId") String deviceId,
                                               @Field("appVersion") String appVersion,
                                               @Field("deviceMake") String deviceMake,
                                               @Field("deviceModel") String deviceModel,
                                               @Field("pushToken") String pushToken,
                                               @Field("driverLicenseNumber") String driverLicenseNumber,
                                               @Field("cityId") String cityId,
                                               @Field("cityName") String cityName,
                                               @Field("driverLicenseExpiry") String driverLicenseExpiry,
                                               @Field("dateOfBirth") String dateOfBirth
    );

    @POST("vehicle/default")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> defaultVehicle (@Header("language")String language,
                                                       @Header("authorization")String authorization,
                                                       @Field("workplaceId") String workplaceId,
                                                       @Field("vehicleTypeId")String vehicleTypeId,
                                                       @Field("goodTypes") String goodTypes);

    @PATCH("driver/logout")
    Observable<Response<ResponseBody>> logout (@Header("language") String language,
                                               @Header("authorization") String authorization);

    @POST("driver/emailPhoneValidate")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> emailPhoneValidate (@Header("language") String language,
                                                           @Field("countryCode") String countryCode,
                                                           @Field("mobile") String mobile,
                                                           @Field("verifyType") int verifyType,
                                                           @Field("email") String email);

    @GET("driver/zone")
    Observable<Response<ResponseBody>> cities(@Header("language") String language);

    @GET("driver/vehicleType")
    Observable<Response<ResponseBody>> vehicleType (@Header("language") String language);

    @GET("driver/makeModel")
    Observable<Response<ResponseBody>> vehicleMake (@Header("language") String language);

    @GET("driver/config")
    Observable<Response<ResponseBody>> config (@Header("language") String language,
                                               @Header("authorization") String authorization);

    @GET("driver/assignedTrips")
    Observable<Response<ResponseBody>> assignedTrips (@Header("language") String language,
                                                      @Header("authorization") String authorization);

    @PATCH("driver/status")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> status (@Header("language") String language,
                                               @Header("authorization") String authorization,
                                               @Field("status")int status);

    @POST("driver/order")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> order (@Header("language") String language,
                                              @Header("authorization") String authorization,
                                              @Field("pageIndex")int index,
                                              @Field("startDate")String startDate,
                                              @Field("endDate")String endDate
    );

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

    @POST("driver/locationLogs")
    @Headers( "Content-Type: application/json;charset=UTF-8")
    Observable<Response<ResponseBody>> locationLogs(@Header("authorization")String authorization,
                                                    @Header("language")String language,
                                                    @Body LatLngBody body);

    @GET("driver/profile")
    Observable<Response<ResponseBody>> profile (@Header("language") String language,
                                                @Header("authorization") String authorization);

    @PATCH("driver/profile")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updateProfile (@Header("language") String language,
                                                      @Header("authorization") String authorization,
                                                      @Field("password")String password,
                                                      @Field("mobile")String mobile,
                                                      @Field("countryCode")String countryCode,
                                                      @Field("email")String email,
                                                      @Field("name")String name,
                                                      @Field("profilePic")String profilePic
    );

    @PATCH("driver/order")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> updateOrder (@Header("authorization") String authorization,
                                                    @Header("language") String language,
                                                    @Field("items")String items,
                                                    @Field("extraNote")String extraNote,
                                                    @Field("orderId")String orderId,
                                                    @Field("deliveryCharge")String deliveryCharge,
                                                    @Field("latitude")String latitude,
                                                    @Field("longitude")String longitude
    );

    @GET("driver/support")
    Observable<Response<ResponseBody>> support (@Header("language") String language);

    @GET("https://api.ipify.org/?format=json")
    Observable<Response<ResponseBody>> ipAddess ();

    @GET("connectAccount")
    Observable<Response<ResponseBody>> connectAccount (@Header("language") String language,
                                                       @Header("authorization") String authorization);

    @POST("connectAccount")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> createStripeAccount (@Header("language") String language,
                                                            @Header("authorization") String authorization,
                                                            @Field("email") String email,
                                                            @Field("city") String city,
                                                            @Field("country") String country,
                                                            @Field("line1") String line1,
                                                            @Field("postal_code") String postal_code,
                                                            @Field("state") String state,
                                                            @Field("day") String day,
                                                            @Field("month") String month,
                                                            @Field("year") String year,
                                                            @Field("first_name") String first_name,
                                                            @Field("last_name") String last_name,
                                                            @Field("document") String document,
                                                            @Field("personal_id_number") String personal_id_number,
                                                            @Field("date") String date,
                                                            @Field("ip") String ip);

    @POST("externalAccount")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> externalAccount (@Header("language") String language,
                                                        @Header("authorization") String authorization,
                                                        @Field("email") String email,
                                                        @Field("account_number") String account_number,
                                                        @Field("routing_number") String routing_number,
                                                        @Field("account_holder_name") String account_holder_name,
                                                        @Field("country") String country
    );

    @GET("/city/zones/{cityId}")
    Observable<Response<ResponseBody>> getZones(@Header("language") String language,
                                                @Path("cityId") String cityId);


    @GET("driver/walletTransaction/{pageIndex}")
    Observable<Response<ResponseBody>> getWalletTransaction(@Header("authorization") String authToken,
                                                            @Header("language") String language,
                                                            @Path("pageIndex") String pageIndex);

    @GET("productsSearchByStoreId/{storeId}/{index}/{limit}/{search}")
    Observable<Response<ResponseBody>> getSearchItems(@Header("authorization") String authToken,
                                                      @Header("language") String language,
                                                      @Path("storeId") String storeId,
                                                      @Path("index") String index,
                                                      @Path("limit") String limit,
                                                      @Path("search") String search);

    @PUT("driver/order")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> cancelOrder(@Header("authorization") String authorization,
                                                   @Header("language") String language,
                                                   @Field("reason") String reason,
                                                   @Field("ipAddress") String extraNote,
                                                   @Field("orderId") String orderId,
                                                   @Field("latitude") String latitude,
                                                   @Field("longitude") String longitude);


    @GET("driver/cancellationReasons")
    Observable<Response<ResponseBody>> cancelReason (@Header("language") String language,
                                                      @Header("authorization") String authorization);
    @GET("driver/card")
    Observable<Response<ResponseBody>> getCard(@Header("authorization") String authorization,
        @Header("language") String language);

    @GET("/driver/walletDetail")
    Observable<Response<ResponseBody>> getWalletApi(@Header("authorization") String authorization,
        @Header("language") String language);

    @POST("driver/rechargeWallet")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> addWalletApi(@Header("authorization") String authorization,
        @Header("language") String language,
        @Field("orderId") String orderId,
        @Field("amount") double amount);

    @PATCH("voucher")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> voucher(@Header("authorization") String authorization,
        @Header("language") String language,
        @Field("voucherCode") String voucherCode);

    @POST("driver/card")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> addCard(@Header("authorization") String authorization,
        @Header("language") String language,
        @Field("email") String email,
        @Field("cardToken") String cardToken);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "driver/card", hasBody = true)
    Observable<Response<ResponseBody>> deleteCard(@Header("authorization") String authorization,
        @Header("language") String language,
        @Field("cardId") String cardId);

    @PATCH("driver/card")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> makeDefaultCard(@Header("authorization") String authorization,
        @Header("language") String language,
        @Field("cardId") String cardId);

    // CASHFREE

    @POST("/customer/getCashFreeToken")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> getCashFreeToken(
            @Header("authorization") String authorization,
            @Header("language") String language,
            @Field("amount") String amount
    );

    @GET("driver/getBankDetails")
    Observable<Response<ResponseBody>> getBankDetails (@Header("authorization")String authorization,
                                                       @Header("language")String language);

    @POST("driver/bankDetailValidation")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> bankDetailValidation (@Header("authorization")String authorization,
                                                             @Header("language")String language,
                                                             @Field("name")String name,
                                                             @Field("phone")String phone,
                                                             @Field("bankAccount")String bankAccount,
                                                             @Field("ifsc")String ifsc);

    @POST("driver/addBankDetails")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> addBankDetails (@Header("authorization")String authorization,
                                                       @Header("language")String language,
                                                       @Field("bankAccount")String bankAccount,
                                                       @Field("ifsc")String ifsc,
                                                       @Field("address")String address,
                                                       @Field("city")String city,
                                                       @Field("state")String state,
                                                       @Field("pincode")String pincode);

    @HTTP(method = "DELETE", path = "driver/deleteBankDetails", hasBody = true)
    @FormUrlEncoded
    Observable<Response<ResponseBody>> deleteBankAccount(@Header("authorization") String authToken,
                                                         @Header("language") String language,
                                                         @Field("beneId") String beneId);

    @GET("zendesk/user/ticket/{emailId}")
    Observable<Response<ResponseBody>> onToGetZendeskTicket(@Header("authorization") String authorization,
                                                            @Header("language") String language,
                                                            @Path("emailId") String emailId);

    @PUT("zendesk/ticket/comments")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> commentOnTicket(@Header("authorization") String authorization,
                                                       @Header("language") String language,
                                                       @Field("id") String id,
                                                       @Field("body") String body,
                                                       @Field("author_id") String author_id);

    @POST("zendesk/ticket")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> createTicket(@Header("createTicket") String authorization,
                                                    @Header("language") String language,
                                                    @Field("subject") String subject,
                                                    @Field("body") String body,
                                                    @Field("status") String status,
                                                    @Field("priority") String priority,
                                                    @Field("type") String type,
                                                    @Field("requester_id") String requester_id);

    @GET("zendesk/ticket/history/{id}")
    Observable<Response<ResponseBody>> onToGetZendeskHistory(@Header("authorization") String authorization,
                                                             @Header("language") String language,
                                                             @Path("id") String id);

    @POST("utility/uploadImage")
    @FormUrlEncoded
    Observable<Response<ResponseBody>> uploadImage(@Field("image") File image,
                                                             @Field("type") String type,
                                                             @Field("res") String res);

}

