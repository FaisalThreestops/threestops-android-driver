package com.delivx.signup.perosonal;

import android.app.Activity;

import com.delivx.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;


@Module
public abstract class SignUpPersonalModule {
    /**
     * <P>This method provides activity reference</P>
     * @return activity.
     */
    @ActivityScoped
    @Binds
    abstract Activity getActivity(SignupPersonal activity);
    /**
     * <P>This method provides SignupPersonal reference</P>
     * @return SignupPersonal.
     */
    @ActivityScoped
    @Binds
    abstract PersonalView getView1(SignupPersonal view);


    @Binds
    @ActivityScoped
    abstract PersonalPresenterContract getPresenter(PersonalPresenter presenter);
}
