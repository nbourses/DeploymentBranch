package com.nbourses.oyeok.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kyleduo.switchbutton.SwitchButton;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.activities.ProfileActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.AddListingBorker;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import static com.nbourses.oyeok.R.id.toggleBtn;


public class AddListingFinalCard extends Fragment {


    /*private OnFragmentInteractionListener mListener;*/

    public AddListingFinalCard() {
        // Required empty public constructor
    }


    private TextView txtcalendar,transaction_type,approx_area,config,min,max,selected_rate,building_name,building_locality,tv_rate;
    Calendar myCalendar = Calendar.getInstance();
    private View v;
    private SwitchButton toggleBtn1;
    CheckBox Req,Avail;
    SeekBar seekBar;
    private int minvalue=20000,maxvalue=120000;
    TextView Cancel_final_card;
    String Furnishing,status,tt;
    LinearLayout submit_listing;
    String numberAsString;
    private int llMin, llMax, orMin, orMax,area;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       v= inflater.inflate( R.layout.fragment_add_listing_final_card, container, false );

        toggleBtn1 = (SwitchButton) v.findViewById(toggleBtn);


        txtcalendar=(TextView)v.findViewById(R.id.txtcalendar1);
        transaction_type=(TextView)v.findViewById(R.id.transaction_type);
        updateLabel();
        txtcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker();
            }
        });
        Req=(CheckBox)v.findViewById(R.id.req);
        Avail=(CheckBox) v.findViewById(R.id.availability);
        approx_area=(TextView)v.findViewById(R.id.approx_area);
        config=(TextView) v.findViewById(R.id.config);
        seekBar=(SeekBar) v.findViewById(R.id.seekBar);
        min=(TextView) v.findViewById(R.id.min);
        max = (TextView) v.findViewById(R.id.max);
        selected_rate=(TextView) v.findViewById(R.id.selected_rate);
        submit_listing=(LinearLayout) v.findViewById(R.id.submit_listing);
        Cancel_final_card=(TextView) v.findViewById(R.id.Cancel_final_card);
        building_name=(TextView)v.findViewById(R.id.building_name);
        building_locality=(TextView)v.findViewById(R.id.building_locality);
        tv_rate=(TextView) v.findViewById(R.id.tv_rate);



        config.setText(General.getSharedPreferences(getContext(), AppConstants.PROPERTY_CONFIG).toString());
        approx_area.setText(General.getSharedPreferences(getContext(), AppConstants.APPROX_AREA).toString());
        building_name.setText(General.getSharedPreferences(getContext(), AppConstants.BUILDING_NAME).toString());
        building_locality.setText(General.getSharedPreferences(getContext(), AppConstants.BUILDING_LOCALITY).toString());
         area=Integer.parseInt(General.getSharedPreferences(getContext(), AppConstants.APPROX_AREA));
        getprice();

        toggleBtn1.toggle();
        Req.setChecked(true);
        tt="rental";
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Property_Furnishing_Condition, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                Log.i("confirmaton", "Furnishing================" + Furnishing);
                Furnishing = parent.getItemAtPosition(position).toString();
                Log.i("confirmaton", "Furnishing+++++++++++++++++++" + Furnishing);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
// Another interface callback

            }
        });


        toggleBtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    transaction_type.setText("RESALE");
                    tt="resale";
                    tv_rate.setText("/sq.ft");
                    min.setText(String.valueOf(orMin*area));
                    max.setText(String.valueOf(orMax*area));
                }
                else{
                    transaction_type.setText("RENT");
                    tt="rental";
                    tv_rate.setText("/month");
                    min.setText(String.valueOf(llMin*area));
                    max.setText(String.valueOf(llMax*area));
                }
            }
        });

        seekBar.setMax(maxvalue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {



            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress <= minvalue){
                    progress = minvalue + progress;
                }
                progress=progress/1000;
                progress=progress*1000;
                numberAsString = String.valueOf(progress);
                selected_rate.setText(General.currencyFormat(String.valueOf(numberAsString)).substring(2, General.currencyFormat(String.valueOf(numberAsString)).length()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        Cancel_final_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClientMainActivity)getActivity()).closeAddBuilding();
            }
        });

init();

        return v;
    }







    final  DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.add(Calendar.DATE,1);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            updateLabel();

        }

    };
    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        //In which you need put here
        myCalendar.add(Calendar.DATE,1);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtcalendar.setText(sdf.format(myCalendar.getTime()));
