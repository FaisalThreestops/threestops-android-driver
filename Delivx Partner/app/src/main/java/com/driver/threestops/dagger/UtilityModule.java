package com.driver.threestops.dagger;

import android.content.Context;

import com.driver.threestops.RxObservers.RxNetworkObserver;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.managers.booking.BookingManager;
import com.driver.threestops.managers.booking.RxBookingAssignObserver;
import com.driver.threestops.managers.booking.RxDriverCancelledObserver;
import com.driver.threestops.managers.mqtt.MQTTManager;
import com.driver.threestops.networking.DispatcherService;
import com.driver.threestops.networking.NetworkStateHolder;
import com.driver.threestops.utility.AcknowledgeHelper;
import com.driver.threestops.utility.DialogHelper;
import com.driver.threestops.utility.FontUtils;

import com.driver.threestops.utility.ImageUploadAPI;
import com.driver.threestops.utility.SessionManager;
import com.driver.threestops.utility.Upload_file_AmazonS3;
import com.driver.Threestops.BuildConfig;
import com.google.gson.Gson;

import javax.inject.Inject;
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
    Upload_file_AmazonS3 getAmazonInstance(Context context) {
        return new Upload_file_AmazonS3(context, BuildConfig.COGNITO_POOL_ID);
    }

    @Provides
    @Singleton
    ImageUploadAPI imageUploadAPI() {
        return new ImageUploadAPI();
    }

    @Provides
    AcknowledgeHelper getAcknowledgeHelper(PreferenceHelperDataSource dataSource, DispatcherService dispatcherService) {
        return new AcknowledgeHelper(dataSource, dispatcherService);
    }

    @Provides
    @Singleton
    SessionManager getSessionManager(Context context) {
        return new SessionManager(context);
    }

    @Provides
    @Singleton
    RxNetworkObserver provideRxNetworkObserver() {
        return new RxNetworkObserver();
    }

    @Provides
    @Singleton
    NetworkStateHolder networkStateHolder() {
        return new NetworkStateHolder();
    }

    @Provides
    @Singleton
    MQTTManager mqttManager(Context context, AcknowledgeHelper acknowledgeHelper,
                            PreferenceHelperDataSource helperDataSource, NetworkStateHolder holder,
                            RxNetworkObserver rxNetworkObserver, BookingManager bookingManager) {
        return new MQTTManager(context, acknowledgeHelper, helperDataSource, holder, rxNetworkObserver, bookingManager);
    }

    @Provides
    @Singleton
    FontUtils fontUtils(Context context) {
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
    Gson provideGson() {
        return new Gson();
    }


    @Provides
    @Singleton
    BookingManager provideBookingManager(RxBookingAssignObserver rxBookingAssignObserver) {
        return new BookingManager(rxBookingAssignObserver);
    }

    @Provides
    @Singleton
    DialogHelper provideDialogHelper() {
        return new DialogHelper();
    }


}
