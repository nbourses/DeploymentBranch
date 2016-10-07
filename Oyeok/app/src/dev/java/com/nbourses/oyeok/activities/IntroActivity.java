package com.nbourses.oyeok.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.adapters.IntroAdapter;
import com.nbourses.oyeok.animations.IntroPageTransformer;
import com.nbourses.oyeok.fragments.GPSTracker;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ritesh on 22/08/16.
 */
public class IntroActivity extends ActionBarActivity {

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Button btnC;
    private Button btnB;
    private TextView useNow;
    private int permissionCheckForLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GetCurrentLocation.CurrentLocationCallback mcallback;
    private GetCurrentLocation getLocationActivity;
    private  GPSTracker gps;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro_layout);
      /*  ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
*/
        gps = new GPSTracker(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

//            gps.showSettingsAlert();

        }
           /* */
        else if(gps.canGetLocation()){ // gps enabled} // return boolean true/false


            saveLatLongLoc(gps.getLatitude(), gps.getLongitude());



             }
       /* permissionCheckForLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        getLocationActivity = new GetCurrentLocation(this, mcallback);



        mcallback = new GetCurrentLocation.CurrentLocationCallback() {

            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    Log.i("Exception11", "inside mcallback "+location);
                    *//*lat = location.getLatitude();


                    lng = location.getLongitude();
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                    General.setSharedPreferences(getContext(),AppConstants.MY_LAT,lat + "");
                    General.setSharedPreferences(getContext(),AppConstants.MY_LNG,lng + "");

              *//*  }
            }
        };*/
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        btnC = (Button) findViewById(R.id.btnC);
        btnB = (Button) findViewById(R.id.btnB);
        useNow = (TextView) findViewById(R.id.useNow);
btnC.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.i("TG","ithe tithe 1");
        SignUpFragment signUpFragment = new SignUpFragment();
        AppConstants.SIGNUP_FLAG = true;
        Bundle bundle = new Bundle();
        bundle.putString("lastFragment", "clientIntro");
        DBHelper dbHelper = new DBHelper(getBaseContext());
        dbHelper.save(DatabaseConstants.userRole, "client");
        General.setSharedPreferences(getApplicationContext(),AppConstants.ROLE_OF_USER,"client");
        loadFragmentAnimated(signUpFragment, bundle, R.id.container_Signup1, "");
         }
});
        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TG","ithe tithe 2");
                SignUpFragment signUpFragment = new SignUpFragment();
                AppConstants.SIGNUP_FLAG = true;
                Bundle bundle = new Bundle();
                bundle.putString("lastFragment", "brokerIntro");
                DBHelper dbHelper = new DBHelper(getBaseContext());
                dbHelper.save(DatabaseConstants.userRole, "broker");
                General.setSharedPreferences(getApplicationContext(),AppConstants.ROLE_OF_USER,"broker");
                loadFragmentAnimated(signUpFragment, bundle, R.id.container_Signup1, "");
            }
        });
        useNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TG","ithe tithe 3");
                useNow.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                Intent intent = new Intent(getApplicationContext(), ClientMainActivity.class);
                intent.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        // Set a PageTransformer
        mViewPager.setPageTransformer(false, new IntroPageTransformer());

    }
    private void loadFragmentAnimated(Fragment fragment, Bundle args, int containerId, String title)
    {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);


        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (AppConstants.SIGNUP_FLAG) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_Signup1)).commit();
            AppConstants.SIGNUP_FLAG = false;
        } else {
            super.onBackPressed();
        }
    }


    /*@OnClick({R.id.btnC, R.id.btnB})
    public void onOptionClick(View v) {
       // Intent intent;
        Log.i("TAG","ithe tithe");
        if (v.getId() == btnC.getId()) {
            Log.i("TG","ithe tithe 1");
        }
        if (v.getId() == btnB.getId()) {
            Log.i("TG","ithe tithe 2");
        }
        *//*if (v.getId() == useNow.getId()) {
            Log.i("TG","ithe tithe 3");
            intent = new Intent(this, ClientMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }*//*
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==LOCATION_PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            Log.i("Tag11","latlong : "+gps.getLatitude()+"  "+gps.getLongitude());
                saveLatLongLoc(gps.getLatitude(), gps.getLongitude());

            }
        }




    }
    private void saveLatLongLoc(Double lat, Double lng){
        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LAT, lat +"");
        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LNG, lng + "");
        General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LAT,lat + "");
        General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LNG,lng + "");
        try {
            geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(lat,lng, 1);
            String region = addresses.get(0).getSubLocality();
            SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LOCALITY, region);
            General.setSharedPreferences(getApplicationContext(),AppConstants.LOCALITY,region);

            Log.i("Tag11","latlong : "+lat+"  "+lng+ ""+region);
        } catch (IOException e) {
            Log.i("TAG","Caught in exception in geocoding 1"+e);
        }
    }
}
