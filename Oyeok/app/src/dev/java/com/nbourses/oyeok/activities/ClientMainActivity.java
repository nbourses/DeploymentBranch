package com.nbourses.oyeok.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.fragments.AppSetting;
import com.nbourses.oyeok.fragments.BrokerMap;
import com.nbourses.oyeok.fragments.DashboardClientFragment;
import com.nbourses.oyeok.fragments.OyeScreenFragment;
import com.nbourses.oyeok.fragments.ShareOwnersNo;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.helpers.NetworkInterface;
import com.nbourses.oyeok.interfaces.OnOyeClick;
import com.nbourses.oyeok.models.PublishLetsOye;
import com.nbourses.oyeok.services.DeviceRegisterService;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.sdsmdg.tastytoast.TastyToast;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import me.leolin.shortcutbadger.ShortcutBadger;

public class ClientMainActivity extends AppCompatActivity implements NetworkInterface, FragmentDrawer.FragmentDrawerListener, OnOyeClick, CustomPhasedListener {

    boolean isShowing = false;
    private static final String TAG = "DashboardActivity";
    final float anchorPoint = 0.4f;

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
   boolean setting=false;
    TextView tv_client_heading;

    @Bind(R.id.profile_image_main)
    ImageView profileImage;

    @Bind(R.id.txtEmail)
    TextView emailTxt;


    @Bind(R.id.toast_text)
    TextView toastText;

    @Bind(R.id.toast_layout)
    LinearLayout toastLayout;




    @Bind(R.id.hdroomsCount)
    TextView hdroomsCount;
    Boolean Owner_detail=false;

    /*@Bind(R.id.tv_dealinfo)
    TextView tv_dealinfo;*/





    private WebView webView;
    private  Boolean autocomplete = false;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onPositionSelected(int position, int count) {
        Log.i(TAG,"card position "+position);
    }

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
                    //bundle.putString("lastFragment", "OyeIntentSpecs");
                    bundle.putString("lastFragment", "oyed");

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

