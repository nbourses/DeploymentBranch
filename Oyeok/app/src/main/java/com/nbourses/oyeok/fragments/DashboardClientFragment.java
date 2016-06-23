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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.os.SystemClock;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import com.nbourses.oyeok.helpers.General;
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
import retrofit.mime.TypedByteArray;

import static java.lang.Math.log10;


public class DashboardClientFragment extends Fragment implements CustomPhasedListener,AdapterView.OnItemClickListener, GoogleMap.OnCameraChangeListener, ChatList, HorizontalPicker.pickerPriceSelected, FragmentDrawer.MDrawerListener {



    //GoogleMap.OnCameraChangeListener,TouchableWrapper.UpdateMapAfterUserInterection
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


    boolean MarkerClicked = false;

    private static final int INITIAL_REQUEST = 133;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    private static final int MAP_ZOOM = 12;
    private Point point;
    DBHelper dbHelper;
    //    private TextView mDrooms;
    private TextView mVisits;
    private ImageView mQrCode;
    private LinearLayout mMarkerPanel;
    private Timer timer;
    private RelativeLayout mMarkerminmax;
    private GoogleMap map;
    private LinearLayout ll_marker;
    private ImageView Mmarker;
    private ImageView search_building_icon;
    private BitmapDescriptor icon1;
    private BitmapDescriptor icon2;

    private boolean flag[] = new boolean[5],RatePanel=false;

    boolean mflag = false;
    private long lastTouched = 0;
    private static final long SCROLL_TIME = 200L;


    private GeoFence geoFence;
    private int permissionCheckForCamera, permissionCheckForLocation;
    private final int MY_PERMISSION_FOR_CAMERA = 11;
    private CustomPhasedSeekBar mPhasedSeekBar;
    Drawable marker = null;
    Marker[] mCustomerMarker = new Marker[5];
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
    //private TextView tvCommingsoon;
    private TextView tv_building;
    private TextView tvFetchingrates;
    CustomMapFragment customMapFragment;
    GPSTracker gpsTracker;
    static int x, y;
    static int top, bottom, left, right, width, height,truncate_first;
    private int llMin, llMax, orMin, orMax;
    private String name,text;

    
    
    private String filterValue;
    private String bhk;
    private int filterValueMultiplier=950;


    private int []or_psf=new int[5], ll_pm=new int[5];
    private LatLng loc;
    LinearLayout recordWorkout;
    
    Intent intent ;


    AutoCompleteTextView inputSearch;
     private  int INDEX;
    @Bind(R.id.seekbar_linearlayout)
    LinearLayout seekbarLinearLayout;

    @Bind(R.id.missingArea)
    RelativeLayout missingArea;

    @Bind(R.id.txtFilterValue)
    TextView txtFilterValue;

    ValueAnimator mFlipAnimator;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private BroadcastReceiver onFilterValueUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {

                if ((intent.getExtras().getString("filterValue") != null)) {
                   // txtFilterValue.setText(Html.fromHtml(intent.getExtras().getString("filterValue")));
                    Log.i("filtervalue","filtervalue "+intent.getExtras().getString("filterValue"));
                    txtFilterValue.setText(intent.getExtras().getString("filterValue"));

                }
                if ((intent.getExtras().getString("filterValue") != null)) {
                    // txtFilterValue.setText(intent.getExtras().getString("filterValue"));
                    bhk = intent.getExtras().getString("bhk");
                    Log.i("bhk","=================  "+bhk);
                    //filterValue =  intent.getExtras().getString("filterValue").toString();

                    if (bhk.equalsIgnoreCase("1bhk") || bhk.equalsIgnoreCase("<600")) {
                        filterValueMultiplier = 600;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin*filterValueMultiplier,llMax*filterValueMultiplier,orMin,orMax);
                        if (brokerType.equals("rent"))
                        onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,llMin*filterValueMultiplier,llMax*filterValueMultiplier,"/month");
//                        BroadCastMinMaxValue(llMin*filterValueMultiplier,llMax*filterValueMultiplier,orMin,orMax);
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,orMin,orMax,"/psf");

                    } else if (bhk.equalsIgnoreCase("2bhk")|| bhk.equalsIgnoreCase("<950")) {

                        filterValueMultiplier = 950;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin*filterValueMultiplier,llMax*filterValueMultiplier,orMin,orMax);
                        if (brokerType.equals("rent"))
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,llMin*filterValueMultiplier,llMax*filterValueMultiplier,"/month");
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,orMin,orMax,"/psf");
                    } else if (bhk.equalsIgnoreCase("3bhk")|| bhk.equalsIgnoreCase("<1600")) {
                        filterValueMultiplier = 1600;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin*filterValueMultiplier,llMax*filterValueMultiplier,orMin,orMax);
                        if (brokerType.equals("rent"))
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,llMin*filterValueMultiplier,llMax*filterValueMultiplier,"/month");
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,orMin,orMax,"/psf");
                    } else if (bhk.equalsIgnoreCase("4bhk")|| bhk.equalsIgnoreCase("<2100")) {
                        filterValueMultiplier = 2100;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin*filterValueMultiplier,llMax*filterValueMultiplier,orMin,orMax);
                        if (brokerType.equals("rent"))
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,llMin*filterValueMultiplier,llMax*filterValueMultiplier,"/month");
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,orMin,orMax,"/psf");
                    } else if (bhk.equalsIgnoreCase("4+bhk")|| bhk.equalsIgnoreCase("<3000")) {
                        filterValueMultiplier = 3000;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin*filterValueMultiplier,llMax*filterValueMultiplier,orMin,orMax);
                        if (brokerType.equals("rent"))
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,llMin*filterValueMultiplier,llMax*filterValueMultiplier,"/month");
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,orMin,orMax,"/psf");
                    } else if(bhk.equalsIgnoreCase("<300")){
                        filterValueMultiplier = 300;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin*filterValueMultiplier,llMax*filterValueMultiplier,orMin,orMax);
                        if (brokerType.equals("rent"))
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,llMin*filterValueMultiplier,llMax*filterValueMultiplier,"/psf");
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier,orMin,orMax,"/psf");
                    }

