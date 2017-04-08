package com.nbourses.oyeok.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.BrokerListingActivity;
import com.nbourses.oyeok.adapters.CatalogDisplayAdapter;
import com.nbourses.oyeok.adapters.watchlistDisplayAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.CreateCatalogListing;
import com.nbourses.oyeok.models.ListingModel;
import com.nbourses.oyeok.models.ReadWatchlistAPI;
import com.nbourses.oyeok.models.WatchlistDisplayBuildingModel;
import com.nbourses.oyeok.models.portListingModel;
import com.nbourses.oyeok.realmModels.ListingCatalogRealm;
import com.nbourses.oyeok.realmModels.ListingRealm;
import com.nbourses.oyeok.realmModels.Listingidsrealm;
import com.nbourses.oyeok.realmModels.WatchListRealmModel;
import com.nbourses.oyeok.realmModels.WatchlistBuildingRealm;
import com.nbourses.oyeok.realmModels.loadBuildingdataModelRealm;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class CatalogDisplayListing extends Fragment {

View v;

    TextView back,catalog_title;
    ListView Listview;
    ProgressBar pg_load_building;
    RealmList<Listingidsrealm> ids1=new RealmList<>();
    ArrayList<String> ids=new ArrayList<>();
   
    private static ArrayList<String> deleteids=new ArrayList<>();
    private static RealmList<ListingRealm> DisplayListRealm = new RealmList<>();
    private static ArrayList<ListingModel> DisplayListmodel = new ArrayList<>();
    private static ArrayList<ListingModel> copyDisplayListmodel = new ArrayList<>();
    private static ArrayList<ListingModel> deleteDisplayList = new ArrayList<>();
    CatalogDisplayAdapter adapter;
    Realm myRealm;
    String catalog_id,catalog_name,matchedid;
    ImageView btn_delete,btn_add_property;
    Bundle b;
LinearLayout btn_connect;
    boolean status=false;
    String User_name,user_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         v= inflater.inflate(R.layout.fragment_catalog_display_listing, container, false);

        back=(TextView)v.findViewById(R.id.list_back);
        Listview=(ListView)v.findViewById(R.id.list_Watchlist_Listview);
        btn_connect=(LinearLayout)v.findViewById(R.id.list_btn_connect);
        pg_load_building=(ProgressBar)v.findViewById(R.id.list_pg_load_building);
        btn_delete=(ImageView)v.findViewById(R.id.list_btn_delete);
        //btn_add_property=(ImageView)v.findViewById(R.id.list_btn_add_property);
        catalog_title=(TextView)v.findViewById(R.id.list_watch_title);

        b=new Bundle();
        b=getArguments();
        /*if(b.containsKey("read")) {
            catalog_id = b.getString("read");
            status=true;
            Log.i("datafromraelm1", "realm data read :"+ catalog_id);
        }
        else*/ if(b.containsKey("catalog_id")) {
            catalog_id = b.getString("catalog_id");
            status=false;
            Log.i("datafromraelm1", "realm catalog_id  :"+ catalog_id);
        }

        if (DisplayListmodel != null)
            DisplayListmodel.clear();


        User_name=General.getSharedPreferences(getContext(),AppConstants.NAME);
        user_id=General.getSharedPreferences(getContext(),AppConstants.USER_ID);
        myRealm = General.realmconfig(getContext());
        adapter = new CatalogDisplayAdapter(getContext(), DisplayListmodel);
        Listview.setAdapter(adapter);


        ListingCatalogRealm result = myRealm.where(ListingCatalogRealm.class).equalTo("catalog_id", catalog_id).findFirst();
        catalog_name=result.getCatalog_name();
        catalog_title.setText(catalog_name);

        DisplayList();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BrokerListingActivity)getActivity()).Close();
            }
        });

        Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setCheckBox(position);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                if(copyDisplayListmodel!=null)
                    copyDisplayListmodel.clear();
                if(deleteDisplayList!=null)
                    deleteDisplayList.clear();
                for(ListingModel hold: adapter.getAllData()){
                    if(hold.isCheckbox()){
                        deleteDisplayList.add(hold);
                        deleteids.add(hold.getListing_id());
                        count++;
                    }
                }

                copyDisplayListmodel.addAll(DisplayListmodel);
                if(count>0){
                    for (final ListingModel d : deleteDisplayList) {
                        for(ListingModel c : copyDisplayListmodel) {
                            if (d.getListing_id() == c.getListing_id()) {
                                Log.i("selected1", "selected building : c 34 " + c.getName() + "d  " + d.getName());
                                DisplayListmodel.remove(c);
                                matchedid=d.getListing_id();
                                //DeleteWatchlist();

                            }
                        }

                        ListingCatalogRealm  result = myRealm.where(ListingCatalogRealm.class).equalTo("catalog_id", catalog_id).findFirst();

                        for(int i=0;i<result.getDisplayListings().size();i++) {

                            Log.i("selected1", "selected building : c22 " + result.getDisplayListings().get(i).getName()+" d.getId(): "+d.getListing_id()+" result : "+result.getDisplayListings().get(i).getListing_id());
                            if (result.getDisplayListings().get(i).getListing_id().equalsIgnoreCase(d.getListing_id())){
                                if (myRealm.isInTransaction())
                                    myRealm.cancelTransaction();
                                Log.i("selected1", "selected building : c12 " + result.getDisplayListings().get(i)+" deletelist "+deleteDisplayList.get(0).getName());
                                //deleteids.add(result.getDisplayBuildinglist().get(i).getId());
                                myRealm.beginTransaction();
                                result.getDisplayListings().get(i).removeFromRealm();
                                //if(result.getListingids().get(i)!=null)

                                myRealm.commitTransaction();
                                break;
                            }

                        }


                        for(int i=0;i<result.getListingids().size();i++) {
                            Log.i("selected1", "selected building : c22 " +" d.getId(): "+d.getListing_id()+" result : "+result.getListingids().get(i).getListing_id());
                            if (result.getListingids().get(i).getListing_id().equalsIgnoreCase(d.getListing_id())){
                                if (myRealm.isInTransaction())
                                    myRealm.cancelTransaction();
                                myRealm.beginTransaction();
                                result.getListingids().get(i).removeFromRealm();
                                myRealm.commitTransaction();
                                break;
                            }

                        }




                    }
                    Log.i("selected1","selected building hold w3: "+count);
                    DeleteCatalog();
                    adapter.notifyDataSetChanged();
                    /*btn_add_property.setVisibility(View.VISIBLE);
                    btn_delete.setVisibility(View.GONE);
                    adapter.Hide_check();*/
                    //
                }
            }
        });



        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareListingCatalog();
            }
        });


        return v;
    }






    private void DisplayList(){

        ListingCatalogRealm results2 = myRealm.where(ListingCatalogRealm.class).equalTo("catalog_id", catalog_id).findFirst();
        DisplayListmodel.clear();
        ListingCatalogRealm c = results2;
        for (int i = 0; i < c.getListingids().size(); i++) {
            //Log.i("datainraelm", "realm data  : " + i + " :  " + c.getBuildingids().get(i).getId());
            ids.add(c.getListingids().get(i).getListing_id());
            Log.i("datainraelm12", "realm  data absent id  :  "+i+"  :: "+c.getListingids().get(i).getListing_id());
        }
        Log.i("datainraelm12", "realm  data absent size   :  "+c.getListingids().size()+"ids : "+ids.size());
        catalog_name=c.getCatalog_name();
        ids1.addAll(c.getListingids());
        pg_load_building.setVisibility(View.VISIBLE);
        status=false;
        ReadListingCatalog();

    }



    private void ReadListingCatalog(){
        Log.i("magic111","addBuildingRealm success response "+ids+"ids1 "+ids1);

        CreateCatalogListing createCatalogListing=new CreateCatalogListing();
        createCatalogListing.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
        createCatalogListing.setAction("read");
        createCatalogListing.setCatalog_id(catalog_id);
        createCatalogListing.setCity("Mumbai");
        createCatalogListing.setTt((AppConstants.TT_TYPE).toLowerCase());
        createCatalogListing.setTitle(catalog_name);
        createCatalogListing.setListing_ids(ids);

       // Log.i("createCatalogListing","addBuildingRealm createCatalogListing request "+createCatalogListing.getUser_id()+" :: "+createCatalogListing.getCatalog_id()+" : "+createCatalogListing.getCatalog_id()+" : "+createCatalogListing.getTitle()+" : "+createCatalogListing.getListing_ids().get(0)+","+createCatalogListing.getListing_ids().get(1));

       // Gson g=new Gson(createCatalogListing);
        Gson gson = new Gson();
        String json = gson.toJson(createCatalogListing);
        Log.i("magic11","getLocality  json "+json);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);

        oyeokApiService.CreateCataloglist(createCatalogListing, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());

                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    Log.i("magical11","addBuildingRealm success response "+response+"\n"+jsonResponse);
                    JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                    Log.i("magical11","addBuildingRealm success response "+building);
                    //watchlist_id=building.getString("watchlist_id");
                    JSONArray buildingdata=new JSONArray(building.getString("listings"));
                    Log.i("magical11","addBuildingRealm success response1 "+buildingdata);
                    Log.i("magical11","addBuildingRealm success response2 "+buildingdata.getString(0));
                    // AddDataToRealm(watchlist_id);
                    DisplayListRealm.clear();
                    int size =buildingdata.length();
                    for(int i=0;i<size;i++){
                        try {
                            JSONObject singleRowData = new JSONObject(buildingdata.get(i).toString());

                            String lat = singleRowData.getJSONArray("loc").get(1).toString();
                            Log.i("Buildingdata", "lat " + lat);
                            String longi = singleRowData.getJSONArray("loc").get(0).toString();

                            ListingRealm listingRealm=new ListingRealm(singleRowData.getString("listing_id"),singleRowData.getString("name"),singleRowData.getString("config"),Integer.parseInt(singleRowData.getString("listed_ll_pm")),Integer.parseInt(singleRowData.getString("listed_or_psf")),lat,longi,Integer.parseInt(singleRowData.getString("real_ll_pm")),Integer.parseInt(singleRowData.getString("real_or_psf")),singleRowData.getString("possession_date"),singleRowData.getString("req_avl"),singleRowData.getString("locality"),singleRowData.getString("city"),singleRowData.getString("rate_growth"),singleRowData.getString("portals"),singleRowData.getString("listings"),singleRowData.getString("transactions"));
                            DisplayListRealm.add(listingRealm);
                            Log.i("Buildingdata", "name " + singleRowData.getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        // AddDataToRealm();

                    }
                    AddDataToRealm();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("magic11c"," failure response   "+error);
                try {
                    if(error.getMessage().equalsIgnoreCase("500 Internal Server Error")){
                        SnackbarManager.show(
                                Snackbar.with(getContext())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text(R.string.server_error)
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                    }else
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



    private  void AddDataToRealm(){
        ListingCatalogRealm results2 = myRealm.where(ListingCatalogRealm.class).equalTo("catalog_id", catalog_id).findFirst();
        ListingCatalogRealm listingCatalogRealm=new ListingCatalogRealm();
        listingCatalogRealm.setDisplayListings(DisplayListRealm);
        listingCatalogRealm.setCatalog_id(catalog_id);
        ids.clear();
        for(ListingRealm c:DisplayListRealm){
          ids.add(c.getListing_id());
        }
        listingCatalogRealm.setListingids(ids1);
        listingCatalogRealm.setCatalog_name(catalog_name);
        listingCatalogRealm.setCity("Mumbai");
        listingCatalogRealm.setTt((AppConstants.TT_TYPE).toLowerCase());
        listingCatalogRealm.setUser_id(user_id);
        listingCatalogRealm.setUser_name(User_name);
        //listingCatalogRealm.setUser_role(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER));
        listingCatalogRealm.setImageuri(results2.getImageuri());
        myRealm.beginTransaction();
        // WatchListRealmModel watchListRealmModel=myRealm.where(WatchListRealmModel.class).equalTo("watchlist_id",watchlist_id).findFirst();
        myRealm.copyToRealmOrUpdate(listingCatalogRealm);
        myRealm.commitTransaction();
        pg_load_building.setVisibility(View.GONE);
        DisplayList1();
    }




    private void DisplayList1(){
        /*RealmResults<*/ListingCatalogRealm/*> */results2 = myRealm.where(ListingCatalogRealm.class).equalTo("catalog_id", catalog_id).findFirst();
        DisplayListmodel.clear();
        ListingCatalogRealm c = results2;
        //for (WatchListRealmModel c : results2) {
        Log.i("datainraelm", "==============================   :" + c.getDisplayListings().size());
        if (c.getDisplayListings().size() != 0 ) {    //&& !status
            Log.i("datainraelm", "realm  data present   :  " + c.getDisplayListings().get(0).getListing_id() + "  :: " + c.getDisplayListings().get(0).getName());
            for (int i = 0; i < c.getDisplayListings().size(); i++) {
                Log.i("datainraelm", "realm  data present   :  " + c.getDisplayListings().get(i).getListing_id() + "  :: " + c.getDisplayListings().get(i).getName());



                ListingModel listingModel = new ListingModel(c.getDisplayListings().get(i).getListing_id(),c.getDisplayListings().get(i).getName(),c.getDisplayListings().get(i).getConfig(),c.getDisplayListings().get(i).getListed_ll_pm(),c.getDisplayListings().get(i).getListed_or_psf(),c.getDisplayListings().get(i).getLat(),c.getDisplayListings().get(i).getLng(),c.getDisplayListings().get(i).getReal_ll_pm(),c.getDisplayListings().get(i).getReal_or_psf(),c.getDisplayListings().get(i).getPossession_date(),c.getDisplayListings().get(i).getReq_avl(),c.getDisplayListings().get(i).getLocality(),c.getDisplayListings().get(i).getCity(),c.getDisplayListings().get(i).getRate_growth(),c.getDisplayListings().get(i).getPortals(),c.getDisplayListings().get(i).getListings(),c.getDisplayListings().get(i).getTransactions(),false);

                DisplayListmodel.add(listingModel);
            }
            //watchlistmodel.addAll(watchlistDisplayBuildingModel);
            adapter.notifyDataSetChanged();
        }
        //}


    }






    private void DeleteCatalog(){
        Log.i("magic111","addBuildingRealm success response "+ids+"ids1 "+ids1);

        CreateCatalogListing createCatalogListing=new CreateCatalogListing();
        createCatalogListing.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
        createCatalogListing.setAction("delete");
        createCatalogListing.setCatalog_id(catalog_id);
        createCatalogListing.setCity("Mumbai");
        createCatalogListing.setTt((AppConstants.TT_TYPE).toLowerCase());
       // createCatalogListing.setTitle(catalog_name);
        createCatalogListing.setListing_ids(deleteids);

       // Log.i("createCatalogListing","addBuildingRealm createCatalogListing request "+createCatalogListing.getUser_id()+" :: "+createCatalogListing.getCatalog_id()+" : "+createCatalogListing.getCatalog_id()+" : "+" : "+createCatalogListing.getListing_ids().get(0)+","+createCatalogListing.getListing_ids().get(1));

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);

        oyeokApiService.CreateCataloglist(createCatalogListing, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());

                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    Log.i("magical11","addBuildingRealm success response "+response+"\n"+jsonResponse);
                    JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                    Log.i("magical11","addBuildingRealm success response "+building);
                    //watchlist_id=building.getString("watchlist_id");
                    JSONArray buildingdata=new JSONArray(building.getString("listings"));
                    Log.i("magical11","addBuildingRealm success response1 "+buildingdata);
                    Log.i("magical11","addBuildingRealm success response2 "+buildingdata.getString(0));
                    // AddDataToRealm(watchlist_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("magic11c"," failure response   "+error);
                try {
                    if(error.getMessage().equalsIgnoreCase("500 Internal Server Error")){
                        SnackbarManager.show(
                                Snackbar.with(getContext())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text(R.string.server_error)
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                    }else
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




    private void ShareListingCatalog(){
        Log.i("magic111","addBuildingRealm success response "+ids+"ids1 "+ids1);

        CreateCatalogListing createCatalogListing=new CreateCatalogListing();
        createCatalogListing.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
        createCatalogListing.setAction("share");
        createCatalogListing.setCatalog_id(catalog_id);
        createCatalogListing.setCity("Mumbai");
        createCatalogListing.setTt((AppConstants.TT_TYPE).toLowerCase());
        createCatalogListing.setTitle(catalog_name);
       // createCatalogListing.setListing_ids(ids);

        // Log.i("createCatalogListing","addBuildingRealm createCatalogListing request "+createCatalogListing.getUser_id()+" :: "+createCatalogListing.getCatalog_id()+" : "+createCatalogListing.getCatalog_id()+" : "+createCatalogListing.getTitle()+" : "+createCatalogListing.getListing_ids().get(0)+","+createCatalogListing.getListing_ids().get(1));

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);

        oyeokApiService.CreateCataloglist(createCatalogListing, new retrofit.Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());

                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    Log.i("magical44","addBuildingRealm success response "+response+"\n"+jsonResponse);
                    JSONObject building = new JSONObject(jsonResponse.getString("responseData"));
                    Log.i("magical44","addBuildingRealm success response "+building);
                    //watchlist_id=building.getString("watchlist_id");
                    String Sharelink=building.getString("share_link");
                    Log.i("magical44","addBuildingRealm success response1 "+Sharelink);

                    openScreenshot(Sharelink);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("magic11c"," failure response   "+error);
                try {
                    if(error.getMessage().equalsIgnoreCase("500 Internal Server Error")){
                        SnackbarManager.show(
                                Snackbar.with(getContext())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text(R.string.server_error)
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                    }else
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




    private void openScreenshot(String url) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hey check this out!");
        startActivity(Intent.createChooser(intent, "Share link via"));

    }




    @Override
    public void onDetach() {
        super.onDetach();
    }

}
