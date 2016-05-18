package com.nbourses.oyeok.fragments;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
public class BrokerPreokFragment extends Fragment implements CustomPhasedListener, CircularSeekBarNew.imageAction, OnAcceptOkSuccess,OnChartValueSelectedListener {

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

    @Bind(R.id.texPtype)
    TextView texPtype;

    @Bind(R.id.texPstype)
    TextView texPstype;

    @Bind(R.id.beaconOK)
    TextView beaconOK;

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

    @Bind(R.id.chart)
    BarChart chart;

    @Bind(R.id.okBtn)
    Button okBtn;

    @Bind(R.id.setB)
    TextView setB;

    @Bind(R.id.selectB)
    TextView selectB;
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
    private int[] arr;
    private Animation slide_up;
    private Animation slide_down;
    private ObjectAnimator animation;



    Animation bounce;
    Animation zoomin;
    Animation zoomout;


    public BrokerPreokFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  v = inflater.inflate(R.layout.fragment_broker_preok, container, false);
        ButterKnife.bind(this, v);
        bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        zoomin = AnimationUtils.loadAnimation(getContext(), R.anim.zoomin);
        zoomout = AnimationUtils.loadAnimation(getContext(), R.anim.zoomout);


        init();

        return v;
    }



    private void init() {

        slide_up = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up);
        slide_down = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_down);

        animation = ObjectAnimator.ofFloat(chart, "rotationY", 0.0f, 360f);  // HERE 360 IS THE ANGLE OF ROTATE, YOU CAN USE 90, 180 IN PLACE OF IT,  ACCORDING TO YOURS REQUIREMENT

        animation.setDuration(500); // HERE 500 IS THE DURATION OF THE ANIMATION, YOU CAN INCREASE OR DECREASE ACCORDING TO YOURS REQUIREMENT
        animation.setInterpolator(new AccelerateDecelerateInterpolator());



        chart();


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


       try {
           SharedPreferences prefs =
                   PreferenceManager.getDefaultSharedPreferences(getContext());
           listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
               public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                   if (key.equals(AppConstants.RENTAL_COUNT)) {
                       if (General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT) <= 0)
                           rentalCount.setVisibility(View.GONE);
                       else {
                           rentalCount.setVisibility(View.VISIBLE);
                           rentalCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT)));
                       }
                   }
                   if (key.equals(AppConstants.RESALE_COUNT)) {
                       if (General.getBadgeCount(getContext(), AppConstants.RESALE_COUNT) <= 0)
                           resaleCount.setVisibility(View.GONE);
                       else {
                           resaleCount.setVisibility(View.VISIBLE);
                           resaleCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RESALE_COUNT)));
                       }


                   }

                   if (key.equals(AppConstants.TENANTS_COUNT)) {
                       if (General.getBadgeCount(getContext(), AppConstants.TENANTS_COUNT) <= 0)
                           option1Count.setVisibility(View.GONE);
                       else {
                           option1Count.setVisibility(View.VISIBLE);
                           option1Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.TENANTS_COUNT)));
                       }

                   }
                   if (key.equals(AppConstants.OWNERS_COUNT)) {
                       if (General.getBadgeCount(getContext(), AppConstants.OWNERS_COUNT) <= 0)
                           option2Count.setVisibility(View.GONE);
                       else {
                           option2Count.setVisibility(View.VISIBLE);
                           option2Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.OWNERS_COUNT)));
                       }

                   }

                   if (key.equals(AppConstants.BUYER_COUNT)) {
                       if (General.getBadgeCount(getContext(), AppConstants.BUYER_COUNT) <= 0)
                           option1Count.setVisibility(View.GONE);
                       else {
                           option1Count.setVisibility(View.VISIBLE);
                           option1Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.BUYER_COUNT)));
                       }

                   }

                   if (key.equals(AppConstants.SELLER_COUNT)) {
                       if (General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT) <= 0)
                           option2Count.setVisibility(View.GONE);
                       else {
                           option2Count.setVisibility(View.VISIBLE);
                           option2Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT)));
                       }

                   }


               }


           };
           prefs.registerOnSharedPreferenceChangeListener(listener);

       }
       catch (Exception e){
           Log.e(TAG, e.getMessage());
       }



        mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(),
                new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector},
                new String[]{"30", "15"},
                new String[]{"Rental", "Resale"
                }));
        mCustomPhasedSeekbar.setListener(this);

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
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4, 0));
        entries.add(new BarEntry(8, 1));
        entries.add(new BarEntry(6, 2));
        entries.add(new BarEntry(12, 3));
        entries.add(new BarEntry(18, 4));
        entries.add(new BarEntry(9, 5));
        entries.add(new BarEntry(14, 6));
        entries.add(new BarEntry(18, 7));
        entries.add(new BarEntry(16, 8));
        entries.add(new BarEntry(22, 9));
        BarDataSet dataset = new BarDataSet(entries, "10");

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("Sept");
        labels.add("Oct");


        BarData data = new BarData(labels, dataset);
        chart.setData(data); // set the data and list of lables into chart

        chart.setDescription("Select three Buildings");

        chart.animateY(2000);


        chart.setScaleYEnabled(false);
        //   chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
