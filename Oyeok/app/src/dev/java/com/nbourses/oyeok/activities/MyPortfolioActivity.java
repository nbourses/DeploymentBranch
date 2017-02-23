package com.nbourses.oyeok.activities;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.adapters.porfolioAdapter;
import com.nbourses.oyeok.fragments.AppSetting;
import com.nbourses.oyeok.fragments.DashboardClientFragment;
import com.nbourses.oyeok.fragments.ShareOwnersNo;
import com.nbourses.oyeok.fragments.WatchlistDisplayBuilding;
import com.nbourses.oyeok.fragments.WatchlistExplorer;
import com.nbourses.oyeok.fragments.WatchlistTitle;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.ReadWatchlistAPI;
import com.nbourses.oyeok.models.loadBuildingDataModel;
import com.nbourses.oyeok.models.portListingModel;
import com.nbourses.oyeok.realmModels.Localities;
import com.nbourses.oyeok.realmModels.MyPortfolioModel;
import com.nbourses.oyeok.realmModels.WatchListRealmModel;
import com.nbourses.oyeok.realmModels.WatchlistBuildingRealm;
import com.nbourses.oyeok.realmModels.addBuildingRealm;
import com.nbourses.oyeok.realmModels.loadBuildingdataModelRealm;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by sushil on 29/09/16.
 */

public class MyPortfolioActivity extends BrokerMainPageActivity implements CustomPhasedListener,FragmentDrawer.FragmentDrawerListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.btnMyDeals)
    Button btnMyDeals;

    @Bind(R.id.btnMyDeals1)
    Button btnMyDeals1;

    @Bind(R.id.container_Signup1)
    FrameLayout container_Signup1;

    @Bind(R.id.confirm_screen_title)
    TextView confirm_screen_title;

    @Bind(R.id.rentalCount)
    TextView rentalCount;

    @Bind(R.id.resaleCount)
    TextView resaleCount;

    CustomPhasedSeekBar mPhasedSeekBar;
    int position = 0;
    ViewPager viewPager;
    private Realm realm;
    ListView rental_list;
    MyPortfolioModel results;
    //    myPortfolioAdapter adapter;
    porfolioAdapter adapter;
    ArrayList<String> ids = new ArrayList<>();

    private static RealmList<loadBuildingdataModelRealm> realmsids=new RealmList<>();
    private static RealmList<WatchlistBuildingRealm> watchcontent=new RealmList<>();


    private static ArrayList<portListingModel> myPortfolioOR = new ArrayList<>();
    //    private static ArrayList<addBuildingRealm> addBuildingLL=new ArrayList<>();
    private static ArrayList<portListingModel> myLocalitiesLL = new ArrayList<>();
    private static ArrayList<portListingModel> myLocalitiesOR = new ArrayList<>();
    private static ArrayList<portListingModel> myPortfolioLL = new ArrayList<>();
    private static ArrayList<portListingModel> portListing = new ArrayList<>();
    private static ArrayList<portListingModel> portListingCopy = new ArrayList<>();
    private static ArrayList<portListingModel> addbuildingLL = new ArrayList<>();
    private static ArrayList<portListingModel> addbuildingOR = new ArrayList<>();
    private static ArrayList<portListingModel> deletelist = new ArrayList<>();
    private static ArrayList<portListingModel> watchlist = new ArrayList<>();
    private static ArrayList<portListingModel> item = new ArrayList<>();

    /*RealmResults<MyPortfolioModel> results1; if(myLocalitiesLL != null)
            myLocalitiesLL.clear();
    if(myLocalitiesOR != null)
            myLocalitiesOR.clear();
    RealmResults<addBuildingRealm> results2;*/
    private static ArrayList<loadBuildingDataModel> selectedlist = new ArrayList<>();
    EditText inputSearch;
    private String TT = "LL";
    //LinearLayout add_build;
    private String matchedId;
    TextView  add_catalog,add_building;
    String watchlist_id;
    ArrayList<String> Listwatchlist_id=new ArrayList<>();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_my_portfolio);

        if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
            setContentView(R.layout.activity_my_portfolio);
        } else {
            LinearLayout dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);

            //        NestedScrollView dynamicContent = (NestedScrollView) findViewById(R.id.myScrollingContent);
            // assuming your Wizard content is in content_wizard.xml myScrollingContent
            View wizard = getLayoutInflater().inflate(R.layout.activity_my_portfolio, null);

            // add the inflated View to the layout
            dynamicContent.addView(wizard);
            RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
            RadioButton rb = (RadioButton) findViewById(R.id.watchList);
            rb.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.favourite_heart, 0, 0);
