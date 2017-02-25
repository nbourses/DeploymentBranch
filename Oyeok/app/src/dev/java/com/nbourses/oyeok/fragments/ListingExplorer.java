package com.nbourses.oyeok.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.activities.BrokerListingActivity;
import com.nbourses.oyeok.adapters.BrokerListingListView;
import com.nbourses.oyeok.adapters.ListingExplorerAdapter;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.loadBuildingDataModel;
import com.nbourses.oyeok.models.portListingModel;

import java.util.ArrayList;

import io.realm.Realm;


public class ListingExplorer extends Fragment {

    View v;
    ListView list_listView;
    TextView list_back,list_next;
    private static ArrayList<portListingModel> portListing1=new ArrayList<>();
    private static ArrayList<portListingModel> selected_listing=new ArrayList<>();
    ListingExplorerAdapter adapter;
    private Realm realm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_listing_explorer, container, false);
        list_listView=(ListView)v.findViewById(R.id.list_listView);
        list_back=(TextView)v.findViewById(R.id.list_back);
        list_next=(TextView)v.findViewById(R.id.list_next);
        //realm= General.realmconfig(getContext() );
        /*if(portListing1 != null)
            portListing1.clear();*/
        portListing1= ((BrokerListingActivity)getActivity()).PortlistingData();
        Log.i("listingtest","listing count "+portListing1.size());

        adapter=new ListingExplorerAdapter(getContext(),portListing1);
        list_listView.setAdapter(adapter);

        list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BrokerListingActivity)getActivity()).Close();
            }
        });

        list_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (portListingModel hold : adapter.getAllData()) {
                    if (hold.isCheckbox()) {

                        selected_listing.add(hold);
                        Log.i("listing_test", "selected building 1: " + hold.getName() + "  selectedlist.size() : " + selected_listing.size());
                    }

                }

                ((BrokerListingActivity)getActivity()).OpenListingTitle(selected_listing);
            }
        });


        list_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.setCheckBox(position);
                Log.i("multimode","inside onItemClick  123 "+position);

            }
        });


        return v;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        //realm.close();

    }

}
