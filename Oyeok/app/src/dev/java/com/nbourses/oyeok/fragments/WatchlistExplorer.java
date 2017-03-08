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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transfermanager.Copy;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.activities.MyPortfolioActivity;
import com.nbourses.oyeok.adapters.watchlistAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.CreateWatchlistAPI;
import com.nbourses.oyeok.models.SearchBuildingModel;
import com.nbourses.oyeok.models.UpdateBuildingRateModel;
import com.nbourses.oyeok.models.loadBuildingDataModel;
import com.nbourses.oyeok.realmModels.WatchListRealmModel;
import com.nbourses.oyeok.realmModels.WatchlistBuildingRealm;
import com.nbourses.oyeok.realmModels.addBuildingRealm;
import com.nbourses.oyeok.realmModels.loadBuildingdataModelRealm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

//implements OnItemClickListener
public class WatchlistExplorer extends Fragment   {


    private TextView Next,back,done;
    private View v;

    ListView listView1;
    EditText inputSearch1;
    watchlistAdapter adapter;
    private Realm realm;
    private ProgressBar progressBar;
    ImageView add;
    String name,watchlist_id,watchlist_name,mImageUri;
    private TextView dialog;
    LinearLayout add_b;
    private SideBar sideBar;
    private Timer clockTickTimer;
    int count=0;
    Realm myRealm;
    //    String name;
    private static ArrayList<loadBuildingDataModel> building_names =new ArrayList<>();
    private static ArrayList<loadBuildingDataModel> Copybuilding_names =new ArrayList<>();
    private static ArrayList<loadBuildingDataModel> selectedlist = new ArrayList<>();
    private static ArrayList<loadBuildingDataModel> selectedlist1 = new ArrayList<>();

    private static RealmList<loadBuildingdataModelRealm> realmidsList = new RealmList<>();
    ArrayList<String> ids=new ArrayList<>();
    private static RealmList<loadBuildingdataModelRealm> addids=new RealmList<>();
   // private static RealmList<loadBuildingdataModelRealm> realmList = new RealmList<>();