///// check if required
//                 try{
//                // txtFilterValue.setText(Html.fromHtml(intent.getExtras().getString("filterValue")));
//             }catch (Exception e){
//               ///  Log.i("txtFilterValue","txtFilterValue: "+ intent.getExtras().getString("filterValue"));
//
                 }
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

        gpsTracker = new GPSTracker(getContext());


        if (General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI).equals("")) {
            General.setSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI, String.valueOf(System.currentTimeMillis()));
            Log.i("TIMESTAMP", "millis " + System.currentTimeMillis());
        }


        LocalBroadcastManager.getInstance(getContext()).registerReceiver(closeOyeScreenSlide, new IntentFilter(AppConstants.CLOSE_OYE_SCREEN_SLIDE));
//TextView tv_client_heading=(TextView)
        //intent = new Intent(AppConstants.CLIENT_HEADING);
//       intent =new Intent(getContext(), ClientMainActivity.class);
//        intent.putExtra("client_heading", "Live Region Rates");
//        startActivity(intent);
//        intent.putExtra("client_heading", "Live Region Rates");
//        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//        startActivity(intent);


        //tv_client_heading.setVisibility(View.VISIBLE);

        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        droomChatFirebase = new DroomChatFirebase(DatabaseConstants.firebaseUrl, getActivity());
//        mDrooms = (TextView) rootView.findViewById(R.id.linearlayout_drooms);
        mVisits = (TextView) rootView.findViewById(R.id.newVisits);
        mQrCode = (ImageView) rootView.findViewById(R.id.qrCode);
        mMarkerPanel = (LinearLayout) rootView.findViewById(R.id.ll_marker);

        mMarkerminmax = (RelativeLayout) rootView.findViewById(R.id.markerpanelminmax);
        ll_marker = (LinearLayout) rootView.findViewById(R.id.ll_marker);
        recordWorkout = (LinearLayout) rootView.findViewById(R.id.recordWorkout);

//        footer = new LinearLayout(getContext());
//        footer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 8));

        //inputSearch= (AutoCompleteTextView) rootView.findViewById(R.id.inputSearch) ;

        // broker_map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.broker_map)).getMap();

        Mmarker = (ImageView) rootView.findViewById(R.id.Mmarker);
        try {
            icon1 = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("buildingiconbeforeclick",30,65));//BitmapDescriptorFactory.fromResource(R.drawable.buildingiconbeforeclick);
           // Bitmap yourBitmap=BitmapFactory.decodeResource(getContext().getResources(), R.drawable.click_building_icon);
           //Bitmap yourBitmap = Bitmap.createScaledBitmap(yourBitmap, 30, 65, true);
            icon2 = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("click_building_icon",67,68));
        }
        catch (Exception e)
        {
            Log.i("BITMAP","message "+e.getMessage());
        }
      recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
        //selected_property = BitmapDescriptorFactory.fromResource(R.drawable.search_building_icon);
        search_building_icon = (ImageView) rootView.findViewById(R.id.selected_property);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Mmarker.getViewTreeObserver().removeGlobalOnLayoutListener(this);


                int[] locations = new int[2];
                Mmarker.getLocationOnScreen(locations);
                x = locations[0] + 26;
                y = locations[1] - 115;
//                x = left - (right - left) / 2;
//                y = bottom;
                bottom = Mmarker.getBottom();
                top = Mmarker.getTop();
                left = Mmarker.getLeft();
                right = Mmarker.getRight();
                width = Mmarker.getMeasuredWidth();
                height = Mmarker.getMeasuredHeight();
                Log.i("t1", "Bottom" + Mmarker.getBottom() + "top" + top + "left" + left + "right" + right);
                Log.i("t1", "width" + width + "height " + height);
                point = new Point(x, y);

                Log.i("t1", "co-ordinate" + x + " " + y);
            }
        });

        dashboardActivity = (ClientMainActivity) getActivity();
        dbHelper = new DBHelper(getContext());
        ll_map = (FrameLayout) rootView.findViewById(R.id.ll_map);


        permissionCheckForLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        errorView = (RelativeLayout) rootView.findViewById(R.id.alertLayout);
        errorText = (TextView) rootView.findViewById(R.id.errorText);

        rupeesymbol = (TextView) rootView.findViewById(R.id.rupeesymbol);
       // tvCommingsoon = (TextView) rootView.findViewById(R.id.tvCommingsoon);
        tvRate = (TextView) rootView.findViewById(R.id.tvRate);
        tvFetchingrates = (TextView) rootView.findViewById(R.id.tvFetchingRates);
        tv_building = (TextView) rootView.findViewById(R.id.tv_building);

        //search_building_icon.setVisibility(View.INVISIBLE);

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
//        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(autoCompView.getWindowToken(), 0);
        //autoCompView.setOnItemClickListener(this);
        autoCompView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompView.clearListSelection();
                autoCompView.setText("");
                autoCompView.showDropDown();
                // new LocationUpdater().execute();
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
                CancelAnimation();

                if(RatePanel==true) {
                    UpdateRatePanel();
                    RatePanel = false;
                }
                else {
                    RatePanel = true;
                   // tvFetchingrates.setVisibility(View.VISIBLE);
                }
//                if (brokerType.equals("rent"))
//     RatePanel               onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),950,llMin*filterValueMultiplier,llMax*filterValueMultiplier);
//                else
//                    onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),950,orMin,orMax);

                openOyeScreen();
                CancelAnimation();

            }
        });

