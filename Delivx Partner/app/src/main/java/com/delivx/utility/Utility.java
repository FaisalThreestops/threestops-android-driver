package com.delivx.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.delivx.app.SplashScreen;
import com.driver.delivx.R;


import java.text.DecimalFormat;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    private static String PREFS_NAME = "preference";

    /**
     * custom method to display the toast message
     * @param context
     * @param msg
     */
    public static synchronized void toastMessage(Context context, String msg) {
        if (context != null && msg != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
        printLog("device id is : "+androidId);
        return androidId;
    }

    //GETTING CURRENT DATE
    public static String date() {
        Calendar calendar = Calendar.getInstance(Locale.US);
        Date date = calendar.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return formater.format(date);
    }

    /**
     * custom method to return the date
     *
     * @return date
     */
    public static String currentDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        month = month + 1;
        String sMonth = "" + month;
        String clmday = "" + day;
        if (month <= 9) {
            sMonth = 0 + sMonth;
        }
        if (day <= 9) {
            clmday = 0 + clmday;
        }
        String todayDate = year + "-" + sMonth + "-" + clmday;

        return todayDate;
    }

    /************************************************    for checking internet connection*******/
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = null;
        boolean isNetworkAvail = false;
        try {
            connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            printLog("info for network  " + info[i].getTypeName());
                            return true;
                        }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connectivity != null) {
                connectivity = null;
            }
        }
        return isNetworkAvail;
    }

    /**
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @return
     */
    public static String sentingDateFormat(int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear + 1;
        String month = "" + monthOfYear;
        String day = "" + dayOfMonth;
        if (monthOfYear <= 9) {
            month = 0 + month;
        }
        if (dayOfMonth <= 9) {
            day = 0 + day;
        }
        String dateFormat = year + "-" + month + "-" + day;

        return dateFormat;
    }

    public static String displayDateFormat(int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear + 1;
        String month = "" + monthOfYear;
        String day = "" + dayOfMonth;
        /*if(monthOfYear<=9)
        {
            month=0+month;
        }*/
        if (dayOfMonth <= 9) {
            day = 0 + day;
        }
        /*switch (monthOfYear) {
            case 1:
                month = "Jan";
                break;

            case 2:
                month = "Feb";
                break;

            case 3:
                month = "Mar";
                break;

            case 4:
                month = "Apr";
                break;

            case 5:
                month = "May";
                break;
            case 6:
                month = "Jun";
                break;

            case 7:
                month = "Jul";
                break;

            case 8:
                month = "Aug";
                break;

            case 9:
                month = "Sep";
                break;

            case 10:
                month = "Oct";
                break;

            case 11:
                month = "Nov";
                break;

            case 12:
                month = "Dec";
                break;

            default:
                break;
        }*/
//        String dateFormat = day + "-" + month + "-" + year;
        String dateFormat = monthOfYear + "/" + day + "/" + year;
        return dateFormat;
    }

    public static String getMonthDay(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "MMM d";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("exe", "ParseException" + e.getMessage());
        }
        return str;
    }

    public static String getMonth(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "d";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("exe", "ParseException" + e.getMessage());
        }
        return str;
    }



    public static String sentingDateNotEdited(String displayDate) {
        String[] date = displayDate.split("-");

        String day = date[0];
        String month = date[1];
        String year = date[2];

        switch (month) {
            case "Jan":
                month = "1";
                break;

            case "Feb":
                month = "2";
                break;

            case "Mar":
                month = "3";
                break;

            case "Apr":
                month = "4";
                break;

            case "May":
                month = "5";
                break;

            case "Jun":
                month = "6";
                break;

            case "Jul":
                month = "7";
                break;

            case "Aug":
                month = "8";
                break;

            case "Sep":
                month = "9";
                break;

            case "Oct":
                month = "10";
                break;

            case "Nov":
                month = "11";
                break;

            case "Dec":
                month = "12";
                break;

            default:
                break;
        }
        String dateFormat = day + "-" + month + "-" + year;
        return dateFormat;
    }

    /**
     * custom method to validate the email
     *
     * @param tilDynamic
     * @param etDynamic
     * @return boolean
     */
    public static boolean validateEmail(TextInputLayout tilDynamic, EditText etDynamic) {

        String email = etDynamic.getText().toString().trim();
        if (!email.isEmpty()) {
            boolean format = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (format) {
                tilDynamic.setErrorEnabled(false);
                return true;
            } else {
                tilDynamic.setError("Enter Valid Email");
                return false;
            }

        } else {
            tilDynamic.setError("Enter Email");
            return false;
        }
    }


    /**
     * custom method to print logs
     *
     * @param msg: to be displayed in logs
     * @return void
     */
    public static void printLog(String... msg) {
        String str = "";
        for (String i : msg) {
            str = str + "\n" + i;
        }
        if (true) {
            Log.d(VariableConstant.PARENT_FOLDER, str);
        }
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("hh:mm a", cal).toString();
        return date;
    }

    public static String getDateFormat(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("MMM dd, hh:mm a", cal).toString();
        return date;
    }

    public static void printLog(String msg) {
        Log.i(VariableConstant.PARENT_FOLDER, msg);
    }

    /**
     * custom method for write the date
     *
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    /**
     * custom method to conver the image to circle shape
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getCircleCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }


    /**********************************************************************************************/
    /**
     * <p>this method is for toast message ,
     * which has blue background</p>
     *
     * @param msg
     */
    public static void BlueToast(Context context, String msg) {
        Typeface ClanaproNarrMedium = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrMedium.otf");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View toastRoot = inflater.inflate(R.layout.toast_msg_back, null);
        TextView tv_toast = (TextView) toastRoot.findViewById(R.id.tv_toast);
        tv_toast.setTypeface(ClanaproNarrMedium);
        /*tv_toast.setWidth(300);
        tv_toast.setHeight(100);*/

        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        toast.show();
        tv_toast.setText(msg);
        toast.setDuration(Toast.LENGTH_LONG);
    }
    /***********************************************************Email Validation*******************/
    /**
     * @param email
     * @return
     */
    public static boolean validateEmail(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    /**********************************************************************************************/

    public static double[] getLocation(Context context) {
        double[] latLng = new double[2];
        LocationManager mLocationManager;
        Location location = null;
        try {
            mLocationManager = (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            latLng[0] = 0.0;
            latLng[1] = 0.0;

            if (isGPSEnabled) {
                Log.d("Utility", "Gps Enabled");
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return latLng;
                }

                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latLng[0] = location.getLatitude();
                    latLng[1] = location.getLongitude();
                }
                Log.i("Utility", "Gps 1  latLng[0]: " + latLng[0] + "  latLng[1]: " + latLng[1]);

                if (latLng[0] == 0.0) {
                    List<String> providers = mLocationManager.getProviders(true);
                    for (int i = providers.size() - 1; i >= 0; i--) {
                        location = mLocationManager.getLastKnownLocation(providers.get(i));
                        if (location != null) break;
                    }

                    if (location != null) {
                        latLng[0] = location.getLatitude();
                        latLng[1] = location.getLongitude();
                    }
                    Log.i("Utility", "Gps 2  latLng[0]: " + latLng[0] + "  latLng[1]: " + latLng[1]);
                }
            }
            if (latLng[0] == 0.0) {
                Log.d("Utility", "Network Enabled");
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latLng[0] = location.getLatitude();
                    latLng[1] = location.getLongitude();
                }
                Log.i("Utility", "NetWork  latLng[0]: " + latLng[0] + "  latLng[1]: " + latLng[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Utility", "getLocation: " + e);
        }
        Log.i("Utility", "latLng[0]: " + latLng[0] + "  latLng[1]: " + latLng[1]);
        return latLng;
    }

    public static String DateFormatChange(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "MMM d. h.mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**********************************************************************************************/
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public static void ShowSoftKeyboard(Activity activity,View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                view.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }


    public static String DateFormatChange(String time, int from) {
        Utility.printLog("Time and date : input :" + time);
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern;
        switch (from) {
            case 1:
                outputPattern = "EEEE, d MMMM , h.mm a";
                break;
            default:
                outputPattern = "MMM d, yyyy h:mm a";

        }
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Utility.printLog("Time and date : output :" + str);
        return str;
    }


    public static AlertDialog DisplayPromptForEnablingGPS(final Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Do you want open GPS setting?";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        return builder.create();
    }

    public static void MakePhoneCall(final String phoneNo, final Context mcontext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle(phoneNo);

        builder.setPositiveButton(R.string.cancel/*"No"*/,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        builder.setNegativeButton("Call",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mcontext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo)));
                    }
                });

        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    static public boolean setPreference(Context c, String value, String key) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    static public String getPreference(Context c, String key) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        settings = c.getSharedPreferences(PREFS_NAME, 0);
        String value = settings.getString(key, "");
        return value;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean isMyServiceRunning(Class<?> serviceClass, Context activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static Typeface getFontRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrMedium.otf");
    }

    public static Typeface getFontBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-WideMedium_0.otf");
    }

    public static Typeface getFontLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Lato_Light.ttf");
    }

    public static String getFormattedPrice(String tempPrice) {
        String price = "";
        Double dPrice = 0.00;
        if (tempPrice != null && !"".equals(tempPrice) && !"0".equals(tempPrice) && !"00".equals(tempPrice)
                && !"0.00".equals(tempPrice) && !"0.0".equals(tempPrice) && !".00".equals(tempPrice)) {
            dPrice = Double.parseDouble(tempPrice);
        }

        NumberFormat nf_out = NumberFormat.getInstance(Locale.US);
        nf_out.setMaximumFractionDigits(2);
        nf_out.setMinimumFractionDigits(2);
        nf_out.setGroupingUsed(false);
        price = nf_out.format(dPrice);

        return price;
    }


