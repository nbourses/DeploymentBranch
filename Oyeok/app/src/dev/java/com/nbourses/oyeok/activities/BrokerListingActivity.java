package com.nbourses.oyeok.activities;

import android.Manifest;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.JsonElement;
import com.nbourses.oyeok.MyApplication;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.adapters.BrokerListingListView;
import com.nbourses.oyeok.fragments.AddListingFinalCard;
import com.nbourses.oyeok.fragments.AppSetting;
import com.nbourses.oyeok.fragments.CatalogDisplayListing;
import com.nbourses.oyeok.fragments.ListingExplorer;
import com.nbourses.oyeok.fragments.ListingTitle;
import com.nbourses.oyeok.fragments.ShareOwnersNo;
import com.nbourses.oyeok.fragments.WatchlistExplorer;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.CreateCatalogListing;
import com.nbourses.oyeok.models.ReadWatchlistAPI;
import com.nbourses.oyeok.models.portListingModel;
import com.nbourses.oyeok.realmModels.ListingCatalogRealm;
import com.nbourses.oyeok.realmModels.MyPortfolioModel;
import com.nbourses.oyeok.realmModels.addBuildingRealm;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;

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
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class BrokerListingActivity extends BrokerMainPageActivity implements CustomPhasedListener ,FragmentDrawer.FragmentDrawerListener{



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

    /*@Bind(R.id.container_sign)
    FrameLayout container_sign;

    @Bind(R.id.card)
    FrameLayout card;*/

    CustomPhasedSeekBar  mPhasedSeekBar;
    int position=0;
    ViewPager viewPager;
    private Realm realm;
    ListView rental_list;
    MyPortfolioModel results;
    //    myPortfolioAdapter adapter;
    BrokerListingListView adapter;
    ArrayList<String> ids =new ArrayList<>(  );
    /*private static ArrayList<portListingModel> myPortfolioOR=new ArrayList<>();
    //    private static ArrayList<addBuildingRealm> addBuildingLL=new ArrayList<>();
    private static ArrayList<portListingModel> myPortfolioLL=new ArrayList<>();*/
    private static ArrayList<portListingModel> portListing=new ArrayList<>();
    private static ArrayList<portListingModel> portListingCopy=new ArrayList<>();
    private static ArrayList<portListingModel> addbuildingLL=new ArrayList<>();
    private static ArrayList<portListingModel> addbuildingOR=new ArrayList<>();
    private static ArrayList<portListingModel> deletelist=new ArrayList<>();
    private static ArrayList<portListingModel> item=new ArrayList<>();
    private static ArrayList<portListingModel> list=new ArrayList<>();
    private static ArrayList<portListingModel> cataloglistLL = new ArrayList<>();
    private static ArrayList<portListingModel> cataloglistOR = new ArrayList<>();
    private static ArrayList<portListingModel> catalogportListing=new ArrayList<>();

    RealmResults<addBuildingRealm> results2;

    EditText inputSearch;
    private String TT = "LL";
    LinearLayout add_build;
    private String matchedId,catalog_id,catalog_title;
//    TextView usertext,add_create;
    TextView  add_catalog,add_building;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    FrameLayout container_sign,card;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_broker_listing);
        LinearLayout dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);

