package com.nbourses.oyeok.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.deleteHDroom;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.adapters.BrokerDealsListAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.BrokerDeals;
import com.nbourses.oyeok.models.HdRooms;
import com.nbourses.oyeok.models.PublishLetsOye;
import com.nbourses.oyeok.realmModels.DefaultDeals;
import com.nbourses.oyeok.realmModels.HalfDeals;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

//import com.google.android.gms.appindexing.AppIndex;



public class ClientDealsListActivity extends AppCompatActivity implements CustomPhasedListener{


    private List<PublishLetsOye> publishLetsOyes;
    private static final String TAG = "DealsListActivity";

    @Bind(R.id.listViewDeals)
    SwipeMenuListView listViewDeals;

    @Bind(R.id.supportChat)
    LinearLayout supportChat;

    @Bind(R.id.dealItemRoot)
    RelativeLayout dealItemRoot;
    //    @Bind(R.id.fragment_container)
//    FrameLayout fragment_container;
    @Bind(R.id.fragment_container1)
    FrameLayout fragment_container1;

    @Bind(R.id.view)
    View view;

    @Bind(R.id.phasedSeekBar)
    CustomPhasedSeekBar mCustomPhasedSeekbar;

    @Bind(R.id.phaseSeekbar)
    LinearLayout phaseSeekBar;



    @Bind(R.id.toolbar)
    Toolbar mToolbar;


   /* @Bind(R.id.search)
    Button search;
    @Bind(R.id.searchgone)
    Button searchgone;*/

    //    @Bind(R.id.searchView)
//    SearchView searchView;




//    @Bind(R.id.searchView)
//    SearchView searchView;



    private boolean default_deal_flag;
    private ArrayList<BrokerDeals> default_deals;
    private ArrayList<BrokerDeals> default_deals_copy;
    //private BrokerDeals deals;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private String deals;
    private Boolean RefreshDrooms = false;

    private BrokerDealsListAdapter listAdapter;
    private Boolean showbgtext = true;
    private HalfDeals halfDeals;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    // private ArrayList<dumpclass> listdata;

    public CustomPhasedSeekBar mPhasedSeekBar1;
    private String TT = "LL";
    private ArrayList<BrokerDeals> total_deals;
    private ArrayList<BrokerDeals> listBrokerDeals_new;
    private ArrayList<BrokerDeals> listBrokerDealsLL;
    private ArrayList<BrokerDeals> listBrokerDealsOR;
    private Set<String> mutedOKIds = new HashSet<String>();
    private int position;   //position of swipe menu item
    private Realm myRealm;
    private DefaultDeals defaultDeals;
    private ArrayList<String> defaultOkIds = new ArrayList<String>();
    private ArrayList<String> matchedOkIds;
    private ArrayList<BrokerDeals> reset;

    private TextView bgtxt;
    private LinearLayout bgtxtlayout;

    private ImageView txtPreviouslySelectedPropertyType;
    private static final String propertyTypeDefaultColor = "#FFFFFF";
    private String filterPtype = null;
    private String searchQuery = null;
    private SearchView searchView;

    private ArrayList<BrokerDeals> cachedDeals;
    private ArrayList<BrokerDeals> cachedDealsLL;
    private ArrayList<BrokerDeals> cachedDealsOR;

    Animation bounce;
    Animation slideUp;
    Animation slideDown;


    //private ListView listViewDeals;


    /*@Bind(R.id.txtNoActiveDeal)
    TextView txtNoActiveDeal;*/

    /*@Bind(R.id.progressBar)
    LoadingAnimationView progressBar;*/

//    private ProgressDialog mProgressDialog = null;

    private BroadcastReceiver networkConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            networkConnectivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            myRealm = General.realmconfig(this);
        }
        catch(Exception e){}


        IntentFilter filter = new IntentFilter("okeyed");
        LocalBroadcastManager.getInstance(this).registerReceiver(handlePushNewMessage, filter);
        setContentView(R.layout.activity_deals_list);


        // listViewDeals	=	(ListView) findViewById(R.id.listViewDeals);
        // listdata		=	new ArrayList<dumpclass>();
        //InitializeValues();


        listViewDeals = (SwipeMenuListView) findViewById(R.id.listViewDeals);
        supportChat = (LinearLayout) findViewById(R.id.supportChat);
        fragment_container1 = (FrameLayout)findViewById(R.id.fragment_container1);
        //  listViewDeals.setAdapter(new SearchingBrokersAdapter(this));

        bgtxt=(TextView) findViewById(R.id.bgtxt) ;
        bgtxtlayout = (LinearLayout) findViewById(R.id.bgtxtlayout);
        bgtxtlayout.setVisibility(View.VISIBLE);
        bgtxt.setText("'OYE' More Leads,\nTo Create Dealing\nRooms with new Client");
        listAdapter = new BrokerDealsListAdapter(default_deals, getApplicationContext());
        supportChat.setVisibility(View.VISIBLE);
        listViewDeals.setVisibility(View.VISIBLE);
