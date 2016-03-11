package com.nbourses.oyeok;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;
import com.crashlytics.android.Crashlytics;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;

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

        //initialize active android library to store data in db
        ActiveAndroid.initialize(this);
    }
}
