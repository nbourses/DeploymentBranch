package com.nbourses.oyeok.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.GoogleCloudMessaging.MyGcmListenerService;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.fragments.AppSetting;
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
import me.leolin.shortcutbadger.ShortcutBadger;

public class BrokerMainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

@Bind(R.id.toolbar)
   Toolbar mToolbar;

    @Bind(R.id.txtEmail)
    TextView emailTxt;

    @Bind(R.id.openmaps)
    Button openmaps;
    @Bind(R.id.DONE)
    Button doneButton;

//    @Bind(R.id.preok_layout)
//    Toolbar preok_layout;

//    @Bind(R.id.buildingSlider)
//    RelativeLayout buildingSlider;
boolean setting=false;

   int backpress;

    TextView tv_change_region;
 private boolean gmap=false;
GoogleMap map;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    private TextView option1Count;
    private TextView option2Count;
    private TextView rentalCount;
    private TextView resaleCount;
    private Animation slide_down;

    private Boolean buildingSliderflag = false;
    private Boolean DrawerFlag = false;
    private Boolean openMapsFlag = false;
    private Boolean webviewFlag = false;
    private Boolean signupSuccessflag = false;





    DBHelper dbHelper;
    private FragmentDrawer drawerFragment;
    private WebView webView;


    SharedPreferences.OnSharedPreferenceChangeListener listener;


    private BroadcastReceiver networkConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            networkConnectivity();

        }
    };


    private BroadcastReceiver buildingSliderFlag = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("buildingSliderflag1","buildingSliderflag "+buildingSliderflag);
          //  if(intent.getExtras().getString("buildingSliderFlag") != null){
                buildingSliderflag = intent.getExtras().getBoolean("buildingSliderFlag");
                Log.i("buildingSliderflag","buildingSliderflag "+buildingSliderflag);
         //   }
        }
    };

    private BroadcastReceiver localityBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras().getString("locality") != null){
                String locality = intent.getExtras().getString("locality");
                Log.i("localityBroadcast","localityBroadcast1 ");
                Log.i("localityBroadcast","localityBroadcast "+locality);
                setLocality(locality);
            }
        }
    };

//    private BroadcastReceiver signupSuccessFlag = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.i("signupSuccessflag rec","signupSuccessflag ");
//            signupSuccessflag = intent.getExtras().getBoolean("signupSuccessflag");
//            Log.i("signupSuccessflag r","signupSuccessflag "+signupSuccessflag);

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
        AppConstants.CURRENT_USER_ROLE ="broker";





Log.i("broker","service running "+isMyServiceRunning(MyGcmListenerService.class));




        ButterKnife.bind(this);

        tv_change_region=(TextView) findViewById(R.id.tv_change_region);
        openmaps=(Button) findViewById(R.id.openmaps);

        tv_change_region.setVisibility(View.VISIBLE);
        try {
            tv_change_region.setText(SharedPrefs.getString(this, SharedPrefs.MY_LOCALITY));
        }catch(Exception e){

        }

        ShortcutBadger.removeCount(this);

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
    @Override
    protected void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(networkConnectivity, new IntentFilter(AppConstants.NETWORK_CONNECTIVITY));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(localityBroadcast, new IntentFilter(AppConstants.LOCALITY_BROADCAST));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(buildingSliderFlag, new IntentFilter(AppConstants.BUILDINGSLIDERFLAG));
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(signupSuccessFlag, new IntentFilter(AppConstants.SIGNUPSUCCESSFLAG));

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister since the activity is not visible
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(networkConnectivity);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(localityBroadcast);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(buildingSliderFlag);
//        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(signupSuccessFlag);


    }







    private void init() {
        slide_down = AnimationUtils.loadAnimation(this,
                R.anim.slide_down);
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


       //buildingSlider = (RelativeLayout) findViewById(R.id.buildingSlider);

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
        dbHelper.save(DatabaseConstants.userRole,"Broker");

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);




        if (!General.getSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
            //if (!dbHelper.getValue(DatabaseConstants.email).equalsIgnoreCase("null")) {
            if (!General.getSharedPreferences(this,AppConstants.EMAIL).equalsIgnoreCase("null")) {
                emailTxt.setVisibility(View.VISIBLE);
                emailTxt.setText(General.getSharedPreferences(this,AppConstants.EMAIL));

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
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                loadFragment(brokerMap,null,R.id.container_map,"");
                doneButton.setVisibility(View.VISIBLE);
                gmap=true;

                tv_change_region.setVisibility(View.VISIBLE);
             //   tv_change_region.setText(SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LOCALITY));

                //tv_change_region.setVisibility(View.VISIBLE);
               // tv_change_region.setText(SharedPrefs.getString(getBaseContext(), SharedPrefs.MY_LOCALITY));

               


//                DashboardClientFragment dashboardClientFragment = new DashboardClientFragment();
//                loadFragment(dashboardClientFragment, null, R.id.container_map, "");

            }
        });
