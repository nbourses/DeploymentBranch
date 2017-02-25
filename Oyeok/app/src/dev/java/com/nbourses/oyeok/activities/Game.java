package com.nbourses.oyeok.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.JsonElement;
import com.google.maps.android.ui.IconGenerator;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.MyApplication;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.AutoCompletePlaces;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.fragments.gameDiscountCard;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import static com.amazonaws.metrics.AwsSdkMetrics.getRegion;

public class Game extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.balance1)
    TextView balance1;

    @Bind(R.id.gm_cashback_btn)
    TextView gm_cashback_btn;


    @Bind(R.id.myaccount)
    LinearLayout myaccount;

    @Bind(R.id.load_card_container)
    FrameLayout load_card_container;

    @Bind(R.id.card1)
    FrameLayout card;

    private GetCurrentLocation getLocationActivity;
    private GoogleMap map;
    View mHelperView;
    ImageView location_button, lock, pin;
    CustomMapFragment customMapFragment;
    AutoCompleteTextView autoCompView;
    private BitmapDescriptor icon1, icon2, icon3, profitIcon, lossIcon;
    private GetCurrentLocation.CurrentLocationCallback mcallback;
    // private ChangeLocation locationName;
    private Point point;
    //SharedPreferences.OnSharedPreferenceChangeListener listener;
    private static final String[] LOCATION_PERMS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final String[] INITIAL_PERMS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final String[] CAMERA_PERMS = {
            android.Manifest.permission.CAMERA
    };
    private static final String[] CONTACTS_PERMS = {
            android.Manifest.permission.READ_CONTACTS
    };
    private static final int INITIAL_REQUEST = 133;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    private final int MY_PERMISSION_FOR_CAMERA = 11;
    private Double lat, lng;

    static IconGenerator iconFactory;
    int[] game_min = new int[15], game_max = new int[15];
    MarkerOptions markerOptions;
    Marker[] Markertext = new Marker[10];
    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "", fullAddres = "", locality = "";

    private String[] config = new String[15], rate_growth = new String[10], name = new String[15], listing = new String[10], portal = new String[10], transaction = new String[10], id = new String[15], distance = new String[5];
    private Marker[] mCustomerMarker = new Marker[15];
    private int llMin = 35, llMax = 60, orMin = 21000, orMax = 27000, c = 0;
    private int[] or_psf = new int[15], ll_pm = new int[15];
    private LatLng[] loc = new LatLng[15];
    private boolean flag[] = new boolean[15];
    Integer balance;
    private Timer[] gametimer = new Timer[15];
    private Timer[] timer1 = new Timer[15];
    private Timer timer, DisplayBuildingTimer, HideBuildingsTimer;
    private int[] SellorBuyPrice = new int[15], rand = new int[15];
    private int mposition = 0;
    private int INDEX;
    LatLng[] cent = new LatLng[15];
    private Point[] buildingPosition = new Point[15];
    LinearLayout recordWorkout, searchbar;

    boolean locked, gameflag = false,gameDiscountCardFlag=false;
    private long lastTouched = 0, start = 0;
    private static final long SCROLL_TIME = 200L;
    private int[] status = new int[15];

    private TextView week, month, year, clocktick;
    private int w = 0, m = 0, y = 0;
    int tickcount = 3, buildingcount = 0, TimeInMillis = 4000;
    private Animation shake;
    private Timer clockTickTimer;
    double results ;

    Point centerPoint,rightendPoint;
    Geofence geofence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game );
        ButterKnife.bind( this );
//        r= getResources().getResourceName( R.raw.fold );
//        iv_markerpin = (ImageView) findViewById(R.id.iv_markerpin);
        mHelperView = (View) findViewById( R.id.br_helperView );
        autoCompView = (AutoCompleteTextView) findViewById( R.id.inputSearch );
        location_button = (ImageView) findViewById( R.id.location_button1 );
        icon1 = BitmapDescriptorFactory.fromResource( R.drawable.buildingiconbeforeclick );
        icon2 = BitmapDescriptorFactory.fromResource( R.drawable.buildingicononclick );
        icon3 = BitmapDescriptorFactory.fromResource( R.drawable.building_icon_red22 );
        profitIcon = BitmapDescriptorFactory.fromResource( R.drawable.profit );
        lossIcon = BitmapDescriptorFactory.fromResource( R.drawable.loss );
        shake = (AnimationUtils.loadAnimation( this, R.anim.zoomout_slide_left ));
        searchbar = (LinearLayout) findViewById( R.id.searchbar );
        pin = (ImageView) findViewById( R.id.pin );
//        recordWorkout = (LinearLayout) findViewById(R.id.recordWorkout);
        lock = (ImageView) findViewById( R.id.lock );

        week = (TextView) findViewById( R.id.week );
        month = (TextView) findViewById( R.id.month );
        year = (TextView) findViewById( R.id.year );
        clocktick = (TextView) findViewById( R.id.clocktick1 );

        try {
            MyApplication application = (MyApplication) getApplication();
            Tracker mTracker = application.getDefaultTracker();

            mTracker.setScreenName("Game");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!SharedPrefs.getString( this, SharedPrefs.My_BALANCE ).equals( "" )) {

            Log.i( "balance", "balance 11:" + SharedPrefs.getString( this, SharedPrefs.My_BALANCE ) );
            balance = Integer.parseInt( SharedPrefs.getString( this, SharedPrefs.My_BALANCE ) );
            balance1.setText( String.valueOf( balance ) );
        } else {
            Log.i( "balance", "balance :" + SharedPrefs.getString(this, SharedPrefs.My_BALANCE));
            SharedPrefs.save(this,SharedPrefs.My_BALANCE,100+"");
            balance = 100;
            balance1.setText(String.valueOf(balance ));
        }
        gm_cashback_btn.setVisibility(View.VISIBLE);
        myaccount.setVisibility( View.VISIBLE );
