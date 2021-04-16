package com.driver.threestops.vehiclelist;

import android.content.Context;

import com.driver.threestops.dagger.ActivityScoped;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DELL on 04-01-2018.
 */
@Module
public class AdapterModule
{
    @Provides
    @ActivityScoped
    VechicleListRVA provideVechicleListRVA(Context context)
    {
        return new VechicleListRVA(context);
    }
}
