package com.driver.threestops.app.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.driver.Threestops.R;
import com.driver.threestops.app.MyApplication;
import com.driver.threestops.app.main.bank.BankListFrag;
import com.driver.threestops.app.main.bank.addBankAccount.BankNewAccountActivity;
import com.driver.threestops.app.main.help_center.zendeskHelpIndex.ZendeskHelpIndexAct;
import com.driver.threestops.app.main.history.HistoryFragment;
import com.driver.threestops.app.main.homeFrag.HomeFragment;
import com.driver.threestops.app.main.profile.MyProfileFrag;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.login.LoginActivity;
import com.driver.threestops.login.language.LanguagesList;
import com.driver.threestops.networking.ConnectivityReceiver;
import com.driver.threestops.networking.NetworkErrorDialog;
import com.driver.threestops.networking.NetworkService;
import com.driver.threestops.payment.PaymentAct;
import com.driver.threestops.service.LocationUpdateService;
import com.driver.threestops.utility.AppConstants;
import com.driver.threestops.utility.CircleImageView;
import com.driver.threestops.utility.DialogHelper;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.Utility;
import com.driver.threestops.utility.VariableConstant;
import com.driver.threestops.wallet.WalletAct;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.net.ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED;


public class MainActivity extends DaggerAppCompatActivity
        implements MainView, NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, EasyPermissions.PermissionCallbacks,
        ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = MainActivity.class.getName();
    private boolean homeOpen = true;

    private boolean doubleBackToExitPressedOnce = false;
    private Typeface ClanaproNarrNews;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.abarMain)
    AppBarLayout abarMain;
    @BindView(R.id.button_menu)
    ImageView button_menu;
    @BindView(R.id.toolbarMenu)
    ImageView toolbarMenu;
    @BindView(R.id.menu_layout)
    RelativeLayout menu_layout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    RelativeLayout rl_profile_view;
    TextView tv_viewpof;
    TextView tv_prof_name;
    CircleImageView iv_profile;
    @BindView(R.id.tv_on_off_statas)
    TextView tv_on_off_statas;
    @BindView(R.id.tv_prof_edit)
    TextView tv_prof_edit;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTitle2)
    TextView tvTitle2;

    @Inject
    MainPresenter presenter;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    NetworkService networkService;
    @Inject
    HomeFragment homeFragment;
    @Inject
    HistoryFragment historyFragment;
    @Inject
    FontUtils fontUtils;
    @Inject
    MyProfileFrag myProfileFrag;
    NetworkErrorDialog networkErrorDialog;
    ArrayList<LanguagesList> languagesLists = new ArrayList<>();
    @Inject
    DialogHelper dialogHelper;

    public static boolean mainActivity_opened = false;

    private Fragment fragment;
    private Dialog dialog;
    private AlertDialog dialogBackgroundDataRequest;

    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Check the language code for RTL UI Check
        Utility.RtlConversion(this, presenter.getlanguageCode());
        setContentView(R.layout.activity_main);


        //if the android version is oreo or above then set the screen always on without lock
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }//if the android version is below oreo then set the screen always on without lock
        else {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        ButterKnife.bind(this);
        checkAndRequestPermissions();

        initVar();

        presenter.getVersion();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mainActivity_opened = true;
        MyApplication.getInstance().setConnectivityListener(this);
        presenter.subscribeNetworkObserver();
        presenter.getAppConfig();
        getOverlayPermission();
        requestIgnoreBatteryOptimizations();
        enableBackgroundDataRestricted();
    }

    /*private void checkLocationUpdateRestrictions() {
        boolean isBackgroundDataRestricted = false, isBatteryOptimized = false;
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Utility.printLog("LocationIssue : Background Data Restricted : " + (connMgr.isActiveNetworkMetered() && connMgr.getRestrictBackgroundStatus() == RESTRICT_BACKGROUND_STATUS_ENABLED));
            if (connMgr.isActiveNetworkMetered() && connMgr.getRestrictBackgroundStatus() == RESTRICT_BACKGROUND_STATUS_ENABLED) {
                isBackgroundDataRestricted = true;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
             isBatteryOptimized = !pm.isIgnoringBatteryOptimizations(packageName);
        }

        if (isBackgroundDataRestricted && isBatteryOptimized)
            Toast.makeText(this, "BG Data Restricted & Battery Optimized!", Toast.LENGTH_LONG).show();
        else if (isBackgroundDataRestricted)
            Toast.makeText(this, "BG Data Restricted!", Toast.LENGTH_LONG).show();
        else if (isBatteryOptimized)
            Toast.makeText(this, "Battery Optimized!", Toast.LENGTH_LONG).show();
    }*/

    /**
     * <h2>enableBackgroundDataRestricted</h2>
     * <p>It's check if app is restricted for access in internet while app in background
     * and if it is true then redirect to controlling background data restriction screen for this particular application</p>
     */
    private void enableBackgroundDataRestricted() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Checks user???s Data Saver settings.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Utility.printLog("LocationIssue : Background Data Restricted : " + (connMgr.isActiveNetworkMetered() && connMgr.getRestrictBackgroundStatus() == RESTRICT_BACKGROUND_STATUS_ENABLED));
            if (connMgr.isActiveNetworkMetered() && connMgr.getRestrictBackgroundStatus() == RESTRICT_BACKGROUND_STATUS_ENABLED) {
                if (dialogBackgroundDataRequest != null && dialogBackgroundDataRequest.isShowing()) {
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.permission_request);
                builder.setMessage(R.string.msg_background_permission_request);
                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    startActivity(new Intent(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS, Uri.parse("package:" + getPackageName())));
                });

                dialogBackgroundDataRequest = builder.create();
                dialogBackgroundDataRequest.setCancelable(false);
                dialogBackgroundDataRequest.show();
            }
        }
    }


    /**
     * <h2>requestIgnoreBatteryOptimizations</h2>
     * <p>It's check if app is restricted in a battery optimizations and if true then
     * redirect user to ask for whitelist app from battery optimizations</p>
     */
    @SuppressLint("BatteryLife")
    private void requestIgnoreBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            Utility.printLog("LocationIssue : Battery Optimization enabled : " + !pm.isIgnoringBatteryOptimizations(packageName));
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainActivity_opened = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAndRequestPermissions();
    }

    /**
     * This method is ask overlay permission on above android Q device
     * so, driver can see booking popup even app is in background or killed.
     */
    private void getOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
