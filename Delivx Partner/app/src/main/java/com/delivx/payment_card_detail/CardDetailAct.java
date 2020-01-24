package com.delivx.payment_card_detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.delivx.utility.FontUtils;
import com.driver.delivx.R;
import dagger.android.support.DaggerAppCompatActivity;
import javax.inject.Inject;

/**
 * @author Pramod
 * @since 16-01-2018.
 */

public class CardDetailAct extends DaggerAppCompatActivity implements CardDetailView {


    @Inject
    CardDetailPresenter cardDetailPresenter;
    @BindView(R.id.tv_cardNumber)
    TextView tv_cardNumber;

    @BindView(R.id.btnMarkDefault)
    Button btnMarkDefault;
    @BindView(R.id.btnDeleteCard)
    Button btnDeleteCard;
    @BindView(R.id.tv_expiryDate)
    TextView tv_expiryDate;
    @BindView(R.id.cardNameTv)
    TextView cardNameTv;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

  @BindDrawable(R.drawable.back_white_btn)
  Drawable back_white_btn;

  Toolbar toolBar;
  TextView tvAbarTitle;

    String cardId;
  @Inject
  FontUtils appTypeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        ButterKnife.bind(this);
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

    private void initialize() {


        Typeface fontMedium = appTypeface.getFont_muli_semi_bold();
        Typeface fontRegular = appTypeface.getFont_muli_regular();
        cardNameTv.setTypeface(fontMedium);
        btnMarkDefault.setTypeface(fontRegular);
        btnDeleteCard.setTypeface(fontMedium);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            cardId = getIntent().getStringExtra("cardId");
            boolean  isDefault = getIntent().getBooleanExtra("isDefault",false);
            if(isDefault)
            {
                btnMarkDefault.setVisibility(View.GONE);
            }

            cardDetailPresenter.getIntentData(cardId,getIntent().getStringExtra("brand"),getIntent().getStringExtra("lastDigit"),getIntent().getIntExtra("mn",0),
                    getIntent().getIntExtra("yr",0) );


        }
    }

    @OnClick(R.id.btnMarkDefault)
    void setBtnMarkDefault() {
          cardDetailPresenter.makeDefault(cardId);

    }

    @OnClick(R.id.btnDeleteCard)
    void setBtnDeleteCard() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.alert));
        alertDialog.setMessage(getString(R.string.sureFordelete));
        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

        alertDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        cardDetailPresenter.deleteCard(cardId);

                    }
                });
        alertDialog.show();


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

    @Override
    public void navToPayment() {
        onBackPressed();
    }

    @Override
    public void setCardData(String brand, String cardNo, String exp) {
        tv_cardNumber.setText(cardNo);
        tv_expiryDate.setText(exp);
        cardNameTv.setText(brand);
    }



    @Override
    public void setErrorMsg(String errorMsg) {
        Toast.makeText(this,""+errorMsg,Toast.LENGTH_LONG).show();
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
