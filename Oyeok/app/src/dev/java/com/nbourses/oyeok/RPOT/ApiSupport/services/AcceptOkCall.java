package com.nbourses.oyeok.RPOT.ApiSupport.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.Firebase;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.RPOT.ApiSupport.models.AcceptOk;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.activities.BrokerDealsListActivity;
import com.nbourses.oyeok.activities.ClientDealsListActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

//import com.nbourses.oyeok.Database.DBHelper;

/**
 * Created by DADDU_DON on 12/30/2015.
 */
public class AcceptOkCall {
   // DroomChatFirebase droomChatFirebase;
    private static final String TAG = "AcceptOkCall";

    public void setmCallBack(OnAcceptOkSuccess mCallBack) {
        this.mCallBack = mCallBack;
    }

    OnAcceptOkSuccess mCallBack;
//    public void acceptOk(final HashMap<String, Float> listings, JSONArray m, int position, final DBHelper dbHelper, final Activity activity) {
public void acceptOk(final HashMap<String, Float> listings, JSONArray m, int position,final Activity activity) {

    Log.i(TAG,"chaman prasanna 2");
        if(General.isNetworkAvailable(activity)) {
            General.slowInternet(activity);

        String oyeId=null,oyeUserId=null,tt = null,size=null,price=null,reqAvl=null;
        Firebase.setAndroidContext(activity);
       // droomChatFirebase=new DroomChatFirebase(DatabaseConstants.firebaseUrl,activity);


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


//        for (Map.Entry<String, Float> entry : listings.entrySet())
//        {
        Log.i("TRACEOK","listings "+listings);
            acceptOk.setListings(listings);
//        }

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
//        acceptOk.setUserId(dbHelper.getValue(DatabaseConstants.userId));
            acceptOk.setUserId(General.getSharedPreferences(activity.getBaseContext(),AppConstants.USER_ID));

            //acceptOk.setUserId("yjhjoy71igl77w1as3krul7mb0wgavoy");
        acceptOk.setOyeUserId(oyeUserId);
        acceptOk.setTimeToMeet("15");

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService user1 = restAdapter.create(OyeokApiService.class);
        Log.i("TRACEOK", "if called "+user1);
//        if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null")&& isNetworkAvailable(activity))
//        if (isNetworkAvailable(activity)) {
            Log.i(TAG,"chaman prasanna 3");
            try {
                user1.acceptOk(acceptOk, new Callback<AcceptOk>() {
                    @Override
                    public void success(AcceptOk acceptOk, Response response) {
                        Log.i(TAG,"chaman prasanna 4");
                        General.slowInternetFlag = false;
                        General.t.interrupt();

                        Log.i("TRACEOK", "if called "+response);

                        String strResponse =  new String(((TypedByteArray)response.getBody()).getBytes());
                        try {
                            JSONObject jsonResponse = new JSONObject(strResponse);
                            JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));
                            Log.i("TRACEOK", "Response listings" + jsonResponseData);
                        }
                        catch (Exception e)
                        {
                            Log.i("Exception ","message : "+e.getMessage());
                        }




                        if (acceptOk.responseData.getMessage() == null) {
                            Log.i("TRACEOK", "if called "+response);
                            Log.i(TAG,"chaman prasanna 5");
                           // Log.d(TAG, "getOyeId " + acceptOk.responseData.getOyeId());
                            Log.i(TAG,"chaman prasanna 6 "+ acceptOk.responseData);

                            Log.i(TAG, "getOkId " + acceptOk.responseData.getOkId());

                           displayDefaultMessageB(activity,acceptOk.responseData.getOkId());

                            //String coolOffString = acceptOk.responseData.getCoolOff();
                            //int coolOff = Integer.parseInt(coolOffString);
                            Bundle args = new Bundle();
                            args.putString("UserId1", acceptOk.responseData.getOkUserId()); //broker
                            args.putString("UserId2", acceptOk.responseData.getOyeUserId()); //client
                            args.putString("OkId", acceptOk.responseData.getOkId()); //channel id


                            Log.i("TRACEOK", "if called mCallBack"+mCallBack);

                            if (mCallBack != null) {
                                Log.i(TAG,"chaman prasanna 7");
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


                                Intent openDealsListing = new Intent(activity, ClientDealsListActivity.class);
                                openDealsListing.putExtra("oyeok "+AppConstants.OK_ID, acceptOk.responseData.getOkId());


                                openDealsListing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



                                /*AppConstants.BROKER_DEAL_FLAG = true;
                                Intent openDealsListing = new Intent(activity, DealConversationActivity.class);
                                Bundle extra = new Bundle();
                                extra.putSerializable("listings",listings);
                                openDealsListing.putExtras(extra);
                                openDealsListing.putExtra("OkAccepted","yes");
                                openDealsListing.putExtra(AppConstants.OK_ID, acceptOk.responseData.getOkId());
                                openDealsListing.putExtra("userRole", "broker");*/


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

                            if(acceptOk.responseData.getMessage().equalsIgnoreCase("Oye user unavailable")){
                                TastyToast.makeText(activity, acceptOk.responseData.getMessage(), TastyToast.LENGTH_LONG, TastyToast.WARNING);
                            }
                            else
                                TastyToast.makeText(activity, acceptOk.responseData.getMessage(), TastyToast.LENGTH_LONG, TastyToast.INFO);
                            Log.i(TAG,"chaman prasanna 8");
                            Log.i("TRACE", "else called ");
                            Log.i("TRACE","serverMessage "+acceptOk.responseData.getMessage());

                            Intent openDealsListing = new Intent(activity, ClientDealsListActivity.class);
                            openDealsListing.putExtra("serverMessage", acceptOk.responseData.getMessage());
                            Log.i("TRACEOK", "serverMessage " + acceptOk.responseData.getMessage());
                            Log.i("TRACEBROKERSIGNUP","3");
                            activity.startActivity(openDealsListing);
                        }
//                            ((ClientMainActivity) activity).showToastMessage(acceptOk.responseData.getMessage());

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
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
//        }
//        else {
//            SnackbarManager.show(
//                    com.nispok.snackbar.Snackbar.with(activity)
//                            .position(Snackbar.SnackbarPosition.BOTTOM)
//                            .text("Please check your internet connection")
//                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
//        }
    }else{

        General.internetConnectivityMsg(activity);
    }


    }

    private void displayDefaultMessageB(Context context, String channelName){
        try {
            PubNub pubnub = General.initPubnub(context, General.getSharedPreferences(context, AppConstants.USER_ID));

            final JSONObject jsonMsg = new JSONObject();
            jsonMsg.put("_from", General.getSharedPreferences(context, AppConstants.USER_ID));
            jsonMsg.put("to", channelName);
            jsonMsg.put("timetoken", String.valueOf(System.currentTimeMillis()));

            jsonMsg.put("name", "");
            jsonMsg.put("message", "Client have initiated enquiry for " + General.getSharedPreferences(context, AppConstants.PTYPE).substring(0, 1).toUpperCase() + General.getSharedPreferences(context, AppConstants.PTYPE).substring(1)
                    + " property (" + General.getSharedPreferences(context, AppConstants.PSTYPE) + ") within budget " + General.currencyFormat(General.getSharedPreferences(context, AppConstants.PRICE)) + ".");

            jsonMsg.put("status", "OKS");

            Map message = new HashMap();

            message.put("pn_gcm", new HashMap(){{put("data",new HashMap(){{put("message",jsonMsg.getString("message")); put("_from",jsonMsg.getString("_from"));put("to",jsonMsg.getString("to"));put("name",jsonMsg.getString("name")); put("status",jsonMsg.getString("status"));}});}});
            message.put("pn_apns", new HashMap(){{put("aps",new HashMap(){{put("alert",jsonMsg.getString("message")); put("_from",jsonMsg.getString("_from"));put("to",jsonMsg.getString("to"));put("name",jsonMsg.getString("name")); put("status",jsonMsg.getString("status"));}});}});


            Log.i(TAG,"Message is "+message);

            pubnub.publish()
                    .message(message)
                    .shouldStore(true)
                    .usePOST(true)
                    .channel(channelName)
                    .async(new PNCallback<PNPublishResult>() {
                        @Override
                        public void onResponse(PNPublishResult result, PNStatus status) {
                            if (status.isError()) {
                                System.out.println("publish failed!");
                                Log.i(TAG,"PUBNUB publish "+status.getStatusCode());
                            } else {
                                System.out.println("push notification worked!");
                                System.out.println(result);
                            }
                        }
                    });


        }
        catch(Exception e){

        }

    }
    private boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