//        mMarkerPanel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });


        try {
            if (isNetworkAvailable())
                customMapFragment = ((CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

           map= customMapFragment.getMap();

            // geoFence = new GeoFence();
             if (isNetworkAvailable()) {


                if (map != null) {
                    if ((int) Build.VERSION.SDK_INT < 23) {

                        // getLocationActivity = new GetCurrentLocation(getActivity(),mcallback);
//                        customMapFragment.getMapAsync(new OnMapReadyCallback() {
//                            @Override
//                            public void onMapReady(GoogleMap googleMap) {
//
//                                map = googleMap;
//
//                               // map = googleMap;
//
//                                //lat = 19.1269299;
//                                //lng = 72.8376545999999;
//                                enableMyLocation();
////                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////
////                                    map.setMyLocationEnabled(true);
////                                    return;
////                                }
//
//
//
//
//                            }
//                        });
                        getLocationActivity = new GetCurrentLocation(getActivity(),mcallback);
                    }




                }


            }





                                /*else  if (flag[i] == true) {
                                    Log.i("flag[i] == true 11 ", "===========================");
                                           // m=mCustomerMarker[i];
                                    mCustomerMarker[i].setIcon(icon1);
                                            //mCustomerMarker[i].remove();
                                            //mCustomerMarker[i] = map.addMarker(new MarkerOptions().position(m.getPosition()).title(m.getTitle()).snippet(m.getSnippet()).icon(icon1));
                                            flag[i]=false;

                                        }*/
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {

        Log.i("MA999999 ","MAP CLICK=========");


            UpdateRatePanel();
            Intent intent = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);



//        mMarkerminmax.setVisibility(View.VISIBLE);
//        // Mmarker.setVisibility(View.VISIBLE);
//        //horizontalPicker.stopScrolling();
//        tvRate.setVisibility(View.VISIBLE);
//        rupeesymbol.setVisibility(View.VISIBLE);
//        tvFetchingrates.setVisibility(View.VISIBLE);
//        tv_building.setVisibility(View.VISIBLE);
//        horizontalPicker.setVisibility(View.VISIBLE);
//        tv_building.setText("Average Rate @ this Locality");


        }


});
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    MarkerClicked = true;
                    intent =new Intent(getContext(), ClientMainActivity.class);
                   int i;
                    Log.i("MARKER ", "=========="+MarkerClicked);
                    for (i = 0; i < 5; i++) {
                        if (marker.getId().equals(mCustomerMarker[i].getId())) {
                           INDEX=i;


                            if(flag[i]==false) {

                                Log.i("flag[i] == false ", "===========================");

                                //clicked=true;
                                mCustomerMarker[i].setIcon(icon2);

                                search_building_icon.setVisibility(View.VISIBLE);
                              // mCustomerMarker[i].showInfoWindow();
                                horizontalPicker.setVisibility(View.GONE);
                                tvFetchingrates.setVisibility(View.VISIBLE);
                                tvRate.setVisibility(View.GONE);
                                rupeesymbol.setVisibility(View.GONE);
                               // tv_building.setVisibility(View.GONE);
                                recordWorkout.setBackgroundColor(Color.parseColor("#ff9f1c"));
                               // tvFetchingrates.setTextColor(Color.parseColor("#ffffff")); &#x20B9
                                Log.i("coming soon","coming soon :"+marker.getTitle().toString());
                                tv_building.setVisibility(View.VISIBLE);
                                tv_building.setText("Average Rate in last 1 WEEK");
                                String text = "<font color=#ffffff >"+marker.getTitle().toString()+"</b></font> <font color=#ffffff> @</font>&nbsp<font color=#ff9f1c>\u20B9 "+General.currencyFormat(String.valueOf(ll_pm[i])).substring(2,General.currencyFormat(String.valueOf(ll_pm[i])).length())+"</font><b><font color=#ff9f1c><sub>/m</sub></font></br>";
                                tvFetchingrates.setText(Html.fromHtml(text));
                                tvFetchingrates.setTypeface(null,Typeface.BOLD);

                                intent.putExtra("client_heading", "Live Building Rates");

                               lng= marker.getPosition().longitude;
                                lat=marker.getPosition().latitude;
                                Log.i("marker lat","==============marker position :"+marker.getPosition()+" "+lat+" "+lng);

                                SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                                SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                                General.setSharedPreferences(getContext(),AppConstants.MY_LAT,lat + "");
                                General.setSharedPreferences(getContext(),AppConstants.MY_LNG,lng + "");
                                new LocationUpdater().execute();
                                //if   (General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("rent")){
                               // tvFetchingrates.setText(marker.getTitle().toString()+" @ "+General.currencyFormat(String.valueOf(ll_pm[i])));
                                //else {tvFetchingrates.setText(marker.getTitle().toString()+" @ "+or_psf[i]);}


                                flag[i]=true;

                            }else{
                                mCustomerMarker[i].setIcon(icon1);
                                search_building_icon.setVisibility(View.INVISIBLE);
                                flag[i]=false;
                                //clicked=false;
                                horizontalPicker.setVisibility(View.VISIBLE);
                                tvFetchingrates.setVisibility(View.INVISIBLE);
                                //tvFetchingrates.setTextColor(Color.parseColor("#ffffff"));
//                                footer.setBackgroundColor(Color.parseColor("#ff9f1c"));
//                                ((LinearLayout)rootView).addView(footer);
                                recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
//                                tv_building.setText("Average Rate @ This Locality");
//                                tv_building.setTypeface(null, Typeface.ITALIC);
//                                tv_building.setVisibility(View.VISIBLE);
                                intent.putExtra("client_heading", "Live Region Rates");
                                //startActivity(intent);
                                //inputSearch.setBackgroundColor(Color.parseColor("#2dc4b6"));
                                Log.i("coming soon","coming soon :"+marker.getTitle().toString()+recordWorkout);
                               // tvFetchingrates.setText(marker.getTitle().toString()+" @ "+marker.getSnippet().toString());
                                tvRate.setVisibility(View.VISIBLE);
                                rupeesymbol.setVisibility(View.VISIBLE);
                                tv_building.setVisibility(View.VISIBLE);
                                tv_building.setText("Average Rate @ this Locality");

                            }


                        }
                        else
                        {

                            mCustomerMarker[i].setIcon(icon1);
//                            horizontalPicker.setVisibility(View.VISIBLE);
//                            tvFetchingrates.setVisibility(View.INVISIBLE);
//                            tvFetchingrates.setTextColor(Color.parseColor("#ffffff"));
//                            Log.i("coming soon","coming soon :"+marker.getTitle().toString());
//                            tvFetchingrates.setText(marker.getTitle().toString()+" @ "+marker.getSnippet().toString());
//
//
//                            tvRate.setVisibility(View.VISIBLE);
//                            rupeesymbol.setVisibility(View.VISIBLE);
//                            tv_building.setVisibility(View.VISIBLE);



                        }



                    }


                    return true;
                }
            });






        } catch (Exception e) {}









        try {
            if (isNetworkAvailable() && map!= null) {
                // customMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
                customMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
                    @Override
                    public void onDrag(MotionEvent motionEvent) {
                        //Log.d("t1", String.format("ME: %s", motionEvent));

//                        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
//                            tvRate.setVisibility(View.INVISIBLE);
//                            rupeesymbol.setVisibility(View.INVISIBLE);
//                            Log.i("MARKER 111","========================="+MarkerClicked);
//                            //tv_building.setVisibility(View.GONE);
////                    Point p= new Point(x,y);
////                    LatLng currentLocation1;
////                   currentLocation1= map.getProjection().fromScreenLocation(point);
////                    lat=currentLocation1.latitude;
////                    lng=currentLocation1.longitude;
////
////
////                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
////                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
//                            //getRegion();
//                            tvFetchingrates.setVisibility(View.INVISIBLE);
//
//                           horizontalPicker.keepScrolling();
////                           // clicked=false;
////                           // MarkerClicked=false;
//                        } else
                          if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                             // horizontalPicker.keepScrolling();
                             // horizontalPicker.stopScrolling();
                            //mMarkerPanel.setVisibility(View.VISIBLE);
                            final long now = SystemClock.uptimeMillis();
                            if (now - lastTouched > SCROLL_TIME) {


                                horizontalPicker.keepScrolling();

                                Log.i("MARKER", "=========================" + MarkerClicked);

                                tv_building.setText("Average Rate @ this Locality");
                                tvFetchingrates.setVisibility(View.VISIBLE);
                               // if (!MarkerClicked) {
                                    mMarkerminmax.setVisibility(View.VISIBLE);
                                    // Mmarker.setVisibility(View.VISIBLE);
                                    //horizontalPicker.stopScrolling();
                                    tvRate.setVisibility(View.VISIBLE);
                                    rupeesymbol.setVisibility(View.VISIBLE);
                                    tvFetchingrates.setVisibility(View.VISIBLE);
                                    tv_building.setVisibility(View.VISIBLE);
                                    tv_building.setText("Average Rate @ this Locality");
                                    recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
//                                tv_building.setText("Average Rate @ This Locality");
//                                tv_building.setTypeface(null, Typeface.ITALIC);
                                    LatLng currentLocation1; //= new LatLng(location.getLatitude(), location.getLongitude());
                                    Log.i("map", "============ map:" + " " + map);
                                    currentLocation1 = map.getProjection().fromScreenLocation(point);
                                    lat = currentLocation1.latitude;
                                    Log.i("t1", "lat" + " " + lat);
                                    lng = currentLocation1.longitude;
                                    Log.i("t1", "lng" + " " + lng);
                                    Log.i("MARKER-- ", "====================================");
                                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                                General.setSharedPreferences(getContext(),AppConstants.MY_LAT,lat + "");
                                General.setSharedPreferences(getContext(),AppConstants.MY_LNG,lng + "");
                                    Log.i("t1", "Sharedpref_lat" + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
                                    Log.i("t1", "Sharedpref_lng" + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
                                    getRegion();
                                    ///horizontalPicker.stopScrolling();
                                    search_building_icon.setVisibility(View.INVISIBLE);
                                horizontalPicker.stopScrolling();
                                    getPrice();
                                    // getPrice();
                                    // mflag = false;


                                    Log.i("t1", "latlong" + " " + currentLocation1);


                                    if (isNetworkAvailable()) {
                                        new LocationUpdater().execute();
                                    }
//                                } else {
//                                    Log.i("Inside Map click else ", "------" + MarkerClicked);
//
//
////                                    MarkerClicked = false;
////                                    clicked = false;
//                                }

                            }

                        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            lastTouched = SystemClock.uptimeMillis();
//                            tvRate.setVisibility(View.GONE);
                             // horizontalPicker.keepScrolling();
                            //rupeesymbol.setVisibility(View.INVISIBLE);
                           // tv_building.setVisibility(View.INVISIBLE);
                            LatLng currentLocation11;
                            Log.i("MotionEvent.ACTION_DOWN","========================="+MarkerClicked);



                                      //recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));







                        }
//                        SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
//                        SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                    }
                });
            }

        } catch (Exception e) {
        }


        mcallback = new GetCurrentLocation.CurrentLocationCallback() {

            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    lat = location.getLatitude();


                    lng = location.getLongitude();
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                    General.setSharedPreferences(getContext(),AppConstants.MY_LAT,lat + "");
                    General.setSharedPreferences(getContext(),AppConstants.MY_LNG,lng + "");
                    if (isNetworkAvailable()) {
                        try {
                            getRegion();
                            new LocationUpdater().execute();

                        } catch (Exception e) {
                            Log.i("Exception", "caught in get region");
                        }
                    }

                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    // LatLng currentLocation= new LatLng(lat, lng);

                    // Log.i("t1","lat_long"+currentLocation);


                    //Log.i("t1", "caught"+width+ " "+height);
                    //broker_map.
                    //broker_map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("I am here!").icon(icon1).anchor(x,y));


                    //broker_map.addMarker(new MarkerOptions().position(currentLocation).title("current Location"));
                    // broker_map.animateCamera(CameraUpdateFactory.newLatLng(currentLocation));
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
        Log.i("t2", "mcallback" + mcallback);


        if (!dbHelper.getValue(DatabaseConstants.userId).equalsIgnoreCase("null"))
            droomChatFirebase.getDroomList(dbHelper.getValue(DatabaseConstants.userId), getActivity());

        dbHelper.save(DatabaseConstants.userRole, "Client");

        rupeesymbol.bringToFront();
        tvRate.bringToFront();
        ll_marker.bringToFront();
       // txtFilterValue.setText(Html.fromHtml(getResources().getString(R.string.default_2_bhk)));
        txtFilterValue.setText("2BHK");


        /**
         * animate views
         */
        mFlipAnimator = ValueAnimator.ofFloat(0f, 1f);
        mFlipAnimator.addUpdateListener(new FlipListener(mVisits, txtFilterValue));

        StartAnimation();
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (isFlipped()) {
//                                Log.i("mFlipAnimator","is flipped");
//                                mFlipAnimator.reverse();
//                            } else {
//                                Log.i("mFlipAnimator","is flipped not");
//                                mFlipAnimator.start();
//                            }
//                        }
//                    });
//                }
//            }
//        }, 2000, 2000);





//for(int i=1;i<10;i++) {
//    ImageView image1 = (ImageView) rootView.findViewById(R.id.beacons1);
//    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade);
//    // animation = AnimationUtils.loadAnimation(getContext(), R.anim.zoomout);
//    image1.startAnimation(animation);
//

//}





        return rootView;
    }

    private BroadcastReceiver closeOyeScreenSlide = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.i("inside notification","=======");
            UpdateRatePanel();
            ll_map.setAlpha(1f);
            StartAnimation();
        }
    };
    private void StartAnimation()
    {
        Log.i("starting timer"," "+ timer);
        if(timer == null) {
            timer = new Timer();

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

        }
    }

    private  void CancelAnimation()
    {
        try {
            if (timer != null) {
                timer.cancel();

                timer = null;

//        if (isFlipped()) {
                mFlipAnimator.start();
//        }
            }
        }catch(Exception e){}
    }




    public void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (map != null) {
            // Access to the location has been granted to the app.
//            LocationManager lm;
//            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            Location location = lm.getL astKnownLocation(LocationManager.GPS_PROVIDER);
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();
            map.setMyLocationEnabled(true);
        }
    }

    @OnClick(R.id.txtFilterValue)
    public void onTxtFilterValueClick(View v) {

        openOyeScreen();
        Log.i("txtFilterValue","txtFilterValue =========================== "+SystemClock.currentThreadTimeMillis());
        CancelAnimation();

        if(RatePanel==true) {
            UpdateRatePanel();
            RatePanel = false;
        }
        else {
            RatePanel = true;
           // tvFetchingrates.setVisibility(View.VISIBLE);
        }
    }

    private void openOyeScreen() {



//        if(android.os.Build.VERSION.SDK_INT >18) {
//            Log.i("FLipanimator paused", "fipanimator paused");
//            mFlipAnimator.end();
//        }

        Intent intent = new Intent(AppConstants.ON_FILTER_VALUE_UPDATE);
        intent.putExtra("tv_dealinfo","Home ");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

        //mFlipAnimator.end();
//        mFlipAnimator.removeAllUpdateListeners();
//        //FlipListener f = new FlipListener(txtFilterValue);
//        mFlipAnimator = ValueAnimator.ofFloat(0f);
//        mFlipAnimator.addUpdateListener(new FlipListener(txtFilterValue));
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (isFlipped()) {
//                                Log.i("mFlipAnimator","is flipped");
//                                mFlipAnimator.reverse();
//                            } else {
//                                Log.i("mFlipAnimator","is flipped not");
//                                mFlipAnimator.start();
//                            }
//                        }
//                    });
//                }
//            }
//        }, 2000, 2000);


//        mFlipAnimator.end();
//        mFlipAnimator.start();
//        mFlipAnimator = ValueAnimator.ofFloat(0f);
//
//        mFlipAnimator.addUpdateListener(new FlipListener(txtFilterValue, txtFilterValue));
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (isFlipped()) {
//                                mFlipAnimator.reverse();
//                            } else {
//                                mFlipAnimator.start();
//                            }
//                        }
//                    });
//                }
//            }
//        }, 2000, 2000);
//        //mFlipAnimator.end();
//        mFlipAnimator.removeAllUpdateListeners();

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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(closeOyeScreenSlide, new IntentFilter(AppConstants.CLOSE_OYE_SCREEN_SLIDE));


    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onFilterValueUpdate);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(closeOyeScreenSlide);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // getLocationActivity.setCallback(null);
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

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("PREOK", "getcontext " + General.getDeviceId(getContext()));
            user.setDeviceId(General.getDeviceId(getContext()));


        } else {
            // preok.setDeviceId(General.getSharedPreferences(this,AppConstants.));
            user.setDeviceId(General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));
            Log.i("PREOK", "getcontext " + General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));

        }

        user.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        user.setUserRole("client");
        user.setLongitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
        user.setProperty_type("home");
        user.setLatitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
        Log.i("t1", "My_lng" + "  " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
        user.setLocality(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
        Log.i("t1", "My_lat" + "  " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));

        user.setPlatform("android");
        Log.i("my_locality", SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
        user.setPincode("400058");

        tv_building.setVisibility(View.INVISIBLE);
        horizontalPicker.setVisibility(View.GONE);
        tvRate.setVisibility(View.GONE);
        rupeesymbol.setVisibility(View.GONE);
        tvFetchingrates.setVisibility(View.VISIBLE);
       // tvCommingsoon.setVisibility(View.GONE);
        tvFetchingrates.setText("Fetching Rates....");
        //tvFetchingrates.setm
        //tvCommingsoon.setHeight(18);

      // tvCommingsoon.setTypeface(null, Typeface.BOLD);
        //tvFetchingrates.setTypeface(null, Typeface.ITALIC);
        tvFetchingrates.setTextSize(15);
      //  missingArea.setVisibility(View.VISIBLE);


        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_101).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        UserApiService userApiService = restAdapter.create(UserApiService.class);

        userApiService.getPrice(user, new Callback<GetPrice>() {

            @Override
            public void success(GetPrice getPrice, Response response) {

                try {

                    String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                    Log.e(TAG, "RETROFIT SUCCESS " + getPrice.getResponseData().getPrice().getLlMin().toString());
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));

                    Log.i("TRACE", "Response" + jsonResponseData);
                    if (getPrice.getResponseData().getPrice().getLlMin() != null &&
                            !getPrice.getResponseData().getPrice().getLlMin().equals("")) {

                        Log.i("tt", "I am here price" + getPrice.getResponseData());
                        Log.i("tt", "I am here price" + getPrice.getResponseData().getPrice());
//                       Log.i("tt", "I am here building" + getPrice.getResponseData().getBuildings());
                        if (Integer.parseInt(getPrice.getResponseData().getPrice().getLlMin()) != 0) {
                            Log.i("tt", "I am here" + 2);
                            Log.i("TRACE", "RESPONSEDATAr" + response);
                            llMin = Integer.parseInt(getPrice.getResponseData().getPrice().getLlMin());
                            llMax = Integer.parseInt(getPrice.getResponseData().getPrice().getLlMax());
                            Log.i("TRACE", "RESPONSEDATArr" + llMin);
                            Log.i("TRACE", "RESPONSEDATArr" + llMax);
                            llMin = 5 * (Math.round(llMin / 5));
                            llMax = 5 * (Math.round(llMax / 5));
                            Log.i("TRACE", "RESPONSEDATAr" + llMin);
                            Log.i("TRACE", "RESPONSEDATAr" + llMax);

                            orMin = Integer.parseInt(getPrice.getResponseData().getPrice().getOrMin());
                            orMax = Integer.parseInt(getPrice.getResponseData().getPrice().getOrMax());
                            Log.i("TRACE", "RESPONSEDATArr" + orMin);
                            Log.i("TRACE", "RESPONSEDATArr" + orMax);
                            orMin = 500 * (Math.round(orMin / 500));
                            orMax = 500 * (Math.round(orMax / 500));
                            Log.i("TRACE", "RESPONSEDATAr" + orMin);
                            Log.i("TRACE", "RESPONSEDATAr" + orMax);

                            // private Marker mCustomerMarker;

                            BroadCastMinMaxValue(llMin,llMax,orMin,orMax);
                            updateHorizontalPicker();

                            for (int i = 0; i < 5; i++) {

                                if (mCustomerMarker[i] != null)
                                    mCustomerMarker[i].remove();
                            }


                            //if(mflag=false) {


                            for (int i = 0; i < 5; i++) {
                                name = getPrice.getResponseData().getBuildings().get(i).getName();
                                Log.i("TRACE", "RESPONSEDATAr" + name);

                                or_psf[i] = Integer.parseInt(getPrice.getResponseData().getBuildings().get(i).getOrPsf());
                                Log.i("TRACE", "RESPONSEDATAr" + or_psf);
                                ll_pm[i] = Integer.parseInt(getPrice.getResponseData().getBuildings().get(i).getLlPm());

                                Log.i("TRACE", "RESPONSEDATAr" + ll_pm);
                                double lat = Double.parseDouble(getPrice.getResponseData().getBuildings().get(i).getLoc().get(1));
                                Log.i("TRACE", "RESPONSEDATAr" + lat);
                                double longi = Double.parseDouble(getPrice.getResponseData().getBuildings().get(i).getLoc().get(0));
                                Log.i("TRACE", "RESPONSEDATAr" + longi);
                                loc = new LatLng(lat, longi);
                                Log.i("TRACE", "RESPONSEDATAr" + loc);
                                Log.i("TRACE", "RESPONSEDATAr" + mCustomerMarker[i]);

                                mCustomerMarker[i] = map.addMarker(new MarkerOptions().position(loc).title(name).snippet("Rent:"+ll_pm[i]+" "+"Sale"+ or_psf[i]).icon(icon1));

                                Log.i("TRACE", "RESPONSEDATAr" + mCustomerMarker[i]);
                                flag[i] = false;
                            }
                            //mflag=true;

                            // }
                           // updateHorizontalPicker();

                            horizontalPicker.setVisibility(View.VISIBLE);
                            tv_building.setVisibility(View.VISIBLE);

                            tvRate.setVisibility(View.VISIBLE);
                            rupeesymbol.setVisibility(View.VISIBLE);
                          //  tvCommingsoon.setVisibility(View.INVISIBLE);
                            tvFetchingrates.setVisibility(View.INVISIBLE);

                            missingArea.setVisibility(View.INVISIBLE);
                        } else {
                            Log.i("tt", "I am here" + 3);
                    /*SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .text("We don't cater here yet")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());*/
                            for (int i = 0; i < 5; i++) {

                                if (mCustomerMarker[i] != null)
                                    mCustomerMarker[i].remove();
                            }
                            tv_building.setVisibility(View.INVISIBLE);
                            horizontalPicker.setVisibility(View.GONE);
                            tvRate.setVisibility(View.INVISIBLE);
                            rupeesymbol.setVisibility(View.INVISIBLE);
                           // tvCommingsoon.setVisibility(View.GONE);
                            tvFetchingrates.setVisibility(View.VISIBLE);
                            tvFetchingrates.setText("Coming Soon...");
                           // tvCommingsoon.setTypeface(null, Typeface.BOLD);
                           // tvCommingsoon.setTextSize(18);
                            missingArea.setVisibility(View.VISIBLE);

                        }
                    } else {
                    /*SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .text("We don't cater here yet")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity()); */


                        Log.i("GETPRICE", "Else mode ====== ");

//                        horizontalPicker.setVisibility(View.INVISIBLE);
//                        tvRate.setVisibility(View.INVISIBLE);
//                        rupeesymbol.setVisibility(View.INVISIBLE);
//                        tvFetchingrates.setVisibility(View.INVISIBLE);
//                        tvCommingsoon.setVisibility(View.VISIBLE);
//                        tvCommingsoon.setText("Coming Soon...");
//                        tvCommingsoon.setTypeface(null, Typeface.BOLD);
//                        tvCommingsoon.setTextSize(18);
//                        missingArea.setVisibility(View.VISIBLE);


                    /*missingArea.setAnimation(AnimationUtils.loadAnimation(getActivity(),
                            R.anim.slide_up));*/
                    }
                } catch (Exception e) {
                    Log.i("Price Error", " " + e.getMessage());
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("getPrice", "error: " + error.getMessage());

            }
        });
    }


    // map.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener));


    private void updateHorizontalPicker() {


        if (horizontalPicker != null) {
            if (brokerType.equals("rent")) {
                //   horizontalPicker.setInterval((llMin*1000), (llMax*1000),10, HorizontalPicker.THOUSANDS);


                Log.i("HORRIZONTALPICKER","filterValue "+filterValue+" filterValueMultiplier "+filterValueMultiplier+"  LLmin && LLmax"+llMin+" "+llMax);
                horizontalPicker.setInterval((llMin * filterValueMultiplier), (llMax * filterValueMultiplier), 10, HorizontalPicker.THOUSANDS);
            }

            else

                horizontalPicker.setInterval(orMin, orMax, 10, HorizontalPicker.THOUSANDS);
        }
    }


    public boolean canAccessLocation() {

        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(getContext(),perm));
    }


    //rem


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
try {
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


                        enableMyLocation();
//                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                                return;
//                            }
//                            map.setMyLocationEnabled(true);
                        //setCameraListener();
                        Log.i("t1", "broker_map" + map);
                        //  geoFence.drawPloygon(map);
                    }
                });
                getLocationActivity = new GetCurrentLocation(getActivity(), mcallback);

            } else {
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
                    Log.i("t1", "lat_long_getcurrentlocation" + " " + currentLocation);
                    //  broker_map.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("I am here!").icon(icon1).anchor(x,y));
                    //   broker_map.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("I am here!"));
                    // point= map.getProjection().toScreenLocation(currentLocation);
                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));

                }

            }
        });
        getLocationActivity = new GetCurrentLocation(getActivity(),mcallback);
        //Log.i("t1","mcallback"+""+mcallback);
    }
        else {
        //Intent intent = new Intent(this, MainActivity.class);
       // startActivity(intent);
        Toast.makeText(getContext(), "Offline Mode", Toast.LENGTH_LONG);
          //((DashboardActivity) getActivity()).showToastMessage("Offline Mode");
    }

    }catch (Exception e){}


   }


    @Override
    public void onPositionSelected(int position, int count) {
        if (count == 2) {
            if (position == 0) {
                tvRate.setText("/ month");
                brokerType = "rent";
                dbHelper.save(DatabaseConstants.brokerType, "LL");
                dbHelper.save("brokerType", "On Rent");
                updateHorizontalPicker();
               // BroadCastMinMaxValue(llMin,llMax,orMin,orMax);
                Log.i("Index","index:"+INDEX+" "+MarkerClicked);
                if(flag[INDEX]==true) {
                    Log.i("Index","index:"+INDEX+" "+MarkerClicked);
                    tv_building.setVisibility(View.VISIBLE);
                    tv_building.setText("Average Rate in last 1 WEEK");
                    String text = "<font color=#ffffff>"+mCustomerMarker[INDEX].getTitle().toString()+"</b></b></font> <font color=#ffffff>@</font>&nbsp&nbsp<font color=#ff9f1c>\u20B9"+General.currencyFormat(String.valueOf(ll_pm[INDEX])).substring(2,General.currencyFormat(String.valueOf(ll_pm[INDEX])).length())+"</font><b><font color=#ff9f1c><sub>/m</sub></font>";
                    tvFetchingrates.setText(Html.fromHtml(text));

                }

                  //  onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),950,llMin*filterValueMultiplier,llMax*filterValueMultiplier);

                updateHorizontalPicker();

            } else if (position == 1) {
                tvRate.setText("/ sq.ft");
                brokerType = "resale";
                dbHelper.save(DatabaseConstants.brokerType, "OR");
                dbHelper.save("brokerType", "For Sale");
               // BroadCastMinMaxValue(llMin,llMax,orMin,orMax);
                if(flag[INDEX]==true) {
                    tv_building.setVisibility(View.VISIBLE);
                    tv_building.setText("Average Rate in last 1 WEEK");
                    String text = "<font color=#ffffff>"+mCustomerMarker[INDEX].getTitle().toString()+"</b></b></font> <font color=#ffffff> @ </font>&nbsp<font color=#ff9f1c>\u20B9"+General.currencyFormat(String.valueOf(or_psf[INDEX])).substring(2,General.currencyFormat(String.valueOf(or_psf[INDEX])).length())+"</font><b><font color=#ff9f1c><sub>/psf</sub></font>";
                    tvFetchingrates.setText(Html.fromHtml(text));
                }
               // onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),950,orMin,orMax);

                updateHorizontalPicker();
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ll_map.setAlpha(1f);
        map.animateCamera(CameraUpdateFactory.zoomTo(12));
        autoCompView.clearListSelection();
        //rem
        getLocationFromAddress(autoCompView.getText().toString());
        if (isNetworkAvailable()) {
            new LocationUpdater().execute();
        }
    }

    public void getRegion() {
        String lat1 = SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT);
        String lng1 = SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lat1), Double.parseDouble(lng1), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            region = addresses.get(0).getSubLocality();
            SharedPrefs.save(getActivity(), SharedPrefs.MY_LOCALITY, region);
            General.setSharedPreferences(getContext(),AppConstants.LOCALITY,region);
            Log.i("localityBroadcast","localityBroadcast3 "+region);

            Intent intent = new Intent(AppConstants.LOCALITY_BROADCAST);
            intent.putExtra("locality",region);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

            pincode = addresses.get(0).getPostalCode();
            // fullAddress = "";
        } catch (Exception e) {
        }
        //for(int i=0; i<addresses.get(0).getMaxAddressLineIndex(); i++){
        //    fullAddress += addresses.get(0).getAddressLine(i);
        //}
        // SharedPrefs.save(getActivity(), SharedPrefs.MY_REGION, fullAddress);
