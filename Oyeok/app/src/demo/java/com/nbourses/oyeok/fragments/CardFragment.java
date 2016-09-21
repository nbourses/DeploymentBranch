package com.nbourses.oyeok.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.AutoOk;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.activities.ClientDealsListActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ritesh on 10/08/16.
 */
public class CardFragment  extends Fragment{
    private Button button;


    private Button signUp;

    private Button later;

    private ImageButton cardMaps;
    private CustomPhasedSeekBar mPhasedSeekBar;
    private FrameLayout cardFrame;
    private TextView locality;
    private CheckBox rental;
    private CheckBox buysell;
    private CheckBox tenant;
    private CheckBox owner;
    private CheckBox buyer;
    private CheckBox seller;
    private Boolean localitySet = false;
    private LinearLayout sh;
    private LinearLayout sh2;
    private LinearLayout hid;
    private LinearLayout hid2;
    private LinearLayout seekbar_linearlayout1;
    private TextView localityText;
    private String reqAvl;
    private String tt;
    Animation animFadein;
    Animation bounce;
    private LinearLayout localityLayout;
    private LinearLayout rentalPanel;
    private LinearLayout resalePanel;

    private BroadcastReceiver localityBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().getString("locality") != null) {
                String loc = intent.getExtras().getString("locality");
                Log.i("localityBroadcast", "localityBroadcast1 ");
                Log.i("localityBroadcast", "localityBroadcast " + locality);
                locality.setText(loc);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cardfragment1, container,
                false);
        button = (Button) rootView.findViewById(R.id.button);
        signUp = (Button) rootView.findViewById(R.id.signUp);
        later = (Button) rootView.findViewById(R.id.later);
        cardMaps = (ImageButton) rootView.findViewById(R.id.cardMaps);
       /* mPhasedSeekBar = (CustomPhasedSeekBar) rootView.findViewById(R.id.phasedSeekBar1);*/
        cardFrame = (FrameLayout) rootView.findViewById(R.id.cardMapFrame);
        locality = (TextView) rootView.findViewById(R.id.locality);
        rental = (CheckBox) rootView.findViewById(R.id.rental);
        buysell = (CheckBox) rootView.findViewById(R.id.buySell);
        tenant = (CheckBox) rootView.findViewById(R.id.tenant);
        owner = (CheckBox) rootView.findViewById(R.id.owner);
        buyer = (CheckBox) rootView.findViewById(R.id.buyer);
        seller = (CheckBox) rootView.findViewById(R.id.seller);

        sh = (LinearLayout) rootView.findViewById(R.id.sh);
        sh2 = (LinearLayout) rootView.findViewById(R.id.sh2);
        hid = (LinearLayout) rootView.findViewById(R.id.hid);
        hid2 = (LinearLayout) rootView.findViewById(R.id.hid2);
        seekbar_linearlayout1 = (LinearLayout) rootView.findViewById(R.id.seekbar_linearlayout1);
        localityText = (TextView) rootView.findViewById(R.id.localityText);
        localityLayout = (LinearLayout) rootView.findViewById(R.id.localityLayout);
        rentalPanel = (LinearLayout) rootView.findViewById(R.id.rentalPanel);
        resalePanel = (LinearLayout) rootView.findViewById(R.id.resalePanel);
        // Do something else
        init();
        return rootView;


    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(localityBroadcast, new IntentFilter(AppConstants.LOCALITY_BROADCAST));

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(localityBroadcast);


    }

    private void init() {
        animFadein = AnimationUtils.loadAnimation(getContext(),
                R.anim.rotate);
        bounce = AnimationUtils.loadAnimation(getContext(),
                R.anim.bounce);
        //cardMaps.startAnimation(animFadein);
        locality.setText(General.getSharedPreferences(getContext(), AppConstants.LOCALITY));
        DBHelper dbHelper = new DBHelper(getContext());
       /* if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getContext().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{getContext().getResources().getString(R.string.Rental), getContext().getResources().getString(R.string.Resale)}));
        else
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getContext().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.real_estate_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Audit", "Auction"}));
        mPhasedSeekBar.setListener(this);*/

        rental.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                  if(isChecked){
                                                      rentalPanel.clearAnimation();
                                                      resalePanel.clearAnimation();
                                                      resalePanel.setVisibility(View.GONE);
                                                      rentalPanel.setVisibility(View.VISIBLE);
                                                      rentalPanel.startAnimation(bounce);

                                                      /*localityLayout.clearAnimation();
                                                      localityLayout.setVisibility(View.GONE);

                                                      hid2.clearAnimation();
                                                      hid2.setVisibility(View.GONE);

                                                      hid.setVisibility(View.VISIBLE);
                                                      hid.startAnimation(bounce);*/

                                                      /*seekbar_linearlayout1.removeView(hid);
                                                      seekbar_linearlayout1.removeView(hid2);
                                                      sh.removeView(rental);
                                                      sh2.removeView(hid2);
                                                      if(buysell.isChecked())
                                                      sh2.addView(buysell);

                                                      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                                              sh2.getLayoutParams();
                                                      params.weight = 0.3f;
                                                      sh2.setLayoutParams(params);


                                                      sh.addView(hid);
                                                      LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)
                                                              sh.getLayoutParams();
                                                      params.weight = 0.7f;
                                                      sh.setLayoutParams(params1);
                                                      hid.setVisibility(View.VISIBLE);*/
                                                      buysell.setChecked(false);
                                                      buyer.setChecked(false);
                                                      seller.setChecked(false);

                                                  }else{
                                                      tenant.setChecked(false);
                                                      owner.setChecked(false);
                                                  }

                                              }
                                          }
        );

        buysell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                   if(isChecked){
                                                       rentalPanel.clearAnimation();
                                                       resalePanel.clearAnimation();
                                                       rentalPanel.setVisibility(View.GONE);
                                                       resalePanel.setVisibility(View.VISIBLE);
                                                       resalePanel.startAnimation(bounce);
                                                      /* localityLayout.clearAnimation();
                                                       localityLayout.setVisibility(View.GONE);
                                                       hid.clearAnimation();
                                                       hid.setVisibility(View.GONE);

                                                       hid2.setVisibility(View.VISIBLE);
                                                       hid2.startAnimation(bounce);*/

                                                       /*seekbar_linearlayout1.removeView(hid);
                                                       seekbar_linearlayout1.removeView(hid2);

                                                       sh2.removeView(buysell);
                                                       sh.removeView(hid);
                                                       if(rental.isChecked())
                                                       sh.addView(rental);
                                                       LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)
                                                               sh.getLayoutParams();
                                                       params1.weight = 0.3f;
                                                       sh.setLayoutParams(params1);

                                                       sh2.addView(hid2);

                                                       LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                                               sh2.getLayoutParams();
                                                       params.weight = 0.7f;
                                                       sh2.setLayoutParams(params);

                                                       hid2.setVisibility(View.VISIBLE);*/
                                                       rental.setChecked(false);
                                                       tenant.setChecked(false);
                                                       owner.setChecked(false);

                                                   }
                                                   else{
                                                       buyer.setChecked(false);
                                                       seller.setChecked(false);
                                                   }

                                               }
                                           }
        );

        tenant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                  if(isChecked){

                                                      owner.setChecked(false);
                                                      rental.setChecked(true);
                                                      showLocalityText();

                                                  }


                                              }
                                          }
        );

        owner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                  if(isChecked){

                                                      tenant.setChecked(false);
                                                      rental.setChecked(true);
                                                      showLocalityText();
                                                  }

                                              }
                                          }
        );

        seller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                  if(isChecked){

                                                      buyer.setChecked(false);
                                                      buysell.setChecked(true);
                                                      showLocalityText();

                                                  }

                                              }
                                          }
        );
        buyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                  if(isChecked){

                                                      seller.setChecked(false);
                                                      buysell.setChecked(true);
                                                      showLocalityText();

                                                  }

                                              }
                                          }
        );

        cardMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  BrokerMap brokerMap=new BrokerMap();
                //set arguments
                BrokerMap brokerMap = new BrokerMap();


                //loadFragment(d,null,R.id.container_Signup,"");
                brokerMap.setArguments(null);
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_up);

                fragmentTransaction.addToBackStack("cardMap");
                fragmentTransaction.replace(R.id.y, brokerMap);
                fragmentTransaction.commitAllowingStateLoss();
                localitySet = true;
                //loadFragment(brokerMap,null,R.id.container_Signup,"");


            }
        });
       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.container_Signup)).commit();
            }
        });*/
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.card)).commit();

                Intent intent = new Intent(AppConstants.DOSIGNUP);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

            }
        });
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