//      balance1=(TextView) findViewById( R.id.balance1 );
        final Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayShowHomeEnabled( true );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle( "" );

        autoCompView.setAdapter( new AutoCompletePlaces.GooglePlacesAutocompleteAdapter( this, R.layout.list_item1 ) );
        autoCompView.setOnItemClickListener( this );
        autoCompView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompView.clearListSelection();
                autoCompView.setText( "" );
                autoCompView.showDropDown();


            }
        } );
        customMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById( R.id.g_map ));
        map = customMapFragment.getMap();
        final View mMapView = getSupportFragmentManager().findFragmentById( R.id.g_map ).getView();
        customMapFragment.getMapAsync( new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                final LocationManager Loc_manager = (LocationManager) getBaseContext().getSystemService( Context.LOCATION_SERVICE );

                if (!isNetworkAvailable() || !(Loc_manager.isProviderEnabled( LocationManager.GPS_PROVIDER ))) {
                    map = googleMap;
                    double lat11 = 19.1269299;
                    double lng11 = 72.8376545999999;
                    Log.i( "slsl", "location====================:1 " );
                    LatLng currLatLong = new LatLng( lat11, lng11 );
                    map.moveCamera( CameraUpdateFactory.newLatLngZoom( currLatLong, 13 ) );
                }

                VisibleRegion visibleRegion = map.getProjection()
                        .getVisibleRegion();
                Point x1 = map.getProjection().toScreenLocation( visibleRegion.farRight );
                Point y1 = map.getProjection().toScreenLocation( visibleRegion.nearLeft );
                centerPoint = new Point( x1.x / 2, y1.y / 2 );
                rightendPoint= new Point( x1.x , y1.y/2  );
//                enableMyLocation();
                Log.i( "slsl", "location====================: " );
                getLocationActivity = new GetCurrentLocation( getBaseContext(), mcallback );
                // map.setPadding(left, top, right, bottom);
                map.setPadding( 0, 100, 0, 0 );


            }
        } );

        map.getUiSettings().setRotateGesturesEnabled( false );
        map.getUiSettings().setMyLocationButtonEnabled( true );
        map.getUiSettings().setScrollGesturesEnabled( true );
        map.getUiSettings().setZoomControlsEnabled( true );

        mHelperView.setOnTouchListener( new View.OnTouchListener() {
            private float scaleFactor = 1f;

            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {

                if (simpleGestureDetector.onTouchEvent( motionEvent )) { // Double tap
                    map.animateCamera( CameraUpdateFactory.zoomIn() ); // Fixed zoom in
                } else if (motionEvent.getPointerCount() == 1) { // Single tap
                    onMapDrag( motionEvent );
                    mMapView.dispatchTouchEvent( motionEvent ); // Propagate the event to the map (Pan)
                } else if (scaleGestureDetector.onTouchEvent( motionEvent )) { // Pinch zoom

                    map.moveCamera( CameraUpdateFactory.zoomBy( // Zoom the map without panning it
                            (map.getCameraPosition().zoom * scaleFactor
                                    - map.getCameraPosition().zoom) / 5 ) );
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
            } );

            // Gesture detector to manage scale gestures
            private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(
                    getBaseContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    scaleFactor = detector.getScaleFactor();
                    return true;
                }
            } );


        } );



        gm_cashback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        StopAllThread();
                       opengameDiscountCard();
            }
        });



        location_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationActivity = new GetCurrentLocation( getBaseContext(), mcallback );
            }
        } );


        map.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Marker m;
                int i;
//                                            if (mposition == 1) {
                for (i = 0; i < 5; i++) {//buildingcount

                    if (mCustomerMarker[i] != null && (marker.getId().equals( mCustomerMarker[i].getId() ) || marker.getId().equals( Markertext[i].getId() ))) {
                        INDEX = i;
                        if (status[i] == 1) {
                            Log.i( "lowbalance", "value sus: " + rand[i] + " " + balance );
                            if (rand[i] <= balance) {
                                mCustomerMarker[i].setIcon( icon2 );
                                SellorBuyPrice[i] = rand[i];
                                balance = balance - SellorBuyPrice[i];
                                //rate= rand[i];
                                buySound();
                                UpdateBalancesub( rand[i] );
                                status[i] = 2;
//                                                               balance1.setText( rand[i] );
//                                                           flag[i] = false;
                            } else {
//                                                                m = mCustomerMarker[i];
                                dropPinEffect1( mCustomerMarker[i] );
                                dropPinEffect1( Markertext[i] );
                                lockedBuilding( mCustomerMarker[i] );
                                errorSound();
//                                                                mCustomerMarker[i].remove();
//                                                                mCustomerMarker[i]=map.addMarker( new MarkerOptions().position( m.getPosition() ).icon(icon3));
                                Vibrator v = (Vibrator) Game.this.getSystemService( Context.VIBRATOR_SERVICE );
                                // Vibrate for 500 milliseconds
                                v.vibrate( 1000 );
                                Log.i( "lowbalance", "lowbalance" + rand[i] + " " + balance );
                                SnackbarManager.show(
                                        Snackbar.with( Game.this )
                                                .text( "Sorry , low balance,find cheaper location or buy credits." )
                                                .position( Snackbar.SnackbarPosition.BOTTOM_CENTER )
                                                .color( Color.RED ) );
//                                                                mCustomerMarker[i].setIcon( icon1 );
                            }

                        } else if (status[i] == 2) {
                            int j;
                            /* if (i < 4)
                               j = i + 2;
                               else
                               j = 0;*/
                            Cancel_timer( i );
                            balance = balance + rand[i];
                            UpdateBalanceadd( rand[i] );

                            if (rand[i] >= SellorBuyPrice[i]) {
                                profitSound();
                                profitImageDisplay( mCustomerMarker[i] );

                            } else {
                                lossSound();
                                lossImageDisplay( mCustomerMarker[i] );
                                myaccount.setBackgroundColor(Color.parseColor("#2dc4b6"));
//                              mCustomerMarker[i].remove();
                            }
                            Markertext[i].remove();
                            status[i] = 0;
                            /*j= RandomBuildingDisplay(i);


                            Log.i( "chekflag","check " +flag[j]+" "+j);

                            checkPlace(j);

                            flag[i]=false;*/


                        }
                    } else {
//                              mCustomerMarker[i].setIcon(icon1);

                    }
                }
