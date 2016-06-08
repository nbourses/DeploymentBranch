package com.nbourses.oyeok.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.ClientDealsListActivity;
import com.nbourses.oyeok.models.PublishLetsOye;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

/**
 * Created by rohit on 09/02/16.
 */
public class General extends BroadcastReceiver{

    public static HashMap<String, String> oyeDataHolder = new HashMap<String, String>();
    public static final String TAG = "General";
    public static Set<String> set;
    private NetworkInterface networkInfo;



  //  private static Set<String> defaultDeals ;

//    public General(NetworkInterface networkInfo)
//    {
//        this.networkInfo = networkInfo;
//    }
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
        Log.i("TRACE","inside shared pref "+prefName +value);
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

        ArrayList<String> MutedOKIds= new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(prefName, value);
        editor.commit();
    }

    public static boolean saveArray(Context mContext, String arrayName, String[] array ) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString(arrayName + "_" + i, array[i]);
        return editor.commit();
    }



    public static String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    public static void setSharedPreferences(Context context, String prefName, String value) {
        Log.i("TRACE","inside shared pref "+prefName +value);
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
        Log.i("TRACE","activeNetworkInfo" +activeNetworkInfo);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public static String currencyFormat(String price){

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        int truncate_first;
        if(currentapiVersion>=23)
            truncate_first = 2;
        else
            truncate_first = 3;

        int x =Integer.parseInt(price);

        Format format1 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        price=format1.format(x);
        price = price.substring(truncate_first,price.length()-3);
        price ="₹ "+price;

        return price;
    }

    public static String getDeviceId(Context context){
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }

    public static void publishOye(final Context context) {
        try {
            String tt;
            String pstype;
            String price;
            final String speccode;
            Log.i("TRACE", "in publishOye");
            //Made gson final to access it in success
            final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(AppConstants.letsOye);

            Log.d(TAG, "AppConstants.letsOye "+json);
            Log.i("TRACE","Get user Id from model "+AppConstants.letsOye.getUserId());

            Log.i("TRACE", "AppConstants.letsOye from model " + json);

            JSONObject jsonObj = new JSONObject(json);
            tt = jsonObj.getString("tt");
            pstype = jsonObj.getString("property_subtype");
            price = jsonObj.getString("price");
            speccode = tt + "-" + pstype + "-" + price;
            Log.i("TRACE", "speccode is" + speccode);

            General.setSharedPreferences(context, "MY_SPEC_CODE", speccode);
            General.getSharedPreferences(context, "MY_SPEC_CODE");
            Log.i("TRACE", "Spec code got from shared prefs" + General.getSharedPreferences(context, "MY_SPEC_CODE"));

           // String[] array;                   // <--declared statement
//            String[] array = new String[100];
//            Log.i("TRACE", "zala be");
//            saveArray(array, speccode, context);
//            Log.i("TRACE", "zala be1");
//
//            Log.i("TRACE", "zala be2" + loadArray(speccode,context));
             //Set<String> set;

       // Set defaultDeals = new HashSet<>();
         //   private List<Issue> issues = new ArrayList<>();
//            String elements[] = {"a"};
   ////           Set<String> set = new HashSet();
//            Set<String> set = new HashSet<>(Arrays.asList(elements));
      //        Log.i("TRACE", "abc");
    //          set = getDefaultDeals(context);
//            Log.i("TRACE", "zala be1");
//          //  Log.i("TRACE", "Set is:-" + set);
     //       set.add(speccode);
     //    Log.i("TRACE", "efg");
//            Log.i("TRACE", "set is:" + speccode);
     //      saveDefaultDeals(context, set);
     //       Log.i("TRACE", "Saved");
//            Log.i("TRACE", "Get default deal" + defaultDeals);
//            if(speccode != null) {
//                set.add(speccode);
//                Log.i("TRACE", "Get default deal" + getDefaultDeals(context));
//                saveDefaultDeals(context, set);
//            }
//            else {
//
//                saveDefaultDeals(context, );
//            }




            //set = new HashSet<String>();
            Log.i("TRACE", "speccode:" + speccode);
            //set = new HashSet<String>(Arrays.asList("speccode"));

//            set = new HashSet<String>(Arrays.asList(new String[]{
//                    "a", "b"
//            }));
    /*        Log.i("TRACE", "efg" + set);
            Log.i("TRACE", "abc ");

          set = getDefaultDeals(context);

            if(set == null)
            {
                set = new HashSet<String>();
            }

            Log.i("TRACE", "efg" + set);
            set.add(speccode);
            Log.i("TRACE", "efg");

            saveDefaultDeals(context, set);
            */






          //  Log.i("TRACE","Okid from shared prefs is " +General.getSharedPreferences(context, "OK_ID"));

            if (isNetworkAvailable(context)) {
                Log.i("TRACE", "is networking available" + General.getSharedPreferences(context, AppConstants.USER_ID));

                //set userId
                AppConstants.letsOye.setUserId(General.getSharedPreferences(context, AppConstants.USER_ID));
                //set gcmId
                AppConstants.letsOye.setGcmId(SharedPrefs.getString(context, SharedPrefs.MY_GCM_ID));

                Log.i("TRACE", "GCM id is" + SharedPrefs.getString(context, SharedPrefs.MY_GCM_ID));

                Log.i("TRACE","Get user Id from model "+AppConstants.letsOye.getUserId());
                Log.i("TRACE","Get user Id from model "+AppConstants.letsOye.getGcmId());


                Log.i("TRACE", "AppConstants.letsOye direct" + AppConstants.letsOye);
                final Gson gsona = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                String jsona = gsona.toJson(AppConstants.letsOye);

                Log.i(TAG, "AppConstants.letsOye parsed "+jsona);
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
                        Log.i("TRACE", "in success"+response);
                        String strResponse =  new String(((TypedByteArray)response.getBody()).getBytes());
                        Log.e(TAG, "RETROFIT SUCCESS " + strResponse);

                        Log.i("TRACE", "Response" + strResponse);

                        //
                        //

                        //now user is logged in user
                        General.setSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER, "yes");

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());

                        try {
                            Log.i("TRACE", "in try toast response");

                            JSONObject jsonResponse = new JSONObject(strResponse);
                            JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));
                            Log.i("TRACE", "Response data"+jsonResponse.getString("responseData"));
                            if("Exhausted your daily limit of Oyes today. Pls try tomorrow".equals(jsonResponseData.getString("message"))){
                                Log.i("TRACE", "Hello user, "+jsonResponseData.getString("message"));

                                Toast.makeText(context, ""+jsonResponseData.getString("message"), Toast.LENGTH_LONG).show();
                                SnackbarManager.show(
                                        Snackbar.with(context)
                                                .position(Snackbar.SnackbarPosition.TOP)
                                                .text("Your old oye with same specs: " +jsonResponseData.getString("message"))
                                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                            }
                            else {
                                Log.i("TRACE", "Ok id from response is " + jsonResponseData.getString("ok_id"));
                                Log.i("TRACE", "step2");
                                General.setSharedPreferences(context, "OK_ID", jsonResponseData.getString("ok_id"));
                                Log.i("TRACE", "json response data" + jsonResponseData);
                                Log.i("TRACE", "json response data message" + jsonResponseData.getString("message"));


                                Toast.makeText(context, "" + jsonResponseData.getString("message"), Toast.LENGTH_LONG).show();
//                                SnackbarManager.show(
//                                        Snackbar.with(context)
//                                                .position(Snackbar.SnackbarPosition.TOP)
//                                                .text("Your old oye with same specs: " + jsonResponseData.getString("message"))
//                                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


                                //  if (jsonResponseData.getInt("code") == 1) {
                                AppConstants.letsOye.setTime(formattedDate);
                               // AppConstants.letsOye.save();
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

                                //Check here if new default deal is redundant Uncomment to replace old oyes with new with same specs
                   /*

                            Collection d = deals1.values();
                            Log.i("TRACE","values after jugad collection1" +d);

                            Iterator it = d.iterator();
                            while (it.hasNext()) {
                                String s = it.next().toString();

                                if(s.equals(speccode)){
                                    Log.i("TRACE","redundant deals comparison " +s +speccode);
                                    Log.i("TRACE","Removed entries for " +s);

                                    deals1.values().removeAll(Collections.singleton(s));
                                    Log.i("TRACE", "hashmap after removing redundant deals:" + deals1);
                                    Toast.makeText(context, "Your old oye with same specs: " + speccode+" has been replaced with new one.", Toast.LENGTH_LONG).show();
                                    SnackbarManager.show(
                                            Snackbar.with(context)
                                                    .position(Snackbar.SnackbarPosition.TOP)
                                                    .text("Your old oye with same specs: " + speccode)
                                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                                }
                                //   Log.i("TRACE", "element of set Set from shared == " + s);

                            } */


                                deals1.put(General.getSharedPreferences(context, "OK_ID"), speccode);
                                // Log.i("TRACE", "hashmap" + deals1);

                                Log.i("TRACE", "step1");

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
                        Log.i("TRACE", "open intent deal listing");
                        //open deals listing
                        Intent openDealsListing = new Intent(context, ClientDealsListActivity.class);
                        openDealsListing.putExtra("default_deal_flag",true);
                        openDealsListing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(openDealsListing);

                        Intent intent = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "error " + error.getMessage());
                        Log.i("TRACE", "RetrofitError");
                    }
                });
            }
        }
        catch (Exception e) {
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

//        if(!(haveConnectedMobile) && !(haveConnectedWifi))
//            Toast.makeText(context, "INTERNET CONNECTIVITY NOT AVAILABLE", Toast.LENGTH_LONG).show();


       /* SnackbarManager.show(
                    Snackbar.with(context)
                            .position(Snackbar.SnackbarPosition.BOTTOM)
                            .text("INTERNET CONNECTIVITY NOT AVAILABLE")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));*/

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