package com.delivx.app.bookingRide;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delivx.data.source.PreferenceHelperDataSource;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.delivx.app.storeDetails.StorePickUpDetails;
import com.delivx.app.storePickUp.StorePickUp;
import com.driver.delivx.R;
import com.delivx.mqttChat.ChattingActivity;
import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.AppConstants;
import com.delivx.utility.FontUtils;
import com.delivx.utility.LocationUtil;
import com.delivx.utility.PicassoMarker;
import com.delivx.utility.Slider;
import com.delivx.utility.Utility;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class BookingRide extends DaggerAppCompatActivity implements OnMapReadyCallback,BookingRideContract.ViewOperations, LocationUtil.GetLocationListener {


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tvAddress) TextView tvAddress;
    @BindView(R.id.tvCall) TextView tvCall;
    @BindView(R.id.tvJobDetails) TextView tvJobDetails;
    @BindView(R.id.tv_distance_value) TextView tv_distance_value;
    @BindView(R.id.tv_timer_value) TextView tv_timer_value;
    @BindView(R.id.tv_distance) TextView tv_distance;
    @BindView(R.id.tv_timer) TextView tv_timer;
    @BindView(R.id.tv_status_text) TextView tv_status_text;
    @BindView(R.id.myseek) Slider myseek;
    @BindView(R.id.ivDirection) ImageView ivDirection;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.ivMyLocation) ImageView ivMyLocation;
    @BindView(R.id.ivAddressDot) ImageView ivAddressDot;
    @BindView(R.id.iv_search) ImageView iv_search;

    @Inject BookingRideContract.PresenterOperations presenter;
    @Inject FontUtils fontUtils;

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private PicassoMarker marker;
    private Location mCurrentLoc,mPreviousLoc;
    private Marker customer_marker;
    private LocationUtil locationUtilObj;
    private boolean first=false;
    private int index;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.RtlConversion(this,presenter.getlanguageCode());
        setContentView(R.layout.activity_booking_ride);
        ButterKnife.bind(this);
        presenter.getBundleData(getIntent().getExtras());
        index = getIntent().getIntExtra("index", 0);
        initLayout();
        presenter.setActionBar();
        presenter.setActionBarTitle();

    }

    /**
     * <h2>initLayout</h2>
     * <p>initializing the views(font and style)</p>
     */
    public void initLayout(){

        Typeface font= fontUtils.titaliumRegular();
        Typeface fontBold=fontUtils.titaliumSemiBold();

        tv_title.setTypeface(fontBold);
        tvCall.setTypeface(fontBold);
        tvJobDetails.setTypeface(fontBold);
        tv_status_text.setTypeface(fontBold);
        tv_distance_value.setTypeface(fontBold);
        tv_timer_value.setTypeface(fontBold);
        tvAddress.setTypeface(font);
        tv_distance.setTypeface(font);
        tv_timer.setTypeface(font);
        locationUtilObj = new LocationUtil(BookingRide.this, this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Rect rect = new Rect();
        myseek.getHitRect(rect);
        rect.right += 100;   // increase left hit area
        myseek.setTouchDelegate( new TouchDelegate( rect , myseek));

        myseek.setSliderProgressCallback(new Slider.SliderProgressCallback() {
            @Override
            public void onSliderProgressChanged(int progress) {

                if (progress > 65) {
                    myseek.setProgress(100);
                    presenter.updateBookingStatus();
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;
        View mapView = mapFragment.getView();
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            Resources r = getResources();
            int marginPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, marginPixels, marginPixels);
        }
        if (ActivityCompat.checkSelfPermission(BookingRide.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BookingRide.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        presenter.getLatLong();

        googleMap.setMyLocationEnabled(false);

        presenter.getMarkers();
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

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.history_back_icon_off);
        }
        iv_search.setImageDrawable(getResources().getDrawable(R.drawable.ic_message_black_24dp));
        iv_search.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(String status) {

        if(status.equals(AppConstants.BookingStatus.JourneyStarted)){
            tv_title.setText(getResources().getString(R.string.on_the_way));
        }else
        {
            tv_title.setText(getResources().getString(R.string.on_the_way_to_pickup));
        }


    }

    @Override
    public void setViews(AssignedAppointments appointments){
        if(appointments.getOrderStatus().equals(AppConstants.BookingStatus.JourneyStarted)){
            String text = "<b>" + appointments.getCustomerName() + "</b> "+" : "+appointments.getDropAddress() ;
            tvAddress.setText(Html.fromHtml(text));
            tv_status_text.setText(getResources().getString(R.string.reached));
            ivAddressDot.setImageDrawable(getResources().getDrawable(R.drawable.history_dropoff_icon));

        }else
        {
            String text = "<b>" + appointments.getStoreName() + "</b> "+" : "+appointments.getPickUpAddress() ;
            tvAddress.setText(Html.fromHtml(text));
            ivAddressDot.setImageDrawable(getResources().getDrawable(R.drawable.history_pickup_icon));
        }

    }

    @Override
    public void setLatLong(double lat, double longi) {
        LatLng latLng = new LatLng(lat, longi);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
    }


    @Override
    public void setmarkers(LatLng current,LatLng customer ) {
        try {
            marker = new PicassoMarker(map.addMarker(new MarkerOptions().position(current).title("First Point")));
            Picasso.with(this).load(R.drawable.car_one).resize(50, 50).into(marker);
            customer_marker = map.addMarker(new MarkerOptions().position(customer));
            customer_marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.store_pickup_location_icon));

            setCarMarker(current);

        } catch (Exception e) {
            Log.e("Picasso", "caught an exception");
        }
    }

    @OnClick({R.id.tvCall,R.id.tvJobDetails,R.id.ivDirection,R.id.ivMyLocation,R.id.iv_search})
    public void onclick(View view){
        switch (view.getId()){
            //call option
            case R.id.tvCall:
                presenter.callCustomer();
                break;
                //job details
            case R.id.tvJobDetails:
                presenter.getJobdetails();
                break;
                //chat option in action bar
            case R.id.iv_search:
                presenter.openChat();
                break;
                //Direction option
            case R.id.ivDirection:
                presenter.getDirection();
                break;
                //my current location
            case R.id.ivMyLocation:
                presenter.getLatLong();
                break;
        }
    }

    @Override
    public void setCarMarker(final Location location)
    {
        mCurrentLoc=location;

        if(mPreviousLoc==null)
        {
            mPreviousLoc=location;
        }

        final float bearing = mPreviousLoc.bearingTo(mCurrentLoc);
        if(marker!=null)
        {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            Projection proj = map.getProjection();
            Point startPoint = proj.toScreenLocation(new LatLng(mPreviousLoc.getLatitude(),mPreviousLoc.getLongitude()));
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final long duration = 500;

            final Interpolator interpolator = new LinearInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t * mCurrentLoc.getLongitude() + (1 - t)
                            * startLatLng.longitude;
                    double lat = t * mCurrentLoc.getLatitude() + (1 - t)
                            * startLatLng.latitude;
                    marker.getmMarker().setPosition(new LatLng(lat,lng));
                    marker.getmMarker().setAnchor(0.5f, 0.5f);
                    marker.getmMarker().setRotation(bearing);
                    marker.getmMarker().setFlat(true);


                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }

        mPreviousLoc=mCurrentLoc;
    }

    @Override
    public void setCarMarker(LatLng latLng)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        map.getUiSettings().setZoomControlsEnabled(false);

        if(marker!=null)
            marker.getmMarker().setPosition(latLng);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void updateLocation(Location location) {
        if (map != null) {
            if(first){
                LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16.0f));
                first=false;
            }


            if (marker != null) {
                setCarMarker(location);
            }
        }
    }

    @Override
    public void location_Error(String error) {

    }

    @Override
    public void openGoogleMap(String uri) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    @Override
    public void openChatAct(AssignedAppointments appointments) {

        Intent intent=new Intent(this,ChattingActivity.class);
        intent.putExtra("BID",appointments.getBid());
        intent.putExtra("CUST_ID",appointments.getCustomerId());
        intent.putExtra("CUST_NAME",appointments.getCustomerName());
        startActivity(intent);
    }
    @Override
    public void onSuccess(AssignedAppointments appointments) {
        if (preferenceHelperDataSource.getDriverScheduleType() == 0) {
            Intent intent = new Intent(BookingRide.this, StorePickUp.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", appointments);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(BookingRide.this, StorePickUp.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", appointments);
            bundle.putInt("index", index);
            intent.putExtras(bundle);
            startActivity(intent);
            startActivityForResult(intent, 123);
        }
    }

    @Override
    public void onError(String message) {
        myseek.setProgress(0);
        Utility.mShowMessage(getResources().getString(R.string.message),message,this);
    }

    @Override
    public void openJobDetails(AssignedAppointments appointments) {
        Intent intent=new Intent(BookingRide.this, StorePickUpDetails.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",appointments);
        bundle.putString("from","JOB_DETAILS");
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void setDistanceText(String distance) {
        tv_distance_value.setText(distance);
    }

    @Override
    public void setTimerText(String s) {
        tv_timer_value.setText(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onActDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            switch (requestCode) {

                case 123:

                    if (resultCode == Activity.RESULT_CANCELED) {
                        setResult(Activity.RESULT_CANCELED, data);
                        finish();
                    }

                    break;
                default:
                    break;
            }
        }
    }
}
