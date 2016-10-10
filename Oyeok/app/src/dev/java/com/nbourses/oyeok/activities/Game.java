package com.nbourses.oyeok.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.maps.android.ui.IconGenerator;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.AutoCompletePlaces;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
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
    @Bind(R.id.iv_markerpin)
    ImageView iv_markerpin;

    @Bind(R.id.myaccount)
    LinearLayout myaccount;
    private GetCurrentLocation getLocationActivity;
    private GoogleMap map;
    View mHelperView;
    ImageView location_button,lock;
    CustomMapFragment customMapFragment;
    AutoCompleteTextView autoCompView;
    private BitmapDescriptor icon1,icon2;
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
    int []game_min= new int[5],game_max=new int[5];
    MarkerOptions markerOptions;
    Marker []Markertext=new Marker[5];
    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "", fullAddres = "", locality = "";

    private  String[] config=new String[5],rate_growth =new String[5],name = new String[5],listing =new String[5],portal= new String[5],transaction=new String[5],id=new String[5],distance=new String[5];
    private  Marker[] mCustomerMarker = new Marker[5];
    private  int llMin=35, llMax=60, orMin=21000, orMax=27000,c=0;
    private int[] or_psf = new int[5], ll_pm = new int[5];
    private LatLng []loc=new LatLng[5];
    private boolean flag[] = new boolean[5];
    int balance=34;
    Thread [] gamethread=new Thread[5];
    boolean [] textFlag=new boolean[5];
    private Timer []gametimer=new Timer[5];
    private Timer [] timer1=new Timer[5];
    private int[] gamecount = new int[5],rand=new int[5];
    private int mposition=0;
    private int INDEX;
    LatLng [] cent = new LatLng[5];
    private  Point []buildingPosition= new Point[5];
    LinearLayout recordWorkout;

    boolean locked,gameflag=false;
    private long lastTouched = 0, start = 0;
    private static final long SCROLL_TIME = 200L;
    private int[] status=new int[5];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game );

        ButterKnife.bind(this);

        iv_markerpin = (ImageView) findViewById(R.id.iv_markerpin);
        mHelperView=(View) findViewById(R.id.br_helperView);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.inputSearch);
        location_button=(ImageView)findViewById( R.id.location_button1);
        icon1 = BitmapDescriptorFactory.fromResource(R.drawable.buildingiconbeforeclick);
        icon2 = BitmapDescriptorFactory.fromResource(R.drawable.buildingicononclick);
//        recordWorkout = (LinearLayout) findViewById(R.id.recordWorkout);
        lock =(ImageView) findViewById(R.id.lock);
        myaccount.setVisibility( View.VISIBLE );
//        balance1=(TextView) findViewById( R.id.balance1 );
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Game");


        autoCompView.setAdapter(new AutoCompletePlaces.GooglePlacesAutocompleteAdapter(this, R.layout.list_item1));
        autoCompView.setOnItemClickListener(this);
        autoCompView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompView.clearListSelection();
                autoCompView.setText("");
                autoCompView.showDropDown();


            }
        });
        customMapFragment = ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.g_map));
        map= customMapFragment.getMap();
        final View mMapView = getSupportFragmentManager().findFragmentById(R.id.g_map).getView();
        customMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                final LocationManager Loc_manager = (LocationManager) getBaseContext().getSystemService( Context.LOCATION_SERVICE);

                if (!isNetworkAvailable() || !(Loc_manager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                    map = googleMap;
                    double lat11 = 19.1269299;
                    double lng11 = 72.8376545999999;
                    Log.i("slsl", "location====================:1 ");
                    LatLng currLatLong = new LatLng(lat11, lng11);
                    map.moveCamera( CameraUpdateFactory.newLatLngZoom(currLatLong, 13));
                }

//                enableMyLocation();
                Log.i("slsl", "location====================: ");
                getLocationActivity = new GetCurrentLocation(getBaseContext(), mcallback);
                // map.setPadding(left, top, right, bottom);
                map.setPadding(0, 100, 0, 0);


            }
        });

        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        mHelperView.setOnTouchListener(new View.OnTouchListener() {
            private float scaleFactor = 1f;

            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {

                if (simpleGestureDetector.onTouchEvent(motionEvent)) { // Double tap
                    map.animateCamera(CameraUpdateFactory.zoomIn()); // Fixed zoom in
                } else if (motionEvent.getPointerCount() == 1) { // Single tap
                    onMapDrag(motionEvent);
                    mMapView.dispatchTouchEvent(motionEvent); // Propagate the event to the map (Pan)
                } else if (scaleGestureDetector.onTouchEvent(motionEvent)) { // Pinch zoom

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


        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationActivity = new GetCurrentLocation(getBaseContext(), mcallback);
            }
        });


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                         @Override
                                         public boolean onMarkerClick(Marker marker) {
                                             Marker m;
                                             int i;
//                                             if (mposition == 1) {
                                                 for (i = 0; i < 5; i++) {

                                                     if (  mCustomerMarker[i]!=null  && marker.getId().equals(mCustomerMarker[i].getId())) {
                                                         INDEX = i;
                                                         if (status[i]==1) {
                                                             Log.i( "lowbalance","value sus: "+ rand[i]+" "+balance);
                                                             if (rand[i] <= balance) {
                                                                 mCustomerMarker[i].setIcon( icon2 );
                                                                 balance = balance - rand[i];
                                                                 //rate= rand[i];
                                                              UpdateBalancesub(rand[i]);
                                                                 status[i]=2;
//                                                                 balance1.setText( rand[i] );
//                                                                 flag[i] = false;
                                                             } else {

                                                                 Log.i( "lowbalance","lowbalance" +rand[i]+" "+balance);
                                                                 SnackbarManager.show(
                                                                         Snackbar.with( Game.this )
                                                                                 .text( "Sorry , low balance" )
                                                                                 .position( Snackbar.SnackbarPosition.BOTTOM_CENTER )
                                                                                 .color( Color.RED ));
                                                             }

                                                         } else if(status[i]==2){
                                                             int j;
                                                            /* if (i < 4)
                                                                 j = i + 2;
                                                             else
                                                                 j = 0;*/
                                                            j= RandomBuildingDisplay(i);
                                                             balance = balance + rand[i];
                                                             Cancel_timer(i);
                                                             UpdateBalanceadd(rand[i]);
//                                                             gamethread[i].interrupt();
                                                             Markertext[i].remove();
                                                             mCustomerMarker[i].remove();


                                                             Log.i( "chekflag","check " +flag[j]+" "+j);

                                                             checkPlace(j);

                                                             flag[i]=false;
                                                             status[i]=0;

                                                         }
                                                     } else {
//                               mCustomerMarker[i].setIcon(icon1);

                                                     }
                                                 }
//                                             }

                                             return  true;

                                         }

                                     });

        mcallback = new GetCurrentLocation.CurrentLocationCallback() {

            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    lat = location.getLatitude();


                    lng = location.getLongitude();
                    SharedPrefs.save(getBaseContext(), SharedPrefs.MY_LAT, lat + "");
                    SharedPrefs.save(getBaseContext(), SharedPrefs.MY_LNG, lng + "");

                    if (isNetworkAvailable()) {
                        try {
                            getRegion();
                            new LocationUpdater().execute();

                        } catch (Exception e) {
                            Log.i("Exception", "caught in get region");
                        }
                    }

                    LatLng currentLocation = new LatLng(lat, lng);
                    Log.i("bbt1","lat_long"+currentLocation);
                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map.moveCamera(CameraUpdateFactory.zoomTo(13));
                    getPrice();


                }
            }
        };




