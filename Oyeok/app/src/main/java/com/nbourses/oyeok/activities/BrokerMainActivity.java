package com.nbourses.oyeok.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.fragments.BrokerMap;
import com.nbourses.oyeok.fragments.BrokerPreokFragment;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class BrokerMainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

@Bind(R.id.toolbar)
   Toolbar mToolbar;

    @Bind(R.id.txtEmail)
    TextView emailTxt;

    @Bind(R.id.openmaps)
    Button openmaps;
TextView tv_change_region;
 private boolean gmap=false;
GoogleMap map;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    private TextView option1Count;
    private TextView option2Count;
    private TextView rentalCount;
    private TextView resaleCount;




    DBHelper dbHelper;
    private FragmentDrawer drawerFragment;
    private WebView webView;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

//    private BroadcastReceiver onNotice= new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // intent can contain anydata
//            Log.d("sushil","onReceive called");
//            if (intent.getExtras() != null) {
//                Log.d("sushil","onReceive called"+intent.getExtras().getString("broker_locality_change"));
//                tv_change_region.setText(intent.getExtras().getString("broker_locality_change"));
//            }
//           // tv_change_region.setText("");
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_main);

        ButterKnife.bind(this);
//        IntentFilter iff= new IntentFilter(AppConstants.Broker_Locality_Change);
//        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, iff);
        ///tv_change_region=(TextView) findViewById(R.id.tv_change_region);

       // tv_change_region.setVisibility(View.VISIBLE);


       // tv_change_region.setText("sushil");
        if (General.isNetworkAvailable(getApplicationContext())) {

            Log.i("TRACE", "network availabe");
        }

            else

            {
                Log.i("TRACE", "network not availabile");
                SnackbarManager.show(
                        Snackbar.with(this)
                                .position(Snackbar.SnackbarPosition.TOP)
                                .text("No internet connectivity.")
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
            }

        //hardcore broker with shared prefs
//        General.setSharedPreferences(this,AppConstants.IS_LOGGED_IN_USER, "yes");
//        General.setSharedPreferences(this,AppConstants.USER_ID, "krve2cnz03rc1hfi06upjpnoh9hrrtsy");
//        General.setSharedPreferences(this,AppConstants.ROLE_OF_USER,"broker");


        init();
    }

    private void init() {
        openmaps.setVisibility(View.VISIBLE);

       /* try {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(this);
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                    if (key.equals(SharedPrefs.MY_LOCALITY)) {
                        Log.i("loc","OnSharedPreferenceChangeListener 1");
                        Log.i("loc","OnSharedPreferenceChangeListener 1 rent "+SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LOCALITY));
                        if (SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LOCALITY) ==null ) {
                            Log.i("loc","OnSharedPreferenceChangeListener 2");
                            tv_change_region.setVisibility(View.GONE);
                        }
                        else {
                            Log.i("loc","OnSharedPreferenceChangeListener 3");
                            tv_change_region.setVisibility(View.VISIBLE);
                            tv_change_region.setText(String.valueOf(SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LOCALITY)));
                        }
                    }


                }


            };
            prefs.registerOnSharedPreferenceChangeListener(listener);

        }
        catch (Exception e){
            Log.e("loc", e.getMessage());
        }*/

        /*Fragment fragment = new Ok_Broker_MainScreen();
        loadFragment(fragment, null, R.id.container_map, "");*/

        Fragment brokerPreokFragment = new BrokerPreokFragment();
        loadFragment(brokerPreokFragment, null, R.id.container_map, "");

//        option1Count = (TextView) findViewById(R.id.option1Count);
//         option2Count = (TextView) findViewById(R.id.option2Count);
//         rentalCount = (TextView) findViewById(R.id.rentalCount);
//        resaleCount = (TextView) findViewById(R.id.resaleCount);
//
//
//
//
//
//        SharedPreferences prefs =
//                PreferenceManager.getDefaultSharedPreferences(this);
//
//        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
//
//                if (key.equals(AppConstants.RENTAL_COUNT)) {
//                    if (General.getBadgeCount(getApplicationContext(), AppConstants.RENTAL_COUNT) <= 0)
//                        rentalCount.setVisibility(View.GONE);
//                    else {
//                        rentalCount.setVisibility(View.VISIBLE);
//                        rentalCount.setText(String.valueOf(General.getBadgeCount(getApplicationContext(), AppConstants.HDROOMS_COUNT)));
//                    }
//                }
//                if (key.equals(AppConstants.RESALE_COUNT)) {
//                    if (General.getBadgeCount(getApplicationContext(), AppConstants.RESALE_COUNT) <= 0)
//                        resaleCount.setVisibility(View.GONE);
//                    else {
//                        resaleCount.setVisibility(View.VISIBLE);
//                        resaleCount.setText(String.valueOf(General.getBadgeCount(getApplicationContext(), AppConstants.RESALE_COUNT)));
//                    }
//
//
//                }
//
//                if (key.equals(AppConstants.TENANTS_COUNT)) {
//                    if (General.getBadgeCount(getApplicationContext(), AppConstants.TENANTS_COUNT) <= 0)
//                        option1Count.setVisibility(View.GONE);
//                    else {
//                        option1Count.setVisibility(View.VISIBLE);
//                        option1Count.setText(String.valueOf(General.getBadgeCount(getApplicationContext(), AppConstants.TENANTS_COUNT)));
//                    }
//
//                }
//                if (key.equals(AppConstants.OWNERS_COUNT)) {
//                    if (General.getBadgeCount(getApplicationContext(), AppConstants.OWNERS_COUNT) <= 0)
//                        option2Count.setVisibility(View.GONE);
//                    else {
//                        option2Count.setVisibility(View.VISIBLE);
//                        option2Count.setText(String.valueOf(General.getBadgeCount(getApplicationContext(), AppConstants.OWNERS_COUNT)));
//                    }
//
//                }
//
//                if (key.equals(AppConstants.BUYER_COUNT)) {
//                    if (General.getBadgeCount(getApplicationContext(), AppConstants.BUYER_COUNT) <= 0)
//                        option1Count.setVisibility(View.GONE);
//                    else {
//                        option1Count.setVisibility(View.VISIBLE);
//                        option1Count.setText(String.valueOf(General.getBadgeCount(getApplicationContext(), AppConstants.BUYER_COUNT)));
//                    }
//
//                }
//
//                if (key.equals(AppConstants.SELLER_COUNT)) {
//                    if (General.getBadgeCount(getApplicationContext(), AppConstants.SELLER_COUNT) <= 0)
//                        option2Count.setVisibility(View.GONE);
//                    else {
//                        option2Count.setVisibility(View.VISIBLE);
//                        option2Count.setText(String.valueOf(General.getBadgeCount(getApplicationContext(), AppConstants.SELLER_COUNT)));
//                    }
//
//                }
//
//
//            }
//
//
//        };
//        prefs.registerOnSharedPreferenceChangeListener(listener);



        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(getBaseContext());

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        if (!General.getSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
            if (!dbHelper.getValue(DatabaseConstants.email).equalsIgnoreCase("null")) {
                emailTxt.setVisibility(View.VISIBLE);
                emailTxt.setText(dbHelper.getValue(DatabaseConstants.email));

            }
        }else{
            emailTxt.setVisibility(View.INVISIBLE);
        }

//Remember

        openmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrokerMap brokerMap=new BrokerMap();
//                brokerMap.setChangeLoction(this);
                loadFragment(brokerMap,null,R.id.container_map,"");
                gmap=true;
                //tv_change_region.setVisibility(View.VISIBLE);
               // tv_change_region.setText(SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LOCALITY));

               /* try {
                    SharedPreferences prefs =
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                            if (key.equals(SharedPrefs.MY_LOCALITY)) {
                                Log.i("loc","OnSharedPreferenceChangeListener 1");
                                Log.i("loc","OnSharedPreferenceChangeListener 1 rent "+SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LOCALITY));
                                if (SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LOCALITY) ==null ) {
                                    Log.i("loc","OnSharedPreferenceChangeListener 2");
                                    tv_change_region.setVisibility(View.GONE);
                                }
                                else {
                                    Log.i("loc","OnSharedPreferenceChangeListener 3");
                                    tv_change_region.setVisibility(View.VISIBLE);
                                    tv_change_region.setText(String.valueOf(SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LOCALITY)));
                                }
                            }


                        }


                    };
                    prefs.registerOnSharedPreferenceChangeListener(listener);

                }
                catch (Exception e){
                    Log.e("loc", e.getMessage());
                }*/

//              Bro


//                DashboardClientFragment dashboardClientFragment = new DashboardClientFragment();
//                loadFragment(dashboardClientFragment, null, R.id.container_map, "");

            }
        });
