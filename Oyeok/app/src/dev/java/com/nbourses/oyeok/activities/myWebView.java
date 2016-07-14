package com.nbourses.oyeok.activities;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by rohit on 14/04/16.
 */



public class myWebView extends WebViewClient {

    private Activity activity = null;

    public myWebView(Activity activity) {
        this.activity = activity;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
    view.loadUrl(url);
    return true;
 }
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        //Call another activity
    }
}



