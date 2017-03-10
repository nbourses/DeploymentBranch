package com.nbourses.oyeok.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.JsonElement;
import com.kyleduo.switchbutton.SwitchButton;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.MyApplication;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.activities.*;
import com.nbourses.oyeok.activities.BrokerMap;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.AddListingBorker;
import com.nbourses.oyeok.realmModels.addBuildingRealm;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import static com.nbourses.oyeok.R.id.defaultView;
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
    DiscreteSeekBar seekBar;
    private int minvalue=20000,maxvalue=120000;
    TextView Cancel_final_card;
    String Furnishing,status,tt;
    int carpet_area;
    LinearLayout submit_listing;
    String numberAsString;
    private int llMin, llMax, orMin, orMax,area;
    TextView txt_req_aval;
    FrameLayout fr_pgbar;
    ProgressBar pg_bar;
    String listing_type="new",listing_id="";
    Bundle b;
    int server_ll_pm=0,server_or_psf=0;
    Spinner spinner;

    boolean portfolio=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MyMaterialTheme);

       // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

       v= localInflater.inflate( R.layout.fragment_add_listing_final_card, container, false );

        toggleBtn1 = (SwitchButton) v.findViewById(toggleBtn);

//        fr_pgbar=(FrameLayout)v.findViewById(R.id.fr_pgBar);
        //pg_bar=(ProgressBar)v.findViewById(R.id.pg_bar);
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
        seekBar=(DiscreteSeekBar) v.findViewById(R.id.seekBar);
        min=(TextView) v.findViewById(R.id.min);
        max = (TextView) v.findViewById(R.id.max);
        selected_rate=(TextView) v.findViewById(R.id.selected_rate);
        submit_listing=(LinearLayout) v.findViewById(R.id.submit_listing);
        Cancel_final_card=(TextView) v.findViewById(R.id.Cancel_final_card);
        building_name=(TextView)v.findViewById(R.id.building_name);
        building_locality=(TextView)v.findViewById(R.id.building_locality);
        tv_rate=(TextView) v.findViewById(R.id.tv_rate);
        txt_req_aval=(TextView) v.findViewById(R.id.txt_req_aval11);
        spinner = (Spinner) v.findViewById(R.id.spinner);

        b=new Bundle();
        b=getArguments();
        if(b!=null&&b.containsKey("edit_listing")) {
            listing_type = b.getString("edit_listing");
            Log.i("timestamp12", "realm data read update  :"+ listing_type);
            listing_id=b.getString("listing_id");

        }
        if(b!=null&&b.containsKey("add_listing")) {
            portfolio=true;
           // listing_type = b.getString("add_listing");
            Log.i("timestamp12", "realm data read update  :"+ listing_type);
           // listing_id=b.getString("listing_id");

        }
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        Log.i("timestamp12", "  tsLong :"+ ts+"    "+System.currentTimeMillis());


        try {
            MyApplication application = (MyApplication) getActivity().getApplication();
            Tracker mTracker = application.getDefaultTracker();

            mTracker.setScreenName("SetBudget");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }

        config.setText(General.getSharedPreferences(getContext(), AppConstants.PROPERTY_CONFIG).toString());
        approx_area.setText(General.getSharedPreferences(getContext(), AppConstants.APPROX_AREA).toString());
        building_name.setText(General.getSharedPreferences(getContext(), AppConstants.BUILDING_NAME).toString());
        building_locality.setText(General.getSharedPreferences(getContext(), AppConstants.BUILDING_LOCALITY).toString());
        if(!General.getSharedPreferences(getContext(), AppConstants.LL_PM).equalsIgnoreCase("")) {
            server_ll_pm = Integer.parseInt(General.getSharedPreferences(getContext(), AppConstants.LL_PM));
            server_or_psf = Integer.parseInt(General.getSharedPreferences(getContext(), AppConstants.OR_PSF));
        }
         area=Integer.parseInt(General.getSharedPreferences(getContext(), AppConstants.APPROX_AREA));
        seekBar.setMax(maxvalue);
        if(General.getSharedPreferences(getContext(),AppConstants.LL_PM).equalsIgnoreCase("")) {
            getprice();
        }else{

                int llvalue= Integer.parseInt(General.getSharedPreferences(getContext(),AppConstants.LL_PM));
            minvalue=(llvalue*area)-((llvalue*area)/2);
            maxvalue=(llvalue*area)+((llvalue*area)/2);
            minvalue=minvalue/1000;
            minvalue=minvalue*1000;
            maxvalue=maxvalue/1000;
            maxvalue=maxvalue*1000;
            min.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((minvalue)+"")));
            max.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((maxvalue)+"")));
            selected_rate.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((minvalue)+"")));
            seekBar.setMax(maxvalue);
            seekBar.setMin(minvalue);
            numberAsString=minvalue+"";
        }

        //deafault values
        toggleBtn1.toggle();
        Req.setChecked(true);
        tt="ll";

        seekBar.setProgress(1000);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Property_Furnishing_Condition, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                    tt="or";
                    tv_rate.setText("");
                    if(General.getSharedPreferences(getContext(),AppConstants.OR_PSF).equalsIgnoreCase("")){
                    minvalue=orMin*area;
                    maxvalue=orMax*area;
                    minvalue=minvalue/1000;
                    minvalue=minvalue*1000;
                    maxvalue=maxvalue/1000;
                    maxvalue=maxvalue*1000;
                    min.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((minvalue)+"")));
                    max.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((maxvalue)+"")));
