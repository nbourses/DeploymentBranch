package com.nbourses.oyeok.fragments;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.animation.AccelerateDecelerateInterpolator;
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
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

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

    @Bind(R.id.beaconOK)
    TextView beaconOK;

    @Bind(R.id.texPtype)
    TextView texPtype;

    // @Bind(R.id.contactText)
    // TextView contactText;

    // @Bind(R.id.pickContact)
    // Button pickContact;

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



//
//    @Bind(R.id.option2CountCont1)
//    LinearLayout option2CountCont1;
//
//    @Bind(R.id.option2CountCont2)
//    LinearLayout option2CountCont2;



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

    private String lookingSeeking = "Tenant is looking for";
    private String atFor = "at";

    private long mSampleDurationTime = 5; // 5 msec
    private boolean continueToRun = true;
    private Handler mHandler;
    private float fl = 1f;
    private int count =1;
    private boolean pagination = false;
    private BarChart chart;
    private View  v;
    private int rentalCount1;
    private int resaleCount1;
    private int tenantsCount1;
    private int ownersCount1;
    private int buyerCount1;
    private int sellerCount1;


    Animation bounce;
    Animation zoomin;
    Animation zoomout;


    public BrokerPreokFragment() {
        // Required empty public constructor
    }

    private BroadcastReceiver slideDownBuildings = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
   Log.i("time to slide down","slide down");
            buildingSlider.startAnimation(slide_down);
            buildingSlider.setVisibility(View.GONE);
            buildingSliderflag = false;

            if(buildingsSelected.size() !=0)
                buildingsSelected.clear();
            selectB.setText("Select buildings ["+buildingsSelected.size()+"]");
            selectB.performClick();
        }
    };

    private BroadcastReceiver badgeCountBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i("gcm local broadcast","gcm local broadcast");
            if(intent.getExtras().getInt(AppConstants.RENTAL_COUNT) != 0 ){
                Log.i("gcm local broadcast","gcm local broadcast1");
                rentalCount1 = intent.getExtras().getInt(AppConstants.RENTAL_COUNT);

            }

            if(intent.getExtras().getInt(AppConstants.RESALE_COUNT) != 0){
                resaleCount1 = intent.getExtras().getInt(AppConstants.RESALE_COUNT);
            }
            if(intent.getExtras().getInt(AppConstants.TENANTS_COUNT) != 0){
                Log.i("gcm local broadcast","gcm local broadcast2");
                tenantsCount1 = intent.getExtras().getInt(AppConstants.TENANTS_COUNT);
            }
            if(intent.getExtras().getInt(AppConstants.OWNERS_COUNT) != 0){
                ownersCount1 = intent.getExtras().getInt(AppConstants.OWNERS_COUNT);
            }
            if(intent.getExtras().getInt(AppConstants.BUYER_COUNT) != 0){
                buyerCount1 = intent.getExtras().getInt(AppConstants.BUYER_COUNT);
            }
            if(intent.getExtras().getInt(AppConstants.SELLER_COUNT) != 0){
                sellerCount1 = intent.getExtras().getInt(AppConstants.SELLER_COUNT);
            }

            setBadges();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_broker_preok, container, false);
        ButterKnife.bind(this, v);
        bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        zoomin = AnimationUtils.loadAnimation(getContext(), R.anim.zoomin);
        zoomout = AnimationUtils.loadAnimation(getContext(), R.anim.zoomout);



       chart = (BarChart) v.findViewById(R.id.chart);
        init();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(slideDownBuildings, new IntentFilter(AppConstants.SLIDEDOWNBUILDINGS));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(badgeCountBroadcast, new IntentFilter(AppConstants.BADGE_COUNT_BROADCAST));
        preok();
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(slideDownBuildings);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(badgeCountBroadcast);
    }



    private void init() {


        highlighter = new ChartHighlighter(chart);

        slide_up = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up);
        slide_down = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_down);

        animation = ObjectAnimator.ofFloat(chart, "rotationY", 0.0f, 360f);  // HERE 360 IS THE ANGLE OF ROTATE, YOU CAN USE 90, 180 IN PLACE OF IT,  ACCORDING TO YOURS REQUIREMENT

        animation.setDuration(500); // HERE 500 IS THE DURATION OF THE ANIMATION, YOU CAN INCREASE OR DECREASE ACCORDING TO YOURS REQUIREMENT
        animation.setInterpolator(new AccelerateDecelerateInterpolator());



        buildingNames = new ArrayList<String>();
        buildingPrice = new ArrayList<Integer>();


//        buildingPrice.addAll(Arrays.asList(4,8,12,15,18,22,16,2,10,7));
//        buildingNames.addAll(Arrays.asList("Abhinav","Mahesh","Neha","Ekdanta","Karachi","Konark","Vishal","Angels Paradise", "Divyam","Om"));
        brokerbuildings(buildingsPage);



        zoomin.setAnimationListener(new Animation.AnimationListener() {

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
                beaconOK.startAnimation(zoomout);

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
                beaconOK.startAnimation(zoomin);

            }
        });


        if (General.getBadgeCount(getContext(), AppConstants.HDROOMS_COUNT) <= 0)
            hdroomsCount.setVisibility(View.GONE);
        else {
            hdroomsCount.setVisibility(View.VISIBLE);
            hdroomsCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.HDROOMS_COUNT)));
        }




//            SharedPreferences prefs =
//                    PreferenceManager.getDefaultSharedPreferences(getContext());
//            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
//                    try {
//                        if (key.equals(AppConstants.RENTAL_COUNT)) {
//                            Log.i(TAG, "OnSharedPreferenceChangeListener 1");
//                            Log.i(TAG, "OnSharedPreferenceChangeListener 1 rent " + General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT));
//                            if (General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT) <= 0) {
//                                Log.i(TAG, "OnSharedPreferenceChangeListener 2");
//                                rentalCount.setVisibility(View.GONE);
//                            } else {
//                                Log.i(TAG, "OnSharedPreferenceChangeListener 3");
//                                rentalCount.setVisibility(View.VISIBLE);
//                                rentalCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT)));
//                            }
//                        }
//                        if (key.equals(AppConstants.RESALE_COUNT)) {
//                            if (General.getBadgeCount(getContext(), AppConstants.RESALE_COUNT) <= 0)
//                                resaleCount.setVisibility(View.GONE);
//                            else {
//                                resaleCount.setVisibility(View.VISIBLE);
//                                resaleCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RESALE_COUNT)));
//                            }
//
//
//                        }
//
//                        if (key.equals(AppConstants.TENANTS_COUNT)) {
//                            if (General.getBadgeCount(getContext(), AppConstants.TENANTS_COUNT) <= 0)
//                                option1Count.setVisibility(View.GONE);
//                            else {
//                                option1Count.setVisibility(View.VISIBLE);
//                                option1Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.TENANTS_COUNT)));
//                            }
//
//                        }
//                        if (key.equals(AppConstants.OWNERS_COUNT)) {
//                            if (General.getBadgeCount(getContext(), AppConstants.OWNERS_COUNT) <= 0)
//                                option2Count.setVisibility(View.GONE);
//                            else {
//                                option2Count.setVisibility(View.VISIBLE);
//                                option2Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.OWNERS_COUNT)));
//                            }
//
//                        }
//
//                        if (key.equals(AppConstants.BUYER_COUNT)) {
//                            if (General.getBadgeCount(getContext(), AppConstants.BUYER_COUNT) <= 0)
//                                option1Count.setVisibility(View.GONE);
//                            else {
//                                option1Count.setVisibility(View.VISIBLE);
//                                option1Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.BUYER_COUNT)));
//                            }
//
//                        }
//
//                        if (key.equals(AppConstants.SELLER_COUNT)) {
//                            if (General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT) <= 0)
//                                option2Count.setVisibility(View.GONE);
//                            else {
//                                option2Count.setVisibility(View.VISIBLE);
//                                option2Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT)));
//                            }
//
//                        }
//
//                    } catch (Exception e) {
//                        Log.e(TAG, e.getMessage());
//                    }
//                }
//
//            };
//            prefs.registerOnSharedPreferenceChangeListener(listener);




