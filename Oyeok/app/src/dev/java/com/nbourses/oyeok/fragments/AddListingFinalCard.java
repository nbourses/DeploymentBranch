package com.nbourses.oyeok.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.nbourses.oyeok.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.nbourses.oyeok.R.id.toggleBtn;


public class AddListingFinalCard extends Fragment {


    /*private OnFragmentInteractionListener mListener;*/

    public AddListingFinalCard() {
        // Required empty public constructor
    }


    private TextView txtcalendar,transaction_type,approx_area,config,min,max,selected_rate;
    Calendar myCalendar = Calendar.getInstance();
    private View v;
    private SwitchButton toggleBtn1;
    CheckBox Req,Avail;
    SeekBar seekBar;
    private int minvalue=20000,maxvalue=80000;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       v= inflater.inflate( R.layout.fragment_add_listing_final_card, container, false );

        toggleBtn1 = (SwitchButton) v.findViewById(toggleBtn);


        txtcalendar=(TextView)v.findViewById(R.id.txtcalendar1);
        transaction_type=(TextView)v.findViewById(R.id.transaction_type);
        updateLabel();
        txtcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker();
            }
        });
        Req=(CheckBox)v.findViewById(R.id.req);
        Avail=(CheckBox) v.findViewById(R.id.availability);
        approx_area=(TextView)v.findViewById(R.id.approx_area);
        config=(TextView) v.findViewById(R.id.config);
        seekBar=(SeekBar) v.findViewById(R.id.seekBar);
        min=(TextView) v.findViewById(R.id.min);
        max = (TextView) v.findViewById(R.id.max);
        selected_rate=(TextView) v.findViewById(R.id.selected_rate);
        min.setText(String.valueOf(minvalue));
        max.setText(String.valueOf(maxvalue));
        toggleBtn1.toggle();

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Property_Furnishing_Condition, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                /*Log.i("confirmaton", "Furnishing================" + Furnishing);
                Furnishing = parent.getItemAtPosition(position).toString();
                Log.i("confirmaton", "Furnishing+++++++++++++++++++" + Furnishing);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
// Another interface callback

            }
        });


        toggleBtn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    transaction_type.setText("RESALE");
                }
                else{
                    transaction_type.setText("RENT");
                }
            }
        });

        seekBar.setMax(maxvalue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {



            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress <= minvalue){
                    progress = minvalue + progress;
                }

                String numberAsString = String.valueOf(progress);
                selected_rate.setText(numberAsString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return v;
    }







    final  DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.add(Calendar.DATE,1);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            updateLabel();

        }

    };
    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        //In which you need put here
        myCalendar.add(Calendar.DATE,1);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtcalendar.setText(sdf.format(myCalendar.getTime()));
//        PossessionDate=txtcalendar.getText().toString();
    }

    private void displayDatePicker(){
        Calendar now= Calendar.getInstance();
        Calendar now1= now;
        now1.add(Calendar.DATE,1);
        DatePickerDialog dpd = new DatePickerDialog(getContext(), date, now1
                .get(Calendar.YEAR), now1.get(Calendar.MONTH),
                now1.get(Calendar.DAY_OF_MONTH));

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date d = null;
        try {


            d  = sdf.parse(sdf.format(now1.getTime()));
            now.add(Calendar.MONTH,6);
            // now.add(Calendar.DATE,1);

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








   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach( context );
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
