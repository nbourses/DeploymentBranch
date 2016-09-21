package com.nbourses.oyeok.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rohit on 10/02/16.
 */
public class OyeOnPropertyTypeSelectFragment extends Fragment {

    private String selectedPropertyType;
    private View rootView;
    private View rootView1;
    private TextView txtPreviousTextView;
    private  TextView txtsqft;

//    TextView tv_dealinfo;
    private static final String propertyTypeDefaultColor = "#FFFFFF";
    private String bhkNumber = "2";
    private String bhkNumberValue = "BHK";
    private String oyeButtonData;
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
        Bundle bundle = getArguments();
        selectedPropertyType = bundle.getString("propertyType");
        rootView = inflater.inflate(R.layout.fragment_any_click, container, false);
        txtsqft = (TextView) rootView.findViewById(R.id.txtsqft);
        init(inflater, container);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    /**
     * based on property type we will decide which view should be render
     * @param inflater
     * @param container
     */
    private void init(LayoutInflater inflater, ViewGroup container){
        switch (selectedPropertyType) {

            case "Home":
                txtsqft.setBackgroundResource(R.drawable.gradient_greenish_blue);
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);
                rootView = inflater.inflate(R.layout.fragment_home_click, container, false);

                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,bhkNumber+""+bhkNumberValue);
                //by default 2 BHK is selected
                txtPreviousTextView = (TextView) rootView.findViewById(R.id.txt2Bhk);
                txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
                Log.i("retail","===========================hhhhhhhhhhh");
     //         txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);
                AppConstants.letsOye.setPropertySubType(txtPreviousTextView.getText().toString());
                AppConstants.letsOye.setSize(txtPreviousTextView.getText().toString());
               // onFilterValueUpdate(bhkNumber+"<sub><small>"+bhkNumberValue+"</small></sub>",bhkNumber+""+bhkNumberValue);
                onFilterValueUpdate(bhkNumber+""+bhkNumberValue,bhkNumber+""+bhkNumberValue);
                break;
            case "Shop":
                txtsqft.setBackgroundResource(R.drawable.gradient_greenish_blue);
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);
                rootView = inflater.inflate(R.layout.fragment_any_click, container, false);
                Log.i("retail","===========================ssssssssss");
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"<950 sq.ft");
                txtPreviousTextView = (TextView) rootView.findViewById(R.id.txt950h);
                txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
//                txtPreviousTextView.setText("<950 sq.ft");

                onFilterValueUpdate("SHOP","default");
                break;
            case "Industrial":
                txtsqft.setBackgroundResource(R.drawable.gradient_greenish_blue);
                General.saveBoolean(getContext(),"propertySubtypeFlag",true);
                rootView = inflater.inflate(R.layout.fragment_any_click, container, false);
                Log.i("retail","===========================iiiiiiiii");
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"<950 sq.ft");
                txtPreviousTextView = (TextView) rootView.findViewById(R.id.txt950h);
                txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
//                txtPreviousTextView.setText("<950 sq.ft");

                onFilterValueUpdate("IND.","default");
                break;
            case "Office":
                txtsqft.setBackgroundResource(R.drawable.gradient_greenish_blue);
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);
                rootView = inflater.inflate(R.layout.fragment_any_click, container, false);
                Log.i("retail","===========================ooooooooo");
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"<950 sq.ft");
                txtPreviousTextView = (TextView) rootView.findViewById(R.id.txt950h);
                txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
//                txtPreviousTextView.setText("<950 sq.ft");

                onFilterValueUpdate("OFFC","default");
                break;
            /*case "others":
                rootView = inflater.inflate(R.layout.others_layout, container, false);
                break;*/
        }
    }

    @Nullable
    @OnClick({R.id.txt1Bhk, R.id.txt2Bhk, R.id.txt3Bhk, R.id.txt4Bhk, R.id.txtAbove4Bhk})
    public void onBhkClick(View v) {
        clean();
        txtPreviousTextView = (TextView) v;
        txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
            //    txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);
                AppConstants.letsOye.setPropertySubType(txtPreviousTextView.getText().toString());
        AppConstants.letsOye.setSize(txtPreviousTextView.getText().toString());
//        onFilterValueUpdate(txtPreviousTextView.getText().toString());

     /*   if (v.getId() == R.id.txt1Rk) {
            bhkNumber = "1";
            bhkNumberValue = "RK";
        }
        else

        */ if (v.getId() == R.id.txt1Bhk) {
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
    }

    @Nullable
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

/*    @Nullable
    @OnClick({R.id.txt300h, R.id.txt600h, R.id.txt950h,R.id.txt1600h,R.id.txt2100h,R.id.txt3000h})
    public void onIndustryClick(View v) {

        txtsqft.setBackgroundResource(R.drawable.gradient_greenish_blue);
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);

        Log.i("retail","===========================mmmmmmmmm");

        clean();
        txtPreviousTextView = (TextView) v;
        txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
        //txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);
        AppConstants.letsOye.setPropertySubType(txtPreviousTextView.getText().toString());
//        tv_dealinfo.setText(txtPreviousTextView.getText().toString());
        AppConstants.letsOye.setSize(txtPreviousTextView.getText().toString());
        oyeButtonData = txtPreviousTextView.getText().toString();
        setOyeButtonData(oyeButtonData);
        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,oyeButtonData);
        onFilterValueUpdate(oyeButtonData,oyeButtonData);
    }

    @Nullable
    @OnClick({R.id.txt300h, R.id.txt600h, R.id.txt950h,R.id.txt1600h,R.id.txt2100h,R.id.txt3000h})
    public void onOfficeClick(View v) {

        txtsqft.setBackgroundResource(R.drawable.gradient_greenish_blue);
        General.saveBoolean(getContext(), "propertySubtypeFlag", true);
        clean();
        txtPreviousTextView = (TextView) v;
        txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
        //txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);
        AppConstants.letsOye.setPropertySubType(txtPreviousTextView.getText().toString());
        AppConstants.letsOye.setSize(txtPreviousTextView.getText().toString());
//        tv_dealinfo.setText(txtPreviousTextView.getText().toString());
        oyeButtonData = selectedPropertyType +" "+txtPreviousTextView.getText().toString();
        setOyeButtonData(oyeButtonData);
//        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,oyeButtonData);
        onFilterValueUpdate(oyeButtonData,oyeButtonData);
    }*/

    private void clean() {
        if (txtPreviousTextView != null)
         //   txtPreviousTextView.setBackgroundColor(Color.parseColor(propertyTypeDefaultColor));
            txtPreviousTextView.setTextColor(Color.parseColor("black"));
    }

    private void onFilterValueUpdate(String filterValue, String bhk) {
        Intent intent = new Intent(AppConstants.ON_FILTER_VALUE_UPDATE);
        intent.putExtra("filterValue", filterValue);
        intent.putExtra("bhk", bhk);
//        intent.putExtra("tv_dealinfo",oyeButtonData);

        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);


    }

    private void setOyeButtonData(String oyeButtonData) {
        Log.i("oyeButtonData","oyeButtonData "+oyeButtonData+"@"+ SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
        Intent intent = new Intent(AppConstants.ON_FILTER_VALUE_UPDATE);
        intent.putExtra("tv_dealinfo",oyeButtonData);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }


}
