package com.nbourses.oyeok.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

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
    private NetworkInterface networkInfo;

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

    public static void setSharedPreferences(Context context, String prefName, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(prefName, value);
        editor.commit();
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
            Log.i("TRACE", "in publishOye");
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(AppConstants.letsOye);

            Log.d(TAG, "AppConstants.letsOye "+json);
            Log.i("TRACE", "AppConstants.letsOye " + json);

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
                        Log.i("TRACE", "in success");
                        String strResponse =  new String(((TypedByteArray)response.getBody()).getBytes());
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
            Toast.makeText(context,"NO NETWORK AVAILABLE",Toast.LENGTH_SHORT).show();
            Log.i("TRACE", "MOBILE NETWORK NOT AVAILABLE ");
        }

        if(haveConnectedWifi)
            Log.i("TRACE","WIFI NETWORK AVAILABLE ");
        else {
            Toast.makeText(context,"NO WIFI AVAILABLE",Toast.LENGTH_SHORT).show();
            Log.i("TRACE", "WIFI NETWORK NOT AVAILABLE ");
        }
    }
}