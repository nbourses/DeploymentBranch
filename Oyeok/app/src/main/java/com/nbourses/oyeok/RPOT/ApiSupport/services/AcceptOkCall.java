package com.nbourses.oyeok.RPOT.ApiSupport.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.Firebase;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.Firebase.DroomChatFirebase;
import com.nbourses.oyeok.RPOT.ApiSupport.models.AcceptOk;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.activities.BrokerDealsListActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by DADDU_DON on 12/30/2015.
 */
public class AcceptOkCall {
    DroomChatFirebase droomChatFirebase;
    private static final String TAG = "AcceptOkCall";

    public void setmCallBack(OnAcceptOkSuccess mCallBack) {
        this.mCallBack = mCallBack;
    }

    OnAcceptOkSuccess mCallBack;
    public void acceptOk(HashMap<String, Float> listings, JSONArray m, int position, final DBHelper dbHelper, final Activity activity) {
        String oyeId=null,oyeUserId=null,tt = null,size=null,price=null,reqAvl=null;
        Firebase.setAndroidContext(activity);
        droomChatFirebase=new DroomChatFirebase(DatabaseConstants.firebaseUrl,activity);


        Log.i("mArray= ",m.toString());
        try {
             Log.i("OKACCPET","position is" +position);
             oyeId=m.getJSONObject(position).getString("oye_id");
            Log.i("OKACCEPT","oyeId is" +oyeId);

            // oyeUserId= m.getJSONObject(position).getString("user_id");
            tt=m.getJSONObject(position).getString("tt");
            //size=m.getJSONObject(position).getString("size");
            price=m.getJSONObject(position).getString("price");
            reqAvl = m.getJSONObject(position).getString("req_avl");
        }
        catch (JSONException e){
            e.printStackTrace();
        }


        String API = DatabaseConstants.serverUrl;
        Oyeok acceptOk = new Oyeok();
        //DBHelper dbHelper1= new DBHelper();


        for (Map.Entry<String, Float> entry : listings.entrySet())
        {
            acceptOk.setListings(entry.getKey(),entry.getValue());
        }

       //
        acceptOk.setDeviceId("Hardware");
        acceptOk.setGcmId(SharedPrefs.getString(activity, SharedPrefs.MY_GCM_ID));
        acceptOk.setPushToken(SharedPrefs.getString(activity, SharedPrefs.MY_GCM_ID));
        acceptOk.setPlatform("android");
        acceptOk.setUserRole("broker");
        //acceptOk.setUserRole("broker");
        acceptOk.setLong(SharedPrefs.getString(activity.getBaseContext(), SharedPrefs.MY_LNG));
        acceptOk.setLat(SharedPrefs.getString(activity.getBaseContext(), SharedPrefs.MY_LAT));
        acceptOk.setTt(tt);
        acceptOk.setSize(size);
        acceptOk.setPrice(price);
        acceptOk.setReqAvl(reqAvl);
        acceptOk.setOyeId(oyeId);
        acceptOk.setUserId(dbHelper.getValue(DatabaseConstants.userId));
        //acceptOk.setUserId("yjhjoy71igl77w1as3krul7mb0wgavoy");
        acceptOk.setOyeUserId(oyeUserId);
        acceptOk.setTimeToMeet("15");

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService user1 = restAdapter.create(OyeokApiService.class);
        Log.i("TRACEOK", "if called "+user1);
//        if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null")&& isNetworkAvailable(activity))
        if (isNetworkAvailable(activity)) {
            try {
                user1.acceptOk(acceptOk, new Callback<AcceptOk>() {
                    @Override
                    public void success(AcceptOk acceptOk, Response response) {
                        Log.i("TRACEOK", "if called "+response);
                        if (acceptOk.responseData.getMessage() == null) {
                            Log.i("TRACEOK", "if called "+response);

                           // Log.d(TAG, "getOyeId " + acceptOk.responseData.getOyeId());
                            Log.d(TAG, "getOkId " + acceptOk.responseData.getOkId());

                            //String coolOffString = acceptOk.responseData.getCoolOff();
                            //int coolOff = Integer.parseInt(coolOffString);
                            Bundle args = new Bundle();
                            args.putString("UserId1", acceptOk.responseData.getOkUserId()); //broker
                            args.putString("UserId2", acceptOk.responseData.getOyeUserId()); //client
                            args.putString("OkId", acceptOk.responseData.getOkId()); //channel id

                            Log.i("TRACEOK", "if called mCallBack"+mCallBack);

                            if (mCallBack != null) {
                                Log.i("TRACEOK", "if called ");
                                Log.i("TRACEOK", "if called " +acceptOk.responseData.getMessage());

                               // dbHelper.save(DatabaseConstants.coolOff, coolOffString);
                     //           dbHelper.save("Time", acceptOk.responseData.getTime().toString());



                                /* Firebase thing excluded: Ritesh
//                                DroomDetails droomDetails = new DroomDetails();
//                                droomDetails.setTitle("Test Droom");
//                                Log.i("call chala", "nacho2");
//                                droomDetails.setLastMessage("Test Last Message");
//                                droomDetails.setStatus("Test Message Not read");
//                                droomDetails.setTimestamp("Test TimeStamp");
//                                String userId1 = acceptOk.responseData.getOkUserId();
//                                String userId2 = acceptOk.responseData.getOyeUserId();
//                                String okId = acceptOk.responseData.getOkId();
//                                droomChatFirebase.createChatRoom(okId, userId1, userId2, droomDetails); */
                                Log.i("TRACEBROKERSIGNUP","2");

                               // mCallBack.replaceFragment(args);



                                Intent openDealsListing = new Intent(activity, BrokerDealsListActivity.class);
                                //openDealsListing.putExtra("serverMessage", acceptOk.responseData.getMessage());
                               // Log.i("TRACEOK", "serverMessage " + acceptOk.responseData.getMessage());
                               // Log.i("TRACEBROKERSIGNUP","3");
                                activity.startActivity(openDealsListing);


                            }
                    /*DroomDetails droomDetails=new DroomDetails();
                    droomDetails.setTitle("Test Droom");
                    droomDetails.setLastMessage("Test Last Message");
                    droomDetails.setStatus("Test Message Not read");
                    droomDetails.setTimestamp("Test TimeStamp");
                    String userId1=acceptOk.responseData.getOkUserId();
                    String userId2=acceptOk.responseData.getOyeUserId();
                    String okId=acceptOk.responseData.getOkId();
                    droomChatFirebase.createChatRoom(okId,userId1,userId2,droomDetails);
                    droomDetails.setTitle("Droom");
                    droomDetails.setLastMessage("Last Message");
                    droomDetails.setStatus("Message Not read");
                    droomDetails.setTimestamp("TimeStamp");
                    droomChatFirebase.updateChatRoom(okId,userId1,userId2,droomDetails);*/
                        }
                        else {
                            Log.i("TRACE", "else called ");
                            Log.i("TRACE","serverMessage "+acceptOk.responseData.getMessage());

                            Intent openDealsListing = new Intent(activity, BrokerDealsListActivity.class);
                            openDealsListing.putExtra("serverMessage", acceptOk.responseData.getMessage());
                            Log.i("TRACEOK", "serverMessage " + acceptOk.responseData.getMessage());
                            Log.i("TRACEBROKERSIGNUP","3");
                            activity.startActivity(openDealsListing);
                        }
//                            ((ClientMainActivity) activity).showToastMessage(acceptOk.responseData.getMessage());

                    }

                    @Override
                    public void failure(RetrofitError error) {
                    /*Log.i("accept error", error.getMessage());
                    Bundle args=new Bundle();
                    args.putString("UserId2","lub8aesblzt8gnokdjlofy5b1xcoduae");
                    args.putString("UserId1","yn4kiiqv91y1r6lrlzzrllypqv52552i");
                    args.putString("OkId", "hep8bfkyigl0v4c");
                    if(mCallBack!=null)
                    {
                        DroomDetails droomDetails=new DroomDetails();
                        droomDetails.setTitle("Test Droom");
                        droomDetails.setLastMessage("Test Last Message");
                        droomDetails.setStatus("Test Message Not read");
                        droomDetails.setTimestamp("Test TimeStamp");
                        String userId2="lub8aesblzt8gnokdjlofy5b1xcoduae";
                        String userId1="yn4kiiqv91y1r6lrlzzrllypqv52552i";
                        String okId="hep8bfkyigl0v4c";
                        droomChatFirebase.createChatRoom(okId,userId1,userId2,droomDetails);
                        mCallBack.replaceFragment(args);
                    }*/
                        Log.i("TRACEOK","serverMessage 1");
                        Log.i("TRACEOK","serverMessage "+ error.getMessage());
                        Log.i("TRACEOK","open broker deals list activity");
                        Intent openDealsListing = new Intent(activity, BrokerDealsListActivity.class);
                        openDealsListing.putExtra("serverMessage", error.getMessage());
                        Log.i("TRACEBROKERSIGNUP", "4");

                        activity.startActivity(openDealsListing);

//                        ((ClientMainActivity) activity).showToastMessage(error.getMessage());

                    }
                });
            } catch (Exception e) {
                Log.i("Exception", "caught in accept ok");
            }
        }
        else {
            SnackbarManager.show(
                    com.nispok.snackbar.Snackbar.with(activity)
                            .position(Snackbar.SnackbarPosition.BOTTOM)
                            .text("Please check your internet connection")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
        }
    }

    private boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
