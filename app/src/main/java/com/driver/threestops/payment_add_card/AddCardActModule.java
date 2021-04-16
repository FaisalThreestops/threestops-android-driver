package com.driver.threestops.payment_add_card;


import static com.driver.threestops.utility.AppConstants.ADDCARD_ACT;
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
public class AddCardActModule {
    @Provides
    @Named(ADDCARD_ACT)
    @ActivityScoped
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
