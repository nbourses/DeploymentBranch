package com.nbourses.oyeok.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.activities.Game;

public class gameCardView extends Fragment {


    public gameCardView() {
        // Required empty public constructor
    }

LinearLayout Start;
    View v;
   TextView Exit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         v=inflater.inflate(R.layout.fragment_gamecardview, container, false);

        Start= (LinearLayout) v.findViewById(R.id.startgame);
        Exit=(TextView) v.findViewById(R.id.btn_exit);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Game.class);
                startActivity(intent);
            }
        });
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ClientMainActivity)getActivity()).closeCardContainer();
            }
        });
        return v;
    }


}
    /*Intent intent = new Intent(getContext(), Game.class);
    startActivity(intent);*/