//            rb.setChecked(true);
            // rb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_select_watchlist) , null, null);
            rb.setTextColor(Color.parseColor("#2dc4b6"));

            drawerFragment = (FragmentDrawer)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
            drawerFragment.setDrawerListener(this);
        }
        ButterKnife.bind(this);
        if (watchlist != null)
            watchlist.clear();
        if (portListing != null)
            portListing.clear();
        if (myPortfolioLL != null)
            myPortfolioLL.clear();
        if (myPortfolioOR != null)
            myPortfolioOR.clear();
        if (addbuildingLL != null)
            addbuildingLL.clear();
        if (addbuildingOR != null)
            addbuildingOR.clear();
        if (portListingCopy != null)
            portListingCopy.clear();
        if (addbuildingLL != null)
            addbuildingLL.clear();
        if (addbuildingOR != null)
            addbuildingOR.clear();
        if(myLocalitiesLL != null)
            myLocalitiesLL.clear();

        Log.i("port", "portListing " + watchlist);
        Log.i("port", "portListing " + portListing);
        Log.i("port", "portListing " + portListing);
        Log.i("port", "myPortfolioLL " + myPortfolioLL);
        Log.i("port", "addbuildingLL " + addbuildingLL);
        //final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setActionBar(toolbar); only for above 21 API
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        confirm_screen_title.setVisibility(View.VISIBLE);
        btnMyDeals.setVisibility(View.GONE);
        btnMyDeals1.setVisibility(View.VISIBLE);
        btnMyDeals1.setBackground(getResources().getDrawable(R.drawable.snapshot));

        //btnMyDeals.setText("Share");
        //Phased seekbar initialisation
        mPhasedSeekBar = (CustomPhasedSeekBar) findViewById(R.id.phasedSeekBar);
        mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(this.getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{this.getResources().getString(R.string.Rental), this.getResources().getString(R.string.Resale)}));
        mPhasedSeekBar.setListener(this);
        rental_list = (ListView) findViewById(R.id.Rental_listview);
        inputSearch = (EditText) findViewById(R.id.inputSearch1);
        add_building = (TextView) findViewById(R.id.add_building);
        add_catalog = (TextView) findViewById(R.id.add_catalog);
        //usertext = (TextView) findViewById(R.id.usertext);
       // add_create = (TextView) findViewById(R.id.add_create);


        if ((General.getBadgeCount(this, AppConstants.ADDB_COUNT_LL) > 0)) {
            rentalCount.setVisibility(View.VISIBLE);
            rentalCount.setText(String.valueOf(General.getBadgeCount(this, AppConstants.ADDB_COUNT_LL)));

        }
        if ((General.getBadgeCount(this, AppConstants.ADDB_COUNT_OR) > 0)) {
            resaleCount.setVisibility(View.VISIBLE);
            resaleCount.setText(String.valueOf(General.getBadgeCount(this, AppConstants.ADDB_COUNT_OR)));

        }



        if (!General.getSharedPreferences(getBaseContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
            Loadwatchlist();
        }





        add_catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (General.getSharedPreferences(getBaseContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {

//                    General.setSharedPreferences(this, AppConstants.ROLE_OF_USER, "client");
                    SignUpFragment d = new SignUpFragment();
                    Bundle bundle = new Bundle();
                    if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        bundle.putString("lastFragment", "clientDrawer");
                    else
                        bundle.putString("lastFragment", "brokerDrawer");
//                    loadFragment(signUpFragment, bundle, R.id.container_Signup1, "");
//                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_Signup)).commit();
                    d.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);

                    fragmentTransaction.addToBackStack("cardSignUp1");
                    //container_Signup1.setVisibility(View.VISIBLE);
                    if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        fragmentTransaction.replace(R.id.container_Signup1, d);
                    else
                        fragmentTransaction.replace(R.id.container_sign, d);
//                    signUpCardFlag = true;
                    fragmentTransaction.commitAllowingStateLoss();
                    AppConstants.SIGNUP_FLAG = true;

                } else if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
                    WatchlistExplorer watchlistExplorer=new WatchlistExplorer();
                    loadFragmentAnimated(watchlistExplorer,null,R.id.container_Signup1,"");
                    /*General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, "PC");
                    Intent in = new Intent(getBaseContext(), ClientMainActivity.class);
                    startActivity(in);*/




                } else {
                    /*General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, "PC");
                    Intent in = new Intent(getBaseContext(), BrokerMap.class);
                    startActivity(in);*/

                    WatchlistExplorer watchlistExplorer=new WatchlistExplorer();
                    loadFragmentAnimated(watchlistExplorer,null,R.id.container_sign,"");
                }

            }
        });



        add_building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (General.getSharedPreferences(getBaseContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
                    SignUpFragment d = new SignUpFragment();
                    Bundle bundle = new Bundle();
                    if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        bundle.putString("lastFragment", "clientDrawer");
                    else
                        bundle.putString("lastFragment", "brokerDrawer");
                    d.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);

                    fragmentTransaction.addToBackStack("cardSignUp1");
                    //container_Signup1.setVisibility(View.VISIBLE);
                    if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        fragmentTransaction.replace(R.id.container_Signup1, d);
                    else
                        fragmentTransaction.replace(R.id.container_sign, d);
//                    signUpCardFlag = true;
                    fragmentTransaction.commitAllowingStateLoss();
                    AppConstants.SIGNUP_FLAG = true;

                } else if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
                    General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, "PC");
                    Intent in = new Intent(getBaseContext(), ClientMainActivity.class);
                    startActivity(in);

                } else {
                    General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, "PC");
                    Intent in = new Intent(getBaseContext(), BrokerMap.class);
                    startActivity(in);
                }

            }
        });

        realm = General.realmconfig(getBaseContext());
        adapter = new porfolioAdapter(getBaseContext(), portListing);
        rental_list.setAdapter(adapter);


        RealmResults<WatchListRealmModel> watch = realm.where(WatchListRealmModel.class).findAll();

        for (WatchListRealmModel c : watch) {

            portListingModel portListingModel = new portListingModel(c.getWatchlist_id(),c.getWatchlist_name(),c.getImageuri(),"watchlist");
            watchlist.add(portListingModel);

        }






        RealmResults<Localities> results2 = realm.where(Localities.class).findAllSorted("timestamp", false);

        for (Localities c : results2) {


            portListingModel portListingModel = new portListingModel(c.getLocality(), c.getType(), ((Integer.parseInt(c.getLlMin()) + Integer.parseInt(c.getLlMax())) / 2), 0, c.getTimestamp(), c.getGrowthRate(), "LOCALITIES");

            myLocalitiesLL.add(portListingModel);
            // portListingModel portListingModel1 = new  portListingModel(c.getLocality(),"budget based suggestion",0,((Integer.parseInt(c.getOrMin()) + Integer.parseInt(c.getOrMax()))/2),c.getTimestamp(),c.getGrowthRate(),"LOCALITIES");

            //myLocalitiesOR.add(portListingModel1);


        }

        RealmResults<MyPortfolioModel> results = realm.where(MyPortfolioModel.class).equalTo("tt", "ll").findAllSorted("timestamp", false);

        for (MyPortfolioModel c : results) {


            portListingModel portListingModel = new portListingModel(c.getId(), c.getName(), c.getLocality(), c.getRate_growth(), c.getLl_pm(), c.getOr_psf(), c.getTimestamp(), c.getTransactions(), c.getConfig(), null, "ll");
            myPortfolioLL.add(portListingModel);


        }


        RealmResults<MyPortfolioModel> results1 = realm.where(MyPortfolioModel.class).equalTo("tt", "or").findAllSorted("timestamp", false);
        for (MyPortfolioModel c : results1) {


            portListingModel portListingModel = new portListingModel(c.getId(), c.getName(), c.getLocality(), c.getRate_growth(), c.getLl_pm(), c.getOr_psf(), c.getTimestamp(), c.getTransactions(), c.getConfig(), null, "or");

            myPortfolioOR.add(portListingModel);


        }


        RealmResults<addBuildingRealm> result11 = realm.where(addBuildingRealm.class).equalTo("display_type", "both").findAllSorted("timestamp", false);
        for (addBuildingRealm c : result11) {

            Log.i("getLocality", "getLocality   : " + c.getLocality());
            portListingModel portListingModel = new portListingModel(c.getId(), c.getBuilding_name(), c.getSublocality(), c.getGrowth_rate(), c.getLl_pm(), 0, c.getTimestamp(), null, c.getConfig(), c.getDisplay_type(), null);

            addbuildingLL.add(portListingModel);


        }

        /*RealmResults<addBuildingRealm> result1= realm.where(addBuildingRealm.class).equalTo("tt", "ll").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result1){

            Log.i("getLocality","getLocality   : "+c.getLocality());
            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),c.getLl_pm(),0,c.getTimestamp(),null,c.getConfig(),c.getDisplay_type(),"ll");

            addbuildingLL.add(portListingModel);


        }*/