//        if (addresses.size() > 0) {
//            pincode = addresses.get(0).getPostalCode();
//
//            //if 1st provider does not have data, loop through other providers to find it.
//            int count = 0;
//            while (pincode == null && count < addresses.size()) {
//                pincode = addresses.get(count).getPostalCode();
//                count++;
//            }
//        }
        // return fullAddres;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (isNetworkAvailable()) {

            //lat = cameraPosition.target.latitude;
            // lng = cameraPosition.target.longitude;
            // LatLng currentLocation1;
            // Point p= new Point(x,y);
            // currentLocation1= broker_map.getProjection().fromScreenLocation(p);
            // lat=currentLocation1.latitude;
            //Log.i("t1","lat"+" "+lat);
            // lng=currentLocation1.longitude;
            Log.i("t1", "lat_target" + lat);
            Log.i("t1", "lng_target" + lng);
            //LatLng  currentLocation1= new LatLng(lat, lng);
            //  broker_map.addMarker(new MarkerOptions().position(currentLocation1).title("marker"));
            //SharedPrefs.save(getActivity(),SharedPrefs.MY_LAT,lat+"");
            //SharedPrefs.save(getActivity(),SharedPrefs.MY_LNG,lng+"");
            // broker_map.addMarker(new MarkerOptions().position(currentLocation1).title("marker"));

            // new LocationUpdater().execute();
        }
    }

    //@Override
    public void onPositionSelected(int position) {
        // Toast.makeText(getActivity(), "Selected position:" + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void sendData(HashMap<String, HashMap<String, String>> hashMap) {
        chatListData = hashMap;
        Log.i("chatdata in rex", chatListData.toString());
    }

    @Override
    public void priceSelected(String val) {
try {
    map.animateCamera(CameraUpdateFactory.zoomTo(12));
}
catch(Exception e){
    Log.i(TAG,"Caught in exception Zoom map to 12 "+e);
}

    }

    @Override
    public void drawerOpened() {
        horizontalPicker.stopScrolling();
    }





    protected class LocationUpdater extends AsyncTask<Double, Double, String> {
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
                String lat1 = SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT);
                String lng1 = SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG);
                JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat1 + ","
                        + lng1 + "&sensor=true");
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
                                Address1 += " " + long_name;
                            } else if (Type.equalsIgnoreCase("sublocality_level_2")) {
                                Address2 = long_name;
                            } else if (Type.equalsIgnoreCase("sublocality_level_1")) {
                                Address2 += " " + long_name;
                                if (getActivity() != null) {
                                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LOCALITY, long_name);
                                    General.setSharedPreferences(getContext(), AppConstants.LOCALITY, region);
                                }
                            } else if (Type.equalsIgnoreCase("locality")) {
                                // Address2 = Address2 + long_name + ", ";
                                City = long_name;
                                SharedPrefs.save(getActivity(), SharedPrefs.MY_CITY, City);
                            } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                                County = long_name;
                            } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                State = long_name;
                            } else if (Type.equalsIgnoreCase("country")) {
                                Country = long_name;
                            } else if (Type.equalsIgnoreCase("postal_code")) {
                                PIN = long_name;
                                SharedPrefs.save(getActivity(), SharedPrefs.MY_PINCODE, PIN);
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
            Log.i("", "");
            autoCompView.dismissDropDown();
            // new LocationUpdater().execute();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean setCameraListener() {
        //broker_map.setOnCameraChangeListener(this);
        return false;
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


    public void getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        //GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                //return null;
            }
            Address location = address.get(0);
            lat = location.getLatitude();
            lng = location.getLongitude();
            Log.i("lat=" + location.getLatitude(), " long=" + location.getLongitude());
            LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("t1", "lng" + " " + lng);
            SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
            SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
            General.setSharedPreferences(getContext(),AppConstants.MY_LAT,lat + "");
            General.setSharedPreferences(getContext(),AppConstants.MY_LNG,lng + "");


            //Marker marker = broker_map.addMarker(new MarkerOptions()
            //     .position(l)
            //  .title("Title")
            //.snippet("Description")
            //.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), Mmarker))));

            //Marker m = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("I am here!").icon(icon1).anchor(x,y));
            // broker_map.animateCamera(CameraUpdateFactory.newLatLng(l));
            map.moveCamera(CameraUpdateFactory.newLatLng(l));
            // broker_map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));

           // getPrice();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getContext().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }





    private void BroadCastMinMaxValue(int llMin, int llMax,int orMin,int orMax) {

        int llmin=numToVals(llMin);
        int llmax=numToVals(llMax);


        Intent intent = new Intent(AppConstants.BROADCAST_MIN_MAX_VAL);
        intent.putExtra("llmin", llmin);
        intent.putExtra("llmax", llmax);
        intent.putExtra("ormin", orMin);
        intent.putExtra("ormax", orMax);
        //intent.putExtra("tv_dealinfo",oyeButtonData);

        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);


    }

