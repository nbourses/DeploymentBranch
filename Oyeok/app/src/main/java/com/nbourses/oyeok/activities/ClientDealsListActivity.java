package com.nbourses.oyeok.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

public class ClientDealsListActivity extends AppCompatActivity implements CustomPhasedListener {

    private List<PublishLetsOye> publishLetsOyes;
    private static final String TAG = "DealsListActivity";

    @Bind(R.id.listViewDeals)
    ListView listViewDeals;

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
    private boolean default_deal_flag;
    private ArrayList<BrokerDeals> default_deals;
    //private BrokerDeals deals;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private String deals;
    private Boolean RefreshDrooms = false;
    public CustomPhasedSeekBar mPhasedSeekBar1;
    private String TT = "LL";



    //private ListView listViewDeals;


    /*@Bind(R.id.txtNoActiveDeal)
    TextView txtNoActiveDeal;*/

    /*@Bind(R.id.progressBar)
    LoadingAnimationView progressBar;*/

//    private ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter("okeyed");
        LocalBroadcastManager.getInstance(this).registerReceiver(handlePushNewMessage, filter);
        setContentView(R.layout.activity_deals_list);
        listViewDeals = (ListView) findViewById(R.id.listViewDeals);
        supportChat = (LinearLayout)findViewById(R.id.supportChat);
        fragment_container1 = (FrameLayout)findViewById(R.id.fragment_container1);
        //  listViewDeals.setAdapter(new SearchingBrokersAdapter(this));

        supportChat.setVisibility(View.VISIBLE);
        listViewDeals.setVisibility(View.VISIBLE);
        fragment_container1.setVisibility(View.GONE);

        Intent myIntent = getIntent();
        default_deal_flag = myIntent.getExtras().getBoolean("default_deal_flag");




        ButterKnife.bind(this);