Log.i("PHASE","before adapter set");
        mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(),
                new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector},
                new String[]{"30", "15"},
                new String[]{"Rental", "Resale"
                }));
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
        preok();
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
                //
                Log.i("yo mana","yo mana "+fr +" "+((buildingsPage - 1) * 10 + 7));
                // chart.zoomAndCenterAnimated(6.6f, 1f, 0, 0, YAxis.AxisDependency.RIGHT, 2000);
                chart.fitScreen();
                Log.i("yo mana","yo mana "+fr +" "+((buildingsPage - 1) * 10 + 7));
                //chart.moveViewToX(10);
                chart.moveViewToX(((buildingsPage*10)-14));
                chart.zoom(fr, 1f, 0, 0);
                // chart.moveViewToX((buildingsPage - 1) * 10 + 7);


            }



//            for (Float fl = 1f;fl<= 3.3f;fl = fl+ 0.1f){
//               // try {
//                    //Thread.sleep(500);
//                    chart.zoom(fl, 1f, 0, 0);
////                } catch (InterruptedException e1) {
////                    e1.printStackTrace();
////                }
//            }

//            new CountDownTimer(30000, 1000) {
//
//                public void onTick(long millisUntilFinished) {
//                    chart.
//                }
//
//                public void onFinish() {
//
//                }
//            }.start();


            try {
                if(buildingsPage<=1) {
                  entries.clear();
                  labels.clear();
                }
            }
            catch(Exception e)
            {

            }



            chart.editEntryValue = false;
