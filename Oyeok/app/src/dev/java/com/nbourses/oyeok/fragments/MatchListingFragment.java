package com.nbourses.oyeok.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.ProfileActivity;
import com.nbourses.oyeok.adapters.MatchListingAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.ActivityLog;
import com.nbourses.oyeok.models.MatchListing;
import com.nbourses.oyeok.models.MatchingOK;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchListingFragment extends Fragment {

    @Bind(R.id.mrview)
    RecyclerView mrview;

    @Bind(R.id.okFab)
    FloatingActionButton okFab;

    @Bind(R.id.ok)
    ImageButton ok;

    @Bind(R.id.root_broker_name)
    TextView root_broker_name;

    @Bind(R.id.root_config)
    TextView root_config;

    @Bind(R.id.root_date)
    TextView root_date;

    @Bind(R.id.root_growth_rate)
    TextView root_growth_rate;

    @Bind(R.id.root_growth_image)
    ImageView root_growth_image;

    @Bind(R.id.root_locality)
    TextView root_locality;

    @Bind(R.id.root_price)
    TextView root_price;

    @Bind(R.id.connectOk)
    CardView connectOk;


    private Timer timer;
private String oye_id;


    private String locality = "";
    private String growth_rate = "";
    private String config = "";
    private String price = "";
    private String broker_name = "";
    private String date = "";
    private MatchListingAdapter mAdapter;
    private List<MatchListing> matchList = new ArrayList<>();
    public MatchListingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_match_listing, container, false);
        ButterKnife.bind(this, v);
        mrview.setHasFixedSize(true);



        mrview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MatchListingAdapter(getContext(),v, matchList);
        mrview.setAdapter(mAdapter);

        Bundle bundle = this.getArguments();
        AppConstants.oyeIdsForOk.clear();
if(bundle != null){
    if(bundle.containsKey("oye_id"))
        oye_id = bundle.getString("oye_id");

    if(bundle.containsKey("config")) {
        config = bundle.getString("config");
        root_config.setText(config);

    }

    if(bundle.containsKey("growth_rate")) {
        growth_rate = bundle.getString("growth_rate");
        root_growth_rate.setText(growth_rate);
    }

    if(bundle.containsKey("price")) {
        price = bundle.getString("price");
        root_price.setText(General.currencyFormat(price));
    }

    if(bundle.containsKey("date")) {
        date = bundle.getString("date");
        root_date.setText(date);
    }

    if(bundle.containsKey("locality")) {
        locality = bundle.getString("locality");
        root_locality.setText(locality);
    }

    if(bundle.containsKey("broker_name")) {
        broker_name = bundle.getString("broker_name");
        root_broker_name.setText(broker_name);
    }


}
       /* if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("illllla","illllla");



                                ok.setVisibility(View.VISIBLE);
                                ok.setAnimation(slide_arrow);

                            }
                        });
                    }
                }
            }, 500, 500);

        }
*/

mrview.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

    }
});



        connectOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

       loadMatchings();



        return v;
    }

    private void loadMatchings(){
        try {
            if(General.isNetworkAvailable(getContext())) {
            MatchingOK m = new MatchingOK();
            m.setOye_id(/*oye_id*/"yi5wizfqz151211");
            m.setGcm_id(General.getSharedPreferences(getContext(), AppConstants.GCM_ID));
            m.setPage("1");
            m.setPlatform("Android");
            m.setUser_id(/*General.getSharedPreferences(getContext(), AppConstants.USER_ID)*/"te82bto1rwmk42mz5a25q2tzo4zocelr");

            Gson gson = new Gson();
            String json = gson.toJson(m);
            Log.i("magic","loadMatchings  json "+json);




            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstants.SERVER_BASE_URL_1)
                    .build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
            try {
                oyeokApiService.matchingOk(m, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Log.i("MATCHINGOK CALLED", "MATCHINGOK success");
                        General.slowInternetFlag = false;
                        General.t.interrupt();

                        try {

                            String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                            JSONObject ne = new JSONObject(strResponse);
                            Log.i("MATCHINGOK CALLED", "matchingok success "+ne);


                                Log.i("MATCHINGOK CALLED", "matchingok success 2" + ne.getJSONObject("responseData").getJSONArray("listings"));
                            JSONArray j = ne.getJSONObject("responseData").getJSONArray("listings");
                          for(int i=0;i<j.length();i++){
                              JSONObject k = j.getJSONObject(0);
                              String config = "";

                              config = k.getString("req_avl").toUpperCase()+" ("+k.getString("property_subtype").toUpperCase()+") "+k.getString("furnishing").toUpperCase()+"@ "+General.currencyFormat(k.getString("price")).toUpperCase();


                              MatchListing item = new MatchListing(k.getString("oye_id"),config,"Broker Name",k.getString("locality"),k.getString("possession_date"),k.getString("price"),"-6");


                              matchList.add(item);

                              getActivity().runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {

                                      mAdapter.notifyDataSetChanged();
                                  }
                              });


                          }




                        } catch (JSONException e) {
                            Log.i("MATCHINGOK CALLED", "caught in exception inside preok"+e);

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("MATCHINGOK CALLED", "Caught in exception preok " + error);
                        try {
                            SnackbarManager.show(
                                    Snackbar.with(getContext())
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .text("Server Error: " + error.getMessage())
                                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                        }
                        catch(Exception e){}
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                    }
                });
            } catch (Exception e) {
                Log.e("TAG", e.getMessage());
            }

        }else{

            try {
                if (!General.isNetworkAvailable(getContext())) {

                    SnackbarManager.show(
                            Snackbar.with(getContext())
                                    .position(Snackbar.SnackbarPosition.BOTTOM)
                                    .text("You are offline.")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                }
            }
            catch(Exception e){

            }

        }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void loadData() {
        for(int i =0; i < 10; i++){
           /* MatchListing item = new MatchListing("Hi Ritesh "+i,i+") Howdy? This is item "+i);

            Log.i("MatchListing","matchwa "+i);
            matchList.add(item);*/

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mAdapter.notifyDataSetChanged();
                }
            });

        }
    }
}

