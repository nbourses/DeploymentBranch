package com.nbourses.oyeok.GoogleCloudMessaging;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by ritesh on 13/05/16.
 */
public class GcmIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 3691;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        Log.i("Pubnub push","inside GSMIntentService");

        if (!extras.isEmpty() && GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

     //     sendNotification("Received: " + extras.toString());

        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }



    // remember changes after deleting file sushil
    /*private void sendNotification(String msg) {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Log.i("Pubnub push","inside GSMIntentService sendNotification");

       /* PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("PubNub GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }*/




    public boolean check(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if ("com.nbourses.oyeok.GoogleCloudMessaging.GcmIntentService"
                    .equals(service.service.getClassName()))
            {
                Log.i("notifications","========================");
                return true;
            }
        }
        return false;
    }


}
