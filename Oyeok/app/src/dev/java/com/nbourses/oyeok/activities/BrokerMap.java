package com.nbourses.oyeok.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.JsonElement;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.fragments.AddBuilding;
import com.nbourses.oyeok.fragments.AddListing;
import com.nbourses.oyeok.fragments.AddListingFinalCard;
import com.nbourses.oyeok.fragments.BuildingOyeConfirmation;
import com.nbourses.oyeok.fragments.MainScreenPropertyListing;
import com.nbourses.oyeok.fragments.searchFragment;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.AddBuildingModel;
import com.nbourses.oyeok.models.buildingCacheModel;
import com.nbourses.oyeok.realmModels.BuildingCacheRealm;
import com.nbourses.oyeok.realmModels.MyPortfolioModel;
import com.nbourses.oyeok.realmModels.addBuildingRealm;
import com.nbourses.oyeok.widgets.HorizontalPicker.HorizontalPicker;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import static com.nbourses.oyeok.R.id.container_Signup;
import static com.nbourses.oyeok.helpers.AppConstants.LOCATION_PERMISSION_REQUEST_CODE;

//implements CustomPhasedListener
public class BrokerMap extends AppCompatActivity implements CustomPhasedListener,HorizontalPicker.pickerPriceSelected ,AdapterView.OnItemClickListener{

    @Bind(R.id.addressBar1)
    TextView addressBar;

    @Bind(R.id.addressPanel1)
    LinearLayout addressPanel;

    @Bind(R.id.buildingIcon)
    ImageView buildingIcon;

    @Bind(R.id.fav)
    ImageView fav;

    @Bind(R.id.missingArea)
    RelativeLayout missingArea;

    @Bind(R.id.cancel_btn)
    TextView cancel_btn;

    @Bind(R.id.confirm_screen_title)
    TextView confirm_screen_title;

    @Bind(R.id.btnMyDeals)
    Button btnMyDeals;

    @Bind(container_Signup)
    FrameLayout containerSignup;

    @Bind(R.id.card)
    FrameLayout card;

    @Bind(R.id.back_btn)
    TextView btn_back;

    @Bind(R.id.btncancel)
    TextView btn_cancel;

    @Bind(R.id.setbaseloc1)
    LinearLayout setbaseloc;

    @Bind(R.id.tv_change_region1)
    TextView tv_change_region;

    private final String TAG = BrokerMap.class.getSimpleName();
    private CustomPhasedSeekBar mPhasedSeekBar;
    ImageView search_building_icon;
    TextView mVisits,txtFilterValue,rupeesymbol,tvRate,tvFetchingrates,tv_building,txt_info,addBText;
    LinearLayout recordWorkout,addlistinglayout,addlistingText;

    // map Variables
    GoogleMap map;
    CustomMapFragment customMapFragment;
    View mHelperView;
    private Point centerPoint;
    private GetCurrentLocation getLocationActivity;
    private GetCurrentLocation.CurrentLocationCallback mcallback;
    private static final int MAP_ZOOM = 14;
    private Double lat,lng;
    private String pincode, region,address, fullAddress,brokerType,B_name;
    private Geocoder geocoder;
    private String Address1 = "", Address2 = "", City = "Mumbai", State = "", Country = "", County = "", PIN = "", fullAddres = "";
    private static final int INITIAL_REQUEST = 133;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    /*private static final int REQUEST_CALL_PHONE = 1;
    private  Intent callIntent;*/
    private HorizontalPicker horizontalPicker;
    private boolean savebuilding,spanning;
    private long lastTouched = 0, start = 0;
    private static final long SCROLL_TIME = 200L;

    ////getprice variable
    private String  text;
    private int llMin=35, llMax=60, orMin=21000, orMax=27000;
    private String[] config=new String[5],rate_growth =new String[5],name = new String[5],listing =new String[5],portal= new String[5],transaction=new String[5],id=new String[5],distance=new String[5];
    Marker[] mCustomerMarker = new Marker[5];
    private int[] or_psf = new int[5], ll_pm = new int[5];
    private LatLng []loc=new LatLng[5];

    //Icon variable

    private BitmapDescriptor icon1;
    private BitmapDescriptor icon2;
    private BitmapDescriptor iconHome;
    private BitmapDescriptor iconOffice;
    private BitmapDescriptor iconOther;
    private Drawable sort_down_black,sort_down_red,sort_up_black,sort_up_green,comman_icon;
    private Realm realm;

    //Marker data Array List
    ArrayList<buildingCacheModel> buildingCacheModels=new ArrayList<>();
    ArrayList<Marker> customMarker=new ArrayList<>();
//search bar variables
    AutoCompleteTextView autoCompView;
    private LinearLayout seekbar_linearlayout;

    private static int INDEX=0,width, height;

///init() variables
    Button home,shop,industrial,office,addbuilding;
    String Property_type="Home",oyetext="2BHK";
    LinearLayout dispProperty,my_loc;
    RelativeLayout property_type_layout;
    TextView rental,resale,btn_add_building,btn_add_listing;
    private Boolean pro_click=false,buidingInfoFlag=false,Signupflag=false,searchflag=false;
    ///Animation variable
    Animation zoomout_right, slide_up, zoomout_left, ani, zoomin_zoomout,slide_up1,slide_left,slideDown,slideUp,bounce;
    FrameLayout map_parent,fr;


    //String   listing,transaction,portal,config;


    private BroadcastReceiver resetMap = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //if(AppConstants.SEARCHFLAG) {
            Log.i(TAG,"aalo re ");
            AppConstants.SEARCHFLAG = false;
            LatLng currentLocation = new LatLng(AppConstants.MY_LATITUDE,AppConstants.MY_LONGITUDE);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,MAP_ZOOM));
            General.setSharedPreferences(getBaseContext(),AppConstants.MY_LAT,AppConstants.MY_LATITUDE+"");
            General.setSharedPreferences(getBaseContext(),AppConstants.MY_LNG,AppConstants.MY_LONGITUDE+"");
            // map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            /*lat=AppConstants.MY_LATITUDE;
            lng=AppConstants.MY_LONGITUDE;*/
            //getRegion();
            new LocationUpdater().execute();
            getPrice();

            if(!AppConstants.SETLOCATION &&!savebuilding) {
                buildingTextChange(General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY), 950);
            }

            //}
        }
    };

    private BroadcastReceiver phasedSeekBarClicked = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i( "prassanababaiasm", "index of layoutsusussjcdnck : " + AppConstants.SETLOCATION+"  "+ savebuilding +"  "+intent.getExtras().getString("phaseseek"));
            if(!AppConstants.SETLOCATION && !savebuilding){
                if (intent.getExtras().getString("phaseseek") != null) {
                    if ((intent.getExtras().getString("phaseseek").equalsIgnoreCase("clicked"))) {
                        try {

                                Log.i( "indexxx", "index of layoutsusussjcdnck : " );
                                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild( property_type_layout );
                                Log.i( "indexxx", "index of layoutsusussjcdnck : " + index );
                                if (index !=2) {
                                    property_type_layout.clearAnimation();
                                    map_parent.removeView( property_type_layout );
                                    map_parent.addView( property_type_layout, 2 );
                                }
                                PropertyButtonSlideAnimation();

                        } catch (Exception e) { }

                    }
                }
            }
        }
    };








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_map);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnMyDeals.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Live Building Rates");
        //confirm_screen_title.setText("Live Building Rates");
        //getSupportActionBar().setTitle("");
        setbaseloc.setVisibility(View.VISIBLE);
        tv_change_region.setText(SharedPrefs.getString(getBaseContext(),SharedPrefs.MY_CITY));
        mVisits = (TextView) findViewById(R.id.newVisits);
        txtFilterValue = (TextView) findViewById(R.id.txtFilterValue);
        mHelperView = (View) findViewById(R.id.br_helperView1);
        tvFetchingrates = (TextView) findViewById(R.id.tvFetchingRates);
        tvRate = (TextView) findViewById(R.id.tvRate1);
        rupeesymbol = (TextView) findViewById(R.id.rupeesymbol1);
        tv_building = (TextView) findViewById(R.id.tv_building);
        recordWorkout = (LinearLayout) findViewById(R.id.recordWorkout);
        search_building_icon = (ImageView) findViewById(R.id.selected_property);
        txt_info=(TextView)findViewById(R.id.txt_info);
        horizontalPicker = (HorizontalPicker) findViewById(R.id.picker);
        seekbar_linearlayout = (LinearLayout) findViewById(R.id.seekbar_linearlayout);
        map_parent=(FrameLayout) findViewById(R.id.map_parent);
        addbuilding=(Button)findViewById(R.id.addbuilding);
        addlistinglayout=(LinearLayout) findViewById(R.id.addlistinglayout);
        my_loc=(LinearLayout) findViewById(R.id.my_loc);
        ///save and done change on phase seek bar
        addlistingText=(LinearLayout) findViewById(R.id.addlistingText);
        addBText = (TextView) findViewById(R.id.addBText);

        ///To show done and continue to listing
        btn_add_building=(TextView) findViewById(R.id.btn_add_building);
        btn_add_listing=(TextView) findViewById(R.id.btn_add_listing);
        //oye_arrow=(TextView) findViewById(R.id.oye_arrow);




        btn_add_building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addbuilding();
                //Reset();
               /* Intent in = new Intent(getBaseContext(), MyPortfolioActivity.class);
                startActivity(in);*/
            }
        });

        btn_add_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                General.setSharedPreferences(getBaseContext(), AppConstants.BUILDING_LOCALITY,General.getSharedPreferences(getBaseContext(),AppConstants.LOCALITY));

                openAddListingFinalCard();
            }
        });


        init();
        try {//buildingiconbeforeclick
            icon1 = BitmapDescriptorFactory.fromResource(R.drawable.buildingiconbeforeclick);
            icon2 = BitmapDescriptorFactory.fromResource(R.drawable.buildingicononclick);
            sort_down_black = getBaseContext().getResources().getDrawable(R.drawable.sort_down_black);
            sort_down_red = getBaseContext().getResources().getDrawable(R.drawable.sort_down_red);
            sort_up_black = getBaseContext().getResources().getDrawable(R.drawable.sort_up_black);
            sort_up_green = getBaseContext().getResources().getDrawable(R.drawable.up);
            iconHome = BitmapDescriptorFactory.fromResource(R.drawable.favhome);
            iconOffice = BitmapDescriptorFactory.fromResource(R.drawable.favoffice);
            iconOther = BitmapDescriptorFactory.fromResource(R.drawable.favother);
           // favIcon = iconHome;
        }
        catch (Exception e)
        {
            Log.i("BITMAP", "message " + e.getMessage());
        }


        onPositionSelected(0, 3);

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


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getBaseContext(), MyPortfolioActivity.class);
                in.putExtra("fav","fav");
                startActivity(in);
            }
        });

