package com.nbourses.oyeok.RPOT.PriceDiscovery;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.GoogleCloudMessaging.RegistrationIntentService;
import com.nbourses.oyeok.JPOT.SalaryDiscovery.UI.JexMarkerPanelScreen;
import com.nbourses.oyeok.LPOT.PriceDiscoveryLoan.UI.LexMarkerPanelScreen;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.OkBroker.UI.Ok_Broker_MainScreen;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.NavDrawer.FragmentDrawer;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.ReferFragment.ReferFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.RexMarkerPanelScreen;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.resideMenu.ResideMenu;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.resideMenu.ResideMenuItem;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.User.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
//import com.rockerhieu.emojicon.EmojiconGridFragment;
//import com.rockerhieu.emojicon.EmojiconsFragment;
//import com.rockerhieu.emojicon.emoji.Emojicon;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, View.OnClickListener{

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

    private static String TAG = MainActivity.class.getSimpleName();
    private String firebaseUrl="https://resplendent-fire-6770.firebaseio.com/";
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;


    //VARIABLES: Reside Menu for User Role Change */
    ResideMenu resideMenu;
    ResideMenuItem[] resideMenuItems;
    private Button resideMenuButton;
    private Button openMaps;
    Switch switchOnOff;
    DBHelper dbHelper;
    Branch branch;
    BranchUniversalObject branchUniversalObject;
    LinkProperties linkProperties;


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
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        resideMenuButton = (Button) mToolbar.findViewById(R.id.residemenu_rightmenu_titlebar);
        openMaps  = (Button) mToolbar.findViewById(R.id.openmaps);
        hideOpenMaps();
        bringResideMenu();
        resideMenuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openRightMenu();
            }
        });
        dbHelper=new DBHelper(getBaseContext());


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        switchOnOff= (Switch) findViewById(R.id.switch_onoffmode);
        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isShown()) {
                    if (isChecked) {
                        Toast.makeText(getBaseContext(), "offline mode",
                                Toast.LENGTH_LONG).show();
                        dbHelper.save(DatabaseConstants.offmode, "yes");
                        Log.i("offmode entry", dbHelper.getValue(DatabaseConstants.offmode));
                        try {
                            RexMarkerPanelScreen r = (RexMarkerPanelScreen) getSupportFragmentManager().findFragmentById(R.id.container_body);
                            r.setPhasedSeekBar();
                        } catch (ClassCastException e) {
                            try {
                                Ok_Broker_MainScreen m = (Ok_Broker_MainScreen) getSupportFragmentManager().findFragmentById(R.id.container_body);
                                m.setPhasedSeekBar();
                            }catch (ClassCastException e1)
                            {

                            }
                        }
                    /*RexMarkerPanelScreen r=new RexMarkerPanelScreen();
                    r.setPhasedSeekBar();*/
                    } else {
                        Toast.makeText(getBaseContext(), "online mode",
                                Toast.LENGTH_LONG).show();
                        dbHelper.save(DatabaseConstants.offmode, "null");
                        Log.i("offmode entry", dbHelper.getValue(DatabaseConstants.offmode));
                        try {
                            RexMarkerPanelScreen r = (RexMarkerPanelScreen) getSupportFragmentManager().findFragmentById(R.id.container_body);
                            r.setPhasedSeekBar();
                        } catch (ClassCastException e) {
                            try {
                                Ok_Broker_MainScreen m = (Ok_Broker_MainScreen) getSupportFragmentManager().findFragmentById(R.id.container_body);
                                m.setPhasedSeekBar();
                            }catch (ClassCastException e1)
                            {

                            }
                        }
                    /*RexMarkerPanelScreen r=new RexMarkerPanelScreen();
                    r.setPhasedSeekBar();*/
                    }
                }
            }
        });


        setUpMenuChangeUserRole();

        if(dbHelper.getValue(DatabaseConstants.user).equals("Broker"))
            changeFragment(new Ok_Broker_MainScreen(),null);
        else
            changeFragment(new RexMarkerPanelScreen(),null);




        openMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapsClicked != null) {
                    mapsClicked.clicked();
                }
            }
        });
        Firebase.setAndroidContext(this);

        refer = (Button) findViewById(R.id.refer);
        refer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFragment(new ReferFragment(), null);
            }
        });
        // display the first navigation drawer view on app launch



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
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {

            case 0:
            if (dbHelper.getValue(DatabaseConstants.user).equals("null"))
            {
                Bundle bundle = new Bundle();
                //bundle.putStringArray("propertySpecification",propertySpecification);
                bundle.putString("lastFragment", "RexMarkerPanel");
                fragment = new SignUpFragment();
                fragment.setArguments(bundle);
                title = "Sign Up";

                drawerFragment = (FragmentDrawer)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
                drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
                drawerFragment.setDrawerListener(this);
                break;
            }
            else {
                fragment = new Profile();
                title = "Profile";

                drawerFragment = (FragmentDrawer)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
                drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
                drawerFragment.setDrawerListener(this);
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
        String mobile = "Get contact number here!";
        //JSONObject sessionParams = branch.getFirstReferringParams();
        branch.setIdentity(mobile);
        
        branchUniversalObject = new BranchUniversalObject()
                // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                .setCanonicalIdentifier(mobile);

        linkProperties = new LinkProperties()
                .setChannel("sms")
                .setFeature("sharing")
                .addControlParameter("user_name",mobile)
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


    public void changeFragment(Fragment f, Bundle args)
    {
        Fragment fragment = f;
        if (fragment != null) {
            f.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            Log.i("Change Fragment",f.toString());
            // set the toolbar title
            getSupportActionBar().setTitle("Dealing rooms");
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

        // create menu items;
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

            changeFragment(new RexMarkerPanelScreen(), null);
            Toast.makeText(getApplicationContext(), "Real Exchange HAS STARTED",
                    Toast.LENGTH_LONG).show();

            resideMenuItems[0].tv_title.setTextColor(Color.BLUE);
            resideMenuItems[1].tv_title.setTextColor(Color.BLACK);
            resideMenuItems[2].tv_title.setTextColor(Color.BLACK);
        }else if (view == resideMenuItems[1]){
            //Amplitude.getInstance().logEvent("resideMenuItems[1] clicked");
            Toast.makeText(getApplicationContext(), "Jobs Exchange HAS STARTED",
                    Toast.LENGTH_LONG).show();
            resideMenuItems[1].tv_title.setTextColor(Color.BLUE);
            resideMenuItems[0].tv_title.setTextColor(Color.BLACK);
            resideMenuItems[2].tv_title.setTextColor(Color.BLACK);
            changeFragment(new JexMarkerPanelScreen(), null);


        }else if (view == resideMenuItems[2])
        {
            Toast.makeText(getApplicationContext(), "Loans Exchange HAS STARTED",
                    Toast.LENGTH_LONG).show();
            resideMenuItems[2].tv_title.setTextColor(Color.BLUE);
            resideMenuItems[1].tv_title.setTextColor(Color.BLACK);
            resideMenuItems[0].tv_title.setTextColor(Color.BLACK);
            changeFragment(new LexMarkerPanelScreen(), null);
        }
        resideMenu.closeMenu();
    }


    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {

        @Override
        public void openMenu() {
            Toast.makeText(getApplicationContext(), "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(getApplicationContext(), "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    public void hideResideMenu()
    {
        resideMenuButton.setVisibility(View.GONE);
    }

    public void bringResideMenu()
    {
        resideMenuButton.setVisibility(View.VISIBLE);
    }

    public void hideOpenMaps()
    {
        openMaps.setVisibility(View.GONE);
    }

    public void showOpenMaps()
    {
        openMaps.setVisibility(View.VISIBLE);
        Log.v(TAG, "opened maps");
    }
    //- End Implementing Reside Menu

    //refer button onclick
}