public void onoyeclickRateChange(String locality,int area,int llmin,int llmax,String psf){
    horizontalPicker.setVisibility(View.GONE);
    tv_building.setVisibility(View.GONE);
    tvRate.setVisibility(View.GONE);
    rupeesymbol.setVisibility(View.GONE);
    tvFetchingrates.setVisibility(View.VISIBLE);

    String llmin1;
    String llmax1;
    llmin1=numToVal(llmin);
    llmax1=numToVal(llmax);

Log.i("TRACE11","llmin"+llmin);
    Log.i("TRACE11","llmax "+llmax);
    Log.i("TRACE11","llmin "+llmin1);
    Log.i("TRACE11","llmax"+llmax1);
    tv_building.setVisibility(View.VISIBLE);
    tv_building.setText("Range @ "+locality+" | AREA = "+area +"sqft");
    Log.i("TRACE11","tv_building"+tv_building.getText());
    text = "<font color=#ff9f1c><b>\u20B9</b> "+llmin1+"<sub> "+psf+" </sub></b></b> <b> - </b> <b>\u20B9</b>"+llmax1+"<b><sub>"+psf+"</sub></font>";
    tvFetchingrates.setText(Html.fromHtml(text));


}



   public void UpdateRatePanel(){
       horizontalPicker.setVisibility(View.VISIBLE);
       tv_building.setText("Average Rate @ This Locality");
       tvRate.setVisibility(View.VISIBLE);
       rupeesymbol.setVisibility(View.VISIBLE);
       tvFetchingrates.setVisibility(View.GONE);

    }


    String numToVal(int no){
        String str = "",v = "";

        int twoWord = 0,val = 1;

        int c = (no == 0 ? 1 : (int)(log10(no)+1));

        if (c > 8) {

            c = 8;
        }
        if (c%2 == 1){

            c--;
        }

        c--;
        //   int q = Int(pow(Double(10),Double(c)))
        switch(c)
        {
            case 7:
//            if(propertyType)
                val = no/10000000;
//            else
//                val = no/100000;
                no = no%10000000;
                String formatted = String.format("%07d", no);
                formatted = formatted.substring(0,1);

                v = val+"."+formatted;
                str = v+" cr";


                twoWord++;
                break;

            case 5:

                val = no/100000;

                v = val+"";
                no = no%100000;
                String s2 = String.format("%05d", no);
                s2 = s2.substring(0,1);

                if (val != 0){
                    str = str+v+"."+s2+" lacs";
                    twoWord++;
                }

                break;

            case 3:
                val = no/1000;
                v = val+"";
                no = no%1000;
                String.format("%05d", no);
                String s3 = String.format("%03d", no);
                s3 = s3.substring(0,1);
                if (val != 0) {
                    str = str+v+"."+s3+" k";
                }
                break;
            default :
                // print("noToWord Default")
                break;
        }
        return str;
    }

    public int numToVals(int no){
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        int truncate_first;
        if(currentapiVersion>=23)
            truncate_first = 2;
        else
            truncate_first = 3;

        Log.i("TRACED","no is"+no);
        String str = "",v = "";

        int twoWord = 0,val = 1;

        int c = (no == 0 ? 1 : (int)(log10(no)+1));

        if (c > 8) {

            c = 8;
        }
        if (c%2 == 1){

            c--;
        }

        c--;
        //   int q = Int(pow(Double(10),Double(c)))
        switch(c)
        {
            case 7:

                val=no;



                twoWord++;
                break;

            case 5:
                val=no;



                    twoWord++;

                break;

            case 3:

                val=no;

                    //str = str+v+"."+s3+"K";


                break;
            default :
                // print("noToWord Default")
                break;
        }
        Log.i("TRACE","budget string"+str);
        return val;
    }




}



















   /* @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                    customMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            map = googleMap;
                            //lat = 19.1269299;
                            //lng = 72.8376545999999;
                            enableMyLocation();
                               if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                    map.setMyLocationEnabled(true);
                                    return;
                               }
                            lat = gpsTracker.getLatitude();
                            Log.i("lat", "===================" + lat);
                            lng = gpsTracker.getLongitude();
                            Log.i("lat", "===================" + lng);
                            SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                            SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                            getPrice();
                            new LocationUpdater().execute();
                            LatLng center = new LatLng(lat,lng);
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 12));


                        }
                    });

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/


//}




