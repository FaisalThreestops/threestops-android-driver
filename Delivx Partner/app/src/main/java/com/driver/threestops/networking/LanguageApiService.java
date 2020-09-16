package com.driver.threestops.networking;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;

public interface LanguageApiService {

    @GET("languageList")
    Observable<Response<ResponseBody>> getLanguage();
}
