package com.nbourses.oyeok.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;

/**
 * Created by rohit on 11/02/16.
 */
public class DeviceRegisterService extends Service {

    private static String TAG = DeviceRegisterService.class.getName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
                    String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    SharedPrefs.save(getApplicationContext(), SharedPrefs.MY_GCM_ID, token);
                    General.setSharedPreferences(getApplicationContext(), AppConstants.GCM_ID,token);

                    Log.i(TAG, "GCM Registration Token: " + token);

                    stopSelf();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return START_NOT_STICKY;
    }
}