//    public portListingModel(String id, String name, String locality, String growth_rate, int ll_pm, int or_psf, String timpstamp, String transaction, String config) {
        RealmResults<addBuildingRealm> result22 = realm.where(addBuildingRealm.class).equalTo("display_type", "both").findAllSorted("timestamp", false);
        for (addBuildingRealm c : result22) {

            portListingModel portListingModel = new portListingModel(c.getId(), c.getBuilding_name(), c.getSublocality(), c.getGrowth_rate(), 0, c.getOr_psf(), c.getTimestamp(), null, c.getConfig(), c.getDisplay_type(), null);
            addbuildingOR.add(portListingModel);

        }
        /*RealmResults<addBuildingRealm> result2= realm.where(addBuildingRealm.class).equalTo("tt", "or").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result2){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),0,c.getOr_psf(),c.getTimestamp(),null,c.getConfig(),c.getDisplay_type(),"or");

            addbuildingOR.add(portListingModel);


        }*/

        Log.i("dataritesh", "myPortfolioLL" + myPortfolioLL);//watchlist

        portListing.addAll(watchlist);
        portListing.addAll(myLocalitiesLL);
        portListing.addAll(addbuildingLL);
        portListing.addAll(myPortfolioLL);
        portListingCopy.addAll(portListing);

        adapter.notifyDataSetChanged();
        if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
            getSupportActionBar().setTitle("");
            confirm_screen_title.setText("My Marketing \nCatalog");
            inputSearch.setHint("Search " + portListing.size() + " Catalog");
           // usertext.setHint("\"My Listing\"");
           // add_create.setText("Create");
        } else {
            getSupportActionBar().setTitle("");
            confirm_screen_title.setText("My WatchList");
            inputSearch.setHint("Search " + portListing.size() + " Building in Watchlist");
            //usertext.setHint("Your Building");
           // add_create.setText("Add");

        }
        // inputSearch.setHint("Search My "+portListing.size()+" Listings");
        Log.i("dataritesh", "myPortfolioLL" + portListing);
//        portListing.addAll(myPortfolioLL);

       /* adapter = new myPortfolioAdapter(this,1);
        rental_list.setAdapter(adapter);
        rental_list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i( "portfolio1","onItemClick   : "+parent+" "+position+" "+id);

            }
        } );*/


        rental_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("zyzz", "zyzz 1");
                if (watchlist.contains(portListing.get(position))) {
                    Bundle b=new Bundle();
                    b.putString("watchlist_id",portListing.get(position).getWatchlist_id()+"");
                    Log.i("datafromraelm1", "realm data 1  :"+ portListing.get(position).getWatchlist_id()+"");
                    WatchlistDisplayBuilding watchlistDisplayBuilding=new WatchlistDisplayBuilding();

                    if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
                        loadFragmentAnimated(watchlistDisplayBuilding,b,R.id.container_Signup1,"");
                    }else {
                        loadFragmentAnimated(watchlistDisplayBuilding,b,R.id.container_sign,"");
                    }

                }else if (myLocalitiesLL.contains(portListing.get(position))) {
                    Log.i("zyzz", "zyzz 2");
                    Realm myRealm = General.realmconfig(MyPortfolioActivity.this);
                    Localities result = myRealm.where(Localities.class).equalTo("timestamp", portListing.get(position).getTimpstamp()).findFirst();
                    AppConstants.MY_LATITUDE = Double.parseDouble(result.getLat());
                    AppConstants.MY_LONGITUDE = Double.parseDouble(result.getLng());
                    Intent in = new Intent(getBaseContext(), ClientMainActivity.class);
                    in.putExtra(AppConstants.RESETMAP, "yes");
                    in.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                /*Intent intent = new Intent(AppConstants.RESETMAP);
                LocalBroadcastManager.getInstance(MyPortfolioActivity.this).sendBroadcast(intent);*/
                } else {
                    if (item != null)
                        item.clear();
                    item.add((portListingModel) adapter.getItem(position));
                    String ids = ((portListingModel) adapter.getItem(position)).getId();
                    Log.i("portfolio1", "portListingModel   : " + position + " " + ids);

                    RealmResults<MyPortfolioModel> result = realm.where(MyPortfolioModel.class).findAll();
                    for (MyPortfolioModel c : result) {
                        Log.i("portfolio1", "portListingModel inside for loop  : " + position + " " + ids + " " + c.getId());
                        if (ids.equalsIgnoreCase(c.getId())) {
                            Log.i("portfolio1", "portListingModel inside if  : " + position + " " + ids);
                            if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
                                General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, ids);
                                Intent in = new Intent(getBaseContext(), ClientMainActivity.class);
                                in.putExtra("id", ids);
                                in.putExtra("Cmarkerflag", "true");
                                in.addFlags(
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP);/*|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                startActivity(in);

                                break;
                            } else {
                                General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, ids);
                                Intent in = new Intent(getBaseContext(), BrokerMap.class);
                                in.putExtra("id", ids);
                                in.putExtra("Bmarkerflag", "true");
                                in.addFlags(
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP);/*|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);*/
                                startActivity(in);
                                break;
                            }
                        }
                    }

                }


            }
        });


        rental_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        rental_list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Prints the count of selected Items in title
                mode.setTitle(rental_list.getCheckedItemCount() + " Selected");


                Log.i("portfolio1", "onItemCheckedStateChanged   : " + position + " " + id + " " + portListing.contains(adapter.getItem(position)));
