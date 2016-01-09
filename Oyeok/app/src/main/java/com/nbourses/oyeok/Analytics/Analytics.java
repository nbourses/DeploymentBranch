package com.nbourses.oyeok.Analytics;
import android.util.Log;

import com.amplitude.api.Amplitude;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by YASH_SHAH on 06/01/2016.
 */
public final class Analytics {
    public static final String TAG = Analytics.class.getSimpleName();
    public static final boolean recordEvents = false;

    public static void logButtonClick(String buttonText, String screenName){
        if(recordEvents == true )
            Amplitude.getInstance().logEvent("["+screenName+"] "+buttonText);
    }
    public static void logScreenVisit(String screenName){
        if(recordEvents == true )
            Amplitude.getInstance().logEvent(screenName+"[Entry]");
    }
    public static void logScreenExit(String screenName){
        if(recordEvents == true )
            Amplitude.getInstance().logEvent(screenName+"[Exit]");
    }
    public static void localitySearched(String locality){
        if(recordEvents == true )
            Amplitude.getInstance().logEvent("[Locality]"+locality);
    }

    private static long calcScreenTime(long start,long end){
        return (end - start)/1000;
    }

    public static void organicUser(boolean b) {
        if(recordEvents == true )
            Amplitude.getInstance().logEvent("Acquired Users");
    }
    public static void recordLoginTime(){
        if(recordEvents == true ) {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            Amplitude.getInstance().logEvent("[LoginTime] " + hour);
        }
    }
    /*
    # of times : screen visited
    # of times : screen exited
    # of times : button clicked
    # of times : location searched
    # of times : app opened
    # of times : app crashed
    time spent on : app
    time spent on : screen - needs a paid account
    time when the app opened
    Inactive time between two consecutive sessions
    member logged in through marketing campaign?
*/

}