//                                            }

                return true;

            }

        } );

        mcallback = new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    SharedPrefs.save( getBaseContext(), SharedPrefs.MY_LAT, lat + "" );
                    SharedPrefs.save( getBaseContext(), SharedPrefs.MY_LNG, lng + "" );
                    if (isNetworkAvailable()) {
                        try {
                            getRegion();
                            new LocationUpdater().execute();

                        } catch (Exception e) {
                            Log.i( "Exception", "caught in get region" );
                        }
                    }
                    LatLng currentLocation = new LatLng( lat, lng );
                    Log.i( "bbt1", "lat_long" + currentLocation );
                    map.moveCamera( CameraUpdateFactory.newLatLng( currentLocation ) );
                    map.moveCamera( CameraUpdateFactory.zoomTo( 13 ) );
                    RadiustoCenter(currentLocation);
                    //getPrice();
                }
            }
        };
        init();

    }


    private void init() {
        mHelperView.setEnabled( false );
//                map.getUiSettings().setScrollGesturesEnabled(false);
//        recordWorkout.setVisibility(View.GONE);

        map.getUiSettings().setZoomControlsEnabled( false );
        map.getUiSettings().setAllGesturesEnabled( false );
        mposition = 1;
//        ((ClientMainActivity)getActivity()).GameModeActivated();
        lock.setVisibility( View.VISIBLE );
        locked = false;
     /* Displaybuilding();

        try{
            if(gameflag==true) {
                        map.clear();
//                        metropolitandraw(0);
                locked=false;
                Displaybuilding();
            }

            else
                gameflag=true;
        }catch (Exception e){}*/


        lock.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (locked) {
                    mHelperView.setEnabled( false );
                    searchbar.setVisibility( View.GONE );
                    pin.setVisibility( View.GONE );
                    map.getUiSettings().setAllGesturesEnabled( false );
                    lock.setBackground( getResources().getDrawable( R.drawable.locked ) );
                    clocktick.setVisibility( View.GONE );
//                    tickcount=3;
                    if(clockTickTimer!=null){
                        clockTickTimer.cancel();
                    }
                    locked = false;
                } else {

                    mHelperView.setEnabled( true );
                    searchbar.setVisibility( View.VISIBLE );
                    pin.setVisibility( View.VISIBLE );
                    map.getUiSettings().setScrollGesturesEnabled( true );
                    lock.setBackground( getResources().getDrawable( R.drawable.unlocked ) );
                    locked = true;
//                    clocktick();
                    clocktick.setVisibility( View.VISIBLE );
                    StopAllThread();
                    lockedTimer ();
                }

            }
        } );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.hideSoftInputFromWindow( autoCompView.getWindowToken(), 0 );
        map.animateCamera( CameraUpdateFactory.zoomTo( 13 ) );
        autoCompView.clearListSelection();
        //rem
        getLocationFromAddress( autoCompView.getText().toString() );
        if (isNetworkAvailable()) {
            getRegion();
            new LocationUpdater().execute();
        }
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
                HttpPost httppost = new HttpPost( url );
                HttpResponse response = httpclient.execute( httppost );
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

            } catch (Exception e) {
            }

            // convert response to string
            try {
                BufferedReader reader = new BufferedReader( new InputStreamReader( is, "iso-8859-1" ), 8 );
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append( line + "\n" );
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
            }

            try {
                jObject = new JSONObject( result );
            } catch (JSONException e) {
            }

            return jObject;
        }

        @Override
        protected String doInBackground(Double[] objects) {

            try {
                String lat1 = SharedPrefs.getString( getBaseContext(), SharedPrefs.MY_LAT );
                String lng1 = SharedPrefs.getString( getBaseContext(), SharedPrefs.MY_LNG );
                JSONObject jsonObj = getJSONfromURL( "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat1 + ","
                        + lng1 + "&sensor=true" );
                String Status = jsonObj.getString( "status" );
                if (Status.equalsIgnoreCase( "OK" )) {
                    JSONArray Results = jsonObj.getJSONArray( "results" );
                    JSONObject zero = Results.getJSONObject( 0 );
                    JSONArray address_components = zero.getJSONArray( "address_components" );
                    fullAddres = zero.getString( "formatted_address" );
                    locality = zero.getString( "sublocality_level_1" );
                    for (int i = 0; i < address_components.length(); i++) {
                        JSONObject zero2 = address_components.getJSONObject( i );
                        String long_name = zero2.getString( "long_name" );
                        JSONArray mtypes = zero2.getJSONArray( "types" );
                        String Type = mtypes.getString( 0 );
                        if (TextUtils.isEmpty( long_name ) == false || !long_name.equals( null ) || long_name.length() > 0 || long_name != "") {
                            if (Type.equalsIgnoreCase( "street_number" )) {
                                Address1 += long_name;
                            } else if (Type.equalsIgnoreCase( "route" )) {
                                Address1 += " " + long_name;
                            } else if (Type.equalsIgnoreCase( "sublocality_level_2" )) {
                                Address2 = long_name;
                                if (this != null) {
                                    SharedPrefs.save( getBaseContext(), SharedPrefs.MY_LOCALITY, long_name );
                                }
                            } else if (Type.equalsIgnoreCase( "sublocality_level_1" )) {
                                Address2 += " " + long_name;
//                                if (getActivity() != null)
//                                    SharedPrefs.save(getActivity(),SharedPrefs.MY_LOCALITY,long_name);
                            } else if (Type.equalsIgnoreCase( "locality" )) {
                                // Address2 = Address2 + long_name + ", ";
                                City = long_name;
                                SharedPrefs.save( getBaseContext(), SharedPrefs.MY_CITY, City );
                            } else if (Type.equalsIgnoreCase( "administrative_area_level_2" )) {
                                County = long_name;
                            } else if (Type.equalsIgnoreCase( "administrative_area_level_1" )) {
                                State = long_name;
                            } else if (Type.equalsIgnoreCase( "country" )) {
                                Country = long_name;
                            } else if (Type.equalsIgnoreCase( "postal_code" )) {
                                PIN = long_name;
                                SharedPrefs.save( getBaseContext(), SharedPrefs.MY_PINCODE, PIN );
                            }
                        }
                        if (this != null)
                            SharedPrefs.save( getBaseContext(), SharedPrefs.MY_REGION, fullAddres );
                        // JSONArray mtypes = zero2.getJSONArray("types");
                        // String Type = mtypes.getString(0);
                        // Log.e(Type,long_name);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return fullAddres;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute( s );
            autoCompView.setText( s );
            Log.i( "address", "address" + s );
            autoCompView.dismissDropDown();
        }
    }

    public void getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder( this );
        List<Address> address;
        try {
            address = coder.getFromLocationName( strAddress, 5 );
            Address location = address.get( 0 );
            lat = location.getLatitude();
            lng = location.getLongitude();
            Log.i( "lat=" + location.getLatitude(), " long=" + location.getLongitude() );
            LatLng l = new LatLng( location.getLatitude(), location.getLongitude() );
            Log.i( "t1", "lng" + " " + lng );
            SharedPrefs.save( this, SharedPrefs.MY_LAT, lat + "" );
            SharedPrefs.save( this, SharedPrefs.MY_LNG, lng + "" );
            map.moveCamera( CameraUpdateFactory.newLatLng( l ) );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {

                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        customMapFragment.getMapAsync( new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                map = googleMap;
                                //enableMyLocation();
                                if (ActivityCompat.checkSelfPermission( getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                map.setMyLocationEnabled( true );
                                Log.i( "t1", "broker_map" + map );
                            }
                        } );
                        getLocationActivity = new GetCurrentLocation( this, mcallback );

                    }

        } catch (Exception e) {
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*CancelDisplayBuildingTimer();
        HideBuildingsTimer();
        clockTickTimer.cancel();*/
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        if(gameDiscountCardFlag)

        {
            closeCardContainer();
        }else {
            if (clockTickTimer != null)

                clockTickTimer.cancel();
            StopAllThread();
            SharedPrefs.save(this, SharedPrefs.My_BALANCE, balance + "");
            Intent intent = new Intent(this, ClientMainActivity.class);
            intent.addFlags(

                    Intent.FLAG_ACTIVITY_CLEAR_TOP |

                            Intent.FLAG_ACTIVITY_CLEAR_TASK |

                            Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        //super.onBackPressed();
    }

    public void StopAllThread(){
        CancelDisplayBuildingTimer();
        HideBuildingsTimer();
        for(int i=0;i<buildingcount;i++){
            if(timer1[i]!=null){
                Cancel_timer(i);
            }
        }
    }

    private void onMapDrag(final MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            tickcount = 3;

        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            final long now = SystemClock.uptimeMillis();
            long future = System.currentTimeMillis() + 3000;


            if (isNetworkAvailable()) {
                if (now - lastTouched > SCROLL_TIME && !(motionEvent.getPointerCount() > 1)) {
                    LatLng currentLocation1; //= new LatLng(location.getLatitude(), location.getLongitude());
                    Log.i( "map", "============ map:" + " " + map );
                  /*  while(System.currentTimeMillis()<future) {
                        try {
                            wait( future - System.currentTimeMillis() );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
//                    if (tickcount <= 3) {
//                    clocktick();

                    /*TimeInMillis = TimeInMillis + 1000;

                    Log.i( "longmillis", "longmillis  111 " + TimeInMillis );*/
//                    }
                    /*VisibleRegion visibleRegion = map.getProjection()
                            .getVisibleRegion();
                    Point x1 = map.getProjection().toScreenLocation( visibleRegion.farRight );
                    Point y1 = map.getProjection().toScreenLocation( visibleRegion.nearLeft );
                    centerPoint = new Point( x1.x / 2, y1.y / 2 );
                    rightendPoint= new Point( x1.x , y1.y/2  );*/
                    currentLocation1 = map.getProjection().fromScreenLocation(
                            centerPoint );
                    /*LatLng currentLocation = map.getProjection().fromScreenLocation(
                            rightendPoint );*/
                   // currentLocation1 = centerFromPoint;
                    lat = currentLocation1.latitude;
                    Log.i( "t1", "lat" + " " + lat );
                    lng = currentLocation1.longitude;
                    SharedPrefs.save( this, SharedPrefs.MY_LAT, lat + "" );
                    SharedPrefs.save( this, SharedPrefs.MY_LNG, lng + "" );
                    General.setSharedPreferences( this, AppConstants.MY_LAT, lat + "" );
                    General.setSharedPreferences( this, AppConstants.MY_LNG, lng + "" );
                    getRegion();
                    new LocationUpdater().execute();
                    //map.addMarker(new MarkerOptions().position(currentLocation1));
                   // map.addMarker(new MarkerOptions().position(currentLocation));
                    RadiustoCenter(currentLocation1);
                    /*for(int i=0;i<buildingcount;i++) {
                        if (Markertext != null) {
                            Markertext[i].remove();

                        }
                    }*/
                    CancelDisplayBuildingTimer();
                    HideBuildingsTimer();
                   // getPrice();
                }

            } else {
                General.internetConnectivityMsg( this );
            }


        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            lastTouched = SystemClock.uptimeMillis();
            Log.i( "MotionEvent.ACTION_DOWN", "=========================" );


        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void getPrice() {

        //getRegion();

        if (General.isNetworkAvailable( this )) {
            General.slowInternet( this );

            User user = new User();

           // user.setDeviceId( General.getSharedPreferences( getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI ) );
           // Log.i( "PREOK", "getcontext " + General.getSharedPreferences( getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI ) );
            user.setGcmId( SharedPrefs.getString( this, SharedPrefs.MY_GCM_ID ) );
            //user.setUserRole( "client" );
            user.setLongitude( SharedPrefs.getString( this, SharedPrefs.MY_LNG ) );
            //user.setProperty_type( "home" );
            user.setLatitude( SharedPrefs.getString( this, SharedPrefs.MY_LAT ) );
            Log.i( "t1", "My_lng inside game api" + "  " + SharedPrefs.getString( this, SharedPrefs.MY_LNG ) +"    : "+ results);
            user.setLocality( "" );
            user.setDistance(results+"");
            Log.i( "t1", "My_lat" + "  " + SharedPrefs.getString( this, SharedPrefs.MY_LAT ) );
            user.setPlatform( "android" );
            Log.i( "my_locality", SharedPrefs.getString( this, SharedPrefs.MY_LOCALITY ) );
//            user.setPincode( "400058" );
            if (General.getSharedPreferences( this, AppConstants.IS_LOGGED_IN_USER ).equals( "" )) {
                user.setUserId( General.getSharedPreferences( this, AppConstants.TIME_STAMP_IN_MILLI ) );

            } else {
                user.setUserId( General.getSharedPreferences( this, AppConstants.USER_ID ) );
            }
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint( AppConstants.SERVER_BASE_URL ).build();
            restAdapter.setLogLevel( RestAdapter.LogLevel.FULL );

            UserApiService userApiService = restAdapter.create( UserApiService.class );


            userApiService.game( user, new retrofit.Callback<JsonElement>() {

                @Override
                public void success(JsonElement jsonElement, Response response) {
                    try {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        String strResponse = new String( ((TypedByteArray) response.getBody()).getBytes() );
                        JSONObject jsonResponse = new JSONObject( strResponse );
                        String errors = jsonResponse.getString( "errors" );
                        JSONObject jsonResponseData = new JSONObject( jsonResponse.getString( "responseData" ) );
                        // horizontalPicker.stopScrolling();
                            Log.i( "TRACE", "Response getprice buildings jsonResponseDat" + jsonResponseData );
                            JSONObject price = new JSONObject( jsonResponseData.getString( "price" ) );
                            Log.i( "TRACE", "Response getprice buildings pricer " );
                            Log.i( "TRACE", "Response getprice buildings price " + price );
                            JSONArray buildings = new JSONArray( jsonResponseData.getString( "buildings" ) );
                            Log.i( "TRACE", "Response getprice buildings" + buildings );
                            JSONObject k = new JSONObject( buildings.get( 1 ).toString() );
                            Log.i( "TRACE", "Response getprice buildings yo" + price.getString( "ll_min" ) );
                            if (!price.getString( "ll_min" ).equalsIgnoreCase( "" )) {
                                if (!price.getString( "ll_min" ).equalsIgnoreCase( "0" )) {
                                    Log.i( "tt", "I am here" + 2 );
                                    Log.i( "TRACE", "RESPONSEDATAr" + response );
                                    llMin = Integer.parseInt( price.getString( "ll_min" ) );
                                    llMax = Integer.parseInt( price.getString( "ll_max" ) );
                                    Log.i( "TRACE", "RESPONSEDATArr" + llMin );
                                    Log.i( "TRACE", "RESPONSEDATArr" + llMax );
                                    llMin = 5 * (Math.round( llMin / 5 ));
                                    llMax = 5 * (Math.round( llMax / 5 ));
                                    Log.i( "TRACE", "RESPONSEDATAr" + llMin );
                                    Log.i( "TRACE", "RESPONSEDATAr" + llMax );
                                    orMin = Integer.parseInt( price.getString( "or_min" ) );
                                    orMax = Integer.parseInt( price.getString( "or_max" ) );
                                    Log.i( "TRACE", "RESPONSEDATArr" + orMin );
                                    Log.i( "TRACE", "RESPONSEDATArr" + orMax );
                                    orMin = 500 * (Math.round( orMin / 500 ));
                                    orMax = 500 * (Math.round( orMax / 500 ));
                                    Log.i( "TRACE", "RESPONSEDATAr" + orMin );
                                    Log.i( "TRACE", "RESPONSEDATAr" + orMax );
                                    map.clear();
                                    Log.i( "sizebuilding", "building" + buildings.length() );
                                    buildingcount = buildings.length();
                                    try {
                                        for (int i = 0; i < buildings.length(); i++) {

                                            JSONObject j = new JSONObject( buildings.get( i ).toString() );
                                           /* config[i] = j.getString( "config" );
                                            Log.i( "metropolitandraw", "config : " + config[i] );*/
                                            name[i] = j.getString( "name" );
                                            Log.i( "metropolitandraw", "name : " + name[i] );
                                            /*rate_growth[i] = j.getString( "rate_growth" );
                                            Log.i( "metropolitandraw", "rate_growth : " + rate_growth[i] );*/
                                            or_psf[i] = Integer.parseInt( j.getString( "or_psf" ) );
                                            Log.i( "metropolitandraw", "or_psf : " + or_psf[i] );
//                                            ll_pm[i] = Integer.parseInt( j.getString( "ll_pm" ) );
                                            id[i] = j.getString( "id" );
                                            Log.i( "metropolitandraw", "id : " + id[i] );
                                            double lat = Double.parseDouble( j.getJSONArray( "loc" ).get( 1 ).toString() );
                                            Log.i( "metropolitandraw", "lat : " + lat );
                                            double longi = Double.parseDouble( j.getJSONArray( "loc" ).get( 0 ).toString() );
                                            Log.i( "metropolitandraw", "long  : " + longi );
                                            loc[i] = new LatLng( lat, longi );
                                            Log.i( "metropolitandraw", "loc :  " + loc );

                                            if (status[i] == 2 || status[i] == 1) {
                                                Cancel_timer( i );
                                            }
//l
                                        }
                                        OnScreenCo_ordinateFromLatLng();
//
                                    } catch (Exception e) {
                                    }
                                    OnScreenCo_ordinateFromLatLng();
                                    map.clear();
                                    DisplayBuilding();
                                    HideBuildings();
                                    gametimer();
                                } else {
                                    Log.i( "tt", "I am here" + 3 );
                                    map.clear();
                                }
                            }
                    } catch (Exception e) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        Log.i( "Price Error", "Caught in exception getprice success" + e.getMessage() );
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    General.slowInternetFlag = false;
                    map.clear();
                    General.t.interrupt();
                    Log.i( "getPrice", "retrofit failure getprice " + error.getMessage() );
                }
            } );

        } else {
            General.internetConnectivityMsg( getBaseContext() );

        }
    }


/*
    public void getPrice() {

        //getRegion();

            User user = new User();

            // user.setDeviceId( General.getSharedPreferences( getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI ) );
            // Log.i( "PREOK", "getcontext " + General.getSharedPreferences( getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI ) );
            user.setGcmId( SharedPrefs.getString( this, SharedPrefs.MY_GCM_ID ) );
            //user.setUserRole( "client" );
            user.setLongitude( SharedPrefs.getString( this, SharedPrefs.MY_LNG ) );
            //user.setProperty_type( "home" );
            user.setLatitude( SharedPrefs.getString( this, SharedPrefs.MY_LAT ) );
            Log.i( "t1", "My_lng inside game api" + "  " + SharedPrefs.getString( this, SharedPrefs.MY_LNG ) );
            user.setLocality( "" );
            Log.i( "t1", "My_lat" + "  " + SharedPrefs.getString( this, SharedPrefs.MY_LAT ) );
            user.setPlatform( "android" );
            Log.i( "my_locality", SharedPrefs.getString( this, SharedPrefs.MY_LOCALITY ) );
//            user.setPincode( "400058" );
            if (General.getSharedPreferences( this, AppConstants.IS_LOGGED_IN_USER ).equals( "" )) {
                user.setUserId( General.getSharedPreferences( this, AppConstants.TIME_STAMP_IN_MILLI ) );

            } else {
                user.setUserId( General.getSharedPreferences( this, AppConstants.USER_ID ) );
            }
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint( AppConstants.SERVER_BASE_URL ).build();
            restAdapter.setLogLevel( RestAdapter.LogLevel.FULL );

            UserApiService userApiService = restAdapter.create( UserApiService.class );


            userApiService.game(user,new retrofit.Callback<JsonElement>(){










        }
    }
    */




    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position, int marker_position) {
        markerOptions = new MarkerOptions().
                icon( BitmapDescriptorFactory.fromBitmap( iconFactory.makeIcon( String.valueOf( (text) ) ) ) ).
                position( position ).
                anchor( iconFactory.getAnchorU(), iconFactory.getAnchorV() );
        Markertext[marker_position] = map.addMarker( markerOptions );
    }


    private void dropPinEffect(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;
        final Interpolator interpolator = new BounceInterpolator();
        handler.post( new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation( (float) elapsed
                                / duration ), 0 );
                marker.setAnchor( 0.5f, 1.0f + 14 * t );
                if (t > 0.0) {
                    handler.postDelayed( this, 15 );
                } else {
//                    Log.i(TAG,"building drop ");
                }
            }
        } );
    }


    private void ProfitLossAnimator(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new AnticipateOvershootInterpolator();

        handler.post( new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation( (float) elapsed
                                / duration ), 0 );
//                marker.setAnchor(0.5f, 1.0f + 14 * t);
                marker.setAnchor( 0.5f + t, 1.0f );
                if (t > 0.0) {
                    handler.postDelayed( this, 15 );
                } else {
//                    Log.i(TAG,"building drop ");


                }
            }
        } );
    }


    public void AnimationShake() {

    }


    private void dropPinEffect1(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1000;

        final Interpolator interpolator = new BounceInterpolator(); /*Interpolator() {
            @Override
            public float getInterpolation(float input) {

                return 0;
            }
        };*/

        handler.post( new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max( 0,
                        1 - interpolator.getInterpolation( (float) elapsed
                                / duration ) );
                marker.setAnchor( 0.5f + t, 1.0f );
//                marker.setAnchor(-(0.5f)+1 *t, 1.0f );
//                marker.setIcon( icon1 );

                if (t > 0.0) {
                    handler.postDelayed( this, 10 );
                } else {
//                    Log.i(TAG,"building drop ");


                }
            }

        } );
