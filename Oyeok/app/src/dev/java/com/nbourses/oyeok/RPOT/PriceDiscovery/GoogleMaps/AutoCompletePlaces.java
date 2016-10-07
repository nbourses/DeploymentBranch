package com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.realmModels.Favourites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by DADDU_DON on 12/7/2015.
 */
public class AutoCompletePlaces {

    private static final String TAG = "AutoCompletePlaces";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static String searchQuery = "";
    private static Context c;
   // private static final String API_KEY = "AIzaSyCLWLri_ZzN0udt86teXNF55vza44uoRJs";
    private static final String API_KEY = "AIzaSyD9u7py1PGKcnlrO77NuY_40jxgIOhX34I";

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        Log.i(TAG,"results list is yo "+input);
        searchQuery = input;
        Log.i(TAG,"results list is yo searchQuery "+searchQuery);
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            //sb.append("&components=country:gr");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            Log.d(TAG, "sb " + sb);

            URL url = new URL(sb.toString());
            Log.d(TAG, "url.getPath "+url.getPath());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        }
        catch (MalformedURLException e) {
            //Log.e(LOG_TAG, "Error processing Places API URL", e);
            e.printStackTrace();
            return resultList;
        }
        catch (IOException e) {
            // Log.e(LOG_TAG, "Error connecting to Places API", e);
            e.printStackTrace();
            return resultList;
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            Log.i(TAG,"results list is "+predsJsonArray);

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }

              try {
              Realm myRealm = General.realmconfig(c);
            RealmResults<Favourites> results1 =
                    myRealm.where(Favourites.class).findAll();

            for (Favourites c : results1) {
                Log.i(TAG, "insiderrn2 ");
                Log.i(TAG, "insiderrn3 " + c.getTitle());
                Log.i(TAG, "insiderrn4 " + c.getLatiLongi().getLat());
                Log.i(TAG, "insiderrn4 " + c.getLatiLongi().getLng());
                Log.i(TAG, "insiderrn yo 1 " + searchQuery + " " + c.getTitle());
                if(c.getTitle().toLowerCase().contains(searchQuery.toLowerCase())){
                    Log.i(TAG, "insiderrn yo " + searchQuery + " " + c.getTitle());
                    //resultList.ins(c.getTitle());
                    resultList.add(0,c.getTitle());
                }

            }



        }
        catch(Exception e){
            Log.i(TAG,"Caught in exception Favourites Realm "+e );
        }

        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Caught in exception Google places autocomplete Cannot process JSON results", e);
        }

        return resultList;
    }

    public static class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList<String> resultList = new ArrayList<String>();

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {

            super(context, textViewResourceId);
            getFilter().filter(null);
            c = context;
        }


        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }




        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    Log.i(TAG, "swat ");
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                       // resultList.add("Colaba, Mumbai, India");
                        Log.d(TAG, "resultList "+resultList);

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    else{
                        resultList.add("swato");
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        Log.i(TAG, "swat 1 ");
                        notifyDataSetChanged();
                    } else {
                        Log.i(TAG, "swat 2");
                        notifyDataSetInvalidated();

                    }
                }
            };
            return filter;
        }
    }
}

