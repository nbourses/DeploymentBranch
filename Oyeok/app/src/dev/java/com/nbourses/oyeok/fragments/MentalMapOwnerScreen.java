package com.nbourses.oyeok.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MentalMapOwnerScreen extends Fragment {

    @Bind(R.id.selected_config)
    TextView selected_config;
    @Bind(R.id.mentalOwnerRG)
    RadioGroup mentalOwnerRG;
    @Bind(R.id.rk1)
    RadioButton rk1;
    @Bind(R.id.bhk1)
    RadioButton bhk1;
    @Bind(R.id.bhk1_5)
    RadioButton bhk1_5;
    @Bind(R.id.bhk2)
    RadioButton bhk2;
    @Bind(R.id.bhk2_5)
    RadioButton bhk2_5;
    @Bind(R.id.bhk3)
    RadioButton bhk3;
    @Bind(R.id.bhk3_5)
    RadioButton bhk3_5;
    @Bind(R.id.bhk4)
    RadioButton bhk4;
    @Bind(R.id.bhk4_5)
    RadioButton bhk4_5;
    @Bind(R.id.bhk5)
    RadioButton bhk5;
    @Bind(R.id.bhk5_5)
    RadioButton bhk5_5;
    @Bind(R.id.bhk6)
    RadioButton bhk6;
    @Bind(R.id.area)
    TextView area;
    @Bind(R.id.next)
    LinearLayout next;

private String config = "2bhk";
    public MentalMapOwnerScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mental_map_owner_screen, container, false);
        ButterKnife.bind(this,v);

        Bundle b = getArguments();
        if(b.containsKey(AppConstants.QUESTION))
            init(b.getString(AppConstants.QUESTION));

        return v;
    }

    private void init(final String Q){

        mentalOwnerRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch (checkedId)
                {
                    case R.id.rk1:
                        selected_config.setText( "1 RK" );

                        area.setText("300 sq.ft.");
                        config = "1rk";

                        break;
                    case R.id.bhk1:
                        selected_config.setText( "1 BHK" );
                        area.setText("600 sq.ft.");
                        config = "1bhk";

                        break;
                    case R.id.bhk1_5:
                        selected_config.setText( "1.5 BHK" );
                        area.setText("800 sq.ft.");
                        config = "1.5bhk";


                        break;
                    case R.id.bhk2:
                        selected_config.setText( "2 BHK" );

                        area.setText("950 sq.ft.");
                        config = "2bhk";

                        break;
                    case R.id.bhk2_5:
                        selected_config.setText( "2.5 BHK" );


                        area.setText("1300 sq.ft.");
                        config = "2.5bhk";

                        break;
                    case R.id.bhk3:
                        selected_config.setText( "3 BHK" );
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"3BHK");
                        area.setText("1600 sq.ft.");
                        config = "3bhk";
                        break;
                    case R.id.bhk3_5:
                        selected_config.setText( "3.5 BHK" );
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"3.5BHK");
                        area.setText("1800 sq.ft.");
                        config = "3.5bhk";
                        break;
                    case R.id.bhk4:
                        selected_config.setText( "4 BHK" );
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"4BHK");
                        config = "4bhk";

                        break;
                    case R.id.bhk4_5:
                        selected_config.setText( "4.5 BHK" );
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"4.5BHK");
                        area.setText("2300 sq.ft.");
                        config = "4.5bhk";

                        break;
                    case R.id.bhk5:
                        selected_config.setText( "5 BHK" );
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"5BHK");
                        area.setText("2500 sq.ft.");
                        config = "5bhk";

                        break;
                    case R.id.bhk5_5:
                        selected_config.setText( "5.5 BHK" );
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"5.5BHK");
                        area.setText("2700 sq.ft.");
                        config = "5.5bhk";

                        break;
                    case R.id.bhk6:
                        selected_config.setText( "6 BHK" );
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"6BHK");
                        area.setText("2900 sq.ft.");
                        config = "6bhk";

                        break;
                    default:
                        break;
                }
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.card)).commit();

            if(Q.equalsIgnoreCase(AppConstants.OWNERQ1)) {
                Intent intent = new Intent(AppConstants.SETLOCN);
                intent.putExtra(AppConstants.QUESTION, AppConstants.OWNERQ1);
                intent.putExtra(AppConstants.PROPERTY_CONFIG,config);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }
             else if(Q.equalsIgnoreCase(AppConstants.OWNERQ2)) {

               Intent intent = new Intent(AppConstants.SETLOCN);
                intent.putExtra(AppConstants.QUESTION, AppConstants.OWNERQ2);

                intent.putExtra(AppConstants.CONFIG,config);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            }


         }
     });

}}
