package com.nbourses.oyeok.fragments;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    @Bind(R.id.btnOnOyeClick)
    GridLayout btnOnOyeClick;

    @Bind(R.id.txtOptionShow)
    TextView txtOptionShow;
    @Bind(R.id.tv_fd_bank)
    TextView tv_fd_bank;

     @Bind(R.id.loadContainer)
    LinearLayout loadContainer;

    @Bind(R.id.budgetSeekBar)
    DiscreteSeekBar budgetSeekBar;


    @Bind(R.id.txtSelected)
    TextView txtSelected;

    @Bind(R.id.checkBox)
    CheckBox satView;

    @Bind(R.id.requestType)
    TextView requestType;

    @Bind(R.id.budgetText)
    TextView budgetText;
    String oyedata="" ;
    TextView txtcalendar;
    String property;

//    DiscreteSeekBar discreteSeekBar;
//@Bind(R.id.tv_dealinfo)
//    TextView tv_dealinfo;
//    @Bind(R.id.txtfixedString)
//    TextView txtfixedString;
   String isclicked;


//    private void setMinMaxValueForDiscreteSeekBar() {
//        if (bundle != null) {
//            if (bundle.getString("brokerType").equalsIgnoreCase("rent")) {
//                discreteSeekBar.setMin(AppConstants.minRent);
//                discreteSeekBar.setMax(AppConstants.maxRent);
//                txtSelected.setText("" + AppConstants.minRent);
//
////                txtMin.setText("15K");
////                txtMax.setText("12L");
//            } else {
//                discreteSeekBar.setMin(AppConstants.minSale);
//                discreteSeekBar.setMax(AppConstants.maxSale);
//                txtSelected.setText("" + AppConstants.minSale);
//
////                txtMin.setText("70L");
////                txtMax.setText("10CR");
//            }
//        }
//    }

    private ImageView txtPreviouslySelectedPropertyType;
    private static final String propertyTypeDefaultColor = "#FFFFFF";
    private TextView txtPreviouslySelectedOption;
    private Bundle bundle;

    public OyeScreenFragment() {
        // Required empty public constructor
    }


    Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_oye_screen, container, false);
        ButterKnife.bind(this, rootView);

//        View rootView1 = inflater.inflate(R.layout.activity_dashboard, container, false);
      satView = (CheckBox) rootView.findViewById(R.id.checkBox);
        satView.setVisibility(View.VISIBLE);
        bundle = getArguments();
      tv_fd_bank=(TextView)rootView.findViewById(R.id.tv_fd_bank);
        txtcalendar=(TextView)rootView.findViewById(R.id.txtcalendar);
//        tv_dealinfo=(TextView)rootView.findViewById(R.id.tv_dealinfo);


        txtcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker();
            }
        });
            init();

        satView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    tv_fd_bank.setTextColor(getResources().getColor(R.color.greenish_blue));
                    satView.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_light_focused));
                    //checked
                }
                else
                {
                    satView.setTextColor(getResources().getColor(R.color.greenish_blue));
                    tv_fd_bank.setTextColor(getResources().getColor(R.color.common_plus_signin_btn_text_light_focused));
                    //not checked
                }

            }
        });



            return rootView;




    }



    private BroadcastReceiver oyebuttondata = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           // if(intent.getExtras().getString("tv_dealinfo") != null) {
                // intent.getExtras().getString("tv_dealinfo")+
//            if(txtPreviouslySelectedOption.getText().toString().equalsIgnoreCase(txtOptionSee.getText().toString())){
                 oyedata = SharedPrefs.getString(context,SharedPrefs.MY_LOCALITY);
