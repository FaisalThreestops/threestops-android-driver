package com.driver.threestops.payment_choose_card;

import android.app.Activity;
import com.driver.threestops.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;


@Module
public abstract class ChoosePaymentDaggerModule {


    @Binds
    @ActivityScoped
    abstract Activity getActivity(ChoosePaymentAct activity);


    @Binds
    @ActivityScoped
    abstract ChoosePaymentView getPaymentView(ChoosePaymentAct view);

    @Binds
    @ActivityScoped
    abstract ChoosePaymentPresenter getPaymentPresenter(ChoosePaymentPresenterImpl presenter);


}
