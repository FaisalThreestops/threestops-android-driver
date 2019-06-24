package com.delivx.dagger;

import android.content.Context;

import com.delivx.RxObservers.RxNetworkObserver;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.managers.booking.BookingManager;
import com.delivx.managers.booking.RxBookingAssignObserver;
import com.delivx.managers.booking.RxDriverCancelledObserver;
import com.delivx.managers.mqtt.MQTTManager;
import com.delivx.networking.DispatcherService;
import com.delivx.networking.NetworkStateHolder;
import com.delivx.utility.AcknowledgeHelper;
import com.delivx.utility.DialogHelper;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Upload_file_AmazonS3;
import com.delivx.utility.VariableConstant;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DELL on 10-01-2018.
 */

@Module
public class UtilityModule {

    @Provides
    @Singleton
    Upload_file_AmazonS3 getAmazonInstance(Context context){
        return new Upload_file_AmazonS3(context,VariableConstant.COGNITO_POOL_ID);
    }

    @Provides
    AcknowledgeHelper getAcknowledgeHelper(PreferenceHelperDataSource dataSource, DispatcherService dispatcherService){
        return new AcknowledgeHelper(dataSource,dispatcherService);
    }

    @Provides
    @Singleton
    RxNetworkObserver provideRxNetworkObserver(){
        return new RxNetworkObserver();
    }

    @Provides
    @Singleton
    NetworkStateHolder networkStateHolder(){
        return  new NetworkStateHolder();
    }

    @Provides
    @Singleton
    MQTTManager mqttManager(Context context,AcknowledgeHelper acknowledgeHelper,
                            PreferenceHelperDataSource helperDataSource,NetworkStateHolder holder,
                            RxNetworkObserver rxNetworkObserver,  BookingManager bookingManager)
    {
        return new MQTTManager(context,acknowledgeHelper,helperDataSource,holder,rxNetworkObserver,bookingManager);
    }

    @Provides
    @Singleton
    FontUtils fontUtils(Context context){
        return new FontUtils(context);
    }

  /*  @Provides
    @Singleton
    CouchDbHandler getCouchDbHandler(Context context,PreferenceHelperDataSource preferenceHelperDataSource){
        return new CouchDbHandler(context,preferenceHelperDataSource);
    }*/



    @Provides
    @Singleton
    RxDriverCancelledObserver rxDriverCancelledObserver() {
        return new RxDriverCancelledObserver();

    }

    @Provides
    @Singleton
    RxBookingAssignObserver rxBookingAssignObserver() {
        return new RxBookingAssignObserver();

    }

    @Provides
    @Singleton
    Gson provideGson()
    {
        return new Gson();
    }


    @Provides
    @Singleton
    BookingManager provideBookingManager(RxBookingAssignObserver rxBookingAssignObserver){
        return new BookingManager(rxBookingAssignObserver);
    }

    @Provides
    @Singleton
    DialogHelper provideDialogHelper() { return new DialogHelper();}



}
