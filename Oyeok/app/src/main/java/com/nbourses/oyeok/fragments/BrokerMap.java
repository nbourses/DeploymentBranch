package com.nbourses.oyeok.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.AutoCompletePlaces;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
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
public class BrokerMap extends DashboardClientFragment{
@Bind(R.id.iv_markerpin)
    ImageView iv_markerpin;
    GoogleMap gmap;
    CustomMapFragment customMapFragment;
    AutoCompleteTextView autoCompView;
    private BitmapDescriptor icon1;
   // private ChangeLocation locationName;
   private Point point;


   // id_markerpin;

TextView tv_change_region;
    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "", fullAddres = "",locality="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.broker_map, container, false);
        final View rootView1 = inflater.inflate(R.layout.toolbar, container, false);
     // ButterKnife.bind(this, rootView);
     //  tv_change_region=(TextView) findViewById(R.id.tv_change_region);
       // icon1 = BitmapDescriptorFactory.fromResource(R.drawable.iv_markerpin);
        iv_markerpin = (ImageView) rootView.findViewById(R.id.iv_markerpin);

//        tv_change_region = (TextView) rootView1.findViewById(R.id.tv_change_region);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                iv_markerpin.getViewTreeObserver().removeGlobalOnLayoutListener(this);


                int[] locations = new int[2];
                iv_markerpin.getLocationOnScreen(locations);
                x = locations[0]+26;
                y = locations[1]-60;
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
//                new LocationUpdater().execute();


            }
        });
if(customMapFragment==null) {
    customMapFragment = ((CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.g_map));
    customMapFragment.getMap();
}

        customMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;
               double lat = 19.1269299;
              double  lng = 72.8376545999999;

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    gmap.setMyLocationEnabled(true);



                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

//                    enableMyLocation();
               // Marker m = gmap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("").icon(icon1));

                SharedPrefs.save(getContext(), SharedPrefs.MY_LAT, lat + "");
                    SharedPrefs.save(getContext(), SharedPrefs.MY_LNG, lng + "");




                    LatLng center = new LatLng(lat, lng);

                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 12));


                }
            });


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
                        gmap.addMarker(new MarkerOptions().position(currentLocation1));
                        if (isNetworkAvailable()) {
                            new LocationUpdater().execute();
                        }
                        tv_change_region.setText(SharedPrefs.getString(getActivity(),SharedPrefs.MY_LOCALITY));
                       // tv_change_region.addTextChangedListener(TextWatcher watcher);
                        //locationName.changeLocation(tv_change_region.getText().toString());
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                        //
                    }
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                }
            });

        }catch (Exception e){}



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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        gmap.animateCamera(CameraUpdateFactory.zoomTo(12));
        autoCompView.clearListSelection();
        //rem
        getLocationFromAddress(autoCompView.getText().toString());
        if (isNetworkAvailable()) {
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
                  locality=  zero.getString("sublocality_level_1");

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
            Log.i("address","address"+s);
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
            lat=location.getLatitude();
            lng=location.getLongitude();
            Log.i("lat="+location.getLatitude()," long="+location.getLongitude());
            LatLng l=new LatLng(location.getLatitude(),location.getLongitude());
            Log.i("t1", "lng" + " " + lng);
            SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
            SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
            gmap.addMarker(new MarkerOptions().position(l).title("marker"));

            //Marker marker = broker_map.addMarker(new MarkerOptions()
            //     .position(l)
            //  .title("Title")
            //.snippet("Description")
            //.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), Mmarker))));

            Marker m = gmap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("I am here!").icon(icon1));
            // broker_map.animateCamera(CameraUpdateFactory.newLatLng(l));
            gmap.moveCamera(CameraUpdateFactory.newLatLng(l));
            // broker_map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }




    }













}
