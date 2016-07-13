package com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by prathyush on 03/12/15.
 */
public class SimpleCustomPhasedAdapter implements CustomPhasedAdapter {


    protected StateListDrawable[] mItems;
    protected String [] mBrokers;
    protected String [] time;

    public SimpleCustomPhasedAdapter(Resources resources, int[] items, String [] brokers, String [] time) {
        int size = items.length;
        mItems = new StateListDrawable[size];
        mBrokers = new String[size];
        this.time = new String[size];
        Drawable drawable;
        for (int i = 0; i < size; i++) {
            drawable = resources.getDrawable(items[i]);
            if (drawable instanceof StateListDrawable) {
                mItems[i] = (StateListDrawable) drawable;

            } else {
                mItems[i] = new StateListDrawable();
                mItems[i].addState(new int[] {}, drawable);
            }
            mBrokers[i] = brokers[i];
            this.time[i] = time[i];
        }
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public StateListDrawable getItem(int position) {
        return mItems[position];
    }

    @Override
    public String getTimeDetails(int position) {
        return mBrokers[position];
    }

    @Override
    public String getBrokerDetails(int position) {
        return time[position];
    }
}
