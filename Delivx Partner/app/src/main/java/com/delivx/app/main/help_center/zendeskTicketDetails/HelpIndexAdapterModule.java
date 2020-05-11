package com.delivx.app.main.help_center.zendeskTicketDetails;


import com.delivx.app.main.help_center.zendeskadapter.HelpIndexRecyclerAdapter;
import com.delivx.dagger.ActivityScoped;

import dagger.Module;
import dagger.Provides;

/**
 * <h>HelpIndexAdapterModule</h>
 * Created by Ali on 2/26/2018.
 */
@Module
public class HelpIndexAdapterModule
{
    @ActivityScoped
    @Provides
    HelpIndexRecyclerAdapter provideHelpAdapter()
    {
     return new  HelpIndexRecyclerAdapter();
    }
}
