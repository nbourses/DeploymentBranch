package com.nbourses.oyeok.JPOT.SalaryDiscovery.UI;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.OyeOkBroker.OyeIntentSpecs;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.MapWrapperLayout;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.rangeSeekBarBrokerTypePanel.marker_pricebar;


public class JexMarkerPanelScreen extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.jex_fragment_home, container, false);
        mDrooms = (Button) rootView.findViewById(R.id.linearlayout_drooms);
        mVisits = (Button) rootView.findViewById(R.id.newVisits);
        mQrCode = (ImageView) rootView.findViewById(R.id.qrCode);
        mMarkerPanel = (LinearLayout) rootView.findViewById(R.id.ll_marker);

        mMarkerpanelminus = (ImageView) rootView.findViewById(R.id.markersliderminus);
        mMarkerpanelplus =   (ImageView) rootView.findViewById(R.id.markerpanelplus);
        mMarkerpriceslider = (marker_pricebar) rootView.findViewById(R.id.price_seekbar);
        mMarkerminmax = (LinearLayout) rootView.findViewById(R.id.markerpanelminmax);
        ll_marker = (LinearLayout) rootView.findViewById(R.id.ll_marker);



        mDrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  ((MainActivity)getActivity()).changeFragment(new Drooms_Client_new(), null,"");
            }
        });
        mVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).changeFragment(new OyeIntentSpecs(), null,"");
            }
        });

        mQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                IntentIntegrator.forSupportFragment(JexMarkerPanelScreen.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(CaptureActivityAnyOrientation.class).setOrientationLocked(false).initiateScan();
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
