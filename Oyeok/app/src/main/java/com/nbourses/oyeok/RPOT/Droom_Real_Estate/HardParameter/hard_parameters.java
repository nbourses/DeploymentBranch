package com.nbourses.oyeok.RPOT.Droom_Real_Estate.HardParameter;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nbourses.oyeok.R;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class hard_parameters extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener, TimePickerDialog.OnTimeSetListener {

     private Button number;
    private Button date;
    private Button time;

    private String key="";
    private String valueString="";

    private onParameterSelect monParameterSelect;

    public onParameterSelect getMonParameterSelect() {
        return monParameterSelect;
    }

    public void setMonParameterSelect(onParameterSelect monParameterSelect) {
        this.monParameterSelect = monParameterSelect;
    }

    public interface onParameterSelect
    {
        public void onParameterSelected(String key,String value);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.testlayout_delete_later, container, false);

        setEmojiconFragment(false);



//        number = (Button) v.findViewById(R.id.number);
//        date   = (Button) v.findViewById(R.id.date);
//        time   = (Button) v.findViewById(R.id.time);
//
//        number.setOnClickListener(this);
//        date.setOnClickListener(this);
//        time.setOnClickListener(this);
        return v;
    }


    private void setEmojiconFragment(boolean useSystemDefault) {
//        getChildFragmentManager()
//                .beginTransaction()
//                .replace(R.id.emojicons,EmojiconsFragment.newInstance(useSystemDefault))
//                .commit();
    }


    @Override
    public void onClick(View v) {



        if(v.getTag().equals("date"))
        {
            key = "Some Date";
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.setOnCancelListener(this);
            dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

        }else if (v.getTag().equals("time"))
        {
            key = "Some Time";
            Calendar now = Calendar.getInstance();
            TimePickerDialog dpd = TimePickerDialog.newInstance(
                    this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
            );
            dpd.setOnCancelListener(this);
            dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

        }else if(v.getTag().equals("number"))
        {
            key = "Some Number";
            final Dialog dialog = new Dialog(getActivity());
            final String[] valueText = {""};
            dialog.setContentView(R.layout.numeric_keyboard_layout);
            dialog.setTitle("Set numeric Value");
            final TextView value = (TextView) dialog.findViewById(R.id.value);
            TextView zero  = (TextView) dialog.findViewById(R.id.n0);
            TextView one  = (TextView) dialog.findViewById(R.id.n1);
            TextView two  = (TextView) dialog.findViewById(R.id.n2);
            TextView three  = (TextView) dialog.findViewById(R.id.n3);
            TextView four  = (TextView) dialog.findViewById(R.id.n4);
            TextView five  = (TextView) dialog.findViewById(R.id.n5);
            TextView six  = (TextView) dialog.findViewById(R.id.n6);
            TextView seven  = (TextView) dialog.findViewById(R.id.n7);
            TextView eight  = (TextView) dialog.findViewById(R.id.n8);
            TextView nine  = (TextView) dialog.findViewById(R.id.n9);
            Button cancel = (Button) dialog.findViewById(R.id.cancel);
            TextView clear  = (TextView) dialog.findViewById(R.id.clear);
            TextView submit  = (TextView) dialog.findViewById(R.id.submit);



            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(v.getId() == R.id.clear)
                    {
                        if(!valueText[0].equals(""))
                        {
                            if(valueText[0].length()>1) {
                                valueText[0] = valueText[0].substring(0, valueText[0].length() - 1);
                            }else
                            {
                                valueText[0] = "";
                            }
                            value.setText(valueText[0]);
                        }


                    }else if(v.getId() == R.id.submit)
                    {
                        if(!valueText[0].equals("")) {
                            valueString = valueText[0];
                            monParameterSelect.onParameterSelected(key,valueString);
                            dialog.dismiss();
                        }


                    }else if(v.getId()==R.id.cancel)
                    {
                        dialog.dismiss();
                        key = "";
                        valueString = "";
                    }
                    else
                    {
                        TextView t = (TextView) v;
                        String s = t.getText().toString();
                        valueText[0] = valueText[0]+s;
                        value.setText(valueText[0]);
                    }

                }
            };

            zero.setOnClickListener(onClickListener);
            one.setOnClickListener(onClickListener);
            two.setOnClickListener(onClickListener);
            three.setOnClickListener(onClickListener);
            four.setOnClickListener(onClickListener);
            five.setOnClickListener(onClickListener);
            six.setOnClickListener(onClickListener);
            seven.setOnClickListener(onClickListener);
            eight.setOnClickListener(onClickListener);
            nine.setOnClickListener(onClickListener);
            clear.setOnClickListener(onClickListener);
            submit.setOnClickListener(onClickListener);
            cancel.setOnClickListener(onClickListener);

            dialog.setCanceledOnTouchOutside(false);
            dialog.show();


        }



    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;
        valueString = String.valueOf(year)+"/"+month+"/"+dayOfMonth;
        monParameterSelect.onParameterSelected(key,valueString);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        key = "";
        valueString = "";
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
     valueString = hourOfDay+":"+minute+":"+second;
        monParameterSelect.onParameterSelected(key,valueString);
    }
}