//                        portListing.contains(adapter.getItem(position));
//                        ids.add(adapter.getItem(position));

                deletelist.add((portListingModel) adapter.getItem(position));

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                        toolbar.setVisibility(View.GONE);
                mode.getMenuInflater().inflate(R.menu.main1, menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        /*if (item.getItemId() == R.id.delete){
                                adapter.removeSelection(ids);
                            ids.clear();
                            mode.finish();
                            return true;
                        }else{
                              for (int i=0;i<adapter.getCount();i++) {
                                  ids.add( adapter.getItem( position ).getId() );
                              }

                        }*/
                if (item.getItemId() == R.id.delete) {
                    General.setBadgeCount(MyPortfolioActivity.this, AppConstants.PORTFOLIO_COUNT, 0);
                    for (final portListingModel d : deletelist) {

                        // Here your room is available
                        Log.i("portfolio1", "deletelist" + portListing.contains(d) + " " + d.getName());
                        Log.i("addbuildingOR", "addbuildingOR 1" + addbuildingOR.contains(d));
                        portListing.remove(d);

                        myPortfolioLL.remove(d);
                        myPortfolioOR.remove(d);
                        portListingCopy.remove(d);
                        myLocalitiesLL.remove(d);
                        myLocalitiesOR.remove(d);
                        if (addbuildingLL.contains(d)) {
                            addbuildingLL.remove(d);
                            matchedId = d.getId();
                            Log.i("portfolio1", "deletelist 23 " + matchedId);


                            for (final portListingModel l : addbuildingOR) {
                                Log.i("portfolio1", "deletelist 25 " + l.getId());
                                if (l.getId().equalsIgnoreCase(matchedId)) {
                                    addbuildingOR.remove(l);
                                    break;
                                }
                            }

                        }

                        if (addbuildingOR.contains(d)) {
                            addbuildingOR.remove(d);
                            matchedId = d.getId();
                            Log.i("portfolio1", "deletelist 33 " + matchedId);


                            for (final portListingModel l : addbuildingLL) {
                                Log.i("portfolio1", "deletelist 35 " + l.getId());
                                if (l.getId().equalsIgnoreCase(matchedId)) {
                                    addbuildingLL.remove(l);
                                    break;
                                }
                            }


                        }


                        if(watchlist.contains(d)){

                            matchedId = d.getWatchlist_id();
                            for (final portListingModel l : watchlist) {
                                Log.i("portfolio11", "deletelist 35  watchlist" + l.getWatchlist_id());
                                if (l.getWatchlist_id().equalsIgnoreCase(matchedId)) {
                                    watchlist.remove(l);
                                    break;
                                }
                            }
                            watchlist_id=d.getWatchlist_id();
                            new RemoveWatchlist().execute();
                        }
                        Log.i("addbuildingOR", "addbuildingOR 2" + addbuildingOR);


                        try {
                            Realm myRealm = General.realmconfig(MyPortfolioActivity.this);
                            MyPortfolioModel result = myRealm.where(MyPortfolioModel.class).equalTo("id", d.getId()).findFirst();
                            if (myRealm.isInTransaction())
                                myRealm.cancelTransaction();
                            myRealm.beginTransaction();
                            result.removeFromRealm();
                            myRealm.commitTransaction();
                        } catch (Exception e) {
                            Log.i("TAG", "caught in exception deleting default droom 21 " + e);
                        }
                        try {
                            Realm myRealm = General.realmconfig(MyPortfolioActivity.this);
                            addBuildingRealm result = myRealm.where(addBuildingRealm.class).equalTo("id", d.getId()).findFirst();
                            if (myRealm.isInTransaction())
                                myRealm.cancelTransaction();
                            myRealm.beginTransaction();
                            result.removeFromRealm();
                            myRealm.commitTransaction();
                        } catch (Exception e) {
                            Log.i("TAG", "caught in exception deleting default droom 31 " + e);
                        }
                        try {
                            Realm myRealm = General.realmconfig(MyPortfolioActivity.this);
                            WatchListRealmModel result = myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id", d.getWatchlist_id()).findFirst();
                            if (myRealm.isInTransaction())
                                myRealm.cancelTransaction();
                            myRealm.beginTransaction();
                            result.removeFromRealm();
                            myRealm.commitTransaction();

                        } catch (Exception e) {
                            Log.i("TAG", "caught in exception deleting default droom 31 " + e);
                        }

                        try {
                            Realm myRealm = General.realmconfig(MyPortfolioActivity.this);
                            Localities result = myRealm.where(Localities.class).equalTo("timestamp", d.getTimpstamp()).findFirst();
                            if (myRealm.isInTransaction())
                                myRealm.cancelTransaction();
                            myRealm.beginTransaction();
                            result.removeFromRealm();
                            myRealm.commitTransaction();
                        } catch (Exception e) {
                            Log.i("TAG", "caught in exception deleting default droom 31 " + e);
                        }



                    }
                    mode.finish();
                    adapter.notifyDataSetChanged();
                    if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                        inputSearch.setHint("Search " + portListing.size() + " Catalog");
                       // usertext.setText("");
                       // usertext.setHint("\"My Listing\"");
                       // add_create.setText("Create");
                    } else {
                        inputSearch.setHint("Search " + portListing.size() + " Building in Watchlist");
                       // usertext.setText("");
                       // usertext.setHint("Your Building");
                       // add_create.setText("Add");

                    }
                    return true;

                            /*for(deletelist.size()
                            adapter.removeSelection(ids);
                            ids.clear();
                            mode.finish();
                            return true;
                        }else{
                            for (int i=0;i<adapter.getCount();i++) {
                                ids.add( adapter.getItem( position ).getId() );
                            }*/

                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
//                        toolbar.setVisibility(View.VISIBLE);
            }
        });




        /*realm = General.realmconfig(this);
        adapter.setResults(realm.where(MyPortfolioModel.class).greaterThan( "ll_pm",0 ).findAll());

  inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(TT=="LL"){
                    Log.i( "portfolio","onTextChanged  LL : "+cs );
                adapter.setResults( realm.where(MyPortfolioModel.class)
                        .greaterThan("ll_pm", 0)  //implicit AND
                        .beginGroup()
                        .contains("name", cs.toString(),false)
                        .endGroup()
                        .findAll() );
                }else{

                    adapter.setResults( realm.where(MyPortfolioModel.class)
                            .greaterThan("or_psf", 0)  //implicit AND
                            .beginGroup()
                            .contains("name", cs.toString(),false)
                            .endGroup()
                            .findAll() );
                    Log.i( "portfolio","onTextChanged  LL : ");

                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(TT=="LL"){
                    Log.i( "portfolio","onTextChanged  LL : "+cs );
                adapter.setResults( realm.where(MyPortfolioModel.class)
                        .greaterThan("ll_pm", 0)  //implicit AND
                        .beginGroup()
                        .contains("name", cs.toString(),false)
                        .endGroup()
                        .findAll() );
                }else{

                    adapter.setResults( realm.where(MyPortfolioModel.class)
                            .greaterThan("or_psf", 0)  //implicit AND
                            .beginGroup()
                            .contains("name", cs.toString(),false)
                            .endGroup()
                            .findAll() );
                    Log.i( "portfolio","onTextChanged  LL : ");

                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });*/


        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchQuery = s.toString().trim();
                Log.i("searcho", "s " + searchQuery.length());
                Log.i("searcho", "sb " + portListingCopy + " ============ : " + inputSearch.getText().toString().equalsIgnoreCase(""));

               if(!searchQuery.equalsIgnoreCase("")) {
                   if (portListing != null)
                       portListing.clear();
                   portListing.addAll(portListingCopy);
                   Log.i("searcho", "sc " + portListing);
                   for (portListingModel c : portListingCopy) {
                       Log.i("searcho", "sd " + c.getLl_pm() + " " + c.getOr_psf() + " ");
                       if (!c.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                           portListing.remove(c);
                       } else if (c.getLl_pm() != 0 && c.getOr_psf() != 0) {
                           if (TT.equalsIgnoreCase("LL")) {
                               if (c.getLl_pm() == 0) {
                                   portListing.remove(c);
                               }
                           } else if (TT.equalsIgnoreCase("OR")) {
                               if (c.getOr_psf() == 0) {
                                   portListing.remove(c);
                               }
                           }


                       }
                       Log.i("searcho", "sd " + portListing);
                       adapter.notifyDataSetChanged();

                   }
               }
            }

            @Override
            public void afterTextChanged(Editable s) {
          /*if(portListing != null)
              portListing.clear();
              portListing.addAll(portListingCopy);*/
                Log.i("search12", "sb outside " + s + " ============ : " + inputSearch.getText().toString().equalsIgnoreCase(""));
                /*String s1 = "\"" + s + "\"";
                if (s.toString().equalsIgnoreCase("")) {
                   // usertext.setText(s);
                    Log.i("search12", "sb inside " + s + " ============ : ");
                    if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        usertext.setHint("Your Building");
                    else
                        usertext.setHint("My Listing");
                } else {
                    usertext.setText(s1);

                }*/
            }
        });


    }


    @Override
    public void onDrawerItemSelected(View view, int position, String itemTitle) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        if (itemTitle.equals(getString(R.string.useAsClient))) {
            // dbHelper = new DBHelper(getBaseContext());
            //dbHelper.save(DatabaseConstants.userRole,"client");
            General.setSharedPreferences(this, AppConstants.ROLE_OF_USER, "client");
            Intent openDashboardActivity = new Intent(this, ClientMainActivity.class);
            openDashboardActivity.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(openDashboardActivity);
        } else if (itemTitle.equals(getString(R.string.profile))) {
            Intent openProfileActivity = new Intent(this, ProfileActivity.class);
            startActivity(openProfileActivity);
        } else if (itemTitle.equals(getString(R.string.brokerOk))) {
            //don't do anything
        } else if (itemTitle.equals(getString(R.string.shareApp))) {
            if (General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")) {
                SignUpFragment signUpFragment = new SignUpFragment();
                // signUpFragment.getView().bringToFront();
                Bundle bundle = new Bundle();
                bundle.putStringArray("Chat", null);
                bundle.putString("lastFragment", "brokerDrawer");
                loadFragmentAnimated(signUpFragment, bundle, R.id.container_sign, "");
            } else
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
        } else if (itemTitle.equals(getString(R.string.likeOnFb))) {
            // setContentView(R.layout.browser);
            webView = (WebView) findViewById(R.id.webView);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("http://www.facebook.com/hioyeok");


        } else if (itemTitle.equals(getString(R.string.aboutUs))) {
            //setContentView(R.layout.browser);
            webView = (WebView) findViewById(R.id.webView);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("http://www.hioyeok.com/blog");


        } else if (itemTitle.equals(getString(R.string.settings))) {
            AppSetting appSetting = new AppSetting();
            setting = true;
            loadFragmentAnimated(appSetting, null, R.id.container_sign, "");


        } else if (itemTitle.equals(getString(R.string.RegisterSignIn))) {
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
            //Owner_detail=true;
        }
        else if (itemTitle.equals(getString(R.string.Listing))) {
            Log.i("myWatchList", "itemTitle 1 " + itemTitle + R.string.Listing);
            Intent intent = new Intent(this, MyPortfolioActivity.class);
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

        if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
            if (drawerFragment.handle(item))
                return true;
        }else{
            onBackPressed();
            return true;
        }
        return false;
    }


    @OnClick(R.id.btnMyDeals1)
    public void onBtnMyDealsClick(View v) {

        // if(btnMyDeals.getText().toString().equalsIgnoreCase("share")) {

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Log.i(TAG,"persy 12345");
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            //dashboardClientFragment.screenShot();

            if (portListing.size() == 0) {
                new AlertDialog.Builder(this)
                        .setTitle("Empty List")
                        .setMessage("Please add building to take Snapshot!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                            }
                        })
                        /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })*/
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else
                takeScreenshot();
        }

    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;

    }*/

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
            //Log.i(TAG,"flaga isa 6 ");
            if (AppConstants.REGISTERING_FLAG) {
            } else {
                // getSupportFragmentManager().popBackStack();
//                getSupportFragmentManager().
                if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
                }else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_Signup1)).commit();
                }
                AppConstants.SIGNUP_FLAG = false;
            }
            Log.i("sushil123", " main activity =================== SIGNUP_FLAGffffffff");
        } else if(setting==true){
            Log.i("BACKsPRESSED"," =================== setting portfolio"+setting);
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
            Log.i("BACKsPRESSED", "loadFragment setting client4 " + getFragmentManager().getBackStackEntryCount());
                setting = false;


        }else
        if(webView != null){
            if (webView.canGoBack()) {
                webView.goBack();
            }
            else {
                webView = null;
                Intent inten = new Intent(this, MyPortfolioActivity.class);
                inten.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inten);
                finish();

                //backpress = 0;
            }
        }else if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
            Intent in = new Intent(getBaseContext(), BrokerMainActivity.class);
            in.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        } else {
            Intent in = new Intent(getBaseContext(), ClientMainActivity.class);
            in.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }
    }


    @Override
    public void onPositionSelected(int position, int count) {
        inputSearch.setText("");
        if (position == 0) {
            TT = "LL";
            portListing.clear();
            portListing.addAll(watchlist);
            portListing.addAll(addbuildingLL);
            portListing.addAll(myPortfolioLL);
            portListing.addAll(myLocalitiesLL);
            portListingCopy.clear();
            portListingCopy.addAll(portListing);
            AppConstants.TT_TYPE = "ll";

            adapter.notifyDataSetChanged();
            if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                inputSearch.setHint("Search " + portListing.size() + " Catalog");
                /*usertext.setText("");
                usertext.setHint("\"My Listing\"");*/
//                add_create.setText("Create");
            } else {
                inputSearch.setHint("Search " + portListing.size() + " Building in Watchlist");
                /*usertext.setText("");
                usertext.setHint("Your Building");*/
//                add_create.setText("Add");

            }
            General.setBadgeCount(this, AppConstants.ADDB_COUNT_LL, 0);
            rentalCount.setVisibility(View.GONE);
        } else {
            TT = "OR";
            AppConstants.TT_TYPE = "or";
            Log.i("addbuildingOR", "addbuildingOR 3" + addbuildingOR);
            portListing.clear();
            portListing.addAll(watchlist);
            portListing.addAll(myLocalitiesOR);
            portListing.addAll(addbuildingOR);
            portListing.addAll(myPortfolioOR);
            portListingCopy.clear();
            portListingCopy.addAll(portListing);
            adapter.notifyDataSetChanged();
            if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                inputSearch.setHint("Search " + portListing.size() + " Catalog");
                /*usertext.setText("");
                usertext.setHint("\"My Listing\"");*/
//                add_create.setText("Create");
            } else {
                inputSearch.setHint("Search " + portListing.size() + " Building in Watchlist");
                /*usertext.setText("");
                usertext.setHint("Your Building");*/
//                add_create.setText("Add");
            }
            General.setBadgeCount(this, AppConstants.ADDB_COUNT_OR, 0);
            resaleCount.setVisibility(View.GONE);
        }




       /* if(position == 0) {
            General.setSharedPreferences(this, AppConstants.TT, AppConstants.RENTAL);
            TT = "LL";

            Log.i( "portfolio","position : "+position );
           adapter = new myPortfolioAdapter(this,1);
            rental_list.setAdapter(adapter);
            adapter.setResults(realm.where(MyPortfolioModel.class).notEqualTo("ll_pm", 0).findAll());

        }
        else{
            General.setSharedPreferences(this, AppConstants.TT, AppConstants.RESALE);
            TT = "OR";
            Log.i( "portfolio","position : "+position );
            adapter = new myPortfolioAdapter(this,2);
            rental_list.setAdapter(adapter);
            adapter.setResults(realm.where(MyPortfolioModel.class).notEqualTo("or_psf", 0).findAll());
        }*/

    }




    /*private void loadFragment(Fragment fragment, Bundle args, int containerId, String title)
    {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(title);
        Log.i("SIGNUP_FLAG","SIGNUP_FLAG=========  loadFragment client "+getFragmentManager().getBackStackEntryCount());
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }*/

    private void loadFragment(Fragment fragment, Bundle args, int containerId, String title) {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }


    private void openScreenshot(File imageFile) {
        // Log.i(TAG,"persy 1234");
        int permission = ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

       /* if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"persy 12345");
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }*/
        // Log.i(TAG,"persy 12346");

        Uri uri = Uri.fromFile(imageFile);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg/text/html");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        //intent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>Hey, please check out these property rates I found out on this super amazing app Oyeok.</p><p><a href=\"https://play.google.com/store/apps/details?id=com.nbourses.oyeok&hl=en/\">Download Oyeok for android</a></p>"));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, please check out these property rates I found out on this super amazing app Oyeok. \n \n  https://play.google.com/store/apps/details?id=com.nbourses.oyeok&hl=en/");
        startActivity(Intent.createChooser(intent, "Share Image"));

