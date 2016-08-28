package com.nbourses.oyeok.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.fragments.AppSetting;
import com.nbourses.oyeok.fragments.BuildingOyeConfirmation;
import com.nbourses.oyeok.fragments.DashboardClientFragment;
import com.nbourses.oyeok.fragments.OyeConfirmation;
import com.nbourses.oyeok.fragments.OyeScreenFragment;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.helpers.NetworkInterface;
import com.nbourses.oyeok.interfaces.OnOyeClick;
import com.nbourses.oyeok.models.PublishLetsOye;
import com.nbourses.oyeok.services.DeviceRegisterService;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import me.leolin.shortcutbadger.ShortcutBadger;

public class ClientMainActivity extends AppCompatActivity implements NetworkInterface, FragmentDrawer.FragmentDrawerListener, OnOyeClick {

    boolean isShowing = false;
    private static final String TAG = "DashboardActivity";
    final float anchorPoint = 0.4f;
   GoogleMap googlemap;
    DBHelper dbHelper;
    private Handler mHandler;
    private FragmentDrawer drawerFragment;

    @Bind(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;

    @Bind(R.id.sliding_view)
    ScrollView slidingView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
  private   int   backpress=0;
   boolean setting=false,buidingInfoFlag=false;
    TextView tv_client_heading;

    @Bind(R.id.profile_image_main)
    ImageView profileImage;

    @Bind(R.id.txtEmail)
    TextView emailTxt;


    @Bind(R.id.toast_text)
    TextView toastText;
    @Bind(R.id.btnMyDeals)
    Button btnMyDeals;
    @Bind(R.id.dealsWrapper)
    RelativeLayout dealsWrapper;
    @Bind(R.id.cancel_btn)
    TextView cancel_btn;
    @Bind(R.id.confirm_screen_title)
    TextView confirm_screen_title;


    @Bind(R.id.toast_layout)
    LinearLayout toastLayout;

    Bundle bundle_args;

    @Bind(R.id.hdroomsCount)
    TextView hdroomsCount;
    String PossessionDate,Furnishing,my_expectation,Property_Config;

    /*@Bind(R.id.tv_dealinfo)
    TextView tv_dealinfo;*/





    private WebView webView;
    private  Boolean autocomplete = false,oyeconfirm_flag=false;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    public interface openMapsClicked{
        public void clicked();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            hideToastMessage();
        }
    };

