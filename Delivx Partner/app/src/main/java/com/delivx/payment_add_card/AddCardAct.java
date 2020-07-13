package com.delivx.payment_add_card;

import static android.Manifest.permission.CAMERA;
import static com.delivx.utility.AppConstants.REQUEST_CODE_PERMISSION_MULTIPLE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.delivx.utility.FontUtils;
import com.delivx.utility.Utility;
import com.driver.Threestops.R;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardMultilineWidget;
import dagger.android.support.DaggerAppCompatActivity;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import javax.inject.Inject;


public class AddCardAct extends DaggerAppCompatActivity implements AddCardView
{

    @Inject
    AddCardPresenter addCardPresenter;
    @BindView(R.id.card_input_widget)
    CardMultilineWidget cardInputWidget;
    @BindView(R.id.saveCardBtn)
    Button saveCardBtn;
    @BindView(R.id.scanCardTv)
    TextView scanCardTv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindDrawable(R.drawable.back_white_btn)
    Drawable back_white_btn;

    Toolbar toolBar;
    TextView tvAbarTitle;

    @Inject
    FontUtils appTypeface;
    private CreditCard scanResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_payment);

        initialize();
        initToolBar();

    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void initToolBar()
    {
        toolBar=findViewById(R.id.toolbar);
        tvAbarTitle=findViewById(R.id.tvAbarTitle);
        final RelativeLayout rlABarBackBtn = findViewById(R.id.rlABarBackBtn);
        setSupportActionBar(toolBar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(back_white_btn);
        }

        tvAbarTitle.setTypeface(appTypeface.clanaproNarrMedium());
        tvAbarTitle.setText(getResources().getString(R.string.flexyCoin));
        rlABarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void initialize()
    {

        ButterKnife.bind(this);
        Typeface fontMedium = appTypeface.getFont_muli_semi_bold();
        String paymentOptions = getResources().getString(R.string.addNewCard);
        scanCardTv.setTypeface(fontMedium);
        saveCardBtn.setTypeface(fontMedium);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    @OnClick({R.id.saveCardBtn,R.id.scanCardTv})
    void setTvDone(View view) {
        Utility.hideSoftKeyboard(cardInputWidget);
        if(progressBar.getVisibility()==View.VISIBLE)
            return;
        switch (view.getId())
        {
            case R.id.saveCardBtn:
                // Remember that the card object will be null if the user inputs invalid data.
                Card card = cardInputWidget.getCard();
                addCardPresenter.addCard(card);
                break;
            case R.id.scanCardTv:
                startScanCard();
                break;
            default:
                break;
        }



    }

    @Override
    public void setErrorMsg(String errorMsg) {
        saveCardBtn.setEnabled(true);
        Toast.makeText(this,""+errorMsg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void startScanCard() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ((checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED)))
        {
            requestPermissions(new String[]{CAMERA}, REQUEST_CODE_PERMISSION_MULTIPLE);
        } else
        {
            Intent scanIntent = new Intent(this, CardIOActivity.class);
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: true
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);
            scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
            scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true);
            startActivityForResult(scanIntent, 1111);
        }


    }

    @Override
    public void stopAct() {
        onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_MULTIPLE:
                boolean isDeninedRTPs=false,showRationaleRTPs=false;
                if (grantResults.length > 0)
                {

                    for (int i = 0; i < permissions.length; i++)
                    {
                        isDeninedRTPs = grantResults[i] == PackageManager.PERMISSION_DENIED;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            showRationaleRTPs = shouldShowRequestPermissionRationale(permissions[i]);
                        }

                    }
                    if (isDeninedRTPs)
                    {
                        if (!showRationaleRTPs)
                        {
                            //goToSettings();
                            showMessageOKCancel("You need to allow access permissions,Otherwise you can't" + " continue.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                        } else
                        {

                            isDeninedRTPs = false;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ActivityCompat.requestPermissions(this, new
                                        String[]{CAMERA}, REQUEST_CODE_PERMISSION_MULTIPLE);
                            }
                        }




                    } else
                    {
                        startScanCard();
                    }
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
            scanCardResult();
           /* Card card;
            scanResult.cvv;
            addCardPresenter.addCard(card);*/
        }
    }

    private void scanCardResult() {
        if (scanResult != null) {
            try{
                EditText etcard = cardInputWidget.findViewById(com.stripe.android.R.id.et_card_number);
                EditText etExpiryDate = cardInputWidget.findViewById(com.stripe.android.R.id.et_expiry_date);
                EditText etcardcvv = cardInputWidget.findViewById(com.stripe.android.R.id.et_cvc_number);
                etcard.setText(scanResult.cardNumber);
                String expiryyear = String.valueOf(scanResult.expiryYear);
                String scancardmonth = String.valueOf(scanResult.expiryMonth);
                int expiryMonth = scanResult.expiryMonth;
                int expiryYear = scanResult.expiryYear;
                if (scanResult.expiryMonth < 10) {
                    scancardmonth = "0" + scancardmonth;
                    etExpiryDate.setText(scancardmonth + "/" + expiryyear);
                } else {
                    etExpiryDate.setText(scancardmonth + "/" + expiryyear);
                }
                etcardcvv.setText(scanResult.cvv);
                etcard.requestFocus();
            }catch (Exception e)
            {

            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }


    /**
     * <h>showMessageOKCancel</h>
     * showMessage if permission not granted and to open settings of device
     * @param message input the message string to show body for dialog
     * @param okListener input for dialog
     */
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {

     /*   new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage(message).setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.cancel), null).create().show();*/
    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
