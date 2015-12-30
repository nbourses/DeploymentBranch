package com.nbourses.oyeok;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

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

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Branch.getAutoInstance(this);

    }
}
