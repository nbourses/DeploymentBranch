package com.nbourses.oyeok.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.fragments.AddBuilding;
import com.nbourses.oyeok.fragments.AddListing;
import com.nbourses.oyeok.fragments.AddListingFinalCard;
import com.nbourses.oyeok.fragments.AppSetting;
import com.nbourses.oyeok.fragments.BrokerPreokFragment;
import com.nbourses.oyeok.fragments.MainScreenPropertyListing;
import com.nbourses.oyeok.fragments.PartnerBrokerFragment;
import com.nbourses.oyeok.fragments.ShareOwnersNo;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.razorpay.PaymentResultListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import me.leolin.shortcutbadger.ShortcutBadger;

//import com.nbourses.oyeok.Database.DBHelper;


public class BrokerMainActivity extends BrokerMainPageActivity implements FragmentDrawer.FragmentDrawerListener, PaymentResultListener {


@Bind(R.id.toolbar)
   Toolbar mToolbar;

    @Bind(R.id.txtEmail)
    TextView emailTxt;

    @Bind(R.id.editBaseLocation)
    ImageView editBaseLocation;

    @Bind(R.id.basewraper)
    RelativeLayout basewraper;

    @Bind(R.id.DONE)
    Button doneButton;

//    @Bind(R.id.favbroker)
//    ImageView favbroker;

    @Bind(R.id.portfolioCount)
    TextView portfolioCount;


    @Bind(R.id.setbaseloc)
    LinearLayout setbaseloc;

    @Bind(R.id.tv_change_region)
    TextView tv_change_region;

    @Bind(R.id.cardb)
    FrameLayout cardb;
    @Bind(R.id.card)
    FrameLayout card;

    //setbaseloc
//    @Bind(R.id.preok_layout)
//    Toolbar preok_layout;

//    @Bind(R.id.buildingSlider)
//    RelativeLayout buildingSlider;
  boolean setting=false,Owner_detail=false;

   int backpress;

  private boolean gmap=false;
  GoogleMap map;


    String baseloc;

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

    private String description;
    private String heading;

    RelativeLayout.LayoutParams params;

    //DBHelper dbHelper;
    private FragmentDrawer drawerFragment;
    private WebView webView;
    LinearLayout dynamicContent,bottonNavBar;

    SharedPreferences.OnSharedPreferenceChangeListener listener;


    private BroadcastReceiver networkConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            networkConnectivity();

        }
    };


    /*private BroadcastReceiver ResetPhase = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("getstring","get string reset phase: "+intent.getExtras().getBoolean("resetphase"));
            if (intent.getExtras().getBoolean("resetphase")){
                ((BrokerPreokFragment)getSupportFragmentManager().findFragmentById(R.id.container_map)).resetSeekBar();
            }
        }
    };*/


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

    private BroadcastReceiver profileEmailUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("closer","closer ");
         updateEmail();
            /*Log.i("pikachu","ppppp1"+intent.getExtras().getString("emailProfile"));
            if(intent.getExtras().getString("emailProfile") != null){
                Log.i("pikachu","ppp111111"+intent.getExtras().getString("emailProfile"));
         String email=intent.getExtras().getString("emailProfile");
                emailTxt.setText(email);
            }*/
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
        //setContentView(R.layout.activity_agent_main);


         dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
//        NestedScrollView dynamicContent = (NestedScrollView) findViewById(R.id.myScrollingContent);
// assuming your Wizard content is in content_wizard.xml myScrollingContent
        View wizard = getLayoutInflater().inflate(R.layout.activity_agent_main, null);

