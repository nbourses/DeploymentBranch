package com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps;

/**
 * Created by Abhishek on 08/05/15.
 */
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GetCurrentLocation extends Activity implements LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    Context context;

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] CAMERA_PERMS={
            Manifest.permission.CAMERA
    };
    private static final String[] CONTACTS_PERMS={
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final long ONE_MIN = 1000 * 60;
    private static final float MINIMUM_ACCURACY = 60.0f;

    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

    private Location location;

    public CurrentLocationCallback currentLocationCallback;

    public void setCallback(CurrentLocationCallback callback) {
        this.currentLocationCallback = callback;
    }


    public interface CurrentLocationCallback {
        void onComplete(Location location);
    }

    public GetCurrentLocation(Context context, CurrentLocationCallback callback){

        //requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);

        currentLocationCallback = callback;

        this.context = context;

        locationRequest = LocationRequest.create();
       locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }

    }



    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(context,perm));
    }

    @SuppressLint("NewApi")
    @Override
    public void onConnected(Bundle bundle) {
        /*if(!canAccessLocation()) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
            Log.i("Debug","prompt");
        }*/

        location = fusedLocationProviderApi.getLastLocation(googleApiClient);
        Log.d("currentLocation", "\n ======> init.Location : " + location);

        if ( location != null ) {
            if(currentLocationCallback != null)
            currentLocationCallback.onComplete(location);
        }else{
            fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("currentLocation", "\n ======> onLocationChange : " + location);
        if ( location != null ) {
           /* if(currentLocationCallback != null) {
                currentLocationCallback.onComplete(location);
            }*/

           double latitude= location.getLatitude();
            double longitude= location.getLongitude();



        }
        //fusedLocationProviderApi.removeLocationUpdates(googleApiClient, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