//        fragment_container1.setVisibility(View.GONE);

        //  Intent myIntent = getIntent();
        //   default_deal_flag = myIntent.getExtras().getBoolean("default_deal_flag");


        ButterKnife.bind(this);

        Log.i("CHAT","in client deals list activity "+DateFormat.getDateTimeInstance().format(new Date()));


        init();

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {



                // create "More" item
//                SwipeMenuItem MoreItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                MoreItem.setBackground(new ColorDrawable(getResources().getColor(R.color.grey)));
//                // set item width
//                MoreItem.setWidth(listAdapter.dp2px(90));
//                Log.i("TRACE1","dp"+" "+listAdapter.dp2px(90));
//                // set item title
//                MoreItem.setIcon(R.drawable.more);
//                MoreItem.setTitle("More");
//                // set item title fontsize
//                MoreItem.setTitleSize(18);
//                // set item title font color
//                MoreItem.setTitleColor(R.color.white);
//                // add to more
//                menu.addMenuItem(MoreItem);


                // create "unmute" item
                SwipeMenuItem MuteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                MuteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.timestamp)));
                // set item width
                MuteItem.setWidth(listAdapter.dp2px(90));
                // set item title
                MuteItem.setIcon(R.drawable.unmute);
                MuteItem.setTitle("mute");
                // set item title fontsize
                MuteItem.setTitleSize(18);
                // set item title font color
                MuteItem.setTitleColor(Color.WHITE);
                // add to more
                menu.addMenuItem(MuteItem);



                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.darkred)));
                // set item width
                deleteItem.setWidth(listAdapter.dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                deleteItem.setTitle("delete");
                MuteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(R.color.white);
                // add to more
                menu.addMenuItem(deleteItem);









            }
        };
        // set creator
        listViewDeals.setMenuCreator(creator);

        // step 2. listener item click event
        listViewDeals.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int pos, SwipeMenu menu, int index) {
                //           ApplicationInfo item =  listAdapter.getItem(position);
                position = pos;
                switch (index) {
                    case 2:

                        Log.i("OPEN OPTION", "=================");
                        //open
//                        open(item);
                        break;
                    case 0:

                        if(listBrokerDeals_new.isEmpty()){
                            total_deals = new ArrayList<BrokerDeals>();
                            total_deals.addAll(default_deals);
                        }

                        Log.i("MUTE", "muted from shared1" + General.getMutedOKIds(ClientDealsListActivity.this));
                        if(!(General.getMutedOKIds(ClientDealsListActivity.this) == null)) {
                            mutedOKIds.addAll(General.getMutedOKIds(ClientDealsListActivity.this));

                            if(mutedOKIds.contains(total_deals.get(position).getOkId())) {
                                mutedOKIds.remove(total_deals.get(position).getOkId());
                                SnackbarManager.show(
                                        Snackbar.with(ClientDealsListActivity.this)
                                                .position(Snackbar.SnackbarPosition.TOP)
                                                .text(total_deals.get(position).getSpecCode() + " unmuted!")
                                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                            }
                            else {
                                mutedOKIds.add(total_deals.get(position).getOkId());
                                SnackbarManager.show(
                                        Snackbar.with(ClientDealsListActivity.this)
                                                .position(Snackbar.SnackbarPosition.TOP)
                                                .text(total_deals.get(position).getSpecCode() + " muted!")
                                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                            }

                        }


                        General.saveMutedOKIds(ClientDealsListActivity.this,mutedOKIds);

                        Log.i("MUTE", "muted from shared" + General.getMutedOKIds(ClientDealsListActivity.this));
//                        Log.i("MUTE CALLED", "ok_id " + total_deals.get(position).getOkId());
//                        General.setSharedPreferences(getApplicationContext(), AppConstants.MUTED_OKIDS, total_deals.get(position).getOkId());
//                        General.getSharedPreferences(getApplicationContext(),AppConstants.MUTED_OKIDS)
                        // delete
//					delete(item);
//                        listAdapter.remove(position);
//                        listAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        Log.i(TAG,"wadala listBrokerDeals_new "+listBrokerDeals_new);
                        Log.i(TAG,"wadala total_deals "+total_deals);
                        Log.i(TAG,"wadala default deals "+default_deals);

                        if(listBrokerDeals_new.isEmpty()){
                            Log.i(TAG,"wadala default deals 1 ");
                            total_deals = new ArrayList<BrokerDeals>();
                            total_deals.addAll(default_deals);
                        }


                        // Log.i("DELETEHDROOM","position "+position+"menu "+menu+"index "+index);


                        Log.i("deleteDR CALLED", "spec code " + total_deals.get(position).getSpecCode());
                        AlertDialog alertDialog = new AlertDialog.Builder(ClientDealsListActivity.this).create();
                        //alertDialog.setTitle("DELETE");
                        alertDialog.setMessage("Do you really want to delete this deal room.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        if(default_deals != null) {
                                            Log.i(TAG,"wadala default deals 2 ");
                                            if (default_deals.contains(total_deals.get(position))){

                                                Log.i("deleteDR CALLED", "Its default deal " + total_deals.get(position).getSpecCode());


                                                String deals;
                                                deals = General.getDefaultDeals(ClientDealsListActivity.this);
                                                java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                                                }.getType();
                                                HashMap<String, String> deals1 = gson.fromJson(deals, type);

                                                Log.i("TRACE", "hashmap:" + deals1);

                                                if (deals1 == null) {
                                                    deals1 = new HashMap<String, String>();

                                                }

                                                Iterator<Map.Entry<String,String>> iter = deals1.entrySet().iterator();

                                                while (iter.hasNext()) {
                                                    Map.Entry<String,String> entry = iter.next();
                                                    Log.i("DELETE DEFAULT DROOM","entry.getKey"+entry.getKey());
                                                    if(total_deals.get(position).getOkId().equalsIgnoreCase(entry.getKey())){
                                                        iter.remove();
                                                        Log.i("DELETE DEFAULT DROOM", "entry.getKey removed" + entry.getKey());
                                                        Log.i("DELETE DEFAULT DROOM", "default droomsremoved" + entry.getKey());
                                                        Log.i("DELETE DEFAULT DROOM", "default droomsremoved okid" + total_deals.get(position).getOkId());
                                                        Log.i("DELETE DEFAULT DROOM","entry.getKey removed"+entry.getValue());
                                                        // RefreshDrooms = true;
                                                    }
                                                }
                                                Log.i(TAG,"after deal "+deals1);
                                                Log.i("Default deals in shared","I am here2");
                                                Gson g = new Gson();
                                                String hashMapString = g.toJson(deals1);
                                                General.saveDefaultDeals(ClientDealsListActivity.this, hashMapString);

                                                deleteDealingroom("1",total_deals.get(position).getOkId(),total_deals.get(position).getSpecCode());
                                                default_deals.clear();
                                                loadDefaultDeals();
                                                loadBrokerDeals();

                                            }
                                        }


                                        if(listBrokerDeals_new != null) {
                                            Log.i(TAG,"wadala default deals 2 ");
                                            if (listBrokerDeals_new.contains(total_deals.get(position))) {

                                                Log.i("deleteDR CALLED", "Its HDroom " + total_deals.get(position).getSpecCode());


                                                deleteDealingroom("0",total_deals.get(position).getOkId(),total_deals.get(position).getSpecCode());
                                                //on delete droom delete that room OK id from mutedOKIds

                                                Log.i("MUTE", "muted from shared1" + General.getMutedOKIds(ClientDealsListActivity.this));
                                                if(!(General.getMutedOKIds(ClientDealsListActivity.this) == null)) {
                                                    mutedOKIds.addAll(General.getMutedOKIds(ClientDealsListActivity.this));

                                                    if(mutedOKIds.contains(total_deals.get(position).getOkId()))
                                                        mutedOKIds.remove(total_deals.get(position).getOkId());

                                                }

                                                General.saveMutedOKIds(ClientDealsListActivity.this, mutedOKIds);

                                                Log.i("MUTE", "muted from shared" + General.getMutedOKIds(ClientDealsListActivity.this));

                                                if(default_deals != null)
                                                    default_deals.clear();
                                                if(listBrokerDeals_new != null)
                                                    listBrokerDeals_new.clear();
                                                loadDefaultDeals();
                                                loadBrokerDeals();
                                            }
                                        }

                                    }
                                });
                        alertDialog.show();



