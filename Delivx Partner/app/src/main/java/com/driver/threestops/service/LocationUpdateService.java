package com.driver.threestops.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.driver.threestops.RxObservers.RXDistanceChangeObserver;
import com.driver.threestops.RxObservers.RXMqttMessageObserver;
import com.driver.threestops.app.main.MainActivity;
import com.driver.threestops.app.MyApplication;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.utility.TextUtil;
import com.driver.Threestops.R;
import com.driver.threestops.networking.DispatcherService;
import com.driver.threestops.networking.NetworkService;
import com.driver.threestops.utility.AppConstants;
import com.driver.threestops.utility.DistanceChangeListner;
import com.driver.threestops.utility.Utility;
import com.driver.threestops.utility.VariableConstant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LocationUpdateService
        extends DaggerService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String METER = "meters";
    private static final String KILOMETER = "Kilometers";
    private static final String NAUTICAL_MILES = "nauticalMiles";
    private static DistanceChangeListner distanceChangeListner;
    public double distancespd;
    private int battery_level;
    private GoogleApiClient mGoogleApiClient;
    private String version;
    private LocationRequest mLocationRequest;
    private String TAG = LocationUpdateService.class.getSimpleName();
    private double prevLat, prevLng, strayLat = -1.0, strayLng = -1.0;
    private double distanceKm;
    private String strDouble;
    private Timer myTimer_publish;
    private TimerTask myTimerTask_publish;
    private Location prevLocation = null;
    private float bearing;
    private double minDistance=0.0;
    private boolean updted = true;
    private CouchDbHandler couchDBHandle;
    private int mqttCount = 0;
    private double prevLatTimer=0.0,prevLongTimer=0.0;
    private static int counter;

    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject
    NetworkService networkService;

    @Inject
    DispatcherService dispatcherService;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("com.app.driverapp.internetStatus")) {

                Bundle bucket = intent.getExtras();
                String status1 = bucket.getString("STATUS");
                Utility.printLog(TAG + " " + status1);

                if ("1".equals(status1)) {
                    if (!updted) {
                        updateLocationLogs();
                    }
                } else {
                    updted = false;
                }

            } else {
                battery_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            }

        }
    };


    public static double distance(double lat1, double lng1, double lat2, double lng2, String unit) {
        double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        if (KILOMETER.equals(unit))                // Kilometer
        {
            dist = dist * 1.609344;
        } else if (NAUTICAL_MILES.equals(unit))            // Nautical Miles
        {
            dist = dist * 0.8684;
        } else if (METER.equals(unit))            // meter
        {
            dist = dist * 1609.344;
        }

        return dist;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        iFilter.addAction("com.app.driverapp.internetStatus");
        registerReceiver(mBroadcastReceiver, iFilter);

        MyApplication controller = (MyApplication) getApplicationContext();
        couchDBHandle = controller.getDBHandler();

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = info.versionName;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(2000); // Update location every 2 second
        mLocationRequest.setSmallestDisplacement(20);


        try {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, currentLoc -> {
                        double currentLat = currentLoc.getLatitude();
                        double currentLng = currentLoc.getLongitude();

                        preferenceHelperDataSource.setDriverCurrentLat("" + currentLat);
                        preferenceHelperDataSource.setDriverCurrentLongi("" + currentLng);

                        if (prevLocation == null) {
                            prevLocation = currentLoc;
                        } else {
                            bearing = prevLocation.bearingTo(currentLoc);
                        }

                        mCalculateRouteArray();

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try
        {

            if (intent!=null && !TextUtil.isEmpty(intent.getAction()) &&intent.getAction().equals(AppConstants.ACTION.STARTFOREGROUND_ACTION))
            {
                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.setAction(AppConstants.ACTION.MAIN_ACTION);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_RECEIVER_FOREGROUND);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                        notificationIntent, 0);

                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_launcher);


                String CHANNEL_ID = getString(R.string.app_name_withoutSpace);;// The id of the channel.
                CharSequence name = getString(R.string.app_name);
                Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setTicker("")
                        .setContentText("Running...")
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setChannelId(CHANNEL_ID)
                        .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setOngoing(true).build();

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    mNotificationManager.createNotificationChannel(mChannel);
                }



                mGoogleApiClient.connect();
                startPublishingWithTimer();

                startForeground(AppConstants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                        notification);


            }
            else if (intent!=null && intent.getAction().equals(
                    AppConstants.ACTION.STOPFOREGROUND_ACTION)) {
                stopForeground(true);
                stopSelf();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            Utility.printLog("Crashed in forground service");
        }

        return START_STICKY;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void mCalculateRouteArray() {

        if (prevLat == 0.0 || prevLng == 0.0) {
            prevLat = preferenceHelperDataSource.getDriverCurrentLat();
            prevLng = preferenceHelperDataSource.getDriverCurrentLongi();
        }

        double curLat = preferenceHelperDataSource.getDriverCurrentLat();
        double curLong = preferenceHelperDataSource.getDriverCurrentLongi();
        double dis = distance(prevLat, prevLng, curLat, curLong, METER);
        double distanceInMtr = 0.0;

        double disFromStaryPts = -1.0;

        if (strayLat != -1.0 && strayLng != -1.0) {
            disFromStaryPts = distance(strayLat, strayLng, curLat, curLong, METER);
        }


        if (((dis >= preferenceHelperDataSource.getMinDistForRouteArray()) && (dis <=  preferenceHelperDataSource.getMaxDistForRouteArray()))
                || (disFromStaryPts != -1.0 && ((disFromStaryPts >= preferenceHelperDataSource.getMinDistForRouteArray()) && (disFromStaryPts <= preferenceHelperDataSource.getMaxDistForRouteArray())))) {

            strayLat = prevLat = preferenceHelperDataSource.getDriverCurrentLat();
            strayLng = prevLng = preferenceHelperDataSource.getDriverCurrentLongi();

            distanceInMtr = (dis > disFromStaryPts) ? dis : disFromStaryPts;


            try {
                JSONArray jsonArray = new JSONArray(preferenceHelperDataSource.getBookings());
                Utility.printLog(TAG + " jsonnarray " + jsonArray.toString());
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);

                        double distance_conv_unit=object.getDouble("dist_conv_unit");
                        Log.d(TAG, "Myservice distance convertion unit " + distance_conv_unit);
                        if (object.getInt("status") == 10 || object.getInt("status") == 12)
                        {
                            distancespd = (object.getDouble("distance") / distance_conv_unit);
                            distancespd += distanceInMtr;
                            Log.d(TAG, "Myservice Total distance" + distancespd);
                            distanceKm = distancespd * distance_conv_unit;
                            strDouble = String.format(Locale.US, "%.2f", distanceKm);
                            object.put("distance",strDouble);
                        }
                    }
                }
                preferenceHelperDataSource.setBookings(jsonArray.toString());
                Utility.printLog("MyService bookings " + preferenceHelperDataSource.getBookings());
                RXDistanceChangeObserver.getInstance().emit(jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (dis > preferenceHelperDataSource.getMaxDistForRouteArray() &&
                    disFromStaryPts > preferenceHelperDataSource.getMaxDistForRouteArray()) {
                strayLat = curLat;
                strayLng = curLong;
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        if(myTimerTask_publish!=null){
            myTimerTask_publish.cancel();
            myTimer_publish.cancel();
        }


        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }

    }

    /********************************************************************************************/

    private void startPublishingWithTimer() {
        try {

            if (myTimer_publish != null) {
                Log.d(TAG, "Timer already started");
                return;
            }
            myTimer_publish = new Timer();

            myTimerTask_publish = new TimerTask() {
                @Override
                public void run() {
                    if (Utility.isNetworkAvailable(getApplicationContext())) {

                        updateLocationLogs();

                        Utility.printLog("mqtt connection iss : " + ((MyApplication) getApplication()).isMQTTConnected());
                        if (!((MyApplication) getApplication()).isMQTTConnected())
                            mqttCount++;
                        else
                            mqttCount = 0;

                        if (!((MyApplication) getApplication()).isMQTTConnected() && mqttCount > 6) {
                            mqttCount = 0;
                            if (preferenceHelperDataSource.getDriverID() != null) {
                                ((MyApplication) getApplication()).unSubscribeMqtt(preferenceHelperDataSource.getDriverID());
                                Utility.printLog("testing unsubScribed Mqtt Topic :   " + preferenceHelperDataSource.getDriverID());
                            }


                            if (preferenceHelperDataSource.getServiceZoneList().getServiceZones().size() > 0)
                                for (int i = 0; i < preferenceHelperDataSource.getServiceZoneList().getServiceZones().size(); i++) {
                                    String topic = "onlineDrivers/".concat(preferenceHelperDataSource.getCityId().concat("/").concat(preferenceHelperDataSource.getServiceZoneList().getServiceZones().get(i)));
                                    Utility.printLog("subscribed Mqtt Zone topic :  " + topic);
                                    ((MyApplication) getApplication()).unSubscribeMqtt(topic);

                                }

                            MyApplication.getInstance().connectMQTT();
                        }

                        if (prevLatTimer == 0.0 || prevLongTimer == 0.0) {
                            prevLatTimer = preferenceHelperDataSource.getDriverCurrentLat();
                            prevLongTimer = preferenceHelperDataSource.getDriverCurrentLongi();
                        }


                        if (counter >= preferenceHelperDataSource.getMinDistForRouteArray()) {
                            counter = 0;
                            if (distance(prevLatTimer, prevLongTimer, preferenceHelperDataSource.getDriverCurrentLat(),
                                    preferenceHelperDataSource.getDriverCurrentLongi(), METER) >= preferenceHelperDataSource.getMinDistForRouteArray()) {
                                prevLatTimer = preferenceHelperDataSource.getDriverCurrentLat();
                                prevLongTimer = preferenceHelperDataSource.getDriverCurrentLongi();
                                publishLocation(preferenceHelperDataSource.getDriverCurrentLat(), preferenceHelperDataSource.getDriverCurrentLongi(), 1);
                                updateLocationMQTT(preferenceHelperDataSource.getDriverCurrentLat(), preferenceHelperDataSource.getDriverCurrentLongi(), 1);

                            } else {
                                updateLocationMQTT(preferenceHelperDataSource.getDriverCurrentLat(), preferenceHelperDataSource.getDriverCurrentLongi(), 0);
                                publishLocation(preferenceHelperDataSource.getDriverCurrentLat(), preferenceHelperDataSource.getDriverCurrentLongi(), 0);
                            }

                        } else {
                            counter++;
                        }

                    } else if (!preferenceHelperDataSource.getBookings().isEmpty()) {
                        couchDBHandle.updateDocument(preferenceHelperDataSource.getDriverCurrentLat(),
                                preferenceHelperDataSource.getDriverCurrentLongi());


                    }

                }

            };
            Log.d(TAG, "myTimer_publish interval " + preferenceHelperDataSource.getTripStartedInterval());
            myTimer_publish.schedule(myTimerTask_publish, 0, /*Long.parseLong(preferenceHelperDataSource.getPresenceInterval())**/1000/**(long) preferenceHelperDataSource.getTripStartedInterval()*/);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**********************************************************************************************/
    public void publishLocation(double latitude, double longitude, final int transit) {

        if (preferenceHelperDataSource.isLoggedIn()) {
            Utility.printLog("LocationIssue : Mqtt Location Update : true");
            String locationChk = "";
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            Intent gps_inIntent = new Intent("com.driver.gps");
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            if (gps_enabled) {
                locationChk = "1";
                gps_inIntent.putExtra("action", "0");
            } else {
                locationChk = "0";
                gps_inIntent.putExtra("action", "1");
            }
            sendBroadcast(gps_inIntent);
            Utility.printLog("gps enabled or not " + gps_enabled);
            final JSONArray jsonArray;
            String pubnubStr = "";
            try {
                if (!preferenceHelperDataSource.getBookings().isEmpty()) {
                    jsonArray = new JSONArray(preferenceHelperDataSource.getBookings());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (pubnubStr.isEmpty()) {
                            pubnubStr = jsonObject.getString("bid") + "|" + jsonObject.getString("custChn") + "|" + jsonObject.getString("status");
                        } else {
                            pubnubStr = pubnubStr + ", " + jsonObject.getString("bid") + "|" + jsonObject.getString("custChn") + "|" + jsonObject.getString("status");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Observable<Response<ResponseBody>> location=dispatcherService.location(
                    /*preferenceHelperDataSource.getLanguage()*/"0",
            ((MyApplication) getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()),
                    /*preferenceHelperDataSource.getToken(),*/
                    latitude+"",
                    longitude+"",
                    null,
                    pubnubStr,
                    version,
                    battery_level+"",
                    locationChk,
                    String.valueOf(VariableConstant.DEVICE_TYPE),
                    bearing+"",
                    transit+"",
                    0+"",
                    preferenceHelperDataSource.getMasterStatus()+""
                    );
            location.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }
                        @Override
                        public void onNext(Response<ResponseBody> value) {
                            try {
                                JSONObject jsonObject;
                                Utility.printLog("Loc Update Code: " + value.code());
                                if(value.code()==200)
                                {
                                    jsonObject=new JSONObject(value.body().string());
                                    jsonObject.put("code",value.code());
                                }else
                                {
                                    jsonObject=new JSONObject(value.errorBody().string());
                                    jsonObject.put("code",value.code());
                                }

                                if(jsonObject.getInt("code")==498){
                                    RXMqttMessageObserver.getInstance().emit(jsonObject);
                                }

                                Utility.printLog("location : "+jsonObject.toString()+" transit: "+transit);

                            }catch (JSONException e)
                            {
                                Utility.printLog("location : Catch :"+e.getMessage());
                            }catch (IOException e)
                            {
                                Utility.printLog("location : Catch :"+e.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: ");
                        }
                    });



        } else
            Log.d(TAG, "MyService" + "isLogin  " + false);
    }


    public void updateLocationLogs() {

        JSONArray list = couchDBHandle.retriveDocument();
        JSONObject joLatLngBody = new JSONObject();
        try {
            joLatLngBody.put("latLong",list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        LatLngBody latLngBody = gson.fromJson(joLatLngBody.toString(),LatLngBody.class);
        Utility.printLog(TAG + "retriveDocument body:  " +joLatLngBody.toString() );

        if(latLngBody.getLatLong().length>0) {
            Utility.printLog(TAG + "retriveDocument body:  "+latLngBody.getLatLong().length+"\n" +joLatLngBody.toString() );
            Observable<Response<ResponseBody>> locationLogs = networkService.locationLogs(
                    ((MyApplication) getApplication()).getAuthToken(preferenceHelperDataSource.getDriverID()),
                    preferenceHelperDataSource.getLanguage(),
                    latLngBody);
            locationLogs.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<Response<ResponseBody>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Response<ResponseBody> value) {
                    try {
                        JSONObject jsonObject;
                        if (value.code() == 200) {
                            jsonObject = new JSONObject(value.body().string());
                            updted = true;
                            couchDBHandle.deleteDocument();
                        } else {
                            jsonObject = new JSONObject(value.errorBody().string());
                        }

                        Utility.printLog("locationLogs : " + jsonObject.toString());

                    } catch (Exception e) {
                        Utility.printLog("locationLogs : Catch :" + e.getMessage());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: ");
                }
            });
        }


    }


    private void updateLocationMQTT(Double latitude,Double longitude, int transit){

        JSONObject reqObject = new JSONObject();

        Utility.printLog("Location UpdateookingStatus()");

        try {
            reqObject.put("longitude", longitude);
            reqObject.put("latitude", latitude);
            reqObject.put("locationHeading", bearing + "");
            reqObject.put("driverId", preferenceHelperDataSource.getDriverID());
            reqObject.put("transit",transit);
            reqObject.put("status",preferenceHelperDataSource.getMasterStatus());
            reqObject.put("batteryPer",battery_level);
            reqObject.put("deviceType",String.valueOf(VariableConstant.DEVICE_TYPE));
            reqObject.put("appVersion",version);


            if(((MyApplication)getApplication()).isMQTTConnected())
            {
                ((MyApplication)getApplication()).publishMqtt(reqObject);

            }else {

                if(preferenceHelperDataSource.getServiceZoneList().getServiceZones().size()>0)
                    for(int i=0;i<preferenceHelperDataSource.getServiceZoneList().getServiceZones().size();i++){
                        String topic = "onlineDrivers/".concat(preferenceHelperDataSource.getCityId().concat("/").concat(preferenceHelperDataSource.getServiceZoneList().getServiceZones().get(i)));
                        Utility.printLog("unsubScribed Mqtt Zone topic :  "+topic);

                        ((MyApplication)getApplication()).unSubscribeMqtt(topic);
                    }

                MyApplication.getInstance().connectMQTT();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
