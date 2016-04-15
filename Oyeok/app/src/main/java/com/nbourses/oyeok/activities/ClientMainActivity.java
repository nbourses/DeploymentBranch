package com.nbourses.oyeok.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.fragments.DashboardClientFragment;
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

public class ClientMainActivity extends AppCompatActivity implements NetworkInterface, FragmentDrawer.FragmentDrawerListener, OnOyeClick {

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

    @Bind(R.id.profile_image_main)
    ImageView profileImage;

    @Bind(R.id.toast_text)
    TextView toastText;

    @Bind(R.id.toast_layout)
    LinearLayout toastLayout;

    @Bind(R.id.btnOnOyeClick)
    Button btnOnOyeClick;

    private WebView webView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        if (General.isNetworkAvailable(getApplicationContext())) {

            Log.i("TRACE", "network available");
        }
        else

        {
            Log.i("TRACE", "network not availabile");
            SnackbarManager.show(
                    Snackbar.with(this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("No internet connection ,please check your settings")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
        }


        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(closeOyeScreenSlide, new IntentFilter(AppConstants.CLOSE_OYE_SCREEN_SLIDE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(closeOyeScreenSlide);
    }

    /**
     * init all components
     */
    private void init() {

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
                resizeScrollView(panel, slideOffset);
            }

            @Override
            public void onPanelExpanded(View panel) {
                resizeScrollView(panel, 0.0f);
            }

            @Override
            public void onPanelCollapsed(View panel) {
            }

            @Override
            public void onPanelAnchored(View panel) {
                resizeScrollView(panel, anchorPoint);
            }

            @Override
            public void onPanelHidden(View panel) {
            }

            private void resizeScrollView(View panel, float slideOffset) {
                final int scrollViewHeight =
                        (int) ((panel.getHeight() - slidingLayout.getPanelHeight()) * (1.0f - slideOffset));
                slidingView.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                scrollViewHeight));
            }
        });

        //setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Client");

        //TODO: need to validate this functionality
        dbHelper = new DBHelper(getBaseContext());
        mHandler = new Handler();

        //setup navigation drawer
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        if(!dbHelper.getValue(DatabaseConstants.imageFilePath).equalsIgnoreCase("null")) {
            Bitmap yourSelectedImage = BitmapFactory.decodeFile(dbHelper.getValue(DatabaseConstants.imageFilePath));
            profileImage.setImageBitmap(yourSelectedImage);
        }

        //by default load map view
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
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();

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

            btnOnOyeClick.setVisibility(View.VISIBLE);
        }
        else {
            closeOyeScreen();
        }
    }

    private void closeOyeScreen() {
        isShowing = false;
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        btnOnOyeClick.setVisibility(View.GONE);
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
            Intent openDealsListing = new Intent(this, ClientDealsListActivity.class);
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
            webView.setWebViewClient(new WebViewClient());

            webView.loadUrl("http://www.hioyeok.com/blog");


        }
        else if (itemTitle.equals(getString(R.string.updateProfile))) {
            //setContentView(R.layout.browser);
            loadFragment(frag, null, R.id.container_map, title);
        }




        if (fragment != null) {
            loadFragment(fragment, null, R.id.container_map, title);
        }
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

    @OnClick(R.id.btnOnOyeClick)
    public void submitOyeOk(View v) {
        Log.i("TRACE =", "oyebutton baher");
        if (General.getSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
            Log.i("TRACE =", "clicked oyebutton if");
            //show sign up screen
            Bundle bundle = new Bundle();
            bundle.putStringArray("propertySpecification", null);
            bundle.putString("lastFragment", "OyeIntentSpecs");

            SignUpFragment signUpFragment = new SignUpFragment();
            loadFragment(signUpFragment, bundle, R.id.container_oye, "");
            Log.i("Signup called =", "Sign up");


            btnOnOyeClick.setVisibility(View.GONE);
        }
        else {
            Log.i("TRACE =", "clicked oyebutton else");
            //create new deal
            General.publishOye(getApplicationContext());
            closeOyeScreen();
        }
    }

    @Override
    public void onBackPressed() {
        if (slidingLayout != null &&
                (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                        slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            closeOyeScreen();
        } else {
            super.onBackPressed();
        }
        if(webView != null){
            Intent back = new Intent(this, ClientMainActivity.class);
            startActivity(back);
        }

    }



    @OnClick(R.id.btnMyDeals)
    public void onBtnMyDealsClick(View v) {
        Intent openDealsListing = new Intent(this, ClientDealsListActivity.class);
        startActivity(openDealsListing);
    }

    ////////////////// Network method implementation //////////

    public void NetworkStatusChanged(String status)
    {
        Log.i("TRACE NETWORK","status ");
    }

}



