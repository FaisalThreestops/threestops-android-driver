package com.delivx.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.TextView;

import com.delivx.data.source.PreferenceHelperDataSource;
import com.delivx.login.language.LanguagesList;
import com.driver.delivx.R;

import java.util.ArrayList;
import javax.inject.Inject;


public class DialogHelper {

    private static AlertDialog alertDialog = null;
    private static String serviceID = "";
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    public DialogHelper() {
    }


    private static DialogCallbackHelper dialogCallbackHelper;
    public void getDialogCallbackHelper(DialogCallbackHelper dialogCallbackHelper) {
        DialogHelper.dialogCallbackHelper = dialogCallbackHelper;
    }

    public interface DialogCallbackHelper {
        void walletFragOpen();

        void changeLanguage(String langCode, String langName, int dir);
    }

    public static void customAlertDialog(final Activity mActivity, String title, String msg, String action) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.alert_dialog_simple_message, null);
        alertDialogBuilder.setView(view);
        TextView tvTitle = view.findViewById(R.id.tv_main_title);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        TextView tvOk = view.findViewById(R.id.tvUpdate);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        View view1 = view.findViewById(R.id.view);
        tvCancel.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);

        tvTitle.setText(title);
        tvMsg.setText(msg);
        tvOk.setText(action);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Animator[] animHide = {null};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.show();
            view.post(new Runnable() {
                @Override
                public void run() {
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;
                    float finalRadius = (float) Math.hypot(cx, cy);
                    Animator animVisible = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                    animHide[0] = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                    animVisible.start();

                }
            });
        } else {
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeBottom;
            alertDialog.show();
        }


        tvOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animHide[0] != null) {
                        animHide[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                alertDialog.dismiss();

                            }
                        });
                        if (!animHide[0].isStarted()) animHide[0].start();
                    } else {
                        alertDialog.dismiss();
                    }
                } else {
                    alertDialog.dismiss();
                }
            }
        });
    }

    /**
     *<h1>customAlertDialogLogout</h1>
     * <p>Logout dialog</p>
     */
    public static void languageSelectDialog(final Activity mActivity,
                                            final ArrayList<LanguagesList> languagesList,
                                            int indexSelected) {

        ArrayList languageListTemp = new ArrayList<>();
        for(int language = 0; language< languagesList.size(); language++)
        {
            languageListTemp.add(languagesList.get(language).getLanguageName());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setTitle(mActivity.getString(R.string.selectLanguage));
        builder.setSingleChoiceItems((CharSequence[]) languageListTemp.toArray(new CharSequence[languagesList.size()]),
                indexSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String langCode = languagesList.get(languagesList.indexOf(languagesList.get(i))).getLanguageCode();
                        String langName = languagesList.get(languagesList.indexOf(languagesList.get(i))).getLanguageName();
                        int dir = Utility.changeLanguageConfig(langCode, mActivity);

                        dialogCallbackHelper.changeLanguage(langCode, langName, dir);

                        if (alertDialog != null && alertDialog.isShowing())
                            alertDialog.dismiss();

                    }
                });

        alertDialog = builder.create();
        alertDialog.show();
    }


}