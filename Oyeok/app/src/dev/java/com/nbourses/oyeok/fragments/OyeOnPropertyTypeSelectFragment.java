package com.nbourses.oyeok.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;

import butterknife.ButterKnife;

/**
 * Created by rohit on 10/02/16.
 */
public class OyeOnPropertyTypeSelectFragment extends Fragment {

    private String selectedPropertyType,area;
    private View v;
    private View rootView1;
    private TextView txtPreviousTextView;
    private TextView txtsqft;
    private RadioButton rk1,bhk1,bhk1_5,bhk2,bhk2_5,bhk3,bhk3_5,bhk4,bhk4_5,bhk5,bhk5_5,bhk6,txt950h,commanbhk;
    private RadioButton txt300h,txt600h,txt800h,txt1300h,txt1600h,txt1800h,txt2100h,txt2300h,txt2500h,txt2700h,txt2900;
    private RadioGroup radioGrouphome,radioGroupany;
//    TextView tv_dealinfo;
    private static final String propertyTypeDefaultColor = "#FFFFFF";
    private String bhkNumber = "2";
    private String bhkNumberValue = "BHK";
    private String oyeButtonData;
    String grouptype="home",Subproperty="home",b_conf="2BHK";
private boolean fisrtconf=false;
    HorizontalScrollView horizontalScrollViewAny,horizontalScrollViewHome;
    public OyeOnPropertyTypeSelectFragment() {
        // Required empty public constructor
    }

    public static OyeOnPropertyTypeSelectFragment newInstance(String propertyType) {
        OyeOnPropertyTypeSelectFragment fragment = new OyeOnPropertyTypeSelectFragment();

        Bundle args = new Bundle();
        args.putString("propertyType", propertyType);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View rootView1 = inflater.inflate(R.layout.activity_dashboard, container, false);
//        tv_dealinfo=(TextView)rootView1.findViewById(R.id.tv_dealinfo);

        v = inflater.inflate(R.layout.fragment_any_click, container, false);
       // txtsqft = (TextView) v.findViewById(R.id.txtsqft);

        ButterKnife.bind(this, v);
        Bundle bundle = getArguments();
        selectedPropertyType = bundle.getString("propertyType");
        horizontalScrollViewHome=(HorizontalScrollView)v.findViewById(R.id.horizontalScrollView);
        horizontalScrollViewAny=(HorizontalScrollView)v.findViewById(R.id.horizontalScrollView1);
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
       // bhk2=(RadioButton) v.findViewById( R.id.bhk2 );
        txt950h=(RadioButton) v.findViewById( R.id.txt950h );

        radioGrouphome = (RadioGroup) v.findViewById(R.id.radioGrouphome);
        radioGroupany = (RadioGroup) v.findViewById(R.id.radioGroupany);
if(!General.getSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG).equalsIgnoreCase("")){
    b_conf=General.getSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG);
    General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"");
    plotconf(b_conf);
    if(b_conf==null){
        b_conf="2bhk";
    }
//    AppConstants.letsOye.setPropertySubType("2bhk");
//    AppConstants.letsOye.setSize("2bhk");
    //plotconf(b_conf);
    //commanbhk=AppConstants.CONFIG;
}else{
    AppConstants.letsOye.setPropertySubType("2bhk");
    AppConstants.letsOye.setSize("2bhk");
    b_conf="2bhk";
}
        Log.i("selectedPropertyType","selectedPropertyType================== "+selectedPropertyType+"  AppConstants.CONFIG   : "+AppConstants.CONFIG+" ===== "+General.getSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG)+" ======= "+AppConstants.PROPERTY);

        /*selected_config =(TextView) v.findViewById( R.id.selected_config );*/
        //init(inflater, container);
        //OnConfigselectionAny();
        init();

        switch (selectedPropertyType) {
            case "home":
                horizontalScrollViewHome.setVisibility(View.VISIBLE);
                horizontalScrollViewAny.setVisibility(View.GONE);
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,b_conf);
                AppConstants.letsOye.setPropertySubType(b_conf);
                AppConstants.letsOye.setSize(b_conf);
                onFilterValueUpdate("950","home");

                plotconf(b_conf);
                Subproperty="home";
                break;
            case "Shop":
                horizontalScrollViewHome.setVisibility(View.GONE);
                horizontalScrollViewAny.setVisibility(View.VISIBLE);
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"<950 sq.ft");
                AppConstants.letsOye.setPropertySubType("<950");
                txt950h.setChecked(true);
                onFilterValueUpdate("950","SHOP");
                Subproperty="shop";
                break;
            case "Industrial":
                horizontalScrollViewHome.setVisibility(View.GONE);
                horizontalScrollViewAny.setVisibility(View.VISIBLE);
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"<950 sq.ft");
                AppConstants.letsOye.setPropertySubType("<950");
                txt950h.setChecked(true);
                onFilterValueUpdate("950","IND.");
                Subproperty="industrial";
                break;
            case "Office":
                horizontalScrollViewHome.setVisibility(View.GONE);
                horizontalScrollViewAny.setVisibility(View.VISIBLE);
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"<950 sq.ft");
                AppConstants.letsOye.setPropertySubType("<950");
                txt950h.setChecked(true);
                onFilterValueUpdate("950","OFFC");
                Subproperty="office";
                break;

        }