init();

    }



    private void init(){
        mHelperView.setEnabled(false);
//                map.getUiSettings().setScrollGesturesEnabled(false);
//        recordWorkout.setVisibility(View.GONE);

        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(false);
        mposition=1;
//        ((ClientMainActivity)getActivity()).GameModeActivated();
        lock.setVisibility(View.VISIBLE);
        locked=false;
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




        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(locked){
                    mHelperView.setEnabled(false);
//                    recordWorkout.setVisibility(View.GONE);
                    map.getUiSettings().setAllGesturesEnabled(false);
                    lock.setBackground(getResources().getDrawable(R.drawable.locked));
                    locked=false;
                }else{
                    mHelperView.setEnabled(true);
//                    recordWorkout.setVisibility(View.VISIBLE);
                    map.getUiSettings().setScrollGesturesEnabled(true);
                    lock.setBackground(getResources().getDrawable(R.drawable.unlocked));
                    locked=true;
                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoCompView.getWindowToken(), 0);
        map.animateCamera(CameraUpdateFactory.zoomTo(13));
        autoCompView.clearListSelection();
        //rem
        getLocationFromAddress(autoCompView.getText().toString());
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
                HttpPost httppost = new HttpPost(url);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

            } catch (Exception e) {
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
            }

            try {
                jObject = new JSONObject(result);
            } catch (JSONException e) {
            }

            return jObject;
        }

        @Override
        protected String doInBackground(Double[] objects) {

            try {
                String lat1 = SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LAT);
                String lng1 = SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LNG);
                JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat1 + ","
                        + lng1 + "&sensor=true");
                String Status = jsonObj.getString("status");
                if (Status.equalsIgnoreCase("OK")) {
                    JSONArray Results = jsonObj.getJSONArray("results");
                    JSONObject zero = Results.getJSONObject(0);
                    JSONArray address_components = zero.getJSONArray("address_components");

                    fullAddres = zero.getString("formatted_address");
                    locality = zero.getString("sublocality_level_1");

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
                                if (this != null) {

                                    SharedPrefs.save(getBaseContext(), SharedPrefs.MY_LOCALITY, long_name);
                                }

                            } else if (Type.equalsIgnoreCase("sublocality_level_1")) {
                                Address2 += " " + long_name;

//                                if (getActivity() != null)
//                                    SharedPrefs.save(getActivity(),SharedPrefs.MY_LOCALITY,long_name);
                            } else if (Type.equalsIgnoreCase("locality")) {
                                // Address2 = Address2 + long_name + ", ";
                                City = long_name;
                                SharedPrefs.save(getBaseContext(), SharedPrefs.MY_CITY, City);
                            } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                                County = long_name;
                            } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                State = long_name;
                            } else if (Type.equalsIgnoreCase("country")) {
                                Country = long_name;
                            } else if (Type.equalsIgnoreCase("postal_code")) {
                                PIN = long_name;
                                SharedPrefs.save(getBaseContext(), SharedPrefs.MY_PINCODE, PIN);
                            }
                        }
                        if (this != null)
                            SharedPrefs.save(getBaseContext(), SharedPrefs.MY_REGION, fullAddres);
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
            super.onPostExecute(s);
            autoCompView.setText(s);
            Log.i("address", "address" + s);
            autoCompView.dismissDropDown();
        }
    }

    public void getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            Address location = address.get(0);
            lat = location.getLatitude();
            lng = location.getLongitude();
            Log.i("lat=" + location.getLatitude(), " long=" + location.getLongitude());
            LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("t1", "lng" + " " + lng);
            SharedPrefs.save(this, SharedPrefs.MY_LAT, lat + "");
            SharedPrefs.save(this, SharedPrefs.MY_LNG, lng + "");
            map.moveCamera(CameraUpdateFactory.newLatLng(l));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                case MY_PERMISSION_FOR_CAMERA: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    } else {

                    }
                }
                case LOCATION_REQUEST:
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        customMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                map = googleMap;

                                //enableMyLocation();

                                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                    return;
                                }
                                map.setMyLocationEnabled(true);

                                Log.i("t1", "broker_map" + map);

                            }
                        });
                        getLocationActivity = new GetCurrentLocation(this, mcallback);

                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    break;
                // other 'case' lines to check for other
                // permissions this app might request
            }


        }catch (Exception e){}


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void onMapDrag(final MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            final long now = SystemClock.uptimeMillis();
            if (  isNetworkAvailable()) {
                if(now - lastTouched > SCROLL_TIME && !(motionEvent.getPointerCount() > 1)) {
                    LatLng currentLocation1; //= new LatLng(location.getLatitude(), location.getLongitude());
                    Log.i( "map", "============ map:" + " " + map );

                    VisibleRegion visibleRegion = map.getProjection()
                            .getVisibleRegion();
                    Point x1 = map.getProjection().toScreenLocation( visibleRegion.farRight );
                    Point y1 = map.getProjection().toScreenLocation( visibleRegion.nearLeft );
                    Point centerPoint = new Point( x1.x / 2, y1.y / 2 );
                    LatLng centerFromPoint = map.getProjection().fromScreenLocation(
                            centerPoint );
                    currentLocation1 = centerFromPoint;
                    lat = currentLocation1.latitude;
                    Log.i( "t1", "lat" + " " + lat );
                    lng = currentLocation1.longitude;
                    SharedPrefs.save( this, SharedPrefs.MY_LAT, lat + "" );
                    SharedPrefs.save( this, SharedPrefs.MY_LNG, lng + "" );
                    General.setSharedPreferences( this, AppConstants.MY_LAT, lat + "" );
                    General.setSharedPreferences( this, AppConstants.MY_LNG, lng + "" );
                    getRegion();
                    new LocationUpdater().execute();
                    getPrice();
                }

            } else {
                General.internetConnectivityMsg(this);
            }


        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            lastTouched = SystemClock.uptimeMillis();
            Log.i("MotionEvent.ACTION_DOWN", "=========================");


        }
    }





    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void getPrice() {

        //getRegion();

        if(General.isNetworkAvailable(this)) {
            General.slowInternet(this);

            User user = new User();

            user.setDeviceId(General.getSharedPreferences(getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI));
            Log.i("PREOK", "getcontext " + General.getSharedPreferences(getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI));
            user.setGcmId(SharedPrefs.getString(this, SharedPrefs.MY_GCM_ID));
            user.setUserRole("client");
            user.setLongitude(SharedPrefs.getString(this, SharedPrefs.MY_LNG));
            user.setProperty_type("home");
            user.setLatitude(SharedPrefs.getString(this, SharedPrefs.MY_LAT));
            Log.i("t1", "My_lng" + "  " + SharedPrefs.getString(this, SharedPrefs.MY_LNG));
            user.setLocality("Andheri west");
            Log.i("t1", "My_lat" + "  " + SharedPrefs.getString(this, SharedPrefs.MY_LAT));
            user.setPlatform("android");
            Log.i("my_locality", SharedPrefs.getString(this, SharedPrefs.MY_LOCALITY));
            user.setPincode("400058");
            if (General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).equals("")) {
                user.setUserId(General.getSharedPreferences(this, AppConstants.TIME_STAMP_IN_MILLI));

            } else {
                user.setUserId(General.getSharedPreferences(this, AppConstants.USER_ID));
            }
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_102).build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

            UserApiService userApiService = restAdapter.create(UserApiService.class);


            userApiService.getPrice(user, new retrofit.Callback<JsonElement>() {

                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                        JSONObject jsonResponse = new JSONObject(strResponse);
                        String errors = jsonResponse.getString("errors");
                        if(errors.equals("8")) {
                       /*     Log.i(TAG, "error code is 2 ");
                            Log.i(TAG, "error code is 1 " + jsonResponse.toString());
                            Log.i(TAG, "error code is " + errors);
                            Log.i(TAG, "error code is 3 ");*/
                            SnackbarManager.show(
                                    Snackbar.with(getBaseContext())
                                            .text("You must update profile to proceed.")
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .color( Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                            Intent openProfileActivity =  new Intent(getBaseContext(), ProfileActivity.class);
                            openProfileActivity.putExtra("msg","compulsary");
                            startActivity(openProfileActivity);
                        }
                        else {
                            JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));
                            // horizontalPicker.stopScrolling();
                            Log.i("TRACE", "Response getprice buildings jsonResponseData" + jsonResponseData);
                            JSONObject price = new JSONObject(jsonResponseData.getString("price"));

                            Log.i("TRACE", "Response getprice buildings pricer ");
                            Log.i("TRACE", "Response getprice buildings price " + price);

                            JSONArray buildings = new JSONArray(jsonResponseData.getString("buildings"));

                            Log.i("TRACE", "Response getprice buildings" + buildings);
                            JSONObject k = new JSONObject(buildings.get(1).toString());
                            Log.i("TRACE", "Response getprice buildings yo" + price.getString("ll_min"));



                            if(!price.getString("ll_min").equalsIgnoreCase("")){
                                if (!price.getString("ll_min").equalsIgnoreCase("0")) {
                                    Log.i("tt", "I am here" + 2);
                                    Log.i("TRACE", "RESPONSEDATAr" + response);
                                    llMin = Integer.parseInt(price.getString("ll_min"));
                                    llMax = Integer.parseInt(price.getString("ll_max"));
                                    Log.i("TRACE", "RESPONSEDATArr" + llMin);
                                    Log.i("TRACE", "RESPONSEDATArr" + llMax);
                                    llMin = 5 * (Math.round(llMin / 5));
                                    llMax = 5 * (Math.round(llMax / 5));
                                    Log.i("TRACE", "RESPONSEDATAr" + llMin);
                                    Log.i("TRACE", "RESPONSEDATAr" + llMax);

                                    orMin = Integer.parseInt(price.getString("or_min"));
                                    orMax = Integer.parseInt(price.getString("or_max"));
                                    Log.i("TRACE", "RESPONSEDATArr" + orMin);
                                    Log.i("TRACE", "RESPONSEDATArr" + orMax);
                                    orMin = 500 * (Math.round(orMin / 500));
                                    orMax = 500 * (Math.round(orMax / 500));
                                    Log.i("TRACE", "RESPONSEDATAr" + orMin);
                                    Log.i("TRACE", "RESPONSEDATAr" + orMax);

                                   /* BroadCastMinMaxValue(llMin, llMax, orMin, orMax);

                                    updateHorizontalPicker();

                                    marquee(500, 100);*/
                                    map.clear();
                                   /* buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                                    recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));

                                    mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
                                    txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));
                                    search_building_icon.setVisibility(View.GONE);
                                    StartOyeButtonAnimation();*/
                                    try {
                                        for (int i = 0; i < 5; i++) {
                                            JSONObject j = new JSONObject(buildings.get(i).toString());
                                            config[i] = j.getString("config");
                                            Log.i("metropolitandraw", "config : " + config[i]);
                                            name [i]= j.getString("name");
                                            Log.i("metropolitandraw", "name : " + name[i]);
                                            rate_growth[i] = j.getString("rate_growth");
                                            Log.i("metropolitandraw", "rate_growth : " + rate_growth[i]);
                                            or_psf[i] = Integer.parseInt(j.getString("or_psf"));
                                            Log.i("metropolitandraw", "or_psf : " + or_psf[i]);
                                            ll_pm[i] = Integer.parseInt(j.getString("ll_pm"));
                                            id[i] = j.getString("id");
                                            Log.i("metropolitandraw", "id : " + ll_pm[i]);
                                            double lat = Double.parseDouble(j.getJSONArray("loc").get(1).toString());
                                            Log.i("metropolitandraw", "lat : " + lat);
                                            double longi = Double.parseDouble(j.getJSONArray("loc").get(0).toString());
                                            Log.i("metropolitandraw", "long  : " + longi);
                                            loc [i]= new LatLng(lat, longi);
                                            Log.i("metropolitandraw", "loc :  " + loc);
//                                            Log.i("TRACE", "RESPONSEDATAr" + mCustomerMarker[i]);
//                                            String customSnippet=rate_growth[i];
//                                            mCustomerMarker[i] = map.addMarker(new MarkerOptions().position(loc[i]).title(name[i]).icon(icon1));
//                                            Log.i("TRACE", "RESPONSEDATAr" + mCustomerMarker[i]);
//                                            flag[i] = false;
//                                            dropPinEffect(mCustomerMarker[i]);
                                            if(status[i]==2 || status[i]==1){
                                                Cancel_timer( i );
                                            }
//l
                                        }
                                        /*OnScreenCo_ordinateFromLatLng();
                                        Displaybuilding();*/
                                        Log.i("metropolitandraw","metropolitandraw11 entry:" );

//                                        metropolitandraw(0);
                                        /*SnackbarManager.show(
                                                Snackbar.with(getBaseContext())
                                                        .text("Displaying 5 buildings out of 12,000.")
                                                        .position(Snackbar.SnackbarPosition.TOP)
                                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));*/
//                                        if(locked==false){

//                                        locked=true;
//                                        }
                                    }catch(Exception e){

                                    }
                                    OnScreenCo_ordinateFromLatLng();
//                                        Displaybuilding();
                                    Log.i("metropolitandraw","metropolitandraw11 entry:" );

                                    map.clear();
                                    metropolitandraw(0);
                                    metropolitandraw(1);
                                    /*showFavourites();
                                    mVisits.setEnabled(true);
                                    txtFilterValue.setEnabled(true);
                                    horizontalPicker.setVisibility(View.VISIBLE);
                                    tv_building.setVisibility(View.VISIBLE);
                                    tvRate.setVisibility(View.VISIBLE);
                                    rupeesymbol.setVisibility(View.VISIBLE);
                                    tvFetchingrates.setVisibility(View.GONE);
                                    missingArea.setVisibility(View.GONE);*/

                                } else {
                                    Log.i("tt", "I am here" + 3);
/*
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
                                    CancelAnimation();*/
                                }
                            } else {

                               /* map.clear();
                                tv_building.setVisibility(View.INVISIBLE);
                                horizontalPicker.setVisibility(View.GONE);
                                tvRate.setVisibility(View.INVISIBLE);
                                rupeesymbol.setVisibility(View.INVISIBLE);
                                tvFetchingrates.setVisibility(View.VISIBLE);
                                tvFetchingrates.setText("Coming Soon...");
                                missingArea.setVisibility(View.VISIBLE);
                                mVisits.setEnabled(false);
                                txtFilterValue.setEnabled(false);
                                CancelAnimation();
                                Log.i("GETPRICE", "Else mode ====== ");*/




                            }

                        } } catch (Exception e) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();

                        Log.i("Price Error", "Caught in exception getprice success" + e.getMessage());
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    General.slowInternetFlag = false;
                    map.clear();
                   /* tv_building.setVisibility(View.INVISIBLE);
                    horizontalPicker.setVisibility(View.GONE);
                    tvRate.setVisibility(View.INVISIBLE);
                    rupeesymbol.setVisibility(View.INVISIBLE);
                    tvFetchingrates.setVisibility(View.VISIBLE);
                    tvFetchingrates.setText("Coming Soon...");
                    missingArea.setVisibility(View.VISIBLE);
                    mVisits.setEnabled(false);
                    txtFilterValue.setEnabled(false);
                    CancelAnimation();*/
                    General.t.interrupt();
                    Log.i("getPrice", "retrofit failure getprice " + error.getMessage());

                }
            });

        }
        else{
            General.internetConnectivityMsg(getBaseContext());

        }
    }




    @Override
    public void onResume() {

        super.onResume();

        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(drawtext1, new IntentFilter(AppConstants.DRAWTEXT));
        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(drawtext2, new IntentFilter(AppConstants.DRAWTEXT1));


        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(drawtext3, new IntentFilter(AppConstants.DRAWTEXT2));

        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(drawtext4, new IntentFilter(AppConstants.DRAWTEXT3));

        LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(drawtext5, new IntentFilter(AppConstants.DRAWTEXT4));


    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(drawtext1);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(drawtext2);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(drawtext3);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(drawtext4);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(drawtext5);
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(oncheckWalkthrough);
        // LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(oncheckbeacon);

    }



    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position, int marker_position) {
        markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(String.valueOf((text))))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        Markertext[marker_position]= map.addMarker(markerOptions);
    }





    public void Displaybuilding() {
        Log.i("countervalue","count : sushil11 ");
        int i=0,count=0;
        if(loc[0]==null)
            loc[0]=new LatLng(19.1095416,72.8413011);
        map.clear();
//       mCustomerMarker[0]= map.addMarker(new MarkerOptions().position(loc[0]).icon(icon1));
        mCustomerMarker[1]= map.addMarker(new MarkerOptions().position(loc[1]).icon(icon1));
        // mCustomerMarker[2]= map.addMarker(new MarkerOptions().position(loc[2]).icon(icon1));
//        mCustomerMarker[3]= map.addMarker(new MarkerOptions().position(loc[3]).icon(icon1));
        mCustomerMarker[4]= map.addMarker(new MarkerOptions().position(loc[4]).icon(icon1));


//        dropPinEffect(mCustomerMarker[0] );
        dropPinEffect(mCustomerMarker[1] );
        /*dropPinEffect(mCustomerMarker[2] );
        dropPinEffect(mCustomerMarker[3] );*/
        dropPinEffect(mCustomerMarker[4] );
        playSound();
//        randomrate1();
        randomrate2();
       /* randomrate3();
        randomrate4();*/
        randomrate5();


    }

    public void randomrate1(){
        Log.i("countervalue","count : sushil12 ");
        gamecount[0]=0;

        textFlag[0]=true;
        game_max[0]=74;
        game_min[0]=50;

        gametimer[0] = new Timer();
        gametimer[0].schedule(new TimerTask() {

            public void run() {
                gamethread[0]=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if(textFlag[0]==true) {
                            gamecount[0] = gamecount[0] +1;
                            Log.i("countervalue","count "+gamecount[0] );
                            Intent intent = new Intent(AppConstants.DRAWTEXT);
                            intent.putExtra("text1", "true");
                            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);

                        }
                            /*else
                                gamethread[0].interrupt();*/
                            /*if(gamecount[0] >=50){

                                mCustomerMarker[0].remove();
                                gamethread[0].interrupt();
                                stopTimer(0);
                                Markertext[0].remove();
                                textFlag[0]=false;
                                gamecount[0] =0;
                            }*/
                        gamethread[0].interrupt();
                    }
                });
                gamethread[0].start();
            }
        }, 1500, 500);
    }

    public void randomrate2(){
        Log.i("countervalue","count : sushil13 ");
        gamecount[1] = 0;
        textFlag[1]=true;
        game_max[1]=67;
        game_min[1]=52;

        gametimer[1] = new Timer();

        gametimer[1].schedule(new TimerTask() {
            @Override
            public void run() {
                gamethread[1]= new Thread(new Runnable() {
                    public void run() {
                        if(textFlag[1]==true) {
                            gamecount[1] = gamecount[1] +1;
                            Log.i("countervalue","count "+gamecount[1] );
                            Intent intent = new Intent(AppConstants.DRAWTEXT1);
                            intent.putExtra("text2", "true1");
                            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);

                        }
//                            else
//                                gamethread[1].interrupt();
                            /*if(gamecount[1] >=50){
                                mCustomerMarker[1].remove();
                                gamethread[1].interrupt();
                                stopTimer(1);
                                Markertext[1].remove();
                                textFlag[1]=false;
                                gamecount[1] = 0;

                            }*/
                        gamethread[1].interrupt();
                    }
                });
                gamethread[1].start();

            }
        }, 1500, 500);

    }
    public void randomrate3(){
        Log.i("countervalue","count : sushil14 ");

        gamecount[2] = 0;
        textFlag[2]=true;
        game_max[2]=84;
        game_min[2]=45;
        gametimer[2] = new Timer();
        gametimer[2].schedule(new TimerTask() {
            @Override
            public void run() {
                gamethread[2]= new Thread(new Runnable() {
                    public void run() {
                        if(textFlag[2]==true) {
                            gamecount[2] = gamecount[2] +1;
                            Log.i("countervalue","count "+gamecount[2] );
                            Intent intent = new Intent(AppConstants.DRAWTEXT2);
                            intent.putExtra("text3", "true2");
                            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);

                        }/*else
                                gamethread[2].interrupt();
*/
                          /*  if(gamecount[2] >=50){
                                mCustomerMarker[2].remove();
                                gamethread[2].interrupt();
                                stopTimer(2);
                                Markertext[2].remove();
                                textFlag[2]=false;
                                gamecount[2] = 0;

                            }*/
                        gamethread[2].interrupt();
                    }
                });
                gamethread[2].start();

            }
        }, 1500, 500);

    }
    public void randomrate4(){
        Log.i("countervalue","count : sushil15 ");
        gamecount[3] = 0;
        textFlag[3]=true;
        game_max[3]=33;
        game_min[3]=15;
        gametimer[3] = new Timer();

        gametimer[3].schedule(new TimerTask() {
            @Override
            public void run() {



                gamethread[3] = new Thread(new Runnable() {
                    public void run() {
                        if(textFlag[3]==true) {
                            gamecount[3] = gamecount[3] +1;
                            Log.i("countervalue","count "+gamecount[3] );
                            Intent intent = new Intent(AppConstants.DRAWTEXT3);
                            intent.putExtra("text4","true3");
                            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);


                        }/*else
                                gamethread[3].interrupt();
                                Log.i(TAG,"thread name is "+gamethread[3]);*/
                               /* if(gamecount[3] >=50){
                                    mCustomerMarker[3].remove();
                                    gamethread[3].interrupt();
                                    stopTimer(3);
                                    Markertext[3].remove();
                                    textFlag[3]=false;
                                    gamecount[3] =0;
                                }*/
                        gamethread[3].interrupt();
                    }

                });
                gamethread[3].start();

            }
        }, 1500, 500);




    }
    public void randomrate5(){
        Log.i("countervalue","count : sushil16 ");
        gamecount[4] = 0;
        textFlag[4]=true;
        game_max[4]=56;
        game_min[4]=30;
        gametimer[4] = new Timer();

        gametimer[4].schedule(new TimerTask() {
            @Override
            public void run() {
                gamethread[4]=   new Thread(new Runnable() {
                    public void run() {

                        if(textFlag[4]==true) {
                            gamecount[4] = gamecount[4] +1;
                            Log.i("countervalue","count "+gamecount[4] );
                            Intent intent = new Intent(AppConstants.DRAWTEXT4);
                            intent.putExtra("text5","true4");
                            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);

                        }
                        gamethread[4].interrupt();
                    }


                });
                gamethread[4].start();


            }
        }, 1500, 500);




    }

    private BroadcastReceiver drawtext1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            try {
                if (intent.getExtras().getString("text1").equalsIgnoreCase("true") && mposition==1) {

                    String rate;
                    rand[0] = randInt(game_min[0], game_max[0],0);
                   /* if(game_min[0]<=game_max[0]){
                        game_min[0]=game_min[0]+1;
                        game_min[0]=game_min[0];
                        rand=game_min[0];

                    }else
                    {
                        game_max[0]=game_max[0]-1;
                        game_max[0]=game_max[0];
                        rand=game_max[0];
                    }*/
                    iconFactory = new IconGenerator(getBaseContext());
                    if (game_min[0]<=game_max[0]) {
                        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                    } else
                        iconFactory.setStyle(IconGenerator.STYLE_RED);
                    rate = String.valueOf(rand[0]);
                    if(Markertext[0]!=null)
                        Markertext[0].remove();

                    addIcon(iconFactory,rate+"k",cent[0],0);
//                    Log.i("countervalue","count : "+ rate);

                    if(game_max[0]<50 && game_min[0]>74)
                    {
                        game_max[0]=74;
                        game_min[0]=50;
                    }
                    if(gamecount[0] >=60 ){

                        if(!flag[0])
                        {
                            stopTimer(0);
                            mCustomerMarker[0].remove();
                            gamethread[0].interrupt();

                            Markertext[0].remove();
                            textFlag[0]=false;
                            Log.i("countervalue","count 11"+gamecount[0] );
                            gamecount[0] =0;
                            Log.i("countervalue","count 12 "+gamecount[0] );
                            mCustomerMarker[1]= map.addMarker(new MarkerOptions().position(loc[1]).icon(icon1));
                            dropPinEffect(mCustomerMarker[1] );
                            playSound();
                            randomrate2();
                            flag[1] = false;
                        }
                        else
                            gamecount[0] =0;
                    }


                }else {
                    stopTimer(0);
//                    getPrice();
                }
            } catch (Exception e) {}
        }

    };


    private BroadcastReceiver drawtext2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            try {
                if (intent.getExtras().getString("text2").equalsIgnoreCase("true1")&& mposition==1) {

                    String rate;
                    rand[1] = randInt(game_min[1], game_max[1],1);
                    iconFactory = new IconGenerator(getBaseContext());
                    if (game_min[1]<=game_max[1]) {
                        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                    } else
                        iconFactory.setStyle(IconGenerator.STYLE_RED);
                    rate = String.valueOf(rand[1]);
                    if(Markertext[1]!=null)
                        Markertext[1].remove();
//                    Log.i("countervalue","count : "+ rate);
                    addIcon(iconFactory,rate+"k",cent[1],1);
                    if(game_max[1]<52 && game_min[1]>67)
                    {
                        game_max[1]=67;
                        game_min[1]=52;
                    }
                    if(gamecount[1] >=50 ){
                        if(!flag[1]){
                            stopTimer(1);
                            mCustomerMarker[1].remove();
                            gamethread[1].interrupt();

                            Markertext[1].remove();
                            textFlag[1]=false;
                            Log.i("countervalue","count 11"+gamecount[1] );
                            gamecount[1] =0;
                            Log.i("countervalue","count 12 "+gamecount[1] );
                            mCustomerMarker[2]= map.addMarker(new MarkerOptions().position(loc[2]).icon(icon1));
                            dropPinEffect(mCustomerMarker[2] );
                            playSound();
                            randomrate3();
                            flag[2] = false;}
                        else
                            gamecount[1] =0;

                    }
                }else {
                    stopTimer(1);
//                    getPrice();
                }

            } catch (Exception e) {}
        }

    };
    private BroadcastReceiver drawtext3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            try {
                if (intent.getExtras().getString("text3").equalsIgnoreCase("true2") && mposition==1) {

                    String rate;

                    rand[2] = randInt(game_min[2], game_max[2],2);
                    iconFactory = new IconGenerator(getBaseContext());
                    if (game_min[2]<=game_max[2]) {
                        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                    } else
                        iconFactory.setStyle(IconGenerator.STYLE_RED);
                    rate = String.valueOf(rand[2]);
                    if(Markertext[2]!=null)
                        Markertext[2].remove();
//                    Log.i("countervalue","count : "+ rate);
                    addIcon(iconFactory,rate+"k",cent[2],2);
                    if(game_max[2]<45 && game_min[2]>84)
                    {
                        game_max[2]=84;
                        game_min[2]=45;
                    }
                    if(gamecount[2] >=60 ){
                        if(!flag[2]){
                            stopTimer(2);
                            mCustomerMarker[2].remove();
                            gamethread[2].interrupt();

                            Markertext[2].remove();
                            textFlag[2]=false;
                            Log.i("countervalue","count 11"+gamecount[2] );
                            gamecount[2] =0;
                            Log.i("countervalue","count 12 "+gamecount[2] );
                            mCustomerMarker[3]= map.addMarker(new MarkerOptions().position(loc[3]).icon(icon1));
                            dropPinEffect(mCustomerMarker[3] );
                            playSound();
                            randomrate4();
                            flag[3] = false;
                        }else
                            gamecount[2] =0;

                    }

                }else {
                    stopTimer(2);
//                    getPrice();
                }

            } catch (Exception e) {}
        }

    };
    private BroadcastReceiver drawtext4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            try {
                if (intent.getExtras().getString("text4").equalsIgnoreCase("true3") && mposition==1) {

                    String rate;

                    rand[3] = randInt(game_min[3], game_max[3],3);
                    iconFactory = new IconGenerator(getBaseContext());
                    if (game_min[3]<=game_max[3]) {
                        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                    } else
                        iconFactory.setStyle(IconGenerator.STYLE_RED);
                    rate = String.valueOf(rand[3]);
                    if(Markertext[3]!=null)
                        Markertext[3].remove();
//                    Log.i("countervalue","count : "+ rate);
                    addIcon(iconFactory,rate+"k",cent[3],3);
                    if((game_max[3]<15 && game_min[3]>33))
                    {
                        game_max[3]=33;
                        game_min[3]=15;
                    }

                    if(gamecount[3] >=50 ){
                        if(!flag[3]) {
                            stopTimer(3);
                            mCustomerMarker[3].remove();
                            gamethread[3].interrupt();

                            Markertext[3].remove();
                            textFlag[3] = false;
                            Log.i("countervalue", "count 11" + gamecount[3]);
                            gamecount[3] = 0;
                            Log.i("countervalue", "count 12 " + gamecount[3]);
                            mCustomerMarker[4] = map.addMarker(new MarkerOptions().position(loc[4]).icon(icon1));
                            dropPinEffect(mCustomerMarker[4]);
                            playSound();
                            randomrate5();
                            flag[4] = false;
                        }else
                            gamecount[3] = 0;

                    }
                }else {
                    stopTimer(3);
//                    getPrice();
                }
            } catch (Exception e) {}
        }

    };
    private BroadcastReceiver drawtext5 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            try {
                if (intent.getExtras().getString("text5").equalsIgnoreCase("true4") && mposition==1) {


                    String rate;
                    rand[4] = randInt(game_min[4], game_max[4],4);
                    iconFactory = new IconGenerator(getBaseContext());
                    if (game_min[4]<=game_max[4]) {
                        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                    } else
                        iconFactory.setStyle(IconGenerator.STYLE_RED);
                    rate = String.valueOf(rand[4]);
                    if(Markertext[4]!=null)
                        Markertext[4].remove();
//                    Log.i("countervalue","count : "+ rate);
                    addIcon(iconFactory,rate+"k",cent[4],4);
                    if((game_max[4]<30 && game_min[4]>56))
                    {
                        game_max[4]=56;
                        game_min[4]=30;
                    }

                    if(gamecount[4] >=60 ){
                        if(!flag[4]) {
                            stopTimer(4);
                            mCustomerMarker[4].remove();
                            gamethread[4].interrupt();

                            Markertext[4].remove();
                            textFlag[4] = false;
                            Log.i("countervalue", "count 11" + gamecount[4]);
                            gamecount[4] = 0;
                            Log.i("countervalue", "count 12 " + gamecount[4]);
                            mCustomerMarker[0] = map.addMarker(new MarkerOptions().position(loc[0]).icon(icon1));
                            dropPinEffect(mCustomerMarker[0]);
                            playSound();
                            randomrate1();
                            flag[0] = false;
                        }else
                            gamecount[4] = 0;

//                                    gamethread[4].interrupt();
                    }
                }else{
                    stopTimer(4);
//                    getPrice();
                }
            } catch (Exception e) {}
        }

    };



    private void dropPinEffect(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    handler.postDelayed(this, 15);
                } else {
//                    Log.i(TAG,"building drop ");


                }
            }
        });
    }



    public  void playSound() {

        MediaPlayer mp = MediaPlayer.create(getBaseContext(),R.raw.coin1);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
            }

        });
        mp.start();

    }


    private void stopTimer(int index){
        if(gametimer[index] != null){
            gametimer[index].cancel();
            gametimer[index].purge();
            timer1 = null;
        }
    }

    public  int randInt(int min, int max,int position) {
        /*Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 10) + min;
        return randomNum;*/
        if(min<=max){
            min=min+1;
            game_min[position]=min;
            return min;


        }else
        {
            max=max-1;
            game_max[position]=max;
            return max;
        }
    }
    public void OnScreenCo_ordinateFromLatLng(){
        Point p;
        for(int i=0;i<5;i++) {
//            mCustomerMarker[i].getPosition();
//            LatLng cent = mCustomerMarker[i].getPosition();
            LatLng cent =loc[i];
            buildingPosition[i]=map.getProjection().toScreenLocation(cent);
            Log.i("OnScreen","OnScreenCo_ordinateFromLatLng  : "+buildingPosition[i]);
        }
        for(int i=0;i<5;i++) {

            p= new Point(buildingPosition[i].x,buildingPosition[i].y-55);
            cent[i]= map.getProjection().fromScreenLocation(p);
            Log.i("OnScreen","OnScreenCo_ordinateFromLatLng  : "+buildingPosition[i]);
        }
    }



    public void UpdateBalanceadd(int value){
        Log.i("balance","balance amount add:"  +value);
        int bal=Integer.parseInt(balance1.getText().toString());
        bal=bal+value;
        balance1.setText(String.valueOf(bal));
    }
    public void UpdateBalancesub(int value){
        Log.i("balance","balance amount sub:"  +value);
        int bal=Integer.parseInt(balance1.getText().toString());
        bal=bal-value;
        balance1.setText(String.valueOf(bal));
    }





    public int randposition(int min,int max){


        if(min<max){
            min=min+1;
            return min;
        }else
        {
            max=max-1;
            return max;
        }
        /*Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 3) + min;
        return randomNum;*/
    }

    private void metropolitandraw(int i){
//        map.clear();
        game_min[i]=(or_psf[i]/1000)-10;
        game_max[i]=(or_psf[i]/1000)+10;
        Log.i("metropolitandraw","metropolitandraw11:" +i );
        iconFactory = new IconGenerator(this);
        mCustomerMarker[i]= map.addMarker(new MarkerOptions().position(loc[i]).icon(icon1));
        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
        addIcon(iconFactory,game_min[i]+"k",cent[i],i);
        dropPinEffect(mCustomerMarker[i]);
        dropPinEffect(Markertext[i]);
//        flag[i]=true;
        status[i]=1;
        PropertyRateChange(i);
        playSound();
        /*if(i==0) {
            map.clear();
        }*/
       // metropolitandraw(1);
//        checkPlace( i );

        /*Thread t=new Thread(runnable);
        t.start();*/

    }



    Runnable runnable=new Runnable() {



        @Override
        public void run() {
            long future= System.currentTimeMillis()+10000;

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

            }*/ Log.i("thread1","handler========1    ");

            h.sendEmptyMessageDelayed(0,10000);
        }
    };

    Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            Log.i("thread1","handler========    ");
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
//            Log.i("starting timer2", " " + timer);
            timer1[i].schedule(new TimerTask() {
                @Override
                public void run() {
                    if (this != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String rate;
                                rand[i] = randInt(game_min[i], game_max[i], i);
                                iconFactory = new IconGenerator(getBaseContext());
                                if (Markertext[i] != null)
                                    Markertext[i].remove();
                                if (game_min[i] <= game_max[i]) {
                                    iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                                } else
                                    iconFactory.setStyle(IconGenerator.STYLE_RED);
                                rate = String.valueOf(rand[i]);

                                Log.i("countervalue11", "count new : " + rate+" "+((or_psf[i]/1000)-10)+" "+((or_psf[i]/1000)+10)+" "+game_min[i]+" "+game_max[i]);
                                addIcon(iconFactory, rate + "k", cent[i], i);
//                                c=1300;
                                if(game_max[i]<((or_psf[i]/1000)-10) && game_min[i]>=((or_psf[i]/1000)+10))
                                {
                                    game_max[i]=(or_psf[i]/1000)+10;
                                    game_min[i]=(or_psf[i]/1000)-10;
                                }
                            }
                        });
                    }
                }
            }, 1000, 200);

        }


        /*try {

            new CountDownTimer(9000, c) {

                public void onTick(long millisUntilFinished) {
                    String rate;
                    rand[i] = randInt(game_min[i], game_max[i], i);
                    iconFactory = new IconGenerator(getBaseContext());
                    if (Markertext[i] != null)
                        Markertext[i].remove();
                    if (game_min[i] <= game_max[i]) {
                        iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                    } else
                        iconFactory.setStyle(IconGenerator.STYLE_RED);
                    rate = String.valueOf(rand[i]);

                    Log.i("countervalue1", "count new : " + rate);
                    addIcon(iconFactory, rate + "k", cent[i], i);
                    c=1300;
                    if((game_max[i]<(or_psf[i]/1000)-10 && game_min[i]>(or_psf[i]/1000)+10))
                    {
                        game_max[i]=(or_psf[i]/1000)-10;
                        game_min[i]=(or_psf[i]/1000)+10;
                    }

                }

                public void onFinish() {



                }
            }.start();


        }catch (Exception e){}*/



    }



    private  void Cancel_timer(int i) {
        try {
            if (timer1[i] != null) {
                timer1[i].cancel();
                timer1[i] = null;
            }
        }catch(Exception e){}
    }



/*
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


  public int RandomBuildingDisplay(int i){
      int j;
      if (i < 4) {
          j = i + 1;
          return  j;
      }
      else {
          j = 0;
          return  j;

      }
  }

public void checkPlace(int j){
    if(status[j]==0){
        metropolitandraw( j );
//                                                                 flag[j]=true;

    }else{
        j= RandomBuildingDisplay(j);
        checkPlace(j);
//                                                                 flag[j]=true;
    }
}

}


















