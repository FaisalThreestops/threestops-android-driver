package com.delivx.app.main.help_center.zendeskTicketDetails;

import android.app.Activity;


import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h>HelpTicketDetailsModule</h>
 * Created by Ali on 2/26/2018.
 */
@Module
public interface HelpTicketDetailsModule
{
    /**
     * <h>get activity</h>
     * <P>This method provides activity reference</P>
     * @return activity.
     */
    @ActivityScoped
    @Binds
    Activity getActivity(HelpIndexTicketDetailsAct activity);

    @ActivityScoped
    @Binds
    HelpIndexContract.presenter providePresenter(HelpIndexContractImpl helpIndexContract);

    @ActivityScoped
    @Binds
    HelpIndexContract.HelpView provideView(HelpIndexTicketDetailsAct helpIndexTicketDetails);
}
