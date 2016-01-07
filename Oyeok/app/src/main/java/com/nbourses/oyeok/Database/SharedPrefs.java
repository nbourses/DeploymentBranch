package com.nbourses.oyeok.Database;

import android.content.Context;
import android.content.SharedPreferences;

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
    public static String MY_CITY = "myCity";
    public static String MY_PINCODE = "myPincode";
    public static String MY_REGION = "myRegion";

    //get the SharedPreferences object instance
    //create SharedPreferences file if not present

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
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
