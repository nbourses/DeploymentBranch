package com.nbourses.oyeok.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.User.Profile;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
try {
    if (getIntent().getExtras() != null) {
        if (getIntent().getExtras().containsKey("msg")) {
            Log.i("TAG", "dattu tappewala ");
            SnackbarManager.show(
                    Snackbar.with(this)
                            .text("You must update profile to proceed.")
                            .position(Snackbar.SnackbarPosition.TOP)
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
        }
    }
}catch(Exception e){}
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        Fragment fragment = new Profile();
        loadFragment(fragment, null, R.id.container_map, "");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * load fragment
     * @param fragment
     * @param args
     * @param title
     */
    private void loadFragment(Fragment fragment, Bundle args, int containerId, String title)
    {
        //set arguments
        fragment.setArguments(args);

        //load fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
