package com.nbourses.oyeok.RPOT.OkBroker.UI;


import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.RPOT.ApiSupport.models.PreOk;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.OkBroker.UI.SlidingTabLayout.PagerItem;
import com.nbourses.oyeok.RPOT.OkBroker.UI.SlidingTabLayout.SlidingTabLayout;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.MapWrapperLayout;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Ok_Broker_MainScreen extends Fragment implements MainActivity.openMapsClicked,CustomPhasedListener {

    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private LinearLayout mHideShow;
    private FrameLayout mMapView;
    private boolean mFirst = false;
    private CustomMapFragment customMapFragment;
    private GoogleMap map;
    private MyPagerAdapter adapter;
    private CustomPhasedSeekBar mCustomPhasedSeekbar;
    private int currentItem;
    private Button earnOk;
    private ImageButton bPinLocation;
    private LatLng latlng;
    DBHelper dbHelper;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        PagerSlidingTabStrip.width = width / 2;
         v= inflater.inflate(R.layout.broker_main_screen, container, false);
        ((MainActivity)getActivity()).setMapsClicked(this);
        //mHideShow = (LinearLayout) v.findViewById(R.id.showMap);
        mMapView = (FrameLayout) v.findViewById(R.id.mapView);
        bPinLocation = (ImageButton)v.findViewById(R.id.bPinLocation);
        dbHelper=new DBHelper(getContext());
        earnOk = (Button) v.findViewById(R.id.earnOk);


        mPager = (ViewPager) v.findViewById(R.id.pager);
        mTabs  = (SlidingTabLayout) v.findViewById(R.id.tabs);
        //mTabs.setDistributeEvenly(true);
        ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
        pagerItems.add(new PagerItem("Tenants", new Rental_Broker_Requirement()));
        pagerItems.add(new PagerItem("Owners", new Rental_Broker_Available()));
        adapter = new MyPagerAdapter(getChildFragmentManager(),pagerItems);
        mTabs.setDistributeEvenly(true);
        mTabs.setBackgroundColor(Color.parseColor("#031625"));
        mPager.setAdapter(adapter);
        mTabs.setViewPager(mPager);




        mCustomPhasedSeekbar = (CustomPhasedSeekBar) v.findViewById(R.id.phasedSeekBar);
        if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{"Rental", "Sale"}));
        else
            mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.broker_type1_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.broker_type1_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Loan", "Auction"}));
        mCustomPhasedSeekbar.setListener(this);
        bPinLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMyLocationEnabled(true);
            }
        });

        earnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeFragment(new EarnOkFragment(), null);
            }
        });

        preok();



        return v;
    }

    private void hideMap(int i) {

        Animation m = null;

        //Load animation
        if(i==0) {
            m = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_down);
        }else {

            m = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_up);
        }

        mMapView.setAnimation(m);
    }

    @Override
    public void onPause() {

        super.onPause();
        ((MainActivity)getActivity()).hideOpenMaps();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).hideResideMenu();
        ((MainActivity)getActivity()).showOpenMaps();
    }

    @Override
    public void clicked() {




        if (mMapView.getVisibility() == View.VISIBLE) {

                    hideMap(0);
                    mMapView.setVisibility(View.GONE);

                } else {
                    mMapView.setVisibility(View.VISIBLE);
                    hideMap(1);

                    if (!mFirst) {



                        customMapFragment = ((CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

                        customMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                map = googleMap;

                                map.setMyLocationEnabled(true);
                                //plotMyNeighboursHail.markerpos(my_user_id, pointer_lng, pointer_lat, which_type, my_role, map);
                                //selectedLocation = map.getCameraPosition().target;


                            }
                        });

                        customMapFragment.setOnDragListener(new MapWrapperLayout.OnDragListener() {
                            @Override
                            public void onDrag(MotionEvent motionEvent) {

                                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                                    //Toast.makeText(getActivity(), "Moved", Toast.LENGTH_LONG).show();

                                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                                    Toast.makeText(getActivity(), "Up", Toast.LENGTH_LONG).show();
                                }
                                //pin location
                                latlng = map.getCameraPosition().target;
                            }
                        });

                        new GetCurrentLocation(getActivity(), new GetCurrentLocation.CurrentLocationCallback() {
                            @Override
                            public void onComplete(Location location) {
                                if (location != null) {
                                    Double lat = location.getLatitude();
                                    Double lng = location.getLongitude();
                                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    //pin location
                                    latlng = map.getCameraPosition().target;

                                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                                    map.animateCamera(CameraUpdateFactory.zoomTo(16));

                                    //make retrofit call to get Min Max price

                                }
                            }
                        });


                        mFirst = true;
                    }
                }



    }

    @Override
    public void onPositionSelected(int position, int count) {

        if(count!=2) {

            if (position == 2) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Loan Seekers", new Loan_Broker_Requirement()));
                pagerItems.add(new PagerItem("Loan Lenders", new Loan_Broker_Available()));
                adapter.setPagerItems(pagerItems);
                adapter.notifyDataSetChanged();
                mTabs.settabData();
                mTabs.setDistributeEvenly(true);
                currentItem = 2;
                //mTabs.notifyAll();

                //mPager.invalidate();
            }

            if (position == 1) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Seekers", new Sale_Broker_Requirement_new()));
                pagerItems.add(new PagerItem("Owners", new Sale_Broker_Available_new()));
                adapter.setPagerItems(pagerItems);
                adapter.notifyDataSetChanged();
                mTabs.settabData();
                mTabs.setDistributeEvenly(true);
                currentItem = 1;
                //mTabs.notifyAll();

                //mPager.invalidate();
            }

            if (position == 0) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Tenants", new Rental_Broker_Requirement()));
                pagerItems.add(new PagerItem("Owners", new Rental_Broker_Available()));
                adapter.setPagerItems(pagerItems);
                adapter.notifyDataSetChanged();
                mTabs.settabData();
                mTabs.setDistributeEvenly(true);
                currentItem = 0;
                //mTabs.notifyAll();

                //mPager.invalidate();
            }

            if (position == 3) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Tenants", new Rental_Broker_Requirement()));
                pagerItems.add(new PagerItem("Owners", new Rental_Broker_Available()));
                adapter.setPagerItems(pagerItems);
                adapter.notifyDataSetChanged();
                mTabs.settabData();
                mTabs.setDistributeEvenly(true);
                currentItem = 3;
                //mTabs.notifyAll();

                //mPager.invalidate();
            }
        }

        else{
            if (position == 1) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Seekers", new Sale_Broker_Requirement_new()));
                pagerItems.add(new PagerItem("Owners", new Sale_Broker_Available_new()));
                adapter.setPagerItems(pagerItems);
                adapter.notifyDataSetChanged();
                mTabs.settabData();
                mTabs.setDistributeEvenly(true);
                currentItem = 1;
                //mTabs.notifyAll();

                //mPager.invalidate();
            }

            if (position == 0) {
                ArrayList<PagerItem> pagerItems = new ArrayList<PagerItem>();
                pagerItems.add(new PagerItem("Tenants", new Rental_Broker_Requirement()));
                pagerItems.add(new PagerItem("Owners", new Rental_Broker_Available()));
                adapter.setPagerItems(pagerItems);
                adapter.notifyDataSetChanged();
                mTabs.settabData();
                mTabs.setDistributeEvenly(true);
                currentItem = 0;
                //mTabs.notifyAll();

                //mPager.invalidate();
            }
        }

    }

    class MyPagerAdapter extends FragmentPagerAdapter
    {
        String[] title = {"Tenants","Owners"};
        int phasedSeekbarPosition = 0;

        private FragmentManager mFragmentManager;
        private ArrayList<PagerItem> mPagerItems;

        public MyPagerAdapter(FragmentManager fm,ArrayList<PagerItem> pagerItems) {
            super(fm);
            mFragmentManager = fm;
            mPagerItems = pagerItems;
        }

        public void setPagerItems(ArrayList<PagerItem> pagerItems) {
            if (mPagerItems != null)
                for (int i = 0; i < mPagerItems.size(); i++) {
                    mFragmentManager.beginTransaction().remove(mPagerItems.get(i).getFragment()).commit();
                }
            mPagerItems = pagerItems;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
//            if(phasedSeekbarPosition == 0) {
//                if (position == 1) {
//                    return new Rental_Broker_Available();
//                } else {
//                    return new Rental_Broker_Requirement();
//                }
//            }else
//            {
//                if (position == 1) {
//                    return new Rental_Broker_Available();
//                } else {
//                    return new Rental_Broker_Requirement();
//                }
//
//            }

            return mPagerItems.get(position).getFragment();

        }

        public void setTitleAndPosition(String[] titles,int position)
        {
            this.title = titles;
            this.phasedSeekbarPosition = position;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPagerItems.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return mPagerItems.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + mPager.getCurrentItem());


        if(currentItem == 0 || currentItem == 3) {

            if (mPager.getCurrentItem() == 0) {
                Rental_Broker_Requirement f = (Rental_Broker_Requirement) page;
                f.onActivityResult(requestCode, resultCode, data);
            } else {
                Rental_Broker_Available f = (Rental_Broker_Available) page;
                f.onActivityResult(requestCode, resultCode, data);
            }
        }else if(currentItem == 1) {

            if (mPager.getCurrentItem() == 0) {
                Sale_Broker_Requirement_new f = (Sale_Broker_Requirement_new) page;
                f.onActivityResult(requestCode, resultCode, data);
            } else {
                Sale_Broker_Available_new f = (Sale_Broker_Available_new) page;
                f.onActivityResult(requestCode, resultCode, data);
            }
        }else if(currentItem == 2) {

            if (mPager.getCurrentItem() == 0) {
                Loan_Broker_Requirement f = (Loan_Broker_Requirement) page;
                f.onActivityResult(requestCode, resultCode, data);
            } else {
                Loan_Broker_Available f = (Loan_Broker_Available) page;
                f.onActivityResult(requestCode, resultCode, data);
            }
        }



    }

    public void setPhasedSeekBar(){

        mCustomPhasedSeekbar = (CustomPhasedSeekBar) v.findViewById(R.id.phasedSeekBar);
        if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{"Rental", "Sale"}));
        else
            mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.broker_type1_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.broker_type1_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Loan", "Auction"}));
        mCustomPhasedSeekbar.setListener(this);
    }

    public void preok() {
        String API = "http://52.25.136.179:9000";
        Oyeok preok = new Oyeok();
        preok.setDeviceId("Hardware");
        preok.setGcmId("gliui");
        preok.setUserRole("broker");
        preok.setLong("72.1456");
        preok.setLat("19.2344");


        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService user1 = restAdapter.create(OyeokApiService.class);
        user1.preOk(preok, new Callback<PreOk>() {
            @Override
            public void success(PreOk preok, Response response) {
                //Toast.makeText(getContext(), "get price success", Toast.LENGTH_LONG).show();
                //Log.i("oye_id", preok.responseData.neighbours.reqLl[0].getOyeId());
                //String req_ll= Arrays.toString(preok.responseData.neighbours.reqLl[0]);
                //Arrays.toString();
                //Log.i("req_ll",req_ll);
                //String[] stringArray = Arrays.copyOf(preok.responseData.neighbours.reqLl, preok.responseData.neighbours.reqLl.length, String[].class);
                //String[] stringArray=Arrays.asList(preok.responseData.neighbours.reqLl).toArray(new String[preok.responseData.neighbours.reqLl.length]);
                //Log.i("req_ll",stringArray.toString());
            }

            @Override
            public void failure(RetrofitError error) {


            }
        });
    }

}
