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
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.ClientDealsListActivity;
import com.nbourses.oyeok.models.PublishLetsOye;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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


    public static boolean saveArray(String[] array, String arrayName, Context mContext) {
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(prefName, value);
        editor.commit();
    }

    public static void saveDefaultDeals(Context context, Set<String> value) {
        Log.i("TRACE", "save default deal inside" + value);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("default_deals", value);
        editor.commit();
    }

    public static Set<String> getDefaultDeals(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        Log.i("TRACE", "rt" + prefName);

        return prefs.getStringSet("default_deals", null);
    }

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



    public static void publishOye(final Context context) {
        try {
            String tt;
            String pstype;
            String price;
            String speccode;
            Log.i("TRACE", "in publishOye");
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(AppConstants.letsOye);

            Log.d(TAG, "AppConstants.letsOye "+json);
            Log.i("TRACE", "AppConstants.letsOye " + json);

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
            Log.i("TRACE", "efg" + set);
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
            Log.i("TRACE", "Saved");

            if (isNetworkAvailable(context)) {
                Log.i("TRACE", "is networking available nik" + General.getSharedPreferences(context, AppConstants.USER_ID));

                //set userId
                AppConstants.letsOye.setUserId(General.getSharedPreferences(context, AppConstants.USER_ID));
                //set gcmId
                AppConstants.letsOye.setGcmId(SharedPrefs.getString(context, SharedPrefs.MY_GCM_ID));

                Log.i("TRACE", "is networking available" + SharedPrefs.getString(context, SharedPrefs.MY_GCM_ID));
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

                            Toast.makeText(context, ""+jsonResponseData.getString("message"), Toast.LENGTH_LONG).show();

                            if (jsonResponseData.getInt("code") == 1) {
                                AppConstants.letsOye.setTime(formattedDate);
                                AppConstants.letsOye.save();
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


        if(haveConnectedMobile) {
            Log.i("TRACE", "MOBILE NETWORK AVAILABLE ");
//            Toast.makeText(context,"NETWORK AVAILABLE",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"MOBILE NETWORK NOT AVAILABLE",Toast.LENGTH_LONG).show();
            Log.i("TRACE", "MOBILE NETWORK NOT AVAILABLE ");
            SnackbarManager.show(
                    Snackbar.with(context)
                            .position(Snackbar.SnackbarPosition.BOTTOM)
                            .text("MOBILE NETWORK NOT AVAILABLE")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


        }

        if(haveConnectedWifi)
            Log.i("TRACE","WIFI NETWORK AVAILABLE ");
        else {
            Toast.makeText(context,"WIFI NETWORK NOT AVAILABLE",Toast.LENGTH_LONG).show();
            Log.i("TRACE", "WIFI NETWORK NOT AVAILABLE ");
            SnackbarManager.show(
                    Snackbar.with(context)
                            .position(Snackbar.SnackbarPosition.BOTTOM)
                            .text("WIFI NETWORK NOT AVAILABLE")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

        }
    }
}
