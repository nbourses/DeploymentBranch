package com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.nbourses.oyeok.R;

import java.util.ArrayList;


public class StartDroomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ImageView clientIcon;
    EditText buildingName;
    TextView numberOfProperties;
    Button addEditText;
    LinearLayout mLayout;
    ArrayList<String> values;
    ArrayAdapter<String> adapter;
    TextView client1Name;
    Button removeEditText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_start_droom, container, false);
        clientIcon = (ImageView) rootView.findViewById(R.id.ImageView01);
        buildingName = (EditText) rootView.findViewById(R.id.et_building_name);
        addEditText = (Button) rootView.findViewById(R.id.bt_add);
        removeEditText = (Button) rootView.findViewById(R.id.bt_remove);
        mLayout =  (LinearLayout)rootView.findViewById(R.id.property_name);
        numberOfProperties= (TextView)rootView.findViewById(R.id.numberofproperties);
        client1Name= (TextView)rootView.findViewById(R.id.tv_ImageView01);
        //final String[] values = new String[] { "Android", "iPhone", "WindowsMobile" };

        values = new ArrayList<String>();
        /*EditText editText = new EditText(getActivity());
        editText.setText("New text");*/

        // use your custom layout
        ListView myListView = (ListView) rootView.findViewById(R.id.listViewswipeview);
         adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.test_layout1, R.id.et_building_name, values);
        myListView.setAdapter(adapter);


        clientIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

                startActivityForResult(intent, 1);

            }
        });


        addEditText.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View v) {
                    if(values.size()<3) {
                        values.add("");
                        //Log.i(getActivity(),values.size());
                        adapter.notifyDataSetChanged();
                        numberOfProperties.setText(Integer.toString(values.size()));
                    }
                }
        });

        removeEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(values.size()>0)
                    values.remove(values.size()-1);
                //Log.i(getActivity(),values.size());
                adapter.notifyDataSetChanged();
                numberOfProperties.setText(Integer.toString(values.size()));
            }
        });
        return rootView;
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
                        client1Name.setText(name);
                        Log.i("start droom","name="+name);

                    }
                }
                //break;
        }

    /*private OnClickListener onClick() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                mLayout.addView(createNeweditText(buildingName.getText().toString()));
            }
        };
    }*/

    /*private EditText createNewEditText(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final EditText editText = new EditText(getActivity());
        editText.setLayoutParams(lparams);
        Log.i("naya","editText");
        editText.setText("New text: " + text);
        return editText;
    }*/

    //}
}

/*
public class MyAdapter extends ArrayAdapter {
    private LayoutInflater mInflater;
    public ArrayList myItems = new ArrayList();

    public MyAdapter() {
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 20; i++) {
            ListItem listItem = new ListItem();
            listItem.caption = "Caption" + i;
            myItems.add(listItem);
        }
        notifyDataSetChanged();
    }

    public int getCount() {
        return myItems.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item, null);
            holder.caption = (EditText) convertView
                    .findViewById(R.id.ItemCaption);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //Fill EditText with the value you have in data source
        holder.caption.setText(myItems.get(position).caption);
        holder.caption.setId(position);

        //we need to update adapter once we finish with editing
        holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    myItems.get(position).caption = Caption.getText().toString();
                }
            }
        });

        return convertView;
    }
}

class ViewHolder {
    EditText caption;
}

class ListItem {
    String caption;
}
}*/