    private BroadcastReceiver closeOyeScreenSlide = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            closeOyeScreen();
        }
    };

    private BroadcastReceiver oyebuttondata = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getExtras().getString("isclicked")=="true") {
                Boolean s = General.retriveBoolean(getBaseContext(), "propertySubtypeFlag");

                if (General.getSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
                    Log.i("TRACE", "clicked oyebutton if");
                    //show ƒlo up screen
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("propertySpecification", null);
                    bundle.putString("lastFragment", "OyeIntentSpecs");

                    if (s.equals(false)) {
                        SnackbarManager.show(
                                Snackbar.with(getBaseContext())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text("Please select property subtype")
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                    } else {
                        if (slidingLayout != null &&
                                (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                                        slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                            closeOyeScreen();

                        }
//                        OyeConfirmation  oyeConfirmation=new OyeConfirmation();
//                        loadFragment(oyeConfirmation, null, R.id.container_OyeConfirmation, "");
//                        oyeconfirm_flag=true;

                        SignUpFragment signUpFragment = new SignUpFragment();
                        loadFragment(signUpFragment, bundle, R.id.container_Signup, "");
                        Log.i("Signup called =", "Sign up");
                        // btnOnOyeClick.setVisibility(View.GONE);
                    }
                } else {
                    Log.i("already", "Signed up");
//                    OyeConfirmation  oyeConfirmation=new OyeConfirmation();
//                    loadFragment(oyeConfirmation, null, R.id.container_OyeConfirmation, "");
                    if (s.equals(false)) {
                        SnackbarManager.show(
                                Snackbar.with(getBaseContext())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text("Please select property subtype")
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

                    } else {
                        //create new deal
                        OyeConfirmation  oyeConfirmation1=new OyeConfirmation( );
                        confirm_screen_title.setVisibility(View.VISIBLE);
                        getSupportActionBar().setDisplayShowHomeEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        cancel_btn.setVisibility(View.VISIBLE);
                        getSupportActionBar().setTitle("");
                        if(AppConstants.CURRENT_DEAL_TYPE.equalsIgnoreCase("rent")){

                            if(AppConstants.CUSTOMER_TYPE.equalsIgnoreCase("Owner"))
                            confirm_screen_title.setText("Posting Confirmation\n(I am Owner)");
                            else
                                confirm_screen_title.setText("Posting Confirmation \n(I am Tenant)");


                        }else
                            {

                            if (AppConstants.CUSTOMER_TYPE.equalsIgnoreCase("Owner"))
                                confirm_screen_title.setText("Posting Confirmation\n(I am Owner)");
                            else
                                confirm_screen_title.setText("Posting Confirmation\n(I am Tenant)");
                            }
                        loadFragment(oyeConfirmation1, null, R.id.container_OyeConfirmation, "");
                        dealsWrapper.setVisibility(View.GONE);
                        ((DashboardClientFragment) getSupportFragmentManager().findFragmentById(R.id.container_map)).getNearbyLatLong();
                        ((DashboardClientFragment) getSupportFragmentManager().findFragmentById(R.id.container_map)).broadcastingConfirmationMsg();

                        oyeconfirm_flag=true;
                        if (slidingLayout != null &&
                                (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                                        slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                            closeOyeScreen();

                        }
//                        alertbuilder();




                    }
                }


            }




        }
    };

    private BroadcastReceiver profileEmailUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("kaka","kaka  kaka     :");
            Log.i("pikachu","ppppp1"+intent.getExtras().getString("emailProfile"));
            if(intent.getExtras().getString("emailProfile") != null){
                Log.i("pikachu","ppp111111"+intent.getExtras().getString("emailProfile"));
                String email=intent.getExtras().getString("emailProfile");
                emailTxt.setText(email);
            }
        }
    };
    private BroadcastReceiver autoComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getExtras().getBoolean("autocomplete")==true) {
                autocomplete = true;
                Log.i(TAG,"hohohoh 1 "+autocomplete);

            }
        }
    };

    private BroadcastReceiver networkConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            networkConnectivity();

        }
    };


private void alertbuilder()

{
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Do you want to publish this oye?")
            .setCancelable(true)
            .setPositiveButton("Publish", new DialogInterface.OnClickListener() {
                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                    General.publishOye(getBaseContext());
                    closeOyeScreen();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                    dialog.cancel();
                }
            });
    final AlertDialog alert = builder.create();
    alert.show();

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check status of Google Play Services
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

// Check Google Play Service Available
        try {
            if (status != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(status, this, 2).show();
            }
        } catch (Exception e) {
            Log.i("GooglePlayServiceUtil: ", "" + e.getMessage());
        }

        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        AppConstants.CURRENT_USER_ROLE ="client";
        ShortcutBadger.removeCount(this);

