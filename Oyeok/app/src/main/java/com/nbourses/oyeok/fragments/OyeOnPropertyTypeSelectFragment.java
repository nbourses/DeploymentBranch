package com.nbourses.oyeok.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private TextView txtPreviousTextView;

    private static final String propertyTypeDefaultColor = "#FFFFFF";
    private String bhkNumber = "2";
    private String bhkNumberValue = "BHK";

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

        Bundle bundle = getArguments();
        selectedPropertyType = bundle.getString("propertyType");

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
            case "home":
                General.saveBoolean(getContext(), "propertySubtypeFlag", true);
                rootView = inflater.inflate(R.layout.fragment_home_click, container, false);

                //by default 2 BHK is selected
                txtPreviousTextView = (TextView) rootView.findViewById(R.id.txt2Bhk);
                txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));

     //         txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);
                AppConstants.letsOye.setPropertySubType(txtPreviousTextView.getText().toString());
                AppConstants.letsOye.setSize(txtPreviousTextView.getText().toString());
                onFilterValueUpdate(bhkNumber+"<sub><small>"+bhkNumberValue+"</small></sub>");
                break;
            case "shop":
                General.saveBoolean(getContext(), "propertySubtypeFlag", false);
                rootView = inflater.inflate(R.layout.fragment_shop_click, container, false);
                onFilterValueUpdate("SHO");
                break;
            case "industrial":
                General.saveBoolean(getContext(),"propertySubtypeFlag",false);
                rootView = inflater.inflate(R.layout.fragment_industry_click, container, false);
                onFilterValueUpdate("IND");
                break;
            case "office":
                General.saveBoolean(getContext(), "propertySubtypeFlag", false);
                rootView = inflater.inflate(R.layout.fragment_office_click, container, false);
                onFilterValueUpdate("OFF");
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
        }
        else if (v.getId() == R.id.txt2Bhk) {
            bhkNumber = "2";
            bhkNumberValue = "BHK";
        }
        else if (v.getId() == R.id.txt3Bhk) {
            bhkNumber = "3";
            bhkNumberValue = "BHK";
        }
        else if (v.getId() == R.id.txt4Bhk) {
            bhkNumber = "4";
            bhkNumberValue = "BHK";
        }
        else if (v.getId() == R.id.txtAbove4Bhk) {
            bhkNumber = "4+";
            bhkNumberValue = "BHK";
        }
        onFilterValueUpdate(bhkNumber+"<sub><small>"+bhkNumberValue+"</small></sub>");
    }

    @Nullable
    @OnClick({R.id.txtRetailOutlet, R.id.txtFoodOutlet, R.id.txtBank})
    public void onShopClick(View v) {
        General.saveBoolean(getContext(), "propertySubtypeFlag", true);
        clean();
        txtPreviousTextView = (TextView) v;
        txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
        //txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);
        AppConstants.letsOye.setPropertySubType(txtPreviousTextView.getText().toString());
        AppConstants.letsOye.setSize(txtPreviousTextView.getText().toString());
    }

    @Nullable
    @OnClick({R.id.txtColdStorage, R.id.txtKitchen, R.id.txtWarehouse, R.id.txtManufacturing, R.id.txtWorkshop})
    public void onIndustryClick(View v) {
        General.saveBoolean(getContext(), "propertySubtypeFlag", true);



        clean();
        txtPreviousTextView = (TextView) v;
        txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
        //txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);
        AppConstants.letsOye.setPropertySubType(txtPreviousTextView.getText().toString());
        AppConstants.letsOye.setSize(txtPreviousTextView.getText().toString());
    }

    @Nullable
    @OnClick({R.id.txt10, R.id.txt20,
            R.id.txt50, R.id.txt100, R.id.txt200})
    public void onOfficeClick(View v) {
        General.saveBoolean(getContext(), "propertySubtypeFlag", true);
        clean();
        txtPreviousTextView = (TextView) v;
        txtPreviousTextView.setTextColor(Color.parseColor("#2DC4B6"));
        //txtPreviousTextView.setBackgroundResource(R.drawable.buy_option_circle);
        AppConstants.letsOye.setPropertySubType(txtPreviousTextView.getText().toString());
        AppConstants.letsOye.setSize(txtPreviousTextView.getText().toString());
    }

    private void clean() {
        if (txtPreviousTextView != null)
         //   txtPreviousTextView.setBackgroundColor(Color.parseColor(propertyTypeDefaultColor));
            txtPreviousTextView.setTextColor(Color.parseColor("black"));
    }

    private void onFilterValueUpdate(String filterValue) {
        Intent intent = new Intent(AppConstants.ON_FILTER_VALUE_UPDATE);
        intent.putExtra("filterValue", filterValue);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
}
