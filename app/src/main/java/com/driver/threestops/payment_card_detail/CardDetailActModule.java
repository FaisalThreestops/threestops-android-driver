package com.driver.threestops.payment_card_detail;

import static com.driver.threestops.utility.AppConstants.CARD_DETAIL_ACT;

import com.driver.threestops.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Named;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

@Module
public class CardDetailActModule {
    @Provides
    @Named(CARD_DETAIL_ACT)
    @ActivityScoped
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }



}
