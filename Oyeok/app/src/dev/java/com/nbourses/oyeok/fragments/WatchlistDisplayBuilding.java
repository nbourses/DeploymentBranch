package com.nbourses.oyeok.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.MyPortfolioActivity;
import com.nbourses.oyeok.adapters.porfolioAdapter;
import com.nbourses.oyeok.adapters.watchlistDisplayAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.ReadWatchlistAPI;
import com.nbourses.oyeok.models.WatchlistDisplayBuildingModel;
import com.nbourses.oyeok.models.loadBuildingDataModel;
import com.nbourses.oyeok.realmModels.WatchListRealmModel;
import com.nbourses.oyeok.realmModels.WatchlistBuildingRealm;
import com.nbourses.oyeok.realmModels.loadBuildingdataModelRealm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class WatchlistDisplayBuilding extends Fragment {


    View v;
    TextView back,watch_title;
    ListView Watchlist_Listview;
    ProgressBar pg_load_building;
    RealmList<loadBuildingdataModelRealm> ids1=new RealmList<>();
    ArrayList<String> ids=new ArrayList<>();
    private static ArrayList<String> deleteids=new ArrayList<>();
    private static RealmList<WatchlistBuildingRealm> realmList = new RealmList<>();
    private static ArrayList<WatchlistDisplayBuildingModel> watchlistmodel = new ArrayList<>();
    private static ArrayList<WatchlistDisplayBuildingModel> copywatchlistmodel = new ArrayList<>();
    private static ArrayList<WatchlistDisplayBuildingModel> deletelist = new ArrayList<>();
    watchlistDisplayAdapter adapter;
    Realm myRealm;
    String watchlist_id,watchlist_name,matchedid;
    LinearLayout btn_connect;
    ImageView btn_delete,btn_add_property;
    Bundle b;

    boolean status=false;
    Calendar myCalendar = Calendar.getInstance();




    public WatchlistDisplayBuilding() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_watchlist_display_building, container, false);
        back=(TextView)v.findViewById(R.id.back);
        Watchlist_Listview=(ListView)v.findViewById(R.id.Watchlist_Listview);
        btn_connect=(LinearLayout)v.findViewById(R.id.btn_connect);
        pg_load_building=(ProgressBar)v.findViewById(R.id.pg_load_building);
        btn_delete=(ImageView)v.findViewById(R.id.btn_delete);
        btn_add_property=(ImageView)v.findViewById(R.id.btn_add_property);
        watch_title=(TextView)v.findViewById(R.id.watch_title);
         b=new Bundle();
        b=getArguments();
        if(b.containsKey("read")) {
            watchlist_id = b.getString("read");
            status=true;
            Log.i("datafromraelm1", "realm data read :"+ watchlist_id);
        }
        else if(b.containsKey("watchlist_id")) {
            watchlist_id = b.getString("watchlist_id");
            status=false;
            Log.i("datafromraelm1", "realm watchlist_id  :"+ watchlist_id);
        }

        if (watchlistmodel != null)
            watchlistmodel.clear();


        myRealm = General.realmconfig(getContext());
        adapter = new watchlistDisplayAdapter(getContext(), watchlistmodel);
        Watchlist_Listview.setAdapter(adapter);


        WatchListRealmModel result = myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id", watchlist_id).findFirst();
        watch_title.setText(result.getWatchlist_name());

        DisplayList();
        btn_add_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MyPortfolioActivity)getActivity()).Back(watchlist_id);

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                if(copywatchlistmodel!=null)
                    copywatchlistmodel.clear();
                if(deletelist!=null)
                    deletelist.clear();
                for(WatchlistDisplayBuildingModel hold: adapter.getAllData()){
                    if(hold.isCheckbox()){
                        deletelist.add(hold);
                        deleteids.add(hold.getId());
                        count++;
                    }
                }

                copywatchlistmodel.addAll(watchlistmodel);
                if(count>0){
                    for (final WatchlistDisplayBuildingModel d : deletelist) {
                            for(WatchlistDisplayBuildingModel c : copywatchlistmodel) {
                                if (d.getId() == c.getId()) {
                                    Log.i("selected1", "selected building : c 34 " + c.getName() + "d  " + d.getName());
                                    watchlistmodel.remove(c);
                                    matchedid=d.getId();
                                    //DeleteWatchlist();

                                }
                            }

                        WatchListRealmModel result = myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id", watchlist_id).findFirst();

                        for(int i=0;i<result.getDisplayBuildinglist().size();i++) {

                            Log.i("selected1", "selected building : c22 " + result.getDisplayBuildinglist().get(i).getName()+" d.getId(): "+d.getId()+" result : "+result.getDisplayBuildinglist().get(i).getId());
                            if (result.getDisplayBuildinglist().get(i).getId().equalsIgnoreCase(d.getId())){
                                if (myRealm.isInTransaction())
                                    myRealm.cancelTransaction();
                                Log.i("selected1", "selected building : c12 " + result.getDisplayBuildinglist().get(i)+" deletelist "+deletelist.get(0).getName());
                                //deleteids.add(result.getDisplayBuildinglist().get(i).getId());
                                myRealm.beginTransaction();
                                result.getDisplayBuildinglist().get(i).removeFromRealm();
                                myRealm.commitTransaction();

                                break;
                            }

                        }


                    }
                    Log.i("selected1","selected building hold w3: "+count);
                    DeleteWatchlist();
                    btn_add_property.setVisibility(View.VISIBLE);
                    btn_delete.setVisibility(View.GONE);
                    adapter.Hide_check();
                   // adapter.notifyDataSetChanged();
                }

            }
        });

        Watchlist_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("datafromrealm1", "onItemSelected  :");
                if(btn_delete.getVisibility()==View.VISIBLE)
                adapter.setCheckBox(position);
                if(checkStatus()==0){
                    adapter.Hide_check();
                    btn_delete.setVisibility(View.GONE);
                    btn_add_property.setVisibility(View.VISIBLE);
                }
            }
        });


        Watchlist_Listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("datafromrealm1", "onItemLongClick  :");
                adapter.Show_check();
                btn_delete.setVisibility(View.VISIBLE);
                btn_add_property.setVisibility(View.GONE);
                return false;
            }
        });



        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DetailWatchlist();
            }
        });



 back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ((MyPortfolioActivity)getActivity()).Close();
    }
});

        return v;
    }



