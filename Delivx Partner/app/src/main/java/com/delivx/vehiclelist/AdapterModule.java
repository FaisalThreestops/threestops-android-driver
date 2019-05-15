package com.delivx.vehiclelist;

import android.content.Context;

import com.delivx.dagger.ActivityScoped;

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