//
//        if(selectedPropertyType.equalsIgnoreCase("home")){
//
//        }else{
//
//        }
        return v;
    }


   /* private void init(LayoutInflater inflater, ViewGroup container){
        switch (selectedPropertyType) {

            case "Home":

                //txtsqft.setBackgroundResource(R.drawable.gradient_greenish_blue);
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);
                v = inflater.inflate(R.layout.fragment_home_click, container, false);
                //OnConfigselectionHome();
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,bhkNumber+""+bhkNumberValue);
                //by default 2 BHK is selected
               // txtPreviousTextView = (TextView) v.findViewById(R.id.txt2Bhk);
               // txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
                Log.i("retail","===========================hhhhhhhhhhh");
     //         txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);
                AppConstants.letsOye.setPropertySubType(bhkNumber+""+bhkNumberValue);
                AppConstants.letsOye.setSize(bhkNumber+""+bhkNumberValue);
               // onFilterValueUpdate(bhkNumber+"<sub><small>"+bhkNumberValue+"</small></sub>",bhkNumber+""+bhkNumberValue);
                onFilterValueUpdate("950",bhkNumber+""+bhkNumberValue);
                break;
            case "Shop":
                //txtsqft.setBackgroundResource(R.drawable.gradient_greenish_blue);
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);
                v = inflater.inflate(R.layout.fragment_any_click, container, false);
                Log.i("retail","===========================ssssssssss");
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"<950 sq.ft");
                //txtPreviousTextView = (TextView) v.findViewById(R.id.txt950h);
                AppConstants.letsOye.setPropertySubType("<950");
                txt950h.setChecked(true);
               // txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
//                txtPreviousTextView.setText("<950 sq.ft");

                onFilterValueUpdate("950","SHOP");
                break;
            case "Industrial":
                //txtsqft.setBackgroundResource(R.drawable.gradient_greenish_blue);
                General.saveBoolean(getContext(),"propertySubtypeFlag",true);
                v = inflater.inflate(R.layout.fragment_any_click, container, false);
                Log.i("retail","===========================iiiiiiiii");
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"<950 sq.ft");
                //txtPreviousTextView = (TextView) v.findViewById(R.id.txt950h);
               // txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
                AppConstants.letsOye.setPropertySubType("<950");
                txt950h.setChecked(true);
//                txtPreviousTextView.setText("<950 sq.ft");

                onFilterValueUpdate("950","IND.");
                break;
            case "Office":
                //txtsqft.setBackgroundResource(R.drawable.gradient_greenish_blue);
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);
                v = inflater.inflate(R.layout.fragment_any_click, container, false);
                Log.i("retail","===========================ooooooooo");
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"<950 sq.ft");
                //txtPreviousTextView = (TextView) v.findViewById(R.id.txt950h);
               // txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
                AppConstants.letsOye.setPropertySubType("<950");
//                txtPreviousTextView.setText("<950 sq.ft");
                txt950h.setChecked(true);
                onFilterValueUpdate("950","OFFC");
                break;
            *//*case "others":
                v = inflater.inflate(R.layout.others_layout, container, false);
                break;*//*
        }




    }*/


  /*private void  init(){
        if(selectedPropertyType.equalsIgnoreCase("home")){

        }else{

        }
    }*/


   /*@Nullable
    @OnClick({R.id.txt1Bhk, R.id.txt2Bhk, R.id.txt3Bhk, R.id.txt4Bhk, R.id.txtAbove4Bhk})
    public void onBhkClick(View v) {
        clean();
        txtPreviousTextView = (TextView) v;
        txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
       AppConstants.letsOye.setPropertySubType(txtPreviousTextView.getText().toString());
       AppConstants.letsOye.setSize(txtPreviousTextView.getText().toString());
            //    txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);

//        onFilterValueUpdate(txtPreviousTextView.getText().toString());

        if (v.getId() == R.id.txt1Rk) {
            bhkNumber = "1";
            bhkNumberValue = "RK";
        }
        else

         if (v.getId() == R.id.txt1Bhk) {
            bhkNumber = "1";
            bhkNumberValue = "BHK";
//            tv_dealinfo.setText("1BHK");
        }
        else if (v.getId() == R.id.txt2Bhk) {
            bhkNumber = "2";
            bhkNumberValue = "BHK";
//            tv_dealinfo.setText("2BHK");
        }
        else if (v.getId() == R.id.txt3Bhk) {
            bhkNumber = "3";
            bhkNumberValue = "BHK";
           // tv_dealinfo.setText("3BHK");
        }
        else if (v.getId() == R.id.txt4Bhk) {
            bhkNumber = "4";
            bhkNumberValue = "BHK";
           // tv_dealinfo.setText("4BHK");
        }
        else if (v.getId() == R.id.txtAbove4Bhk) {
            bhkNumber = "4+";
            bhkNumberValue = "BHK";
           // tv_dealinfo.setText("4+BHK");
        }
        onFilterValueUpdate(bhkNumber+""+bhkNumberValue,bhkNumber+""+bhkNumberValue);
        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,bhkNumber+""+bhkNumberValue);
        oyeButtonData = selectedPropertyType +" "+txtPreviousTextView.getText().toString();
        setOyeButtonData(oyeButtonData);
    }*/


