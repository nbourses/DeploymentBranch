package com.nbourses.oyeok.fragments;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.PointD;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.loopj.android.image.SmartImageView;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.BrokerBuildings;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.RPOT.ApiSupport.services.AcceptOkCall;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OnAcceptOkSuccess;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.OkBroker.CircularSeekBar.CircularSeekBarNew;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.activities.BrokerDealsListActivity;
import com.nbourses.oyeok.activities.BrokerMap;
import com.nbourses.oyeok.activities.ClientDealsListActivity;
import com.nbourses.oyeok.activities.ProfileActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrokerPreokFragment extends Fragment implements CustomPhasedListener, CircularSeekBarNew.imageAction, OnAcceptOkSuccess, OnChartValueSelectedListener, OnChartGestureListener {

    @Bind(R.id.phasedSeekBar)
    CustomPhasedSeekBar mCustomPhasedSeekbar;

    @Bind(R.id.txtOption1)
    TextView txtOption1;

    @Bind(R.id.txtOption2)
    TextView txtOption2;

    @Bind(R.id.displayOkText)
    TextView displayOkText;

    @Bind(R.id.rentText)
    TextView rentText;

    @Bind(R.id.texPstype)
    TextView texPstype;

//    @Bind(R.id.beaconOK)
//    TextView beaconOK;

    @Bind(R.id.texPtype)
    TextView texPtype;

    @Bind(R.id.pd)
    TextView pd;

    @Bind(R.id.okButton)
    Button okButton;

    @Bind(R.id.deal)
    Button deal;
    @Bind(R.id.hdroomsCount)
    TextView hdroomsCount;

    @Bind(R.id.notClicked)
    LinearLayout notClicked;

    @Bind(R.id.circularSeekbar)
    CircularSeekBarNew circularSeekbar;


    @Bind(R.id.option1Count)
    TextView option1Count;

    @Bind(R.id.option2Count)
    TextView option2Count;

    @Bind(R.id.rentalCount)
    TextView rentalCount;

    @Bind(R.id.resaleCount)
    TextView resaleCount;

    @Bind(R.id.buildingSlider)
    RelativeLayout buildingSlider;

//    @Bind(R.id.chart)
//    BarChart chart;

    @Bind(R.id.okBtn)
    Button okBtn;

    @Bind(R.id.setB)
    TextView setB;

    @Bind(R.id.selectB)
    TextView selectB;

    @Bind(R.id.leadPrompt)
    TextView leadPrompt;
    private int countertut;

    private static final String TAG = "BrokerPreokFragment";
    private static final int REQUEST_CODE_TO_SELECT_CLIENT = 302;

    private JSONArray jsonArrayReqLl;
    private JSONArray jsonArrayReqOr;
    private JSONArray jsonArrayAvlLl;
    private JSONArray jsonArrayAvlOr;
    private JSONArray buildings;
    private List<String> names;
    //  private JSONArray jsonArrayPreokRecent;

    private String strTenants = "Tenants";
    private String strOwners = "Owners";
    private String strSeekers = "Buyer";
    private String strSeller = "Seller";

    private final int currentCount = 2; //TODO: need to discuss with team
    private int currentSeekbarPosition = 0; //default is rental
    private String currentOptionSelectedString = strTenants; //default is Tenants
    private TextView txtPreviouslySelectedOption;
    private TextView txtPreviouslySelectedOptionB;
    private JSONArray jsonObjectArray;
    private int selectedItemPosition;
    private DBHelper dbHelper;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    //private int[] buildingsSelected = new int[3];
    private List<Integer> buildingsSelected = new ArrayList<Integer>();
    private List<Highlight> highlights = new ArrayList<Highlight>();
    private Highlight [] highs;
    private Highlight [] highsa;
    private int[] arr;
    private Animation slide_up;
    private Animation slide_down;
    private ObjectAnimator animation;


    private ChartHighlighter highlighter;
    private Highlight highlight;
    private int chartIndex;
    private ArrayList<BarEntry> entries;
    private ArrayList<String> labels;
    private BarDataSet dataset;
    private PointD p;
    private Entry e;
    ArrayList<String> buildingNames;
    ArrayList<Integer> buildingPrice;
    ArrayList<Integer> LLbuildingPrice = new ArrayList<Integer>();
    ArrayList<Integer> ORbuildingPrice = new ArrayList<Integer>();
    ArrayList<Integer> buildingPriceLL = new ArrayList<Integer>();
    ArrayList<Integer> buildingPriceOR = new ArrayList<Integer>();
    private boolean pricechart = false;
    private HashMap<String, Float> listings;
    private int permissionCheckForDeviceId;
    private Boolean buildingSliderflag = false;
    private Integer buildingsPage = 1;

    //lead description variables

    private String lookingSeeking1 = "Tenant ( ";
    private String lookingSeeking2 = ") is looking";
    private String lookingSeeking = "";
    private String atFor = "at";

    private long mSampleDurationTime = 5; // 5 msec
    private boolean continueToRun = true;
    private Handler mHandler;
    private float fl = 1f;
    private int count =1;
    private boolean pagination = false;
    private BarChart chart;
    public View  v;
    private int rentalCount1;
    private int resaleCount1;
    private int tenantsCount1;
    private int ownersCount1;
    private int buyerCount1;
    private int sellerCount1;
    private String Walkthrough,beacon;
    private int prompt = 2;
    private static final long THRESHOLD_MILLIS = 2000L;
    private long lastClickMillis = 0;

    Animation bounce;
    Animation zoomin;
    Animation zoomout;
private String transaction_type="Rental";

    public BrokerPreokFragment() {
        // Required empty public constructor
    }


    /*private BroadcastReceiver ResetPhase = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("getstring","get string reset phase: "+intent.getExtras().getBoolean("resetphase"));
            if (intent.getExtras().getBoolean("resetphase")){
            resetSeekBar();
            }
        }
    };*/





    private BroadcastReceiver slideDownBuildings = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
   Log.i("time to slide down","slide down");
            buildingSlider.startAnimation(slide_down);
            buildingSlider.setVisibility(View.GONE);
            buildingSliderflag = false;

            if(buildingsSelected.size() !=0)
                buildingsSelected.clear();
            selectB.setText("Selected buildings ["+buildingsSelected.size()+"]");
            selectB.performClick();
        }
    };

    private BroadcastReceiver badgeCountBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                Log.i("gcm local broadcast", "gcm local broadcast");
                if (intent.getExtras().getInt(AppConstants.RENTAL_COUNT) != 0) {
                    Log.i("gcm local broadcast", "gcm local broadcast1");
                    rentalCount1 = intent.getExtras().getInt(AppConstants.RENTAL_COUNT);

                }

                if (intent.getExtras().getInt(AppConstants.RESALE_COUNT) != 0) {
                    resaleCount1 = intent.getExtras().getInt(AppConstants.RESALE_COUNT);
                }
                if (intent.getExtras().getInt(AppConstants.TENANTS_COUNT) != 0) {
                    Log.i("gcm local broadcast", "gcm local broadcast2");
                    tenantsCount1 = intent.getExtras().getInt(AppConstants.TENANTS_COUNT);
                }
                if (intent.getExtras().getInt(AppConstants.OWNERS_COUNT) != 0) {
                    ownersCount1 = intent.getExtras().getInt(AppConstants.OWNERS_COUNT);
                }
                if (intent.getExtras().getInt(AppConstants.BUYER_COUNT) != 0) {
                    buyerCount1 = intent.getExtras().getInt(AppConstants.BUYER_COUNT);
                }
                if (intent.getExtras().getInt(AppConstants.SELLER_COUNT) != 0) {
                    sellerCount1 = intent.getExtras().getInt(AppConstants.SELLER_COUNT);
                }

                setBadges();
            }
            catch(Exception e){

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_broker_preok, container, false);
        ButterKnife.bind(this, v);
        bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        zoomin = AnimationUtils.loadAnimation(getContext(), R.anim.zoomin);
        zoomout = AnimationUtils.loadAnimation(getContext(), R.anim.zoomout);
      //chart removed// chart = (BarChart) v.findViewById(R.id.chart);

        SmartImageView myImage = (SmartImageView) v.findViewById(R.id.my_image);

        init();
        if(SharedPrefs.getString(getContext(),SharedPrefs.CHECK_BEACON).equalsIgnoreCase("")) {
            beacon = "false";  // beacon disabled
            SharedPrefs.save(getContext(), SharedPrefs.CHECK_BEACON, "false");
        }
        else {
            beacon = SharedPrefs.getString(getContext(), SharedPrefs.CHECK_BEACON);
            Log.i("ischecked", "walkthrough3dashboard" + beacon);
        }
        if(SharedPrefs.getString(getContext(),SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("")) {
            Walkthrough = "true";
            SharedPrefs.save(getContext(), SharedPrefs.CHECK_WALKTHROUGH, "false");
        }
        else {
            Walkthrough = SharedPrefs.getString(getContext(), SharedPrefs.CHECK_WALKTHROUGH);
            Log.i("ischecked", "walkthrough3dashboard" + Walkthrough);
        }
        Walkthrough=SharedPrefs.getString(getContext(),SharedPrefs.CHECK_WALKTHROUGH);
        Log.i("ischecked","walkthrough3_broker"+Walkthrough);

        //Tutorial and Beacon code
        if(Walkthrough.equalsIgnoreCase("true")) {
            Log.i("ischecked","walkthrough3dashboard1111111"+Walkthrough);
            walkthroughBroker(v);
            Walkthrough="false";
        } else if(beacon.equalsIgnoreCase("true") ) {
            Log.i("ischecked","walkthrough3dashboard1111111"+beacon);
            try {
                beaconAlertBroker(v);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            beacon="false";
        }
        resetSeekBar();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(slideDownBuildings, new IntentFilter(AppConstants.SLIDEDOWNBUILDINGS));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(badgeCountBroadcast, new IntentFilter(AppConstants.BADGE_COUNT_BROADCAST));
       // LocalBroadcastManager.getInstance(getContext()).registerReceiver(ResetPhase, new IntentFilter(AppConstants.RESETPHASE));
        preok();
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(slideDownBuildings);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(badgeCountBroadcast);
       // LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(ResetPhase);

    }



    private void init() {
        bounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                option1Count.clearAnimation();
                option2Count.clearAnimation();
                rentalCount.clearAnimation();
                resaleCount.clearAnimation();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        try {
        Log.i(TAG,"GCM "+General.getSharedPreferences(getContext(),AppConstants.GCM_ID));
        }catch(Exception e){
            Log.i(TAG,"Caught in exception reading GCM id from shared "+e);
        }
        /*zoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                //beaconOK.startAnimation(zoomout);
            }
        });
        zoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                //beaconOK.startAnimation(zoomin);
            }
        });*/
        if (General.getBadgeCount(getContext(), AppConstants.HDROOMS_COUNT) <= 0)
            hdroomsCount.setVisibility(View.GONE);
        else {
            hdroomsCount.setVisibility(View.VISIBLE);
            hdroomsCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.HDROOMS_COUNT)));
        }
        Log.i("PHASE","before adapter set");
        /*mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(),
                new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector},
                new String[]{"30", "15"},
                new String[]{getContext().getResources().getString(R.string.Rental), getContext().getResources().getString(R.string.Resale)
                }));*/

        mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector,
                R.drawable.broker_type2_selector, R.drawable.broker_type2_selector}, new String[]{"30", "40", "15"}, new String[]{getContext().getResources().getString(R.string.Rental), "Listing", getContext().getResources().getString(R.string.Resale)}));
        mCustomPhasedSeekbar.setListener(this);

        Log.i("PHASE","after adapter set");

        txtPreviouslySelectedOption = txtOption1;
        txtPreviouslySelectedOption.setBackgroundResource(R.color.greenish_blue);

        txtPreviouslySelectedOptionB = selectB;
        txtPreviouslySelectedOptionB.setBackgroundResource(R.color.greenish_blue);

        txtOption1.setText(strTenants);
        txtOption2.setText(strOwners);

        circularSeekbar.setmImageAction(this);

        dbHelper = new DBHelper(getContext());

        //get preok data
       // preok();
    }




    public void refreshCircularSeekbar(JSONArray arr,int currentSeekbarPosition){

        circularSeekbar.setValues(arr.toString());

    }





    public void chart() {
        Log.i("GRAPH","scale before set chart 1"+ chart.getScaleX());
        // pricechart = false;
        entries = new ArrayList<>();
        labels = new ArrayList<String>();
        //if(!pricechart) {
        // buildingPrice.addAll(Arrays.asList(4,8,12,15,18,22,16,2,10,7));
        //buildingNames.addAll(Arrays.asList("Abhinav","Mahesh","Neha","Ekdanta","Karachi","Konark","Vishal","Angels Paradise", "Divyam","Om"));
        Log.i("STEP2", "STEP2");
        setChart();
           /* for (int i = 0; i < buildingPrice.size(); i++) {
                entries.add(new BarEntry(buildingPrice.get(i), i));
            }
            dataset = new BarDataSet(entries, Integer.toString(buildingPrice.size()));
            Log.i("GRAPH", "entries " + entries + "buildingPrice.get(i) " + buildingPrice.get(1));
            labels.addAll(buildingNames);
            //labels.addAll(Arrays.asList("Abhinav","Mahesh","Neha","Ekdanta","Karachi","Konark","Vishal","Angels Paradise", "Divyam","Om"));
            Log.i("GRAPH", "labels " + labels);
            BarData data = new BarData(labels, dataset);
            chart.setData(data); // set the data and list of lables into chart  */
            chart.setDescription("Select three Buildings.");
          //chart.animateY(1500);
//            chart.setScaleYEnabled(false);
//            chart.setScaleXEnabled(false);
        // chart.setScaleEnabled(false);
        //chart.fitScreen();
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        Log.i("GRAPH", "scale after set chart1 " + chart.getScaleX());
        chart.setDragEnabled(true);
        YAxis yaxis = chart.getAxis(YAxis.AxisDependency.LEFT);
        XAxis xAxis = chart.getXAxis();
        yaxis.setAxisMinValue(500);
        Log.i("GRAPH", "chart.getYMax " + chart.getYMax() + "chart.getYChartMax " + chart.getYChartMax());
        //yaxis.setAxisMaxValue(999999999);
        //   chart.setPinchZoom(false);
        xAxis.setLabelsToSkip(0);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDrawHighlightArrow(true);
        chart.setOnChartValueSelectedListener(this);
        chart.setOnChartGestureListener(this);
      /*  }
        else {
            setB.setClickable(false);
            //pricechart = true;
            entries.add(new BarEntry(buildingPrice.get(buildingsSelected.get(0)), 0));
            entries.add(new BarEntry(buildingPrice.get(buildingsSelected.get(1)), 1));
            entries.add(new BarEntry(buildingPrice.get(buildingsSelected.get(2)), 2));
            buildingsSelected.clear();
            dataset = new BarDataSet(entries, "3");
            // creating labels
            labels.add("January");
            labels.add("February");
            labels.add("March");
            BarData data = new BarData(labels, dataset);
            chart.setData(data);
            chart.notifyDataSetChanged();
            chart.invalidate();
            // highlight all three buildings
            Highlight h0 = new Highlight(0, 0);
            Highlight h1 = new Highlight(1, 0);
            Highlight h2 = new Highlight(2, 0);
            chart.highlightValues(new Highlight[]{h0, h1, h2});
            chart.setDescription("Set price for buildings.");
            chart.animateY(2000);
            chart.setScaleYEnabled(false);
            //chart.setScaleEnabled(false);
            chart.fitScreen();
            chart.zoom(1.001f, 1f, 0, 0);
            Log.i("GRAPH", "scale after set chart2 " + chart.getScaleX());
            chart.setDragEnabled(true);
            //   chart.setPinchZoom(false);
            chart.setDoubleTapToZoomEnabled(false);
            chart.setTouchEnabled(true);
            // chart1.setOnChartValueSelectedListener(this);
        }*/
    }

    void setChart()
    {
        if(buildingsSelected.size()!=3) {
            pricechart = false;
            chart.highlightValues(null);
            float fr = buildingNames.size() * 0.33f;
            Log.i("FR is", "fr is " + fr);
            if(!pagination) {
                // chart.fitScreen();
                //chart.zoomAndCenterAnimated(fr, 1f, 0, 0, YAxis.AxisDependency.LEFT, 2000);
            }
            else{
                pagination = false;
                Log.i("yo mana","yo mana "+fr +" "+((buildingsPage - 1) * 10 + 7));
                // chart.zoomAndCenterAnimated(6.6f, 1f, 0, 0, YAxis.AxisDependency.RIGHT, 2000);
                chart.fitScreen();
                Log.i("yo mana","yo mana "+fr +" "+((buildingsPage - 1) * 10 + 7));
                //chart.moveViewToX(10);
                chart.moveViewToX(((buildingsPage*10)-14));
                chart.zoom(fr, 1f, 0, 0);
                // chart.moveViewToX((buildingsPage - 1) * 10 + 7);
            }
            try {
                if(buildingsPage<=1) {
                    entries.clear();
                    labels.clear();
                }
            }
            catch(Exception e) { }
            chart.editEntryValue = false;
            for (int i = 0; i < buildingPrice.size(); i++) {
                Log.i("adding CHARTS","==========");
                entries.add(new BarEntry(buildingPrice.get(i), i));
            }
            if(buildingPrice.size()>0) {
                dataset = new BarDataSet(entries, Integer.toString(buildingPrice.size()));
                Log.i("GRAPH","buildingPrice "+buildingPrice+" entries "+entries+" labels "+labels+" dataset "+dataset);
                dataset.setColors(new int[] { R.color.greenish_blue, R.color.google_yellow, R.color.red_light}, getContext());
                //dataset.setColors(new int[]{Color.parseColor("#2dc4b6"), Color.parseColor("#eeb110"), Color.parseColor("#e74c3c")}, getActivity());
                //  dataset.setColors(new int[] { R.color.red, R.color.green, R.color.blue, R.color.orange }, 50);
            }
            labels.addAll(buildingNames);
        }
        else
        {
            pricechart = true;
            chart.fitScreen();
            chart.highlightValues(null);
            entries.clear();
            labels.clear();
            chart.editEntryValue = true;
            for (int i = 0; i < buildingsSelected.size(); i++) {
                entries.add(new BarEntry(buildingPrice.get(buildingsSelected.get(i)), i));
                labels.add(buildingNames.get(buildingsSelected.get(i)));
            }
            dataset = new BarDataSet(entries, Integer.toString(buildingsSelected.size()));
            dataset.setColors(new int[] { R.color.greenish_blue, R.color.google_yellow, R.color.red_light}, getContext());
        }
        //labels.addAll(Arrays.asList("Abhinav","Mahesh","Neha","Ekdanta","Karachi","Konark","Vishal","Angels Paradise", "Divyam","Om"));
        Log.i("GRAPH", "labels " + labels);
        Log.i("GRAPH", "labels " + dataset);
        BarData data = new BarData(labels, dataset);
        chart.setData(data); // set the data and list of labels into chart
    }

    private final Runnable mRunnable = new Runnable() {
        public void run() {
            fl = fl+0.00005f;
            count++;
            if(count<=220) {
                chart.zoom(fl, 1f, 0, 0);
                Log.i("ZOOM leve set", "zoom level set "+fl);
            }else{
                fl = 1;
                count = 1;
                continueToRun = false;
            }
            if(continueToRun == true){
                mHandler.postDelayed(mRunnable, mSampleDurationTime);
            }
        }
    };


    public void brokerbuildings(final Integer buildingsPage){
        if(General.isNetworkAvailable(getContext())) {
            General.slowInternet(getContext());
            Log.i("BROKER BUILDINGS CALLED","with page "+ buildingsPage);
            BrokerBuildings brokerBuildings = new BrokerBuildings();
            brokerBuildings.setDeviceId("Hardware");
            brokerBuildings.setGcmId(SharedPrefs.getString(getContext(), SharedPrefs.MY_GCM_ID));
            brokerBuildings.setPage(buildingsPage.toString());
            brokerBuildings.setLng(SharedPrefs.getString(getContext(), SharedPrefs.MY_LNG));
            brokerBuildings.setLat(SharedPrefs.getString(getContext(), SharedPrefs.MY_LAT));
            brokerBuildings.setPropertyType("home");
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstants.SERVER_BASE_URL)
                    .build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
            try {
                oyeokApiService.brokerBuildings(brokerBuildings, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        JsonObject k = jsonElement.getAsJsonObject();
                        try {
                            Log.i("BROKER BUILDINGS CALLED","success response "+response);
                            JSONObject ne = new JSONObject(k.toString());
                            Log.i("BROKER BUILDINGS CALLED","success ne "+ne);
                            buildings = ne.getJSONObject("responseData").getJSONArray("buildings");
                            Log.i("BROKER BUILDINGS CALLED","buildings"+ne.getJSONObject("responseData"));
                            for (int i=0; i<buildings.length(); i++) {
                                JSONObject actor = buildings.getJSONObject(i);
                                String name = actor.getString("name");
                                buildingPriceOR.add(actor.getInt("or_psf"));
                                buildingPriceLL.add(actor.getInt("ll_pm"));
                                Log.i("BROKER BUILDINGS CALLED", "buildingPriceOR" + buildingPriceOR);
                                Log.i("BROKER BUILDINGS CALLED", "buildingPriceLL" + buildingPriceLL);
                                buildingNames.add(name);
                                // buildingPrice.add(price);
                                Log.i("BROKER BUILDINGS CALLED", "buildingNames" + buildingNames);
                                Log.i("STEP1","STEP1");
                            }
                            LLbuildingPrice.addAll(buildingPriceLL);
                            ORbuildingPrice.addAll(buildingPriceOR);
                            Log.i("BROKER BUILDINGS CALLED", "LLbuildingPrice" +  LLbuildingPrice);
                            buildingPrice.addAll(LLbuildingPrice);
                            Log.i("BROKER BUILDINGS CALLED", "buildingPrice" +  buildingPrice);
                            Log.i("BROKER BUILDINGS CALLED","buildings"+buildings);
                            Log.i("BROKER BUILDINGS CALLED","buildingNames"+buildingNames);
                            Log.i("BROKER BUILDINGS CALLED","buildingPrice"+buildingPrice);
                            if(buildings.length()>0)
                                chart();
                        }
                        catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            Log.i("BROKER BUILDINGS CALLED","Failed "+e.getMessage());
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

            General.internetConnectivityMsg(getContext());
        }
    }


    /**
     * load preok data by making server API call
     */

    public void preok() {
        Log.i(TAG,"preok called ");
        if(General.isNetworkAvailable(getContext())) {
            General.slowInternet(getContext());
            Log.i("TRACE", "GCM id is" + SharedPrefs.getString(getContext(), SharedPrefs.MY_GCM_ID));
            //preok params
            Oyeok preok = new Oyeok();
            preok.setDeviceId(General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));
            Log.i("PREOK", "getcontext " + General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));
            preok.setUserRole("broker");
            preok.setEmail("");
            preok.setGcmId(SharedPrefs.getString(getContext(), SharedPrefs.MY_GCM_ID));
            Log.i("baseLoc","Base location case "+General.getSharedPreferences(getContext(),AppConstants.MY_BASE_LOCATION));
            if(General.getSharedPreferences(getContext(),AppConstants.MY_BASE_LOCATION).equalsIgnoreCase("")) {
                Log.i("baseLoc","Base location case 1"+General.getSharedPreferences(getContext(),AppConstants.MY_BASE_LOCATION));
                preok.setLocality("Mumbai");
                preok.setLong(SharedPrefs.getString(getContext(), SharedPrefs.MY_LNG));
                preok.setLat(SharedPrefs.getString(getContext(), SharedPrefs.MY_LAT));
            }else{
                Log.i("baseLoc","Base location case 2"+General.getSharedPreferences(getContext(),AppConstants.MY_BASE_LOCATION));
                preok.setLocality(General.getSharedPreferences(getContext(),AppConstants.MY_BASE_LOCATION));
                preok.setLat(General.getSharedPreferences(getContext(),AppConstants.MY_BASE_LAT));
                preok.setLong(General.getSharedPreferences(getContext(),AppConstants.MY_BASE_LNG));
            }
            preok.setPlatform("android");
            Log.i("PREOK", "user_id1 " + General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER));
            if (General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
                preok.setUserId(General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));

            } else {
                preok.setUserId(General.getSharedPreferences(getContext(), AppConstants.USER_ID));
                Log.i("PREOK", "user_id " + General.getSharedPreferences(getContext(), AppConstants.USER_ID));
            }
            RestAdapter restAdapter = new RestAdapter.Builder()
                    //.setEndpoint(AppConstants.SERVER_BASE_URL_101)
                    .setEndpoint(AppConstants.SERVER_BASE_URL_11)
                    .build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
            try {
                oyeokApiService.preOk(preok, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        //
                        Log.i("PREOK CALLED", "preok success");
                        General.slowInternetFlag = false;
                        General.t.interrupt();

                        JsonObject k = jsonElement.getAsJsonObject();

                        try {
                            JSONObject ne = new JSONObject(k.toString());
                            if(ne.getString("errors").equals("8")){
                                 Intent openProfileActivity =  new Intent(getContext(), ProfileActivity.class);
                                openProfileActivity.putExtra("msg","compulsary");
                                startActivity(openProfileActivity);

                            }
                                else{
                                Log.i("PREOK CALLED", "ne is the" + ne);
                                JSONObject neighbours = ne.getJSONObject("responseData").getJSONObject("neighbours");

                                Log.i("PREOK CALLED", "neighbours" + neighbours);

                                jsonArrayReqLl = neighbours.getJSONArray("req_ll");
                                ;//neighbours.getJSONArray("req_ll");
                                jsonArrayAvlLl = neighbours.getJSONArray("avl_ll");//neighbours.getJSONArray("avl_ll");

                                jsonArrayReqOr = neighbours.getJSONArray("req_or");//neighbours.getJSONArray("req_or");
                                jsonArrayAvlOr = neighbours.getJSONArray("avl_or");//neighbours.getJSONArray("avl_or");

                                Log.i("PREOK CALLED", "jsonArrayReqLl" + jsonArrayReqLl);
                                Log.i("PREOK CALLED", "jsonArrayAvlLl" + jsonArrayAvlLl);
                                Log.i("PREOK CALLED", "jsonArrayReqOr" + jsonArrayReqOr);
                                Log.i("PREOK CALLED", "jsonArrayAvlOr" + jsonArrayAvlOr);
                                onPositionSelected(currentSeekbarPosition, currentCount);
                            }
                        } catch (JSONException e) {
                            Log.i("PREOK CALLED", "caught in exception inside preok"+e);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("PREOK CALLED", "Caught in exception preok " + error);
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

        }else{

            try {
                if (!General.isNetworkAvailable(getContext())) {
                    texPtype.setText("Go online to get Leads.");
                }
            }
            catch(Exception e){

            }
        }
    }


    @OnClick({R.id.okButton, R.id.deal})
    public void onButtonsClick(View v) {
        if (okButton.getId() == v.getId()) {
            long now = SystemClock.elapsedRealtime();
            if (now - lastClickMillis > THRESHOLD_MILLIS) {
                listings = new HashMap<String, Float>();
                listings.put("building1", 1f);
                listings.put("building2", 2f);
                listings.put("building3", 3f);

                Log.i("GRAPH", "jsonObjectArray is " + jsonObjectArray);


                if (jsonObjectArray == null) {


                    SnackbarManager.show(
                            com.nispok.snackbar.Snackbar.with(getContext())
                                    .position(Snackbar.SnackbarPosition.BOTTOM)
                                    .text("Please select a Lead and then press OK.")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                } else {
                    if (!General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("yes")) {

                        dbHelper.save(DatabaseConstants.userRole, "Broker");  //to show userr that he is logging is as user
                        General.setSharedPreferences(getContext(), AppConstants.ROLE_OF_USER, "broker");
                        //show sign up screen if broker is not registered
                        Bundle bundle = new Bundle();
                        //bundle.putString("lastFragment", "BrokerPreokFragment");
                        bundle.putString("JsonArray", jsonObjectArray.toString());
                        bundle.putInt("Position", selectedItemPosition);
                        String[] bNames = new String[]{"building1", "building2", "building3"};
                        int[] bPrice = new int[]{1, 2, 3};
                        bundle.putIntArray("bPrice", bPrice);
                        bundle.putStringArray("bNames", bNames);
                        bundle.putString("lastFragment", "okyed");
                        bundle.putSerializable("listings", listings);
                        Fragment fragment = null;
                        fragment = new SignUpFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
                        fragmentTransaction.replace(R.id.container_sign, fragment);
                        // fragmentTransaction.replace(R.id.container_map, fragment);
                        fragmentTransaction.commit();
                        AppConstants.SIGNUP_FLAG = true;

                    } else {
                        //here broker is registered
                        AcceptOkCall a = new AcceptOkCall();
                        a.setmCallBack(BrokerPreokFragment.this);
                        a.acceptOk(listings, jsonObjectArray, selectedItemPosition, dbHelper, getActivity());
                        General.setBadgeCount(getContext(), AppConstants.RENTAL_COUNT, 0);
                        General.setBadgeCount(getContext(), AppConstants.RESALE_COUNT, 0);
                        General.setBadgeCount(getContext(), AppConstants.TENANTS_COUNT, 0);
                        General.setBadgeCount(getContext(), AppConstants.OWNERS_COUNT, 0);
                        General.setBadgeCount(getContext(), AppConstants.BUYER_COUNT, 0);
                        General.setBadgeCount(getContext(), AppConstants.SELLER_COUNT, 0);
                    }
                    //show buildings
                /*//buildings removed// buildingSlider.startAnimation(slide_up);
                buildingSlider.setVisibility(View.VISIBLE);
                buildingSliderflag = true;
                float fr = buildingNames.size() * 0.33f;
               //chart.fitScreen();
               chart.zoomAndCenterAnimated(fr,1f,0,0, YAxis.AxisDependency.LEFT ,2000);
                Log.i("brokerpreok","buildingSliderflag "+buildingSliderflag);
                Intent intent = new Intent(AppConstants.BUILDINGSLIDERFLAG);
                intent.putExtra("buildingSliderFlag",buildingSliderflag);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                /////okBtn.setEnabled(false);
                //okBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey));
                okBtn.setText("Choose 3 Buildings");*/
                }
//            else {
//                if (!General.getSharedPreferences(getActivity(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
//                    dbHelper.save(DatabaseConstants.userRole, "Broker");
//                    //show sign up screen if broker is not registered
//                    Bundle bundle = new Bundle();
//                    bundle.putString("lastFragment", "BrokerPreokFragment");
//                    bundle.putString("JsonArray", jsonObjectArray.toString());
//                    bundle.putInt("Position", selectedItemPosition);
//                    Fragment fragment = null;
//                    fragment = new SignUpFragment();
//                    fragment.setArguments(bundle);
//
//                    FragmentManager fragmentManager = getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.container_map, fragment);
//                    fragmentTransaction.commit();
//                } else {
//                    //here broker is registered  show buildings
//
//                    buildingSlider.startAnimation(slide_up);
//                    buildingSlider.setVisibility(View.VISIBLE);
//
//                }
//            }
                lastClickMillis = now;
            }
            }
        else if (deal.getId() == v.getId()) {
            if (General.getBadgeCount(getContext(), AppConstants.HDROOMS_COUNT) > 0) {
                General.setBadgeCount(getContext(), AppConstants.HDROOMS_COUNT,0);
                hdroomsCount.setVisibility(View.GONE);
            }

            //open deals listing
            Intent openDealsListing = new Intent(getActivity(), ClientDealsListActivity.class);
            /*openDealsListing.addFlags(
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);*/
            startActivity(openDealsListing);
        }
//        else if (pickContact.getId() == v.getId()) {
//            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//            getActivity().startActivityFromFragment(this, intent, REQUEST_CODE_TO_SELECT_CLIENT);
//        }
    }


    @OnClick(R.id.okBtn)
    public void onOptionClickok(View v) {
        long now = SystemClock.elapsedRealtime();
        if (now - lastClickMillis > THRESHOLD_MILLIS) {
            Log.i("CHART", "clickeda ");
        if(okBtn.getText().equals("OK")) {
                try {
                    Log.i("CHART", "y value " + chart.getEntriesAtIndex(0).get(0).getVal());
                    listings = new HashMap<String, Float>();
                    listings.put(buildingNames.get(buildingsSelected.get(0)), chart.getEntriesAtIndex(0).get(0).getVal());
                    listings.put(buildingNames.get(buildingsSelected.get(1)), chart.getEntriesAtIndex(1).get(0).getVal());
                    listings.put(buildingNames.get(buildingsSelected.get(2)), chart.getEntriesAtIndex(2).get(0).getVal());
                } catch (Exception e) {

                }
                //if (!General.getSharedPreferences(getContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
            if (!General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("yes")) {
                    dbHelper.save(DatabaseConstants.userRole, "Broker");  //to show userr that he is logging is as user
                    //show sign up screen if broker is not registered
                    Bundle bundle = new Bundle();
                    //bundle.putString("lastFragment", "BrokerPreokFragment");
                    bundle.putString("JsonArray", jsonObjectArray.toString());
                    bundle.putInt("Position", selectedItemPosition);
                    Log.i("listings", "building1 " + buildingNames.get(buildingsSelected.get(0)));
                    Log.i("listings", "building2 " + buildingNames.get(buildingsSelected.get(1)));
                    Log.i("listings", "building3 " + buildingNames.get(buildingsSelected.get(2)));

                    Log.i("listings", "price1 " + chart.getEntriesAtIndex(0).get(0).getVal());
                    Log.i("listings", "price2 " + chart.getEntriesAtIndex(1).get(0).getVal());
                    Log.i("listings", "price3 " + chart.getEntriesAtIndex(2).get(0).getVal());

                    String[] bNames = new String[]{buildingNames.get(buildingsSelected.get(0)), buildingNames.get(buildingsSelected.get(1)), buildingNames.get(buildingsSelected.get(2))};
                    int[] bPrice = new int[]{Math.round(chart.getEntriesAtIndex(0).get(0).getVal()), Math.round(chart.getEntriesAtIndex(1).get(0).getVal()), Math.round(chart.getEntriesAtIndex(2).get(0).getVal())};


                    bundle.putIntArray("bPrice", bPrice);
                    bundle.putStringArray("bNames", bNames);
                    bundle.putString("lastFragment","okyed");


                    bundle.putSerializable("listings", listings);
                    Fragment fragment = null;
                    fragment = new SignUpFragment();
                    fragment.setArguments(bundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
                    fragmentTransaction.replace(R.id.container_sign, fragment);
                    // fragmentTransaction.replace(R.id.container_map, fragment);
                    fragmentTransaction.commit();
                AppConstants.SIGNUP_FLAG = true;

                } else {
                    //here broker is registered
                    AcceptOkCall a = new AcceptOkCall();
                    a.setmCallBack(BrokerPreokFragment.this);
                    a.acceptOk(listings, jsonObjectArray, selectedItemPosition, dbHelper, getActivity());
                    General.setBadgeCount(getContext(), AppConstants.RENTAL_COUNT, 0);
                    General.setBadgeCount(getContext(), AppConstants.RESALE_COUNT, 0);
                    General.setBadgeCount(getContext(), AppConstants.TENANTS_COUNT, 0);
                    General.setBadgeCount(getContext(), AppConstants.OWNERS_COUNT, 0);
                    General.setBadgeCount(getContext(), AppConstants.BUYER_COUNT, 0);
                    General.setBadgeCount(getContext(), AppConstants.SELLER_COUNT, 0);
                }
        }
        else{
            try {
                SnackbarManager.show(
                        Snackbar.with(getContext())
                                .position(Snackbar.SnackbarPosition.TOP)
                                .text("Please change listing rates.")
                                //.animation(true)
                                .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
            }
            catch(Exception e){
                Log.i(TAG,"Caught in exception click ok btn "+e);
            }
        }
            lastClickMillis = now;
        }
    }



    @OnClick({R.id.selectB, R.id.setB})
    public void onOptionClickB(View v) {
        if (txtPreviouslySelectedOptionB != null)
            txtPreviouslySelectedOptionB.setBackgroundResource(R.color.colorPrimaryDark);

        txtPreviouslySelectedOptionB = (TextView) v;
        if (v.getId() == selectB.getId()) {
            /////okBtn.setEnabled(false);
            //okBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey));
            okBtn.setText("Choose 3 Buildings");

            selectB.setBackgroundResource(R.color.greenish_blue);
            chart.setDescription("Select three buildings.");
            // chart.clear();
//             if(entries.size() !=0)
//            entries.clear();
//            if(labels.size() !=0)
//            labels.clear();
//              if(buildingPrice.size() !=0)
//              buildingPrice.clear();

            setB.setClickable(true);
          //  clearChart();
            if(buildingsSelected.size() !=0)
              buildingsSelected.clear();

//            if(buildingNames.size() !=0)
//                buildingNames.clear();
//            if(buildingPrice.size() !=0)
//                buildingPrice.clear();
            selectB.setText("Selected buildings ["+buildingsSelected.size()+"]");
            Log.i("STEP3","STEP3");
            // chart();
            if(labels.size() != 0)
                labels.clear();
            if(dataset.getStackSize() !=0)
                dataset.clear();
            setChart();

            chart.animateY(1500);



            animation.start();



            mHandler = new Handler();
            chart.fitScreen();
            continueToRun = true;
            fl = 1;
            count = 1;
           // mHandler.postDelayed(mRunnable, mSampleDurationTime);
            chart.zoomAndCenterAnimated(3.3f,1f,0,0, YAxis.AxisDependency.LEFT ,2000);

//            chart.fitScreen();
//
//
//                chart.zoom(3.3f, 1f, 0, 0);
//                Log.i("ZOOM leve set","zoom level set");


        }
        else if (v.getId() == setB.getId()) {
            if(buildingsSelected.size() == 3) {
                chart.setDescription("Please set price and click Ok.");
                setB.setBackgroundResource(R.color.greenish_blue);
                selectB.setBackgroundResource(R.color.colorPrimaryDark);

                /////okBtn.setEnabled(true);
                okBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.greenish_blue));
                okBtn.setText("OK");
//                if(entries.size() !=0)
//                    entries.clear();
                // chart.zoom(1f, 1f, 0, 0);
                //  chart.clear();

                //  priceChart();
                setChart();
                chart.animateY(1500);
                animation.start();
            }
            else{
                SnackbarManager.show(
                        Snackbar.with(getContext())
                                .position(Snackbar.SnackbarPosition.TOP)
                                .text("Please select 3 buildings first.")
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                selectB.setBackgroundResource(R.color.greenish_blue);
            }

        }

    }



    @OnClick({R.id.txtOption1, R.id.txtOption2})
    public void onOptionClick(View v) {
        jsonObjectArray = null;
        notClicked.setVisibility(View.VISIBLE);
        option1Count.setVisibility(View.GONE);
        option2Count.setVisibility(View.GONE);
        animatebadges();
        Log.i("PREOK CALLED11","yoyoyoy2 sus1 "+General.getSharedPreferences(getContext(),AppConstants.TT)+" "+transaction_type);
        circularSeekbar.onTabclick();
        if (txtPreviouslySelectedOption != null)
            txtPreviouslySelectedOption.setBackgroundResource(R.color.colorPrimaryDark);
        txtPreviouslySelectedOption = (TextView) v;
        Log.i("PREOK CALLED11","yoyoyoy2 sus2 "+General.getSharedPreferences(getContext(),AppConstants.TT) + "  "+transaction_type);

        if (v.getId() == txtOption1.getId()) {
            Log.i("PREOK CALLED11","yoyoyoy1 "+General.getSharedPreferences(getContext(),AppConstants.TT)+ "  "+transaction_type);
            Log.i("PREOK CALLED11","jetcool"+currentOptionSelectedString);
//            if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("rental")){
            if(transaction_type.equalsIgnoreCase("rental")){
                lookingSeeking1 = "Tenant (";
                lookingSeeking2 = ") looking for";
                Log.i("PREOK CALLED", "tototo" + lookingSeeking);
            }
//            else if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("resale")){
            else if(transaction_type.equalsIgnoreCase("resale")){
                lookingSeeking1 = "Buyer (";
                lookingSeeking2 = ") is looking for";
                Log.i("PREOK CALLED", "tototo" + lookingSeeking);
            }
            rentText.setVisibility(View.GONE);
            texPtype.setText("Please select a Lead and press OK.");
            texPstype.setVisibility(View.GONE);
            txtOption1.setBackgroundResource(R.color.greenish_blue);
            currentOptionSelectedString = txtOption1.getText().toString();
            Log.i("PREOK CALLED11","currentOptionSelectedString"+currentOptionSelectedString);
            // update circular seekbar
            if (currentOptionSelectedString.equalsIgnoreCase(strSeekers))
                currentOptionSelectedString = strTenants;
            Log.i("PREOK CALLED1","currentOptionSelectedString1 phase"+currentOptionSelectedString);
            if (jsonArrayReqLl != null && currentOptionSelectedString.equalsIgnoreCase(strTenants)) {


                Log.i("PREOK CALLED","values set phase"+jsonArrayReqLl.toString());
                circularSeekbar.setValues(jsonArrayReqLl.toString());


            }
            else if (jsonArrayAvlLl != null && currentOptionSelectedString.equalsIgnoreCase(strOwners)) {

                Log.i("PREOK CALLED", "values set phase" + jsonArrayAvlLl.toString());

                circularSeekbar.setValues(jsonArrayAvlLl.toString());

            }

        }
        else if (v.getId() == txtOption2.getId()) {
            Log.i("PREOK CALLED11","yoyoyoy2 "+General.getSharedPreferences(getContext(),AppConstants.TT) + " "+transaction_type);
            Log.i("PREOK CALLED11","jetcool"+currentOptionSelectedString);
//            if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("rental")){
            if(transaction_type.equalsIgnoreCase("rental")){
                lookingSeeking1 = "Owner (";
                lookingSeeking2 = ") is having";
                Log.i("PREOK CALLED", "tototo" + lookingSeeking + " "+ txtOption2.getText().toString());
            }
//            else if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("resale")){
            else if(transaction_type.equalsIgnoreCase("resale")){
                lookingSeeking = "Seller (";
                lookingSeeking = ") is having";
                Log.i("PREOK CALLED", "tototo" + lookingSeeking);
            }
            txtOption2.setBackgroundResource(R.color.greenish_blue);
            currentOptionSelectedString = txtOption2.getText().toString();
            Log.i("PREOK CALLED1","currentOptionSelectedString"+currentOptionSelectedString);
            rentText.setVisibility(View.GONE);
            texPtype.setText("Please select a Lead and press OK.");
            texPstype.setVisibility(View.GONE);
            if (currentOptionSelectedString.equalsIgnoreCase(strTenants))
                currentOptionSelectedString = strSeekers;
            Log.i(TAG,"jsonArrayReqOr yo "+jsonArrayReqOr);
            if (jsonArrayReqOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeekers)) {
                Log.i(TAG,"jsonArrayReqOr yo 1"+jsonArrayReqOr);
                Log.i("PREOK CALLED", "values set phase" + jsonArrayReqOr.toString());
                circularSeekbar.setValues(jsonArrayReqOr.toString());
            }
            else if (jsonArrayAvlOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeller)) {
                Log.i(TAG,"jsonArrayReqOr yo 2"+jsonArrayReqOr);
                Log.i("PREOK CALLED1", "values set phase" + jsonArrayAvlOr.toString());
                Log.i("tester","4"+currentOptionSelectedString);
                circularSeekbar.setValues(jsonArrayAvlOr.toString());
            }
        }
        onPositionSelected(currentSeekbarPosition, currentCount);
        if(!General.isNetworkAvailable(getContext())){
            texPtype.setText("Go online to get Leads.");
        }
       animatebadges();
    }


    @Override
    public void onPositionSelected(int position, int count) {
        circularSeekbar.onTabclick();
        currentSeekbarPosition = position;
        Log.i("PREOK CALLED","currentSeekbarPosition"+currentSeekbarPosition);
        if (position == 0) {
            General.setSharedPreferences(getContext(),AppConstants.TT,"RENTAL");
            transaction_type="RENTAL";
            option1Count.setBackgroundResource(R.drawable.circle_badge);
            option2Count.setBackgroundResource(R.drawable.circle_badge);
            atFor = "at";
            jsonObjectArray = null;
            try {
                SnackbarManager.show(
                        Snackbar.with(getContext())
                                .position(Snackbar.SnackbarPosition.TOP)
                                .text("Rental Property Type set")
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
            }
            catch(Exception e){}
            notClicked.setVisibility(View.VISIBLE);
            rentText.setVisibility(View.GONE);
            texPtype.setText("Please select a Lead and press OK.");
            texPstype.setVisibility(View.GONE);
            resaleCount.setVisibility(View.GONE);
            Log.i(TAG, "itha count " + AppConstants.TENANTS_COUNT);
            try {
                Log.i("CONTEXT", "object " + getContext());
                if (General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT) <= 0) {
                    Log.i(TAG, "itha count 1 " + AppConstants.TENANTS_COUNT);
                    rentalCount.setVisibility(View.GONE);
                    option1Count.setVisibility(View.GONE);
                    option2Count.setVisibility(View.GONE);
                } else {
                    Log.i(TAG, "itha count 2 " + AppConstants.TENANTS_COUNT);
                    rentalCount.setVisibility(View.VISIBLE);
                    rentalCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT)));
                    if (General.getBadgeCount(getContext(), AppConstants.TENANTS_COUNT) <= 0) {
                        Log.i(TAG, "itha count 3 " + AppConstants.TENANTS_COUNT);
                        //option1Count.setVisibility(View.GONE);
                        Log.i(TAG, "itha " + AppConstants.TENANTS_COUNT);
                        option1Count.setVisibility(View.GONE);
                    } else {
                        Log.i(TAG, "itha count 4 " + AppConstants.TENANTS_COUNT);
                        option1Count.setVisibility(View.VISIBLE);
                        option1Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.TENANTS_COUNT)));
                    }
                    if (General.getBadgeCount(getContext(), AppConstants.OWNERS_COUNT) <= 0) {
                        Log.i(TAG, "ownerscount1" + General.getBadgeCount(getContext(), AppConstants.OWNERS_COUNT));
                        option2Count.setVisibility(View.GONE);
                    } else {
                        Log.i(TAG, "ownerscount2" + General.getBadgeCount(getContext(), AppConstants.OWNERS_COUNT));
                        option2Count.setVisibility(View.VISIBLE);
                        option2Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.OWNERS_COUNT)));
                    }
                }
                if (General.getBadgeCount(getContext(), AppConstants.RESALE_COUNT) > 0) {
                    resaleCount.setVisibility(View.VISIBLE);
                    resaleCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RESALE_COUNT)));
                }
            }
            catch (Exception e){
                Log.i("sustesting", "currentOptionSelectedString2" + e);

            }
            animatebadges();
            txtOption1.setText(strTenants);
            txtOption2.setText(strOwners);
            Log.i("PREOK CALLED13#", "currentOptionSelectedString2" + currentOptionSelectedString);
            if (currentOptionSelectedString.equalsIgnoreCase(strSeekers)) {
                currentOptionSelectedString = strTenants;
                lookingSeeking1 = "Tenant (";
                lookingSeeking2 = ") looking for";
                Log.i("PREOK CALLED10", "currentOptionSelectedString1" + currentOptionSelectedString+" lookingSeeking: "+lookingSeeking);
            }else
            if (currentOptionSelectedString.equalsIgnoreCase(strSeller)) {
                currentOptionSelectedString = strOwners;
                lookingSeeking1 = "Owner (";
                lookingSeeking2 = ") is having";
                Log.i("PREOK CALLED13", "currentOptionSelectedString1" + currentOptionSelectedString+" lookingSeeking: "+lookingSeeking);
            }
            if (jsonArrayReqLl != null && currentOptionSelectedString.equalsIgnoreCase(strTenants)) {
                Log.i("PREOK CALLED11","values set"+jsonArrayReqLl.toString());
                prompt = circularSeekbar.setValues(jsonArrayReqLl.toString());
            }
            else if (jsonArrayAvlLl != null && currentOptionSelectedString.equalsIgnoreCase(strOwners)) {
                Log.i("PREOK CALLED12", "values set" + jsonArrayAvlLl.toString());
                prompt = circularSeekbar.setValues(jsonArrayAvlLl.toString());
            }
            if(prompt == 1 ){
                texPtype.setText("No leads available in this area for now.");
                leadPrompt.setText("No leads available in this area for now.");
            }
            else{
                texPtype.setText("Please select a Lead and press OK.");
                leadPrompt.setText("Please select a Lead and press OK.");
            }

         Log.i("ja munna ja"," agaya beta tu : "+General.getSharedPreferences(getContext(),AppConstants.TT)+"   "+transaction_type);
