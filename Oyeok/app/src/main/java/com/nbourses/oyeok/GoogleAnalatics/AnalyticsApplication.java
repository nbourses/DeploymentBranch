//package com.nbourses.oyeok.GoogleAnalatics;
//
//
///**
// * Created by sushil on 24/12/16.
// */
//
//import android.app.Application;
//
//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;
//
//public class AnalyticsApplication extends Application {
//
//    private Tracker mTracker;
//
//    synchronized public Tracker getDefaultTracker() {
//        if (mTracker == null) {
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//            mTracker = analytics.newTracker(R.xml.global_tracker);
//        }
//        return mTracker;
//    }
//}