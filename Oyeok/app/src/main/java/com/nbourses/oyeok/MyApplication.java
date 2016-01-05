package com.nbourses.oyeok;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.amplitude.api.Amplitude;
import com.crashlytics.android.Crashlytics;
import com.nbourses.oyeok.GoogleCloudMessaging.RegistrationIntentService;

import io.fabric.sdk.android.Fabric;
import org.json.JSONException;
import org.json.JSONObject;

import io.branch.referral.Branch;

/**
 * Created by prathyush on 16/12/15.
 */
public class MyApplication extends MultiDexApplication {

    private static final String AMPLITUDE_KEY = "28931796b17fc24d41e081bccf6dd344";
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Branch.getAutoInstance(this);
        Amplitude.getInstance().initialize(this, AMPLITUDE_KEY)
                .enableForegroundTracking(this);

    }
}