//            entries.clear();
            for (int i = 0; i < buildingPrice.size(); i++) {

                Log.i("adding CHARTS","==========");
                entries.add(new BarEntry(buildingPrice.get(i), i));
            }

            if(buildingPrice.size()>0) {
                dataset = new BarDataSet(entries, Integer.toString(buildingPrice.size()));
               dataset.setColors(new int[] { R.color.greenish_blue, R.color.google_yellow, R.color.red_light}, getContext());
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
            //buildingsSelected.clear();

//chart = (BarChart) v.findViewById(R.id.chart);

            dataset = new BarDataSet(entries, Integer.toString(buildingsSelected.size()));
            dataset.setColors(new int[] { R.color.greenish_blue, R.color.google_yellow, R.color.red_light}, getContext());

        }





        //labels.addAll(Arrays.asList("Abhinav","Mahesh","Neha","Ekdanta","Karachi","Konark","Vishal","Angels Paradise", "Divyam","Om"));

        Log.i("GRAPH", "labels " + labels);
        Log.i("GRAPH", "labels " + dataset);


       // chart = (BarChart) v.findViewById(R.id.chart);
        BarData data = new BarData(labels, dataset);
        chart.setData(data); // set the data and list of lables into chart

    }

    private final Runnable mRunnable = new Runnable() {

        //...
        public void run() {

                fl = fl+0.00005f;
            count++;
        //fl<=1.3499997f
if(count<=220) {
    chart.zoom(fl, 1f, 0, 0);

    Log.i("ZOOM leve set", "zoom level set "+fl);
}else{
    fl = 1;
    count = 1;
    continueToRun = false;
}
            // do your stuff here, like update
            // this block of code you going to reach every  second

            if(continueToRun == true){
                mHandler.postDelayed(mRunnable, mSampleDurationTime);
            }

        }

    };

    public void brokerbuildings(final Integer buildingsPage){
        Log.i("BROKER BUILDINGS CALLED","with page "+ buildingsPage);

        BrokerBuildings brokerBuildings = new BrokerBuildings();
        brokerBuildings.setDeviceId("Hardware");
        brokerBuildings.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        brokerBuildings.setPage(buildingsPage.toString());
        brokerBuildings.setLng(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
        brokerBuildings.setLat(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));

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

                    Log.i("BROKER BUILDINGS CALLED","success response "+response);

                    Log.i("BROKER BUILDINGS","LAT1 "+General.getSharedPreferences(getActivity(),AppConstants.MY_LAT));
                    Log.i("BROKER BUILDINGS","LNG1 "+General.getSharedPreferences(getActivity(),AppConstants.MY_LNG));
                    Log.i("BROKER BUILDINGS","LAT "+SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
                    Log.i("BROKER BUILDINGS","LNG "+SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));

                    JsonObject k = jsonElement.getAsJsonObject();
                    try {
                        JSONObject ne = new JSONObject(k.toString());
                        Log.i("BROKER BUILDINGS CALLED","success ne "+ne);
                        buildings = ne.getJSONObject("responseData").getJSONArray("buildings");
                        Log.i("BROKER BUILDINGS CALLED","buildings"+ne.getJSONObject("responseData"));

                        for (int i=0; i<buildings.length(); i++) {
                            JSONObject actor = buildings.getJSONObject(i);
                            String name = actor.getString("name");
                            // if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("RESALE"))



                            buildingPriceOR.add(actor.getInt("or_psf"));
                            buildingPriceLL.add(actor.getInt("ll_pm"));

                            Log.i("BROKER BUILDINGS CALLED", "buildingPriceOR" + buildingPriceOR);

                            Log.i("BROKER BUILDINGS CALLED", "buildingPriceLL" + buildingPriceLL);




                            // Log.i("BROKER BUILDINGS CALLED","success"+name+" "+price);


                            //  Log.i("BROKER BUILDINGS CALLED","success"+price.getClass().getName()+" "+buildingPrice.getClass().getName());

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







                        // Log.i("PREOK CALLED","buildings"+a);
                        Log.i("BROKER BUILDINGS CALLED","buildings"+buildings);
                        Log.i("BROKER BUILDINGS CALLED","buildingNames"+buildingNames);
                        Log.i("BROKER BUILDINGS CALLED","buildingPrice"+buildingPrice);

                        if(buildings.length()>0)
                        chart();

//                        jsonArrayReqLl = neighbours.getJSONArray("recent");;//neighbours.getJSONArray("req_ll");
//                        jsonArrayAvlLl = neighbours.getJSONArray("recent");//neighbours.getJSONArray("avl_ll");
//
//                        jsonArrayReqOr = neighbours.getJSONArray("recent");//neighbours.getJSONArray("req_or");
//                        jsonArrayAvlOr = neighbours.getJSONArray("recent");//neighbours.getJSONArray("avl_or");
//
//                        Log.i("PREOK CALLED","jsonArrayReqLl"+jsonArrayReqLl);
//                        Log.i("PREOK CALLED","jsonArrayAvlLl"+jsonArrayAvlLl);
//                        Log.i("PREOK CALLED","jsonArrayReqOr"+jsonArrayReqOr);
//                        Log.i("PREOK CALLED","jsonArrayAvlOr"+jsonArrayAvlOr);
//
//
//
//                        onPositionSelected(currentSeekbarPosition, currentCount);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        Log.i("BROKER BUILDINGS CALLED","Failed "+e.getMessage());
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


    /**
     * load preok data by making server API call
     */
    public void preok() {
        Log.i("TRACE","GCM id is"+SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        //preok params
        Oyeok preok = new Oyeok();
//        int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.WRITE_CALENDAR);
        //permissionCheckForDeviceId = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("PREOK","getcontext "+General.getDeviceId(getContext()));
            preok.setDeviceId(General.getDeviceId(getContext()));


        }else{
           // preok.setDeviceId(General.getSharedPreferences(this,AppConstants.));
            preok.setDeviceId(General.getSharedPreferences(getContext(),AppConstants.TIME_STAMP_IN_MILLI));
            Log.i("PREOK","getcontext "+General.getSharedPreferences(getContext(),AppConstants.TIME_STAMP_IN_MILLI));

        }

        preok.setUserRole("broker");
        preok.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        preok.setLong(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
        preok.setLat(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
        preok.setPlatform("android");
        Log.i("PREOK","user_id1 "+General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER));
        if(General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
            preok.setUserId("demo_id");

        }
        else {
            preok.setUserId(General.getSharedPreferences(getContext(), AppConstants.USER_ID));
            Log.i("PREOK","user_id "+General.getSharedPreferences(getContext(), AppConstants.USER_ID));
        }


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppConstants.SERVER_BASE_URL)
                .build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        try {
            oyeokApiService.preOk(preok, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    JsonObject k = jsonElement.getAsJsonObject();
                    try {
                        JSONObject ne = new JSONObject(k.toString());
                        JSONObject neighbours = ne.getJSONObject("responseData").getJSONObject("neighbours");
                        Log.i("PREOK CALLED","neighbours"+ne);
                        Log.i("PREOK CALLED","neighbours"+neighbours);

                        jsonArrayReqLl = neighbours.getJSONArray("req_ll");;//neighbours.getJSONArray("req_ll");
                        jsonArrayAvlLl = neighbours.getJSONArray("avl_ll");//neighbours.getJSONArray("avl_ll");

                        jsonArrayReqOr = neighbours.getJSONArray("req_or");//neighbours.getJSONArray("req_or");
                        jsonArrayAvlOr = neighbours.getJSONArray("recent");//neighbours.getJSONArray("avl_or");

                        Log.i("PREOK CALLED","jsonArrayReqLl"+jsonArrayReqLl);
                        Log.i("PREOK CALLED","jsonArrayAvlLl"+jsonArrayAvlLl);
                        Log.i("PREOK CALLED","jsonArrayReqOr"+jsonArrayReqOr);
                        Log.i("PREOK CALLED","jsonArrayAvlOr"+jsonArrayAvlOr);



                        // jsonArrayPreokRecent = neighbours.getJSONArray("recent");
                        //if all values are empty then show from resent
//                        if (jsonArrayReqLl.length() == 0 && jsonArrayAvlLl.length() == 0 &&
//                                jsonArrayReqOr.length() == 0 && jsonArrayAvlOr.length() == 0) {
//                            jsonArrayReqLl = jsonArrayPreokRecent;
//                            jsonArrayAvlLl = jsonArrayPreokRecent;
//                            jsonArrayReqOr = jsonArrayPreokRecent;
//                            jsonArrayAvlOr = jsonArrayPreokRecent;
//                        }


                        onPositionSelected(currentSeekbarPosition, currentCount);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
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

    @OnClick(R.id.okBtn)
    public void onOptionClickok(View v) {

        try {
            Log.i("CHART", "y value " + chart.getEntriesAtIndex(0).get(0).getVal());
            listings = new HashMap<String, Float>();
            listings.put(buildingNames.get(buildingsSelected.get(0)), chart.getEntriesAtIndex(0).get(0).getVal());
            listings.put(buildingNames.get(buildingsSelected.get(1)), chart.getEntriesAtIndex(1).get(0).getVal());
            listings.put(buildingNames.get(buildingsSelected.get(2)), chart.getEntriesAtIndex(2).get(0).getVal());
        }
        catch (Exception e) {

        }


        if (!General.getSharedPreferences(getActivity(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
            dbHelper.save(DatabaseConstants.userRole, "Broker");
            //show sign up screen if broker is not registered
            Bundle bundle = new Bundle();
            bundle.putString("lastFragment", "BrokerPreokFragment");
            bundle.putString("JsonArray", jsonObjectArray.toString());
            bundle.putInt("Position", selectedItemPosition);
            Log.i("listings","building1 "+buildingNames.get(buildingsSelected.get(0)));
            Log.i("listings","building2 "+buildingNames.get(buildingsSelected.get(1)));
            Log.i("listings","building3 "+buildingNames.get(buildingsSelected.get(2)));

            Log.i("listings","price1 "+chart.getEntriesAtIndex(0).get(0).getVal());
            Log.i("listings","price2 "+chart.getEntriesAtIndex(1).get(0).getVal());
            Log.i("listings","price3 "+chart.getEntriesAtIndex(2).get(0).getVal());

            String[] bNames = new String[]{buildingNames.get(buildingsSelected.get(0)),buildingNames.get(buildingsSelected.get(1)),buildingNames.get(buildingsSelected.get(2))};
            int[] bPrice = new int[]{Math.round(chart.getEntriesAtIndex(0).get(0).getVal()),Math.round(chart.getEntriesAtIndex(1).get(0).getVal()),Math.round(chart.getEntriesAtIndex(2).get(0).getVal())};


            bundle.putIntArray("bPrice",bPrice);
            bundle.putStringArray("bNames",bNames);


            bundle.putSerializable("listings", listings);
            Fragment fragment = null;
            fragment = new SignUpFragment();
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_sign, fragment);
           // fragmentTransaction.replace(R.id.container_map, fragment);
            fragmentTransaction.commit();
        } else {
            //here broker is registered




            AcceptOkCall a = new AcceptOkCall();
            a.setmCallBack(BrokerPreokFragment.this);
            a.acceptOk(listings, jsonObjectArray, selectedItemPosition, dbHelper, getActivity());

            General.setBadgeCount(getContext(),AppConstants.RENTAL_COUNT,0);
            General.setBadgeCount(getContext(),AppConstants.RESALE_COUNT,0);
            General.setBadgeCount(getContext(),AppConstants.TENANTS_COUNT,0);
            General.setBadgeCount(getContext(),AppConstants.OWNERS_COUNT,0);
            General.setBadgeCount(getContext(),AppConstants.BUYER_COUNT,0);
            General.setBadgeCount(getContext(),AppConstants.SELLER_COUNT,0);


        }



//        buildingSlider.startAnimation(slide_down);
//        buildingSlider.setVisibility(View.GONE);



//        if (buildingSlider.getVisibility() == View.VISIBLE) {
//            TranslateAnimation animate = new TranslateAnimation(0, 0, buildingSlider.getHeight(), 0);
//            animate.setDuration(500);
//            animate.setFillAfter(true);
//            buildingSlider.startAnimation(animate);
//            buildingSlider.setVisibility(View.GONE);
//        }

    }



    @OnClick({R.id.selectB, R.id.setB})
    public void onOptionClickB(View v) {
        if (txtPreviouslySelectedOptionB != null)
            txtPreviouslySelectedOptionB.setBackgroundResource(R.color.colorPrimaryDark);

        txtPreviouslySelectedOptionB = (TextView) v;
        if (v.getId() == selectB.getId()) {
            selectB.setBackgroundResource(R.color.greenish_blue);
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
            selectB.setText("Select buildings ["+buildingsSelected.size()+"]");
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
                setB.setBackgroundResource(R.color.greenish_blue);
                selectB.setBackgroundResource(R.color.colorPrimaryDark);

                okBtn.setEnabled(true);
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

        //okButton.setAnimation(zoomin);
        //okButton.setAnimation(zoomout);

        if (txtPreviouslySelectedOption != null)
            txtPreviouslySelectedOption.setBackgroundResource(R.color.colorPrimaryDark);

        txtPreviouslySelectedOption = (TextView) v;

        if (v.getId() == txtOption1.getId()) {
            rentText.setVisibility(View.GONE);
           // texPtype.setVisibility(View.GONE);
            texPtype.setText("Please select a Lead and press OK.");

            texPstype.setVisibility(View.GONE);
//            option2CountCont2.setVisibility(View.GONE);
            //    option2Count.setVisibility(View.GONE);
            txtOption1.setBackgroundResource(R.color.greenish_blue);
            currentOptionSelectedString = txtOption1.getText().toString();
            Log.i("PREOK CALLED11","currentOptionSelectedString"+currentOptionSelectedString);

            // update circular seekbar

            if (currentOptionSelectedString.equalsIgnoreCase(strSeekers))
                currentOptionSelectedString = strTenants;
            Log.i("PREOK CALLED1","currentOptionSelectedString1 phase"+currentOptionSelectedString);
            if (jsonArrayReqLl != null && currentOptionSelectedString.equalsIgnoreCase(strTenants)) {

                lookingSeeking = "Tenant is looking for";
                Log.i("PREOK CALLED","values set phase"+jsonArrayReqLl.toString());
                circularSeekbar.setValues(jsonArrayReqLl.toString());
               // onclick(,null,null,-1);
                circularSeekbar.onTabclick();
            }
            else if (jsonArrayAvlLl != null && currentOptionSelectedString.equalsIgnoreCase(strOwners)) {
                lookingSeeking = "Owner is having";
                Log.i("PREOK CALLED", "values set phase" + jsonArrayAvlLl.toString());

                circularSeekbar.setValues(jsonArrayAvlLl.toString());
                circularSeekbar.onTabclick();
            }

        }
        else if (v.getId() == txtOption2.getId()) {

            //           option2CountCont1.setVisibility(View.GONE);
            //  option1Count.setVisibility(View.GONE);
            txtOption2.setBackgroundResource(R.color.greenish_blue);
            currentOptionSelectedString = txtOption2.getText().toString();
            Log.i("PREOK CALLED1","currentOptionSelectedString"+currentOptionSelectedString);
            rentText.setVisibility(View.GONE);
            //displayOkText.setVisibility(View.GONE);
           // texPtype.setVisibility(View.GONE);
            texPtype.setText("Please select a Lead and press OK.");
            texPstype.setVisibility(View.GONE);
            // update circular seekbar

            if (currentOptionSelectedString.equalsIgnoreCase(strTenants))
                currentOptionSelectedString = strSeekers;
            Log.i("PREOK CALLED1","currentOptionSelectedString2 phase"+currentOptionSelectedString);

            if (jsonArrayReqOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeekers)) {

                lookingSeeking = "Owner is having";
                Log.i("PREOK CALLED", "values set phase" + jsonArrayReqOr.toString());

                circularSeekbar.setValues(jsonArrayReqOr.toString());
                circularSeekbar.onTabclick();
            }
            else if (jsonArrayAvlOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeller)) {
                Log.i("PREOK CALLED1", "values set phase" + jsonArrayAvlOr.toString());
                Log.i("tester","4"+currentOptionSelectedString);
                circularSeekbar.setValues(jsonArrayAvlOr.toString());
                circularSeekbar.onTabclick();
            }

        }

        onPositionSelected(currentSeekbarPosition, currentCount);
    }

    @Override
    public void onPositionSelected(int position, int count) {




        currentSeekbarPosition = position;
        Log.i("PREOK CALLED","currentSeekbarPosition"+currentSeekbarPosition);

        if (position == 0) {
            atFor = "at";
            jsonObjectArray = null;
            notClicked.setVisibility(View.VISIBLE);

            rentText.setVisibility(View.GONE);
           // texPtype.setVisibility(View.GONE);
            texPtype.setText("Please select a Lead and press OK.");
            texPstype.setVisibility(View.GONE);

            resaleCount.setVisibility(View.GONE);


            Log.i("CONTEXT","object "+getContext());
            if(General.getBadgeCount(getContext(),AppConstants.RENTAL_COUNT)<=0){
                rentalCount.setVisibility(View.GONE);
                option1Count.setVisibility(View.GONE);
                option2Count.setVisibility(View.GONE);
            }
            else {
                rentalCount.setVisibility(View.VISIBLE);
                rentalCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT)));

                if(General.getBadgeCount(getContext(),AppConstants.TENANTS_COUNT)<=0) {
                    //option1Count.setVisibility(View.GONE);
                    Log.i(TAG,"itha "+AppConstants.TENANTS_COUNT);
                    option1Count.setVisibility(View.GONE);
                }
                else {
                    //option1Count.setVisibility(View.VISIBLE);
                    option1Count.setVisibility(View.VISIBLE);
                    option1Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.TENANTS_COUNT)));
                }
                if(General.getBadgeCount(getContext(),AppConstants.OWNERS_COUNT)<=0) {
                    Log.i(TAG,"ownerscount1"+General.getBadgeCount(getContext(),AppConstants.OWNERS_COUNT));
                    //option2Count.setVisibility(View.GONE);
                    option2Count.setVisibility(View.GONE);

                }
                else {
                    Log.i(TAG,"ownerscount2"+General.getBadgeCount(getContext(),AppConstants.OWNERS_COUNT));
                    //option2Count.setVisibility(View.VISIBLE);
                    option2Count.setVisibility(View.VISIBLE);
                    option2Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.OWNERS_COUNT)));
                }
            }
            if(General.getBadgeCount(getContext(),AppConstants.RESALE_COUNT)>0){
                resaleCount.setVisibility(View.VISIBLE);
                resaleCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RESALE_COUNT)));
            }


            animatebadges();



            //rental
            //Tenants, Owners
            txtOption1.setText(strTenants);
            txtOption2.setText(strOwners);
            Log.i("PREOK CALLED13#", "currentOptionSelectedString2" + currentOptionSelectedString);

            if (currentOptionSelectedString.equalsIgnoreCase(strSeekers)) {
                currentOptionSelectedString = strTenants;
                Log.i("PREOK CALLED10", "currentOptionSelectedString1" + currentOptionSelectedString);
            }
            if (currentOptionSelectedString.equalsIgnoreCase(strSeller)) {
                currentOptionSelectedString = strOwners;
                Log.i("PREOK CALLED13", "currentOptionSelectedString1" + currentOptionSelectedString);
            }

            if (jsonArrayReqLl != null && currentOptionSelectedString.equalsIgnoreCase(strTenants)) {
                Log.i("PREOK CALLED11","values set"+jsonArrayReqLl.toString());
                circularSeekbar.setValues(jsonArrayReqLl.toString());
            }
            else if (jsonArrayAvlLl != null && currentOptionSelectedString.equalsIgnoreCase(strOwners)) {
                Log.i("PREOK CALLED12", "values set" + jsonArrayAvlLl.toString());
                circularSeekbar.setValues(jsonArrayAvlLl.toString());

            }

            //added

