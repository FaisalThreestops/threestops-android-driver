package com.delivx.app.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.delivx.app.MyApplication;
import com.delivx.app.main.history.HistoryFragment;
import com.delivx.app.main.profile.MyProfileFrag;
import com.delivx.app.main.homeFrag.HomeFragment;
import com.delivx.app.main.support.SupportFrag;
import com.delivx.app.main.bank.addBankAccount.BankNewAccountActivity;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.login.LoginActivity;
import com.delivx.networking.NetworkService;
import com.delivx.service.LocationUpdateService;
import com.delivx.utility.AppConstants;
import com.delivx.utility.CircleImageView;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;
import com.delivx.utility.VariableConstant;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends DaggerAppCompatActivity
        implements MainView,NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = MainActivity.class.getName();

//    private ProgressDialog pDialog;
    private boolean homeOpen = true;

    private boolean doubleBackToExitPressedOnce=false;
    private Typeface ClanaproNarrMedium,ClanaproNarrNews;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.abarMain) AppBarLayout abarMain;
    @BindView(R.id.button_menu) ImageView button_menu;
    @BindView(R.id.toolbarMenu) ImageView toolbarMenu;
    @BindView(R.id.iv_history) ImageView iv_history;
    @BindView(R.id.menu_layout) RelativeLayout menu_layout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.tvVersion) TextView tvVersion;
    RelativeLayout rl_profile_view;
    TextView tv_viewpof;
    TextView tv_prof_name;
    CircleImageView iv_profile;
    @BindView(R.id.tv_on_off_statas) TextView tv_on_off_statas;
    @BindView(R.id.tv_prof_edit) TextView tv_prof_edit;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvTitle2) TextView  tvTitle2;

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
    private Fragment fragment;
    private Dialog dialog;

    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setShowWhenLocked(true);
        setTurnScreenOn(true);
*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
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
    protected void onStart() {
        super.onStart();
        presenter.getAppConfig();
        checkAndRequestPermissions();
    }


    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
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

    private void onPermissionGranted() {

        if (!Utility.isMyServiceRunning(LocationUpdateService.class, MainActivity.this)) {
            Intent startIntent = new Intent(MainActivity.this, LocationUpdateService.class);
            startIntent.setAction(AppConstants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);
        }
        if (homeOpen && homeFragment.isAdded()) {
            homeFragment.setGoogleMap();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initVar() {

        ClanaproNarrMedium = fontUtils.titaliumSemiBold();
        ClanaproNarrNews = fontUtils.titaliumRegular();


        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        rl_profile_view=ButterKnife.findById(header,R.id.rl_profile_view);
        tv_viewpof=ButterKnife.findById(header,R.id.tv_viewpof);
        tv_prof_name=ButterKnife.findById(header,R.id.tv_prof_name);
        iv_profile=ButterKnife.findById(header,R.id.iv_profile);
        tv_viewpof.setTypeface(ClanaproNarrNews);
        tv_prof_name.setTypeface(ClanaproNarrNews);
        rl_profile_view.setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                presenter.onDrawerOpen();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

        };

        drawer.addDrawerListener(toggle);

        fragment=homeFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, homeFragment)
                .commit();
        tv_on_off_statas.setTypeface(ClanaproNarrMedium);

        tv_prof_edit.setTypeface(ClanaproNarrMedium);

        tvTitle.setTypeface(ClanaproNarrMedium);

        tvTitle2.setTypeface(ClanaproNarrMedium);

        menu_layout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.transparent));
        abarMain.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
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
                iv_history.setVisibility(View.GONE);
                button_menu.setImageResource(R.drawable.selector_hamburger);
                menu_layout.setVisibility(View.VISIBLE);
                menu_layout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.transparent));
                break;

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
                iv_history.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                break;
            case R.id.nav_support:
                homeOpen = false;
                fragment = new SupportFrag();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .commit();

               /* Intent intent=new Intent(this,ChattingActivity.class);
                intent.putExtra("BID","12345");
                intent.putExtra("PROID","12345");
                intent.putExtra("PRONAME","12345");
                startActivity(intent);*/

                tv_on_off_statas.setVisibility(View.GONE);
                tv_prof_edit.setVisibility(View.GONE);
                tvTitle.setText((getResources().getString(R.string.support)));
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle2.setVisibility(View.GONE);
                iv_history.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);

                button_menu.setImageResource(R.drawable.selector_hamburger_white);
                break;

