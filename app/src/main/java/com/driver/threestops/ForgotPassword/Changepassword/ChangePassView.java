package com.driver.threestops.ForgotPassword.Changepassword;

import com.driver.threestops.BaseView;

public interface ChangePassView extends BaseView {

    /**
     * <h2>initActionBar</h2>
     * <p></p>initializing the action bar</p>
     */
    void initActionBar();

    /**
     * <h2>setTitle</h2>
     * <p>set the title to action bar</p>
     */
    void setTitle();

    /**
     * <h2>startLoginAct</h2>
     * <p>moving to loginActivity</p>
     */
    void startLoginAct();

    /**
     * <h2>showError</h2>
     * <p>shows the error message</p>
     * @param string : error
     */
    void showError(String string);
}
