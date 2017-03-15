package com.nbourses.oyeok.adapters;

/**
 /**
 * Created by ritesh on 30/09/16.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.realmModels.Favourites;
import com.nbourses.oyeok.realmModels.addBuildingRealm;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Note that this adapter requires a valid {@link GoogleApiClient}.
 * The API client must be maintained in the encapsulating Activity, including all lifecycle and
 * connection states. The API client must be connected with the {@link Places#GEO_DATA_API} API.
 */
public class PlacesAutoCompleteAdapter extends RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder> implements Filterable {

    private static final String TAG = "PlacesAutoCompleteAdapter";
    private ArrayList<PlaceAutocomplete> mResultList;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds mBounds;
    private AutocompleteFilter mPlaceFilter;

    private Context mContext;
    private int layout;

    public PlacesAutoCompleteAdapter(Context context, int resource, GoogleApiClient googleApiClient,
                                    LatLngBounds bounds, AutocompleteFilter filter) {
        mContext = context;
        layout = resource;
        mGoogleApiClient = googleApiClient;
        mBounds = bounds;
        mPlaceFilter = filter;
        getFilter().filter(null);
    }

    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getAutocomplete(constraint);
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                else{
                    mResultList = getAutocomplete(constraint);
                    Log.i("TAG","narcoss "+mResultList);
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    Log.i("TAG","narcoss 2 ");
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    private ArrayList<PlaceAutocomplete> getAutocomplete(CharSequence constraint) {
        ArrayList resultList = new ArrayList<>();

        if (mGoogleApiClient.isConnected()) {
            Log.i("", "Starting autocomplete query for: " + constraint);

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    mBounds, mPlaceFilter);

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                //Toast.makeText(mContext, "Error contacting API: " + status.toString(),Toast.LENGTH_SHORT).show();
                SnackbarManager.show(
                        Snackbar.with(mContext)
                                .text("Search failed! connectivity issue.")
                                .position(Snackbar.SnackbarPosition.TOP)
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                Log.e("", "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i("", "Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");

            // Copy the results into our own data structure, because we can't hold onto the buffer.
            // AutocompletePrediction objects encapsulate the API response (place ID and description).

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
             resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                Log.i("prediction","narcos predictions Pid: "+prediction.getPlaceId()+" SD "+prediction.getPrimaryText(null)+" SD "+prediction.getSecondaryText(null)+" FD "+prediction.getFullText(null));
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),
                        prediction.getPrimaryText(null),prediction.getSecondaryText(null),false));
            }

            // Release the buffer now that all data has been copied.
            autocompletePredictions.release();
            try {

                Realm myRealm = General.realmconfig(mContext);
                RealmResults<Favourites> results11 = myRealm.where(Favourites.class).findAll();
                Log.i("TAG", "insiderrn yo 1 sushil ==================================");
                for (Favourites c : results11) {
                    Log.i("TAG", "insiderrnm21 ");
                    Log.i("TAG", "insiderrnm31 " + c.getTitle());
                    Log.i("TAG", "insiderrnm41 " + c.getLatiLongi().getLat());
                    Log.i("TAG", "insiderrnm41 " + c.getLatiLongi().getLng());
                    Log.i("TAG", "insiderrnm sushil "+ c.getTitle());

                        if (constraint.toString().equalsIgnoreCase("Ritz369")) {
                            resultList.add(0, new PlaceAutocomplete(c.getId(),
                                    c.getTitle(), c.getAddress(), false));

                        } else if (c.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            Log.i("TAG", "insiderrn yo  " + constraint.toString() + " " + c.getTitle());
                            //resultList.ins(c.getTitle());
                            resultList.add(0, new PlaceAutocomplete(c.getId(),
                                    c.getTitle(), c.getAddress(), false));
                        }



                }

               // Realm myRealm = General.realmconfig(mContext);
                RealmResults<addBuildingRealm> results1 =
                        myRealm.where(addBuildingRealm.class).findAll();

                for (addBuildingRealm c : results1) {

                    //* Log.i("TAG", "insiderrn yo 1 " + searchQuery + " " + c.getTitle());*//*
                    if(c.getType().equalsIgnoreCase("ADD")) {
                        if (constraint.toString().equalsIgnoreCase("Ritz369")) {
                            resultList.add(1, new PlaceAutocomplete(c.getId(),
                                    c.getBuilding_name(), c.getAddress(), true));

                        } else if (c.getBuilding_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            Log.i("TAG", "insiderrn yo " + constraint.toString() + " " + c.getBuilding_name());
                            //resultList.ins(c.getTitle());
                            resultList.add(1, new PlaceAutocomplete(c.getId(),
                                    c.getBuilding_name(), c.getAddress(), false));
                        }

                    }

                }



            }
            catch(Exception e){
                Log.i("TAG","Caught in exception Favourites Realm "+e );
            }

