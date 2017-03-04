package com.nbourses.oyeok.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.MyApplication;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.UpdateStatus;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.activities.ClientDealsListActivity;
import com.nbourses.oyeok.activities.ProfileActivity;
import com.nbourses.oyeok.fragments.GPSTracker;
import com.nbourses.oyeok.models.PublishLetsOye;
import com.nbourses.oyeok.realmModels.DefaultDeals;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.nbourses.oyeok.R.id.area;
import static java.lang.Math.log10;

/**
 * Created by rohit on 09/02/16.
 */
public class General extends BroadcastReceiver {

    public static HashMap<String, String> oyeDataHolder = new HashMap<String, String>();
    public static final String TAG = "General";
    public static Set<String> set;
    public static Boolean clearChart = false;
    private NetworkInterface networkInfo;
    public static long startTime;
    private static Context con;
    public static Thread t;
    public static Boolean slowInternetFlag;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("h:mm a");
    private static Bitmap mIcon12 = null; // null image for test condition in download image
    private static ImageView img;
    private static String OKID = null ;
    private static int llMin, llMax;
    private static String gr;
    private static JSONObject j = new JSONObject();

    public static void showPermissionDialog(Context context,Activity activity) {
        GPSTracker gps = new GPSTracker(context);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {



            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    AppConstants.LOCATION_PERMISSION_REQUEST_CODE);

        } else {
            if (gps.canGetLocation()) {

               saveLatLongLoc(context, gps.getLatitude(), gps.getLongitude());
            }else{
                SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LAT, "19.1230339");
                SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LNG, "72.8350437");
                General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LAT,"19.1230339");
                General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LNG,"72.8350437");
                SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LOCALITY, "Andheri West");
                General.setSharedPreferences(getApplicationContext(),AppConstants.LOCALITY,"Andheri West");
            }
        }
    }

    public static void saveLatLongLoc(Context context, Double lat, Double lng){
        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LAT, lat +"");
        SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LNG, lng + "");
        General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LAT,lat + "");
        General.setSharedPreferences(getApplicationContext(),AppConstants.MY_LNG,lng + "");
        Log.i("Tag11","latlong 12 : "+lat+"  "+lng);
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(lat,lng, 1);
            String region = addresses.get(0).getSubLocality();
            SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_LOCALITY, region);
            General.setSharedPreferences(getApplicationContext(),AppConstants.LOCALITY,region);

            Log.i("Tag11","latlong 13 : "+lat+"  "+lng+ ""+region);
        } catch (IOException e) {
            Log.i("TAG","Caught in exception in geocoding 1"+e);
        }
    }

    public static void slowInternet(Context context) {
       try {
           General.startTime = SystemClock.uptimeMillis();

            con = context;

            t = new Thread(new Runnable() {
                public void run() {
                    slowInternetFlag = true;
                    Log.i(TAG," 1 "+((SystemClock.uptimeMillis() - startTime) / 1000.0));
while(slowInternetFlag) {
    if (((SystemClock.uptimeMillis() - startTime) / 1000.0) > AppConstants.slowInternet) {
        Log.i(TAG, " Slow net connection ,Please move to better connectivity area.");
        try {
            SnackbarManager.show(
                    Snackbar.with(con)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("Slow net connection ,Please move to better connectivity area.")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        slowInternetFlag = false;
        General.t.interrupt();

    }
}

                }
            });
            t.start();
        }
        catch(Exception e){
            Log.i(TAG,"Caught in exception slowInternet in General.java "+e);
        }
    }


    public static void filterSetSnackbar(Context context, String filterPtype){
        try {
            SnackbarManager.show(
                    Snackbar.with(context)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("Current filter set to: " + filterPtype.substring(0, 1).toUpperCase() + filterPtype.substring(1) +" in "+(General.getSharedPreferences(context,AppConstants.TT).equalsIgnoreCase(AppConstants.RENTAL) ? "Rental" : "Resale"))
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
        }
        catch(Exception e){
            Log.i(TAG,"Caught in exception filterSetSnackbar in General.java "+e);
        }

    }

    public static void internetConnectivityMsg(Context context) {
        try {
            SnackbarManager.show(
                    Snackbar.with(context)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("No internet Connectivity.")
                            //.animation(true)
                            .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
        }
        catch(Exception e){
            Log.i(TAG,"Caught in exception internetConnectivityMsg in General.java "+e);
        }

    }



    //  private static Set<String> defaultDeals ;

    //    public General(NetworkInterface networkInfo)
//    {
//        this.networkInfo = networkInfo;
//    }

   public static Realm realmconfig(Context context){
        RealmConfiguration config = new RealmConfiguration
                .Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
       Realm myRealm = Realm.getInstance(config);
        return myRealm;
    }

public static PubNub initPubnub(Context context, String UUID){
    PNConfiguration pnConfiguration = new PNConfiguration();
    pnConfiguration.setSubscribeKey(AppConstants.PUBNUB_SUBSCRIBE_KEY);
    pnConfiguration.setPublishKey(AppConstants.PUBNUB_PUBLISH_KEY);

        pnConfiguration.setUuid(UUID);


    return new PubNub(pnConfiguration);
}


    public static void saveSet(Context context,String name, Set<String> value) {
// for storing catched msgs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(name, value);
        editor.commit();

    }

    public static Set<String> getSet(Context context, String name) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);


        return prefs.getStringSet(name, null);


    }


    public static void saveMutedOKIds(Context context, Set<String> value) {

        Log.i("TRACE", "save default deal inside" + value);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(AppConstants.MUTED_OKIDS, value);
        editor.commit();

    }

    public static Set<String> getMutedOKIds(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);


        return prefs.getStringSet(AppConstants.MUTED_OKIDS, null);


    }

    public static String getSampleDealsJsonData(Context contex) {
        return getRawString(contex, "sample_deals_data");
    }

    public static String getRawString(Context context, String resourceName) {
        try {
            InputStream inputStream = context.getResources().openRawResource(context.getResources()
                    .getIdentifier(resourceName, "raw", context.getPackageName()));
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String rawString = new String(buffer);
            return rawString;
        } catch (IOException ex) {
        }
        return null;
    }


    public static void setBadgeCount(Context context, String prefName, int value) {
        Log.i("TRACE", "inside shared pref " + prefName + value);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(prefName, value);
        editor.commit();
    }

    public static int getBadgeCount(Context context, String prefName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Log.i("TRACE", "rt" + prefName);

        return prefs.getInt(prefName, 0);
    }


    public static void saveDefaultDeals(Context context, String value) {

        Log.i("TRACE", "save default deal inside" + value);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("DefaultDeals", value);
        editor.commit();

    }


    public static String getDefaultDeals(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        Log.i("TRACE", "rt" + prefName);

        return prefs.getString("DefaultDeals", null);
    }

    public static void saveDealTime(Context context, String value) {

        Log.i("TRACE", "save default deal inside" + value);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("DealTime", value);
        editor.commit();

    }

    public static String getDealTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);


        return prefs.getString("DealTime", null);
    }


   /* public static void saveDefaultDeals(Context context,String key, String value) {


        Log.i("TRACE", "save default deal inside" + value);
        SharedPreferences defaultdeals = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = defaultdeals.edit();
        editor.putString(key, value);
        editor.commit();

    }
    */

  /*  public static Map<String,?> getDefaultDeals(Context context) {
        SharedPreferences defaultdeals = PreferenceManager.getDefaultSharedPreferences(context);
//        Log.i("TRACE", "rt" + prefName);
        Set<String> set = new HashSet<String>();
       // set = defaultdeals.getAll();
        Map<String, ?> default_deals_map = defaultdeals.getAll();
        return default_deals_map;
    }  */

    public static void setArraylist(Context context, String prefName, String value) {

        ArrayList<String> MutedOKIds = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(prefName, value);
        editor.commit();
    }

    public static boolean saveArray(Context mContext, String arrayName, String[] array) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);
        for (int i = 0; i < array.length; i++)
            editor.putString(arrayName + "_" + i, array[i]);
        return editor.commit();
    }


    public static String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for (int i = 0; i < size; i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    public static void setSharedPreferences(Context context, String prefName, String value) {
        Log.i("TRACE", "inside shared pref " + prefName + value);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(prefName, value);
        editor.commit();
    }


    public static void saveBoolean(Context context, String prefName, boolean value) {
        Log.i("TRACE", "inside shared pref " + prefName + value);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(prefName, value).commit();
    }


    public static Boolean retriveBoolean(Context context, String prefName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getBoolean(prefName, false);
    }


    /*
    public static void saveDefaultDeals(Context context, Set<String> value) {


       Log.i("TRACE", "save default deal inside" + value);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("default_deals", value);
        editor.commit();

    }  */

   /* public static Set<String> getDefaultDeals(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        Log.i("TRACE", "rt" + prefName);

        return prefs.getStringSet("default_deals", null);
    }
    */

    public static String getSharedPreferences(Context context, String prefName) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Log.i("TRACE", "rt" + prefName);


            return prefs.getString(prefName, "");

    }




    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.i("TRACE", "activeNetworkInfo" + activeNetworkInfo);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String currencyFormat(String price) {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        int truncate_first;
        if (currentapiVersion >= 23)
            truncate_first = 2;
        else
            truncate_first = 3;

        int x = Integer.parseInt(price);

        Format format1 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        price = format1.format(x);
        price = price.substring(truncate_first, price.length() - 3);
        price = "₹ " + price;

        return price;
    }


    public static String currencyFormatWithoutRupeeSymbol(String price) {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        int truncate_first;
        if (currentapiVersion >= 23)
            truncate_first = 2;
        else
            truncate_first = 3;

        int x = Integer.parseInt(price);

        Format format1 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        price = format1.format(x);
        price = price.substring(truncate_first, price.length() - 3);
       // price = "₹ " + price;

        return price;
    }

    public static String getDeviceId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }


    public static String timestampToString(long timeStamp){


            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);


    }
    public static String timeStampToString(Long timeStamp){

        int secondsInADay   = 60*60*24;
        int daysSinceEpoch1 = (int) ((System.currentTimeMillis()/1000)/secondsInADay);
        int daysSinceEpoch2 = (int) ((timeStamp/1000)/secondsInADay);


        if( daysSinceEpoch1 == daysSinceEpoch2 )
            return SIMPLE_DATE_FORMAT.format(timeStamp);

        else if((daysSinceEpoch1 - daysSinceEpoch2) == 1)
            return "Yesterday";

        else if( ((daysSinceEpoch1 - daysSinceEpoch2) < 7))
           return "Last week";

        else if( (daysSinceEpoch1 - daysSinceEpoch2) < 30 )
            return "This month";//timestamp2 is a day before timetamp1

        else
            return "Last month";

    }




    public static void publishOye(final Activity context) {
        try {

            if(isNetworkAvailable(context)) {
                slowInternet(context);

            String intend;

            final String ptype;
            final String pstype;
            //String ptype;
            final String price;
            final String speccode;
            final Realm myRealm = realmconfig(context);
            Log.i("TRACE16", "in publishOye");
            //Made gson final to access it in success
            final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(AppConstants.letsOye);


            Log.i("TRACE16", "Get user Id from model " + AppConstants.letsOye.getUserId());

            Log.i("TRACE16", "AppConstants.letsOye from model " + json);
            JSONObject jsonObj = new JSONObject(json);
            intend = jsonObj.getString("req_avl");
                final String tt = jsonObj.getString("tt");
            ptype = jsonObj.getString("property_type");
            pstype = jsonObj.getString("property_subtype");
            price = jsonObj.getString("price");
            speccode = intend.toUpperCase() + "-" + tt + "-" +ptype+ "-" + pstype + "-" + price;
            Log.i("TRACE16", "speccode is" + speccode);
            General.setSharedPreferences(context, "MY_SPEC_CODE", speccode);
            General.getSharedPreferences(context, "MY_SPEC_CODE");
            Log.i("TRACE16", "Spec code got from shared prefs" + General.getSharedPreferences(context, "MY_SPEC_CODE"));
                //set = new HashSet<String>();
            Log.i("TRACE16", "speccode:" + speccode);
            //set = new HashSet<String>(Arrays.asList("speccode"));
            //  Log.i("TRACE","Okid from shared prefs is " +General.getSharedPreferences(context, "OK_ID"));
                Log.i("TRACE16", "is networking available" + General.getSharedPreferences(context, AppConstants.USER_ID));
                //set userId
                AppConstants.letsOye.setUserId(General.getSharedPreferences(context, AppConstants.USER_ID));
                //set gcmId
                AppConstants.letsOye.setGcmId(SharedPrefs.getString(context, SharedPrefs.MY_GCM_ID));

                String timestamp1=General.DateToTimeStamp(General.getSharedPreferences(context,AppConstants.POSSESSION_DATE)).getTime()+"";

                AppConstants.letsOye.setPossession_date(timestamp1);
                AppConstants.letsOye.setBuilding_id("");
                String furnishingStatus="sf",furnishing=General.getSharedPreferences(context,AppConstants.FURNISHING);
                Log.i("TRACE16","oye published r"+General.getSharedPreferences(context,AppConstants.FURNISHING));
                    if (furnishing.equalsIgnoreCase("Fully-Furnished")){
                        furnishingStatus="ff";
                    }else if (furnishing.equalsIgnoreCase("Un-furnished")){
                        furnishingStatus="uf";
                    }else if (furnishing.equalsIgnoreCase("Semi-furnished")){
                        furnishingStatus="sf";
                    }
                Log.i("TRACE16","oye published r 1 "+furnishingStatus);
                AppConstants.letsOye.setFurnishing(furnishingStatus);
                AppConstants.letsOye.setNo_call(General.getSharedPreferences(context,AppConstants.NO_CALL));
                Log.i("TRACE16 NO_CALL", "Get Furnishing from model " + AppConstants.letsOye.getFurnishing());
                Log.i("TRACE16", "Get Possession Date from model " + AppConstants.letsOye.getPossession_date());
                Log.i("TRACE16", "GCM id is" + SharedPrefs.getString(context, SharedPrefs.MY_GCM_ID));
                Log.i("TRACE16", "Get user Id from model " + AppConstants.letsOye.getUserId());
                Log.i("TRACE16", "Get user Id from model " + AppConstants.letsOye.getGcmId());
                Log.i("TRACE16", "Get user Id from model no call " + AppConstants.letsOye.getNo_call() +"  "+General.getSharedPreferences(context,AppConstants.NO_CALL));
                Log.i("TRACE16", "AppConstants.letsOye direct" + AppConstants.letsOye);
                final Gson gsona = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                String jsona = gsona.toJson(AppConstants.letsOye);
                Log.i("TRACE16", "AppConstants.letsOye parsed " + jsona);
                RestAdapter restAdapter = new RestAdapter
                        .Builder()
                        .setEndpoint(AppConstants.SERVER_BASE_URL_11)
                        .setConverter(new GsonConverter(gson))
                        .build();
                restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
                OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);

                oyeokApiService.publishOye(AppConstants.letsOye, new Callback<PublishLetsOye>() {
                    @Override
                    public void success(PublishLetsOye letsOye, Response response) {

                        try {
                            MyApplication application = (MyApplication) context.getApplication();
                            Tracker mTracker = application.getDefaultTracker();

                            mTracker.setScreenName("OyePublished");
                            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                        General.slowInternetFlag = false;
                        General.t.interrupt();

                        Log.i("TRACE16", "in success" + response);
                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                        Log.i("TRACE16", "RETROFIT SUCCESS " + strResponse);

                        Log.i("TRACE16", "Response" + strResponse);

                        //now user is logged in user
                        General.setSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER, "yes");

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                            String msg = "Recently Oyed";


                            Log.i("TRACE16", "in try toast response");

                            JSONObject jsonResponse = new JSONObject(strResponse);
                            JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));
                            Log.i("TRACE16", "Response data" + jsonResponse.getString("responseData"));

                            //change this msg with error code
                            if ("Exhausted your daily limit of Oyes today. Pls try tomorrow".equalsIgnoreCase(jsonResponseData.getString("message"))) {
                                Log.i("TRACE16", "Hello user, " + jsonResponseData.getString("message"));
                              TastyToast.makeText(con,jsonResponseData.getString("message"),TastyToast.LENGTH_LONG,TastyToast.INFO);

                                SnackbarManager.show(
                                        Snackbar.with(con)
                                                .position(Snackbar.SnackbarPosition.TOP)
                                                .text(jsonResponseData.getString("message"))
                                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                                return;
                            } else {
                                OKID = jsonResponseData.getString("ok_id");

                                final String pushmsg = "Have initiated enquiry for a "+AppConstants.letsOye.getFurnishing()+" "+ ptype.substring(0,1).toUpperCase() + ptype.substring(1) + " property (" + pstype + ") by "+AppConstants.letsOye.getPossession_date()+" within budget " + General.currencyFormat(price) + ".";


                                try {

                                    if(myRealm.isInTransaction()) {

                                        myRealm.commitTransaction();
                                    }
                                    DefaultDeals defaultDeals = new DefaultDeals();

                                    defaultDeals.setOk_id(OKID);
                                    //defaultDeals.setOk_id(General.getSharedPreferences(context, "OK_ID"));
                                    defaultDeals.setSpec_code(speccode);
                                    defaultDeals.setLastSeen(String.valueOf(System.currentTimeMillis()));
                                    defaultDeals.setFurnishing(AppConstants.letsOye.getFurnishing());
                                    defaultDeals.setP_type(ptype);
                                    defaultDeals.setPs_type(pstype);
                                    defaultDeals.setPossation_date(AppConstants.letsOye.getPossession_date());
                                    defaultDeals.setBudget(price);

                                    Log.i("TRACE16","locality is thee " +SharedPrefs.getString(context,SharedPrefs.MY_LOCALITY));
                                    defaultDeals.setLocality(SharedPrefs.getString(context,SharedPrefs.MY_LOCALITY));
                                    myRealm.beginTransaction();
                                    DefaultDeals defaultDeals1 = myRealm.copyToRealmOrUpdate(defaultDeals);
                                    Log.i("TRACE16","locality is thee w " +defaultDeals1.getLocality());

                                    myRealm.commitTransaction();
                                }
                                catch(Exception e){
                                    Log.i("TRACE16","Caught in saving default deal "+e.getMessage());
                                }

                                try{
                                    RealmResults<DefaultDeals> results1 =
                                            myRealm.where(DefaultDeals.class).findAll();

                                    for(DefaultDeals r:results1) {
                                        // Log.i(TAG,"insiderro2 ");
                                        Log.i("TRACE16", "insiderrou3 " + r.getOk_id());
                                        Log.i("TRACE16", "insiderrou4 " + r.getSpec_code());
                                        Log.i("TRACE16", "insiderrou5 " + r.getLocality());
                                        Log.i("TRACE16", "insiderrou6 " + r.getLastSeen());
                                        Log.i("TRACE16", "insiderrou7 " + r.getP_type());
                                        Log.i("TRACE16", "insiderrou8 " + r.getPs_type());
                                        Log.i("TRACE16", "insiderrou9 " + r.getPossation_date());
                                        Log.i("TRACE16", "insiderrou10 " + r.getFurnishing());
                                    }

                                }
                                catch(Exception e){

                                }

                                Log.i("TRACE16", "open intent deal listing "+OKID);
                                //open deals listing
                                if(tt.equalsIgnoreCase("LL")) {
                                    try {
                                        General.setBadgeCount(getApplicationContext(), AppConstants.RENTAL_COUNT, General.getBadgeCount(getApplicationContext(), AppConstants.RENTAL_COUNT) + 1);
                                    } catch (NumberFormatException e) {
                                        General.setBadgeCount(getApplicationContext(), AppConstants.RENTAL_COUNT, 1);

                                        e.printStackTrace();
                                    }
                                } else if(tt.equalsIgnoreCase("OR")) {
                                    try {
                                        General.setBadgeCount(getApplicationContext(), AppConstants.RESALE_COUNT, General.getBadgeCount(getApplicationContext(), AppConstants.RESALE_COUNT) + 1);
                                    } catch (NumberFormatException e) {
                                        General.setBadgeCount(getApplicationContext(), AppConstants.RESALE_COUNT, 1);

                                    }

                                }AppConstants.CLIENT_DEAL_FLAG = true;
                                Intent openDealsListing = new Intent(context, ClientDealsListActivity.class);
                                openDealsListing.putExtra("oyeok "+AppConstants.OK_ID, OKID);


                                openDealsListing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(openDealsListing);
//sushil changed start
//                                Intent intent = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);
//                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//end
                                AppConstants.letsOye.setTime(formattedDate);

                                storeDealTime(OKID,context);
                                TastyToast.makeText(context, jsonResponseData.getString("message"), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
//                                
try{
    General.setSharedPreferences(context,AppConstants.REFERING_ACTIVITY_LOG_ID,AppConstants.MASTER_ACTIVITY_LOG_ID);
    if(General.getSharedPreferences(context,AppConstants.REFERING_ACTIVITY_LOG_ID) != "") {
        Map message = new HashMap();
        String name;
        if (General.getSharedPreferences(context, AppConstants.USER_ID) == null || General.getSharedPreferences(context, AppConstants.USER_ID) == "")
            name = "Client";
        else
            name = General.getSharedPreferences(context, AppConstants.NAME);

        final String finalName = name;
        message.put("pn_gcm", new HashMap() {{
            put("data", new HashMap() {{
                put("message", pushmsg);
                put("_from", getSharedPreferences(context, AppConstants.USER_ID));
                put("to", AppConstants.MASTER_ACTIVITY_LOG_ID);
                put("name", finalName);
                put("status", "LOG_OYE");
            }});
        }});
        message.put("pn_apns", new HashMap() {{
            put("aps", new HashMap() {{
                put("alert", pushmsg);
                put("from", getSharedPreferences(context, AppConstants.USER_ID));
                put("to", AppConstants.MASTER_ACTIVITY_LOG_ID);
                put("name", finalName);
                put("status", "LOG_OYE");
            }});
        }});

        String channel = "global_log_" + General.getSharedPreferences(context, AppConstants.REFERING_ACTIVITY_LOG_ID);
        Log.i(TAG,"channel channel channel "+channel);
        pushMessage(context, channel, message);
    }
}
catch(Exception e){}

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("TRACE16","Caught in lets oye "+e.getMessage());
                        }


                        //to do: use Appconstants.OKID replace all dependencies

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        try {
                            SnackbarManager.show(
                                    Snackbar.with(context)
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .text("Server Error: " + error.getMessage())
                                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                        }
                        catch(Exception e){}


                    }
                });
            }
            else{
                General.internetConnectivityMsg(context);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("oye","Caught in exception in oye publish "+e.getMessage());
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
//        String networkStr = NetworkUtils.g
        Log.d("TRACE", "action: "
                + intent.getAction());
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
//        networkInfo.NetworkStatusChanged("esfrghatheth===");


   /*     if(haveConnectedMobile) {
            Log.i("TRACE", "MOBILE NETWORK AVAILABLE ");
//            Toast.makeText(context,"NETWORK AVAILABLE",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"MOBILE NETWORK NOT AVAILABLE",Toast.LENGTH_LONG).show();
            Log.i("TRACE", "MOBILE NETWORK NOT AVAILABLE ");
//            SnackbarManager.show(
//                    Snackbar.with(context)
//                            .position(Snackbar.SnackbarPosition.BOTTOM)
//                            .text("MOBILE NETWORK NOT AVAILABLE")
//                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


        }

        if(haveConnectedWifi)
            Log.i("TRACE","WIFI NETWORK AVAILABLE ");
        else {
            Toast.makeText(context,"WIFI NETWORK NOT AVAILABLE",Toast.LENGTH_LONG).show();
            Log.i("TRACE", "WIFI NETWORK NOT AVAILABLE ");
            //Couldn't get Activity from the Snackbar's Context. Try calling #show(Snackbar, Activity) instead
//            SnackbarManager.show(
//                    Snackbar.with(context)
//                            .position(Snackbar.SnackbarPosition.BOTTOM)
//                            .text("WIFI NETWORK NOT AVAILABLE")
//                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

        }

        */

        if (!(haveConnectedMobile) && !(haveConnectedWifi)) {


            Intent i = new Intent(AppConstants.NETWORK_CONNECTIVITY);
            i.putExtra("tv_dealinfo","noConnectivity");
            LocalBroadcastManager.getInstance(context).sendBroadcast(i);
       /* SnackbarManager.show(
                    Snackbar.with(context)
                            .position(Snackbar.SnackbarPosition.BOTTOM)
                            .text("INTERNET CONNECTIVITY NOT AVAILABLE")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));*/

        }
    }


    public static void storeDealTime(String okId, Context context){
        String dealTime;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        dealTime = General.getDealTime(context);

        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> dealTime1 = gson.fromJson(dealTime, type);
        if (dealTime1 == null) {
            dealTime1 = new HashMap<String, String>();

        }
        dealTime1.put(okId,String.valueOf(System.currentTimeMillis()));

        Gson g = new Gson();
        String hashMapString = g.toJson(dealTime1);
        General.saveDealTime(context, hashMapString);
        Log.i("dealtime","DealTime "+General.getDealTime(context));



    }

    public static PopupWindow showOptions(final Context mcon, final Bitmap b, final String desc, final String heading){
        Log.i(TAG,"porter 13 "+b);
        Log.i(TAG,"porter 14 "+mcon);
        try{
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            Log.i(TAG,"popup window shown 20 "+width+" "+height);
            LayoutInflater inflater = (LayoutInflater) mcon.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.card1,null);

           // CustomPhasedSeekBar mPhasedSeekBar = (CustomPhasedSeekBar) layout.findViewById(R.id.phasedSeekBar1);
            Button button =(Button) layout.findViewById(R.id.button);
            TextView description =(TextView) layout.findViewById(R.id.desc);
            button.setText(heading);
            description.setText(desc);
            img = (ImageView) layout.findViewById(R.id.imageView6);
            img.setImageBitmap(b);
           /* try {
                if (Environment.getExternalStorageState() == null) {
                    //create new file directory object

                    File file = new File(Environment.getDataDirectory()+"/oyeok/promo.png"); //your image file path
                    Uri uri = Uri.fromFile(file);
                    img.setImageURI(uri);
//
                    // if phone DOES have sd card
                }
                else if (Environment.getExternalStorageState() != null) {
                    // search for directory on SD card


                    File file = new File(Environment.getExternalStorageDirectory()+"/oyeok/promo.png"); //your image file path
                    Uri uri = Uri.fromFile(file);
                    img.setImageURI(uri);


                }

            }catch(Exception e){
                Log.i("chatlistadapter", "Caught in exception saving image recievers /oyeok "+e);
            }*/




           // new DownloadImageTask(img)
                  //  .execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");


            //new DownloadImageTask(img).execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");




           /* DBHelper dbHelper = new DBHelper(mcon);
       */   /*  if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
                mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(mcon.getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{mcon.getResources().getString(R.string.Rental), mcon.getResources().getString(R.string.Resale)}));
            else
                mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(mcon.getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.real_estate_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Audit", "Auction"}));
           // mPhasedSeekBar.setListener(this);*/
            final PopupWindow optionspu1 = greyOut(mcon);
            //final PopupWindow optionspu = new PopupWindow(layout, 600,1000, true);
            final PopupWindow optionspu = new PopupWindow(layout);
            optionspu.setWidth(width-180);
            optionspu.setHeight(height-180);
            optionspu.setAnimationStyle(R.style.AnimationPopup);

            /*optionspu.setTouchable(true);
            optionspu.setOutsideTouchable(false);*/

            optionspu.setFocusable(false);
            optionspu.setTouchable(true);
            optionspu.setOutsideTouchable(false);
           // optionspu.showAtLocation(layout, Gravity.CENTER, 0, 0);
           optionspu.showAtLocation(layout, Gravity.TOP, 0, 100);


            //optionspu.update(0, 0,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //optionspu.setAnimationStyle(R.anim.bounce);
            Log.i(TAG,"popup window shown 3 ");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"popup window shown 13 ");
                    optionspu1.dismiss();
                    optionspu.dismiss();
                }
            });

            General.setSharedPreferences(mcon,AppConstants.PROMO_IMAGE_URL,"");
            AppConstants.cardNotif = true;
            AppConstants.optionspu = optionspu;
            return optionspu;

        }
        catch (Exception e){e.printStackTrace();
            AppConstants.cardNotif = false;
            Log.i(TAG,"popup window shown 4 "+e);
            return null;}


    }

    public static PopupWindow greyOut(final Context mcon){
        Log.i(TAG,"popup window shown 2 ");
        try{
            LayoutInflater inflater = (LayoutInflater) mcon.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.grey_out_popup,null);
            final PopupWindow optionspu1 = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT, true);
            optionspu1.setFocusable(false);
            optionspu1.setTouchable(true);
            optionspu1.setOutsideTouchable(false);
            optionspu1.showAtLocation(layout, Gravity.CENTER, 0, 0);
            AppConstants.optionspu1 = optionspu1;
            return optionspu1;
        }
        catch (Exception e){e.printStackTrace();
            Log.i(TAG,"popup window shown 4 "+e);
            return null;}


    }




    /*private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }*/



    public static void setDealStatus(Context c,String dealStatus, String okId, String lastseen,String blockBy){



        UpdateStatus updateStatus = new UpdateStatus();



        updateStatus.setOkId(okId);
        updateStatus.setStatus(dealStatus);
        updateStatus.setLast_seen(lastseen);
        updateStatus.setBlocked_by(blockBy);
        Log.i("updateStatus ","user_id "+General.getSharedPreferences(c,AppConstants.USER_ID)+" "+okId+" "+dealStatus+" "+lastseen+" "+blockBy);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppConstants.SERVER_BASE_URL)
                .build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


        try {
            oyeokApiService.updateStatus(updateStatus, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    Log.i("updateStatus CALLED","updateStatus success ");



                    JsonObject k = jsonElement.getAsJsonObject();
                    try {

                        Log.i("updateStatus","updateStatus success response "+response);


                        JSONObject ne = new JSONObject(k.toString());
                        Log.i("updateStatus","updateStatus success ne "+ne);


                    }
                    catch (JSONException e) {
                        Log.e("TAG", e.getMessage());
                        Log.i("updateStatus CALLED","updateStatus Failed "+e.getMessage());
                    }




                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("BROKER BUILDINGS CALLED","updateStatus failed "+error);
                    try {
                        SnackbarManager.show(
                                Snackbar.with(con)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text("Server Error: " + error.getMessage())
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                    }
                    catch(Exception e){}

                }
            });


        }
        catch (Exception e){
            Log.e("TAG", e.getMessage());
        }

    }

    /*@Override
    public void onPositionSelected(int position, int count) {

    }*/





    public static Bitmap getBitmapFromURL(String src) {

        Log.i("chatlistadapter", "promot called");
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.i("chatlistadapter", "promot 22 "+myBitmap);
           // saveImageLocally("promo",myBitmap);
            return myBitmap;

        } catch (Exception e) {
            Log.i("chatlistadapter", "Caught in exception saving image recievers /oyeok 8 "+e);
            return null;
        }
    }

    public static void saveImageLocally(String imageName, Bitmap result){
        Log.i("chatlistadapter", "promot called 2");
        try {
            if (Environment.getExternalStorageState() == null) {
                //create new file directory object
                File directory = new File(Environment.getDataDirectory()
                        + "/oyeok/");

                // if no directory exists, create new directory
                if (!directory.exists()) {
                    directory.mkdir();
                }

                OutputStream stream = new FileOutputStream(Environment.getDataDirectory() + "/oyeok/"+imageName+".png");
                result.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
                Log.i("TAG","result compressed"+result);
//
                // if phone DOES have sd card
            }
            else if (Environment.getExternalStorageState() != null) {
                // search for directory on SD card
                File directory = new File(Environment.getExternalStorageDirectory()
                        + "/oyeok/");


                if (!directory.exists()) {
                    directory.mkdir();
                }

                OutputStream stream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/oyeok/"+imageName+".png");
                result.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
                Log.i("TAG","result compressed"+result);

            }

        }catch(Exception e){
            Log.i("chatlistadapter", "Caught in exception saving image recievers /oyeok 1 "+e);
        }


    }



    public static int getFormatedPrice(String conf,int rate){
        Log.i("conf case","conf  : "+conf+"  "+rate);
        int price=rate*950;
        switch(conf) {
            case "1rk":
                price = rate * 300;
                break;
            case "1bhk":
                price = rate * 600;
                break;
            case "1.5bhk":
                price = rate * 800;
                break;
            case "2bhk":
                price = rate * 950;
                break;
            case "2.5bhk":
                price = rate * 1300;
                break;
            case "3bhk":
                price = rate * 1600;
                break;
            case "3.5bhk":
                price = rate * 1800;
                break;
            case "4bhk":
                price = rate * 2100;
                break;
            case "4.5bhk":
                price = rate * 2300;
                break;
            case "5bhk":
                price = rate * 2500;
                break;
            case "5.5bhk":
                price = rate * 2700;
                break;
            case "6bhk":
                price = rate * 2900;
                break;
        }
        /*price=price/1000;
        price=price*1000;*/
        return price;
    }

    public static int getFormatedarea(String conf){
//        Log.i("conf case","conf  : "+conf+"  "+rate);
        int price=0;//=rate*950;
        switch(conf.toLowerCase()) {
            case "1rk":
                price =  300;
                break;
            case "1bhk":
                price = 600;
                break;
            case "1.5bhk":
                price =  800;
                break;
            case "2bhk":
                price = 950;
                break;
            case "2.5bhk":
                price = 1300;
                break;
            case "3bhk":
                price = 1600;
                break;
            case "3.5bhk":
                price = 1800;
                break;
            case "4bhk":
                price = 2100;
                break;
            case "4.5bhk":
                price = 2300;
                break;
            case "5bhk":
                price = 2500;
                break;
            case "5.5bhk":
                price = 2700;
                break;
            case "6bhk":
                price = 2900;
                break;
        }
        /*price=price/500;
        price=price*500;*/
        return price;
    }


   public static  String numToVal(int no){
        String str = "",v = "";
        int twoWord = 0,val = 1;
        int c = (no == 0 ? 1 : (int)(log10(no)+1));
        if (c > 8) {
            c = 8;
        }
        if (c%2 == 1){
            c--;
        }
        c--;
        switch(c)
        {
            case 7:
                val = no/10000000;
                no = no%10000000;
                String formatted = String.format("%06d", no);
                formatted = formatted.substring(0,1);
                v = val+"."+formatted;
                str = v+" cr";
                twoWord++;
                break;
            case 5:
                val = no/100000;
                v = val+"";
                no = no%100000;
                String s2 = String.format("%04d", no);
                s2 = s2.substring(0,1);

                if (val != 0){
                    str = str+v+"."+s2+" lacs";
                    twoWord++;
                }
                break;
            case 3:
                /*val = no/1000;
                v = val+"";
                no = no%1000;
                String.format("%05d", no);
                String s3 = String.format("%03d", no);
                s3 = s3.substring(0,1);
                if (val != 0) {*/
                    //str = str+v+"."+s3+" k";
                    str=General.currencyFormatWithoutRupeeSymbol(no+"");
                //}
                break;
            default :
                break;
        }
        return str;
    }