                        SignUpFragment signUpFragment = new SignUpFragment();
                        loadFragment(signUpFragment, bundle, R.id.container_Signup, "");
                        Log.i("Signup called =", "Sign up");
                        // btnOnOyeClick.setVisibility(View.GONE);
                    }
                } else {
                    Log.i("already", "Signed up");
                    if (s.equals(false)) {
                        SnackbarManager.show(
                                Snackbar.with(getBaseContext())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text("Please select property subtype")
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

                    } else {
                        //create new deal

                        alertbuilder();




                    }
                }


            }




        }
    };

    private BroadcastReceiver doSignUp = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            SignUpFragment signUpFragment = new SignUpFragment();
            Bundle bundle = new Bundle();
            bundle.putString("lastFragment", "clientDrawer");  //consider as direct signup so keep last fragment as clientDrawer
            loadFragment(signUpFragment, bundle, R.id.container_Signup, "");


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

        ShortcutBadger.removeCount(this);
        Log.i(TAG,"popup window shown 1 ");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"popup window shown delay ");
                showOptions(ClientMainActivity.this);
            }
        }, 2000);

        Log.i(TAG,"popup window shown 5 ");
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
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(doSignUp, new IntentFilter(AppConstants.DOSIGNUP));

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
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(doSignUp);

    }


    /**
     * init all components
     */
    private void init() {


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
        //set title
//        getSupportActionBar().setTitle(title);
    }

    public void onClickButton(Bundle args) {
        if (!isShowing) {


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
        Log.i(TAG,"itemTitle "+itemTitle  + R.string.shareNo);

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
            if(General.getSharedPreferences(this,AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")){
                SignUpFragment signUpFragment = new SignUpFragment();
                // signUpFragment.getView().bringToFront();
                Bundle bundle = new Bundle();
                bundle.putStringArray("Chat", null);
                bundle.putString("lastFragment", "drawer");
                loadFragment(signUpFragment, bundle, R.id.container_Signup, "");
            }
            else
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
            bundle.putString("lastFragment", "clientDrawer");
            loadFragment(signUpFragment, bundle, R.id.container_Signup, "");



        }
        else if(itemTitle.equals(getString(R.string.shareNo))){
            Log.i(TAG,"itemTitle 1 "+itemTitle + R.string.shareNo);
            ShareOwnersNo shareOwnersNo = new ShareOwnersNo();
            loadFragment(shareOwnersNo, null, R.id.container_Signup, "");
            Owner_detail=true;

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
                .setTitle("OYEOK")
                .setContentDescription("Get property at right price. ")
                .setCanonicalIdentifier(mob_no);




        LinkProperties linkProperties = new LinkProperties()
                .setChannel("android")
                .setFeature("share")
                .addControlParameter("user_name", user_id)
                .addControlParameter("mob_no", mob_no)
                //.addControlParameter("$android_url", AppConstants.GOOGLE_PLAY_STORE_APP_URL)
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
                    intent.putExtra(Intent.EXTRA_SUBJECT, "OYEOK! Get property at right price.");
                    Log.i("mob_no url","before share ");
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

               /* try {
                    DashboardClientFragment dash=new DashboardClientFragment();
                    dash.Wlak_Beacon();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
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


            }
            else {

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

        }else if(Owner_detail==true){
            super.onBackPressed();
            Owner_detail=false;
            backpress = 0;
        } else{

            Log.i("SIGNUP_FLAG"," closing app =================== 3"+getFragmentManager().getBackStackEntryCount());
            if(backpress <1) {
                backpress = (backpress + 1);
                TastyToast.makeText(this, "Press Back again to Exit!", TastyToast.LENGTH_LONG, TastyToast.INFO);
                //Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
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



    private PopupWindow showOptions(final Context mcon){
        Log.i(TAG,"popup window shown 2 ");
        try{
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            Log.i(TAG,"popup window shown 20 "+width+" "+height);
            LayoutInflater inflater = (LayoutInflater) mcon.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.card1,null);

            CustomPhasedSeekBar mPhasedSeekBar = (CustomPhasedSeekBar) layout.findViewById(R.id.phasedSeekBar1);
            Button button =(Button) layout.findViewById(R.id.button);
            Button signUp =(Button) layout.findViewById(R.id.signUp);
            Button later =(Button) layout.findViewById(R.id.later);
            ImageButton cardMaps = (ImageButton) layout.findViewById(R.id.cardMaps);
             final FrameLayout cardFrame = (FrameLayout) layout.findViewById(R.id.card_frame);
            final FrameLayout a = (FrameLayout) layout.findViewById(R.id.a);



            DBHelper dbHelper = new DBHelper(mcon);
            if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
                mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(mcon.getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{mcon.getResources().getString(R.string.Rental), mcon.getResources().getString(R.string.Resale)}));
            else
                mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(mcon.getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.real_estate_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Audit", "Auction"}));
              mPhasedSeekBar.setListener(this);
            final PopupWindow optionspu1 = greyOut(mcon);
            //final PopupWindow optionspu = new PopupWindow(layout, 600,1000, true);
            final PopupWindow optionspu = new PopupWindow(layout);
            optionspu.setWidth(width-140);
            optionspu.setHeight(height-140);
            optionspu.setAnimationStyle(R.style.AnimationPopup);

            /*optionspu.setTouchable(true);
            optionspu.setOutsideTouchable(false);*/

            optionspu.setFocusable(false);
            optionspu.setTouchable(true);
            optionspu.setOutsideTouchable(false);
            // optionspu.showAtLocation(layout, Gravity.CENTER, 0, 0);
            optionspu.showAtLocation(layout, Gravity.TOP, 0, 100);


            //optionspu.update(0, 0,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //optionspu.setAnimationStyle(R.anim.bounce);
//            BrokerMap brokerMap=new BrokerMap();
//
//
//            loadFragment(brokerMap,null,cardFrame.getId(),"");
            cardMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  BrokerMap brokerMap=new BrokerMap();
                    //set arguments
                    BrokerMap brokerMap=new BrokerMap();


                    loadFragment(brokerMap,null,R.id.container_Signup,"");
                    //load fragment
                   /* FragmentManager fragmentManager = getSupportFragmentManager();
                    *//*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(cardFrame.getId(), brokerMap);
                    fragmentTransaction.commitAllowingStateLoss();*//*
                   // loadFragment(brokerMap,null,R.id.a,"");


                    fragmentManager.beginTransaction()
                            .replace(a.getId(), brokerMap)
                            .addToBackStack(null)
                            .commit();*/
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"popup window shown 13 ");
                    optionspu1.dismiss();
                    optionspu.dismiss();
                }
            });
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"popup window shown 13 ");
                    optionspu1.dismiss();
                    optionspu.dismiss();
                    Intent intent = new Intent(AppConstants.DOSIGNUP);
                    LocalBroadcastManager.getInstance(mcon).sendBroadcast(intent);

                }
            });
            later.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"popup window shown 13 ");
                    optionspu1.dismiss();
                    optionspu.dismiss();
                    TastyToast.makeText(mcon, "We have connected you with 3 brokers in your area.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    TastyToast.makeText(mcon, "Sign up to connect with 7 more brokers waiting for you.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                }
            });

            return optionspu;
        }
        catch (Exception e){e.printStackTrace();
            Log.i(TAG,"popup window shown 4 "+e);
            return null;}


    }

    private PopupWindow greyOut(final Context mcon){
        Log.i(TAG,"popup window shown 2 ");
        try{
            LayoutInflater inflater = (LayoutInflater) mcon.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.grey_out_popup,null);
            final PopupWindow optionspu1 = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT, true);
            optionspu1.setFocusable(false);
            optionspu1.setTouchable(true);
            optionspu1.setOutsideTouchable(false);
            optionspu1.showAtLocation(layout, Gravity.CENTER, 0, 0);
            return optionspu1;
        }
        catch (Exception e){e.printStackTrace();
            Log.i(TAG,"popup window shown 4 "+e);
            return null;}


    }

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







}

