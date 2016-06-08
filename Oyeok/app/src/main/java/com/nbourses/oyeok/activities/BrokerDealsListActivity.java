package com.nbourses.oyeok.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
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
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

public class BrokerDealsListActivity extends AppCompatActivity implements CustomPhasedListener {

    private static final String TAG = "BrokerDealsListActivity";
    private DBHelper dbHelper;

    @Bind(R.id.listViewDeals)
    SwipeMenuListView listViewDeals;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.supportChat)
    LinearLayout supportChat;
    @Bind(R.id.view)
    View view;
    @Bind(R.id.fragment_container1)
    FrameLayout fragment_container1;

    @Bind(R.id.phasedSeekBar)
    CustomPhasedSeekBar mCustomPhasedSeekbar;

    @Bind(R.id.phaseSeekbar)
    LinearLayout phaseSeekBar;

    /*@Bind(R.id.txtNoActiveDeal)
    TextView txtNoActiveDeal;

    @Bind(R.id.progressBar)
    LoadingAnimationView progressBar;*/

//    private ProgressDialog mProgressDialog = null;

    private String TT = "LL";
    private Set<String> mutedOKIds = new HashSet<String>();
    private ArrayList<BrokerDeals> listBrokerDeals_new;
    private BrokerDealsListAdapter listAdapter;
    private SwipeMenuItem MuteItem,unMuteItem;
    private SwipeMenuCreator creator;
  //  private Boolean signupSuccessflag = false;

    private BroadcastReceiver networkConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            networkConnectivity();
        }
    };
//    private BroadcastReceiver signupSuccessFlag = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.i("signupSuccessflag rec","signupSuccessflag ");
//            signupSuccessflag = intent.getExtras().getBoolean("signupSuccessflag");
//            Log.i("signupSuccessflag r","signupSuccessflag "+signupSuccessflag);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_list);
        listAdapter = new BrokerDealsListAdapter(listBrokerDeals_new, getApplicationContext());
        listViewDeals = (SwipeMenuListView) findViewById(R.id.listViewDeals);
        supportChat = (LinearLayout)findViewById(R.id.supportChat);
        fragment_container1 = (FrameLayout)findViewById(R.id.fragment_container1);
        //  listViewDeals.setAdapter(new SearchingBrokersAdapter(this));

        supportChat.setVisibility(View.VISIBLE);
        listViewDeals.setVisibility(View.VISIBLE);
        fragment_container1.setVisibility(View.GONE);


        ButterKnife.bind(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        init();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(networkConnectivity, new IntentFilter(AppConstants.NETWORK_CONNECTIVITY));
     //   LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(signupSuccessFlag, new IntentFilter(AppConstants.SIGNUPSUCCESSFLAG));

    }


    @Override
    protected void onPause() {
        super.onPause();
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(networkConnectivity);
     //   LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(signupSuccessFlag);


    }

    private void init() {

//        final SwipeMenu[] menu1 = new SwipeMenu[1];
        creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

Log.i("SWIPE","inside swipe menu creator");

//                menu1[0] = menu;
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
//                MoreItem.setTitleColor(R.color.black);
//                // add to more
//
//                menu.addMenuItem(MoreItem);


                // create "unmute" item
                MuteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                MuteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.timestamp)));
                // set item width
                MuteItem.setWidth(listAdapter.dp2px(90));
                // set item title
                MuteItem.setIcon(R.drawable.unmute);
                MuteItem.setTitle("Mute");
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
                deleteItem.setTitle("Delete");
                MuteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(R.color.white);
                // add to more
                menu.addMenuItem(deleteItem);