//                    selected_rate.setText(String.valueOf(General.currencyFormat((orMin*area)+"")));
                    selected_rate.setText(General.currencyFormat(String.valueOf(minvalue)).substring(2, General.currencyFormat(String.valueOf(minvalue)).length()));
                    seekBar.setMax(maxvalue);
                    seekBar.setMin(minvalue);
                    }else{
                        setMinMaxValue();
                    }
                }
                else{
                    transaction_type.setText("RENT");
                    tt="ll";
                    tv_rate.setText("/month");
                    if(General.getSharedPreferences(getContext(),AppConstants.LL_PM).equalsIgnoreCase("")) {
                        int price = llMin * area;
                        minvalue = llMin * area;
                        maxvalue = llMax * area;
                        minvalue = minvalue / 1000;
                        minvalue = minvalue * 1000;
                        maxvalue = maxvalue / 1000;
                        maxvalue = maxvalue * 1000;
                        min.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((minvalue) + "")));
                        max.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((maxvalue) + "")));
//                    selected_rate.setText(String.valueOf(General.currencyFormat((llMin*area)+"")));
                        selected_rate.setText(General.currencyFormat(String.valueOf(minvalue)).substring(2, General.currencyFormat(String.valueOf(minvalue)).length()));
                        seekBar.setMax(maxvalue);
                        seekBar.setMin(minvalue);
                    }else{
                        setMinMaxValue();
                    }
                }
            }
        });

        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                value=value/1000;
                value=value*1000;
                String val=String.valueOf(value);
                numberAsString = String.valueOf(val);
                selected_rate.setText(General.currencyFormat(String.valueOf(numberAsString)).substring(2, General.currencyFormat(String.valueOf(numberAsString)).length()));

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });


        /*seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int  p=0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("onProgress","onProgressChanged"+progress);

                Log.i("onProgress","onProgressChanged 1"+progress);

                progress=progress/1000;
                progress=progress*1000;
                Log.i("onProgress","onProgressChanged 2"+progress);
                if(progress <= minvalue){
                   p= minvalue + progress;
                }
                numberAsString = String.valueOf(p);
                selected_rate.setText(General.currencyFormat(String.valueOf(numberAsString)).substring(2, General.currencyFormat(String.valueOf(numberAsString)).length()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/



        Cancel_final_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(portfolio){
                    Intent in = new Intent(getContext(), MyPortfolioActivity.class);
                    startActivity(in);
                }else
                if(listing_type.equalsIgnoreCase("new"))
                    ((BrokerMap)getActivity()).closeCardContainer();
                else{
                    ((BrokerListingActivity)getActivity()).closeCardContainer();
                }

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
            Log.i("datecheck ","Calendar  before == "+Calendar.MONTH+"   monthOfYear : "+monthOfYear+" Calendar.DATE " +Calendar.DATE+"Calendar.DAY_OF_MONTH "+Calendar.DAY_OF_MONTH+" dayOfMonth "+dayOfMonth);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            //myCalendar.add(Calendar.DATE,1);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
           // myCalendar.add(Calendar.DAY_OF_MONTH,1);
            Log.i("datecheck ","Calendar after   == "+Calendar.MONTH+"   monthOfYear : "+monthOfYear+" Calendar.DATE " +Calendar.DATE+"Calendar.DAY_OF_MONTH "+Calendar.DAY_OF_MONTH+" dayOfMonth "+dayOfMonth);

            updateLabel();

        }

    };
    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        //In which you need put here
        //myCalendar.add(Calendar.DATE,1);
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
            now.add(Calendar.MONTH,7);
            // now.add(Calendar.DATE,1);

            Date dd = sdf.parse(  now.get(Calendar.DATE)
                    + "/"+ (now.get(Calendar.MONTH))
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
                txt_req_aval.setText("Availability");


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

                txt_req_aval.setText("Requirement");
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
           // fr_pgbar.setVisibility(View.VISIBLE);
            addListing();

            submit_listing.setClickable(false);
            submit_listing.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    });
}




   private void addListing(){
       try {
           MyApplication application = (MyApplication) getActivity().getApplication();
           Tracker mTracker = application.getDefaultTracker();

           mTracker.setScreenName("ListingAPI");
           mTracker.send(new HitBuilders.ScreenViewBuilder().build());
       } catch (Exception e) {
           e.printStackTrace();
       }

       AddListingBorker addListingBorker=new AddListingBorker();
       addListingBorker.setProperty_type(General.getSharedPreferences(getContext(),AppConstants.PROPERTY));
       addListingBorker.setConfig(General.getSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG));
       addListingBorker.setListing_date(System.currentTimeMillis()+"");
       addListingBorker.setListing_id(listing_id);
       addListingBorker.setAdd_type(General.getSharedPreferences(getContext(), AppConstants.ADD_TYPE));  //listing || building
       addListingBorker.setBuilding_id(General.getSharedPreferences(getContext(),AppConstants.BUILDING_ID));
       Log.i("AddListingBorker","myCalendar current date"+System.currentTimeMillis()+" "+General.getSharedPreferences(getContext(), AppConstants.ADD_TYPE)+" == "+General.getSharedPreferences(getContext(),AppConstants.BUILDING_ID)+" "+General.getSharedPreferences(getContext(),AppConstants.BUILDING_NAME));
       addListingBorker.setCity("Mumbai");
       if(Furnishing.equalsIgnoreCase("fully-furnished")){
       addListingBorker.setFurnishing("ff");
       }else  if(Furnishing.equalsIgnoreCase("un-furnished")) {
           addListingBorker.setFurnishing("uf");

       }else{
           addListingBorker.setFurnishing("sf");

       }

       try {
           String timestamp1=General.DateToTimeStamp(txtcalendar.getText().toString()).getTime()+"";
           Log.i("AddListingBorker","myCalendar current date : "+timestamp1);
           addListingBorker.setPossession_date(timestamp1);
       } catch (ParseException e) {
           e.printStackTrace();
           Log.i("AddListingBorker","myCalendar current date : "+e);
       }
       addListingBorker.setBuilding_name(General.getSharedPreferences(getContext(),AppConstants.BUILDING_NAME));
       addListingBorker.setLat(General.getSharedPreferences(getContext(),AppConstants.MY_LAT));
       addListingBorker.setLng(General.getSharedPreferences(getContext(),AppConstants.MY_LNG));
       addListingBorker.setUser_name(General.getSharedPreferences(getContext(),AppConstants.NAME));
       Log.i("magic","username   : "+ General.getSharedPreferences(getContext(),AppConstants.NAME)+"  "+General.getSharedPreferences(getContext(),AppConstants.MOBILE_NUMBER));
       addListingBorker.setTt(tt);
       addListingBorker.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
        carpet_area=Integer.parseInt(approx_area.getText().toString());
       addListingBorker.setCarpet_area(carpet_area);
       Log.i("Reqstatus","Reqstatus 1 : "+Avail.isChecked()+ " "+Req.isChecked()+"numberAsString : "+numberAsString);
       if(Avail.isChecked()){
           Log.i("Reqstatus","Reqstatus 1 inside: "+Avail.isChecked()+ " "+Req.isChecked());

           addListingBorker.setReq_avl("avl");
       }else{
           addListingBorker.setReq_avl("req");
       }
       if(tt.equalsIgnoreCase("ll")){
          addListingBorker.setLl_pm(numberAsString);

           addListingBorker.setOr_psf("0");

       }else{
           addListingBorker.setLl_pm("0");

           addListingBorker.setOr_psf(numberAsString);
       }
       if(General.getSharedPreferences(getContext(),AppConstants.BUILDING_LOCALITY) == "")
       addListingBorker.setLocality("");
       else
           addListingBorker.setLocality(General.getSharedPreferences(getContext(),AppConstants.BUILDING_LOCALITY));


      // addListingBorker.setMobile("+918655201886");
       addListingBorker.setMobile(General.getSharedPreferences(getContext(),AppConstants.MOBILE_NUMBER));



       RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_11).build();
       restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
       OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);

       try {
           oyeokApiService.addListing(addListingBorker, new Callback<JsonElement>() {
               @Override
               public void success(JsonElement jsonElement, Response response) {
                   String ids="";
                   try {
                       String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                       JSONObject jsonResponse = new JSONObject(strResponse);

                       Log.i("magic","addBuildingRealm success "+jsonResponse.getJSONObject("responseData"));
                       Log.i("magic","addBuildingRealm success "+jsonResponse.getJSONObject("responseData").getString("listing_id"));
                       ids= jsonResponse.getJSONObject("responseData").getString("listing_id");
                   } catch (Exception e) {Log.i("magic","msg "+e);}
                   Log.i("magic","addBuildingRealm success ids value :  "+ids);
                   AddBuildingDataToRealm(ids);


               }

               @Override
               public void failure(RetrofitError error) {
                   Log.i("magic","addBuildingRealm failed "+error);
               }
           });


       }
       catch (Exception e){
           Log.e("TAG", "Caught in the the"+ e.getMessage());
       }


   }


    public void AddBuildingDataToRealm(String id) {

        try {
            MyApplication application = (MyApplication) getActivity().getApplication();
            Tracker mTracker = application.getDefaultTracker();

            mTracker.setScreenName("ListingSuccess");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Realm myRealm = General.realmconfig(getContext());
        addBuildingRealm add_Building = new addBuildingRealm();
        add_Building.setTimestamp(String.valueOf(System.currentTimeMillis()));
        add_Building.setBuilding_name(General.getSharedPreferences(getContext(),AppConstants.BUILDING_NAME));
        add_Building.setConfig(General.getSharedPreferences(getContext(), AppConstants.PROPERTY_CONFIG));
        add_Building.setProperty_type(AppConstants.PROPERTY);
        add_Building.setLat(General.getSharedPreferences(getContext(),AppConstants.MY_LAT));
        add_Building.setLng(General.getSharedPreferences(getContext(),AppConstants.MY_LNG));
        add_Building.setId(id);
        add_Building.setType("LIST");
        add_Building.setDisplay_type(null);
        add_Building.setServer_ll_pm(server_ll_pm);
        add_Building.setServer_or_psf(server_or_psf);
        int area=carpet_area;
        Log.i("magic","reached "+numberAsString+"  area : "+area+" id : "+id);

       String growthrate=null;
        //Log.i("magic","reached "+p);
       // percentageCalculator
        if(tt.equalsIgnoreCase("ll")){
            int p= 0;

                p = Integer.parseInt(General.getSharedPreferences(getContext(), AppConstants.LL_PM));
                growthrate= percentageCalculator(Integer.parseInt(numberAsString),p*area);
                add_Building.setMarket_rate(p);
             add_Building.setGrowth_rate(growthrate);

            add_Building.setLl_pm(Integer.parseInt(numberAsString));

            add_Building.setOr_psf(0);
            add_Building.setTt("ll");

        }else{
            //int p=Integer.parseInt(numberAsString)/area;
                int p= 0;

                p = Integer.parseInt(General.getSharedPreferences(getContext(), AppConstants.OR_PSF));
                growthrate= percentageCalculator(Integer.parseInt(numberAsString),p*area);
                add_Building.setMarket_rate(p);
            add_Building.setGrowth_rate(growthrate);
            add_Building.setLl_pm(0);
            add_Building.setOr_psf(Integer.parseInt(numberAsString));
            add_Building.setTt("or");


        }
        Log.i("percent","growthrate : "+growthrate);
       // add_Building.setGrowth_rate(growthrate);
        add_Building.setPossession_date(txtcalendar.getText().toString());
        if(Avail.isChecked()){
            Log.i("Reqstatus","Reqstatus 1 inside: "+Avail.isChecked()+ " "+Req.isChecked());

            add_Building.setReq_avl("Avail.");
        }else{
            add_Building.setReq_avl("Req.");
        }

        if(Furnishing.equalsIgnoreCase("fully-furnished")){
            add_Building.setFurnishing("FF");
        }else  if(Furnishing.equalsIgnoreCase("un-furnished")) {
            add_Building.setFurnishing("UF");

        }else{
            add_Building.setFurnishing("SF");

        }
        if(General.getSharedPreferences(getContext(),AppConstants.BUILDING_LOCALITY).equalsIgnoreCase(""))
        {
            Log.i("AddListingBorker","myCalendar current date  12"+General.getSharedPreferences(getContext(),AppConstants.BUILDING_LOCALITY));

            add_Building.setSublocality("Mumbai");

        }
        else {
            Log.i("AddListingBorker","myCalendar current date  22 :"+General.getSharedPreferences(getContext(),AppConstants.BUILDING_LOCALITY));

            add_Building.setSublocality(General.getSharedPreferences(getContext(), AppConstants.BUILDING_LOCALITY));
        }
        Log.i("AddListingBorker","myCalendar current date"+add_Building.getSublocality());

        myRealm.beginTransaction();
        myRealm.copyToRealmOrUpdate(add_Building);
//        myRealm.copyToRealmOrUpdate((Iterable<RealmObject>) myPortfolioModel);
        myRealm.commitTransaction();
       // Log.i("magic","reached "+General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER));
//        if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
            Intent in = new Intent(getContext(), BrokerListingActivity.class);
            in.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP /*|
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK*/);
            startActivity(in);
      /*  }else {
            Intent in = new Intent(getContext(), MyPortfolioActivity.class);
            in.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }*/

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
            user.setLocality("Mumbai");
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

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_11).build();
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
                                orMin = 1000 * (Math.round(orMin / 1000));
                                orMax = 1000 * (Math.round(orMax / 1000));
                                Log.i("getprice", "orMin" + orMin);
                                Log.i("getprice", "orMax" + orMax);

                                minvalue=llMin*area;
                                maxvalue=llMax*area;
                                minvalue=minvalue/1000;
                                minvalue=minvalue*1000;
                                maxvalue=maxvalue/1000;
                                maxvalue=maxvalue*1000;
                                min.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((minvalue)+"")));
                                max.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((maxvalue)+"")));
                                selected_rate.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((minvalue)+"")));
                                seekBar.setMax(maxvalue);
                                seekBar.setMin(minvalue);
                                numberAsString=minvalue+"";
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


