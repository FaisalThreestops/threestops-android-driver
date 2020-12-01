package com.driver.threestops.app.bookingRequest;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.driver.Threestops.R;
import com.driver.threestops.app.main.MainActivity;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.pojo.NewBookingMQTTResponse;
import com.driver.threestops.utility.AppConstants;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.TextUtil;
import com.driver.threestops.utility.Utility;
import com.driver.threestops.utility.VariableConstant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.driver.threestops.utility.VariableConstant.IS_POP_UP_OPEN;


public class BookingPopUp extends DaggerAppCompatActivity implements View.OnClickListener, BookingPopUpMainMVP.ViewOperations {

    public static MediaPlayer mediaPlayer;
    private ProgressDialog mDialog;

    @BindView(R.id.circular_progress_bar)
    ProgressBar circular_progress_bar;
    @BindView(R.id.tv_popup_pickup)
    TextView tv_popup_pickup;
    @BindView(R.id.tvBID)
    TextView tvBID;
    @BindView(R.id.tvHeaderDistance)
    TextView tvHeaderDistance;
    @BindView(R.id.tvHeaderPayment)
    TextView tvHeaderPayment;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvPayment)
    TextView tvPayment;
    @BindView(R.id.btnReject)
    Button btnReject;
    @BindView(R.id.tv_popup_drop)
    TextView tv_popup_drop;
    @BindView(R.id.tv_lefttoaccept)
    TextView tv_lefttoaccept;
    @BindView(R.id.tv_deliveryfee)
    TextView tv_deliveryfee;
    @BindView(R.id.tv_popup_cur)
    TextView tv_popup_cur;
    @BindView(R.id.tv_pickuptime)
    TextView tv_pickuptime;
    @BindView(R.id.tv_popup_droploc)
    TextView tv_popup_droploc;
    @BindView(R.id.tv_popup_pickuploc)
    TextView tv_popup_pickuploc;
    @BindView(R.id.tv_droptime)
    TextView tv_droptime;
    @BindView(R.id.tv_timer)
    TextView tv_timer;
    @BindView(R.id.tv_delivery_charge)
    TextView tv_delivery_charge;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.ll_booking_popup)
    LinearLayout ll_booking_popup;

    @BindView(R.id.tvordertypeHandelers)
    TextView tvordertypeHandelers;
    @BindView(R.id.tvordertype)
    TextView tvordertype;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_OrderTime)
    TextView tv_OrderTime;

    @Inject
    BookingPopUpMainMVP.PresenterOperations presenter;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    FontUtils fontUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this, preferenceHelperDataSource.getLanguageSettings().getLanguageCode());
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
        IS_POP_UP_OPEN = true;
        initializeViews();
        presenter.getBundleData(getIntent());
        presenter.startTimer();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }


    /**
     * <h1>initializeViews</h1>
     * <p>Initialize the view(fonts and styles</p>
     */
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
        tvTips.setTypeface(ClanaproNarrMedium);

        mediaPlayer = MediaPlayer.create(this, R.raw.ring);
        mediaPlayer.setLooping(true);

        mDialog = new ProgressDialog(BookingPopUp.this);
        tv_OrderTime.setText("DATE & TIME");

    }

    @Override
    public void setTexts(NewBookingMQTTResponse newBookingMQTTResponse) {
        tv_popup_cur.setText(newBookingMQTTResponse.getCurrencySymbol());
        tvDistance.setText(String.format("%s %s", newBookingMQTTResponse.getDis(), newBookingMQTTResponse.getMileageMetric()));
        tv_popup_pickuploc.setText(String.format("%s:%s", newBookingMQTTResponse.getStoreName(), newBookingMQTTResponse.getAdr1()));
//        tv_pickuptime.setText(Utility.formatDateWeek(newBookingMQTTResponse.getDt()));
        if (newBookingMQTTResponse.getStoreType() != null && newBookingMQTTResponse.getStoreType().equals("7"))
            tv_popup_drop.setText(this.getResources().getString(R.string.delivery));
        else
            tv_popup_drop.setText(this.getResources().getString(R.string.drop));
        tv_popup_droploc.setText(String.format("%s:%s", newBookingMQTTResponse.getCustomerName(), newBookingMQTTResponse.getDrop1()));
        tv_droptime.setText(Utility.getDate(Long.parseLong(newBookingMQTTResponse.getDeliveryDatetimeTimeStamp())));
//        tv_droptime.setText(Utility.formatDateWeek(newBookingMQTTResponse.getDropDt()));
        tvordertype.setText(newBookingMQTTResponse.getStoreTypeMsg());
        tv_date.setText(Utility.bookingDate(Long.parseLong(newBookingMQTTResponse.getOrderDateTimeStamp())));

        if (newBookingMQTTResponse.getPaymentType().equals("2"))
            tvPayment.setText(getResources().getString(R.string.cash));
        else if (newBookingMQTTResponse.getPaymentType().equals("1"))
            tvPayment.setText(getResources().getString(R.string.lbl_wallet));
        else
            tvPayment.setText(getResources().getString(R.string.card));

        tv_delivery_charge.setText(Utility.formatPrice(newBookingMQTTResponse.getDeliveryFee()));
        tvTips.setText(newBookingMQTTResponse.getCurrencySymbol().concat(Utility.formatPrice(TextUtil.isEmpty(newBookingMQTTResponse.getDriverTip()) ? "0" : newBookingMQTTResponse.getDriverTip())));
        tvBID.setText(getResources().getString(R.string.oid).concat(newBookingMQTTResponse.getBid()));

    }

    @Override
    public void startMusicPlayer() {
        mediaPlayer.start();
    }

    @OnClick({R.id.circular_progress_bar, R.id.btnReject})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //Accept Booking
            case R.id.circular_progress_bar:
                presenter.updateApptRequest(AppConstants.BookingStatus.Accept);
                break;

            //Reject  Booking
            case R.id.btnReject:
                presenter.updateApptRequest(AppConstants.BookingStatus.Reject);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NotificationManagerCompat.from(this).cancelAll();
        unregisterReceiver(mOrderUpdateReceiver);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mOrderUpdateReceiver, new IntentFilter(VariableConstant.BOOKING_DISPATCH_CANCEL));
    }

    private final BroadcastReceiver mOrderUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.cancelCountDownTimer();
        }
    };

    @Override
    public void onSuccess(String msg) {
        NotificationManagerCompat.from(this).cancelAll();
    }

    @Override
    public void onError(String error) {
        if (error.isEmpty())
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
    public void onTimerChanged(int progress, String time) {
        tv_timer.setText(time);
        circular_progress_bar.setProgress(progress);
    }

    @Override
    public void onFinish() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        NotificationManagerCompat.from(this).cancelAll();
        IS_POP_UP_OPEN = false;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