//




    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (map != null) {
            // Access to the location has been granted to the app.
            map.setMyLocationEnabled(true);
        }
    }



    /**
     * load fragment
     * @param fragment
     * @param args
     * @param title
     */
    private void loadFragment(Fragment fragment, Bundle args, int containerId, String title)
    {
        //set arguments
        fragment.setArguments(args);

        //load fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onDrawerItemSelected(View view, int position, String itemTitle) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        if (itemTitle.equals(getString(R.string.useAsClient))) {
            Intent openDashboardActivity =  new Intent(this, ClientMainActivity.class);
            startActivity(openDashboardActivity);
        }
        else if (itemTitle.equals(getString(R.string.profile))) {
            Intent openProfileActivity =  new Intent(this, ProfileActivity.class);
            startActivity(openProfileActivity);
        }
        else if (itemTitle.equals(getString(R.string.brokerOk))) {
            //don't do anything
        }
        else if (itemTitle.equals(getString(R.string.shareApp))) {
            shareReferralLink();
        }
     /*   else if (itemTitle.equals(getString(R.string.supportChat))) {
            //TODO: integration is pending
        } */

        else if (itemTitle.equals(getString(R.string.notifications))) {
            Intent openDealsListing = new Intent(this, ClientDealsListActivity.class);
            openDealsListing.putExtra("default_deal_flag",false);
            startActivity(openDealsListing);
        }
        else if (itemTitle.equals(getString(R.string.likeOnFb))) {
           // setContentView(R.layout.browser);
            webView = (WebView) findViewById(R.id.webView);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("http://www.facebook.com/nexchanges");


        }
        else if (itemTitle.equals(getString(R.string.aboutUs))) {
            //setContentView(R.layout.browser);
            webView = (WebView) findViewById(R.id.webView);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("http://www.hioyeok.com/blog");


        }

       if (fragment != null) {
            loadFragment(fragment, null, R.id.container_map, title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerFragment.handle(item))
            return true;

        return false;
    }




    private void shareReferralLink() {
        DBHelper dbHelper=new DBHelper(getApplicationContext());
        String user_id = dbHelper.getValue(DatabaseConstants.userId);

        Branch branch = Branch.getInstance(getApplicationContext());
        branch.setIdentity(user_id);

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                .setCanonicalIdentifier(user_id);

        LinkProperties linkProperties = new LinkProperties()
                .setChannel("sms")
                .setFeature("sharing")
                .addControlParameter("user_name", user_id)
                .addControlParameter("$android_url", AppConstants.GOOGLE_PLAY_STORE_APP_URL)
                .addControlParameter("$always_deeplink", "true");

        branchUniversalObject.generateShortUrl(getApplicationContext(), linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Hey check this out!");
                    startActivity(Intent.createChooser(intent, "Share link via"));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(webView != null){
            webView = null;
           Intent back = new Intent(this, BrokerMainActivity.class);
            startActivity(back);

        }

        else if(gmap ==true){
            Intent back = new Intent(this, BrokerMainActivity.class);
            startActivity(back);
            tv_change_region.setVisibility(View.VISIBLE);
            tv_change_region.setText(SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LOCALITY));

        }

        else{

                super.onBackPressed();

        }
//        tv_change_region.setVisibility(View.VISIBLE);
        tv_change_region.setText(SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LOCALITY));

        //tv_change_region.setText(intent.getExtras().getString("broker_locality_change"));


    }
//    protected void onResume() {
//        super.onResume();
//      // LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onFilterValueUpdate, new IntentFilter(AppConstants.ON_FILTER_VALUE_UPDATE));
//        IntentFilter iff= new IntentFilter(AppConstants.Broker_Locality_Change);
//        //LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, iff);
//    }
//
//    protected void onPause() {
//        super.onPause();
//      //  LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
//    }
//    @Override
//    public void changeLocation(String location) {
//        tv_change_region.setText(location);
//    }
}
