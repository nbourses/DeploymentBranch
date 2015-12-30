package com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom;

import android.graphics.drawable.StateListDrawable;

/**
 * Created by prathyush on 03/12/15.
 */
public interface CustomPhasedAdapter {

    int getCount();

    StateListDrawable getItem(int position);

    String getBrokerDetails(int position);

    String getTimeDetails(int position);

}
