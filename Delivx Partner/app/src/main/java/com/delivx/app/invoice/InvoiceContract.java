package com.delivx.app.invoice;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.delivx.BaseView;
import com.delivx.pojo.AssignedAppointments;


public interface InvoiceContract
{
    interface ViewOpetaions extends BaseView
    {
        /**
         * <h2>initActionBar</h2>
         * <p>initializing the action bar</p>
         */
        void initActionBar();

        /**
         * <h2>setTitle</h2>
         * <p>set the text to title in action bar</p>
         * @param title : activity title
         * @param bid : status (online or offline)
         */
        void setTitle(String title,String bid);

        /**
         * <h2>setViews</h2>
         * <p>set the view by values</p>
         * @param bill : billing details
         */
        void setViews(String bill);

        /**
         * <h2>onSuccess</h2>
         * <p>completed the invoice activity</p>
         * @param appointments : appontmentsPojo details
         */
        void onSuccess(AssignedAppointments appointments);

        /**
         * <h2>onError</h2>
         * <p>shows error message</p>
         * @param message : error message
         */
        void onError(String message);

        /**
         * <h2>clearSignature</h2>
         * <p>clear the signature</p>
         */
        void clearSignature();

        /**
         * <h2>hideSignature</h2>
         * <p>hiding the signature</p>
         * @param signBitmap : signature image
         */
        void hideSignature(Bitmap signBitmap);

        /**
         * <h2>showSignature</h2>
         * <p>visibiling the signaturePad</p>
         */
        void showSignature();

        /**
         * <h2>onSignatureApprove</h2>
         * <p>signature is approved and set the signature </p>
         * @param bitmap : signature
         */
        void onSignatureApprove(Bitmap bitmap);

        /**
         * <h2>methodRequiresOnePermission</h2>
         * <p>requesting the run time permission for camera</p>
         */
        void methodRequiresOnePermission();
    }

    interface PresenterOpetaions{

        /**
         * <h2>setActionBar</h2>
         * <p>set the action bar</p>
         */
        void setActionBar();

        /**
         * <h2>setActionBarTitle</h2>
         * <p>set the title to the action bar</p>
         */
        void setActionBarTitle();

        /**
         * <h2>getBundleData</h2>
         * <p>get the data from (SlotAppointmentActivity)</p>
         * @param bundle : data
         */
        void getBundleData(Bundle bundle);

        /**
         * <h2>refresh</h2>
         * <p>clear the signature</p>
         */
        void refresh();

        /**
         * <h2>onBackPressSign</h2>
         * <p>hiding the signature field</p>
         */
        void onBackPressSign();

        /**
         * <h2>onSigned</h2>
         * <p>set the signature resource to presenter</p>
         * @param signBitmap : signature resource
         */
        void onSigned(Bitmap signBitmap);

        /**
         * <h2>onSignatureApprove</h2>
         * <p>adding the signature to amazon url</p>
         */
        void onSignatureApprove();

        /**
         * <h2>openSignaturePad</h2>
         * <p>opening the signature pad to sign</p>
         */
        void openSignaturePad();

        /**
         * <h2>completeBooking</h2>
         * <p>booking completed</p>
         * @param rating : rating(stars)
         */
        void completeBooking(float rating);

        /**
         * <h2>requestPermission</h2>
         * <p>requseting the run time permission for camera</p>
         */
        void requestPermission();

        /**
         * <h2>getlanguageCode</h2>
         * <P>get the language code</P>
         * @return : country code
         */
        String getlanguageCode();

    }
}
