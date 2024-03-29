package com.nbourses.oyeok.RPOT.OyeOkBroker;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.MyFragment;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.LetsOye;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI.Droom_chats_list;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.RexMarkerPanelScreen;
import com.nbourses.oyeok.SignUp.SignUpFragment;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.DecimalFormat;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import static java.lang.Math.log10;

public class OyeIntentSpecs extends Fragment implements MyFragment.OnFragmentInteractionListener {

    DBHelper dbHelper;
    private Button mOye;
    public static String data="";
    TextView rentOrSale,inputSearch;
    String rentSale,budget="";
    Bundle b;
    RadioGroup seeShowGrp;
    RadioButton seeRadioButton,showRadioButton;
    //MainActivity mActivity = new MainActivity();
    private ImageView homeImageView,shopImageView,officeImageView,industrialImageView,othersImageView;
    RangeBar priceRangeBar;
    String dataFromMyFragment="",seeOrShow="";
    String[] propertySpecification;
    DiscreteSeekBar discreteSeekBar;
    //String off_mode;
    MyFragment myFragment;
    private TextView pricevalue;
    public OyeIntentSpecs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        b=getArguments();
       /* if(b.getStringArray("propertySpecification")!=null)
        {
            propertySpecification=b.getStringArray("propertySpecification");
            Log.i("new","came from signup");
            letsOye();
        }*/
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        rentOrSale= (TextView) rootView.findViewById(R.id.textForRentSale);
        inputSearch= (TextView) rootView.findViewById(R.id.inputSearch);
        inputSearch.setText(b.getString("Address"));

        dbHelper=new DBHelper(getContext());
        rentSale=dbHelper.getValue("brokerType");
        rentOrSale.setText(rentSale);
        mOye = (Button) rootView.findViewById(R.id.bt_oye);
        homeImageView= (ImageView) rootView.findViewById(R.id.icon_home);
        shopImageView= (ImageView) rootView.findViewById(R.id.icon_shop);
        myFragment=new MyFragment();
        //dbHelper.save(DatabaseConstants.offmode, "null");
        //priceRangeBar= (RangeBar) rootView.findViewById(R.id.priceRangeBar);
        myFragment.setmListener(this);
        propertySpecification=new String[10];
        officeImageView= (ImageView) rootView.findViewById(R.id.icon_office);
        industrialImageView= (ImageView) rootView.findViewById(R.id.icon_industrial);
        othersImageView= (ImageView) rootView.findViewById(R.id.icon_others);
        seeShowGrp= (RadioGroup) rootView.findViewById(R.id.RadioBtnGroup);
        seeRadioButton= (RadioButton) rootView.findViewById(R.id.seeRadioButton);
        showRadioButton= (RadioButton) rootView.findViewById(R.id.showRadioButton);
        discreteSeekBar=(DiscreteSeekBar) rootView.findViewById(R.id.discreteSeekBar1);
        pricevalue = (TextView) rootView.findViewById(R.id.textSelected);
        /*final FragmentManager fm = getChildFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        final Fragment fragOne = new MyFragment();*/


