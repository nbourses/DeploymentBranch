package com.nbourses.oyeok.RPOT.PriceDiscovery.UI;



import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI.Droom_chats_list;
import com.nbourses.oyeok.RPOT.OyeOkBroker.OyeIntentSpecs;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.AutoCompletePlaces;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GeoFence;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.MapWrapperLayout;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.QrCode.CaptureActivityAnyOrientation;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
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

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

//import com.nbourses.oyeok.R;

//import com.nbourses.oyeok.R;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;*/


public class RexMarkerPanelScreen extends Fragment implements CustomPhasedListener, AdapterView.OnItemClickListener, GoogleMap.OnCameraChangeListener, ChatList {

    private final String TAG = RexMarkerPanelScreen.class.getSimpleName();
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] CAMERA_PERMS = {
            Manifest.permission.CAMERA
    };
    private static final String[] CONTACTS_PERMS = {
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST = 133;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;

    DBHelper dbHelper;
    private TextView mDrooms;
    private TextView mVisits;
    private ImageView mQrCode;
    private LinearLayout mMarkerPanel;

    private RelativeLayout mMarkerminmax;
    private GoogleMap map;
    private LinearLayout ll_marker;
    private TextView maxPrice;
    private TextView minPrice;
    private GeoFence geoFence;
    private int permissionCheckForCamera,permissionCheckForLocation;
    private final int MY_PERMISSION_FOR_CAMERA=11;
    private CustomPhasedSeekBar mPhasedSeekBar;
    String brokerType;
    private Geocoder geocoder;
    private GetCurrentLocation.CurrentLocationCallback mcallback;
    private FrameLayout ll_map;
    String pincode, region, fullAddress;
    Double lat, lng;
    MainActivity mainActivity;
    DroomChatFirebase droomChatFirebase;

    private GetCurrentLocation getLocationActivity;
    //View rootView;
    HashMap<String, HashMap<String, String>> chatListData;

    View rootView;
    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "", fullAddres = "";
    AutoCompleteTextView autoCompView;
    private RelativeLayout errorView;
    private TextView errorText;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View  rootView = inflater.inflate(R.layout.rex_fragment_home, container, false);
        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        droomChatFirebase=new DroomChatFirebase(DatabaseConstants.firebaseUrl);
        mDrooms = (TextView) rootView.findViewById(R.id.linearlayout_drooms);
       mVisits = (TextView) rootView.findViewById(R.id.newVisits);
        mQrCode = (ImageView) rootView.findViewById(R.id.qrCode);
        mMarkerPanel = (LinearLayout) rootView.findViewById(R.id.ll_marker);

       mMarkerminmax = (RelativeLayout) rootView.findViewById(R.id.markerpanelminmax);
        //ll_marker = (LinearLayout) rootView.findViewById(R.id.ll_marker);
        maxPrice = (TextView) rootView.findViewById(R.id.tv_max);
        minPrice = (TextView) rootView.findViewById(R.id.tv_min);
         permissionCheckForCamera = ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.CAMERA);
        mainActivity=(MainActivity)getActivity();
        dbHelper=new DBHelper(getContext());
        ll_map = (FrameLayout) rootView.findViewById(R.id.ll_map);
        permissionCheckForLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        errorView = (RelativeLayout) rootView.findViewById(R.id.alertLayout);
        errorText = (TextView) rootView.findViewById(R.id.errorText);
        onPositionSelected(0,2);





        mPhasedSeekBar = (CustomPhasedSeekBar) rootView.findViewById(R.id.phasedSeekBar);
        if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{"Rental", "Sale"}));
        else
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.real_estate_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Audit", "Auction"}));
        mPhasedSeekBar.setListener(this);

        autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.inputSearch);
        autoCompView.setAdapter(new AutoCompletePlaces.GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item1));		        autoCompView.setAdapter(new AutoCompletePlaces.GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item1));
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


        mDrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putSerializable("HashMap",chatListData);
                Fragment f=new Droom_chats_list();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                f.setArguments(b);
                fragmentTransaction.replace(R.id.container_body, f);
                fragmentTransaction.commitAllowingStateLoss();

                Log.i("Change Fragment", f.toString());
                // set the toolbar title

               // ((MainActivity)getActivity()).changeFragment(new Drooms_Client_new(), null);
               // ((MainActivity)getActivity()).changeFragment(new Drooms_Client_new(),null);
            }
        });
        mVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("BrokerType", brokerType);
                args.putString("Address",SharedPrefs.getString(getActivity(),SharedPrefs.MY_REGION));
                ((MainActivity)getActivity()).changeFragment(new OyeIntentSpecs(), args,"");
                //OyeIntentSpecs oye = new OyeIntentSpecs();

                //oye.setArguments(args);
                //getFragmentManager().beginTransaction().add(R.id., oye).commit();
            }
        });


        mQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CAMERA)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},MY_PERMISSION_FOR_CAMERA);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }






                /*IntentIntegrator.forSupportFragment(RexMarkerPanelScreen.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(CaptureActivityAnyOrientation.class).setOrientationLocked(false).initiateScan();*/
            }
        });



        mMarkerPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMarkerminmax.setVisibility(View.GONE);


            }
        });




        CustomMapFragment customMapFragment = ((CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        geoFence = new GeoFence();
        customMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMyLocationEnabled(true);
                setCameraListener();
                //plotMyNeighboursHail.markerpos(my_user_id, pointer_lng, pointer_lat, which_type, my_role, map);
                //selectedLocation = map.getCameraPosition().target;
                geoFence.drawPloygon(map);

            }
        });


        customMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
            @Override
            public void onDrag(MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    mMarkerminmax.setVisibility(View.GONE);
                    mMarkerPanel.setVisibility(View.INVISIBLE);


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    mMarkerPanel.setVisibility(View.VISIBLE);
                    mMarkerminmax.setVisibility(View.VISIBLE);
                    getPrice();
                    /*
                    LatLng latlng = map.getCameraPosition().target;
                    lat = latlng.latitude;
                    lng = latlng.longitude;
                    if (isNetworkAvailable()) {
                        try {
                            getRegion();
                        } catch (Exception e) {
                            Log.i("Exception", "caught in get region");
                        }
                    }*/
                }

                SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");


            }
        });

        mcallback           =  new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    SharedPrefs.save(getActivity(),SharedPrefs.MY_LAT,lat+"");
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                    if(isNetworkAvailable()) {
                        try {
                            //getRegion();
                        } catch (Exception e) {
                            Log.i("Exception", "caught in get region");
                        }
                    }

                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(16));

                    //make retrofit call to get Min Max price
                    if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null") && isNetworkAvailable()) {
                        try {
                            getPrice();
                        } catch (Exception e) {

                        }
                    }
                }
            }
        };

        getLocationActivity = new GetCurrentLocation(getActivity(),mcallback);