//        Spanned spanned = Html.fromHtml(code, this, null);
    }




















   /*TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        TabLayout.Tab tab1=tabLayout.newTab();
        TabLayout.Tab tab2=tabLayout.newTab();
        tabLayout.addTab(tab1.setText("Rental"));
        tabLayout.addTab(tab2.setText("Buy/Sell"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);*/

     /*   viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(adapter);*/


        /*tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                TabFragment1 tabFragment1=new TabFragment1();

                loadFragment(tabFragment1, null, R.id.container_map, "Client Dashboard");

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/


    private void shareReferralLink() {
        //DBHelper dbHelper=new DBHelper(getApplicationContext());
        //String user_id = dbHelper.getValue(DatabaseConstants.userId);
        String user_id = General.getSharedPreferences(getBaseContext(), AppConstants.USER_ID);

        Branch branch = Branch.getInstance(getApplicationContext());

        String mob_no = General.getSharedPreferences(this, AppConstants.MOBILE_NUMBER);
        Log.i("mob_no", "mob_no " + mob_no);

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


   public void closeWatchlistFragment(){
       if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
           getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_Signup1)).commit();

       }else {
           getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
       }
   }


    public ArrayList<loadBuildingDataModel> passingListActivity(){
       // ArrayList<loadBuildingDataModel> selectedlist = new ArrayList<>();
        //selectedlist.addAll(((WatchlistExplorer) getSupportFragmentManager().findFragmentById(R.id.container_map)).passingListFragA());
        return (selectedlist);
    }

    public void OpenFrag(ArrayList<loadBuildingDataModel> selectedlist1 ){
        for(int i=0;i<selectedlist.size();i++){
            Log.i("selected111","selected building init : "+selectedlist.size()+"   === "+selectedlist.get(i).getName() );

        }
        selectedlist.clear();
        selectedlist.addAll(selectedlist1);
        Log.i("selected111","selected building init : "+selectedlist.size() );
        if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_Signup1)).commit();
            WatchlistTitle watchlistTitle=new WatchlistTitle();
            loadFragmentAnimated(watchlistTitle,null,R.id.container_Signup1,"");
        }else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
            WatchlistTitle watchlistTitle=new WatchlistTitle();
            loadFragmentAnimated(watchlistTitle,null,R.id.container_sign,"");
        }




    }


    public void Back(String edit){
        Bundle b=new Bundle();
        b.putString("edit",edit);
        if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_Signup1)).commit();
            WatchlistExplorer watchlistExplorer=new WatchlistExplorer();
            if(edit.equalsIgnoreCase(""))
            loadFragmentAnimated(watchlistExplorer,null,R.id.container_Signup1,"");
            else
                loadFragmentAnimated(watchlistExplorer,b,R.id.container_Signup1,"");
        }else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
            WatchlistExplorer watchlistExplorer=new WatchlistExplorer();
            if(edit.equalsIgnoreCase(""))
            loadFragmentAnimated(watchlistExplorer,null,R.id.container_sign,"");
            else
                loadFragmentAnimated(watchlistExplorer,b,R.id.container_sign,"");

        }
        adapter.notifyDataSetChanged();

    }

    public void Refresh(String read){
        Bundle b=new Bundle();
        b.putString("read",read);
        if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_Signup1)).commit();
            WatchlistDisplayBuilding watchlistDisplayBuilding=new WatchlistDisplayBuilding();
            loadFragmentAnimated(watchlistDisplayBuilding,b,R.id.container_Signup1,"");
        }else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
            WatchlistDisplayBuilding watchlistDisplayBuilding=new WatchlistDisplayBuilding();
            loadFragmentAnimated(watchlistDisplayBuilding,b,R.id.container_sign,"");
        }
        recreate();
        /*WatchlistExplorer watchlistExplorer=new WatchlistExplorer();
        loadFragmentAnimated(watchlistExplorer,null,R.id.container_sign,"");*/
    }

    public void Close(){
        if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_Signup1)).commit();

        }else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
        }

        recreate();

        /*WatchlistExplorer watchlistExplorer=new WatchlistExplorer();
        loadFragmentAnimated(watchlistExplorer,null,R.id.container_sign,"");*/
    }

    protected class RemoveWatchlist extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            ReadWatchlistAPI readWatchlistAPI=new ReadWatchlistAPI();
            readWatchlistAPI.setUser_id(General.getSharedPreferences(getBaseContext(), AppConstants.USER_ID));
            readWatchlistAPI.setAction("remove");
            readWatchlistAPI.setCity("mumbai");
            readWatchlistAPI.setTt("ll");
            //readWatchlistAPI.setBuild_list(ids);
            Log.i("magic111","addBuildingRealm success response "+watchlist_id);
            readWatchlistAPI.setWatchlist_id(watchlist_id);
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
            oyeokApiService.ReadWatchlist(readWatchlistAPI, new retrofit.Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());

                    try {
                        JSONObject jsonResponse = new JSONObject(strResponse);
                        Log.i("magic111","addBuildingRealm success response "+response+"\n"+jsonResponse);
                        JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                        Log.i("magic111","addBuildingRealm success response "+building);
                        //watchlist_id=building.getString("watchlist_id");
                        JSONArray buildingdata=new JSONArray(building.getString("buildings"));
                        Log.i("magic111","addBuildingRealm success response1 "+buildingdata);
                        Log.i("magic111","addBuildingRealm success response2 "+buildingdata.getString(0));
                        // AddDataToRealm(watchlist_id);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


            return null;
        }


    }



    /*private void RemoveWatchlist(){
        ReadWatchlistAPI readWatchlistAPI=new ReadWatchlistAPI();
        readWatchlistAPI.setUser_id(General.getSharedPreferences(getBaseContext(), AppConstants.USER_ID));
        readWatchlistAPI.setAction("remove");
        readWatchlistAPI.setCity("mumbai");
        readWatchlistAPI.setTt("ll");
        //readWatchlistAPI.setBuild_list(ids);
        Log.i("magic111","addBuildingRealm success response "+watchlist_id);
        readWatchlistAPI.setWatchlist_id(watchlist_id);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        oyeokApiService.ReadWatchlist(readWatchlistAPI, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());

                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    Log.i("magic111","addBuildingRealm success response "+response+"\n"+jsonResponse);
                    JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                    Log.i("magic111","addBuildingRealm success response "+building);
                    //watchlist_id=building.getString("watchlist_id");
                    JSONArray buildingdata=new JSONArray(building.getString("buildings"));
                    Log.i("magic111","addBuildingRealm success response1 "+buildingdata);
                    Log.i("magic111","addBuildingRealm success response2 "+buildingdata.getString(0));
                    // AddDataToRealm(watchlist_id);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }*/



    protected class DetailWatchlist extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