//        lintent=getIntent();
//        String txt=lintent.getStringExtra("client_heading");
      //  getSupportActionBar().setTitle(txt);

       //
       // recIntent.getStringExtra("key");
        if (General.isNetworkAvailable(getApplicationContext())) {

            Log.i("TRACE", "network available");
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

        //Hardcode user login in shared prefs
        //General.settSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER, yes);
//       General.setSharedPreferences(this,AppConstants.IS_LOGGED_IN_USER, "yes");
        init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(closeOyeScreenSlide, new IntentFilter(AppConstants.CLOSE_OYE_SCREEN_SLIDE));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(oyebuttondata, new IntentFilter(AppConstants.ON_FILTER_VALUE_UPDATE));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(networkConnectivity, new IntentFilter(AppConstants.NETWORK_CONNECTIVITY));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(markerstatus, new IntentFilter(AppConstants.MARKERSELECTED));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(autoComplete, new IntentFilter(AppConstants.AUTOCOMPLETEFLAG));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(profileEmailUpdate, new IntentFilter(AppConstants.EMAIL_PROFILE));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(SetPropertyDetails,new IntentFilter((AppConstants.BROADCAST_PROPERTY_DETAILS)));


    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(closeOyeScreenSlide);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(oyebuttondata);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(networkConnectivity);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(markerstatus);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(autoComplete);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(profileEmailUpdate);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(SetPropertyDetails);

    }


    /**
     * init all components
     */
    private void init() {


//        RealmConfiguration config = new RealmConfiguration
//                .Builder(this)
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        Realm myRealm = Realm.getInstance(config);



//        myRealm.beginTransaction();
//
//        // Create an object
//        Country country1 = myRealm.createObject(Country.class);
//
//        // Set its fields
//        country1.setName("Norway");
//        country1.setPopulation(5165800);
//
//        myRealm.commitTransaction();
//
//
//        Country country2 = new Country();
//        country2.setName("Russia");
//        country2.setPopulation(146430430);
//
//
//        myRealm.beginTransaction();
//        Country copyOfCountry2 = myRealm.copyToRealm(country2);
//        myRealm.commitTransaction();



//        RealmResults<Country> results1 =
//                myRealm.where(Country.class).findAll();
//
//        for(Country c:results1) {
//            Log.d("results2", c.getName());
//        }


//        myRealm.beginTransaction();
//        UserInfo user = myRealm.createObject(UserInfo.class);
//       // UserInfo user = new UserInfo();
//        user.setName("Rapp");
//        user.setMobileNumber("146430430");
//
//
//
//        UserInfo copyOfCountry = myRealm.copyToRealmOrUpdate(user);
//        myRealm.commitTransaction();



//        UserInfo usera = new UserInfo();
//        usera.setName("Rapter");
//        usera.setMobileNumber("1464304308");
//
//
//        myRealm.beginTransaction();
//        UserInfo copyOfCountry2 = myRealm.copyToRealmOrUpdate(usera);
//        myRealm.commitTransaction();
//
//        RealmResults<UserInfo> results4 =
//                myRealm.where(UserInfo.class).findAll();
//        Log.i(TAG,"insider1 ");
//        for(UserInfo c:results4) {
////            Log.i(TAG,"insider2 ");
////            Log.i(TAG,"insider3 "+c.getName());
////            Log.i(TAG,"insider4 "+c.getEmailId());
//        }



        if(General.getBadgeCount(this,AppConstants.HDROOMS_COUNT)<=0)
            hdroomsCount.setVisibility(View.GONE);
        else {
            hdroomsCount.setVisibility(View.VISIBLE);
            hdroomsCount.setText(String.valueOf(General.getBadgeCount(this, AppConstants.HDROOMS_COUNT)));
        }


        try {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(this);

            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    if (key.equals(AppConstants.HDROOMS_COUNT)) {
                        if (General.getBadgeCount(getApplicationContext(), AppConstants.HDROOMS_COUNT) <= 0)
                            hdroomsCount.setVisibility(View.GONE);
                        else {
                            hdroomsCount.setVisibility(View.VISIBLE);
                            hdroomsCount.setText(String.valueOf(General.getBadgeCount(getApplicationContext(), AppConstants.HDROOMS_COUNT)));


                        }


                    }
                }
            };

            prefs.registerOnSharedPreferenceChangeListener(listener);
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        //You need to set the Android context using Firebase.setAndroidContext() before using Firebase.
        Firebase.setAndroidContext(this);

        //gcm implementation
        Intent intent = new Intent(this, DeviceRegisterService.class);
        startService(intent);

        //setup sliding layout
        slidingLayout.setTouchEnabled(false);
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        slidingLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
//                resizeScrollView(panel, slideOffset);
            }

            @Override
            public void onPanelExpanded(View panel) {
//                resizeScrollView(panel, 0.0f);
            }

            @Override
            public void onPanelCollapsed(View panel) {
            }

            @Override
            public void onPanelAnchored(View panel) {
//                resizeScrollView(panel, anchorPoint);
            }

            @Override
            public void onPanelHidden(View panel) {
            }

