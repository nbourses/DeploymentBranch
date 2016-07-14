package com.nbourses.oyeok.RPOT.OkBroker.UI.SlidingTabLayout;

import android.support.v4.app.Fragment;

/**
 * Created by prathyush on 10/12/15.
 */
public class PagerItem {


    private String mTitle;
    private Fragment mFragment;


    public PagerItem(String mTitle, Fragment mFragment) {
        this.mTitle = mTitle;
        this.mFragment = mFragment;
    }
    public String getTitle() {
        return mTitle;
    }
    public Fragment getFragment() {
        return mFragment;
    }
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }
}
