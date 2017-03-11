package com.nbourses.oyeok.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.activities.BrokerMap;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


public class BuildingOyeConfirmation extends Fragment {

    public BuildingOyeConfirmation() {

    }

    View v;
    TextView conf1,conf2,conf3,conf4,conf;
    TextView listingcount,show_more,btn_listing,sharing;
    private RadioGroup radioGroup1;
    String  listing,portal,transaction,approx_area,config,Broker_count;
    Bundle  data;
    int approx_area1,price;
    boolean fisrtconf=true;
    int ll_pm,or_psf,formattedPrice;

    private RadioButton rk1,bhk1,bhk1_5,bhk2,bhk2_5,bhk3,bhk3_5,bhk4,bhk4_5,bhk5,bhk5_5,bhk6,commanbhk;

  /*  private BroadcastReceiver RecieveListingData =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            listing=intent.getExtras().getString( "listing" );
            transaction=intent.getExtras().getString( "transaction" );
            portal=intent.getExtras().getString( "portal" );
            Log.i("sendDataToOye"," listing  : "+listing+" "+intent.getExtras());
            String text;
            text="<font color=#2dc4b6>In last 6 Months : <b><font color=#2dc4b6><big>"+transaction+" Txns</big></font> | <font color=#ff9f1c><big>"+listing+"</big></font> listing on <big>"+portal+" </big>portals<b></font>";
            listingcount.setText( Html.fromHtml(text));
        }
    };*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v=inflater.inflate(R.layout.fragment_building_oye_confirmation, container, false);
        /*conf1=(TextView) v.findViewById(R.id.conf1);
        conf2=(TextView) v.findViewById(R.id.conf2);
        conf3=(TextView) v.findViewById(R.id.conf3);*/
        listingcount=(TextView ) v.findViewById(R.id.listingcount);
        sharing=(TextView) v.findViewById(R.id.sharing);
        show_more=(TextView) v.findViewById(R.id.show_more);
        btn_listing=(TextView) v.findViewById(R.id.btn_listing);
        sharing=(TextView) v.findViewById(R.id.sharing);

        rk1=(RadioButton) v.findViewById( R.id.rk1 );
        bhk1=(RadioButton) v.findViewById( R.id.bhk1 );
        bhk1_5=(RadioButton) v.findViewById( R.id.bhk1_5 );
        bhk2=(RadioButton) v.findViewById( R.id.bhk2 );
        bhk2_5=(RadioButton) v.findViewById( R.id.bhk2_5 );
        bhk3=(RadioButton) v.findViewById( R.id.bhk3 );
        bhk3_5=(RadioButton) v.findViewById( R.id.bhk3_5 );
        bhk4=(RadioButton) v.findViewById( R.id.bhk4 );
        bhk4_5=(RadioButton) v.findViewById( R.id.bhk4_5 );
        bhk5=(RadioButton) v.findViewById( R.id.bhk5 );
        bhk5_5=(RadioButton) v.findViewById( R.id.bhk5_5 );
        bhk6=(RadioButton) v.findViewById( R.id.bhk6 );
        radioGroup1 = (RadioGroup) v.findViewById(R.id.radioGroup1);

       // if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
            //sharing.setVisibility(View.GONE);
            show_more.setVisibility(View.VISIBLE);
            btn_listing.setVisibility(View.VISIBLE);
        //}
        show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show_more.getText().toString().equalsIgnoreCase("Show More")) {
                    showconfigs();
                }
                else{
                    Hideconfigs();
                }
            }
        });
        init();
