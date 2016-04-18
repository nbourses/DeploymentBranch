package com.nbourses.oyeok.activities;

import android.content.Intent;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

public class ClientDealsListActivity extends AppCompatActivity {

    private List<PublishLetsOye> publishLetsOyes;
    private static final String TAG = "DealsListActivity";

    @Bind(R.id.listViewDeals)
    ListView listViewDeals;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    //private ListView listViewDeals;


    /*@Bind(R.id.txtNoActiveDeal)
    TextView txtNoActiveDeal;*/

    /*@Bind(R.id.progressBar)
    LoadingAnimationView progressBar;*/

//    private ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deals_list);
       // listViewDeals = (ListView) findViewById(R.id.listViewDeals);
      //  listViewDeals.setAdapter(new SearchingBrokersAdapter(this));
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        /*mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...Please wait...");
        mProgressDialog.show();*/

        /*progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation();*/

        //call API to load deals for broker
        loadBrokerDeals();

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

    private void loadBrokerDeals() {


        String defaultOK = "{\"for_oyes\":[{\"loc\":[72.8312300000001,19.1630000000001],\"ok_id\":\"szimjqcufrd784371\",\"time\":[\"2016\",\"4\",\"10\",\"8\",\"24\",\"28\"],\"oye_id\":\"3xd6amo1245617\",\"ok_user_id\":\"krve2cnz03rc1hfi06upjpnoh9hrrtsy\",\"name\":\"Shlok M\",\"mobile_no\":\"9769036234\",\"spec_code\":\"Searching for brokers\"}],\"for_oks\":[]}";
        Log.i("TRACE","DefailtOK" +defaultOK);
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






        Log.i("TRACE","in LOad broker deals");
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        oyeokApiService.seeHdRooms(hdRooms, new Callback<PublishLetsOye>() {
            @Override
            public void success(PublishLetsOye letsOye, Response response) {
                Log.i("TRACE","in successs");
                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                try {
                    JSONObject jsonObjectServer = new JSONObject(strResponse);
                    if (jsonObjectServer.getBoolean("success")) {
                        JSONObject jsonObjectResponseData = new JSONObject(jsonObjectServer.getString("responseData"));
                        Log.i("TRACE","jsonObjectResponseData" +jsonObjectResponseData);
                        Gson gsonForOks = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        ArrayList<BrokerDeals> listBrokerDeals = (ArrayList<BrokerDeals>)
                                gsonForOks.fromJson(jsonObjectResponseData.getString("for_oyes"),
                                        new TypeToken<ArrayList<BrokerDeals>>() {
                                        }.getType());
                        Log.i("TRACE","list broker deals" +listBrokerDeals);

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
                        if(listBrokerDeals.size()< 0)
                        {
                            Log.i("inside default deal","=======");
                            BrokerDealsListAdapter listAdapter = new BrokerDealsListAdapter(true, getApplicationContext());
                            Log.i("inside adapter ","object "+listAdapter);
                            listViewDeals.setAdapter(listAdapter);
                        }

                        if (listBrokerDeals.size() > 0) {
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

                           BrokerDealsListAdapter listAdapter = new BrokerDealsListAdapter(listBrokerDeals, getApplicationContext());
                            listViewDeals.setAdapter(listAdapter);
                            listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                    BrokerDeals brokerDeals = (BrokerDeals) adapterView.getAdapter().getItem(position);

                                    Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
                                    intent.putExtra("userRole", "client");
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
    @OnClick(R.id.dealItemRoot)
    public void onClickDealItemRoot(View v) {
        Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
        intent.putExtra("userRole", "client");
        intent.putExtra(AppConstants.OK_ID, AppConstants.SUPPORT_CHANNEL_NAME);
        startActivity(intent);
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
        TextView txt = (TextView) row.findViewById(R.id.textView4);

        SearchingBrokerItem temp = list.get(0);
        txt.setText(temp.txt);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txt.startAnimation(anim);
        return row;  //Return modified relativelayout object
    }
}

*/