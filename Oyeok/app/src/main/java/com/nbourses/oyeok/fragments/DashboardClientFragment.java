package com.nbourses.oyeok.fragments;


import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.Firebase.ChatList;
import com.nbourses.oyeok.Firebase.DroomChatFirebase;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.GetPrice;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.AutoCompletePlaces;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GeoFence;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.MapWrapperLayout;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.animations.FlipListener;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.interfaces.OnOyeClick;
import com.nbourses.oyeok.widgets.HorizontalPicker.HorizontalPicker;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class DashboardClientFragment extends Fragment implements CustomPhasedListener, AdapterView.OnItemClickListener, GoogleMap.OnCameraChangeListener, ChatList, HorizontalPicker.pickerPriceSelected, FragmentDrawer.MDrawerListener {

    private final String TAG = DashboardClientFragment.class.getSimpleName();
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final String[] CAMERA_PERMS = {
            Manifest.permission.CAMERA
    };
    private static final String[] CONTACTS_PERMS = {
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int INITIAL_REQUEST = 133;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    private static final int MAP_ZOOM = 12;

    DBHelper dbHelper;
    //    private TextView mDrooms;
    private TextView mVisits;
    private ImageView mQrCode;
    private LinearLayout mMarkerPanel;

    private RelativeLayout mMarkerminmax;
    private GoogleMap map;
    private LinearLayout ll_marker;
    private GeoFence geoFence;
    private int permissionCheckForCamera, permissionCheckForLocation;
    private final int MY_PERMISSION_FOR_CAMERA = 11;
    private CustomPhasedSeekBar mPhasedSeekBar;
    String brokerType;
    private Geocoder geocoder;
    private GetCurrentLocation.CurrentLocationCallback mcallback;
    private FrameLayout ll_map;
    String pincode, region, fullAddress;
    Double lat, lng;
    ClientMainActivity dashboardActivity;
    DroomChatFirebase droomChatFirebase;

    private GetCurrentLocation getLocationActivity;
    //View rootView;
    HashMap<String, HashMap<String, String>> chatListData;

    View rootView;
    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "", fullAddres = "";
    AutoCompleteTextView autoCompView;
    private RelativeLayout errorView;
    private TextView errorText;
    private HorizontalPicker horizontalPicker;
    private TextView tvRate;
    private TextView rupeesymbol;
    private OnOyeClick onOyeClick;
    CustomMapFragment customMapFragment;
    private int x,y,top,bottom,left,right,width,height;
    private android.graphics.Point point;

    private int llMin, llMax, orMin, orMax;

    @Bind(R.id.seekbar_linearlayout)
    LinearLayout seekbarLinearLayout;

    @Bind(R.id.missingArea)
    RelativeLayout missingArea;

    @Bind(R.id.txtFilterValue)
    TextView txtFilterValue;

    @Bind(R.id.iMarker)
    ImageView iMarker;



    ValueAnimator mFlipAnimator;

    private BroadcastReceiver onFilterValueUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                txtFilterValue.setText(Html.fromHtml(intent.getExtras().getString("filterValue")));
            }
        }
    };

    public void setOyeButtonClickListener(OnOyeClick onOyeClick) {
        this.onOyeClick = onOyeClick;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.rex_fragment_home, container, false);
        ButterKnife.bind(this, rootView);


        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);


//        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_markerpin);
//        LatLng latLng = new LatLng(21.4225, 39.8262);
//        map.addMarker(new MarkerOptions().position(latLng).title("Mecca").icon(icon)).showInfoWindow();
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int[] locations = new int[2];
                iMarker.getLocationOnScreen(locations);
//                x = locations[0]+26;
//                y = locations[1]-115;
                x = locations[0];
                y = locations[1];




                point= new android.graphics.Point(x,y);
                Log.i("MARKER","oncreate data"+x+" "+y+" "+point);




                bottom = iMarker.getBottom();
                top = iMarker.getTop();
                left=iMarker.getLeft();
                right=iMarker.getRight();
                width=iMarker.getMeasuredWidth();
                height=iMarker.getMeasuredHeight();