//					delete(item);
//                        listAdapter.remove(position);
//                        listAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        listViewDeals.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        listViewDeals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
//              Toast.makeText(getApplicationContext(), position + " long click", 0).show();
                return false;
            }
        });


//        touchListener = new ListViewSwipeGesture(listViewDeals, swipeListener, this);
//        touchListener.SwipeType	=	ListViewSwipeGesture.Double;    //Set two options at background of list item
//
//


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void delete(ApplicationInfo item) {
        // delete app
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.fromParts("package", item.packageName, null));
            startActivity(intent);
        } catch (Exception e) {
        }
    }


    private void more(ApplicationInfo item) {
        // open app
//        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
//        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        resolveIntent.setPackage(item.packageName);
//        List<ResolveInfo> resolveInfoList = getPackageManager()
//                .queryIntentActivities(resolveIntent, 0);
//        if (resolveInfoList != null && resolveInfoList.size() > 0) {
//            ResolveInfo resolveInfo = resolveInfoList.get(0);
//            String activityPackageName = resolveInfo.activityInfo.packageName;
//            String className = resolveInfo.activityInfo.name;
//
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            ComponentName componentName = new ComponentName(
//                    activityPackageName, className);
//
//            intent.setComponent(componentName);
//            startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(networkConnectivity, new IntentFilter(AppConstants.NETWORK_CONNECTIVITY));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(networkConnectivity);

    }


    private void init() {

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query.trim();
                // callSearch1(query);
                Log.i(TAG,"1111111111");

                if(default_deals != null)
                    default_deals.clear();
                if(listBrokerDeals_new != null)
                    listBrokerDeals_new.clear();
                loadDefaultDeals();
                loadBrokerDeals();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText.trim();
                Log.i(TAG,"newText "+searchQuery);

                if(default_deals != null)
                    default_deals.clear();
                if(listBrokerDeals_new != null)
                    listBrokerDeals_new.clear();
                loadDefaultDeals();
                loadBrokerDeals();
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
//                if(default_deals != null)
//                    default_deals.clear();
//                if(listBrokerDeals_new != null)
//                    listBrokerDeals_new.clear();
//                loadDefaultDeals();
//                loadBrokerDeals();
//              }
                return true;
            }



        });

        /*search.setVisibility(View.VISIBLE);*/
        //searchView.setVisibility(View.VISIBLE);
        //supportChat.setVisibility(View.GONE);



        bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        //if user is logged in then make phase seek bar visible, view is already made GONE from layout, on safer side we will still make it gone initially programatically

        phaseSeekBar.setVisibility(View.GONE);
        if (!General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).isEmpty()) {
            phaseSeekBar.setVisibility(View.VISIBLE);

        }


        General.setSharedPreferences(this, AppConstants.TT, AppConstants.RENTAL);

        // mPhasedSeekBar = (CustomPhasedSeekBar) findViewById(R.id.phasedSeekBar);
        mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(this.getResources(),
                new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector},
                new String[]{"30", "15"},
                new String[]{getBaseContext().getResources().getString(R.string.Rental), getBaseContext().getResources().getString(R.string.Resale)
                }));

        mCustomPhasedSeekbar.setListener((this));

        Log.i("Phaseseekbar", "oncreate value " + General.getSharedPreferences(this, AppConstants.TT));

        /*mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...Please wait...");
        mProgressDialog.show();*/

        /*progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation();*/

        //call API to load deals for broker

