package com.delivx.dagger;

import android.app.Application;


import com.delivx.app.MyApplication;
import com.delivx.networking.LanguageApiService;
import com.delivx.networking.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * <h1>AppComponent</h1>
 * Interface is used to provide the app component
 */
@Singleton
@Component(modules = {AppModule.class,UtilityModule.class,
        NetworkModule.class, ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication>
{
    void inject(MyApplication application);

    @Override
    void inject(DaggerApplication instance);

    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.
    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}


