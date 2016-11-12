package com.nbourses.oyeok.fragments;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.log10;


public class OyeConfirmation extends Fragment {


   private ImageView calendar,proceed_to_oye,addContact;
    private TextView display_date;
    LinearLayout available_sizes;
    LinearLayout confirm_layout_with_edit_button;
    Button editDetails;
    String PossessionDate,Furnishing,my_expectation,Property_Config;
    TextView MyExpectation;
    TextView Property_conf_furnishing,selected_loc_to_oye;
    GoogleMap googleMap;
    Animation slide_arrow;
    private Timer timer;
    private OnFragmentInteractionListener mListener;
    private long mLastClickTime = SystemClock.elapsedRealtime();
    public OyeConfirmation() {
        // Required empty public constructor
    }

    /*
    // TODO: Rename and change types and number of parameters
    public static OyeConfirmation newInstance(String param1, String param2) {

        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Calendar myCalendar = Calendar.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_oye_confirmation, container, false);
//        calendar=(ImageView) view.findViewById(R.id.calendar);
        display_date=(TextView) view.findViewById(R.id.display_date);
        available_sizes=(LinearLayout) view.findViewById(R.id.available_sizes);
        confirm_layout_with_edit_button=(LinearLayout) view.findViewById(R.id.confirm_layout_with_edit_button);
        proceed_to_oye=(ImageView) view.findViewById(R.id.proceed_to_oye);
        addContact=(ImageView) view.findViewById(R.id.addContact);
        editDetails=(Button) view.findViewById(R.id.editDetails);
        MyExpectation=(TextView) view.findViewById(R.id.rate);
        Property_conf_furnishing=(TextView) view.findViewById(R.id.property_config);
        selected_loc_to_oye=(TextView) view.findViewById(R.id.selected_loc_to_oye);
        slide_arrow=(AnimationUtils.loadAnimation(getContext(), R.anim.sliding_arrow));
        StartOyeButtonAnimation();
        updateLabel();

        init();
        SetPropertyDetail();
        return view;
    }


    private void StartOyeButtonAnimation() {

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                proceed_to_oye.startAnimation(slide_arrow);

                            }
                        });
                    }
                }
            }, 2000, 2000);

        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    final  DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                updateLabel();

        }

    };

    private void init(){





//        calendar.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                displayDatePicker();
//                calendar.setVisibility(View.GONE);
//                display_date.setVisibility(View.VISIBLE);
//
//            }
//        });
//        display_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                displayDatePicker();
//                calendar.setVisibility(View.GONE);
//                display_date.setVisibility(View.VISIBLE);
//            }
//        });
//

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              proceed_to_oye.performClick();

            }
        });



        proceed_to_oye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }else{
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (General.getSharedPreferences(getActivity(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
                        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container_OyeConfirmation)).commit();
                        ((ClientMainActivity)getActivity()).signUp();
                        ((ClientMainActivity) getActivity()).closeOyeConfirmation();

                        // General.publishOye(getContext());



                    }else {
                        General.publishOye(getContext());
//                    ((ClientMainActivity) getActivity()).closeOyeConfirmation();
                    }
                }
//                mLastClickTime = SystemClock.elapsedRealtime();
                Log.i("TAG","oye oye 2");



            }
        });

        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ((ClientMainActivity)getActivity()).closeOyeConfirmation();

                ((ClientMainActivity)getActivity()).EditOyeDetails();
               /* FragmentManager fm = getFragmentManager();
                DashboardClientFragment fragm = (DashboardClientFragment) fm.findFragmentById(R.id.container_map);
                fragm.disablepanel(true);*/
               // ((DashboardClientFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.container_map)).disablepanel(true);
               // fragm.disable();
                ///(DashboardClientFragment)disablepanel(true);
            }
        });

    }


    private void updateLabel(){
        myCalendar.add(Calendar.DATE,1);
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        display_date.setText(sdf.format(myCalendar.getTime()));
    }

  /*  private void displayDatePicker(){

        DatePickerDialog dpd = new DatePickerDialog(getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date d = null;
        try {
             Calendar now= Calendar.getInstance();
            d  = sdf.parse(sdf.format(now.getTime()));
            now.add(Calendar.MONTH,6);

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
    }*/



    private BroadcastReceiver SetPropertyDetails1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           my_expectation= intent.getExtras().getString("myExpectation1").toString();
            Log.i("confirmation1","myExpectation1 :="+my_expectation+"  "+intent.getExtras().getString("myExpectation").toString());
           Property_Config= intent.getExtras().getString("propertyConfig1");
            Log.i("confirmation1","PropertyConfig1 := "+Property_Config);
           PossessionDate= intent.getExtras().getString("possessionDate1");
            Log.i("confirmation1","PossessionDate1 := "+PossessionDate);
           Furnishing=intent.getExtras().getString("furnishing1");
            Log.i("confirmaton1","Furnishing1 :="+Furnishing);





        }
    };




    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(SetPropertyDetails1,new IntentFilter((AppConstants.RECEIVE_PROPERTY_DETAILS)));

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(SetPropertyDetails1);

    }


    public void SetPropertyDetail(){
        /*Bundle bundle = this.getArguments();
        Log.i("confirmation1", "myexpectation" + bundle);
        if(bundle.getString("my")!=null) {
            my_expectation = bundle.getString("myexpectation");
            Log.i("confirmation1", "myexpectation" + my_expectation);
        }*/

        String text;
       Log.i("shared data","MY_EXPECTATION : "+General.getSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG));
        my_expectation=General.getSharedPreferences(getContext(),AppConstants.MY_EXPECTATION);
        Property_Config=General.getSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG);
        PossessionDate=General.getSharedPreferences(getContext(),AppConstants.POSSESSION_DATE);
        Furnishing=General.getSharedPreferences(getContext(),AppConstants.FURNISHING);
       int price= Integer.parseInt(my_expectation);
        display_date.setText(PossessionDate);
        if(AppConstants.CURRENT_DEAL_TYPE.equalsIgnoreCase("rent")) {
            if(AppConstants.CUSTOMER_TYPE.equalsIgnoreCase("Owner")) {
                text = "<u><b><big>" + AppConstants.PROPERTY + "</big></u>&nbsp   <small>   Expectation = </small><big>\u20B9 " + numToVal(price) + " </big><small>| Deposit </small><big>\u20B9 " + numToVal(price * 4) + " </big></b><small>(negotiable)</small>";
                MyExpectation.setText(Html.fromHtml(text));
                text = "Send Msg to<b> "+ SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY)+" </b>Brokers to match <b>Tenants</b>";
                selected_loc_to_oye.setText(Html.fromHtml(text));
            } else {
                text = "<u><b><big>" + AppConstants.PROPERTY + "</big></u>  &nbsp <small>   Budget = </small><big>\u20B9 " + numToVal(price) + " </big><small> | Deposit </small><big>\u20B9 " + numToVal(price * 4) + " </big></b><small>(negotiable)<small>";
                MyExpectation.setText(Html.fromHtml(text));
                text = "Send Msg to<b> "+ SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY)+" </b>Brokers to match <b>Properties</b>";
                selected_loc_to_oye.setText(Html.fromHtml(text));
            }
        }
       else {
            if (AppConstants.CUSTOMER_TYPE.equalsIgnoreCase("Owner")) {
                text = "<u><b><big>" + AppConstants.PROPERTY + "</big></u>&nbsp<small>   Expectation = </small><big>\u20B9 " + numToVal(price) + " </big><b><small>(negotiable)</small>";
                MyExpectation.setText(Html.fromHtml(text));
                text = "Send Msg to<b> "+ SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY)+" </b>Brokers to match <b>Tenants</b>";
                selected_loc_to_oye.setText(Html.fromHtml(text));

            }else {
                text = "<u><b><big>" + AppConstants.PROPERTY + "</big></u>&nbsp<small>   Budget = </small><big>\u20B9 " + numToVal(price) + " </big><b><small>(negotiable)</small>";
                MyExpectation.setText(Html.fromHtml(text));
                text = "Send Msg to<b> "+ SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY)+" </b>Brokers to match <b>Properties</b>";
                selected_loc_to_oye.setText(Html.fromHtml(text));

            }
            }

                Property_conf_furnishing.setText(Property_Config+" "+Furnishing);

    }




    String numToVal(int no){
        String str = "",v = "";

        int twoWord = 0,val = 1;

        int c = (no == 0 ? 1 : (int)(log10(no)+1));

        if (c > 8) {

            c = 8;
        }
        if (c%2 == 1){

            c--;
        }

        c--;
        //   int q = Int(pow(Double(10),Double(c)))
        switch(c)
        {
            case 7:
//            if(propertyType)
                val = no/10000000;
//            else
//                val = no/100000;
                no = no%10000000;
                String formatted = String.format("%07d", no);
                formatted = formatted.substring(0,1);

                v = val+"."+formatted;
                str = v+" cr";


                twoWord++;
                break;

            case 5:

                val = no/100000;

                v = val+"";
                no = no%100000;
                String s2 = String.format("%05d", no);
                s2 = s2.substring(0,1);

                if (no>0){
                    str = str+v+"."+s2+"L";
                    twoWord++;
                }else{
                    str = str+v+" L";
                    twoWord++;
                }

                break;

            case 3:
                val = no/1000;
                v = val+"";
                no = no%1000;
                String.format("%05d", no);
                String s3 = String.format("%03d", no);
                s3 = s3.substring(0,1);
                if (no > 0) {
                    str = str+v+"."+s3+" k";
                }else
                {
                    str = str+v+" k";
                }
                break;
            default :
                // print("noToWord Default")
                break;
        }
        return str;
    }

    public void num(int value){
        int price;
        String budget;
   /*     if (value<=99999) {
            if ((price=value%1000)>0) {
                price=value/1000.0f;
                budget= [NSString stringWithFormat:@"%.02fK",price];
            }
            else{
                value=value/1000;
                budget= [NSString stringWithFormat:@"%ldk",(long)value];
            }
        }
        else
        if (value<=9999999) {
            if ((price=value%100000)>0) {
                price=value/100000.0f;
                budget= [NSString stringWithFormat:@"%.02fL",price];
            }
            else{
                value=value/100000;
                budget= [NSString stringWithFormat:@"%ldL",(long)value];
            }
        }
        if(value>9999999)
        {
            if ((price=value%10000000)>0) {
                price=value/10000000.0f;
                budget= [NSString stringWithFormat:@"%.02fCR",price];
            }
            else{
                value=value/10000000;
                budget= [NSString stringWithFormat:@"%ldCR",(long)value];
            }
        }*/

    }



    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