//        commanbhk=bhk2;

        /*conf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//             conf1.setBackground(R.drawable.gradient_button_border);
            }
        });
        conf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        conf3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        int center = (width - rk1.getWidth())/2;

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                int center = (width - rk1.getWidth())/2;
                switch (checkedId)
                {
                    case R.id.rk1:
                        approx_area="300";
                        formattedPrice=General.getFormatedPrice("1rk",ll_pm);
                        setEMI();
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"1Rk");
                        break;
                    case R.id.bhk1:
                        approx_area="600";
                        formattedPrice=General.getFormatedPrice("1bhk",ll_pm);
                        setEMI();
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"1BHK");

                        break;
                    case R.id.bhk1_5:
                        approx_area="800";
                        formattedPrice=General.getFormatedPrice("1.5bhk",ll_pm);
                        setEMI();
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"1.5BHK");

                        break;
                    case R.id.bhk2:
                        approx_area="950";
                        formattedPrice=General.getFormatedPrice("2bhk",ll_pm);
                        setEMI();
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"2BHK");

                        break;
                    case R.id.bhk2_5:
                        approx_area="1300";
                        formattedPrice=General.getFormatedPrice("2.5bhk",ll_pm);
                        setEMI();
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"2.5BHK");

                        break;
                    case R.id.bhk3:
                        approx_area="1600";
                        formattedPrice=General.getFormatedPrice("3bhk",ll_pm);
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"3BHK");
                        setEMI();
                        break;
                    case R.id.bhk3_5:
                        approx_area="1800";
                        formattedPrice=General.getFormatedPrice("3.5bhk",ll_pm);
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"3.5BHK");
                        setEMI();
                        break;
                    case R.id.bhk4:
                        approx_area="2100";
                        formattedPrice=General.getFormatedPrice("4bhk",ll_pm);
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"4BHK");
                        setEMI();
                        break;
                    case R.id.bhk4_5:
                        approx_area="2300";
                        formattedPrice=General.getFormatedPrice("4.5bhk",ll_pm);
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"4.5BHK");
                        setEMI();
                        break;
                    case R.id.bhk5:
                        approx_area="2500";
                        formattedPrice=General.getFormatedPrice("5bhk",ll_pm);
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"5BHK");
                        setEMI();
                        break;
                    case R.id.bhk5_5:
                        approx_area="2700";
                        formattedPrice=General.getFormatedPrice("5.5bhk",ll_pm);
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"5.5BHK");
                        setEMI();
                        break;
                    case R.id.bhk6:
                        approx_area="2900";
                        formattedPrice=General.getFormatedPrice("6bhk",ll_pm);
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"6BHK");
                        setEMI();
                        break;
                    /*default:
                        break;*/
                }

                Intent intent=new Intent(AppConstants.DISPLAY_CONFIG_BASED_PRICE);
                intent.putExtra("area1",approx_area);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        });
        return v;
    }

    @Override
    public void onPause() {
       // LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(RecieveListingData);
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(Displayconf);

        super.onPause();
    }

    @Override
    public void onResume() {
       // LocalBroadcastManager.getInstance(getContext()).registerReceiver(RecieveListingData, new IntentFilter( AppConstants.SEND_LISTING));
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(Displayconf, new IntentFilter( AppConstants.DISPLAY_BUILDING_CONF));

        super.onResume();

    }

    private void init(){
        data=getArguments();
        listing=data.getString( "listing" );
        transaction=data.getString( "transaction" );
        portal=data.getString( "portal" );
        ll_pm=data.getInt("ll_pm");
        or_psf=data.getInt("or_psf");
        Broker_count=data.getString("count");
        Log.i("sendDataToOye"," listing  : "+listing+" ");
        String text;
        text="<font color=#000000>In last 6 Months : <b><font color=#2dc4b6><big>"+transaction+" Txns</big></font> | <font color=#ff9f1c><big>"+listing+"</big></font> listing on <font color=#ff9f1c><big>"+portal+" </big></font>portals<b></font>";
        listingcount.setText( Html.fromHtml(text));
        config=data.getString("config");
        SplitString(config);
        if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
            btn_listing.setBackground(getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
            setEMI();
           // btn_listing.setText(Html.fromHtml(text));

        }else{
            sharing.setVisibility(View.GONE);

        }


        btn_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text;
                if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                    if(General.getSharedPreferences(getContext(),AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")){
                        ((BrokerMap)getActivity()).OpenSignUpFrag();

                    }
                    else {
                        AppConstants.PROPERTY = "home";
                        General.setSharedPreferences(getContext(), AppConstants.PROPERTY, "home");
                        General.setSharedPreferences(getContext(), AppConstants.APPROX_AREA, approx_area);
                        ((BrokerMap) getActivity()).openAddListingFinalCard();
                    }
                }else{
                   // AppConstants.PROPERTY = "home";
                    AppConstants.CONFIG="";
                    //text="<html>Connect'.........,, : <font color=#ff9f1c><big>32</big></font> Nearby Brokers</html>";
                    //btn_listing.setText(Html.fromHtml(text));
                    ((ClientMainActivity)getActivity()).CloseBuildingOye();
                    ((ClientMainActivity)getActivity()).openOyeSreen();
                }
            }
        });

    }

  /* private BroadcastReceiver Displayconf=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            intent.getExtras().getString("");
        }
    };*/


    private void   setEMI(){
        if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
            String text1;
            text1 = "<html>Connect : <font color=#ff9f1c>"+Broker_count+"</font> Nearby Brokers</html>";
            btn_listing.setText(Html.fromHtml(text1));
            if (AppConstants.CURRENT_DEAL_TYPE.equalsIgnoreCase("rent")) {
                int emi = (formattedPrice * 4) / 3;
                sharing.setText(Html.fromHtml("<html>EMI Deposits starts at : <big>" + General.currencyFormat(emi + "") + "<big><html>"));

            } else {
                if(approx_area==null){
                    approx_area="950";
                }
                try {
                    BigDecimal emi = calcEMI(BigDecimal.valueOf(or_psf * Integer.parseInt(approx_area)), BigDecimal.valueOf(240), BigDecimal.valueOf(9.5));
                    sharing.setText(Html.fromHtml("<html>EMI starts at : <big>" + General.currencyFormat(emi + "") + "<big><html>"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                /*text1="<html>Connect : <font color=#ff9f1c>32</font> Nearby Brokers</html>";
                btn_listing.setText(Html.fromHtml(text1));
*/
            }
        }

    }

   private void Hideconfigs(){
       rk1.setVisibility(View.GONE);
       bhk1.setVisibility(View.GONE);
       bhk1_5.setVisibility(View.GONE);
       bhk2.setVisibility(View.GONE);
       bhk2_5.setVisibility(View.GONE);
       bhk3.setVisibility(View.GONE);
       bhk3_5.setVisibility(View.GONE);
       bhk4.setVisibility(View.GONE);
       bhk4_5.setVisibility(View.GONE);
       bhk5.setVisibility(View.GONE);
       bhk5_5.setVisibility(View.GONE);
       bhk6.setVisibility(View.GONE);
       //show_more.setVisibility(View.VISIBLE);
       show_more.setText("Show More");
       SplitString(config);
   }
    private void showconfigs(){
        rk1.setVisibility(View.VISIBLE);
        bhk1.setVisibility(View.VISIBLE);
        bhk1_5.setVisibility(View.VISIBLE);
        bhk2.setVisibility(View.VISIBLE);
        bhk2_5.setVisibility(View.VISIBLE);
        bhk3.setVisibility(View.VISIBLE);
        bhk3_5.setVisibility(View.VISIBLE);
        bhk4.setVisibility(View.VISIBLE);
        bhk4_5.setVisibility(View.VISIBLE);
        bhk5.setVisibility(View.VISIBLE);
        bhk5_5.setVisibility(View.VISIBLE);
        bhk6.setVisibility(View.VISIBLE);
        show_more.setVisibility(View.VISIBLE);
        show_more.setText("Show Less");
    }

    private void SplitString(String conf){
        Log.i("configdata", "getting and parsing config data 1 : " + conf+" ");
        try{

            if(conf.contains("??")) {
                Log.i("configdata", "getting and parsing config data above :::: " );
                String[] parts = conf.split("[??]");
                Log.i("configdata", "getting and parsing config data length" + parts.length+parts[0]);

                String part1 = parts[0];
                String part2 = parts[1];
                Log.i("configdata", "getting and parsing config data" + parts.length + " " + part1 + " " + part2);

                for(int i=0;i<parts.length;i++){
                    plotconf(parts[i]);
                }
            } else{
                plotconf(conf);
                formattedPrice=General.getFormatedPrice(conf,ll_pm);
               /* if(fisrtconf==true){
                    Log.i("entered","got it "+conf);
                    commanbhk.setChecked(true);
                    fisrtconf=false;
                }*/
                Log.i("configdata", "getting and parsing config data 1 : " + conf);
            }
        }catch (Exception e){}
    }


          void plotconf(String conf){

              Log.i("configdata", "getting and parsing config data 123 : " + conf);
              if(conf.equalsIgnoreCase("1rk")){
                  rk1.setVisibility(View.VISIBLE);
                  commanbhk=rk1;
                  approx_area="300";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="1rk";
                  }else{
                      AppConstants.CONFIG="300";
                  }
              }else
              if(conf.equalsIgnoreCase("1bhk")){
                  bhk1.setVisibility(View.VISIBLE);
                  commanbhk=bhk1;
                  approx_area="600";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="1bhk";
                  }else{
                      AppConstants.CONFIG="600";
                  }
              }else
              if(conf.equalsIgnoreCase("1.5bhk")){
                  bhk1_5.setVisibility(View.VISIBLE);
                  commanbhk=bhk1_5;
                  approx_area="800";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="1.5bhk";
                  }else{
                      AppConstants.CONFIG=approx_area;
                  }
              }else
              if(conf.equalsIgnoreCase("2bhk")){
                  bhk2.setVisibility(View.VISIBLE);
                  commanbhk=bhk2;
                  approx_area="950";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="2bhk";
                  }else{
                      AppConstants.CONFIG=approx_area;
                  }
              }else
              if(conf.equalsIgnoreCase("2.5bhk")){
                  bhk2_5.setVisibility(View.VISIBLE);
                  commanbhk=bhk2_5;
                  approx_area="13000";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="2.5bhk";
                  }else{
                      AppConstants.CONFIG=approx_area;
                  }
              }else
              if(conf.equalsIgnoreCase("3bhk")){
                  bhk3.setVisibility(View.VISIBLE);
                  commanbhk=bhk3;
                  approx_area="1600";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="3bhk";
                  }else{
                      AppConstants.CONFIG=approx_area;
                  }
              }else
              if(conf.equalsIgnoreCase("3.5bhk")){
                  bhk3_5.setVisibility(View.VISIBLE);
                  commanbhk=bhk3_5;
                  approx_area="1800";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="3.5bhk";
                  }else{
                      AppConstants.CONFIG=approx_area;
                  }
              }else
              if(conf.equalsIgnoreCase("4bhk")){
                  bhk4.setVisibility(View.VISIBLE);
                  commanbhk=bhk4;
                  approx_area="2100";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="4bhk";
                  }else{
                      AppConstants.CONFIG=approx_area;
                  }
              }else
              if(conf.equalsIgnoreCase("4.5bhk")){
                  bhk4_5.setVisibility(View.VISIBLE);
                  commanbhk=bhk4_5;
                  approx_area="2300";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="4.5bhk";
                  }else{
                      AppConstants.CONFIG=approx_area;
                  }
              }else
              if(conf.equalsIgnoreCase("5bhk")){
                  bhk5.setVisibility(View.VISIBLE);
                  commanbhk=bhk5;
                  approx_area="2500";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="5bhk";
                  }else{
                      AppConstants.CONFIG=approx_area;
                  }
              }else
              if(conf.equalsIgnoreCase("5.5bhk")){
                  bhk5_5.setVisibility(View.VISIBLE);
                  commanbhk=bhk5_5;
                  approx_area="2700";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="5.5bhk";
                  }else{
                      AppConstants.CONFIG=approx_area;
                  }

              }else
              if(conf.equalsIgnoreCase("6bhk")){
                  bhk6.setVisibility(View.VISIBLE);
                  commanbhk=bhk6;
                  approx_area="2900";
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="6bhk";
                  }else{
                      AppConstants.CONFIG=approx_area;
                  }

              }

              if(fisrtconf==true){
                  Log.i("entered","got it "+conf);
                  commanbhk.setChecked(true);
                  formattedPrice=General.getFormatedPrice(conf,ll_pm);
                  General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,conf);
                  if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                      AppConstants.CONFIG="1.5bhk";
                  }else{
                      AppConstants.CONFIG=approx_area;
                  }
                  Intent intent=new Intent(AppConstants.DISPLAY_CONFIG_BASED_PRICE);
                  intent.putExtra("area1",approx_area);
                  LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                  fisrtconf=false;
              }

          }


    public BigDecimal calcEMI(BigDecimal P, BigDecimal N, BigDecimal ROI) {
        /*if(BigDecimalUtil.anyOneZeroOrNull(P,N,ROI)){
            SCBSUtils.throwSmartCBSException("Invalid data for calculating EMI %s, %s %s", P,N,ROI);
        }*/
        MathContext mc = MathContext.DECIMAL128;
        //Excel equivalent formula == P*R*POWER(1+R,N)/(POWER(1+R,N)-1)
        BigDecimal R = ROI.divide(new BigDecimal(1200),mc);
        BigDecimal nemi1 = P.multiply(R,mc);
        BigDecimal npower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(),mc);
        BigDecimal dpower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(),mc);
        BigDecimal denominator = dpower1.subtract(BigDecimal.ONE);
        BigDecimal numerator = nemi1.multiply(npower1);
        BigDecimal emi = numerator.divide(denominator,0, RoundingMode.UP);
        return emi;
    }



}