//// phase seekbar code
        if(General.getSharedPreferences(getBaseContext(),AppConstants.CALLING_ACTIVITY).equalsIgnoreCase("PC")){

            General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, "");
            openAddListing();
        }
        mPhasedSeekBar = (CustomPhasedSeekBar) findViewById(R.id.phasedSeekBar1);
        mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(this.getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"10", "15"}, new String[]{this.getResources().getString(R.string.Rental), this.getResources().getString(R.string.Resale)}));
        mPhasedSeekBar.setListener((this));
        mVisits.setBackground(this.getResources().getDrawable(R.drawable.bg_animation));
        txtFilterValue.setBackground(this.getResources().getDrawable(R.drawable.oye_button_border));

        /*try
        {*/
            //if (isNetworkAvailable())

            customMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.g_map));
            map = customMapFragment.getMap();

            final View mMapView = customMapFragment.getView();
            map.getUiSettings().setRotateGesturesEnabled(false);

            map.getUiSettings().setScrollGesturesEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setZoomGesturesEnabled(true);

        customMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                final LocationManager Loc_manager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
                if (!isNetworkAvailable() || !(Loc_manager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                    double lat11 = 19.1269299;
                    double lng11 = 72.8376545999999;
                    Log.i("slsl", "location====================:1 ");
                    LatLng currLatLong = new LatLng(lat11, lng11);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currLatLong, 14));
                }

                enableMyLocation();
                Log.i("slsl", "location====================: ");
                getLocationActivity = new GetCurrentLocation(getBaseContext(), mcallback);
                // map.setPadding(left, top, right, bottom);
                map.setPadding(0, -10, 0, 0);

                Log.i("centerPoint", "centerPoint : " + centerPoint);
            }
        });
           // mHelperView = findViewById(R.id.br_helperView1);
            mHelperView.setOnTouchListener(new View.OnTouchListener() {
                private float scaleFactor = 1f;

                @Override
                public boolean onTouch(final View view, final MotionEvent motionEvent) {
                    if (simpleGestureDetector.onTouchEvent(motionEvent)) { // Double tap
                        map.animateCamera(CameraUpdateFactory.zoomIn()); // Fixed zoom in
                    } else if (motionEvent.getPointerCount() == 1) { // Single tap
                        // horizontalPicker.keepScrolling();
                        onMapDrag(motionEvent);
                        mMapView.dispatchTouchEvent(motionEvent); // Propagate the event to the map (Pan)
                    } else if (scaleGestureDetector.onTouchEvent(motionEvent)) { // Pinch zoom
                         spanning = true;
                        map.moveCamera(CameraUpdateFactory.zoomBy( // Zoom the map without panning it
                                (map.getCameraPosition().zoom * scaleFactor
                                        - map.getCameraPosition().zoom) / 5));
                    }
                    return true; // Consume all the gestures
                }

                // Gesture detector to manage double tap gestures
                private GestureDetector simpleGestureDetector = new GestureDetector(
                        getBaseContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        return true;
                    }
                    /* public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                                     int dx = (int) (e2.getX() - e1.getX());
                                     return true;
                                 }*/
                });
                private GestureDetector simpleGestureDetector1 = new GestureDetector(
                        getBaseContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        Log.i("sushil", "in fling:  ");
                        return true;
                    }
                });
                // Gesture detector to manage scale gestures
                private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(
                        getBaseContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        scaleFactor = detector.getScaleFactor();
                        return true;
                    }
                });
            });


        /*} catch (Exception e) {
        }*/

        fr=(FrameLayout)findViewById(R.id.list_container);
        //int height=fr.getHeight();
        //int width=fr.getWidth();

        ViewTreeObserver vto = fr.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fr.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                width  = fr.getMeasuredWidth();
                height = fr.getMeasuredHeight();
                Log.i("measurement","frag me========: "+height+"  ; "+width);
                Bundle b=new Bundle();
                b.putString("height1",height+"");
                // Log.i("measurement","frag me========: "+height+"  ; "+width);
                MainScreenPropertyListing mainScreenPropertyListing= new MainScreenPropertyListing();
                loadFragmentAnimated(mainScreenPropertyListing,b,R.id.list_container,"");

            }
        });



        setbaseloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment c1 = new searchFragment();
                loadFragmentAnimated(c1, null, R.id.container_Signup, "Search1");
                searchflag=true;
            }
        });


        my_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimer(200, 50) {
                    public void onTick(long millisUntilFinished) {
                        /*if (!AppConstants.SETLOCATION && !savebuilding) {
//                                 ( (ClientMainActivity)getActivity()).closeOyeScreen();
                            ((ClientMainActivity) getActivity()).CloseBuildingOyeComfirmation();
                            if(!General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                                Intent intent = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);
                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                                ((ClientMainActivity) getActivity()).closeOyeConfirmation();
                            }
                            txtFilterValue.setTextSize(13);
                            txtFilterValue.setTextColor(Color.parseColor("white"));
                            txtFilterValue.setText(oyetext);
                            buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                            recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                            map.getUiSettings().setAllGesturesEnabled(true);
                            mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
                            txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));
                           // UpdateRatePanel();
                            search_building_icon.setVisibility(View.GONE);
                            buildingIcon.setVisibility(View.GONE);
                            fav.setVisibility(View.VISIBLE);

                        }*/
                        txtFilterValue.setTextSize(13);
                        txtFilterValue.setTextColor(Color.parseColor("white"));
                        txtFilterValue.setText(oyetext);
                        //txtFilterValue.setText("Home");
                        recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                        map.getUiSettings().setAllGesturesEnabled(true);
                        buildingTextChange(General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY), 950);


                        mVisits.setBackground(getBaseContext().getResources().getDrawable(R.drawable.bg_animation));
                        txtFilterValue.setBackground(getBaseContext().getResources().getDrawable(R.drawable.oye_button_border));
                        // UpdateRatePanel();
                        search_building_icon.setVisibility(View.GONE);
                        buildingIcon.setVisibility(View.GONE);
                        fav.setVisibility(View.VISIBLE);

                    }
                    public void onFinish() {
                        getLocationActivity = new GetCurrentLocation(getBaseContext(), mcallback);
                    }
                }.start();
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                /*Log.i("MA999999 ", "MAP CLICK=========");
                   if(!AppConstants.SETLOCATION && !savebuilding) {

           *//*tvRate.setVisibility(View.VISIBLE);
           rupeesymbol.setVisibility(View.VISIBLE);*//*
                      */
                onMapclicked();
            }
        });



        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                Log.i("inside getinfo","==========");
                LatLng latLng = arg0.getPosition();
                View v1;
//                    v1 = inflater.inflate(R.layout.info_window_layout, null);
                v1 = getLayoutInflater().inflate(R.layout.info_window_layout, null);
                ImageView rate_change_img = (ImageView) v1.findViewById(R.id.rate_change_img);
                TextView rate_change_value = (TextView) v1.findViewById(R.id.rate_change_value);
                String rate="0";
                for(int i=0;i<customMarker.size();i++) {
                    if (arg0.getId().equals(customMarker.get(i).getId())) {
                        if (buildingCacheModels.get(i).getFlag() == false)
                            rate = buildingCacheModels.get(i).getRate_growth();
                    }
                }
                if (Integer.parseInt(rate) < 0){
                    comman_icon=sort_down_red;
                    rate_change_value.setTextColor(Color.parseColor("#ffb91422"));// FFA64139 red
                    rate_change_img.setBackground(comman_icon);
                    rate_change_value.setText(rate.subSequence(1, rate.length())+" %");
                }
                else if(Integer.parseInt(rate) > 0){
                    comman_icon = sort_up_green;
                    rate_change_value.setTextColor(Color.parseColor("#2dc4b6"));// FF377C39 green FF2CA621   FFB91422
                    rate_change_img.setBackground(comman_icon);
                    rate_change_value.setText(Integer.parseInt(rate)+" %");
                }
                else{
                    rate_change_img.setBackground(null);
                    rate_change_value.setTextColor(Color.parseColor("black"));
                    rate_change_value.setText(Integer.parseInt(rate)+" %");
                }
                return v1;
            }
            @Override
            public View getInfoContents(Marker arg0) {

                return null;
            }
        });


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Marker m;
                //intent =new Intent(getContext(), ClientMainActivity.class);
                int i;
                Log.i(" sushil flag[i] ==  ", "===========================  Entry to marker listner :  "+marker.getId().toString()+" "+ horizontalPicker.getValues());
               // if(MarkerClickEnable) {
                    for (i = 0; i < customMarker.size(); i++) {
                        if (marker.getId().equals(customMarker.get(i).getId())) {
                            INDEX = i;
                            Log.i("1sushil11", "===========================   :  " + marker.getId().toString() + " " + customMarker.get(i).getId().toString() + " " + buildingCacheModels.get(i).getFlag());
                            if (buildingCacheModels.get(i).getFlag() == false) {
                                Log.i("1sushil11", "===========================");
                                customMarker.get(i).setIcon(icon2);
                                customMarker.get(i).showInfoWindow();
                               // markerSelected();
                                OpenBuildingOyeConfirmation(buildingCacheModels.get(i).getListing(),buildingCacheModels.get(i).getTransactions(),buildingCacheModels.get(i).getPortals(),buildingCacheModels.get(i).getConfig());
                                SaveBuildingDataToRealm();
                                buildingIcon.setVisibility(View.VISIBLE);
                                fav.setVisibility(View.GONE);
                                horizontalPicker.setVisibility(View.GONE);
                                tvRate.setVisibility(View.GONE);
                                rupeesymbol.setVisibility(View.GONE);
                                recordWorkout.setBackgroundColor(Color.parseColor("#ff9f1c"));
                                mVisits.setBackground(getBaseContext().getResources().getDrawable(R.drawable.oyebutton_bg_color_yellow));
                                txtFilterValue.setBackground(getBaseContext().getResources().getDrawable(R.drawable.oye_bg_color_white));
                                String text1;
                                //="<font color=#ffffff size=20> "+rate_growth[i] + " %</font>";
//                                text1 = "<font color=#ffffff>Observed </font><font color=#ff9f1c> "+buildingCacheModels.get(i).getListing()+" </font> <font color=#ffffff>online listing in last 1 WEEK</font>";
//                                tv_building.setText(Html.fromHtml(text1));
                                text1="<font color=#2dc4b6>Today's Rate</font>";
                                tv_building.setText(Html.fromHtml(text1));
                                txtFilterValue.setText(buildingCacheModels.get(i).getRate_growth() + " %");
                                txtFilterValue.setTextSize(16);
                                txtFilterValue.setTypeface(Typeface.DEFAULT_BOLD);
                                tvFetchingrates.setVisibility(View.VISIBLE);
                                if (Integer.parseInt(buildingCacheModels.get(i).getRate_growth()) < 0){
                                    txtFilterValue.setTextColor(Color.parseColor("#ffb91422"));// FFA64139 red
                                    if (brokerType.equalsIgnoreCase("rent")) {
                                        String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9<big> " + General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).substring(2, General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).length()) + "</big></font><b><font color=#b91422><sub>/m</sub></font></br>";
                                        tvFetchingrates.setText(Html.fromHtml(text));
                                    } else {
                                        String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9 <big>" + General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).substring(2, General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).length()) + "</big></font><b><font color=#b91422><sub>/sq.ft</sub></font></br>";
                                        tvFetchingrates.setText(Html.fromHtml(text));
                                    }
                                }
                                else if(Integer.parseInt(buildingCacheModels.get(i).getRate_growth()) > 0){
                                    txtFilterValue.setTextColor(Color.parseColor("#2dc4b6"));// FF377C39 green FF2CA621   FFB91422
                                    if (brokerType.equalsIgnoreCase("rent")) {
                                        String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9<big> " + General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).substring(2, General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).length()) + "</big></font><b><font color=#b91422><sub>/m</sub></font></br>";
                                        tvFetchingrates.setText(Html.fromHtml(text));
                                    } else {
                                        String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9 <big>" + General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).substring(2, General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).length()) + "</big></font><b><font color=#b91422><sub>/sq.ft</sub></font></br>";
                                        tvFetchingrates.setText(Html.fromHtml(text));
                                    }
                                }
                                else{
                                    if (brokerType.equalsIgnoreCase("rent")) {
                                        String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9<big> " + General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).substring(2, General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).length()) + "</big></font><b><font color=#b91422><sub>/m</sub></font></br>";
                                        tvFetchingrates.setText(Html.fromHtml(text));
                                    } else {
                                        String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9 <big>" + General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).substring(2, General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).length()) + "</big></font><b><font color=#b91422><sub>/sq.ft</sub></font></br>";
                                        tvFetchingrates.setText(Html.fromHtml(text));
                                    }
                                    txtFilterValue.setTextColor(Color.parseColor("black"));
                                }
//                                    txtFilterValue.setTextColor(Color.parseColor("black"));
                                //ll_marker.setEnabled(false);
                                mVisits.setEnabled(false);
                                txtFilterValue.setEnabled(false);
//                                txtFilterValue.setTextColor(Color.parseColor("green"));
                                tv_building.setVisibility(View.VISIBLE);
                                tvFetchingrates.setTypeface(null, Typeface.BOLD);
                                lng = customMarker.get(i).getPosition().longitude;
                                lat = customMarker.get(i).getPosition().latitude;
                                Log.i("marker lat", "==============marker position :" + customMarker.get(i).getPosition() + " " + lat + " " + lng);
                                General.setSharedPreferences(getBaseContext(), AppConstants.MY_LAT, lat + "");
                                General.setSharedPreferences(getBaseContext(), AppConstants.MY_LNG, lng + "");//*/
//                                  mCustomerMarker[i].showInfoWindow();
                                new LocationUpdater().execute();
                                //flag[i] = true;
                                buildingCacheModels.get(i).setFlag(true);

                            } else {
                                customMarker.get(i).setIcon(icon1);
                                updateHorizontalPicker();
                                Log.i("mm_mithai", "marker draw");
                                CloseBuildingOyeComfirmation();
                               // markerDeselected();
                                search_building_icon.setVisibility(View.GONE);
                                buildingIcon.setVisibility(View.GONE);
                                fav.setVisibility(View.VISIBLE);
                                buildingCacheModels.get(i).setFlag(false);
                                horizontalPicker.setVisibility(View.VISIBLE);
                                tvFetchingrates.setVisibility(View.GONE);
                                recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                                mVisits.setBackground(getBaseContext().getResources().getDrawable(R.drawable.bg_animation));
                                txtFilterValue.setBackground(getBaseContext().getResources().getDrawable(R.drawable.oye_button_border));
                                txtFilterValue.setTextSize(13);
                                txtFilterValue.setTextColor(Color.parseColor("white"));
                                txtFilterValue.setText(oyetext);
//                                txtFilterValue.setText("Home");
                                mVisits.setEnabled(true);
                                txtFilterValue.setEnabled(true);
                                Intent in = new Intent(AppConstants.MARKERSELECTED);
                                in.putExtra("markerClicked", "false");
                                tvRate.setVisibility(View.VISIBLE);
                                rupeesymbol.setVisibility(View.VISIBLE);
                                tv_building.setVisibility(View.VISIBLE);
                                buildingTextChange(General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY), 950);
                                //tv_building.setText("Average Rate @ this Locality");
                            }
                        } else {
                            customMarker.get(i).setIcon(icon1);

                        }
                    }
                //}
                return true;
            }
        });


        if(AppConstants.MY_BASE_LOCATION_FLAG) {
            Log.i("setBaseRegion","==========================================base Region is not yet set. : ");
            setBaseRegion();
        }

        mcallback = new GetCurrentLocation.CurrentLocationCallback() {

            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    Log.i("Exception11", "inside mcallback");
                    lat = location.getLatitude();


                    lng = location.getLongitude();


                    General.setSharedPreferences(getBaseContext(),AppConstants.MY_LAT,lat + "");
                    General.setSharedPreferences(getBaseContext(),AppConstants.MY_LNG,lng + "");
                    if (isNetworkAvailable()) {
                        try {
                            getRegion();

                            buildingTextChange(General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY), 950);

                            new LocationUpdater().execute();

                        } catch (Exception e) {
                            Log.i("Exception", "caught in get region");
                        }
                    }

                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());


                    // map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,MAP_ZOOM));


                    //make retrofit call to get Min Max price
                    if (isNetworkAvailable()) {
                        try {

                            Log.i("Network available","%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                            marquee(200,50);
                            getPrice();
                        } catch (Exception e) {}
                    }
                }
            }
        };


    }



        @OnClick({R.id.addressPanel1, R.id.ic_search})
        public void onOptionClickS (View v){
            searchFragment c1 = new searchFragment();
           // AppConstants.SEARCHFLAG = true;

            loadFragmentAnimated(c1, null, R.id.container_Signup, "Search1");
            searchflag=true;
            /*Intent in = new Intent(AppConstants.MARKERSELECTED);
            in.putExtra("markerClicked", "false");
            LocalBroadcastManager.getInstance(this).sendBroadcast(in);*/
            // if(!General.getSharedPreferences(this,AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
//                ((ClientMainActivity) getActivity()).closeOyeConfirmation();
//                ((ClientMainActivity) getActivity()).closeOyeScreen();
            // }
            // ((ClientMainActivity) getActivity()).CloseBuildingOyeComfirmation();
            //onMapclicked();

        }


   // }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.i("Checking network","====================");
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void loadFragmentAnimated(Fragment fragment, Bundle args, int containerId, String title)
    {
        fragment.setArguments(args);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


    public void getRegion() {
        String lat1 = General.getSharedPreferences(getBaseContext(),AppConstants.MY_LAT);
        String lng1 = General.getSharedPreferences(getBaseContext(),AppConstants.MY_LNG);
        Log.i("localityBroadcast","addresses latlng "+lat1+" "+lng1);
        geocoder = new Geocoder(this, Locale.getDefault());
        List<android.location.Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lat1), Double.parseDouble(lng1), 1);
        } catch (IOException e) {
            Log.i("exception1","Caught in exception in getRegion 1"+e);
        }
        try {
            Log.i("localityBroadcast","addresses "+addresses);
            region = addresses.get(0).getSubLocality();
           General.setSharedPreferences(getBaseContext(),AppConstants.LOCALITY,region);
            Log.i("localityBroadcast","localityBroadcast3 "+region);
            Intent intent = new Intent(AppConstants.LOCALITY_BROADCAST);
            intent.putExtra("locality",region);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            pincode = addresses.get(0).getPostalCode();
            address = addresses.get(0).getAddressLine(0);
            Log.i("exception1","address "+address);
            // fullAddress = "";
        } catch (Exception e) {
            Log.i("exception1","Caught in exception in getRegion"+e);
        }

    }

    @Override
    public void priceSelected(String val) {
        try {
            map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));
        }
        catch(Exception e){
            Log.i(TAG,"Caught in exception Zoom map to 12 "+e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       // hideOnSearch.setVisibility(View.GONE);
        seekbar_linearlayout.setVisibility(View.VISIBLE);
//        property_type_layout.setVisibility(View.VISIBLE);
       // dispProperty.setVisibility(View.VISIBLE);
        seekbar_linearlayout.setBackgroundColor(Color.WHITE);
        seekbar_linearlayout.setAlpha(1);
        InputMethodManager imm = (InputMethodManager)getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoCompView.getWindowToken(), 0);



        mPhasedSeekBar.setVisibility(View.VISIBLE);
        map.animateCamera(CameraUpdateFactory.zoomTo(12));
        autoCompView.clearListSelection();
        //autoc = false;
        //rem
        getLocationFromAddress(autoCompView.getText().toString());
        if (isNetworkAvailable()) {
            new LocationUpdater().execute();

        }
    }

    public void getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
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
            //SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
           // SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
            General.setSharedPreferences(getBaseContext(),AppConstants.MY_LAT,lat + "");
            General.setSharedPreferences(getBaseContext(),AppConstants.MY_LNG,lng + "");
            map.moveCamera(CameraUpdateFactory.newLatLng(l));
        } catch (IOException e) {e.printStackTrace();}
    }
    protected class LocationUpdater extends AsyncTask<Double, Double, String> {
        public JSONObject getJSONfromURL(String url) {

            // initialize
            InputStream is = null;
            String result = "";
            JSONObject jObject = null;

            // http post
            try {
                org.apache.http.client.HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                org.apache.http.HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("TAGi", "Error in http connection " + e.toString());
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
                Log.e("TAGi", "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                jObject = new JSONObject(result);
            } catch (JSONException e) {
                Log.e("TAGi", "Error parsing data " + e.toString());
            }
            return jObject;
        }


        @Override
        protected String doInBackground(Double[] objects) {
            try {
                String lat1 = General.getSharedPreferences(getBaseContext(),AppConstants.MY_LAT);
                String lng1 = General.getSharedPreferences(getBaseContext(),AppConstants.MY_LNG);
                JSONObject jsonObj = getJSONfromURL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat1 + ","
                        + lng1 + "&sensor=true&key=AIzaSyC7aqVbRyNsF1JNgtYbpPDsJAf981dPp5Q");
                Log.i("chai","Response_chai1");
               /* Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

                addresses = geocoder.getFromLocation(Double.parseDouble(lat1),Double.parseDouble(lng1), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = addresses.get(0).getAddressLine(0);*/
//                           JSONObject jsonObj = getJSONfromURL("https://maps.googleapis.com/maps/api/place/queryautocomplete/json?input=Arun%20Ka&scope=APP&key=AIzaSyC7aqVbRyNsF1JNgtYbpPDsJAf981dPp5Q");
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
                                if (this != null) {
                                    //SharedPrefs.save(getActivity(), SharedPrefs.MY_LOCALITY, long_name);
                                   General.setSharedPreferences(getBaseContext(), AppConstants.LOCALITY, long_name);
                                }
                            } else if (Type.equalsIgnoreCase("locality")) {
                                // Address2 = Address2 + long_name + ", ";
                                City = long_name;
                                General.setSharedPreferences(getBaseContext(),AppConstants.MY_CITY,City);
                            } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                                County = long_name;
                                SharedPrefs.save(getBaseContext(), SharedPrefs.MY_CITY, County);

                            } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                State = long_name;
                            } else if (Type.equalsIgnoreCase("country")) {
                                Country = long_name;
                            } else if (Type.equalsIgnoreCase("postal_code")) {
                                PIN = long_name;
                               // SharedPrefs.save(getBaseContext(), SharedPrefs.MY_PINCODE, PIN);
                            }
                        }
                        //if (this != null)
                        //SharedPrefs.save(getActivity(), SharedPrefs.MY_REGION, fullAddress);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("savebuilding","savebuilding: "+General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY)+"  "+SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_CITY));
            return fullAddress; //address
          //  return address;
        }
