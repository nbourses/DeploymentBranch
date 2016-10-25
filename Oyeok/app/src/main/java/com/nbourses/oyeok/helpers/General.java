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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.UpdateStatus;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.DealConversationActivity;
import com.nbourses.oyeok.fragments.GPSTracker;
import com.nbourses.oyeok.models.PublishLetsOye;
import com.nbourses.oyeok.realmModels.DefaultDeals;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

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
    private static Bitmap mIcon12 = null; // null image for test condition in download image
    private static ImageView img;



    public static void showPermissionDialog(Context context,Activity activity) {
        GPSTracker gps = new GPSTracker(context);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {



            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    AppConstants.LOCATION_PERMISSION_REQUEST_CODE);

        } else {
            if (gps.canGetLocation())
                Log.i("TAG","ralph "+gps.getLatitude());
            saveLatLongLoc(context, gps.getLatitude(), gps.getLongitude());
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
        Log.i(TAG,"pokemon");
            con = context;

            t = new Thread(new Runnable() {
                public void run() {
                    slowInternetFlag = true;
                    Log.i(TAG,"pokemon 1 "+((SystemClock.uptimeMillis() - startTime) / 1000.0));
while(slowInternetFlag) {
    if (((SystemClock.uptimeMillis() - startTime) / 1000.0) > AppConstants.slowInternet) {
        Log.i(TAG, "pokemon Slow net connection ,Please move to better connectivity area.");
        SnackbarManager.show(
                Snackbar.with(con)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .text("Slow net connection ,Please move to better connectivity area.")
                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
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

    public static void publishOye(final Context context) {
        try {

            if(isNetworkAvailable(context)) {
                slowInternet(context);

            String intend;
            String tt;
            String ptype;
            String pstype;
            //String ptype;
            String price;
            final String speccode;
            final Realm myRealm = realmconfig(context);
            Log.i("TRACE", "in publishOye");
            //Made gson final to access it in success
            final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(AppConstants.letsOye);

            Log.d(TAG, "AppConstants.letsOye " + json);
            Log.i("TRACE", "Get user Id from model " + AppConstants.letsOye.getUserId());

            Log.i("TRACE", "AppConstants.letsOye from model " + json);

            JSONObject jsonObj = new JSONObject(json);

            intend = jsonObj.getString("req_avl");

            tt = jsonObj.getString("tt");
            ptype = jsonObj.getString("property_type");
            pstype = jsonObj.getString("property_subtype");
            price = jsonObj.getString("price");
            speccode = intend.toUpperCase() + "-" + tt + "-" +ptype+ "-" + pstype + "-" + price;

            Log.i("TRACE", "speccode is" + speccode);


            General.setSharedPreferences(context, "MY_SPEC_CODE", speccode);
            General.getSharedPreferences(context, "MY_SPEC_CODE");
            Log.i("TRACE", "Spec code got from shared prefs" + General.getSharedPreferences(context, "MY_SPEC_CODE"));



            //set = new HashSet<String>();
            Log.i("TRACE", "speccode:" + speccode);
            //set = new HashSet<String>(Arrays.asList("speccode"));


            //  Log.i("TRACE","Okid from shared prefs is " +General.getSharedPreferences(context, "OK_ID"));


                Log.i("TRACE", "is networking available" + General.getSharedPreferences(context, AppConstants.USER_ID));

                //set userId
                AppConstants.letsOye.setUserId(General.getSharedPreferences(context, AppConstants.USER_ID));
                //set gcmId
                AppConstants.letsOye.setGcmId(SharedPrefs.getString(context, SharedPrefs.MY_GCM_ID));
                AppConstants.letsOye.setPossession_date(General.getSharedPreferences(context,AppConstants.POSSESSION_DATE));

                String furnishingStatus="sf",furnishing=General.getSharedPreferences(context,AppConstants.FURNISHING);
                    if (furnishing.equalsIgnoreCase("Fully-Furnished")){
                        furnishingStatus="ff";
                    }else if (furnishing.equalsIgnoreCase("Un-furnished")){
                        furnishingStatus="uf";
                    }else if (furnishing.equalsIgnoreCase("Semi-furnished")){
                        furnishingStatus="sf";
                    }
                AppConstants.letsOye.setFurnishing(furnishingStatus);

                AppConstants.letsOye.setNo_call(General.getSharedPreferences(context,AppConstants.NO_CALL));
                Log.i("NO_CALL", "Get Furnishing from model " + General.getSharedPreferences(context,AppConstants.NO_CALL));
                Log.i("TRACE", "Get Possession Date from model " + AppConstants.letsOye.getPossession_date());
                Log.i("TRACE", "GCM id is" + SharedPrefs.getString(context, SharedPrefs.MY_GCM_ID));
                Log.i("TRACE", "Get user Id from model " + AppConstants.letsOye.getUserId());
                Log.i("TRACE", "Get user Id from model " + AppConstants.letsOye.getGcmId());
                Log.i("TRACE", "AppConstants.letsOye direct" + AppConstants.letsOye);
                final Gson gsona = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                String jsona = gsona.toJson(AppConstants.letsOye);

                Log.i(TAG, "AppConstants.letsOye parsed " + jsona);
                RestAdapter restAdapter = new RestAdapter
                        .Builder()
                        .setEndpoint(AppConstants.SERVER_BASE_URL)
                        .setConverter(new GsonConverter(gson))
                        .build();

                restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
                OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
                oyeokApiService.publishOye(AppConstants.letsOye, new Callback<PublishLetsOye>() {
                    @Override
                    public void success(PublishLetsOye letsOye, Response response) {

                        General.slowInternetFlag = false;
                        General.t.interrupt();

                        Log.i("TRACE", "in success" + response);
                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                        Log.e(TAG, "RETROFIT SUCCESS " + strResponse);

                        Log.i("TRACE", "Response" + strResponse);

                        //now user is logged in user
                        General.setSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER, "yes");

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());

                        try {
                            Log.i("TRACE", "in try toast response");

                            JSONObject jsonResponse = new JSONObject(strResponse);
                            JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));
                            Log.i("TRACE", "Response data" + jsonResponse.getString("responseData"));

                            //change this msg with error code
                            if ("Exhausted your daily limit of Oyes today. Pls try tomorrow".equals(jsonResponseData.getString("message"))) {
                                Log.i("TRACE", "Hello user, " + jsonResponseData.getString("message"));

                                Toast.makeText(context, "" + jsonResponseData.getString("message"), Toast.LENGTH_LONG).show();
                                SnackbarManager.show(
                                        Snackbar.with(context)
                                                .position(Snackbar.SnackbarPosition.TOP)
                                                .text("Your old oye with same specs: " + jsonResponseData.getString("message"))
                                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                            } else {
                                Log.i("TRACE", "Ok id from response is " + jsonResponseData.getString("ok_id"));
                                Log.i("TRACE", "step2");
                                General.setSharedPreferences(context, "OK_ID", jsonResponseData.getString("ok_id"));
                                Log.i("TRACE", "json response data" + jsonResponseData);
                                Log.i("TRACE", "json response data message" + jsonResponseData.getString("message"));

                                TastyToast.makeText(context, jsonResponseData.getString("message"), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                //Toast.makeText(context, "" + jsonResponseData.getString("message"), Toast.LENGTH_LONG).show();
//                                SnackbarManager.show(
//                                        Snackbar.with(context)
//                                                .position(Snackbar.SnackbarPosition.TOP)
//                                                .text("Your old oye with same specs: " + jsonResponseData.getString("message"))
//                                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


                                //  if (jsonResponseData.getInt("code") == 1) {
                                AppConstants.letsOye.setTime(formattedDate);

//                                AppConstants.letsOye.save();

                                //  }

                                // Create default deal here after letsoye success

                                String speccode;
                                speccode = General.getSharedPreferences(context, "MY_SPEC_CODE");
                                String deals;
                                deals = getDefaultDeals(context);
                                java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                                }.getType();
                                HashMap<String, String> deals1 = gson.fromJson(deals, type);

                                Log.i("TRACE", "hashmap:" + deals1);

                                if (deals1 == null) {
                                    deals1 = new HashMap<String, String>();

                                }

                                // HashMap<String, String> hashMap = new HashMap<String, String>();
                                Log.i("TRACE", "hashmap entry" + General.getSharedPreferences(context, "OK_ID"));

                                storeDealTime(jsonResponseData.getString("ok_id"),context);


                                deals1.put(General.getSharedPreferences(context, "OK_ID"), speccode);
                                // Log.i("TRACE", "hashmap" + deals1);

//                                Log.i("TRACE", "step1");

                                //convert to string using gson
                                Gson g = new Gson();
                                String hashMapString = g.toJson(deals1);
                                Log.i("TRACE", "hashmapstring" + hashMapString);

                                saveDefaultDeals(context, hashMapString);
                                Log.i("TRACE", "Saved");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        //to do: use Appconstants.OKID replace all dependencies
                        try {

                            if(myRealm.isInTransaction()) {
                                Log.i("HOTFIX","is in transaction saving default deal");

                                myRealm.commitTransaction();
                            }
                            DefaultDeals defaultDeals = new DefaultDeals();
                            defaultDeals.setOk_id(General.getSharedPreferences(context, "OK_ID"));
                            defaultDeals.setSpec_code(speccode);
                            defaultDeals.setLastSeen(String.valueOf(System.currentTimeMillis()));
                            Log.i("locality is thee ","locality is thee " +SharedPrefs.getString(context,SharedPrefs.MY_LOCALITY));
                            defaultDeals.setLocality(SharedPrefs.getString(context,SharedPrefs.MY_LOCALITY));
                            myRealm.beginTransaction();
                            DefaultDeals defaultDeals1 = myRealm.copyToRealmOrUpdate(defaultDeals);
                            Log.i("locality is thee ","locality is thee w " +defaultDeals1.getLocality());

                            myRealm.commitTransaction();
                        }
                        catch(Exception e){
                            Log.i("Exception","Caught in saving default deal "+e);
                        }

                        try{
                            RealmResults<DefaultDeals> results1 =
                                    myRealm.where(DefaultDeals.class).findAll();

                            for(DefaultDeals r:results1) {
                                // Log.i(TAG,"insiderro2 ");
                                Log.i(TAG, "insiderrou3 " + r.getOk_id());
                                Log.i(TAG, "insiderrou4 " + r.getSpec_code());
                                Log.i(TAG, "insiderrou5 " + r.getLocality());
                                Log.i(TAG, "insiderrou6 " + r.getLastSeen());
                            }

                        }
                        catch(Exception e){

                        }

                        Log.i("TRACE", "open intent deal listing");
                        //open deals listing
                        AppConstants.CLIENT_DEAL_FLAG = true;
                        Intent openDealsListing = new Intent(context, DealConversationActivity.class);
                        //openDealsListing.putExtra("default_deal_flag", true);

                        openDealsListing.putExtra("userRole", "client");
                        openDealsListing.putExtra("Oyed", "yes");
                        openDealsListing.putExtra(AppConstants.SPEC_CODE, speccode);
                        openDealsListing.putExtra(AppConstants.OK_ID, General.getSharedPreferences(context, "OK_ID"));
                        openDealsListing.putExtra("isDefaultDeal",true);

                        openDealsListing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(openDealsListing);

                        Intent intent = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        Log.e(TAG, "error " + error.getMessage());
                        if(error.getMessage().equalsIgnoreCase("timeout")){

                            SnackbarManager.show(
                    Snackbar.with(context)
                            .position(Snackbar.SnackbarPosition.BOTTOM)
                            .text("Time out, please move to better connectivity area.")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

                        }

                        Log.i("TRACE", "RetrofitError");
                    }
                });
            }
            else{
                General.internetConnectivityMsg(context);

            }
        } catch (Exception e) {
            e.printStackTrace();
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