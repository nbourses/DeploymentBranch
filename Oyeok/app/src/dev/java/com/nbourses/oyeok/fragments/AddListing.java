package com.nbourses.oyeok.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;


public class AddListing extends Fragment {

    private OnFragmentInteractionListener mListener;
    View v;
    private Button rk1,bhk1,bhk1_5,bhk2,bhk2_5,bhk3,bhk3_5,bhk4,bhk4_5,bhk5,bhk5_5,bhk6;
    private RadioGroup radioGroup1;
    TextView selected_config,home,office,shop,industry,next,Cancel_add_building,b_type,area;
    private String Property;
    HorizontalScrollView hsl;

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

        rk1=(Button) v.findViewById( R.id.rk1 );
        bhk1=(Button) v.findViewById( R.id.bhk1 );
        bhk1_5=(Button) v.findViewById( R.id.bhk1_5 );
        bhk2=(Button) v.findViewById( R.id.bhk2 );
        bhk2_5=(Button) v.findViewById( R.id.bhk2_5 );
        bhk3=(Button) v.findViewById( R.id.bhk3 );
        bhk3_5=(Button) v.findViewById( R.id.bhk3_5 );
        bhk4=(Button) v.findViewById( R.id.bhk4 );
        bhk4_5=(Button) v.findViewById( R.id.bhk4_5 );
        bhk5=(Button) v.findViewById( R.id.bhk5 );
        bhk5_5=(Button) v.findViewById( R.id.bhk5_5 );
        bhk6=(Button) v.findViewById( R.id.bhk6 );


        radioGroup1 = (RadioGroup) v.findViewById(R.id.radioGroup1);

        selected_config =(TextView) v.findViewById( R.id.selected_config );

        home=(TextView) v.findViewById( R.id.btn_home ) ;
        office=(TextView) v.findViewById( R.id.btn_office ) ;
        shop=(TextView) v.findViewById( R.id.btn_shop ) ;
        industry=(TextView) v.findViewById( R.id.btn_industrial ) ;
        hsl=(HorizontalScrollView) v.findViewById( R.id.horizontalScrollView );
        home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));

        area=(TextView) v.findViewById(R.id.area);


        home.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Property="home" ;
                b_type.setText("Home");
                industry.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                shop.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                office.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));

            }
        } );
        office.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Property="office";
                b_type.setText("Office");

                industry.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                shop.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                office.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));
                home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
            }
        } );
        shop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Property="shop";
                b_type.setText("Shop");

                industry.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                shop.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));
                office.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
            }
        } );

        industry.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Property="industries";
                b_type.setText("INDUSTRIES");
                industry.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_btn_bg_with_check));
                shop.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                office.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));
                home.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_button_bg_with_border));

            }
        } );


        next=(TextView) v.findViewById(R.id.next);
        Cancel_add_building=(TextView) v.findViewById(R.id.Cancel_add_building);
        b_type=(TextView)v.findViewById(R.id.b_type);
        Display display = ((Activity )getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;

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
                        General.setSharedPreferences(getContext(), AppConstants.PROPERTY_CONFIG,"1 RK");
                        area.setText("300 sq.ft.");
                        break;
                    case R.id.bhk1:
                        selected_config.setText( "1 BHK" );
                        hsl.scrollTo(bhk1.getLeft() - center/2, bhk1.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"1 BHK");
                        area.setText("600 sq.ft.");

                        break;
                    case R.id.bhk1_5:
                        selected_config.setText( "1.5 BHK" );
                        hsl.scrollTo(bhk1_5.getLeft() - center/2, bhk1_5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"1.5 BHK");
                        area.setText("800 sq.ft.");

                        break;
                    case R.id.bhk2:
                        selected_config.setText( "2 BHK" );
                        hsl.scrollTo(bhk2.getLeft() - center/2, bhk2.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"2 BHK");
                        area.setText("950 sq.ft.");

                        break;
                    case R.id.bhk2_5:
                        selected_config.setText( "2.5 BHK" );

                        hsl.scrollTo(bhk2_5.getLeft() - center/2, bhk2_5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"2.5 BHK");
                        area.setText("1300 sq.ft.");

                        break;
                    case R.id.bhk3:
                        selected_config.setText( "3 BHK" );
                        hsl.scrollTo(bhk3.getLeft() - center/2, bhk3.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"3 BHK");
                        area.setText("1600 sq.ft.");

                        break;
                    case R.id.bhk3_5:
                        selected_config.setText( "3.5 BHK" );
                        hsl.scrollTo(bhk3_5.getLeft() - center/2, bhk3_5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"3.5 BHK");
                        area.setText("1800 sq.ft.");

                        break;
                    case R.id.bhk4:
                        selected_config.setText( "4 BHK" );
                        hsl.scrollTo(bhk4.getLeft() - center/2, bhk4.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"4 BHK");
                        area.setText("2100 sq.ft.");

                        break;
                    case R.id.bhk4_5:
                        selected_config.setText( "4.5 BHK" );
                        hsl.scrollTo(bhk4_5.getLeft() - center/2, bhk4_5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"4.5 BHK");
                        area.setText("2300 sq.ft.");

                        break;
                    case R.id.bhk5:
                        selected_config.setText( "5 BHK" );
                        hsl.scrollTo(bhk5.getLeft() - center/2, bhk5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"5 BHK");
                        area.setText("2500 sq.ft.");

                        break;
                    case R.id.bhk5_5:
                        selected_config.setText( "5.5 BHK" );
                        hsl.scrollTo(bhk5_5.getLeft() - center/2, bhk5_5.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"5.5 BHK");
                        area.setText("2700 sq.ft.");

                        break;
                    case R.id.bhk6:
                        selected_config.setText( "6 BHK" );
                        hsl.scrollTo(bhk6.getLeft() - center/2, bhk6.getTop());
                        General.setSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG,"6 BHK");
                        area.setText("2900 sq.ft.");

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
                ((ClientMainActivity)getActivity()).closeAddListing();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.PROPERTY=Property;
                ((ClientMainActivity)getActivity()).closeAddListing();
                ((ClientMainActivity)getActivity()).openAddBuilding();
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
