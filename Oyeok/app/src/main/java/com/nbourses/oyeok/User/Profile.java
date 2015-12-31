package com.nbourses.oyeok.User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.R;


public class Profile extends Fragment {






    private TextView username_txt,phone_txt,email_txt,role_txt;
    DBHelper dbhelper;



    public Profile() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout= inflater.inflate(R.layout.fragment_profile, container, false);
        dbhelper=new DBHelper(getActivity());
        username_txt=(TextView)layout.findViewById(R.id.txt_user);
        if(!dbhelper.getValue(DatabaseConstants.name).equals("null"))
            username_txt.setText(dbhelper.getValue(DatabaseConstants.name));

        phone_txt=(TextView)layout.findViewById(R.id.txt_phone);
        if(!dbhelper.getValue(DatabaseConstants.mobileNumber).equals("null"))
            phone_txt.setText(dbhelper.getValue(DatabaseConstants.mobileNumber));

        email_txt=(TextView)layout.findViewById(R.id.txt_email);
        if(!dbhelper.getValue(DatabaseConstants.email).equals("null"))
            email_txt.setText(dbhelper.getValue(DatabaseConstants.email));

        role_txt=(TextView)layout.findViewById(R.id.txt_role);
        if(!dbhelper.getValue(DatabaseConstants.user).equals("null"))
            role_txt.setText(dbhelper.getValue(DatabaseConstants.user));

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event





}
