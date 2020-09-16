package com.driver.threestops.networking;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.driver.Threestops.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>FareBreakdownDialog</h1>
 * Used to show the fare estimate breakdown
 * @author 3Embed
 * @since on 2/19/2018.
 */
public class NetworkErrorDialog extends Dialog
{
    @BindView(R.id.tv_no_internet_title) TextView tv_no_internet_title;
    Typeface typeface;

    public NetworkErrorDialog(@NonNull Context context, Typeface typeface)
    {
        super(context, R.style.FullScreenDialogStyle);
        this.typeface = typeface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.layout_no_network_alert);
        initialize();
    }

    /**
     * <h2>initialize</h2>
     * This method is used to initialize
     */
    private void initialize()
    {
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;
        tv_no_internet_title.setTypeface(typeface);
    }
}

