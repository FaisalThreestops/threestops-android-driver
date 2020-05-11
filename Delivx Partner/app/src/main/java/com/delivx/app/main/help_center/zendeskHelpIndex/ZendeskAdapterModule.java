package com.delivx.app.main.help_center.zendeskHelpIndex;

import com.delivx.app.main.help_center.zendeskadapter.HelpIndexAdapter;
import com.delivx.dagger.ActivityScoped;

import dagger.Module;
import dagger.Provides;

/**
 * <h>ZendeskAdapterModule</h>
 * Created by Ali on 2/26/2018.
 */

@Module
public class ZendeskAdapterModule
{
    @ActivityScoped
    @Provides
    HelpIndexAdapter provideHelpIndexAdapter()
    {
        return  new HelpIndexAdapter();
    }
}