//            private void resizeScrollView(View panel, float slideOffset) {
//                final int scrollViewHeight =
//                        (int) ((panel.getHeight() - slidingLayout.getPanelHeight()) * (1.0f - slideOffset));
//                slidingView.setLayoutParams(
//                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                                scrollViewHeight));
//            }
        });

      //  RelativeLayout re = (RelativeLayout) findViewById(R.id.badge);
        //setup toolbar
        setSupportActionBar(mToolbar);
       getSupportActionBar().setDisplayShowHomeEnabled(true);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  //      mToolbar.setNavigationIcon(R.drawable.home);
      getSupportActionBar().setTitle("Live Region Rates");

    //    getSupportActionBar().setIcon(R.drawable.ic_launcher); // or setLogo()
    //    getSupportActionBar().setLogo(R.drawable.industry);

//        ActionBar actionbar = getSupportActionBar ();
//        actionbar.setDisplayHomeAsUpEnabled(true);
//        actionbar.setHomeAsUpIndicator(R.drawable.home);


//        mToolbar.setNavigationIcon(R.drawable.home);
//        mToolbar.setTitle("Title");
//        mToolbar.setSubtitle("Sub");
//        mToolbar.setLogo(R.drawable.ic_launcher);


   //  getSupportActionBar().setHomeAsUpIndicator(R.drawable.shop);
//        if (Build.VERSION.SDK_INT >= 18) {
//            getSupportActionBar().setHomeAsUpIndicator(
//                    getResources().getDrawable(R.drawable.home));
//        }






        //TODO: need to validate this functionality
        dbHelper = new DBHelper(getBaseContext());
        mHandler = new Handler();

        dbHelper.save(DatabaseConstants.userRole,"Client");

        //setup navigation drawer
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
//        if(!dbHelper.getValue(DatabaseConstants.imageFilePath).equalsIgnoreCase("null")) {
//            Bitmap yourSelectedImage = BitmapFactory.decodeFile(dbHelper.getValue(DatabaseConstants.imageFilePath));
//            profileImage.setImageBitmap(yourSelectedImage);
//
//
//        }

        if (!General.getSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
            //if (!dbHelper.getValue(DatabaseConstants.email).equalsIgnoreCase("null")) {
            if (!General.getSharedPreferences(this,AppConstants.EMAIL).equalsIgnoreCase("null")) {
                emailTxt.setVisibility(View.VISIBLE);
                emailTxt.setText(General.getSharedPreferences(this,AppConstants.EMAIL));
                Log.i(TAG,"emailsa "+General.getSharedPreferences(this,AppConstants.EMAIL));

            }
        }else{
            emailTxt.setVisibility(View.INVISIBLE);
        }









        //by default load broker_map view
        DashboardClientFragment dashboardClientFragment = new DashboardClientFragment();
        dashboardClientFragment.setOyeButtonClickListener(this);
        loadFragment(dashboardClientFragment, null, R.id.container_map, "Client Dashboard");
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
        fragmentTransaction.addToBackStack(title);
        Log.i("SIGNUP_FLAG","SIGNUP_FLAG=========  loadFragment client "+getFragmentManager().getBackStackEntryCount());
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();

        //set title
