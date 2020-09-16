package com.driver.threestops.wallet;

import android.app.Activity;
import com.driver.threestops.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */
@Module
public abstract  class WalletDaggerModule {
    /**
     * <P>This method provides activity reference</P>
     * @return activity.
     */
    @Binds
    @ActivityScoped
    abstract Activity getActivity(WalletAct activity);

    @Binds
    @ActivityScoped
    abstract WalletView getWalletView(WalletAct activity);


    @Binds
    @ActivityScoped
    abstract WalletPresenter getWalletPresenter(WalletPresenterImpl presenter);




}
