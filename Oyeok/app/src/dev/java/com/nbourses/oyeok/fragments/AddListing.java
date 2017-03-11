package com.nbourses.oyeok.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nbourses.oyeok.MyApplication;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.activities.*;
import com.nbourses.oyeok.activities.BrokerMap;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;


public class AddListing extends Fragment {

    private OnFragmentInteractionListener mListener;
    View v;
    private RadioButton rk1,bhk1,bhk1_5,bhk2,bhk2_5,bhk3,bhk3_5,bhk4,bhk4_5,bhk5,bhk5_5,bhk6;
    private RadioGroup radioGroup1;
    TextView selected_config,home,office,shop,industry,next1,Cancel_add_building,b_type,area,conf;
    private String Property;
    LinearLayout next;
    HorizontalScrollView hsl;
    String approx_area,Entry_point="";

    Bundle b;

    public AddListing() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       v  =  inflater.inflate(R.layout.fragment_add_building_card_view, container, false);

        rk1=(RadioButton) v.findViewById( R.id.rk1 );
        bhk1=(RadioButton) v.findViewById( R.id.bhk1 );
        bhk1_5=(RadioButton) v.findViewById( R.id.bhk1_5 );
        bhk2=(RadioButton) v.findViewById( R.id.bhk2 );
        bhk2_5=(RadioButton) v.findViewById( R.id.bhk2_5 );
        bhk3=(RadioButton) v.findViewById( R.id.bhk3 );
        bhk3_5=(RadioButton) v.findViewById( R.id.bhk3_5 );
        bhk4=(RadioButton) v.findViewById( R.id.bhk4 );
        bhk4_5=(RadioButton) v.findViewById( R.id.bhk4_5 );
        bhk5=(RadioButton) v.findViewById( R.id.bhk5 );
        bhk5_5=(RadioButton) v.findViewById( R.id.bhk5_5 );
        bhk6=(RadioButton) v.findViewById( R.id.bhk6 );
        conf=(TextView)v.findViewById(R.id.conf);
        b_type=(TextView)v.findViewById(R.id.b_type);

        radioGroup1 = (RadioGroup) v.findViewById(R.id.radioGroup1);

        selected_config =(TextView) v.findViewById( R.id.selected_config );

        home=(TextView) v.findViewById( R.id.btn_home ) ;
        office=(TextView) v.findViewById( R.id.btn_office ) ;
        shop=(TextView) v.findViewById( R.id.btn_shop ) ;
        industry=(TextView) v.findViewById( R.id.btn_industrial ) ;
        hsl=(HorizontalScrollView) v.findViewById( R.id.horizontalScrollView );
        home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));

        area=(TextView) v.findViewById(R.id.area);

        General.setSharedPreferences(getContext(),AppConstants.PROPERTY,"home");

        b=new Bundle();
        b=getArguments();
        if(b!=null&&b.containsKey("add_listing")) {
            Entry_point = b.getString("add_listing");
            Log.i("Entry_point", "Entry_point read   :"+ Entry_point);
           // Entry_point=b.getString("listing_id");

        }

        MyApplication application = (MyApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();

        mTracker.setScreenName("ListingStart");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());




        home.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        } );
        office.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SnackbarManager.show(
                        Snackbar.with(getActivity())
                                .text("Comming Soon..")
                                .position(Snackbar.SnackbarPosition.TOP)
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());
                /*Property="office";
                b_type.setText("Office");

                industry.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                shop.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                office.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));
                home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));*/
            }
        } );
        shop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SnackbarManager.show(
                        Snackbar.with(getActivity())
                                .text("Comming Soon..")
                                .position(Snackbar.SnackbarPosition.TOP)
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());

                /*Property="shop";
                b_type.setText("Shop");

                industry.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                shop.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));
                office.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));*/
            }
        } );

        industry.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SnackbarManager.show(
                        Snackbar.with(getActivity())
                                .text("Comming Soon..")
                                .position(Snackbar.SnackbarPosition.TOP)
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());

                /*Property="industries";
                b_type.setText("INDUSTRIES");
                industry.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));
                shop.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                office.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));*/

            }
        } );
        home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));
