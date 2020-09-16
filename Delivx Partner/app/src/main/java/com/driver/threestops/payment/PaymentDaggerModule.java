package com.driver.threestops.payment;

import android.app.Activity;
import com.driver.threestops.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */
@Module
public abstract class PaymentDaggerModule {
    /**
     * <P>This method provides activity reference</P>
     * @return activity.
     */
    @ActivityScoped
    @Binds
    abstract Activity getActivity(PaymentAct activity);

    @ActivityScoped
    @Binds
    abstract PaymentView getPaymentView(PaymentAct view);

    @ActivityScoped
    @Binds
    abstract PaymentPresenter getpaymentPresenter(PaymentPresenterImpl presenter);



}