//                tv_dealinfo.setText(oyedata);
                Log.i("oyedata","oyedata==============="+oyedata);





               // setdealtext(oyedata);
               // tv_dealinfo.setText("sushil");


            //}

        }
    };







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

        setInitialValuesLoans();

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
                String val=String.valueOf(value);
                txtSelected.setText(General.currencyFormat(val));

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
            if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("RENTAL")){
            tv_fd_bank.setText("I like to pay my \n SECURITY DEPOSIT");
            satView.setText("Apply for finance \n SECURITY DEPOSIT");
                budgetText.setText("My Rent Budget");
//                txtfixedString.setText("CONNECT NOW | FIND @ ");
            }
            else{
                tv_fd_bank.setText("I don't \n want loan");
                satView.setText("I want LOAN \n to buy");
                budgetText.setText("My Budget");
//                txtfixedString.setText("CONNECT NOW | FIND @ ");

            }
            AppConstants.letsOye.setReqAvl("req");
        }

        if (txtOptionShow.getId() == v.getId()) {
            txtOptionShow.setBackgroundResource(R.color.greenish_blue);
            if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("RENTAL")){
            tv_fd_bank.setText("I like Monthly RENT CHEQUE(s)");
            satView.setText("Apply for finance FULL ADVANCE RENT");
                budgetText.setText("My Asking Rent");
//                txtfixedString.setText("CONNECT NOW | LIST @ ");
            }
            else{
                tv_fd_bank.setText("No Loan On \n Property");
                satView.setText("I have Loan On \n Propery");
                budgetText.setText("Sale Price");
//                txtfixedString.setText("CONNECT NOW | LIST @ ");

            }
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
            property="home";
            //tv_dealinfo.setText(tv_dealinfo.getText()+" "+"home");

        }
        else if(txtShop.getId() == v.getId()) {
            txtShop.setBackgroundResource(R.drawable.buy_option_circle);
            AppConstants.letsOye.setPropertyType("shop");
            loadHomeOptionView("shop");
            property="shop";
           // tv_dealinfo.setText(tv_dealinfo.getText()+" "+"shop");
        }
        else if(txtIndustrial.getId() == v.getId()) {
            txtIndustrial.setBackgroundResource(R.drawable.buy_option_circle);
            AppConstants.letsOye.setPropertyType("industrial");
            loadHomeOptionView("industrial");
            property="industrial";
           // tv_dealinfo.setText(tv_dealinfo.getText()+" "+"industrial");
        }
        else if(txtOffice.getId() == v.getId()) {
            txtOffice.setBackgroundResource(R.drawable.buy_option_circle);
            AppConstants.letsOye.setPropertyType("office");
            loadHomeOptionView("office");
            property="office";
            //tv_dealinfo.setText(tv_dealinfo.getText()+" "+"office");
        }
    }

    /**
     * set min and max value for seek bar
     */

    private BroadcastReceiver BroadCastMinMaxValue = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i("llmin111111", "llmin ++++++++++++++++++++++++++" + intent.getExtras().getInt("llmin"));
            if (intent.getExtras().getInt("llmin") != 0)
            {
                AppConstants.minRent = intent.getExtras().getInt("llmin");
            Log.i("llmin111111", " min rent" + AppConstants.minRent);
//                AppConstants.minRent = AppConstants.minRent / 1000;
//                AppConstants.minRent = AppConstants.minRent * 1000;
            AppConstants.minRent = AppConstants.minRent / 2;

            AppConstants.maxRent = intent.getExtras().getInt("llmax");
//                AppConstants.maxRent = AppConstants.maxRent + AppConstants.maxRent / 1000;
//                AppConstants.maxRent = AppConstants.maxRent + AppConstants.maxRent  * 1000;
            AppConstants.maxRent = AppConstants.maxRent + AppConstants.maxRent / 2;

            Log.i("llmin111111", "max rent" + AppConstants.maxRent);
            AppConstants.minSale = intent.getExtras().getInt("ormin");
            Log.i("llmin111111", "min Sale" + AppConstants.minSale);
//                AppConstants.minSale = AppConstants.minSale / 1000;
//                AppConstants.minSale = AppConstants.minSale * 1000;
            AppConstants.minSale = AppConstants.minSale / 2;

            AppConstants.maxSale = intent.getExtras().getInt("ormax");
            Log.i("llmin111111", "max Sale" + AppConstants.maxSale);
//                AppConstants.maxSale = (AppConstants.maxSale + (AppConstants.maxSale / 1000));
//                AppConstants.maxSale = (AppConstants.maxSale + (AppConstants.maxSale * 1000));
            AppConstants.maxSale = (AppConstants.maxSale + (AppConstants.maxSale / 2));

        }
            setMinMaxValueForDiscreteSeekBar();


        }
    };





    private void setMinMaxValueForDiscreteSeekBar() {

        if (bundle != null) {

         //   requestType.setText(bundle.getString("brokerType").toUpperCase());
//            Log.i("value"," data"+bundle.getString("brokerType").toUpperCase());
            DecimalFormat formatter = new DecimalFormat();


            if (bundle.getString("brokerType").equalsIgnoreCase("rent")) {
                requestType.setText("Rental");
                budgetSeekBar.setMin(AppConstants.minRent);
                budgetSeekBar.setMax(AppConstants.maxRent);
                budgetSeekBar.setProgress(AppConstants.minRent);
               // budgetSeekBar.setProgress(500);
                txtSelected.setText(General.currencyFormat(String.valueOf(AppConstants.minRent)));

                txtOptionSee.setText(getString(R.string.oye_rental_req));
                txtOptionShow.setText(getString(R.string.oye_rental_avail));

                AppConstants.letsOye.setPrice("" + AppConstants.minRent);
                AppConstants.letsOye.setTt("LL");
            }
            else {
                requestType.setText("Buy/Sell");
                budgetSeekBar.setMin(AppConstants.minSale);
                budgetSeekBar.setMax(AppConstants.maxSale);
                budgetSeekBar.setProgress(AppConstants.minRent);
               // budgetSeekBar.setProgress(10);
               // budgetSeekBar.incrementProgressBy(10);
                txtSelected.setText(General.currencyFormat(String.valueOf(AppConstants.minSale)));

                txtOptionSee.setText(getString(R.string.oye_sale_req));
                txtOptionShow.setText(getString(R.string.oye_sale_avail));

                AppConstants.letsOye.setPrice("" + AppConstants.minSale);
                AppConstants.letsOye.setTt("OR");
            }
        }
    }

    private void setInitialValuesLoans(){

        if(General.getSharedPreferences(getContext(),AppConstants.TT).equalsIgnoreCase("RENTAL")){
            tv_fd_bank.setText("I like to pay my \n SECURITY DEPOSIT");
            satView.setText("Apply for finance \n SECURITY DEPOSIT");
            budgetText.setText("My Rent Budget");


        }
        else{
            tv_fd_bank.setText("I don't \n want loan");
            satView.setText("I want LOAN \n to buy");
            budgetText.setText("My Budget");


        }
//        txtfixedString.setText("CONNECT NOW | FIND @ ");

    }





    @Override
    public void onResume() {

        super.onResume();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(BroadCastMinMaxValue, new IntentFilter(AppConstants.BROADCAST_MIN_MAX_VAL));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(oyebuttondata, new IntentFilter(AppConstants.ON_FILTER_VALUE_UPDATE));

    }

    @Override
    public void onPause() {
        super.onPause();
       LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(BroadCastMinMaxValue);

    }






    @OnClick(R.id.btnCloseOyeScreenSlide)
    public void onBtnCloseOyeScreenSlideClick(View v) {




        Intent intent = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

//        DashboardClientFragment d=new DashboardClientFragment();
//        d.UpdateRatePanel();
    }


   // satView = (CheckBox) findViewById(R.id.checkBox);