private void  setMinMaxValue(){
/*
    if(General.getSharedPreferences(getContext(),AppConstants.LL_PM).equalsIgnoreCase("")) {
        getprice();
    }else{
*/

        if(tt.equalsIgnoreCase("ll")) {
            int llvalue = Integer.parseInt(General.getSharedPreferences(getContext(), AppConstants.LL_PM));
            minvalue = (llvalue * area) - ((llvalue * area) / 2);
            maxvalue = (llvalue * area) + ((llvalue * area) / 2);
            minvalue = minvalue / 1000;
            minvalue = minvalue * 1000;
            maxvalue = maxvalue / 1000;
            maxvalue = maxvalue * 1000;
            min.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((minvalue) + "")));
            max.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((maxvalue) + "")));
            selected_rate.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((minvalue) + "")));
            seekBar.setMax(maxvalue);
            seekBar.setMin(minvalue);
            numberAsString = minvalue + "";
        }else{
            int llvalue = Integer.parseInt(General.getSharedPreferences(getContext(), AppConstants.OR_PSF));
            minvalue = (llvalue * area) - ((llvalue * area) / 2);
            maxvalue = (llvalue * area) + ((llvalue * area) / 2);
            minvalue = minvalue / 1000;
            minvalue = minvalue * 1000;
            maxvalue = maxvalue / 1000;
            maxvalue = maxvalue * 1000;
            min.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((minvalue) + "")));
            max.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((maxvalue) + "")));
            selected_rate.setText(String.valueOf(General.currencyFormatWithoutRupeeSymbol((minvalue) + "")));
            seekBar.setMax(maxvalue);
            seekBar.setMin(minvalue);
            numberAsString = minvalue + "";
        }
    //}
}







}
