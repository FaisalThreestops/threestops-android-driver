package com.driver.threestops.app.main.homeFrag;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.driver.Threestops.R;
import com.driver.threestops.adapter.CurrentUpcomingJobRVA;
import com.driver.threestops.adapter.SlotFlowJobRVA;
import com.driver.threestops.adapter.SortSlotStartTimeCmp;
import com.driver.threestops.dagger.ActivityScoped;
import com.driver.threestops.data.source.PreferenceHelperDataSource;
import com.driver.threestops.pojo.AssignedAppointments;
import com.driver.threestops.service.LocationUpdateService;
import com.driver.threestops.utility.AppConstants;
import com.driver.threestops.utility.FontUtils;
import com.driver.threestops.utility.PicassoMarker;
import com.driver.threestops.utility.Utility;
import com.driver.threestops.utility.VariableConstant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerFragment;

import static com.driver.threestops.utility.AppConstants.Offline_status;

@ActivityScoped
public class HomeFragment extends DaggerFragment implements HomeFragmentContract.View, OnMapReadyCallback, View.OnClickListener {

    private View rootView;
    private TextView tv_on_off_statas;
    private ImageView button_menu, button_back;
    private SupportMapFragment mapFragment;
    private GoogleMap map;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.ll_bookings)
    LinearLayout ll_bookings;

    @BindView(R.id.ll_frag_map)
    RelativeLayout ll_frag_map;

    @BindView(R.id.tv_no_network)
    TextView tv_no_network;

    @BindView(R.id.ivMyLocation)
    ImageView ivMyLocation;

    @BindView(R.id.rv_job_home)
    RecyclerView recyclerView;

    @BindView(R.id.llMarkerFilter)
    LinearLayout llMarkerFilter;

    @BindView(R.id.tvMarkerAll)
    TextView tvMarkerAll;

    @BindView(R.id.tvMarkerPickUp)
    TextView tvMarkerPickUp;

    @BindView(R.id.tvMarkerDelivery)
    TextView tvMarkerDelivery;

    @BindView(R.id.rl_slot_flow)
    RelativeLayout rl_slot_flow;

    @BindView(R.id.rvSlots)
    RecyclerView rvSlots;

    @Inject
    HomeFragmentContract.Presenter presenter;
    @Inject
    PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject
    FontUtils fontUtils;

    private boolean first = false;
    private PicassoMarker marker;
    private Location mCurrentLoc, mPreviousLoc;
    private ArrayList<AssignedAppointments> appointments = new ArrayList<>();
    private CurrentUpcomingJobRVA adapter;
    private SlotFlowJobRVA slotFlowJobRVA;
    private ArrayList<Marker> pickUp = new ArrayList<>();
    private ArrayList<Marker> delivery = new ArrayList<>();
    private AlertDialog alertDialog;
    private String driverType;
    private String driverScheduleType;
    private ArrayList<AssignedAppointments> orderDetails = new ArrayList<>();
    private ArrayList<AssignedAppointments> assignedTrips = new ArrayList<>();

    @Inject
    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        presenter.attachView(this);

        initLayoutId();

        return rootView;
    }

    /**
     * <h2>initLayoutId</h2>
     * <p>finding views by Id in content_main.xml and initialize the views</p>
     */
    public void initLayoutId() {

        tv_on_off_statas = getActivity().findViewById(R.id.tv_on_off_statas);
        button_menu = getActivity().findViewById(R.id.button_menu);
        button_back = getActivity().findViewById(R.id.button_back);
        Typeface fontBold = fontUtils.titaliumSemiBold();
        Typeface font = fontUtils.titaliumRegular();
        tv_on_off_statas.setTypeface(fontBold);
        tv_on_off_statas.setOnClickListener(this);
        button_back.setOnClickListener(this);
        tvMarkerAll.setTypeface(font);
        tvMarkerPickUp.setTypeface(font);
        tvMarkerDelivery.setTypeface(font);
        driverType = preferenceHelperDataSource.getDriverType();
        adapter = new CurrentUpcomingJobRVA(getActivity(), appointments, fontUtils, driverType);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frag_map);
        mapFragment.getMapAsync(this);

        slotFlowJobRVA = new SlotFlowJobRVA(getContext(), orderDetails, assignedTrips);
        rvSlots.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSlots.setAdapter(slotFlowJobRVA);
        slotFlowJobRVA.notifyDataSetChanged();
    }


    @Override
    public void networkError(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
//        rvSlots.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
//        rvSlots.setVisibility(View.VISIBLE);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        first = true;
        this.map = googleMap;
        View mapView = mapFragment.getView();
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            Resources r = getResources();
            int marginPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, marginPixels, marginPixels);
            map.getUiSettings().setMyLocationButtonEnabled(false);
        }
        setGoogleMap();
    }

    /**
     * <h2>setGoogleMap</h2>
     * <p>implementing the google map</p>
     */
    public void setGoogleMap() {
        Utility.printLog("setGoogleMap context " + getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            map.setMyLocationEnabled(true);
            map.clear();
            LatLng latlng = new LatLng(preferenceHelperDataSource.getDriverCurrentLat(), preferenceHelperDataSource.getDriverCurrentLongi());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16.0f));
            marker = new PicassoMarker(map.addMarker(new MarkerOptions().position(latlng).title("First Point")));
            ivMyLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LatLng latlng = new LatLng(preferenceHelperDataSource.getDriverCurrentLat(), preferenceHelperDataSource.getDriverCurrentLongi());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16.0f));
                }
            });
        } catch (Exception e) {
            Log.e("Picasso", "caught an exception");
        }
    }


    /**
     * <h1>setCarMarker</h1>
     * <p>set the marker in map</p>
     *
     * @param location latitude and longitude
     */
    private void setCarMarker(final Location location) {
        mCurrentLoc = location;

        if (mPreviousLoc == null) {
            mPreviousLoc = location;
        }

        final float bearing = mPreviousLoc.bearingTo(mCurrentLoc);
        if (marker != null) {

            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            Projection proj = map.getProjection();
            Point startPoint = proj.toScreenLocation(new LatLng(mPreviousLoc.getLatitude(), mPreviousLoc.getLongitude()));
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

                    if (marker != null) {
                        marker.getmMarker().setPosition(new LatLng(lat, lng));
                        marker.getmMarker().setAnchor(0.5f, 0.5f);
                        marker.getmMarker().setRotation(bearing);
                        marker.getmMarker().setFlat(true);
                    }

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }

        mPreviousLoc = mCurrentLoc;
    }

    @Override
    public void updateLocation(Location location) {
        if (map != null) {
            if (first) {
                LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16.0f));
                first = false;
            }
            if (marker != null) {
                setCarMarker(location);
            }
            presenter.updateLocation(location);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        VariableConstant.FORGROUND_LOCK = false;
        Utility.startPowerSaverIntent(getContext());
        presenter.startLocationUpdate();
        presenter.getDriverScheduleType();
        presenter.checkBookingPopUp();
        presenter.getAssignedTRips();
    }

    @Override
    public void onPause() {
        super.onPause();
        VariableConstant.FORGROUND_LOCK = true;
        presenter.stopLocationUpdate();
    }

    @Override
    public void onStart() {
        super.onStart();

        minimizeMap(null);
        presenter.getAssignedTRips();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @OnClick({R.id.tvMarkerPickUp, R.id.tvMarkerAll, R.id.tvMarkerDelivery})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //event for online/offline
            case R.id.tv_on_off_statas:
                presenter.updateMasterStatus();
                break;
            //event for back option in toolbar
            case R.id.button_back:
                presenter.expandMap();
                Utility.printLog("expand minimize");
                break;
            //event for markerAll
            case R.id.tvMarkerAll:
                presenter.markerAllOnclik();
                break;
            //event for markerPickUp
            case R.id.tvMarkerPickUp:
                presenter.markerPickUpOnclik();
                break;
            case R.id.tvMarkerDelivery:
                presenter.markerDeliveryOnclik();
                break;

        }
    }

    @Override
    public void onMasterStatusUpdate(int status) {
        switch (status) {
            //online
            case 3:
                tv_on_off_statas.setText(getString(R.string.go_offline));
                tv_on_off_statas.setSelected(true);
                startUpdateLocation();
                break;
            //offline
            case 4:
                tv_on_off_statas.setText(getString(R.string.go_online));
                tv_on_off_statas.setSelected(false);
                stopUpdateLocation();
                break;

        }
    }

    @Override
    public void setAssignedTrips(ArrayList<AssignedAppointments> assignedTrips) {
        rvSlots.setVisibility(View.VISIBLE);
        this.assignedTrips.clear();
        this.assignedTrips.addAll(assignedTrips);
        if (this.driverScheduleType.equals("0")) {
            if (assignedTrips.size() == 0) {
                ll_bookings.setVisibility(View.GONE);
            } else {
                appointments.clear();
                appointments.addAll(assignedTrips);
                adapter.notifyDataSetChanged();
                ll_bookings.setVisibility(View.VISIBLE);
            }
        } else {
            Collections.sort(assignedTrips, new AssignedAppointments());
            if (assignedTrips != null && assignedTrips.size() > 0) {

                ArrayList<AssignedAppointments> assignedTripsTemp = new ArrayList<>();

                String previousSlotId = "", slotId = "";

                for (int i = 0; i < assignedTrips.size(); i++) {

                    AssignedAppointments assignedAppointments = assignedTrips.get(i);
                    slotId = assignedAppointments.getSlotId();
                    Log.i("check", "setAssignedTrips: " + slotId);
                    if (!slotId.equals(previousSlotId)) {

                        assignedTripsTemp.add(assignedAppointments);

                    }
                    previousSlotId = slotId;
                }
                appointments.clear();
                appointments.addAll(assignedTripsTemp);

            }

            for (int i = 0; i < appointments.size(); i++) {
                for (int j = i + 1; j < appointments.size(); j++) {
                    String s1 = appointments.get(i).getSlotId();
                    String s2 = appointments.get(j).getSlotId();
                    if (s1.equals(s2)) {
                        appointments.remove(j);
                    }
                }
            }
            Collections.sort(appointments, new SortSlotStartTimeCmp());
            Collections.reverse(appointments);
            ArrayList<AssignedAppointments> orderDetailsTemp = new ArrayList<>();

            orderDetailsTemp.clear();


            int index = 0;

            if (appointments != null && appointments.size() > 0) {


                for (int i = 0; i < appointments.size(); i++) {


                    AssignedAppointments orderDetails = appointments.get(i);

                    Date d = new Date((long) Long.parseLong(orderDetails.getSlotStartTime()) * 1000);
                    DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    System.out.println(f.format(d));
                    Utility.printLog("date" + f.format(d));
                    String monthIndex = Utility.getMonth(f.format(d));
//                    String monthIndex = Utility.getMonth(orderDetails.getDueDatetime());


                    if (monthIndex != null) {

                        AssignedAppointments orderDetailsDate = new AssignedAppointments();

                        orderDetailsDate.setItemType(0);

                        orderDetailsDate.setDueDatetime(f.format(d));

                        orderDetailsTemp.add(orderDetailsDate);

                        orderDetails.setItemType(1);

                        orderDetailsTemp.add(orderDetails);


                        if (i < appointments.size()) {


                            for (int j = (i + 1); j < appointments.size(); j++) {


                                AssignedAppointments futureOrderDetails = appointments.get(j);
                                Date d1 = new Date((long) Long.parseLong(futureOrderDetails.getSlotStartTime()) * 1000);
                                DateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                System.out.println(f1.format(d1));
                                Utility.printLog("date" + f1.format(d1));
                                String futureMonthIndex = Utility.getMonth(f1.format(d1));

//                                String futureMonthIndex = Utility.getMonth(futureOrderDetails.getDueDatetime());


                                if (monthIndex.equals(futureMonthIndex)) {

                                    futureOrderDetails.setItemType(1);

                                    orderDetailsTemp.add(futureOrderDetails);


                                    index = j;

                                    //  i=index-1;

                                } else {


                                    //   index = j;

                                    i = (j - 1);

                                    index = i;


                                    break;

                                }

                            }


                            if (index == appointments.size() - 1) {
                                break;
                            }


                        } else {

                            orderDetailsTemp.add(orderDetails);

                            break;

                        }


                    }


                }


                orderDetails.clear();
                //    orderDetails.addAll(orders);

                orderDetails.addAll(orderDetailsTemp);

                slotFlowJobRVA.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void openMapInFullView() {

        ll_bookings.setVisibility(View.GONE);
        llMarkerFilter.setVisibility(View.VISIBLE);
        tv_on_off_statas.setVisibility(View.GONE);
        button_back.setVisibility(View.VISIBLE);
        button_menu.setVisibility(View.GONE);

        tvMarkerPickUp.setTextColor(getActivity().getResources().getColor(R.color.color_sup_txt));
        tvMarkerAll.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        tvMarkerDelivery.setTextColor(getActivity().getResources().getColor(R.color.color_sup_txt));
    }

    @Override
    public void minimizeMap(ArrayList<AssignedAppointments> appointments) {
        if (appointments != null && appointments.size() > 0) {
            ll_bookings.setVisibility(View.VISIBLE);
        }
        llMarkerFilter.setVisibility(View.GONE);
        tv_on_off_statas.setVisibility(View.VISIBLE);
        button_back.setVisibility(View.GONE);
        button_menu.setVisibility(View.VISIBLE);
    }

    @Override
    public void addMarkers(ArrayList<AssignedAppointments> appointments) {
        map.clear();
        tvMarkerPickUp.setTextColor(getActivity().getResources().getColor(R.color.color_sup_txt));
        tvMarkerAll.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        tvMarkerDelivery.setTextColor(getActivity().getResources().getColor(R.color.color_sup_txt));

        pickUp.clear();
        delivery.clear();

        if (appointments != null && appointments.size() > 0) {
            for (AssignedAppointments appointment : appointments) {
                if ((appointment.getOrderStatus().compareTo(AppConstants.BookingStatus.JourneyStarted)) > 0) {
                    String[] latLong = appointment.getDropLatLng().split(",");
                    Double lat = null, lng = null;
                    if (latLong.length > 0) {
                        lat = Double.parseDouble(latLong[0]);
                        lng = Double.parseDouble(latLong[1]);

                        Marker customer_marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
                        customer_marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_orange_pin_icon));
                        delivery.add(customer_marker);
                    }

                } else {
                    String[] latLong = appointment.getPickUpLatLng().split(",");
                    Double lat = null, lng = null;
                    if (latLong.length > 0) {
                        lat = Double.parseDouble(latLong[0]);
                        lng = Double.parseDouble(latLong[1]);

                        Marker customer_marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
                        customer_marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.store_pickup_location_icon));
                        pickUp.add(customer_marker);
                    }
                }
            }

        }
    }

    @Override
    public void removePickUpMarkers() {
        tvMarkerPickUp.setTextColor(getActivity().getResources().getColor(R.color.color_sup_txt));
        tvMarkerAll.setTextColor(getActivity().getResources().getColor(R.color.color_sup_txt));
        tvMarkerDelivery.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));


        if (pickUp.size() > 0) {
            for (int position = 0; position < pickUp.size(); position++) {
                pickUp.get(position).setVisible(false);
            }
        }
        if (delivery.size() > 0) {
            for (int position = 0; position < delivery.size(); position++) {
                delivery.get(position).setVisible(true);
            }
        }
    }

    @Override
    public void removeDeliveryMarkers() {
        tvMarkerPickUp.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        tvMarkerAll.setTextColor(getActivity().getResources().getColor(R.color.color_sup_txt));
        tvMarkerDelivery.setTextColor(getActivity().getResources().getColor(R.color.color_sup_txt));

        if (delivery.size() > 0) {
            for (int position = 0; position < delivery.size(); position++) {
                delivery.get(position).setVisible(false);
            }
        }
        if (pickUp.size() > 0) {
            for (int position = 0; position < pickUp.size(); position++) {
                pickUp.get(position).setVisible(true);
            }
        }
    }

    @Override
    public void showError(String statusMsg) {

        if (isAdded()) {

            if (alertDialog != null && !alertDialog.isShowing()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.show();
                    }
                });

            } else if (alertDialog == null) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), 5);
                builder.setTitle(getActivity().getResources().getString(R.string.message));
                builder.setMessage(statusMsg);
                builder.setPositiveButton(getActivity().getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        presenter.getAssignedTRips();
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

            }
        }

    }

    /**
     * <h1>startUpdateLocation</h1>
     * <p>start LocationUpdate Service</p>
     */
    private void startUpdateLocation() {
        if (!Utility.isMyServiceRunning(LocationUpdateService.class, getActivity())) {
            Offline_status=true;
            Intent startIntent = new Intent(getActivity(), LocationUpdateService.class);
            startIntent.setAction(AppConstants.ACTION.STARTFOREGROUND_ACTION);
            getActivity().startService(startIntent);
        }
    }

    /**
     * <h1>stopUpdateLocation</h1>
     * <p>stop LocationUpdate Service</p>
     */
    private void stopUpdateLocation() {
        if (Utility.isMyServiceRunning(LocationUpdateService.class, getActivity())) {
            Intent stopIntent = new Intent(getActivity(), LocationUpdateService.class);
            stopIntent.setAction(AppConstants.ACTION.STOPFOREGROUND_ACTION);
            getActivity().startService(stopIntent);
        }
    }

    @Override
    public void driverStatusType(int driverStatus) {
        this.driverScheduleType = driverStatus + "";
        if (this.driverScheduleType.equals("1")) {
            ll_frag_map.setVisibility(View.GONE);
            rl_slot_flow.setVisibility(View.VISIBLE);

        } else {
            ll_frag_map.setVisibility(View.VISIBLE);
            rl_slot_flow.setVisibility(View.GONE);
        }
    }
}
