package com.nbourses.oyeok.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.AutoCompletePlaces;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.MapWrapperLayout;

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

import butterknife.Bind;

//import com.google.android.gms.identity.intents.Address;

/**
 * Created by sushil on 25/05/16.
 */
public class BrokerMap extends DashboardClientFragment {
    @Bind(R.id.iv_markerpin)
    ImageView iv_markerpin;
    private GetCurrentLocation getLocationActivity;
    GoogleMap gmap;
    CustomMapFragment customMapFragment;
    AutoCompleteTextView autoCompView;
    private BitmapDescriptor icon1;
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
    // id_markerpin;

    //TextView tv_change_region;
    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "", fullAddres = "", locality = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.broker_map, container, false);
        //  final View rootView1 = inflater.inflate(R.layout.toolbar, container, false);
        // ButterKnife.bind(this, rootView);
        //  ButterKnife.bind(this, rootView1);
        //tv_change_region=(TextView) rootView1.findViewById(R.id.tv_change_region);
        // icon1 = BitmapDescriptorFactory.fromResource(R.drawable.iv_markerpin);
        iv_markerpin = (ImageView) rootView.findViewById(R.id.iv_markerpin);

//        tv_change_region = (TextView) rootView1.findViewById(R.id.tv_change_region);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                iv_markerpin.getViewTreeObserver().removeGlobalOnLayoutListener(this);


                int[] locations = new int[2];
                iv_markerpin.getLocationOnScreen(locations);
                x = locations[0] + 35;
                y = locations[1] - 64;
//                x = left - (right - left) / 2;
//                y = bottom;
                bottom = rootView.getBottom();
                top = rootView.getTop();
                left = rootView.getLeft();
                right = rootView.getRight();
                width = rootView.getMeasuredWidth();
                height = rootView.getMeasuredHeight();
                Log.i("t1", "Bottom" + rootView.getBottom() + "top" + top + "left" + left + "right" + right);
                Log.i("t1", "width" + width + "height " + height);
                point = new Point(x, y);

                Log.i("t1", "co-ordinate" + x + " " + y);
            }
        });


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
    //           new LocationUpdater().execute();


            }
        });
        if (customMapFragment == null) {
            customMapFragment = ((CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.g_map));
            customMapFragment.getMap();
        }

        customMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;
//               double lat = 19.1269299;
//              double  lng = 72.8376545999999;

//                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//
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
                gmap.setMyLocationEnabled(true);
//
//

