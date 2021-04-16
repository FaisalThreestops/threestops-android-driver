package com.driver.threestops.dagger;




import com.driver.threestops.ForgotPassword.Changepassword.ChangePasswordModule;
import com.driver.threestops.ForgotPassword.ForgotPasswordChangePass;
import com.driver.threestops.ForgotPassword.ForgotPasswordMobNum;
import com.driver.threestops.ForgotPassword.ForgotPasswordVerify;
import com.driver.threestops.ForgotPassword.RetriveFromMobile.RetriveFromMobModule;
import com.driver.threestops.ForgotPassword.VerifyMobile.VerifyMobileModule;
import com.driver.threestops.app.main.bank.addBankAccount.BankNewAccountModule;
import com.driver.threestops.app.main.help_center.zendeskHelpIndex.ZendeskAdapterModule;
import com.driver.threestops.app.main.help_center.zendeskHelpIndex.ZendeskHelpIndexAct;
import com.driver.threestops.app.main.help_center.zendeskHelpIndex.ZendeskModule;
import com.driver.threestops.app.main.help_center.zendeskTicketDetails.HelpIndexAdapterModule;
import com.driver.threestops.app.main.help_center.zendeskTicketDetails.HelpIndexTicketDetailsAct;
import com.driver.threestops.app.main.help_center.zendeskTicketDetails.HelpTicketDetailsModule;
import com.driver.threestops.app.orderEdit.OrderEditActivity;
import com.driver.threestops.app.orderEdit.OrderEditModule;
import com.driver.threestops.app.replaceItem.ReplaceItemsActivity;
import com.driver.threestops.app.replaceItem.ReplaceModule;
import com.driver.threestops.app.selectedStore.SelectedStoreIDModule;
import com.driver.threestops.app.selectedStore.SelectedStoreIdActivity;
import com.driver.threestops.app.slotAppointments.SlotAppointmentActivity;
import com.driver.threestops.app.slotAppointments.SlotAppointmentModule;
import com.driver.threestops.app.SplashScreen;
import com.driver.threestops.app.bookingRequest.BookingPopUp;
import com.driver.threestops.app.bookingRequest.BookingPopupModule;
import com.driver.threestops.app.bookingRide.BookingRide;
import com.driver.threestops.app.bookingRide.BookingRideModule;
import com.driver.threestops.app.invoice.InvoiceActivity;
import com.driver.threestops.app.invoice.InvoiceModule;
import com.driver.threestops.app.main.MainActivity;
import com.driver.threestops.app.main.MainActivityModule;
import com.driver.threestops.app.main.bank.BankDetailsModule;
import com.driver.threestops.app.main.bank.addBankAccount.BankNewAccountActivity;
import com.driver.threestops.app.main.bank.stripe.BankNewStripeActivity;
import com.driver.threestops.app.main.bank.stripe.StripeAccountModule;
import com.driver.threestops.app.main.history.HistoryModule;
import com.driver.threestops.app.main.history.orderDetails.HistoryOrderDetailsNew;
import com.driver.threestops.app.main.history.orderDetails.OrderHistoryModule;
import com.driver.threestops.app.main.homeFrag.HomeFragmentModule;
import com.driver.threestops.app.main.profile.ProfileFragmentModule;
import com.driver.threestops.app.main.profile.editProfile.EditProfileActivity;
import com.driver.threestops.app.main.profile.editProfile.EditProfileModule;
import com.driver.threestops.app.main.support.SupportModule;
import com.driver.threestops.app.main.support.subCategory.SubCatActivityModule;
import com.driver.threestops.app.main.support.subCategory.SupportSubCategoryActivity;
import com.driver.threestops.app.main.support.webView.WebViewActivity;
import com.driver.threestops.app.main.support.webView.WebViewModule;
import com.driver.threestops.app.storeDetails.StoreDetailsModule;
import com.driver.threestops.app.storeDetails.StorePickUpDetails;
import com.driver.threestops.app.storePickUp.PickUpModule;
import com.driver.threestops.app.storePickUp.StorePickUp;
import com.driver.threestops.login.LoginActivity;
import com.driver.threestops.login.LoginDaggerModule;
import com.driver.threestops.mqttChat.ChattingActivity;
import com.driver.threestops.mqttChat.ChattingModule;
import com.driver.threestops.networking.NetworkCheckerService;
import com.driver.threestops.payment.PaymentAct;
import com.driver.threestops.payment.PaymentActModule;
import com.driver.threestops.payment.PaymentDaggerModule;
import com.driver.threestops.payment_add_card.AddCardAct;
import com.driver.threestops.payment_add_card.AddCardActModule;
import com.driver.threestops.payment_add_card.AddCardModule;
import com.driver.threestops.payment_card_detail.CardDetailAct;
import com.driver.threestops.payment_card_detail.CardDetailActModule;
import com.driver.threestops.payment_card_detail.CardDetailModule;
import com.driver.threestops.payment_choose_card.ChoosePaymentAct;
import com.driver.threestops.payment_choose_card.ChoosePaymentActModule;
import com.driver.threestops.payment_choose_card.ChoosePaymentDaggerModule;
import com.driver.threestops.service.LocationUpdateService;
import com.driver.threestops.signup.perosonal.SignUpPersonalModule;
import com.driver.threestops.signup.perosonal.SignupPersonal;
import com.driver.threestops.vehiclelist.AdapterModule;
import com.driver.threestops.vehiclelist.VehicleList;
import com.driver.threestops.vehiclelist.VehicleListModule;
import com.driver.threestops.wallet.WalletAct;
import com.driver.threestops.wallet.WalletActModule;
import com.driver.threestops.wallet.WalletDaggerModule;
import com.driver.threestops.wallet.voucher.VoucherModule;
import com.driver.threestops.walletNew.WalletTransActivity;
import com.driver.threestops.walletNew.WalletTransactionActivityDaggerModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module ActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create 4 subcomponents for us.
 */
