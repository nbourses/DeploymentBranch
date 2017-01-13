package com.nbourses.oyeok.GoogleCloudMessaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.nbourses.oyeok.activities.ClientDealsListActivity;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.activities.DealConversationActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.realmModels.DealTime;
import com.nbourses.oyeok.realmModels.DefaultDeals;
import com.nbourses.oyeok.realmModels.HalfDeals;
import com.nbourses.oyeok.realmModels.NotifCount;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
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
    private String description;
    private String heading;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // Log.i("notifications","bundle data is "+data);
    // [START receive_message]


    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.i(TAG, "bundle data is inside pubnubgcm");

        Log.i(TAG, "bundle data is pubnubgcm yo " + data);
        //Log.i("notifications","bundle data is "+data);

        //clear all badge counts
//        General.setBadgeCount(getApplicationContext(),AppConstants.HDROOMS_COUNT,0);
//        General.setBadgeCount(getApplicationContext(),AppConstants.BADGE_COUNT,0);
//        General.setBadgeCount(getApplicationContext(),AppConstants.SUPPORT_COUNT,0);

        badgeCount = General.getBadgeCount(getApplicationContext(), AppConstants.BADGE_COUNT);
        supportCount = General.getBadgeCount(getApplicationContext(), AppConstants.SUPPORT_COUNT);
        hdRoomsCount = General.getBadgeCount(getApplicationContext(), AppConstants.HDROOMS_COUNT);


        //ShortcutBadger.applyCount(this, badgeCount);

        if (data.containsKey("title")) {
            String title = data.getString("title");
            String message = null;
            String imgUrl = null;
            if(data.containsKey("message")) {
                message = data.getString("message");
            }else{
                message = data.getString("alert");
            }
            String okId = null;
            String deals;


            Log.i(TAG, "Title is " + title);
            if(title.toLowerCase().contains("promo")){

                if(data.containsKey("bicon") && data.containsKey("alert")) {
                    heading = title.substring(title.indexOf("-") + 1);
                    //imgUrl = data.getString("bicon");
                    sendNotification(title,message,data);
                    return;
                }

            }

            else if (title.equalsIgnoreCase("Oye")) {
                badgeCount++;
                General.setBadgeCount(getApplicationContext(), AppConstants.BADGE_COUNT, badgeCount);
                ShortcutBadger.applyCount(this, badgeCount);
                rentalCount = General.getBadgeCount(getApplicationContext(), AppConstants.RENTAL_COUNT);
                resaleCount = General.getBadgeCount(getApplicationContext(), AppConstants.RESALE_COUNT);
                tenantsCount = General.getBadgeCount(getApplicationContext(), AppConstants.TENANTS_COUNT);
                ownersCount = General.getBadgeCount(getApplicationContext(), AppConstants.OWNERS_COUNT);
                buyerCount = General.getBadgeCount(getApplicationContext(), AppConstants.BUYER_COUNT);
                sellerCount = General.getBadgeCount(getApplicationContext(), AppConstants.SELLER_COUNT);

                Log.i(TAG, "Message is " + message);   // Message: REQ-industrial-kitchen-1200000-LL


                String[] split = message.split("-");
                String a = null;
                String b = null;

                //for (int i = 0; i < split.length; i++) {

                intend = split[0];
                tType = split[1];
                ptype = split[2];
                pstype = split[3];
                price = split[4];
                //  DecimalFormat formatter = new DecimalFormat();

                price = General.currencyFormat(price);
//            int x =Integer.parseInt(price);
//
//            Format format1 = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
//            price=format1.format(x);
//            price = price.substring(0,price.length()-3);
                //price ="₹ "+price;


                // }
                Log.i("NOTIF", "LL set true intend " + intend);
                Log.i("NOTIF", "LL set true ptype " + ptype);
                Log.i("NOTIF", "LL set true pstype " + pstype);
                Log.i("NOTIF", "LL set true price " + price);
                Log.i("NOTIF", "LL set true tType " + tType);


                if (tType.equalsIgnoreCase("LL")) {
                    Log.i("NOTIF", "LL set true");
                    LL = true;
                    Log.i("NOTIF", "LL set true value " + LL);
                    b = "rent";
                } else {
                    OR = true;
                    b = "sale";
                }
                if (intend.equalsIgnoreCase("REQ")) {
                    REQ = true;
                    a = "required";
                } else {
                    AVL = true;
                    a = "available";
                }
                if (LL) {
                    rentalCount++;
                    LL = true;
                    Log.i("NOTIF", "LL set true value rentalCount " + rentalCount);
                    if (REQ) {
                        tenantsCount++;
                        Log.i("NOTIF", "LL set true value rentalCount " + tenantsCount);

                    } else if (AVL)
                        ownersCount++;
                    LL = false;
                    REQ = false;
                    AVL = false;

                } else if (OR) {
                    resaleCount++;
                    if (REQ)
                        buyerCount++;
                    else if (AVL)
                        sellerCount++;
                    OR = false;
                    REQ = false;
                    AVL = false;

                }
                ptype = ptype.substring(0, 1).toUpperCase() + ptype.substring(1);

                message = ptype + "(" + pstype + ")" + " is " + a + " at price " + price + " for " + b + ".";

                General.setBadgeCount(getApplicationContext(), AppConstants.RENTAL_COUNT, rentalCount);
                General.setBadgeCount(getApplicationContext(), AppConstants.RESALE_COUNT, resaleCount);
                General.setBadgeCount(getApplicationContext(), AppConstants.TENANTS_COUNT, tenantsCount);
                General.setBadgeCount(getApplicationContext(), AppConstants.OWNERS_COUNT, ownersCount);
                General.setBadgeCount(getApplicationContext(), AppConstants.BUYER_COUNT, buyerCount);
                General.setBadgeCount(getApplicationContext(), AppConstants.SELLER_COUNT, sellerCount);

                Intent intent = new Intent(AppConstants.BADGE_COUNT_BROADCAST);
                intent.putExtra(AppConstants.RENTAL_COUNT, rentalCount);
                intent.putExtra(AppConstants.RESALE_COUNT, resaleCount);
                intent.putExtra(AppConstants.TENANTS_COUNT, tenantsCount);
                intent.putExtra(AppConstants.OWNERS_COUNT, ownersCount);
                intent.putExtra(AppConstants.BUYER_COUNT, buyerCount);
                intent.putExtra(AppConstants.SELLER_COUNT, sellerCount);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                Log.i(TAG, "rentalCount " + rentalCount);
                Log.i(TAG, "resaleCount " + resaleCount);
                Log.i(TAG, "tenantsCount " + tenantsCount);
                Log.i(TAG, "ownersCount " + ownersCount);
                Log.i(TAG, "buyerCount " + buyerCount);
                Log.i(TAG, "sellerCount " + sellerCount);


            }


            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            deals = General.getDefaultDeals(this);

            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            HashMap<String, String> deals1 = gson.fromJson(deals, type);

            Log.d(TAG, "data is" + data);
            Log.d(TAG, "deals are" + deals);

            Log.d(TAG, "Message: " + message);     //["+918483014575","ritesh"]

            Log.d(TAG, "ROLE_OF_USER: " + General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));

            //Update hdRoomsCount badge

            Log.i(TAG, "msg is " + message + " type of msg is: " + message.getClass().getSimpleName());



            if (General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equals("client")) {
                try {
                    Log.d(TAG, "Inside try");
                    JSONObject jsonObjectMsg = new JSONObject(data.getString("message"));
                    Log.d(TAG, "jsonObjectMsg is " + jsonObjectMsg);
                    message = jsonObjectMsg.getString("text");
                    Log.d(TAG, "text is " + message);
                    okId = jsonObjectMsg.getString("ok_id");
                    Log.d(TAG, "okId is: " + okId);

// store ok time for deals list
                    storeDealTime(okId);


                    // Collection d = deals1.values();

                    Log.i(TAG, "before deal " + deals1);
                    Iterator<Map.Entry<String, String>> iter = deals1.entrySet().iterator();

                    while (iter.hasNext()) {
                        Map.Entry<String, String> entry = iter.next();
                        Log.d(TAG, "entry.getKey" + entry.getKey());
                        if (okId.equalsIgnoreCase(entry.getKey())) {
                            iter.remove();
                            Log.d(TAG, "entry.getKey removed" + entry.getKey());
                            Log.d("CHATTRACE", "default droomsremoved" + entry.getKey());
                            Log.d("CHATTRACE", "default droomsremoved okid" + okId);
                            Log.d(TAG, "entry.getKey removed" + entry.getValue());
                            RefreshDrooms = true;
                        }
                    }

                    Log.i(TAG, "after deal " + deals1);
                    Log.i("Default deals in shared", "I am here2");
                    Gson g = new Gson();
                    String hashMapString = g.toJson(deals1);
                    General.saveDefaultDeals(this, hashMapString);
                    String deals5 = General.getDefaultDeals(this);
                    Log.i("Default deals in shared", "I am here");
                    Log.i("Default deals in shared", "are" + deals5);

                    //Delete ddroom from realm
                    try {
                        Realm myRealm = General.realmconfig(this);
                        RealmResults<DefaultDeals> result = myRealm.where(DefaultDeals.class).equalTo(AppConstants.OK_ID, okId).findAll();

                        myRealm.beginTransaction();
                        result.clear();
                        RefreshDrooms = true;
                        myRealm.commitTransaction();
                    } catch (Exception e) {
                        Log.i(TAG, "caught in exception deleting default droom");
                    }

                    try {
                        Realm myRealm = General.realmconfig(this);
                        RealmResults<DefaultDeals> results1 =
                                myRealm.where(DefaultDeals.class).findAll();
                        Log.i(TAG, "insiderrer3 " + results1);
                        for (DefaultDeals c : results1) {
                            Log.i(TAG, "insiderrer3 " + c.getOk_id());
                            Log.i(TAG, "insiderrer4 " + c.getSpec_code());
                        }
                    } catch (Exception e) {
                    }


                    General.setSharedPreferences(getApplicationContext(), AppConstants.CLIENT_OK_ID, okId);
                    if (RefreshDrooms) {
                        Log.d(TAG, "Refresh Drooms flag is set");

                        Intent intent2 = new Intent("okeyed");
                        intent2.putExtra("RefreshDrooms", true);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
                        //Intent intent = new Intent(this, ClientDealsListActivity.class);
                        //startActivity(intent);

                    }


                    Log.d(TAG, "CLIENT_OK_ID: " + General.getSharedPreferences(getApplicationContext(), AppConstants.CLIENT_OK_ID));

                } catch (Exception e) {
                    Log.i("TAG", "Inside catch");
                    e.printStackTrace();
                }
            }

            Log.d(TAG, "From: " + from);   // 463092685367
            Log.d(TAG, "Title: " + title);  // OyeOk
            Log.d(TAG, "Message: " + message); //["+918483014575","ritesh"]

            if (from.startsWith("/topics/")) {
                // message received from some topic.
            } else {
                // normal downstream message.
            }


            if (title.equalsIgnoreCase("Oyeok")) {

                badgeCount++;
                General.setBadgeCount(getApplicationContext(), AppConstants.BADGE_COUNT, badgeCount);
                ShortcutBadger.applyCount(this, badgeCount);
                if (General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                    message = "We have just assigned a broker to your request.";
                else
                    message = "We have just created a dealing room on your request.";
            }


            if (!message.equalsIgnoreCase("hello"))
                this.sendNotification(title, message, data);
            Log.d(TAG, "After sendNotification");

            // [END_EXCLUDE]
        } else {
           try {
                Log.i(TAG,"isuue gcm 9 uid "+General.getSharedPreferences(this, AppConstants.USER_ID)+" from "+data.getString("_from")+" tinmilli "+General.getSharedPreferences(this, AppConstants.TIME_STAMP_IN_MILLI));
                Log.i(TAG,"isuue gcm 91 uid "+General.getSharedPreferences(this, AppConstants.CHAT_OPEN_OK_ID)+" to "+data.getString("to"));

                Log.i(TAG,General.getSharedPreferences(this, AppConstants.CHAT_OPEN_OK_ID)+" marine    "+data.getString("to"));
                if ((!General.getSharedPreferences(this, AppConstants.USER_ID).equalsIgnoreCase(data.getString("_from"))) && !(General.getSharedPreferences(this, AppConstants.TIME_STAMP_IN_MILLI).equalsIgnoreCase(data.getString("_from"))))
                {
                    Log.i(TAG,"isuue gcm 1 uid "+General.getSharedPreferences(this, AppConstants.USER_ID)+" from "+data.getString("_from")+" tinmilli "+General.getSharedPreferences(this, AppConstants.TIME_STAMP_IN_MILLI));
                    if(!General.getSharedPreferences(this, AppConstants.CHAT_OPEN_OK_ID).equalsIgnoreCase(data.getString("to"))){
if(General.getSharedPreferences(this, AppConstants.TIME_STAMP_IN_MILLI).equalsIgnoreCase(data.getString("to")))
                        {

                            General.setBadgeCount(getApplicationContext(), AppConstants.SUPPORT_COUNT, supportCount+1);
                        }

                        Log.i(TAG,"isuue gcm 12 openchat "+General.getSharedPreferences(this, AppConstants.CHAT_OPEN_OK_ID)+" to "+data.getString("to"));
                    Log.i(TAG,"Inside gcm user id "+General.getSharedPreferences(this, AppConstants.USER_ID)+" from "+data.getString("_from")+" ts "+General.getSharedPreferences(this, AppConstants.TIME_STAMP_IN_MILLI+" open id "+General.getSharedPreferences(this, AppConstants.CHAT_OPEN_OK_ID)));
                    Log.i(TAG,"muted ok ids "+General.getMutedOKIds(this));
                    if(General.getMutedOKIds(this) != null) {
                        if (!General.getMutedOKIds(this).contains(data.getString("to"))) {
                            Log.i(TAG,"isuue gcm 14");
                            sendNotification("New Message Recieved", data.getString("to") + "@" + data.getString("message"), data);

                        }else{
                            Log.i(TAG,"isuue gcm 15");
                        }
                    }
                    else{
                        Log.i(TAG,"isuue gcm 16");
                        Log.i(TAG,"pushed on drawer");
                        sendNotification("New Message Recieved", data.getString("to") + "@" + data.getString("message"), data);
                    }

                    try {
                        Realm myRealm = General.realmconfig(this);
                        NotifCount notifcount = myRealm.where(NotifCount.class).equalTo(AppConstants.OK_ID, data.getString("to")).findFirst();
                        Log.i(TAG, "Caught in exception notif insiderr cached msgs is the chat id "+data.getString("to") + " notifcount " + notifcount);
                        if (notifcount == null) {
                            NotifCount notifCount = new NotifCount();
                            notifCount.setOk_id(data.getString("to"));
                            notifCount.setNotif_count(1);
                            myRealm.beginTransaction();
                            NotifCount notifCount1 = myRealm.copyToRealmOrUpdate(notifCount);
                            myRealm.commitTransaction();
                        } else {
                            myRealm.beginTransaction();
                            notifcount.setNotif_count(notifcount.getNotif_count() + 1);
                            myRealm.commitTransaction();
                        }



                        Intent intent = new Intent(AppConstants.BADGE_COUNT_BROADCAST);// (this is for dealslist) same used for badges in preok also
                       intent.putExtra("channel_name",data.getString("to"));
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                        try {
                            Realm myRealm1 = General.realmconfig(this);
                            RealmResults<NotifCount> results1 =
                                    myRealm1.where(NotifCount.class).findAll();
                            Log.i(TAG, "notif insiderr cached msgs is the " + results1);
                            for (NotifCount c : results1) {
                                Log.i(TAG, "notif insiderr cached msgs ");
                                Log.i(TAG, "notif insiderr cached msgs " + c.getOk_id());
                                Log.i(TAG, "notif insiderr cached msgs " + c.getNotif_count());
                            }

                            NotifCount notifcount1 = myRealm.where(NotifCount.class).equalTo(AppConstants.OK_ID, data.getString("to")).findFirst();
                            Log.i(TAG, "notif count is the " + notifcount1.getNotif_count());

                        } catch (Exception e) {
                            Log.i(TAG, "Caught in exception notif insiderr cached msgs is the 2 " + e);
                        }

                    } catch (Exception e) {
                        Log.i(TAG, "Caught in exception notif insiderr cached msgs is the 3 1 " + e);
                    }
                }
                }
                else{
                    Log.i(TAG,"isuue gcm 13 ");

                }
            }
            catch (Exception e) {
                Log.i(TAG, "Caught in exception notif insiderr cached msgs is the 3 2 " + e);
            }

        }
    }

    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String title,String message, Bundle data) {
        Log.i(TAG,"send notifications ");
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
        if(data.containsKey("to")) {

            if(data.getString("to").equalsIgnoreCase(General.getSharedPreferences(context,AppConstants.TIME_STAMP_IN_MILLI))){
                intent = new Intent(context, DealConversationActivity.class);
                intent.putExtra(AppConstants.OK_ID, data.getString("to"));
                //intent.putExtra(AppConstants.SPEC_CODE, halfDeals.getSpec_code());
                intent.putExtra("userRole", "client");
            }
            else {

                Realm myRealm = General.realmconfig(this);
                HalfDeals halfDeals = myRealm.where(HalfDeals.class).equalTo(AppConstants.OK_ID, data.getString("to")).findFirst();
                Log.i(TAG, "halfDeals is the " + halfDeals);


                if (!General.getSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER).equals("") &&
                        General.getSharedPreferences(context, AppConstants.ROLE_OF_USER).equals("broker")) {

                    if (halfDeals.equals("") || halfDeals.getSpec_code().isEmpty()) {
                        Log.i(TAG, "halfDeals is the 1 ");
                        intent = new Intent(context, ClientDealsListActivity.class);
                        intent.putExtra("defaul_deal_flag", "false");
                    } else {
                        Log.i(TAG, "halfDeals is the 2 ");
                        intent = new Intent(context, DealConversationActivity.class);
                        intent.putExtra(AppConstants.OK_ID, data.getString("to"));
                        intent.putExtra(AppConstants.SPEC_CODE, halfDeals.getSpec_code());
                        intent.putExtra("userRole", "broker");
                    }

                } else if (!General.getSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER).equals("") &&
                        General.getSharedPreferences(context, AppConstants.ROLE_OF_USER).equals("client")) {
                    if (halfDeals.equals("") || halfDeals.getSpec_code().isEmpty()) {
                        Log.i(TAG, "halfDeals is the 3 ");
                        intent = new Intent(context, ClientDealsListActivity.class);
                        intent.putExtra("defaul_deal_flag", "false");
                    } else {
                        Log.i(TAG, "halfDeals is the 4 ");
                        intent = new Intent(context, DealConversationActivity.class);
                        intent.putExtra(AppConstants.OK_ID, data.getString("to"));
                        intent.putExtra(AppConstants.SPEC_CODE, halfDeals.getSpec_code());
                        intent.putExtra("userRole", "client");
                    }
                } else if (General.getSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER).equals("")) {
                    intent = new Intent(context, DealConversationActivity.class);
                    intent.putExtra(AppConstants.OK_ID, data.getString("to"));
                    //intent.putExtra(AppConstants.SPEC_CODE, halfDeals.getSpec_code());
                    intent.putExtra("userRole", "client");
                }
            }
        }
        else if(data.containsKey("bicon")){
            if(!General.getSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER).equals("") &&
                    General.getSharedPreferences(context, AppConstants.ROLE_OF_USER).equals("broker")) {
                Log.i("TRACE", " toto 3");
                intent = new Intent(context, BrokerMainActivity.class);
                intent.putExtra("bicon", data.getString("bicon"));
                intent.putExtra("desc", data.getString("alert"));
                intent.putExtra("title", heading);
            }
            else {
                intent = new Intent(context, ClientMainActivity.class);
                intent.putExtra("bicon", data.getString("bicon"));
                intent.putExtra("desc", data.getString("alert"));
                intent.putExtra("title", heading);
            }
        }

        else if(title.equalsIgnoreCase("oyeok")) {
            intent = new Intent(context, ClientDealsListActivity.class);
        }
        else{
            if(!General.getSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER).equals("") &&
                    General.getSharedPreferences(context, AppConstants.ROLE_OF_USER).equals("broker"))
                intent = new Intent(context, BrokerMainActivity.class);
            else
                intent = new Intent(context, ClientMainActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int requestID = (int) System.currentTimeMillis();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification;
        if(data.containsKey("bicon")){
            Log.i(TAG,"a 1 ");
            Bitmap b = getBitmapFromURL(data.getString("bicon"));

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notification = new NotificationCompat.Builder(this)
                    .setContentTitle(heading)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(b))
                    //.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .build();

            General.setSharedPreferences(this,AppConstants.PROMO_IMAGE_URL,data.getString("bicon"));




        }else{
            Log.i(TAG,"tw 2 ");

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setGroup("ritz")
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(b))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .build();

        }



       /* NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setGroup("ritz")
                .setGroupSummary(true)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));*/



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
        notificationManager.notify(NOTIFICATION_ID++ /* ID of notification *//*, notificationBuilder.build()*/,notification);
        Log.d(TAG,"Notified");
    }
    private void storeDealTime(String okId){
        String dealTime;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        dealTime = General.getDealTime(this);

        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> dealTime1 = gson.fromJson(dealTime, type);
        if (dealTime1 == null) {
            dealTime1 = new HashMap<String, String>();

        }
        dealTime1.put(okId,String.valueOf(System.currentTimeMillis()));

        Gson g = new Gson();
        String hashMapString = g.toJson(dealTime1);
        General.saveDealTime(this, hashMapString);
        Log.i("dealtime","DealTime "+General.getDealTime(this));


        //store dealtimestamp in realm db

        try {
            Realm myRealm = General.realmconfig(this);
            DealTime dealtime = new DealTime();

            dealtime.setOk_id(okId);
            dealtime.setTimestamp(String.valueOf(System.currentTimeMillis()));

            myRealm.beginTransaction();
            myRealm.copyToRealmOrUpdate(dealtime);
            myRealm.commitTransaction();

        }
        catch(Exception e){
            Log.i(TAG,"caught in exception deleting default droom timestamp "+e);
        }

        try{
            Realm mmyRealm = General.realmconfig(this);
            RealmResults<DealTime> results1 =
                    mmyRealm.where(DealTime.class).findAll();
            Log.i(TAG, "insider timestamp " +results1);
            for (DealTime c : results1) {
                Log.i(TAG, "insider timestamp " + c.getOk_id());
                Log.i(TAG, "insider timestamp " + c.getTimestamp());
            }
        }
        catch(Exception e){}


    }
    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            saveImageLocally("promo",myBitmap);
            return myBitmap;

        } catch (Exception e) {
            // Log exception
            return null;
        }
    }

    private void saveImageLocally(String imageName,Bitmap result){
        try {
            if (Environment.getExternalStorageState() == null) {
                //create new file directory object
                File directory = new File(Environment.getDataDirectory()
                        + "/oyeok/");

                // if no directory exists, create new directory
                if (!directory.exists()) {
                    directory.mkdir();
                }

                OutputStream stream = new FileOutputStream(Environment.getDataDirectory() + "/oyeok/"+imageName);
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