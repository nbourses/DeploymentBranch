package com.nbourses.oyeok.GoogleCloudMessaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.activities.BrokerMainActivity;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by YASH_SHAH on 28/12/2015.
 */

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    public static int NOTIFICATION_ID = 1;
    Boolean RefreshDrooms = false;
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String title = data.getString("title");
        String message = data.getString("message");
        String okId = null;
        String deals;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        deals = General.getDefaultDeals(this);

        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> deals1 = gson.fromJson(deals, type);

        Log.d(TAG,"deals are" + deals);

        Log.d(TAG, "Message: " + message);     //["+918483014575","ritesh"]

        Log.d(TAG, "ROLE_OF_USER: " + General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));


        if (General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equals("client")) {
            try {
                Log.d(TAG, "Inside try");
                JSONObject jsonObjectMsg = new JSONObject(data.getString("message"));
                Log.d(TAG, "jsonObjectMsg is " + jsonObjectMsg);
                message = jsonObjectMsg.getString("text");
                Log.d(TAG, "text is " + message);
                okId = jsonObjectMsg.getString("ok_id");
                Log.d(TAG, "okId is: " + okId);


               // Collection d = deals1.values();

                Log.i(TAG,"before deal "+deals1);
                Iterator<Map.Entry<String,String>> iter = deals1.entrySet().iterator();

                while (iter.hasNext()) {
                    Map.Entry<String,String> entry = iter.next();
                    Log.d(TAG,"entry.getKey"+entry.getKey());
                    if(okId.equalsIgnoreCase(entry.getKey())){
                        iter.remove();
                        Log.d(TAG, "entry.getKey removed" + entry.getKey());
                        Log.d("CHATTRACE", "default droomsremoved" + entry.getKey());
                        Log.d("CHATTRACE", "default droomsremoved okid" + okId);
                        Log.d(TAG,"entry.getKey removed"+entry.getValue());
                        RefreshDrooms = true;
                    }
                }
                Log.i(TAG,"after deal "+deals1);

                Gson g = new Gson();
                String hashMapString = g.toJson(deals1);
                General.saveDefaultDeals(this, hashMapString);




        /*        Iterator it = d.iterator();
                while (it.hasNext()) {
                    String s = it.next().toString();

                    if (s.equals(okId)) {
//                        Log.i("TRACE","redundant deals comparison " +s +speccode);
//                        Log.i("TRACE","Removed entries for " +s);
//
//                        deals1.values().removeAll(Collections.singleton(s));
//                        Log.i("TRACE", "hashmap after removing redundant deals:" + deals1);
//                        Toast.makeText(context, "Your old oye with same specs: " + speccode + " has been replaced with new one.", Toast.LENGTH_LONG).show();

                    }
                }
*/
                    //Delete default dealing room for this ok Id  // what is user discards notification? dealing room bhi nahi khulega and default deal bhi delete ho jayegi
                    // dealing room is already present at server so when SeeHDrooms call hoga then dealing room aa jayegi (But where is clients approval that he wants to chat with broker)
                    // currently there is no choice to client if broker oks then mob no. exchange ho jayenge.


                    // code for deleting default dealing room





             /*
  */


                    General.setSharedPreferences(getApplicationContext(), AppConstants.CLIENT_OK_ID, okId);
                if(RefreshDrooms){
                    Log.d(TAG, "Refresh Drooms flag is set");

                 //   Intent intent2 = new Intent("shine");
                 //   intent2.putExtra("RefreshDrooms", true);
                 //   LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
                    //Intent intent = new Intent(this, ClientDealsListActivity.class);
                    //startActivity(intent);

                }


                    Log.d(TAG, "CLIENT_OK_ID: " + General.getSharedPreferences(getApplicationContext(), AppConstants.CLIENT_OK_ID));

                }catch(Exception e){
                    Log.i("TAG", "Inside catch");
                    e.printStackTrace();
                }
            }

        Log.d(TAG, "From: " + from);   // 463092685367
        Log.d(TAG, "Title: "+ title);  // OyeOk
        Log.d(TAG, "Message: " + message); //["+918483014575","ritesh"]

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */

/*
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> deals1 = gson.fromJson(deals, type);

        Log.d("TRACE", "hashmap:" + deals1);

        if(deals1 == null){
            deals1 = new HashMap<String, String>();

        }


        Iterator<Map.Entry<String,String>> iter = deals1.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String,String> entry = iter.next();
            Log.d("TRACE","Inside iterator "+entry);
            if(okId.equalsIgnoreCase(entry.getKey())){
                Log.d("TRACE","Deleted default deal "+entry);
                iter.remove();

            }
        }

*/
        Log.d(TAG,"OnMessageReceived MyGcmListenerService sending notification");
        this.sendNotification(title, message);
        Log.d(TAG, "After sendNotification");

        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String title,String message) {
        Context context = this.getBaseContext();
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn = pm.isScreenOn();

        Log.e("screen on.......", ""+isScreenOn);

        if(isScreenOn==false)
        {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
            wl_cpu.acquire(10000);
        }
//        Intent intent = new Intent(this, DashboardActivity.class);

        Intent intent = null;
        if (!General.getSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER).equals("") &&
                General.getSharedPreferences(context, AppConstants.ROLE_OF_USER).equals("broker")) {
            intent = new Intent(context, BrokerMainActivity.class);
        }
        else {
            intent = new Intent(context, ClientMainActivity.class);
            //intent = new Intent(context, ClientDealsListActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int requestID = (int) System.currentTimeMillis();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (NOTIFICATION_ID > 1073741824) {
            NOTIFICATION_ID = 0;
        }
        if(RefreshDrooms){
            Log.d(TAG,"Refresh Drooms flag is set1");
           // Intent i = new Intent(this, ClientDealsListActivity.class);
           // startActivity(i);

        }
        notificationManager.notify(NOTIFICATION_ID++ /* ID of notification */, notificationBuilder.build());
        Log.d(TAG,"Notified");
    }
}