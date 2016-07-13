package com.nbourses.oyeok.SignUp;

import android.app.Application;
import android.util.Log;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


public class AuthenticateDigits extends Application {

    private AuthCallback authCallback;
  private static final String TWITTER_KEY ="CE00enRZ4tIG82OJp6vKib8YS";
  private static final String TWITTER_SECRET = "5AMXDHAXG0luBuuHzSrDLD0AvwP8GzF06klXFgcwnzAVurXUoS";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Ritz =", "Auth");
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY,TWITTER_SECRET);
        Fabric.with(this,new Digits());
        authCallback = new AuthCallback() {

            @Override
            public void success(DigitsSession session, String phoneNumber) {
//                Digits.authenticate(authCallback, "+918483014575");
//                Log.d("successful ============");
                System.out.println("phoneNumber " + phoneNumber);
            }

            @Override
            public void failure(DigitsException error) {

            }
        };
    }
    public AuthCallback getAuthCallback(){
        return authCallback;
    }

}
