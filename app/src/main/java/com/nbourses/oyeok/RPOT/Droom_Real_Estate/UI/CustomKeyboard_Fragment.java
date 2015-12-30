package com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI;


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
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.CustomKeyBoard.CustomKeyBoard;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomKeyboard_Fragment extends Fragment implements CustomKeyBoard.onItemClicked,DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener, TimePickerDialog.OnTimeSetListener {

    private String valueString = "";

    public void setmItems(JSONArray mItems) {
        this.mItems = mItems;
    }

    private JSONArray mItems = new JSONArray();

    private CustomKeyBoard mCustom;

    private String key;

    private String value;

    public void setMonValueSelected(onValueSelected monValueSelected) {
        this.monValueSelected = monValueSelected;
    }

    public interface onValueSelected{
        public void onValueSelected(String obj);
        public void setValueFromCommand(String status);
    }

    private onValueSelected monValueSelected;

    public CustomKeyboard_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_custom_keyboard_, container, false);

        mCustom = (CustomKeyBoard) v.findViewById(R.id.custom);
        mCustom.setOnItemClicked(this);

//        for(int i=0;i<20;i++)
//        {
//            if(i % 3 ==0)
//            {
//                JSONObject obj = new JSONObject();
//                try {
//                    obj.put("text","Rent");
//                    obj.put("isCommand","no");
//                    obj.put("getValue",null);
//                    obj.put("valuetype","number");
//                    obj.put("isimage","no");
//                    obj.put("url",null);
//                    obj.put("drawableid",-1);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                mItems.put(obj);
//
//            }else if (i % 3 ==1)
//            {
//                JSONObject obj = new JSONObject();
//                try {
//                    obj.put("text","Date");
//                    obj.put("isCommand","no");
//                    obj.put("getValue",null);
//                    obj.put("valuetype","date");
//                    obj.put("isimage","no");
//                    obj.put("url",null);
//                    obj.put("drawableid",-1);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                mItems.put(obj);
//
//            }else if (i % 3 == 2)
//            {
//                JSONObject obj = new JSONObject();
//                try {
//                    obj.put("text","Time");
//                    obj.put("isCommand","no");
//                    obj.put("getValue",null);
//                    obj.put("valuetype","time");
//                    obj.put("isimage","no");
//                    obj.put("url",null);
//                    obj.put("drawableid",-1);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                mItems.put(obj);
//
//            }
//
//        }


           setCommands();

        return v;
    }


    public void setCommands()
    {
        mItems = new JSONArray();

        JSONObject obj = new JSONObject();
        try {
            obj.put("text","Accepted");
            obj.put("isCommand","yes");
            obj.put("getValue",null);
            obj.put("valuetype","null");
            obj.put("isimage","no");
            obj.put("url",null);
            obj.put("drawableid",-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mItems.put(obj);

        JSONObject obj1 = new JSONObject();
        try {
            obj1.put("text","Rejected");
            obj1.put("isCommand","yes");
            obj1.put("getValue",null);
            obj1.put("valuetype","null");
            obj1.put("isimage","no");
            obj1.put("url",null);
            obj1.put("drawableid",-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mItems.put(obj1);

        JSONObject obj2 = new JSONObject();
        try {
            obj2.put("text","Get All Params");
            obj2.put("isCommand","yes");
            obj2.put("getValue",null);
            obj2.put("valuetype","null");
            obj2.put("isimage","no");
            obj2.put("url",null);
            obj2.put("drawableid",-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mItems.put(obj2);
        mCustom.setChildren(mItems);


    }


    @Override
    public void itemclicked(int position) {
        JSONObject obj = null;
        try {
             obj = mItems.getJSONObject(position);
            if(obj.getString("isCommand").equals("yes"))
            {
                handleCommands(obj);
            }else
            {
                handleValues(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void handleCommands(JSONObject obj)
    {
        try {
            if(obj.getString("text").equals("Get All Params"))
            {
                setAllParams();
            }
            else if(obj.getString("text").equals("Rejected"))
            {
                monValueSelected.setValueFromCommand("rejected");
            }
            else if(obj.getString("text").equals("Accepted"))
            {
                monValueSelected.setValueFromCommand("accepted");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setAllParams()
    {

        mItems = new JSONArray();

        JSONObject obj = new JSONObject();
        try {
            obj.put("text","Rent");
            obj.put("isCommand","no");
            obj.put("getValue",null);
            obj.put("valuetype","number");
            obj.put("isimage","no");
            obj.put("url",null);
            obj.put("drawableid",-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mItems.put(obj);

        JSONObject obj1 = new JSONObject();
        try {
            obj1.put("text","Start Date");
            obj1.put("isCommand","no");
            obj1.put("getValue",null);
            obj1.put("valuetype","date");
            obj1.put("isimage","no");
            obj1.put("url",null);
            obj1.put("drawableid",-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mItems.put(obj1);

        JSONObject obj2 = new JSONObject();
        try {
            obj2.put("text","End Date");
            obj2.put("isCommand","no");
            obj2.put("getValue",null);
            obj2.put("valuetype","date");
            obj2.put("isimage","no");
            obj2.put("url",null);
            obj2.put("drawableid",-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mItems.put(obj2);

        JSONObject obj3 = new JSONObject();
        try {
            obj3.put("text","Deposit");
            obj3.put("isCommand","no");
            obj3.put("getValue",null);
            obj3.put("valuetype","number");
            obj3.put("isimage","no");
            obj3.put("url",null);
            obj3.put("drawableid",-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mItems.put(obj3);

        mCustom.setChildren(mItems);



    }

    public void handleValues(JSONObject obj)
    {
        try {

            if (obj.getString("valuetype").equals("date")) {
                key = obj.getString("text");
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setOnCancelListener(this);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

            } else if (obj.getString("valuetype").equals("time")) {
                key = obj.getString("text");
                Calendar now = Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                dpd.setOnCancelListener(this);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

            } else if (obj.getString("valuetype").equals("number"))
            {
                key = obj.getString("text");;
                final Dialog dialog = new Dialog(getActivity());
                final String[] valueText = {""};
                dialog.setContentView(R.layout.numeric_keyboard_layout);
                dialog.setTitle("Set numeric Value");
                final TextView value = (TextView) dialog.findViewById(R.id.value);
                TextView zero = (TextView) dialog.findViewById(R.id.n0);
                TextView one = (TextView) dialog.findViewById(R.id.n1);
                TextView two = (TextView) dialog.findViewById(R.id.n2);
                TextView three = (TextView) dialog.findViewById(R.id.n3);
                TextView four = (TextView) dialog.findViewById(R.id.n4);
                TextView five = (TextView) dialog.findViewById(R.id.n5);
                TextView six = (TextView) dialog.findViewById(R.id.n6);
                TextView seven = (TextView) dialog.findViewById(R.id.n7);
                TextView eight = (TextView) dialog.findViewById(R.id.n8);
                TextView nine = (TextView) dialog.findViewById(R.id.n9);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                TextView clear = (TextView) dialog.findViewById(R.id.clear);
                TextView submit = (TextView) dialog.findViewById(R.id.submit);


                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (v.getId() == R.id.clear) {
                            if (!valueText[0].equals("")) {
                                if (valueText[0].length() > 1) {
                                    valueText[0] = valueText[0].substring(0, valueText[0].length() - 1);
                                } else {
                                    valueText[0] = "";
                                }
                                value.setText(valueText[0]);
                            }


                        } else if (v.getId() == R.id.submit) {
                            if (!valueText[0].equals("")) {
                                valueString = valueText[0];
                                //monParameterSelect.onParameterSelected(key, valueString);
                                if(monValueSelected != null)
                                {
                                    monValueSelected.onValueSelected(key + " : "+valueString);

                                }
                                dialog.dismiss();
                            }


                        } else if (v.getId() == R.id.cancel) {
                            dialog.dismiss();
                            key = "";
                            valueString = "";
                        } else {
                            TextView t = (TextView) v;
                            String s = t.getText().toString();
                            valueText[0] = valueText[0] + s;
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
        }catch (JSONException e)
        {

        }



    }

    @Override
    public void onCancel(DialogInterface dialog) {

        key = "";
        valueString = "";

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        int month = monthOfYear + 1;
        valueString = String.valueOf(year)+"/"+month+"/"+dayOfMonth;
        //monParameterSelect.onParameterSelected(key,valueString);
        if(monValueSelected != null)
        {
            monValueSelected.onValueSelected(key + " : "+valueString);
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        valueString = hourOfDay+":"+minute+":"+second;
       // monParameterSelect.onParameterSelected(key,valueString);
        if(monValueSelected != null)
        {
            monValueSelected.onValueSelected(key + " : "+valueString);
        }
    }
}