    String user_id,user_name,user_role;

Thread thread;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_watchlist_explorer, container, false);
        Next=(TextView)v.findViewById(R.id.Cancel);
        back=(TextView)v.findViewById(R.id.back);
        done=(TextView)v.findViewById(R.id.done);
        listView1=(ListView) v.findViewById(R.id.listView1);
        inputSearch1=(EditText)v.findViewById(R.id.inputSearch1);
        add=(ImageView) v.findViewById(R.id.add);
        add_b=(LinearLayout)v.findViewById(R.id.add_b);
        //usertext=(TextView)v.findViewById(R.id.usertext);
        progressBar=(ProgressBar)v.findViewById(R.id.loadbuilding);
        if(getArguments()!=null) {
            Bundle b = getArguments();
            watchlist_id = b.getString("edit");
            Log.i("getArguments","inside getArguments :  "+watchlist_id);
            if (!watchlist_id.equalsIgnoreCase("")) {
                done.setVisibility(View.VISIBLE);
                Next.setVisibility(View.GONE);
            }
        }
        myRealm = General.realmconfig(getContext());
       // listView1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        if (selectedlist != null)
            selectedlist.clear();
        if (selectedlist1 != null)
            selectedlist1.clear();
        if (building_names != null)
            building_names.clear();



        user_id=General.getSharedPreferences(getContext(),AppConstants.USER_ID);
        user_name=General.getSharedPreferences(getContext(),AppConstants.NAME);
        user_role=General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER);

        adapter= new watchlistAdapter(building_names,getContext());
        listView1.setAdapter(adapter);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                try {

                    if(building_names.size()!=0) {
                        selectedlist.clear();
                        ids.clear();
                        addids.clear();
                        Log.i("selected1","selected building 12: "+selectedlist.size());

                        for (loadBuildingDataModel hold : adapter.getAllData()) {
                            if (hold.isCheckbox()) {


                                ids.add(hold.getId());
                                loadBuildingdataModelRealm  loadBuildingdataModelRealm1=new loadBuildingdataModelRealm(hold.getId());
                                addids.add(loadBuildingdataModelRealm1);
                                Log.i("selected1", "selected building 1: " + hold.getName() + "  selectedlist.size() : " + +ids.size());
                                count++;
                            }

                        }
                        if(count>0) {
                            WatchListRealmModel result = myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id", watchlist_id).findFirst();

                            watchlist_name=result.getWatchlist_name();
                            mImageUri=result.getImageuri();
                            addids.addAll(result.getBuildingids());

                            Log.i("selected1","selected building 83:");
                            CreateWatchlist();
                        }else{
                            Log.i("selected1","selected building 14: "+selectedlist.size());

                            new AlertDialog.Builder(getContext())
                                    .setTitle("No Building Selected")
                                    .setMessage("Please select alteast 1 building !")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                    else{
                        Log.i("selected1","selected building 15: "+selectedlist.size());

                        new AlertDialog.Builder(getContext())
                                .setTitle("No Building Selected")
                                .setMessage("Please selected alteast 1 building !")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })*/

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);

                if(General.getSharedPreferences(getContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                    ((MyPortfolioActivity) getActivity()).closeWatchlistFragment();

                }else{

                    ((MyPortfolioActivity) getActivity()).closeWatchlistFragment();
//                    ((ClientMainActivity)getActivity()).openAddListing();
                }


            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);
                Log.i("selected1","selected building 11: "+selectedlist.size());
                int count=0;
                try {

                    if(building_names.size()!=0) {
                            selectedlist.clear();
                        Log.i("selected1","selected building 12: "+selectedlist.size());

                            for (loadBuildingDataModel hold : adapter.getAllData()) {
                                if (hold.isCheckbox()) {

                                    loadBuildingDataModel loadBuildingDataModel1 = new loadBuildingDataModel(hold.getName(), hold.getLat(), hold.getLng(), hold.getId(), hold.getLocality(), hold.getCity(), hold.getLl_pm(), hold.getOr_psf());
                                    selectedlist.add(loadBuildingDataModel1);
                                    Log.i("selected1", "selected building 1: " + hold.getName() + "  selectedlist.size() : " + selectedlist.size());
                                 count++;
                                }

                            }
                        if(count>0) {
                            Log.i("selected1","selected building 13: "+selectedlist.size());

                            ((MyPortfolioActivity) getActivity()).OpenFrag(selectedlist);
                        }else{
                            Log.i("selected1","selected building 14: "+selectedlist.size());

                            new AlertDialog.Builder(getContext())
                                    .setTitle("No Building Selected")
                                    .setMessage("Please select alteast 1 building !")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                    else{
                        Log.i("selected1","selected building 15: "+selectedlist.size());

                        new AlertDialog.Builder(getContext())
                                .setTitle("No Building Selected")
                                .setMessage("Please selected alteast 1 building !")
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        inputSearch1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                if(TT=="LL"){
                Log.i( "portfolio","onTextChanged  LL : "+cs );
                name=String.valueOf(cs);
                //usertext.setText("'"+cs+"'");

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
                        //Selectedbuilding();
                        SearchBuilding();
                        // TODO
                    }
                };

                thread.start();

            }
        });

/*        add_b.setOnClickListener(new View.OnClickListener() {
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
                        *//*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })*//*
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }else {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);
                    if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                        ((com.nbourses.oyeok.activities.BrokerMap) getActivity()).closeCardContainer();
                        ((com.nbourses.oyeok.activities.BrokerMap) getActivity()).setlocation(name);
                    }else{


                        ((ClientMainActivity) getActivity()).setlocation(name);
                    }
                }

                General.setSharedPreferences(getContext(), AppConstants.BUILDING_NAME,name);
            }
        });*/


        //init();







        return v;
    }



    public void Selectedbuilding(){
        selectedlist.clear();
        for (loadBuildingDataModel hold : adapter.getAllData()) {
            if (hold.isCheckbox()) {

                loadBuildingDataModel loadBuildingDataModel1 = new loadBuildingDataModel(hold.getName(), hold.getLat(), hold.getLng(), hold.getId(), hold.getLocality(), hold.getCity(), hold.getLl_pm(), hold.getOr_psf(),true);
                selectedlist.add(loadBuildingDataModel1);
                Log.i("selected1", "selected building 1: " + hold.getName() + "  selectedlist.size() : " + selectedlist.size());
                count++;
            }

        }
    }


    public void SearchBuilding()
    {
        Log.i("updateStatus CALLED","updateStatus success called ");
        SearchBuildingModel searchBuildingModel = new SearchBuildingModel();
        searchBuildingModel.setBuilding(name);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_101).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
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
                        Copybuilding_names.clear();
//                        List<String> building_names = new ArrayList<String>();
                        Log.i("sizeof ","selectedlist"+count);
                        if(selectedlist!=null){
                            building_names.addAll(selectedlist);
                        }
                        for(int i=0;i<size;i++){
                            JSONObject j = new JSONObject(buildings.get(i).toString());
                            double lat = Double.parseDouble(j.getJSONArray("loc").get(1).toString());
                            double longi = Double.parseDouble(j.getJSONArray("loc").get(0).toString());
                            Log.i("Buildingdata", "lat " + lat+"longi:  "+longi+"id:  "+j.getString("id")+"name: "+j.getString("name"));
                            Copybuilding_names.add(new loadBuildingDataModel(j.getString("name"),lat,longi,j.getString("id"),j.getString("locality"),j.getString("city"),j.getString("ll_pm"),j.getString("or_psf"),false));

                        }