//        NestedScrollView dynamicContent = (NestedScrollView) findViewById(R.id.myScrollingContent);
        // assuming your Wizard content is in content_wizard.xml myScrollingContent
        View wizard = getLayoutInflater().inflate(R.layout.activity_broker_listing, null);

        // add the inflated View to the layout
        dynamicContent.addView(wizard);

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.listing);
        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_listing_clicked, 0,0);
        //rb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_select_listing) , null, null);
        rb.setTextColor(Color.parseColor("#2dc4b6"));
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
        container_sign=(FrameLayout)findViewById(R.id.container_sign);
        card=(FrameLayout)findViewById(R.id.card);
        ButterKnife.bind(this);

        if(portListing != null)
            portListing.clear();
        if(cataloglistLL != null)
            cataloglistLL.clear();
        if(cataloglistOR != null)
            cataloglistOR.clear();
        if(addbuildingLL != null)
            addbuildingLL.clear();
        if(addbuildingOR != null)
            addbuildingOR.clear();
        if(portListingCopy != null)
            portListingCopy.clear();

        if(catalogportListing != null)
            catalogportListing.clear();

        try {
            MyApplication application = (MyApplication) getApplication();
            mTracker = application.getDefaultTracker();

            mTracker.setScreenName("BrokerListingActivity");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setActionBar(toolbar); only for above 21 API
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        confirm_screen_title.setVisibility(View.VISIBLE);
        btnMyDeals.setVisibility(View.GONE);
        btnMyDeals1.setVisibility(View.VISIBLE);
        btnMyDeals1.setBackground(getResources().getDrawable(R.drawable.snapshot));

        mPhasedSeekBar = (CustomPhasedSeekBar) findViewById(R.id.phasedSeekBar);
        mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(this.getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{this.getResources().getString(R.string.Rental), this.getResources().getString(R.string.Resale)}));
        mPhasedSeekBar.setListener(this);


        /*BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.matching).setChecked(false);
        menu.findItem(R.id.listing).setChecked(true);
        menu.findItem(R.id.listing).setIcon(R.drawable.ic_select_listing);*/
//        if (menu.findItem(R.id.listing).isChecked()) menu.findItem(R.id.listing).setChecked(false);
//        else menu.findItem(R.id.listing).setChecked(true);

        AppConstants.TT_TYPE = "ll";
        rental_list=(ListView) findViewById(R.id.Rental_listview);
        inputSearch=(EditText) findViewById( R.id.inputSearch1);
        add_build=(LinearLayout)findViewById(R.id.add_build);
        add_building = (TextView) findViewById(R.id.add_building);
        add_catalog = (TextView) findViewById(R.id.add_catalog);
//        usertext=(TextView)findViewById(R.id.usertext);
//        add_create=(TextView)findViewById(R.id.add_create);


        /*if ((General.getBadgeCount(this, AppConstants.ADDB_COUNT_LL) > 0) ) {
            rentalCount.setVisibility(View.VISIBLE);
            rentalCount.setText(String.valueOf(General.getBadgeCount(this, AppConstants.ADDB_COUNT_LL)));

        }
        if ((General.getBadgeCount(this, AppConstants.ADDB_COUNT_OR) > 0)) {
            resaleCount.setVisibility(View.VISIBLE);
            resaleCount.setText(String.valueOf(General.getBadgeCount(this, AppConstants.ADDB_COUNT_OR)));

        }*/

       /* add_build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (General.getSharedPreferences(getBaseContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {

//                    General.setSharedPreferences(this, AppConstants.ROLE_OF_USER, "client");
                    SignUpFragment d = new SignUpFragment();
                    Bundle bundle = new Bundle();
                    if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        bundle.putString("lastFragment", "clientDrawer");
                    else
                        bundle.putString("lastFragment", "brokerDrawer");
                    d.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);

                    fragmentTransaction.addToBackStack("cardSignUp1");
                    container_Signup1.setVisibility(View.VISIBLE);
                    fragmentTransaction.replace(R.id.container_sign, d);
//                    signUpCardFlag = true;
                    fragmentTransaction.commitAllowingStateLoss();
//                    AppConstants.SIGNUP_FLAG = true;

                }else{
                    General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, "PC");
                    Intent in = new Intent(getBaseContext(), BrokerMap.class);
                    startActivity(in);
                }

            }
        });*/





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
                    /*if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        fragmentTransaction.replace(R.id.container_Signup1, d);
                    else*/
                        fragmentTransaction.replace(R.id.container_sign, d);
//                    signUpCardFlag = true;
                    fragmentTransaction.commitAllowingStateLoss();
                    AppConstants.SIGNUP_FLAG = true;

                } else {

                    // Get tracker.
                   /* Tracker t = ((AnalyticsSampleApp) getActivity().getApplication()).getTracker(
                            TrackerName.APP_TRACKER);*/
// Build and send an Event.
                   /* mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Broker Listing")
                            .setAction("")
                            .setLabel("")
                            .build());*/
                    General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, "PC");
                    Intent in = new Intent(getBaseContext(), BrokerMap.class);
                    startActivity(in);
                }

            }
        });







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
                    /*if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        fragmentTransaction.replace(R.id.container_Signup1, d);
                    else*/
                        fragmentTransaction.replace(R.id.container_sign, d);
