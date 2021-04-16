package com.driver.threestops.dagger;

import android.app.Application;
import android.content.Context;


import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.data.source.local.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>AppModule</h1>
 * Used to inject the dependency
 * @author 3Embed
 * @since 21-Dec-17
 */

@Module
abstract class AppModule {
    //expose Application as an injectable context
    @Binds
    abstract Context bindContext(Application application);

    @Binds
    @Singleton
    abstract PreferenceHelperDataSource provideSharedPref(PreferencesHelper sessionManager);
}
