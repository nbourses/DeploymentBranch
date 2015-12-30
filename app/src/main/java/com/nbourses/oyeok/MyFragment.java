package com.nbourses.oyeok;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.channguyen.rsv.RangeSliderView;

public class MyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    RangeSliderView homeRangeSliderView;
    private String mParam2;
   private String dataTobePassed="";

    EditText othersEditText,seatNoEditText;
    static String buttonSelected="";
    Button coldStorage,kitchen,warehouse,officeSpace,manufacturing,workshop;

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    private OnFragmentInteractionListener mListener;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = (View) inflater.inflate(R.layout.fragment_my, container, false);
        final String propertyType = getArguments().getString("propertyType");

        Log.i("Frag",""+propertyType);
        //homeRangeSliderView= (RangeSliderView) layout.findViewById(R.id.rsv_small);
        //othersEditText= (EditText) layout.findViewById(R.id.othersEditText);
        switch (propertyType){
            case "House":
                layout = (View) inflater.inflate(R.layout.home_layout, container, false);
                homeRangeSliderView= (RangeSliderView) layout.findViewById(R.id.rsv_small);
                break;
            case "Shop":
                layout = (View) inflater.inflate(R.layout.shop_layout, container, false);
                homeRangeSliderView= (RangeSliderView) layout.findViewById(R.id.rsv_small);
                break;
            case "Others":
                layout = (View) inflater.inflate(R.layout.others_layout, container, false);
                othersEditText= (EditText) layout.findViewById(R.id.othersEditText);
                break;
            case "Industrial":
                layout = (View) inflater.inflate(R.layout.industrial_layout, container, false);
                //othersEditText= (EditText) layout.findViewById(R.id.othersEditText);
                coldStorage= (Button) layout.findViewById(R.id.btn_coldStorage);
                kitchen= (Button) layout.findViewById(R.id.btn_kitchen);
                warehouse= (Button) layout.findViewById(R.id.btn_wareHouse);
                officeSpace= (Button) layout.findViewById(R.id.btn_officeSpace);
                manufacturing= (Button) layout.findViewById(R.id.btn_manufacturing);
                workshop= (Button) layout.findViewById(R.id.btn_workshop);
                break;
            case "Office":
                layout = (View) inflater.inflate(R.layout.office_layout, container, false);
                seatNoEditText= (EditText) layout.findViewById(R.id.seatNoEditText);
                break;

        }

        if(propertyType.equals("Office")){
            seatNoEditText.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {

                    // you can call or do what you want with your EditText here
                    onButtonPressed("Office "+seatNoEditText.getText().toString());

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
        }

        if(propertyType.equals("Industrial")){
            kitchen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kitchen.setSelected(true);
                    warehouse.setSelected(false);
                    manufacturing.setSelected(false);
                    coldStorage.setSelected(false);
                    workshop.setSelected(false);
                    officeSpace.setSelected(false);

                    onButtonPressed("Industrial Kitchen");
                }
            });
            warehouse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kitchen.setSelected(false);
                    warehouse.setSelected(true);
                    manufacturing.setSelected(false);
                    coldStorage.setSelected(false);
                    workshop.setSelected(false);
                    officeSpace.setSelected(false);
                    onButtonPressed("Industrial Warehouse");
                }
            });
            manufacturing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kitchen.setSelected(false);
                    warehouse.setSelected(false);
                    manufacturing.setSelected(true);
                    coldStorage.setSelected(false);
                    workshop.setSelected(false);
                    officeSpace.setSelected(false);
                    onButtonPressed("Industrial Manufacturing");
                }
            });
            coldStorage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kitchen.setSelected(false);
                    warehouse.setSelected(false);
                    manufacturing.setSelected(false);
                    coldStorage.setSelected(true);
                    workshop.setSelected(false);
                    officeSpace.setSelected(false);
                    onButtonPressed("Industrial ColdStorage");
                }
            });
            workshop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kitchen.setSelected(false);
                    warehouse.setSelected(false);
                    manufacturing.setSelected(false);
                    coldStorage.setSelected(false);
                    workshop.setSelected(true);
                    officeSpace.setSelected(false);
                    onButtonPressed("Industrial Workshop");
                }
            });
            officeSpace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kitchen.setSelected(false);
                    warehouse.setSelected(false);
                    manufacturing.setSelected(false);
                    coldStorage.setSelected(false);
                    workshop.setSelected(false);
                    officeSpace.setSelected(true);
                    onButtonPressed("Industrial OfficeSpace");
                }
            });
        }

        if(propertyType.equals("Others")){
            othersEditText.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {

                    // you can call or do what you want with your EditText here
                    onButtonPressed("Others "+othersEditText.getText().toString());

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
        }


        if(propertyType.equals("House")||propertyType.equals("Shop")){
            final RangeSliderView.OnSlideListener listener = new RangeSliderView.OnSlideListener() {
                @Override
                public void onSlide(int index) {
                    Log.i("home", "" + index);
                    if(propertyType.equals("House"))
                        onButtonPressed("House " + index+1);
                    else
                        onButtonPressed("Shop " + index+1);
                }
            };
            homeRangeSliderView.setOnSlideListener(listener);
        }
        /*if (shouldCreateChild) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            fm.beginTransaction();
            Fragment fragTwo = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putBoolean("shouldYouCreateAChildFragment", false);
            fragTwo.setArguments(arguments);
            ft.add(R.id.frag_container, fragTwo);
            ft.commit();

        }*/

        return layout;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String dataTobePassed) {
        if (mListener != null) {
            mListener.onFragmentInteraction(dataTobePassed);
        }
    }

  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String s);
    }
}
