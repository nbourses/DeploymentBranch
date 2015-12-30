package com.nbourses.oyeok.RPOT.OkBroker.SupportClass;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.OkBroker.UI.Ok_Broker_MainScreen;

/**
 * Created by TP on 01/08/15.
 */
public class YoPopup {
    int timer_int;
    Context myContext;
    String timer_value;
    Boolean min_press = false;
    Button min10,min15,SendYo;
    AlertDialog alertD;
    Ok_Broker_MainScreen mainBrokerActivity;


    public void inflateYo(final Context context, final String spec, String U_tp) {
        mainBrokerActivity = new Ok_Broker_MainScreen();

        this.myContext = context;


                LayoutInflater layoutInflater = LayoutInflater.from(this.myContext);
                View promptView = layoutInflater.inflate(R.layout.yo_timer, null);

                min10 = (Button) promptView.findViewById(R.id.mins10);
                min15 = (Button) promptView.findViewById(R.id.mins15);

                alertD = new AlertDialog.Builder(myContext).create();
                alertD.setCanceledOnTouchOutside(true);

                SendYo = (Button) promptView.findViewById(R.id.sendYo);
                TextView spec1 = (TextView) promptView.findViewById(R.id.tvspec);
                String spec_fin;
                if (U_tp.equalsIgnoreCase("broker"))
                    spec_fin = "Plus-"+spec;
                else
                spec_fin = "Direct-"+spec;
                spec1.setText(spec_fin);


                final TextView timerval = (TextView) promptView.findViewById(R.id.tvtimerValue);

                min10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        min_press = true;
                        SendYo.setVisibility(View.VISIBLE);
                        timer_value = 10 + "";
                        timer_int = 10;
                        timerval.setText(timer_value + " mins");
                        min10.setTextColor(Color.WHITE);
                        min10.setBackgroundColor(Color.parseColor("#FFA500"));

                        min15.setTextColor(Color.BLACK);
                        min15.setBackgroundResource(R.drawable.button_border);

                    }
                });

                min15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        min_press=true;
                        SendYo.setVisibility(View.VISIBLE);

                        timer_value = 15 + "";
                        timer_int=15;
                        timerval.setText(timer_value + " mins");
                        min15.setTextColor(Color.WHITE);
                        min15.setBackgroundColor(Color.parseColor("#FFA500"));

                        min10.setTextColor(Color.BLACK);
                        min10.setBackgroundResource(R.drawable.button_border);

                    }
                });


                SendYo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                       // mainBrokerActivity.sendYo();
                        alertD.dismiss();

                    }
                });
                alertD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertD.setView(promptView);
                alertD.show();

            }


  }


