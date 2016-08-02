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

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

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
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();
        branch.initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error == null && branchUniversalObject != null) {
                    // This code will execute when your app is opened from a Branch deep link, which
                    // means that you can route to a custom activity depending on what they clicked.
                    // In this example, we'll just print out the data from the link that was clicked.

                    Log.i("BranchTestBed", "referring Branch Universal Object: " + branchUniversalObject.toString());

                    // check if the item is contained in the metadata
                    if (branchUniversalObject.getMetadata().containsKey("item_id")) {
                        Intent i = new Intent(getApplicationContext(), ClientMainActivity.class);
                        i.putExtra("picture_id", branchUniversalObject.getMetadata().get("item_id"));
                        startActivity(i);
                    }
                }
            }
        }, this.getIntent().getData(), this);
    }

}
