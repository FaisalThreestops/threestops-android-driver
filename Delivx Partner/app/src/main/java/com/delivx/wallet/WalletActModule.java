package com.delivx.wallet;


import static com.delivx.utility.AppConstants.WALLET_ACT;

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
public class WalletActModule {

    @Provides
    @Named(WALLET_ACT)
    @ActivityScoped
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }



}
