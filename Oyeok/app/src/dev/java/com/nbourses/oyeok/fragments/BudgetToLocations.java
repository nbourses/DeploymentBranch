package com.nbourses.oyeok.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.kyleduo.switchbutton.SwitchButton;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.GetLocality;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
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


public class BudgetToLocations extends Fragment {




  /*  @Bind(R.id.radioGroup1)
    Button rg;

    @Bind(R.id.rk1)
    Button rk1;

    @Bind(R.id.bhk1)
    Button bhk1;

    @Bind(R.id.bhk1_5)
    Button bhk1_5;

    @Bind(R.id.bhk2)
    Button bhk2;

    @Bind(R.id.bhk2_5)
    Button bhk2_5;

    @Bind(R.id.bhk3)
    Button bhk3;
    @Bind(R.id.bhk3_5)
    Button bhk3_5;
    @Bind(R.id.bhk4)
    Button bhk4;

    @Bind(R.id.bhk4_5)
    Button bhk4_5;

    @Bind(R.id.bhk5)
    Button bhk5;

    @Bind(R.id.bhk5_5)
    Button bhk5_5;

    @Bind(R.id.bhk6)
    Button bhk6;*/

private RadioGroup rg;
    private Button rk1;
    private Button bhk1;
    private Button bhk1_5;
    private Button bhk2;
    private Button bhk2_5;
    private Button bhk3;
    private Button bhk3_5;
    private Button bhk4;
    private Button bhk4_5;
    private Button bhk5;
    private Button bhk5_5;
    private Button bhk6;
    private TextView calender;
    private LinearLayout submitb;
    private SwitchButton toggleBtn;

    Calendar myCalendar = Calendar.getInstance();

private String configArea = "600";



private Spinner spinner;
    private DiscreteSeekBar seekBar;

    private String Furnishing;
    private int minvalue=20000,maxvalue=120000;
    private String Property;
private String TT;
    private TextView budget;
    private TextView min;
    private TextView max;
    private TextView home;
    private TextView shop;
    private TextView office;
    private TextView industry;
private TextView ptype;
    private int mean;


    public BudgetToLocations() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_budget_to_locations, container,
                false);
        toggleBtn = (SwitchButton) rootView.findViewById(R.id.toggleBtn);
        spinner = (Spinner) rootView.findViewById(R.id.spinner1);
        seekBar = (DiscreteSeekBar) rootView.findViewById(R.id.seekBar1);
        budget = (TextView) rootView.findViewById(R.id.budget1);
        min = (TextView) rootView.findViewById(R.id.min);
        max = (TextView) rootView.findViewById(R.id.max);
        home = (TextView) rootView.findViewById(R.id.btn_home1);
        shop = (TextView) rootView.findViewById(R.id.btn_shop1);
        office = (TextView) rootView.findViewById(R.id.btn_office);
        industry = (TextView) rootView.findViewById(R.id.btn_industrial);
        ptype = (TextView) rootView.findViewById(R.id.p_type);
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroup1);
        rk1 = (Button) rootView.findViewById(R.id.rk1);
        bhk1 = (Button) rootView.findViewById(R.id.bhk1);
        bhk1_5 = (Button) rootView.findViewById(R.id.bhk1_5);
        bhk2 = (Button) rootView.findViewById(R.id.bhk2);
        bhk2_5 = (Button) rootView.findViewById(R.id.bhk2_5);
        bhk3 = (Button) rootView.findViewById(R.id.bhk3);
        bhk3_5 = (Button) rootView.findViewById(R.id.bhk3_5);
        bhk4 = (Button) rootView.findViewById(R.id.bhk4);
        bhk4_5 = (Button) rootView.findViewById(R.id.bhk4_5);
        bhk5 = (Button) rootView.findViewById(R.id.bhk5);
        bhk5_5 = (Button) rootView.findViewById(R.id.bhk5_5);
        bhk6 = (Button) rootView.findViewById(R.id.bhk6);
        calender = (TextView) rootView.findViewById(R.id.cal);
        submitb = (LinearLayout) rootView.findViewById(R.id.submitb);



        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if(bundle.containsKey(AppConstants.TT)) {
               TT = bundle.getString(AppConstants.TT);
            }
        }

        init();
        return rootView;

    }

    private void init(){
        toggleBtn.performClick();

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Property="home" ;
                ptype.setText("Home");
                home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));
                industry.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                shop.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                office.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));

            }
        });

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Property="shop" ;
                ptype.setText("Shop");
                shop.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));
                industry.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                office.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
            }
        });

        office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Property="office" ;
                ptype.setText("Office");
                office.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));
                industry.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                shop.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
            }
        });

        industry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Property="industrial" ;
                ptype.setText("Industrial");
                industry.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));
                shop.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                office.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
            }
        });


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.rk1:
                        configArea = "300";

                        break;
                    case R.id.bhk1:
                        configArea = "600";


                        break;
                    case R.id.bhk1_5:
                        configArea = "800";



                        break;
                    case R.id.bhk2:
                        configArea = "950";

                        break;
                    case R.id.bhk2_5:
                        configArea = "1300";

                        break;
                    case R.id.bhk3:
                        configArea = "1600";

                        break;
                    case R.id.bhk3_5:
                        configArea = "1800";

                        break;
                    case R.id.bhk4:
                        configArea = "2100";

                        break;
                    case R.id.bhk4_5:
                        configArea = "2300";

                        break;
                    case R.id.bhk5:
                        configArea = "2500";


                        break;
                    case R.id.bhk5_5:
                        configArea = "2700";

                        break;
                    case R.id.bhk6:
                        configArea = "2900";

                        break;
                    default:
                        configArea = "600";
                        break;
                }
            }
        });

