package com.nbourses.oyeok.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.razorpay.Checkout;

import org.json.JSONObject;

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
    @OnClick({R.id.pb_free, R.id.pb_partner, R.id.pb_continue})
    public void onOptionClick(View v) {


        if (v.getId() == pb_free.getId()) {
pb_title.setText("Lifetime Free Trial");
            pb_des.setText("*Plus Leads of other Brokers\n*Connect with App Users in your Locality\n*Match and Market your Listings with Brokers\n*Add new Buildings in Locality, get Site Visits");
           pb_free.setTextColor(ContextCompat.getColor(getContext(), R.color.greenish_blue));
            pb_partner.setTextColor(ContextCompat.getColor(getContext(), R.color.whitesmoke));
pb_continue.setText("Continue Lifetime Free Trial");
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
            pb_continue.setText("Pay Rs. 50,000 for 3 Months");
        }
        else if (v.getId() == pb_continue.getId()) {
            if(pb_continue.getText().toString().equalsIgnoreCase("Pay Rs. 50,000 for 3 Months")){

                startPayment();
            }
        }


    }

    public void startPayment() {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.digitslogo);

        /**
         * Reference to current activity
         */
        final Activity activity = getActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: Rentomojo || HasGeek etc.
             */
            options.put("name", "Ritesh Warke");
            JSONObject theme = new JSONObject();
            theme.put("color","#2dc4b6");
            options.put("theme",theme);
            checkout.setFullScreenDisable(true);

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Paying for OYEOK Partner Broker Service");

            options.put("currency", "INR");

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            options.put("amount", "100");

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.i("Razorpay", "Error in starting Razorpay Checkout", e);
        }
    }

}