//        getSupportActionBar().setTitle(title);
    }

    public void onClickButton(Bundle args) {
        if (!isShowing) {

            bundle_args=args;
            isShowing = true;
//            OyeIntentFragment oye = new OyeIntentFragment();

            //reset PublishLetsOye object
            AppConstants.letsOye = new PublishLetsOye();

            OyeScreenFragment oye = new OyeScreenFragment();
            loadFragment(oye, args, R.id.container_oye, "");
            slidingLayout.setAnchorPoint(0.5f);
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

           // btnOnOyeClick.setVisibility(View.VISIBLE);
        }
        else {
            closeOyeScreen();
        }
    }




    public  void EditOyeDetails(){
        getSupportFragmentManager().popBackStack();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cancel_btn.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Live Region Rates");
        confirm_screen_title.setVisibility(View.GONE);
        dealsWrapper.setVisibility(View.VISIBLE);
        oyeconfirm_flag=false;
        ((DashboardClientFragment) getSupportFragmentManager().findFragmentById(R.id.container_map)).OnOyeClick();

    }
    public void openOyeSreen(){
        if (!isShowing) {


            isShowing = true;
//            OyeIntentFragment oye = new OyeIntentFragment();

            //reset PublishLetsOye object
            AppConstants.letsOye = new PublishLetsOye();

            OyeScreenFragment oye = new OyeScreenFragment();
            loadFragment(oye, bundle_args, R.id.container_oye, "");
            slidingLayout.setAnchorPoint(0.5f);
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

            // btnOnOyeClick.setVisibility(View.VISIBLE);
        }
    }


    public  void closeOyeConfirmation(){
        getSupportFragmentManager().popBackStack();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cancel_btn.setVisibility(View.GONE);
        ((DashboardClientFragment) getSupportFragmentManager().findFragmentById(R.id.container_map)).getPrice();
        getSupportActionBar().setTitle("Live Region Rates");
        confirm_screen_title.setVisibility(View.GONE);
        dealsWrapper.setVisibility(View.VISIBLE);
        oyeconfirm_flag=false;
    }
    public void closeOyeScreen() {
        isShowing = false;
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
       // btnOnOyeClick.setVisibility(View.GONE);
    }

    @Override
    public void onDrawerItemSelected(View view, int position, String itemTitle) {
        Fragment fragment = null;
        Fragment frag = null;
        String title = getString(R.string.app_name);

        if (itemTitle.equals(getString(R.string.useAsClient))) {
            //don't do anything
        }
        else if (itemTitle.equals(getString(R.string.profile))) {
            Intent openProfileActivity =  new Intent(this, ProfileActivity.class);
            startActivity(openProfileActivity);
        }
        else if (itemTitle.equals(getString(R.string.brokerOk))) {
            Intent openDashboardActivity =  new Intent(this, BrokerMainActivity.class);
            startActivity(openDashboardActivity);
        }
        else if (itemTitle.equals(getString(R.string.shareApp))) {
            shareReferralLink();
        }
   /*     else if (itemTitle.equals(getString(R.string.supportChat))) {
            //TODO: integration is pending
        } */

        else if (itemTitle.equals(getString(R.string.notifications))) {
            AppConstants.CLIENT_DEAL_FLAG = true;
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
            webView.setWebViewClient(new WebViewClient());

            webView.loadUrl("http://www.hioyeok.com/blog");


        }
        else if (itemTitle.equals(getString(R.string.updateProfile))) {
            //setContentView(R.layout.browser);
            loadFragment(frag, null, R.id.container_map, title);
        }
        else if (itemTitle.equals(getString(R.string.settings))) {
            AppSetting appSetting=new AppSetting();
            loadFragment(appSetting,null,R.id.container_Signup,"");
            setting=true;


        }
        else if (itemTitle.equals(getString(R.string.RegisterSignIn))) {
            SignUpFragment signUpFragment = new SignUpFragment();
            // signUpFragment.getView().bringToFront();
            Bundle bundle = new Bundle();
            bundle.putStringArray("Chat", null);
            bundle.putString("lastFragment", "drawer");
            loadFragment(signUpFragment, bundle, R.id.container_Signup, "");



        }


//        if (fragment != null) {
//            loadFragment(fragment, null, R.id.container_map, title);
//        }
    }

    private void shareReferralLink() {
        DBHelper dbHelper=new DBHelper(getApplicationContext());
        String user_id = dbHelper.getValue(DatabaseConstants.userId);

        Branch branch = Branch.getInstance(getApplicationContext());

        String mob_no = General.getSharedPreferences(this,AppConstants.MOBILE_NUMBER);
        Log.i("mob_no","mob_no "+mob_no +user_id );

        branch.setIdentity(mob_no);

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                .setCanonicalIdentifier(user_id);


        branchUniversalObject.registerView();

        LinkProperties linkProperties = new LinkProperties()
                .setChannel("sms")
                .setFeature("sharing")
                .addControlParameter("user_name", user_id)
                .addControlParameter("$android_url", AppConstants.GOOGLE_PLAY_STORE_APP_URL)
                .addControlParameter("$always_deeplink", "true");

        branchUniversalObject.generateShortUrl(getApplicationContext(), linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                Log.i("mob_no url","mob_no url " +url );
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

    public void setTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerFragment.handle(item))
        {
            return true;
        }
        switch (item.getItemId()) {

            case android.R.id.home:
                //Do stuff
                //Toast.makeText(this,"getscalled",Toast.LENGTH_LONG).show();
                if(dbHelper.getValue(DatabaseConstants.user).equals("Broker"))
                {
//                    changeFragment(new Ok_Broker_MainScreen(), null, "MarkerPanel");
                }else {
//                    changeFragment(new RexMarkerPanelScreen(), null, "MarkerPanel");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showToastMessage(String message) {
        toastText.setText(message);
        toastLayout.setVisibility(View.VISIBLE);
        hideMap(1);
        mHandler.postDelayed(mStatusChecker, 2000);
    }

    public void hideToastMessage(){
        hideMap(0);
        toastLayout.setVisibility(View.INVISIBLE);
    }

    private void hideMap(int i) {

        Animation m = null;

        //Load animation
        if(i==0) {
            m = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        }else {
            m = AnimationUtils.loadAnimation(this, R.anim.slide_down_toast_layout);
        }
        toastLayout.setAnimation(m);
    }



    /*@OnClick(R.id.btnOnOyeClick)
    public void submitOyeOk(View v) {
        Log.i("TRACE", "oyebutton");
        Boolean s = General.retriveBoolean(this, "propertySubtypeFlag");

        if (General.getSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
            Log.i("TRACE", "clicked oyebutton if");
            //show ƒlo up screen
            Bundle bundle = new Bundle();
            bundle.putStringArray("propertySpecification", null);
            bundle.putString("lastFragment", "OyeIntentSpecs");

            if(s.equals(false)){
                SnackbarManager.show(
                        Snackbar.with(this)
                                .position(Snackbar.SnackbarPosition.TOP)
                                .text("Please select property subtype")
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
            }else {

                    SignUpFragment signUpFragment = new SignUpFragment();
                    loadFragment(signUpFragment, bundle, R.id.container_oye, "");
                    Log.i("Signup called =", "Sign up");
                    btnOnOyeClick.setVisibility(View.GONE);
            }
        }
        else {
            Log.i("already", "Signed up");
            if(s.equals(false)){
                SnackbarManager.show(
                        Snackbar.with(this)
                                .position(Snackbar.SnackbarPosition.TOP)
                                .text("Please select property subtype")
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

            }else {
                //create new deal

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Dou you want to publish this oye?")
                        .setCancelable(true)
                        .setPositiveButton("Publish", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                General.publishOye(getApplicationContext());
                                closeOyeScreen();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();




            }
        }
    }*/

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//        ((DashboardClientFragment) getSupportFragmentManager().findFragmentById(R.id.container_map)).getNearbyLatLong();

        if(buidingInfoFlag==true)
        {
            ((DashboardClientFragment) getSupportFragmentManager().findFragmentById(R.id.container_map)).onMapclicked();
//            buidingInfoFlag=false;
           // CloseBuildingOyeComfirmation();

        }else

        if(oyeconfirm_flag==true){
//                super.onBackPressed();
            Log.i("SIGNUP_FLAG","Poke Poke Pokemon......: "+getFragmentManager().getBackStackEntryCount());
//            getSupportFragmentManager().popBackStack();
            closeOyeConfirmation();

//            oyeconfirm_flag=false;
            backpress = 0;
        }else
        if(AppConstants.SIGNUP_FLAG){
/*            if(dbHelper.getValue(DatabaseConstants.userRole).equalsIgnoreCase("broker")){
            Intent back = new Intent(this, BrokerMainActivity.class);
            startActivity(back);
            }
            else{
                Intent back = new Intent(this, ClientMainActivity.class);
                startActivity(back);
            }
            finish();*/
            if(AppConstants.REGISTERING_FLAG){}else{
            getSupportFragmentManager().popBackStack();

            AppConstants.SIGNUP_FLAG=false;
            backpress = 0;}
            Log.i("SIGNUP_FLAG"," main activity =================== SIGNUP_FLAGffffffff");

        }


       else if(autocomplete){

            Log.i("BACK PRESSED","  autocomplete"+autocomplete);
//
            Intent intentt = new Intent(AppConstants.AUTOCOMPLETEFLAG1);
            intentt.putExtra("autocomplete",true);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentt);
            autocomplete=false;
            backpress = 0;

        }

       else if(setting==true){


            Log.i("BACK PRESSED"," =================== setting"+setting);
            if(SharedPrefs.getString(this, SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true") || SharedPrefs.getString(this, SharedPrefs.CHECK_BEACON).equalsIgnoreCase("true")){
                Intent inten = new Intent(this, ClientMainActivity.class);
                inten.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inten);
                finish();
                super.onBackPressed();
                finish();
                Log.i("SIGNUP_FLAG", "SIGNUP_FLAG=========  loadFragment setting client4 " + getFragmentManager().getBackStackEntryCount());
                setting = false;
                backpress = 0;


            }else {
                super.onBackPressed();
                Log.i("SIGNUP_FLAG", "SIGNUP_FLAG=========  loadFragment setting client4 " + getFragmentManager().getBackStackEntryCount());
                setting = false;
                backpress = 0;
            }

        }

        else if(webView != null){
            if (webView.canGoBack()) {
                webView.goBack();
            }else {

                Log.i("SIGNUP_FLAG", " webView =================== 3");
                Intent inten = new Intent(this, ClientMainActivity.class);
                inten.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inten);
                finish();
                backpress = 0;
            }
        } else if (slidingLayout != null && (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            closeOyeScreen();
            backpress = 0;

        } else{

            Log.i("SIGNUP_FLAG"," closing app =================== 3"+getFragmentManager().getBackStackEntryCount());
            if(backpress <1) {
                backpress = (backpress + 1);
                Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
            }else if (backpress>=1) {
                backpress = 0;
                this.finish();
            }

        }





    }



    @OnClick(R.id.btnMyDeals)
    public void onBtnMyDealsClick(View v) {
        if (General.getBadgeCount(this, AppConstants.HDROOMS_COUNT) > 0) {
            General.setBadgeCount(this, AppConstants.HDROOMS_COUNT,0);
            hdroomsCount.setVisibility(View.GONE);
        }
        Intent openDealsListing = new Intent(this, ClientDealsListActivity.class);
        openDealsListing.putExtra("defaul_deal_flag","false");
        startActivity(openDealsListing);
    }
