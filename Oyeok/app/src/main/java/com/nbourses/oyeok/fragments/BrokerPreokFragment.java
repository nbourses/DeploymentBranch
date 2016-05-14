package com.nbourses.oyeok.fragments;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class BrokerPreokFragment extends Fragment implements CustomPhasedListener, CircularSeekBarNew.imageAction, OnAcceptOkSuccess {

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
    private JSONArray jsonObjectArray;
    private int selectedItemPosition;
    private DBHelper dbHelper;


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



        if(General.getBadgeCount(getContext(),AppConstants.HDROOMS_COUNT)<=0)
            hdroomsCount.setVisibility(View.GONE);
        else {
            hdroomsCount.setVisibility(View.VISIBLE);
            hdroomsCount.setText(String.valueOf(General.getBadgeCount(getContext(), AppConstants.HDROOMS_COUNT)));
        }







        mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(),
                new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector},
                new String[]{"30", "15"},
                new String[]{"Rental", "Resale"
                }));
        mCustomPhasedSeekbar.setListener(this);

        txtPreviouslySelectedOption = txtOption1;
        txtPreviouslySelectedOption.setBackgroundResource(R.color.greenish_blue);

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

    @OnClick({R.id.txtOption1, R.id.txtOption2})
    public void onOptionClick(View v) {
        animatebadges();
        //okButton.setAnimation(zoomin);
        //okButton.setAnimation(zoomout);

        if (txtPreviouslySelectedOption != null)
            txtPreviouslySelectedOption.setBackgroundResource(R.color.colorPrimaryDark);

        txtPreviouslySelectedOption = (TextView) v;

        if (v.getId() == txtOption1.getId()) {
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
            displayOkText.setVisibility(View.GONE);
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
            rentText.setText("Rs "+jsonObjectArray.getJSONObject(position).getString("price")+" /m.");
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
                SnackbarManager.show(
                        com.nispok.snackbar.Snackbar.with(getActivity())
                                .position(Snackbar.SnackbarPosition.BOTTOM)
                                .text("Please select a deal")
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
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


}
