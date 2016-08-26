package com.nbourses.oyeok.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.adapters.IntroAdapter;
import com.nbourses.oyeok.animations.IntroPageTransformer;

/**
 * Created by ritesh on 22/08/16.
 */
public class IntroActivity extends ActionBarActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro_layout);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));

        // Set a PageTransformer
        mViewPager.setPageTransformer(false, new IntroPageTransformer());
    }

}
