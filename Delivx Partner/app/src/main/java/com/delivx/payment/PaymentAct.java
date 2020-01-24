package com.delivx.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.delivx.payment_add_card.AddCardAct;
import com.delivx.payment_card_detail.CardDetailAct;
import com.delivx.utility.FontUtils;
import com.driver.delivx.R;
import dagger.android.support.DaggerAppCompatActivity;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * <h1>PaymentAct</h1>
 * <p>This calss is used for the payment activities</p>
 */

public class PaymentAct extends DaggerAppCompatActivity implements View.OnClickListener, PaymentView {


    private Typeface fontMedium, fontRegular;

    @BindView(R.id.savedCardstxtTv)
    TextView savedCardstxtTv;

    @BindView(R.id.cardsListRv)
    RecyclerView cardsListRv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.addCardBtn)
    Button addCardBtn;
  @BindDrawable(R.drawable.back_white_btn)
  Drawable back_white_btn;


  @BindView(R.id.noCardsTv)
    TextView noCardsTv;
    @BindView(R.id.startShoppingTv)
    TextView startShoppingTv;
    @BindView(R.id.emptyRl)
    RelativeLayout emptyRl;
    @BindView(R.id.savedCardsLl)
    LinearLayout savedCardsLl;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.rlToolbarDummy)
    RelativeLayout rlToolbarDummy;

  Toolbar toolBar;
  TextView tvAbarTitle;

    @Inject
    PaymentPresenter mPresenter;
  @Inject
  FontUtils appTypeface;
    RecyclerView.Adapter madapter;
    private ArrayList<Cards> paymentCardsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mInitialization();
        setTypeFaces();
        nestedScrollViewListener();
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


    private void mInitialization() {
        ButterKnife.bind(this);
        fontMedium = appTypeface.getFont_muli_bold();
        fontRegular = appTypeface.getFont_muli_regular();
        RecyclerView.LayoutManager mlayoutManager;
        madapter = new PaymentAdapter(this, paymentCardsList, fontRegular);
        cardsListRv.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        cardsListRv.setLayoutManager(mlayoutManager);
        cardsListRv.setItemAnimator(new DefaultItemAnimator());
        cardsListRv.setAdapter(madapter);

    }

    private void nestedScrollViewListener() {
       /* nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > rlToolbarDummy.getHeight()) {
                tvToolbarTitle.setVisibility(View.VISIBLE);
                view_top_shadow.setVisibility(View.VISIBLE);
            } else {
                tvToolbarTitle.setVisibility(View.INVISIBLE);
                view_top_shadow.setVisibility(View.INVISIBLE);
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.callApi();

    }

    private void setTypeFaces() {
        savedCardstxtTv.setTypeface(fontMedium);
        addCardBtn.setTypeface(fontMedium);
        noCardsTv.setTypeface(fontRegular);
        startShoppingTv.setTypeface(fontMedium);
    }

    @OnClick( R.id.addCardBtn)
    public void onClick(View view) {
        if (progressBar.getVisibility() == View.VISIBLE) {
            return;
        }

        switch (view.getId()) {


            case R.id.addCardBtn:
                mPresenter.addNewCard();
                break;
            default:
                break;

        }

    }

    @Override
    public void setPaymentCardsList(ArrayList<Cards> cardsList) {

        if (cardsList.size() > 0) {
            emptyRl.setVisibility(View.GONE);
            savedCardstxtTv.setVisibility(View.VISIBLE);
            cardsListRv.setVisibility(View.VISIBLE);
            paymentCardsList.clear();
            paymentCardsList.addAll(cardsList);
            madapter.notifyDataSetChanged();
        } else {
            emptyRl.setVisibility(View.VISIBLE);
            savedCardstxtTv.setVisibility(View.GONE);
            cardsListRv.setVisibility(View.GONE);
        }

    }

    @Override
    public void startAddCardAct() {
        Intent intent1 = new Intent(this, AddCardAct.class);
        startActivity(intent1);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
    }

    @Override
    public void startCardDetail(String brand, String id, String lastDigit, int mn, int yr, boolean isDefault) {
        Intent intent = new Intent(this, CardDetailAct.class);
        intent.putExtra("brand", brand);
        intent.putExtra("cardId", id);
        intent.putExtra("lastDigit", lastDigit);
        intent.putExtra("mn", mn);
        intent.putExtra("yr", yr);
        intent.putExtra("isDefault", isDefault);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);

    }

    @Override
    public void onError(String msg) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void startShopping() {

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();


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

    public void getCardDetail(String brand, String id, String lastDigit, int mn, int yr, boolean isDefault) {
        mPresenter.getCardDetail(brand, id, lastDigit, mn, yr, isDefault);
    }

  @Override
  public void networkError(String message) {

  }

  @Override
  public void showProgress() {
    if (progressBar != null) {
      progressBar.setVisibility(View.VISIBLE);
      progressBar.setIndeterminate(true);
      progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);
    }
  }

  @Override
  public void hideProgress() {
    progressBar.setVisibility(View.GONE);
  }
}