//

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Intent mainscreen  = ;
                startActivity(new Intent(getApplicationContext(),BrokerMainActivity.class));
                Log.i("donebutton","done button clicked   ");
                getFragmentManager().popBackStack();
                finish();
                gmap =false;
                backpress = 0;
//                getSupportActionBar().setDisplayShowHomeEnabled(true);
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                doneButton.setVisibility(View.GONE);




            }
        });
        
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

        fragmentTransaction.show(fragment);
        fragmentTransaction.addToBackStack(title);
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
            AppConstants.BROKER_DEAL_FLAG = true;
            Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
            intent.putExtra("userRole", "client");
            intent.putExtra(AppConstants.OK_ID, AppConstants.SUPPORT_CHANNEL_NAME);
            startActivity(intent);
        }
        else if (itemTitle.equals(getString(R.string.likeOnFb))) {
           // setContentView(R.layout.browser);
            webView = (WebView) findViewById(R.id.webView);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("http://www.facebook.com/hioyeok");



        }
        else if (itemTitle.equals(getString(R.string.aboutUs))) {
            //setContentView(R.layout.browser);
            webView = (WebView) findViewById(R.id.webView);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("http://www.hioyeok.com/blog");


        }
        else if (itemTitle.equals(getString(R.string.settings))) {
            AppSetting appSetting=new AppSetting();
            setting=true;
            loadFragment(appSetting,null,R.id.container_sign,"");


        }
        else if (itemTitle.equals(getString(R.string.RegisterSignIn))) {
            SignUpFragment signUpFragment = new SignUpFragment();
            // signUpFragment.getView().bringToFrFont();
            Bundle bundle = new Bundle();
            bundle.putStringArray("Chat", null);
            bundle.putString("lastFragment", "drawer");
            loadFragment(signUpFragment, bundle, R.id.container_sign, "");



        }



