package com.nbourses.oyeok.RPOT.ApiSupport.services;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.Firebase.DroomChatFirebase;
import com.nbourses.oyeok.Firebase.DroomDetails;
import com.nbourses.oyeok.RPOT.ApiSupport.models.AcceptOk;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;

import org.json.JSONArray;
import org.json.JSONException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by DADDU_DON on 12/30/2015.
 */
public class AcceptOkCall {
    DroomChatFirebase droomChatFirebase;

    public void setmCallBack(OnAcceptOkSuccess mCallBack) {
        this.mCallBack = mCallBack;
    }

    OnAcceptOkSuccess mCallBack;
    public void acceptOk(JSONArray m,int position, DBHelper dbHelper, Activity activity) {
        String oyeId=null,oyeUserId=null,tt = null,size=null,price=null,reqAvl=null;
        droomChatFirebase=new DroomChatFirebase(DatabaseConstants.firebaseUrl);


        Log.i("mArray= ",m.toString());
        try {
             oyeId=m.getJSONObject(position).getString("oye_id");
             oyeUserId= m.getJSONObject(position).getString("user_id");
            tt=m.getJSONObject(position).getString("tt");
            size=m.getJSONObject(position).getString("size");
            price=m.getJSONObject(position).getString("price");
            reqAvl = m.getJSONObject(position).getString("req_avl");
        }
        catch (JSONException e){
            e.printStackTrace();
            Log.i("Munni","null hai");
        }


        String API = "http://ec2-52-25-136-179.us-west-2.compute.amazonaws.com:9000";
        Oyeok acceptOk = new Oyeok();
        //DBHelper dbHelper1= new DBHelper();
        acceptOk.setDeviceId("Hardware");
        acceptOk.setGcmId(SharedPrefs.getString(activity,SharedPrefs.MY_GCM_ID));
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
        if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null")&& isNetworkAvailable(activity))
        try {
            user1.acceptOk(acceptOk, new Callback<AcceptOk>() {
                @Override
                public void success(AcceptOk acceptOk, Response response) {
                    Log.i("call chala", "nachoo");
                    Bundle args=new Bundle();
                    args.putString("UserId1",acceptOk.responseData.getOkUserId());
                    args.putString("UserId2",acceptOk.responseData.getOyeUserId());
                    args.putString("OkId",acceptOk.responseData.getOkId());
                    if(mCallBack!=null)
                    {
                        DroomDetails droomDetails=new DroomDetails();
                        droomDetails.setTitle("Test Droom");
                        Log.i("call chala","nacho2");
                        droomDetails.setLastMessage("Test Last Message");
                        droomDetails.setStatus("Test Message Not read");
                        droomDetails.setTimestamp("Test TimeStamp");
                        String userId1=acceptOk.responseData.getOkUserId();
                        String userId2=acceptOk.responseData.getOyeUserId();
                        String okId=acceptOk.responseData.getOkId();
                        droomChatFirebase.createChatRoom(okId,userId1,userId2,droomDetails);
                        mCallBack.replaceFragment(args);
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

                @Override
                public void failure(RetrofitError error) {
                    Log.i("accept error", error.getMessage());
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
                    }

                }
            });
        }catch (Exception e){
            Log.i("Exception","caught in accept ok");
        }
    }

    private boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
