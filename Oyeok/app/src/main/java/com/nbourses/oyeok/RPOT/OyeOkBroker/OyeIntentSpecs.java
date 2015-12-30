package com.nbourses.oyeok.RPOT.OyeOkBroker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;
import com.nbourses.oyeok.activity.MessagesFragment;
import com.nbourses.oyeok.RPOT.ApiSupport.models.LetsOye;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class OyeIntentSpecs extends Fragment {

    private Button mOye;
    private ImageView homeImageView,shopImageView,officeImageView,industrialImageView,othersImageView;
    //String off_mode;

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
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        mOye = (Button) rootView.findViewById(R.id.bt_oye);
        homeImageView= (ImageView) rootView.findViewById(R.id.icon_home);
        shopImageView= (ImageView) rootView.findViewById(R.id.icon_shop);
        officeImageView= (ImageView) rootView.findViewById(R.id.icon_office);
        industrialImageView= (ImageView) rootView.findViewById(R.id.icon_industrial);
        othersImageView= (ImageView) rootView.findViewById(R.id.icon_others);
        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout fragContainer = (LinearLayout) getActivity().findViewById(R.id.layout_container);

                LinearLayout ll = new LinearLayout(getContext());
                ll.setOrientation(LinearLayout.HORIZONTAL);

                //ll.setId(12345);

                //getFragmentManager().beginTransaction().add(ll.getId(), TestFragment.newInstance("I am frag 1"), "someTag1").commit();
                //getFragmentManager().beginTransaction().add(ll.getId(), TestFragment.newInstance("I am frag 2"), "someTag2").commit();

                fragContainer.addView(ll);

            }
        });
        shopImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        officeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        industrialImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        othersImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        mOye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        Oyeok oyeOk = new Oyeok();
        oyeOk.setSpecCode("LL-3BHK-9Cr");
        oyeOk.setReqAvl("req");
        oyeOk.setUserId("egtgxhr02ai31a2uzu82ps2bysljv43n");
        oyeOk.setUser_role("client");
        oyeOk.setLong("19");
        oyeOk.setLat("17");
        oyeOk.setRegion("powai");
        oyeOk.setPincode("400058");
        String off_mode = "NO";
        String API="http://ec2-52-25-136-179.us-west-2.compute.amazonaws.com:9000";
        RestAdapter restAdapter1 = new RestAdapter.Builder().setEndpoint(API).build();
        restAdapter1.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeok = restAdapter1.create(OyeokApiService.class);
        oyeok.letsOye(oyeOk, new Callback<LetsOye>() {
            @Override
            public void success(LetsOye letsOye, retrofit.client.Response response) {
                //if(!off_mode.equals("yes")) {
                    //String s = post(nameValuePairs);
                    String s= letsOye.getResponseData();
                    if (!s.equals("")) {
                        try {

                            if (s.equalsIgnoreCase("Your Oye is published")) {
                                /*FirebaseClass.setOyebookRecord(UserCredentials.getString(EnterConfigActivity.this, PreferenceKeys.MY_SHORTMOBILE_KEY), reNt, show, lng.toString(), lat.toString(), user_id, bhkval + "BHK", msg4, UserCredentials.getString(EnterConfigActivity.this, PreferenceKeys.CURRENT_LOC_KEY));
                                Intent NextActivity = new Intent(context, MainActivity.class);
                                startActivity(NextActivity);
                                UserCredentials.saveString(context, PreferenceKeys.SUCCESSFUL_HAIL, "true");*/
                                Toast.makeText(getContext(), "Oye published.Sit back and relax while we find a broker for you", Toast.LENGTH_LONG).show();
                                //finish();

                            } else if (s.equalsIgnoreCase("User already has an active oye. Pls end first")) {
                                /*Intent NextActivity = new Intent(context, MainActivity.class);
                                startActivity(NextActivity);*/
                                Toast.makeText(getContext(), "You already have an active oye. Pls end it first", Toast.LENGTH_LONG).show();
                                //finish();
                            } else

                            {
                                /*Intent NextActivity = new Intent(context, MainActivity.class);
                                startActivity(NextActivity);*/
                                Toast.makeText(getContext(), "There is some error.", Toast.LENGTH_LONG).show();
                                //finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }
                ((MainActivity)getActivity()).changeFragment(new MessagesFragment());
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
                Toast.makeText(getContext(), "lets oye call failed in enter config",
                        Toast.LENGTH_LONG).show();
                // FirebaseClass.setOyebookRecord(UserCredentials.getString(EnterConfigActivity.this,PreferenceKeys.MY_SHORTMOBILE_KEY),reNt,show,lng,lat,user_id,bhkval+"BHK",msg4,UserCredentials.getString(EnterConfigActivity.this,PreferenceKeys.CURRENT_LOC_KEY));
                //Intent NextActivity = new Intent(context, MainActivity.class);
                //startActivity(NextActivity);finish();
                Log.i("TAG", "lets oye call failed in enter config");
                Log.i("TAG", "inside error" + error.getMessage());
            }
        });

    }

}