//            txtPreviouslySelectedOption = txtOption1;

        }else  if(position==1){

            try {
                new CountDownTimer(1000, 500) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        Intent intent = new Intent(getContext(), BrokerMap.class);
                        startActivity(intent);
                    }
                }.start();
            } catch (Exception e) {}

        } else if (position == 2) {
//            txtPreviouslySelectedOption = txtOption1;
            General.setSharedPreferences(getContext(),AppConstants.TT,"RESALE");
            transaction_type="RESALE";

            option1Count.setBackgroundResource(R.drawable.badge_navy);
            option2Count.setBackgroundResource(R.drawable.badge_navy);
            atFor = "for";
            jsonObjectArray = null;
            try {
                SnackbarManager.show(
                        Snackbar.with(getContext())
                                .position(Snackbar.SnackbarPosition.TOP)
                                .text("Buy/Sell Property Type set")
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
            }
            catch(Exception e){}
            notClicked.setVisibility(View.VISIBLE);
            rentText.setVisibility(View.GONE);
            texPtype.setText("Please select a Lead and press OK.");
            texPstype.setVisibility(View.GONE);
            rentalCount.setVisibility(View.GONE);
            option1Count.setVisibility(View.INVISIBLE);
            option2Count.setVisibility(View.INVISIBLE);
            try {
                if (General.getBadgeCount(getContext(), AppConstants.RESALE_COUNT) <= 0) {
                    resaleCount.setVisibility(View.GONE);
                    option1Count.setVisibility(View.GONE);
                    option2Count.setVisibility(View.GONE);
                } else {
                    resaleCount.setVisibility(View.VISIBLE);
                    resaleCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RESALE_COUNT)));
                    if (General.getBadgeCount(getContext(), AppConstants.BUYER_COUNT) <= 0) {
                        Log.i("BADGE", "BUYER COUNT5 " + General.getBadgeCount(getContext(), AppConstants.BUYER_COUNT));
                        option1Count.setVisibility(View.GONE);
                    } else {
                        option1Count.setVisibility(View.VISIBLE);
                        option1Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.BUYER_COUNT)));
                    }
                    if (General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT) <= 0) {
                        Log.i("BADGE", "SELLER_COUNT1 " + General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT));
                        //option2Count.setVisibility(View.GONE);
                        option2Count.setVisibility(View.GONE);
                    } else {
                        Log.i("BADGE", "SELLER_COUNT2 " + General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT));
                        //option2Count.setVisibility(View.VISIBLE);
                        option2Count.setVisibility(View.VISIBLE);
                        option2Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT)));
                    }
                }
                if (General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT) > 0) {
                    rentalCount.setVisibility(View.VISIBLE);
                    rentalCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT)));
                }
            }
            catch (Exception e){
            }
            animatebadges();
            //sale
            //Buyer, Seller
            txtOption1.setText(strSeekers);
            txtOption2.setText(strSeller);
            Log.i("PREOK CALLED13*", "currentOptionSelectedString2" + currentOptionSelectedString);
            if (currentOptionSelectedString.equalsIgnoreCase(strTenants)) {
                currentOptionSelectedString = strSeekers;
                lookingSeeking1 = "Buyer (";
                lookingSeeking2 = ") is looking for";
                Log.i("PREOK CALLED16", "currentOptionSelectedString2" + currentOptionSelectedString+" lookingSeeking: "+lookingSeeking);
            }else
           // if (currentOptionSelectedString.equalsIgnoreCase(strOwners))
            {
                currentOptionSelectedString = strSeller;
                lookingSeeking1 = "Seller (";
                lookingSeeking2 = ") is having";
                Log.i("PREOK CALLED19", "currentOptionSelectedString2" + currentOptionSelectedString+" lookingSeeking: "+lookingSeeking);
            }
            if (jsonArrayReqOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeekers)) {
                Log.i("PREOK CALLED17", "values set" + jsonArrayReqOr.toString());
                prompt = circularSeekbar.setValues(jsonArrayReqOr.toString());
            }
            else if (jsonArrayAvlOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeller)) {
                Log.i("PREOK CALLED18", "values set" + jsonArrayAvlOr.toString());
                prompt = circularSeekbar.setValues(jsonArrayAvlOr.toString());
            }
            try {
                buildingPrice.clear();
                buildingPrice.addAll(ORbuildingPrice);
                setChart();
            }catch(Exception e){
            }
            if(prompt == 1 ){
                texPtype.setText("No leads available in this area for now.");
                leadPrompt.setText("No leads available in this area for now.");
            }
            else{
                texPtype.setText("Please select a Lead and press OK.");
                leadPrompt.setText("Please select a Lead and press OK.");
            }
        }
        synchronized (circularSeekbar) {
            circularSeekbar.post(new Runnable() {
                @Override
                public void run() {
                    circularSeekbar.requestLayout();
                    circularSeekbar.invalidate();
                }
            });
        }
        try {
            if (!General.isNetworkAvailable(getContext())) {
                texPtype.setText("Go online to get Leads.");
            }
        }
        catch(Exception e){
        }
        Log.i("ja munna ja"," agaya beta tu 1 : "+General.getSharedPreferences(getContext(),AppConstants.TT)+ "  "+transaction_type);
    }


    @Override
    public void onclick(int position, JSONArray m, String show, int x_c, int y_c) {
        deal.setEnabled(true);
        deal.setBackground(getResources().getDrawable(R.drawable.deals_button_background));
        try {
            leadPrompt.setVisibility(View.VISIBLE);
            leadPrompt.setText("Please select a Lead and press OK.");
            jsonObjectArray = m;
            selectedItemPosition = position;
            String ptype = null;
            String pstype;
            String name;
            String furnishing = "Semi-Furnished";
            String possession_date = "";
            if(jsonObjectArray.getJSONObject(position).getString("possession_date") != ""){
                possession_date = jsonObjectArray.getJSONObject(position).getString("possession_date");
            }
            pstype = jsonObjectArray.getJSONObject(position).getString("property_subtype");
            if(jsonObjectArray.getJSONObject(position).getString("furnishing").equalsIgnoreCase("uf"))
                furnishing = "Un-Furnished";
            else if(jsonObjectArray.getJSONObject(position).getString("furnishing").equalsIgnoreCase("uf"))
                furnishing = "Fully-Furnished";
            Log.i("debug circ","inside onclick  : "+lookingSeeking1);
            Log.i("debug circ","inside onclick m "+jsonObjectArray);
            Log.i(TAG,"furnishing "+jsonObjectArray.getJSONObject(position).getString("furnishing"));
            ptype = jsonObjectArray.getJSONObject(position).getString("property_type");
            name = jsonObjectArray.getJSONObject(position).getString("name");
            Log.i(TAG,"property_type "+ptype);
            Log.i(TAG, "property_subtype " + pstype);
            texPtype.setText(lookingSeeking1+name+lookingSeeking2);
            if(ptype.equalsIgnoreCase("home")) {
                texPstype.setText(furnishing + " " + ptype.substring(0, 1).toUpperCase() + ptype.substring(1));
            }
            else {
                texPstype.setText(furnishing + " " + ptype.substring(0, 1).toUpperCase() + ptype.substring(1) );
                pstype = pstype + " sq.ft";
            }
            Log.i("Diamond","diamond "+possession_date);
            if(possession_date.isEmpty()){
                Log.i("Diamond","diamond 1 "+possession_date);
                pd.setVisibility(View.VISIBLE);
                pd.setText(pstype);
            }else{
                Log.i("Diamond","diamond 2 "+possession_date);
                pd.setVisibility(View.VISIBLE);
                pd.setText(pstype+" by "+possession_date);
            }
            if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("RENTAL")) {
                rentText.setText("@" + General.currencyFormat(jsonObjectArray.getJSONObject(position).getString("price")) + " /m.");
            }
            else {
                rentText.setText("@" + General.currencyFormat(jsonObjectArray.getJSONObject(position).getString("price")));
            }
            General.setSharedPreferences(getContext(),AppConstants.PTYPE,ptype);
            General.setSharedPreferences(getContext(),AppConstants.PSTYPE,pstype);
            General.setSharedPreferences(getContext(),AppConstants.PRICE,jsonObjectArray.getJSONObject(position).getString("price"));
            Log.i(TAG, "show is " + show);
            if(show.equals("show")) {
                notClicked.setVisibility(View.GONE);
                leadPrompt.setVisibility(View.GONE);
                rentText.setVisibility(View.VISIBLE);
                //   displayOkText.setVisibility(View.VISIBLE);
                texPtype.setVisibility(View.VISIBLE);
                texPstype.setVisibility(View.VISIBLE);
                rentText.startAnimation(bounce);
                deal.setEnabled(false);
                deal.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey));
            }
            else if(show.equals("hide")) {
                jsonObjectArray = null;
                if(buildingSliderflag) {
                    buildingSlider.startAnimation(slide_down);
                    buildingSlider.setVisibility(View.GONE);
                    buildingSliderflag = false;
                    Intent intent = new Intent(AppConstants.BUILDINGSLIDERFLAG);
                    intent.putExtra("buildingSliderFlag",buildingSliderflag);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    if(buildingsSelected.size() !=0)
                        buildingsSelected.clear();
                    selectB.setText("Selected buildings ["+buildingsSelected.size()+"]");
                    selectB.performClick();
                }
                notClicked.setVisibility(View.VISIBLE);
                rentText.setVisibility(View.GONE);
                texPtype.setVisibility(View.GONE);
                texPstype.setVisibility(View.GONE);
                pd.setVisibility(View.GONE);
            }
            else {
                notClicked.setVisibility(View.GONE);
                rentText.setVisibility(View.VISIBLE);
                texPtype.setVisibility(View.VISIBLE);
                texPstype.setVisibility(View.VISIBLE);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (REQUEST_CODE_TO_SELECT_CLIENT == reqCode) {
                Uri contactData = data.getData();
                Cursor c = getActivity().getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // TODO Fetch other Contact details as you want to use
                    //  contactText.setText(name);
                    //  contactText.setPadding(8, 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void replaceFragment(Bundle args) {
        //open deals listing
        Intent openDealsListing = new Intent(getActivity(), BrokerDealsListActivity.class);
        startActivity(openDealsListing);
    }

    public void animatebadges(){
        if(General.getBadgeCount(getContext(),AppConstants.RENTAL_COUNT)>0) {
            rentalCount.startAnimation(bounce);
            if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("resale")) {
                if (General.getBadgeCount(getContext(), AppConstants.TENANTS_COUNT) > 0) {
                    option1Count.startAnimation(bounce);
                }
                if (General.getBadgeCount(getContext(), AppConstants.OWNERS_COUNT) > 0) {
                    option2Count.startAnimation(bounce);
                }
            }
        }
        if(General.getBadgeCount(getContext(),AppConstants.RESALE_COUNT)>0) {
            resaleCount.startAnimation(bounce);

            if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("rental")) {
                if (General.getBadgeCount(getContext(), AppConstants.BUYER_COUNT) > 0) {
                    option1Count.startAnimation(bounce);
                }
                if (General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT) > 0) {
                    option2Count.startAnimation(bounce);
                }
            }
        }
    }


    private void setBadges(){

        if(rentalCount1 == 0){
            Log.i(TAG,"saki rental if "+rentalCount1);
            Log.i("gcm local broadcast","gcm local broadcast5");
            rentalCount.setVisibility(View.GONE);
//            option1Count.setVisibility(View.GONE);
//            option2Count.setVisibility(View.GONE);

        }
     else {
            Log.i(TAG,"saki rental else "+rentalCount1);

            Log.i("gcm local broadcast","gcm local broadcast3");
        rentalCount.setVisibility(View.VISIBLE);
        rentalCount.setText(String.valueOf(rentalCount1));
            if (General.getSharedPreferences(getContext(), AppConstants.TT).equalsIgnoreCase("rental")) {

                if (tenantsCount1 == 0) {
                    Log.i(TAG, "saki ten if " + tenantsCount1);
                    option1Count.setVisibility(View.GONE);
                } else {
                    Log.i(TAG, "saki ten else " + tenantsCount1);
                    Log.i("gcm local broadcast", "gcm local broadcast4 " + tenantsCount1);
                    option1Count.setVisibility(View.VISIBLE);
                    option1Count.setText(String.valueOf(tenantsCount1));
                }

                if (ownersCount1 == 0) {
                    Log.i(TAG, "saki ow if " + ownersCount1);
                    option2Count.setVisibility(View.GONE);
                } else {
                    Log.i(TAG, "saki ow else " + ownersCount1);
                    Log.i(TAG, "OnSharedPreferenceChangeListener 3");
                    option2Count.setVisibility(View.VISIBLE);
                    option2Count.setText(String.valueOf(ownersCount1));
                }
            }
         }

        if(resaleCount1 == 0){
            Log.i(TAG,"saki resale if "+resaleCount1);
            resaleCount.setVisibility(View.GONE);
//            option1Count.setVisibility(View.GONE);
//            option2Count.setVisibility(View.GONE);

        }
        else {
            Log.i(TAG, "saki resale else " + resaleCount1);
            Log.i(TAG, "OnSharedPreferenceChangeListener 3");
            resaleCount.setVisibility(View.VISIBLE);
            resaleCount.setText(String.valueOf(resaleCount1));

            if (General.getSharedPreferences(getContext(), AppConstants.TT).equalsIgnoreCase("resale")) {

                if (buyerCount1 == 0) {
                    Log.i(TAG, "saki buy if " + buyerCount1);
                    option1Count.setVisibility(View.GONE);
                } else {
                    Log.i(TAG, "saki buy else " + buyerCount1);
                    Log.i(TAG, "OnSharedPreferenceChangeListener 3");
                    option1Count.setVisibility(View.VISIBLE);
                    option1Count.setText(String.valueOf(buyerCount1));
                }

                if (sellerCount1 == 0) {
                    Log.i(TAG, "saki buy if " + sellerCount1);
                    option2Count.setVisibility(View.GONE);
                } else {
                    Log.i(TAG, "saki buy else " + sellerCount1);
                    Log.i(TAG, "OnSharedPreferenceChangeListener 3");
                    option2Count.setVisibility(View.VISIBLE);
                    option2Count.setText(String.valueOf(sellerCount1));
                }
            }
        }


    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        Log.i("GRAPH","on value selected");


        if(!pricechart) {
            // make highlights null as we are going to rehighlight altogether
            chart.highlightValues(null);
            Log.i("GRAPH", "executed ");
            Log.i("GRAPH10", "buildings selected and new build " + buildingsSelected + " " + e.getXIndex());

            if (!buildingsSelected.contains(e.getXIndex())) {
                Log.i("GRAPH", "1");


                if (buildingsSelected.size() < 3) {
                    buildingsSelected.add(e.getXIndex());
                    selectB.setText("Selected buildings ["+buildingsSelected.size()+"]");

                    Log.i("GRAPH10", "buildings selected after add " + buildingsSelected);

                    // for (int i = 0; i < buildingsSelected.size(); i++) {

                    if (buildingsSelected.size() == 1) {
                        okBtn.setText(buildingNames.get((buildingsSelected.get(0))));
                        Log.i("GRAPH10", "buildings selected " + buildingsSelected);
                        Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                        chart.highlightValues(new Highlight[]{h0});


                    } else if (buildingsSelected.size() == 2) {
                        okBtn.setText(buildingNames.get((buildingsSelected.get(0)))+", "+buildingNames.get((buildingsSelected.get(1))));
                        Log.i("GRAPH11", "buildings selected " + buildingsSelected);
                        Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                        Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
                        chart.highlightValues(new Highlight[]{h0, h1});

                    } else {
                        okBtn.setText(buildingNames.get((buildingsSelected.get(0)))+", "+buildingNames.get((buildingsSelected.get(1)))+", "+buildingNames.get((buildingsSelected.get(2))));
                        Log.i("GRAPH12", "buildings selected " + buildingsSelected);
                        Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                        Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
                        Highlight h2 = new Highlight(buildingsSelected.get(2), 0);
                        chart.highlightValues(new Highlight[]{h0, h1, h2});


                    }
                    //             }
                } else if (buildingsSelected.size() == 3) {
                    Log.i("GRAPH", "3");
                    //cant be added

                    SnackbarManager.show(
                            Snackbar.with(getContext())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .text("Max 3 buildings can be selected at a time.")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


                    Log.i("GRAPH30", "buildings selected " + buildingsSelected);
                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                    Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
                    Highlight h2 = new Highlight(buildingsSelected.get(2), 0);
                    chart.highlightValues(new Highlight[]{h0, h1, h2});

                }
            } else {
                Log.i("GRAPH", "2");
                Log.i("GRAPH", "removed " + Integer.valueOf(e.getXIndex()));
                // buildingsSelected.remove(Integer.valueOf(e.getXIndex()));
                buildingsSelected.remove(Integer.valueOf(e.getXIndex()));
                selectB.setText("Selected buildings ["+buildingsSelected.size()+"]");
                Log.i("GRAPH", "after removing " + buildingsSelected);
                //    for (int i = 0; i < buildingsSelected.size(); i++) {

                Log.i("GRAPH", "size " + buildingsSelected.size());
                if (buildingsSelected.size() == 1) {
                    okBtn.setText(buildingNames.get((buildingsSelected.get(0))));

                    Log.i("GRAPH20", "buildings selected " + buildingsSelected);
                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                    chart.highlightValues(new Highlight[]{h0});

                } else if (buildingsSelected.size() == 2) {

                    okBtn.setText(buildingNames.get((buildingsSelected.get(0)))+", "+buildingNames.get((buildingsSelected.get(1))));

                    Log.i("GRAPH21", "buildings selected " + buildingsSelected);
                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                    Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
                    chart.highlightValues(new Highlight[]{h0, h1});


                } else if (buildingsSelected.size() == 0) {
                    okBtn.setText("Choose 3 buildings");

                    chart.highlightValues(null);
                }
                //        else if (buildingsSelected.size() == 3) {
//                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
//                    Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
//                    Highlight h2 = new Highlight(buildingsSelected.get(2), 0);
//                    chart.highlightValues(new Highlight[]{h0, h1, h2});
//                }
                //   }
            }
            //  Highlight [] highs = highlights.toArray(new Highlight[highlights.size()]);
            // chart.highlightValues(highs);


        }
        else{
            chart.highlightValues(null);
        }

    }


    @Override
    public void onNothingSelected() {

        if(!pricechart) {
            Log.i("GRAPH", "nothing selected called size " + buildingsSelected.size() + " chartIndex " + chartIndex);

            if (buildingsSelected.contains(chartIndex)) {
                Log.i("GRAPH", "2");
                Log.i("GRAPH", "removed " + Integer.valueOf(chartIndex));
                // buildingsSelected.remove(Integer.valueOf(e.getXIndex()));
                buildingsSelected.remove(Integer.valueOf(chartIndex));
                selectB.setText("Selected buildings ["+buildingsSelected.size()+"]");
                Log.i("GRAPH", "after removing " + buildingsSelected);
                //    for (int i = 0; i < buildingsSelected.size(); i++) {

                Log.i("GRAPH", "size " + buildingsSelected.size());
                if (buildingsSelected.size() == 1) {
                    okBtn.setText(buildingNames.get((buildingsSelected.get(0))));

                    Log.i("GRAPH20", "buildings selected " + buildingsSelected);
                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                    chart.highlightValues(new Highlight[]{h0});

                } else if (buildingsSelected.size() == 2) {
                    okBtn.setText(buildingNames.get((buildingsSelected.get(0)))+", "+buildingNames.get((buildingsSelected.get(1))));

                    Log.i("GRAPH21", "buildings selected " + buildingsSelected);
                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                    Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
                    chart.highlightValues(new Highlight[]{h0, h1});


                } else if (buildingsSelected.size() == 0) {
                    okBtn.setText("Choose 3 buildings");

                    chart.highlightValues(null);
                }
            } else if (buildingsSelected.size() == 3) {
                //if building touched is already not selected and already 3 buildings are highlighted
                Log.i("GRAPH", "3");
                //cant be added

                SnackbarManager.show(
                        Snackbar.with(getContext())
                                .position(Snackbar.SnackbarPosition.TOP)
                                .text("Max 3 buildings can be selected at a time.")
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


                Log.i("GRAPH30", "buildings selected " + buildingsSelected);
                Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
                Highlight h2 = new Highlight(buildingsSelected.get(2), 0);
                chart.highlightValues(new Highlight[]{h0, h1, h2});


            }
        }
        else{
            chart.highlightValues(null);
        }

//        if (buildingsSelected.size() == 0) {
//            // chart.highlightValues(null);
//            //do nothing
//        }
//        else if (buildingsSelected.size() == 1) {
//            // chart.highlightValues(null);
//            buildingsSelected.remove(Integer.valueOf(chartIndex));
//        }else {
//            Log.i("GRAPH", "nothing selected " + chartIndex + " " + buildingsSelected.get(0));
//            //buildingsSelected.remove(Integer.valueOf(chartIndex));
//
//            if (buildingsSelected.get(0) == chartIndex) {
//                Log.i("GRAPH", "nothing selected  highlight baki array");
//                //remove first element from array and heiglight remaining
//                buildingsSelected.remove(Integer.valueOf(chartIndex));
//                if (buildingsSelected.size() == 1) {
//                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
//                    chart.highlightValues(new Highlight[]{h0});
//                }
//                else if (buildingsSelected.size() == 2) {
//
//                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
//                    Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
//                    chart.highlightValues(new Highlight[]{h0, h1});
//                }
//            } else {
//
//                Log.i("GRAPH", "nothing selected  highlight else " + buildingsSelected.get(0));
//            }
//        }
//
//
 }


    ////////////////////////////////
//  Custom chart methos
////////////////////////////////////
    public void highlight(){

        Highlight h0 = new Highlight(3, 0);
        Highlight h1 = new Highlight(6, 0);
        Highlight h2 = new Highlight(9, 0);

        chart.highlightValues(new Highlight[]{h0, h1, h2});

        highlighter = chart.getHighlighter();

        Log.i("GRAPH","Highlighted "+h0+" " + h1 +" "+ h2 );

        highs = chart.getHighlighted();
        Log.i("GRAPH","highs "+highs );
    }


    ////////////////////////////////
//  ChartGestureListener
////////////////////////////////////


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        // Log.i("GRAPH","onChartGestureStart "+me);
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        //  Log.i("GRAPH","onChartGestureEnd "+me);

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        //   Log.i("GRAPH","onChartLongPressed "+me);
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

        //Log.i("GRAPH","onChartDoubleTapped "+me);
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        //  Log.i("GRAPH","me onChartSingleTapped "+me);
//
        //highlighter.getHighlight(me.getX(0),me.getY(0));
        highlight = highlighter.getHighlight(me.getX(),me.getY());
        // highlights = new ArrayList<Highlight>(Arrays.asList(highs));
//        highlights.add(highlight);
//        highs = highlights.toArray(new Highlight[highlights.size()]);
//               chart.highlightValues(highs);
//        Highlight h0 = new Highlight(0, 0);
//        Highlight h1 = new Highlight(1, 0);
//        Highlight h2 = new Highlight(2, 0);
//        chart.highlightValues(new Highlight[]{h0, h1, h2});

//        Highlight h0 = new Highlight(3, 0);
//        Highlight h1 = new Highlight(6, 0);
//        Highlight h2 = new Highlight(9, 0);
//        chart.highlightValues(new Highlight[]{h0, h1, h2});

//        Log.i("GRAPH","me "+highlight.getXIndex());
        chartIndex = highlight.getXIndex();
//        highlight();
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("GRAPH","onChartFling "+me2);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
//        Log.i("GRAPH","onChartScale "+me);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

        if(!pricechart) {
            try {
                e = chart.getEntryByTouchPoint(me.getX(), me.getY());
                Log.i("GRAPH", "onChartTranslate entry " + e + "  (buildingsPage-1)*10+6 " + ((buildingsPage - 1) * 10 + 6));

                if (buildingsPage < 3) {
                    Log.i("buildingsPage","buildingsPage "+buildingsPage);
                    if (e != null && e.getXIndex() >= ((buildingsPage - 1) * 10 + 6)) {

                        buildingsPage++;
                        if (buildingPriceOR.size() != 0) {
                            buildingPriceOR.clear();
                            LLbuildingPrice.clear();
                        }


                        if (buildingPriceLL.size() != 0) {
                            buildingPriceLL.clear();
                            ORbuildingPrice.clear();
                        }
                        pagination = true;
                        chart.clear();
                        brokerbuildings(buildingsPage);
                    }
                }
            }
            catch(Exception e){

            }
        }
    }

   /* public void onTabclick(int position, JSONArray m, String show, int x_c, int y_c) {
        try {
            jsonObjectArray = m;
            selectedItemPosition = position;
            String ptype = null;
            String pstype;
            String name;
            pstype = jsonObjectArray.getJSONObject(position).getString("property_subtype");
            Log.i("debug circ","inside onclick sushil");
            Log.i("debug circ","inside onclick m "+jsonObjectArray);
            ptype = jsonObjectArray.getJSONObject(position).getString("property_type");
            name = jsonObjectArray.getJSONObject(position).getString("name");
            Log.i(TAG,"property_type "+ptype);
            Log.i(TAG, "property_subtype " + pstype);
            texPtype.setText(lookingSeeking1+name+lookingSeeking2);
            texPstype.setText(ptype.substring(0, 1).toUpperCase() + ptype.substring(1)+" ("+pstype+")");

            if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("RENTAL")){
                rentText.setText("@"+General.currencyFormat(jsonObjectArray.getJSONObject(position).getString("price"))+" /m.");
                Log.i(TAG, "Rental type " + General.currencyFormat(jsonObjectArray.getJSONObject(position).getString("price"))+" /m.");
            }
            else{
                rentText.setText("@"+General.currencyFormat(jsonObjectArray.getJSONObject(position).getString("price")));
                Log.i(TAG, "Resale type " + General.currencyFormat(jsonObjectArray.getJSONObject(position).getString("price")));

            }
            Log.i(TAG, "show is " + show);
            if(show.equals("show")) {
                notClicked.setVisibility(View.GONE);
                rentText.setVisibility(View.VISIBLE);
                texPtype.setVisibility(View.VISIBLE);
                texPstype.setVisibility(View.VISIBLE);
            }
            else if(show.equals("hide")) {
                jsonObjectArray = null;
                if(buildingSliderflag) {
                    buildingSlider.startAnimation(slide_down);
                    buildingSlider.setVisibility(View.GONE);
                    buildingSliderflag = false;
                    Intent intent = new Intent(AppConstants.BUILDINGSLIDERFLAG);
                    intent.putExtra("buildingSliderFlag",buildingSliderflag);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    //clearChart();
                    if(buildingsSelected.size() !=0)
                        buildingsSelected.clear();
                    selectB.setText("Selected buildings ["+buildingsSelected.size()+"]");
                    selectB.performClick();
                }
                notClicked.setVisibility(View.VISIBLE);
                rentText.setVisibility(View.GONE);
                texPtype.setVisibility(View.GONE);
                texPstype.setVisibility(View.GONE);
            }
            else {
                notClicked.setVisibility(View.GONE);
                rentText.setVisibility(View.VISIBLE);
                texPtype.setVisibility(View.VISIBLE);
                texPstype.setVisibility(View.VISIBLE);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    public void walkthroughBroker(final View v) {
        countertut = 0;
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this.getActivity());
        /*sequence.addSequenceItem(v.findViewById(R.id.iv_client_type),
                "         Select client type", "      GOT IT! (Go to next screen)");*/
        sequence.addSequenceItem(v.findViewById(R.id.iv_transection_type),
                "         Select TransactionType\n\n\n", "       GOT IT! (Go to next screen)");
        sequence.addSequenceItem(v.findViewById(R.id.iv_client_type),
                "            Select client type\n\n\n", "      GOT IT! (Go to next screen)");
        sequence.addSequenceItem(v.findViewById(R.id.imageView111),
                "           Select Client Lead", "      GOT IT! (Go to next screen)");
        sequence.addSequenceItem(v.findViewById(R.id.okButton),
                " Press 'OK' to Start dealing with Client", "      GOT IT! (Go to next screen)");
        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
            @Override
            public void onDismiss(MaterialShowcaseView materialShowcaseView, int i) {
                countertut++;
                if (countertut == 4) {
                    Log.i("ischecked", "beacon_walk_broker==========  :" + beacon);
                    Log.i("ischecked", "beacon_walk1_broker  ==========   :" + beacon);
                    try {
                        if (beacon.equalsIgnoreCase("true"))
                            beaconAlertBroker(v);
                    } catch (InterruptedException e) {e.printStackTrace();}
                    // rippleBackground4.startRippleAnimation();
                }
            }
        });
        sequence.start();

    }

    public void beaconAlertBroker( final View rootView) throws InterruptedException {
        final RippleBackground rippleBackground1 = (RippleBackground) rootView.findViewById(R.id.broker_content);
        final RippleBackground rippleBackground2 = (RippleBackground) rootView.findViewById(R.id.broker_content1);
        final RippleBackground rippleBackground3 = (RippleBackground) rootView.findViewById(R.id.broker_content3);
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                rippleBackground1.startRippleAnimation();
                SnackbarManager.show(
                        Snackbar.with(getContext())
                                .text("Select the Role")

                                .position(Snackbar.SnackbarPosition.TOP)
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());

            }

            public void onFinish() {
                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        rippleBackground1.stopRippleAnimation();
                        rippleBackground2.startRippleAnimation();
                        SnackbarManager.show(
                                Snackbar.with(getContext())
                                        .text("Select the Lead")

                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());

                    }
                    public void onFinish() {
                        new CountDownTimer(3000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                rippleBackground2.stopRippleAnimation();
                                rippleBackground3.startRippleAnimation();
                                SnackbarManager.show(
                                        Snackbar.with(getContext())
                                                .text("Press 'OK' to select three Property for Visit")
                                                .position(Snackbar.SnackbarPosition.TOP)
                                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());
                            }
                            public void onFinish() {
                                rippleBackground3.stopRippleAnimation();
                            }
                        }.start();
                    }
                }.start();
            }
        }.start();
    }


    public void resetSeekBar(){
        mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type2_selector}, new String[]{"30", "40", "15"}, new String[]{getContext().getResources().getString(R.string.Rental),"listing", getContext().getResources().getString(R.string.Resale)}));
    }




}