//                x = left + (right-left)/2;
//                y = bottom+115;

                Log.i("t1","Bottom"+ iMarker.getBottom()+"top"+top+"left"+left+"right"+right);
                Log.i("t1","width"+width+"height "+height);
         //       point= new android.graphics.Point(x,y);

                Log.i("t1","co-ordinate"+x+" "+y);
            }

        });

       // LatLng r = map.getProjection().fromScreenLocation(point);


       // map.addMarker(new MarkerOptions().position(r).title("Tester"));

        droomChatFirebase = new DroomChatFirebase(DatabaseConstants.firebaseUrl, getActivity());
//        mDrooms = (TextView) rootView.findViewById(R.id.linearlayout_drooms);
        mVisits = (TextView) rootView.findViewById(R.id.newVisits);
        mQrCode = (ImageView) rootView.findViewById(R.id.qrCode);
        mMarkerPanel = (LinearLayout) rootView.findViewById(R.id.ll_marker);

        mMarkerminmax = (RelativeLayout) rootView.findViewById(R.id.markerpanelminmax);
        ll_marker = (LinearLayout) rootView.findViewById(R.id.ll_marker);
        permissionCheckForCamera = ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.CAMERA);
        dashboardActivity = (ClientMainActivity) getActivity();
        dbHelper = new DBHelper(getContext());
        ll_map = (FrameLayout) rootView.findViewById(R.id.ll_map);
        permissionCheckForLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        errorView = (RelativeLayout) rootView.findViewById(R.id.alertLayout);
        errorText = (TextView) rootView.findViewById(R.id.errorText);

        rupeesymbol = (TextView) rootView.findViewById(R.id.rupeesymbol);
        tvRate = (TextView) rootView.findViewById(R.id.tvRate);


        onPositionSelected(0, 2);

        horizontalPicker = (HorizontalPicker) rootView.findViewById(R.id.picker);
        horizontalPicker.setMpicker(this);
        horizontalPicker.setTvRate(tvRate, rupeesymbol);
        horizontalPicker.setSelectedItem(2);
        horizontalPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        horizontalPicker.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
            @Override
            public void onItemSelected(int index) {
                horizontalPicker.addValues(index);
            }
        });

        mPhasedSeekBar = (CustomPhasedSeekBar) rootView.findViewById(R.id.phasedSeekBar);
        if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{"Rental", "Resale"}));
        else
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.real_estate_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Audit", "Auction"}));
        mPhasedSeekBar.setListener(this);

        autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.inputSearch);
        autoCompView.setAdapter(new AutoCompletePlaces.GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item1));
        autoCompView.setOnItemClickListener(this);
        //autoCompView.setOnItemClickListener(this);
        autoCompView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompView.clearListSelection();
                autoCompView.setText("");
                autoCompView.showDropDown();
                ll_map.setAlpha(0.5f);

            }
        });


        /*mDrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open deals listing
                Intent openDealsListing = new Intent(getActivity(), ClientDealsListActivity.class);
                startActivity(openDealsListing);
            }
        });*/

        mVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOyeScreen();
            }
        });

        mMarkerPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

