package com.nbourses.oyeok.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.fragments.AppSetting;
import com.nbourses.oyeok.fragments.ShareOwnersNo;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sdsmdg.tastytoast.TastyToast;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

public class BrokerMainPageActivity extends AppCompatActivity {


    RadioGroup radioGroup1;
    RadioButton deals;
    FragmentDrawer drawerFragment;
    WebView webView;
    boolean setting=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_main_page);
        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        deals = (RadioButton)findViewById(R.id.deals);
      //  deals.setButtonDrawable(R.drawable.ic_deals_unclicked);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Intent in;
                //int center = (width - rk1.getWidth())/2;
                Log.i("matching", "matching inside1 bro" + checkedId);
                switch (checkedId)
                {
                    case R.id.matching:
                        Log.i("matching", "matching inside1 matching" + checkedId);
                        in=new Intent(getBaseContext(),BrokerMainActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.watchList:
                        Log.i("matching", "matching inside1 watchlist" + checkedId);

                        in = new Intent(getBaseContext(), MyPortfolioActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);

                        break;
                    case R.id.rates:
                        Log.i("matching", "matching inside1 rate" + checkedId);

                        in = new Intent(getBaseContext(),BrokerMap.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.listing:
                        Log.i("matching", "matching inside1 listing" + checkedId);
                        in = new Intent(getBaseContext(), BrokerListingActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.deals:
                        Log.i("matching", "matching inside1 deals" + checkedId);
                        in = new Intent(getBaseContext(), ClientDealsListActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    default:
                        break;
                }
            }
        });








    }




/*

    @Override
    public void onDrawerItemSelected(View view, int position, String itemTitle) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        if (itemTitle.equals(getString(R.string.useAsClient))) {
            // dbHelper = new DBHelper(getBaseContext());
            //dbHelper.save(DatabaseConstants.userRole,"client");
            General.setSharedPreferences(this,AppConstants.ROLE_OF_USER,"client");
            Intent openDashboardActivity =  new Intent(this, ClientMainActivity.class);
            openDashboardActivity.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(openDashboardActivity);
        }
        else if (itemTitle.equals(getString(R.string.profile))) {
            Intent openProfileActivity =  new Intent(this, ProfileActivity.class);
            startActivity(openProfileActivity);
        }
        else if (itemTitle.equals(getString(R.string.brokerOk))) {
            //don't do anything
        }
        else if (itemTitle.equals(getString(R.string.shareApp))) {
            if(General.getSharedPreferences(this,AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")){
                SignUpFragment signUpFragment = new SignUpFragment();
                // signUpFragment.getView().bringToFront();
                Bundle bundle = new Bundle();
                bundle.putStringArray("Chat", null);
                bundle.putString("lastFragment", "brokerDrawer");
                loadFragment(signUpFragment, bundle, R.id.container_sign, "");
            }
            else
                shareReferralLink();
        }
     */
/*   else if (itemTitle.equals(getString(R.string.supportChat))) {
            //TODO: integration is pending
        } *//*


        else if (itemTitle.equals(getString(R.string.notifications))) {
            AppConstants.BROKER_DEAL_FLAG = true;
            Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
            intent.putExtra("userRole", "client");
            intent.putExtra(AppConstants.OK_ID, AppConstants.SUPPORT_CHANNEL_NAME);
            startActivity(intent);
        }
        else if (itemTitle.equals(getString(R.string.likeOnFb))) {
            // setContentView(R.layout.browser);
            webView = (WebView) findViewById(R.id.webView);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("http://www.facebook.com/hioyeok");



        }
        else if (itemTitle.equals(getString(R.string.aboutUs))) {
            //setContentView(R.layout.browser);
            webView = (WebView) findViewById(R.id.webView);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("http://www.hioyeok.com/blog");


        }
        else if (itemTitle.equals(getString(R.string.settings))) {
            AppSetting appSetting=new AppSetting();
            setting=true;
            loadFragment(appSetting,null,R.id.container_sign,"");


        }
        else if (itemTitle.equals(getString(R.string.RegisterSignIn))) {
            SignUpFragment signUpFragment = new SignUpFragment();
            // signUpFragment.getView().bringToFrFont();
            Bundle bundle = new Bundle();
            bundle.putStringArray("Chat", null);
            bundle.putString("lastFragment", "brokerDrawer");
            loadFragmentAnimated(signUpFragment, bundle, R.id.container_sign, "");
        }
        else if(itemTitle.equals(getString(R.string.shareNo))){
            ShareOwnersNo shareOwnersNo = new ShareOwnersNo();

            loadFragment(shareOwnersNo, null, R.id.container_sign, "");
           // Owner_detail=true;
        }else if(itemTitle.equals(getString(R.string.Listing))){
            Log.i("myWatchList","itemTitle 1 "+itemTitle + R.string.Listing);
            Intent intent =new Intent( this,MyPortfolioActivity.class );
            startActivity(intent);
            */