//    private void DetailWatchlist(){
            ids.clear();
            Log.i("magic1112", "addBuildingRealm success response " + ids);
            ReadWatchlistAPI readWatchlistAPI = new ReadWatchlistAPI();
            readWatchlistAPI.setUser_id(General.getSharedPreferences(getBaseContext(), AppConstants.USER_ID));
            readWatchlistAPI.setAction("details");
            readWatchlistAPI.setCity("mumbai");
            readWatchlistAPI.setTt("ll");
        /*
        readWatchlistAPI.setBuild_list(ids);
        readWatchlistAPI.setWatchlist_id("");*/
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
            oyeokApiService.ReadWatchlist(readWatchlistAPI, new retrofit.Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());

                    try {
                        JSONObject jsonResponse = new JSONObject(strResponse);
                        Log.i("magic1112", "addBuildingRealm success response " + response + "\n" + jsonResponse);
                        JSONArray building = new JSONArray(jsonResponse.getString("responseData"));
                        Log.i("magic1112", "addBuildingRealm success response " + building.length() + "\n" + building);
                        int size = building.length();
                       /* if (realmsids != null)
                            realmsids.clear();*/

                        if(size>0) {
                            for (int i = 0; i < size; i++) {
                                JSONObject j = new JSONObject(building.get(i).toString());
                                Log.i("magic1112", "addBuildingRealm success response  :::::  " + " i: " + i + "  ::  build_list  :: " + j.getJSONArray("build_list") + " ::\n " + j.getString("title"));

                                Log.i("magic1112", "addBuildingRealm success response  :::::  " + j.getJSONArray("build_list").length());

                                if (realmsids != null)
                                    realmsids.clear();
                                int size1 = j.getJSONArray("build_list").length();
                                for (int k = 0; k < size1; k++) {
                                    loadBuildingdataModelRealm loadBuildingdataModelRealm1 = new loadBuildingdataModelRealm(j.getJSONArray("build_list").get(k) + "");
                                    realmsids.add(loadBuildingdataModelRealm1);
                                    Log.i("magic1112", "addBuildingRealm success response  :::::  " + j.getJSONArray("build_list").get(k));
                                }

                                Log.i("magic1112", "addBuildingRealm success response  :::::  " + j.getString("title"));

                                AddDataToRealm(j.getString("title"), j.getString("watchlist_id"), j.getString("city"), j.getString("tt"), j.getString("user_id"), j.getString("user_role"), j.getString("user_name"));
                            }
                        }
                        //watchlist_id=building.getString("watchlist_id");
                    /*JSONArray buildingdata=new JSONArray(building.getString("buildings"));
                    Log.i("magic111","addBuildingRealm success response1 "+buildingdata);
                    Log.i("magic111","addBuildingRealm success response2 "+buildingdata.getString(0));*/
                        // AddDataToRealm(watchlist_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


            return null;
        }
    }

    void Loadwatchlist(){

        Log.i("magic1112","  Loadwatchlist =======================:  "+General.getSharedPreferences(getBaseContext(),AppConstants.IS_SIGNUP));

        if(General.getSharedPreferences(getBaseContext(),AppConstants.IS_SIGNUP).equalsIgnoreCase("true")){
            new DetailWatchlist().execute();
          General.setSharedPreferences(getBaseContext(),AppConstants.IS_SIGNUP,"false");
        }

    }



    private  void AddDataToRealm(String watchlist_name,String watchlist_id,String city,String tt,String user_id,String user_role,String  user_name){
       // WatchListRealmModel results2 = realm.where(WatchListRealmModel.class).equalTo("watchlist_id", watchlist_id).findFirst();
        WatchListRealmModel watchListRealmModel=new WatchListRealmModel();
        watchListRealmModel.setWatchlist_id(watchlist_id);
        watchListRealmModel.setBuildingids(realmsids);
        watchListRealmModel.setWatchlist_name(watchlist_name);
        watchListRealmModel.setCity(city);
        watchListRealmModel.setTt(tt);
        watchListRealmModel.setUser_id(user_id);
        watchListRealmModel.setUser_name(user_name);
        watchListRealmModel.setUser_role(user_role);//watchcontent
        watchListRealmModel.setDisplayBuildinglist(watchcontent);
        realm.beginTransaction();
        // WatchListRealmModel watchListRealmModel=myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id",watchlist_id).findFirst();
        realm.copyToRealmOrUpdate(watchListRealmModel);
        realm.commitTransaction();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}