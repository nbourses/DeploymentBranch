package com.nbourses.oyeok.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

import static com.nbourses.oyeok.helpers.AppConstants.LOCATION_PERMISSION_REQUEST_CODE;

//import com.nbourses.oyeok.Database.DBHelper;

/**
 * Created by ritesh on 22/08/16.
 */
public class IntroActivity extends ActionBarActivity {

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Button btnC;
    private Button btnB;
    private Button playgame;
    private Button skip;
    private int permissionCheckForLocation;
    //private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GetCurrentLocation.CurrentLocationCallback mcallback;
    private GetCurrentLocation getLocationActivity;
    private  GPSTracker gps;
    private Geocoder geocoder;
    ImageView img1,img2,img3,img4,img5;
    private boolean GPSIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro_layout);
      /*  ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
*/
        if (General.getSharedPreferences(getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI).equals("")) {
            General.setSharedPreferences(getBaseContext(), AppConstants.TIME_STAMP_IN_MILLI, String.valueOf(System.currentTimeMillis()));
            Log.i("TIMESTAMP", "millis " + System.currentTimeMillis());
        }

        mcallback = new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                Log.i("TAG", "ralpher 26 ");

                if(location != null){
                    Log.i("Exception11", "inside mcallback tra "+location.getLatitude()+"  "+location.getLongitude());
                    General.saveLatLongLoc(IntroActivity.this, location.getLatitude(), location.getLongitude());
                }
                else{

                        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LAT, "19.1230339");
                        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LNG, "72.8350437");
                        General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LAT,"19.1230339");
                        General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LNG,"72.8350437");
                        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LOCALITY, "Andheri West");
                        General.setSharedPreferences(getApplicationContext(),AppConstants.LOCALITY,"Andheri West");

                }

            }
        };







      /*  mcallback = new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {F
                Log.i("TAG", "ralpher 26 ");
                Log.i("Exception11", "inside mcallback tra "+location);
            }
        };*/

       ///// gps = new GPSTracker(this);
        //showPermissionDialog();
        General.setSharedPreferences(getBaseContext(),AppConstants.NO_CALL,"0");


        Log.i("latlongi","latlongi 76 "+General.getSharedPreferences(this,AppConstants.MY_LAT));
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.setBackgroundResource(R.drawable.intro_bg);
        btnC = (Button) findViewById(R.id.btnC);
        btnB = (Button) findViewById(R.id.btnB);
        playgame = (Button) findViewById(R.id.playgame);
        skip = (Button) findViewById(R.id.skip);

        img1=(ImageView)findViewById(R.id.img1);
        img2=(ImageView)findViewById(R.id.img2);

        img3=(ImageView)findViewById(R.id.img3);

        img4=(ImageView)findViewById(R.id.img4);
        img5=(ImageView)findViewById(R.id.img5);

        String s =null;
        SpannableString ss1;
        StyleSpan iss;
        s= "I am a Customer";
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 7,s.length(), 0); // set size
        iss = new StyleSpan(android.graphics.Typeface.ITALIC);
        ss1.setSpan(iss, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        btnC.setText(ss1);


        s= "I am a Broker";
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 7,s.length(), 0); // set size
        iss = new StyleSpan(android.graphics.Typeface.ITALIC);
        ss1.setSpan(iss, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        btnB.setText(ss1);


btnC.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.i("TG","ithe tithe 1");
        SignUpFragment signUpFragment = new SignUpFragment();
        AppConstants.SIGNUP_FLAG = true;
        Bundle bundle = new Bundle();
        bundle.putString("lastFragment", "clientIntro");
        //DBHelper dbHelper = new DBHelper(getBaseContext());
       // dbHelper.save(DatabaseConstants.userRole, "client");
        General.setSharedPreferences(getApplicationContext(),AppConstants.ROLE_OF_USER,"client");
        skip.setVisibility(View.GONE);
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
               // DBHelper dbHelper = new DBHelper(getBaseContext());
               // dbHelper.save(DatabaseConstants.userRole, "broker");
                General.setSharedPreferences(getApplicationContext(),AppConstants.ROLE_OF_USER,"broker");
                skip.setVisibility(View.GONE);
                loadFragmentAnimated(signUpFragment, bundle, R.id.container_Signup1, "");
            }
        });
        playgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TG","ithe tithe 3");
                playgame.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                General.setSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER, "client");
                General.setSharedPreferences(getBaseContext(),AppConstants.ROLE_GAMER,"gamer");
                Intent intent = new Intent(getBaseContext(), ClientMainActivity.class);
                intent.putExtra("data","game");
                intent.putExtra("role","");
                intent.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playgame.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
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
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        img1.setImageResource(R.drawable.selected_dot1);
                        img2.setImageResource(R.drawable.selected_dot);
                        img3.setImageResource(R.drawable.selected_dot);
                        img4.setImageResource(R.drawable.selected_dot);
                        img5.setImageResource(R.drawable.selected_dot);
                        break;

                    case 1:
                        img1.setImageResource(R.drawable.selected_dot);
                        img2.setImageResource(R.drawable.selected_dot1);
                        img3.setImageResource(R.drawable.selected_dot);
                        img4.setImageResource(R.drawable.selected_dot);
                        img5.setImageResource(R.drawable.selected_dot);
                        break;

                    case 2:
                        img1.setImageResource(R.drawable.selected_dot);
                        img2.setImageResource(R.drawable.selected_dot);
                        img3.setImageResource(R.drawable.selected_dot1);
                        img4.setImageResource(R.drawable.selected_dot);
                        img5.setImageResource(R.drawable.selected_dot);
                        break;

                    case 3:
                        img1.setImageResource(R.drawable.selected_dot);
                        img2.setImageResource(R.drawable.selected_dot);
                        img3.setImageResource(R.drawable.selected_dot);
                        img4.setImageResource(R.drawable.selected_dot1);
                        img5.setImageResource(R.drawable.selected_dot);
                        break;
                    case 4:
                        img1.setImageResource(R.drawable.selected_dot);
                        img2.setImageResource(R.drawable.selected_dot);
                        img3.setImageResource(R.drawable.selected_dot);
                        img4.setImageResource(R.drawable.selected_dot);
                        img5.setImageResource(R.drawable.selected_dot1);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Set a PageTransformer
        mViewPager.setPageTransformer(false, new IntroPageTransformer());



    }

    public void onResume() {
        Log.i("yessp","yesspo "+GPSIntent);
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(GPSIntent && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            if (ContextCompat.checkSelfPermission(IntroActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // General.showPermissionDialog(this, this);
                ActivityCompat.requestPermissions(IntroActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        AppConstants.LOCATION_PERMISSION_REQUEST_CODE);

            }
            else{

                Log.i("TAG", "ralpher 24 ");
                getLocationActivity = new GetCurrentLocation(IntroActivity.this, mcallback);

            }
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i("TAG", "ralpher re  ");
            buildAlertMessageNoGps();
        }else{
            Log.i("TAG", "ralpher re1 ");
            if (ContextCompat.checkSelfPermission(IntroActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // General.showPermissionDialog(this, this);
                ActivityCompat.requestPermissions(IntroActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        AppConstants.LOCATION_PERMISSION_REQUEST_CODE);

            }
            else{

                Log.i("TAG", "ralpher 24 ");
                getLocationActivity = new GetCurrentLocation(IntroActivity.this, mcallback);

            }
        }
        super.onResume();


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
        Log.i("yessp","yessp "+GPSIntent);
        if (AppConstants.SIGNUP_FLAG) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_Signup1)).commit();
            AppConstants.SIGNUP_FLAG = false;
            skip.setVisibility(View.VISIBLE);
        }
        else{
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

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==LOCATION_PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            Log.i("Tag11","latlong : "+gps.getLatitude()+"  "+gps.getLongitude());
                saveLatLongLoc(gps.getLatitude(), gps.getLongitude());

            }
        }




    }*/

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        GPSIntent = true;
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LAT, "19.1230339");
                        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LNG, "72.8350437");
                        General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LAT,"19.1230339");
                        General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LNG,"72.8350437");
                        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LOCALITY, "Andheri West");
                        General.setSharedPreferences(getApplicationContext(),AppConstants.LOCALITY,"Andheri West");
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveLatLongLoc(Double lat, Double lng){
        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LAT, lat +"");
        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LNG, lng + "");
        General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LAT,lat + "");
        General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LNG,lng + "");
        Log.i("Tag11","latlong 1 : "+lat+"  "+lng);
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


    private void showPermissionDialog() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {



            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

        } else {
            if (gps.canGetLocation())
                Log.i("TAG","ralph "+gps.getLatitude());
            saveLatLongLoc(gps.getLatitude(), gps.getLongitude());
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.i("TAG","ralph "+requestCode+" "+permissions+ " "+grantResults);
        switch (requestCode) {
            case AppConstants.LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG","ralphorp "+requestCode+" "+permissions+ " "+grantResults);
                    SharedPrefs.save(this, SharedPrefs.PERMISSION, "false");
                  /////  gps = new GPSTracker(this);
                    //Log.i("TAG","ralph 3 "+ gps.canGetLocation()+" "+gps.getLatitude());
                   /////gps.canGetLocation()) {
                        Log.i("TAG", "ralpher 25 ");
                        getLocationActivity = new GetCurrentLocation(IntroActivity.this, mcallback);
                           // General.saveLatLongLoc(IntroActivity.this, gps.getLatitude(), gps.getLongitude());


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.



                } else {

                    SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LAT, "19.1230339");
                    SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LNG, "72.8350437");
                    General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LAT,"19.1230339");
                    General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LNG,"72.8350437");
                    SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LOCALITY, "Andheri West");
                    General.setSharedPreferences(getApplicationContext(),AppConstants.LOCALITY,"Andheri West");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }





}
