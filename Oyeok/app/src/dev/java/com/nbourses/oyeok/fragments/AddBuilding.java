package com.nbourses.oyeok.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.adapters.addBuildingAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.realmModels.addBuilding;

import io.realm.Realm;


public class AddBuilding extends Fragment {

/*
    private OnFragmentInteractionListener mListener;*/

    public AddBuilding() {
        // Required empty public constructor
    }

private TextView Cancel,back,usertext;
    private View v;
    ListView listView1;
    EditText inputSearch1;
    addBuildingAdapter adapter;
    private Realm realm;
    ImageView add;
    String name;
    private TextView dialog;
    LinearLayout add_b;
    private SideBar sideBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v=inflater.inflate( R.layout.fragment_add_building, container, false );
        Cancel=(TextView)v.findViewById(R.id.Cancel);
        back=(TextView)v.findViewById(R.id.back);
        listView1=(ListView) v.findViewById(R.id.listView1);
        inputSearch1=(EditText)v.findViewById(R.id.inputSearch1);
        add=(ImageView) v.findViewById(R.id.add);
        adapter = new addBuildingAdapter(getContext(),1);
        listView1.setAdapter(adapter);
        realm = General.realmconfig(getContext());
        adapter.setResults(realm.where(addBuilding.class).findAll());
        add_b=(LinearLayout)v.findViewById(R.id.add_b);
        usertext=(TextView)v.findViewById(R.id.usertext);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClientMainActivity)getActivity()).closeAddBuilding();
                ((ClientMainActivity)getActivity()).openAddListing();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.PROPERTY="Home";
                ((ClientMainActivity)getActivity()).closeAddBuilding();
            }
        });


        inputSearch1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                if(TT=="LL"){
                    Log.i( "portfolio","onTextChanged  LL : "+cs );

                    usertext.setText("'"+cs+"'");
                    adapter.setResults( realm.where(addBuilding.class) //implicit AND
                            .beginGroup()
                            .contains("Building_name", cs.toString(),false)
                            .endGroup()
                            .findAll() );
               /* }else{

                    adapter.setResults( realm.where(MyPortfolioModel.class)
                            .greaterThan("or_psf", 0)  //implicit AND
                            .beginGroup()
                            .contains("name", cs.toString(),false)
                            .endGroup()
                            .findAll() );
                    Log.i( "portfolio","onTextChanged  LL : ");

                }*/

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                name=String.valueOf(arg0);
            }
        });

        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((ClientMainActivity)getActivity()).closeAddBuilding();
                if(inputSearch1.getText().toString().equalsIgnoreCase("")) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Empty Text")
                            .setMessage("Please Type Your Building Name.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete

                                }
                            })

                        /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })*/
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }else {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputSearch1.getWindowToken(), 0);
                    ((ClientMainActivity) getActivity()).setlocation(name);
                }

            }
        });




        sideBar = (SideBar) v.findViewById(R.id.sideIndex);
//        dialog = (TextView) v.findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //Set the right touch monitor
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //The position of the first occurrence of the letter
                /*int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }*/

            }
        });


         init();

        return v;
    }





  private void  init(){
      listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

          }
      });
    }

  /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }*/

/*    @Override
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
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