//http://maps.googleapis.com/maps/api/geocode/json?latlng=19.138875873865832,72.82106649130584&sensor=true&key=AIzaSyC7aqVbRyNsF1JNgtYbpPDsJAf981dPp5Q"
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //autoCompView.setText(s);
            Log.i("sssss","addressBar "+s);
            addressBar.setText(s);
            if(savebuilding)
            tv_building.setText(fullAddress);
            tv_change_region.setText(SharedPrefs.getString(getBaseContext(),SharedPrefs.MY_CITY));
            /*sLog.i("sssss","addressBar "+s);
                      favAdrs.setText(s);
                      Log.i("", "");
                      autoCompView.dismissDropDown();
                      autoCompView.setCursorVisible(false);
                      // new LocationUpdater().execute();
                      Log.i(TAG,"locality automata ");
                      try {
                          if(savebuilding)
                          tv_building.setText(fullAddress);
                          getRegion();
                          if(!savebuilding) {
                              if (autoIsClicked == true) {
                                  Log.i(TAG, "locality automata " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));

                                  getPrice();
                                  autoIsClicked = false;
                                  buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                              }
                          }


                      }catch(Exception e){}*/
        }
    }
    public boolean canAccessLocation() {

        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(getBaseContext(),perm));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                /*case REQUEST_CALL_PHONE:
                    startActivity(callIntent);
                    break;*/
                case LOCATION_REQUEST:
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        customMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                map = googleMap;
                                enableMyLocation();
                                getLocationActivity = new GetCurrentLocation(getBaseContext(), mcallback);
                                Log.i("t1", "broker_map" + map);
                                //  geoFence.drawPloygon(map);
                            }
                        });
                    }
                    else {
                        lat=Double.parseDouble(General.getSharedPreferences(getBaseContext(),AppConstants.MY_LAT));
                        lng=Double.parseDouble(General.getSharedPreferences(getBaseContext(),AppConstants.MY_LNG));
                        LatLng latlng=new LatLng(lat,lng);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,13));
                    }
                    break;
            }
            if (canAccessLocation()) {
                new GetCurrentLocation(this, new GetCurrentLocation.CurrentLocationCallback() {
                    @Override
                    public void onComplete(Location location) {
                        if (location != null) {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            Log.i("t1", "lat_long_getcurrentlocation" + " " + currentLocation);
                            map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                            map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));
                        }
                        getLocationActivity = new GetCurrentLocation(getBaseContext(),mcallback);
                    }
                });
            }
            else {
                Toast.makeText(getBaseContext(), "Offline Mode", Toast.LENGTH_LONG);
            }
        }catch (Exception e){}
    }


    private void enableMyLocation() {
        Log.i("slsl","loc+++++++++++++++++++ : ");
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (map != null) {
            map.setMyLocationEnabled(true);
        }
    }


    private void onMapDrag(final MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            // if (!AppConstants.SETLOCATION && !savebuilding) {
            tvRate.setVisibility(View.GONE);
            rupeesymbol.setVisibility(View.GONE);
            horizontalPicker.keepScrolling();
            horizontalPicker.stopScrolling();
            // }

//                                      Log.i("MotionEvent.ACTION_MOVE", "=========================");
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                                              horizontalPicker.stopScrolling();
            marquee(500,100);
            Log.i("MotionEvent.ACTION_UP", "========================="+savebuilding+" "+AppConstants.SETLOCATION+" "+spanning);
            updateHorizontalPicker();
            if (!spanning) {
                if (isNetworkAvailable()) {
                    Log.i("MotionEvent.ACTION_UP", "=========================" + savebuilding + " " + AppConstants.SETLOCATION);
                    final long now = SystemClock.uptimeMillis();
                    if (now - lastTouched > SCROLL_TIME) {
                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!AppConstants.SETLOCATION && !savebuilding) {
                                txtFilterValue.setTextSize(13);
                                txtFilterValue.setTextColor(Color.parseColor("white"));
                                //txtFilterValue.setText("Home");
                                txtFilterValue.setText(oyetext);
                               // CloseBuildingOyeComfirmation();
                                //((ClientMainActivity) getActivity()).CloseBuildingOyeComfirmation();
//                                        Intent in = new Intent(AppConstants.MARKERSELECTED);
//                                        in.putExtra("markerClicked", "false");
//                                      //  buildingSelected = true;
//                                        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(in);
////                                                      txtFilterValue.setTextColor(Color.parseColor("white"));
//                                        txtFilterValue.setText(oyetext);
                                tvFetchingrates.setVisibility(View.VISIBLE);
                                //mMarkerminmax.setVisibility(View.VISIBLE);
//                                        tvRate.setVisibility(View.VISIBLE);
//                                        rupeesymbol.setVisibility(View.VISIBLE);
//                                        tvFetchingrates.setVisibility(View.VISIBLE);
                                    buildingTextChange(General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY), 950);

                                    tv_building.setVisibility(View.VISIBLE);
                                recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                                }
                                VisibleRegion visibleRegion = map.getProjection().getVisibleRegion();
                                Point x1 = map.getProjection().toScreenLocation(visibleRegion.farRight);
                                Point y1 = map.getProjection().toScreenLocation(visibleRegion.nearLeft);
                                centerPoint = new Point((x1.x / 2) + 2, (y1.y / 2) + 4);
                                LatLng currentLocation1; //= new LatLng(location.getLatitude(), location.getLongitude());
                                Log.i("centerPoint", "============ map:" + " " + centerPoint);
//                                                      currentLocation1 = map.getProjection().fromScreenLocation(point);
                                LatLng centerFromPoint = map.getProjection().fromScreenLocation(centerPoint);
                                currentLocation1 = centerFromPoint;
                                lat = currentLocation1.latitude;
                                Log.i("t1", "lat" + " " + lat);
                                lng = currentLocation1.longitude;
                                Log.i("t1", "lng" + " " + lng);
                                //map.addMarker(new MarkerOptions().title("hey").position(currentLocation1));
                                Log.i("MARKER-- ", "====================================");
//                                SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
//                                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                                General.setSharedPreferences(getBaseContext(), AppConstants.MY_LAT, lat + "");
                                General.setSharedPreferences(getBaseContext(), AppConstants.MY_LNG, lng + "");
                                Log.i("t1", "Sharedpref_lat" + General.getSharedPreferences(getBaseContext(), AppConstants.MY_LAT));
                                Log.i("t1", "Sharedpref_lng" + General.getSharedPreferences(getBaseContext(), AppConstants.MY_LNG));
                                //addBText.setText("Find your Building "+"\""+B_name+"\""+" Location on map and click on Save.");
                                addBText.setText("Find your Building Location on map and click on Save.");
                                addressBar.setText("Getting Address... ");
                                getRegion();

                                new LocationUpdater().execute();
                                if (!AppConstants.SETLOCATION && !savebuilding) {
                                    search_building_icon.setVisibility(View.GONE);
                                    buildingIcon.setVisibility(View.GONE);
                                    fav.setVisibility(View.VISIBLE);
                                    horizontalPicker.stopScrolling();
                                    missingArea.setVisibility(View.GONE);
                                    getPrice();
                                }
                                if (txtFilterValue.getText().toString().equalsIgnoreCase("done")) {
                                    txtFilterValue.setText("SAVE");
                                    txt_info.setText("Find Building on Map & Save");
                                    addBText.setText("Find your Building  Location on map and click on Save.");
                                   // map.clear();
                                }
                                tv_change_region.setText(SharedPrefs.getString(getBaseContext(),SharedPrefs.MY_CITY));
                                //buildingTextChange(fullAddres);
                            }
                        });
                    }
