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
import android.support.v4.content.LocalBroadcastManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by YASH_SHAH on 28/12/2015.
 */

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    public static int NOTIFICATION_ID = 1;
    Boolean RefreshDrooms = false;
    private int badgeCount;
    private int supportCount;
    private int hdRoomsCount;
    private int rentalCount;
    private int resaleCount;
    private int tenantsCount;
    private int ownersCount;
    private int buyerCount;
    private int sellerCount;


    private String tType;
    private String intend;
    private String ptype;
    private String pstype;
    private String price;
    private Boolean LL = false;
    private Boolean OR = false;
    private Boolean REQ = false;
    private Boolean AVL = false;
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


        Log.i(TAG,"bundle data is "+data);
        //clear all badge counts
//        General.setBadgeCount(getApplicationContext(),AppConstants.HDROOMS_COUNT,0);
//        General.setBadgeCount(getApplicationContext(),AppConstants.BADGE_COUNT,0);
//        General.setBadgeCount(getApplicationContext(),AppConstants.SUPPORT_COUNT,0);

        badgeCount = General.getBadgeCount(getApplicationContext(),AppConstants.BADGE_COUNT);
        supportCount = General.getBadgeCount(getApplicationContext(),AppConstants.SUPPORT_COUNT);
        hdRoomsCount = General.getBadgeCount(getApplicationContext(),AppConstants.HDROOMS_COUNT);



        //ShortcutBadger.applyCount(this, badgeCount);

        String title = data.getString("title");
        String message = data.getString("message");
        String okId = null;
        String deals;


        Log.i(TAG,"Title is "+title);

        if(title.equalsIgnoreCase("Oye")){

            rentalCount = General.getBadgeCount(getApplicationContext(),AppConstants.RENTAL_COUNT);
            resaleCount = General.getBadgeCount(getApplicationContext(),AppConstants.RESALE_COUNT);
            tenantsCount = General.getBadgeCount(getApplicationContext(),AppConstants.TENANTS_COUNT);
            ownersCount = General.getBadgeCount(getApplicationContext(),AppConstants.OWNERS_COUNT);
            buyerCount = General.getBadgeCount(getApplicationContext(),AppConstants.BUYER_COUNT);
            sellerCount  = General.getBadgeCount(getApplicationContext(),AppConstants.SELLER_COUNT);

            Log.i(TAG,"Message is "+message);   // Message: REQ-industrial-kitchen-1200000-LL


            String[] split = message.split("-");
            String a = null;
            String b = null;

            //for (int i = 0; i < split.length; i++) {

                intend = split[0];
                ptype = split[1];
                pstype = split[2];
                price = split[3];
                tType = split[4];
         //  DecimalFormat formatter = new DecimalFormat();

            price = General.currencyFormat(price);
//            int x =Integer.parseInt(price);
//
//            Format format1 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
//            price=format1.format(x);
//            price = price.substring(0,price.length()-3);
            //price ="₹ "+price;


           // }

            if(tType.equalsIgnoreCase("LL")) {
                Log.i("NOTIF","LL set true");
                LL = true;
                Log.i("NOTIF","LL set true value "+LL);
                b = "rent";
            }
               else {
                OR = true;
                b = "sale";
            }
            if(intend.equalsIgnoreCase("REQ")) {
                REQ = true;
                a = "required";
            }else {
                AVL = true;
                a = "available";
            }
             if(LL){
                 rentalCount++;
                 LL = true;
                 Log.i("NOTIF","LL set true value rentalCount "+rentalCount);
                 if(REQ) {
                     tenantsCount++;
                     Log.i("NOTIF", "LL set true value rentalCount " + tenantsCount);

                 }else if(AVL)
                     ownersCount++;
                 LL = false;
                 REQ = false;
                 AVL = false;

             }
            else if(OR){
                 resaleCount++;
                 if(REQ)
                     buyerCount++;
                 else if(AVL)
                     sellerCount++;
                 OR = false;
                 REQ = false;
                 AVL = false;

             }
            ptype = ptype.substring(0, 1).toUpperCase() + ptype.substring(1);

            message = ptype+"("+pstype+")"+" is "+ a + " at price "+price+ " for "+b+".";

            General.setBadgeCount(getApplicationContext(),AppConstants.RENTAL_COUNT,rentalCount);
            General.setBadgeCount(getApplicationContext(),AppConstants.RESALE_COUNT,resaleCount);
            General.setBadgeCount(getApplicationContext(),AppConstants.TENANTS_COUNT,tenantsCount);
            General.setBadgeCount(getApplicationContext(),AppConstants.OWNERS_COUNT,ownersCount);
            General.setBadgeCount(getApplicationContext(),AppConstants.BUYER_COUNT,buyerCount);
            General.setBadgeCount(getApplicationContext(),AppConstants.SELLER_COUNT,sellerCount);

            Log.i(TAG,"rentalCount "+rentalCount);
            Log.i(TAG,"resaleCount "+resaleCount);
            Log.i(TAG,"tenantsCount "+tenantsCount);
            Log.i(TAG,"ownersCount "+ownersCount);
            Log.i(TAG,"buyerCount "+buyerCount);
            Log.i(TAG,"sellerCount "+sellerCount);


        }





        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        deals = General.getDefaultDeals(this);

        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> deals1 = gson.fromJson(deals, type);

        Log.d(TAG,"data is" + data);
        Log.d(TAG,"deals are" + deals);

        Log.d(TAG, "Message: " + message);     //["+918483014575","ritesh"]

        Log.d(TAG, "ROLE_OF_USER: " + General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));

        //Update hdRoomsCount badge

        Log.i(TAG,"msg is "+message+" type of msg is: "+message.getClass().getSimpleName());

        try{
            JSONObject json = new JSONObject(data.getString("message"));

                okId = json.getString("ok_id");
            if(!okId.equals("")){
                hdRoomsCount++;
                badgeCount++;
                Log.i(TAG,"badecount hd rooms badgeCount "+badgeCount);
                Log.i(TAG,"badecount hd rooms hdRoomsCount "+hdRoomsCount);

                ShortcutBadger.applyCount(this, badgeCount);
                General.setBadgeCount(getApplicationContext(),AppConstants.HDROOMS_COUNT,hdRoomsCount);
                General.setBadgeCount(getApplicationContext(),AppConstants.BADGE_COUNT,badgeCount);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }


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
                Log.i("Default deals in shared","I am here2");
                Gson g = new Gson();
                String hashMapString = g.toJson(deals1);
                General.saveDefaultDeals(this, hashMapString);
                String deals5 = General.getDefaultDeals(this);
                Log.i("Default deals in shared","I am here");
                Log.i("Default deals in shared","are" +deals5);




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
                    //delete default dealing room for this ok Id  // what is user discards notification? dealing room bhi nahi khulega and default deal bhi delete ho jayegi
                    // dealing room is already present at server so when SeeHDrooms call hoga then dealing room aa jayegi (But where is clients approval that he wants to chat with broker)
                    // currently there is no choice to client if broker oks then mob no. exchange ho jayenge.


                    // code for deleting default dealing room





             /*
  */


                    General.setSharedPreferences(getApplicationContext(), AppConstants.CLIENT_OK_ID, okId);
                if(RefreshDrooms){
                    Log.d(TAG, "Refresh Drooms flag is set");

                    Intent intent2 = new Intent("okeyed");
                   intent2.putExtra("RefreshDrooms", true);
                  LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
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

        if(title.equalsIgnoreCase("Oyeok")) {
            if(General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equals("client"))
            message = "We have just assigned a broker to your request.";
            else
             message = "We have just created a dealing room on your request.";
        }
        Log.d(TAG,"OnMessageReceived MyGcmListenerService sending notification");

       if(!message.equalsIgnoreCase("hello"))
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
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));


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