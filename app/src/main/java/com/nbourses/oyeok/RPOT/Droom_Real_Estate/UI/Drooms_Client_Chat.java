package com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.HardParameter.hard_parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Drooms_Client_Chat extends Fragment implements Droom_Chat_Layout.itemClickListener,hard_parameters.onParameterSelect {
    private FloatingActionButton floatingActionButton;
    private JSONArray mChatArray = new JSONArray();
    private JSONArray mAccepted  = new JSONArray();
    private JSONArray mRejected  = new JSONArray();

    private FloatingActionButton mAcceptedButton;
    private FloatingActionButton mRejectedButton;

    private FrameLayout macceptedContainer;
    private FrameLayout mRejectedContainer;
    private Droom_Chat_Layout chatScreen;
    private Droom_Chat_Layout acceptedScreen;
    private Droom_Chat_Layout rejectedScreen;
    private boolean acceptedFirst = false;
    private boolean rejectedFirst = false;
    private FrameLayout mChatContainer;
    private FrameLayout keyboardContainer;
    private ViewPager mPagerParameters;
    private ParametersPagerAdapter pa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.droom_chat, container, false);
        floatingActionButton =  (FloatingActionButton) v.findViewById(R.id.menu);
        mAcceptedButton    = (FloatingActionButton) v.findViewById(R.id.liked);
        mRejectedButton    = (FloatingActionButton) v.findViewById(R.id.disliked);
        macceptedContainer = (FrameLayout) v.findViewById(R.id.likescontainer);
        mRejectedContainer = (FrameLayout) v.findViewById(R.id.dislikescontainer);
        mChatContainer     = (FrameLayout) v.findViewById(R.id.container);
        keyboardContainer  = (FrameLayout) v.findViewById(R.id.keyboardContainer);
        mPagerParameters   = (ViewPager) v.findViewById(R.id.pagerParameters);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboardContainer.setVisibility(View.VISIBLE);
                if(pa == null) {
                    pa = new ParametersPagerAdapter(getChildFragmentManager());

                    mPagerParameters.setAdapter(pa);
                }
            }
        });

        mAcceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mRejectedContainer.getVisibility() == View.VISIBLE) {
                    hide(0, mRejectedContainer);
                    mRejectedContainer.setVisibility(View.GONE);
                }

                if (macceptedContainer.getVisibility() == View.VISIBLE) {
                    hide(0, macceptedContainer);
                    macceptedContainer.setVisibility(View.GONE);
                    macceptedContainer.setAlpha(1.0f);

                } else {
                    macceptedContainer.setAlpha(0.5f);
                    macceptedContainer.setVisibility(View.VISIBLE);
                    hide(1, macceptedContainer);
                    if (!acceptedFirst) {

                        acceptedScreen = new Droom_Chat_Layout();
                        Bundle args = new Bundle();
                        args.putString("array", mAccepted.toString());
                        args.putBoolean("flag", false);
                        acceptedScreen.setArguments(args);
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.add(R.id.likescontainer, acceptedScreen).commit();
                        acceptedFirst = true;

                    } else {
                        acceptedScreen.setAdapter(mAccepted, false);
                        acceptedScreen.setupview();

                    }
                }


            }
        });


        mRejectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(macceptedContainer.getVisibility() == View.VISIBLE)
                {
                    hide(0,macceptedContainer);
                    macceptedContainer.setVisibility(View.GONE);
                }



                if (mRejectedContainer.getVisibility() == View.VISIBLE) {
                    hide(0, mRejectedContainer);
                    mRejectedContainer.setVisibility(View.GONE);
                    mRejectedContainer.setAlpha(1.0f);

                } else {
                    mRejectedContainer.setAlpha(0.5f);
                    mRejectedContainer.setVisibility(View.VISIBLE);
                    hide(1, mRejectedContainer);
                    if (!rejectedFirst) {

                        rejectedScreen = new Droom_Chat_Layout();
                        Bundle args = new Bundle();
                        args.putString("array", mRejected.toString());
                        args.putBoolean("flag", false);
                        rejectedScreen.setArguments(args);
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.add(R.id.dislikescontainer, rejectedScreen).commit();
                        rejectedFirst = true;

                    } else {
                        rejectedScreen.setAdapter(mRejected, false);
                        rejectedScreen.setupview();

                    }
                }




            }
        });








        JSONObject x = new JSONObject();
        try {
            x.put("direction","left");
            x.put("text","some percentage 20%");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mChatArray.put(x);
        mAccepted.put(x);
        mRejected.put(x);

        JSONObject y = new JSONObject();
        try {
            y.put("direction","right");
            y.put("text", "Rent : 20k");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mChatArray.put(y);
        mAccepted.put(y);
        mRejected.put(y);

        JSONObject z = new JSONObject();
        try {
            z.put("direction", "left");
            z.put("text", "Advance 3L");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mChatArray.put(z);
        mAccepted.put(z);
        mRejected.put(z);






        chatScreen = new Droom_Chat_Layout();
        Bundle args = new Bundle();
        args.putString("array", mChatArray.toString());
        args.putBoolean("flag", true);
        chatScreen.setArguments(args);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.container, chatScreen).commit();
         chatScreen.setItemClickListener(this);







        return v;
    }


    private void hide(int i,FrameLayout p) {

        Animation m = null;

        //Load animation
        if(i==0) {
            m = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_down);
        }else {

            m = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_up);
        }

        p.setAnimation(m);
    }


    @Override
    public void onClicked(int position, String status) {
        JSONObject moved_object = new JSONObject();
        JSONArray shift_array = new JSONArray();
        for(int i=0;i<mChatArray.length();i++)
        {
            if( i != position)
            {
                try {
                    shift_array.put(mChatArray.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                try {
                    moved_object = mChatArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        mChatArray = shift_array;


        if(status.equals("liked"))
        {
            mAccepted.put(moved_object);
        }else
        {
            mRejected.put(moved_object);
        }
    }

    @Override
    public void onParameterSelected(String key, String value) {

        Toast.makeText(getActivity(),"key : "+key+" and value :"+value,Toast.LENGTH_LONG).show();
        JSONObject new_value = new JSONObject();
        try {
            new_value.put("text",key + " : "+value);
            new_value.put("direction","left");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mChatArray.put(new_value);
        chatScreen.setAdapter(mChatArray,true);
        chatScreen.setupview();
        keyboardContainer.setVisibility(View.GONE);
    }


    class ParametersPagerAdapter extends FragmentPagerAdapter
    {

        public ParametersPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            hard_parameters hp = new hard_parameters();
            hp.setMonParameterSelect(Drooms_Client_Chat.this);
            return hp;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