//        if(default_deal_flag)
        //     {
        //Log.i("TRACE", "Spec code from shared prefs" + General.getSharedPreferences(this, "MY_SPEC_CODE"));
        //  String OK_id= General.getSharedPreferences(this, "OK_ID");


        if (!RefreshDrooms) {

            Log.i("TRACE", "refreshdrooms is not set " + RefreshDrooms);
            deals = General.getDefaultDeals(this);
            Log.d("CHATTRACE", "deals from shared" + deals);
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            HashMap<String, String> deals1 = null;
            if (deals != null) {
                deals1 = gson.fromJson(deals, type);
            }


            Log.i("TRACE", "values after " + deals1);
            if (deals1 == null) {
                deals1 = new HashMap<String, String>();
                Log.i("TRACE", "values after initialization " + deals1);

            }

            if (deals1 != null) {

                if (defaultOkIds != null)
                    defaultOkIds.clear();
                else
                    defaultOkIds = new ArrayList<String>();


                Iterator<Map.Entry<String, String>> iter = deals1.entrySet().iterator();

                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    Log.i(TAG, "entry.getKey" + entry.getKey());
                    Log.i(TAG, "entry.getKeystring" + entry.getKey().toString());
                    Log.i(TAG, "entry.getvalue" + entry.getValue());



                    String ok_id = entry.getKey();
                    String specs = entry.getValue();

                    try {
                        DefaultDeals defaultDeals = new DefaultDeals();
                        defaultDeals.setOk_id(ok_id);
                        defaultDeals.setSpec_code(specs);

                        defaultOkIds.add(ok_id);
                        myRealm.beginTransaction();
                        DefaultDeals defaultDeals1 = myRealm.copyToRealmOrUpdate(defaultDeals);
                        myRealm.commitTransaction();

                    }

                    catch(Exception e){

                    }



                    String name = General.getSharedPreferences(this, AppConstants.NAME);  //name of client to show in default deal title
                    Log.i("specs","specs "+specs);
                    BrokerDeals dealsa = new BrokerDeals(name, ok_id, specs, true);


                    if (dealsa.getSpecCode().contains(TT + "-")) {


                        if (default_deals == null) {
                            default_deals = new ArrayList<BrokerDeals>();
                        }


                        Log.i(TAG, "default deals are" + default_deals);
                        default_deals.add(dealsa);

                    }

                }
                try{
                    RealmResults<DefaultDeals> results1 =
                            myRealm.where(DefaultDeals.class).findAll();

                    for(DefaultDeals c:results1) {
                        // Log.i(TAG,"insiderro2 ");
                        Log.i(TAG, "insiderro3 " + c.getOk_id());
                        Log.i(TAG, "insiderro4 " + c.getSpec_code());
                    }

                }
                catch(Exception e){

                }
            }




      /*        Before adding OK id as chanel name in default dealing rooms
                Collection d = deals1.values();
                Log.i("TRACE", "values after jugad collection" + d);
                Iterator it = d.iterator();
                while (it.hasNext()) {
                    // Log.i("TRACE", "values from hashmap " +it.next());
                    String s = it.next().toString();
                    Log.i("TRACE", "element of set Set from shared == " + s);
                    BrokerDeals dealsa = new BrokerDeals(s,ok_id);
                    Log.i("TRACE", "*************");
                    Log.i("TRACE", "ele" + default_deals);
                    Log.i("TRACE", "element of set Set from shared" + dealsa);
                    // Log.i("TRACE", "default_deals type" + dealswa.getClass().getName());
                    // Log.i("TRACE", "default_deals type" + default_deals.getClass().getName());
                    if (default_deals == null) {
                        default_deals = new ArrayList<BrokerDeals>();
                    }//default_deals.addAll(dealsa);
                    default_deals.add(dealsa);
                }  */






   /*         default_deals = new ArrayList<BrokerDeals>();
           Iterator it = General.getDefaultDeals(this).iterator();
            while (it.hasNext()) {
                //System.out.println(it.next());
                String s= it.next().toString();
                Log.i("TRACE", "element of set Set from shared == " + s);
               deals = new BrokerDeals(s);
//                Log.i("TRACE", "element of set Set from shared" + deals);
                default_deals.add(deals);
               // Log.i("TRACE", "element of set Set from shared tostring" + it.next().toString());
            }
    //Log.i("TRACE", "ele"+default_deals);
    //deals = new BrokerDeals(General.getSharedPreferences(this, "MY_SPEC_CODE"));
    // Log.i("TRACE", "ment");
            } */
            //Log.i("TRACE", "ele"+default_deals);
            //deals = new BrokerDeals(General.getSharedPreferences(this, "MY_SPEC_CODE"));
            // Log.i("TRACE", "ment");
            loadCachedDeals();
            if(default_deals != null){
                if(default_deals_copy == null)
                    default_deals_copy = new ArrayList<BrokerDeals>();
                else
                    default_deals_copy.clear();

                default_deals_copy.addAll(default_deals);
            }
            if(cachedDeals != null && default_deals !=null){
                default_deals.addAll(cachedDeals);
            }
            if (default_deals != null) {
                BrokerDealsListAdapter listAdapter = new BrokerDealsListAdapter(default_deals, getApplicationContext());
                listViewDeals.setAdapter(listAdapter);
                Log.i("inside adapter ", "object " + listAdapter);
                if(default_deals.size() <3 && showbgtext == true){
                    bgtxtlayout.setVisibility(View.VISIBLE);
                    bgtxt.setText("'OYE' More Leads,\nTo Create Dealing\nRooms with new Client");
                }else{bgtxtlayout.setVisibility(View.GONE);}
                listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                        Log.i("TRACE", "default deals adapter clicked" + position);


                        if (default_deals != null) {
                            //Log.i(TAG, "default deals are1" + default_deals.get(0).getSpecCode());
                            BrokerDealsListAdapter listAdapter = new BrokerDealsListAdapter(default_deals, getApplicationContext());
                            listViewDeals.setAdapter(listAdapter);

                            Log.i("inside adapter ", "object " + listAdapter);

                            BrokerDeals brokerDeals = (BrokerDeals) adapterView.getAdapter().getItem(position);
                            Log.i(TAG, "default deals are17" + brokerDeals.getSpecCode());
                            Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
                            intent.putExtra("userRole", "client");
                            intent.putExtra(AppConstants.SPEC_CODE, brokerDeals.getSpecCode());
                            intent.putExtra(AppConstants.OK_ID, brokerDeals.getOkId());
                            intent.putExtra("isDefaultDeal",brokerDeals.getdefaultDeal());
                            Log.i("TRACE", "ment" + AppConstants.OK_ID);

                            startActivity(intent);
                        }
                    }
                });


            }

