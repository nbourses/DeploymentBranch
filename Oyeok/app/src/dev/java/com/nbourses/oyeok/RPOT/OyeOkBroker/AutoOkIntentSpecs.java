package com.nbourses.oyeok.RPOT.OyeOkBroker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nbourses.oyeok.R;

/**
 * Created by YASH_SHAH on 14/12/2015.
 */
public class AutoOkIntentSpecs extends Fragment {

    private Button mAutoOk;
    //String off_mode;

    public AutoOkIntentSpecs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auto_ok, container, false);
        mAutoOk = (Button) rootView.findViewById(R.id.bt_auto_ok);
        mAutoOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoOk();

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void autoOk(){


    }

}
