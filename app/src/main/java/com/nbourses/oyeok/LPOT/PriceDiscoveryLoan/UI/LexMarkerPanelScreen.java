package com.nbourses.oyeok.LPOT.PriceDiscoveryLoan.UI;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI.Drooms_Client_new;
import com.nbourses.oyeok.RPOT.OyeOkBroker.OyeIntentSpecs;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.MapWrapperLayout;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.QrCode.CaptureActivityAnyOrientation;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.rangeSeekBarBrokerTypePanel.marker_pricebar;


public class LexMarkerPanelScreen extends Fragment {

    private Button mDrooms;
    private Button mVisits;
    private ImageView mQrCode;
    private LinearLayout mMarkerPanel;

    private ImageView mMarkerpanelminus;
    private ImageView mMarkerpanelplus;
    private marker_pricebar mMarkerpriceslider;
    private LinearLayout mMarkerminmax;
    private GoogleMap map;
    private LinearLayout ll_marker;
    private LinearLayout broker_panel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lex_fragment_home, container, false);
        mDrooms = (Button) rootView.findViewById(R.id.linearlayout_drooms);
        mVisits = (Button) rootView.findViewById(R.id.newVisits);
        mQrCode = (ImageView) rootView.findViewById(R.id.qrCode);
        mMarkerPanel = (LinearLayout) rootView.findViewById(R.id.ll_marker);

        mMarkerpanelminus = (ImageView) rootView.findViewById(R.id.markersliderminus);
        mMarkerpanelplus =   (ImageView) rootView.findViewById(R.id.markerpanelplus);
        mMarkerpriceslider = (marker_pricebar) rootView.findViewById(R.id.price_seekbar);
        mMarkerminmax = (LinearLayout) rootView.findViewById(R.id.markerpanelminmax);
        ll_marker = (LinearLayout) rootView.findViewById(R.id.ll_marker);
        broker_panel = (LinearLayout) rootView.findViewById(R.id.seekbar_linearlayout);


        mDrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).changeFragment(new Drooms_Client_new(), null);
            }
        });
        mVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).changeFragment(new OyeIntentSpecs(), null);
            }
        });

        mQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator.forSupportFragment(LexMarkerPanelScreen.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(CaptureActivityAnyOrientation.class).setOrientationLocked(false).initiateScan();
            }
        });

        mMarkerPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMarkerminmax.setVisibility(View.GONE);
                mMarkerpanelminus.setVisibility(View.VISIBLE);
                mMarkerpanelplus.setVisibility(View.VISIBLE);
                mMarkerpriceslider.setVisibility(View.VISIBLE);

            }
        });

        mMarkerpanelminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m = mMarkerpriceslider.getLeft();
                mMarkerpriceslider.setLeft(m - 1);

            }
        });
        mMarkerpanelplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m = mMarkerpriceslider.getRight();
                mMarkerpriceslider.setRight(m + 1);
            }
        });





        CustomMapFragment customMapFragment = ((CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

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
                    mMarkerpanelminus.setVisibility(View.GONE);
                    mMarkerpanelplus.setVisibility(View.GONE);
                    mMarkerpriceslider.setVisibility(View.GONE);
                    mMarkerminmax.setVisibility(View.GONE);
                    ll_marker.setVisibility(View.INVISIBLE);


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    mMarkerpanelminus.setVisibility(View.GONE);
                    mMarkerpanelplus.setVisibility(View.GONE);
                    mMarkerpriceslider.setVisibility(View.GONE);
                    ll_marker.setVisibility(View.VISIBLE);
                    mMarkerminmax.setVisibility(View.VISIBLE);

                }
            }
        });




        new GetCurrentLocation(getActivity(), new GetCurrentLocation.CurrentLocationCallback() {
            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    Double lat = location.getLatitude();
                    Double lng = location.getLongitude();
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());



                    map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map.animateCamera(CameraUpdateFactory.zoomTo(16));

                }
            }
        });



//        PhasedSeekBar psbHorizontal = (PhasedSeekBar) rootView.findViewById(R.id.psb_hor);
//        final Resources resources = getResources();
//        psbHorizontal.setAdapter(new SimplePhasedAdapter(resources, new int[]{
//                R.drawable.btn_square_selector,
//                R.drawable.btn_triangle_selector,
//                R.drawable.btn_xis_selector,
//                R.drawable.btn_square_selector,
//                R.drawable.btn_triangle_selector,
//                R.drawable.btn_xis_selector
//
//        }
//        ));
        // Inflate the layout for this fragment
         DisplayMetrics metrics = new DisplayMetrics();
         getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int layoutwidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, metrics);
        int layoutheight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, metrics);
        int marginleft = (width - (3*layoutwidth)) / 3;
        final String[] agents = {"Home","Car","P2P"};
        int [] time = {10,20,30};
        int [] drawables= {R.drawable.broker_type1,R.drawable.broker_type2,R.drawable.broker_type4};

        for(int i=0;i<3;i++) {

            View child = getActivity().getLayoutInflater().inflate(R.layout.layout_item, null);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    layoutwidth, layoutheight);

            layoutParams.setMargins(marginleft, 0, 0, 0);



            TextView agent_name = (TextView) child.findViewById(R.id.brokerType);
            ImageView agent_pic = (ImageView) child.findViewById(R.id.brokerImg);
            TextView  agent_time = (TextView) child.findViewById(R.id.brokerTime);
            agent_name.setText(agents[i]);
            agent_pic.setImageResource(drawables[i]);
            agent_time.setText(String.valueOf(time[i]) + " mins");
            final String name = agents[i];

            agent_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getActivity(),"You have selected "+name+"loan agent",Toast.LENGTH_LONG).show();
                }
            });
            broker_panel.addView(child,layoutParams);
        }




        return rootView;
    }

    private void displayToast(String toast) {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String toast = null;
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Cancelled from fragment";
            } else {
                toast = "Scanned from fragment: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast(toast);
        }
    }


}
