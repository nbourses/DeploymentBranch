package com.nbourses.oyeok.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Set;

/**
 * Created by YASH_SHAH on 29/12/2015.
 */
public class SharedPrefs {
    //SharedPreferences file name
    private static String SHARED_PREFS_FILE_NAME = "oyeok_shared_prefs";

    public static String MY_GCM_ID = "myGCMId";
    public static String MY_LAT = "myLat";
    public static String MY_LNG = "myLong";
    public static String MY_LOCALITY = "myLocality";
    public static String My_BALANCE= "34";
    public static String MY_CITY = "myCity";
    public static String MY_PINCODE = "myPincode";
    public static String MY_REGION = "myRegion";
    public static String MY_SPEC_CODE = "mySpecCode";
    public static final String CHECK_BEACON = "check_beacon";
    public static final String CHECK_WALKTHROUGH = "check_walkthrough";
    public static String EMAIL_PROFILE = "emailprofile";

    public static final String PERMISSION = "permission";
    //public static String MY_PROPERTY_SUBTYPE = "myPropertySubtype";
    //public static String MY_TT = "myTT";
    //public static String MY_Price = "myPrice";

    //get the SharedPreferences object instance
    //create SharedPreferences file if not present

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }
    public static int getBadgeCount(Context context, String prefName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Log.i("TRACE", "rt" + prefName);

        return prefs.getInt(prefName, 0);
    }
    //Strings
    public static void save(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();

    }
    public static void save(Context context, String key, Set<String> value) {
        getPrefs(context).edit().putStringSet(key,value).commit();

    }

    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }
}
