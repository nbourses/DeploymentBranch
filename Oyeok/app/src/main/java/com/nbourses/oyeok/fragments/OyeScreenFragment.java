package com.nbourses.oyeok.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OyeScreenFragment extends Fragment {

    @Bind(R.id.txtHome)
    ImageView txtHome;

    @Bind(R.id.txtShop)
    ImageView txtShop;

    @Bind(R.id.txtIndustrial)
    ImageView txtIndustrial;

    @Bind(R.id.txtOffice)
    ImageView txtOffice;

    /*@Bind(R.id.txtOthers)
    TextView txtOthers;*/

    @Bind(R.id.txtOptionSee)
    TextView txtOptionSee;

    @Bind(R.id.txtOptionShow)
    TextView txtOptionShow;

    @Bind(R.id.loadContainer)
    LinearLayout loadContainer;

    @Bind(R.id.budgetSeekBar)
    DiscreteSeekBar budgetSeekBar;


    @Bind(R.id.txtSelected)
    TextView txtSelected;

    @Bind(R.id.requestType)
    TextView requestType;

    private ImageView txtPreviouslySelectedPropertyType;
    private static final String propertyTypeDefaultColor = "#FFFFFF";
    private TextView txtPreviouslySelectedOption;
    private Bundle bundle;

    public OyeScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_oye_screen, container, false);
        ButterKnife.bind(this, rootView);

        bundle = getArguments();

        init();

        return rootView;
    }

    private void init() {
        //by default Home option is selected
        txtPreviouslySelectedPropertyType = txtHome;
        txtHome.setBackgroundResource(R.drawable.buy_option_circle);
        AppConstants.letsOye.setPropertyType("home");
        loadHomeOptionView("home");

        //by default buy option is selected
        txtPreviouslySelectedOption = txtOptionSee;
        txtOptionSee.setBackgroundResource(R.color.greenish_blue);
        AppConstants.letsOye.setReqAvl("req");

        //set seek bar min and max value
        setMinMaxValueForDiscreteSeekBar();

        //budget bar listener
        budgetSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

                DecimalFormat formatter = new DecimalFormat();
                Log.i("val", String.valueOf(+ value));
                System.out.println(value);

              //  if(value>15000)
               // {
               value=value/1000;
                value=value*1000;
               // }
                txtSelected.setText(formatter.format(value));

                AppConstants.letsOye.setPrice("" + value);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });

        //set user role, lat and long
        AppConstants.letsOye.setUserRole("client");
        AppConstants.letsOye.setLat(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
        AppConstants.letsOye.setLon(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
    }

    private void loadHomeOptionView(String propertyType) {
        OyeOnPropertyTypeSelectFragment oyeOnPropertyTypeSelectFragment = OyeOnPropertyTypeSelectFragment.newInstance(propertyType);
        loadFragment(oyeOnPropertyTypeSelectFragment, loadContainer.getId());
    }

    private void loadFragment(Fragment fragment, int containerId)
    {
        //load fragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Nullable
    @OnClick({R.id.txtOptionSee, R.id.txtOptionShow})
    public void onBuyOptionClick(View v) {
        if (txtPreviouslySelectedOption != null)
            txtPreviouslySelectedOption.setBackgroundResource(R.color.colorPrimaryDark);

        txtPreviouslySelectedOption = (TextView) v;

        if (txtOptionSee.getId() == v.getId()) {
            txtOptionSee.setBackgroundResource(R.color.greenish_blue);
            AppConstants.letsOye.setReqAvl("req");
        }

        if (txtOptionShow.getId() == v.getId()) {
            txtOptionShow.setBackgroundResource(R.color.greenish_blue);
            AppConstants.letsOye.setReqAvl("avl");
        }
    }

    @Nullable
    @OnClick({R.id.txtHome, R.id.txtShop, R.id.txtIndustrial, R.id.txtOffice})
    public void onPropertyTypeClick(View v) {

        if(txtPreviouslySelectedPropertyType != null)
            txtPreviouslySelectedPropertyType.setBackgroundColor(Color.parseColor(propertyTypeDefaultColor));

        txtPreviouslySelectedPropertyType = (ImageView) v;

        if (txtHome.getId() == v.getId()) {
            txtHome.setBackgroundResource(R.drawable.buy_option_circle);
            AppConstants.letsOye.setPropertyType("home");
            loadHomeOptionView("home");
        }
        else if(txtShop.getId() == v.getId()) {
            txtShop.setBackgroundResource(R.drawable.buy_option_circle);
            AppConstants.letsOye.setPropertyType("shop");
            loadHomeOptionView("shop");
        }
        else if(txtIndustrial.getId() == v.getId()) {
            txtIndustrial.setBackgroundResource(R.drawable.buy_option_circle);
            AppConstants.letsOye.setPropertyType("industrial");
            loadHomeOptionView("industrial");
        }
        else if(txtOffice.getId() == v.getId()) {
            txtOffice.setBackgroundResource(R.drawable.buy_option_circle);
            AppConstants.letsOye.setPropertyType("office");
            loadHomeOptionView("office");
        }
    }

    /**
     * set min and max value for seek bar
     */
    private void setMinMaxValueForDiscreteSeekBar() {

        if (bundle != null) {

            requestType.setText(bundle.getString("brokerType").toUpperCase());
            DecimalFormat formatter = new DecimalFormat();

            if (bundle.getString("brokerType").equalsIgnoreCase("rent")) {
                budgetSeekBar.setMin(AppConstants.minRent);
                budgetSeekBar.setMax(AppConstants.maxRent);
               // budgetSeekBar.setProgress(500);
                txtSelected.setText(formatter.format(AppConstants.minRent));

                txtOptionSee.setText(getString(R.string.oye_rental_req));
                txtOptionShow.setText(getString(R.string.oye_rental_avail));

                AppConstants.letsOye.setPrice("" + AppConstants.minRent);
                AppConstants.letsOye.setTt("LL");
            }
            else {
                budgetSeekBar.setMin(AppConstants.minSale);
                budgetSeekBar.setMax(AppConstants.maxSale);
               // budgetSeekBar.setProgress(10);
               // budgetSeekBar.incrementProgressBy(10);
                txtSelected.setText(formatter.format(AppConstants.minSale));

                txtOptionSee.setText(getString(R.string.oye_sale_req));
                txtOptionShow.setText(getString(R.string.oye_sale_avail));

                AppConstants.letsOye.setPrice("" + AppConstants.minSale);
                AppConstants.letsOye.setTt("OR");
            }
        }
    }

    @OnClick(R.id.btnCloseOyeScreenSlide)
    public void onBtnCloseOyeScreenSlideClick(View v) {
        Intent intent = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
}