//                                              new LocationUpdater().execute();
                } else {
                    tvFetchingrates.setVisibility(View.VISIBLE);
                    tvRate.setVisibility(View.GONE);
                    rupeesymbol.setVisibility(View.GONE);
                    horizontalPicker.setVisibility(View.GONE);
                    tv_building.setVisibility(View.GONE);
                    tvFetchingrates.setText("No Internet Connection..");
                    General.internetConnectivityMsg(getBaseContext());
                }
            }else{spanning = false;}

        }else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            //if (!AppConstants.SETLOCATION && !savebuilding) {
            lastTouched = SystemClock.uptimeMillis();
            // map.getUiSettings().setScrollGesturesEnabled(true);
            //LatLng currentLocation11;
            Log.i("MotionEvent.ACTION_DOWN", "=========================");

            //}
        }
    }


    @OnClick(R.id.txtFilterValue)
    public void onTxtFilterValueClick(View v) {
        Log.i("user_role","auto ok ...OnOyeClick1"+savebuilding);


        if(txtFilterValue.getText().toString().equalsIgnoreCase("save")){
            map.clear();
            map.addMarker(new MarkerOptions().icon(iconOffice).position(new LatLng(lat,lng)));
            txtFilterValue.setText("Done");
            txt_info.setText("Is this Location Correct ? press Done");
            addlistingText.setVisibility(View.VISIBLE);
            if(AppConstants.MY_BASE_LOCATION_FLAG) {
                addBText.setText("To confirm your Base Location click on Done");
            }else
            addBText.setText("To confirm your Building Location click on Done");
        }else if(txtFilterValue.getText().toString().equalsIgnoreCase("Done")){
            Log.i("user_role","role of user :  " +AppConstants.MY_BASE_LOCATION_FLAG);
//           if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                if(AppConstants.MY_BASE_LOCATION_FLAG) {
                    General.setSharedPreferences(getBaseContext(), AppConstants.MY_BASE_LAT, General.getSharedPreferences(getBaseContext(), AppConstants.MY_LAT));
                    General.setSharedPreferences(getBaseContext(), AppConstants.MY_BASE_LNG, General.getSharedPreferences(getBaseContext(), AppConstants.MY_LNG));
                    General.setSharedPreferences(getBaseContext(), AppConstants.MY_BASE_LOCATION, General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY));
//                  AppConstants.BROKER_BASE_REGION="true";
                    brokerType = "rent";
                    AppConstants.MY_BASE_LOCATION_FLAG=false;
                    fav.setClickable(true);
                    fav.setEnabled(true);
                    onBackPressed();
                    //Reset();
                    //getPrice();


                }else {
                    addlistingText.setVisibility(View.GONE);
                    addlistinglayout.setVisibility(View.VISIBLE);
                   // txtFilterValue.setText(General.getSharedPreferences(getBaseContext(), AppConstants.PROPERTY_CONFIG));
                }

        }


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.i("onBackPressed ","onBackPressed() =========== "+getSupportFragmentManager().getBackStackEntryCount()+" "+getFragmentManager().getBackStackEntryCount());

      /*  Intent in =new Intent(AppConstants.RESETPHASE);
        in.putExtra("resetphase",true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);*/
        if(searchflag){
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(container_Signup)).commitAllowingStateLoss();
            searchflag=false;
        }else if( buidingInfoFlag){
           CloseBuildingOyeComfirmation();
       }else if(Signupflag){
            CloseSignUP();
        }else if(AppConstants.MY_BASE_LOCATION_FLAG){
            //AppConstants.MY_BASE_LOCATION_FLAG = false;
            //do nothing
        }else{
           // onBackPressed();
            General.setSharedPreferences(getBaseContext(), AppConstants.RESETPHASE, "true");
            this.finish();
        }
        Log.i("onBackPressed ","onBackPressed() =========== "+getSupportFragmentManager().getBackStackEntryCount()+" "+getFragmentManager().getBackStackEntryCount());

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(resetMap);
        LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(phasedSeekBarClicked);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(resetMap, new IntentFilter(AppConstants.RESETMAP));
        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(phasedSeekBarClicked, new IntentFilter(AppConstants.PHASED_SEEKBAR_CLICKED));
    }


    @OnClick(R.id.cancel_btn)
    public void oncancelBtnClick(){

        if(buidingInfoFlag==true) {
            Intent in = new Intent(AppConstants.MARKERSELECTED);
            in.putExtra("markerClicked", "false");
            LocalBroadcastManager.getInstance(this).sendBroadcast(in);
            CloseBuildingOyeComfirmation();

            onMapclicked();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;

    }
    @Override
    public void onPositionSelected(int position, int count) {
        //p=position;
       // if(!AppConstants.SETLOCATION && !savebuilding) {


            switch (position) {
                case 0:
                    brokerType = "rent";
                    updateHorizontalPicker();
                    marquee( 500, 100 );
                    int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild( property_type_layout );
                    Log.i( "indexxx", "index of layout : " + index +" "+((ViewGroup) property_type_layout.getParent()).getId());
                    if (index == 3) {
                                           property_type_layout.clearAnimation();
                                           map_parent.removeView( property_type_layout );
                                           map_parent.addView( property_type_layout, 2 );
                                       }

                    if(!AppConstants.MY_BASE_LOCATION_FLAG){
                        property_type_layout.setVisibility(View.VISIBLE);
                        PropertyButtonSlideAnimation();

                    }
                    else{
                        property_type_layout.clearAnimation();
                        property_type_layout.setVisibility(View.GONE);
                    }

                    SnackbarManager.show(
                            Snackbar.with( getBaseContext() )
                                    .text( "Rental Property Type set" )
                                    .position( Snackbar.SnackbarPosition.TOP )
                                    .color( Color.parseColor( AppConstants.DEFAULT_SNACKBAR_COLOR ) ) );
                    tvRate.setText( "/ month" );

                    //AppConstants.CURRENT_DEAL_TYPE = "rent";
                    /*dbHelper.save( DatabaseConstants.brokerType, "LL" );
                    dbHelper.save( "brokerType", "On Rent" );*/
                   // recordWorkout.setBackgroundColor( Color.parseColor( "#2dc4b6" ) );
                    if (Property_type.equalsIgnoreCase( "" )) {
                        rental.setText( "Home" );
                        rental.setVisibility( View.VISIBLE );
                        resale.setVisibility( View.INVISIBLE );
                        property_type_layout.setVisibility( View.VISIBLE );
                    } else {
                        rental.setVisibility( View.VISIBLE );
                        resale.setVisibility( View.INVISIBLE );
                        rental.setText( Property_type );
                        property_type_layout.setVisibility( View.VISIBLE );
                    }
                    General.setSharedPreferences(getBaseContext(),AppConstants.AUTO_TT_CHANGE,"ll");
                    /*try {
                        if (buildingCacheModels.get(INDEX).getFlag() == true) {
                            tv_building.setVisibility(View.VISIBLE);
                            tv_building.setText("Average Rate in last 1 WEEK");
                            String text = "<font color=#ffffff>" + buildingCacheModels.get(INDEX).getName() + "</b></b></font> <font color=#ffffff>@</font>&nbsp&nbsp<font color=#ff9f1c>\u20B9" + General.currencyFormat(String.valueOf(price(buildingCacheModels.get(INDEX).getConfig(), buildingCacheModels.get(INDEX).getLl_pm()))).substring(2, General.currencyFormat(String.valueOf(price(buildingCacheModels.get(INDEX).getConfig(), buildingCacheModels.get(INDEX).getLl_pm()))).length()) + "</font><b><font color=#ff9f1c><sub>/m</sub></font>";
                            tvFetchingrates.setText(Html.fromHtml(text));
                        }
                    }catch (Exception e){}*/
                    break;


                case 1:
                    brokerType = "resale";
                    updateHorizontalPicker();
                        marquee( 500, 100 );
                        int index1 = ((ViewGroup) property_type_layout.getParent()).indexOfChild( property_type_layout );
                        Log.i( "indexxx", "index of layout : " + index1 +" "+property_type_layout);
                        if (index1 == 3) {
                            Log.i( "indexx", "inside if stmt" );
                            property_type_layout.clearAnimation();
                            map_parent.removeView( property_type_layout );
                            map_parent.addView( property_type_layout, 2 );
                                       }

                                       PropertyButtonSlideAnimation();
                        SnackbarManager.show(
                                Snackbar.with( getBaseContext() )
                                        .text( "Buy/Sell Property Type set" )
                                        .position( Snackbar.SnackbarPosition.TOP )
                                        .color( Color.parseColor( AppConstants.DEFAULT_SNACKBAR_COLOR ) ) );
                       // updateHorizontalPicker();
                        tvRate.setText( "/ sq.ft" );

                       // AppConstants.CURRENT_DEAL_TYPE = "resale";
                       /* dbHelper.save( DatabaseConstants.brokerType, "OR" );
                        dbHelper.save( "brokerType", "For Sale" );*/
                        if (Property_type.equalsIgnoreCase( "" )) {
                            rental.setText( "Home" );
                            resale.setVisibility( View.VISIBLE );
                            rental.setVisibility( View.INVISIBLE );
                            property_type_layout.setVisibility( View.VISIBLE );
                        } else {
                            resale.setText( Property_type );
                            resale.setVisibility( View.VISIBLE );
                            rental.setVisibility( View.INVISIBLE );
                            property_type_layout.setVisibility( View.VISIBLE );
                        }
                    General.setSharedPreferences(getBaseContext(),AppConstants.AUTO_TT_CHANGE,"or");

                        /*if (buildingCacheModels.get(INDEX).getFlag() == true) {
                            tv_building.setVisibility( View.VISIBLE );
                            tv_building.setText( "Average Rate in last 1 WEEK" );
                            String text = "<font color=#ffffff>" + buildingCacheModels.get(INDEX).getName() + "</b></b></font> <font color=#ffffff> @ </font>&nbsp<font color=#ff9f1c>\u20B9" + General.currencyFormat( String.valueOf( buildingCacheModels.get(INDEX).getOr_psf() ) ).substring( 2, General.currencyFormat( String.valueOf( buildingCacheModels.get(INDEX).getOr_psf() ) ).length() ) + "</font><b><font color=#ff9f1c><sub>/sq.ft</sub></font>";
                            tvFetchingrates.setText( Html.fromHtml( text ) );
                        }*/
                    break;


            }
       // }

    }

    private void updateHorizontalPicker() {
        try {
            if (horizontalPicker != null) {
                if (brokerType.equals("rent")) {
                    Log.i(TAG, "updateHorizontalPicker rental andro " + llMin + " " + llMax);
                    //   horizontalPicker.setInterval((llMin*1000), (llMax*1000),10, HorizontalPicker.THOUSANDS);
                   // Log.i("HORRIZONTALPICKER", "filterValue " + filterValue + " filterValueMultiplier " + filterValueMultiplier + "  LLmin && LLmax" + llMin + " " + llMax);
//                    horizontalPicker.setInterval((roundoff1(llMin * filterValueMultiplier)), (roundoff1(llMax * filterValueMultiplier)), 10, HorizontalPicker.THOUSANDS);
                    horizontalPicker.setInterval(llMin * 1000, llMax * 1000, 10, HorizontalPicker.THOUSANDS);

                } else {
                    Log.i(TAG, "updateHorizontalPicker resale andro " + orMin + " " + orMax);
                    horizontalPicker.setInterval(roundoff1(orMin), roundoff1(orMax), 10, HorizontalPicker.THOUSANDS);
                }
            }
        }catch(Exception e){ }

    }

    public void getPrice() {
        //getRegion();
        if(General.isNetworkAvailable(getBaseContext())) {
          if (!AppConstants.SETLOCATION && !savebuilding) {
                General.slowInternet(getBaseContext());
              //  MarkerClickEnable = true;
                mVisits.setEnabled(false);
                //disablepanel(false);
                txtFilterValue.setEnabled(false);
                //CancelAnimation();
                User user = new User();
                Log.i("jumba", "get price prepaaration locality " + General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY)+" locality from getregion api : "+region);
                user.setDeviceId(General.getSharedPreferences(getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI));
                Log.i("jumba", "getcontext " + General.getSharedPreferences(getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI));
                user.setGcmId(SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_GCM_ID));
                user.setUserRole("broker");
                user.setLongitude(General.getSharedPreferences(getBaseContext(), AppConstants.MY_LNG));
                user.setProperty_type(Property_type.toLowerCase());//Property_type
                user.setLatitude(General.getSharedPreferences(getBaseContext(), AppConstants.MY_LAT));
                Log.i("jumba", "My_lng" + "  " + SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LNG));
                if (General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY) == "")
                    user.setLocality("Mumbai");
                else
                    user.setLocality(General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY));
                Log.i("jumba", "My_lat" + "  " + General.getSharedPreferences(getBaseContext(), AppConstants.MY_LAT));
                user.setPlatform("android");
                Log.i("jumba", General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY));
                user.setPincode(PIN);
                if (General.getSharedPreferences(getBaseContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
                    user.setUserId(General.getSharedPreferences(getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI));
                } else {
                    user.setUserId(General.getSharedPreferences(getBaseContext(), AppConstants.USER_ID));
                }
                Log.i(TAG, "jumba " + General.getSharedPreferences(getBaseContext(), AppConstants.USER_ID)+"   "+General.getSharedPreferences(getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI));

                tv_building.setVisibility(View.INVISIBLE);
                horizontalPicker.setVisibility(View.GONE);
                tvRate.setVisibility(View.GONE);
                rupeesymbol.setVisibility(View.GONE);
                tvFetchingrates.setVisibility(View.VISIBLE);
                tvFetchingrates.setText("Fetching Rates....");
                tvFetchingrates.setTextSize(15);
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_11).build();
                restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
                UserApiService userApiService = restAdapter.create(UserApiService.class);
                userApiService.getPrice(user, new retrofit.Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {

//                      START reset building selection(Click immediately old building after drag)
                        txtFilterValue.setTextSize(13);
                        txtFilterValue.setTextColor(Color.parseColor("white"));
                       // txtFilterValue.setText("Home");
                        txtFilterValue.setText(oyetext);
                        txtFilterValue.setBackground(getBaseContext().getResources().getDrawable(R.drawable.oye_button_border));
                       // ((ClientMainActivity)getActivity()).CloseBuildingOyeComfirmation();
                        /*Intent in = new Intent(AppConstants.MARKERSELECTED);
                        in.putExtra("markerClicked", "false");
                        //buildingSelected = true;
                        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(in);*/
                        //END

                       // if (!AppConstants.SETLOCATION||!savebuilding) {
                            try {
                                General.slowInternetFlag = false;
                                General.t.interrupt();
                                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
//                        Log.e(TAG, "RETROFIT SUCCESS " + getPrice.getResponseData().getPrice().getLlMin().toString());
                                JSONObject jsonResponse = new JSONObject(strResponse);
                                String errors = jsonResponse.getString("errors");
                                String msg = "";
                                try {
                                    msg = jsonResponse.getJSONObject("responseData").getJSONObject("price").getString("message");

                                    msg = jsonResponse.getJSONObject("responseData").getString("message");
                                } catch (Exception e) {

                                }


                                Log.i("Chaman", "ho ghe " + General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY) + " " + General.getSharedPreferences(getBaseContext(), AppConstants.MY_LAT) + " " + General.getSharedPreferences(getBaseContext(), AppConstants.MY_LNG) + " " + jsonResponse);

                                if (errors.equals("8")) {
                                    Log.i(TAG, "error code is 2 ");
                                    Log.i(TAG, "error code is 1 " + jsonResponse.toString());
                                    Log.i(TAG, "error code is " + errors);
                                    Log.i(TAG, "error code is 3 ");
                                    SnackbarManager.show(
                                            Snackbar.with(getBaseContext())
                                                    .text("You must update profile to proceed.")
                                                    .position(Snackbar.SnackbarPosition.TOP)
                                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                                    Intent openProfileActivity = new Intent(getBaseContext(), ProfileActivity.class);
                                    openProfileActivity.putExtra("msg", "compulsary");
                                    startActivity(openProfileActivity);
                                } else if (msg.equalsIgnoreCase("We dont cater here yet") || msg.equalsIgnoreCase("missing Fields in get price")) {
                                    tvFetchingrates.setText("We only cater in Mumbai");
                                } else {
                                    JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));
                                    // horizontalPicker.stopScrolling();
                                    Log.i("TRACE", "Response getprice buildings jsonResponse" + jsonResponse);
                                    Log.i("TRACE", "Response getprice buildings jsonResponseData" + jsonResponseData);
                                    JSONObject price = new JSONObject(jsonResponseData.getString("price"));
                                    Log.i("TRACE", "Response getprice buildings pricer ");
                                    Log.i("TRACE", "Response getprice buildings price " + price);

                                    Log.i("TRACE", "Response getprice buildings yo" + price.getString("ll_min"));
                                    if (!price.getString("ll_min").equalsIgnoreCase("")) {
                                        if (!price.getString("ll_min").equalsIgnoreCase("0")) {
                                            Log.i("tt", "I am here" + 2);
                                            Log.i("TRACE", "RESPONSEDATAr" + response);
                                            llMin = Integer.parseInt(price.getString("ll_min"));
                                            llMax = Integer.parseInt(price.getString("ll_max"));
                                            Log.i("TRACE", "RESPONSEDATArr" + llMin);
                                            Log.i("TRACE", "RESPONSEDATArr" + llMax);
                                           // llMin = 5 * (Math.round(llMin / 5));
                                           // llMax = 5 * (Math.round(llMax / 5));
                                            /*Log.i("TRACE", "RESPONSEDATAr" + llMin);
                                            Log.i("TRACE", "RESPONSEDATAr" + llMax);*/
                                            orMin = Integer.parseInt(price.getString("or_min"));
                                            orMax = Integer.parseInt(price.getString("or_max"));
                                            Log.i("TRACE", "RESPONSEDATArr" + orMin);
                                            Log.i("TRACE", "RESPONSEDATArr" + orMax);
                                           // orMin = 500 * (Math.round(orMin / 500));
                                            //orMax = 500 * (Math.round(orMax / 500));
//                                            Log.i("TRACE", "RESPONSEDATAr" + orMin);
//                                            Log.i("TRACE", "RESPONSEDATAr" + orMax);
                                           // BroadCastMinMaxValue(llMin, llMax, orMin, orMax);
                                            updateHorizontalPicker();
                                           // marquee(500, 100);
                                            map.clear();
                                            buildingTextChange(General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY), 950);
                                            recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                                            mVisits.setBackground(getBaseContext().getResources().getDrawable(R.drawable.bg_animation));
                                            txtFilterValue.setBackground(getBaseContext().getResources().getDrawable(R.drawable.oye_button_border));
                                            search_building_icon.setVisibility(View.GONE);
                                            buildingIcon.setVisibility(View.GONE);
                                            fav.setVisibility(View.VISIBLE);
                                            //StartOyeButtonAnimation();

                                            try {
                                                JSONArray buildings = new JSONArray(jsonResponseData.getString("buildings"));
                                                Log.i("TRACE", "Response getprice buildings" + buildings);
                                                JSONObject k = new JSONObject(buildings.get(1).toString());
                                                for (int i = 0; i < 5; i++) {
                                                    JSONObject j = new JSONObject(buildings.get(i).toString());
                                                    config[i] = j.getString("config");
                                                    Log.i("Buildingdata", "config" + config[i]);
                                                    name[i] = j.getString("name");
                                                    Log.i("Buildingdata", "name" + name[i]);
                                                    rate_growth[i] = j.getString("rate_growth");
                                                    Log.i("Buildingdata", "rate_growth" + rate_growth[i]);
                                                    or_psf[i] = Integer.parseInt(j.getString("or_psf"));
                                                    Log.i("Buildingdata", "or_psf" + or_psf);
                                                    ll_pm[i] = Integer.parseInt(j.getString("ll_pm"));

                                                    Log.i("Buildingdata", "ll_pm" + ll_pm);
                                                    id[i] = j.getString("id");
                                                    double lat = Double.parseDouble(j.getJSONArray("loc").get(1).toString());
                                                    Log.i("Buildingdata", "lat " + lat);
                                                    double longi = Double.parseDouble(j.getJSONArray("loc").get(0).toString());
                                                    Log.i("Buildingdata", "longi" + longi);
                                                    loc[i] = new LatLng(lat, longi);
                                                    Log.i("Buildingdata", "loc " + loc);
                                                    Log.i("Buildingdata", "mCustomerMarker " + mCustomerMarker[i]);
                                                    listing[i] = j.getString("listings");
                                                    transaction[i] = j.getString("transactions");
                                                    portal[i] = j.getString("portals");
                                                    //city=j.getString("city");
                                                    Log.i("Buildingdata", "listing transaction portal" + listing[i] + " " + transaction[i] + " " + portal[i]);
                                                    String customSnippet = rate_growth[i];
                                                   CacheBuildings(name[i],lat+"",longi+"",j.getString("locality"),ll_pm[i],or_psf[i],id[i],config[i],listing[i],portal[i],rate_growth[i],transaction[i],j.getString("city") );


                                                    //mCustomerMarker[i] = map.addMarker(new MarkerOptions().position(loc[i]).title(name[i]).snippet(customSnippet).icon(icon1).flat(true));
                                                   // Log.i("TRACE", "mCustomerMarker after :" + mCustomerMarker[i]);
                                                    //flag[i] = false;
//                                                dropPinEffect(mCustomerMarker[i]);
//                                                private void CacheBuildings(String name,String lat,String longi,String locality,int ll_pm,int or_psf,String id,String conf,String listing,String portal,String rate_growth,String transaction){

                                                }


                                                String building_count = jsonResponseData.getString("building_count");
                                                Log.i("Buildingdata", "loc " + building_count + " " + mCustomerMarker.length);

                                                try {
                                                    SnackbarManager.show(
                                                            Snackbar.with(getApplicationContext())
                                                                    .text("Displaying 5 buildings out of " + building_count)
                                                                    .position(Snackbar.SnackbarPosition.TOP)
                                                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } catch (Exception e) {
                                                Log.i("Price Error", "Caught in exception Building plot success" + e.getMessage());
                                            }

                                            //showFavourites();
                                            mVisits.setEnabled(true);
                                            txtFilterValue.setEnabled(true);
                                           // disablepanel(true);
                                            horizontalPicker.setVisibility(View.VISIBLE);
                                            tv_building.setVisibility(View.VISIBLE);
                                            tvRate.setVisibility(View.VISIBLE);
                                            rupeesymbol.setVisibility(View.VISIBLE);
                                            tvFetchingrates.setVisibility(View.GONE);
                                            missingArea.setVisibility(View.GONE);
                                            PlotBuilding();

                                            // map.addMarker(new MarkerOptions().title("hey").position(new LatLng(lat,lng)));

                                        } else {
                                            Log.i("tt", "I am here" + 3);

                                            map.clear();
                                            tv_building.setVisibility(View.INVISIBLE);
                                            horizontalPicker.setVisibility(View.GONE);
                                            tvRate.setVisibility(View.INVISIBLE);
                                            rupeesymbol.setVisibility(View.INVISIBLE);
                                            tvFetchingrates.setVisibility(View.VISIBLE);
                                            tvFetchingrates.setText("Coming Soon...");
                                            missingArea.setVisibility(View.VISIBLE);
                                            mVisits.setEnabled(false);
                                            txtFilterValue.setEnabled(false);
                                            //CancelAnimation();
                                        }
                                    } else {

                                        map.clear();
                                        tv_building.setVisibility(View.INVISIBLE);
                                        horizontalPicker.setVisibility(View.GONE);
                                        tvRate.setVisibility(View.INVISIBLE);
                                        rupeesymbol.setVisibility(View.INVISIBLE);
                                        tvFetchingrates.setVisibility(View.VISIBLE);
                                        tvFetchingrates.setText("Coming Soon...");
                                        missingArea.setVisibility(View.VISIBLE);
                                        mVisits.setEnabled(false);
                                        txtFilterValue.setEnabled(false);
                                       // CancelAnimation();
                                        Log.i("GETPRICE", "Else mode ====== ");
                                    }

                                }
                            } catch (Exception e) {
                                General.slowInternetFlag = false;
                                General.t.interrupt();

                                Log.i("Price Error", "Caught in exception getprice success" + e.getMessage());
                            }


                        }
                   // }
                    @Override
                    public void failure(RetrofitError error) {
                        General.slowInternetFlag = false;
                        map.clear();
                        tv_building.setVisibility(View.INVISIBLE);
                        horizontalPicker.setVisibility(View.GONE);
                        tvRate.setVisibility(View.INVISIBLE);
                        rupeesymbol.setVisibility(View.INVISIBLE);
                        tvFetchingrates.setVisibility(View.VISIBLE);
                        tvFetchingrates.setText("Coming Soon...");
                        missingArea.setVisibility(View.VISIBLE);
                        mVisits.setEnabled(false);
                        txtFilterValue.setEnabled(false);
                       // CancelAnimation();
                        General.t.interrupt();
                        Log.i("getPrice", "retrofit failure getprice " + error.getMessage());

                    }
                });

            }
        }
        else{
            General.internetConnectivityMsg(getBaseContext());

        }
    }


    private int   roundoff1(int val){

        val=val/500;
        val=val * 500;
        return  val;
    }



    private void CacheBuildings(String name,String lat,String longi,String locality,int ll_pm,int or_psf,String id,String conf,String listing,String portal,String rate_growth,String transaction,String city){
        Realm myRealm = General.realmconfig( getBaseContext());
        RealmResults<BuildingCacheRealm> result1= myRealm.where(BuildingCacheRealm.class).findAllSorted("timestamp");
        RealmResults<MyPortfolioModel> result= realm.where(MyPortfolioModel.class).findAll();
        int size=100+result.size();
        Log.i("dataformrealm1","BuildingCacheRealm before "+result1.size());
        if(result1.size()>size){
            if(myRealm.isInTransaction())
                myRealm.cancelTransaction();
            realm.beginTransaction();
            result1.remove(size);
            realm.commitTransaction();
            Log.i("dataformrealm1","BuildingCacheRealm entered 123456 "+result1.size());
            BuildingCacheRealm buildingCacheRealm = new BuildingCacheRealm();
            buildingCacheRealm.setTimestamp(SystemClock.currentThreadTimeMillis());
            buildingCacheRealm.setName(name);
            buildingCacheRealm.setLat(lat);
            buildingCacheRealm.setLng(longi);
            buildingCacheRealm.setCity(city);
            buildingCacheRealm.setLocality(locality);
            buildingCacheRealm.setLl_pm(ll_pm);
            buildingCacheRealm.setOr_psf(or_psf);
            buildingCacheRealm.setId(id);
            buildingCacheRealm.setConfig(conf);
            buildingCacheRealm.setListing(listing);
            buildingCacheRealm.setPortals(portal);
            buildingCacheRealm.setRate_growth(rate_growth);
            buildingCacheRealm.setTransactions(transaction);
            if(myRealm.isInTransaction())
                myRealm.cancelTransaction();
            myRealm.beginTransaction();
            myRealm.copyToRealmOrUpdate( buildingCacheRealm );
//        myRealm.copyToRealmOrUpdate((Iterable<RealmObject>) myPortfolioModel);
            myRealm.commitTransaction();

        }else{
            Log.i("dataformrealm1","BuildingCacheRealm entered 123456 "+result1.size());
            BuildingCacheRealm buildingCacheRealm = new BuildingCacheRealm();
            buildingCacheRealm.setTimestamp(SystemClock.currentThreadTimeMillis());
            buildingCacheRealm.setName(name);
            buildingCacheRealm.setLat(lat);
            buildingCacheRealm.setLng(longi);
            buildingCacheRealm.setLocality(locality);
            buildingCacheRealm.setLl_pm(ll_pm);
            buildingCacheRealm.setOr_psf(or_psf);
            buildingCacheRealm.setId(id);
            buildingCacheRealm.setConfig(conf);
            buildingCacheRealm.setListing(listing);
            buildingCacheRealm.setPortals(portal);
            buildingCacheRealm.setRate_growth(rate_growth);
            buildingCacheRealm.setTransactions(transaction);
            if(myRealm.isInTransaction())
                myRealm.cancelTransaction();
            myRealm.beginTransaction();
            myRealm.copyToRealmOrUpdate( buildingCacheRealm );
//        myRealm.copyToRealmOrUpdate((Iterable<RealmObject>) myPortfolioModel);
            myRealm.commitTransaction();
        }
    }

    private void PlotBuilding(){
        Log.i("dataformrealm","BuildingCacheRealm entered 122 ");
        map.clear();
        customMarker.clear();
        buildingCacheModels.clear();
        realm = General.realmconfig(getBaseContext());
        RealmResults<MyPortfolioModel> result= realm.where(MyPortfolioModel.class).findAll();

        addbuildingCache(result);
        RealmResults<BuildingCacheRealm> result1= realm.where(BuildingCacheRealm.class).findAll();
        Log.i("dataformrealm","BuildingCacheRealm entered 12234 ");
        int len =result1.size();
        Log.i("dataformrealm","BuildingCacheRealm entered kamine  "+result1.size());

        for(BuildingCacheRealm c :result1){
            Log.i("dataformrealm","BuildingCacheRealm"+c.getName());
//          customMarker.add( map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(c.getLng()),Double.parseDouble(c.getLng()))).title(c.getName()).snippet(c.getId()).icon(icon1).flat(true)));
//           map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(c.getLng()),Double.parseDouble(c.getLng()))).title(c.getName()).snippet(c.getId()).icon(icon1).flat(true));
            customMarker.add(map.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(c.getLat()), Double.parseDouble(c.getLng())))
                    .title(c.getName())
                    .snippet(c.getId())
                    .icon(icon1)));
            buildingCacheModels.add(new buildingCacheModel(c.getId(),c.getName(),c.getConfig(),c.getOr_psf(),c.getLl_pm(),c.getLat(),c.getLng(),c.getRate_growth(),c.getListing(),c.getPortals(),c.getTimestamp(),c.getTransactions(),c.getLocality(),false));
            // public buildingCacheModel(String id, String name, String config, int or_psf, int ll_pm, String lat, String lng, String rate_growth, String listing, String portals, String timestamp, String transactions, String locality) {
            Log.i("dataformrealm","BuildingCacheRealm11m :  "+c.getName());
          /*buildingCacheModel buildingCacheModels = new  buildingCacheModel(c.getId(),c.getName(),c.getLocality(),c.getRate_growth(),c.getLl_pm(),c.getOr_psf(),c.getTimestamp(),c.getTransactions(),c.getConfig(),null);
          myPortfolioOR.add(portListingModel);*/
        }

        if(!General.getSharedPreferences(getBaseContext(),AppConstants.CALLING_ACTIVITY).equalsIgnoreCase("")&&!General.getSharedPreferences(getBaseContext(),AppConstants.CALLING_ACTIVITY).equalsIgnoreCase("PC")){
            autoMarkerClick(General.getSharedPreferences(getBaseContext(),AppConstants.CALLING_ACTIVITY));
        }
        Intent inn = new Intent(AppConstants.REFRESH_LISTVIEW);
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(inn);
    }


    private void addbuildingCache(RealmResults<MyPortfolioModel> result){

        for(MyPortfolioModel c :result) {
            Realm myRealm = General.realmconfig(getBaseContext());
            BuildingCacheRealm buildingCacheRealm = new BuildingCacheRealm();
            buildingCacheRealm.setTimestamp((long)Double.parseDouble(c.getTimestamp()));
            buildingCacheRealm.setName(c.getName());
            buildingCacheRealm.setLat(c.getLat());
            buildingCacheRealm.setLng(c.getLng());
            buildingCacheRealm.setLocality(c.getLocality());
            buildingCacheRealm.setLl_pm(c.getLl_pm());
            buildingCacheRealm.setOr_psf(c.getOr_psf());
            buildingCacheRealm.setId(c.getId());
            buildingCacheRealm.setConfig(c.getConfig());
            buildingCacheRealm.setListing(c.getListing());
            buildingCacheRealm.setPortals(c.getPortals());
            buildingCacheRealm.setRate_growth(c.getRate_growth());
            buildingCacheRealm.setTransactions(c.getTransactions());
            if (myRealm.isInTransaction())
                myRealm.cancelTransaction();
            myRealm.beginTransaction();
            myRealm.copyToRealmOrUpdate(buildingCacheRealm);
//        myRealm.copyToRealmOrUpdate((Iterable<RealmObject>) myPortfolioModel);
            myRealm.commitTransaction();
        }
    }

    public int price(String conf,int rate){
        Log.i("conf case","conf  : "+conf+"  "+rate);
        int price=rate*950;
        switch(conf) {
            case "1rk":
                price = rate * 300;
                break;
            case "1bhk":
                price = rate * 600;
                break;
            case "1.5bhk":
                price = rate * 800;
                break;
            case "2bhk":
                price = rate * 950;
                break;
            case "2.5bhk":
                price = rate * 1300;
                break;
            case "3bhk":
                price = rate * 1600;
                break;
            case "3.5bhk":
                price = rate * 1800;
                break;
            case "4bhk":
                price = rate * 2100;
                break;
            case "4.5bhk":
                price = rate * 2300;
                break;
            case "5bhk":
                price = rate * 2500;
                break;
            case "5.5bhk":
                price = rate * 2700;
                break;
            case "6bhk":
                price = rate * 2900;
                break;
        }
        price=price/500;
        price=price*500;
        return price;
    }
    public void SaveBuildingDataToRealm(){
        Realm myRealm = General.realmconfig( this );
        MyPortfolioModel myPortfolioModel = new MyPortfolioModel();
        myPortfolioModel.setName( buildingCacheModels.get(INDEX).getName() );
        myPortfolioModel.setConfig( buildingCacheModels.get(INDEX).getConfig() );
        myPortfolioModel.setLat( buildingCacheModels.get(INDEX).getLat()+ "" );
        myPortfolioModel.setLng( buildingCacheModels.get(INDEX).getLng() + "" );
        myPortfolioModel.setId( buildingCacheModels.get(INDEX).getId() );
        myPortfolioModel.setLl_pm(buildingCacheModels.get(INDEX).getLl_pm());
        myPortfolioModel.setOr_psf( buildingCacheModels.get(INDEX).getOr_psf() );


        if(brokerType=="rent") {

            myPortfolioModel.setTt("ll");
        }else{
            myPortfolioModel.setTt("or");

        }

        myPortfolioModel.setPortals( buildingCacheModels.get(INDEX).getPortals() );
        myPortfolioModel.setListing( buildingCacheModels.get(INDEX).getListing() );
        myPortfolioModel.setRate_growth(buildingCacheModels.get(INDEX).getRate_growth() );
        myPortfolioModel.setTransactions( buildingCacheModels.get(INDEX).getTransactions() );
        myPortfolioModel.setLocality( buildingCacheModels.get(INDEX).getLocality() );
        myPortfolioModel.setTimestamp(String.valueOf(System.currentTimeMillis()));
        if(myRealm.isInTransaction())
            myRealm.cancelTransaction();
        myRealm.beginTransaction();
        myRealm.copyToRealmOrUpdate( myPortfolioModel );
//        myRealm.copyToRealmOrUpdate((Iterable<RealmObject>) myPortfolioModel);
        myRealm.commitTransaction();

    }

    public void onMapclicked(){
        spanning = false;
        Log.i("onMapclicked","Inside onMapclicked   ");
        for (int i = 0; i < customMarker.size(); i++) {
            if (buildingCacheModels.get(i).getFlag() == true) {
                mVisits.setEnabled(true);
                txtFilterValue.setEnabled(true);
                tvRate.setVisibility(View.VISIBLE);
                rupeesymbol.setVisibility(View.VISIBLE);
                customMarker.get(i).setIcon(icon1);
                customMarker.get(i).hideInfoWindow();
                CloseBuildingOyeComfirmation();
               // markerDeselected();
               // ((ClientMainActivity)getActivity()).CloseBuildingOyeComfirmation();
                /*Intent in = new Intent(AppConstants.MARKERSELECTED);
                in.putExtra("markerClicked", "false");
               // buildingSelected = true;
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);*/
                search_building_icon.setVisibility(View.GONE);
                buildingIcon.setVisibility(View.GONE);
                fav.setVisibility(View.VISIBLE);
                buildingCacheModels.get(i).setFlag(false);
                horizontalPicker.setVisibility(View.VISIBLE);
                tvFetchingrates.setVisibility(View.GONE);
                recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                tvRate.setVisibility(View.VISIBLE);
                buildingTextChange(General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY), 950);

                mVisits.setBackground(getBaseContext().getResources().getDrawable(R.drawable.bg_animation));
                //StartOyeButtonAnimation();
                updateHorizontalPicker();
                txtFilterValue.setTextSize(13);
                txtFilterValue.setTextColor(Color.parseColor("white"));
                txtFilterValue.setText(oyetext);
//                txtFilterValue.setText("Home");
                txtFilterValue.setBackground(getBaseContext().getResources().getDrawable(R.drawable.oye_button_border));
                Log.i("onMapclicked","Inside onMapclicked 909099099099  ");
               // ll_marker.setEnabled(true);
                tv_building.setVisibility(View.VISIBLE);
            }
        }

    }


    public  void OpenBuildingOyeConfirmation(String listing,String transaction,String portal,String Config){
       // hdroomsCount.setVisibility(View.GONE);
       // drawerFragment.setMenuVisibility(false);
        buidingInfoFlag=true;
        Bundle args = new Bundle();
        args.putString("listing", listing);
        args.putString("transaction", transaction);
        args.putString("portal", portal);
        args.putString("config", Config);
        confirm_screen_title.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        cancel_btn.setVisibility(View.VISIBLE);
        cancel_btn.setText("Back");
        markerSelected();
        getSupportActionBar().setTitle("");

        setbaseloc.setVisibility(View.GONE);

        if(brokerType.equalsIgnoreCase("rent")){
            confirm_screen_title.setText("Live Building Rates \n(Rent)");
        }else
        {
            confirm_screen_title.setText("Live Building Rates \n" + "(Buy/Sell)");

        }
        BuildingOyeConfirmation buildingOyeConfirmation = new BuildingOyeConfirmation();
        loadFragment(buildingOyeConfirmation, args, R.id.container_OyeConfirmation, "");
    }


    public  void CloseBuildingOyeComfirmation(){

        confirm_screen_title.setVisibility(View.GONE);
        /*Intent in = new Intent(AppConstants.MARKERSELECTED);
        in.putExtra("markerClicked", "false");
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);*/
        markerDeselected();
        cancel_btn.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        setbaseloc.setVisibility(View.VISIBLE);
        tv_change_region.setText(SharedPrefs.getString(getBaseContext(),SharedPrefs.MY_CITY));

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_OyeConfirmation)).commit();

        //if( buidingInfoFlag==true)