//                        try {
//                            adapter= new watchlistAdapter(building_names,getContext());
//                            listView1.setAdapter(adapter);
                        building_names.addAll(Copybuilding_names);
                       // buildings
                            adapter.notifyDataSetChanged();
                       /* } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        progressBar.setVisibility(View.GONE);
//                        listView1.setOnItemSelectedListener();
                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                adapter.setCheckBox(position);
                                Selectedbuilding();
                                Log.i("multimode","inside onItemClick  123 "+position+" "+selectedlist.size());


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

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("insidecheck","inside onItemClick before  123 "+position+" "+selectedlist.size());

        CheckBox cb = (CheckBox) v.findViewById(R.id.txt_title);
       // LinearLayout row=(LinearLayout)v.findViewById(R.id.row1);

        //cb.get

        cb.performClick();
        if (cb.isChecked()) {
            Log.i("insidecheck","inside onItemClick after11    "+position+" "+selectedlist.size());

            //row.setBackgroundColor(Color.GREEN);
            selectedlist.add((loadBuildingDataModel) adapter.getItem(position));
        } else if (!cb.isChecked()) {
            selectedlist.remove( adapter.getItem(position));
        }
        Log.i("insidecheck","inside onItemClick after   123 "+position+" "+selectedlist.size());
    }*/


        /*@Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // view.setSelected(true);
            //selectedlist.add(adapter.getItem(position));
            CheckBox cb = (CheckBox) v.findViewById(R.id.txt_title);
            LinearLayout row=(LinearLayout)v.findViewById(R.id.row1);

            //cb.get
            Log.i("multimode","inside onItemClick before  123 "+position+" "+selectedlist.size());

            cb.performClick();
            if (cb.isChecked()) {

                //row.setBackgroundColor(Color.GREEN);
                selectedlist.add(adapter.getItem(position));
            } else if (!cb.isChecked()) {
                selectedlist.remove(adapter.getItem(position));
            }
            Log.i("multimode","inside onItemClick  123 "+position+" "+selectedlist.size());


        }*/


    public ArrayList<loadBuildingDataModel> passingListFragA(){
        return (selectedlist);
    }







    private void CreateWatchlist(){
        Log.i("magic11","addBuildingRealm success response CreateWatchlist  :::::::::::: ");
        CreateWatchlistAPI createWatchlistAPI=new CreateWatchlistAPI();
        createWatchlistAPI.setUser_id(user_id);
        createWatchlistAPI.setAction("create");
        createWatchlistAPI.setCity("mumbai");
        createWatchlistAPI.setTt("ll");
        createWatchlistAPI.setWatchlist_id(watchlist_id);
        createWatchlistAPI.setTitle(watchlist_name);
        createWatchlistAPI.setBuild_list(ids);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        oyeokApiService.CreateWatchlist(createWatchlistAPI, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());

                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    Log.i("magic11","addBuildingRealm success response CreateWatchlist  "+response+"\n"+jsonResponse);
                    JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                    Log.i("magic11","addBuildingRealm success response CreateWatchlist 1  "+building+" : : "+watchlist_id+" ::::: "+building.getString("watchlist_id"));
                    watchlist_id=building.getString("watchlist_id");
                    AddDataToRealm(watchlist_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }


    public void AddDataToRealm(String watchlist_id){
        Realm myRealm = General.realmconfig(getContext());
        WatchListRealmModel watchListRealmModel=new WatchListRealmModel();
        watchListRealmModel.setWatchlist_id(watchlist_id);
        watchListRealmModel.setWatchlist_name(watchlist_name);
        watchListRealmModel.setImageuri(mImageUri+"");
        watchListRealmModel.setCity("mumbai");
        watchListRealmModel.setTt(AppConstants.TT_TYPE);
        watchListRealmModel.setUser_id(user_id);
        watchListRealmModel.setUser_name(user_name);
        watchListRealmModel.setUser_role(user_role);
        watchListRealmModel.setBuildingids(addids);
        myRealm.beginTransaction();
        myRealm.copyToRealmOrUpdate(watchListRealmModel);
        myRealm.commitTransaction();
        //RealmResults<WatchListRealmModel> results2 = myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id", watchlist_id).findAll();
        //RealmResults<WatchListRealmModel> results2 = myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id", watchlist_id).findAll();

       /* for (WatchListRealmModel c : results2) {

            for(int i=0;i<c.getBuildingids().size();i++) {
                Log.i("datafromraelm", "realm data  in relm 2s3 : "+i+" :  " + c.getBuildingids().get(i).getId());
            }


        }*/
        //pg_create_watch.setVisibility(View.GONE);
        ((MyPortfolioActivity)getActivity()).Refresh(watchlist_id);
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myRealm.close();
    }




}