// add the inflated View to the layout
        dynamicContent.addView(wizard);

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.matching);
        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_matching_clicked, 0,0);
       // rb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_select_matching) , null, null);
        rb.setTextColor(Color.parseColor("#2dc4b6"));
       // LinearLayout l_container=(LinearLayout)findViewById(R.id.);

        AppConstants.CURRENT_USER_ROLE ="broker";

       // RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT); //Parent Params
        //linearLayout.setLayoutParmas(params);
       // params = (LinearLayout.LayoutParams)dynamicContent.getLayoutParams();
    //  params.setMargins(0, 0, 0, 56);
        //params.setMargins(left, top, right, bottom);
        //dynamicContent.setLayoutParams(params);



       // dbHelper = new DBHelper(getBaseContext());
       // dbHelper.save(DatabaseConstants.userRole,"Broker");
        General.setSharedPreferences(this,AppConstants.ROLE_OF_USER,"broker");

        Log.i("uas","yo man 96");



        ButterKnife.bind(this);

        //tv_change_region=(TextView) findViewById(R.id.tv_change_region);
       // editBaseLocation=(ImageView) findViewById(R.id.editBaseLocation);
        setbaseloc.setVisibility(View.VISIBLE);
        //favbroker.setVisibility(View.VISIBLE);
        tv_change_region.setVisibility(View.VISIBLE);
        basewraper.setVisibility(View.VISIBLE);
        try {
            if(General.getSharedPreferences(getBaseContext(),AppConstants.MY_BASE_LOCATION).equalsIgnoreCase("") && !General.getSharedPreferences(this, AppConstants.LOCALITY).equalsIgnoreCase("")) {//tv_change_region.setText(General.getSharedPreferences(this, AppConstants.LOCALITY));
                AppConstants.MY_BASE_LOCATION_FLAG = true;

                Intent intent = new Intent(getBaseContext(), BrokerMap.class);
                startActivity(intent);
            }else {
//                AppConstants.BROKER_BASE_REGION="true";
                tv_change_region.setText(General.getSharedPreferences(getBaseContext(), AppConstants.MY_BASE_LOCATION));
            }
        }catch(Exception e){

        }

       // showCard();
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

       /* favbroker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getBaseContext(),MyPortfolioActivity.class);
                startActivity(in);
            }
        });*/

        init();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
       // ((BrokerPreokFragment)getSupportFragmentManager().findFragmentById(R.id.container_map)).resetSeekBar();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(networkConnectivity, new IntentFilter(AppConstants.NETWORK_CONNECTIVITY));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(localityBroadcast, new IntentFilter(AppConstants.LOCALITY_BROADCAST));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(buildingSliderFlag, new IntentFilter(AppConstants.BUILDINGSLIDERFLAG));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(profileEmailUpdate, new IntentFilter(AppConstants.EMAIL_PROFILE));
       // LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(ResetPhase, new IntentFilter(AppConstants.RESETPHASE));

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister since the activity is not visible
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(networkConnectivity);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(localityBroadcast);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(buildingSliderFlag);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(profileEmailUpdate);
       // LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(ResetPhase);


    }







    private void init() {
        slide_down = AnimationUtils.loadAnimation(this,
                R.anim.slide_down);
       // editBaseLocation.setVisibility(View.VISIBLE);

       try {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(this);
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                    if (key.equals(AppConstants.EMAIL)) {
                        emailTxt.setText(General.getSharedPreferences(BrokerMainActivity.this,AppConstants.EMAIL));
                    }else if (key.equals(AppConstants.MY_BASE_LOCATION)) {
                        tv_change_region.setText(General.getSharedPreferences(getBaseContext(),AppConstants.MY_BASE_LOCATION));
                    }/*else if (key.equals(AppConstants.RESETPHASE)) {
                        try {
                            ((BrokerPreokFragment) getSupportFragmentManager().findFragmentById(R.id.container_map)).resetSeekBar();
                        }
                        catch(Exception e){}
                        General.setSharedPreferences(BrokerMainActivity.this,AppConstants.RESETPHASE,"false");
                    }*/
                     if (key.equals(AppConstants.ADDB_COUNT_LL)) {

                        try {
                            if((General.getBadgeCount(BrokerMainActivity.this, AppConstants.ADDB_COUNT_LL)+General.getBadgeCount(BrokerMainActivity.this, AppConstants.ADDB_COUNT_OR)) == 0){
                                portfolioCount.setVisibility(View.GONE);
                            } else{

                                portfolioCount.setText((General.getBadgeCount(BrokerMainActivity.this, AppConstants.ADDB_COUNT_LL)+General.getBadgeCount(BrokerMainActivity.this, AppConstants.ADDB_COUNT_OR))+"");
                            portfolioCount.setVisibility(View.VISIBLE);
                            }

                            /*portfolioCount.setText((General.getBadgeCount(getContext(), AppConstants.ADDB_COUNT_LL)+General.getBadgeCount(getContext(), AppConstants.ADDB_COUNT_OR))+"");
                            portfolioCount.setVisibility(View.VISIBLE);*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    if (key.equals(AppConstants.ADDB_COUNT_OR)) {

                        try {
                            if((General.getBadgeCount(BrokerMainActivity.this, AppConstants.ADDB_COUNT_LL)+General.getBadgeCount(BrokerMainActivity.this, AppConstants.ADDB_COUNT_OR)) == 0){
                                portfolioCount.setVisibility(View.GONE);
                            }else{
                                portfolioCount.setText((General.getBadgeCount(BrokerMainActivity.this, AppConstants.ADDB_COUNT_LL)+General.getBadgeCount(BrokerMainActivity.this, AppConstants.ADDB_COUNT_OR))+"");
                                portfolioCount.setVisibility(View.VISIBLE);
                            }
                            /*portfolioCount.setText((General.getBadgeCount(getContext(), AppConstants.ADDB_COUNT_LL)+General.getBadgeCount(getContext(), AppConstants.ADDB_COUNT_OR))+"");
                            portfolioCount.setVisibility(View.VISIBLE);*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }


            };
            prefs.registerOnSharedPreferenceChangeListener(listener);

        }
        catch (Exception e){
            Log.e("loc", e.getMessage());
        }

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



        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);




        updateEmail();



//Remember

        setbaseloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*General.setSharedPreferences(getBaseContext(),AppConstants.CALLING_ACTIVITY,"BC");
                Intent intent = new Intent(getBaseContext(),ClientMainActivity.class);
                startActivity(intent);*/

                // baseloc=General.getSharedPreferences(getBaseContext(),AppConstants.MY_BASE_LOCATION);
                // General.setSharedPreferences(getBaseContext(), AppConstants.MY_BASE_LOCATION,"");
                AppConstants.MY_BASE_LOCATION_FLAG=true;
                Intent intent = new Intent(getBaseContext(),BrokerMap.class);
                startActivity(intent);

            }
        });
//

        /*doneButton.setOnClickListener(new View.OnClickListener() {
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
        });*/


        Bundle bundle = getIntent().getExtras();

        try {
            if (bundle != null) {
                if (bundle.containsKey("bicon")) {
                    description = bundle.getString("desc");
                    heading = bundle.getString("title");
                    Log.i("TRACE", " toto "+bundle.getString("bicon"));
                    Log.i("TRACE", " toto 1 "+bundle.getString("bicon"));
                    new DownloadImageTask().execute(bundle.getString("bicon"));
                }}}
        catch(Exception e){}


        /*if(General.getSharedPreferences(BrokerMainActivity.this,AppConstants.PROMO_IMAGE_URL) != "") {
            Log.i("TAG","porter 1 "+General.getSharedPreferences(BrokerMainActivity.this,AppConstants.PROMO_IMAGE_URL));
            new DownloadImageTask().execute(General.getSharedPreferences(BrokerMainActivity.this, AppConstants.PROMO_IMAGE_URL));
        }*/

        showPortfoliobadge();
    }

    @Override
    public void onPaymentSuccess(String s) {
        try {
            Log.i("RAzorpay","success "+s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Log.i("RAzorpay","failure"+s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            Log.i("TAG","stopDownloadImage3 yo bro porter 2 "+urls[0]);
            final String urldisplay = urls[0];
            Bitmap mIcon11 = General.getBitmapFromURL(urldisplay);
            return mIcon11;
        }

        protected void onPostExecute(final Bitmap result) {
            if(result != null) {
                Log.i("flok", "flokai 2 porter 3 "+result);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        General.showOptions(BrokerMainActivity.this,result, description, heading);
                        // General.setSharedPreferences(ClientMainActivity.this,AppConstants.PROMO_IMAGE_URL,"");

                    }

                }, 1000);
            }
        }
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
    public void onDrawerItemSelected(View view, int position, String itemTitle) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        if (itemTitle.equals(getString(R.string.useAsClient))) {
           // dbHelper = new DBHelper(getBaseContext());
            //dbHelper.save(DatabaseConstants.userRole,"client");
            General.setSharedPreferences(this,AppConstants.ROLE_OF_USER,"client");
            Intent openDashboardActivity =  new Intent(this, ClientMainActivity.class);
            openDashboardActivity.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
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
            if(General.getSharedPreferences(this,AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")){
                SignUpFragment signUpFragment = new SignUpFragment();
                // signUpFragment.getView().bringToFront();
                Bundle bundle = new Bundle();
                bundle.putStringArray("Chat", null);
                bundle.putString("lastFragment", "brokerDrawer");
                loadFragment(signUpFragment, bundle, R.id.container_sign, "");
            }
            else
                shareReferralLink();
        }
        else if (itemTitle.equals(getString(R.string.supportChat))) {
            //TODO: integration is pending
        }

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
            bundle.putString("lastFragment", "brokerDrawer");
            loadFragmentAnimated(signUpFragment, bundle, R.id.container_sign, "");
        }
        else if(itemTitle.equals(getString(R.string.shareNo))){
            ShareOwnersNo shareOwnersNo = new ShareOwnersNo();

            loadFragment(shareOwnersNo, null, R.id.container_sign, "");
            Owner_detail=true;
        }else if(itemTitle.equals(getString(R.string.Listing))){
            Log.i("myWatchList","itemTitle 1 "+itemTitle + R.string.Listing);
            Intent intent =new Intent( this,MyPortfolioActivity.class );
            startActivity(intent);
            /*MainScreenPropertyListing my_portfolio = new MainScreenPropertyListing();
            loadFragment(my_portfolio, null, R.id.container_Signup, "");
            Myportfolio=true;*/

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





    /*@Override
    public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        isopen = false;
    }

    *//** Called when a drawer has settled in a completely open state. *//*
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        isopen = true;
    }
    public void onDrawerSlide(View drawerView,float slideOffset){
        super.onDrawerSlide(drawerView,slideOffset);
        if(!isopen){
            setStatusBarColor("#00102b");
        }
        if(isopen){
            setStatusBarColor("#EFEFF0");
        }
    }*/



    private void shareReferralLink() {
        //DBHelper dbHelper=new DBHelper(getApplicationContext());
        //String user_id = dbHelper.getValue(DatabaseConstants.userId);
        String user_id = General.getSharedPreferences(this,AppConstants.USER_ID);

        Branch branch = Branch.getInstance(getApplicationContext());

        String mob_no = General.getSharedPreferences(this,AppConstants.MOBILE_NUMBER);
        Log.i("mob_no","mob_no "+mob_no);

        branch.setIdentity(mob_no);

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                .setTitle("OYEOK")
                .setContentDescription("Get property at right price. ")
                .addContentMetadata("user_id",user_id)
                .setCanonicalIdentifier(mob_no);


        LinkProperties linkProperties = new LinkProperties()
                .setChannel("android")
                .setFeature("share")
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

        if(AppConstants.cardNotif){
            AppConstants.cardNotif = false;
            AppConstants.optionspu1.dismiss();
            AppConstants.optionspu.dismiss();
        }
        else if(AppConstants.SIGNUP_FLAG){
           if(AppConstants.REGISTERING_FLAG){}else{
                //getSupportFragmentManager().popBackStackImmediate();
               getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
            AppConstants.SIGNUP_FLAG=false;
               //ShowBottomNavBar();
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

                backpress = 0;
                 }
        }
        else if(gmap ==true){
            Log.i("ONBACKPRESSED","broker main activity gmap"+getSupportFragmentManager().getBackStackEntryCount());
            Log.i("ONBACKPRESSED","broker main activity gmap"+getSupportFragmentManager().getBackStackEntryCount());
            Intent inten = new Intent(this, BrokerMainActivity.class);
            startActivity(inten);
            finish();
            gmap =false;
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
            if(SharedPrefs.getString(this, SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true") || SharedPrefs.getString(this, SharedPrefs.CHECK_BEACON).equalsIgnoreCase("true")){
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
                Log.i("SIGNUP_FLAG", "SIGNUP_FLAG=========  loadFragment setting client4 " + getFragmentManager().getBackStackEntryCount());
                setting = false;
                backpress = 0;
            }

        }
        else if(Owner_detail==true){
            getSupportFragmentManager().popBackStackImmediate();
            Owner_detail=false;
            backpress = 0;
        }

        else if(!General.getSharedPreferences(this,AppConstants.IS_LOGGED_IN_USER).equals("")) {
            Log.i("SIGNUP_FLAG", " closing app =================== 3" + getFragmentManager().getBackStackEntryCount());
            if (backpress < 1) {
                backpress = (backpress + 1);
                TastyToast.makeText(this, "Press Back again to Exit!", TastyToast.LENGTH_LONG, TastyToast.INFO);
            } else if (backpress >= 1) {
                backpress = 0;
                this.finish();
            }
        }
        else{

//            if(backpress <1) {
//                backpress = (backpress + 1);
//
//                Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
//            }else if (backpress>=1) {
//                backpress = 0;
                /*this.finish();*/
//            }
            //dbHelper = new DBHelper(getBaseContext());
           // dbHelper.save(DatabaseConstants.userRole,"client");
            General.setSharedPreferences(this,AppConstants.ROLE_OF_USER,"client");
            Intent inten = new Intent(this, ClientMainActivity.class);
            inten.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(inten);
            finish();

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



    public void updateEmail(){
        Log.i("closer","closer 1 ");
        if (!General.getSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
            Log.i("closer","closer 2 "+General.getSharedPreferences(this,AppConstants.EMAIL));
            //if (!dbHelper.getValue(DatabaseConstants.email).equalsIgnoreCase("null")) {
            if (!General.getSharedPreferences(this,AppConstants.EMAIL).equalsIgnoreCase("null")) {
                emailTxt.setVisibility(View.VISIBLE);
                emailTxt.setText(General.getSharedPreferences(this,AppConstants.EMAIL));


            }
        }else{
            emailTxt.setVisibility(View.INVISIBLE);
        }

    }


    public void openAddListing(){
        AddListing addBuildingCardView = new AddListing();
        loadFragmentAnimated(addBuildingCardView, null, R.id.container_map, "");
    }

    public void openAddBuilding(){
        AddBuilding addBuilding= new AddBuilding();
        loadFragmentAnimated(addBuilding, null, R.id.container_map, "");

    }

    public void openAddListingFinalCard(){
        AddListingFinalCard addListingFinalCard= new AddListingFinalCard();
        loadFragmentAnimated(addListingFinalCard, null, R.id.container_map, "");

    }
    private void showPortfoliobadge(){
        if (General.getBadgeCount(this, AppConstants.ADDB_COUNT_LL) > 0 || General.getBadgeCount(this, AppConstants.ADDB_COUNT_OR) > 0 ) {

            portfolioCount.setText((General.getBadgeCount(this, AppConstants.ADDB_COUNT_LL)+General.getBadgeCount(this, AppConstants.ADDB_COUNT_OR))+"");
            portfolioCount.setVisibility(View.VISIBLE);
        }
        else{
            portfolioCount.setVisibility(View.GONE);
        }
    }

    public void showCard() {

       /* if (General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("") && General.getSharedPreferences(this, AppConstants.STOP_CARD).equalsIgnoreCase("") &&  !General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {

            if (AppConstants.cardCounter >3) {*/

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        cardb.setBackgroundColor(Color.parseColor("#CC000000"));
                        cardb.setClickable(true);
                        //containerSignup.setBackgroundColor(getResources().getColor(R.color.transparent));
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);


                        PartnerBrokerFragment c = new PartnerBrokerFragment();
                        //loadFragment(d,null,R.id.container_Signup,"");
                        c.setArguments(null);
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
                        card.setClickable(true);
                        fragmentTransaction.addToBackStack("card");
                        fragmentTransaction.replace(R.id.card, c);
                        fragmentTransaction.commitAllowingStateLoss();

                        AppConstants.cardCounter = 0;

                    }

                }, 500);
            /*}
        }*/
    }

    /*public void HideBottomNavBar(){
        Log.i("HideBottomNavBar","HideBottomNavBar");
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
        dynamicContent.setLayoutParams(params);
        Log.i("HideBottomNavBar","HideBottomNavBar"+dynamicContent.getBottom());
        bottonNavBar.setVisibility(View.GONE);
    }
    public void ShowBottomNavBar(){
        Log.i("HideBottomNavBar","HideBottomNavBar"+dynamicContent.getBottom());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0,156);
        dynamicContent.setLayoutParams(params);
        bottonNavBar.setVisibility(View.VISIBLE);
    }*/

}