        init();
    }

    private void init() {

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



if(!RefreshDrooms) {

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


        Iterator<Map.Entry<String, String>> iter = deals1.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            Log.i(TAG, "entry.getKey" + entry.getKey());
            Log.i(TAG, "entry.getKeystring" + entry.getKey().toString());
            Log.i(TAG, "entry.getvalue" + entry.getValue());

            Log.d("CHATTRACE", "default drooms" + entry);
            String ok_id = entry.getKey();
            String specs = entry.getValue();
            String name = General.getSharedPreferences(this, AppConstants.NAME);  //name of client to show in default deal title
            BrokerDeals dealsa = new BrokerDeals(name, ok_id, specs, true);

            if (default_deals == null) {
                default_deals = new ArrayList<BrokerDeals>();
            }
            Log.i(TAG, "default deals are" + default_deals);
            default_deals.add(dealsa);

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
                Log.i("TRACE", "ment" + AppConstants.OK_ID);

                startActivity(intent);
            }
        });


    }

    loadBrokerDeals();

}

        //save default deal
        //       }//Log.i("TRACE", "Get default deal" + General.getDefaultDeals(this));




        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("My Deals");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
        startActivity(new Intent(this, ClientMainActivity.class));
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


            Iterator<Map.Entry<String, String>> iter = deals1.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                Log.i(TAG, "entry.getKey" + entry.getKey());
                Log.i(TAG, "entry.getKeystring" + entry.getKey().toString());
                Log.i(TAG, "entry.getvalue" + entry.getValue());

                Log.d("CHATTRACE", "default drooms" + entry);
                String ok_id = entry.getKey();
                String specs = entry.getValue();
                String name = General.getSharedPreferences(this, AppConstants.NAME);  //name of client to show in default deal title
                BrokerDeals dealsa = new BrokerDeals(name, ok_id, specs, true);

                if (default_deals == null) {
                    default_deals = new ArrayList<BrokerDeals>();
                }
                Log.i(TAG, "default deals are" + default_deals);
                default_deals.add(dealsa);

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
                    Log.i("TRACE", "ment" + AppConstants.OK_ID);

                    startActivity(intent);
                }
            });


        }


    }



    private void loadBrokerDeals() {


        // String defaultOK = "{\"for_oyes\":[{\"loc\":[72.8312300000001,19.1630000000001],\"ok_id\":\"szimjqcufrd784371\",\"time\":[\"2016\",\"4\",\"10\",\"8\",\"24\",\"28\"],\"oye_id\":\"3xd6amo1245617\",\"ok_user_id\":\"krve2cnz03rc1hfi06upjpnoh9hrrtsy\",\"name\":\"Shlok M\",\"mobile_no\":\"9769036234\",\"spec_code\":\"Searching for brokers\"}],\"for_oks\":[]}";
        // Log.i("TRACE","DefailtOK" +defaultOK);
        // JSONObject jsonObj = new JSONObject("{\"for_oyes\":[{\"loc\":[72.8312300000001,19.1630000000001],\"ok_id\":\"szimjqcufrd784371\",\"time\":[\"2016\",\"4\",\"10\",\"8\",\"24\",\"28\"],\"oye_id\":\"3xd6amo1245617\",\"ok_user_id\":\"krve2cnz03rc1hfi06upjpnoh9hrrtsy\",\"name\":\"Shlok M\",\"mobile_no\":\"9769036234\",\"spec_code\":\"LL-200+-15000\"}],\"for_oks\":[]}");


        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        RestAdapter restAdapter = new RestAdapter
                .Builder()
                .setEndpoint(AppConstants.SERVER_BASE_URL)
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


        Log.i("TRACE", "in LOad broker deals");
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        oyeokApiService.seeHdRooms(hdRooms, new Callback<PublishLetsOye>() {
            @Override
            protected Object clone() throws CloneNotSupportedException {
                return super.clone();
            }

            @Override
            public void success(PublishLetsOye letsOye, Response response) {





                Log.i("TRACE","in successs");
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





//General.getSharedPreferences(this, "TT")







                        Gson gsonForOks = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        ArrayList<BrokerDeals> listBrokerDeals = (ArrayList<BrokerDeals>)
                                gsonForOks.fromJson(jsonObjectResponseData.getString("for_oyes"),
                                        new TypeToken<ArrayList<BrokerDeals>>() {
                                        }.getType());
                        Log.i("TRACE","list broker deals" +listBrokerDeals);

                        Iterator<BrokerDeals> it = listBrokerDeals.iterator();

                        ArrayList<BrokerDeals> listBrokerDeals_new = new ArrayList<BrokerDeals>();
                        while (it.hasNext())
                        {
                            BrokerDeals deals = it.next();
                            Log.i("TRACE==","deals.are"+deals);
                            Log.i("TRACE==","deals.ok_id"+deals.getOkId());
                            if(!(deals.getOkId() == null))
                            {   if(deals.getSpecCode().contains(TT+"-")) {
                                Log.i("DEALREFRESHPHASESEEKBA","deal spec code "+deals.getSpecCode()+" for "+TT);
                                listBrokerDeals_new.add(deals);

                            }
                            }

                        }

//                        if(!listBrokerDeals_new)
//                            listBrokerDeals =

                        Log.i("TRACE==","list broker deals" +listBrokerDeals_new);

            /*            String[] FirstItem = {"Searching brokers for you."};
                        Log.i("TRACE", "firstitem" + FirstItem[0]);
                        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,FirstItem);
                        listViewDeals.setAdapter(Adapter); */

                        // listViewDeals = (ListView) findViewById(R.id.listViewDeals);

                        ////                 listViewDeals.setAdapter(new SearchingBrokersAdapter(getApplicationContext()));
                  /*     listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           }
                       });  */

                       /* if(listBrokerDeals.size()<= 0)
                        {
                            Log.i("TRACE","inside default deal");
                            BrokerDeals deals = new BrokerDeals();
                            ArrayList bdeals = new ArrayList<BrokerDeals>();
                            bdeals.add(deals);
                            BrokerDealsListAdapter listAdapter = new BrokerDealsListAdapter(bdeals,getApplicationContext());
                            listViewDeals.setAdapter(listAdapter);
                            Log.i("TRACE","Default deal is "+bdeals);
                        } */

                    /*    if(listBrokerDeals.size()<= 0)
                        {
                            Log.i("TRACE","inside default deal");
                            listViewDeals = (ListView) findViewById(R.id.listViewDeals);
                            listViewDeals.setAdapter(new SearchingBrokersAdapter(getApplicationContext()));
                        }   */

                        if (listBrokerDeals_new.size() > 0) {

                            Log.i("TRACE", "NOT inside default deal");
//                            displayListView();
                            //final int firstListItemPosition = listViewDeals.getFirstVisiblePosition();
                            // String[] FirstItem = {"Searching brokers for you."};
                            //String FirstItem = "Searching for brokers";
                            //Log.i("TRACE","firstitem" +FirstItem[0]);
                            //ArrayAdapter<String> Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,FirstItem);
                            //ArrayAdapter<String> FirstItemAdapter = new ArrayAdapter<String>();
                            // Log.i("TRACE","adapter" +Adapter);
                            //listViewDeals.setAdapter(Adapter);
                            //list all broker deals

                            ArrayList<BrokerDeals> total_deals = new ArrayList<BrokerDeals>();
                            ;
                            // if(default_deal_flag)
                            // {
                            //append default_deals with listBrokerDeals

                            Log.i("Shine", "default_deals2 " + default_deals);
                            if(listBrokerDeals_new != null)
                                total_deals.addAll(listBrokerDeals_new);
                            if(default_deals != null)
                                total_deals.addAll(default_deals);

//
                            // }

                            BrokerDealsListAdapter listAdapter = new BrokerDealsListAdapter(total_deals, getApplicationContext());

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

                                        Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
                                        intent.putExtra("userRole", "client");
                                        intent.putExtra(AppConstants.OK_ID, brokerDeals.getOkId());
                                        intent.putExtra("isDefaultDeal",brokerDeals.getdefaultDeal());
                                        Log.i("TRACE DEALS FLAG 2", "FLAG " + brokerDeals.getdefaultDeal());
                                        startActivity(intent);
                                    }
                                });


                            }

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
                Log.i("TRACE","in failure");
//                dismissProgressBar();
//                displayTextMessage(getString(R.string.no_internet_connection));
            }
        });
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




    private final BroadcastReceiver handlePushNewMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            // Update list here and refresh listview using adapter.notifyDataSetChanged();

            RefreshDrooms = intent.getExtras().getBoolean("RefreshDrooms");

            Log.i("TRACE","refreshdrooms is set" +RefreshDrooms);
            Log.i("Shine","RefreshDrooms is "+RefreshDrooms);
            Log.i("Shine","1");
