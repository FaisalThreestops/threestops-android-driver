package com.delivx.payment_add_card;


import android.app.Activity;
import com.delivx.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;


/**
 * @author Pramod
 * @since  31-01-2018.
 */

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link AddCardPresenter}.
 */
@Module
public abstract class AddCardModule {

    @ActivityScoped
    @Binds
    abstract Activity paymentMethodActivity(AddCardAct paymentMethodActivity);

    @ActivityScoped
    @Binds
    abstract AddCardPresenter paymentMethodPresenter(AddCardPresenterImpl presenter);

   @ActivityScoped
   @Binds
    abstract AddCardView paymentMethodView(AddCardAct paymentMethodActivity);

}
