package com.nbourses.oyeok.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.adapters.addBuildingAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.SearchBuildingModel;
import com.nbourses.oyeok.realmModels.addBuilding;

import org.json.JSONException;
import org.json.JSONObject;

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
    addBuildingAdapter adapter;
    private Realm realm;
    ImageView add;
    String name;
    private TextView dialog;
    LinearLayout add_b;
    private SideBar sideBar;
    private Timer clockTickTimer;
    int count=3;

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
        adapter = new addBuildingAdapter(getContext(),1);
        listView1.setAdapter(adapter);
        realm = General.realmconfig(getContext());
        adapter.setResults(realm.where(addBuilding.class).findAll());
        add_b=(LinearLayout)v.findViewById(R.id.add_b);
        usertext=(TextView)v.findViewById(R.id.usertext);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClientMainActivity)getActivity()).closeAddBuilding();
                ((ClientMainActivity)getActivity()).openAddListing();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.PROPERTY="Home";
                ((ClientMainActivity)getActivity()).closeAddBuilding();
            }
        });


        inputSearch1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                if(TT=="LL"){
                    Log.i( "portfolio","onTextChanged  LL : "+cs );

                    usertext.setText("'"+cs+"'");
                    adapter.setResults( realm.where(addBuilding.class) //implicit AND
                            .beginGroup()
                            .contains("Building_name", cs.toString(),false)
                            .endGroup()
                            .findAll() );
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
                    ((ClientMainActivity) getActivity()).setlocation(name);
                }

            }
        });




        sideBar = (SideBar) v.findViewById(R.id.sideIndex);
//        dialog = (TextView) v.findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //Set the right touch monitor
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //The position of the first occurrence of the letter
                /*int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }*/

            }
        });


         init();

        return v;
    }





   private void  init(){
      listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {

                 General.setSharedPreferences(getContext(),AppConstants.BUILDING_NAME,adapter.getItem(position).getBuilding_name());
                 General.setSharedPreferences(getContext(),AppConstants.BUILDING_LOCALITY,adapter.getItem(position).getLocality());
                 General.setSharedPreferences(getContext(),AppConstants.MY_LAT,adapter.getItem(position).getLat());
                 General.setSharedPreferences(getContext(),AppConstants.MY_LNG,adapter.getItem(position).getLng());
//                 General.setSharedPreferences(getContext(),AppConstants.PROPERTY,adapter.getItem(position).getProperty_type());



                 ((ClientMainActivity)getActivity()).openAddListingFinalCard();
             }
          }
      });
   }

    public void SearchBuilding()
    {
        Log.i("updateStatus CALLED","updateStatus success called ");
        SearchBuildingModel searchBuildingModel = new SearchBuildingModel();
        searchBuildingModel.setBuilding("Krishna");
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_TEST).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

//        UserApiService userApiService = restAdapter.create(UserApiService.class);


        /*userApiService.addBuilding(AddBuildingModel, new retrofit.Callback<JsonElement>() {*/




        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


        try {
            oyeokApiService.SearchBuilding(searchBuildingModel, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    Log.i("magic1","addBuilding success ");




                    JsonObject k = jsonElement.getAsJsonObject();


                    try {
                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
//                        Log.e(TAG, "RETROFIT SUCCESS " + getPrice.getResponseData().getPrice().getLlMin().toString());

                         JSONObject jsonResponse = new JSONObject(strResponse);

                        JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));

                        Log.i("magic","addBuilding success response "+response);
                        Log.i("magic","addBuilding success jsonResponse "+jsonResponse);

                        JSONObject ne = new JSONObject(k.toString());
//                        General.setSharedPreferences(getContext(),AppConstants.token,ne.getString("token"));
//                        setDealStatus3(getContext());
//                        Log.i("magic","addBuilding success ne "+ne);
//                        JSONObject re = new JSONObject(jsonResponse.getString("responseData"));
                        /*Log.i("magic","addBuilding success re data "+re);
                        Log.i("magic","addBuilding success re "+re.length());*/



                    }
                    catch (JSONException e) {
                        Log.e("TAG", e.getMessage());
                        Log.i("magic","addBuilding Failed1 "+e.getMessage());
                    }




                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("magic","addBuilding failed "+error);
                }
            });


        }
        catch (Exception e){
            Log.e("TAG", "Caught in the the"+ e.getMessage());
        }

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



  /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }*/

/*    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