//                    signUpCardFlag = true;
                    fragmentTransaction.commitAllowingStateLoss();
                    AppConstants.SIGNUP_FLAG = true;

                }else {


                    ListingExplorer listingExplorer=new ListingExplorer();
                    loadFragmentAnimated(listingExplorer,null,R.id.container_sign,"");
                }

            }
        });















        realm= General.realmconfig( getBaseContext() );
        adapter=new BrokerListingListView(getBaseContext(),portListing);
        rental_list.setAdapter(adapter);



        RealmResults<ListingCatalogRealm> results = realm.where(ListingCatalogRealm.class).equalTo("tt","ll").findAll();

        for(ListingCatalogRealm c :results){

            portListingModel portListingModel = new portListingModel(c.getCatalog_id(),c.getCatalog_name(),c.getImageuri(),"catalog","ll");

            cataloglistLL.add(portListingModel);


        }
        RealmResults<ListingCatalogRealm> results1 = realm.where(ListingCatalogRealm.class).equalTo("tt","or").findAll();

        for(ListingCatalogRealm c :results){

            portListingModel portListingModel = new portListingModel(c.getCatalog_id(),c.getCatalog_name(),c.getImageuri(),"catalog","tt");

            cataloglistOR.add(portListingModel);


        }










       /* RealmResults<MyPortfolioModel> results= realm.where(MyPortfolioModel.class).equalTo("tt", "ll").findAllSorted("timestamp",false);

        for(MyPortfolioModel c :results){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getName(),c.getLocality(),c.getRate_growth(),c.getLl_pm(),c.getOr_psf(),c.getTimestamp(),c.getTransactions(),c.getConfig(),null,"ll");
            myPortfolioLL.add(portListingModel);


        }


        RealmResults<MyPortfolioModel> results1= realm.where(MyPortfolioModel.class).equalTo("tt", "or").findAllSorted("timestamp",false);
        for(MyPortfolioModel c :results1){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getName(),c.getLocality(),c.getRate_growth(),c.getLl_pm(),c.getOr_psf(),c.getTimestamp(),c.getTransactions(),c.getConfig(),null,"or");

            myPortfolioOR.add(portListingModel);


        }*/


        /*RealmResults<addBuildingRealm> result11= realm.where(addBuildingRealm.class).equalTo("display_type","both").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result11){

            Log.i("getLocality","getLocality   : "+c.getLocality());
            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),c.getLl_pm(),0,c.getTimestamp(),null,c.getConfig(),c.getDisplay_type(),null,false);

            addbuildingLL.add(portListingModel);


        }*/

        RealmResults<addBuildingRealm> result1= realm.where(addBuildingRealm.class).equalTo("tt", "ll").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result1){

            Log.i("getLocality","getLocality   : "+c.getLocality());
            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),c.getLl_pm(),0,c.getTimestamp(),null,c.getConfig(),c.getDisplay_type(),"ll",c.getReq_avl(),c.getFurnishing(),c.getMarket_rate(),c.getPossession_date(),false);

            addbuildingLL.add(portListingModel);


        }



