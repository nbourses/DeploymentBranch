package com.nbourses.oyeok.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nbourses.oyeok.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartnerBrokerFragment extends Fragment {

    @Bind(R.id.pb_title)
    TextView pb_title;

    @Bind(R.id.pb_continue)
    TextView pb_continue;

    @Bind(R.id.pb_des)
    TextView pb_des;

    @Bind(R.id.pb_free)
    Button pb_free;

    @Bind(R.id.pb_partner)
    Button pb_partner;

    public PartnerBrokerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_partner_broker, container,
                false);
        ButterKnife.bind(this, rootView);


        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();


    }
    private void init(){






    }
    @OnClick({R.id.pb_free, R.id.pb_partner})
    public void onOptionClick(View v) {


        if (v.getId() == pb_free.getId()) {
pb_title.setText("Lifetime Free Trial");
            pb_des.setText("*Plus Leads of other Brokers\n*Connect with App Users in your Locality\n*Match and Market your Listings with Brokers\n*Add new Buildings in Locality, get Site Visits");
           pb_free.setTextColor(ContextCompat.getColor(getContext(), R.color.greenish_blue));
            pb_partner.setTextColor(ContextCompat.getColor(getContext(), R.color.whitesmoke));

            //pb_free.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.gradient_button_bg_with_border));
            //pb_partner.setBackground(null);

        }
        else if (v.getId() == pb_partner.getId()) {
            //pb_partner.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.gradient_button_bg_with_border));
            //pb_free.setBackground(null);
            pb_title.setText("Exclusive Properties");
            pb_free.setTextColor(ContextCompat.getColor(getContext(), R.color.whitesmoke));
            pb_partner.setTextColor(ContextCompat.getColor(getContext(), R.color.greenish_blue));
pb_des.setText("*Assured 30 Site Visits per Month\n*Dedicated Oyeok Assistant Manager\n*Every Deal min: Rs 35,000\n*Limited Edition to 10 Brokers per Area");
        }


    }

}