        homeImageView.setSelected(true);
        shopImageView.setSelected(false);
        industrialImageView.setSelected(false);
        officeImageView.setSelected(false);
        othersImageView.setSelected(false);
        LinearLayout fragContainer = (LinearLayout) getActivity().findViewById(R.id.linearlayout_container);

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);

       // ll.setId(12345);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MyFragment fragOne = new MyFragment();
        fragOne.setmListener((MyFragment.OnFragmentInteractionListener) OyeIntentSpecs.this);
        //fragOne.setmListener(OyeIntentSpecs.this);


        fm.beginTransaction();
        //Fragment fragOne = new MyFragment();
        Bundle arguments = new Bundle();
        arguments.putString("propertyType", "House");
        fragOne.setArguments(arguments);
        ft.replace(R.id.linearlayout_container, fragOne);
        ft.commit();




        ((MainActivity)getActivity()).changeDrawerToggle(false,"OyeABroker");



        discreteSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
               /* if (fromUser) {
                    settings.setTextZoom(value);
                }*/
               int val= seekBar.getProgress();
                Log.i("Debug",""+value);

                DecimalFormat formatter = new DecimalFormat();
                //String s = numToVal(value);
                //seekBar.setIndicatorFormatt er(s);
                budget = ""+value;
                pricevalue.setText("Rs "+ formatter.format(value));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });


        seeShowGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup seeShowGrp, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)seeShowGrp.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    seeOrShow= (String) checkedRadioButton.getText();
                    Log.i("Checked:" ,""+ checkedRadioButton.getText());
                }
            }
        });

        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                homeImageView.setSelected(true);
                shopImageView.setSelected(false);
                industrialImageView.setSelected(false);
                officeImageView.setSelected(false);
                othersImageView.setSelected(false);
                LinearLayout fragContainer = (LinearLayout) getActivity().findViewById(R.id.linearlayout_container);

                LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);

               // ll.setId(12345);
                 FragmentManager fm = getChildFragmentManager();
                 FragmentTransaction ft = fm.beginTransaction();
                 MyFragment fragOne = new MyFragment();
                fragOne.setmListener((MyFragment.OnFragmentInteractionListener) OyeIntentSpecs.this);
                //fragOne.setmListener(OyeIntentSpecs.this);


                fm.beginTransaction();
                //Fragment fragOne = new MyFragment();
                Bundle arguments = new Bundle();
                arguments.putString("propertyType", "House");
                fragOne.setArguments(arguments);
                ft.replace(R.id.linearlayout_container, fragOne);
                ft.commit();
                //getFragmentManager().beginTransaction().add(ll.getId(), TestFragment.newInstance("I am frag 1"), "someTag1").commit();
                //getFragmentManager().beginTransaction().add(ll.getId(), TestFragment.newInstance("I am frag 2"), "someTag2").commit();

                /*fragContainer.addView(ll);*/

            }
        });
        shopImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                homeImageView.setSelected(false);
                shopImageView.setSelected(true);
                industrialImageView.setSelected(false);
                officeImageView.setSelected(false);
                othersImageView.setSelected(false);
                LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);

                //ll.setId(12345);

                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MyFragment fragOne = new MyFragment();
                fragOne.setmListener(OyeIntentSpecs.this);
                fm.beginTransaction();
                //Fragment fragOne = new MyFragment();
                Bundle arguments = new Bundle();
                arguments.putString("propertyType", "Shop");

                fragOne.setArguments(arguments);
                ft.replace(R.id.linearlayout_container, fragOne);
                ft.commit();
            }
        });

        /*priceRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                Log.i("Mehul2", "rangeBar is" + rangeBar + "\n" + "LeftpinIndex is" + leftPinIndex + "\n" + "LeftpinValue is" + leftPinValue
                        + "\n" + "RightpinIndex is" + rightPinIndex + "\n" + "RightpinValue is" + rightPinValue);

                int number = rangeBar.getTickCount();

                Log.d("Nikhil", numToVal(number));*//*
                Log.d("Nikhil",numToVal(11293435));
                Log.d("Nikhil",numToVal(20000));
                Log.d("Nikhil",numToVal(120000));
                Log.d("Nikhil",numToVal(6700340));*//*
            }
        });*/


        officeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                homeImageView.setSelected(false);
                shopImageView.setSelected(false);
                industrialImageView.setSelected(false);
                officeImageView.setSelected(true);
                othersImageView.setSelected(false);
                LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);

                //ll.setId(12345);
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MyFragment fragOne = new MyFragment();
                fragOne.setmListener(OyeIntentSpecs.this);

                fm.beginTransaction();
                //Fragment fragOne = new MyFragment();
                Bundle arguments = new Bundle();
                arguments.putString("propertyType", "Office");
                fragOne.setArguments(arguments);
                ft.replace(R.id.linearlayout_container, fragOne);
                ft.commit();
            }
        });

        industrialImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                homeImageView.setSelected(false);
                shopImageView.setSelected(false);
                industrialImageView.setSelected(true);
                officeImageView.setSelected(false);
                othersImageView.setSelected(false);
                LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);

               // ll.setId(12345);
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MyFragment fragOne = new MyFragment();
                fragOne.setmListener((MyFragment.OnFragmentInteractionListener) OyeIntentSpecs.this);

                fm.beginTransaction();
                //Fragment fragOne = new MyFragment();
                Bundle arguments = new Bundle();
                arguments.putString("propertyType", "Industrial");
                fragOne.setArguments(arguments);
                ft.replace(R.id.linearlayout_container, fragOne);
                ft.commit();
            }
        });

        othersImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                homeImageView.setSelected(false);
                shopImageView.setSelected(false);
                industrialImageView.setSelected(false);
                officeImageView.setSelected(false);
                othersImageView.setSelected(true);
                LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);

                

                //ll.setId(12345);

                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MyFragment fragOne = new MyFragment();
                fragOne.setmListener((MyFragment.OnFragmentInteractionListener) OyeIntentSpecs.this);
                fm.beginTransaction();

                Bundle arguments = new Bundle();
                arguments.putString("propertyType", "Others");
                fragOne.setArguments(arguments);
                ft.replace(R.id.linearlayout_container, fragOne);
                ft.commit();
            }
        });
        mOye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] temp=dataFromMyFragment.split(" ");
                if(temp.length<=1)
                {
                    //Toast.makeText(getActivity(),"Please enter all the fields",Toast.LENGTH_LONG).show();
                    ((MainActivity)getActivity()).showToastMessage("Please enter all the fields");
                }
                else
                letsOye();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void letsOye(){

        splitDataFromMyFragment();
        if(dbHelper.getValue(DatabaseConstants.userId).equals("null"))
        {
            Log.i("new","going to signup");
            Fragment fragment = null;
            Bundle bundle=new Bundle();
            bundle.putStringArray("propertySpecification",propertySpecification);
            bundle.putString("lastFragment","OyeIntentSpecs");
            Log.i("Bundle_oye", propertySpecification[0]);
            dbHelper.save(DatabaseConstants.userRole,"Client");
            fragment = new SignUpFragment();
            fragment.setArguments(bundle);
            String title= "Sign Up";
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

        }
        else {


            Oyeok oyeOk = new Oyeok();
            Log.i("tt="+propertySpecification[2]," size="+propertySpecification[1]+" price="+propertySpecification[4]+" req_avl="+propertySpecification[3]);
            oyeOk.setTt(propertySpecification[2]);
            oyeOk.setSize(propertySpecification[1]);
            oyeOk.setPrice(propertySpecification[4]);
            oyeOk.setReqAvl(propertySpecification[3]);
            oyeOk.setUserId(dbHelper.getValue(DatabaseConstants.userId));
            oyeOk.setLong(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
            oyeOk.setLat(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
            oyeOk.setUserRole("client");
            oyeOk.setPropertyType(propertySpecification[0]);
            oyeOk.setPropertySubtype(propertySpecification[1]);
            oyeOk.setPushToken(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
            oyeOk.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
            oyeOk.setPlatform("android");
            Log.i("UserId", "saved in DB");


        /*oyeOk.setSpecCode("LL-3BHK-9Cr");
        oyeOk.setReqAvl("req");
        oyeOk.setUserId("egtgxhr02ai31a2uzu82ps2bysljv43n");
        oyeOk.setUserRole("client");
        oyeOk.setLong("19");
        oyeOk.setLat("17");
        oyeOk.setRegion("powai");
        oyeOk.setPincode("400058");*/
            String off_mode = "NO";
            String API = DatabaseConstants.serverUrl;
            RestAdapter restAdapter1 = new RestAdapter.Builder().setEndpoint(API).build();
            restAdapter1.setLogLevel(RestAdapter.LogLevel.FULL);
            OyeokApiService oyeok = restAdapter1.create(OyeokApiService.class);
            Log.i("Check",isNetworkAvailable()+"  "+dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null") );
            if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null") && isNetworkAvailable()) {
                try {
                    oyeok.letsOye(oyeOk, new Callback<LetsOye>() {
                        @Override
                        public void success(LetsOye letsOye, retrofit.client.Response response) {
                            //if(!off_mode.equals("yes")) {
                            //String s = post(nameValuePairs);
                            String s = letsOye.getResponseData();
                            if (!s.equals("")) {
                                try {
                                    if (s.equalsIgnoreCase("Your Oye is published")) {
                                /*FirebaseClass.setOyebookRecord(UserCredentials.getString(EnterConfigActivity.this, PreferenceKeys.MY_SHORTMOBILE_KEY), reNt, show, lng.toString(), lat.toString(), user_id, bhkval + "BHK", msg4, UserCredentials.getString(EnterConfigActivity.this, PreferenceKeys.CURRENT_LOC_KEY));
                                Intent NextActivity = new Intent(context, MainActivity.class);
                                startActivity(NextActivity);
                                UserCredentials.saveString(context, PreferenceKeys.SUCCESSFUL_HAIL, "true");*/
                                        //Toast.makeText(getContext(), "Oye published.Sit back and relax while we find a broker for you", Toast.LENGTH_LONG).show();
                                        //mActivity.showToastMessage("Oye published.Sit back and relax while we find a broker for you");
                                        ((MainActivity)getActivity()).showToastMessage("Oye published.Sit back and relax while we find a broker for you");
                                        //finish();
                                        Bundle b = new Bundle();
                                        b.putString("lastFragment","oyeIntentSpecs");
                                        Fragment f=new Droom_chats_list();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        f.setArguments(b);
                                        fragmentTransaction.replace(R.id.container_body, f);
                                        fragmentTransaction.commit();

                                        Log.i("Change Fragment", f.toString());
                                    } else if (s.equalsIgnoreCase("User already has an active oye. Pls end first")) {
                                /*Intent NextActivity = new Intent(context, MainActivity.class);
                                startActivity(NextActivity);*/
                                        //Toast.makeText(getContext(), "You already have an active oye. Pls end it first", Toast.LENGTH_LONG).show();
                                        //mActivity.showToastMessage("You already have an active oye. Pls end it first");
                                        ((MainActivity)getActivity()).showToastMessage("You already have an active oye. Pls end it first");
                                        /// /finish();
                                    } else

                                    {
                                /*Intent NextActivity = new Intent(context, MainActivity.class);
                                startActivity(NextActivity);*/
                                        //Toast.makeText(getContext(), "There is some error.", Toast.LENGTH_LONG).show();
                                        //mActivity.showToastMessage("There is some error.");
                                        ((MainActivity)getActivity()).showToastMessage("There is some error");
                                        //finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            }
                            //((MainActivity) getActivity()).changeFragment(new RexMarkerPanelScreen(), null,"");
                /*}else
                {
                    *//*Intent NextActivity = new Intent(context, MainActivity.class);
                    startActivity(NextActivity);*//*
                    Toast.makeText(getContext(), "In offline mode.Done", Toast.LENGTH_LONG).show();
                    //finish();
                }*/
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getContext(), "lets oye call failed in enter config", Toast.LENGTH_LONG).show();
                            //mActivity.showToastMessage("lets oye call failed in enter config");
                            ((MainActivity)getActivity()).showToastMessage("lets oye call failed in enter config");
                            // FirebaseClass.setOyebookRecord(UserCredentials.getString(EnterConfigActivity.this,PreferenceKeys.MY_SHORTMOBILE_KEY),reNt,show,lng,lat,user_id,bhkval+"BHK",msg4,UserCredentials.getString(EnterConfigActivity.this,PreferenceKeys.CURRENT_LOC_KEY));
                            //Intent NextActivity = new Intent(context, MainActivity.class);
                            //startActivity(NextActivity);finish();
                            Log.i("TAG", "lets oye call failed in enter config");
                            Log.i("TAG", "inside error" + error.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Log.i("Exception", "caught in lets oye");
                }
            }else{
                //Toast.makeText(getContext(), "You are are using offline mode or you are not connected to internet", Toast.LENGTH_LONG).show();
                ((MainActivity) getActivity()).showToastMessage("You are are using offline mode or you are not connected to internet");
                ((MainActivity) getActivity()).changeFragment(new RexMarkerPanelScreen(), null,"");
            }

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void splitDataFromMyFragment() {
        String[] temp=dataFromMyFragment.split(" ");
        propertySpecification[0]=temp[0];
        switch(temp[0]){
            case "House":
                propertySpecification[1]=temp[1];
                break;
            case "Shop":
                propertySpecification[1]=temp[1];
                break;
            case "Industrial":
                propertySpecification[1]=temp[1];
                break;
            case "Office":
                propertySpecification[1]=temp[1]+" Seater";
                break;
            case "Others":
                propertySpecification[1]=dataFromMyFragment;
                break;
        }
        propertySpecification[2]=dbHelper.getValue(DatabaseConstants.brokerType);

        if(seeRadioButton.isChecked())
            propertySpecification[3]="req";
        else
            propertySpecification[3]="avl";

        propertySpecification[4]=budget;
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

                v = val+"";
                str = v+"CR";
                no = no%10000000;

                twoWord++;

            case 5:

                val = no/100000;

                v = val+"";
                no = no%100000;
                if (val != 0){
                    str = str+v+"L ";
                    twoWord++;
                }
                if (twoWord == 2){
                    break;}

            case 3:
                val = no/1000;
                v = val+"";
                if (val != 0) {
                    str = str+v+"K";
                }
                break;
            default :
                // print("noToWord Default")
                break;
        }
        return str;
    }
    @Override
    public void onFragmentInteraction(String data) {
        dataFromMyFragment=data;
        //Log.i("Gfrag",""+data);
    }


}
