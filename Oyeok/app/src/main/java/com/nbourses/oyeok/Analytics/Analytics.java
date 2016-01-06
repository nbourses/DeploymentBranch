package com.nbourses.oyeok.Analytics;
import android.util.Log;

import com.amplitude.api.Amplitude;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YASH_SHAH on 06/01/2016.
 */
public final class Analytics {
    public static final String TAG = Analytics.class.getSimpleName();

    public static void logButtonClick(String buttonText, String screenName){
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put("Screen", screenName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Amplitude.getInstance().logEvent(buttonText,eventProperties);
        Log.v(TAG,buttonText+" from "+screenName+" clicked");
    }
    public static void logScreenVisit(String screenName){
        Amplitude.getInstance().logEvent(screenName);
    }
    public static void logScreenExit(String screenName){
        /*long duration = calcScreenTime(startTime,endTime);
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put("Duration", duration);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v(TAG,duration+" sec spent on "+screenName);*/
        Amplitude.getInstance().logEvent(screenName);
    }
    public static void localitySearched(String locality){
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put("locality",locality );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Amplitude.getInstance().logEvent("Locality from Rex",eventProperties);
    }
    public static void logDayTime(){

    }
    private static long calcScreenTime(long start,long end){
        return (end - start)/1000;
    }
    /*
    # of times : screen visited
    # of times : screen exited
    # of times : button clicked
    # of times : location searched
    # of times : app opened
    # of times : app crashed
    time spent on : app
    time spent on : screen
    time when the app opened
    Inactive time between two consecutive sessions
    member logged in through marketing campaign?
*/

}