//            if (currentOptionSelectedString.equalsIgnoreCase(strSeller)) {
//                currentOptionSelectedString = strOwners;
//                Log.i("PREOK CALLED13", "currentOptionSelectedString1" + currentOptionSelectedString);
//            }
//            if (jsonArrayAvlLl != null && currentOptionSelectedString.equalsIgnoreCase(strOwners)) {
//                Log.i("PREOK CALLED14","values set"+jsonArrayAvlLl.toString());
//                circularSeekbar.setValues(jsonArrayReqLl.toString());
//            }
//            else if (jsonArrayReqLl != null && currentOptionSelectedString.equalsIgnoreCase(strTenants)) {
//                Log.i("PREOK CALLED15", "values set" + jsonArrayReqLl.toString());
//                circularSeekbar.setValues(jsonArrayAvlLl.toString());
//            }
//            buildingPrice.clear();
//            buildingPrice.addAll(buildingPriceLL);
//            setChart();

        }
        else if (position == 1) {
            atFor = "for";
            jsonObjectArray = null;
            notClicked.setVisibility(View.VISIBLE);

            rentText.setVisibility(View.GONE);
           // texPtype.setVisibility(View.GONE);
            texPtype.setText("Please select a Lead and press OK.");
            texPstype.setVisibility(View.GONE);

            rentalCount.setVisibility(View.GONE);
            option1Count.setVisibility(View.INVISIBLE);
            option2Count.setVisibility(View.INVISIBLE);
            
            if(General.getBadgeCount(getContext(),AppConstants.RESALE_COUNT)<=0) {
                resaleCount.setVisibility(View.GONE);
                option1Count.setVisibility(View.GONE);
                option2Count.setVisibility(View.GONE);
            }
            else {
                resaleCount.setVisibility(View.VISIBLE);
                resaleCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RESALE_COUNT)));

                if(General.getBadgeCount(getContext(),AppConstants.BUYER_COUNT)<=0) {
                    Log.i("BADGE","BUYER COUNT5 "+General.getBadgeCount(getContext(),AppConstants.BUYER_COUNT));
                    //option1Count.setVisibility(View.GONE);
                    option1Count.setVisibility(View.GONE);
                }
                else {
                    //option1Count.setVisibility(View.VISIBLE);
                    option1Count.setVisibility(View.VISIBLE);
                    option1Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.BUYER_COUNT)));
                }
                if(General.getBadgeCount(getContext(),AppConstants.SELLER_COUNT)<=0) {
                    Log.i("BADGE","SELLER_COUNT1 "+General.getBadgeCount(getContext(),AppConstants.SELLER_COUNT));
                    //option2Count.setVisibility(View.GONE);
                    option2Count.setVisibility(View.GONE);
                }
                else {
                    Log.i("BADGE","SELLER_COUNT2 "+General.getBadgeCount(getContext(),AppConstants.SELLER_COUNT));
                    //option2Count.setVisibility(View.VISIBLE);
                    option2Count.setVisibility(View.VISIBLE);
                    option2Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT)));
                }

            }
            if(General.getBadgeCount(getContext(),AppConstants.RENTAL_COUNT)>0){
                rentalCount.setVisibility(View.VISIBLE);
                rentalCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT)));
            }


            animatebadges();

            //sale
            //Buyer, Seller
            txtOption1.setText(strSeekers);
            txtOption2.setText(strSeller);

            Log.i("PREOK CALLED13*", "currentOptionSelectedString2" + currentOptionSelectedString);

            if (currentOptionSelectedString.equalsIgnoreCase(strTenants)) {
                currentOptionSelectedString = strSeekers;
                Log.i("PREOK CALLED16", "currentOptionSelectedString2" + currentOptionSelectedString);
            }
            if (currentOptionSelectedString.equalsIgnoreCase(strOwners)) {
                currentOptionSelectedString = strSeller;
                Log.i("PREOK CALLED19", "currentOptionSelectedString2" + currentOptionSelectedString);
            }

            if (jsonArrayReqOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeekers)) {
                Log.i("PREOK CALLED17", "values set" + jsonArrayReqOr.toString());
                circularSeekbar.setValues(jsonArrayReqOr.toString());
            }
            else if (jsonArrayAvlOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeller)) {
                Log.i("PREOK CALLED18", "values set" + jsonArrayAvlOr.toString());
                circularSeekbar.setValues(jsonArrayAvlOr.toString());
            }

            // Added

