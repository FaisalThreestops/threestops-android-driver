package com.delivx.wallet.voucher;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.delivx.utility.LockableBottomSheetBehavior;
import com.delivx.wallet.WalletAct;
import com.driver.Threestops.R;
import java.util.Objects;
import javax.inject.Inject;

/**
 * Created by 3Embed on 27/10/2017.
 */
public class VoucherBottomSheet extends BottomSheetDialogFragment implements VoucherView {

    @BindView(R.id.addAmountEt)
    EditText addAmountEt;
    @BindView(R.id.addMoneyBtn)
    Button addMoneyBtn;
    @BindView(R.id.payRl)
    RelativeLayout payRl;

    @BindView(R.id.progressBar)ProgressBar progressBar;


    private View rootView;
    private BottomSheetBehavior behavior;
    private  StyleSpan boldStyleSpan = new StyleSpan(Typeface.BOLD);

    @Inject
    VoucherPresenter presenter;

    @Inject
    public VoucherBottomSheet() {
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
            new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {

                        case BottomSheetBehavior.STATE_COLLAPSED:
                            break;

                        case BottomSheetBehavior.STATE_SETTLING:
                            break;

                        case BottomSheetBehavior.STATE_EXPANDED:
                            break;
                        case STATE_HIDDEN:
                            dismiss();
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            break;

                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            };


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style)
    {
        super.setupDialog(dialog, style);
        rootView = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.voucher_bottom_sheet, null, false);
        ButterKnife.bind(this, rootView);
        dialog.setContentView(rootView);

        initViews(rootView);
        clickEvent(rootView);

    }

    private void initViews(View view) {
        presenter.attachView(this);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        behavior = new LockableBottomSheetBehavior();
        params.setBehavior(behavior);
        ((View) view.getParent()).setLayoutParams(params);
        if (behavior != null) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
            behavior.setHideable(false);
        }
    }

    @OnClick({R.id.addMoneyBtn})
    public void clickEvent(View view) {
        if(progressBar.getVisibility()==View.VISIBLE)
            return;
        switch (view.getId()) {



            case R.id.addMoneyBtn:
                if (payRl.isSelected())
                    presenter.addAmount(addAmountEt.getText().toString());
                break;


                default:
                    break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                behavior.setPeekHeight(rootView.getHeight());
            }
        }, 500);

        addAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (addAmountEt.getText() != null && !addAmountEt.getText().toString().equals("")) {
                    payRl.setSelected(true);
                }else
                {
                    payRl.setSelected(false);
                }

            }
        });
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closePopup() {
        if(getActivity() instanceof WalletAct)
        {
            ((WalletAct)(getActivity())).getAmount();
        }
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(progressBar!=null)
        {
            if(progressBar.getVisibility()==View.VISIBLE)
                return;
        }

        super.onDismiss(dialog);

        presenter.clearData();
    }

    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        if(progressBar!=null)
        {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if(progressBar!=null)
        {
            progressBar.setVisibility(View.GONE);
        }
    }
}