public static void pushMessage(Context c,String channel, Map message){

    try {
        PubNub pubnub = initPubnub(c,getSharedPreferences(c,AppConstants.USER_ID));
        Log.i(TAG,"Message is "+message);

        pubnub.publish()
                .message(message)
                .shouldStore(true)
                .usePOST(true)
                .channel(channel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        try {
                            if (status.isError()) {
                                System.out.println("publish failed!");
                                Log.i(TAG,"PUBNUB publish "+status.getStatusCode());
                            } else {
                                System.out.println("push notification worked!");
                                System.out.println(result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    } catch (Exception e) {
        e.printStackTrace();
    }


}

    public static void shareReferralLink(final Context context) {
        String user_id = General.getSharedPreferences(context, AppConstants.USER_ID);

        Branch branch = Branch.getInstance(getApplicationContext());

        String mob_no = General.getSharedPreferences(context, AppConstants.MOBILE_NUMBER);
        Log.i("mob_no", "mob_no " + mob_no);

        branch.setIdentity(mob_no);

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                // The identifier is what Branch will use to de-dupe the content across many different Universal Objects
                .setTitle("OYEOK")
                .setContentDescription("Get property at right price. ")
                .setCanonicalIdentifier(mob_no);


        LinkProperties linkProperties = new LinkProperties()
                .setChannel("android")
                .setFeature("share")
                .addControlParameter("user_name", user_id)
                .addControlParameter("$android_url", AppConstants.GOOGLE_PLAY_STORE_APP_URL)
                .addControlParameter("$always_deeplink", "true");


        branchUniversalObject.generateShortUrl(getApplicationContext(), linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("MyApp", "got my Branch link to share: " + url);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Hey check this out!");
                    context.startActivity(Intent.createChooser(intent, "Share link via"));
                }
            }
        });
    }



    public static String  getMapsApiDirectionsUrl(LatLng origin, LatLng dest) {
        Log.i(TAG, "route drawer 1");
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;

    }





   public static Date DateToTimeStamp(String date1) throws ParseException {
       String str_date=date1;
       DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//       Date date = (Date)formatter.parse(str_date);
       Date date = (Date)formatter.parse(str_date);
       System.out.println("Today is " +date.getTime());

       return date;
   }



    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(time);
        Log.i("dateformat","formated date ");
        return date;
    }




  public static String percentageCalculator(int selectedRate,int MarketRate){
        int Percentage;
        int diff;
        String str="";


        // Log.i("percent","selectedRate : "+selectedRate+"MarketRate : "+MarketRate+"Percentage : "+Percentage);
        if(selectedRate>MarketRate){
            str="+";
            diff=selectedRate-MarketRate;

        }else{
            str="-";
            diff=MarketRate-selectedRate;
        }

      Percentage=Math.abs((diff*100)/MarketRate);
        Log.i("percent","selectedRate : "+selectedRate+"MarketRate : "+MarketRate+"Percentage : "+Percentage+"str : "+str);
        return str+Percentage+"";
    }














}

   // private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
   /* private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (broker_map != null) {
            // Access to the location has been granted to the app.
            broker_map.setMyLocationEnabled(true);
        }
    }*/