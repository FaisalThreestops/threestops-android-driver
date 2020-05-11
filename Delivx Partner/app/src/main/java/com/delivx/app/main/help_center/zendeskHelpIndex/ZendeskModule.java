package com.delivx.app.main.help_center.zendeskHelpIndex;

import android.app.Activity;


import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h>ZendeskModule</h>
 * Created by Ali on 2/26/2018.
 */
@Module
public interface ZendeskModule
{
    @ActivityScoped
    @Binds
    Activity getActivity(ZendeskHelpIndexAct activity);

    @ActivityScoped
    @Binds
    ZendeskHelpIndexContract.Presenter providePresenter(ZendeskHelpIndexImpl zendeskHelpIndex);

    @ActivityScoped
    @Binds
    ZendeskHelpIndexContract.ZendeskView provideView(ZendeskHelpIndexAct zendeskHelpIndex);
}