//
    }

    public void playSound() {

        MediaPlayer mp = MediaPlayer.create( this, R.raw.building_visible );
        mp.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
            }

        } );
        mp.start();

    }


    public void buySound() {

        MediaPlayer mp = MediaPlayer.create( this, R.raw.jump );
        mp.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
            }

        } );
        mp.start();

    }

    public void tickSound() {

        MediaPlayer mp = MediaPlayer.create( this, R.raw.tick );
        mp.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
            }

        } );
        mp.start();

    }


    public void profitSound() {

        MediaPlayer mp = MediaPlayer.create( this, R.raw.profit );
        mp.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
            }

        } );
        mp.start();

    }

    public void lossSound() {

        MediaPlayer mp = MediaPlayer.create( this, R.raw.loss );
        mp.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
            }

        } );
        mp.start();

    }

    public void errorSound() {

        MediaPlayer mp = MediaPlayer.create( this, R.raw.error );
        mp.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
            }

        } );
        mp.start();

    }

    private void stopTimer(int index) {
        if (gametimer[index] != null) {
            gametimer[index].cancel();
            gametimer[index].purge();
            timer1 = null;
        }
    }

    public int randInt(int min, int max, int position) {
        /*Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 10) + min;
        return randomNum;*/
        if (min <= max) {
            min = min + 1;
            game_min[position] = min;
            return min;


        } else {
            max = max - 1;
            game_max[position] = max;
            return max;
        }
    }

    public void OnScreenCo_ordinateFromLatLng() {
        Point p;
        for (int i = 0; i < 5; i++) {
//            mCustomerMarker[i].getPosition();
//            LatLng cent = mCustomerMarker[i].getPosition();
            LatLng cent = loc[i];
            buildingPosition[i] = map.getProjection().toScreenLocation( cent );
            Log.i( "OnScreen", "OnScreenCo_ordinateFromLatLng  : " + buildingPosition[i] );
        }
        for (int i = 0; i < 5; i++) {

            p = new Point( buildingPosition[i].x, buildingPosition[i].y - 65 );
            cent[i] = map.getProjection().fromScreenLocation( p );
            Log.i( "OnScreen", "OnScreenCo_ordinateFromLatLng  : " + buildingPosition[i] );
        }
    }


    public void UpdateBalanceadd(int value) {
        Log.i( "balance", "balance amount add:" + value );
        int bal = Integer.parseInt( balance1.getText().toString() );
        bal = bal + value;
        balance1.setText( String.valueOf( bal ) );
    }

    public void UpdateBalancesub(int value) {
        Log.i( "balance", "balance amount sub:" + value );
        int bal = Integer.parseInt( balance1.getText().toString() );
        bal = bal - value;
        balance1.setText( String.valueOf( bal ) );
    }


    public int randposition(int min, int max) {


        if (min < max) {
            min = min + 1;
            return min;
        } else {
            max = max - 1;
            return max;
        }
        /*Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 3) + min;
        return randomNum;*/
    }

    private void metropolitandraw(int i) {
        game_min[i] = (or_psf[i] / 1000) + 10;
        game_max[i] = (or_psf[i] / 1000) + 20;
        Log.i( "metropolitandraw", "metropolitandraw11:" + i );
        iconFactory = new IconGenerator( this );
        mCustomerMarker[i] = map.addMarker( new MarkerOptions().position( loc[i] ).icon( icon1 ) );
        iconFactory.setStyle( IconGenerator.STYLE_GREEN );
        addIcon( iconFactory, game_min[i] + "k", cent[i], i );
        dropPinEffect( mCustomerMarker[i] );
        dropPinEffect( Markertext[i] );
        playSound();
        status[i] = 1;
        PropertyRateChange( i );
        /*Thread t=new Thread(runnable);
        t.start();*/

    }


    Runnable runnable = new Runnable() {


        @Override
        public void run() {
            long future = System.currentTimeMillis() + 10000;

            /* while(System.currentTimeMillis()<future){
            try {
                    wait(future-System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String rate;
                rand[4] = randInt(game_min[4], game_max[4],4);
                iconFactory = new IconGenerator(getContext());
                if (game_min[4]<=game_max[4]) {
                    iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                } else
                    iconFactory.setStyle(IconGenerator.STYLE_RED);
                rate = String.valueOf(rand[4]);
                if(Markertext[4]!=null)
                    Markertext[4].remove();
                addIcon(iconFactory,rate+"k",cent[4],4);
                if((game_max[4]<30 && game_min[4]>56))
                {
                    game_max[4]=56;
                    game_min[4]=30;
                }

            }*/
            Log.i( "thread1", "handler========1    " );

            h.sendEmptyMessageDelayed( 0, 10000 );
        }
    };

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            Log.i( "thread1", "handler========    " );
            /*if(mposition==1){

                metropolitandraw(randposition(0,4));
            }else
                map.clear();*/
        }

    };


    public void PropertyRateChange(final int i) {
  /*  long future = System.currentTimeMillis() + 200;
    while (System.currentTimeMillis() < future) {
        try {
            wait(future - System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }*/
        if (timer1[i] == null) {
            timer1[i] = new Timer();
            timer1[i].schedule( new TimerTask() {
                @Override
                public void run() {
                    if (this != null) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                String rate;
                                rand[i] = randInt( game_min[i], game_max[i], i );
                                rate = String.valueOf( rand[i] );
                                iconFactory = new IconGenerator( getBaseContext() );
                                if (Markertext[i] != null)
                                    Markertext[i].remove();
                                if (game_min[i] <= game_max[i]) {
                                    iconFactory.setStyle( IconGenerator.STYLE_GREEN );
                                } else
                                    iconFactory.setStyle( IconGenerator.STYLE_RED );
                                Log.i("countervalue11", "count new : " + rate+" "+((or_psf[i]/1000)+10)+" "+((or_psf[i]/1000)+20)+" "+game_min[i]+" "+game_max[i]);
                                addIcon( iconFactory, rate + "k", cent[i], i );
//                                c=1300;
                                if (game_max[i] < ((or_psf[i] / 1000) + 10) && game_min[i] >= ((or_psf[i] / 1000) + 20)) {
                                    game_max[i] = (or_psf[i] / 1000) + 20;
                                    game_min[i] = (or_psf[i] / 1000) +10;
                                }
                            }
                        } );
                    }
                }
            }, 500, 300 );

        }
    }


    private void Cancel_timer(int i) {
        try {
            if (timer1[i] != null) {
                timer1[i].cancel();
                timer1[i] = null;
            }
        } catch (Exception e) {
        }
    }

    private void HideBuildingsTimer() {
        try {
            if (HideBuildingsTimer != null) {
                HideBuildingsTimer.cancel();
                HideBuildingsTimer = null;
            }
        } catch (Exception e) {
        }
    }

    private void CancelDisplayBuildingTimer() {
        try {
            if (DisplayBuildingTimer != null) {
                DisplayBuildingTimer.cancel();
                DisplayBuildingTimer = null;
            }
        } catch (Exception e) {
        }
    }