//        chart.setDragEnabled(true);
        chart.setOnChartValueSelectedListener(this);






    }




    /**
     * load preok data by making server API call
     */
    public void preok() {
        Log.i("TRACE","GCM id is"+SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        //preok params
        Oyeok preok = new Oyeok();
        preok.setDeviceId("Hardware");
        preok.setUserRole("broker");
        preok.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        preok.setLong(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
        preok.setLat(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
        preok.setPlatform("android");
        if(General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER).equals(""))
        preok.setUserId("demo_id");
        else
            preok.setUserId(General.getSharedPreferences(getContext(),AppConstants.USER_ID));


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

                        jsonArrayReqLl = neighbours.getJSONArray("recent");;//neighbours.getJSONArray("req_ll");
                        jsonArrayAvlLl = neighbours.getJSONArray("recent");//neighbours.getJSONArray("avl_ll");

                        jsonArrayReqOr = neighbours.getJSONArray("recent");//neighbours.getJSONArray("req_or");
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
    public void onOptionClickok(View v){

        buildingSlider.startAnimation(slide_down);
        buildingSlider.setVisibility(View.GONE);

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
            animation.start();
        }
        else if (v.getId() == setB.getId()) {
            setB.setBackgroundResource(R.color.greenish_blue);
            animation.start();
        }

    }
    @OnClick({R.id.txtOption1, R.id.txtOption2})
    public void onOptionClick(View v) {

        //okButton.setAnimation(zoomin);
        //okButton.setAnimation(zoomout);

        if (txtPreviouslySelectedOption != null)
            txtPreviouslySelectedOption.setBackgroundResource(R.color.colorPrimaryDark);

        txtPreviouslySelectedOption = (TextView) v;

        if (v.getId() == txtOption1.getId()) {
            rentText.setVisibility(View.GONE);
            texPtype.setVisibility(View.GONE);
            texPstype.setVisibility(View.GONE);
//            option2CountCont2.setVisibility(View.GONE);
        //    option2Count.setVisibility(View.GONE);
            txtOption1.setBackgroundResource(R.color.greenish_blue);
            currentOptionSelectedString = txtOption1.getText().toString();
            Log.i("PREOK CALLED","currentOptionSelectedString"+currentOptionSelectedString);

           // update circular seekbar

            if (currentOptionSelectedString.equalsIgnoreCase(strSeekers))
                currentOptionSelectedString = strTenants;
            Log.i("PREOK CALLED","currentOptionSelectedString1 phase"+currentOptionSelectedString);
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

 //           option2CountCont1.setVisibility(View.GONE);
          //  option1Count.setVisibility(View.GONE);
            txtOption2.setBackgroundResource(R.color.greenish_blue);
            currentOptionSelectedString = txtOption2.getText().toString();
            Log.i("PREOK CALLED","currentOptionSelectedString"+currentOptionSelectedString);
            rentText.setVisibility(View.GONE);
            //displayOkText.setVisibility(View.GONE);
            texPtype.setVisibility(View.GONE);
            texPstype.setVisibility(View.GONE);
            // update circular seekbar

            if (currentOptionSelectedString.equalsIgnoreCase(strTenants))
                currentOptionSelectedString = strSeekers;
            Log.i("PREOK CALLED","currentOptionSelectedString2 phase"+currentOptionSelectedString);

            if (jsonArrayReqOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeekers)) {
                Log.i("PREOK CALLED", "values set phase" + jsonArrayReqOr.toString());
                circularSeekbar.setValues(jsonArrayReqOr.toString());
            }
            else if (jsonArrayAvlOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeller)) {
                Log.i("PREOK CALLED", "values set phase" + jsonArrayAvlOr.toString());
                circularSeekbar.setValues(jsonArrayAvlOr.toString());
            }

        }

        onPositionSelected(currentSeekbarPosition, currentCount);
    }

    @Override
    public void onPositionSelected(int position, int count) {
        animatebadges();

        currentSeekbarPosition = position;
        Log.i("PREOK CALLED","currentSeekbarPosition"+currentSeekbarPosition);

        if (position == 0) {

            rentText.setVisibility(View.GONE);
            texPtype.setVisibility(View.GONE);
            texPstype.setVisibility(View.GONE);

            resaleCount.setVisibility(View.GONE);


            if(General.getBadgeCount(getContext(),AppConstants.RENTAL_COUNT)<=0)
                rentalCount.setVisibility(View.GONE);
            else {
                rentalCount.setVisibility(View.VISIBLE);
                rentalCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RENTAL_COUNT)));
            }

            if(General.getBadgeCount(getContext(),AppConstants.TENANTS_COUNT)<=0)
                //option1Count.setVisibility(View.GONE);
                option1Count.setVisibility(View.GONE);
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


        }
        else if (position == 1) {


            rentText.setVisibility(View.GONE);
            texPtype.setVisibility(View.GONE);
            texPstype.setVisibility(View.GONE);

            rentalCount.setVisibility(View.GONE);
            if(General.getBadgeCount(getContext(),AppConstants.RESALE_COUNT)<=0)
                resaleCount.setVisibility(View.GONE);
            else {
                resaleCount.setVisibility(View.VISIBLE);
                resaleCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.RESALE_COUNT)));
            }
            if(General.getBadgeCount(getContext(),AppConstants.BUYER_COUNT)<=0)
                //option1Count.setVisibility(View.GONE);
                option1Count.setVisibility(View.GONE);
            else {
                //option1Count.setVisibility(View.VISIBLE);
                option1Count.setVisibility(View.VISIBLE);
                option1Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.BUYER_COUNT)));
            }
            if(General.getBadgeCount(getContext(),AppConstants.SELLER_COUNT)<=0)
                //option2Count.setVisibility(View.GONE);
                option2Count.setVisibility(View.GONE);
            else {
                //option2Count.setVisibility(View.VISIBLE);
                option2Count.setVisibility(View.VISIBLE);
                option2Count.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.SELLER_COUNT)));
            }



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



            if (jsonObjectArray == null) {


                buildingSlider.startAnimation(slide_up);
                buildingSlider.setVisibility(View.VISIBLE);
//                SnackbarManager.show(
//                        com.nispok.snackbar.Snackbar.with(getActivity())
//                                .position(Snackbar.SnackbarPosition.BOTTOM)
//                                .text("Please select a deal")
//                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
//                if (buildingSlider.getVisibility() == View.GONE) {
//                    TranslateAnimation animate = new TranslateAnimation(0, 0, 0 , buildingSlider.getHeight());
//                    animate.setDuration(2000);
//                    animate.setFillAfter(true);
//                    buildingSlider.startAnimation(animate);
//                    buildingSlider.setVisibility(View.VISIBLE);
//                }



            }
            else {
                if (!General.getSharedPreferences(getActivity(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                    dbHelper.save(DatabaseConstants.userRole, "Broker");
                    //show sign up screen if broker is not registered
                    Bundle bundle = new Bundle();
                    bundle.putString("lastFragment", "BrokerPreokFragment");
                    bundle.putString("JsonArray", jsonObjectArray.toString());
                    bundle.putInt("Position", selectedItemPosition);
                    Fragment fragment = null;
                    fragment = new SignUpFragment();
                    fragment.setArguments(bundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_map, fragment);
                    fragmentTransaction.commit();
                } else {
                    //here broker is registered
                    AcceptOkCall a = new AcceptOkCall();
                    a.setmCallBack(BrokerPreokFragment.this);
                    a.acceptOk(jsonObjectArray, selectedItemPosition, dbHelper, getActivity());
                }
            }
        }
        else if (deal.getId() == v.getId()) {
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
        option1Count.startAnimation(bounce);
        option2Count.startAnimation(bounce);
        rentalCount.startAnimation(bounce);
        resaleCount.startAnimation(bounce);
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {



        highs = chart.getHighlighted();

        highlights = new ArrayList<Highlight>(Arrays.asList(highs));

        if(highlights.contains(h)){
            highlights.remove(h);
            highs = highlights.toArray(new Highlight[highlights.size()]);
              chart.highlightValues(highs);
        } else{
            highlights.add(h);
            highs = highlights.toArray(new Highlight[highlights.size()]);
            chart.highlightValues(highs);
        }


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



/*
  Log.i("GRAPH","executed");


        if (!buildingsSelected.contains(e.getXIndex())) {
            Log.i("GRAPH","1");
            buildingsSelected.add(e.getXIndex());
            Log.i("GRAPH10", "buildings selected after add " + buildingsSelected);
            Log.i("GRAPH10", "buildings selected after add " + buildingsSelected.get(0));

            if (buildingsSelected.size() < 3) {

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
            } else if(buildingsSelected.size() == 3){
                Log.i("GRAPH","3");
                //cant be added
                Log.i("GRAPH30", "buildings selected " + buildingsSelected);
                Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
                Highlight h2 = new Highlight(buildingsSelected.get(2), 0);
                chart.highlightValues(new Highlight[]{h0, h1, h2});

            }
        }
        else {
            Log.i("GRAPH","2");
            Log.i("GRAPH","removed "+Integer.valueOf(e.getXIndex()));
            buildingsSelected.remove(Integer.valueOf(e.getXIndex()));
            Log.i("GRAPH","after removing "+buildingsSelected);
        //    for (int i = 0; i < buildingsSelected.size(); i++) {

            Log.i("GRAPH","size "+buildingsSelected.size());
                if (buildingsSelected.size() == 1) {
                    Log.i("GRAPH20", "buildings selected " + buildingsSelected);
                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                    chart.highlightValues(new Highlight[]{h0});

                } else if (buildingsSelected.size() == 2) {
                    Log.i("GRAPH21", "buildings selected " + buildingsSelected);
                    Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
                    Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
                    chart.highlightValues(new Highlight[]{h0, h1});


                }else if(buildingsSelected.size() == 0){
                    chart.highlightValue(-1,-1);
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



*/
    }


//        if(buildingsSelected.size()<3){
//            if(!buildingsSelected.contains(e.getXIndex())) {
//                buildingsSelected.add(e.getXIndex());
//
//                //chart.highlightValues(buildingsSelected);
//                //     Integer [] buildings = buildingsSelected.toArray(new Integer[buildingsSelected.size()]);
//                for (int i = 0; i < buildingsSelected.size(); i++) {
//
//                    if (buildingsSelected.size() == 1) {
//                        Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
//                        chart.highlightValues(new Highlight[]{h0});
//                    } else if (buildingsSelected.size() == 2) {
//                        Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
//                        Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
//                        chart.highlightValues(new Highlight[]{h0, h1});
//                    }else {
//                        Highlight h0 = new Highlight(buildingsSelected.get(0),0);
//                        Highlight h1 = new Highlight(buildingsSelected.get(1),0);
//                        Highlight h2 = new Highlight(buildingsSelected.get(2),0);
//                        chart.highlightValues(new Highlight[] {h0,h1,h2});
//                    }
//                    }
//
////                   Highlight h1 = new Highlight(0,0);// 1st value to highlight
////                    Highlight h2 = new Highlight(4,0);// 1st value to highlight
////                    Highlight h3 = new Highlight(8,0);// 1st value to highlight
////                   highlights.add(h1);
//                    //     highlights.add(new Highlight(buildingsSelected.get(i),0));
//
//
//                }
////                Highlight h1 = new Highlight(); // 1st value to highlight
////                Highlight h2 = new Highlight(); // 2nd value to highlight
//
//                //chart.highlightValues(new Highlight[] {h1, h2});
//
//            }       // chart.highlightValues(new buildings[] buildings);
//            else if(buildingsSelected.size()==3){
//                if(!buildingsSelected.contains(e.getXIndex())) {
// //only three can be selected
//
//                }
//                else{
//
//                    buildingsSelected.remove(e.getXIndex());
//
//                    for (int i = 0; i < buildingsSelected.size(); i++) {
//
//                        if(buildingsSelected.size()==1){
//                            Highlight h0 = new Highlight(buildingsSelected.get(0),0);
//                            chart.highlightValues(new Highlight[] {h0});
//                        }else if(buildingsSelected.size()==2) {
//                            Highlight h0 = new Highlight(buildingsSelected.get(0), 0);
//                            Highlight h1 = new Highlight(buildingsSelected.get(1), 0);
//                            chart.highlightValues(new Highlight[]{h0, h1});
//                        }
////                        }else {
////                            Highlight h0 = new Highlight(buildingsSelected.get(0),0);
////                            Highlight h1 = new Highlight(buildingsSelected.get(1),0);
////                            Highlight h2 = new Highlight(buildingsSelected.get(2),0);
////                            chart.highlightValues(new Highlight[] {h0,h1,h2});
////                        }
//                    }
//
//                }
//
//            }
//
//            else{
//                buildingsSelected.remove(e.getXIndex());
//
//            for (int i = 0; i < buildingsSelected.size(); i++) {
//
//                if(buildingsSelected.size()==1){
//                    Highlight h0 = new Highlight(buildingsSelected.get(0),0);
//                    chart.highlightValues(new Highlight[] {h0});
//                }else if(buildingsSelected.size()==2) {
//                    Highlight h0 = new Highlight(buildingsSelected.get(0),0);
//                    Highlight h1 = new Highlight(buildingsSelected.get(1),0);
//                    chart.highlightValues(new Highlight[] {h0,h1});
//                }else if(buildingsSelected.size()==3){
//                    Highlight h0 = new Highlight(buildingsSelected.get(0),0);
//                    Highlight h1 = new Highlight(buildingsSelected.get(1),0);
//                    Highlight h2 = new Highlight(buildingsSelected.get(2),0);
//                    chart.highlightValues(new Highlight[] {h0,h1,h2});
//                }
//            }
//            }
//
//        }


//        buildingsSelected.contains()




    @Override
    public void onNothingSelected() {


    }


}
