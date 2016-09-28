package com.nbourses.oyeok.fragments;

/**
 * Created by ritesh on 09/08/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.sdsmdg.tastytoast.TastyToast;


public class DFragment extends Fragment implements CustomPhasedListener {

    private Button button;


    private Button signUp;

    private Button later;

    private ImageButton cardMaps;
    private CustomPhasedSeekBar mPhasedSeekBar;
    private FrameLayout cardFrame;
    private TextView locality;


    private BroadcastReceiver localityBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras().getString("locality") != null){
                String loc = intent.getExtras().getString("locality");
                Log.i("localityBroadcast","localityBroadcast1 ");
                Log.i("localityBroadcast","localityBroadcast "+locality);
                locality.setText(loc);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogfragment, container,
                false);
        button =(Button) rootView.findViewById(R.id.button);
        signUp =(Button) rootView.findViewById(R.id.signUp);
       later =(Button) rootView.findViewById(R.id.later);
        cardMaps = (ImageButton) rootView.findViewById(R.id.cardMaps);
        mPhasedSeekBar = (CustomPhasedSeekBar) rootView.findViewById(R.id.phasedSeekBar1);
        cardFrame = (FrameLayout) rootView.findViewById(R.id.cardMapFrame);
        locality = (TextView) rootView.findViewById(R.id.locality);



        // Do something else
        init();
        return rootView;


    }
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(localityBroadcast, new IntentFilter(AppConstants.LOCALITY_BROADCAST));

    }

    @Override
    public void onPause() {
        super.onPause();
         LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(localityBroadcast);


    }
    private  void init(){
        locality.setText(General.getSharedPreferences(getContext(),AppConstants.LOCALITY));
        DBHelper dbHelper = new DBHelper(getContext());
        if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getContext().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{getContext().getResources().getString(R.string.Rental), getContext().getResources().getString(R.string.Resale)}));
        else
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getContext().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.real_estate_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Audit", "Auction"}));
        mPhasedSeekBar.setListener(this);

        cardMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  BrokerMap brokerMap=new BrokerMap();
                //set arguments
                BrokerMap brokerMap=new BrokerMap();


                //loadFragment(d,null,R.id.container_Signup,"");
                brokerMap.setArguments(null);
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_up);

                fragmentTransaction.addToBackStack("cardMap");
                fragmentTransaction.replace(R.id.y, brokerMap);
                fragmentTransaction.commitAllowingStateLoss();

                //loadFragment(brokerMap,null,R.id.container_Signup,"");


            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.container_Signup)).commit();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.container_Signup)).commit();

                Intent intent = new Intent(AppConstants.DOSIGNUP);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

            }
        });
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.container_Signup)).commit();

                TastyToast.makeText(getContext(), "We have connected you with 3 brokers in your area.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                TastyToast.makeText(getContext(), "Sign up to connect with 7 more brokers waiting for you.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

            }
        });


    }

    @Override
    public void onPositionSelected(int position, int count) {

    }
}