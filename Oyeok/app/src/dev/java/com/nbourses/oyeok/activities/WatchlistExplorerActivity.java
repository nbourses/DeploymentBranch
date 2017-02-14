package com.nbourses.oyeok.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
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
import com.nbourses.oyeok.adapters.searchBuilding;
import com.nbourses.oyeok.fragments.SideBar;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.SearchBuildingModel;
import com.nbourses.oyeok.models.loadBuildingDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

import io.realm.Realm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class WatchlistExplorerActivity extends AppCompatActivity {
    private TextView Cancel,back;
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
    ArrayList<loadBuildingDataModel> building_names =new ArrayList<>();
    private static ArrayList<loadBuildingDataModel> selectedlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // v=inflater.inflate(R.layout.fragment_watchlist_explorer, container, false);
        
        setContentView(R.layout.activity_watchlist_explorer);
        Cancel=(TextView)findViewById(R.id.Cancel);
        back=(TextView)findViewById(R.id.back);
        listView1=(ListView) findViewById(R.id.listView1);
        inputSearch1=(EditText)findViewById(R.id.inputSearch1);
        add=(ImageView) findViewById(R.id.add);
        add_b=(LinearLayout)findViewById(R.id.add_b);
        //usertext=(TextView)findViewById(R.id.usertext);
        progressBar=(ProgressBar)findViewById(R.id.loadbuilding);
        building_names= new ArrayList<>();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);

                /*if(General.getSharedPreferences(getBaseContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                    ((com.nbourses.oyeok.activities.BrokerMap) getActivity()).closeCardContainer();
                    ((com.nbourses.oyeok.activities.BrokerMap)getActivity()).openAddListing();
                }else{

                    ((ClientMainActivity)getActivity()).closeAddBuilding();
                    ((ClientMainActivity)getActivity()).openAddListing();
                }*/


            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);
                AppConstants.PROPERTY="Home";

               /* if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                    ((com.nbourses.oyeok.activities.BrokerMap) getActivity()).closeCardContainer();
                }else{

                    ((ClientMainActivity)getActivity()).closeAddBuilding();

                }*/
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
                SearchBuilding();

            }
        });
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
                            adapter= new searchBuilding(building_names,getBaseContext());
                            listView1.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                /*if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                                    loadBuildingDataModel dataModel= building_names.get(position);
                                    General.setSharedPreferences(getContext(),AppConstants.BUILDING_NAME,adapter.getItem(position).getName());
                                    General.setSharedPreferences(getContext(),AppConstants.BUILDING_LOCALITY,adapter.getItem(position).getLocality()+"");
                                    General.setSharedPreferences(getContext(),AppConstants.MY_LAT,adapter.getItem(position).getLat()+"");
                                    General.setSharedPreferences(getContext(),AppConstants.MY_LNG,adapter.getItem(position).getLng()+"");
                                    General.setSharedPreferences(getContext(), AppConstants.MY_CITY,adapter.getItem(position).getCity());
                                    General.setSharedPreferences(getContext(), AppConstants.LL_PM,adapter.getItem(position).getLl_pm());
                                    General.setSharedPreferences(getContext(), AppConstants.OR_PSF,adapter.getItem(position).getOr_psf());
                                    Log.i("Buildingdata", "lat " +adapter.getItem(position).getLl_pm()+" ====== "+adapter.getItem(position).getOr_psf());
                                    //General.setSharedPreferences(getContext(),AppConstants.PROPERTY,adapter.getItem(position).getProperty_type());
                                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);
                                    ((BrokerMap)getActivity()).openAddListingFinalCard();
                                }else{
                                    AddbuildingAPICall(adapter.getItem(position).getName(),adapter.getItem(position).getLat() + "",adapter.getItem(position).getLng() + "",adapter.getItem(position).getId()+"",adapter.getItem(position).getLocality(),adapter.getItem(position).getCity());
                                }*/

                            }
                        });


                        listView1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                        listView1.setItemsCanFocus(false);
                        listView1.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                            @Override
                            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                                mode.setTitle(listView1.getCheckedItemCount() + " Selected");
                                selectedlist.add((loadBuildingDataModel) adapter.getItem(position));

                            }

                            @Override
                            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                                return false;
                            }

                            @Override
                            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                                return false;
                            }

                            @Override
                            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                                return false;
                            }

                            @Override
                            public void onDestroyActionMode(ActionMode mode) {

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


















}