//                unMuteItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                unMuteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.timestamp)));
//                // set item width
//                unMuteItem.setWidth(listAdapter.dp2px(90));
//                // set item title
//                unMuteItem.setIcon(R.drawable.mute2);
//                unMuteItem.setTitle("unmute");
//                // set item title fontsize
//                unMuteItem.setTitleSize(18);
//                // set item title font color
//                unMuteItem.setTitleColor(Color.WHITE);
//                // add to more
//                menu.addMenuItem(unMuteItem);









            }
        };
        // set creator
        listViewDeals.setMenuCreator(creator);
        listViewDeals.setCloseInterpolator(new BounceInterpolator());
        listViewDeals.setOpenInterpolator(new BounceInterpolator());

        // step 2. listener item click event
        listViewDeals.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //           ApplicationInfo item =  listAdapter.getItem(position);
                switch (index) {
                    case 2:

                        Log.i("OPEN OPTION", "=================");
                        //open
//                        open(item);
                        break;
                    case 0:

                        Log.i("MUTE", "muted from shared1" + General.getMutedOKIds(BrokerDealsListActivity.this));
                        if(!(General.getMutedOKIds(BrokerDealsListActivity.this) == null)) {
                            mutedOKIds.addAll(General.getMutedOKIds(BrokerDealsListActivity.this));

                            if(mutedOKIds.contains(listBrokerDeals_new.get(position).getOkId())) {
                                mutedOKIds.remove(listBrokerDeals_new.get(position).getOkId());
                               // Log.i("SWIPE", "MuteItem " + MuteItem + " " + MuteItem.getIcon());
                               // MuteItem=menu.getMenuItem(position);
                               // Log.i("SWIPE", "MuteItem " + MuteItem + " " + MuteItem.getIcon());
                                //MuteItem=menu.getMenuItem(position);

//                                menu.removeMenuItem(MuteItem);
//
//
//
//                                unMuteItem = new SwipeMenuItem(
//                                        getApplicationContext());
//                                // set item background
//                                unMuteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.timestamp)));
//                                // set item width
//                                unMuteItem.setWidth(listAdapter.dp2px(90));
//                                // set item title
//                                unMuteItem.setIcon(R.drawable.mute2);
//                                unMuteItem.setTitle("mute");
//                                // set item title fontsize
//                                unMuteItem.setTitleSize(18);
//                                // set item title font color
//                                unMuteItem.setTitleColor(Color.WHITE);
//                                // add to more
//                                menu.addMenuItem(unMuteItem);
//
//
//                                synchronized (creator) {
//                                    creator.notify();
//                                }
//                               listAdapter.notifyDataSetChanged();
//                                loadBrokerDeals();



//                                MuteItem.setIcon(R.drawable.unmute);
//                                menu.addMenuItem(MuteItem);
                                SnackbarManager.show(
                                        Snackbar.with(BrokerDealsListActivity.this)
                                                .position(Snackbar.SnackbarPosition.TOP)
                                                .text(listBrokerDeals_new.get(position).getSpecCode() + " unmuted!")
                                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                            }
                            else {
                                mutedOKIds.add(listBrokerDeals_new.get(position).getOkId());


                              //   Log.i("SWIPE", "MuteItem " + MuteItem + " " + MuteItem.getIcon());
                               //  MuteItem=menu.getMenuItem(position);
                              //   Log.i("SWIPE", "MuteItem " + MuteItem + " " + MuteItem.getIcon());
                                //menu.removeMenuItem(MuteItem);

                               // MuteItem.setIcon(R.drawable.mute2);
                               // menu.addMenuItem(MuteItem);

                                SnackbarManager.show(
                                        Snackbar.with(BrokerDealsListActivity.this)
                                                .position(Snackbar.SnackbarPosition.TOP)
                                                .text(listBrokerDeals_new.get(position).getSpecCode() + " muted!")
                                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                            }

                        }


                        General.saveMutedOKIds(BrokerDealsListActivity.this,mutedOKIds);

                        Log.i("MUTE", "muted from shared" + General.getMutedOKIds(BrokerDealsListActivity.this));
//                        Log.i("MUTE CALLED", "ok_id " + total_deals.get(position).getOkId());
//                        General.setSharedPreferences(getApplicationContext(), AppConstants.MUTED_OKIDS, total_deals.get(position).getOkId());
//                        General.getSharedPreferences(getApplicationContext(),AppConstants.MUTED_OKIDS)
                        // delete
//					delete(item);
//                        listAdapter.remove(position);
//                        listAdapter.notifyDataSetChanged();
                        break;
                    case 1:

                        Log.i("DELETEHDROOM","position "+position+"menu "+menu+"index "+index);


                        Log.i("deleteDR CALLED", "spec code " + listBrokerDeals_new.get(position).getSpecCode());




                        if(listBrokerDeals_new != null) {
                            if (listBrokerDeals_new.contains(listBrokerDeals_new.get(position))) {

                                Log.i("deleteDR CALLED", "Its HDroom " + listBrokerDeals_new.get(position).getSpecCode());


                                deleteDealingroom(listBrokerDeals_new.get(position).getOkId(),listBrokerDeals_new.get(position).getSpecCode());
                                //on delete droom delete that room OK id from mutedOKIds

                                Log.i("MUTE", "muted from shared1" + General.getMutedOKIds(BrokerDealsListActivity.this));
                                if(!(General.getMutedOKIds(BrokerDealsListActivity.this) == null)) {
                                    mutedOKIds.addAll(General.getMutedOKIds(BrokerDealsListActivity.this));

                                    if(mutedOKIds.contains(listBrokerDeals_new.get(position).getOkId()))
                                        mutedOKIds.remove(listBrokerDeals_new.get(position).getOkId());

                                }

                                General.saveMutedOKIds(BrokerDealsListActivity.this, mutedOKIds);

                                Log.i("MUTE", "muted from shared" + General.getMutedOKIds(BrokerDealsListActivity.this));

                            }
                        }

                        // delete
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


        //if user is logged in then make phase seek bar visible, view is already made GONE from layout, on safer side we will still make it gone initially programatically

        phaseSeekBar.setVisibility(View.GONE);
        if(!General.getSharedPreferences(this,AppConstants.IS_LOGGED_IN_USER).isEmpty()){
            phaseSeekBar.setVisibility(View.VISIBLE);

        }

        General.setSharedPreferences(this, AppConstants.TT, AppConstants.RENTAL);

        // mPhasedSeekBar = (CustomPhasedSeekBar) findViewById(R.id.phasedSeekBar);
        mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(this.getResources(),
                new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector},
                new String[]{"30", "15"},
                new String[]{"Rental", "Resale"
                }));
        mCustomPhasedSeekbar.setListener((this));

        Log.i("Phaseseekbar","oncreate value "+General.getSharedPreferences(this, AppConstants.TT));

        dbHelper = new DBHelper(this);
        /*mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...Please wait...");
        mProgressDialog.show();*/

        /*progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation();*/

        //call API to load deals for broker
        Log.i("TRACEOK","before loadbroker deals ");
        loadBrokerDeals();
        Log.i("TRACEOK", "after loadbroker deals ");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Deals");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        Log.i("TRACEOK","bundle is"+bundle);
        if (bundle != null) {
            if (bundle.containsKey("serverMessage")) {
                SnackbarManager.show(
                        Snackbar.with(this)
                                .text(bundle.getString("serverMessage"))
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), this);
            }
        }
    }

    /*private void dismissProgressBar() {
        *//*if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();*//*
        progressBar.pauseAnimation();
        progressBar.setVisibility(View.GONE);
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


    private void deleteDealingroom(String deleteOKId, final String specCode){




        deleteHDroom deleteHDroom  = new deleteHDroom();
        deleteHDroom.setOkId(deleteOKId);
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

                    Log.i("deleteDR CALLED","success");
                    loadBrokerDeals();

                    SnackbarManager.show(
                            Snackbar.with(BrokerDealsListActivity.this)
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

                }
            });
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }


    }



    private void loadBrokerDeals() {
        Log.i("TRACEOK","inside loadbroker deals ");

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        RestAdapter restAdapter = new RestAdapter
                .Builder()
                .setEndpoint(AppConstants.SERVER_BASE_URL_101)
                .setConverter(new GsonConverter(gson))
                .build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        Log.i("TRACEOK", "hd rooms api call restAdapter " + restAdapter);

        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.i("TRACEOK", "device id is "+deviceId);

        //params
        HdRooms hdRooms = new HdRooms();
        hdRooms.setUserRole("broker");
        hdRooms.setUserId(General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID));
        hdRooms.setGcmId(SharedPrefs.getString(getApplicationContext(), SharedPrefs.MY_GCM_ID));
        hdRooms.setLat("123456789");
        hdRooms.setLon("123456789");
        hdRooms.setPage("1");
        hdRooms.setDeviceId(deviceId);

        Log.i("TRACEOK", "before hd rooms api call ");

        Log.i("TRACEOK", "hd rooms api call " + hdRooms);

        Log.i("TRACEOK", "hd rooms api call " + General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID));
        Log.i("TRACEOK", "hd rooms api call " + SharedPrefs.getString(getApplicationContext(), SharedPrefs.MY_GCM_ID));

        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        oyeokApiService.seeHdRooms(hdRooms, new Callback<PublishLetsOye>() {


            @Override
            public void success(PublishLetsOye letsOye, Response response) {
                Log.i("TRACEOK", "inside hdrooms api call success ");
                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                Log.i("TRACEOK", "strResponse "+strResponse);
                try {
                    Log.i("TRACEOK", "inside try ");
                    JSONObject jsonObjectServer = new JSONObject(strResponse);
                    Log.i("TRACEOK", "strResponse "+jsonObjectServer);
                    if (jsonObjectServer.getBoolean("success")) {
                        Log.i("TRACEOK", "inside if ");
                        JSONObject jsonObjectResponseData = new JSONObject(jsonObjectServer.getString("responseData"));
                        Log.i("TRACEOK","jsonObjectServer responseData "+jsonObjectServer.getString("responseData"));

                        Gson gsonForOks = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        ArrayList<BrokerDeals> listBrokerDeals= (ArrayList<BrokerDeals>)
                                            gsonForOks.fromJson(jsonObjectResponseData.getString("for_oks"),
                                            new TypeToken<ArrayList<BrokerDeals>>() {
                                            }.getType());
                        Log.i("TRACEOK", "listbrokerdeals size is "+listBrokerDeals.size());
                        if (listBrokerDeals.size() > 0) {



                            Iterator<BrokerDeals> it = listBrokerDeals.iterator();

                            listBrokerDeals_new = new ArrayList<BrokerDeals>();
                            while (it.hasNext())
                            {
                                BrokerDeals deals = it.next();
                                Log.i("TRACE==","deals.are"+deals);
                                Log.i("TRACE==","deals.ok_id"+deals.getOkId());
                                if(!(deals.getOkId() == null))
                                {

                                    if(deals.getSpecCode().contains(TT+"-")) {
                                        Log.i("DEALREFRESHPHASESEEKBA", "deal spec code " + deals.getSpecCode() + " for " + TT);
                                        listBrokerDeals_new.add(deals);
                                    }
                                }

                            }

                            Log.i("TRACE==","list broker deals" +listBrokerDeals_new);







//                            displayListView();

                            //list all broker deals
                            BrokerDealsListAdapter listAdapter = new BrokerDealsListAdapter(listBrokerDeals_new, getApplicationContext());
                            listViewDeals.setAdapter(listAdapter);
                            listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                    BrokerDeals brokerDeals = (BrokerDeals) adapterView.getAdapter().getItem(position);

                                    Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
                                    intent.putExtra("userRole", "broker");
                                    Bundle bundle = new Bundle();

                                    String[] bNames = new String[]{"Building1","Building2","BUilding3"};
                                    int[] bPrice = new int[]{35000,50000,65000};


                                    bundle.putIntArray("bPrice",bPrice);
                                    bundle.putStringArray("bNames",bNames);

                                    intent.putExtra(AppConstants.OK_ID, brokerDeals.getOkId());
                                    intent.putExtra(AppConstants.SPEC_CODE, brokerDeals.getSpecCode());
                                    startActivity(intent);
                                }
                            });
                        }
                        else {
//                            displayTextMessage(null);
                        }
                    }
                    else {
//                        displayTextMessage(null);
                    }
                }
                catch (Exception e) {
//                    displayTextMessage(getString(R.string.no_internet_connection));
                }
                finally {
//                    dismissProgressBar();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                /*dismissProgressBar();
                displayTextMessage(getString(R.string.no_internet_connection));*/
                Log.i("TRACEOK", "hdrooms failure "+error);
            }
        });
    }

    @OnClick(R.id.dealItemRoot)
    public void onClickDealItemRoot(View v) {



        Log.i("USER_ID", " " + General.getSharedPreferences(this, AppConstants.USER_ID).isEmpty());

        if(!General.getSharedPreferences(this ,AppConstants.USER_ID).isEmpty())  {

            Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
            intent.putExtra("userRole", "broker");
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
            bundle.putString("lastFragment", "ChatBroker");
            dbHelper.save(DatabaseConstants.userRole, "Broker");


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

    @Override
    public void onBackPressed() {
//        Log.i("signupSuccessflag back","signupSuccessflag "+signupSuccessflag);
//        signupSuccessflag = true;
//        Log.i("signupSuccessflag back1","signupSuccessflag "+signupSuccessflag);
//        if(signupSuccessflag){
            Intent intent = new Intent(this, BrokerMainActivity.class);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//        }
//        else {
////        startActivity(new Intent(this, BrokerMainActivity.class));
//            finish();
//        }
    }

    private void loadFragment(Fragment fragment, Bundle args, int containerId, String title)
    {
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

    @Override
    public void onPositionSelected(int position, int count) {
        if(position == 0) {

            General.setSharedPreferences(this, AppConstants.TT, AppConstants.RENTAL);
            TT = "LL";
            loadBrokerDeals();
        }
        else{

            General.setSharedPreferences(this, AppConstants.TT, AppConstants.RESALE);
            TT = "OR";
            loadBrokerDeals();
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

}