if(!(rental.isChecked() || buysell.isChecked())) {
    SnackbarManager.show(
            Snackbar.with(getContext())
                    .position(Snackbar.SnackbarPosition.TOP)
                    .text("Please select transaction type: Rental or BuySell.")
                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

}
               else if(!(tenant.isChecked() || owner.isChecked() || buyer.isChecked() || seller.isChecked())) {
                    SnackbarManager.show(
                            Snackbar.with(getContext())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .text("Please select transaction subtype.")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

                }
else if(!localitySet){
    SnackbarManager.show(
            Snackbar.with(getContext())
                    .position(Snackbar.SnackbarPosition.TOP)
                    .text("Please select your location of interest.")
                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
}
                else{
    //getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.container_Signup)).commit();
    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.card)).commit();

    autoOk();


}
            }
        });


    }

private void showLocalityText(){
    hid.clearAnimation();
    hid2.clearAnimation();
    hid.setVisibility(View.GONE);
    hid2.setVisibility(View.GONE);
    localityLayout.clearAnimation();
    localityLayout.setVisibility(View.VISIBLE);
    localityLayout.startAnimation(bounce);


}
    private void autoOk(){
        Log.i("AUTO OK CALLED","autook 1 is "+General.getSharedPreferences(getContext(),AppConstants.MY_LAT)+" "+General.getSharedPreferences(getContext(),AppConstants.MY_LNG)+" "+General.getSharedPreferences(getContext(), AppConstants.LOCALITY));
        if(rental.isChecked())
            tt = "LL";
        else
            tt = "OR";

        if(tenant.isChecked() || buyer.isChecked())
            reqAvl = "req";
        else
            reqAvl = "avl";
        AutoOk autoOk = new AutoOk();
        autoOk.setGcm_id(General.getSharedPreferences(getContext(),AppConstants.GCM_ID));
        autoOk.setLat(General.getSharedPreferences(getContext(),AppConstants.MY_LAT));
        autoOk.setLng(General.getSharedPreferences(getContext(),AppConstants.MY_LNG));
        autoOk.setUser_id(General.getSharedPreferences(getContext(),AppConstants.TIME_STAMP_IN_MILLI));
        autoOk.setPlatform("android");
        autoOk.setLocality(General.getSharedPreferences(getContext(), AppConstants.LOCALITY));
        autoOk.setEmail("ritesh@nexchanges1.com");
        autoOk.setReq_avl(reqAvl);
        autoOk.setTt(tt);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppConstants.SERVER_BASE_URL)
                .build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


        try {
            oyeokApiService.autoOk(autoOk, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    Log.i("AUTO OK CALLED","autook success "+General.getSharedPreferences(getContext(),AppConstants.TIME_STAMP_IN_MILLI));



                    JsonObject k = jsonElement.getAsJsonObject();
                    try {

                        Log.i("AUTOOK CALLED","autook success response "+response);

                        JSONObject ne = new JSONObject(k.toString());
//                        JSONObject neo = ne.getJSONObject("responseData");
//                        Log.i("AUTOOK CALLED","autook response "+neo);
                        Log.i("AUTOOK CALLED","autook response "+ne);
                        //Log.i("AUTOOK CALLED","autook responser "+ne.getJSONObject("responseData").getString("message"));
                     //  Log.i("AUTOOK CALLED","autook responser "+ne.getJSONObject("responseData").getJSONArray("ok_ids"));
                       // Log.i("AUTOOK CALLED","autook responser "+ne.getJSONObject("responseData").getJSONArray("ok_ids").toJSONArray());

                        TastyToast.makeText(getContext(), "We have connected you with 3 brokers in your area.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        TastyToast.makeText(getContext(), "Sign up to connect with 7 more brokers waiting for you.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);


                        /*Log.i("BROKER BUILDINGS CALLED","success ne "+ne);
                        Log.i("BROKER BUILDINGS CALLED","success ne "+ne.getJSONObject("responseData"));



                        Log.i("BROKER BUILDINGS CALLED","success ne "+ne.getJSONObject("responseData").getString("coupon"));
                        coupon.setText(ne.getJSONObject("responseData").getString("coupon"));*/
                       // getFragmentManager().popBackStack();


if(ne.getString("success").equalsIgnoreCase("true")){
    General.setSharedPreferences(getContext(),AppConstants.STOP_CARD,"yes");
}

                        Intent inten = new Intent(getContext(), ClientDealsListActivity.class);

                        startActivity(inten);

                    }
                    catch (JSONException e) {
                        Log.e("TAG","Something went wrong "+e.getMessage());
                        TastyToast.makeText(getContext(), "Something went wrong.", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
//                        TastyToast.makeText(getContext(), "Please signup to get connected with 10 brokers waiting for you", TastyToast.LENGTH_LONG, TastyToast.INFO);

                        Log.i("BROKER AUTOOK CALLED ","autook Failed "+e.getMessage());
                    }




                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("AUTOOK CALLED","coupon fail "+error);

                }
            });


        }
        catch (Exception e){
            Log.e("AUTO OK CALLED", e.getMessage());
        }




    }



}

