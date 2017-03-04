package com.nbourses.oyeok;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.onesignal.OneSignal;
//import com.squareup.leakcanary.LeakCanary;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;

/**
 * Created by prathyush on 16/12/15.
 */
public class MyApplication extends MultiDexApplication {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "CE00enRZ4tIG82OJp6vKib8YS";
    private static final String TWITTER_SECRET = "5AMXDHAXG0luBuuHzSrDLD0AvwP8GzF06klXFgcwnzAVurXUoS";

    private AuthCallback authCallback;
    private Tracker mTracker;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);

        OneSignal.startInit(this).init();
       // new Instabug.Builder(this, "cc39b2bf5c9fffb413e7fd81ce5e9f2e").setInvocationEvent(IBGInvocationEvent.IBGInvocationEventShake) .build();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new TwitterCore(authConfig), new Digits());

        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // Do something with the session
                System.out.println("isValidUser " + session.isValidUser());
                System.out.println("phoneNumber " + phoneNumber);
                System.out.println("getPhoneNumber " + session.getPhoneNumber());
            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
                exception.printStackTrace();
            }
        };


        Branch.getAutoInstance(this);
       // Intercom.initialize(this, "android_sdk-1848409f6329896229237973e1baf97af02b56df", "zs3wjecf");
        //initialize active android library to store data in db


       // ActiveAndroid.initialize(this);

    }
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        mTracker.enableAdvertisingIdCollection(true);
        return mTracker;
    }


    public AuthCallback getAuthCallback(){
        return authCallback;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

}
