package com.nbourses.oyeok.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.adapters.PlacesAutoCompleteAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.listeners.RecyclerItemClickListener;
import com.nbourses.oyeok.realmModels.Favourites;
import com.nbourses.oyeok.realmModels.LatiLongi;
import com.nbourses.oyeok.realmModels.addBuildingRealm;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import io.realm.Realm;

/**
 * Created by ritesh on 30/09/16.
 */

public class searchFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    protected GoogleApiClient mGoogleApiClient;

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    private EditText mAutocompleteView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    ImageView delete;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        buildGoogleApiClient();
        View rootView = inflater.inflate(R.layout.fragment_search, container,
                false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        mAutocompleteView = (EditText)rootView.findViewById(R.id.autocomplete_places);

        delete=(ImageView)rootView.findViewById(R.id.cross);


        mAutoCompleteAdapter =  new PlacesAutoCompleteAdapter(getContext(), R.layout.searchview_adapter,
                mGoogleApiClient, BOUNDS_INDIA, null);

        mRecyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);

        // Do something else
        /*mAutocompleteView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mAutocompleteView, InputMethodManager.SHOW_FORCED);*/
        init();
        return rootView;


    }

    private void init(){
        mLinearLayoutManager=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);
        delete.setOnClickListener(this);

        mAutoCompleteAdapter.getFilter().filter("ritz369");
        mAutocompleteView.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                }else if(!mGoogleApiClient.isConnected()){
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .text("Connection failed")
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                    //Toast.makeText(getApplicationContext(), AppConstants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
                    Log.e("PlacesTag",AppConstants.API_NOT_CONNECTED);
                }else if(s.toString().equals("")){
                    mAutoCompleteAdapter.getFilter().filter("ritz369");
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });




        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "floki Autocomplete item selected: "+placeId+"  title  "+mAutoCompleteAdapter.getItem(position).title+"  address : "+mAutoCompleteAdapter.getItem(position).description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if(places.getCount()==1){
                                    //Do the things here on Click.....
                                   // Toast.makeText(getContext(),String.valueOf(places.get(0).getLatLng()),Toast.LENGTH_SHORT).show();
                                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                     Log.i("TAG","latlong "+String.valueOf(places.get(0).getLatLng().longitude) + " "+String.valueOf(places.get(0).getLatLng().latitude ));
//                                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT,String.valueOf(places.get(0).getLatLng().latitude));
//                                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, String.valueOf(places.get(0).getLatLng().longitude));
//General.setSharedPreferences(getContext(),AppConstants.MY_LATITUDE,Double.parseDouble(places.get(0).getLatLng().latitude));
                                    AppConstants.MY_LATITUDE=places.get(0).getLatLng().latitude;
                                    AppConstants.MY_LONGITUDE=places.get(0).getLatLng().longitude;
                                    Log.i("savebuilding123","savebuilding12: "+General.getSharedPreferences(getContext(), AppConstants.LOCALITY)+"  "+ SharedPrefs.getString(getContext(), SharedPrefs.MY_CITY)+" AppConstants.MY_LATITUDE : "+AppConstants.MY_LATITUDE+"AppConstants.MY_Longitute "+AppConstants.MY_LONGITUDE);

                                }else {
                                    try {
                                        Realm myRealm = General.realmconfig(getContext());
                                        addBuildingRealm results1 = myRealm.where(addBuildingRealm.class).equalTo("id", placeId).findFirst();

                                        AppConstants.MY_LATITUDE= Double.parseDouble(results1.getLat());
                                        AppConstants.MY_LONGITUDE= Double.parseDouble(results1.getLng());
                                        Log.i("savebuilding123","savebuilding1: "+General.getSharedPreferences(getContext(), AppConstants.LOCALITY)+"  "+ SharedPrefs.getString(getContext(), SharedPrefs.MY_CITY)+" AppConstants.MY_LATITUDE : "+AppConstants.MY_LATITUDE+"AppConstants.MY_Longitute "+AppConstants.MY_LONGITUDE);




                                    }
                                    catch(Exception e){
                                        Log.i("TAG","Caught in exception Favourites Realm "+e );
                                    }
                                   // Toast.makeText(getContext(),AppConstants.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                                }
                                saveAddress(item.title+"",item.description+"",AppConstants.MY_LATITUDE,AppConstants.MY_LONGITUDE,item.placeId+"");
                                General.setSharedPreferences(getContext(), AppConstants.LOCALITY,item.title+"");
                                General.setSharedPreferences(getContext(), AppConstants.MY_BASE_LAT,AppConstants.MY_LATITUDE+"");
                                General.setSharedPreferences(getContext(), AppConstants.MY_BASE_LNG,AppConstants.MY_LONGITUDE+"");
                                General.setSharedPreferences(getContext(), AppConstants.MY_LAT,AppConstants.MY_LATITUDE+"");
                                General.setSharedPreferences(getContext(), AppConstants.MY_LNG,AppConstants.MY_LONGITUDE+"");
                                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.container_Signup)).commit();
                                if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                                    Intent intent = new Intent(AppConstants.RESETMAP);
                                    intent.putExtra("b_resetmap", "b_map");
                                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                                }else{
                                    Intent intent = new Intent(AppConstants.RESETMAP);
                                    intent.putExtra("c_resetmap", "c_map");
                                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                                }

                            }
                        });

                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                    }
                })
        );
        mAutocompleteView.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

       // imgr.showSoftInput(mAutocompleteView, InputMethodManager.SHOW_IMPLICIT);

    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Google API Callback","Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        //Toast.makeText(getContext(), AppConstants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
        SnackbarManager.show(
                Snackbar.with(getActivity())
                        .text("Connection failed.")
                        .position(Snackbar.SnackbarPosition.TOP)
                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
    }

    @Override
    public void onClick(View v) {
        if(v==delete){
            mAutocompleteView.setText("");
            mAutoCompleteAdapter.getFilter().filter("ritz369");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()){
            Log.v("Google API","Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            Log.v("Google API","Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

   public void saveAddress(String title,String Address,Double lat,Double longi,String placeid) {
       Realm myRealm = General.realmconfig( getContext());
        Favourites favourites = new Favourites();
        favourites.setTitle(title);
        favourites.setAddress(Address);
        LatiLongi latlon = new LatiLongi();
        latlon.setLat(lat);
        latlon.setLng(longi);
        favourites.setLatiLongi(latlon);
        favourites.setId(placeid);

       if (myRealm.isInTransaction())
            myRealm.cancelTransaction();
        myRealm.beginTransaction();
        myRealm.copyToRealmOrUpdate(favourites);
        myRealm.commitTransaction();
   }









}
