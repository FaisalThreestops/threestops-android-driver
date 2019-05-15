package com.delivx.app.invoice;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;

/**
 * Created by DELL on 10-10-2017.
 */

public interface InvoiceContract
{
    interface ViewOpetaions extends BaseView
    {
        void initActionBar();

        void setTitle(String title,String bid);

        void setViews(String bill);

        void onSuccess(AssignedAppointments appointments);

        void onError(String message);

        void clearSignature();

        void hideSignature(Bitmap signBitmap);

        void showSignature();

        void onSignatureApprove(Bitmap bitmap);

        void methodRequiresOnePermission();
    }

    interface PresenterOpetaions{

        void setActionBar();

        void setActionBarTitle();

        void getBundleData(Bundle bundle);

        void refresh();

        void onBackPressSign();

        void onSigned(Bitmap signBitmap);

        void onSignatureApprove();

        void openSignaturePad();

        void completeBooking(float rating);

        void requestPermission();
    }
}
