package com.nbourses.oyeok.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nbourses.oyeok.R;


public class BuildingOyeConfirmation extends Fragment {

    public BuildingOyeConfirmation() {

    }

    View view;
    TextView conf1,conf2,conf3,conf4,conf;
    TextView listingcount,sharing;


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




    private void init(){
       String text;
        text="In last 6 Months : <b><font color=#2dc4b6><big>3 Txns</big></font> | <font color=#ff9f1c><big>30</big></font> listing on <big>5 </big>portals<b>";
        listingcount.setText(Html.fromHtml(text));

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
