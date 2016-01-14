package com.nbourses.oyeok.RPOT.OkBroker.UI;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.Firebase.DroomChatFirebase;
import com.nbourses.oyeok.Firebase.HourGlassDetails;
import com.nbourses.oyeok.Firebase.HourGlassFirebase;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.AcceptOkCall;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI.Droom_Chat_New;
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI.Droom_chats_list;
import com.nbourses.oyeok.RPOT.OkBroker.UI.SlidingTabLayout.PagerItem;
import com.nbourses.oyeok.RPOT.OkBroker.UI.SlidingTabLayout.SlidingTabLayout;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.MapWrapperLayout;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.SignUp.SignUpFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Ok_Broker_MainScreen extends Fragment implements MainActivity.openMapsClicked,CustomPhasedListener {

    private static final String TAG = Ok_Broker_MainScreen.class.getSimpleName();
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private LinearLayout mHideShow;
    private LinearLayout popup;
    private FrameLayout mMapView;
    private boolean mFirst = false;
    private CustomMapFragment customMapFragment;
    private GoogleMap map;
    public int noOfOkRequired=0;
    private MyPagerAdapter adapter;
    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    protected float DPTOPX_SCALE;// = getResources().getDisplayMetrics().density;

    private CustomPhasedSeekBar mCustomPhasedSeekbar;
    private int currentItem,currentCount;
    private Button earnOk;
    private int totalTime=100,currentTime=0;
    private ImageButton bPinLocation;
    private LatLng latlng;
    DBHelper dbHelper;
    int intervalCount=0;
    HourGlassDetails hourGlassDetails;
    int leftHourGlasses=2;
    HourGlassFirebase hourGlassFirebase;
    String coolOffString="";
    int filledHourGlass=5;
    int percentage=0;
    int coolOff=0;
    DroomChatFirebase droomChatFirebase;
    Double lat, lng;
    String pincode, region, fullAddress;
    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "", fullAddres = "";
    Toolbar mToolbar;
    TextView timeCount1,timeCount2,timeCount3,timeCount4,timeCount5,totalTimeTextView;
    ImageView hourGlass1,hourGlass2,hourGlass3,hourGlass4,hourGlass5;
    ImageView aboveImageView,aboveImageView1,aboveImageView2,aboveImageView3,aboveImageView4,aboveImageView5;
    ImageView belowImageView,belowImageView1,belowImageView2,belowImageView3,belowImageView4,belowImageView5;
    ImageView aboveAboveImageView,aboveAboveImageView1,aboveAboveImageView2,aboveAboveImageView3,aboveAboveImageView4,aboveAboveImageView5;
    //MainActivity mActivity = new MainActivity();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        PagerSlidingTabStrip.width = width / 2;
       View  v= inflater.inflate(R.layout.broker_main_screen, container, false);
        ((MainActivity)getActivity()).setMapsClicked(this);

        //mHideShow = (LinearLayout) v.findViewById(R.id.showMap);
        mMapView = (FrameLayout) v.findViewById(R.id.mapView);
        bPinLocation = (ImageButton)v.findViewById(R.id.bPinLocation);


        hourGlass1= (ImageView) v.findViewById(R.id.hglass_imageView1);
        aboveImageView1= (ImageView) v.findViewById(R.id.imageView_above1);
        belowImageView1= (ImageView) v.findViewById(R.id.imageView_below1);
        aboveAboveImageView1= (ImageView) v.findViewById(R.id.imageView_above_above1);
        timeCount1= (TextView) v.findViewById(R.id.timeCount1);

        hourGlass2= (ImageView) v.findViewById(R.id.hglass_imageView2);
        aboveImageView2= (ImageView) v.findViewById(R.id.imageView_above2);
        belowImageView2= (ImageView) v.findViewById(R.id.imageView_below2);
        aboveAboveImageView2= (ImageView) v.findViewById(R.id.imageView_above_above2);
        timeCount2= (TextView) v.findViewById(R.id.timeCount2);

        hourGlass3= (ImageView) v.findViewById(R.id.hglass_imageView3);
        aboveImageView3= (ImageView) v.findViewById(R.id.imageView_above3);
        belowImageView3= (ImageView) v.findViewById(R.id.imageView_below3);
        aboveAboveImageView3= (ImageView) v.findViewById(R.id.imageView_above_above3);
        timeCount3= (TextView) v.findViewById(R.id.timeCount3);

        hourGlass4= (ImageView) v.findViewById(R.id.hglass_imageView4);
        aboveImageView4= (ImageView) v.findViewById(R.id.imageView_above4);
        belowImageView4= (ImageView) v.findViewById(R.id.imageView_below4);
        aboveAboveImageView4= (ImageView) v.findViewById(R.id.imageView_above_above4);
        timeCount4= (TextView) v.findViewById(R.id.timeCount4);

        hourGlass5= (ImageView) v.findViewById(R.id.hglass_imageView5);
        aboveImageView5= (ImageView) v.findViewById(R.id.imageView_above5);
        belowImageView5= (ImageView) v.findViewById(R.id.imageView_below5);
        aboveAboveImageView5= (ImageView) v.findViewById(R.id.imageView_above_above5);
        timeCount5= (TextView) v.findViewById(R.id.timeCount5);

        totalTimeTextView= (TextView) v.findViewById(R.id.total_time_textView);
        popup= (LinearLayout) v.findViewById(R.id.popup_element);

        hourGlassFirebase=new HourGlassFirebase(getActivity(),DatabaseConstants.firebaseUrl);

        dbHelper=new DBHelper(getContext());


        if(!dbHelper.getValue(DatabaseConstants.userId).equals("null"))
        {
            hourGlassDetails=hourGlassFirebase.getHourGlassDetails();
            filledHourGlass=hourGlassDetails.getWholeHourGlass();
            percentage=hourGlassDetails.getPercentage();

        }

        timeCount1.setText("" + totalTime);
        timeCount2.setText(""+ totalTime);
        timeCount3.setText(""+totalTime);
        timeCount4.setText(""+totalTime);
        timeCount5.setText("" + totalTime);

        initialFill(filledHourGlass-2);
        fillHourGlasses(filledHourGlass-2, 50);
        updateTotalTime();

        leftHourGlasses=500-filledHourGlass*100-percentage;
        if(!dbHelper.getValue(DatabaseConstants.coolOff).equals("null")) {
            // coolOffString=dbHelper.getValue(DatabaseConstants.coolOff);
            coolOff=Integer.parseInt(dbHelper.getValue(DatabaseConstants.coolOff));

            if(leftHourGlasses != 0 ) {
                totalTime = coolOff * 100 / leftHourGlasses;
            }
            else
            {
                totalTime = 0;
            }
            currentTime=totalTime*filledHourGlass;
            currentTime+=percentage*totalTime/100;
        }

       // earnOk = (Button) v.findViewById(R.id.earnOk);
        if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null")&& isNetworkAvailable())
            preok();
        droomChatFirebase=new DroomChatFirebase(DatabaseConstants.firebaseUrl);
        mPager = (ViewPager) v.findViewById(R.id.pager);
        mTabs  = (SlidingTabLayout) v.findViewById(R.id.tabs);
        //mTabs.setDistributeEvenly(true);
        ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
        pagerItems.add(new PagerItem("Tenants", new Rental_Broker_Requirement()));
        pagerItems.add(new PagerItem("Owners", new Rental_Broker_Available()));
        adapter = new MyPagerAdapter(getChildFragmentManager(),pagerItems);
        mTabs.setDistributeEvenly(true);
        mTabs.setBackgroundColor(Color.parseColor("#031625"));
        mPager.setAdapter(adapter);
        mTabs.setViewPager(mPager);
        //fillHourGlasses(0, 99);
        /*for(int i=0;i<100;i++) {
            fillHourGlasses(0, i);
        }*/
        //Log.i("Test",droomChatFirebase.getDroomList(dbHelper.getValue(DatabaseConstants.userId)).toString());

            mCustomPhasedSeekbar = (CustomPhasedSeekBar) v.findViewById(R.id.phasedSeekBar);
            if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{"Rental", "Sale"}));
        else
            mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.broker_type1_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.broker_type1_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Loan", "Auction"}));
        mCustomPhasedSeekbar.setListener(this);
        bPinLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMyLocationEnabled(true);
            }
        });

