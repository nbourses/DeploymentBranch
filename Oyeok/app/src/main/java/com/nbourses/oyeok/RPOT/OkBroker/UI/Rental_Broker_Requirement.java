package com.nbourses.oyeok.RPOT.OkBroker.UI;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.AcceptOkCall;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OnAcceptOkSuccess;
import com.nbourses.oyeok.RPOT.OkBroker.CircularSeekBar.CircularSeekBarNew;
import com.nbourses.oyeok.RPOT.OyeOkBroker.AutoOkIntentSpecs;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class Rental_Broker_Requirement extends Fragment implements CircularSeekBarNew.imageAction,OnAcceptOkSuccess {

    CircularSeekBarNew cbn;
    TextView mTitle;
    LinearLayout mNotClicked;
    TextView rentText;
    TextView displayOkText;
    Button mOkbutton;
    private Button pickContact;
    private TextView contactName;
    private JSONArray values = new JSONArray();
    DBHelper dbHelper;
    JSONArray dummyData = new JSONArray();
    String oyeId,specCode,oyeUserId,reqAvl;
    JSONArray p= new JSONArray();
    int j;
    Ok_Broker_MainScreen ok_broker_mainScreen;
    Button droom;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_rental_ok__broker, container, false);

        mTitle = (TextView) v.findViewById(R.id.title);
        mNotClicked = (LinearLayout) v.findViewById(R.id.notClicked);
        rentText = (TextView) v.findViewById(R.id.rentText);
        displayOkText = (TextView) v.findViewById(R.id.displayOkText);
        mOkbutton = (Button) v.findViewById(R.id.okButton);

        ok_broker_mainScreen=(Ok_Broker_MainScreen)getParentFragment();
        pickContact = (Button) v.findViewById(R.id.pickContact);
        contactName = (TextView) v.findViewById(R.id.contactText);
        rentText.setText("Rent : 50k Rs/month");
        dbHelper = new DBHelper(getContext());

        droom= (Button) v.findViewById(R.id.droom);

        mOkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mOkbutton.getText().toString().equals("Auto Ok"))
                {
                    ((MainActivity)getActivity()).changeFragment(new AutoOkIntentSpecs(), null,"");
                }
                else{
                    if (!dbHelper.getValue(DatabaseConstants.user).equals("Broker"))
                    {
                        ok_broker_mainScreen=(Ok_Broker_MainScreen)getParentFragment();
                        ok_broker_mainScreen.replaceWithSignUp(p,j);
                    }
                    else
                    {

                        AcceptOkCall a = new AcceptOkCall();
                        a.setmCallBack(Rental_Broker_Requirement.this);
                        a.acceptOk(p,j,dbHelper, getActivity());
                    }
                }
            }
        });
        droom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ok_broker_mainScreen.openDroomList();
            }
        });

        pickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

                getActivity().startActivityForResult(intent, 302);

            }
        });


        cbn = (CircularSeekBarNew) v.findViewById(R.id.circularseekbar);
        cbn.setmImageAction(this);
        for(int i=0;i<2;i++) {
            JSONObject element= new JSONObject();
            try {
                element.put("oye_id", "vhdhCMSDMz");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                element.put("req_avl", "req");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                element.put("size", i+1+"bhk");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                    element.put("price", (1+i)*(100000));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if(i%2==0)
                    element.put("oye_status", "active");
                else
                    element.put("oye_status","inactive");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                element.put("user_id", "vhdhCMSDMz");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {if(i%2==0)
                element.put("user_role", "client");
                else
                    element.put("user_role","broker");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                dummyData.put(i, element);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            cbn.setValues(dbHelper.getValue(DatabaseConstants.reqLl));
        else
            cbn.setValues(dummyData.toString());


        return v;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
               // mOkbutton.setBackgroundColor(Color.parseColor("#B2DFDB"));
                mOkbutton.setText("Ok(4290)");
                Log.i("start droom", "name=" + name);

            }
        }
        //break;
    }


    @Override
    public void onclick(int position, JSONArray m,String show) {
        // Toast.makeText(getActivity(),"The value is"+m.get(position),Toast.LENGTH_LONG).show();
//        YoPopup yoPopup = new YoPopup();
//        yoPopup.inflateYo(getActivity(), "LL-3BHK-20K", "broker");
        try {
            p=m;
            j=position;
            rentText.setText("Price : Rs "+ m.getJSONObject(position).getString("price"));
            DecimalFormat formatter = new DecimalFormat();
            //rentText.setText("Price : Rs "+ formatter.format(Double.parseDouble(m.getJSONObject(position).getString("price")))+"\n"+m.getJSONObject(position).getString("property_type")+"\n"+m.getJSONObject(position).getString("property_subtype"));
            /*oyeId=m.getJSONObject(position).getString("oye_id");
            oyeUserId= m.getJSONObject(position).getString("user_id");
            specCode=m.getJSONObject(position).getString("tt")+"-"+m.getJSONObject(position).getString("size")+"-"+m.getJSONObject(position).getString("price");
            reqAvl=m.getJSONObject(position).getString("req_avl");*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(show.equals("show"))
        {
            mNotClicked.setVisibility(View.GONE);
            mTitle.setVisibility(View.VISIBLE);
            //mOkbutton.setBackgroundColor(Color.parseColor("#B2DFDB"));
            mOkbutton.setText("Ok(4290)");
            rentText.setVisibility(View.VISIBLE);
            displayOkText.setVisibility(View.VISIBLE);
            pickContact.setVisibility(View.GONE);
            contactName.setVisibility(View.GONE);

        }else if(show.equals("hide"))
        {
            mNotClicked.setVisibility(View.VISIBLE);
            mTitle.setVisibility(View.GONE);
            //mOkbutton.setBackgroundColor(Color.parseColor("#E0E0E0"));
            rentText.setVisibility(View.GONE);
            displayOkText.setVisibility(View.GONE);
            pickContact.setVisibility(View.GONE);
            contactName.setVisibility(View.GONE);
            mOkbutton.setText("Auto Ok");

        }else
        {
            mNotClicked.setVisibility(View.GONE);
            mTitle.setVisibility(View.VISIBLE);
            //mOkbutton.setBackgroundColor(Color.parseColor("#E0E0E0"));
            rentText.setVisibility(View.VISIBLE);
            displayOkText.setVisibility(View.VISIBLE);
            mOkbutton.setText("Auto Ok");
            pickContact.setVisibility(View.VISIBLE);
            contactName.setVisibility(View.VISIBLE);

        }

    }


    @Override
    public void replaceFragment(Bundle args) {
        ok_broker_mainScreen.openChat(args);
    }
}