//       if (fragment != null && !itemTitle.equals(getString(R.string.settings))) {
//
//            loadFragment(fragment, null, R.id.container_map, title);
//           Log.i("ONBACKPRESSED","broker main activity "+setting);
//
//           Log.i("ONBACKPRESSED","broker main activity "+setting);
//        }

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

        String mob_no = General.getSharedPreferences(this,AppConstants.MOBILE_NUMBER);
        Log.i("mob_no","mob_no "+mob_no);

        branch.setIdentity(mob_no);

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
        Log.i("ONBACKPRESSED","signupSuccessflag 432 "+signupSuccessflag);
        Log.i("ONBACKPRESSED","buildingSliderflag "+buildingSliderflag);

        Log.i("ONBACKPRESSED","broker main activity "+setting);


        if(AppConstants.SIGNUP_FLAG){
           if(AppConstants.REGISTERING_FLAG){}else{
                getSupportFragmentManager().popBackStackImmediate();

            Intent inten = new Intent(this, BrokerMainActivity.class);

            startActivity(inten);
            finish();
            AppConstants.SIGNUP_FLAG=false;

            backpress = 0;}
        }else
        if(webView != null){
            if (webView.canGoBack()) {
             webView.goBack();
            }
            else {
            webView = null;

                Intent inten = new Intent(this, BrokerMainActivity.class);
                inten.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inten);
                finish();

//            finish();
                backpress = 0;
                 }
        }
        else if(gmap ==true){
            Log.i("ONBACKPRESSED","broker main activity gmap"+getSupportFragmentManager().getBackStackEntryCount());

//            int count = getFragmentManager().getBackStackEntryCount();
//            for(int i = 0; i < count; ++i) {
//            getSupportFragmentManager().popBackStackImmediate();
//            }
            Log.i("ONBACKPRESSED","broker main activity gmap"+getSupportFragmentManager().getBackStackEntryCount());
            Intent inten = new Intent(this, BrokerMainActivity.class);

            startActivity(inten);
            finish();
//            }


            gmap =false;
//            finish();

            backpress = 0;
        }
        else if(buildingSliderflag == true){
            Log.i("ONBACKPRESSED","buildingSliderflag "+buildingSliderflag);

            Intent intent = new Intent(AppConstants.SLIDEDOWNBUILDINGS);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            buildingSliderflag = false;
            backpress = 0;
        }else if(setting==true){
            Log.i("BACK PRESSED"," =================== setting"+setting);
//            getFragmentManager().popBackStack();
            if(SharedPrefs.getString(this, SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true") || SharedPrefs.getString(this, SharedPrefs.CHECK_BEACON).equalsIgnoreCase("true")){
//                super.onBackPressed();
//                getSupportFragmentManager().popBackStack();

                int count = getFragmentManager().getBackStackEntryCount();
                for(int i = 0; i < count; ++i) {
                    getFragmentManager().popBackStackImmediate();
                }

                Intent inten = new Intent(this, BrokerMainActivity.class);
                inten.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inten);
                finish();
                Log.i("SIGNUP_FLAG", "SIGNUP_FLAG=========  loadFragment setting client4 " + getFragmentManager().getBackStackEntryCount());
                setting = false;

                backpress = 0;


            }else {
                Log.i("BACK","FRAGMENT COUNT "+getSupportFragmentManager().getBackStackEntryCount());
                getSupportFragmentManager().popBackStackImmediate();
                //super.onBackPressed();
                //finish();
                Log.i("SIGNUP_FLAG", "SIGNUP_FLAG=========  loadFragment setting client4 " + getFragmentManager().getBackStackEntryCount());
                setting = false;
                backpress = 0;
            }

        }
        else{

//            if(backpress <1) {
//                backpress = (backpress + 1);
//
//                Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
//            }else if (backpress>=1) {
//                backpress = 0;
                this.finish();
//            }

        }



    }





    private  void networkConnectivity(){
        SnackbarManager.show(
                Snackbar.with(this)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .text("INTERNET CONNECTIVITY NOT AVAILABLE")
                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
    }



//    @Override
//    public void changeLocation(String location) {
//        tv_change_region.setText(location);
//    }
    private void setLocality(String locality){
        tv_change_region.setText(locality);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }




    public void Wlak_Beacon() throws InterruptedException {
     BrokerPreokFragment preokFragment = new BrokerPreokFragment();

        if (SharedPrefs.getString(this, SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true") && SharedPrefs.getString(this, SharedPrefs.CHECK_BEACON).equalsIgnoreCase("true")) {

            preokFragment.beaconAlertBroker(preokFragment.v);
            preokFragment.walkthroughBroker(preokFragment.v);


        }else if (SharedPrefs.getString(this, SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true")){
            preokFragment.walkthroughBroker(preokFragment.v);
        }else{
            preokFragment.beaconAlertBroker(preokFragment.v);
        }


    }









}