//        ((DashboardActivity)getActivity()).changeDrawerToggle(true,"MarkerPanelScreen");
        customMapFragment = ((CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        geoFence = new GeoFence();
        if ((int) Build.VERSION.SDK_INT < 23) {
            customMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    map.setMyLocationEnabled(true);
                    setCameraListener();
                    geoFence.drawPloygon(map);
                }
            });
            getLocationActivity = new GetCurrentLocation(getActivity(), mcallback);
        }

        customMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
            @Override
            public void onDrag(MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    tvRate.setVisibility(View.GONE);
                    rupeesymbol.setVisibility(View.GONE);
//                    horizontalPicker.keepScrolling();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mMarkerPanel.setVisibility(View.VISIBLE);
                    mMarkerminmax.setVisibility(View.VISIBLE);
//                    horizontalPicker.stopScrolling();
                    tvRate.setVisibility(View.VISIBLE);
                    rupeesymbol.setVisibility(View.VISIBLE);

LatLng currentLocation1;
                    currentLocation1 = map.getProjection().fromScreenLocation(point);
                    Log.i("MARKER","point"+point + " " + point.x+ " " +point.y+ " " +x+ " " +y +" "+currentLocation1);
//                        x = left + (right-left)/2;
//                        y = bottom;
  map.addMarker(new MarkerOptions().position(currentLocation1));
                //    point.set(point.x,point.y);


                    getPrice();
                    map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    tvRate.setVisibility(View.GONE);
                    rupeesymbol.setVisibility(View.GONE);
//                    horizontalPicker.keepScrolling();

                }
                SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
            }
        });

        mcallback = new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                    if (isNetworkAvailable()) {
                        try {
                            //getRegion();
                        } catch (Exception e) {
                            Log.i("Exception", "caught in get region");
                        }
                    }

                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));

                    //make retrofit call to get Min Max price
                    if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null") && isNetworkAvailable()) {
                        try {
                            getPrice();
                        } catch (Exception e) {

                        }
                    }
                }
            }
        };


        if (!dbHelper.getValue(DatabaseConstants.userId).equalsIgnoreCase("null"))
            droomChatFirebase.getDroomList(dbHelper.getValue(DatabaseConstants.userId), getActivity());

        dbHelper.save(DatabaseConstants.userRole, "Client");

        rupeesymbol.bringToFront();
        tvRate.bringToFront();
        ll_marker.bringToFront();
        txtFilterValue.setText(Html.fromHtml(getResources().getString(R.string.default_2_bhk)));

        /**
         * animate views
         */
        mFlipAnimator = ValueAnimator.ofFloat(0f, 1f);
        mFlipAnimator.addUpdateListener(new FlipListener(mVisits, txtFilterValue));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isFlipped()) {
                                mFlipAnimator.reverse();
                            } else {
                                mFlipAnimator.start();
                            }
                        }
                    });
                }
            }
        }, 2000, 2000);

        return rootView;
    }

    @OnClick(R.id.txtFilterValue)
    public void onTxtFilterValueClick(View v) {
        openOyeScreen();
    }

    private void openOyeScreen() {
        Bundle args = new Bundle();
        args.putString("brokerType", brokerType);
        args.putString("Address", SharedPrefs.getString(getActivity(), SharedPrefs.MY_REGION));
        onOyeClick.onClickButton(args);
    }

    private boolean isFlipped() {
        return mFlipAnimator.getAnimatedFraction() == 1;
    }

    @Override
    public void onResume() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        super.onResume();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onFilterValueUpdate, new IntentFilter(AppConstants.ON_FILTER_VALUE_UPDATE));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onFilterValueUpdate);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        getLocationActivity.setCallback(null);
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
    }

    private void displayToast(String toast) {
        if (getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
        }
    }


    private Handler mHandler;
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            hideMap(0);
            errorView.setVisibility(View.GONE);
        }
    };

    private void hideMap(int i) {
        Animation m = null;
        if (isAdded())
            //Load animation
            if (i == 0)
                m = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
            else
                m = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);

        errorView.setAnimation(m);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String toast = null;
        if (result != null) {
            if (result.getContents() == null) {
                toast = "Cancelled from fragment";
            } else {
                toast = "Scanned from fragment: " + result.getContents();
            }
            // At this point we may or may not have a reference to the activity
            displayToast(toast);
        }
    }

    public void getPrice() {
        //getRegion();
        User user = new User();
        user.setDeviceId("Hardware");
        user.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        user.setUserRole("client");
        user.setLongitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
        user.setLatitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
        user.setLocality(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
        user.setPlatform("android");
        Log.i("my_locality", SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
        user.setPincode("400058");

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        UserApiService userApiService = restAdapter.create(UserApiService.class);
        userApiService.getPrice(user, new Callback<GetPrice>() {
            @Override
            public void success(GetPrice getPrice, Response response) {
                if (getPrice.responseData.getLl_min() != null &&
                        !getPrice.responseData.getLl_min().equals("")) {
                    Log.i("TRACE", "RESPONSEDATAr" + response);
                    llMin = Integer.parseInt(getPrice.responseData.getLl_min());
                    llMax = Integer.parseInt(getPrice.responseData.getLl_max());
                    Log.i("TRACE", "RESPONSEDATAr" + llMin);
                    Log.i("TRACE", "RESPONSEDATAr" + llMax);
                    llMin = 5 * (Math.round(llMin / 5));
                    llMax = 5 * (Math.round(llMax / 5));
                    Log.i("TRACE", "RESPONSEDATAr" + llMin);
                    Log.i("TRACE", "RESPONSEDATAr" + llMax);

                    orMin = Integer.parseInt(getPrice.responseData.getOr_min());
                    orMax = Integer.parseInt(getPrice.responseData.getOr_max());
                    Log.i("TRACE", "RESPONSEDATAr" + orMin);
                    Log.i("TRACE", "RESPONSEDATAr" + orMax);
                    orMin = 500 * (Math.round(orMin / 500));
                    orMax = 500 * (Math.round(orMax / 500));
                    Log.i("TRACE", "RESPONSEDATAr" + orMin);
                    Log.i("TRACE", "RESPONSEDATAr" + orMax);


                    updateHorizontalPicker();

                    missingArea.setVisibility(View.GONE);
                } else {
                    /*SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .text("We don't cater here yet")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());*/

                    missingArea.setVisibility(View.VISIBLE);
                    /*missingArea.setAnimation(AnimationUtils.loadAnimation(getActivity(),
                            R.anim.slide_up));*/
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("getPrice", "error: " + error.getMessage());
            }
        });
    }

    private void updateHorizontalPicker() {

        if (horizontalPicker != null) {
            if (brokerType.equals("rent"))
                //   horizontalPicker.setInterval((llMin*1000), (llMax*1000),10, HorizontalPicker.THOUSANDS);
                horizontalPicker.setInterval((llMin * 1000), (llMax * 1000), 10, HorizontalPicker.THOUSANDS);
            else

                horizontalPicker.setInterval(orMin, orMax, 10, HorizontalPicker.THOUSANDS);
        }
    }

    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(getContext(), perm));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_FOR_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    IntentIntegrator.forSupportFragment(DashboardClientFragment.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(CaptureActivityAnyOrientation.class).setOrientationLocked(false).initiateScan();
                    // permission was granted, y-------------ay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            case LOCATION_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    customMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            map = googleMap;
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            map.setMyLocationEnabled(true);
                            setCameraListener();
                            geoFence.drawPloygon(map);
                        }
                    });
                    getLocationActivity = new GetCurrentLocation(getActivity(),mcallback);

                }
                else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
        if (canAccessLocation()) {
            new GetCurrentLocation(getActivity(), new GetCurrentLocation.CurrentLocationCallback() {
                @Override
                public void onComplete(Location location) {
                    if (location != null) {
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                      //  point = map.getProjection().toScreenLocation(currentLocation);
                       // Log.i("MARKER","point "+point);
                        map.addMarker(new MarkerOptions().position(currentLocation).title("Ritesh"));

                       //LatLng c = map.getProjection().fromScreenLocation(point);

                        x = left + (right-left)/2;
                        y = bottom;

                        point = map.getProjection().toScreenLocation(currentLocation);
                        Log.i("MARKER","point"+point + " " + point.x+ " " +point.y+ " " +x+ " " +y +" "+currentLocation);
//                        x = left + (right-left)/2;
//                        y = bottom;

                        point.set(point.x,point.y-50);




                       LatLng target = map.getProjection().fromScreenLocation(point);
                     CameraPosition c = new CameraPosition.Builder().target(currentLocation).build();


                        map.moveCamera(CameraUpdateFactory.newCameraPosition(c));
                        map.addMarker(new MarkerOptions().position(target).title("Ritesh1"));

                        Log.i("MARKER","locn"+currentLocation+" "+target);







                        //map.moveCamera(CameraUpdateFactory.newLatLng(c));
                        map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));

                    }
                }
            });
            getLocationActivity = new GetCurrentLocation(getActivity(),mcallback);
        }
        else {
            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
            Toast.makeText(getContext(), "Offline Mode", Toast.LENGTH_LONG);
//            ((DashboardActivity) getActivity()).showToastMessage("Offline Mode");
        }

    }

    @Override
    public void onPositionSelected(int position, int count) {
        if(count==2){
            if(position==0) {
                tvRate.setText("/ month");
                brokerType = "rent";
                dbHelper.save(DatabaseConstants.brokerType, "LL");
                dbHelper.save("brokerType","On Rent");

                updateHorizontalPicker();
            }
            else if(position==1) {
                tvRate.setText("/ sq.ft");
                brokerType = "resale";
                dbHelper.save(DatabaseConstants.brokerType, "OR");
                dbHelper.save("brokerType","For Sale");

                updateHorizontalPicker();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ll_map.setAlpha(1f);
        map.animateCamera(CameraUpdateFactory.zoomTo(12));
        autoCompView.clearListSelection();
        getLocationFromAddress(autoCompView.getText().toString());
    }

    public void getRegion() {
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        region = addresses.get(0).getLocality();
        pincode = addresses.get(0).getPostalCode();
        fullAddress = "";
        for(int i=0; i<addresses.get(0).getMaxAddressLineIndex(); i++){
            fullAddress += addresses.get(0).getAddressLine(i);
        }
        SharedPrefs.save(getActivity(), SharedPrefs.MY_REGION, fullAddress);
        if (addresses.size() > 0) {
            pincode = addresses.get(0).getPostalCode();

            //if 1st provider does not have data, loop through other providers to find it.
            int count = 0;
            while (pincode == null && count < addresses.size()) {
                pincode = addresses.get(count).getPostalCode();
                count++;
            }
        }
    }
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (isNetworkAvailable()) {
            lat = cameraPosition.target.latitude;
            lng = cameraPosition.target.longitude;
            SharedPrefs.save(getActivity(),SharedPrefs.MY_LAT,lat+"");
            SharedPrefs.save(getActivity(),SharedPrefs.MY_LNG,lng+"");
            new LocationUpdater().execute();
        }
    }

    //@Override
    public void onPositionSelected(int position) {
        //Toast.makeText(getActivity(), "Selected position:" + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void sendData(HashMap<String, HashMap<String, String>> hashMap) {
        chatListData=hashMap;
        Log.i("chatdata in rex",chatListData.toString());
    }

    @Override
    public void priceSelected(String val) {

        map.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void drawerOpened() {
        horizontalPicker.stopScrolling();
    }

    protected class LocationUpdater extends AsyncTask<Double, Double, String>{
        public JSONObject getJSONfromURL(String url) {

            // initialize
            InputStream is = null;
            String result = "";
            JSONObject jObject = null;

            // http post
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

            } catch (Exception e) {
                Log.e(TAG, "Error in http connection " + e.toString());
            }

            // convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e(TAG, "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                jObject = new JSONObject(result);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing data " + e.toString());
            }

            return jObject;
        }
        @Override
        protected String doInBackground(Double[] objects) {
            try {
                JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + ","
                        + lng + "&sensor=true");
                String Status = jsonObj.getString("status");
                if (Status.equalsIgnoreCase("OK")) {
                    JSONArray Results = jsonObj.getJSONArray("results");
                    JSONObject zero = Results.getJSONObject(0);
                    JSONArray address_components = zero.getJSONArray("address_components");

                    fullAddress = zero.getString("formatted_address");
                    for (int i = 0; i < address_components.length(); i++) {
                        JSONObject zero2 = address_components.getJSONObject(i);
                        String long_name = zero2.getString("long_name");
                        JSONArray mtypes = zero2.getJSONArray("types");
                        String Type = mtypes.getString(0);

                        if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                            if (Type.equalsIgnoreCase("street_number")) {
                                Address1 += long_name;
                            } else if (Type.equalsIgnoreCase("route")) {
                                Address1 += " "+long_name;
                            } else if (Type.equalsIgnoreCase("sublocality_level_2")) {
                                Address2 = long_name;
                            } else if (Type.equalsIgnoreCase("sublocality_level_1")){
                                Address2 += " "+long_name;
                                if (getActivity() != null)
                                    SharedPrefs.save(getActivity(),SharedPrefs.MY_LOCALITY,long_name);
                            } else if (Type.equalsIgnoreCase("locality")) {
                                // Address2 = Address2 + long_name + ", ";
                                City = long_name;
                                SharedPrefs.save(getActivity(),SharedPrefs.MY_CITY, City);
                            } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                                County = long_name;
                            } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                State = long_name;
                            } else if (Type.equalsIgnoreCase("country")) {
                                Country = long_name;
                            } else if (Type.equalsIgnoreCase("postal_code")) {
                                PIN = long_name;
                                SharedPrefs.save(getActivity(),SharedPrefs.MY_PINCODE,PIN);
                            }
                        }
                        if (getActivity() != null)
                            SharedPrefs.save(getActivity(), SharedPrefs.MY_REGION, fullAddress);
                        // JSONArray mtypes = zero2.getJSONArray("types");
                        // String Type = mtypes.getString(0);
                        // Log.e(Type,long_name);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return fullAddress;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            autoCompView.setText(s);
            autoCompView.dismissDropDown();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean setCameraListener(){
        map.setOnCameraChangeListener(this);
        return true;
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        //GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                //return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();
            Log.i("lat="+location.getLatitude()," long="+location.getLongitude());
            LatLng l=new LatLng(location.getLatitude(),location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(l));
            map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));
            getPrice();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