//            for(int i=0;i<getSupportFragmentManager().getBackStackEntryCount();i++)
          //  getSupportFragmentManager().popBackStack();
       buidingInfoFlag=false;
    }

    private void  buildingTextChange(String locality,int area){
        if(isNetworkAvailable()) {
            Log.i(TAG,"buildingTextChange if called");
            tv_building.setText(Html.fromHtml("<font color='#2dc4b6'>Avg Rate</font> @ " + locality + " | Area " + area + "sqft."));
        }else{
            Log.i(TAG,"buildingTextChange else called");
            tv_building.setText(Html.fromHtml("<font color='#2dc4b6'>Avg Rate</font> @ Andheri | Area " + area + "sqft."));

        }
    }


    private void marquee(int timeInMillis, int timeDivider){
        try {
            new CountDownTimer(timeInMillis, timeDivider) {
                public void onTick(long millisUntilFinished) {
                    horizontalPicker.keepScrolling();
//                       if(horizontalPicker.getVisibility()==View.VISIBLE)
                    rupeesymbol.setVisibility(View.GONE);
                }
                public void onFinish() {
                    horizontalPicker.stopScrolling();
                    updateHorizontalPicker();
                    if(horizontalPicker.getVisibility()==View.VISIBLE)
                        rupeesymbol.setVisibility(View.VISIBLE);
                }
            }.start();
        }catch (Exception e){}
    }


    private void init()     {

        //setDealStatus2(getContext());
        bounce = AnimationUtils.loadAnimation(getBaseContext(), R.anim.bounce);
        slideUp = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_down);
        home = (Button) findViewById(R.id.home);
        shop = (Button) findViewById(R.id.shop);
        industrial = (Button) findViewById(R.id.industrial);
        office = (Button) findViewById(R.id.office);

        rental = (TextView) findViewById(R.id.rental);
        resale = (TextView) findViewById(R.id.sale);
        property_type_layout = (RelativeLayout) findViewById(R.id.property_type);
        dispProperty=(LinearLayout) findViewById(R.id.property_type_layout);
        zoomout_right = (AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoomout_slide));
        zoomout_left = (AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoomout_slide_left));
        slide_up1 = (AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_up_and_down));
        zoomin_zoomout = (AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoomout_zoomin));//slide_left
        slide_left = (AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_left1));
        //if(!savebuilding && !AppConstants.MY_BASE_LOCATION_FLAG)
       // PropertyButtonSlideAnimation();
        AppConstants.PROPERTY = "Home";

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_click=true;
                property_type_layout.clearAnimation();
                Property_type = "Home";
                oyetext = "2BHK";
                AppConstants.PROPERTY = "Home";
                Log.i("home", "you are in home ");
                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);

                Log.i("indexxx", "index of layout : " + index);
                if(index!=3) {
                    map_parent.removeView(property_type_layout);
                    Log.i("indexxx", "index of layout : 3 " + index);
                    map_parent.addView(property_type_layout, 3);
                }
                PropertyButtonAnimation();
               index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);
                Log.i("indexxx", "index of layout :12 " + index);
                property_type_layout.setVisibility(View.GONE);
                getPrice();
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_click=true;
                property_type_layout.clearAnimation();
                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);
                Log.i("indexxx", "index of layout : " + index);
                if(index!=3) {
                    map_parent.removeView(property_type_layout);
                    map_parent.addView(property_type_layout, 3);
                }
                Property_type = "Shop";
                oyetext = "SHOP";
                AppConstants.PROPERTY = "Shop";
                Log.i("home", "you are in shop ");
                PropertyButtonAnimation();
