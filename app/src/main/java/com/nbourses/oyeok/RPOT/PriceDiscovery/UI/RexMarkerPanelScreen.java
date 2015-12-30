package com.nbourses.oyeok.RPOT.PriceDiscovery.UI;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class RexMarkerPanelScreen extends Fragment implements CustomPhasedListener, AdapterView.OnItemClickListener {

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

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.inputSearch);
        autoCompView.setAdapter(new AutoCompletePlaces.GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item1));
        autoCompView.setOnItemClickListener(this);


        mDrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).changeFragment(new Drooms_Client_new(), null);
            }
        });
        mVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("BrokerType", brokerType);
                ((MainActivity)getActivity()).changeFragment(new OyeIntentSpecs(),args);
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
                LatLng latlng = map.getCameraPosition().target;
                lat = latlng.latitude;
                lng  = latlng.longitude;
                SharedPrefs.save(getContext(),SharedPrefs.MY_LAT,lat+"");
                SharedPrefs.save(getContext(), SharedPrefs.MY_LNG, lng + "");
                getRegion();
            }
        });


        new GetCurrentLocation(getActivity(), new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    SharedPrefs.save(getContext(),SharedPrefs.MY_LAT,lat+"");
                    SharedPrefs.save(getContext(),SharedPrefs.MY_LNG,lng+"");
                    getRegion();
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

        Toast.makeText(getActivity(), "Selected position:" + position, Toast.LENGTH_LONG).show();
        if(count!=2){
            if(position==0)
                 brokerType="rent";
            else if(position==1)
                brokerType="sale";
        }
        else{

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
    //alternative for geocoder
    public static JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            address = address.replaceAll(" ","%20");

            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }
    private static List<Address> getAddrByWeb(JSONObject jsonObject){
        List<Address> res = new ArrayList<Address>();
        try
        {
            JSONArray array = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < array.length(); i++)
            {
                Double lon = new Double(0);
                Double lat = new Double(0);
                String name = "";
                try
                {
                    lon = array.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                    lat = array.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    name = array.getJSONObject(i).getString("formatted_address");
                    Address addr = new Address(Locale.getDefault());
                    addr.setLatitude(lat);
                    addr.setLongitude(lon);
                    addr.setAddressLine(0, name != null ? name : "");
                    res.add(addr);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();

                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return res;
    }


}
