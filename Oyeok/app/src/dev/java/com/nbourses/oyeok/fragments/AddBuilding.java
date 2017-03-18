package com.nbourses.oyeok.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nbourses.oyeok.MyApplication;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.BrokerListingActivity;
import com.nbourses.oyeok.activities.BrokerMap;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.activities.MyPortfolioActivity;
import com.nbourses.oyeok.adapters.searchBuilding;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.SearchBuildingModel;
import com.nbourses.oyeok.models.UpdateBuildingRateModel;
import com.nbourses.oyeok.models.loadBuildingDataModel;
import com.nbourses.oyeok.realmModels.addBuildingRealm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class AddBuilding extends Fragment {

/*
    private OnFragmentInteractionListener mListener;*/

    public AddBuilding() {
        // Required empty public constructor
    }

private TextView Cancel,back,usertext;
    private View v;
    ListView listView1;
    EditText inputSearch1;
    searchBuilding adapter;
    private Realm realm;
    private ProgressBar progressBar;
    ImageView add;
    String name;
    private TextView dialog;
    LinearLayout add_b;
    private SideBar sideBar;
    private Timer clockTickTimer;
    int count=3;
//    String name;
    ArrayList<loadBuildingDataModel> building_names;


    Thread thread;


    String Entry_point="";

    Bundle b;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v=inflater.inflate( R.layout.fragment_add_building, container, false );
        Cancel=(TextView)v.findViewById(R.id.Cancel);
        back=(TextView)v.findViewById(R.id.back);
        listView1=(ListView) v.findViewById(R.id.listView1);
        inputSearch1=(EditText)v.findViewById(R.id.inputSearch1);
        add=(ImageView) v.findViewById(R.id.add);
        add_b=(LinearLayout)v.findViewById(R.id.add_b);
        usertext=(TextView)v.findViewById(R.id.usertext);
        progressBar=(ProgressBar)v.findViewById(R.id.loadbuilding);
        building_names= new ArrayList<>();
        b=new Bundle();
        b=getArguments();
        if(b!=null&&b.containsKey("add_listing")) {
            Entry_point = b.getString("add_listing");
            Log.i("Entry_point", "Entry_point read   :"+ Entry_point);
            // Entry_point=b.getString("listing_id");

        }
        MyApplication application = (MyApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();

        mTracker.setScreenName("SearchBuilding");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);
                if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("Listing")){
                    ((BrokerListingActivity) getActivity()).closeCardContainer();
                    ((BrokerListingActivity) getActivity()).openAddListing();
                }else
                if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("portfolio")){
                    ((MyPortfolioActivity) getActivity()).closeCardContainer();
                    ((MyPortfolioActivity) getActivity()).openAddListing();
                }else
                if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                    ((BrokerMap) getActivity()).closeCardContainer();
                    ((BrokerMap)getActivity()).openAddListing();
                }else{

                    ((ClientMainActivity)getActivity()).closeAddBuilding();
                    ((ClientMainActivity)getActivity()).openAddListing();
                }


            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);
                AppConstants.PROPERTY="Home";
                if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("Listing")){
                    ((MyPortfolioActivity) getActivity()).closeCardContainer();
                }else
                if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("portfolio")){
                    ((MyPortfolioActivity) getActivity()).closeCardContainer();
                }else
                if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                    ((BrokerMap) getActivity()).closeCardContainer();
                }else{

                    ((ClientMainActivity)getActivity()).closeAddBuilding();

                }
            }
        });


        inputSearch1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                if(TT=="LL"){
                    Log.i( "portfolio","onTextChanged  LL : "+cs );
                name=String.valueOf(cs);
                    usertext.setText("'"+cs+"'");

                count=3;


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                Log.i("magic","beforeTextChanged  : ");


            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                name=String.valueOf(arg0);
                progressBar.setVisibility(View.VISIBLE);
//                SearchBuilding();
                thread=  new Thread(){
                    @Override
                    public void run(){
                        try {
                            synchronized(this){
                                wait(3000);
                            }
                        }
                        catch(InterruptedException ex){
                        }
                        SearchBuilding();
                        // TODO
                    }
                };

                thread.start();

            }
        });

        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((ClientMainActivity)getActivity()).closeAddBuilding();

                if(inputSearch1.getText().toString().equalsIgnoreCase("")) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Empty Text")
                            .setMessage("Please Type Your Building Name.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete

                                }
                            })
                        /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })*/
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }else {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);
                    General.setSharedPreferences(getContext(), AppConstants.BUILDING_NAME,name);

                   // General.setSharedPreferences(getContext(), AppConstants.ADD_TYPE,"building");
                    if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("Listing")){
                        // ((MyPortfolioActivity) getActivity()).closeCardContainer();
                            General.setSharedPreferences(getContext(), AppConstants.CALLING_ACTIVITY, "PC");
                            Intent in = new Intent(getContext(), BrokerMap.class);
                            in.putExtra("name_build",name);
                            in.putExtra("add_listing","Listing");//add_listing
                            startActivity(in);

                    }else
                    if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("portfolio")){
                       // ((MyPortfolioActivity) getActivity()).closeCardContainer();
                        if (General.getSharedPreferences(getContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        {
                           General.setSharedPreferences(getContext(), AppConstants.CALLING_ACTIVITY, "PC");
                           Intent in = new Intent(getContext(), ClientMainActivity.class);
                            in.putExtra("name_build",name);
                            in.putExtra("add_listing","portfolio");//add_listing
                            in.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           startActivity(in);
                        } else {
                            General.setSharedPreferences(getContext(), AppConstants.CALLING_ACTIVITY, "PC");
                            Intent in = new Intent(getContext(), BrokerMap.class);
                            in.putExtra("name_build",name);
                            in.putExtra("add_listing","portfolio");//add_listing

                                    startActivity(in);
                        }
                    }else
                    if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                        ((BrokerMap) getActivity()).closeCardContainer();
                        ((BrokerMap) getActivity()).setlocation(name);
                    }else{


                    ((ClientMainActivity) getActivity()).setlocation(name);
                    }
                }

            }
        });


         init();

        return v;
    }





   private void  init(){
     /* listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {

                *//* General.setSharedPreferences(getContext(),AppConstants.BUILDING_NAME,adapter.getItem(position).getBuilding_name());
                 General.setSharedPreferences(getContext(),AppConstants.BUILDING_LOCALITY,adapter.getItem(position).getLocality());
                 General.setSharedPreferences(getContext(),AppConstants.MY_LAT,adapter.getItem(position).getLat());
                 General.setSharedPreferences(getContext(),AppConstants.MY_LNG,adapter.getItem(position).getLng());*//*
//                 General.setSharedPreferences(getContext(),AppConstants.PROPERTY,adapter.getItem(position).getProperty_type());



                 ((ClientMainActivity)getActivity()).openAddListingFinalCard();
             }
          }
      });*/
   }

    public void SearchBuilding()
    {
        Log.i("updateStatus CALLED","updateStatus success called ");
        SearchBuildingModel searchBuildingModel = new SearchBuildingModel();
        searchBuildingModel.setBuilding(name);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_101).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
