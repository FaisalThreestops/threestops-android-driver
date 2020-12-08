package com.driver.threestops.managers.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.driver.threestops.utility.Utility;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

/**
 * <h2>LocationProvider</h2>
 * This method is used to provide the current location
 *
 * @author 3Embed
 * @since on 11-01-2018.
 */
public class LocationProvider {
    private static final String TAG = "LocationProvider";
    private Context mContext;
    private Observable<Location> locationUpdatesObservable;
    private ReactiveLocationProvider locationProvider;
    private LocationRequest locationRequest;
    private Disposable updatableLocationDisposable;
    private RxLocationObserver rxLocationObserver;

    /**
     * <h2>LocationProvider</h2>
     *
     * @param context            context of the application
     * @param rxLocationObserver observer to be published
     */
    public LocationProvider(Context context, RxLocationObserver rxLocationObserver) {
        mContext = context;
        locationProvider = new ReactiveLocationProvider(context);
        this.rxLocationObserver = rxLocationObserver;
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(30 * 100)        // 1 seconds, in milliseconds
                .setFastestInterval(1000);
    }

    /**
     * <h2>startLocation</h2>
     * This method is used to start the location update
     */
    public void startLocation(LocationCallBack locationCallBack) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationUpdatesObservable = locationProvider
                .checkLocationSettings(new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest)
                        .setAlwaysShow(true)
                        .build())
                .doOnNext(locationSettingsResult ->
                {
                    Status status = locationSettingsResult.getStatus();
                    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        Utility.printLog(TAG + "opening startResolutionForResult.");
                        locationCallBack.onLocationServiceDisabled(status);
                    }
                })
                .flatMap(locationSettingsResult ->
                        locationProvider.getUpdatedLocation(locationRequest))
                .observeOn(AndroidSchedulers.mainThread());

        subscribeLocationChange();
    }

    /**
     * <h2>subscribeLocationChange</h2>
     * This method is used to subscribe for the location change
     */
    private void subscribeLocationChange() {
        locationUpdatesObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Location>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        updatableLocationDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Location location) {
                        Utility.printLog("location Observed: " + location.getLatitude() + ", " + location.getLongitude());
                        if (!updatableLocationDisposable.isDisposed())
                            rxLocationObserver.publishData(location);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Utility.printLog(TAG + " onError location Observed  " + e);
                    }

                    @Override
                    public void onComplete() {
                        Utility.printLog(TAG + " onComplete location Observed  ");
                    }
                });
    }

    /**
     * <h2>stopLocationUpdates</h2>
     * This method is used to stop the location updates
     */
    public void stopLocationUpdates() {
        Utility.printLog("Disposing Location Update");
        updatableLocationDisposable.dispose();
    }

    /**
     * <h2>isLocationUpdating</h2>
     * This method is return boolean flag based on location update status
     * @return true if location update is running
     */
    public boolean isLocationUpdating() {
        return updatableLocationDisposable != null && !updatableLocationDisposable.isDisposed();
    }
}
