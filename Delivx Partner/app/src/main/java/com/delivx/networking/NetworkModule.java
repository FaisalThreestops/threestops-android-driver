package com.delivx.networking;

/**
 * Created by DELL on 27-12-2017.
 */

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.driver.delivx.BuildConfig;

import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.driver.delivx.BuildConfig.LANGUAGE_URL;


@Module
public class NetworkModule
{
    private static final String NAME_BASE_URL = "NAME_BASE_URL";
    private static final String NAME_DISPATCH_URL = "NAME_DISPATCH_URL";
    private static final String NAME_MESSAGE_URL = "NAME_MESSAGE_URL";
    private static final String NAME_LANGUAGE_URL = "Name_LANGUAGE_URL";


    @Provides
    @Named(NAME_BASE_URL)
    String provideBaseUrlString() {
        return BuildConfig.BASEURL;
    }
    @Provides
    @Named(NAME_DISPATCH_URL)
    String provideBaseDispatcherUrlString() {
        return BuildConfig.DISPATCHER_BASE;
    }
    @Provides
    @Named(NAME_MESSAGE_URL)
    String provideBaseMessageUrlString() {
        return BuildConfig.MESSAGE_BASE;
    }

    @Provides
    @Named(NAME_LANGUAGE_URL)
    String provideLanguageUrlString() {
        return BuildConfig.LANGUAGE_URL;
    }

    @Provides
    @Singleton
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Converter.Factory converter, @Named(NAME_BASE_URL) String baseUrl)
    {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    @Provides
    @Singleton
    NetworkService provideUsdaApi(Retrofit retrofit)
    {
        return retrofit.create(NetworkService.class);
    }
    @Provides
    @Singleton
    DispatcherService provideDisApi(Converter.Factory converter,@Named(NAME_DISPATCH_URL) String baseUrl)
    {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(DispatcherService.class);
    }

    @Provides
    @Singleton
    MessageService provideMessageApi(Converter.Factory converter,@Named(NAME_MESSAGE_URL) String baseUrl)
    {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(MessageService.class);
    }

    @Provides
    @Singleton
    LanguageApiService provideLanguageApi(Converter.Factory converter, @Named(NAME_LANGUAGE_URL) String baseUrl)
    {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(LanguageApiService.class);
    }


}