@OnClick(R.id.cancel_btn)
public void oncancelBtnClick(){

    if(buidingInfoFlag==true) {
        CloseBuildingOyeComfirmation();
        ((DashboardClientFragment) getSupportFragmentManager().findFragmentById(R.id.container_map)).onMapclicked();
    }
    else
        closeOyeConfirmation();
}
    ////////////////// Network method implementation //////////

    public void NetworkStatusChanged(String status)
    {
        Log.i("TRACE NETWORK","status ");
    }

    private  void networkConnectivity(){
        SnackbarManager.show(
                Snackbar.with(this)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .text("INTERNET CONNECTIVITY NOT AVAILABLE")
                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
    }





    private BroadcastReceiver markerstatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("markerstatus","markerstatus  :"+intent.getExtras().getString("markerClicked"));
           if(intent.getExtras().getString("markerClicked").equalsIgnoreCase("true"))
           {
               getSupportActionBar().setTitle("Live Building Rates");
           }
            else{
               getSupportActionBar().setTitle("Live Region Rates");
           }
        }
    };



//    public void Wlak_Beacon() throws InterruptedException {
//        DashboardClientFragment DashboardClient = new DashboardClientFragment();
//
//        if (SharedPrefs.getString(this, SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true") && SharedPrefs.getString(this, SharedPrefs.CHECK_BEACON).equalsIgnoreCase("true")) {
//
//            DashboardClient.beaconAlert();
//            DashboardClient.tutorialAlert();
//
//
//        }else if (SharedPrefs.getString(this, SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true")){
//            DashboardClient.tutorialAlert();
//        }else{
//            DashboardClient.beaconAlert();
//        }
//
//
//    }
private BroadcastReceiver SetPropertyDetails = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        my_expectation= intent.getExtras().getString("myExpectation").toString();
        Log.i("confirmation","myexpectation"+my_expectation+"  "+intent.getExtras().getString("myExpectation").toString());
        Property_Config= intent.getExtras().getString("propertyConfig");
        Log.i("confirmation","PropertyConfig : "+Property_Config);
        PossessionDate= intent.getExtras().getString("possessionDate");
        Log.i("confirmation","PossessionDate : "+PossessionDate);
        Furnishing=intent.getExtras().getString("furnishing");
        Log.i("confirmation","Furnishing"+Furnishing);


        Intent intent1 = new Intent(AppConstants.RECEIVE_PROPERTY_DETAILS);
        intent1.putExtra("propertyConfig1", intent.getExtras().getString("myExpectation").toString());
        intent1.putExtra("furnishing1", intent.getExtras().getString("propertyConfig"));
        intent1.putExtra("possessionDate1", intent.getExtras().getString("possessionDate"));
        intent1.putExtra("myExpectation1", intent.getExtras().getString("furnishing"));

        Log.i("confirmation","Furnishing       ;"+Furnishing);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);