/*DisplayBuildingTimer
    public void  Reminder(int seconds) {
        timer1 = new Timer();
        timer1.schedule(new RemindTask(), seconds*1000);
    }

    class RemindTask extends TimerTask {
        public void run() {
            System.out.println( "Time's up!" );
            timer1[i].cancel(); //Terminate the timer thread
        }

    }*/


    private void gametimer() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule( new TimerTask() {
                @Override
                public void run() {
                    if (this != null) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                week.setText( String.valueOf( ++w ) );
                                if (w > 0 && w % 4 == 0) {
                                    month.setText( String.valueOf( ++m ) );
                                    w = 0;
                                }
                                if (m > 0 && m % 12 == 0) {
                                    year.setText( String.valueOf( y++ ) );
                                    m = 0;
                                }
                            }
                        } );
                    }
                }
            }, 1000, 1000 );

        }

    }


    private void lockedBuilding(final Marker m) {
        try {

            new CountDownTimer( 1000, 500 ) {

                public void onTick(long millisUntilFinished) {
                    m.setIcon( icon3 );
                }

                public void onFinish() {
try {
    m.setIcon(icon1);
}catch (Exception e){}

                }
            }.start();


        } catch (Exception e) {
        }
    }


    private void profitImageDisplay(final Marker m) {
        try {

            new CountDownTimer( 1000, 500 ) {

                public void onTick(long millisUntilFinished) {
                    m.setIcon( profitIcon );
                    myaccount.setBackgroundColor(Color.parseColor("#2dc4b6"));
//                    ProfitLossAnimator( m );
                }

                public void onFinish() {
                    m.remove();

                    myaccount.setBackgroundColor(0);
                }
            }.start();


        } catch (Exception e) {
        }
    }


    private void lossImageDisplay(final Marker m) {
        try {

            new CountDownTimer( 1000, 500 ) {

                public void onTick(long millisUntilFinished) {
                    m.setIcon( lossIcon );
                    myaccount.setBackgroundColor(Color.parseColor("red"));

                }

                public void onFinish() {
                    m.remove();
                    myaccount.setBackgroundColor(0);
                }
            }.start();


        } catch (Exception e) {
        }
    }



    private void lockedTimer() {
        if(clockTickTimer!=null){
            clockTickTimer.cancel();
        }
      clockTickTimer = new Timer();
        clockTickTimer.schedule(new TimerTask() {
               @Override
               public void run () {
                   runOnUiThread( new Runnable() {
                       @Override
                       public void run() {
                           if(tickcount==0){
                               tickcount = 3;
                               SnackbarManager.show(
                                       Snackbar.with( Game.this )
                                               .text( "                  Unlock to enable Drag." )
                                               .position( Snackbar.SnackbarPosition.BOTTOM_CENTER )
                                               .color( Color.RED ) );

                               mHelperView.setEnabled( false );
                               map.getUiSettings().setAllGesturesEnabled( false );
                               lock.setBackground( getResources().getDrawable( R.drawable.locked ) );
                               locked = false;
                               clocktick.setVisibility( View.GONE );
                               searchbar.setVisibility( View.GONE );
                               pin.setVisibility( View.GONE );
                               clockTickTimer.cancel();
                           }else{
                               clocktick.setText( String.valueOf( tickcount-- ) );
                               if(tickcount<2)
                               tickSound();
                           }
                       }
                   });

               }
           },1000,1000);

    }


    /*private void clocktick() {
        tickcount = 4;
        *//*mHelperView.setEnabled( true );
        map.getUiSettings().setScrollGesturesEnabled( true );
        lock.setBackground( getResources().getDrawable( R.drawable.unlocked ) );
        locked = true;*//*


        try {


            new CountDownTimer( 3000, 1000 ) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {

                    new CountDownTimer( 4000, 1000 ) {

                        public void onTick(long millisUntilFinished) {
                            Log.i( "longmillis", "millisUntilFinished : " + millisUntilFinished );
                            clocktick.setVisibility( View.VISIBLE );
                            clocktick.setText( String.valueOf( --tickcount ) );
                            tickSound();
                        }

                        public void onFinish() {
                            if (tickcount <= 0) {
                                tickcount = 0;
                                SnackbarManager.show(
                                        Snackbar.with( Game.this )
                                                .text( "Unlock to enable Drag." )
                                                .position( Snackbar.SnackbarPosition.BOTTOM_CENTER )
                                                .color( Color.RED ) );

                                mHelperView.setEnabled( false );
                                map.getUiSettings().setAllGesturesEnabled( false );
                                lock.setBackground( getResources().getDrawable( R.drawable.locked ) );
                                locked = false;
                                clocktick.setVisibility( View.GONE );
                            }
                        }
                    }.start();


                }
            }.start();
        } catch (Exception e) {
        }
    }*/


    public void DisplayBuilding() {

        if (DisplayBuildingTimer == null) {
            DisplayBuildingTimer = new Timer();
            DisplayBuildingTimer.schedule( new TimerTask() {
                @Override
                public void run() {
                    if (this != null) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                        Log.i( "startgame","startgame : " );
                                try {
                                    radomizedBuildingDisplay();
                                } catch (Exception e) {
                                }

                            }
                        } );
                    }
                }
            }, 200, 3000 );

        }
    }


    public void radomizedBuildingDisplay() {
       // int count = buildingcount;
                int count = 5;

        Log.i( "startgame","startgame : ======================= : "+count );
        for (int i = 0; i < count; i++) {
            if (status[i] == 0) {
                Log.i( "startgame","startgame : metropolitandraw =======================" );
                metropolitandraw( i );
                break;
            }
        }
    }

    public void HideBuildings() {
        if (HideBuildingsTimer == null) {
            HideBuildingsTimer = new Timer();
            HideBuildingsTimer.schedule( new TimerTask() {
                @Override
                public void run() {
                    if (this != null) {
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    HideBuilding();
                                } catch (Exception e) {
                                }
                            }
                        } );
                    }
                }
            }, 10000, 20000 );

        }
    }

    public void HideBuilding() {
       // int count = buildingcount;
                int count = 5;

        for (int i = 0; i < count; i++) {
            if (status[i] == 1) {
                Cancel_timer( i );
                mCustomerMarker[i].remove();
                Markertext[i].remove();
                status[i] = 0;
                break;
            }
        }
    }



   private void RadiustoCenter(LatLng  oldPosition){
//       oldPosition.distanceTo(newPosition);
      /* LatLng  oldPosition = map.getProjection().fromScreenLocation(
               centerPoint );*/
       LatLng newposition = map.getProjection().fromScreenLocation(
               rightendPoint );
       // currentLocation1 = centerFromPoint;
      /* lat = oldPosition.latitude;
       Log.i( "t1", "lat" + " " + lat );
       lng = oldPosition.longitude;
       SharedPrefs.save( this, SharedPrefs.MY_LAT, lat + "" );
       SharedPrefs.save( this, SharedPrefs.MY_LNG, lng + "" );
       General.setSharedPreferences( this, AppConstants.MY_LAT, lat + "" );
       General.setSharedPreferences( this, AppConstants.MY_LNG, lng + "" );*/
       float[] result=new float[1];
       Location.distanceBetween(oldPosition.latitude, oldPosition.longitude,
               newposition.latitude, newposition.longitude, result);
       /*geofence = new Geofence.Builder()
               .setRequestId(GEOFENCE_REQ_ID) // Geofence ID
               .setCircularRegion( oldPosition.latitude, oldPosition.longitude, results) // defining fence region
              // .setExpirationDuration( 500 ) // expiring date
               // Transition types that it should look for
               .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT )
               .build();*/

Log.i("t1","result distance value ===================    :   "+result[0]);
       results=result[0];
       CircleOptions circleOptions = new CircleOptions()
               .center( new LatLng(oldPosition.latitude, oldPosition.longitude) )
               .radius( results)
               .fillColor(Color.DKGRAY)
               .strokeColor(Color.BLUE)
               .strokeWidth(2);


       getPrice();


   }


    public void opengameDiscountCard(){

        load_card_container.setBackgroundColor(Color.parseColor("#CC000000"));
        load_card_container.setClickable(true);
        gameDiscountCard gameDiscountcard= new gameDiscountCard();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        card.setClickable(true);
        gameDiscountCardFlag=true;
        fragmentTransaction.addToBackStack("card");
        fragmentTransaction.replace(R.id.card1, gameDiscountcard);
        fragmentTransaction.commitAllowingStateLoss();
//        loadFragmentAnimated(addListingFinalCard, null, R.id.card, "");

    }



    public void closeCardContainer(){

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.card1)).commit();
        load_card_container.setBackgroundColor(getResources().getColor(R.color.transparent));
        load_card_container.setClickable(false);
        card.setClickable(false);
        gameDiscountCardFlag=false;
    }



}








/*(CLLocationDistance)getMapRadius {
        CGPoint point = CGPointMake(5, self.gameMapView.frame.size.height / 2.0);
        CLLocationCoordinate2D topCenterCoor = [_gameMapView.projection coordinateForPoint:point];
        CLLocation *p1 = [[CLLocation alloc] initWithLatitude:topCenterCoor.latitude longitude:topCenterCoor.longitude];
        CLLocation *p2 = [[CLLocation alloc] initWithLatitude:_coordinate.latitude longitude:_coordinate.longitude];

        //    CLLocationCoordinate2D position = CLLocationCoordinate2DMake(lat, lon);

        _mapRadius = [p1 distanceFromLocation:p2];
        GMSCircle *geoFenceCircle = [[GMSCircle alloc] init];
        geoFenceCircle.radius = _mapRadius; // Meters
        geoFenceCircle.position = _coordinate;
        geoFenceCircle.fillColor = [UIColor colorWithWhite:0.7 alpha:0.5];
        geoFenceCircle.strokeWidth = 3;
        geoFenceCircle.strokeColor = [UIColor orangeColor];
        geoFenceCircle.map = _gameMapView; // Add it to the map.
        return _mapRadius;
        }*/

















