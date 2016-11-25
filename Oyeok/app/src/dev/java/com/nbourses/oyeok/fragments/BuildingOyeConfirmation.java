package com.nbourses.oyeok.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.nbourses.oyeok.helpers.AppConstants;


public class BuildingOyeConfirmation extends Fragment {

    public BuildingOyeConfirmation() {

    }

    View v;
    TextView conf1,conf2,conf3,conf4,conf;
    TextView listingcount,sharing;
    private RadioGroup radioGroup1;
    String  listing,portal,transaction,approx_area,config;
    Bundle  data;
    int approx_area1,price;
    boolean fisrtconf=true;

    private RadioButton rk1,bhk1,bhk1_5,bhk2,bhk2_5,bhk3,bhk3_5,bhk4,bhk4_5,bhk5,bhk5_5,bhk6,commanbhk;

    private BroadcastReceiver RecieveListingData =new BroadcastReceiver() {
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
    };

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
                        break;
                    case R.id.bhk1:
                        approx_area="600";
                        break;
                    case R.id.bhk1_5:
                        approx_area="800";
                        break;
                    case R.id.bhk2:
                        approx_area="950";
                        break;
                    case R.id.bhk2_5:
                        approx_area="1300";
                        break;
                    case R.id.bhk3:
                        approx_area="1600";
                        break;
                    case R.id.bhk3_5:
                        approx_area="1800";
                        break;
                    case R.id.bhk4:
                        approx_area="2100";
                        break;
                    case R.id.bhk4_5:
                        approx_area="2300";
                        break;
                    case R.id.bhk5:
                        approx_area="2500";
                        break;
                    case R.id.bhk5_5:
                        approx_area="2700";
                        break;
                    case R.id.bhk6:
                        approx_area="2900";
                        break;
                    default:
                        break;
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
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(RecieveListingData);
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(Displayconf);

        super.onPause();
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(RecieveListingData, new IntentFilter( AppConstants.SEND_LISTING));
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(Displayconf, new IntentFilter( AppConstants.DISPLAY_BUILDING_CONF));

        super.onResume();

    }

    private void init(){
        data=getArguments();
        listing=data.getString( "listing" );
        transaction=data.getString( "transaction" );
        portal=data.getString( "portal" );
        Log.i("sendDataToOye"," listing  : "+listing+" ");
        String text;
        text="<font color=#000000>In last 6 Months : <b><font color=#2dc4b6><big>"+transaction+" Txns</big></font> | <font color=#ff9f1c><big>"+listing+"</big></font> listing on <font color=#ff9f1c><big>"+portal+" </big></font>portals<b></font>";
        listingcount.setText( Html.fromHtml(text));
        config=data.getString("config");
        SplitString(config);

    }

  /* private BroadcastReceiver Displayconf=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            intent.getExtras().getString("");
        }
    };*/

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
              }else
              if(conf.equalsIgnoreCase("1bhk")){
                  bhk1.setVisibility(View.VISIBLE);
                  commanbhk=bhk1;
              }else
              if(conf.equalsIgnoreCase("1.5bhk")){
                  bhk1_5.setVisibility(View.VISIBLE);
                  commanbhk=bhk1_5;
              }else
              if(conf.equalsIgnoreCase("2bhk")){
                  bhk2.setVisibility(View.VISIBLE);
                  commanbhk=bhk2;
              }else
              if(conf.equalsIgnoreCase("2.5bhk")){
                  bhk2_5.setVisibility(View.VISIBLE);
                  commanbhk=bhk2_5;
              }else
              if(conf.equalsIgnoreCase("3bhk")){
                  bhk3.setVisibility(View.VISIBLE);
                  commanbhk=bhk3;
              }else
              if(conf.equalsIgnoreCase("3.5bhk")){
                  bhk3_5.setVisibility(View.VISIBLE);
                  commanbhk=bhk3_5;
              }else
              if(conf.equalsIgnoreCase("4bhk")){
                  bhk4.setVisibility(View.VISIBLE);
                  commanbhk=bhk4;
              }else
              if(conf.equalsIgnoreCase("4.5bhk")){
                  bhk4_5.setVisibility(View.VISIBLE);
                  commanbhk=bhk4_5;
              }else
              if(conf.equalsIgnoreCase("5bhk")){
                  bhk5.setVisibility(View.VISIBLE);
                  commanbhk=bhk5;
              }else
              if(conf.equalsIgnoreCase("5.5bhk")){
                  bhk5_5.setVisibility(View.VISIBLE);
                  commanbhk=bhk5_5;
              }else
              if(conf.equalsIgnoreCase("6bhk")){
                  bhk6.setVisibility(View.VISIBLE);
                  commanbhk=bhk6;
              }

              if(fisrtconf==true){
                  Log.i("entered","got it "+conf);
                  commanbhk.setChecked(true);
                  fisrtconf=false;
              }

          }


}
