package com.nbourses.oyeok.Firebase;

import android.app.Activity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.nbourses.oyeok.Database.SharedPrefs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abhinandan on 1/11/2016.
 */
public class HourGlassFirebase {
    Firebase firebaseReference;

    public HourGlassFirebase(Activity activity, String url) {

        this.firebaseReference = new Firebase(url);
        if (!SharedPrefs.getString(activity, "UserId").equals(null))
            firebaseReference = firebaseReference.child("HourGlass").child(SharedPrefs.getString(activity, "UserId"));
    }

    public void saveHourGlassDetails(HourGlassDetails hourGlassDetails) {
        firebaseReference.setValue(hourGlassDetails);
    }

    public void updateHourGlassDetails(HourGlassDetails hourGlassDetails) {
        Map<String, Object> map1 = new HashMap<String, Object>();
        //map1.put("lastUpdated", hourGlassDetails.getLastUpdated());
        map1.put("wholeHourGlass", hourGlassDetails.getWholeHourGlass());
        map1.put("percentage", hourGlassDetails.getPercentage());
        firebaseReference.updateChildren(map1);
    }

    public HourGlassDetails getHourGlassDetails() {
        final HourGlassDetails hourGlassDetails = new HourGlassDetails();
        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot child : snapshot.getChildren()) {
                    switch (child.getKey()) {
                        case "wholeHourGlass":
                            hourGlassDetails.setWholeHourGlass((Integer) child.getValue());
                            break;
                        case "percentage":
                            hourGlassDetails.setPercentage((Integer) child.getValue());
                            break;
                    }

                }
                Log.i("Test3", "");

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return hourGlassDetails;
    }


}