//        PossessionDate=txtcalendar.getText().toString();
    }

    private void displayDatePicker(){
        Calendar now= Calendar.getInstance();
        Calendar now1= now;
        now1.add(Calendar.DATE,1);
        DatePickerDialog dpd = new DatePickerDialog(getContext(), date, now1
                .get(Calendar.YEAR), now1.get(Calendar.MONTH),
                now1.get(Calendar.DAY_OF_MONTH));

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date d = null;
        try {


            d  = sdf.parse(sdf.format(now1.getTime()));
            now.add(Calendar.MONTH,6);
            // now.add(Calendar.DATE,1);

            Date dd = sdf.parse(  now.get(Calendar.DATE)
                    + "/"+ (now.get(Calendar.MONTH) + 1)
                    + "/"

                    + now.get(Calendar.YEAR));

//                    Date dd = sdf.parse("26/1/2017");
            dpd.getDatePicker().setMinDate(d.getTime());
            dpd.getDatePicker().setMaxDate(dd.getTime());
            dpd.show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




private void init(){
    Req.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i("Reqstatus","Reqstatus  : "+isChecked);
            if(!isChecked){
                Req.setChecked(false);
                Avail.setChecked(true);

            }else{
                Req.setChecked(true);
                Avail.setChecked(false);
            }
            Log.i("Reqstatus","Reqstatus  : "+Avail.isChecked()+ " "+Req.isChecked());
        }
    });
    Avail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            Log.i("Reqstatus","Reqstatus  : "+isChecked);

            if(!isChecked){

                Req.setChecked(true);
                Avail.setChecked(false);



            }else{
                Req.setChecked(false);
                Avail.setChecked(true);
            }

            Log.i("Reqstatus","Reqstatus 1 : "+Avail.isChecked()+ " "+Req.isChecked());

        }
    });

    submit_listing.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addListing();
        }
    });
}




   private void addListing(){
       AddListingBorker addListingBorker=new AddListingBorker();
       addListingBorker.setProperty_type(General.getSharedPreferences(getContext(),AppConstants.PROPERTY));
       addListingBorker.setConfig(General.getSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG));
       addListingBorker.setListing_date(myCalendar+"");
       Log.i("AddListingBorker","myCalendar current date"+myCalendar);
       addListingBorker.setLocality("mumbai");
       addListingBorker.setBuilding_name(General.getSharedPreferences(getContext(),AppConstants.BUILDING_NAME));
       addListingBorker.setPossession_date(txtcalendar.getText().toString());
       addListingBorker.setLat(General.getSharedPreferences(getContext(),AppConstants.MY_LAT));
       addListingBorker.setLng(General.getSharedPreferences(getContext(),AppConstants.MY_LNG));
       addListingBorker.setUser_name("sushil");
       Log.i("magic","username   : "+ General.getSharedPreferences(getContext(),AppConstants.NAME)+"  "+General.getSharedPreferences(getContext(),AppConstants.MOBILE_NUMBER));
       addListingBorker.setTt(tt);

       Log.i("Reqstatus","Reqstatus 1 : "+Avail.isChecked()+ " "+Req.isChecked());
       if(Avail.isChecked()){
           Log.i("Reqstatus","Reqstatus 1 inside: "+Avail.isChecked()+ " "+Req.isChecked());

           addListingBorker.setReq_avl("avail");
       }else{
           addListingBorker.setReq_avl("req");
       }
       if(tt.equalsIgnoreCase("rental")){
          addListingBorker.setLl_pm(numberAsString);

           addListingBorker.setOr_psf("0");

       }else{
           addListingBorker.setLl_pm("0");

           addListingBorker.setOr_psf(numberAsString);
       }

       addListingBorker.setSublocality(General.getSharedPreferences(getContext(),AppConstants.BUILDING_LOCALITY));
       addListingBorker.setMobile("+918655201886");



       RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_TEST).build();
       restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
       OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);

       try {
           oyeokApiService.addListing(addListingBorker, new Callback<JsonElement>() {
               @Override
               public void success(JsonElement jsonElement, Response response) {

                   Log.i("magic1","addBuilding success ");




                   JsonObject k = jsonElement.getAsJsonObject();


                   try {
                       /*String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
//                        Log.e(TAG, "RETROFIT SUCCESS " + getPrice.getResponseData().getPrice().getLlMin().toString());

                       JSONObject jsonResponse = new JSONObject(strResponse);

                       JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));

                       Log.i("magic","addBuilding success response "+response);
                       Log.i("magic","addBuilding success jsonResponse "+jsonResponse);*/

                       JSONObject ne = new JSONObject(k.toString());
//                        General.setSharedPreferences(getContext(),AppConstants.token,ne.getString("token"));
//                        setDealStatus3(getContext());
                        Log.i("magic","addBuilding success ne "+ne);
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




private  void getprice()
 {
     if (General.isNetworkAvailable(getContext())) {

        User user = new User();

        user.setDeviceId(General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));
        Log.i("getprice", "getcontext " + General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));
        user.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        user.setUserRole("client");
        user.setLongitude(General.getSharedPreferences(getContext(), AppConstants.MY_LNG));
        user.setProperty_type("home");
        user.setLatitude(General.getSharedPreferences(getContext(), AppConstants.MY_LAT));
        Log.i("getprice", "My_lng" + "  " + General.getSharedPreferences(getContext(), AppConstants.MY_LNG));
        if (SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY) == "")
            user.setLocality("mumbai");
        else
            user.setLocality(General.getSharedPreferences(getContext(), AppConstants.LOCALITY));
        Log.i("getprice", "My_lat" + "  " + General.getSharedPreferences(getContext(), AppConstants.MY_LAT));
        user.setPlatform("android");
        Log.i("getprice", SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
        user.setPincode("400058");
        if (General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
            user.setUserId(General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));

        } else {
            user.setUserId(General.getSharedPreferences(getContext(), AppConstants.USER_ID));
            Log.i("getprice", "user_id " + General.getSharedPreferences(getContext(), AppConstants.USER_ID));
        }

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_102).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        UserApiService userApiService = restAdapter.create(UserApiService.class);


        userApiService.getPrice(user, new retrofit.Callback<JsonElement>() {

            @Override
            public void success(JsonElement jsonElement, Response response) {

                try {
                    General.slowInternetFlag = false;
                    General.t.interrupt();
                    String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
//                        Log.e(TAG, "RETROFIT SUCCESS " + getPrice.getResponseData().getPrice().getLlMin().toString());
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    String errors = jsonResponse.getString("errors");
                    String msg = "";
                    try {
                        msg = jsonResponse.getJSONObject("responseData").getJSONObject("price").getString("message");
                    } catch (Exception e) {

                    }
                    try {
                        msg = jsonResponse.getJSONObject("responseData").getString("message");
                    } catch (Exception e) {

                    }


                    Log.i("Chaman", "ho ghe " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY) + " " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG) + " " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT) + " " + jsonResponse);

                    if (errors.equals("8")) {
                        Log.i("getprice", "error code is 2 ");
                        Log.i("getprice", "error code is 1 " + jsonResponse.toString());
                        Log.i("getprice", "error code is " + errors);
                        Log.i("getprice", "error code is 3 ");
                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .text("You must update profile to proceed.")
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());
                        Intent openProfileActivity = new Intent(getContext(), ProfileActivity.class);
                        openProfileActivity.putExtra("msg", "compulsary");
                        startActivity(openProfileActivity);
                    } else if (msg.equalsIgnoreCase("We dont cater here yet") || msg.equalsIgnoreCase("missing Fields in get price")) {
//                            tvFetchingrates.setText("We only cater in Mumbai");
                    } else {
                        JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));
                        // horizontalPicker.stopScrolling();
                        Log.i("getprice", "Response getprice buildings jsonResponse" + jsonResponse);
                        Log.i("getprice", "Response getprice buildings jsonResponseData" + jsonResponseData);
                        JSONObject price = new JSONObject(jsonResponseData.getString("price"));
                        Log.i("getprice", "Response getprice buildings pricer ");
                        Log.i("getprice", "Response getprice buildings price " + price);
                        JSONArray buildings = new JSONArray(jsonResponseData.getString("buildings"));
                        Log.i("getprice", "Response getprice buildings" + buildings);
                        JSONObject k = new JSONObject(buildings.get(1).toString());
                        Log.i("getprice", "Response getprice buildings yo" + price.getString("ll_min"));
                        if (!price.getString("ll_min").equalsIgnoreCase("")) {
                            if (!price.getString("ll_min").equalsIgnoreCase("0")) {
                                Log.i("tt", "I am here" + 2);
                                Log.i("getprice", "RESPONSEDATAr" + response);
                                llMin = Integer.parseInt(price.getString("ll_min"));
                                llMax = Integer.parseInt(price.getString("ll_max"));
                                Log.i("getprice", "llMin" + llMin);
                                Log.i("getprice", "llMax" + llMax);
                                llMin = 5 * (Math.round(llMin / 5));
                                llMax = 5 * (Math.round(llMax / 5));
                                Log.i("getprice", "llMin" + llMin);
                                Log.i("getprice", "llMax" + llMax);
                                orMin = Integer.parseInt(price.getString("or_min"));
                                orMax = Integer.parseInt(price.getString("or_max"));
                                Log.i("getprice", "orMin" + orMin);
                                Log.i("getprice", "orMax" + orMax);
                                orMin = 500 * (Math.round(orMin / 500));
                                orMax = 500 * (Math.round(orMax / 500));
                                Log.i("getprice", "orMin" + orMin);
                                Log.i("getprice", "orMax" + orMax);
                                min.setText(String.valueOf(General.currencyFormat((llMin*area)+"")));
                                max.setText(String.valueOf(General.currencyFormat((llMax*area)+"")));
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }

 }







   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }*/

    /*@Override
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
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