//@Nullable
//@OnClick({R.id.rk1,R.id.bhk1,R.id.bhk1_5,R.id.bhk2,R.id.bhk2_5,R.id.bhk3,R.id.bhk3_5,R.id.bhk4,R.id.bhk4_5,R.id.bhk5,R.id.bhk5_5,R.id.bhk6})




    /*//RadioGroup group,
   // /@Nullable
    @OnClick({R.id.txt300h,R.id.txt600h,R.id.txt800h,R.id.txt950h,R.id.txt1300h,R.id.txt1500h,R.id.txt1800h,R.id.txt2100h,R.id.txt2300h,R.id.txt2500h,R.id.txt2700h,R.id.txt2900h})
   // @OnCheckedChanged(R.id.radioGrouphome)*/


    private void init(){

        radioGroupany.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // int center = (width - rk1.getWidth())/2;
                switch (checkedId)
                {
                    case R.id.txt300h:
                        bhkNumber = "1";
                        bhkNumberValue = "RK";
                        //Subproperty="nonbhk";
                        area="300";
                        addDataAny();
                        break;
                    case R.id.txt600h:
                        bhkNumber = "1";
                        bhkNumberValue = "BHK";
                        area="600";
                        addDataAny();
                        break;
                    case R.id.txt800h:
                        bhkNumber = "1.5";
                        bhkNumberValue = "BHK";
                        area="800";
                        addDataAny();
                        break;
                    case R.id.txt950h:
                        bhkNumber = "2";
                        bhkNumberValue = "BHK";
                        area="950";
                        addDataAny();
                        break;
                    case R.id.txt1300h:
                        bhkNumber = "2.5";
                        bhkNumberValue = "BHK";
                        area="1300";
                        addDataAny();
                        break;
                    case R.id.txt1500h:
                        bhkNumber = "3";
                        bhkNumberValue = "BHK";
                        area="1600";
                        addDataAny();
                        break;
                    case R.id.txt1800h:
                        bhkNumber = "3.4";
                        bhkNumberValue = "BHK";
                        area="1800";
                        addDataAny();
                        break;
                    case R.id.txt2100h:
                        bhkNumber = "4";
                        bhkNumberValue = "BHK";
                        area="2100";
                        addDataAny();
                        break;
                    case R.id.txt2300h:
                        bhkNumber = "4.5";
                        bhkNumberValue = "BHK";
                        area="2300";
                        addDataAny();
                        break;
                    case R.id.txt2500h:
                        bhkNumber = "5";
                        bhkNumberValue = "BHK";
                        area="2500";
                        addDataAny();
                        break;
                    case R.id.txt2700h:
                        bhkNumber = "5.5";
                        bhkNumberValue = "BHK";
                        //hsl.scrollTo(bhk5_5.getLeft() - center/2, bhk5_5.getTop());
                        area="2700";
                        addDataAny();
                        break;
                    case R.id.txt2900h:
                        bhkNumber = "6";
                        bhkNumberValue = "BHK";
                        area="2900";
                        addDataAny();
                        break;

                }


            }
        });




        radioGrouphome.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.rk1:
                        bhkNumber = "1";
                        bhkNumberValue = "RK";
                        area="300";
                        addData();
                        Log.i("retail","===============rk1============"+"< " +area);
                        break;
                    case R.id.bhk1:
                        bhkNumber = "1";
                        bhkNumberValue = "BHK";
                        area="600";
                        Log.i("retail","===============rk1============"+"< " +area);
                        addData();
                        break;
                    case R.id.bhk1_5:
                        bhkNumber = "1.5";
                        bhkNumberValue = "BHK";
                        area="800";
                        addData();
                        break;
                    case R.id.bhk2:
                        bhkNumber = "2";
                        bhkNumberValue = "BHK";
                        area="950";
                        addData();
                        break;
                    case R.id.bhk2_5:
                        bhkNumber = "2.5";
                        bhkNumberValue = "BHK";
                        area="1300";
                        addData();
                        break;
                    case R.id.bhk3:
                        bhkNumber = "3";
                        bhkNumberValue = "BHK";
                        area="1600";
                        addData();
                        break;
                    case R.id.bhk3_5:
                        bhkNumber = "3.5";
                        bhkNumberValue = "BHK";
                        area="1800";
                        addData();
                        break;
                    case R.id.bhk4:
                        bhkNumber = "4";
                        bhkNumberValue = "BHK";
                        area="2100";
                        addData();
                        break;
                    case R.id.bhk4_5:
                        bhkNumber = "4.5";
                        bhkNumberValue = "BHK";
                        area="2300";
                        addData();
                        break;
                    case R.id.bhk5:
                        bhkNumber = "5";
                        bhkNumberValue = "BHK";
                        area="2500";
                        addData();
                        break;
                    case R.id.bhk5_5:

                        //hsl.scrollTo(bhk5_5.getLeft() - center/2, bhk5_5.getTop());
                        bhkNumber = "5.5";
                        bhkNumberValue = "BHK";
                        area="2700";
                        addData();
                        break;
                    case R.id.bhk6:
                        bhkNumber = "6";
                        bhkNumberValue = "BHK";
                        area="2900";
                        addData();
                        break;
                    /*default:
                        break;*/
                }


                //setOyeButtonData(oyeButtonData);
            }
        });



    }


