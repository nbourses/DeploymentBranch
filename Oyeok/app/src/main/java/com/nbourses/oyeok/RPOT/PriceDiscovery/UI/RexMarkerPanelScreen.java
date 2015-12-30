package com.nbourses.oyeok.RPOT.PriceDiscovery.UI;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.GetPrice;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.GetPrice;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI.Drooms_Client_new;
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
import org.apache.http.client.ClientProtocolException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class RexMarkerPanelScreen extends Fragment implements CustomPhasedListener, AdapterView.OnItemClickListener, GoogleMap.OnCameraChangeListener {

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

    private ImageView mMarkerpanelminus;
    private ImageView mMarkerpanelplus;
    private DiscreteSeekBar mMarkerpriceslider;
    private RelativeLayout mMarkerminmax;
    private GoogleMap map;
    private LinearLayout ll_marker;
    private TextView maxPrice;
    private TextView minPrice;
    private GeoFence geoFence;
    private int permissionCheckForCamera;
    private final int MY_PERMISSION_FOR_CAMERA=11;
    private CustomPhasedSeekBar mPhasedSeekBar;
    String brokerType;
    private Geocoder geocoder;
    String pincode, region, fullAddress;
    Double lat, lng;
    View rootView;
    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "", fullAddres = "";
    AutoCompleteTextView autoCompView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rex_fragment_home, container, false);
        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        mDrooms = (TextView) rootView.findViewById(R.id.linearlayout_drooms);
       mVisits = (TextView) rootView.findViewById(R.id.newVisits);
        mQrCode = (ImageView) rootView.findViewById(R.id.qrCode);
        mMarkerPanel = (LinearLayout) rootView.findViewById(R.id.ll_marker);
        mMarkerpanelminus = (ImageView) rootView.findViewById(R.id.markersliderminus);
        mMarkerpanelplus = (ImageView) rootView.findViewById(R.id.markerpanelplus);
        mMarkerpriceslider = (DiscreteSeekBar) rootView.findViewById(R.id.price_seekbar);
       mMarkerminmax = (RelativeLayout) rootView.findViewById(R.id.markerpanelminmax);
        //ll_marker = (LinearLayout) rootView.findViewById(R.id.ll_marker);
        maxPrice = (TextView) rootView.findViewById(R.id.tv_max);
        minPrice = (TextView) rootView.findViewById(R.id.tv_min);
         permissionCheckForCamera = ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.CAMERA);
        dbHelper=new DBHelper(getContext());


        mPhasedSeekBar = (CustomPhasedSeekBar) rootView.findViewById(R.id.phasedSeekBar);
        if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{"Rental", "Sale"}));
        else
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.real_estate_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Audit", "Auction"}));
        mPhasedSeekBar.setListener(this);

        autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.inputSearch);
        autoCompView.setAdapter(new AutoCompletePlaces.GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item1));
        autoCompView.setOnItemClickListener(this);
        autoCompView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompView.showDropDown();
            }
        });


        mDrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).changeFragment(new Drooms_Client_new());
            }
        });
        mVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("BrokerType", brokerType);
                ((MainActivity)getActivity()).changeFragment(new OyeIntentSpecs());
                //OyeIntentSpecs oye = new OyeIntentSpecs();

                ((MainActivity)getActivity()).changeFragment(new OyeIntentSpecs());
            }
        });


        mQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator.forSupportFragment(RexMarkerPanelScreen.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(CaptureActivityAnyOrientation.class).setOrientationLocked(false).initiateScan();
            }
        });

        mMarkerPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMarkerminmax.setVisibility(View.GONE);
                mMarkerpanelminus.setVisibility(View.VISIBLE);
                mMarkerpanelplus.setVisibility(View.VISIBLE);
                mMarkerpriceslider.setVisibility(View.VISIBLE);

            }
        });

        mMarkerpanelminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m = mMarkerpriceslider.getLeft();
                mMarkerpriceslider.setLeft(m - 1);

            }
        });
        mMarkerpanelplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m = mMarkerpriceslider.getRight();
                mMarkerpriceslider.setRight(m + 1);
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
                    mMarkerpanelminus.setVisibility(View.GONE);
                    mMarkerpanelplus.setVisibility(View.GONE);
                    mMarkerpriceslider.setVisibility(View.GONE);
                    mMarkerminmax.setVisibility(View.GONE);
                    mMarkerPanel.setVisibility(View.INVISIBLE);


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    mMarkerpanelminus.setVisibility(View.GONE);
                    mMarkerpanelplus.setVisibility(View.GONE);
                    mMarkerpriceslider.setVisibility(View.GONE);
                    mMarkerPanel.setVisibility(View.VISIBLE);
                    mMarkerminmax.setVisibility(View.VISIBLE);
                    getPrice();
                }
                //getRegion();
            }
        });


        new GetCurrentLocation(getActivity(), new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(16));

                    //make retrofit call to get Min Max price
                    getPrice();
                }
            }
        });


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


        mMarkerpriceslider.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

                 }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                //Toast.makeText(getActivity(),"test",Toast.LENGTH_LONG).show();
                map.animateCamera(CameraUpdateFactory.zoomTo(12));
            }
        });



        return rootView;
    }

    public void setPhasedSeekBar(){
        //View rootView = inflater.inflate(R.layout.rex_fragment_home, container, false);
        mPhasedSeekBar = (CustomPhasedSeekBar) rootView.findViewById(R.id.phasedSeekBar);
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
        String API = "http://52.25.136.179:9000";
        User user = new User();
        user.setDeviceId("Hardware");
        user.setGcmId("gliui");
        user.setUserRole("client");
        user.setLongitude(19.2);
        user.setLatitude(72.45);
        user.setLocality("andheri west");

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

        }

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        SharedPrefs.save(getContext(),SharedPrefs.MY_REGION, fullAddress);
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

    @Override
    public void onPositionSelected(int position) {
        Toast.makeText(getActivity(), "Selected position:" + position, Toast.LENGTH_LONG).show();
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
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean setCameraListener(){
        map.setOnCameraChangeListener(this);
        return true;
    }
}