//NwOufE64hYV3

    private void ReadWatchlist(){
        Log.i("magic111","addBuildingRealm success response "+ids+"ids1 "+ids1);
        ReadWatchlistAPI readWatchlistAPI=new ReadWatchlistAPI();
        readWatchlistAPI.setUser_id(General.getSharedPreferences(getContext(), AppConstants.USER_ID));
        readWatchlistAPI.setAction("read");
        readWatchlistAPI.setCity("mumbai");
        readWatchlistAPI.setTt("ll");
        readWatchlistAPI.setBuild_list(ids);
        readWatchlistAPI.setWatchlist_id(watchlist_id);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        oyeokApiService.ReadWatchlist(readWatchlistAPI, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());

                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    Log.i("magic111","addBuildingRealm success response "+response+"\n"+jsonResponse);
                    JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                    Log.i("magic111","addBuildingRealm success response "+building);
                    //watchlist_id=building.getString("watchlist_id");
                    JSONArray buildingdata=new JSONArray(building.getString("buildings"));
                    Log.i("magic111","addBuildingRealm success response1 "+buildingdata);
                    Log.i("magic111","addBuildingRealm success response2 "+buildingdata.getString(0));
                   // AddDataToRealm(watchlist_id);
                    realmList.clear();
                int size =buildingdata.length();
                for(int i=0;i<size;i++){
                    JSONObject singleRowData = new JSONObject(buildingdata.get(i).toString());

                    String lat = singleRowData.getJSONArray("loc").get(1).toString();
                    Log.i("Buildingdata", "lat " + lat);
                    String longi = singleRowData.getJSONArray("loc").get(0).toString();

                    WatchlistBuildingRealm watchlistBuildingRealm=new WatchlistBuildingRealm(singleRowData.getString("id"),singleRowData.getString("name"),singleRowData.getString("config"),Integer.parseInt(singleRowData.getString("or_psf")),Integer.parseInt(singleRowData.getString("ll_pm")),lat,longi,singleRowData.getString("rate_growth"),singleRowData.getString("listings"),singleRowData.getString("portals"),singleRowData.getString("transactions"),singleRowData.getString("locality"),singleRowData.getString("req_avl"),singleRowData.getString("status"),singleRowData.getString("city"),singleRowData.getString("possession_date"));
                    realmList.add(watchlistBuildingRealm);
                    Log.i("Buildingdata", "name " + singleRowData.getString("name"));
                   // AddDataToRealm();

                }
                    AddDataToRealm();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }



private  void AddDataToRealm(){
    WatchListRealmModel results2 = myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id", watchlist_id).findFirst();
    WatchListRealmModel watchListRealmModel=new WatchListRealmModel();
    watchListRealmModel.setDisplayBuildinglist(realmList);
    watchListRealmModel.setWatchlist_id(watchlist_id);
    watchListRealmModel.setBuildingids(ids1);
    watchListRealmModel.setWatchlist_name(watchlist_name);
    watchListRealmModel.setCity("mumbai");
    watchListRealmModel.setTt(AppConstants.TT_TYPE);
    watchListRealmModel.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
    watchListRealmModel.setUser_name(General.getSharedPreferences(getContext(),AppConstants.NAME));
    watchListRealmModel.setUser_role(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER));
    watchListRealmModel.setImageuri(results2.getImageuri());
    myRealm.beginTransaction();
   // WatchListRealmModel watchListRealmModel=myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id",watchlist_id).findFirst();
    myRealm.copyToRealmOrUpdate(watchListRealmModel);
    myRealm.commitTransaction();
    pg_load_building.setVisibility(View.GONE);
   DisplayList1();
}

private void DisplayList(){

        /*RealmResults<*/WatchListRealmModel/*>*/ results2 = myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id", watchlist_id).findFirst();
        watchlistmodel.clear();
        WatchListRealmModel c = results2;
       // for (WatchListRealmModel c : results2) {
            Log.i("datainraelm", "==============================   :"+c.getDisplayBuildinglist().size());
           /* if(c.getDisplayBuildinglist().size()!=0 && !status){
                Log.i("datainraelm", "realm  data present   :  " +c.getDisplayBuildinglist().get(0).getId()+"  :: "+c.getDisplayBuildinglist().get(0).getName());
                for (int i = 0; i < c.getDisplayBuildinglist().size(); i++) {
                    Log.i("datainraelm", "realm  data present   :  " +c.getDisplayBuildinglist().get(i).getId()+"  :: "+c.getDisplayBuildinglist().get(i).getName());
                    WatchlistDisplayBuildingModel watchlistDisplayBuildingModel=new WatchlistDisplayBuildingModel(c.getDisplayBuildinglist().get(i).getId(),c.getDisplayBuildinglist().get(i).getName(),c.getDisplayBuildinglist().get(i).getConfig(),c.getDisplayBuildinglist().get(i).getOr_psf(),c.getDisplayBuildinglist().get(i).getLl_pm(),c.getDisplayBuildinglist().get(i).getLat(),c.getDisplayBuildinglist().get(i).getLng(),c.getDisplayBuildinglist().get(i).getRate_growth(),c.getDisplayBuildinglist().get(i).getListing(),c.getDisplayBuildinglist().get(i).getPortals(),c.getDisplayBuildinglist().get(i).getTransactions(),c.getDisplayBuildinglist().get(i).getLocality(),c.getDisplayBuildinglist().get(i).getReq_avl(),c.getDisplayBuildinglist().get(i).getStatus(),c.getDisplayBuildinglist().get(i).getCity(),c.getDisplayBuildinglist().get(i).getPossession_date(),false);
                    watchlistmodel.add(watchlistDisplayBuildingModel);
                }
                //watchlistmodel.addAll(watchlistDisplayBuildingModel);
                adapter.notifyDataSetChanged();
            }else {*/
                Log.i("datainraelm12", "realm  data absent size   :  "+c.getBuildingids().size());
                for (int i = 0; i < c.getBuildingids().size(); i++) {
                    //Log.i("datainraelm", "realm data  : " + i + " :  " + c.getBuildingids().get(i).getId());
                    ids.add(c.getBuildingids().get(i).getId());
                    Log.i("datainraelm12", "realm  data absent id  :  "+i+"  :: "+c.getBuildingids().get(i).getId());
                }
    Log.i("datainraelm12", "realm  data absent size   :  "+c.getBuildingids().size()+"ids : "+ids.size());
                watchlist_name=c.getWatchlist_name();
                ids1.addAll(c.getBuildingids());
                pg_load_building.setVisibility(View.VISIBLE);
                status=false;
                ReadWatchlist();
           // }

       // }
}


    private void DisplayList1(){
        /*RealmResults<*/WatchListRealmModel/*> */results2 = myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id", watchlist_id).findFirst();
        watchlistmodel.clear();
        WatchListRealmModel c = results2;
        //for (WatchListRealmModel c : results2) {
            Log.i("datainraelm", "==============================   :" + c.getDisplayBuildinglist().size());
            if (c.getDisplayBuildinglist().size() != 0 && !status) {
                Log.i("datainraelm", "realm  data present   :  " + c.getDisplayBuildinglist().get(0).getId() + "  :: " + c.getDisplayBuildinglist().get(0).getName());
                for (int i = 0; i < c.getDisplayBuildinglist().size(); i++) {
                    Log.i("datainraelm", "realm  data present   :  " + c.getDisplayBuildinglist().get(i).getId() + "  :: " + c.getDisplayBuildinglist().get(i).getName());
                    WatchlistDisplayBuildingModel watchlistDisplayBuildingModel = new WatchlistDisplayBuildingModel(c.getDisplayBuildinglist().get(i).getId(), c.getDisplayBuildinglist().get(i).getName(), c.getDisplayBuildinglist().get(i).getConfig(), c.getDisplayBuildinglist().get(i).getOr_psf(), c.getDisplayBuildinglist().get(i).getLl_pm(), c.getDisplayBuildinglist().get(i).getLat(), c.getDisplayBuildinglist().get(i).getLng(), c.getDisplayBuildinglist().get(i).getRate_growth(), c.getDisplayBuildinglist().get(i).getListing(), c.getDisplayBuildinglist().get(i).getPortals(), c.getDisplayBuildinglist().get(i).getTransactions(), c.getDisplayBuildinglist().get(i).getLocality(), c.getDisplayBuildinglist().get(i).getReq_avl(), c.getDisplayBuildinglist().get(i).getStatus(), c.getDisplayBuildinglist().get(i).getCity(), c.getDisplayBuildinglist().get(i).getPossession_date(), false);
                    watchlistmodel.add(watchlistDisplayBuildingModel);
                }
                //watchlistmodel.addAll(watchlistDisplayBuildingModel);
                adapter.notifyDataSetChanged();
            }
        //}


    }












    private void DeleteWatchlist(){
        Log.i("magic1112","addBuildingRealm success response "+ids);
        ReadWatchlistAPI readWatchlistAPI=new ReadWatchlistAPI();
        readWatchlistAPI.setUser_id(General.getSharedPreferences(getContext(), AppConstants.USER_ID));
        readWatchlistAPI.setAction("delete");
        readWatchlistAPI.setCity("mumbai");
        readWatchlistAPI.setTt("ll");
        readWatchlistAPI.setBuild_list(deleteids);
        readWatchlistAPI.setWatchlist_id(watchlist_id);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        oyeokApiService.ReadWatchlist(readWatchlistAPI, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());

                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    Log.i("magic11123","addBuildingRealm success response "+response+"\n"+jsonResponse);
                    JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                    Log.i("magic11123","addBuildingRealm success response "+building);
                    //watchlist_id=building.getString("watchlist_id");
                    /*JSONArray buildingdata=new JSONArray(building.getString("buildings"));
                    Log.i("magic111","addBuildingRealm success response1 "+buildingdata);
                    Log.i("magic111","addBuildingRealm success response2 "+buildingdata.getString(0));*/
                    // AddDataToRealm(watchlist_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }



public int checkStatus(){
    int count=0;
    for(WatchlistDisplayBuildingModel hold: adapter.getAllData()){
        if(hold.isCheckbox()){
            // selectedlist.remove(hold);
            count++;
                        /*WatchlistDisplayBuildingModel loadBuildingDataModel1=new WatchlistDisplayBuildingModel(hold.getName(),hold.getLat(),hold.getLng(),hold.getId(),hold.getLocality(),hold.getCity(),hold.getLl_pm(),hold.getOr_psf());
                        templist.add(loadBuildingDataModel1);*/
            //Log.i("selected1","selected building hold : "+hold.getName());
        }

    }
    return count;
}


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myRealm.close();
    }





}
