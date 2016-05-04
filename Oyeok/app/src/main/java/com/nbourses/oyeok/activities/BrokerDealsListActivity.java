package com.nbourses.oyeok.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.adapters.BrokerDealsListAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.BrokerDeals;
import com.nbourses.oyeok.models.HdRooms;
import com.nbourses.oyeok.models.PublishLetsOye;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

public class BrokerDealsListActivity extends AppCompatActivity {

    private static final String TAG = "BrokerDealsListActivity";

    @Bind(R.id.listViewDeals)
    ListView listViewDeals;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    /*@Bind(R.id.txtNoActiveDeal)
    TextView txtNoActiveDeal;

    @Bind(R.id.progressBar)
    LoadingAnimationView progressBar;*/

//    private ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_deals_list);


        ButterKnife.bind(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        init();
    }

    private void init() {
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

    private void loadBrokerDeals() {
        Log.i("TRACEOK","inside loadbroker deals ");

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        RestAdapter restAdapter = new RestAdapter
                .Builder()
                .setEndpoint(AppConstants.SERVER_BASE_URL)
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

                            ArrayList<BrokerDeals> listBrokerDeals_new = new ArrayList<BrokerDeals>();
                            while (it.hasNext())
                            {
                                BrokerDeals deals = it.next();
                                Log.i("TRACE==","deals.are"+deals);
                                Log.i("TRACE==","deals.ok_id"+deals.getOkId());
                                if(!(deals.getOkId() == null))
                                {
                                    Log.i("TRACE==","deals.ok_id inside cond");
                                    listBrokerDeals_new.add(deals);
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
                                    intent.putExtra(AppConstants.OK_ID, brokerDeals.getOkId());
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
        Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
        intent.putExtra("userRole", "broker");
        intent.putExtra(AppConstants.OK_ID, AppConstants.SUPPORT_CHANNEL_NAME);
        startActivity(intent);
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
        startActivity(new Intent(this, BrokerMainActivity.class));
    }
}