//                property_type_layout.setVisibility(View.GONE);
                getPrice();
            }
        });
        industrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_click=true;
                property_type_layout.clearAnimation();
                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);

                Log.i("indexxx", "index of layout : " + index);
                if(index!=3) {
                    map_parent.removeView(property_type_layout);
                    map_parent.addView(property_type_layout, 3);
                }
                Property_type = "Industrial";
                oyetext = "INDUS";
                AppConstants.PROPERTY = "Industrial";
                Log.i("home", "you are in industrial ");
                PropertyButtonAnimation();
//                property_type_layout.setVisibility(View.GONE);
                getPrice();
            }
        });
        office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_click=true;
                property_type_layout.clearAnimation();
                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);
                Log.i("indexxx", "index of layout : " + index+" "+((ViewGroup) property_type_layout.getParent()));

                if(index!=3) {
                    map_parent.removeView(property_type_layout);
                    map_parent.addView(property_type_layout, 3);
                }
                Log.i("indexxx", "you are in office "+" "+((ViewGroup) property_type_layout.getParent()));
                Property_type = "Office";
                AppConstants.PROPERTY = "Office";
                oyetext = "OFFIC";
                PropertyButtonAnimation();
                // property_type_layout.setVisibility(View.GONE);
                Log.i("indexxx", "index of layout last : " + index+" "+((ViewGroup) property_type_layout.getParent()));
                getPrice();
            }
        });

        addbuilding.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("signupstatus","General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER)   "+General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER));