private void addData(){
    onFilterValueUpdate(area,bhkNumber+""+bhkNumberValue);
    General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,bhkNumber+""+bhkNumberValue);
    oyeButtonData = selectedPropertyType +" "+bhkNumber+""+bhkNumberValue;
    AppConstants.letsOye.setPropertySubType(bhkNumber+""+bhkNumberValue);
    AppConstants.letsOye.setSize(bhkNumber+""+bhkNumberValue);
    General.saveBoolean(getContext(), "propertySubtypeFlag", true);

}



    private void addDataAny(){
        AppConstants.letsOye.setPropertySubType("<" +area);
        Log.i("retail","==========================="+"<" +area);
        //  tv_dealinfo.setText(txtPreviousTextView.getText().toString());
        AppConstants.letsOye.setSize(area);
        // oyeButtonData = area;
        General.saveBoolean(getContext(), "propertySubtypeFlag", true);
        //setOyeButtonData(oyeButtonData);
        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,oyeButtonData+" sq.ft");
        onFilterValueUpdate(area,bhkNumber+""+bhkNumberValue);
    }








   /* @Nullable
    @OnClick({R.id.txt1Bhk, R.id.txt2Bhk, R.id.txt3Bhk, R.id.txt4Bhk, R.id.txtAbove4Bhk})
    public void onBhkClick(View v) {
        clean();
        txtPreviousTextView = (TextView) v;
        txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
            //    txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);
                AppConstants.letsOye.setPropertySubType(txtPreviousTextView.getText().toString());
        AppConstants.letsOye.setSize(txtPreviousTextView.getText().toString());
//        onFilterValueUpdate(txtPreviousTextView.getText().toString());

     *//*   if (v.getId() == R.id.txt1Rk) {
            bhkNumber = "1";
            bhkNumberValue = "RK";
        }
        else

        *//* if (v.getId() == R.id.txt1Bhk) {
            bhkNumber = "1";
            bhkNumberValue = "BHK";
//            tv_dealinfo.setText("1BHK");
        }
        else if (v.getId() == R.id.txt2Bhk) {
            bhkNumber = "2";
            bhkNumberValue = "BHK";
//            tv_dealinfo.setText("2BHK");
        }
        else if (v.getId() == R.id.txt3Bhk) {
            bhkNumber = "3";
            bhkNumberValue = "BHK";
           // tv_dealinfo.setText("3BHK");
        }
        else if (v.getId() == R.id.txt4Bhk) {
            bhkNumber = "4";
            bhkNumberValue = "BHK";
           // tv_dealinfo.setText("4BHK");
        }
        else if (v.getId() == R.id.txtAbove4Bhk) {
            bhkNumber = "4+";
            bhkNumberValue = "BHK";
           // tv_dealinfo.setText("4+BHK");
        }
        onFilterValueUpdate(bhkNumber+""+bhkNumberValue,bhkNumber+""+bhkNumberValue);
        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,bhkNumber+""+bhkNumberValue);
        oyeButtonData = selectedPropertyType +" "+txtPreviousTextView.getText().toString();
        setOyeButtonData(oyeButtonData);
    }*/

  /*  @Nullable
    @OnClick({R.id.txt300h, R.id.txt600h, R.id.txt950h,R.id.txt1600h,R.id.txt2100h,R.id.txt3000h})
    public void onShopClick(View v) {

        txtsqft.setBackgroundResource(R.drawable.gradient_greenish_blue);

        General.saveBoolean(getContext(), "propertySubtypeFlag", true);
        clean();
        Log.i("retail","===========================");
        txtPreviousTextView = (TextView) v;
        txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
        //txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);
        AppConstants.letsOye.setPropertySubType(txtPreviousTextView.getText().toString());
        Log.i("retail","==========================="+txtPreviousTextView.getText().toString());
      //  tv_dealinfo.setText(txtPreviousTextView.getText().toString());
        AppConstants.letsOye.setSize(txtPreviousTextView.getText().toString());
        oyeButtonData = txtPreviousTextView.getText().toString();
        setOyeButtonData(oyeButtonData);
        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,oyeButtonData+" sq.ft");
        onFilterValueUpdate(oyeButtonData,oyeButtonData);
    }
*/

    private void clean() {
        if (txtPreviousTextView != null)
         //   txtPreviousTextView.setBackgroundColor(Color.parseColor(propertyTypeDefaultColor));
            txtPreviousTextView.setTextColor(Color.parseColor("black"));
    }

    private void onFilterValueUpdate(String area, String bhk) {
        Log.i("jadu", "getting and parsing config data 12  filterValue : " + area+" bhk "+bhk);
        Intent intent = new Intent(AppConstants.ON_FILTER_VALUE_UPDATE);
        intent.putExtra("bhkvalue",bhk );//selectedPropertyType
        intent.putExtra("area", area);
        intent.putExtra("subproperty",Subproperty );
//        intent.putExtra("tv_dealinfo",oyeButtonData);

        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);


    }

    /*private void setOyeButtonData(String oyeButtonData) {
        Log.i("oyeButtonData","oyeButtonData "+oyeButtonData+"@"+ SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
        Intent intent = new Intent(AppConstants.ON_FILTER_VALUE_UPDATE);
        intent.putExtra("tv_dealinfo",oyeButtonData);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }*/

    void plotconf(String conf){

        Log.i("configdata", "getting and parsing config data 12 : " + conf+"  "+AppConstants.PROPERTY);
        if(conf.equalsIgnoreCase("1rk")){
            area="300";
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="1rk";
                commanbhk=rk1;
            }else{
                AppConstants.CONFIG="300";
                commanbhk=txt300h;
            }
        }else
        if(conf.equalsIgnoreCase("1bhk")){
            area="600";
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="1bhk";
                commanbhk=bhk1;
            }else{
                AppConstants.CONFIG="600";
                commanbhk=txt600h;
            }
        }else
        if(conf.equalsIgnoreCase("1.5bhk")){
            area="800";
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="bhk1_5";
                commanbhk=bhk1_5;
            }else{
                AppConstants.CONFIG="800";
                commanbhk=txt800h;
            }
        }else
        if(conf.equalsIgnoreCase("2bhk")){
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="2bhk";
                commanbhk=bhk2;

            }else{
                AppConstants.CONFIG=area;
                commanbhk=txt950h;
            }
        }else
        if(conf.equalsIgnoreCase("2.5bhk")){
            area="13000";
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="2.5bhk";
                commanbhk=bhk2_5;

            }else{
                AppConstants.CONFIG=area;
                commanbhk=txt1300h;

            }
        }else
        if(conf.equalsIgnoreCase("3bhk")){

            area="1600";
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="3bhk";
                commanbhk=bhk3;
            }else{
                AppConstants.CONFIG=area;
                commanbhk=txt1600h;
            }
        }else
        if(conf.equalsIgnoreCase("3.5bhk")){
            area="1800";
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="3.5bhk";
                commanbhk=bhk3_5;

            }else{
                AppConstants.CONFIG=area;
                commanbhk=txt1800h;

            }
        }else
        if(conf.equalsIgnoreCase("4bhk")){
            area="2100";
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="4bhk";
                commanbhk=bhk4;

            }else{
                AppConstants.CONFIG=area;
                commanbhk=txt2100h;

            }
        }else
        if(conf.equalsIgnoreCase("4.5bhk")){
            area="2300";
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="4.5bhk";
                commanbhk=bhk4_5;

            }else{
                AppConstants.CONFIG=area;
                commanbhk=txt2300h;

            }
        }else
        if(conf.equalsIgnoreCase("5bhk")){
            area="2500";
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="5bhk";
                commanbhk=bhk5;

            }else{
                AppConstants.CONFIG=area;
                commanbhk=txt2500h;

            }
        }else
        if(conf.equalsIgnoreCase("5.5bhk")){
            area="2700";
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="5.5bhk";
                commanbhk=bhk5_5;

            }else{
                AppConstants.CONFIG=area;
                commanbhk=txt2700h;

            }

        }else
        if(conf.equalsIgnoreCase("6bhk")){
            area="2900";
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="6bhk";
                commanbhk=bhk6;

            }else{
                AppConstants.CONFIG=area;
                commanbhk=txt2900;

            }

        }

        /*if(fisrtconf==true){
            Log.i("entered","got it "+conf);
            commanbhk.setChecked(true);
            //formattedPrice=General.getFormatedPrice(conf,ll_pm);
           // General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,conf);
            if(AppConstants.PROPERTY.equalsIgnoreCase("home")){
                AppConstants.CONFIG="1.5bhk";
            }else{
                AppConstants.CONFIG=area;
            }
            fisrtconf=false;
        }*/
        if(commanbhk==null){
            commanbhk=txt950h;
        }
        commanbhk.setChecked(true);
        onFilterValueUpdate(area,AppConstants.CONFIG);
    }



}
