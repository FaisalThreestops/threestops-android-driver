package com.driver.threestops.payment_card_detail;


import android.app.Activity;
import com.driver.threestops.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;


/**
 * @author Pramod
 * @since  31-01-2018.
 */

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link CardDetailPresenter}.
 */
@Module
public abstract class CardDetailModule {

    @ActivityScoped
    @Binds
    abstract Activity cardEditActivity(CardDetailAct cardDetailAct);

    @ActivityScoped
    @Binds
    abstract CardDetailPresenter cardEditPresenter(CardDetailPresenterImpl presenter);

   @ActivityScoped
   @Binds
    abstract CardDetailView cardEditView(CardDetailAct cardDetailAct);

}
