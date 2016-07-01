package com.nbourses.oyeok.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;


public class AppSetting extends Fragment {



    public AppSetting() {

    }

    CheckBox checkBoxWalkthrough;
    CheckBox checkBoxBeacon;
    String walkthrough,beacon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_app_setting, container, false);

         checkBoxBeacon=  (CheckBox) v.findViewById(R.id.check_beacon);

       checkBoxWalkthrough=  (CheckBox) v.findViewById(R.id.check_walkthrough);
        walkthrough= SharedPrefs.getString(getContext(),SharedPrefs.CHECK_WALKTHROUGH);
        Log.i("ischecked","ischecked1"+walkthrough);
        if(walkthrough.equalsIgnoreCase("true")) {
            Log.i("ischecked","ischecked1========="+walkthrough);
            checkBoxWalkthrough.setChecked(true);
        }
        beacon= SharedPrefs.getString(getContext(),SharedPrefs.CHECK_BEACON);
        Log.i("ischecked","ischecked1"+beacon);
        if(beacon.equalsIgnoreCase("true")) {
            Log.i("ischecked","beacon========="+beacon);
            checkBoxBeacon.setChecked(true);
        }
//
        checkBoxWalkthrough.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//        StringBuffer result = new StringBuffer();
//        result.append("WALKTHROUGH ").append(checkBoxWalkthrough.isChecked());
        Log.i("ischecked","wal"+isChecked);
        if(isChecked){
            checkBoxWalkthrough.setChecked(true);
         SharedPrefs.save(getContext(),SharedPrefs.CHECK_WALKTHROUGH,isChecked+"");
//            Intent intent = new Intent(AppConstants.CHECK_WALKTHROUGH);
//            intent.putExtra("checkWalkthrough", "true");
            Log.i("ischecked","wal1"+isChecked);

        }
        else {
            checkBoxWalkthrough.setChecked(false);
            SharedPrefs.save(getContext(), SharedPrefs.CHECK_WALKTHROUGH, isChecked + "");
            Log.i("ischecked","wal"+isChecked);

        }
    }
   });


        checkBoxBeacon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    SharedPrefs.save(getContext(),SharedPrefs.CHECK_BEACON,isChecked+"");
//                    Intent intent = new Intent(AppConstants.CHECK_BEACON);
//                    intent.putExtra("checkBeacon", isChecked);

                }else {
                    checkBoxBeacon.setChecked(false);
                    SharedPrefs.save(getContext(), SharedPrefs.CHECK_BEACON, isChecked + "");
                    Log.i("ischecked","wal"+isChecked);

                }
            }
        });







return v;
    }



























    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/



//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//        Intent intent=new Intent(getContext(), ClientMainActivity.class);
//        startActivity(intent);
////        Intent back = new Intent(getContext(), BrokerMainActivity.class);
////        startActivity(back);
//    }
}
