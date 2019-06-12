package com.delivx.app.bookingRequest;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delivx.app.main.MainActivity;
import com.delivx.data.source.PreferenceHelperDataSource;
import com.driver.delivx.R;
import com.delivx.pojo.PubnubResponse;
import com.delivx.utility.AppConstants;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.delivx.utility.VariableConstant.IS_POP_UP_OPEN;


/**************************************************************************************************/
public class BookingPopUp extends DaggerAppCompatActivity implements View.OnClickListener ,BookingPopUpMainMVP.ViewOperations{


    private String TAG = "BookingPopUp";
    public static MediaPlayer mediaPlayer;
    private ProgressDialog mDialog;

    @BindView(R.id.circular_progress_bar) ProgressBar circular_progress_bar;
    @BindView(R.id.tv_popup_pickup) TextView  tv_popup_pickup;
    @BindView(R.id.tvBID) TextView  tvBID;
    @BindView(R.id.tvHeaderDistance) TextView tvHeaderDistance ;
    @BindView(R.id.tvHeaderPayment) TextView tvHeaderPayment;
    @BindView(R.id.tvDistance) TextView tvDistance;
    @BindView(R.id.tvPayment) TextView tvPayment;
    @BindView(R.id.btnReject) Button btnReject;
    @BindView(R.id.tv_popup_drop) TextView tv_popup_drop;
    @BindView(R.id.tv_lefttoaccept) TextView tv_lefttoaccept;
    @BindView(R.id.tv_deliveryfee) TextView tv_deliveryfee;
    @BindView(R.id.tv_popup_cur) TextView tv_popup_cur;
    @BindView(R.id.tv_pickuptime) TextView tv_pickuptime;
    @BindView(R.id.tv_popup_droploc) TextView tv_popup_droploc;
    @BindView(R.id.tv_popup_pickuploc) TextView tv_popup_pickuploc;
    @BindView(R.id.tv_droptime) TextView tv_droptime;
    @BindView(R.id.tv_timer) TextView tv_timer;
    @BindView(R.id.tv_delivery_charge) TextView tv_delivery_charge;
    @BindView(R.id.ll_booking_popup) LinearLayout ll_booking_popup;

    @BindView(R.id.tvordertypeHandelers) TextView tvordertypeHandelers;
    @BindView(R.id.tvordertype) TextView tvordertype;

    @Inject
    BookingPopUpMainMVP.PresenterOperations presenter;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    FontUtils fontUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,preferenceHelperDataSource.getLanguageSettings().getLanguageCode());
        setContentView(R.layout.activity_booking_pop_up);

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
        IS_POP_UP_OPEN=true;

        initializeViews();
        presenter.getBundleData(getIntent());
        presenter.startTimer();

    }


    private void initializeViews() {

        Typeface ClanaproNarrNews, ClanaproNarrMedium;
        ClanaproNarrNews = fontUtils.titaliumRegular();
        ClanaproNarrMedium = fontUtils.titaliumSemiBold();
        tv_popup_pickup.setTypeface(ClanaproNarrNews);
        tvBID.setTypeface(ClanaproNarrNews);
        tvHeaderDistance.setTypeface(ClanaproNarrNews);
        tvHeaderPayment.setTypeface(ClanaproNarrNews);
        tvDistance.setTypeface(ClanaproNarrNews);
        tvPayment.setTypeface(ClanaproNarrNews);

        tvordertypeHandelers.setTypeface(ClanaproNarrNews);
        tvordertype.setTypeface(ClanaproNarrNews);

        btnReject.setTypeface(ClanaproNarrNews);
        tv_popup_drop.setTypeface(ClanaproNarrNews);
        tv_lefttoaccept.setTypeface(ClanaproNarrNews);
        tv_deliveryfee.setTypeface(ClanaproNarrNews);
        tv_popup_cur.setTypeface(ClanaproNarrMedium);
        tv_popup_pickuploc.setTypeface(ClanaproNarrNews);
        tv_pickuptime.setTypeface(ClanaproNarrNews);
        tv_popup_droploc.setTypeface(ClanaproNarrNews);
        tv_droptime.setTypeface(ClanaproNarrNews);
        tv_timer.setTypeface(ClanaproNarrMedium);
        tv_delivery_charge.setTypeface(ClanaproNarrMedium);

        mediaPlayer = MediaPlayer.create(this, R.raw.ring);
        mediaPlayer.setLooping(true);

        mDialog = new ProgressDialog(BookingPopUp.this);

    }

    @Override
    public void setTexts(PubnubResponse pubnubResponse)
    {
        tv_popup_cur.setText(pubnubResponse.getCurrencySymbol());
        tvDistance.setText(pubnubResponse.getDis()+" "+pubnubResponse.getMileageMetric());
        tvPayment.setText(pubnubResponse.getPaymentType());
        tv_popup_pickuploc.setText(pubnubResponse.getStoreName()+":"+pubnubResponse.getAdr1());
        tv_pickuptime.setText(Utility.formatDateWeek(pubnubResponse.getDt()));
        tv_popup_droploc.setText(pubnubResponse.getCustomerName()+":"+pubnubResponse.getDrop1());
        tv_droptime.setText(Utility.formatDateWeek(pubnubResponse.getDropDt()));
        tvordertype.setText(pubnubResponse.getStoreTypeMsg());


        if(pubnubResponse.getPaymentType().equals("2"))
            tvPayment.setText(getResources().getString(R.string.cash));
        else
            tvPayment.setText(getResources().getString(R.string.card));

        tv_delivery_charge.setText(Utility.formatPrice(pubnubResponse.getDeliveryFee()));
        tvBID.setText(getResources().getString(R.string.order_id)+ pubnubResponse.getBid());

    }

    @Override
    public void startMusicPlayer() {
        mediaPlayer.start();
    }

    @OnClick({R.id.ll_booking_popup,R.id.btnReject})
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ll_booking_popup:
                presenter.updateApptRequest(AppConstants.BookingStatus.Accept);
                break;

            case R.id.btnReject:
                presenter.updateApptRequest(AppConstants.BookingStatus.Reject);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess(String msg) {
//        Toast.makeText(BookingPopUp.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error) {
        if(error.isEmpty())
            Toast.makeText(BookingPopUp.this, getString(R.string.smthWentWrong), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(BookingPopUp.this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressbar() {
        mDialog.setMessage(getResources().getString(R.string.loading));
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    public void dismissProgressbar() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog.cancel();
        }
    }

    @Override
    public void onTimerChanged(int progress,String time) {
        tv_timer.setText(time);
        circular_progress_bar.setProgress(progress);
    }

    @Override
    public void onFinish() {
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        IS_POP_UP_OPEN=false;

//        finish();
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
