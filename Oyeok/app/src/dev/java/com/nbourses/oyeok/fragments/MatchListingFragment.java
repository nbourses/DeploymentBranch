package com.nbourses.oyeok.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.ClientDealsListActivity;
import com.nbourses.oyeok.activities.ProfileActivity;
import com.nbourses.oyeok.adapters.MatchListingAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.AutoOkCall;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.ActivityLog;
import com.nbourses.oyeok.models.GetLocality;
import com.nbourses.oyeok.models.MatchListing;
import com.nbourses.oyeok.models.MatchingOK;
import com.nbourses.oyeok.models.OkAccept;
import com.nbourses.oyeok.realmModels.Localities;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
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

    @Bind(R.id.root_match_icon)
    ImageView root_match_icon;

    @Bind(R.id.root)
    LinearLayout root;

    @Bind(R.id.matching_text)
    TextView matching_text;



    private Timer timer;
    private String oye_id;
    private String req_avl = "REQ";


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

   /* if(bundle.containsKey("config")) {
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
    }*/


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



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("percent","okaccept called 3 :");
                              okaccept();
            }
        });

       loadMatchings();



        return v;
    }

    private void loadMatchings(){
        try {
            if(General.isNetworkAvailable(getContext())) {
            MatchingOK m = new MatchingOK();
            m.setOye_id(oye_id/*"r9x5oxid488741"*/);
            m.setGcm_id(General.getSharedPreferences(getContext(), AppConstants.GCM_ID));
            m.setPage("1");
            m.setPlatform("Android");
                if(!General.getSharedPreferences(getContext(),AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase(""))
                    m.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
                else
                    m.setUser_id(General.getSharedPreferences(getContext(),AppConstants.TIME_STAMP_IN_MILLI));
            //m.setUser_id(General.getSharedPreferences(getContext(), AppConstants.USER_ID)/*"te82bto1rwmk42mz5a25q2tzo4zocelr"*/);

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
                            Log.i("MATCHINGOK CALLED", "loadMatchings matchingok success "+ne);


                                Log.i("MATCHINGOK CALLED", "matchingok success 2" + ne.getJSONObject("responseData"));
                            JSONObject jo = ne.getJSONObject("responseData").getJSONObject("oye_details");

                            config = jo.getString("req_avl").toUpperCase()+" ("+jo.getString("property_subtype").toUpperCase()+") "+jo.getString("furnishing").toUpperCase()+"@ "+General.currencyFormat(jo.getString("price")).toUpperCase();
                            root_config.setText(config);

//                            growth_rate = jo.getString("rate_growth");

                            growth_rate = General.percentageCalculator(Integer.parseInt(jo.getString("price")),Integer.parseInt(jo.getString("real_price")));
                            if (Integer.parseInt(growth_rate) < 0) {

                                root_growth_image.setImageResource( R.drawable.sort_down_red );
                                root_growth_rate.setTextColor( Color.parseColor("#ffb91422"));
                                root_growth_rate.setText( (growth_rate).subSequence( 1, (growth_rate).length() ) + "%" );

                            } else if (Integer.parseInt( growth_rate ) > 0) {

                                root_growth_image.setImageResource( R.drawable.sort_up_green );
                                root_growth_rate.setTextColor( Color.parseColor("#2dc4b6"));
                                root_growth_rate.setText( (growth_rate).subSequence( 1, (growth_rate).length() ) + "%" );

                            } else {
                                root_growth_image.setImageResource( R.drawable.sort_up_black );
                                root_growth_rate.setTextColor( Color.parseColor("black") );
                                root_growth_rate.setText( growth_rate + "%" );
                            }

                            price = jo.getString("real_price");
                            root_price.setText(General.currencyFormat(price));


                            try {
                                date = jo.getString("possession_date");
                                root_date.setText(General.timestampToString(Long.parseLong(date)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                date = jo.getString("possession_date");
                                root_date.setText(date);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                date = jo.getString("possession_date");
                                root_date.setText(date);
                            }

                            locality = jo.getString("oye_locality");
                            root_locality.setText(locality);

                            broker_name = jo.getString("name");
                            root_broker_name.setText(broker_name);


                          if(jo.getString("req_avl").equalsIgnoreCase("REQ")) {

                              root_match_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.svg_icon_req));


                            }
                            else {
                                req_avl = "AVL";
                                if(jo.getString("property_type").equalsIgnoreCase("home"))
                                    root_match_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_home));
                                else if(jo.getString("property_type").equalsIgnoreCase("office"))
                                    root_match_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_office));
                                else if(jo.getString("property_type").equalsIgnoreCase("shop"))
                                    root_match_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_shop));
                                else if(jo.getString("property_type").equalsIgnoreCase("office"))
                                    root_match_icon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_office));
                            }

                            root.setVisibility(View.VISIBLE);
                            JSONArray j = ne.getJSONObject("responseData").getJSONArray("listings");
                            if(j.length() == 0)
                                matching_text.setText("No Matchings Found!");
                            else
                                matching_text.setText(j.length()+" Matchings Found");
                          for(int i=0;i<j.length();i++){
                              try {
                                  JSONObject k = j.getJSONObject(i);
                                  MatchListing item;
                                  String config = "";


                                  if(jo.getString("tt").equalsIgnoreCase("LL")) {
                                      config = k.getString( "req_avl").toUpperCase()+" ("+k.getString("config").toUpperCase()+") "+k.getString("furnishing").toUpperCase()+"@ "+General.currencyFormat(k.getString("listed_ll_pm")).toUpperCase();

                                      item = new MatchListing(k.getString("user_id"),k.getString("property_type"), req_avl, k.getString("oye_id"), config, k.getString("user_name"), k.getString("locality"), k.getString("possession_date"), k.getString("real_ll_pm"), percentageCalculator(Integer.parseInt(k.getString("listed_ll_pm")), Integer.parseInt(k.getString("real_ll_pm"))));

                                  }else {
                                      config = k.getString( "req_avl").toUpperCase()+" ("+k.getString("config").toUpperCase()+") "+k.getString("furnishing").toUpperCase()+"@ "+General.currencyFormat(k.getString("listed_or_psf")).toUpperCase();

                                      item = new MatchListing(k.getString("user_id"),k.getString("property_type"), req_avl, k.getString("oye_id"), config, k.getString("user_name"), k.getString("locality"), k.getString("possession_date"), k.getString("real_or_psf"), percentageCalculator(Integer.parseInt(k.getString("listed_or_psf")), Integer.parseInt(k.getString("real_ll_pm"))));

                                  }
                                  matchList.add(item);

                                  getActivity().runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {

                                          mAdapter.notifyDataSetChanged();
                                      }
                                  });
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              } catch (NumberFormatException e) {
                                  e.printStackTrace();
                              }

                          }




                        } catch (JSONException e) {
                            Log.i("MATCHINGOK CALLED", "caught in exception inside preok"+e);

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        matching_text.setText("No Matchings Found.");
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

    String percentageCalculator(int selectedRate,int MarketRate){
        int Percentage;
        int diff;
        String str="";


        // Log.i("percent","selectedRate : "+selectedRate+"MarketRate : "+MarketRate+"Percentage : "+Percentage);
        if(selectedRate>MarketRate){
            str="+";
            diff=selectedRate-MarketRate;

        }else{
            str="-";
            diff=MarketRate-selectedRate;
        }
        Percentage=(diff*100)/MarketRate;
        Log.i("percent","selectedRate : "+selectedRate+"MarketRate : "+MarketRate+"Percentage : "+Percentage+"str : "+str);
        return str+Percentage+"";
    }

    private void okaccept(){

        try {
            Log.i("percent","okaccept called :");

           // {"user_id":"14dlr9x50bbuq1ptgkbb0lfh26f7y6no","long":"72","lat":"19","user_role":"broker","gcm_id":"gyani","oye_id":"xu4susi2y0852992","listing":"1"}
            OkAccept o = new OkAccept();
            o.setOye_id(oye_id);
            if(!General.getSharedPreferences(getContext(),AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase(""))
                o.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
            else
                o.setUser_id(General.getSharedPreferences(getContext(),AppConstants.TIME_STAMP_IN_MILLI));

            o.setLng(General.getSharedPreferences(getContext(),AppConstants.MY_BASE_LNG));
            o.setLat(General.getSharedPreferences(getContext(),AppConstants.MY_BASE_LAT));
            o.setUser_role("broker");

            o.setGcm_id(General.getSharedPreferences(getContext(),AppConstants.GCM_ID));
            o.setListings(AppConstants.oyeIdsForOk);

            // g.setProperty_type(ptype.getText().toString());

            Gson gson = new Gson();
            String json = gson.toJson(o);
            Log.i("magic","okAccept  json "+json);

            Log.i("magic","okAccept 1");
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_1).build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
            Log.i("magic","okAccept 2");

            oyeokApiService.okAccept(o, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    try {
                        Log.i("magic", "okAccept");
                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                        JSONObject jsonResponse = new JSONObject(strResponse);

                        Log.i("magic", "okAccept " + jsonResponse);


                        if (jsonResponse.getString("success").equalsIgnoreCase("false")) {


                            if(jsonResponse.getJSONObject("responseData").getString("message").equalsIgnoreCase("Oye user unavailable")){
                                TastyToast.makeText(getContext(), jsonResponse.getJSONObject("responseData").getString("message"), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            }
                            else
                                TastyToast.makeText(getContext(), jsonResponse.getJSONObject("responseData").getString("message"), TastyToast.LENGTH_LONG, TastyToast.INFO);

                            Intent openDealsListing = new Intent(getContext(), ClientDealsListActivity.class);
                            openDealsListing.putExtra("serverMessage", jsonResponse.getJSONObject("responseData").getString("message"));
                            Log.i("TRACEOK", "serverMessage " + jsonResponse.getJSONObject("responseData").getString("message"));
                            Log.i("TRACEBROKERSIGNUP","3");

                            startActivity(openDealsListing);
                            AppConstants.MATCHINGOKFLAG = false;




                        } else {
                            Log.i("magic", "okaccept success " + jsonResponse.getJSONObject("responseData"));
                            JSONObject j = jsonResponse.getJSONObject("responseData");
                            Log.i("magic", "rollonar 1 " + jsonResponse.getJSONObject("responseData"));
                            Intent openDealsListing = new Intent(getContext(), ClientDealsListActivity.class);
                            openDealsListing.putExtra("oyeok "+AppConstants.OK_ID, j.getString("ok_id"));
                            displayDefaultMessageB(getContext(),j.getString("ok_id"));

                            openDealsListing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(openDealsListing);
                            AppConstants.MATCHINGOKFLAG = false;
                        }

                    }catch(Exception e){
                        Log.e("TAG", "Caught in the exception getLocality 1" + e.getMessage());

                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("magic","getLocality failed 2 "+error);
                    try {
                        SnackbarManager.show(
                                Snackbar.with(getContext())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text("Server Error: " + error.getMessage())
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                    }
                    catch(Exception e){}

                }
            });


        }
        catch (Exception e){
            Log.e("TAG", "Caught in the exception getLocality"+ e.getMessage());
        }

    }

    private void displayDefaultMessageB(Context context, String channelName){
        try {
            PubNub pubnub = General.initPubnub(context, General.getSharedPreferences(context, AppConstants.USER_ID));

            final JSONObject jsonMsg = new JSONObject();
            jsonMsg.put("_from", General.getSharedPreferences(context, AppConstants.USER_ID));
            jsonMsg.put("to", channelName);
            jsonMsg.put("timetoken", String.valueOf(System.currentTimeMillis()));

            jsonMsg.put("name", "");
            jsonMsg.put("message", "Client have initiated enquiry for " + General.getSharedPreferences(context, AppConstants.PTYPE).substring(0, 1).toUpperCase() + General.getSharedPreferences(context, AppConstants.PTYPE).substring(1)
                    + " property (" + General.getSharedPreferences(context, AppConstants.PSTYPE) + ") within budget " + General.currencyFormat(General.getSharedPreferences(context, AppConstants.PRICE)) + ".");

            jsonMsg.put("status", "OKS");

            Map message = new HashMap();

            message.put("pn_gcm", new HashMap(){{put("data",new HashMap(){{put("message",jsonMsg.getString("message")); put("_from",jsonMsg.getString("_from"));put("to",jsonMsg.getString("to"));put("name",jsonMsg.getString("name")); put("status",jsonMsg.getString("status"));}});}});
            message.put("pn_apns", new HashMap(){{put("aps",new HashMap(){{put("alert",jsonMsg.getString("message")); put("_from",jsonMsg.getString("_from"));put("to",jsonMsg.getString("to"));put("name",jsonMsg.getString("name")); put("status",jsonMsg.getString("status"));}});}});


            Log.i("TAG","Message is "+message);
            Log.i("magic", "rollonar 2 " + message);

            pubnub.publish()
                    .message(message)
                    .shouldStore(true)
                    .usePOST(true)
                    .channel(channelName)
                    .async(new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            if (status.isError()) {
                                System.out.println("publish failed!");
                                Log.i("TAG","PUBNUB publish "+status.getStatusCode());
                            } else {
                                System.out.println("push notification worked!");
                                System.out.println(result);
                            }
                        }
                    });


        }
        catch(Exception e){

        }

    }
}