//        OyeConfirmation  oyeConfirmation1=new OyeConfirmation( );
//        Bundle args = new Bundle();
//        args.putString("my", "sushil");
//        args.putString("PropertyConfig", Property_Config);
//        args.putString("possessionDate", PossessionDate);
//        args.putString("furnishing", Furnishing);
//        oyeConfirmation1.setArguments(args);



    }
};


public  void OpenBuildingOyeConfirmation(){
    buidingInfoFlag=true;

    confirm_screen_title.setVisibility(View.VISIBLE);
    getSupportActionBar().setDisplayShowHomeEnabled(false);
    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    cancel_btn.setVisibility(View.VISIBLE);
    getSupportActionBar().setTitle("");
    if(AppConstants.CURRENT_DEAL_TYPE.equalsIgnoreCase("rent")){

            confirm_screen_title.setText("Live Building Rates \n(Rent)");


    }else
    {


            confirm_screen_title.setText("Live Building Rates \n" + "(Buy/Sell)");

    }
    BuildingOyeConfirmation buildingOyeConfirmation = new BuildingOyeConfirmation();
    loadFragment(buildingOyeConfirmation, null, R.id.container_OyeConfirmation, "");
}


public  void CloseBuildingOyeComfirmation(){
    confirm_screen_title.setVisibility(View.GONE);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    cancel_btn.setVisibility(View.GONE);
    getSupportActionBar().setTitle("Live Building Rates");
   if( buidingInfoFlag==true)
        getSupportFragmentManager().popBackStack();
   buidingInfoFlag=false;
    }






}

