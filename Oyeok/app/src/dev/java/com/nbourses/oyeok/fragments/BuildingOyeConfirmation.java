package com.nbourses.oyeok.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;


public class BuildingOyeConfirmation extends Fragment {

    public BuildingOyeConfirmation() {

    }

    View view;
    TextView conf1,conf2,conf3,conf4,conf;
    TextView listingcount,sharing;

    String  listing,portal,transaction;
    Bundle  data;

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


        view=inflater.inflate(R.layout.fragment_building_oye_confirmation, container, false);
        conf1=(TextView) view.findViewById(R.id.conf1);
        conf2=(TextView) view.findViewById(R.id.conf2);
        conf3=(TextView) view.findViewById(R.id.conf3);
        listingcount=(TextView ) view.findViewById(R.id.listingcount);
        sharing=(TextView) view.findViewById(R.id.sharing);


        init();
        conf1.setOnClickListener(new View.OnClickListener() {
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
        });

        return view;





    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(RecieveListingData);
        super.onPause();

    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(RecieveListingData, new IntentFilter( AppConstants.SEND_LISTING));
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

    }








/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
