package com.delivx.dagger;




import com.delivx.ForgotPassword.Changepassword.ChangePasswordModule;
import com.delivx.ForgotPassword.ForgotPasswordChangePass;
import com.delivx.ForgotPassword.ForgotPasswordMobNum;
import com.delivx.ForgotPassword.ForgotPasswordVerify;
import com.delivx.ForgotPassword.RetriveFromMobile.RetriveFromMobModule;
import com.delivx.ForgotPassword.VerifyMobile.VerifyMobileModule;
import com.delivx.app.orderEdit.OrderEditActivity;
import com.delivx.app.orderEdit.OrderEditModule;
import com.delivx.app.replaceItem.ReplaceItemsActivity;
import com.delivx.app.replaceItem.ReplaceModule;
import com.delivx.app.selectedStore.SelectedStoreIDModule;
import com.delivx.app.selectedStore.SelectedStoreIdActivity;
import com.delivx.app.slotAppointments.SlotAppointmentActivity;
import com.delivx.app.slotAppointments.SlotAppointmentModule;
import com.delivx.app.SplashScreen;
import com.delivx.app.bookingRequest.BookingPopUp;
import com.delivx.app.bookingRequest.BookingPopupModule;
import com.delivx.app.bookingRide.BookingRide;
import com.delivx.app.bookingRide.BookingRideModule;
import com.delivx.app.invoice.InvoiceActivity;
import com.delivx.app.invoice.InvoiceModule;
import com.delivx.app.main.MainActivity;
import com.delivx.app.main.MainActivityModule;
import com.delivx.app.main.bank.BankDetailsModule;
import com.delivx.app.main.bank.addBankAccount.AddBankAccountModule;
import com.delivx.app.main.bank.addBankAccount.BankNewAccountActivity;
import com.delivx.app.main.bank.stripe.BankNewStripeActivity;
import com.delivx.app.main.bank.stripe.StripeAccountModule;
import com.delivx.app.main.history.HistoryModule;
import com.delivx.app.main.history.orderDetails.HistoryOrderDetailsNew;
import com.delivx.app.main.history.orderDetails.OrderHistoryModule;
import com.delivx.app.main.homeFrag.HomeFragmentModule;
import com.delivx.app.main.profile.ProfileFragmentModule;
import com.delivx.app.main.profile.editProfile.EditProfileActivity;
import com.delivx.app.main.profile.editProfile.EditProfileModule;
import com.delivx.app.main.support.SupportModule;
import com.delivx.app.main.support.subCategory.SubCatActivityModule;
import com.delivx.app.main.support.subCategory.SupportSubCategoryActivity;
import com.delivx.app.main.support.webView.WebViewActivity;
import com.delivx.app.main.support.webView.WebViewModule;
import com.delivx.app.storeDetails.StoreDetailsModule;
import com.delivx.app.storeDetails.StorePickUpDetails;
import com.delivx.app.storePickUp.PickUpModule;
import com.delivx.app.storePickUp.StorePickUp;
import com.delivx.login.LoginActivity;
import com.delivx.login.LoginDaggerModule;
import com.delivx.mqttChat.ChattingActivity;
import com.delivx.mqttChat.ChattingModule;
import com.delivx.networking.NetworkCheckerService;
import com.delivx.payment.PaymentAct;
import com.delivx.payment.PaymentActModule;
import com.delivx.payment.PaymentDaggerModule;
import com.delivx.payment_add_card.AddCardAct;
import com.delivx.payment_add_card.AddCardActModule;
import com.delivx.payment_add_card.AddCardModule;
import com.delivx.payment_card_detail.CardDetailAct;
import com.delivx.payment_card_detail.CardDetailActModule;
import com.delivx.payment_card_detail.CardDetailModule;
import com.delivx.payment_choose_card.ChoosePaymentAct;
import com.delivx.payment_choose_card.ChoosePaymentActModule;
import com.delivx.payment_choose_card.ChoosePaymentDaggerModule;
import com.delivx.service.LocationUpdateService;
import com.delivx.signup.perosonal.SignUpPersonalModule;
import com.delivx.signup.perosonal.SignupPersonal;
import com.delivx.vehiclelist.AdapterModule;
import com.delivx.vehiclelist.VehicleList;
import com.delivx.vehiclelist.VehicleListModule;
import com.delivx.wallet.WalletAct;
import com.delivx.wallet.WalletActModule;
import com.delivx.wallet.WalletDaggerModule;
import com.delivx.wallet.voucher.VoucherModule;
import com.delivx.walletNew.WalletTransActivity;
import com.delivx.walletNew.WalletTransactionActivityDaggerModule;

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
    @ContributesAndroidInjector(modules ={AddBankAccountModule.class})
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
}