//    public portListingModel(String id, String name, String locality, String growth_rate, int ll_pm, int or_psf, String timpstamp, String transaction, String config) {
        /*RealmResults<addBuildingRealm> result22= realm.where(addBuildingRealm.class).equalTo("display_type", "both").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result22){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),0,c.getOr_psf(),c.getTimestamp(),null,c.getConfig(),c.getDisplay_type(),null,false);

            addbuildingOR.add(portListingModel);


        }*/
        RealmResults<addBuildingRealm> result2= realm.where(addBuildingRealm.class).equalTo("tt", "or").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result2){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),0,c.getOr_psf(),c.getTimestamp(),null,c.getConfig(),c.getDisplay_type(),"or",c.getReq_avl(),c.getFurnishing(),c.getMarket_rate(),c.getPossession_date(),false);

            addbuildingOR.add(portListingModel);


        }

        //Log.i("dataritesh","myPortfolioLL"+myPortfolioLL);
        portListing.addAll(cataloglistLL);
        catalogportListing.addAll(addbuildingLL);
        portListing.addAll(catalogportListing);
       // portListing.addAll(myPortfolioLL);
        portListingCopy.addAll(portListing);

            adapter.notifyDataSetChanged();
            getSupportActionBar().setTitle("");
            confirm_screen_title.setText("My Advertised\nListings");
            inputSearch.setHint("Search "+portListing.size()+" Building in Listings");
           /* usertext.setHint("\"My Listing\"");
            add_create.setText("Create");*/


        rental_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                if (cataloglistLL.contains(portListing.get(position))|| cataloglistOR.contains(portListing.get(position))) {
                    Bundle b=new Bundle();
                    b.putString("catalog_id",portListing.get(position).getCatalog_id()+"");
                    Log.i("datafromraelm1", "realm data 1  :"+ portListing.get(position).getCatalog_id()+"");
                    CatalogDisplayListing catalogDisplayListing=new CatalogDisplayListing();

//                    if (General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
//                        loadFragmentAnimated(catalogDisplayListing,b,R.id.container_Signup1,"");
//                    }else {
                        loadFragmentAnimated(catalogDisplayListing,b,R.id.container_sign,"");
//                    }

                }else {


                    if (item != null)
                        item.clear();
                    item.add((portListingModel) adapter.getItem(position));
                    portListingModel p = (portListingModel) adapter.getItem(position);
                    String ids = p.getId();

                    try {
                        MyApplication application = (MyApplication) getApplication();
                        Tracker mTracker = application.getDefaultTracker();

                        mTracker.setScreenName("UpdateListing");
                        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    RealmResults<addBuildingRealm> result = realm.where(addBuildingRealm.class).findAll();
                    Log.i("portfolio1", "portListingModel   : " + position + " " + (portListingModel) adapter.getItem(position) + "  : " + "   " + p.getName() + " ::" + p.getId() + result);

                    for (addBuildingRealm c : result) {
                        Log.i("portfolio1", "portListingModel inside for loop  : " + position + " " + ids + " " + c.getId());
                        if (ids.equalsIgnoreCase(c.getId())) {
                            Log.i("portfolio11", "portListingModel inside if sushil  : " + position + " " + p.getName() + " : realm : " + c.getBuilding_name());

                            General.setSharedPreferences(getBaseContext(), AppConstants.BUILDING_NAME, p.getName());
                            General.setSharedPreferences(getBaseContext(), AppConstants.BUILDING_LOCALITY, p.getLocality() + "");
                            General.setSharedPreferences(getBaseContext(), AppConstants.MY_LAT, c.getLat() + "");
                            General.setSharedPreferences(getBaseContext(), AppConstants.MY_LNG, c.getLng() + "");
                            General.setSharedPreferences(getBaseContext(), AppConstants.MY_CITY, p.getCity());
                            General.setSharedPreferences(getBaseContext(), AppConstants.LL_PM, c.getServer_ll_pm() + "");
                            General.setSharedPreferences(getBaseContext(), AppConstants.OR_PSF, c.getServer_or_psf() + "");
                            General.setSharedPreferences(getBaseContext(), AppConstants.PROPERTY, "home");
                            General.setSharedPreferences(getBaseContext(), AppConstants.CONFIG, p.getConfig());
                            openAddListingFinalCard(p.getId());

                        }
                    }
                }

            }
        });


        rental_list.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE_MODAL);
        rental_list.setMultiChoiceModeListener( new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Prints the count of selected Items in title
                mode.setTitle(rental_list.getCheckedItemCount() + " Selected");


                Log.i( "portfolio1","onItemCheckedStateChanged   : "+position+" "+id+" "+portListing.contains(adapter.getItem(position)));
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
                if (item.getItemId() == R.id.delete){

                    for (final portListingModel d : deletelist) {

                        // Here your room is available
                        Log.i("portfolio1","deletelist"+portListing.contains(d)+" "+d.getName());
                        Log.i("addbuildingOR","addbuildingOR 1"+addbuildingOR.contains(d));
                        portListing.remove(d);
                       // myPortfolioLL.remove(d);
                       // myPortfolioOR.remove(d);
                        portListingCopy.remove(d);
                        if(addbuildingLL.contains(d)){
                            addbuildingLL.remove(d);
                            matchedId = d.getId();
                            Log.i("portfolio1","deletelist 23 "+matchedId);


                            for (final portListingModel l : addbuildingOR) {
                                Log.i("portfolio1","deletelist 25 "+l.getId());
                                if(l.getId().equalsIgnoreCase(matchedId)){
                                    addbuildingOR.remove(l);
                                    break;
                                }
                            }

                        }


                        if(addbuildingOR.contains(d)){
                            addbuildingOR.remove(d);
                            matchedId = d.getId();
                            Log.i("portfolio1","deletelist 33 "+matchedId);


                            for (final portListingModel l : addbuildingLL) {
                                Log.i("portfolio1","deletelist 35 "+l.getId());
                                if(l.getId().equalsIgnoreCase(matchedId)){
                                    addbuildingLL.remove(l);
                                    break;
                                }
                            }



                        }

                       // catalogportListing

                        if(catalogportListing.contains(d)){
                            catalogportListing.remove(d);
                        }

                        if(cataloglistLL.contains(d)){
                            cataloglistLL.remove(d);
                            matchedId = d.getCatalog_id();
                            Log.i("portfolio1","deletelist 23 "+matchedId);

                            for (final portListingModel l : cataloglistOR) {
                                Log.i("portfolio1","deletelist 25 "+l.getId());
                                if(l.getCatalog_id().equalsIgnoreCase(matchedId)){
                                    cataloglistOR.remove(l);
                                    break;
                                }
                            }

                            catalog_id=matchedId;
                            catalog_title=d.getName();
                            Log.i("addbuildingOR","addbuildingOR 2"+d.getName()+" :: "+catalog_id);
                         new RemoveCatalog().execute();

                        }

                        Log.i("addbuildingOR","addbuildingOR 2"+addbuildingOR);


                        try {
                            Realm myRealm = General.realmconfig(BrokerListingActivity.this);
                            MyPortfolioModel result = myRealm.where(MyPortfolioModel.class).equalTo("id", d.getId()).findFirst();
                            if(myRealm.isInTransaction())
                                myRealm.cancelTransaction();
                            myRealm.beginTransaction();
                            result.removeFromRealm();
                            myRealm.commitTransaction();
                        } catch (Exception e) {
                            Log.i("TAG", "caught in exception deleting default droom 21 "+e);
                        }
                        try {
                            Realm myRealm = General.realmconfig(BrokerListingActivity.this);
                            addBuildingRealm result = myRealm.where(addBuildingRealm.class).equalTo("id", d.getId()).findFirst();
                            if(myRealm.isInTransaction())
                                myRealm.cancelTransaction();
                            myRealm.beginTransaction();
                            result.removeFromRealm();
                            myRealm.commitTransaction();
                        } catch (Exception e) {
                            Log.i("TAG", "caught in exception deleting default droom 31 "+e);
                        }
                        try {
                            Realm myRealm = General.realmconfig(BrokerListingActivity.this);
                            ListingCatalogRealm result = myRealm.where(ListingCatalogRealm.class).equalTo("catalog_id",d.getCatalog_id()).findFirst();
                            if(myRealm.isInTransaction())
                                myRealm.cancelTransaction();
                            myRealm.beginTransaction();
                            result.removeFromRealm();
                            myRealm.commitTransaction();
                        } catch (Exception e) {
                            Log.i("TAG", "caught in exception deleting default droom 31 "+e);
                        }



                    }
                    mode.finish();
                    adapter.notifyDataSetChanged();
                    if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
                        inputSearch.setHint("Search "+portListing.size()+" Building in Listings");
                      /*  usertext.setText("");
                        usertext.setHint("\"My Listing\"");
                        add_create.setText("Create");*/
                    }else{
                        inputSearch.setHint("Search "+ portListing.size()+" Building in Watchlist");
                       /* usertext.setText("");
                        usertext.setHint("Your Building");
                        add_create.setText("Add");*/

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



        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchQuery = s.toString().trim();
                Log.i("searcho","s "+searchQuery.length());
                Log.i("searcho","sb "+portListingCopy+" ============ : "+inputSearch.getText().toString().equalsIgnoreCase(""));


                if(portListing != null)
                    portListing.clear();
                portListing.addAll(portListingCopy);
                Log.i("searcho","sc "+portListing);
                for(portListingModel c :portListingCopy){
                    Log.i("searcho", "sd " + c.getLl_pm() + " "+ c.getOr_psf() +" ");
                    if(!c.getName().toLowerCase().contains(searchQuery.toLowerCase())){
                        portListing.remove(c);
                    } else if(c.getLl_pm() != 0 && c.getOr_psf() != 0) {
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

            @Override
            public void afterTextChanged(Editable s) {
          /*if(portListing != null)
              portListing.clear();
              portListing.addAll(portListingCopy);*/
                Log.i("search12","sb outside "+s+" ============ : "+inputSearch.getText().toString().equalsIgnoreCase(""));
                String s1="\""+s+"\"";
                /*if(s.toString().equalsIgnoreCase(""))
                {
                    usertext.setText(s);
                    Log.i("search12","sb inside "+s+" ============ : ");
                    if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        usertext.setHint("Your Building");
                    else
                        usertext.setHint("My Listing");
                }

                else {
                    usertext.setText(s1);

                }*/
            }
        });





















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

            if(portListing.size()==0) {
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
            }else
                takeScreenshot();
        }

    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;

    }*/

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
                General.shareReferralLink(getBaseContext());
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerFragment.handle(item))
            return true;

        return false;
    }

    @Override
    public void onBackPressed() {
        if(AppConstants.SIGNUP_FLAG){
            //Log.i(TAG,"flaga isa 6 ");

            if(AppConstants.REGISTERING_FLAG){}else{
                // getSupportFragmentManager().popBackStack();
//                getSupportFragmentManager().
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
                AppConstants.SIGNUP_FLAG=false;
            }
            Log.i("sushil123"," main activity =================== SIGNUP_FLAGffffffff");

        }else if(setting==true){
            Log.i("BACKsPRESSED"," =================== setting portfolio"+setting);

            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();

            Log.i("BACKsPRESSED", "loadFragment setting client4 " + getFragmentManager().getBackStackEntryCount());
            setting = false;


        }else if(webView != null){
            if (webView.canGoBack()) {
                webView.goBack();
            }
            else {
                webView = null;
                Intent inten = new Intent(this, BrokerListingActivity.class);
                inten.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inten);
                finish();

                //backpress = 0;
            }
        }else if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
            Intent in = new Intent(getBaseContext(),BrokerMainActivity.class);
            in.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        } else
            super.onBackPressed();
    }




    @Override
    public void onPositionSelected(int position, int count){
        inputSearch.setText("");
        if (position == 0) {
            TT = "LL";
            portListing.clear();
            portListing.addAll(cataloglistLL);
            portListing.addAll(addbuildingLL);
            catalogportListing.clear();
            catalogportListing.addAll(addbuildingLL);

//            portListing.addAll(myPortfolioLL);
            portListingCopy.clear();
            portListingCopy.addAll(portListing);
            AppConstants.TT_TYPE = "ll";
            adapter.notifyDataSetChanged();
            if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
                inputSearch.setHint("Search "+portListing.size()+" Building in Listings");
               /* usertext.setText("");
                usertext.setHint("\"My Listing\"");
                add_create.setText("Create");*/
            }/*else{
                inputSearch.setHint("Search "+ portListing.size()+" Building in Watchlist");
                usertext.setText("");
                usertext.setHint("Your Building");
                add_create.setText("Add");

            }*/
            General.setBadgeCount(this, AppConstants.ADDB_COUNT_LL, 0);
            rentalCount.setVisibility(View.GONE);
        } else {
            TT = "OR";
            AppConstants.TT_TYPE = "or";
            Log.i("addbuildingOR", "addbuildingOR 3" + addbuildingOR);
            portListing.clear();
            portListing.addAll(cataloglistOR);
            portListing.addAll(addbuildingOR);
//            portListing.addAll(myPortfolioOR);
            catalogportListing.clear();
            catalogportListing.addAll(addbuildingOR);
            portListingCopy.clear();
            portListingCopy.addAll(portListing);
            adapter.notifyDataSetChanged();
            if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
                inputSearch.setHint("Search "+portListing.size()+" Building in Listings");
               /* usertext.setText("");
                usertext.setHint("\"My Listing\"");
                add_create.setText("Create");*/
            }else{
                inputSearch.setHint("Search "+ portListing.size()+" Building in Watchlist");
                /*usertext.setText("");
                usertext.setHint("Your Building");
                add_create.setText("Add");*/

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





 private void loadFragment(Fragment fragment, Bundle args, int containerId, String title) {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(containerId, fragment);
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






    public void openAddListingFinalCard(String id){

        container_sign.setBackgroundColor(Color.parseColor("#CC000000"));
        container_sign.setClickable(true);
        AddListingFinalCard addListingFinalCard= new AddListingFinalCard();
        Bundle b =new Bundle();
        b.putString("edit_listing","update");
        b.putString("listing_id",id);

        loadFragmentAnimated(addListingFinalCard,b,R.id.card,"card");

    }
    public void closeCardContainer(){

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.card)).commit();
        container_sign.setBackgroundColor(getResources().getColor(R.color.transparent));
        container_sign.setClickable(false);
        card.setClickable(false);
    }




    public ArrayList<portListingModel> PortlistingData(){
        Log.i("listingtest","listing count 12 "+catalogportListing.size());

        return catalogportListing;
    }


   public ArrayList<portListingModel> PortlistingData1(){
    Log.i("listingtest","listing count 1111 "+portListing.size());
       for (portListingModel c:list){
           c.setCheckbox(false);
           Log.i("listingtest","listing count 1111 "+c.isCheckbox());
           //list.add();
       }
    return list;
}
    public void Back(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
        Log.i("stacktop","backstack check111 "+getSupportFragmentManager().getBackStackEntryCount());
       // getSupportFragmentManager().popBackStackImmediate();
                ListingExplorer listingExplorer=new ListingExplorer();
                loadFragmentAnimated(listingExplorer,null,R.id.container_sign,"");
    }


    public void Close(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
        Log.i("stacktop","backstack check11 "+getSupportFragmentManager().getBackStackEntryCount());
        //getSupportFragmentManager().popBackStackImmediate();
        //recreate();

    }


    public void Refresh(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
        Log.i("stacktop","backstack check11 "+getSupportFragmentManager().getBackStackEntryCount());
        //getSupportFragmentManager().popBackStackImmediate();
        //recreate();

       Intent in = new Intent(getBaseContext(), BrokerListingActivity.class);
        startActivity(in);
    }



    public void OpenListingTitle(ArrayList<portListingModel> list1){

        list=list1;

        /*Bundle b=new Bundle();
        //putParcelableArrayList("listings",list);
        b.putParcelableArrayList("listings", list1);*/
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
        Log.i("stacktop","backstack check 1 "+getSupportFragmentManager().getBackStackEntryCount());
        //getSupportFragmentManager().popBackStackImmediate();
        ListingTitle listingTitle=new ListingTitle();
        loadFragmentAnimated(listingTitle,null,R.id.container_sign,"");
    }



    protected class RemoveCatalog extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {


            CreateCatalogListing createCatalogListing=new CreateCatalogListing();
            createCatalogListing.setUser_id(General.getSharedPreferences(getBaseContext(),AppConstants.USER_ID));
            createCatalogListing.setAction("remove");
            createCatalogListing.setCatalog_id(catalog_id);
            createCatalogListing.setCity("Mumbai");
            createCatalogListing.setTitle(catalog_title);
            createCatalogListing.setTt((AppConstants.TT_TYPE).toLowerCase());
            // createCatalogListing.setTitle(catalog_name);
            ids.clear();
            createCatalogListing.setListing_ids(ids);



            //readWatchlistAPI.setBuild_list(ids);
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
            oyeokApiService.CreateCataloglist(createCatalogListing, new retrofit.Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                    try {

                        JSONObject jsonResponse = new JSONObject(strResponse);
                        Log.i("magic111","RemoveCatalog success response "+response+"\n"+jsonResponse);
                        JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                        Log.i("magic111","RemoveCatalog success response "+building);
                        //watchlist_id=building.getString("watchlist_id");
                        JSONArray buildingdata=new JSONArray(building.getString("buildings"));
                        Log.i("magic111","RemoveCatalog success response1 "+buildingdata);
                        Log.i("magic111","RemoveCatalog success response2 "+buildingdata.getString(0));
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





}
