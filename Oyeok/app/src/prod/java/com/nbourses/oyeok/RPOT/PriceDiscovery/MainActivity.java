package com.nbourses.oyeok.RPOT.PriceDiscovery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.digits.sdk.android.Digits;
import com.firebase.client.Firebase;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.GoogleCloudMessaging.RegistrationIntentService;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI.Droom_Chat_New;

import com.nbourses.oyeok.SignUp.SignUpFragment;

import com.nbourses.oyeok.RPOT.OkBroker.UI.Ok_Broker_MainScreen;

import com.nbourses.oyeok.User.Profile;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.fabric.sdk.android.Fabric;


//import com.rockerhieu.emojicon.EmojiconGridFragment;
//import com.rockerhieu.emojicon.EmojiconsFragment;
//import com.rockerhieu.emojicon.emoji.Emojicon;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, View.OnClickListener{


//digits
    private static final String TWITTER_KEY = "CE00enRZ4tIG82OJp6vKib8YS";
    private static final String TWITTER_SECRET = "5AMXDHAXG0luBuuHzSrDLD0AvwP8GzF06klXFgcwnzAVurXUoS";
//digits ends



    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] CAMERA_PERMS={
            Manifest.permission.CAMERA
    };
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=102;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3,CAMERA_REQUEST=INITIAL_REQUEST+7;

    private static String TAG = MainActivity.class.getSimpleName();
    private String firebaseUrl="https://resplendent-fire-6770.firebaseio.com/";
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private Button help;


    //VARIABLES: Reside Menu for User Role Change */
    ResideMenu resideMenu;
    ResideMenuItem[] resideMenuItems;
    //private Button resideMenuButton;
    private Button openMaps;
    Switch switchOnOff;
    DBHelper dbHelper;
    Branch branch;
    private int mInterval=5000;
    BranchUniversalObject branchUniversalObject;
    LinkProperties linkProperties;
    ImageView profileImage;
    TextView changeRegion;
    TextView toastText;
    LinearLayout toastLayout;
    private Handler mHandler;
    boolean netAvailable;

    public void setMapsClicked(openMapsClicked mapsClicked) {
        this.mapsClicked = mapsClicked;
    }

    private openMapsClicked mapsClicked;

    Button refer;


  /*  @Override
    public void onFragmentInteraction(String s) {
        Log.i("House",""+s);
        OyeIntentSpecs.data=s;
    }*/


    public interface openMapsClicked{
        public void clicked();
    }

    /*public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 302)
        {

            Ok_Broker_MainScreen f1 = (Ok_Broker_MainScreen) getSupportFragmentManager().findFragmentById(R.id.container_body);
            f1.onActivityResult(requestCode,resultCode,data);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //digits
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
       //digits end



        setContentView(R.layout.activity_main);


        Log.i("MAIN", "MAIN");

        if (General.isNetworkAvailable(getApplicationContext())){

            Log.i("TRACE", "network availability");
        }
        else

        {
            Log.i("TRACE", "network availabil");
        }

       // netAvailable = General.isNetworkAvailable(getApplicationContext());
        Log.i("TRACE", "network availability");
        Log.i("TRACE =", "In main");

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        toastLayout= (LinearLayout) findViewById(R.id.toastLayout);
        //resideMenuButton = (Button) mToolbar.findViewById(R.id.residemenu_rightmenu_titlebar);
        openMaps  = (Button) mToolbar.findViewById(R.id.openmaps);
        changeRegion = (TextView) mToolbar.findViewById(R.id.tv_change_region);
        help     = (Button) mToolbar.findViewById(R.id.help);
        hideOpenMaps();
        //bringResideMenu();
//        resideMenuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openRightMenu();
//            }
//        });
        dbHelper=new DBHelper(getBaseContext());
        toastText= (TextView) findViewById(R.id.toastText);


        setSupportActionBar(mToolbar);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
        }
        else
        {
            //if(dbHelper.getValue(DatabaseConstants.user).equals("Broker"))
            Log.i("MAIN","gadbad "+General.getSharedPreferences(this, AppConstants.ROLE_OF_USER));
            if(General.getSharedPreferences(this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                Log.i("MAIN","gadbad 1");
                changeFragment(new Ok_Broker_MainScreen(), null, "Broker HomeScreen");
            }
            else {
                Log.i("MAIN","gadbad 1");
                changeFragment(new RexMarkerPanelScreen(), null, "Oye HomeScreen");
            }
        }






        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        profileImage = (ImageView)findViewById(R.id.profile_image_main);
        if(!dbHelper.getValue(DatabaseConstants.imageFilePath).equalsIgnoreCase("null")) {
            Bitmap yourSelectedImage = BitmapFactory.decodeFile(dbHelper.getValue(DatabaseConstants.imageFilePath));
            profileImage.setImageBitmap(yourSelectedImage);
        }
        //switchOnOff= (Switch) findViewById(R.id.switch_onoffmode);
        dbHelper.save(DatabaseConstants.offmode, "null");
/*
        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(getBaseContext(), "offline mode",Toast.LENGTH_LONG).show();
                    showToastMessage("offline Mode");
                    dbHelper.save(DatabaseConstants.offmode, "yes");
                    Log.i("offmode entry", dbHelper.getValue(DatabaseConstants.offmode));
                    try {
                        RexMarkerPanelScreen r = (RexMarkerPanelScreen) getSupportFragmentManager().findFragmentById(R.id.container_body);
                        r.setPhasedSeekBar();
                    } catch (ClassCastException e) {
                        try {
                            Ok_Broker_MainScreen m = (Ok_Broker_MainScreen) getSupportFragmentManager().findFragmentById(R.id.container_body);
                            m.setPhasedSeekBar();
                        } catch (ClassCastException x) {

                        }

                    }

                    */