//    loadBrokerDeals();
//
//}

            //save default deal
            //       }//Log.i("TRACE", "Get default deal" + General.getDefaultDeals(this));


            //save default deal
            //       }//Log.i("TRACE", "Get default deal" + General.getDefaultDeals(this));


            loadBrokerDeals();

            setSupportActionBar(mToolbar);

            getSupportActionBar().setTitle("DEALING ROOMs");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        }
    }

    /*private void dismissProgressBar() {
        *//*if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();*//*
        *//*progressBar.pauseAnimation();
        progressBar.setVisibility(View.GONE);*//*
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, ClientMainActivity.class);
        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //     startActivity(new Intent(this, ClientMainActivity.class));
        //   finish();
    }




    private void deleteDealingroom(String deleteOyeId,String deleteOKId, final String specCode){
        if(General.isNetworkAvailable(this)) {
            General.slowInternet(this);
            Log.i(TAG,"wadala default deals 3 ");


            deleteHDroom deleteHDroom  = new deleteHDroom();
            deleteHDroom.setOkId(deleteOKId);
            deleteHDroom.setDeleteOyeId(deleteOyeId);
            deleteHDroom.setUserId(General.getSharedPreferences(this,AppConstants.USER_ID));
            deleteHDroom.setPage("1");
            deleteHDroom.setGcmId(General.getSharedPreferences(this,AppConstants.GCM_ID));

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstants.SERVER_BASE_URL)
                    .build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


            try {
                oyeokApiService.deleteHDroom(deleteHDroom, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        Log.i("deleteDR CALLED","success");
                        loadBrokerDeals();

                        SnackbarManager.show(
                                Snackbar.with(ClientDealsListActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text(specCode + " deleted")
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


                        JsonObject k = jsonElement.getAsJsonObject();
                        try {
                            JSONObject ne = new JSONObject(k.toString());
                            String success = ne.getString("success");




                        }



                        catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            Log.i("deleteDR CALLED","Failed "+e.getMessage());
                        }




                    }

                    @Override
                    public void failure(RetrofitError error) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                    }
                });
            }
            catch (Exception e){
                Log.e(TAG, e.getMessage());
            }

        }else{

            General.internetConnectivityMsg(this);
        }

    }


    private void loadDefaultDeals(){

        Log.i("TRACE", "refreshdrooms is not set "+RefreshDrooms);
        deals = General.getDefaultDeals(this);
        Log.d("CHATTRACE", "deals from shared" + deals);
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        HashMap<String, String> deals1 = null;
        if (deals != null) {
            deals1 = gson.fromJson(deals, type);
        }


        Log.i("TRACE", "values after " + deals1);
        if (deals1 == null) {
            deals1 = new HashMap<String, String>();
            Log.i("TRACE", "values after initialization " + deals1);

        }

        if (deals1 != null) {

            if (defaultOkIds != null)
                defaultOkIds.clear();
            else
                defaultOkIds = new ArrayList<String>();


            Iterator<Map.Entry<String, String>> iter = deals1.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                Log.i(TAG, "entry.getKey" + entry.getKey());
                Log.i(TAG, "entry.getKeystring" + entry.getKey().toString());
                Log.i(TAG, "entry.getvalue" + entry.getValue());

                Log.d("CHATTRACE", "default drooms" + entry);
                String ok_id = entry.getKey();
                String specs = entry.getValue();
                defaultOkIds.add(ok_id);
                String name = General.getSharedPreferences(this, AppConstants.NAME);  //name of client to show in default deal title
                BrokerDeals dealsa = new BrokerDeals(name, ok_id, specs, true);

                if (dealsa.getSpecCode().contains(TT + "-")) {

                    if((filterPtype != null) && dealsa.getSpecCode().contains(filterPtype)){


                        if (default_deals == null) {
                            default_deals = new ArrayList<BrokerDeals>();
                        }


                        Log.i(TAG, "default deals are" + default_deals);
                        default_deals.add(dealsa);

                    }
                    else if(filterPtype == null){

                        if(searchQuery != null) {

                            if (dealsa.getSpecCode().contains(searchQuery)/*|| dealsa.getName().contains(searchQuery)||dealsa.getLocality().contains(searchQuery)*/){

                                if (default_deals == null) {
                                    default_deals = new ArrayList<BrokerDeals>();
                                }


                                Log.i(TAG, "default deals are" + default_deals);
                                default_deals.add(dealsa);
                            }
                        }

                        if(searchQuery == null)
                            default_deals.add(dealsa); // add all

                    }
                }

            }
        }




      /*        Before adding OK id as chanel name in default dealing rooms
                Collection d = deals1.values();
                Log.i("TRACE", "values after jugad collection" + d);
                Iterator it = d.iterator();
                while (it.hasNext()) {
                    // Log.i("TRACE", "values from hashmap " +it.next());
                    String s = it.next().toString();
                    Log.i("TRACE", "element of set Set from shared == " + s);
                    BrokerDeals dealsa = new BrokerDeals(s,ok_id);
                    Log.i("TRACE", "*************");
                    Log.i("TRACE", "ele" + default_deals);
                    Log.i("TRACE", "element of set Set from shared" + dealsa);
                    // Log.i("TRACE", "default_deals type" + dealswa.getClass().getName());
                    // Log.i("TRACE", "default_deals type" + default_deals.getClass().getName());
                    if (default_deals == null) {
                        default_deals = new ArrayList<BrokerDeals>();
                    }//default_deals.addAll(dealsa);
                    default_deals.add(dealsa);
                }  */






   /*         default_deals = new ArrayList<BrokerDeals>();
           Iterator it = General.getDefaultDeals(this).iterator();
            while (it.hasNext()) {
                //System.out.println(it.next());
                String s= it.next().toString();
                Log.i("TRACE", "element of set Set from shared == " + s);
               deals = new BrokerDeals(s);
//                Log.i("TRACE", "element of set Set from shared" + deals);
                default_deals.add(deals);
               // Log.i("TRACE", "element of set Set from shared tostring" + it.next().toString());
            } */
        //Log.i("TRACE", "ele"+default_deals);
        //deals = new BrokerDeals(General.getSharedPreferences(this, "MY_SPEC_CODE"));
        // Log.i("TRACE", "ment");


        loadCachedDeals();
        if(default_deals != null){
            if(default_deals_copy == null)
                default_deals_copy = new ArrayList<BrokerDeals>();
            else
                default_deals_copy.clear();

            default_deals_copy.addAll(default_deals);
        }
        if(cachedDeals != null && default_deals !=null){
            default_deals.addAll(cachedDeals);
        }


        if (default_deals != null) {
            BrokerDealsListAdapter listAdapter = new BrokerDealsListAdapter(default_deals, getApplicationContext());
            listViewDeals.setAdapter(listAdapter);
            Log.i("inside adapter ", "object " + listAdapter);

            listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Log.i("TRACE", "default deals adapter clicked" + position);


                    BrokerDeals brokerDeals = (BrokerDeals) adapterView.getAdapter().getItem(position);

                    Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
                    intent.putExtra("userRole", "client");
                    intent.putExtra(AppConstants.OK_ID, brokerDeals.getOkId());
                    intent.putExtra(AppConstants.SPEC_CODE, brokerDeals.getSpecCode());
                    Log.i("TRACE", "ment" + AppConstants.OK_ID);

                    startActivity(intent);
                }
            });


        }


    }



    private void loadBrokerDeals() {
        if(General.isNetworkAvailable(this)) {
            General.slowInternet(this);
            Log.i("TRACE", "in Load broker deals=================");

            // String defaultOK = "{\"for_oyes\":[{\"loc\":[72.8312300000001,19.1630000000001],\"ok_id\":\"szimjqcufrd784371\",\"time\":[\"2016\",\"4\",\"10\",\"8\",\"24\",\"28\"],\"oye_id\":\"3xd6amo1245617\",\"ok_user_id\":\"krve2cnz03rc1hfi06upjpnoh9hrrtsy\",\"name\":\"Shlok M\",\"mobile_no\":\"9769036234\",\"spec_code\":\"Searching for brokers\"}],\"for_oks\":[]}";
            // Log.i("TRACE","DefailtOK" +defaultOK);
            // JSONObject jsonObj = new JSONObject("{\"for_oyes\":[{\"loc\":[72.8312300000001,19.1630000000001],\"ok_id\":\"szimjqcufrd784371\",\"time\":[\"2016\",\"4\",\"10\",\"8\",\"24\",\"28\"],\"oye_id\":\"3xd6amo1245617\",\"ok_user_id\":\"krve2cnz03rc1hfi06upjpnoh9hrrtsy\",\"name\":\"Shlok M\",\"mobile_no\":\"9769036234\",\"spec_code\":\"LL-200+-15000\"}],\"for_oks\":[]}");


            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            RestAdapter restAdapter = new RestAdapter
                    .Builder()
                    .setEndpoint(AppConstants.SERVER_BASE_URL_101)
                    .setConverter(new GsonConverter(gson))

                    .build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

            String deviceId = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            //params
            HdRooms hdRooms = new HdRooms();
            hdRooms.setUserRole("client");
            hdRooms.setUserId(General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID));
            hdRooms.setGcmId(SharedPrefs.getString(getApplicationContext(), SharedPrefs.MY_GCM_ID));
            hdRooms.setLat("123456789");
            hdRooms.setLon("123456789");
            hdRooms.setDeviceId(deviceId);
            hdRooms.setPage("1");


            Log.i("TRACE", "in Load broker deals");
            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
            oyeokApiService.seeHdRooms(hdRooms, new Callback<PublishLetsOye>() {
                @Override
                protected Object clone() throws CloneNotSupportedException {
                    return super.clone();
                }

                @Override
                public void success(PublishLetsOye letsOye, Response response) {
                    General.slowInternetFlag = false;
                    General.t.interrupt();

                    Log.i("TRACE", "in successs");
                    String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                    try {
                        JSONObject jsonObjectServer = new JSONObject(strResponse);
                        if (jsonObjectServer.getBoolean("success")) {
                            JSONObject jsonObjectResponseData = new JSONObject(jsonObjectServer.getString("responseData"));
                            Log.i("TRACE", "jsonObjectResponseData" + jsonObjectResponseData);
                            Log.d("CHATTRACE", "default drooms" + jsonObjectResponseData);


//                        JSONObject jsonObjectResponseData1 = new JSONObject(jsonObjectResponseData.getString("for_oyes"));
//
//                        for (int i = 0; i < jsonObjectResponseData1.length(); i++) {
//                            jsonObjectResponseData1.getJSONObject()
//                        }



                            Gson gsonForOks = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                            ArrayList<BrokerDeals> listBrokerDeals = (ArrayList<BrokerDeals>)
                                    gsonForOks.fromJson(jsonObjectResponseData.getString("for_oyes"),
                                            new TypeToken<ArrayList<BrokerDeals>>() {
                                            }.getType());
                            Log.i("TRACE", "list broker deals" + listBrokerDeals);

                            Iterator<BrokerDeals> it = listBrokerDeals.iterator();
                            listBrokerDealsLL = new ArrayList<BrokerDeals>();
                            listBrokerDealsOR = new ArrayList<BrokerDeals>();
                            listBrokerDeals_new = new ArrayList<BrokerDeals>();
                            myRealm = General.realmconfig(ClientDealsListActivity.this);
                            myRealm.beginTransaction();
                            while (it.hasNext()) {
                                BrokerDeals deals = it.next();

                                Log.i("TRACE==","deals.are"+deals);
                                Log.i("TRACE==","deals.ok_id"+deals.getOkId());
                                if(deals.getOkId() != null)
                                {

                                    if(defaultOkIds.contains(deals.getOkId())){
                                        if(matchedOkIds==null)
                                            matchedOkIds = new ArrayList<String>();
                                        matchedOkIds.add(deals.getOkId());

                                    }
                                    Log.i(TAG,"chakala default ok ids are matched ok ids "+matchedOkIds);
                                    if(matchedOkIds != null){
                                        Log.i(TAG,"chakala default ok ids are matched ok ids 2 "+matchedOkIds);
                                        deleteDefaultDeals();
                                        if(default_deals != null)
                                            default_deals.clear();
                                        if(listBrokerDeals_new != null)
                                            listBrokerDeals_new.clear();
                                        loadDefaultDeals();
                                        loadBrokerDeals();
                                        return;                // important

                                    }


                                    halfDeals = new HalfDeals();
                                    Log.i("DEALREFRESHPHASESEEKBA", "yaha kaha 4 "+deals.getOkId());


                                    Log.i("DEALREFRESHPHASESEEKBA", "yaha kaha 9");
                                    halfDeals.setOk_id(deals.getOkId());
                                    Log.i("DEALREFRESHPHASESEEKBA", "yaha kaha 5");
                                    halfDeals.setName(deals.getName());
                                    halfDeals.setLocality(deals.getLocality());
                                    halfDeals.setSpec_code(deals.getSpecCode());
                                    Log.i("DEALREFRESHPHASESEEKBA", "yaha kaha 1");
                                    myRealm.copyToRealmOrUpdate(halfDeals);

                                    Log.i(TAG,"hdroom madhe name "+deals.getLocality());


                                    if(deals.getSpecCode().contains(TT+"-")) {
                                        if((filterPtype != null)&&deals.getSpecCode().contains(filterPtype)) {
                                            Log.i("DEALREFRESHPHASESEEKBA", "deal spec code " + deals.getSpecCode() + " for " + TT);

                                            listBrokerDeals_new.add(deals);
                                        }
                                        else if (filterPtype == null) {

                                            if(searchQuery != null)
                                                if (deals.getSpecCode().contains(searchQuery) /*|| deals.getName().contains(searchQuery)||deals.getLocality().contains(searchQuery)*/) {
                                                    listBrokerDeals_new.add(deals);
                                                }

                                            if(searchQuery == null)
                                                listBrokerDeals_new.add(deals); // add all

                                        }

                                    }
                                }

                            }
                            myRealm.commitTransaction();

//

                            Log.i("TRACE==", "list broker deals" + listBrokerDeals_new);



                            if (listBrokerDeals_new.size() > 0) {

                                Log.i("TRACE", "NOT inside default deal");
//

                                total_deals = new ArrayList<BrokerDeals>();


                                Log.i("Shine", "default_deals2 " + default_deals_copy);

                                if(default_deals_copy != null)

                                  total_deals.addAll(default_deals_copy);
                                if(listBrokerDeals_new != null)
                                    total_deals.addAll(listBrokerDeals_new);


                                BrokerDealsListAdapter listAdapter = new BrokerDealsListAdapter(total_deals, getApplicationContext());
                                if(total_deals.size() <3 && showbgtext == true){
                                    bgtxtlayout.setVisibility(View.VISIBLE);
                                    bgtxt.setText("'OYE' More Leads,\nTo Create Dealing\nRooms with new Client");
                                }else{bgtxtlayout.setVisibility(View.GONE);}
                                //after rental resale deals
                                listViewDeals.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();


                                if(RefreshDrooms) {

                                    Log.i("Shine", "Drooms refreshed");
                                    listViewDeals.setAdapter(listAdapter);
                                    listAdapter.notifyDataSetChanged();
                                    //listViewDeals.setAdapter(null);
                                    //listViewDeals.setAdapter(listAdapter);
                                }else {

                                    Log.i("Shine", "Drooms not refreshed");


                                    listViewDeals.setAdapter(listAdapter);

                                    listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                                            Log.i("TRACE", "main deals adapter clicked" + position);


                                            final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                                            String json = gson.toJson(AppConstants.letsOye);

                                            Log.d(TAG, "AppConstants.letsOye " + json);
                                            Log.i("TRACE", "AppConstants.letsOye " + json);


                                            BrokerDeals brokerDeals = (BrokerDeals) adapterView.getAdapter().getItem(position);
                                            Log.i("getSpecCode","getSpecCode yo 2 "+brokerDeals.getSpecCode());
                                            Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
                                            intent.putExtra("userRole", "client");
                                            intent.putExtra(AppConstants.OK_ID, brokerDeals.getOkId());
                                            intent.putExtra(AppConstants.SPEC_CODE, brokerDeals.getSpecCode());
                                            intent.putExtra("isDefaultDeal",brokerDeals.getdefaultDeal());
                                            Log.i("TRACE DEALS FLAG 2", "FLAG " + brokerDeals.getdefaultDeal());
                                            startActivity(intent);
                                        }
                                    });


                                }

                            } else {

                                Log.i(TAG,"chakala empty aahe listviewdeals_new");
                                if(default_deals == null) {
                                    reset = new ArrayList<BrokerDeals>();
                                    BrokerDealsListAdapter listAdapter = new BrokerDealsListAdapter(reset, getApplicationContext());

                                    //after rental resale deals
                                    listViewDeals.setAdapter(listAdapter);
                                    listAdapter.notifyDataSetChanged();
                                }


                            }
                        } else {

                        }
                    } catch (Exception e) {

                    } finally {

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    General.slowInternetFlag = false;
                    General.t.interrupt();

                    Log.i("TRACE", "in failure");
//                dismissProgressBar();
//                displayTextMessage(getString(R.string.no_internet_connection));
                }
            });

        }else{

            General.internetConnectivityMsg(this);
        }
    }

    private void deleteDefaultDeals() {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        deals = General.getDefaultDeals(this);

        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> deals1 = gson.fromJson(deals, type);

        Iterator<Map.Entry<String,String>> iter = deals1.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String,String> entry = iter.next();
            Log.d(TAG,"entry.getKey"+entry.getKey());
            if(matchedOkIds.contains(entry.getKey())){
                iter.remove();

            }
        }
        Log.i(TAG,"after deal "+deals1);

        Gson g = new Gson();
        String hashMapString = g.toJson(deals1);
        General.saveDefaultDeals(this, hashMapString);



    }


    /*private void displayTextMessage(String message) {
        if (message == null)
            message = getString(R.string.no_active_deal);
        txtNoActiveDeal.setText(message);
        txtNoActiveDeal.setVisibility(View.VISIBLE);
        listViewDeals.setVisibility(View.GONE);
    }
    private void displayListView() {
        txtNoActiveDeal.setVisibility(View.GONE);
        listViewDeals.setVisibility(View.VISIBLE);
    }*/
