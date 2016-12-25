package com.nbourses.oyeok.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.activities.Game;


public class gameDiscountCard extends Fragment {


    public gameDiscountCard() {
        // Required empty public constructor
    }

TextView gm_heading,gm_discount,download_discount,total_discount;
    LinearLayout share,resume_game;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v=inflater.inflate(R.layout.fragment_game_discount_card, container, false);
        gm_heading=(TextView)v.findViewById(R.id.gm_heading);
        gm_discount=(TextView)v.findViewById(R.id.gm_discount);
        download_discount=(TextView) v.findViewById(R.id.download_discount);
        total_discount=(TextView)v.findViewById(R.id.total_discount);
        share=(LinearLayout)v.findViewById(R.id.share);
        resume_game=(LinearLayout)v.findViewById(R.id.resume_game);
        String text="<html>Play more,INVITE more<br>get UPTO<font color=#ff9f1c><big> 50%</big></font><br> Brokerage Cashback<br> and Pay No Deposit</html>";
        gm_heading.setText(Html.fromHtml(text));

        text="<font color=#000000><big>20 ~ </font><font color=#ff9f1c>2%<big></font>";
        gm_discount.setText(Html.fromHtml(text));

        text="<font color=#000000><big>77 ~ </font><font color=#ff9f1c>7%<big></font>";
        download_discount.setText(Html.fromHtml(text));

        //text="<font color=#000000><big>77 ~ </font><font color=#ff9f1c>7%<big></font>";
        total_discount.setText("9");

        resume_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Game)getActivity()).DisplayBuilding();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