//        UserApiService userApiService = restAdapter.create(UserApiService.class);
        /*userApiService.addBuildingRealm(AddBuildingModel, new retrofit.Callback<JsonElement>() {*/

        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


        try {
            oyeokApiService.SearchBuilding(searchBuildingModel, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.i("magic1","addBuildingRealm success ");
                    JsonObject k = jsonElement.getAsJsonObject();
                    try {
                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
//                        Log.e(TAG, "RETROFIT SUCCESS " + getPrice.getResponseData().getPrice().getLlMin().toString());
                         JSONObject jsonResponse = new JSONObject(strResponse);
//                        JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));
                        String errors = jsonResponse.getString("errors");
                        Log.i("magic","addBuildingRealm success response "+response);
                        Log.i("magic","addBuildingRealm success jsonResponse "+jsonResponse);
                        JSONArray buildings = new JSONArray(jsonResponse.getString("responseData"));
                        JSONObject ne = new JSONObject(k.toString());
//                        General.setSharedPreferences(getContext(),AppConstants.token,ne.getString("token"));
//                        setDealStatus3(getContext());
                        int size= buildings.length();
                        Log.i("magic","addBuildingRealm success ne "+ne);
                        Log.i("magic","addBuildingRealm success buildings "+size+"  "+buildings);
                        building_names.clear();
//                        List<String> building_names = new ArrayList<String>();
                        for(int i=0;i<size;i++){
                            JSONObject j = new JSONObject(buildings.get(i).toString());
                            double lat = Double.parseDouble(j.getJSONArray("loc").get(1).toString());

                            double longi = Double.parseDouble(j.getJSONArray("loc").get(0).toString());
                            Log.i("Buildingdata", "lat " + lat+"longi:  "+longi+"id:  "+j.getString("id")+"name: "+j.getString("name"));
                            building_names.add(new loadBuildingDataModel(j.getString("name"),lat,longi,j.getString("id"),j.getString("locality"),j.getString("city"),j.getString("ll_pm"),j.getString("or_psf")));

                        }
                        try {
                            adapter= new searchBuilding(building_names,getContext());
                            listView1.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                                    loadBuildingDataModel dataModel= building_names.get(position);
                                    General.setSharedPreferences(getContext(),AppConstants.BUILDING_NAME,adapter.getItem(position).getName());
                                    General.setSharedPreferences(getContext(),AppConstants.BUILDING_LOCALITY,adapter.getItem(position).getLocality()+"");
                                    General.setSharedPreferences(getContext(),AppConstants.MY_LAT,adapter.getItem(position).getLat()+"");
                                    General.setSharedPreferences(getContext(),AppConstants.MY_LNG,adapter.getItem(position).getLng()+"");
                                    General.setSharedPreferences(getContext(), AppConstants.MY_CITY,adapter.getItem(position).getCity());
                                    General.setSharedPreferences(getContext(), AppConstants.LL_PM,adapter.getItem(position).getLl_pm());
                                    General.setSharedPreferences(getContext(), AppConstants.OR_PSF,adapter.getItem(position).getOr_psf());
                                    General.setSharedPreferences(getContext(), AppConstants.ADD_TYPE,"listing");
                                    General.setSharedPreferences(getContext(),AppConstants.BUILDING_ID,adapter.getItem(position).getId());
                                    Log.i("Buildingdata", "lat " +adapter.getItem(position).getLl_pm()+" ====== "+adapter.getItem(position).getOr_psf());//ADD_TYPE
                                    //General.setSharedPreferences(getContext(),AppConstants.PROPERTY,adapter.getItem(position).getProperty_type());
                                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);
                                    if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("Listing")){
                                        ((BrokerListingActivity) getActivity()).openAddListingFinalCard();
                                    }else
                                    if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("portfolio")){
                                        ((MyPortfolioActivity) getActivity()).openAddListingFinalCard();
                                    }else
                                    ((BrokerMap)getActivity()).openAddListingFinalCard();
                                }else{

                                    AddbuildingAPICall(adapter.getItem(position).getName(),adapter.getItem(position).getLat() + "",adapter.getItem(position).getLng() + "",adapter.getItem(position).getId()+"",adapter.getItem(position).getLocality(),adapter.getItem(position).getCity());
                                }

                            }
                        });


                    }
                    catch (JSONException e) {
                        Log.e("TAG", e.getMessage());
                        Log.i("magic","addBuildingRealm Failed1 "+e.getMessage());
                    }




                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("magic","addBuildingRealm failed 2: "+error);
                }
            });


        }
        catch (Exception e){
            Log.e("TAG", "Caught in the the : "+ e.getMessage());
        }

    }



    public void AddbuildingAPICall(final String name,final String lat,final String longi,final String b_id,final String locality,String city) {

        Log.i("updateStatus CALLED", "updateStatus success called ");
        UpdateBuildingRateModel updateBuildingRateModel =new UpdateBuildingRateModel();
        updateBuildingRateModel.setBuilding(name);
        updateBuildingRateModel.setLat(lat);
        updateBuildingRateModel.setCity(city);
        updateBuildingRateModel.setLongiute(longi);
        updateBuildingRateModel.setBuilding_id(b_id);
        updateBuildingRateModel.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
        Log.i("user_id","======== "+General.getSharedPreferences(getContext(),AppConstants.USER_ID));
        updateBuildingRateModel.setUser_role(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER));
        updateBuildingRateModel.setLocality(locality);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_101).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        try {
            oyeokApiService.updateBuildingData(updateBuildingRateModel, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                        try {
                            JSONObject jsonResponse = new JSONObject(strResponse);
                            Log.i("magic1","addBuildingRealm success response "+response+"\n"+jsonResponse);
                            JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                            Log.i("magic1","addBuildingRealm success response "+building);
//                            Log.i("magic1","addBuildingRealm success response "+building.getString("rate_growth")+" 2."+building.getString("or_psf")+" 2."+building.getString("ll_pm"));

                            for(int i=0;i<2;i++) {
                                Realm myRealm = General.realmconfig(getContext());
                                addBuildingRealm add_Building = new addBuildingRealm();
                                add_Building.setTimestamp(String.valueOf(SystemClock.currentThreadTimeMillis()));
                                add_Building.setBuilding_name(name);
                                add_Building.setType("ADD");

                                add_Building.setAddress(" Mumbai");
                                add_Building.setConfig(General.getSharedPreferences(getContext(), AppConstants.PROPERTY_CONFIG));
                                add_Building.setProperty_type(AppConstants.PROPERTY);
                                add_Building.setLat(lat);
                                add_Building.setLng(longi);
                                add_Building.setSublocality(locality);
                                add_Building.setGrowth_rate(building.getString("rate_growth"));
                                add_Building.setDisplay_type(null);
                                add_Building.setId(b_id);
                                add_Building.setLl_pm(Integer.parseInt(building.getString("ll_pm")));
                                add_Building.setOr_psf(Integer.parseInt(building.getString("or_psf")));
                                if(AppConstants.TT_TYPE.equalsIgnoreCase("ll")){
                                    add_Building.setTt("ll");
                                   // General.setBadgeCount(getContext(),AppConstants.ADDB_COUNT_LL,General.getBadgeCount(getContext(),AppConstants.ADDB_COUNT_LL)+1);
                                }else{
                                    //add_Building.setId(b_id+"1");
                                    add_Building.setTt("or");
                                   // General.setBadgeCount(getContext(),AppConstants.ADDB_COUNT_OR,General.getBadgeCount(getContext(),AppConstants.ADDB_COUNT_OR)+1);

                                }
                                if(myRealm.isInTransaction())
                                    myRealm.cancelTransaction();
                                myRealm.beginTransaction();
                                myRealm.copyToRealmOrUpdate(add_Building);
                                myRealm.commitTransaction();
                            }
                            AppConstants.PROPERTY="Home";
                            if(AppConstants.SETLOCATIONOWNERQ3){
                                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.card)).commit();

                            }else {
                                ((MyPortfolioActivity) getActivity()).closeCardContainer();
                                getActivity().recreate();
                            }
//                            Intent in = new Intent(getContext(), MyPortfolioActivity.class);
//                            startActivity(in);
                        } catch (JSONException e) {e.printStackTrace();}

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        }catch (Exception e){}

    }





    private void lockedTimer() {
        if(clockTickTimer!=null){
            clockTickTimer.cancel();
            count = 3;
        }
        clockTickTimer = new Timer();
        clockTickTimer.schedule(new TimerTask() {
            @Override
            public void run () {

                new Thread( new Runnable() {
                    @Override
                    public void run() {
                        if(count==0){
                            SearchBuilding();
                            count = 3;

                            clockTickTimer.cancel();
                        }else{
                            --count;
                        }
                    }
                });

            }
        },0,100);

    }



}