//            if (currentOptionSelectedString.equalsIgnoreCase(strOwners)) {
//                currentOptionSelectedString = strSeller;
//                Log.i("PREOK CALLED19", "currentOptionSelectedString2" + currentOptionSelectedString);
//            }
//
//            if (jsonArrayAvlOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeller)) {
//                Log.i("PREOK CALLED120", "values set" + jsonArrayAvlOr.toString());
//                circularSeekbar.setValues(jsonArrayReqOr.toString());
//            }
//            else if (jsonArrayReqOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeekers)) {
//                Log.i("PREOK CALLED121", "values set" + jsonArrayReqOr.toString());
//                circularSeekbar.setValues(jsonArrayAvlOr.toString());
//            }

            try {
                buildingPrice.clear();
                buildingPrice.addAll(ORbuildingPrice);
                setChart();
            }catch(Exception e){

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
    }

    @Override
    public void onclick(int position, JSONArray m, String show, int x_c, int y_c) {
        deal.setEnabled(true);
        deal.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        try {
            leadPrompt.setVisibility(View.VISIBLE);
            leadPrompt.setText("Please select a Lead and press OK");
            jsonObjectArray = m;
            selectedItemPosition = position;
            String ptype = null;
            String pstype;
            pstype = jsonObjectArray.getJSONObject(position).getString("property_subtype");
            Log.i("debug circ","inside onclick");
            Log.i("debug circ","inside onclick m "+jsonObjectArray);




          /*  if(pstype.equals("1bhk") || pstype.equals("2bhk") || pstype.equals("3bhk") || pstype.equals("4bhk") || pstype.equals("4+bhk")){
                ptype = "home";
            }
            else if(pstype.equals("retail outlet") || pstype.equals("food outlet") || pstype.equals("shop")){
                ptype = "shop";
            }
            else if(pstype.equals("cold storage") || pstype.equals("kitchen") || pstype.equals("manufacturing") || pstype.equals("warehouse") || pstype.equals("workshop")){
                ptype = "industrial";
            }
            else if(pstype.equals("<15") || pstype.equals("<35") || pstype.equals("<50") || pstype.equals("<100") || pstype.equals("100+")){
                ptype = "office";
            }
            */

            ptype = jsonObjectArray.getJSONObject(position).getString("property_type");

            Log.i(TAG,"property_type "+ptype);
            Log.i(TAG, "property_subtype " + pstype);
            texPtype.setText("Property type: "+ptype);
            texPstype.setText("Property subtype: "+pstype);

       /*     texPtype.setText("Property Type: "+ptype);
            texPstype.setText("Property Subtype: "+pstype);
            */
            //texPstype.setText("Property Subtype: "+jsonObjectArray.getJSONObject(position).getString("property_subtype."));
            if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("RENTAL"))
                rentText.setText(General.currencyFormat(jsonObjectArray.getJSONObject(position).getString("price"))+" /m.");
            else
                rentText.setText(General.currencyFormat(jsonObjectArray.getJSONObject(position).getString("price")));
            //  rentText.setText("Rs "+jsonObjectArray.getJSONObject(position).getString("price")+" /m.");
            //      displayOkText.setText(jsonObjectArray.getJSONObject(position).getString("ok_price")+" Oks will be used.");

            Log.i(TAG, "show is " + show);

            if(show.equals("show")) {

                notClicked.setVisibility(View.GONE);
                leadPrompt.setVisibility(View.GONE);
                rentText.setVisibility(View.VISIBLE);
                //   displayOkText.setVisibility(View.VISIBLE);
                texPtype.setVisibility(View.VISIBLE);
                texPstype.setVisibility(View.VISIBLE);

                deal.setEnabled(false);
                deal.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey));
                // pickContact.setVisibility(View.GONE);
                // contactText.setVisibility(View.GONE);
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
                    selectB.setText("Select buildings ["+buildingsSelected.size()+"]");
                    selectB.performClick();
                }
                notClicked.setVisibility(View.VISIBLE);
                rentText.setVisibility(View.GONE);
                //   displayOkText.setVisibility(View.GONE);
                texPtype.setVisibility(View.GONE);
                texPstype.setVisibility(View.GONE);
                // pickContact.setVisibility(View.GONE);
                // contactText.setVisibility(View.GONE);
            }
            else {
                notClicked.setVisibility(View.GONE);
                rentText.setVisibility(View.VISIBLE);
                //   displayOkText.setVisibility(View.VISIBLE);
                texPtype.setVisibility(View.VISIBLE);
                texPstype.setVisibility(View.VISIBLE);
                //  pickContact.setVisibility(View.VISIBLE);
                //  contactText.setVisibility(View.VISIBLE);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //   @OnClick({R.id.okButton, R.id.deal, R.id.pickContact})
    @OnClick({R.id.okButton, R.id.deal})
    public void onButtonsClick(View v) {
        if (okButton.getId() == v.getId()) {

            Log.i("GRAPH","jsonObjectArray is "+jsonObjectArray);


            if (jsonObjectArray == null) {


                SnackbarManager.show(
                        com.nispok.snackbar.Snackbar.with(getActivity())
                                .position(Snackbar.SnackbarPosition.BOTTOM)
                                .text("Please select a Lead and then press OK.")
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
//                if (buildingSlider.getVisibility() == View.GONE) {
//                    TranslateAnimation animate = new TranslateAnimation(0, 0, 0 , buildingSlider.getHeight());
//                    animate.setDuration(2000);
//                    animate.setFillAfter(true);
//                    buildingSlider.startAnimation(animate);
//                    buildingSlider.setVisibility(View.VISIBLE);
//                }



            }
            else {
                //show buildings


                buildingSlider.startAnimation(slide_up);
                buildingSlider.setVisibility(View.VISIBLE);
                buildingSliderflag = true;
                float fr = buildingNames.size() * 0.33f;
               //chart.fitScreen();
               chart.zoomAndCenterAnimated(fr,1f,0,0, YAxis.AxisDependency.LEFT ,2000);
                Log.i("brokerpreok","buildingSliderflag "+buildingSliderflag);
                Intent intent = new Intent(AppConstants.BUILDINGSLIDERFLAG);
                intent.putExtra("buildingSliderFlag",buildingSliderflag);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                okBtn.setEnabled(false);
                okBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey));
                okBtn.setText("OK");

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
        }
        else if (deal.getId() == v.getId()) {
            if (General.getBadgeCount(getContext(), AppConstants.HDROOMS_COUNT) > 0) {
                General.setBadgeCount(getContext(), AppConstants.HDROOMS_COUNT,0);
                hdroomsCount.setVisibility(View.GONE);
            }

            //open deals listing
            Intent openDealsListing = new Intent(getActivity(), BrokerDealsListActivity.class);
            startActivity(openDealsListing);
        }
//        else if (pickContact.getId() == v.getId()) {
//            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//            getActivity().startActivityFromFragment(this, intent, REQUEST_CODE_TO_SELECT_CLIENT);
//        }
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
//        if(General.getBadgeCount(getContext(),AppConstants.RENTAL_COUNT)>0) {
//            rentalCount.startAnimation(bounce);
//if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("resale")) {
//    if (General.getBadgeCount(getContext(), AppConstants.TENANTS_COUNT) > 0) {
//        option1Count.startAnimation(bounce);
//    }
//    if (General.getBadgeCount(getContext(), AppConstants.OWNERS_COUNT) > 0) {
//        option2Count.startAnimation(bounce);
//    }
//}
//        }
//        if(General.getBadgeCount(getContext(),AppConstants.RESALE_COUNT)>0) {
//            resaleCount.startAnimation(bounce);
//
//            if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("rental")) {
//                if (General.getBadgeCount(getContext(), AppConstants.BUYER_COUNT) > 0) {
//                    option1Count.startAnimation(bounce);
//                }
//                if (General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT) > 0) {
//                    option2Count.startAnimation(bounce);
//                }
//            }
//
//
//
//        }


    }

    private void setBadges(){
        if(rentalCount1 == 0){
            Log.i("gcm local broadcast","gcm local broadcast5");
            rentalCount.setVisibility(View.GONE);
//            option1Count.setVisibility(View.GONE);
//            option2Count.setVisibility(View.GONE);

        }
     else {
            Log.i("gcm local broadcast","gcm local broadcast3");
        rentalCount.setVisibility(View.VISIBLE);
        rentalCount.setText(String.valueOf(rentalCount1));

            if(tenantsCount1 == 0){
                option1Count.setVisibility(View.GONE);
            }
            else {
                Log.i("gcm local broadcast","gcm local broadcast4 " +tenantsCount1);
                option1Count.setVisibility(View.VISIBLE);
                option1Count.setText(String.valueOf(tenantsCount1));
            }

            if(ownersCount1 == 0){
                option2Count.setVisibility(View.GONE);
            }
            else {
                Log.i(TAG, "OnSharedPreferenceChangeListener 3");
                option2Count.setVisibility(View.VISIBLE);
                option2Count.setText(String.valueOf(ownersCount1));
            }

         }

        if(resaleCount1 == 0){
            resaleCount.setVisibility(View.GONE);
//            option1Count.setVisibility(View.GONE);
//            option2Count.setVisibility(View.GONE);

        }
        else {
            Log.i(TAG, "OnSharedPreferenceChangeListener 3");
            resaleCount.setVisibility(View.VISIBLE);
            resaleCount.setText(String.valueOf(resaleCount1));

//            if(buyerCount1 == 0){
//                option1Count.setVisibility(View.GONE);
//            }
//            else {
//                Log.i(TAG, "OnSharedPreferenceChangeListener 3");
//                option1Count.setVisibility(View.VISIBLE);
//                option1Count.setText(String.valueOf(buyerCount1));
//            }
//
//            if(sellerCount1 == 0){
//                option2Count.setVisibility(View.GONE);
//            }
//            else {
//                Log.i(TAG, "OnSharedPreferenceChangeListener 3");
//                option2Count.setVisibility(View.VISIBLE);
//                option2Count.setText(String.valueOf(sellerCount1));
//            }
        }


    }





//    public void priceChart(){
//
//
//        setB.setClickable(false);
//        Log.i("GRAPH","scale before set chart2 "+ chart.getScaleX());
//        pricechart = true;
//        entries = new ArrayList<>();

//        entries.add(new BarEntry(buildingPrice.get(buildingsSelected.get(0)), 0));
//        entries.add(new BarEntry(buildingPrice.get(buildingsSelected.get(1)), 1));
//        entries.add(new BarEntry(buildingPrice.get(buildingsSelected.get(2)), 2));
//
//
//
//        buildingsSelected.clear();
//        dataset = new BarDataSet(entries, "3");
//
//        // creating labels
//        labels = new ArrayList<String>();
//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        BarData data = new BarData(labels, dataset);
//        chart.setData(data);
//        chart.notifyDataSetChanged();
//        chart.invalidate();
//
//
//        // highlight all three buildings
//        Highlight h0 = new Highlight(0, 0);
//        Highlight h1 = new Highlight(1, 0);
//        Highlight h2 = new Highlight(2, 0);
//        chart.highlightValues(new Highlight[]{h0, h1, h2});
//
//
//        chart.setDescription("Set price for buildings.");
//
//        chart.animateY(2000);
//
//
//        chart.setScaleYEnabled(false);
//        //chart.setScaleEnabled(false);
//
//        chart.fitScreen();
//        chart.zoom(1.001f, 1f, 0, 0);
//        Log.i("GRAPH","scale after set chart2 "+ chart.getScaleX());
//
//        chart.setDragEnabled(true);
//
//        //   chart.setPinchZoom(false);
//        chart.setDoubleTapToZoomEnabled(false);
//        chart.setTouchEnabled(true);
//
//
//        // chart1.setOnChartValueSelectedListener(this);
//        chart.setOnChartGestureListener(this);
//
//
//    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        Log.i("GRAPH","on value selected");


//        Log.i("GRAPH", "h is " + h);
//
//        highlights = new ArrayList<Highlight>(Arrays.asList(highs));
//
//
//            highs = chart.getHighlighted();
//
//
//
//
//
//        Log.i("GRAPH", "highs " + highs);
//
//        highlights = new ArrayList<Highlight>(Arrays.asList(highs));

//
//        Log.i("GRAPH", "highlights " + highlights);
//
//        if(highlights.contains(h)){
//            highlights.remove(h);
//            Log.i("GRAPH", "h removed " + h);
//            highsa = highlights.toArray(new Highlight[highlights.size()]);
//            Log.i("GRAPH", "graph drawn with " + highsa);
//              chart.highlightValues(highsa);
//        } else{
//            highlights.add(h);
//            Log.i("GRAPH", "h added " + h);
//            highsa = highlights.toArray(new Highlight[highlights.size()]);
//            Log.i("GRAPH", "graph drawn with " + highsa);
//            chart.highlightValues(highsa);
//        }


//        Log.i("GRAPH", "Start " + highlights.size() + "list " + highlights + "h is " + h);
//        Log.i("GRAPH", "buildings selected " + buildingsSelected);
//
//
//        if (!buildingsSelected.contains(h.getXIndex())) {
//            Log.i("GRAPH", "1 " + highlights.size());
//            if (highlights.size() < 3) {
//                Log.i("GRAPH", "2");
//                highlights.add(h);
//                buildingsSelected.add(h.getXIndex());
//                Log.i("GRAPH2", "buildings selected " + buildingsSelected);
//                Log.i("GRAPH", "added " + h);
//                highs = highlights.toArray(new Highlight[highlights.size()]);
//                chart.highlightValues(highs);
//            } else if (highlights.size() == 3) {
//                Log.i("GRAPH", "3 " + highlights.size());
//                //can select only three buildings
//
//                chart.highlightValues(highs);
//            }
//
//        } else {
//            Log.i("GRAPH", "4 " + highlights.size() + "list " + highlights + "h is " +h);
//
//            for (Iterator<Highlight> it = highlights.iterator(); it.hasNext(); ) {
//
//                Highlight hi = it.next();
//                Log.i("GRAPH", "hi is " +hi);
//                if (Integer.valueOf(hi.getXIndex()).equals(Integer.valueOf(h.getXIndex()))) {
//                    it.remove();
//                    //highlights.remove(Integer.valueOf(h.getXIndex()));
//                    Log.i("GRAPH", "Removed " + hi.getXIndex() + "ved  " + h.getXIndex());
//                    buildingsSelected.remove(Integer.valueOf(h.getXIndex()));
//                }
//
//                //  buildingsSelected.remove(Integer.valueOf(h.getXIndex()));
//                Log.i("GRAPH", "removed " + h);
//                Log.i("GRAPH4", "buildings selected " + buildingsSelected);
//                highs = highlights.toArray(new Highlight[highlights.size()]);
//                chart.highlightValues(highs);
//            }
//
//
//        }


//        if (!highlights.contains(h)){
//            Log.i("GRAPH","1 "+highlights.size());
//            if(highlights.size()<3){
//                Log.i("GRAPH","2");
//                highlights.add(h);
//                Log.i("GRAPH","added "+h);
//                highs = highlights.toArray(new Highlight[highlights.size()]);
//                chart.highlightValues(highs);
//            }
//            else if(highlights.size()==3){
//                Log.i("GRAPH","3 "+highlights.size());
//                //can select only three buildings
//
//                chart.highlightValues(highs);
//            }
//
//        }
//        else{Log.i("GRAPH","4 "+highlights.size()+"list "+highlights +"h is "+h);
//
//
//             highlights.remove(h);
//            Log.i("GRAPH","removed "+h);
//            highs = highlights.toArray(new Highlight[highlights.size()]);
//            chart.highlightValues(highs);
//        }

        if(!pricechart) {
            // make highlights null as we are going to rehighlight altogether
            chart.highlightValues(null);
            Log.i("GRAPH", "executed ");
            Log.i("GRAPH10", "buildings selected and new build " + buildingsSelected + " " + e.getXIndex());

            if (!buildingsSelected.contains(e.getXIndex())) {
                Log.i("GRAPH", "1");


                if (buildingsSelected.size() < 3) {
                    buildingsSelected.add(e.getXIndex());
                    selectB.setText("Select buildings ["+buildingsSelected.size()+"]");

                    Log.i("GRAPH10", "buildings selected after add " + buildingsSelected);

                    // for (int i = 0; i < buildingsSelected.size(); i++) {

                    if (buildingsSelected.size() == 1) {
                        Log.i("GRAPH10", "buildings selected " + buildingsSelected);
                        Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                        chart.highlightValues(new Highlight[]{h0});


                    } else if (buildingsSelected.size() == 2) {
                        Log.i("GRAPH11", "buildings selected " + buildingsSelected);
                        Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                        Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
                        chart.highlightValues(new Highlight[]{h0, h1});

                    } else {
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
                selectB.setText("Select buildings ["+buildingsSelected.size()+"]");
                Log.i("GRAPH", "after removing " + buildingsSelected);
                //    for (int i = 0; i < buildingsSelected.size(); i++) {

                Log.i("GRAPH", "size " + buildingsSelected.size());
                if (buildingsSelected.size() == 1) {
                    Log.i("GRAPH20", "buildings selected " + buildingsSelected);
                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                    chart.highlightValues(new Highlight[]{h0});

                } else if (buildingsSelected.size() == 2) {
                    Log.i("GRAPH21", "buildings selected " + buildingsSelected);
                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                    Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
                    chart.highlightValues(new Highlight[]{h0, h1});


                } else if (buildingsSelected.size() == 0) {
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
                selectB.setText("Select buildings ["+buildingsSelected.size()+"]");
                Log.i("GRAPH", "after removing " + buildingsSelected);
                //    for (int i = 0; i < buildingsSelected.size(); i++) {

                Log.i("GRAPH", "size " + buildingsSelected.size());
                if (buildingsSelected.size() == 1) {
                    Log.i("GRAPH20", "buildings selected " + buildingsSelected);
                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                    chart.highlightValues(new Highlight[]{h0});

                } else if (buildingsSelected.size() == 2) {
                    Log.i("GRAPH21", "buildings selected " + buildingsSelected);
                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                    Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
                    chart.highlightValues(new Highlight[]{h0, h1});


                } else if (buildingsSelected.size() == 0) {
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
//                        float fr = buildingNames.size() * 0.33f;
//                        chart.fitScreen();
                        //chart.zoom(9.3f,1f,0,0);
                       // chart.zoomAndCenterAnimated(6.6f,1f,0,0, YAxis.AxisDependency.LEFT ,2000);
                        brokerbuildings(buildingsPage);
                    }
                }
            }
            catch(Exception e){

            }
        }

    //    Log.i("GRAPH","onChartTranslate "+me);
        // Log.i("GRAPH","onChartTranslate "+me.getAction() +" "+me.getAxisValue(MotionEvent.AXIS_Y));

        // highlight = highlighter.getHighlight(me.getX(),me.getY());
        // e = dataset.getEntryForIndex(highlight.getXIndex());

//        if(pricechart) {
//            e = chart.getEntryByTouchPoint(me.getX(), me.getY());
//            e.setVal(chart.getYValueByTouchPoint(me.getX(), me.getY(), YAxis.AxisDependency.LEFT));
//
//            chart.notifyDataSetChanged();
//            chart.invalidate();
////            Log.i("GRAPH", "once onChartTranslate again " + me);
//        }

        // Log.i("GRAPH","onChartTranslate1 "+me.getX()+" "+me.getY()+" "+dX+" "+dY+" ");




    }





    public void onTabclick(int position, JSONArray m, String show, int x_c, int y_c) {
        try {
            jsonObjectArray = m;
            selectedItemPosition = position;
            String ptype = null;
            String pstype;
            pstype = jsonObjectArray.getJSONObject(position).getString("property_subtype");
            Log.i("debug circ","inside onclick");
            Log.i("debug circ","inside onclick m "+jsonObjectArray);


          /*  if(pstype.equals("1bhk") || pstype.equals("2bhk") || pstype.equals("3bhk") || pstype.equals("4bhk") || pstype.equals("4+bhk")){
                ptype = "home";
            }
            else if(pstype.equals("retail outlet") || pstype.equals("food outlet") || pstype.equals("shop")){
                ptype = "shop";
            }
            else if(pstype.equals("cold storage") || pstype.equals("kitchen") || pstype.equals("manufacturing") || pstype.equals("warehouse") || pstype.equals("workshop")){
                ptype = "industrial";
            }
            else if(pstype.equals("<15") || pstype.equals("<35") || pstype.equals("<50") || pstype.equals("<100") || pstype.equals("100+")){
                ptype = "office";
            }
            */

            ptype = jsonObjectArray.getJSONObject(position).getString("property_type");

            Log.i(TAG,"property_type "+ptype);
            Log.i(TAG, "property_subtype " + pstype);

            texPtype.setText("Property Type: "+ptype);
            texPstype.setText("Property Subtype: "+pstype);
            //texPstype.setText("Property Subtype: "+jsonObjectArray.getJSONObject(position).getString("property_subtype."));
            if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("RENTAL"))
                rentText.setText(General.currencyFormat(jsonObjectArray.getJSONObject(position).getString("price"))+" /m.");
            else
                rentText.setText(General.currencyFormat(jsonObjectArray.getJSONObject(position).getString("price")));
            //  rentText.setText("Rs "+jsonObjectArray.getJSONObject(position).getString("price")+" /m.");
            //      displayOkText.setText(jsonObjectArray.getJSONObject(position).getString("ok_price")+" Oks will be used.");

            Log.i(TAG, "show is " + show);

            if(show.equals("show")) {
                notClicked.setVisibility(View.GONE);
                rentText.setVisibility(View.VISIBLE);
                //   displayOkText.setVisibility(View.VISIBLE);
                texPtype.setVisibility(View.VISIBLE);
                texPstype.setVisibility(View.VISIBLE);

                // pickContact.setVisibility(View.GONE);
                // contactText.setVisibility(View.GONE);
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
                    selectB.setText("Select buildings ["+buildingsSelected.size()+"]");
                    selectB.performClick();
                }
                notClicked.setVisibility(View.VISIBLE);
                rentText.setVisibility(View.GONE);
                //   displayOkText.setVisibility(View.GONE);
                texPtype.setVisibility(View.GONE);
                texPstype.setVisibility(View.GONE);
                // pickContact.setVisibility(View.GONE);
                // contactText.setVisibility(View.GONE);
            }
            else {
                notClicked.setVisibility(View.GONE);
                rentText.setVisibility(View.VISIBLE);
                //   displayOkText.setVisibility(View.VISIBLE);
                texPtype.setVisibility(View.VISIBLE);
                texPstype.setVisibility(View.VISIBLE);
                //  pickContact.setVisibility(View.VISIBLE);
                //  contactText.setVisibility(View.VISIBLE);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }






}