//                startActivity(intent);
            }
        }

    }


    /**
     * <h1>checkAndRequestPermissions</h1>
     * <p>Check the location permission using EasyPermissions library</p>
     */
    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
            }
            if (EasyPermissions.hasPermissions(this, perms)) {
                // Already have permission
                if (!Utility.isMyServiceRunning(LocationUpdateService.class, MainActivity.this)) {
                    Intent startIntent = new Intent(MainActivity.this, LocationUpdateService.class);
                    startIntent.setAction(AppConstants.ACTION.STARTFOREGROUND_ACTION);
                    startService(startIntent);
                }
            } else {
                // Do not have permissions, requesting permission
                EasyPermissions.requestPermissions(this, getString(R.string.location_permission_message),
                        VariableConstant.RC_LOCATION_STATE, perms);
            }
        } else {
            // Pre-Marshmallow
            onPermissionGranted();
        }
    }

    /**
     * <h1>onPermissionGranted</h1>
     * <p>start the service for location update</p>
     */
    private void onPermissionGranted() {
        //check whether the service already not running
        if (!Utility.isMyServiceRunning(LocationUpdateService.class, MainActivity.this)) {
            Intent startIntent = new Intent(MainActivity.this, LocationUpdateService.class);
            startIntent.setAction(AppConstants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);
        }

        //if the Home fragment is created and added in Main Activity then set the google map
        if (homeOpen && homeFragment.isAdded()) {
            homeFragment.setGoogleMap();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * <h1>initVar</h1>
     * <p>Initialize the Variable</p>
     */
    private void initVar() {

        //initialize the Dialog Helper for change language call back
        dialogHelper.getDialogCallbackHelper(new DialogHelper.DialogCallbackHelper() {

            @Override
            public void walletFragOpen() {
            }

            @Override
            public void changeLanguage(String langCode, String langName, int dir) {

                //Location update service stop  if running
                if (Utility.isMyServiceRunning(LocationUpdateService.class, getApplicationContext())) {
                    Intent stopIntent = new Intent(MainActivity.this, LocationUpdateService.class);
                    stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
                    startService(stopIntent);
                }
                presenter.languageChanged(langCode, langName);
            }
        });

        ///register the Network check Broadcast receiver
        try {
            registerReceiver(new ConnectivityReceiver(),
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Typeface clanaproNarrMedium = fontUtils.titaliumSemiBold();
        ClanaproNarrNews = fontUtils.titaliumRegular();

        networkErrorDialog = new NetworkErrorDialog(this, ClanaproNarrNews);

        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        rl_profile_view = header.findViewById(R.id.rl_profile_view);
        tv_viewpof = header.findViewById(R.id.tv_viewpof);
        tv_prof_name = header.findViewById(R.id.tv_prof_name);
        iv_profile = header.findViewById(R.id.iv_profile);

        tv_viewpof.setTypeface(ClanaproNarrNews);
        tv_prof_name.setTypeface(ClanaproNarrNews);
        rl_profile_view.setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                presenter.onDrawerOpen();


                Menu menu = navigationView.getMenu();
                MenuItem menuItemBank = menu.getItem(2);
                if (preferenceHelperDataSource.getEnableBankAccount().matches("1")) {
                    menuItemBank.setVisible(true);
                } else {
                    menuItemBank.setVisible(false);
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

        };

        drawer.addDrawerListener(toggle);

        //assign the home fragment as default
        fragment = homeFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, homeFragment)
                .commit();

        tv_on_off_statas.setTypeface(clanaproNarrMedium);
        tv_prof_edit.setTypeface(clanaproNarrMedium);
        tvTitle.setTypeface(clanaproNarrMedium);
        tvTitle2.setTypeface(clanaproNarrMedium);
        menu_layout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.transparent));
        abarMain.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        // check navigation drawer open and close
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //checking for double press the back button  and close activity
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            //wait 2 second for back press and if not done make default value
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {

            //event for Home Select
            case R.id.nav_home:
                fragment = homeFragment;
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .commit();
                homeOpen = true;
                tv_on_off_statas.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.GONE);
                tvTitle2.setVisibility(View.GONE);
                abarMain.setVisibility(View.GONE);
                tv_prof_edit.setVisibility(View.GONE);
                button_menu.setImageResource(R.drawable.selector_hamburger);
                menu_layout.setVisibility(View.VISIBLE);
                menu_layout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.transparent));
                break;

            //event for History Select
            case R.id.nav_history:
                homeOpen = false;
                fragment = historyFragment;
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .commit();

                tv_on_off_statas.setVisibility(View.GONE);
                tv_prof_edit.setVisibility(View.GONE);
                tvTitle.setText((getResources().getString(R.string.history)));
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle2.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                break;

            //event for Support Select
            case R.id.nav_support:

                try {
                    Intent intent = new Intent(this, ZendeskHelpIndexAct.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("comingFrom", "profile");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                } catch (Exception e) {
                    Utility.printLog(" caught : " + e.getMessage());
                }

                /*homeOpen = false;
                fragment = new SupportFrag();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .commit();

                tv_on_off_statas.setVisibility(View.GONE);
                tv_prof_edit.setVisibility(View.GONE);
                tvTitle.setText((getResources().getString(R.string.support)));
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle2.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);*/
                break;

            //event for Bank Details Select
            case R.id.nav_bank_details:
                homeOpen = false;

                fragment = new BankListFrag();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .commit();

                tv_on_off_statas.setVisibility(View.GONE);
                tvTitle.setText((getResources().getString(R.string.bank_details)));
                tv_prof_edit.setText(getResources().getString(R.string.add));
                tvTitle2.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                tv_prof_edit.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                break;

            //event for Wallet Select
            case R.id.nav_payment:
                try {
                    Intent intent = new Intent(MainActivity.this, WalletAct.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Utility.printLog(TAG + " caught : " + e.getMessage());
                }
                break;
             /*case R.id.item_menu_nav_terms:
               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.TERMS_AND_CONDITIONS));
                if (browserIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(browserIntent);
                }
                break;*/
            case R.id.nav_cards:
                try {
                    Intent intent = new Intent(MainActivity.this, PaymentAct.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Utility.printLog(TAG + " caught : " + e.getMessage());
                }
                break;

            //event for Language Select
            case R.id.item_menu_nav_language:
                presenter.getLanguages();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @OnClick({R.id.button_menu, R.id.toolbarMenu, R.id.tv_prof_edit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //for navigation drawer open and close
            case R.id.button_menu:
            case R.id.toolbarMenu: {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            }

            //for edit the profile in Profile Fragment (Enable only in profile)
            case R.id.tv_prof_edit:
                if (VariableConstant.IS_STRIPE_ADDED) {
                    Intent intent = new Intent(MainActivity.this, BankNewAccountActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.bottom_to_top, R.anim.stay);
                } else {
                    Utility.BlueToast(MainActivity.this, getString(R.string.plsAddStripeFirst));
                }
                break;

            //open the profile Fragment
            case R.id.rl_profile_view:
                homeOpen = false;
                fragment = myProfileFrag;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .addSharedElement(iv_profile, ViewCompat.getTransitionName(iv_profile))
                        .commit();
                drawer.closeDrawer(GravityCompat.START);
                tv_on_off_statas.setVisibility(View.GONE);
                tv_prof_edit.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
                tvTitle2.setVisibility(View.VISIBLE);
                tvTitle2.setText(getString(R.string.profile));
                abarMain.setVisibility(View.GONE);
                menu_layout.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        onPermissionGranted();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //Location permission check
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else if (requestCode == VariableConstant.RC_LOCATION_STATE) {
            EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.location_permission_message)
                    , VariableConstant.RC_LOCATION_STATE, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void networkError(String message) {
        Utility.printLog(TAG + "networkError called..1");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && !dialog.isShowing()) {
                    showNoInternetMessage();
                } else if (dialog == null) {
                    showNoInternetMessage();
                }
            }
        });
    }


    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void setVersiontext(String version) {
        tvVersion.setTypeface(ClanaproNarrNews);
        tvVersion.setText(getString(R.string.version) + ": " + version);
    }

    @Override
    public void setError(final int code, String msg) {
        if (code == 498 || code == 440) {
            logout();
            return;
        }
        Utility.mShowMessage(getResources().getString(R.string.message), msg, this, new Utility.AlertDialogCallBack() {
            @Override
            public void onOkPressed() {
                if (code == 498 || code == 440) {
                    logout();
                }
            }

            @Override
            public void onCancelPressed() {

            }
        });
    }

    @Override
    public void setProfileDetails() {
        tv_prof_name.setText(preferenceHelperDataSource.getMyName());
        Picasso.get()
                .load(preferenceHelperDataSource.getProfilePic())
                .resize(200, 200)
                .into(iv_profile, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("success", "onDrawerOpened: ");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("error", "onDrawerOpened: ");
                    }
                });
    }

    @Override
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void logout() {
        LanguagesList languagesList = preferenceHelperDataSource.getLanguageSettings();
        preferenceHelperDataSource.clearSharedPredf();
        preferenceHelperDataSource.setLanguageSettings(languagesList);

        //unsubscribe the fcm topics
        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/" + preferenceHelperDataSource.getPushTopic());
        ((MyApplication) getApplicationContext()).disconnectMqtt();

        //stop the update location service if running
        if (Utility.isMyServiceRunning(LocationUpdateService.class, MainActivity.this)) {
            Intent stopIntent = new Intent(MainActivity.this, LocationUpdateService.class);
            stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
            MainActivity.this.startService(stopIntent);
        }

        //Redirect to  the  Login activity  withe close  all existing Activities
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void networkAvailable() {
        runOnUiThread(() -> {
            if (mainActivity_opened && networkErrorDialog != null && networkErrorDialog.isShowing()) {
                networkErrorDialog.dismiss();
            }
        });
    }

    @Override
    public void networkNotAvailable() {
        runOnUiThread(() -> {
            if (mainActivity_opened && networkErrorDialog != null && !networkErrorDialog.isShowing())
                networkErrorDialog.show();
        });

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        presenter.checkForNetwork(isConnected);
    }


    /**
     * <h1>showNoInternetMessage</h1>
     * <p>Dialog initialize and show for internet not available</p>
     */
    public void showNoInternetMessage() {
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.no_internet_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvTryAgain = dialog.findViewById(R.id.tvTryAgain);
        tvTryAgain.setTypeface(fontUtils.titaliumRegular());
        TextView tvErrorMessage = dialog.findViewById(R.id.tvErrorMessage);
        tvErrorMessage.setTypeface(fontUtils.titaliumSemiBold());
        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        try {
            dialog.show();
        } catch (Exception e) {
        }
    }


    @Override
    public void setLanguageDialog(ArrayList<LanguagesList> languagesListModel, int indexOf) {
        languagesLists = languagesListModel;
        DialogHelper.languageSelectDialog(this, languagesLists, indexOf);
    }

    @Override
    public void setLanguageSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Runtime.getRuntime().exit(0);
    }


}