/*
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
                iv_history.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);

                break;*/

           /* case R.id.nav_portal:
                *//*homeOpen = false;
                walletOpen=false;
                fragment = new PortalFrag();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                tv_on_off_statas.setVisibility(View.GONE);
                tvTitle.setText("Portal");
                tv_prof_edit.setVisibility(View.GONE);
                tvTitle2.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                tv_prof_edit.setVisibility(View.GONE);
                iv_history.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.VISIBLE);
                button_menu.setImageResource(R.drawable.selector_hamburger_white);*//*

                break;*/
            case R.id.nav_payment:
                /*homeOpen = false;
                walletOpen=true;
                fragment=new WalletFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .commit();
                tv_on_off_statas.setVisibility(View.GONE);
                tv_prof_edit.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
//                tvTitle.setText(getString(R.string.payments));
                tvTitle2.setVisibility(View.GONE);
                iv_history.setVisibility(View.GONE);
                menu_layout.setVisibility(View.GONE);
                abarMain.setVisibility(View.GONE);*/
                try {

                    Intent intent = new Intent(MainActivity.this, com.delivx.walletNew.WalletTransActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                    Utility.printLog(TAG + " caught : " + e.getMessage());
                }
                break;


           /* case R.id.nav_live_chat:
                if (homeOpen) {
                    abarMain.setVisibility(View.GONE);
                }
//                walletOpen=false;
                menu_layout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.transparent));

                try {

                    Intent intent = new Intent(MainActivity.this, com.livechatinc.inappchat.ChatWindowActivity.class);
                    intent.putExtra(ChatWindowActivity.KEY_GROUP_ID, "your_group_id");
                    intent.putExtra(ChatWindowActivity.KEY_LICENCE_NUMBER, "4711811");
                    startActivity(intent);

                } catch (Exception e) {
                    Utility.printLog(TAG + " caught : " + e.getMessage());
                }

                break;*/

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @OnClick({R.id.button_menu,R.id.toolbarMenu,R.id.iv_history,R.id.tv_prof_edit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_menu:
            case R.id.toolbarMenu: {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            }

            case R.id.tv_prof_edit:
                if (VariableConstant.IS_STRIPE_ADDED) {
                    Intent intent = new Intent(MainActivity.this, BankNewAccountActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.bottom_to_top, R.anim.stay);
                } else {
                    Utility.BlueToast(MainActivity.this, getString(R.string.plsAddStripeFirst));
                }
                break;

            case R.id.iv_history:
                homeFragment.hideList();
                break;

            case R.id.rl_profile_view:
                homeOpen = false;
                fragment = myProfileFrag ;
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .addSharedElement(iv_profile, ViewCompat.getTransitionName(iv_profile))
                        .commit();
                drawer.closeDrawer(GravityCompat.START);
                tv_on_off_statas.setVisibility(View.GONE);
                iv_history.setVisibility(View.GONE);
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

        if(fragment!=null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        onPermissionGranted();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else if (requestCode == VariableConstant.RC_LOCATION_STATE) {
            EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.location_permission_message)
                     , VariableConstant.RC_LOCATION_STATE, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    public void moveDrawer(DrawerLayout mDrawerLayout) {
        // Drawer State checking
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void networkError(String message) {
        Utility.printLog(TAG+ "networkError called..1");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(dialog!=null && !dialog.isShowing()) {
                        mShowNoInternetMessage();
                    }else if(dialog==null){
                        mShowNoInternetMessage();
                    }
                }
            });
        }


    @Override
    public void showProgress() {
//        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
//        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setVersiontext(String version) {
        tvVersion.setTypeface(ClanaproNarrNews);
        tvVersion.setText(getString(R.string.version) + ": " + version);
    }

    @Override
    public void setError(final int code, String msg) {


        if(code==498){
            logout();
            return;
        }
        Utility.mShowMessage(getResources().getString(R.string.message), msg, this, new Utility.AlertDialogCallBack() {
                @Override
                public void onOkPressed() {
                    if(code==498){
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
        Picasso.with(MainActivity.this)
                .load(preferenceHelperDataSource.getProfilePic())
                .resize(200, 200)
                .into(iv_profile, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("success", "onDrawerOpened: ");
                    }
                    @Override
                    public void onError() {
                        Log.d("error", "onDrawerOpened: ");
                    }
                });
    }

    @Override
    public void dismissDialog() {
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void logout() {
        preferenceHelperDataSource.clearSharedPredf();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/" + preferenceHelperDataSource.getPushTopic());
        ((MyApplication)getApplicationContext()).disconnectMqtt();

        if(Utility.isMyServiceRunning(LocationUpdateService.class,MainActivity.this))
        {
            Intent stopIntent = new Intent(MainActivity.this, LocationUpdateService.class);
            stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
            MainActivity.this.startService(stopIntent);
        }
        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void mShowNoInternetMessage() {

        dialog=new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.no_internet_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);


        TextView tvTryAgain= (TextView) dialog.findViewById(R.id.tvTryAgain);
        tvTryAgain.setTypeface(fontUtils.titaliumRegular());
        TextView tvErrorMessage= (TextView) dialog.findViewById(R.id.tvErrorMessage);
        tvErrorMessage.setTypeface(fontUtils.titaliumSemiBold());
        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        try{
            dialog.show();
        }catch (Exception e){
        }
    }
}
