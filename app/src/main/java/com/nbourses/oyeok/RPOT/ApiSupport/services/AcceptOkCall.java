package com.nbourses.oyeok.RPOT.ApiSupport.services;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
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
    public void acceptOk(JSONArray m,int position, DBHelper dbHelper, FragmentActivity activity) {
        String oyeId=null,oyeUserId=null,tt = null,size=null,price=null,reqAvl=null;
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
        }


        String API = "http://ec2-52-25-136-179.us-west-2.compute.amazonaws.com:9000";
        Oyeok acceptOk = new Oyeok();
        //DBHelper dbHelper1= new DBHelper();
        acceptOk.setDeviceId("Hardware");
        acceptOk.setGcmId(dbHelper.getValue(DatabaseConstants.gcmId));
        //acceptOk.setUserRole(dbHelper.getValue(DatabaseConstants.userRole));
        acceptOk.setUserRole("broker");
        acceptOk.setLong(SharedPrefs.getString(activity.getBaseContext(), SharedPrefs.MY_LNG));
        acceptOk.setLat(SharedPrefs.getString(activity.getBaseContext(),SharedPrefs.MY_LAT));
        acceptOk.setTt(tt);
        acceptOk.setSize(size);
        acceptOk.setPrice(price);
        acceptOk.setReqAvl(reqAvl);
        acceptOk.setOyeId(oyeId);
        //acceptOk.setUserId(dbHelper.getValue(DatabaseConstants.userId));
        acceptOk.setUserId("yjhjoy71igl77w1as3krul7mb0wgavoy");
        acceptOk.setOyeUserId(oyeUserId);
        acceptOk.setTimeToMeet("15");


        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService user1 = restAdapter.create(OyeokApiService.class);
        user1.acceptOk(acceptOk, new Callback<AcceptOk>() {
            @Override
            public void success(AcceptOk acceptOk, Response response) {
                Log.i("call chala","nachoo");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("accept error",error.getMessage());
            }
        });
    }
}