@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract NetworkCheckerService provideNetworkCheckerService();

    @ContributesAndroidInjector
    abstract LocationUpdateService locationUpdateService();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract SplashScreen splashActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = LoginDaggerModule.class)
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector (modules = RetriveFromMobModule.class)
    abstract ForgotPasswordMobNum forgotPasswordMobNum();

    @ActivityScoped
    @ContributesAndroidInjector (modules = VerifyMobileModule.class)
    abstract ForgotPasswordVerify forgotPasswordVerify();

    @ActivityScoped
    @ContributesAndroidInjector (modules = ChangePasswordModule.class)
    abstract ForgotPasswordChangePass forgotPasswordChangePass();

    @ActivityScoped
    @ContributesAndroidInjector (modules = {VehicleListModule.class,AdapterModule.class})
    abstract VehicleList vehicleList();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={SignUpPersonalModule.class})
    abstract SignupPersonal signupPersonal();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={MainActivityModule.class,HomeFragmentModule.class,HistoryModule.class,ProfileFragmentModule.class,SupportModule.class,BankDetailsModule.class})
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={StoreDetailsModule.class})
    abstract StorePickUpDetails storePickUpDetails();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={BookingPopupModule.class})
    abstract BookingPopUp bookingPopUp();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={BookingRideModule.class})
    abstract BookingRide bookingRide();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={PickUpModule.class})
    abstract StorePickUp storePickUp();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={ReplaceModule.class})
    abstract ReplaceItemsActivity replaceItemsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={OrderEditModule.class})
    abstract OrderEditActivity orderEditActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={SlotAppointmentModule.class})
    abstract SlotAppointmentActivity slotAppointmentsActiv();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={SelectedStoreIDModule.class})
    abstract SelectedStoreIdActivity selectedStoreIdActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={InvoiceModule.class})
    abstract InvoiceActivity invoiceActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={EditProfileModule.class})
    abstract EditProfileActivity editProfileActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={SubCatActivityModule.class})
    abstract SupportSubCategoryActivity supportSubCategoryActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={WebViewModule.class})
    abstract WebViewActivity webViewActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={OrderHistoryModule.class})
    abstract HistoryOrderDetailsNew historyOrderDetails();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={StripeAccountModule.class})
    abstract BankNewStripeActivity  bankNewStripeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={BankNewAccountModule.class})
    abstract BankNewAccountActivity  bankNewAccountActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules ={ChattingModule.class})
    abstract ChattingActivity  chattingActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WalletTransactionActivityDaggerModule.class})
    abstract WalletTransActivity walletTransactionsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WalletDaggerModule.class, WalletActModule.class, VoucherModule.class})
    abstract WalletAct mWalletAct();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ChoosePaymentDaggerModule.class, ChoosePaymentActModule.class})
    abstract ChoosePaymentAct mChoosePaymentAct();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddCardModule.class, AddCardActModule.class})
    abstract AddCardAct paymentDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PaymentDaggerModule.class, PaymentActModule.class})
    abstract PaymentAct paymentAct();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CardDetailModule.class, CardDetailActModule.class})
    abstract CardDetailAct cardEditActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = {ZendeskModule.class, ZendeskAdapterModule.class})
    abstract ZendeskHelpIndexAct provideZendeskHelp();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {HelpTicketDetailsModule.class, HelpIndexAdapterModule.class})
    abstract HelpIndexTicketDetailsAct provideHelpIndexDetails();
}