//        home.setPressed(true);


        next=(LinearLayout) v.findViewById(R.id.next);
        Cancel_add_building=(TextView) v.findViewById(R.id.Cancel_add_building);



        Display display = ((Activity )getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;

        int center = (width - rk1.getWidth())/2;
        Property="home" ;
        b_type.setText("Home");
        //General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"2BHK");

        selected_config.setText( "2 BHK" );
        hsl.scrollTo(bhk2.getLeft() - center/2, bhk2.getTop());
        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"2BHK");
        area.setText("950 sq.ft.");
        approx_area="950";

        // Checked change Listener for RadioGroup 1
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                int center = (width - rk1.getWidth())/2;
                switch (checkedId)
                {
                    case R.id.rk1:
                        selected_config.setText( "1 RK" );
                        hsl.scrollTo(rk1.getLeft() - center/2, rk1.getTop());
                        General.setSharedPreferences(getContext(), AppConstants.PROPERTY_CONFIG,"1RK");
                        area.setText("300 sq.ft.");
                        approx_area="300";
                        conf.setText(selected_config.getText());

                        break;
                    case R.id.bhk1:
                        selected_config.setText( "1 BHK" );
                        hsl.scrollTo(bhk1.getLeft() - center/2, bhk1.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"1BHK");
                        area.setText("600 sq.ft.");
                        approx_area="600";
                        conf.setText(selected_config.getText());

                        break;
                    case R.id.bhk1_5:
                        selected_config.setText( "1.5 BHK" );
                        hsl.scrollTo(bhk1_5.getLeft() - center/2, bhk1_5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"1.5BHK");
                        area.setText("800 sq.ft.");
                        approx_area="800";
                        conf.setText(selected_config.getText());


                        break;
                    case R.id.bhk2:
                        selected_config.setText( "2 BHK" );
                        hsl.scrollTo(bhk2.getLeft() - center/2, bhk2.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"2BHK");
                        area.setText("950 sq.ft.");
                        approx_area="950";
                        conf.setText(selected_config.getText());

                        break;
                    case R.id.bhk2_5:
                        selected_config.setText( "2.5 BHK" );

                        hsl.scrollTo(bhk2_5.getLeft() - center/2, bhk2_5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"2.5BHK");
                        area.setText("1300 sq.ft.");
                        approx_area="1300";
                        conf.setText(selected_config.getText());

                        break;
                    case R.id.bhk3:
                        selected_config.setText( "3 BHK" );
                        hsl.scrollTo(bhk3.getLeft() - center/2, bhk3.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"3BHK");
                        area.setText("1600 sq.ft.");
                        approx_area="1600";
                        conf.setText(selected_config.getText());
                        break;
                    case R.id.bhk3_5:
                        selected_config.setText( "3.5 BHK" );
                        hsl.scrollTo(bhk3_5.getLeft() - center/2, bhk3_5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"3.5BHK");
                        area.setText("1800 sq.ft.");
                        approx_area="1800";
                        conf.setText(selected_config.getText());
                        break;
                    case R.id.bhk4:
                        selected_config.setText( "4 BHK" );
                        hsl.scrollTo(bhk4.getLeft() - center/2, bhk4.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"4BHK");
                        approx_area="2100";
                        area.setText("2100 sq.ft.");
                        conf.setText(selected_config.getText());

                        break;
                    case R.id.bhk4_5:
                        selected_config.setText( "4.5 BHK" );
                        hsl.scrollTo(bhk4_5.getLeft() - center/2, bhk4_5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"4.5BHK");
                        area.setText("2300 sq.ft.");
                        approx_area="2300";
                        conf.setText(selected_config.getText());

                        break;
                    case R.id.bhk5:
                        selected_config.setText( "5 BHK" );
                        hsl.scrollTo(bhk5.getLeft() - center/2, bhk5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"5BHK");
                        area.setText("2500 sq.ft.");
                        approx_area="2500";
                        conf.setText(selected_config.getText());

                        break;
                    case R.id.bhk5_5:
                        selected_config.setText( "5.5 BHK" );
                        hsl.scrollTo(bhk5_5.getLeft() - center/2, bhk5_5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"5.5BHK");
                        area.setText("2700 sq.ft.");
                        approx_area="2700";
                        conf.setText(selected_config.getText());

                        break;
                    case R.id.bhk6:
                        selected_config.setText( "6 BHK" );
                        hsl.scrollTo(bhk6.getLeft() - center/2, bhk6.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"6BHK");
                        area.setText("2900 sq.ft.");
                        approx_area="2900";
                        conf.setText(selected_config.getText());

                        break;
                    default:
                        break;
                }
            }
        });


        Cancel_add_building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.PROPERTY="Home";
                if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("Listing")){
                    ((BrokerListingActivity) getActivity()).closeCardContainer();
                }else
                if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("portfolio")){
                    ((MyPortfolioActivity) getActivity()).closeCardContainer();
                }else
                if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                    ((BrokerMap) getActivity()).closeCardContainer();
                }else{

                    ((ClientMainActivity) getActivity()).closeAddListing();
                    //((ClientMainActivity) getActivity()).onBackPressed();

                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.PROPERTY=Property;
                General.setSharedPreferences(getContext(),AppConstants.PROPERTY,Property);
                General.setSharedPreferences(getContext(),AppConstants.APPROX_AREA,approx_area);
                if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("Listing")){
                    ((BrokerListingActivity) getActivity()).closeCardContainer();
                    ((BrokerListingActivity) getActivity()).openAddBuilding();

                }else
                if(!Entry_point.equalsIgnoreCase("")&&Entry_point.equalsIgnoreCase("portfolio")){
                    ((MyPortfolioActivity) getActivity()).closeCardContainer();
                    ((MyPortfolioActivity) getActivity()).openAddBuilding();

                }else
               if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
                   ((BrokerMap) getActivity()).closeCardContainer();
                   ((BrokerMap) getActivity()).openAddBuilding();

                }else{
                    ((ClientMainActivity) getActivity()).closeAddListing();
                   ((ClientMainActivity) getActivity()).openAddBuilding();
                }
            }
        });

        return v;
    }




    private void loadFragmentAnimated(Fragment fragment, Bundle args, int containerId, String title)
    {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }













    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
