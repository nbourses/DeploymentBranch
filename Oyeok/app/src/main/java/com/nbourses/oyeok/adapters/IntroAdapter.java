package com.nbourses.oyeok.adapters;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nbourses.oyeok.fragments.IntroFragment;

/**
 * Created by ritesh on 22/08/16.
 */
public class IntroAdapter extends FragmentPagerAdapter {

    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return IntroFragment.newInstance(Color.parseColor("#20BF9F"), position); // blue
            case 1:
                return IntroFragment.newInstance(Color.parseColor("#20BF9F"), position); // orange
            case 2:
                return IntroFragment.newInstance(Color.parseColor("#20BF9F"), position);
            case 3:
                return IntroFragment.newInstance(Color.parseColor("#20BF9F"), position);
            default:
                return IntroFragment.newInstance(Color.parseColor("#20BF9F"), position); // green
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

}
