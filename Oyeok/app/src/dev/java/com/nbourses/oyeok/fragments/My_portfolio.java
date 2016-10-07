package com.nbourses.oyeok.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

import com.nbourses.oyeok.R;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link My_portfolio.OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class My_portfolio extends Fragment {

    private OnFragmentInteractionListener mListener;

    Realm realm;
    private View view;

    public My_portfolio() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

              view=  inflater.inflate(R.layout.fragment_my_portfolio, container, false);


        TabHost host = (TabHost) view.findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Rent");
        spec.setContent(R.id.Rental);
        spec.setIndicator("Rent");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Buy/Sell");
        spec.setContent(R.id.Buy_Sell);
        spec.setIndicator("Buy/Sell");
        host.addTab(spec);


//        RecyclerView rental_list=(RecyclerView) view.findViewById(R.id.Rental_listview);
        //        String [] Buidings={"Bhakti Park","Crossword","Girnar Heights","BHakti-Park,Old Club House","Himalayan Height","Garden View","Hirak","Imperial Residency","siddhesh Residential","Nikhil Residential","Ritesh height","swift height"};
        // ListAdapter sampleadapter= new myPortfolioAdapter(getContext());
        ListView rental_list=(ListView) view.findViewById(R.id.Rental_listview);




        /*myPortfolioAdapter adapter = new myPortfolioAdapter(getContext(), 1,rental_list);

        rental_list.setAdapter(adapter);
        realm = General.realmconfig(getContext());
        adapter.setResults(realm.where(MyPortfolioModel.class).findAll());*/


//        rental_list.setAdapter(sampleadapter);
        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
       /* mListener = null;*/
    }

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
    public interface OnFragmentInteractionListener {

    }
}