//    satView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            if (buttonView.isChecked()) {
//                //checked
//            }
//            else
//            {
//                //not checked
//            }
//
//        });



    @OnClick(R.id.btnOnOyeClick)
    public void submitOyeOk(View v) {

        if(General.isNetworkAvailable(getContext())) {
            Log.i("TAG", "property subtype selected" + General.retriveBoolean(getContext(), "propertySubtypeFlag"));
            if (General.retriveBoolean(getContext(), "propertySubtypeFlag")) {
                isclicked = "true";
                Intent intent = new Intent(AppConstants.ON_FILTER_VALUE_UPDATE);
                intent.putExtra("isclicked", isclicked);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                Log.i("isclicked", "isclicked===============================");
            } else {
                SnackbarManager.show(
                        Snackbar.with(getContext())
                                .position(Snackbar.SnackbarPosition.TOP)
                                .text("Please select property subtype.")
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
            }
        }else{
            General.internetConnectivityMsg(getContext());
        }
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

    private void updateLabel(){
        String myFormat = "dd/MMM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtcalendar.setText(sdf.format(myCalendar.getTime()));
    }

    private void displayDatePicker(){

        DatePickerDialog dpd = new DatePickerDialog(getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        String myFormat = "dd/MM/yy"; //In which you need put here
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
    }





}