//                ((ClientMainActivity)getActivity()).CloseBuildingOyeComfirmation();
                onMapclicked();
                if (General.getSharedPreferences(getBaseContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
//                    General.setSharedPreferences(getContext(), AppConstants.ROLE_OF_USER, "client");
                    SignUpFragment signUpFragment = new SignUpFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("lastFragment", "brokerDrawer");
                    loadFragmentAnimated(signUpFragment, bundle, container_Signup, "");
                    AppConstants.SIGNUP_FLAG = true;
                }else {
                    openAddListing();
                }
                getPrice();
            }
        } );

    }

    protected void PropertyButtonSlideAnimation() {
        Log.i("indexxx","inside PropertyButtonSlideAnimation : ");
        property_type_layout.startAnimation(slide_up1);
        pro_click=false;
        slide_up1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if(  pro_click==false) {
                    property_type_layout.clearAnimation();
                    property_type_layout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }


    protected void PropertyButtonAnimation() {


        /*Intent intent = new Intent(AppConstants.PROPERTY_TYPE_BROADCAST);
                 intent.putExtra("protype",Property_type );
                 LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);*/
//        if(brokerType.equalsIgnoreCase(""))
//            brokerType="rent";
        if (brokerType.equalsIgnoreCase("rent")) {
            ani = zoomout_left;
            property_type_layout.startAnimation(ani);

        } else {
            ani = zoomout_right;
            property_type_layout.startAnimation(ani);

        }

        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (brokerType.equalsIgnoreCase("rent")) {
                    property_type_layout.clearAnimation();
                    property_type_layout.setVisibility(View.GONE);
                    txtFilterValue.setText(oyetext);
                    rental.setText(Property_type);
                } else {
                    property_type_layout.clearAnimation();
                    property_type_layout.setVisibility(View.GONE);
                    txtFilterValue.setText(oyetext);
                    resale.setText(Property_type);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });

    }


    private void  markerSelected(){
          // confirm_screen_title.setVisibility(View.VISIBLE);
            //getSupportActionBar().setTitle("Live Building Rates");
            btnMyDeals.setVisibility(View.VISIBLE);
            btnMyDeals.setBackground(getResources().getDrawable(R.drawable.share_btn_background));
            btnMyDeals.setText("Share");

    }

    private void  markerDeselected(){
        /*confirm_screen_title.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Live Region Rates");*/
        /*btnMyDeals.setBackgroundResource(R.drawable.asset_dealsbutton_v1);
        btnMyDeals.setText("");*/
        btnMyDeals.setVisibility(View.GONE);
    }

    public void openAddListing(){
        containerSignup.setBackgroundColor(Color.parseColor("#CC000000"));
        containerSignup.setClickable(true);
        AddListing addBuildingCardView = new AddListing();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        card.setClickable(true);
        fragmentTransaction.addToBackStack("card");
        fragmentTransaction.replace(R.id.card, addBuildingCardView);
        fragmentTransaction.commitAllowingStateLoss();
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //pc=true;
//    loadFragmentAnimated(addBuildingCardView, null, R.id.card, "");
    }

    public void openAddBuilding(){

        containerSignup.setBackgroundColor(Color.parseColor("#CC000000"));
        containerSignup.setClickable(true);

        AddBuilding addBuilding= new AddBuilding();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        card.setClickable(true);
        fragmentTransaction.addToBackStack("card");
        fragmentTransaction.replace(R.id.card, addBuilding);
        fragmentTransaction.commitAllowingStateLoss();
//        loadFragmentAnimated(addBuildingRealm, null, R.id.card, "");

    }

    public void openAddListingFinalCard(){

        containerSignup.setBackgroundColor(Color.parseColor("#CC000000"));
        containerSignup.setClickable(true);
        AddListingFinalCard addListingFinalCard= new AddListingFinalCard();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        card.setClickable(true);
        fragmentTransaction.addToBackStack("card");
        fragmentTransaction.replace(R.id.card, addListingFinalCard);
        fragmentTransaction.commitAllowingStateLoss();
//        loadFragmentAnimated(addListingFinalCard, null, R.id.card, "");

    }
    public void closeCardContainer(){

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.card)).commit();
        containerSignup.setBackgroundColor(getResources().getColor(R.color.transparent));
        containerSignup.setClickable(false);
        card.setClickable(false);
        Reset();
       // ((DashboardClientFragment) getSupportFragmentManager().findFragmentById(R.id.container_map)).resetSeekBar();
    }

    public  void setlocation(String b_name){
        btnMyDeals.setVisibility(View.GONE);
        btn_back.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.VISIBLE);
        fr.setVisibility(View.GONE);
        /*getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);*/
        confirm_screen_title.setVisibility(View.VISIBLE);
        confirm_screen_title.setText(b_name);
        getSupportActionBar().setTitle("");
        setbaseloc.setVisibility(View.GONE);
        closeCardContainer();
        containerSignup.setBackgroundColor(getResources().getColor(R.color.transparent));
        containerSignup.setClickable(false);
        card.setClickable(false);
        saveBuiding(b_name);
       // hdroomsCount.setVisibility(View.GONE);

    }
    public  void setBaseRegion(){
        btnMyDeals.setVisibility(View.GONE);
        fav.setEnabled(false);
        fr.setVisibility(View.GONE);
        /*btn_back.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.VISIBLE);*/
        /*getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);*/
        property_type_layout.clearAnimation();
        property_type_layout.setVisibility(View.GONE);
        setbaseloc.setVisibility(View.GONE);
        confirm_screen_title.setText("Set Base Region");
        getSupportActionBar().setTitle("");
       // hdroomsCount.setVisibility(View.GONE);
//        closeAddBuilding();
      /*  containerSignup.setBackgroundColor(getResources().getColor(R.color.transparent));
        containerSignup.setClickable(false);
        card.setClickable(false);*/
        Log.i(TAG,"set base region 5");
        //setLocation11();
        saveBuiding("");
        Log.i(TAG,"set base region 6");

    }


   /* public void setLocation11(){
        Log.i(TAG,"set base region 7");

//        savebuilding=true;
//        AppConstants.SETLOCATION=true;
        fav.setClickable(false);
        map.clear();
        new LocationUpdater().execute();
        horizontalPicker.setVisibility(View.GONE);
        rupeesymbol.setVisibility(View.GONE);
        tvRate.setVisibility(View.GONE);
        txtFilterValue.setText("SAVE");

        tv_building.setText(fullAddress);
        tvFetchingrates.setVisibility(View.VISIBLE);

        //CallButton.setVisibility(View.GONE);
        addbuilding.setVisibility(View.GONE);
        mPhasedSeekBar.setVisibility(View.GONE);
        dispProperty.setVisibility(View.GONE);
        property_type_layout.clearAnimation();
        property_type_layout.setVisibility(View.GONE);
        txt_info.setVisibility(View.VISIBLE);
        txt_info.setText("Find Your Location on Map & Save");
        String txt;
        txt="<font color=#2dc4b6><big>Drag & Save Base Location</big></font>";
        tvFetchingrates.setText(Html.fromHtml(txt));
    }*/


    public void saveBuiding(String b_name){

         B_name=b_name;
        addlistingText.setVisibility(View.VISIBLE);
        String txt;

        if(b_name.equalsIgnoreCase(""))
        {
            txt="<font color=#2dc4b6><big>Drag & Save Base Location</big></font>";
            tvFetchingrates.setText(Html.fromHtml(txt));
            txt_info.setText("Find Base Location on Map & Save");
            addBText.setText("Find your Base Location on map and click on Save to proceed.");
        }else {
            txt="<font color=#2dc4b6><big>Drag & Save Building Location</big></font>";
            tvFetchingrates.setText(Html.fromHtml(txt));
            txt_info.setText("Find Building on Map & Save");
            addBText.setText("Find your Building Location on map and click on Save.");
        }
       // addBText.setText("Find your Building "+"\""+B_name+"\""+" Location on map and click on Save.");
        fr.setVisibility(View.GONE);
        savebuilding=true;
        map.clear();
        new LocationUpdater().execute();
        horizontalPicker.setVisibility(View.GONE);
        rupeesymbol.setVisibility(View.GONE);
        tvRate.setVisibility(View.GONE);
        txtFilterValue.setText("SAVE");
        setbaseloc.setVisibility(View.GONE);
        //confirm_screen_title.setText("Save Building");

        tv_building.setText(fullAddress);
        tvFetchingrates.setVisibility(View.VISIBLE);
        txt_info.setVisibility(View.VISIBLE);
        //CallButton.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        addbuilding.setVisibility(View.GONE);
        mPhasedSeekBar.setVisibility(View.GONE);
        dispProperty.setVisibility(View.GONE);


    }

    @OnClick(R.id.back_btn)
    public void Onbtn_BackClick1(View v) {
        Reset();
        //markerDeselected();
        openAddBuilding();
    }

    @OnClick(R.id.btncancel)
    public void Onbtn_cancelClick1(View v) {
        Reset();
        markerDeselected();
    }


    public void Reset(){
        Log.i(TAG,"rolewa 2 ");
        //]]btnMyDeals.setVisibility(View.VISIBLE);
        addlistingText.setVisibility(View.GONE);
        btn_back.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
        savebuilding=false;
        fr.setVisibility(View.VISIBLE);
        setbaseloc.setVisibility(View.VISIBLE);
        txt_info.setVisibility(View.GONE);
        tvFetchingrates.setVisibility(View.GONE);
        horizontalPicker.setVisibility(View.VISIBLE);
        rupeesymbol.setVisibility(View.VISIBLE);
        tvRate.setVisibility(View.VISIBLE);
        addbuilding.setVisibility(View.VISIBLE);
       // pc=false;
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        txtFilterValue.setText("2BHK");
        AppConstants.PROPERTY="Home";
        dispProperty.setVisibility(View.VISIBLE);
        mPhasedSeekBar.setVisibility(View.VISIBLE);
        addlistinglayout.setVisibility(View.GONE);
        confirm_screen_title.setVisibility(View.GONE);
       // confirm_screen_title.setText("Live Region Rates");
        //getSupportActionBar().setTitle("");
        setbaseloc.setVisibility(View.VISIBLE);
        tv_change_region.setText(SharedPrefs.getString(getBaseContext(),SharedPrefs.MY_CITY));
      //  ((DashboardClientFragment) getSupportFragmentManager().findFragmentById(R.id.container_map)).ResetChanges();

    }


    private void AddBuildingDataToRealm(String id){

        Realm myRealm = General.realmconfig( getBaseContext());
        addBuildingRealm add_Building = new addBuildingRealm();
        add_Building.setTimestamp(String.valueOf(SystemClock.currentThreadTimeMillis()));
        add_Building.setBuilding_name(B_name);
        add_Building.setType("ADD");
        add_Building.setAddress(fullAddress);
        add_Building.setConfig(General.getSharedPreferences(getBaseContext(),AppConstants.PROPERTY_CONFIG));
        add_Building.setProperty_type(AppConstants.PROPERTY);
        add_Building.setLat( lat + "" );
        add_Building.setLng( lng + "" );
        add_Building.setId(id);
        add_Building.setSublocality( SharedPrefs.getString( getBaseContext(), SharedPrefs.MY_LOCALITY ) );
        add_Building.setLl_pm(0);
        add_Building.setOr_psf(0);
        add_Building.setGrowth_rate(null);
        add_Building.setDisplay_type("both");
        myRealm.beginTransaction();
        myRealm.copyToRealmOrUpdate(add_Building);
//        myRealm.copyToRealmOrUpdate((Iterable<RealmObject>) myPortfolioModel);
        myRealm.commitTransaction();
        Reset();
       // if(!General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
            Intent in = new Intent(getBaseContext(), MyPortfolioActivity.class);
            startActivity(in);
            //Reset();
       // }
    }

    public void Addbuilding()
    {
        Log.i("updateStatus CALLED","updateStatus success called ");
        AddBuildingModel addBuildingModel = new AddBuildingModel();
        addBuildingModel.setBuilding(B_name);
        addBuildingModel.setLat(lat+"");
        addBuildingModel.setLng(lng+"");
        addBuildingModel.setCity(City);
        addBuildingModel.setLocality(General.getSharedPreferences( getBaseContext(), AppConstants.LOCALITY ));
        addBuildingModel.setUser_role(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER));
        addBuildingModel.setUser_id(General.getSharedPreferences(getBaseContext(),AppConstants.NAME));
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_101).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

