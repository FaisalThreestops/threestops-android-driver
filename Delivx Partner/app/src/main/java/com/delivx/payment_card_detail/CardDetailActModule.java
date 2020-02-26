package com.delivx.payment_card_detail;

import static com.delivx.utility.AppConstants.CARD_DETAIL_ACT;

import com.delivx.dagger.ActivityScoped;
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