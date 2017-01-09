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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
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
//        adapter = new addBuildingAdapter(getContext(),1);
        /*listView1.setAdapter(adapter);
        realm = General.realmconfig(getContext());
        adapter.setResults(realm.where(addBuildingRealm.class).findAll());*/
        add_b=(LinearLayout)v.findViewById(R.id.add_b);
        usertext=(TextView)v.findViewById(R.id.usertext);
        progressBar=(ProgressBar)v.findViewById(R.id.loadbuilding);
        building_names= new ArrayList<>();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);
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

                    usertext.setText("'"+cs+"'");
                    /*adapter.setResults( realm.where(addBuildingRealm.class) //implicit AND
                            .beginGroup()
                            .contains("Building_name", cs.toString(),false)
                            .endGroup()
                            .findAll() );*/
                count=3;
               /* }else{

                    adapter.setResults( realm.where(MyPortfolioModel.class)
                            .greaterThan("or_psf", 0)  //implicit AND
                            .beginGroup()
                            .contains("name", cs.toString(),false)
                            .endGroup()
                            .findAll() );
                    Log.i( "portfolio","onTextChanged  LL : ");

                }*/

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
//                if(count==3)
//                lockedTimer();
                progressBar.setVisibility(View.VISIBLE);
                SearchBuilding();

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
                    if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                        ((BrokerMap) getActivity()).closeCardContainer();
                        ((BrokerMap) getActivity()).setlocation(name);
                    }else{


                    ((ClientMainActivity) getActivity()).setlocation(name);
                    }
                }

                General.setSharedPreferences(getContext(), AppConstants.BUILDING_NAME,name);
            }
        });




 /*       sideBar = (SideBar) v.findViewById(R.id.sideIndex);
//        dialog = (TextView) v.findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //Set the right touch monitor
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //The position of the first occurrence of the letter
                *//*int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }*//*

            }
        });*/


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
                            building_names.add(new loadBuildingDataModel(j.getString("name"),lat,longi,j.getString("id"),j.getString("locality"),j.getString("city")));

                        }
                        adapter= new searchBuilding(building_names,getContext());
                        listView1.setAdapter(adapter);
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
                                   //General.setSharedPreferences(getContext(),AppConstants.PROPERTY,adapter.getItem(position).getProperty_type());
                                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);
                                    ((BrokerMap)getActivity()).openAddListingFinalCard();
                                }else{
                                    AddbuildingAPICall(adapter.getItem(position).getName(),adapter.getItem(position).getLat() + "",adapter.getItem(position).getLng() + "",adapter.getItem(position).getId()+"",adapter.getItem(position).getLocality(),adapter.getItem(position).getCity());
                                }

                            }
                        });

                        /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getContext(),
                                android.R.layout.simple_list_item_1, building_names );*/

//                        listView1.setAdapter(arrayAdapter);
//                        JSONObject re = new JSONObject(jsonResponse.getString("responseData"));
                        /*Log.i("magic","addBuildingRealm success re data "+re);
                        Log.i("magic","addBuildingRealm success re "+re.length());*/



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



    /*public void AddBuildingDataToRealm(String id) {

        Realm myRealm = General.realmconfig(getContext());
        addBuildingRealm add_Building = new addBuildingRealm();
        add_Building.setTimestamp(String.valueOf(SystemClock.currentThreadTimeMillis()));
        add_Building.setBuilding_name(B_name);
        add_Building.setType("ADD");
        add_Building.setAddress(fullAddress);
        add_Building.setConfig(General.getSharedPreferences(getContext(), AppConstants.PROPERTY_CONFIG));
        add_Building.setProperty_type(AppConstants.PROPERTY);
        add_Building.setLat(lat + "");
        add_Building.setLng(lng + "");
        add_Building.setId(id);
        add_Building.setSublocality(SharedPrefs.getString(getContext(), SharedPrefs.MY_LOCALITY));
        myRealm.beginTransaction();
        myRealm.copyToRealmOrUpdate(add_Building);
//        myRealm.copyToRealmOrUpdate((Iterable<RealmObject>) myPortfolioModel);
        myRealm.commitTransaction();
    }*/



    public void AddbuildingAPICall(final String name,final String lat,final String longi,final String b_id,final String locality,String city) {

        Log.i("updateStatus CALLED", "updateStatus success called ");
        UpdateBuildingRateModel updateBuildingRateModel =new UpdateBuildingRateModel();
        updateBuildingRateModel.setBuilding(name);
        updateBuildingRateModel.setLat(lat);
        updateBuildingRateModel.setCity(city);
        updateBuildingRateModel.setLongiute(longi);
        updateBuildingRateModel.setBuilding_id(b_id);
        updateBuildingRateModel.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
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
                                add_Building.setLl_pm(price(General.getSharedPreferences(getContext(), AppConstants.PROPERTY_CONFIG),Integer.parseInt(building.getString("ll_pm"))));
                                add_Building.setOr_psf(Integer.parseInt(building.getString("or_psf")));
                                if(i==0){
                                    add_Building.setTt("ll");
                                }else{
                                    //add_Building.setId(b_id+"1");
                                    add_Building.setTt("or");
                                }
                                myRealm.beginTransaction();
                                myRealm.copyToRealmOrUpdate(add_Building);
                                myRealm.commitTransaction();
                            }
                            AppConstants.PROPERTY="Home";
                            ((ClientMainActivity)getActivity()).closeAddBuilding();
                            Intent in = new Intent(getContext(), MyPortfolioActivity.class);
                            startActivity(in);
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

 public int price(String conf,int rate){
    Log.i("conf case","conf  : "+conf+"  "+rate);
    int price=rate*950;
    switch(conf) {
        case "1rk":
            price = rate * 300;
            break;
        case "1bhk":
            price = rate * 600;
            break;
        case "1.5bhk":
            price = rate * 800;
            break;
        case "2bhk":
            price = rate * 950;
            break;
        case "2.5bhk":
            price = rate * 1300;
            break;
        case "3bhk":
            price = rate * 1600;
            break;
        case "3.5bhk":
            price = rate * 1800;
            break;
        case "4bhk":
            price = rate * 2100;
            break;
        case "4.5bhk":
            price = rate * 2300;
            break;
        case "5bhk":
            price = rate * 2500;
            break;
        case "5.5bhk":
            price = rate * 2700;
            break;
        case "6bhk":
            price = rate * 2900;
            break;
    }
    price=price/500;
    price=price*500;
    return price;
 }
}