/*MainScreenPropertyListing my_portfolio = new MainScreenPropertyListing();
            loadFragment(my_portfolio, null, R.id.container_Signup, "");
            Myportfolio=true;*//*


        }



//       if (fragment != null && !itemTitle.equals(getString(R.string.settings))) {
//
//            loadFragment(fragment, null, R.id.container_map, title);
//           Log.i("ONBACKPRESSED","broker main activity "+setting);
//
//           Log.i("ONBACKPRESSED","broker main activity "+setting);
//        }

    }
*/







   /* @Override
    public void onBackPressed() {
     super.onBackPressed();

      if(AppConstants.SIGNUP_FLAG){
            if(AppConstants.REGISTERING_FLAG){}else{
                //getSupportFragmentManager().popBackStackImmediate();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_sign)).commit();
                AppConstants.SIGNUP_FLAG=false;
              }
        }else
        if(webView != null){
            if (webView.canGoBack()) {
                webView.goBack();
            }
            else {
                webView = null;
                Intent inten = new Intent(this, BrokerMainActivity.class);
                inten.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inten);
                finish();

            }
        }
    else if(setting==true){
            Log.i("BACK PRESSED"," =================== setting"+setting);
            if(SharedPrefs.getString(this, SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true") || SharedPrefs.getString(this, SharedPrefs.CHECK_BEACON).equalsIgnoreCase("true")){
                int count = getFragmentManager().getBackStackEntryCount();
                for(int i = 0; i < count; ++i) {
                    getFragmentManager().popBackStackImmediate();
                }
                Intent inten = new Intent(this, BrokerMainActivity.class);
                inten.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inten);
                finish();
                Log.i("SIGNUP_FLAG", "SIGNUP_FLAG=========  loadFragment setting client4 " + getFragmentManager().getBackStackEntryCount());
                setting = false;

            }else {
                Log.i("BACK","FRAGMENT COUNT "+getSupportFragmentManager().getBackStackEntryCount());
                getSupportFragmentManager().popBackStackImmediate();
                Log.i("SIGNUP_FLAG", "SIGNUP_FLAG=========  loadFragment setting client4 " + getFragmentManager().getBackStackEntryCount());
                setting = false;

            }

        }
      *//* else if(!General.getSharedPreferences(this,AppConstants.IS_LOGGED_IN_USER).equals("")) {
            Log.i("SIGNUP_FLAG", " closing app =================== 3" + getFragmentManager().getBackStackEntryCount());
            if (backpress < 1) {
                backpress = (backpress + 1);
                TastyToast.makeText(this, "Press Back again to Exit!", TastyToast.LENGTH_LONG, TastyToast.INFO);
            } else if (backpress >= 1) {
                backpress = 0;
                this.finish();
            }
        }*//*




    }*/

    /*private void loadFragment(Fragment fragment, Bundle args, int containerId, String title) {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void loadFragmentAnimated(Fragment fragment, Bundle args, int containerId, String title)
    {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }*/







}