//
//    @OnClick(R.id.dealItemRoot)
//    public void onClickDealItemRoot(View v) {
//        Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
//        intent.putExtra("userRole", "client");
//        intent.putExtra(AppConstants.OK_ID, AppConstants.SUPPORT_CHANNEL_NAME);
//        startActivity(intent);
//    }

    @OnClick(R.id.dealItemRoot)
    public void onClickDealItemRoot(View v) {



        Log.i("USER_ID", " " + General.getSharedPreferences(this, AppConstants.USER_ID).isEmpty());

        if(!General.getSharedPreferences(this ,AppConstants.USER_ID).isEmpty())  {

            Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
            intent.putExtra("userRole", "client");
//        intent.putExtra("channel_name","my_channel");
            intent.putExtra(AppConstants.OK_ID, AppConstants.SUPPORT_CHANNEL_NAME);
            startActivity(intent);
        }
        else
        {
            supportChat.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            listViewDeals.setVisibility(View.GONE);
            fragment_container1.setVisibility(View.VISIBLE);
            Bundle bundle = new Bundle();
            bundle.putStringArray("Chat", null);
            bundle.putString("lastFragment", "Chat");

//            FrameLayout frame = new FrameLayout(this);
//            frame.setId(SIGNUP_VIEW_ID);
//            setContentView(frame, new LayoutParams(
//                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));


            SignUpFragment signUpFragment = new SignUpFragment();
//            signUpFragment.getView().bringToFront();
            loadFragment(signUpFragment, bundle, R.id.fragment_container1, "");
            Log.i("Signup called =", "Sign up");

        }
    }

    /*private void init() {
        try {
            publishLetsOyes = PublishLetsOye.getAll();
            if (publishLetsOyes.size() > 0) {
                txtNoActiveDeal.setVisibility(View.GONE);
                listViewDeals.setVisibility(View.VISIBLE);
                DealsListAdapter listAdapter = new DealsListAdapter(publishLetsOyes, this);
                listViewDeals.setAdapter(listAdapter);
                listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
                        String clientOkId = General.getSharedPreferences(getApplicationContext(), AppConstants.CLIENT_OK_ID);
                        intent.putExtra(AppConstants.OK_ID, clientOkId);
                        startActivity(intent);
                    }
                });
            }
            else {
                txtNoActiveDeal.setVisibility(View.VISIBLE);
                listViewDeals.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Deals");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/




   /* @OnClick(R.id.search)
    public void onClickzSearch(View v) {
        showbgtext = false;

        filterPtype = null;
        searchgone.setVisibility(View.VISIBLE);
        search.setVisibility(View.GONE);
        supportChat.clearAnimation();
        supportChat.setVisibility(View.GONE);

        if(listBrokerDeals_new != null)
            listBrokerDeals_new.clear();
        if(default_deals != null)
            default_deals.clear();
        loadDefaultDeals();
        loadBrokerDeals();

        supportChat.setVisibility(View.GONE);

        searchView.setVisibility(View.VISIBLE);
        searchView.startAnimation(bounce);
        searchView.setIconified(false);




    }

    @OnClick(R.id.searchgone)
    public void onClickzSearchgone(View v) {
        showbgtext = true;

        filterPtype = null;
        searchQuery = null;
        search.setVisibility(View.VISIBLE);
        searchgone.setVisibility(View.GONE);
        searchView.clearAnimation();
        searchView.setVisibility(View.GONE);
        supportChat.clearAnimation();
        supportChat.setVisibility(View.VISIBLE);
        supportChat.startAnimation(bounce);


        if(listBrokerDeals_new != null)
            listBrokerDeals_new.clear();
        if(default_deals != null)
            default_deals.clear();
        loadDefaultDeals();
        loadBrokerDeals();






    }*/







    private final BroadcastReceiver handlePushNewMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            // Update list here and refresh listview using adapter.notifyDataSetChanged();

            RefreshDrooms = intent.getExtras().getBoolean("RefreshDrooms");

            Log.i("TRACE","refreshdrooms is set" +RefreshDrooms);
            Log.i("Shine","RefreshDrooms is "+RefreshDrooms);
            Log.i("Shine","1");

            default_deals.clear();
            loadDefaultDeals();
            //RefreshDrooms = false;


            loadBrokerDeals();

            Toast.makeText(context, "We have just assigned a broker to your request.", Toast.LENGTH_LONG).show();

        }
    };


    @Override
    protected void onDestroy() {
        Log.i("SHINE3", "dystroyed");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(handlePushNewMessage);
        super.onDestroy();
    }

    private void loadFragment(Fragment fragment, Bundle args, int containerId, String title) {
        //set arguments
        fragment.setArguments(args);
//        fragment.getView().bringToFront();
        //load fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();

        //set title
//        getSupportActionBar().setTitle(title);
    }
   /* @Override
   public boolean onCreateOptionsMenu(Menu more) {
        // Inflate the more; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.more.swipe, more);
        return true;
    }*/


    /// phase seekbar
    @Override
    public void onPositionSelected(int position, int count) {

        if(position == 0) {

            General.setSharedPreferences(this, AppConstants.TT, AppConstants.RENTAL);
            TT = "LL";
            if (default_deals != null) {
                default_deals.clear();
            }


            loadDefaultDeals();
            loadBrokerDeals();
            getSupportActionBar().setTitle("DEALING ROOMs (Rental)");
            SnackbarManager.show(
                    Snackbar.with(this)
                            .text("Rental Deals Type set")
                            .position(Snackbar.SnackbarPosition.TOP)
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), this);
        }
        else{

            General.setSharedPreferences(this, AppConstants.TT, AppConstants.RESALE);
            TT = "OR";
            if (default_deals != null) {
                default_deals.clear();
            }

            loadDefaultDeals();
            loadBrokerDeals();
            SnackbarManager.show(
                    Snackbar.with(this)
                            .text("Buy/Sell Deal Type set")
                            .position(Snackbar.SnackbarPosition.TOP)
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), this);
            getSupportActionBar().setTitle("DEALING ROOMs (Resale)");
        }


        //General.setSharedPreferences(this, AppConstants.TT, TT);

        Log.i(TAG, "PHASED seekbar current onPositionSelected" + position +" "+ " count "+count+" "+General.getSharedPreferences(this, "TT"));



    }
    private  void networkConnectivity(){
        SnackbarManager.show(
                Snackbar.with(this)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .text("INTERNET CONNECTIVITY NOT AVAILABLE")
                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
    }


    private  void loadCachedDeals(){

         if(cachedDeals != null){
             cachedDeals.clear();
         }
        if(cachedDealsLL != null){
            cachedDealsLL.clear();
        }
        if(cachedDealsOR != null){
            cachedDealsOR.clear();
        }

        Realm myRealm = General.realmconfig(this);

        try {



            // listAdapter = new BrokerDealsListAdapter(cachedDeals, getApplicationContext());
            Log.i(TAG, "until loadCachedDeals called 2");
            // listViewDeals.setAdapter(listAdapter);
            Log.i(TAG, "until loadCachedDeals called 3");
            RealmResults<HalfDeals> results1 =
                    myRealm.where(HalfDeals.class).findAll();

            Log.i(TAG, "until loadCachedDeals called 4 "+results1);

            for (HalfDeals c : results1) {
                Log.i(TAG, "until loadCachedDeals ");
                Log.i(TAG, "until loadCachedDeals " + c.getOk_id());
                Log.i(TAG, "until loadCachedDeals " + c.getName());
                Log.i(TAG, "until loadCachedDeals " + c.getLocality());
                BrokerDeals dealsa = new BrokerDeals(c.getName(), c.getOk_id(), c.getSpec_code(), c.getLocality(), true);

                if(cachedDealsLL == null){
                    cachedDealsLL = new ArrayList<BrokerDeals>();
                }
                if(cachedDealsOR == null){
                    cachedDealsOR = new ArrayList<BrokerDeals>();
                }
                if(c.getSpec_code().contains("LL-")){
                    cachedDealsLL.add(dealsa);
                }
                else if(c.getSpec_code().contains("OR-")){
                    cachedDealsOR.add(dealsa);
                }

            }

            setCachedDeals();

        }catch(Exception e){
            Log.i(TAG,"Caught in the exception reading cache from realm "+e);
        }
        finally {

            Log.i(TAG,"finally loadCachedDeals ");
        }
    }


    private void setCachedDeals(){
        if(cachedDeals == null){
            cachedDeals = new ArrayList<BrokerDeals>();
        }
        else{
            cachedDeals.clear();
        }

        if(TT.equalsIgnoreCase("LL"))
            cachedDeals.addAll(cachedDealsLL);
        else
            cachedDeals.addAll(cachedDealsOR);

        if(cachedDeals.size() <3 && showbgtext == true){
            bgtxtlayout.setVisibility(View.VISIBLE);
            bgtxt.setText("'OK' More Leads,\nTo Create Dealing\nRooms with new Client");
        }else{bgtxtlayout.setVisibility(View.GONE);}


        if (cachedDeals != null) {
            listAdapter = new BrokerDealsListAdapter(cachedDeals, getApplicationContext());
            listViewDeals.setAdapter(listAdapter);

            Log.i("inside adapter ", "object cached" + listAdapter);
            listAdapter.notifyDataSetChanged();

            listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Log.i("TRACE", "cached deals adapter clicked" + position);


                    BrokerDeals brokerDeals = (BrokerDeals) adapterView.getAdapter().getItem(position);

                    Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
                    intent.putExtra("userRole", "client");
                    intent.putExtra(AppConstants.OK_ID, brokerDeals.getOkId());
                    intent.putExtra(AppConstants.SPEC_CODE, brokerDeals.getSpecCode());
                    Log.i("TRACE", "ment" + AppConstants.OK_ID);

                    startActivity(intent);
                }
            });


        }
    }

    private void deleteDroomDb(String okId){

        try {
            Realm myRealm = General.realmconfig(this);

            //clear cache
            Log.i(TAG,"until 3 ");
            myRealm.beginTransaction();
            Log.i(TAG,"until 4 ");
            RealmResults<HalfDeals> result = myRealm.where(HalfDeals.class).equalTo(AppConstants.OK_ID,okId).findAll();
            Log.i(TAG,"until result to del is 6 "+result);
            result.clear();

        }catch(Exception e){
            Log.i(TAG,"Caught in the exception clearing cache "+e );
        }
        finally{
            myRealm.commitTransaction();
        }


    }
}