//
////                    enableMyLocation();
//               // Marker m = gmap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("").icon(icon1));
//
//                SharedPrefs.save(getContext(), SharedPrefs.MY_LAT, lat + "");
//                    SharedPrefs.save(getContext(), SharedPrefs.MY_LNG, lng + "");
//
//                new LocationUpdater().execute();
//
//
//
//                    LatLng center = new LatLng(lat, lng);
//
//                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 12));

                getLocationActivity = new GetCurrentLocation(getActivity(), mcallback);
            }
        });




        /*try {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(getContext());
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                    if (key.equals(SharedPrefs.MY_REGION)) {
                        Log.i("loc","OnSharedPreferenceChangeListener 1");
                        Log.i("loc","OnSharedPreferenceChangeListener 1 rent "+SharedPrefs.getString(getContext(), SharedPrefs.MY_REGION));
                        if (SharedPrefs.getString(getContext(), SharedPrefs.MY_REGION) ==null ) {
                            Log.i("loc","OnSharedPreferenceChangeListener 2");
                            tv_change_region.setVisibility(View.GONE);
                        }
                        else {
                            Log.i("loc","OnSharedPreferenceChangeListener 3");
                            tv_change_region.setVisibility(View.VISIBLE);
                            tv_change_region.setText(String.valueOf(SharedPrefs.getString(getContext(), SharedPrefs.MY_REGION)));
                        }
                    }


                }


            };
            prefs.registerOnSharedPreferenceChangeListener(listener);

        }
        catch (Exception e){
            Log.e("loc", e.getMessage());
        }
*/


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
                            getRegion();
                            new LocationUpdater().execute();

                        } catch (Exception e) {
                            Log.i("Exception", "caught in get region");
                        }
                    }

                    LatLng currentLocation = new LatLng(lat, lng);
                    // LatLng currentLocation= new LatLng(lat, lng);


                    Log.i("bbt1","lat_long"+currentLocation);


                    //Log.i("t1", "caught"+width+ " "+height);
                    //broker_map.
                   // gmap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("I am here!").icon(icon1));


                    //broker_map.addMarker(new MarkerOptions().position(currentLocation).title("current Location"));
                    // broker_map.animateCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    gmap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    gmap.animateCamera(CameraUpdateFactory.zoomTo(12));

                    //make retrofit call to get Min Max price

                }
            }
        };


        try {
            customMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
                @Override
                public void onDrag(MotionEvent motionEvent) {
                    //Log.d("t1", String.format("ME: %s", motionEvent));

                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {


                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {


                        //Remmember changes sushil start
                        LatLng currentLocation1; //= new LatLng(location.getLatitude(), location.getLongitude());

                        currentLocation1 = gmap.getProjection().fromScreenLocation(point);
                        lat = currentLocation1.latitude;
                        Log.i("t1", "lat" + " " + lat);
                        lng = currentLocation1.longitude;
                        Log.i("t1", "lng" + " " + lng);
                        SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                        SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                        Log.i("t1", "Sharedpref_lat" + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
                        Log.i("t1", "Sharedpref_lng" + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
                        getRegion();
                        Log.i("t1", "latlong" + " " + currentLocation1);
                       // gmap.addMarker(new MarkerOptions().position(currentLocation1));
                        if (isNetworkAvailable()) {
                            new LocationUpdater().execute();
                        }

                       // tv_change_region.setText(SharedPrefs.getString(getActivity(),SharedPrefs.MY_LOCALITY));

                       // tv_change_region.addTextChangedListener(TextWatcher watcher);

                        //tv_change_region.setText("");
                        // Log.i("t1", "Sharedpref_lng_above" + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
                        // // tv_change_region.setText(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
                        // Log.i("t1", "Sharedpref_lng_below" + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));


                        // Intent intent = new Intent(AppConstants.Broker_Locality_Change);
                        // intent.putExtra("broker_locality_change", SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
                        // Log.i("sendbroadcast", "====================");
                        // Log.i("sendbroadcast", "====================" + LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent));


                        // tv_change_region.addTextChangedListener(TextWatcher watcher);

                        //locationName.changeLocation(tv_change_region.getText().toString());
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                        //
                    }
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                }
            });

        } catch (Exception e) {
        }


        return rootView;

    }

//    public void setChangeLoction(ChangeLocation loc)
//    {
//        this.locationName = loc;
//    }
//
//    public interface ChangeLocation
//    {
//        public void changeLocation(String location);
//    }


    //
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        gmap.animateCamera(CameraUpdateFactory.zoomTo(12));
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
                // Log.e(TAG, "Error in http connection " + e.toString());
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
                //Log.e(TAG, "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                jObject = new JSONObject(result);
            } catch (JSONException e) {
                // Log.e(TAG, "Error parsing data " + e.toString());
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
                                if (getActivity() != null) {

                                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LOCALITY, long_name);
                                }

                            } else if (Type.equalsIgnoreCase("sublocality_level_1")) {
                                Address2 += " " + long_name;

//                                if (getActivity() != null)
//                                    SharedPrefs.save(getActivity(),SharedPrefs.MY_LOCALITY,long_name);
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
            Log.i("address", "address" + s);
            autoCompView.dismissDropDown();
            //new LocationUpdater().execute();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
            //gmap.addMarker(new MarkerOptions().position(l).title("marker"));

            //Marker marker = broker_map.addMarker(new MarkerOptions()
            //     .position(l)
            //  .title("Title")
            //.snippet("Description")
            //.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), Mmarker))));

           // Marker m = gmap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("I am here!").icon(icon1));
            // broker_map.animateCamera(CameraUpdateFactory.newLatLng(l));
            gmap.moveCamera(CameraUpdateFactory.newLatLng(l));
            // broker_map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));

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
                                gmap = googleMap;

                                //enableMyLocation();

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
                                gmap.setMyLocationEnabled(true);
                                //setCameraListener();
                                Log.i("t1", "broker_map" + gmap);
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
                            gmap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                            gmap.animateCamera(CameraUpdateFactory.zoomTo(12));

                        }
                    }
                });
                getLocationActivity = new GetCurrentLocation(getActivity(),mcallback);
                //Log.i("t1","mcallback"+""+mcallback);
            }
            else {
                //Intent intent = new Intent(this, MainActivity.class);
                // startActivity(intent);
               // Toast.makeText(getContext(), "Offline Mode", Toast.LENGTH_LONG);
                //((DashboardActivity) getActivity()).showToastMessage("Offline Mode");
            }

        }catch (Exception e){}


    }










}