/*RexMarkerPanelScreen r=new RexMarkerPanelScreen();
                    r.setPhasedSeekBar();*//*

                } else {
                    //Toast.makeText(getBaseContext(), "online mode", Toast.LENGTH_LONG).show();
                    showToastMessage("Online Mode");
                    dbHelper.save(DatabaseConstants.offmode, "null");
                    Log.i("offmode entry", dbHelper.getValue(DatabaseConstants.offmode));
                    try {
                        RexMarkerPanelScreen r = (RexMarkerPanelScreen) getSupportFragmentManager().findFragmentById(R.id.container_body);
                        r.setPhasedSeekBar();
                    } catch (ClassCastException e) {
                        try {
                            Ok_Broker_MainScreen m = (Ok_Broker_MainScreen) getSupportFragmentManager().findFragmentById(R.id.container_body);
                            m.setPhasedSeekBar();
                        } catch (ClassCastException x) {

                        }
                    */
/*RexMarkerPanelScreen r=new RexMarkerPanelScreen();
                    r.setPhasedSeekBar();*//*

                    }
                }
            }
        });
*/

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

                Fragment fragment = new Droom_Chat_New();
                Bundle bundle=new Bundle();
                bundle.putString("UserId1",dbHelper.getValue(DatabaseConstants.userId));
                bundle.putString("OkId",telephonyManager.getDeviceId());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        setUpMenuChangeUserRole();

        if(dbHelper.getValue(DatabaseConstants.user).equalsIgnoreCase("Broker"))
            changeFragment(new Ok_Broker_MainScreen(),null,"Broker HomeScreen");
        else
            changeFragment(new RexMarkerPanelScreen(), null,"Oye HomeScreen");




        openMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapsClicked != null) {
                    mapsClicked.clicked();
                }
            }
        });
        Firebase.setAndroidContext(this);
        mHandler = new Handler();
        //startRepeatingTask();

toastLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mHandler.removeCallbacks(mStatusChecker);
        hideToastMessage();
    }
});

       /* refer = (Button) findViewById(R.id.refer);
        refer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFragment(new ReferFragment(), null);
            }
        });
         display the first navigation drawer view on app launch
*/

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    void startRepeatingTask() {
        mStatusChecker.run();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            //fillHourGlasses(0, intervalCount * mInterval / 1000);

           hideToastMessage();
        }
    };

    public void showToastMessage(String message){
        toastText.setText(message);
        toastLayout.setVisibility(View.VISIBLE);
        hideMap(1);
        mHandler.postDelayed(mStatusChecker, 2000);
    }
    public void hideToastMessage(){
        hideMap(0);
        toastLayout.setVisibility(View.INVISIBLE);
    }






    public void changeDrawerToggle(boolean string,String title)
    {
//        if(string)
//        {
//            //getSupportActionBar().setIcon(null);
//            getSupportActionBar().setTitle(title);
//        }else
//        {
//           //getSupportActionBar().setIcon(R.drawable.ic_arrow_back_24dp);
//            getSupportActionBar().setTitle(title);
//        }
        drawerFragment.setmDrawerToggle(string);
        getSupportActionBar().setTitle(title);

    }



    private void hideMap(int i) {

        Animation m = null;

        //Load animation
        if(i==0) {
            m = AnimationUtils.loadAnimation(this,
                    R.anim.slide_up);

            //SharedPrefs.getString(getActivity(),SharedPrefs.MY_LOCALITY)+","+SharedPrefs.getString(getActivity(),SharedPrefs.MY_CITY)
        }else {

            m = AnimationUtils.loadAnimation(this,
                    R.anim.slide_down_toast_layout);
        }

        toastLayout.setAnimation(m);
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
                if(dbHelper.getValue(DatabaseConstants.user).equalsIgnoreCase("Broker"))
                {
                    changeFragment(new Ok_Broker_MainScreen(), null, "MarkerPanel");
                }else {
                    changeFragment(new RexMarkerPanelScreen(), null, "MarkerPanel");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        branch = Branch.getInstance(getApplicationContext());
        boolean isReferrable = false;

        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                    String referrer;
                    try {
                        referrer = referringParams.getString("user_name");
                        Log.v(TAG, "Refferer is " + referrer);
                        //put referrer rewarding mechanism here
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.i(TAG, error.getMessage());
                }
            }
        }, isReferrable, this.getIntent().getData(), this);

    }
    @Override
    protected void onStop() {
        super.onStop();
        branch.closeSession();
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    {
                        if(dbHelper.getValue(DatabaseConstants.user).equalsIgnoreCase("Broker"))
                            changeFragment(new Ok_Broker_MainScreen(),null,"Broker HomeScreen");
                        else
                            changeFragment(new RexMarkerPanelScreen(),null,"Oye HomeScreen");
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                break;


            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    @Override
    public void onDrawerItemSelected(View view, int position, String itemTitle) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {

            case 0:
            if (dbHelper.getValue(DatabaseConstants.user).equalsIgnoreCase("null"))
            {
                Bundle bundle = new Bundle();
                //bundle.putStringArray("propertySpecification",propertySpecification);
                bundle.putString("lastFragment", "RexMarkerPanel");
                fragment = new SignUpFragment();
                fragment.setArguments(bundle);
                title = "Sign Up";
                break;
            }
            else {
                fragment = new Profile();
                title = "Profile";
                break;

            }
            case 1:
               fragment = new Ok_Broker_MainScreen();
                title = "Broker";
                break;
            case 2:
                shareReferralLink();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
           // fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public void setTitle(String title)
    {

     getSupportActionBar().setTitle(title);
    }

    private void shareReferralLink() {
        //TO-DO
        //get users contact number here
        String user_id = dbHelper.getValue(DatabaseConstants.userId);
        //JSONObject sessionParams = branch.getFirstReferringParams();
        branch.setIdentity(user_id);

        branchUniversalObject = new BranchUniversalObject()
                // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                .setCanonicalIdentifier(user_id);

        linkProperties = new LinkProperties()
                .setChannel("sms")
                .setFeature("sharing")
                .addControlParameter("user_name",user_id)
                .addControlParameter("$android_url", "https://www.dropbox.com/s/2ro7ae5y4xjte34/app-release.apk?dl=0")
                .addControlParameter("$desktop_url","https://www.dropbox.com/s/2ro7ae5y4xjte34/app-release.apk?dl=0")
                .addControlParameter("$always_deeplink", "true");

        branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Hey check this out!");
                    startActivity(Intent.createChooser(intent, "Share link via"));
                }
            }
        });
    }


    public void changeFragment(Fragment f, Bundle args,String title)
    {
        Fragment fragment = f;
        if (fragment != null) {
            f.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commitAllowingStateLoss();

            Log.i("Change Fragment",f.toString());
            // set the toolbar title
           getSupportActionBar().setTitle(title);
        }

    }


    //@OnClick(R.id.residemenu_rightmenu_titlebar)
    public void openRightMenu() {
        //Amplitude.getInstance().logEvent("title_bar_right_menu clicked");
        Log.i("TAG", "@OnClick() + Inside openRightMenu()");
        resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
    }

    //- Start Implementing Reside Menu

    private void setUpMenuChangeUserRole(){

        Log.i("TAG", "Inside setUpMenuChangeUserRole()");

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //resideMenu.setBackground(R.drawable.residemenu_background);
        resideMenu.attachToActivity(this);

        // create more items;
        String titles[] = { "R-Exchange", "J-Exchange", "L-Exchange"};
        int icon[] = { R.drawable.residemenu_realestate, R.drawable.residemenu_jobs, R.drawable.residemenu_loan};

        //Initializing the Array
        resideMenuItems = new ResideMenuItem[titles.length];

        for (int i = 0; i < titles.length; i++){

            resideMenuItems[i] = new ResideMenuItem(MainActivity.this, icon[i], titles[i]);
            resideMenuItems[i].setOnClickListener(MainActivity.this);
            resideMenu.addMenuItem(resideMenuItems[i],  ResideMenu.DIRECTION_RIGHT); // or  ResideMenu.DIRECTION_RIGHT
        }
        resideMenuItems[0].tv_title.setTextColor(Color.BLUE);


    }

    /*private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @SuppressLint("NewApi")
    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if(canAccessLocation()){

        }
        else{
            Toast.makeText(this,"Offline Mode",Toast.LENGTH_LONG);
        }

    }*/


        @Override
    public void onClick(View view ) {

        if (view == resideMenuItems[0]){
            //Amplitude.getInstance().logEvent("resideMenuItems[0] clicked");

            changeFragment(new RexMarkerPanelScreen(), null,"Oye HomeScreen");
            //Toast.makeText(getApplicationContext(), "Real Exchange HAS STARTED", Toast.LENGTH_LONG).show();
            showToastMessage("Real Exchange HAS STARTED");
            resideMenuItems[0].tv_title.setTextColor(Color.BLUE);
            resideMenuItems[1].tv_title.setTextColor(Color.BLACK);
            resideMenuItems[2].tv_title.setTextColor(Color.BLACK);
        }else if (view == resideMenuItems[1]){
            //Amplitude.getInstance().logEvent("resideMenuItems[1] clicked");
            //Toast.makeText(getApplicationContext(), "Jobs Exchange HAS STARTED", Toast.LENGTH_LONG).show();
            showToastMessage("Jobs Exchange HAS STARTED");
            resideMenuItems[1].tv_title.setTextColor(Color.BLUE);
            resideMenuItems[0].tv_title.setTextColor(Color.BLACK);
            resideMenuItems[2].tv_title.setTextColor(Color.BLACK);
            changeFragment(new JexMarkerPanelScreen(), null,"Jex HomeScreen");


        }else if (view == resideMenuItems[2])
        {
            //Toast.makeText(getApplicationContext(), "Loans Exchange HAS STARTED", Toast.LENGTH_LONG).show();
            showToastMessage("Loans Exchange HAS STARTED");
            resideMenuItems[2].tv_title.setTextColor(Color.BLUE);
            resideMenuItems[1].tv_title.setTextColor(Color.BLACK);
            resideMenuItems[0].tv_title.setTextColor(Color.BLACK);
            changeFragment(new LexMarkerPanelScreen(), null,"Lex HomeScreen");
        }
        resideMenu.closeMenu();
    }


    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {

        @Override
        public void openMenu() {
            //Toast.makeText(getApplicationContext(), "Menu is opened!", Toast.LENGTH_SHORT).show();
            showToastMessage("Menu is Opened!");
        }

        @Override
        public void closeMenu() {
            //Toast.makeText(getApplicationContext(), "Menu is closed!", Toast.LENGTH_SHORT).show();
            showToastMessage("Menu is Closed!");
        }
    };

//    public void hideResideMenu()
//    {
//        resideMenuButton.setVisibility(View.GONE);
//    }
//
//    public void bringResideMenu()
//    {
//        resideMenuButton.setVisibility(View.VISIBLE);
//    }

    public void hideOpenMaps()
    {
        openMaps.setVisibility(View.GONE);
        changeRegion.setVisibility(View.GONE);
    }

    public  void refresh(){
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
    }

    public void setListener(FragmentDrawer.MDrawerListener mDrawerListener)
    {
          drawerFragment.setmDrawerListener(mDrawerListener);
    }

    public void showOpenMaps()
    {

        openMaps.setVisibility(View.VISIBLE);
        changeRegion.setVisibility(View.VISIBLE);
        Log.v(TAG, "opened maps");
    }
    //- End Implementing Reside Menu
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    //refer button onclick


}
