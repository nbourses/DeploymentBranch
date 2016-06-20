package com.nbourses.oyeok.GoogleCloudMessaging;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by ritesh on 13/05/16.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Pubnub push","GcmBroadcastReceiver");
        Log.i("notifications","GcmBroadcastReceiver ==========================");

        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));

        ComponentName comp1 = new ComponentName(context.getPackageName(),
                MyGcmListenerService.class.getName());
        startWakefulService(context, (intent.setComponent(comp1)));


        setResultCode(Activity.RESULT_OK);
    }
}
