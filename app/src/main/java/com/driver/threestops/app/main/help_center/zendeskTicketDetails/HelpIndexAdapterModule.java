package com.driver.threestops.app.main.help_center.zendeskTicketDetails;


import com.driver.threestops.app.main.help_center.zendeskadapter.HelpIndexRecyclerAdapter;
import com.driver.threestops.dagger.ActivityScoped;

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
