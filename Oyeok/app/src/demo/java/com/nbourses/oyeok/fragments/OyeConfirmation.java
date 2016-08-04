package com.nbourses.oyeok.fragments;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.helpers.General;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class OyeConfirmation extends Fragment {


   private ImageView calendar,proceed_to_oye;
    private TextView display_date;
    LinearLayout available_sizes;
LinearLayout confirm_layout_with_edit_button;
Button editDetails;



    private OnFragmentInteractionListener mListener;

    public OyeConfirmation() {
        // Required empty public constructor
    }

    /*
    // TODO: Rename and change types and number of parameters
    public static OyeConfirmation newInstance(String param1, String param2) {

        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }












    Calendar myCalendar = Calendar.getInstance();

















    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_oye_confirmation, container, false);
        calendar=(ImageView) view.findViewById(R.id.calendar);
        display_date=(TextView) view.findViewById(R.id.display_date);
        available_sizes=(LinearLayout) view.findViewById(R.id.available_sizes);
        confirm_layout_with_edit_button=(LinearLayout) view.findViewById(R.id.confirm_layout_with_edit_button);
        proceed_to_oye=(ImageView) view.findViewById(R.id.proceed_to_oye);
        editDetails=(Button) view.findViewById(R.id.editDetails);


        init();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    final  DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                updateLabel();

        }

    };

    private void init(){





        calendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                displayDatePicker();
                calendar.setVisibility(View.GONE);
                display_date.setVisibility(View.VISIBLE);

            }
        });
        display_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker();
                calendar.setVisibility(View.GONE);
                display_date.setVisibility(View.VISIBLE);
            }
        });


        proceed_to_oye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                General.publishOye(getContext());
            }
        });

        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClientMainActivity)getActivity()).closeOyeConfirmation();
                ((ClientMainActivity)getActivity()).openOyeSreen();


            }
        });

    }


    private void updateLabel(){
        String myFormat = "dd/MMM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        display_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void displayDatePicker(){

        DatePickerDialog dpd = new DatePickerDialog(getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date d = null;
        try {
             Calendar now= Calendar.getInstance();
            d  = sdf.parse(sdf.format(now.getTime()));
            now.add(Calendar.MONTH,6);

            Date dd = sdf.parse(  now.get(Calendar.DATE)
                    + "/"+ (now.get(Calendar.MONTH) + 1)
                    + "/"

                    + now.get(Calendar.YEAR));

//                    Date dd = sdf.parse("26/1/2017");
            dpd.getDatePicker().setMinDate(d.getTime());
            dpd.getDatePicker().setMaxDate(dd.getTime());
            dpd.show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }








//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
        void onFragmentInteraction(Uri uri);
    }
}