////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String formatDate(String fdate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

        try {
            Date date = format1.parse(fdate);
            String dateString = format.format(date);


            return dateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDay(long time) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("MM-dd", cal).toString();
        return date;
    }

    //Friday, 13 April, 11:30 Am
    public static String formatDateWeek(String fdate) {
        SimpleDateFormat format = new SimpleDateFormat("EEEE,dd MMM, hh:mm a", Locale.US);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

        try {
            Date date = format1.parse(fdate);
            String dateString = format.format(date);


            return dateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDurationString(int seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        int hours = (int) (TimeUnit.SECONDS.toHours(seconds) - (day * 24));
        int minute = (int) (TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60));
        int second = (int) (TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60));
        //Utility.printLog(TAG+"Duration "+hours+" : "+minute);

        String timer = String.format("%02d", hours) + " : " + String.format("%02d", minute) + " : " + String.format("%02d", second);

        return timer;
    }


    /**
     * <h1>getDurationString</h1>
     * <p>format of timer set</p>
     * @param seconds : the input in seconds for convert the time format
     * @return :time format
     */
    public static String getDurationString_(int seconds)
    {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        if(hours!=0){
            return twoDigitString(hours) + "hr : " + twoDigitString(minutes) + "min : " + twoDigitString(seconds)+"sec";
        }else if(minutes!=0)
        {
            return twoDigitString(minutes) + "min : " + twoDigitString(seconds)+"sec";
        }
        else {
            return twoDigitString(seconds) + "sec";
        }
    }

    /**
     * <h1>twoDigitString</h1>
     * <p>number format for keep 2 digit</p>
     * @param number : number
     * @return : 2 digit number
     */
    public static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }
        return String.valueOf(number);
    }



    public static String getCounrtyCode(Context context) {
        /*double[] latlng=getLocation(context);
        String code=getAddress(latlng[0],latlng[1],context);*/
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String CountryID = telephonyManager.getNetworkCountryIso().toUpperCase();

        return CountryID;
    }

    public static String getAddress(double lat, double lng, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            if (addresses.size() > 0) {
                Address obj = addresses.get(0);
                String add = obj.getCountryCode();
                Utility.printLog("Country code " + " Address" + add);
                return add;
            }


        } catch (IOException e) {
            Log.v("Country code", "Address crash");
            e.printStackTrace();
        }
        return "";
    }

    public static void mShowMessage(String title, String msg, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, 5);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(activity.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        builder.show();
    }

    public static void mShowMessage(String title, String msg, Activity activity, final AlertDialogCallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, 5);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(activity.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callBack.onOkPressed();
                dialogInterface.dismiss();

            }
        });


        builder.show();
    }

    public static String formatPrice(String amount) {
        float amt=0.0f;
        if(amount!=null && !amount.isEmpty()){
            amt= (float) Double.parseDouble(amount);
        }
        return String.format(Locale.US,"%.2f",amt);
    }


    public interface AlertDialogCallBack {
        void onOkPressed();

        void onCancelPressed();
    }

    /*wallet*/

    /**
     * <h2>sessionExpire</h2>
     * <p>
     * This method is used when our current Session got expired, so we instructs user to again do LoginActivity.
     * </p>
     *
     * @param context: calling activity reference
     */
    public static void sessionExpire(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        sessionManager.setIsLogin(false);
        sessionManager.setImageUrl("");
        sessionManager.setLastCard("");
        sessionManager.setLastCardNumber("");
        sessionManager.setCardType("");
        sessionManager.setLastCardImage("");
        Intent intent = new Intent(context, SplashScreen.class);

        ArrayList<String> pushTopics = sessionManager.getPushTopics();

        if (pushTopics != null && pushTopics.size() > 0) {
            for (String pushTopic : pushTopics) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/" + pushTopic);
            }
        }
        //FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/" + );