//            deals = General.getDefaultDeals(context);
//            Log.i("Shine","2 "+deals);
//
//
//            Log.i("Default deals in shared","handlePushNewMessage" +deals);
//
//
//            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
//            HashMap<String, String> deals1 = null;
//            if(deals != null) {
//                deals1 = gson.fromJson(deals, type);
//            }
//
//
//            Log.i("SHINE","deals are after removed"+deals1);
//            if(deals1 == null){
//                deals1 = new HashMap<String, String>();
//                Log.i("SHINE","deals are after removed null con"+deals1);
//
//            }
//
//            if(deals1 != null) {
//
//
//
//                Iterator<Map.Entry<String,String>> iter = deals1.entrySet().iterator();
//
//                while (iter.hasNext()) {
//                    Map.Entry<String,String> entry = iter.next();
//                    Log.d(TAG,"entry.getKey"+entry.getKey());
//                    String name = General.getSharedPreferences(context,AppConstants.NAME);  //name of client to show in default deal title
//
//                    BrokerDeals dealsa = new BrokerDeals(name, entry.getKey(),entry.getValue(),true);
//
//                    if (default_deals == null) {
//                        default_deals = new ArrayList<BrokerDeals>();
//                    }else{
//                        default_deals.clear();
//                    }
//                    Log.i(TAG, "default deals are before adding in handle push" + default_deals);
//                  //  default_deals.clear();
//                    Log.i(TAG, "default deals are before adding in handle push cleared" + default_deals);
//                    default_deals.add(dealsa);
//                    Log.i(TAG, "default deals are after adding in adding in handle push" + default_deals);
//
//                }
//            }
//
//            if(default_deals != null) {
//
//                Log.i(TAG, "default deals are after adding in adding in handle push3" + default_deals);
//
//                BrokerDealsListAdapter listAdapter = new BrokerDealsListAdapter(default_deals, getApplicationContext());
//               // listViewDeals.setAdapter(null);
//                //listAdapter.notifyDataSetChanged();
//                listViewDeals.setAdapter(listAdapter);
//                Log.i("inside adapter ", "object " + listAdapter);
//            }

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


    /// phase seekbar
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
}


/*
class SearchingBrokerItem
{
   String txt;
    SearchingBrokerItem(String txt)
    {
        this.txt = txt;
    }
}
class SearchingBrokersAdapter extends BaseAdapter
{
    ArrayList<SearchingBrokerItem> list;
    Context context;
    SearchingBrokersAdapter(Context c)
    {
       context = c;
       list = new ArrayList<SearchingBrokerItem>();
       Resources res = c.getResources();
        String[] txt = res.getStringArray(R.array.searchingbrokers);
        list.add(new SearchingBrokerItem(txt[0]));
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(0);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflator.inflate(R.layout.searching_brokers_row, parent, false); //Row contains relative layout
//        TextView txt = (TextView) row.findViewById(R.id.textView4);
//
//        SearchingBrokerItem temp = list.get(0);
//        txt.setText(temp.txt);
//
//        Animation anim = new AlphaAnimation(0.0f, 1.0f);
//        anim.setDuration(1000); //You can manage the blinking time with this parameter
//        anim.setStartOffset(20);
//        anim.setRepeatMode(Animation.REVERSE);
//        anim.setRepeatCount(Animation.INFINITE);
//        txt.startAnimation(anim);
        return row;  //Return modified relativelayout object
    }
}
*/