//        UserApiService userApiService = restAdapter.create(UserApiService.class);


        /*userApiService.addBuildingRealm(AddBuildingModel, new retrofit.Callback<JsonElement>() {*/




        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


        try {
            oyeokApiService.addBuilding(addBuildingModel, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    Log.i("magic","addBuildingRealm success ");


                    try {
                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                        JSONObject jsonResponse = new JSONObject(strResponse);

                        Log.i("magic","addBuildingRealm success "+jsonResponse.getJSONObject("responseData").getString("id"));
                        AddBuildingDataToRealm(jsonResponse.getJSONObject("responseData").getString("id"));

                    } catch (Exception e) {

                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("magic","addBuildingRealm failed "+error);
                }
            });


        }
        catch (Exception e){
            Log.e("TAG", "Caught in the the"+ e.getMessage());
        }

    }

public void OpenSignUpFrag(){
    SignUpFragment signUpFragment = new SignUpFragment();
   // signUpFragment.getView().bringToFront();
    Bundle bundle = new Bundle();
    bundle.putStringArray("Chat", null);
    bundle.putString("lastFragment", "brokermap");
    loadFragmentAnimated(signUpFragment, bundle,R.id.container_Signup, "");
    Signupflag=true;

}

public void CloseSignUP(){
    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(container_Signup)).commitAllowingStateLoss();
    OpenBuildingOyeConfirmation(buildingCacheModels.get(INDEX).getListing(),buildingCacheModels.get(INDEX).getTransactions(),buildingCacheModels.get(INDEX).getPortals(),buildingCacheModels.get(INDEX).getConfig());
    Signupflag=false;


}

    private void loadFragment(Fragment fragment, Bundle args, int containerId, String title)
    {
        //set arguments
        fragment.setArguments(args);
        //load fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(title);
        Log.i("SIGNUP_FLAG","SIGNUP_FLAG=========  loadFragment client "+getFragmentManager().getBackStackEntryCount());
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
        //set title
        //set title
//        getSupportActionBar().setTitle(title);
    }


    public void autoMarkerClick(String id){
        Log.i("1sushil11", "=============markerClick==============   :  ");

        for (int i = 0; i < customMarker.size(); i++) {
//            Log.i("1sushil11", "=============markerClick==============   :  "+customMarker.get(i).getSnippet());

            if (customMarker.get(i).getSnippet().equalsIgnoreCase(id)) {
                INDEX = i;
                Log.i("1sushil11", "===========================   :  "  + customMarker.get(i).getId().toString() + " " + buildingCacheModels.get(i).getFlag());
                if (buildingCacheModels.get(i).getFlag() == false) {
                    Log.i("1sushil11", "===========================");
                    customMarker.get(i).setIcon(icon2);
                    customMarker.get(i).showInfoWindow();
                    // markerSelected();
                    OpenBuildingOyeConfirmation(buildingCacheModels.get(i).getListing(),buildingCacheModels.get(i).getTransactions(),buildingCacheModels.get(i).getPortals(),buildingCacheModels.get(i).getConfig());
                    //SaveBuildingDataToRealm();
                    buildingIcon.setVisibility(View.VISIBLE);
                    fav.setVisibility(View.GONE);
                    horizontalPicker.setVisibility(View.GONE);
                    tvRate.setVisibility(View.GONE);
                    rupeesymbol.setVisibility(View.GONE);
                    recordWorkout.setBackgroundColor(Color.parseColor("#ff9f1c"));
                    mVisits.setBackground(getBaseContext().getResources().getDrawable(R.drawable.oyebutton_bg_color_yellow));
                    txtFilterValue.setBackground(getBaseContext().getResources().getDrawable(R.drawable.oye_bg_color_white));
                    String text1;
                    //="<font color=#ffffff size=20> "+rate_growth[i] + " %</font>";
//                                text1 = "<font color=#ffffff>Observed </font><font color=#ff9f1c> "+buildingCacheModels.get(i).getListing()+" </font> <font color=#ffffff>online listing in last 1 WEEK</font>";
//                                tv_building.setText(Html.fromHtml(text1));
                    text1="<font color=#2dc4b6>Today's Rate</font>";
                    tv_building.setText(Html.fromHtml(text1));
                    txtFilterValue.setText(buildingCacheModels.get(i).getRate_growth() + " %");
                    txtFilterValue.setTextSize(16);
                    txtFilterValue.setTypeface(Typeface.DEFAULT_BOLD);
                    tvFetchingrates.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(buildingCacheModels.get(i).getRate_growth()) < 0){
                        txtFilterValue.setTextColor(Color.parseColor("#ffb91422"));// FFA64139 red
                        if (brokerType.equalsIgnoreCase("rent")) {
                            String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9<big> " + General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).substring(2, General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).length()) + "</big></font><b><font color=#b91422><sub>/m</sub></font></br>";
                            tvFetchingrates.setText(Html.fromHtml(text));
                        } else {
                            String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9 <big>" + General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).substring(2, General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).length()) + "</big></font><b><font color=#b91422><sub>/sq.ft</sub></font></br>";
                            tvFetchingrates.setText(Html.fromHtml(text));
                        }
                    }
                    else if(Integer.parseInt(buildingCacheModels.get(i).getRate_growth()) > 0){
                        txtFilterValue.setTextColor(Color.parseColor("#2dc4b6"));// FF377C39 green FF2CA621   FFB91422
                        if (brokerType.equalsIgnoreCase("rent")) {
                            String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9<big> " + General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).substring(2, General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).length()) + "</big></font><b><font color=#b91422><sub>/m</sub></font></br>";
                            tvFetchingrates.setText(Html.fromHtml(text));
                        } else {
                            String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9 <big>" + General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).substring(2, General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).length()) + "</big></font><b><font color=#b91422><sub>/sq.ft</sub></font></br>";
                            tvFetchingrates.setText(Html.fromHtml(text));
                        }
                    }
                    else{
                        if (brokerType.equalsIgnoreCase("rent")) {
                            String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9<big> " + General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).substring(2, General.currencyFormat(String.valueOf(price(buildingCacheModels.get(i).getConfig(),buildingCacheModels.get(i).getLl_pm()))).length()) + "</big></font><b><font color=#b91422><sub>/m</sub></font></br>";
                            tvFetchingrates.setText(Html.fromHtml(text));
                        } else {
                            String text = "<font color=#ffffff ><small>" + buildingCacheModels.get(i).getName() + "</small></b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9 <big>" + General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).substring(2, General.currencyFormat(String.valueOf(buildingCacheModels.get(i).getOr_psf())).length()) + "</big></font><b><font color=#b91422><sub>/sq.ft</sub></font></br>";
                            tvFetchingrates.setText(Html.fromHtml(text));
                        }
                        txtFilterValue.setTextColor(Color.parseColor("black"));
                    }
//                                    txtFilterValue.setTextColor(Color.parseColor("black"));
                    //ll_marker.setEnabled(false);
                    mVisits.setEnabled(false);
                    txtFilterValue.setEnabled(false);
//                                txtFilterValue.setTextColor(Color.parseColor("green"));
                    tv_building.setVisibility(View.VISIBLE);
                    tvFetchingrates.setTypeface(null, Typeface.BOLD);
                    lng = customMarker.get(i).getPosition().longitude;
                    lat = customMarker.get(i).getPosition().latitude;
                    Log.i("marker lat", "==============marker position :" + customMarker.get(i).getPosition() + " " + lat + " " + lng);
                    General.setSharedPreferences(getBaseContext(), AppConstants.MY_LAT, lat + "");
                    General.setSharedPreferences(getBaseContext(), AppConstants.MY_LNG, lng + "");//*/
//                                  mCustomerMarker[i].showInfoWindow();
                    new LocationUpdater().execute();
                    //flag[i] = true;
                    buildingCacheModels.get(i).setFlag(true);

                } else {
                    customMarker.get(i).setIcon(icon1);
                    updateHorizontalPicker();
                    Log.i("mm_mithai", "marker draw");
                    CloseBuildingOyeComfirmation();
                    // markerDeselected();
                    search_building_icon.setVisibility(View.GONE);
                    buildingIcon.setVisibility(View.GONE);
                    fav.setVisibility(View.VISIBLE);
                    buildingCacheModels.get(i).setFlag(false);
                    horizontalPicker.setVisibility(View.VISIBLE);
                    tvFetchingrates.setVisibility(View.GONE);
                    recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                    mVisits.setBackground(getBaseContext().getResources().getDrawable(R.drawable.bg_animation));
                    txtFilterValue.setBackground(getBaseContext().getResources().getDrawable(R.drawable.oye_button_border));
                    txtFilterValue.setTextSize(13);
                    txtFilterValue.setTextColor(Color.parseColor("white"));
                    txtFilterValue.setText(oyetext);
//                                txtFilterValue.setText("Home");
                    mVisits.setEnabled(true);
                    txtFilterValue.setEnabled(true);
                    Intent in = new Intent(AppConstants.MARKERSELECTED);
                    in.putExtra("markerClicked", "false");
                    tvRate.setVisibility(View.VISIBLE);
                    rupeesymbol.setVisibility(View.VISIBLE);
                    tv_building.setVisibility(View.VISIBLE);
                    buildingTextChange(General.getSharedPreferences(getBaseContext(), AppConstants.LOCALITY), 950);
                    //tv_building.setText("Average Rate @ this Locality");
                }
            } else {
                customMarker.get(i).setIcon(icon1);

            }
        }
    }


}