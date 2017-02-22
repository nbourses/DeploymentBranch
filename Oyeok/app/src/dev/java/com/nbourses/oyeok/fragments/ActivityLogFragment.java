package com.nbourses.oyeok.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.adapters.ActivityLogAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.ActivityLog;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityLogFragment extends Fragment {

    private List<ActivityLog> logList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView tcx;
    private ActivityLogAdapter mAdapter;
    private PubNub pubnub;
    private  String TAG = "Activity Log";
    public ActivityLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // View v = inflater.inflate(R.layout.fragment_activity_log, container, false);
        View layout = inflater.inflate(R.layout.fragment_activity_log, container, false);
      //  tcx = (TextView) layout.findViewById(R.id.tcx);
       // tcx.setText("asdfghjkljhgfdsdfghjkjhgfdsdfghj ");
      RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.rview);

       // mAdapter = new ActivityLogAdapter(logList);
       // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
       pubnub = General.initPubnub(getContext(),General.getSharedPreferences(getContext(), AppConstants.USER_ID));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       // recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
       // recyclerView.setLayoutManager(mLayoutManager);

      //  recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.setAdapter(mAdapter);

        mAdapter = new ActivityLogAdapter(logList);
        recyclerView.setAdapter(mAdapter);

        LoadLog();

        return layout;
    }




    private void LoadLog() {

        try {
            Log.i(TAG, "Load log called ");

            pubnub.history()
                    .channel(/*"global_log_14dlr9x50bbuq1ptgkbb0lfh26f7y6no"*/"global_log_"+AppConstants.MASTER_ACTIVITY_LOG_ID) // where to fetch history from
                    .count(AppConstants.MSG_COUNT) // how many items to fetch
                    .includeTimetoken(true) // include timetoken with each entry
                    .async(new PNCallback<PNHistoryResult>() {
                        @Override
                        public void onResponse(PNHistoryResult result, PNStatus status) {

                            try {
                                Log.i(TAG, "Load log called result 2 "+status);
                                for (PNHistoryItemResult historyItemResult : result.getMessages()) {

                                    try {
                                        if (historyItemResult.getEntry().has("pn_gcm")) {
                                            Log.i(TAG, "history is tha 3 " + historyItemResult.getEntry().toString());
                                            // String content = jsonNode.get("data").textValue();

                                            String j = historyItemResult.getEntry().get("pn_gcm").get("data").toString();
                                            // JSONObject j = new JSONObject(historyItemResult.getEntry().get("pn_gcm").toString());
                                            Log.i(TAG, "history is tha 7 " + j);
                                            final JSONObject jsonObj = new JSONObject(j);
                                            jsonObj.put("timetoken", historyItemResult.getTimetoken().toString());


                                            // jsonA = new JSONArray();
                                            Log.i(TAG, "history is tha 10 jsonArrayHistory jsonObj " + jsonObj);

                                            String name = jsonObj.getString("name");
                                            String message = jsonObj.getString("message");

                                            Log.i(TAG, "history is tha 11 jsonArrayHistory jsonObj inside createRunnable run " +name +" "+message);
                                            ActivityLog item = new ActivityLog(name,message);
                                            logList.add(item);

                                            mAdapter.notifyDataSetChanged();

                                           //createRunnable(jsonObj);
                                        }

                                    } catch (Exception e) {
                                        Log.i(TAG, "PUBNUB history Caught in exception recieved message not proper " + e);
                                    }
                                }

                            } catch (Exception e) {
                                Log.i(TAG, "Caught in exception loading final history " + e);
                            }
                        }

                    });



        }catch(Exception e){

        }

    }


    private void createRunnable(final JSONObject j){

        Log.i(TAG, "history is tha 11 jsonArrayHistory jsonObj inside createRunnable " + j);
        Runnable aRunnable = new Runnable(){
            public void run(){
                try {
                   final String name = j.getString("name");
                    final String message = j.getString("message");

                Log.i(TAG, "history is tha 11 jsonArrayHistory jsonObj inside createRunnable run " +name +" "+message);
                    ActivityLog item = new ActivityLog(name,message);
                    logList.add(item);

                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.i(TAG, "Caught in exception inside createRunnable run "+e.getMessage());

                }

            }
        };



    }
}