submitb.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        getLocalities();
    }
});
        budget.setText(General.currencyFormat(10000+"").substring(2, General.currencyFormat(10000+"").length()));


        seekBar.setMax(100000);
        seekBar.setMin(10000);
        seekBar.setProgress(10000);
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar1, int value, boolean fromUser) {
                value=value/1000;
                value=value*1000;
                //budget.setText(General.currencyFormat(value+"").substring(2, General.currencyFormat(value+"").length()));
                Log.i("btol","value is the budget "+value+" "+seekBar.getMax()+" "+seekBar.getMin());

                mean = (seekBar.getMax() - seekBar.getMin())/2;
                Log.i("btol","value is the budget 1 "+mean);
                if(value > mean && value <100000000){
                    int newMin = mean;
                    int newMax = seekBar.getMax()+mean;
                    Log.i("btol","value is the budget 2 "+newMin+" "+newMax);

                    seekBar.setMax(newMax);
                    max.setText(newMax+"");
                    seekBar.setMin(newMin);
                    min.setText(newMin+"");
                    seekBar.setProgress(seekBar.getMin());
                    budget.setText(General.currencyFormat(seekBar.getMin()+"").substring(2, General.currencyFormat(seekBar.getMin()+"").length()));
                    mean = (seekBar.getMax() - seekBar.getMin())/2;
                }else{

                    budget.setText(General.currencyFormat(value+"").substring(2, General.currencyFormat(value+"").length()));
                }


              /* value=value/1000;
                value=value*1000;
                String val=String.valueOf(value);
                //numberAsString = String.valueOf(val);

                budget.setText(General.currencyFormat(value+"").substring(2, General.currencyFormat(value+"").length()));*/


            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });


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


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();


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
        calender.setText(sdf.format(myCalendar.getTime()));
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
    private void getLocalities(){

        try {
        GetLocality g = new GetLocality();
        g.setCity("mumbai");
        g.setGcm_id(General.getSharedPreferences(getContext(),AppConstants.GCM_ID));
        g.setTt(TT);
        g.setPrice(seekBar.getProgress()+"");
        g.setConfig_area(configArea);
        g.setProperty_type("home");
           // g.setProperty_type(ptype.getText().toString());

            /*Gson gson = new Gson();
            String json = gson.toJson(g);
            Log.i("magic","getLocality  json "+json);*/

            Log.i("magic","getLocality 1");
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_11).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
            Log.i("magic","getLocality 2");

            oyeokApiService.getLocality(g, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    try {
                        Log.i("magic","getLocality3");
                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                        JSONObject jsonResponse = new JSONObject(strResponse);
                        Log.i("magic","getLocality3 "+jsonResponse);
                        Log.i("magic","getLocality success "+jsonResponse.getJSONArray("responseData"));

                    } catch (Exception e) {
                        Log.e("TAG", "Caught in the exception getLocality 1"+ e.getMessage());

                    }




                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("magic","getLocality failed "+error);
                }
            });


        }
        catch (Exception e){
            Log.e("TAG", "Caught in the exception getLocality"+ e.getMessage());
        }

    }



}