//        new GetCurrentLocation(getActivity(), new GetCurrentLocation.CurrentLocationCallback() {
//            @Override
//            public void onComplete(Location location) {
//                if (location != null) {
//                    lat = location.getLatitude();
//                    lng = location.getLongitude();
//                    SharedPrefs.save(getActivity(),SharedPrefs.MY_LAT,lat+"");
//                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
//                    if(isNetworkAvailable()) {
//                        try {
//                            getRegion();
//                        } catch (Exception e) {
//                            Log.i("Exception", "caught in get region");
//                        }
//                    }
//
//                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
//                    map.animateCamera(CameraUpdateFactory.zoomTo(16));
//
//                    //make retrofit call to get Min Max price
//                    if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null") && isNetworkAvailable()) {
//                        try {
//                            getPrice();
//                        } catch (Exception e) {
//
//                        }
//                    }
//                }
//            }
//        });


        ((MainActivity) getActivity()).bringResideMenu();


//        PhasedSeekBar psbHorizontal = (PhasedSeekBar) rootView.findViewById(R.id.psb_hor);
//        final Resources resources = getResources();
//        psbHorizontal.setAdapter(new SimplePhasedAdapter(resources, new int[]{
//                R.drawable.btn_square_selector,
//                R.drawable.btn_triangle_selector,
//                R.drawable.btn_xis_selector,
//                R.drawable.btn_square_selector,
//                R.drawable.btn_triangle_selector,
//                R.drawable.btn_xis_selector
//
//        }
//        ));
        // Inflate the layout for this fragment


        if(!dbHelper.getValue(DatabaseConstants.userId).equalsIgnoreCase("null"))
            droomChatFirebase.getDroomList(dbHelper.getValue(DatabaseConstants.userId), getActivity());

        return rootView;
    }
    @Override
    public void onResume() {
        final LocationManager manager = (LocationManager)getActivity(). getSystemService( Context.LOCATION_SERVICE );



    
    


        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        super.onResume();
    }
    @Override
    public void onDetach() {
        super.onDetach();

        getLocationActivity.setCallback(null);
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        mHandler = new Handler();
        mHandler.postDelayed(mStatusChecker1,5000);

        //getLocationActivity.setCallback(null);
    }


    public void setPhasedSeekBar(){
        //View rootView = inflater.inflate(R.layout.rex_fragment_home, container, false);
        //mPhasedSeekBar = (CustomPhasedSeekBar) rootView.findViewById(R.id.phasedSeekBar);
        if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{"Rental", "Sale"}));
        else
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.real_estate_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Audit", "Auction"}));
        mPhasedSeekBar.setListener(this);
    }

    private void displayToast(String toast) {
        if (getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    private Handler mHandler;
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            //fillHourGlasses(0, intervalCount * mInterval / 1000);

            hideMap(0);
            errorView.setVisibility(View.GONE);


        }
    };

    Runnable mStatusChecker1 = new Runnable() {
        @Override
        public void run() {
            //fillHourGlasses(0, intervalCount * mInterval / 1000);

            showInfoMessage("Sample information test");


        }
    };

    public void showInfoMessage(String message)
    {
        errorView.setVisibility(View.VISIBLE);
        errorText.setText(message);
        hideMap(1);
        mHandler.postDelayed(mStatusChecker, 5000);

    }

    private void hideMap(int i) {

        Animation m = null;

        if(isAdded())

            //Load animation
            if(i==0) {
                m = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_down);


                //SharedPrefs.getString(getActivity(),SharedPrefs.MY_LOCALITY)+","+SharedPrefs.getString(getActivity(),SharedPrefs.MY_CITY)
            }else {

                m = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_up);
            }

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

    /*private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/

    public void getPrice() {

        //getRegion();
        String API = DatabaseConstants.serverUrl;
        User user = new User();
        user.setDeviceId("Hardware");
        user.setPushToken(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        user.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        user.setUserRole("client");
        user.setLongitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
        user.setLatitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
        user.setLocality(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
        user.setPlatform("android");
        Log.i("my_locality",SharedPrefs.getString(getActivity(),SharedPrefs.MY_LOCALITY));
        user.setPincode("400058");
		
        /*user.setDeviceId(dbHelper.getValue("deviceId"));
        user.setGcmId(dbHelper.getValue("dcmId"));
        user.setUserRole(dbHelper.getValue("userRole"));
        user.setLongitude(Double.parseDouble(dbHelper.getValue("currentLng")));
        user.setLatitude(Double.parseDouble(dbHelper.getValue("currentLat")));
        user.setLocality(region);
		user.setPincode(pincode);*/

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        UserApiService user1 = restAdapter.create(UserApiService.class);
        user1.getPrice(user, new Callback<GetPrice>() {
            @Override
            public void success(GetPrice getPrice, Response response) {
                //Toast.makeText(getContext(), "get price success", Toast.LENGTH_LONG).show();
                Log.i("getPrice", "success");
                minPrice.setText(getPrice.responseData.getOr_max());
                maxPrice.setText(getPrice.responseData.getOr_min());

            }

            @Override
            public void failure(RetrofitError error) {

                Log.i("getPrice", "error: " + error.getMessage());
            }
        });
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
                    IntentIntegrator.forSupportFragment(RexMarkerPanelScreen.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(CaptureActivityAnyOrientation.class).setOrientationLocked(false).initiateScan();

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
                    getLocationActivity = new GetCurrentLocation(getActivity(),mcallback);

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
                        map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                        map.animateCamera(CameraUpdateFactory.zoomTo(16));

                    }
                }
            });


        } else {
            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);




            Toast.makeText(getContext(), "Offline Mode", Toast.LENGTH_LONG);
        }

    }

    @Override
    public void onPositionSelected(int position, int count) {

        //Toast.makeText(getActivity(), "Selected position:" + position, Toast.LENGTH_LONG).show();
        if(count==2){
            if(position==0) {

                brokerType = "rent";
                dbHelper.save(DatabaseConstants.brokerType, "LL");
                dbHelper.save("brokerType","On Rent");
            }
            else if(position==1) {

                brokerType = "sale";
                dbHelper.save(DatabaseConstants.brokerType, "OR");
                dbHelper.save("brokerType","For Sale");
            }
        }
        else{

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ll_map.setAlpha(1f);
        //Log.i("pratik","location");

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
        SharedPrefs.save(getActivity(),SharedPrefs.MY_REGION, fullAddress);
        Log.v(TAG, fullAddress);
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
                    Log.v(TAG, "from async task : address is" + fullAddress);
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

                        SharedPrefs.save(getActivity(),SharedPrefs.MY_REGION,fullAddress);
                        Log.v(TAG,"from asynctask "+fullAddress);
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
            /*p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
                    (int) (location.getLongitude() * 1E6));*/
            LatLng l=new LatLng(location.getLatitude(),location.getLongitude());
            //map.addMarker(new MarkerOptions().position(l));
            map.moveCamera(CameraUpdateFactory.newLatLng(l));
            map.animateCamera(CameraUpdateFactory.zoomTo(16));
            //return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