//        DataBaseHelper db = new DataBaseHelper(context);
//        db.clearDb();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * <h2>hideSoftKeyBoard</h2>
     * <p>
     * This method is used for hiding the soft keyboard.
     * </p>
     *
     * @param v view instance.
     */
    public static void hideSoftKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * <h2>showAlert</h2>
     * <p>
     * This method is used for showing the alert.
     * </P>
     *
     * @param msg:     message, that we need to show.
     * @param context: calling activity reference
     */
    public static void showAlert(String msg, Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle(context.getString(R.string.note));
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //closing the application
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    //====================================================================

    /**
     * <h2>checkCardType</h2>
     * <p>
     * method to check the card type and return
     * card type corresponding image drawable id if card type found else 0
     * </p>
     *
     * @param type: card type name
     * @return card type image drawable id if card type found else 0
     */
    @SuppressLint("PrivateResource")
    public static int checkCardType(String type) {
        if (type.equalsIgnoreCase("visa")) {
            return R.drawable.ic_visa;
        } else if (type.equalsIgnoreCase("MasterCard")) {
            return R.drawable.ic_mastercard;
        } else if (type.equalsIgnoreCase("American Express")) {
            return R.drawable.ic_amex;
        } else if (type.equalsIgnoreCase("Discover")) {
            return R.drawable.ic_discover;
        } else if (type.equalsIgnoreCase("Diners Club")) {
            return R.drawable.ic_diners;
        } else if (type.equalsIgnoreCase("JCB")) {
            return R.drawable.ic_jcb;
        }
        return 0;
    }

    public static void subscribeOrUnsubscribeTopics(JSONArray topics, boolean subscribe) {

        try{
            Utility.printLog("Pushtopics: "+topics.toString());
            if(subscribe){
                for (int index = 0; index < topics.length(); index++) {
                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/" + topics.get(index));
                }
            }else {
                for (int index = 0; index < topics.length(); index++) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/" + topics.get(index));
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }


        System.out.println();
    }


    /**
     * <h2>phoneNumberLengthValidation</h2>
     This method is used to validate the length of the phone number
     @param countryCode country code
     @return returns true if valid else false
     **/
    public static boolean phoneNumberLengthValidation(String mobileNumber, String countryCode)
    {
        String phoneNumberE164Format = countryCode.concat(mobileNumber);
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        try {
            Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(phoneNumberE164Format, null);
            boolean isValid = phoneUtil.isValidNumber(phoneNumberProto); // returns true if valid
            Utility.printLog("phoneNumberLengthValidation"+" is valid "+isValid);
            return isValid;
        }catch (NumberParseException e){
            Utility.printLog("phoneNumberLengthValidation"+" NumberParseException "+e);
        }
        return false;
    }


    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM yyyy, h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * changeLanguageConfig      used to set the language configuration
     * @param code language code
     **/
    public static int changeLanguageConfig(String code,Context context )
    {
        return 0;
    }


    /**
     * <h1>RtlConversion</h1>
     * <p>Check the RTL depends on the language code</p>
     * @param activity : activity
     * @param lang : Language Code
     */
    public static void RtlConversion(Activity activity, String lang)
    {
        //Checking the language code is empty, if not empty and whether it is "ar"(Arabic), then assign as RTL(Right to left)
        if(lang!=null && lang.equals("ar"))
        {
            setLocale(lang,activity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
        //if the language is null or arabic
        else
        {
            //if the language is null initiate with english and assign as LTR(left to right)
            if(lang==null)
                lang="en";
            setLocale(lang,activity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }

    }

    /**
     * <h1>setLocale</h1>
     * <p>set Locale for the language</p>
     * @param lang : language code
     * @param context : activity or context of Activity
     */
    private static void setLocale(String lang, Context context) {
        try {
            Utility.printLog("Locale lang " + lang);
            Locale myLocale = new Locale(lang);
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        } catch (Exception e) {
            Utility.printLog("select_language inside Exception" + e.toString());
        }
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(Double.parseDouble(amount));
    }

}
