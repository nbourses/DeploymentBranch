package com.nbourses.oyeok.RPOT.OkBroker.UI;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.OkBroker.LineSeekBar.LineSeekBar;
import com.nbourses.oyeok.RPOT.OyeOkBroker.AutoOkIntentSpecs;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;

import java.util.ArrayList;


public class Sale_Broker_Available extends Fragment implements LineSeekBar.imageAction {

    LineSeekBar cbn;
    //TextView mTitle;
    LinearLayout mNotClicked;
    TextView rentText;
    TextView displayOkText;
    Button mOkbutton;
    private Button pickContact;
    private TextView contactName;
    private ArrayList<Integer> values = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_sale_ok__broker, container, false);

        //mTitle = (TextView) v.findViewById(R.id.title);
        mNotClicked = (LinearLayout) v.findViewById(R.id.notClicked);
        rentText = (TextView) v.findViewById(R.id.rentText);
        displayOkText = (TextView) v.findViewById(R.id.displayOkText);
        mOkbutton = (Button) v.findViewById(R.id.okButton);


        pickContact = (Button) v.findViewById(R.id.pickContact);
        contactName = (TextView) v.findViewById(R.id.contactText);


        cbn = (LineSeekBar) v.findViewById(R.id.circularseekbar);
        cbn.setmImageAction(this);
        values.add(2);
        values.add(17);
        values.add(19);
        values.add(8);

        cbn.setValues(values);


        mOkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOkbutton.getText().toString().equals("Auto Ok")) {
                    ((MainActivity) getActivity()).changeFragment(new AutoOkIntentSpecs(), null,"");
                }
            }
        });


        pickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

                getActivity().startActivityForResult(intent, 302);

            }
        });


        return v;
    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        /*switch (reqCode) {
            case (PICK_CONTACT):*/
        if (resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            Cursor c = getActivity().getContentResolver().query(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                // TODO Fetch other Contact details as you want to use
                contactName.setText(name);
                mOkbutton.setBackgroundColor(Color.parseColor("#B2DFDB"));
                Log.i("start droom", "name=" + name);

            }
        }
        //break;
    }


    @Override
    public void onclick(int position, ArrayList<Integer> m,String show) {
       // Toast.makeText(getActivity(),"The value is"+m.get(position),Toast.LENGTH_LONG).show();
//        YoPopup yoPopup = new YoPopup();
//        yoPopup.inflateYo(getActivity(), "LL-3BHK-20K", "broker");
        if(show.equals("show"))
        {
            mNotClicked.setVisibility(View.GONE);
            //mTitle.setVisibility(View.VISIBLE);
            //mOkbutton.setBackgroundColor(Color.parseColor("#B2DFDB"));
            mOkbutton.setText("Ok(4290)");
            rentText.setVisibility(View.VISIBLE);
            displayOkText.setVisibility(View.VISIBLE);
            pickContact.setVisibility(View.GONE);
            contactName.setVisibility(View.GONE);

        }else if(show.equals("hide"))
        {
            mNotClicked.setVisibility(View.VISIBLE);
            //mTitle.setVisibility(View.GONE);
            //mOkbutton.setBackgroundColor(Color.parseColor("#E0E0E0"));
            mOkbutton.setText("Auto Ok");
            rentText.setVisibility(View.GONE);
            displayOkText.setVisibility(View.GONE);
            pickContact.setVisibility(View.GONE);
            contactName.setVisibility(View.GONE);

        }else
        {
            mNotClicked.setVisibility(View.GONE);
            //mTitle.setVisibility(View.VISIBLE);
            //mOkbutton.setBackgroundColor(Color.parseColor("#E0E0E0"));
            rentText.setVisibility(View.VISIBLE);
            displayOkText.setVisibility(View.VISIBLE);
            mOkbutton.setText("Auto Ok");
            pickContact.setVisibility(View.VISIBLE);
            contactName.setVisibility(View.VISIBLE);

        }

    }


}
