package com.nbourses.oyeok.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;

import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class SplashScreenActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        context = this;
        if( SharedPrefs.getString(this,SharedPrefs.PERMISSION).equalsIgnoreCase(""))
        SharedPrefs.save(this,SharedPrefs.PERMISSION,"true");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;

                Log.i("splash","is logged in yo man " +General.getSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER));
                Log.i("splash","is logged in yo man 3 " +General.getSharedPreferences(context, AppConstants.ROLE_OF_USER));
                if (!General.getSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("") &&
                        General.getSharedPreferences(context, AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
  
                    intent = new Intent(context, BrokerMainActivity.class);
                }
                else {
                    Log.i("splash","is logged in yo man 2 ");
                    intent = new Intent(context, ClientMainActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

        }, 2000);
    }

    @Override
    public void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

        branch.initSession(new Branch.BranchReferralInitListener(){
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                } else {
                    Log.i("MyApp", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

}
