package com.nbourses.oyeok.RPOT.PriceDiscovery.UI.ReferFragment;

/**
 * Created by Smitesh on 15-12-2015.
 */

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nbourses.oyeok.R;

import static com.nbourses.oyeok.R.layout.refer_fragment;
import static com.nbourses.oyeok.R.layout.view_expandable;

public class ReferFragment extends Fragment {
    LinearLayout line_lay1;
    LinearLayout line_lay2;
    TextView more;
    TextView details;
    Button refer;
    boolean flag = true;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view= inflater.inflate(R.layout.refer_fragment, container, false);
            line_lay1= (LinearLayout) view.findViewById(R.id.line_lay1);
            line_lay2= (LinearLayout) view.findViewById(R.id.line_lay2);
            more = (TextView) view.findViewById(R.id.more);
            details= (TextView) view.findViewById(R.id.details);

            click_more();
            click_detail();
            visibility();
            // Inflate the layout for this fragment
            return view;
        }

    @Override
    public void onResume() {
        super.onResume();
        visibility();
    }

    @Override
    public void onPause() {
        super.onPause();
        refer.setVisibility(View.VISIBLE);
    }


    public void visibility() {
        refer = (Button) getActivity().findViewById(R.id.refer);
        refer.setVisibility(View.GONE);
    }

    public void click_more(){
        more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!flag) {
                    line_lay1.setVisibility(View.VISIBLE);
                    flag = true;
                }
                else {
                    line_lay1.setVisibility(View.GONE);
                    flag = false;
                }
            }
        });

    }
    public void click_detail(){
        details.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!flag) {
                    line_lay2.setVisibility(View.VISIBLE);
                    flag = true;
                }
                else {
                    line_lay2.setVisibility(View.GONE);
                    flag = false;
                }
            }
        });
    }
}