            return resultList;
        }
        Log.e("", "Google API client is not connected for autocomplete query.");

        try {
            /*Realm myRealm = General.realmconfig(mContext);
            RealmResults<Favourites> results1 =
                    myRealm.where(Favourites.class).findAll();

            for (Favourites c : results1) {
                Log.i("TAG", "insiderrnm2 "+ c.getAddress());
                Log.i("TAG", "insiderrnm3 " + c.getTitle());
                Log.i("TAG", "insiderrnm4 " + c.getLatiLongi().getLat());
                Log.i("TAG", "insiderrnm4 " + c.getLatiLongi().getLng());
                   *//* Log.i("TAG", "insiderrn yo 1 " + searchQuery + " " + c.getTitle());*//*
                    *//*if(c.getTitle().toLowerCase().contains(searchQuery.toLowerCase())){
                        Log.i("TAG", "insiderrn yo " + searchQuery + " " + c.getTitle());*//*
                //resultList.ins(c.getTitle());
                resultList.add(0,new PlaceAutocomplete(c.getTitle(),
                        c.getTitle(), c.getAddress(),true));
                // }

            }*/



            Realm myRealm = General.realmconfig(mContext);
            RealmResults<Favourites> results11 = myRealm.where(Favourites.class).findAll();
            Log.i("TAG", "insiderrn yo 1 sushil ==================================");
            for (Favourites c : results11) {
                Log.i("TAG", "insiderrnm21 ");
                Log.i("TAG", "insiderrnm31 " + c.getTitle());
                Log.i("TAG", "insiderrnm41 " + c.getLatiLongi().getLat());
                Log.i("TAG", "insiderrnm41 " + c.getLatiLongi().getLng());
                Log.i("TAG", "insiderrnm sushil "+ c.getId());

               // if (constraint.toString().equalsIgnoreCase("Ritz369")) {
                    resultList.add(0, new PlaceAutocomplete(c.getId(),
                            c.getTitle(), c.getAddress(), true));

               /* } else if (c.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    Log.i("TAG", "insiderrn yo  " + constraint.toString() + " " + c.getTitle());
                    //resultList.ins(c.getTitle());
                    resultList.add(0, new PlaceAutocomplete(c.getId(),
                            c.getTitle(), c.getAddress(), false));
               }*/



            }




//            Realm myRealm = General.realmconfig(mContext);
            RealmResults<addBuildingRealm> results1 =
                    myRealm.where(addBuildingRealm.class).findAll();

            for (addBuildingRealm c : results1) {


                //resultList.ins(c.getTitle());
                if(c.getType().equalsIgnoreCase("ADD")){
                resultList.add(0,new PlaceAutocomplete(c.getId(),
                        c.getBuilding_name(), c.getAddress(),true));
                 }

            }

        }
        catch(Exception e){
            Log.i("TAG","Caught in exception Favourites Realm "+e );
        }
        return resultList;
       // return null;
    }

    @Override
    public PredictionHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, viewGroup, false);
        final PredictionHolder mPredictionHolder = new PredictionHolder(convertView);

        mPredictionHolder.delete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               /* if(event.getAction() == MotionEvent.ACTION_DOWN)*/
                Log.i("TAG", "brara yo "+event);

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        final AlertDialog.Builder alert = new AlertDialog.Builder(
                                mContext);
                        alert.setTitle("Delete!!");
                        alert.setMessage("Are you sure to delete favourite?");
                        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //do your work here
                                try {
                                    Realm myRealm = General.realmconfig(mContext);

                                    myRealm.beginTransaction();
                                    RealmResults<Favourites> results = myRealm.where(Favourites.class).equalTo("id",mResultList.get(mPredictionHolder.getAdapterPosition()).placeId.toString()).findAll();
                                    results.clear();
                                    myRealm.commitTransaction();


                                    myRealm.beginTransaction();
                                    RealmResults<addBuildingRealm> results1 = myRealm.where(addBuildingRealm.class).equalTo("id",mResultList.get(mPredictionHolder.getAdapterPosition()).placeId.toString()).findAll();
                                    results1.clear();
                                    myRealm.commitTransaction();

                                    getFilter().filter("ritz369");

                                }
                                catch(Exception e){
                                    Log.i("TAG","Caught in exception Favourites Realm "+e );
                                }
                                dialog.dismiss();


                            }
                        });
                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });


                            alert.show();

                        break;

                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        return mPredictionHolder;
    }

    @Override
    public void onBindViewHolder(PredictionHolder mPredictionHolder, final int i) {
        mPredictionHolder.mPrediction.setText(mResultList.get(i).description);
        mPredictionHolder.title.setText(mResultList.get(i).title);
        if(!mResultList.get(i).fav) {
            Log.i("TAG", "lara "+mResultList.get(i).fav);
            mPredictionHolder.delete.setVisibility(View.GONE);
            mPredictionHolder.delete.setOnClickListener(null);
            mPredictionHolder.favIcon.setImageResource(R.drawable.s_marker);
        }
        else{
            Log.i("TAG", "brara "+mResultList.get(i).fav);
            mPredictionHolder.delete.setVisibility(View.VISIBLE);
            if(mResultList.get(i).title.toString().toLowerCase().contains("home"))
            mPredictionHolder.favIcon.setImageResource(R.drawable.s_home);
            else if(mResultList.get(i).title.toString().toLowerCase().contains("office"))
                mPredictionHolder.favIcon.setImageResource(R.drawable.s_office);
            else
                mPredictionHolder.favIcon.setImageResource(R.drawable.s_fav);
        }

        /*mPredictionHolder.mRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetLatLonCallback.getLocation(resultList.get(i).toString());
            }
        });*/


    }

    @Override
    public int getItemCount() {
        if(mResultList != null)
            return mResultList.size();
        else
            return 0;
    }

    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }

    public class PredictionHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*//*, View.OnTouchListener*/ {
        private TextView mPrediction;
        private TextView title;
        private ImageView delete;
        private ImageView favIcon;
        private RelativeLayout mRow;

        public PredictionHolder(View itemView) {

            super(itemView);
            mPrediction = (TextView) itemView.findViewById(R.id.address);
            title = (TextView) itemView.findViewById(R.id.title);
            mRow=(RelativeLayout)itemView.findViewById(R.id.predictedRow);

            delete = (ImageView) itemView.findViewById(R.id.delete);
            favIcon = (ImageView) itemView.findViewById(R.id.image);


        }



    }


    /**
     * Holder for Places Geo Data Autocomplete API results.
     */
    public class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence title;
        public CharSequence description;
        public Boolean fav;

        PlaceAutocomplete(CharSequence placeId, CharSequence title, CharSequence description, Boolean fav) {
            Log.i("TAG", "insiderrn yo pro " + title + "description " + description+" "+fav);
            this.placeId = placeId;
            this.title = title;
            this.description = description;
            this.fav = fav;
        }

        @Override
        public String toString() {
            return description.toString();
        }

    }


}