//        earnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) getActivity()).changeFragment(new EarnOkFragment(), null);
//            }
//        });

        mHandler = new Handler();
        mHandler.postDelayed(mStatusChecker, 2000);
        return v;
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
                //fillHourGlasses(0, intervalCount * mInterval / 1000);
            if((leftHourGlasses!=0))
                calculateFillingQuantity(currentTime);
                //updateStatus(); //this function can change value of mInterval.
                mHandler.postDelayed(mStatusChecker, 2000);


        }
    };

    public void calculateFillingQuantity(int time){
        fillHourGlasses(time/totalTime,time%totalTime);
    }

    private void hideMap(int i) {

        Animation m = null;

        //Load animation
        if(i==0) {
            m = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_down);


                    //SharedPrefs.getString(getActivity(),SharedPrefs.MY_LOCALITY)+","+SharedPrefs.getString(getActivity(),SharedPrefs.MY_CITY)
        }else {

            m = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_up);
        }

        mMapView.setAnimation(m);
    }

    public void updateTotalTime(){
        totalTimeTextView.setText("Total time left is : "+(Integer.parseInt((String) timeCount1.getText())+Integer.parseInt((String) timeCount2.getText())+Integer.parseInt((String) timeCount3.getText())+Integer.parseInt((String) timeCount4.getText())+Integer.parseInt((String) timeCount5.getText())));
    }

    @Override
    public void onPause() {

        super.onPause();
        ((MainActivity)getActivity()).hideOpenMaps();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity)getActivity()).hideResideMenu();
        ((MainActivity)getActivity()).showOpenMaps();
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    @Override
    public void clicked() {




        if (mMapView.getVisibility() == View.VISIBLE) {

                    hideMap(0);
                    mMapView.setVisibility(View.GONE);
                    preok();
                    onPositionSelected(currentItem, currentCount);

                } else {
            final LocationManager manager = (LocationManager)getActivity(). getSystemService( Context.LOCATION_SERVICE );







            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
            }
                    mMapView.setVisibility(View.VISIBLE);
                    hideMap(1);

                    if (!mFirst) {



                        customMapFragment = ((CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

                        customMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                map = googleMap;

                                map.setMyLocationEnabled(false);
                                //plotMyNeighboursHail.markerpos(my_user_id, pointer_lng, pointer_lat, which_type, my_role, map);
                                //selectedLocation = map.getCameraPosition().target;


                            }
                        });

                        customMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
                            @Override
                            public void onDrag(MotionEvent motionEvent) {
                                //pin location
                                latlng = map.getCameraPosition().target;
                                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                                    //Toast.makeText(getActivity(), "Moved", Toast.LENGTH_LONG).show();
                                    //mActivity.showToastMessage("Moved");
                                    ((MainActivity)getActivity()).showToastMessage("Moved");

                                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                                    //Toast.makeText(getActivity(), "Up", Toast.LENGTH_LONG).show();
                                    //mActivity.showToastMessage("Up");
                                    ((MainActivity)getActivity()).showToastMessage("Up");
                                    if (isNetworkAvailable()) {

                                        lat = latlng.latitude;
                                        lng = latlng.longitude;
                                        SharedPrefs.save(getActivity(),SharedPrefs.MY_LAT,lat+"");
                                        SharedPrefs.save(getActivity(),SharedPrefs.MY_LNG,lng+"");
                                        new LocationUpdater().execute();
                                    }
                                }
                                
                            }
                        });

                        new GetCurrentLocation(getActivity(), new GetCurrentLocation.CurrentLocationCallback() {
                            @Override
                            public void onComplete(Location location) {
                                if (location != null) {
                                    Double lat = location.getLatitude();
                                    Double lng = location.getLongitude();
                                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    //pin location
                                    latlng = map.getCameraPosition().target;

                                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                                    map.animateCamera(CameraUpdateFactory.zoomTo(16));

                                    //make retrofit call to get Min Max price

                                }
                            }
                        });


                        mFirst = true;
                    }
                }



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        DPTOPX_SCALE = getResources().getDisplayMetrics().density;

    }
    public void initialFill(int noOfHourGlass) {
        int i=1;
        for(i=1;i<=noOfHourGlass;i++)
        {
            fillHourGlasses(i-1,99);
        }
        leftHourGlasses=5-noOfHourGlass;
        switch (noOfHourGlass)
        {
            case 1:timeCount1.setText("0");
                break;
            case 2:timeCount1.setText("0");
                timeCount2.setText("0");
                break;
            case 3:timeCount1.setText("0");
                timeCount2.setText("0");
                timeCount3.setText("0");
                break;
            case 4:timeCount1.setText("0");
                timeCount2.setText("0");
                timeCount3.setText("0");
                timeCount4.setText("0");
                break;
            case 5:timeCount1.setText("0");
                timeCount2.setText("0");
                timeCount3.setText("0");
                timeCount4.setText("0");
                timeCount5.setText("0");
                break;
        }
    }

    public void fillHourGlasses(int wholeNumber, final int percentageToBeFilled){
        final int originalHeight=hourGlass1.getLayoutParams().height/2;
        final int calculatedHeight=((percentageToBeFilled  * originalHeight)/100);
        if(percentageToBeFilled>=100)
        {
            leftHourGlasses-=100;
        }

        switch (wholeNumber)
        {
            case 0:
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) aboveAboveImageView1.getLayoutParams();
                params1.height                    = calculatedHeight;
                aboveAboveImageView1.setLayoutParams(params1);

                LinearLayout.LayoutParams params11 = (LinearLayout.LayoutParams) aboveImageView1.getLayoutParams();
                params11.height                    = originalHeight-calculatedHeight;
                aboveImageView1.setLayoutParams(params11);

                LinearLayout.LayoutParams params12 = (LinearLayout.LayoutParams) belowImageView1.getLayoutParams();
                params12.height                    = originalHeight-calculatedHeight;
                belowImageView1.setLayoutParams(params12);


                timeCount1.setText("" + percentageToBeFilled * totalTime / 100);
                updateTotalTime();

                break;
            case 1:
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) aboveAboveImageView2.getLayoutParams();
                params2.height                    = calculatedHeight;
                aboveAboveImageView2.setLayoutParams(params2);

                LinearLayout.LayoutParams params21 = (LinearLayout.LayoutParams) aboveImageView2.getLayoutParams();
                params21.height                    = originalHeight-calculatedHeight;
                aboveImageView2.setLayoutParams(params21);

                LinearLayout.LayoutParams params22 = (LinearLayout.LayoutParams) belowImageView2.getLayoutParams();
                params22.height                    = originalHeight-calculatedHeight;
                belowImageView2.setLayoutParams(params22);
                timeCount2.setText("" + percentageToBeFilled * totalTime / 100);
                updateTotalTime();
                break;
            case 2:

                LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) aboveAboveImageView3.getLayoutParams();
                params3.height                    = calculatedHeight;
                aboveAboveImageView3.setLayoutParams(params3);

                LinearLayout.LayoutParams params31 = (LinearLayout.LayoutParams) aboveImageView3.getLayoutParams();
                params31.height                    = originalHeight-calculatedHeight;
                aboveImageView3.setLayoutParams(params31);

                LinearLayout.LayoutParams params32 = (LinearLayout.LayoutParams) belowImageView3.getLayoutParams();
                params32.height                    = originalHeight-calculatedHeight;
                belowImageView3.setLayoutParams(params32);
                timeCount3.setText("" + percentageToBeFilled * totalTime / 100);
                updateTotalTime();
                break;
            case 3:

                LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) aboveAboveImageView4.getLayoutParams();
                params4.height                    = calculatedHeight;
                aboveAboveImageView4.setLayoutParams(params4);

                LinearLayout.LayoutParams params41 = (LinearLayout.LayoutParams) aboveImageView4.getLayoutParams();
                params41.height                    = originalHeight-calculatedHeight;
                aboveImageView4.setLayoutParams(params41);

                LinearLayout.LayoutParams params42 = (LinearLayout.LayoutParams) belowImageView4.getLayoutParams();
                params42.height                    = originalHeight-calculatedHeight;
                belowImageView4.setLayoutParams(params42);
                timeCount4.setText("" + percentageToBeFilled * totalTime / 100);
                updateTotalTime();
                break;
            case 4:

                LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) aboveAboveImageView5.getLayoutParams();
                params5.height                    = calculatedHeight;
                aboveAboveImageView5.setLayoutParams(params5);

                LinearLayout.LayoutParams params51 = (LinearLayout.LayoutParams) aboveImageView5.getLayoutParams();
                params51.height                    = originalHeight-calculatedHeight;
                aboveImageView5.setLayoutParams(params51);

                LinearLayout.LayoutParams params52 = (LinearLayout.LayoutParams) belowImageView5.getLayoutParams();
                params52.height                    = originalHeight-calculatedHeight;
                belowImageView5.setLayoutParams(params52);
                timeCount5.setText(""+percentageToBeFilled*totalTime/100);
                updateTotalTime();
                break;

                //Log.i("Mnni", originalHeight + "   " + calculatedHeight + "  " + percentageToBeFilled);

        }
    }

    @Override
    public void onPositionSelected(int position, int count) {

        currentCount=count;
        if(count!=2) {

            if (position == 2) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Loan Seekers", new Loan_Broker_Requirement()));
                pagerItems.add(new PagerItem("Loan Lenders", new Loan_Broker_Available()));
                if(isAdded()) {
                    adapter.setPagerItems(pagerItems);
                    adapter.notifyDataSetChanged();
                    mTabs.settabData();
                    mTabs.setDistributeEvenly(true);
                    currentItem = 2;
                }
                //mTabs.notifyAll();

                //mPager.invalidate();
            }

            if (position == 1) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Seekers", new Sale_Broker_Requirement_new()));
                pagerItems.add(new PagerItem("Owners", new Sale_Broker_Available_new()));
                if(isAdded()) {
                    adapter.setPagerItems(pagerItems);
                    adapter.notifyDataSetChanged();
                    mTabs.settabData();
                    mTabs.setDistributeEvenly(true);
                    currentItem = 1;
                }
                //mTabs.notifyAll();

                //mPager.invalidate();
            }

            if (position == 0) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Tenants", new Rental_Broker_Requirement()));
                pagerItems.add(new PagerItem("Owners", new Rental_Broker_Available()));
                if(isAdded()) {
                    adapter.setPagerItems(pagerItems);
                    adapter.notifyDataSetChanged();
                    mTabs.settabData();
                    mTabs.setDistributeEvenly(true);
                    currentItem = 0;
                }
                //mTabs.notifyAll();

                //mPager.invalidate();
            }

            if (position == 3) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Tenants", new Rental_Broker_Requirement()));
                pagerItems.add(new PagerItem("Owners", new Rental_Broker_Available()));
                if(isAdded()) {
                    adapter.setPagerItems(pagerItems);
                    adapter.notifyDataSetChanged();
                    mTabs.settabData();
                    mTabs.setDistributeEvenly(true);
                    currentItem = 3;
                }
                //mTabs.notifyAll();

                //mPager.invalidate();
            }
        }

        else{
            if (position == 1) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Seekers", new Sale_Broker_Requirement_new()));
                pagerItems.add(new PagerItem("Owners", new Sale_Broker_Available_new()));
                if(isAdded()) {
                    adapter.setPagerItems(pagerItems);
                    adapter.notifyDataSetChanged();
                    mTabs.settabData();
                    mTabs.setDistributeEvenly(true);
                    currentItem = 1;
                }
                //mTabs.notifyAll();

                //mPager.invalidate();
            }

            if (position == 0) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Tenants", new Rental_Broker_Requirement()));
                pagerItems.add(new PagerItem("Owners", new Rental_Broker_Available()));
                if(isAdded()) {
                    adapter.setPagerItems(pagerItems);
                    adapter.notifyDataSetChanged();
                    mTabs.settabData();
                    mTabs.setDistributeEvenly(true);
                    currentItem = 0;
                }
                //mTabs.notifyAll();

                //mPager.invalidate();
            }
        }

    }


    class MyPagerAdapter extends FragmentPagerAdapter
    {
        String[] title = {"Tenants","Owners"};
        int phasedSeekbarPosition = 0;

        private FragmentManager mFragmentManager;
        private ArrayList<PagerItem> mPagerItems;

        public MyPagerAdapter(FragmentManager fm,ArrayList<PagerItem> pagerItems) {
            super(fm);
            mFragmentManager = fm;
            mPagerItems = pagerItems;
        }

        public void setPagerItems(ArrayList<PagerItem> pagerItems) {
            if (mPagerItems != null)
                for (int i = 0; i < mPagerItems.size(); i++) {
                    mFragmentManager.beginTransaction().remove(mPagerItems.get(i).getFragment()).commit();
                }
            mPagerItems = pagerItems;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
//            if(phasedSeekbarPosition == 0) {
//                if (position == 1) {
//                    return new Rental_Broker_Available();
//                } else {
//                    return new Rental_Broker_Requirement();
//                }
//            }else
//            {
//                if (position == 1) {
//                    return new Rental_Broker_Available();
//                } else {
//                    return new Rental_Broker_Requirement();
//                }
//
//            }

            return mPagerItems.get(position).getFragment();

        }


        public void setTitleAndPosition(String[] titles,int position)
        {
            this.title = titles;
            this.phasedSeekbarPosition = position;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPagerItems.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return mPagerItems.size();
        }
    }

    public void openDroomList(){
        Fragment fragment = new Droom_chats_list();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + mPager.getCurrentItem());


        if(currentItem == 0 || currentItem == 3) {

            if (mPager.getCurrentItem() == 0) {
                Rental_Broker_Requirement f = (Rental_Broker_Requirement) page;
                f.onActivityResult(requestCode, resultCode, data);
            } else {
                Rental_Broker_Available f = (Rental_Broker_Available) page;
                f.onActivityResult(requestCode, resultCode, data);
            }
        }else if(currentItem == 1) {

            if (mPager.getCurrentItem() == 0) {
                Sale_Broker_Requirement_new f = (Sale_Broker_Requirement_new) page;
                f.onActivityResult(requestCode, resultCode, data);
            } else {
                Sale_Broker_Available_new f = (Sale_Broker_Available_new) page;
                f.onActivityResult(requestCode, resultCode, data);
            }
        }else if(currentItem == 2) {

            if (mPager.getCurrentItem() == 0) {
                Loan_Broker_Requirement f = (Loan_Broker_Requirement) page;
                f.onActivityResult(requestCode, resultCode, data);
            } else {
                Loan_Broker_Available f = (Loan_Broker_Available) page;
                f.onActivityResult(requestCode, resultCode, data);
            }
        }



    }

    public void setPhasedSeekBar(){

       // mCustomPhasedSeekbar = (CustomPhasedSeekBar) v.findViewById(R.id.phasedSeekBar);
        if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{"Rental", "Sale"}));
        else
            mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.broker_type1_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.broker_type1_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Loan", "Auction"}));
        mCustomPhasedSeekbar.setListener(this);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void preok() {
        String API = DatabaseConstants.serverUrl;
        Oyeok preok = new Oyeok();
        preok.setDeviceId("Hardware");
        //preok.setGcmId("gliui");
        preok.setUserRole("broker");
        //preok.setLong("72.1456");
        //preok.setLat("19.2344");
        preok.setPushToken(dbHelper.getValue(DatabaseConstants.gcmId));
        preok.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        preok.setLong(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
        preok.setLat(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
        preok.setPlatform("android");


        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService user1 = restAdapter.create(OyeokApiService.class);
        try {
            user1.preOk(preok, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    JsonObject k = jsonElement.getAsJsonObject();
                    try {
                        JSONObject ne = new JSONObject(k.toString());
                        Log.i("preok response", ne.toString());
                        JSONObject response1 = ne.getJSONObject("responseData");
                        String coolOffPeriod = response1.getString("cool_off");
                        Log.i("cool_off=", coolOffPeriod);
                        JSONObject neighbours = ne.getJSONObject("responseData").getJSONObject("neighbours");
                        JSONArray reqLl = neighbours.getJSONArray("req_ll");
                        JSONArray reqOr = neighbours.getJSONArray("req_or");
                        JSONArray avlLl = neighbours.getJSONArray("avl_ll");
                        JSONArray avlOr = neighbours.getJSONArray("avl_or");
                        //   Log.i("oye_id=", reqLl.getJSONObject(0).getString("oye_id"));
                        dbHelper.save(DatabaseConstants.coolOff,coolOffPeriod);
                        dbHelper.save(DatabaseConstants.reqLl, reqLl.toString());
                        dbHelper.save(DatabaseConstants.reqOr, reqOr.toString());
                        dbHelper.save(DatabaseConstants.avlLl, avlLl.toString());
                        dbHelper.save(DatabaseConstants.avlOr, avlOr.toString());
                        //
                        //  Log.i("req_ll from db: ", dbHelper.getValue(DatabaseConstants.reqLl));
                        onPositionSelected(currentItem,currentCount);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {


                }
//            @Override
//            public void success(PreOk.ResponseData n, Response response) {
//                //Log.i("preok response",neighbours);
//                Log.i("preok",n.neighbours.toString());
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//
//            }
            });
        }catch (Exception e){
            Log.i("Exception","caught in preok");
        }
    }

    protected class LocationUpdater extends AsyncTask<Double, Double, String> {
        public JSONObject getJSONfromURL(String url) {

            // initialize
            InputStream is = null;
            String result = "";
            JSONObject jObject = null;

            // http post
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

            } catch (Exception e) {
                Log.e(TAG, "Error in http connection " + e.toString());
            }

            // convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e(TAG, "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                jObject = new JSONObject(result);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing data " + e.toString());
            }

            return jObject;
        }
        @Override
        protected String doInBackground(Double[] objects) {
            try {
                JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + ","
                        + lng + "&sensor=true");
                String Status = jsonObj.getString("status");
                if (Status.equalsIgnoreCase("OK")) {
                    JSONArray Results = jsonObj.getJSONArray("results");
                    JSONObject zero = Results.getJSONObject(0);
                    JSONArray address_components = zero.getJSONArray("address_components");

                    fullAddress = zero.getString("formatted_address");
                    Log.v(TAG, "from async task : address is" + fullAddress);
                    for (int i = 0; i < address_components.length(); i++) {
                        JSONObject zero2 = address_components.getJSONObject(i);
                        String long_name = zero2.getString("long_name");
                        JSONArray mtypes = zero2.getJSONArray("types");
                        String Type = mtypes.getString(0);

                        if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                            if (Type.equalsIgnoreCase("street_number")) {
                                Address1 += long_name;
                            } else if (Type.equalsIgnoreCase("route")) {
                                Address1 += " "+long_name;
                            } else if (Type.equalsIgnoreCase("sublocality_level_2")) {
                                Address2 = long_name;
                            } else if (Type.equalsIgnoreCase("sublocality_level_1")){
                                Address2 += " "+long_name;
                                SharedPrefs.save(getActivity(),SharedPrefs.MY_LOCALITY,long_name);
                            } else if (Type.equalsIgnoreCase("locality")) {
                                // Address2 = Address2 + long_name + ", ";
                                City = long_name;
                                SharedPrefs.save(getActivity(),SharedPrefs.MY_CITY, City);
                            } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                                County = long_name;
                            } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                State = long_name;
                            } else if (Type.equalsIgnoreCase("country")) {
                                Country = long_name;
                            } else if (Type.equalsIgnoreCase("postal_code")) {
                                PIN = long_name;
                                SharedPrefs.save(getActivity(),SharedPrefs.MY_PINCODE,PIN);
                            }
                        }

                        SharedPrefs.save(getActivity(),SharedPrefs.MY_REGION,fullAddress);
                        Log.v(TAG,"from asynctask "+fullAddress);
                        // JSONArray mtypes = zero2.getJSONArray("types");
                        // String Type = mtypes.getString(0);
                        // Log.e(Type,long_name);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return fullAddress;
        }

        @Override
        protected void onPostExecute(String s) {

            MainActivity main = (MainActivity)getActivity();
            main.setTitle(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY) + "," + SharedPrefs.getString(getActivity(), SharedPrefs.MY_CITY));

            //TODO: set action bar title here
        }
    }

    public void replaceWithSignUp(final JSONArray p, final int j){
        if(!dbHelper.getValue(DatabaseConstants.user).equals("Broker"))
        {
            if(!dbHelper.getValue(DatabaseConstants.user).equals("Client"))
            {

                dbHelper.save(DatabaseConstants.userRole, "Broker");
                Bundle bundle = new Bundle();
                //bundle.putStringArray("propertySpecification",propertySpecification);
                bundle.putString("lastFragment", "RentalBrokerRequirement");
                bundle.putString("JsonArray", p.toString());
                bundle.putInt("Position",j);
                Fragment fragment = null;
                fragment = new SignUpFragment();
                fragment.setArguments(bundle);

                String title = "Sign Up";
                FragmentManager fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
            }
            else
            {
                dbHelper.save(DatabaseConstants.userRole, "Broker");
                dbHelper.save(DatabaseConstants.user,"Broker");
                String API=DatabaseConstants.serverUrl;
                //{"user_role":"broker","device_id":"device", "lat":89.2, "long":78.2, "user_role":"client", "gcm_id":"ping"}
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(API).setLogLevel(RestAdapter.LogLevel.FULL).build();
                OyeokApiService service;

                User user = new User();
                user.setUserRole("broker");
                user.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
                user.setPushToken(dbHelper.getValue(DatabaseConstants.gcmId));
                user.setLongitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
                user.setLatitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
                user.setDeviceId("deviceId");
                user.setPlatform("android");
                if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null") && isNetworkAvailable()) {
                    try {
                        UserApiService user1 = restAdapter.create(UserApiService.class);
                        user1.userGps(user, new Callback<User>() {
                            @Override
                            public void success(User user, Response response) {
                                Log.i("role changed to", "Broker");
                                AcceptOkCall a = new AcceptOkCall();
                                a.acceptOk(p,j,dbHelper, getActivity());




                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("to broker failed", error.getMessage());
                            }
                        });
                    }
                    catch (Exception e){
                        Log.i("Exception","caught in user gps call");
                    }
                }
            }
        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void openChat(Bundle args){
        Fragment fragment = new Droom_Chat_New();
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

}
